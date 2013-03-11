/*
 * Inductor.java
 *
 * Created on May 23, 2006, 3:43 PM
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

import javolution.util.StandardLog;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class IndInstance extends IndModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    private int brEqIndex;
    /* Specific (one turn) inductance */
    private double specInd;
    private Complex posIbrNode;
    private Complex negIbrNode;
    private Complex ibrNegNode;
    private Complex ibrPosNode;
    private Complex ibrIbrNode;
    private StateVector fluxStates;
    private StateVector voltStates;
    private Real rEq = Real.zero();
    private Real vEq = Real.zero();

    /**
     * Creates a new instance of IndInstance
     */
    public IndInstance() {
    }

    public StateVector getFluxStates() {
        return fluxStates;
    }

    public int getBrEqIndex() {
        return brEqIndex;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        fluxStates = stateTable.createRow();
        voltStates = stateTable.createRow();
        if (length > 0.0) {
            if (mu != 0) {
                specInd = (mu * Constants.muZero * cSect * cSect) / length;
            } else {
                specInd = (Constants.muZero * cSect * cSect) / length;
            }
        } else {
            specInd = 0.0;
        }
        if (induct == 0) {
            induct = nt * nt * specInd;
        }
        if (temp == 0) {
            temp = env.getTemp();
        } else {
            StandardLog.info(getInstName()
                    + ": Instance temperature specified, dtemp ignored");
            dtemp = 0;
        }
        if (brEqIndex == 0) {
            brEqIndex = ckt.makeCurrentNode(getInstName() + "#branch").getIndex();
        }
        posIbrNode = wrk.aquireNode(posIndex, brEqIndex);
        negIbrNode = wrk.aquireNode(negIndex, brEqIndex);
        ibrNegNode = wrk.aquireNode(brEqIndex, negIndex);
        ibrPosNode = wrk.aquireNode(brEqIndex, posIndex);
        ibrIbrNode = wrk.aquireNode(brEqIndex, brEqIndex);
        return true;
    }

    public boolean unSetup() {
        if (brEqIndex == 0) {
            ckt.deleteNode(brEqIndex);
            brEqIndex = 0;
        }
        return true;
    }

    public boolean acLoad(Mode mode) {
        double val = tmprl.getOmega() * induct;
        posIbrNode.realPlusEq(1);
        negIbrNode.realMinusEq(1);
        ibrPosNode.realPlusEq(1);
        ibrNegNode.realMinusEq(1);
        ibrIbrNode.imagMinusEq(val);
        return true;
    }

    public boolean load(Mode mode) {
        if (mode.contains(MODE.DC, MODE.INIT_PRED)) {
            if (mode.contains(MODE.INIT_TRAN) && mode.isUseIC()) {
                fluxStates.set(0, induct * initCond);
            } else {
                fluxStates.set(0, induct * wrk.getRhsOldRealAt(brEqIndex));
            }
        }
        if (mode.contains(MODE.DC)) {
            rEq.setZero();
            vEq.setZero();
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                fluxStates.set(0, fluxStates.get(1));
            } else {
                if (mode.contains(MODE.INIT_TRAN)) {
                    fluxStates.set(1, fluxStates.get(0));
                }
            }
            stateTable.integrate(rEq, vEq, induct, fluxStates, voltStates);
        }
        wrk.getRhsAt(brEqIndex).realPlusEq(vEq);
        if (mode.contains(MODE.INIT_TRAN)) {
            voltStates.set(1, voltStates.get(0));
        }
        posIbrNode.realPlusEq(1);
        negIbrNode.realMinusEq(1);
        ibrPosNode.realPlusEq(1);
        ibrNegNode.realMinusEq(1);
        ibrIbrNode.realMinusEq(rEq);
        return true;
    }

    public boolean pzLoad(Complex s) {
        double val = induct;
        posIbrNode.realPlusEq(1);
        negIbrNode.realMinusEq(1);
        ibrPosNode.realPlusEq(1);
        ibrNegNode.realMinusEq(1);
        ibrIbrNode.realMinusEq(val * s.getReal());
        ibrIbrNode.imagMinusEq(val * s.getImag());
        return true;
    }

    public boolean temperature() {
        double difference = (temp + dtemp) - tnom;
        double factor = 1.0 + tempCoeff1 * difference
                + tempCoeff2 * difference * difference;
        induct = induct * factor * scale;
        induct = induct / m;
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return stateTable.terr(fluxStates, voltStates, timeStep);
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
