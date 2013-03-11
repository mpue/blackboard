/*
 * TransFunc.java
 *
 * Created on April 28, 2006, 7:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.List;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.output.Count;
import org.pmedv.blackboard.spice.sim.output.Statistics;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcInstance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcInstance;

import javolution.util.FastList;
import javolution.util.StandardLog;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class TransFunc extends Analysis {

    private Node outPos;
    private Node outNeg;
    private String outSrcStr;
    private String inSrcStr;
    private String outName;
    private boolean outIsV;
    private boolean outIsI;
    private boolean inIsV;
    private boolean inIsI;
    public static final String TF_TIMER = "transFuncTimer";
    private Count tfTimer;

    public static enum TF {

        OUT_POS, OUT_NEG, OUT_SRC, IN_SRC, OUT_NAME;
    }

    /**
     * Creates a new instance of TransFunc
     */
    public TransFunc() {
        Statistics.addCount(TF_TIMER);
    }

    public String getInSrcStr() {
        return inSrcStr;
    }

    public void setInSrcStr(String inSrcStr) {
        this.inSrcStr = inSrcStr;
    }

    public String getOutSrcStr() {
        return outSrcStr;
    }

    public void setOutSrcStr(String outSrcStr) {
        this.outSrcStr = outSrcStr;
    }

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }

    public void addMultiSetEventListener(MultiSetAnalysisListener l) {
        addAnalysisEventListener(l);

    }

    public boolean analyze(boolean restart) {
        int inSrc = 0;
        int outSrc = 0;
        double outPut = 0;
        FastList<Real> outData = FastList.newInstance();
        FastList<String> uIDs = FastList.newInstance();

        if (!doDCOperations()) {
            return false;
        }
        ISrcInstance iSrc = null;
        VSrcInstance vSrc = null;
        FastList<Instance> instances = ckt.getModels();
        for (FastList.Node<Instance> n = instances.head(),
                end = instances.tail(); (n = n.getNext()) != end;) {
            Instance dev = n.getValue();
            if (dev instanceof ISrcInstance && dev.getInstName().equals(inSrcStr)) {
                iSrc = (ISrcInstance) dev;
            } else if (dev instanceof VSrcInstance && dev.getInstName().equals(inSrcStr)) {
                vSrc = (VSrcInstance) dev;
            } else {
                StandardLog.warning("Transfer function source " + inSrcStr
                        + " not in circuit");
                return false;
            }
        }
        wrk.clearRhs();
        if (iSrc != null) {
            wrk.getRhsAt(iSrc.getPosIndex()).realMinusEq(1);
            wrk.getRhsAt(iSrc.getNegIndex()).realPlusEq(1);
        } else if (vSrc != null) {
            inSrc = ckt.findBranch(inSrcStr);
            wrk.getRhsAt(inSrc).realPlusEq(1);
        }
        wrk.solve();
        wrk.setRhsRealAt(0, 0);
        uIDs.add("Transfer function");
        uIDs.add(inSrcStr + "'s Input impedance");
        if (outIsI) {
            uIDs.add(outSrcStr + "'s Output impedance");
        } else {
            uIDs.add("output impedance at " + outName);
        }
    //TODO tf needs work
        beginOutput(outName, (List<String>) uIDs);
        if (outIsV) {
            outPut = wrk.getRhsRealAt(outPos.getIndex())
                    - wrk.getRhsRealAt(outNeg.getIndex());
        } else {
            outSrc = ckt.findBranch(outSrcStr);
            outPut = wrk.getRhsRealAt(outSrc);
        }
        outData.add(Real.valueOf(outPut));
        if (iSrc != null) {
            outPut = wrk.getRhsRealAt(iSrc.getNegIndex())
                    - wrk.getRhsRealAt(iSrc.getPosIndex());
        } else if (vSrc != null) {
            if (Math.abs(wrk.getRhsRealAt(inSrc)) < 1e-20) {
                outPut = 1e20;
            } else {
                outPut = -1 / wrk.getRhsRealAt(inSrc);
            }
        }
        outData.add(Real.valueOf(outPut));
        if (!outIsI && outSrc != inSrc) {
            wrk.clearRhs();
            if (outIsV) {
                wrk.getRhsAt(outPos.getIndex()).realMinusEq(1);
                wrk.getRhsAt(outNeg.getIndex()).realPlusEq(1);
            } else {
                wrk.getRhsAt(outSrc).realPlusEq(1);
            }
            wrk.solve();
            wrk.setRhsRealAt(0, 0);
            if (outIsV) {
                outPut = wrk.getRhsRealAt(outNeg.getIndex())
                        - wrk.getRhsRealAt(outPos.getIndex());
            } else {
                outPut = 1 / Math.max(1e-20, wrk.getRhsRealAt(outSrc));
            }
        }
        outData.add(Real.valueOf(outPut));
        fireUpdate(0, outData);
        endOutput();
        return true;
    }

    public void fireUpdate(double regValue, FastList<Real> data) {
        MultiSetAnalysisEvent evt = new MultiSetAnalysisEvent(this, regValue, data);
        for (FastList.Node<AnalysisEventListener> n = listeners.head(),
                end = listeners.tail(); (n = n.getNext()) != end;) {
            AnalysisEventListener l = n.getValue();
            if (l instanceof MultiSetAnalysisListener) {
                l.update(evt);
            }
        }
    }
}
