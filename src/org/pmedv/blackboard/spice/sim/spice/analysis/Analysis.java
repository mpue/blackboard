/*
 * Analysis.java
 *
 * Created on April 1, 2006, 3:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.output.Count;
import org.pmedv.blackboard.spice.sim.output.Statistics;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.StateTable;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.DOMAIN;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcInstance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcInstance;
import org.pmedv.blackboard.spice.sim.spice.node.SpiceNode;
import org.pmedv.blackboard.spice.sim.spice.node.VoltageNode;

import javolution.lang.MathLib;
import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.util.FastTable;
import javolution.util.StandardLog;
import ktb.math.matrix.MatrixException;
import ktb.math.matrix.SingularException;

/**
 *
 * @author Kristopher T. Beck
 */
public abstract class Analysis {

    protected String name;
    protected String desc;
    protected FastList<AnalysisEventListener> listeners = new FastList<AnalysisEventListener>();
    protected FastList<NonConvergenceListener> nclisteners = new FastList<NonConvergenceListener>();
    protected Circuit ckt;
    protected WorkEnv wrk;
    protected EnvVars env;
    protected StateTable stateTable;
    protected Temporal tmprl;
    protected Mode mode;
    public static final String ITERATIONS = "ITERATIONS";
    protected Count iters;
    //Count defaultIters = new Count();
    protected double[] state0;
    boolean shouldReorder;
    boolean reordered;
    boolean uninitialized;
    boolean acShouldReordered;
    boolean acReordered;
    boolean acUninitialized;
    boolean didPreorder;
    boolean pzShouldReorder;
    protected boolean lastIterCall;
    protected boolean pause;
    protected DOMAIN domain = DOMAIN.NONE;
    protected boolean doIC;
    private static final EnumSet<MODE> jctMode = EnumSet.of(MODE.DCOP, MODE.INIT_JCT);
    private static final EnumSet<MODE> floatMode = EnumSet.of(MODE.DCOP, MODE.INIT_FLOAT);
    public static final EnumSet<MODE> initSmSig = EnumSet.of(MODE.DCOP, MODE.INIT_SMSIG);

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        env = ckt.getEnv();
        wrk = ckt.wrk;
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        iters = Statistics.addCount(ITERATIONS);
        mode = new Mode(null);
        return true;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public Circuit getCircuit() {
        return ckt;
    }

    public WorkEnv getWrk() {
        return wrk;
    }

    public void addAnalysisEventListener(AnalysisEventListener listener) {
        listeners.add(listener);
    }

    public void addNonConverganceEventListener(NonConvergenceListener listener) {
        nclisteners.add(listener);
    }

    public void setDomain(DOMAIN domain) {
        this.domain = domain;
    }

    public DOMAIN getDomain() {
        return domain;
    }

    public void beginOutput() {
        AnalysisEvent evt = new AnalysisEvent(this);
        fireBeginOutputEvent(evt);
    }

    public void beginOutput(String refName) {
        AnalysisEvent evt = new AnalysisEvent(this);
        evt.refName = refName;
        fireBeginOutputEvent(evt);
    }

    public void beginOutput(String name, List<String> varIds) {
        MultiSetAnalysisEvent evt = new MultiSetAnalysisEvent(this);
        //TODO transfunc needs work
        evt.setDataIds(varIds);
        evt.refName = name;
        fireBeginOutputEvent(evt);
    }

    public void restartOutput() {
        AnalysisEvent evt = new AnalysisEvent(this);
        fireRestartOutputEvent(evt);
    }

    public void endOutput() {
        AnalysisEvent evt = new AnalysisEvent(this);
        fireEndOutputEvent(evt);
    }

    public abstract boolean analyze(boolean restart);

    public boolean doDCOperations() {
        return doOperations(jctMode, floatMode, env.getDcMaxIter());
    }

    public boolean doACOperations(int iterLim) {
        return doOperations(EnumSet.of(MODE.AC), null, iterLim);
    }

