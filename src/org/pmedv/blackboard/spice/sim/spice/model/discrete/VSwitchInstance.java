/*
 * sw.java
 *
 * Created on August 28, 2006, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.StateTable;
import org.pmedv.blackboard.spice.sim.spice.SwitchState;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.StateTable.StateVector;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;

import javolution.util.StandardLog;
import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class VSwitchInstance extends VSwitchModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Current conductance */
    protected double conduct;

    /* Default on conductance = 1 mho */
    protected double ON_CONDUCTANCE = 1.0 / env.getGMin();

    /* Default off conductance */
    protected double OFF_CONDUCTANCE = env.getGMin() - 1;

    /* Switch "on" conductance  */
    protected double onConduct = ON_CONDUCTANCE;

    /* Switch "off" conductance  */
    protected double offConduct = OFF_CONDUCTANCE;

    private StateVector swStates1;
    private StateVector swStates2;
    private Complex posPosNode;
    private Complex negPosNode;
    private Complex posNegNode;
    private Complex negNegNode;

    //    double[] nVar = new double[NDATA.STATVARS.ordinal()];

    /** Creates a new instance of sw */
    public VSwitchInstance() {
    }

    public boolean acLoad(Mode mode) {
        int current_state = (int) swStates1.get(0);
        double gNow = current_state != 0 ? (onConduct) : (offConduct);
        posPosNode.realPlusEq(gNow);
        posNegNode.realMinusEq(gNow);
        negPosNode.realMinusEq(gNow);
        negNegNode.realPlusEq(gNow);
        return true;
    }

    public boolean load(Mode mode) {
        boolean converged = true;
        double gNow;
        double currentState = -1;
        double oldCurrentState = swStates1.get(0);
        double previousState = swStates1.get(1);
        double vCtrl = wrk.getRhsOldAt(posCtrlIndex).getReal() - wrk.getRhsOldAt(negCtrlIndex).getReal();
        if (mode.contains(MODE.INIT_FIX, MODE.INIT_JCT)) {
            if (icStateOn) {
                if ((vHysteresis >= 0) && (vCtrl > (vThreshold + vHysteresis))) {
                    currentState = SwitchState.REALLY_ON.ordinal();
                } else if ((vHysteresis < 0) && (vCtrl > (vThreshold - vHysteresis))) {
                    currentState = SwitchState.REALLY_ON.ordinal();
                } else {
                    currentState = SwitchState.HYST_ON.ordinal();
                }
            } else {
                if ((vHysteresis >= 0) && (vCtrl < (vThreshold - vHysteresis))) {
                    currentState = SwitchState.REALLY_OFF.ordinal();
                } else if ((vHysteresis < 0) && (vCtrl < (vThreshold + vHysteresis))) {
                    currentState = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentState = SwitchState.HYST_OFF.ordinal();
                }
            }
        } else if (mode.contains(MODE.INIT_SMSIG)) {
            currentState = previousState;
        } else if (mode.contains(MODE.INIT_FLOAT)) {
            if (vHysteresis > 0) {
                if (vCtrl > (vThreshold + vHysteresis)) {
                    currentState = SwitchState.REALLY_ON.ordinal();
                } else if (vCtrl < (vThreshold - vHysteresis)) {
                    currentState = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentState = oldCurrentState;
                }
            } else {
                if (vCtrl > (vThreshold - vHysteresis)) {
                    currentState = SwitchState.REALLY_ON.ordinal();
                } else if (vCtrl < (vThreshold + vHysteresis)) {
                    currentState = SwitchState.REALLY_OFF.ordinal();
                } else {
                    if ((previousState == SwitchState.HYST_OFF.ordinal())
                            || (previousState == SwitchState.HYST_ON.ordinal())) {
                        currentState = previousState;
                    } else if (previousState == SwitchState.REALLY_ON.ordinal()) {
                        currentState = SwitchState.HYST_OFF.ordinal();
                    } else if (previousState == SwitchState.REALLY_OFF.ordinal()) {
                        currentState = SwitchState.HYST_ON.ordinal();
                    } else {
                        StandardLog.warning("Bad value for previous state in swload");
                    }
                }
            }
            if (currentState != oldCurrentState) {
                converged = false;
            }
        } else if (mode.contains(MODE.INIT_TRAN, MODE.INIT_PRED)) {
            if (vHysteresis > 0) {
                if (vCtrl > (vThreshold + vHysteresis)) {
                    currentState = SwitchState.REALLY_ON.ordinal();
                } else if (vCtrl < (vThreshold - vHysteresis)) {
                    currentState = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentState = previousState;
                }
            } else {
                if (vCtrl > vThreshold - vHysteresis) {
                    currentState = SwitchState.REALLY_ON.ordinal();
                } else if (vCtrl < vThreshold + vHysteresis) {
                    currentState = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentState = 0.0;
                    if ((previousState == SwitchState.HYST_ON.ordinal())
                            || (previousState == SwitchState.HYST_OFF.ordinal())) {
                        currentState = previousState;
                    } else if (previousState == SwitchState.REALLY_ON.ordinal()) {
                        currentState = SwitchState.REALLY_OFF.ordinal();
                    } else if (previousState == SwitchState.REALLY_OFF.ordinal()) {
                        currentState = SwitchState.REALLY_ON.ordinal();
                    }
                }
            }
        }
        swStates1.set(0, currentState);
        if ((currentState == SwitchState.REALLY_ON.ordinal()) || (currentState == SwitchState.HYST_ON.ordinal())) {
            gNow = onConduct;
        } else {
            gNow = offConduct;
        }
        conduct = gNow;
        posPosNode.realPlusEq(gNow);
        posNegNode.realMinusEq(gNow);
        negPosNode.realMinusEq(gNow);
        negNegNode.realPlusEq(gNow);
        return converged;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        if (offResist == 0) {
            onResist = 1.0 / ON_CONDUCTANCE;
        }

        if (offResist == 0) {
            offResist = 1.0 / OFF_CONDUCTANCE;
        }
        swStates1 = stateTable.createRow();
        swStates2 = stateTable.createRow();
        posPosNode = wrk.aquireNode(posIndex, posIndex);
        posNegNode = wrk.aquireNode(posIndex, negIndex);
        negPosNode = wrk.aquireNode(negIndex, posIndex);
        negNegNode = wrk.aquireNode(negIndex, negIndex);
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        double maxChange, maxStep, ref;
        double lastChange = swStates1.get(1) - swStates2.get(1);
        if (swStates1.get(0) == 0) {
            ref = vThreshold + vHysteresis;
            if (swStates1.get(1) < ref && lastChange > 0) {
                maxChange = (ref - swStates1.get(1)) * 0.75 + 0.05;
                maxStep = maxChange / lastChange * tmprl.getOldDeltaAt(0);
                if (timeStep > maxStep) {
                    timeStep = maxStep;
                }
            }
        } else {
            ref = vThreshold - vHysteresis;
            if (swStates1.get(1) > ref && lastChange < 0) {
                maxChange = (ref - swStates1.get(1)) * 0.75 - 0.05;
                maxStep = maxChange / lastChange * tmprl.getOldDeltaAt(0);
                if (timeStep > maxStep) {
                    timeStep = maxStep;
                }
            }
        }
        return timeStep;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean unSetup() {
        return true;
    }

    public boolean temperature() {
        return true;
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}
