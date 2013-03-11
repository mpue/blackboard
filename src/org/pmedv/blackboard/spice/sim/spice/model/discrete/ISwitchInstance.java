/*
 * csw.java
 *
 * Created on August 25, 2006, 2:27 AM
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
public class ISwitchInstance extends ISwitchModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Current conductance of switch */
    protected double conduct;

    /* Default on conductance = 1 mho */
    protected double ON_CONDUCTANCE = 1.0;

    /* Default off conductance */
    protected double OFF_CONDUCTANCE = env.getGMin();

    /* Switch "on" conductance  */
    protected double onConduct = ON_CONDUCTANCE;

    /* Switch "off" conductance  */
    protected double offConduct = OFF_CONDUCTANCE;

    private Complex posPosNode;
    private Complex posNegNode;
    private Complex negNegNode;
    private Complex negPosNode;

    private StateVector state1States;
    private StateVector state2States;
    // double[] nVar = new double[NDATA.STATVARS.ordinal()];

    /** Creates a new instance of csw */
    public ISwitchInstance() {
    }

    public boolean acLoad(Mode mode) {
        double gNow;
        int currentState;
        currentState = (int) state1States.get(0);
        gNow = currentState != 0 ? (onConduct) : (offConduct);
        posPosNode.realPlusEq(gNow);
        posNegNode.realMinusEq(gNow);
        negPosNode.realMinusEq(gNow);
        negNegNode.realPlusEq(gNow);
        return true;
    }

    public boolean load(Mode mode) {
        double gNow;
        double iCtrl;
        double previousState = -1;
        double currentCtate = -1;
        double oldCurrentState = -1;
        oldCurrentState = state1States.get(0);
        previousState = state1States.get(1);
        iCtrl = wrk.getRhsOldAt(contBranch).getReal();
        boolean converged = true;
        if (mode.contains(MODE.INIT_FIX, MODE.INIT_JCT)) {
            if (icStateOn) {
                if ((iHysteresis >= 0) && (iCtrl > (iThreshold + iHysteresis))) {
                    currentCtate = SwitchState.REALLY_ON.ordinal();
                } else if ((iHysteresis < 0) && (iCtrl > (iThreshold - iHysteresis))) {
                    currentCtate = SwitchState.REALLY_ON.ordinal();
                } else {
                    currentCtate = SwitchState.HYST_ON.ordinal();
                }
            } else {
                if ((iHysteresis >= 0) && (iCtrl < (iThreshold - iHysteresis))) {
                    currentCtate = SwitchState.REALLY_OFF.ordinal();
                } else if ((iHysteresis < 0) && (iCtrl < (iThreshold + iHysteresis))) {
                    currentCtate = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentCtate = SwitchState.HYST_OFF.ordinal();
                }
            }
        } else if (mode.contains(MODE.INIT_SMSIG)) {
            currentCtate = previousState;
        } else if (mode.contains(MODE.INIT_FLOAT)) {
            /* use state0 since INITTRAN or INITPRED already called */
            if (iHysteresis > 0) {
                if (iCtrl > (iThreshold + iHysteresis)) {
                    currentCtate = SwitchState.REALLY_ON.ordinal();
                } else if (iCtrl < (iThreshold - iHysteresis)) {
                    currentCtate = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentCtate = previousState;
                }
            } else {
                if (iCtrl > (iThreshold - iHysteresis)) {
                    currentCtate = SwitchState.REALLY_ON.ordinal();
                } else if (iCtrl < (iThreshold + iHysteresis)) {
                    currentCtate = SwitchState.REALLY_OFF.ordinal();
                } else {
                    /* in hysteresis... change value ifgoing from low to hysteresis, or from hi to hysteresis. */
                    /* ifprevious state was in hysteresis, then don't change the state.. */
                    if ((previousState == SwitchState.HYST_OFF.ordinal()) || (previousState == SwitchState.HYST_ON.ordinal())) {
                        currentCtate = previousState;
                    } else if (previousState == SwitchState.REALLY_ON.ordinal()) {
                        currentCtate = SwitchState.HYST_OFF.ordinal();
                    } else if (previousState == SwitchState.REALLY_OFF.ordinal()) {
                        currentCtate = SwitchState.HYST_ON.ordinal();
                    } else {
                        StandardLog.warning("bad value for previous region in swload");
                    }
                }
            }
            if (currentCtate != oldCurrentState) {
                converged = false;
            }
        } else if (mode.contains(MODE.INIT_TRAN, MODE.INIT_PRED)) {
            if (iHysteresis > 0) {
                if (iCtrl > (iThreshold + iHysteresis)) {
                    currentCtate = SwitchState.REALLY_ON.ordinal();
                } else if (iCtrl < (iThreshold - iHysteresis)) {
                    currentCtate = SwitchState.REALLY_OFF.ordinal();
                } else {
                    currentCtate = previousState;
                }
            } else {
                if (iCtrl > (iThreshold - iHysteresis)) {
                    currentCtate = SwitchState.REALLY_ON.ordinal();
                } else if (iCtrl < (iThreshold + iHysteresis)) {
                    currentCtate = SwitchState.REALLY_OFF.ordinal();
                } else {
                    /* in hysteresis... change value ifgoing from low to hysteresis, or from hi to hysteresis. */
                    /* ifprevious state was in hysteresis, then don't change the state.. */
                    if ((previousState == SwitchState.HYST_OFF.ordinal()) || (previousState == SwitchState.HYST_ON.ordinal())) {
                        currentCtate = previousState;
                    } else if (previousState == SwitchState.REALLY_ON.ordinal()) {
                        currentCtate = SwitchState.HYST_OFF.ordinal();
                    } else if (previousState == SwitchState.REALLY_OFF.ordinal()) {
                        currentCtate = SwitchState.HYST_ON.ordinal();
                    } else {
                        StandardLog.warning("bad value for previous region in cswload");
                    }
                }
            }
        }
        state1States.set(0, currentCtate);
        state2States.set(1, previousState);
        if ((currentCtate == SwitchState.REALLY_ON.ordinal()) || (currentCtate == SwitchState.HYST_ON.ordinal())) {
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

    public boolean pzLoad(Complex s) {
        double g_now;
        int current_state;
        current_state = (int) state1States.get(0);
        g_now = current_state != 0 ? (onConduct) : (offConduct);
        posPosNode.realPlusEq(g_now);
        posNegNode.realMinusEq(g_now);
        negPosNode.realMinusEq(g_now);
        negNegNode.realPlusEq(g_now);
        return true;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        state1States = stateTable.createRow();
        state2States = stateTable.createRow();
        onResist = 1.0 / onConduct;
        offResist = 1.0 / offConduct;
        contBranch = ckt.findBranch(contName);
        if (contBranch == 0) {
            StandardLog.warning(getInstName() + ": unknown controlling source " + contName);
            return false;
        }
        posPosNode = wrk.aquireNode(posIndex, posIndex);
        posNegNode = wrk.aquireNode(posIndex, negIndex);
        negPosNode = wrk.aquireNode(negIndex, posIndex);
        negNegNode = wrk.aquireNode(negIndex, negIndex);
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        double lastChange,
         maxChange,
         maxStep,
         ref;
        lastChange = state1States.get(1) -
                state2States.get(1);
        if (state1States.get(0) == 0) {
            ref = (iThreshold + iHysteresis);
            if (state1States.get(1) < ref && lastChange > 0) {
                maxChange = ref - state1States.get(1) *
                        0.75 + 0.00005;
                maxStep = maxChange / lastChange * tmprl.getOldDeltaAt(0);
                if (timeStep > maxStep) {
                    timeStep = maxStep;
                }
            }
        } else {
            ref = (iThreshold - iHysteresis);
            if (state1States.get(1) > ref && lastChange < 0) {
                maxChange = ref - state1States.get(1) *
                        0.75 - 0.00005;
                maxStep = maxChange / lastChange * tmprl.getOldDeltaAt(0);
                if (timeStep > maxStep) {
                    timeStep = maxStep;
                }
            }
        }
        return timeStep;
    }

    public boolean accept(Mode mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean unSetup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean temperature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean convTest(Mode mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean loadInitCond() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

