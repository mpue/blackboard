/** Capasitor.java
 *
 * Created on May 10, 2006, 11:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Constants;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.StateTable;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.StateTable.StateVector;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;

import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class CapInstance extends CapModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    private Complex posPosNode;
    private Complex posNegNode;
    private Complex negPosNode;
    private Complex negNegNode;
    private Real gEq = Real.zero();
    private Real iEq = Real.zero();
    /* Charge on the capacitor */
    private StateVector qcapStates;

    /* Current through the capacitor */
    private StateVector icapStates;

    /** Creates a new CapInstance of Capasitor */
    public CapInstance() {
    }

    public boolean unSetup() {
        return true;
    }

    public boolean acLoad(Mode mode) {
        double val = tmprl.getOmega() * capAC;
        posPosNode.imagPlusEq(m * val);
        negNegNode.imagPlusEq(m * val);
        posNegNode.imagMinusEq(m * val);
        negPosNode.imagMinusEq(m * val);
        return true;
    }

    public boolean doIC() {
        if (initCond == Double.MIN_VALUE) {
            initCond = wrk.getRhsRealAt(posIndex) - wrk.getRhsRealAt(negIndex);
        }
        return true;
    }

    public boolean load(Mode mode) {
        double vcap;
        if (mode.contains(MODE.TRAN, MODE.AC, MODE.TRANOP)) {
            if (mode.contains(MODE.DC, MODE.INIT_JCT)
                    || (mode.contains(MODE.INIT_TRAN) && mode.isUseIC())) {
                vcap = initCond;
            } else {
                vcap = wrk.getRhsOldRealAt(posIndex) - wrk.getRhsOldRealAt(negIndex);
            }
            if (mode.contains(MODE.TRAN, MODE.AC)) {
                if (mode.contains(MODE.INIT_PRED)) {
                    qcapStates.set(0, qcapStates.get(1));
                } else {
                    qcapStates.set(0, capAC * vcap);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qcapStates.set(1, qcapStates.get(0));
                    }
                }
                stateTable.integrate(gEq, iEq, capAC, qcapStates, icapStates);
                if (mode.contains(MODE.INIT_TRAN)) {
                    icapStates.set(1, icapStates.get(0));
                }
                posPosNode.realPlusEq(m * gEq.get());
                negNegNode.realPlusEq(m * gEq.get());
                posNegNode.realMinusEq(m * gEq.get());
                negPosNode.realMinusEq(m * gEq.get());
                wrk.setRhsRealAt(posIndex, wrk.getRhsRealAt(posIndex) - (m * iEq.get()));
                wrk.setRhsRealAt(negIndex, wrk.getRhsRealAt(negIndex) + (m * iEq.get()));
            } else {
                qcapStates.set(0, getCapAC() * vcap);
            }
        }
        return true;
    }

    public boolean pzLoad(Complex c) {
        posPosNode.plusEq(c.times(m * capAC));
        negNegNode.plusEq(c.times(m * capAC));
        posNegNode.minusEq(c.times(m * capAC));
        negPosNode.minusEq(c.times(m * capAC));
        return true;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        qcapStates = stateTable.createRow();
        icapStates = stateTable.createRow();
        if (cj != 0) {
            if (thick > 0.0) {
                if (di != 0) {
                    setCj((getDi() * Constants.EPS_ZERO) / getThick());
                } else {
                    setCj(Constants.EPS_SiO2 / getThick());
                }
            } else {
                setCj(0.0);
            }
        }
        posPosNode = wrk.aquireNode(posIndex, posIndex);
        negNegNode = wrk.aquireNode(negIndex, negIndex);
        posNegNode = wrk.aquireNode(posIndex, negIndex);
        negPosNode = wrk.aquireNode(negIndex, posIndex);
        return true;
    }

    public boolean temperature() {
        double difference;
        double factor;
        if (temp == 0) {
            setTemp(env.getTemp());
        } else {
            setDTemp(0.0);
        }
        if (capAC == 0) {
            capAC = cj * (width - narrow) * (length - shorter)
                    + cjsw * 2 * ((length - shorter) + (width - narrow));
        }
        difference = (getTemp() + getDTemp()) - getTnom();
        factor = 1.0 + (tempCoeff1) * difference
                + (tempCoeff2) * difference * difference;
        capAC = capAC * factor * scale;
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return stateTable.terr(qcapStates, icapStates, timeStep);
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}
