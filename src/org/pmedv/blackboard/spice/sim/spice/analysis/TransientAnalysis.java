
/*
 * TransientAnalysis.java
 *
 * Created on April 28, 2006, 7:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.Arrays;
import java.util.EnumSet;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.output.Count;
import org.pmedv.blackboard.spice.sim.output.Statistics;
import org.pmedv.blackboard.spice.sim.output.StopWatch;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.KEY;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.txline.LTRAInstance;

import javolution.lang.MathLib;
import javolution.util.FastList;
import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class TransientAnalysis extends Analysis {

    private double stopTime;
    private double step;
    private double maxStep;
    private double startTime;
    private boolean firstRun;
    public static final String TRAN = "Trans";
    public static final String TRAN_TRUNC = "TranTrunc";
    public static final String TRAN_PTS = "TranPts";
    public static final String TRAN_ITER = "TransIter";
    public static final String ACCEPTED = "Accepted";
    public static final String REJECTED = "Rejected";
    private StopWatch transTime;
    private StopWatch transTruncTime;
    private Count transIter;
    private Count timePts;
    private Count accepted;
    private Count rejected;
    private Count defaultIters;

    /** Creates a new instance of TransientAnalysis */
    public TransientAnalysis() {
        transTime = Statistics.addTime(TRAN);
        transTruncTime = Statistics.addTime(TRAN_TRUNC);
        transIter = Statistics.addCount(TRAN_ITER);
        timePts = Statistics.addCount(TRAN_PTS);
        accepted = Statistics.addCount(ACCEPTED);
        rejected = Statistics.addCount(REJECTED);
    }

    public double getStopTime() {
        return stopTime;
    }

    public void setStopTime(double finalTime) {
        this.stopTime = finalTime;
    }

    public double getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(double maxStep) {
        this.maxStep = maxStep;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean init(Circuit ckt) {
        super.init(ckt);
        tmprl.setFinalTime(stopTime);
        tmprl.setStep(step);
        tmprl.setInitTime(startTime);
        if (maxStep == 0) {
            if (tmprl.getStep() < (stopTime - startTime) / 50.0) {
                tmprl.setMaxStep(maxStep = tmprl.getStep());
            } else {
                tmprl.setMaxStep((tmprl.getFinalTime() - tmprl.getInitTime()) / 50.0);
            }
        } else {
            tmprl.setMaxStep(maxStep);
        }
        tmprl.setDelMin(1e-11 * tmprl.getMaxStep());
        return true;
    }

    public boolean analyze(boolean restart) {
        double oldDelta = 0;
        double delta;
        double timeStep;
        double maxStepSize = 0.0;
        Node node;
        double[] timePoints = null;
        double[] breakPoints = null;
        KEY key = KEY.INIT;
        loop:
        while (true) {
            switch (key) {
                case INIT:
                    if (restart || tmprl.getTime() == 0) {
                        defaultIters = iters;
                        iters = transIter;
                        transTime.start();
                        delta = Math.min(tmprl.getFinalTime() / 200, tmprl.getStep()) / 10;
                        timePoints = tmprl.getTimePoints();
                        if (timePoints != null && timePoints.length != 0) {
                            tmprl.setTimePoints(null);
                        }
                        if (tmprl.getStep() >= tmprl.getMaxStep()) {
                            maxStepSize = tmprl.getStep();
                        } else {
                            maxStepSize = tmprl.getMaxStep();
                        }
                        tmprl.setIncrSize(10);
                        tmprl.setTimeIndex(-1);
                        int listSize = (int) (tmprl.getFinalTime() / maxStepSize + 0.5);
                        LTRAInstance ltra;
                        FastList<Instance> models = ckt.getModels();
                        for (FastList.Node<Instance> m = models.head(), end = models.tail();
                                (m = m.getNext()) != end;) {
                            Instance inst = m.getValue();
                            if (inst instanceof LTRAInstance) {
                                ltra = (LTRAInstance) inst;
                                timePoints = new double[listSize];
                                tmprl.setTimePoints(timePoints);
                                break;
                            }
                        }
                        breakPoints = tmprl.getBreakPoints();
                        if (breakPoints.length > 0) {
                            breakPoints = new double[2];
                            breakPoints[0] = 0;
                            breakPoints[1] = tmprl.getFinalTime();
                            tmprl.setBreakPoints(breakPoints);
                        }
                        if (tmprl.getMinBreak() == 0) {
                            if (env.isXspice()) {
                                tmprl.setMinBreak(10.0 * tmprl.getDelMin());
                            } else {
                                tmprl.setMinBreak(tmprl.getMaxStep() * 5e-5);
                            }
                        }
                        tmprl.setTime(0);
                        tmprl.setDelta(0);
                        tmprl.setBreak(true);
                        firstRun = true;
                        EnumSet<MODE> firstModes = EnumSet.of(MODE.TRANOP, MODE.INIT_JCT);
                        EnumSet<MODE> contModes = EnumSet.of(MODE.TRANOP, MODE.INIT_FLOAT);
                        /*X if (env.isXspice()) {
                        if (tmprl.getRampTime() > 0.0) {
                        tmprl.addBreak(tmprl.getRampTime());
                        }
                        if(ckt.getInsts().size() != 0){
                        converged = doDCOperations(true);
                        evt  ckt.dump(OP.DC, 0.0);
                        evt ckt.opSave(false, 0.0);
                        }
                        } else*/
//                        mode.setUseIC(true);
                        if (!doOperations(firstModes, contModes, env.getDcMaxIter())) {
                            StandardLog.severe("Transient solution failed!");
                            fireNonConvEvent();
                            return false;
                        } else {
                            System.out.println("Initial Transient Solution");
                            System.out.println(String.format("%-30s %-15s", "Node",
                                    "Voltage"));
                            System.out.println(String.format("%-30s %-15s", "----",
                                    "-------"));
                            for (int i = 0; i < ckt.getNodeCount(); i++) {
                                node = ckt.getNode(i);
                                if (node.getID().equals("#branch") || !node.getID().equals("#")) {
                                    System.out.println(String.format("%-30s %-15g",
                                            node.getID(), wrk.getRhsOldRealAt(node.getIndex())));
                                }
                            }
                        }
//X                        ckt.setAnalInit(true);
                        beginOutput();
//                        tmprl.setCurrentBreak(1.0e30);
//                        tmprl.setLastBreak(1.0e30);
                        timePts.increment();
                        tmprl.setOrder(1);
                        for (int i = 0; i < 7; i++) {
                            tmprl.setOldDeltaAt(i, tmprl.getMaxStep());
                        }
                        tmprl.setDelta(delta);
                        StandardLog.fine("Delta initialized to " + delta);
                        tmprl.setSaveDelta(tmprl.getFinalTime() / 50);
                        mode.setModes(MODE.TRAN, MODE.INIT_TRAN);
                        tmprl.setAgAt(0, 0);
                        tmprl.setAgAt(1, 0);
                        state0 = stateTable.getColumnAt(0);
                        double[] state1 = Arrays.copyOf(state0, state0.length);
                        stateTable.setColumnAt(1, state1);
                        transTime.start();
                    } else {
                        transTime.start();
                        mode.setModes(MODE.TRAN, MODE.INIT_PRED);
                        if (tmprl.getMinBreak() == 0) {
                            tmprl.setMinBreak(tmprl.getMaxStep() * 5e-5);
                        }
                        beginOutput();
                        firstRun = false;
                        key = KEY.RESUME;
                        continue loop;
                    }
                case NEXT:
                    if (timePoints != null) {
                        tmprl.incrTimeIndex();
                        if (tmprl.getTimeIndex() >= timePoints.length) {
                            int need = (int) (0.5 + (tmprl.getFinalTime() - tmprl.getTime()) / maxStepSize);
                            double sizeIncr = tmprl.getIncrSize();
                            if (need < sizeIncr) {
                                need = (int) tmprl.getIncrSize();
                            }
                            int timeListSize = timePoints.length + need;
                            timePoints = new double[timeListSize];
                            tmprl.setIncrSize(sizeIncr * 1.4);
                        }
                        timePoints[tmprl.getTimeIndex()] = tmprl.getTime();
                    }
                    if (ckt.accept(mode)) {
                        StandardLog.fine("Delta " + tmprl.getDelta()
                                + " accepted at time " + tmprl.getTime());
                        accepted.increment();
                    } else {
                        transTime.stop();
                        transIter = defaultIters;
                        return false;
                    }
                    tmprl.setBreak(false);
                    if (tmprl.getTime() > tmprl.getBreakAt(0)) {
                        tmprl.clrBreak0();
                    }
                    //X more xspice
                    if (tmprl.getTime() >= tmprl.getInitTime()) {
                        update(tmprl.getTime());
                    }
                    //TODO better stopping points
                    if (Math.abs(tmprl.getTime() - tmprl.getFinalTime()) < tmprl.getMinBreak()
                            || approx(tmprl.getTime(), tmprl.getFinalTime(), 100)) {
                        transTime.stop();
                        iters = defaultIters;
                        return true;
                    }
                    if (pause) {
                        transTime.stop();
                        iters = defaultIters;
                        return false;
                    }
                case RESUME:
                    tmprl.setDelta(Math.min(tmprl.getDelta(), tmprl.getMaxStep()));
                    if (env.isXspice()) {
                        if (tmprl.getTime() == tmprl.getLastBreak()) {
                            tmprl.setOrder(1);
                        }
                    }
                    if (approx(tmprl.getTime(), tmprl.getBreakAt(0), 100) || tmprl.getBreakAt(0)
                            - tmprl.getTime() <= tmprl.getDelMin()) {
                        tmprl.setOrder(1);
                        if ((tmprl.getDelta() > 0.1 * tmprl.getSaveDelta())
                                || (tmprl.getDelta() > 0.1 * (tmprl.getBreakAt(1) - tmprl.getBreakAt(0)))) {
                            StandardLog.fine("Limited by pre-breakpoint delta");
                        }
                        tmprl.setDelta(Math.min(tmprl.getDelta(), 0.1 * Math.min(tmprl.getSaveDelta(),
                                tmprl.getBreakAt(1) - tmprl.getBreakAt(0))));
                        if (firstRun) {
                            tmprl.setDelta(tmprl.getDelta() / 10);
                            StandardLog.fine("Delta cut for initial timepoint");
                        }
                        /*X                        if (env.isXspice()) {
                        if (tmprl.getTime() + tmprl.getDelta() >= tmprl.getCurrentBreak()) {
                        tmprl.setSaveDelta(tmprl.getDelta());
                        tmprl.setDelta(tmprl.getCurrentBreak() - tmprl.getTime());
                        tmprl.setLastBreak(tmprl.getTime() + tmprl.getDelta());
                        } else {
                        tmprl.setLastBreak(1.0e30);
                        }
                        //? needs ng-20
                        while (tmprl.getBreakPoints().length != 0) {
                        if (tmprl.getBreakAt(0) <= tmprl.getTime() + tmprl.getMinBreak()) {
                        tmprl.clrBreaks();
                        } else {
                        break;
                        }
                        }
                        if (tmprl.getTime() + tmprl.getDelta() > tmprl.getBreakAt(0)) {
                        tmprl.setBreak(true);
                        tmprl.setSaveDelta(tmprl.getDelta());
                        tmprl.setDelta(tmprl.getBreakAt(0) - tmprl.getTime());
                        }
                        } else {
                         */
                        tmprl.setDelta(Math.max(tmprl.getDelta(), tmprl.getDelMin() * 2.0));
//X                        }
                    } else if (tmprl.getTime() + tmprl.getDelta() >= tmprl.getBreakAt(0)) {
                        tmprl.setSaveDelta(tmprl.getDelta());
                        tmprl.setDelta(tmprl.getBreakAt(0) - tmprl.getTime());
                        StandardLog.fine("Delta cut to " + tmprl.getDelta() + " to hit breakpoint ");
                        tmprl.setBreak(true);
                    }
                    //X
            /*
                    if(env.isXspice() && wrong ckt.getInsts().size() > 0) {
                    if(tmprl.getTime() == 0.0 && (!xckt.opAlternate))
                    iterate();
                    tmprl.setStep(xckt.nextTime());
                    while(xckt.nextTime() <= tmprl.getTime() + tmprl.getDelta()) {
                    tmprl.setCurrentBreak(1e30);
                    xckt.dequeue((long)(tmprl.getStep() * 1e-9));
                    iterate();
                    if(tmprl.getBreakAt(0) < tmprl.getCurrentBreak())
                    if(tmprl.getBreakAt(0) > tmprl.getTime() + tmprl.getMinBreak())
                    tmprl.setCurrentBreak(tmprl.getBreakAt(0));
                    if(tmprl.getCurrentBreak() < (tmprl.getTime() + tmprl.getDelta())) {
                    if(tmprl.getCurrentBreak() > tmprl.getTime() + tmprl.getMinBreak()
                    && tmprl.getCurrentBreak() >= tmprl.getStep()) {
                    tmprl.setSaveDelta(tmprl.getDelta());
                    tmprl.setDelta(tmprl.getCurrentBreak() - tmprl.getTime());
                    tmprl.setLastBreak(tmprl.getTime() + tmprl.getDelta());
                    }
                    }
                    tmprl.setStep(tmprl.nextTime());
                    }
                     */
                    for (int i = 5; i >= 0; i--) {
                        tmprl.setOldDeltaAt(i + 1, tmprl.getOldDeltaAt(i));
                    }
                    tmprl.setOldDeltaAt(0, tmprl.getDelta());
                    double[] tmp = stateTable.getColumnAt(tmprl.getMaxOrder() + 1);
                    for (int i = tmprl.getMaxOrder(); i >= 0; i--) {
                        stateTable.setColumnAt(i + 1, stateTable.getColumnAt(i));
                    }
                    stateTable.setColumnAt(0, tmp);
                    while (true) {
                        oldDelta = tmprl.getDelta();
                        tmprl.setTime(tmprl.getTime() + tmprl.getDelta());
                        tmprl.setOldDeltaAt(0, tmprl.getDelta());
                        computeCoefficients();
                        //?      pred();
                        /*X                tmprl.setCurrentBreak(1.0e30);
                        if (tmprl.getDelta() <= tmprl.getDelMin()) {
                        lastIterCall = true;
                        } else {
                        lastIterCall = false;
                        }
                        tmprl.setStep(tmprl.getTime());
                         */
                        boolean converged = iterate(env.getTranMaxIter());

                        //X                    tmprl.setStep(tmprl.getTime());
                        //X                    ckt.callHybrids();
                        timePts.increment();
                        mode.setModes(MODE.TRAN, MODE.INIT_PRED);
//                        printStates();
                        if (firstRun) {
                            for (int i = 0; i < stateTable.getSize(); i++) {
                                stateTable.setStateAt(i, 2, stateTable.getStateAt(i, 1));
                                stateTable.setStateAt(i, 3, stateTable.getStateAt(i, 1));
                            }
                        }
                        if (!converged) {
                            tmprl.setTime(tmprl.getTime() - tmprl.getDelta());
                            tmprl.setDelta(tmprl.getDelta() / 8);
                            rejected.increment();
                            StandardLog.fine("Delta cut to " + tmprl.getDelta()
                                    + " for non-convergance");
                            if (firstRun) {
                                mode.setModes(MODE.TRAN, MODE.INIT_TRAN);
                            }
                            tmprl.setOrder(1);
                            /*X                } else if (tmprl.getCurrentBreak() < tmprl.getTime()) {
                            tmprl.setSaveDelta(tmprl.getDelta());
                            tmprl.setTime(tmprl.getTime() - tmprl.getDelta());
                            tmprl.setDelta(tmprl.getCurrentBreak() - tmprl.getTime());
                            tmprl.setLastBreak(tmprl.getTime() + tmprl.getDelta());
                            if (firstRun) {
                            mode.setModes(MODE.TRAN, MODE.INIT_TRAN);
                            }
                            tmprl.setOrder(1);
                             *
                             */
                        } else {
                            if (firstRun) {
                                firstRun = false;
                                key = KEY.NEXT;
                                continue loop;
                            }
                            timeStep = tmprl.getDelta();
                            if ((timeStep = truncateTimeStep(timeStep)) == 0) {
                                transTime.stop();
                                iters = defaultIters;
                                return false;
                            }
                            if (timeStep > 0.9 * tmprl.getDelta()) {
                                if (tmprl.getOrder() == 1) {
                                    timeStep = tmprl.getDelta();
                                    tmprl.setOrder(2);
                                    if ((timeStep = truncateTimeStep(timeStep)) == 0) {
                                        transTime.stop();
                                        iters = defaultIters;
                                        return false;
                                    }
                                    if (timeStep <= 1.05 * tmprl.getDelta()) {
                                        tmprl.setOrder(1);
                                    }
                                }
                                tmprl.setDelta(timeStep);
                                StandardLog.fine("Delta set to truncation error result: "
                                        + tmprl.getDelta() + ". Point accepted");
                                key = KEY.NEXT;
                                continue loop;
                            } else {
                                tmprl.setTime(tmprl.getTime() - tmprl.getDelta());
                                rejected.increment();
                                tmprl.setDelta(timeStep);
                                StandardLog.fine("Delta set to truncation error result:point rejected");
                            }
                        }
                        if (tmprl.getDelta() <= tmprl.getDelMin()) {
                            if (oldDelta > tmprl.getDelMin()) {
                                tmprl.setDelta(tmprl.getDelMin());
                                StandardLog.fine("Delta at delMin");
                            } else {
                                transTime.stop();
                                iters = defaultIters;
                                StandardLog.warning("TimeStep too small");
                                return false;
                            }
                        }
                        //X                ckt.backup(tmprl.getTime() + tmprl.getDelta());
                    }
            }
        }
    }

    public double pred(double[] loc) {
        double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
        return (((1 + xfact) * loc[1]) - (xfact * loc[2]));
    }

    public boolean pred() {
        int size = wrk.getSize();
        switch (env.getIntegMethod()) {
            case TRAPEZOIDAL:
                double dd0;
                double dd1;
                switch (tmprl.getOrder()) {
                    case 1:
                        for (int i = 0; i <= size; i++) {
                            dd0 = (wrk.getPredSolAt(0, i) - wrk.getPredSolAt(1, i))
                                    / tmprl.getOldDeltaAt(1);
                            wrk.setRhsRealAt(i, wrk.getPredSolAt(0, i)
                                    + tmprl.getOldDeltaAt(0) * dd0);
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    case 2:
                        for (int i = 0; i <= size; i++) {
                            double b = -tmprl.getOldDeltaAt(0) / (2 * tmprl.getOldDeltaAt(1));
                            double a = 1 - b;
                            dd0 = (wrk.getPredSolAt(0, i) - wrk.getPredSolAt(1, i))
                                    / tmprl.getOldDeltaAt(1);
                            dd1 = (wrk.getPredSolAt(1, i) - wrk.getPredSolAt(2, i))
                                    / tmprl.getOldDeltaAt(2);
                            wrk.setRhsRealAt(i, wrk.getPredSolAt(0, i)
                                    + (b * dd1 + a * dd0) * tmprl.getOldDeltaAt(0));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    default:
                        StandardLog.warning("Inproper order: " + ckt.getName()
                                + " " + tmprl.getOrder());
                        return false;
                }
            case GEAR:
                switch (tmprl.getOrder()) {
                    case 1:
                        for (int i = 0; i <= size; i++) {
                            wrk.setRhsRealAt(i, tmprl.getAgpAt(0) * wrk.getPredSolAt(0, i)
                                    + tmprl.getAgpAt(1) * wrk.getPredSolAt(1, i));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    case 2:
                        for (int i = 0; i <= size; i++) {
                            wrk.setRhsRealAt(i, tmprl.getAgpAt(0) * wrk.getPredSolAt(0, i)
                                    + tmprl.getAgpAt(1) * wrk.getPredSolAt(1, i)
                                    + tmprl.getAgpAt(2) * wrk.getPredSolAt(2, i));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    case 3:
                        for (int i = 0; i <= size; i++) {
                            wrk.setRhsRealAt(i, tmprl.getAgpAt(0) * wrk.getPredSolAt(0, i)
                                    + tmprl.getAgpAt(1) * wrk.getPredSolAt(1, i)
                                    + tmprl.getAgpAt(2) * wrk.getPredSolAt(2, i)
                                    + tmprl.getAgpAt(3) * wrk.getPredSolAt(3, i));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    case 4:
                        for (int i = 0; i <= size; i++) {
                            wrk.setRhsRealAt(i, tmprl.getAgpAt(0) * wrk.getPredSolAt(0, i)
                                    + tmprl.getAgpAt(1) * wrk.getPredSolAt(1, i)
                                    + tmprl.getAgpAt(2) * wrk.getPredSolAt(2, i)
                                    + tmprl.getAgpAt(3) * wrk.getPredSolAt(3, i)
                                    + tmprl.getAgpAt(4) * wrk.getPredSolAt(4, i));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    case 5:
                        for (int i = 0; i <= size; i++) {
                            wrk.setRhsRealAt(i, tmprl.getAgpAt(0) * wrk.getPredSolAt(0, i)
                                    + tmprl.getAgpAt(1) * wrk.getPredSolAt(1, i)
                                    + tmprl.getAgpAt(2) * wrk.getPredSolAt(2, i)
                                    + tmprl.getAgpAt(3) * wrk.getPredSolAt(3, i)
                                    + tmprl.getAgpAt(4) * wrk.getPredSolAt(4, i)
                                    + tmprl.getAgpAt(5) * wrk.getPredSolAt(5, i));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    case 6:
                        for (int i = 0; i <= size; i++) {
                            wrk.setRhsRealAt(i, tmprl.getAgpAt(0) * wrk.getPredSolAt(0, i)
                                    + tmprl.getAgpAt(1) * wrk.getPredSolAt(1, i)
                                    + tmprl.getAgpAt(2) * wrk.getPredSolAt(2, i)
                                    + tmprl.getAgpAt(3) * wrk.getPredSolAt(3, i)
                                    + tmprl.getAgpAt(4) * wrk.getPredSolAt(4, i)
                                    + tmprl.getAgpAt(5) * wrk.getPredSolAt(5, i)
                                    + tmprl.getAgpAt(6) * wrk.getPredSolAt(6, i));
                            wrk.setPredAt(i, wrk.getRhsRealAt(i));
                        }
                        break;
                    default:
                        return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }
    private double[][] matrix = new double[8][8];
    /*
    Computes coefficients for particular integration methods
     */

    public boolean computeCoefficients() {
        int order = tmprl.getOrder();
        double arg;
        double arg1;
        switch (env.getIntegMethod()) {
            case TRAPEZOIDAL:
                switch (order) {
                    case 1:
                        tmprl.setAgAt(0, 1 / tmprl.getDelta());
                        tmprl.setAgAt(1, -1 / tmprl.getDelta());
                        break;
                    case 2:
                        tmprl.setAgAt(0, (1.0 / tmprl.getDelta() / (1.0 - 0.5)));
                        tmprl.setAgAt(1, 0.5 / (1.0 - 0.5));
                        break;
                    default:
                        return false;
                }
                break;
            case GEAR:
                switch (order) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        tmprl.setAg(new double[7]);
                        tmprl.setAgAt(1, (-1 / tmprl.getDelta()));
                        arg = 0;
                        for (int i = 0; i <= order; i++) {
                            matrix[0][i] = 1;
                        }
                        for (int i = 1; i <= order; i++) {
                            matrix[i][0] = 0;
                        }
                        for (int i = 1; i <= order; i++) {
                            arg += tmprl.getOldDeltaAt(i - 1);
                            arg1 = 1;
                            for (int j = 1; j <= order; j++) {
                                arg1 *= arg / tmprl.getDelta();
                                matrix[j][i] = arg1;
                            }
                        }
                        for (int i = 1; i <= order; i++) {
                            for (int j = i + 1; j <= order; j++) {
                                matrix[j][i] /= matrix[i][i];
                                for (int k = i + 1; k <= order; k++) {
                                    matrix[j][k] -= matrix[j][i] * matrix[i][k];
                                }
                            }
                        }
                        for (int i = 1; i <= order; i++) {
                            for (int j = i + 1; j <= order; j++) {
                                tmprl.setAgAt(j, tmprl.getAgAt(j) - matrix[j][i] * tmprl.getAgAt(i));
                            }
                        }
                        tmprl.setAgAt(order, tmprl.getAgAt(order) / matrix[order][order]);
                        for (int i = order - 1; i >= 0; i--) {
                            for (int j = i + 1; j <= order; j++) {
                                tmprl.setAgAt(i, tmprl.getAgAt(i) - matrix[i][j] * tmprl.getAgAt(j));
                            }
                            tmprl.setAgAt(i, tmprl.getAgAt(i) / matrix[i][i]);
                        }
                        break;
                    default:
                        return false;
                }
                break;
            default:
                return false;
        }
        /*
        switch (env.getIntegMethod()) {
        case TRAPEZOIDAL:
        arg = tmprl.getDelta() / (2 * tmprl.getOldDeltaAt(1));
        tmprl.setAgpAt(0, 1 + arg);
        tmprl.setAgpAt(1, -arg);
        break;
        case GEAR:
        tmprl.setAgp(new double[7]);
        tmprl.setAgpAt(0, 1);
        for (int i = 0; i <= order; i++) {
        matrix[0][i] = 1;
        }
        arg = 0;
        for (int i = 0; i <= order; i++) {
        arg += tmprl.getOldDeltaAt(i);
        arg1 = 1;
        for (int j = 1; j <= order; j++) {
        arg1 *= arg / tmprl.getDelta();
        matrix[j][i] = arg1;
        }
        }
        for (int i = 0; i <= order; i++) {
        for (int j = i + 1; j <= order; j++) {
        matrix[j][i] /= matrix[i][i];
        for (int k = i + 1; k <= order; k++) {
        matrix[j][k] -= matrix[j][i] * matrix[i][k];
        }
        }
        }
        for (int i = 0; i <= order; i++) {
        for (int j = i + 1; j <= order; j++) {
        tmprl.setAgpAt(j, tmprl.getAgpAt(j) - (matrix[j][i] * tmprl.getAgpAt(i)));
        }
        }
        tmprl.setAgpAt(order, tmprl.getAgpAt(order) / matrix[order][order]);
        for (int i = order - 1; i >= 0; i--) {
        for (int j = i + 1; j <= order; j++) {
        tmprl.setAgpAt(i, tmprl.getAgpAt(i) - (matrix[i][j] * tmprl.getAgpAt(j)));
        }
        tmprl.setAgpAt(i, tmprl.getAgpAt(i) / matrix[i][i]);
        }
        break;
        default:
        return false;
        }*/
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        double timeTmp = Double.MAX_VALUE;
        transTruncTime.start();
        double debugTmp = timeTmp;
        FastList<Instance> models = ckt.getModels();
        for (FastList.Node<Instance> m = models.head(), end = models.tail();
                (m = m.getNext()) != end;) {
            if ((timeTmp = m.getValue().truncateTimeStep(timeTmp)) != 0) {
                if (debugTmp != timeTmp) {
                    StandardLog.fine("Timestep cut by device type "
                            + m.getValue().getInstName() + " from "
                            + debugTmp + " to " + timeTmp);
                }
            } else {
                transTruncTime.stop();
                return 0;
            }
        }
        return MathLib.min(2 * timeStep, timeTmp);
    }

    /*
    double tmp;
    double diff;
    double tol;
    StandardLog.fine("At time " + tmprl.getTime() + ", Delta = "
    + tmprl.getOldDeltaAt(0));
    switch (env.getIntegMethod()) {
    case TRAPEZOIDAL:
    switch (tmprl.getOrder()) {
    case 1:
    for (int i = 0; i < ckt.getNodeCount(); i++) {
    node = ckt.getNode(i);
    tol = MathLib.max(MathLib.abs(wrk.getRhsRealAt(i)),
    MathLib.abs(wrk.getPredAt(i)))
     * env.getLteRelTol() + env.getLteAbsTol();
    if (!(node instanceof VoltageNode)) {
    continue;
    }
    diff = wrk.getRhsRealAt(i) - wrk.getPredAt(i);
    StandardLog.fine(node.getID() + ": cor = "
    + wrk.getRhsRealAt(i) + " pred = " + wrk.getPredAt(i));
    if (diff != 0) {
    tmp = env.getTrTol() * tol * 2 / diff;
    tmp = tmprl.getOldDeltaAt(0)
     * MathLib.sqrt(MathLib.abs(tmp));
    timeTmp = MathLib.min(timeTmp, tmp);
    StandardLog.fine("tol = " + tol + " diff = " + diff + " h " + tmp);
    } else {
    StandardLog.fine("diff is 0");
    }
    }
    break;
    case 2:
    for (int i = 0; i < ckt.getNodeCount(); i++) {
    node = ckt.getNode(i);
    tol = MathLib.max(MathLib.abs(wrk.getRhsRealAt(i)),
    MathLib.abs(wrk.getPredAt(i))
     * env.getLteRelTol() + env.getLteAbsTol());
    if (!(node instanceof VoltageNode)) {
    continue;
    }
    diff = wrk.getRhsRealAt(i) - wrk.getPredAt(i);
    StandardLog.fine(node.getID() + ": cor = "
    + wrk.getRhsRealAt(i) + " pred = " + wrk.getPredAt(i));
    if (diff != 0) {
    tmp = tmprl.getOldDeltaAt(0) * env.getTrTol() * tol * 3 * (tmprl.getOldDeltaAt(0)
    + tmprl.getOldDeltaAt(1)) / diff;
    tmp = MathLib.abs(tmp);
    timeTmp = MathLib.min(timeTmp, tmp);
    StandardLog.fine("tol = " + tol + " diff = " + diff + " h " + tmp);
    } else {
    StandardLog.fine("diff is 0");
    }
    }
    break;
    default:
    return 0;
    }
    break;
    case GEAR: {
    double delsum = 0;
    for (int i = 0; i <= tmprl.getOrder(); i++) {
    delsum += tmprl.getOldDeltaAt(i);
    for (int j = 0; j < ckt.getNodeCount(); j++) {
    node = ckt.getNode(j);
    if (!(node instanceof VoltageNode)) {
    continue;
    }
    tol = MathLib.max(MathLib.abs(wrk.getRhsRealAt(j)),
    MathLib.abs(wrk.getPredAt(j))) * env.getLteRelTol()
    + env.getLteAbsTol();
    diff = wrk.getRhsRealAt(j) - wrk.getPredAt(j);
    StandardLog.fine(node.getID() + ": cor = "
    + wrk.getRhsRealAt(j) + " pred = " + wrk.getPredAt(j));
    if (diff != 0) {
    tmp = tol * env.getTrTol() * delsum
    / (diff * tmprl.getDelta());
    tmp = MathLib.abs(tmp);
    switch (tmprl.getOrder()) {
    case 0:
    break;
    case 1:
    tmp = MathLib.sqrt(tmp);
    break;
    default:
    tmp = MathLib.exp(MathLib.log(tmp)
    / (tmprl.getOrder() + 1));
    break;
    }
    tmp *= tmprl.getDelta();
    timeTmp = MathLib.min(timeTmp, tmp);
    StandardLog.fine("tol = " + tol + " diff = "
    + diff + " h " + tmp);
    } else {
    StandardLog.fine("diff is 0");
    }
    }
    }
    }
    break;
    default:
    return 0;
    }
    timeStep = MathLib.min(2 * timeStep, timeTmp);
    transTruncTime.stop();
    return timeStep;
    }*/
    private void printStates() {
        for (int i = 0; i < stateTable.getSize(); i++) {
            double[][] states = stateTable.getStates();
            System.out.printf("%10g %10g %10g %10g\n", states[0][i], states[1][i], states[2][i], states[3][i]);
        }
    }
}