    public boolean doOperations(EnumSet<MODE> first, EnumSet<MODE> cont, int iterlim) {
        boolean converged = false;
        mode.setModes(first);
        if (!env.isNoOpIter()) {
            if (env.getNumGMinSteps() <= 0 && env.getNumSrcSteps() <= 0) {
                lastIterCall = true;
            } else {
                lastIterCall = false;
            }
            converged = iterate(iterlim);
        }
        if (!converged) {
            if (env.getNumGMinSteps() >= 1) {
                if (env.getNumGMinSteps() == 1) {
                    if (dynamicGMin(first, cont, iterlim)) {
                        return true;
                    }
                } else if (spice3GMin(first, cont, iterlim)) {
                    return true;
                }
            }
            if (env.getNumSrcSteps() >= 1) {
                if (env.getNumSrcSteps() == 1) {
                    if (gillespieSrc(first, cont, iterlim)) {
                        lastIterCall = false;
                        return true;
                    }
                } else if (spice3Src(first, cont, iterlim)) {
                    lastIterCall = false;
                    return true;
                }
            }
        }
        return converged;
    }

    public boolean dynamicGMin(EnumSet<MODE> first, EnumSet<MODE> cont, int iterlim) {
        boolean converged = false;
        double[] oldRhsOld = new double[wrk.getSize()];
        mode.setModes(first);
        StandardLog.info("Starting dynamic GMin stepping");
        double oldState0[] = null;
        wrk.clearRhsOld();
        stateTable.clearColumn(0);
        double factor = env.getGMinFactor();
        double oldGMin = 1e-2;
        env.setDiagGMin(oldGMin / factor);
        double gTarget = MathLib.max(env.getGMin(), env.getGShunt());
        boolean success = false;
        boolean failed = false;
        while (!success && !failed) {
            StandardLog.info("Trying GMin = " + env.getDiagGMin());
            int _iters = iters.getCount();
            converged = iterate(env.getDcTrcvMaxIter());
            _iters = iters.getCount() - _iters;
            if (converged) {
                mode.setModes(cont);
                StandardLog.info("One successful GMin step");
                if (env.getDiagGMin() <= gTarget) {
                    success = true;
                } else {
                    for (int i = 0; i < wrk.getSize(); i++) {
                        oldRhsOld[i] = wrk.getRhsOldRealAt(i);
                    }
                    oldState0 = Arrays.copyOf(stateTable.getColumnAt(0), stateTable.getSize());
                    if (_iters <= (env.getDcTrcvMaxIter() / 4)) {
                        factor *= MathLib.sqrt(factor);
                        if (factor > env.getGMinFactor()) {
                            factor = env.getGMinFactor();
                        }
                    }
                    if (_iters > (3 * env.getDcTrcvMaxIter() / 4)) {
                        factor = MathLib.sqrt(factor);
                    }
                    oldGMin = env.getDiagGMin();
                    if (env.getDiagGMin() < (factor * gTarget)) {
                        factor = env.getDiagGMin() / gTarget;
                        env.setDiagGMin(gTarget);
                    } else {
                        env.setDiagGMin(env.getDiagGMin() / factor);
                    }
                }
            } else {
                if (factor < 1.00005) {
                    failed = true;
                    StandardLog.warning("Last GMin step failed");
                } else {
                    factor = MathLib.sqrt(MathLib.sqrt(factor));
                    env.setDiagGMin(oldGMin / factor);
                    for (int i = 0; i < wrk.getSize(); i++) {
                        wrk.setRhsOldRealAt(i, oldRhsOld[i]);
                    }
                    if (oldState0 != null) {
                        stateTable.setColumnAt(0, oldState0);
                    }
                }
            }
        }
        oldRhsOld = null;
        oldState0 = null;
        env.setDiagGMin(env.getGShunt());
        if (env.getNumSrcSteps() <= 0) {
            lastIterCall = true;
        } else {
            lastIterCall = false;
        }
        if (!iterate(iterlim)) {
            StandardLog.warning("Dynamic GMin stepping failed");
            return false;
        }
        StandardLog.info("Dynamic GMin stepping completed");
        lastIterCall = false;
        return true;
    }

    public boolean spice3GMin(EnumSet<MODE> first, EnumSet<MODE> cont, int iterlim) {
        mode.setModes(first);
        double diagGMin;
        StandardLog.info("Starting GMin stepping");
        if (env.getGShunt() == 0) {
            diagGMin = env.getGMin();
        } else {
            diagGMin = env.getGShunt();
        }
        for (int i = 0; i < env.getNumGMinSteps(); i++) {
            diagGMin *= env.getGMinFactor();
        }
        env.setDiagGMin(diagGMin);
        for (int i = 0; i <= env.getNumGMinSteps(); i++) {
            StandardLog.info(String.format("Trying GMin = %12.4E ",
                    env.getDiagGMin()));
            if (!iterate(env.getDcTrcvMaxIter())) {
                env.setDiagGMin(env.getGShunt());
                StandardLog.warning("GMin step failed");
                break;
            }
            env.setDiagGMin(env.getDiagGMin() / env.getGMinFactor());
            StandardLog.info("One successful GMin step");
            mode.setModes(cont);
        }
        env.setDiagGMin(env.getGShunt());
        if (env.getNumSrcSteps() <= 0) {
            lastIterCall = true;
        } else {
            lastIterCall = false;
        }

        if (iterate(iterlim)) {
            StandardLog.info("GMin stepping completed");
            lastIterCall = false;
            return true;
        }
        StandardLog.warning("GMin stepping failed");
        return false;

    }

    public boolean gillespieSrc(EnumSet<MODE> first, EnumSet<MODE> cont, int iterlim) {
        //int iters;
        mode.setModes(first);
        StandardLog.info("Starting source stepping");
        wrk.setSrcFact(0);
        double raise = 0.001;
        double convFact = 0;
        boolean converged = false;
        double[] oldRhsOld = new double[wrk.getSize()];
        double[] oldState0 = null;
        wrk.clearRhsOld();
        stateTable.clearColumn(0);
        StandardLog.info(String.format("Supplies reduced to %8.4f ",
                wrk.getSrcFact() * 100));
        if (!iterate(env.getDcTrcvMaxIter())) {
            if (env.getGShunt() <= 0) {
                env.setDiagGMin(env.getGMin());
            } else {
                env.setDiagGMin(env.getGShunt());
            }
            env.setDiagGMin(env.getDiagGMin() * 10000000000.0);
            for (int i = 0; i <= 10; i++) {
                StandardLog.info(String.format("Trying env.getGMin() ="
                        + " %12.4E ", env.getDiagGMin()));
                lastIterCall = true;
                if (!iterate(env.getDcTrcvMaxIter())) {
                    env.setDiagGMin(env.getGShunt());
                    StandardLog.warning("GMin step failed");
                    lastIterCall = false;
                    converged = false;
                    break;
                }
                env.setDiagGMin(env.getDiagGMin() / 10);
                mode.setModes(cont);
                StandardLog.info("One successful GMin step");
                converged = true;
            }
            env.setDiagGMin(env.getGShunt());
        }
        if (converged) {
            for (int i = 0; i < wrk.getSize(); i++) {
                oldRhsOld[i] = wrk.getRhsOldRealAt(i);
            }
            oldState0 = Arrays.copyOf(stateTable.getColumnAt(0), stateTable.getSize());
            StandardLog.info("One successful source step");
            wrk.setSrcFact(convFact + raise);
            do {
                StandardLog.info("Supplies reduced to %8.4f "
                        + wrk.getSrcFact() * 100);
                int _iters = iters.getCount();
                lastIterCall = true;
                converged = iterate(env.getDcTrcvMaxIter());
                _iters = iters.getCount() - _iters;
                mode.setModes(cont);
                if (converged) {
                    convFact = wrk.getSrcFact();
                    for (int i = 0; i < ckt.getNodeCount(); i++) {
                        oldRhsOld[i] = wrk.getRhsOldRealAt(i);
                    }
                    for (int i = 0; i < stateTable.getSize(); i++) {
                        oldState0[i] = stateTable.getStateAt(0, i);
                    }
                    StandardLog.info("One successful source step");
                    wrk.setSrcFact(convFact + raise);
                    if (iters.getCount() <= (env.getDcTrcvMaxIter() / 4)) {
                        raise = raise * 1.5;
                    }
                    if (iters.getCount() > (3 * env.getDcTrcvMaxIter() / 4)) {
                        raise = raise * 0.5;
                    }
                } else {
                    if ((wrk.getSrcFact() - convFact) < 1e-8) {
                        break;
                    }
                    raise = raise / 10;
                    if (raise > 0.01) {
                        raise = 0.01;
                    }
                    wrk.setSrcFact(convFact);
                    for (int i = 0; i < wrk.getSize(); i++) {
                        wrk.setRhsOldRealAt(i, oldRhsOld[i]);
                    }
                    for (int i = 0; i < stateTable.getSize(); i++) {
                        stateTable.setStateAt(i, 0, oldState0[i]);
                    }
                }
                if (wrk.getSrcFact() > 1) {
                    wrk.setSrcFact(1);
                }
            } while ((raise >= 1e-7) && (convFact < 1));
        }
        wrk.setSrcFact(1);
        if (convFact != 1) {
            wrk.setSrcFact(1);
            StandardLog.warning("Source stepping failed");
        } else {
            StandardLog.info("Source stepping completed");
            return true;
        }
        return false;
    }

    public boolean spice3Src(EnumSet<MODE> first, EnumSet<MODE> cont, int iterlim) {
        mode.setModes(first);
        StandardLog.info("Starting source stepping");
        for (int i = 0; i <= env.getNumSrcSteps(); i++) {
            wrk.setSrcFact(((double) i) / ((double) env.getNumSrcSteps()));
            if (!iterate(env.getDcTrcvMaxIter())) {
                wrk.setSrcFact(1);
                StandardLog.warning("Source stepping failed");
                lastIterCall = false;
                return false;
            }
            lastIterCall = true;
            StandardLog.info("One successful source step");
            mode.setModes(cont);
        }
        StandardLog.info("Source stepping completed");
        wrk.setSrcFact(1);
        return true;
    }

    public boolean convTest() {
        SpiceNode node;
        double tol;
        FastTable<SpiceNode> nodes = ckt.getNodes();
        for (int i = 1; i < nodes.size(); i++) {
            node = nodes.get(i);
            double cur = wrk.getRhsRealAt(i);
            double old = wrk.getRhsOldRealAt(i);
            if (node instanceof VoltageNode) {
                tol = env.getRelTol() * MathLib.max(MathLib.abs(old),
                        MathLib.abs(cur)) + env.getVoltTol();
            } else {
                tol = env.getRelTol() * MathLib.max(MathLib.abs(old),
                        MathLib.abs(cur)) + env.getAbsTol();
            }
            if (MathLib.abs(cur - old) > tol) {
                trouble(node, null, "Non-convergence at node " + node.getID());
                return false;
            }
        }
        return ckt.convTest(mode);
    }

    public boolean iterate(int maxIter) {
        int iterNo = iters.getCount();
        int oldIters = iters.getCount();
        boolean ipass = false;
        if (maxIter < 100) {
            maxIter = 100;
        }
        double[] oldState0 = null;
        if (mode.contains(MODE.TRANOP) && mode.isUseIC()) {
            wrk.advanceRhs();
            if (!ckt.load(mode)) {
                StandardLog.warning("Load returned error");
                return false;
            }
        }
        if (uninitialized) {
            if (!reInit()) {
                StandardLog.warning("Re-init returned error");
                return false;
            }
            uninitialized = false;
        }
        while (true) {
            ckt.setNonConverged(false);
            if (true) {//!mode.contains(MODE.INIT_PRED)) {
//                System.out.println(wrk.work);
                if (ckt.load(mode)) {
//                    System.out.println(wrk.getMatrix());
//                    System.out.println(wrk.work);
                    iterNo = iters.increment() - oldIters;
                } else {
                    StandardLog.warning("Load returned error");
                    oldState0 = null;
                    return false;
                }
                if (!didPreorder) {
                    if (!wrk.preOrder()) {
                        oldState0 = null;
                        return false;
                    }
                    didPreorder = true;
                }

                if (mode.contains(MODE.INIT_JCT) || (mode.contains(MODE.INIT_TRAN) && iterNo == 1)) {
                    shouldReorder = true;
                }
                if (shouldReorder) {
                    /*
                    //                 try {
                    System.out.println("befor reorder");
                    System.out.println(wrk.work);
                     */           //         wrk.order();
                    wrk.factor();
                    /*                    System.out.println("after reorder");
                    System.out.println(wrk.work);
                    /*
                    if (!wrk.reorder(env.getPivotAbsTol(),
                    env.getPivotRelTol(), env.getDiagGMin())) {
                    StandardLog.warning("Reorder returned error");
                    oldState0 = null;
                    return false;
                    }
                    } catch (SingularException ex) {
                    StandardLog.warning("Singular matrix:  check nodes "
                    + ckt.getNodeNameAt(ex.getRow()) + " and "
                    + ckt.getNodeNameAt(ex.getCol()));
                    StandardLog.warning("Reorder returned error");
                    oldState0 = null;
                    return false;
                    } catch (MatrixException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                    }
                     */
                    shouldReorder = false;
                } else {
                    try {
//                    System.out.println("befor reorder");
//                    System.out.println(wrk.work);
//                    wrk.work.order();
                        wrk.factor();
//                    System.out.println("after reorder");
//                    System.out.println(wrk.work);
                        //                      wrk.factor(env.getPivotAbsTol(), env.getDiagGMin());
                    } catch (SingularException ex) {
                        shouldReorder = true;
                        StandardLog.warning("Forced reordering...");
                        continue;
                    } catch (MatrixException ex) {
                        StandardLog.warning("LU Factor returned error");
                        oldState0 = null;
                        return false;
                    }
                }
                if (oldState0 == null) {
                    oldState0 = Arrays.copyOf(stateTable.getColumnAt(0), stateTable.getSize());
                }

                wrk.solve();

                wrk.getRhsAt(0).setZero();
                wrk.getRhsOldAt(0).setZero();

                if (iters.getCount() - oldIters > maxIter) {
                    StandardLog.warning("Iteration limit exceeded");
                    oldState0 = null;
                    return false;
                }
                if (!ckt.isNonConverged() && iterNo != 1) {
                    ckt.setNonConverged(!convTest());
                } else {
                    ckt.setNonConverged(true);
                }
//                StandardLog.info("Converged is " + !ckt.isNonConverged());
            }
            if (env.isNodeDamping() && ckt.isNonConverged()
                    && (mode.contains(MODE.TRANOP) || mode.contains(MODE.DCOP)) && iterNo > 1) {
                double diff;
                double maxDiff = 0;
                Node node;
                for (int i = 0; i < wrk.getSize(); i++) {
                    node = ckt.getNode(i);
                    if (node instanceof VoltageNode) {
                        diff = wrk.getRhsRealAt(i) - wrk.getRhsOldRealAt(i);
                        if (diff > maxDiff) {
                            maxDiff = diff;
                        }
                    }
                }
                if (maxDiff > 10) {
                    double dampFactor = 10 / maxDiff;
                    if (dampFactor < 0.1) {
                        dampFactor = 0.1;
                    }
                    for (int i = 0; i < ckt.getNodeCount(); i++) {
                        diff = wrk.getRhsRealAt(i) - wrk.getRhsOldRealAt(i);
                        //TODO setRealAt imag to 0?
                        wrk.getRhsAt(i).setReal(wrk.getRhsOldRealAt(i) + (dampFactor * diff));
                    }
                    for (int i = 0; i < state0.length; i++) {
                        diff = stateTable.getStateAt(i, 0) - oldState0[i];
                        stateTable.setStateAt(i, 0, oldState0[i] + dampFactor * diff);
                    }
                }
            }

            if (mode.contains(MODE.INIT_FLOAT)) {
                if (mode.contains(MODE.DC) && env.isHadNodeSet()) {
                    if (ipass) {
                        ckt.setNonConverged(true);
                    }
                    ipass = false;
                }
                if (!ckt.isNonConverged()) {
                    oldState0 = null;
                    return true;
                }
            } else if (mode.contains(MODE.INIT_JCT)) {
                mode.removeAllInitFlags();
                mode.getModes().add(MODE.INIT_FIX);
                shouldReorder = true;
            } else if (mode.contains(MODE.INIT_FIX)) {
                if (!ckt.isNonConverged()) {
                    mode.removeAllInitFlags();
                    mode.getModes().add(MODE.INIT_FLOAT);
                }
                ipass = true;
            } else if (mode.contains(MODE.INIT_SMSIG)) {
                mode.removeAllInitFlags();
                mode.getModes().add(MODE.INIT_FLOAT);
            } else if (mode.contains(MODE.INIT_TRAN)) {
                if (iterNo <= 1) {
                    shouldReorder = true;
                }
                mode.removeAllInitFlags();
                mode.getModes().add(MODE.INIT_FLOAT);
            } else if (mode.contains(MODE.INIT_PRED)) {
                mode.removeAllInitFlags();
                mode.getModes().add(MODE.INIT_FLOAT);
            } else {
                StandardLog.warning("Bad Initialization Mode");
                oldState0 = null;
                return false;
            }
            wrk.advanceRhs();
        }
    }

    public boolean acIterate() {
        while (true) {
            if (!ckt.acLoad(mode)) {
                return false;
            }
            if (acShouldReordered) {
                try {
                    if (!wrk.reorder(env.getPivotAbsTol(), env.getPivotRelTol())) {
                        acShouldReordered = false;
                        return false;
                    }
                } catch (MatrixException ex) {
                    StandardLog.severe(ex.getLocalizedMessage());
                }
            } else {
                try {
                    if (!wrk.factor(env.getPivotAbsTol())) {
                        return false;
                    }
                } catch (SingularException ex) {
                    acShouldReordered = true;
                    continue;
                } catch (Exception ex) {
                    return false;
                }
            }
            break;
        }
        wrk.solve();

        wrk.getRhsAt(0).setZero();
        wrk.getRhsOldAt(0).setZero();
        //wrk.getC(0).setZero();

        wrk.advanceRhs();
        return true;
    }

    public boolean reInit() {
        //TODO fix make sure arrays and matrix are the right size and initialized!!!
        //oldState0 = new double[wrk.getSize()][numStates];
        //
        //    for (int i = 0; i < 8; i++) {
        //}
        //  shouldReorder = true;
        //        acShouldReordered = true;
        //        pzShouldReorder = true;
        return true;
    }

    public void update() {
        AnalysisEvent evt = new AnalysisEvent(this);
        fireUpdateEvent(evt);
    }

    public void update(double refValue) {
        AnalysisEvent evt = new AnalysisEvent(this, refValue);
        fireUpdateEvent(evt);
    }

    public void fireUpdateEvent(AnalysisEvent evt) {
        for (FastList.Node<AnalysisEventListener> n = listeners.head(), end = listeners.tail();
                (n = n.getNext()) != end;) {
            n.getValue().update(evt);
        }
    }

    public void fireBeginOutputEvent(AnalysisEvent evt) {
        for (FastList.Node<AnalysisEventListener> n = listeners.head(), end = listeners.tail();
                (n = n.getNext()) != end;) {
            n.getValue().beginOutput(evt);
        }
    }

    public void fireRestartOutputEvent(AnalysisEvent evt) {
        for (FastList.Node<AnalysisEventListener> n = listeners.head(), end = listeners.tail();
                (n = n.getNext()) != end;) {
            n.getValue().restartOutput(evt);
        }
    }

    public void fireEndOutputEvent(AnalysisEvent evt) {
        for (FastList.Node<AnalysisEventListener> n = listeners.head(), end = listeners.tail();
                (n = n.getNext()) != end;) {
            n.getValue().endOutput(evt);
        }
    }

    public String trouble(Node node, Instance instance, String optMsg) {
        TextBuilder msg = new TextBuilder();
        msg.append(getName());
        if (optMsg != null && optMsg.equals("")) {
            msg.append(optMsg);
        }
        switch (getDomain()) {
            case TIME:
                if (tmprl.getTime() == 0.0) {
                    msg.append("Initial timepoint: ");
                } else {
                    msg.append("Time = " + tmprl.getTime() + ": timestep = " + tmprl.getDelta());
                }
                break;
            case FREQUENCY:
                double d = (tmprl.getOmega() / (2.0 * Math.PI));
                msg.append("Frequency = " + d);
                break;
            case SWEEP:
                if (this instanceof DcTrCurv) {
                    DcTrCurv tc = (DcTrCurv) this;
                    for (int i = 0; i <= tc.nestLevel; i++) {
                        if (tc.trvcs[i].inst.getClass() == VSrcInstance.class) {
                            msg.append(tc.trvcs[i].name + " = "
                                    + ((VSrcInstance) tc.trvcs[i].inst).getDcValue());
                        } else {
                            msg.append(tc.trvcs[i].name + " = "
                                    + ((ISrcInstance) tc.trvcs[i].inst).getDcValue());
                        }
                    }
                }
                break;
            case NONE:
            default:
                break;
        }
        if (node != null) {
            msg.append("Trouble with node " + node.getID());
        } else if (instance != null) {
            msg.append("Trouble with instance: " + instance.getInstName());
        } else {
            msg.append("Cause unrecorded.");
        }

        return msg.toString();
    }

    void displayNames() {
        for (int i = 0; i < ckt.getNodeCount(); i++) {
            StandardLog.info(i + " " + ckt.getNode(i).getID());
        }
    }

    public void fireNonConvEvent() {
        AnalysisEvent evt = new AnalysisEvent(this);
        for (FastList.Node<NonConvergenceListener> n = nclisteners.head(), end = nclisteners.tail();
                (n = n.getNext()) != end;) {
            n.getValue().nonConvergence(evt);
        }
    }

    /*
     *
    public boolean dIter(){
    double[] temp;
    ckt.setConverged(false);
    while(true){
    if(acShouldReordered) {
    try {
    acShouldReordered = false;
    wrk.reorder(env.getPivotAbsTol(), env.getPivotRelTol());
    break;
    } catch (SingularException ex) {
    Logger.getLogger("global").log(Level.SEVERE, null, ex);
    } catch (MatrixException ex) {
    Logger.getLogger("global").log(Level.SEVERE, null, ex);
    }
    } else {
    try{
    wrk.factor(env.getPivotAbsTol());
    } catch(SingularException ex){
    acShouldReordered = true;
    continue;
    } catch(Exception ex){
    return false;
    }
    break;
    }
    ckt.setConverged(true);
    wrk.swapAnC();
    ckt.ACLOAD();
    wrk.swapAnC();
    }
    wrk.solve();
    wrk.getRhsAt(0).setZero();
    wrk.getRhsOld(0).setZero();
    wrk.getC(0).setZero();
    wrk.advanceRhs();
    return true;
    }

    public void nzIter(int posDrive, int negDrive) {
    wrk.clearRhs();
    wrk.setRhsRealAt(posDrive, 1.0);
    wrk.setRhsImagAt(negDrive, -1.0);
    wrk.solve();
    wrk.getRhsAt(0).setZero();
    }

    int pzTrapped;

    void normEq(double x1, double x2){
    x1 = Math.sqrt(x1 * x1 + x2 * x2);
    }
    void zaddeq(double a, int amag, double x, int xmag, double y, int ymag) {
    if (xmag > ymag) {
    amag = xmag;
    if (xmag > 50 + ymag)
    y = 0.0;
    else
    for (xmag -= ymag; xmag > 0; xmag--)
    y /= 2.0;
    } else {
    amag = ymag;
    if (ymag > 50 + xmag)
    x = 0.0;
    else
    for (ymag -= xmag; ymag > 0; ymag--)
    x /= 2.0;
    }
    a = x + y;
    if (a == 0.0)
    amag = 0;
    else {
    while (MathLib.abs(a) > 1.0) {
    a /= 2.0;
    amag += 1;
    }
    while (MathLib.abs(a) < 0.5) {
    a *= 2.0;
    amag -= 1;
    }
    }
    }
     */
    public static boolean approx(double a, double b, int maxUlps) {
        long aInt = Double.doubleToLongBits(a);
        if (aInt < 0) {
            aInt = 0x8000000000000000L - aInt;
        }
        long bInt = Double.doubleToLongBits(b);
        if (bInt < 0) {
            bInt = 0x8000000000000000L - bInt;
        }
        long intDiff = Math.abs(aInt - bInt);
        if (intDiff <= maxUlps) {
            return true;
        }
        return false;
    }
}
