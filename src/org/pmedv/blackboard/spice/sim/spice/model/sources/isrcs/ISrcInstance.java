/*
 * ISRC.java
 *
 * Created on May 23, 2006, 3:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentInstance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.IndependentLinearSource;

import javolution.lang.MathLib;
import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISrcInstance extends IndependentLinearSource implements CurrentInstance {

    protected Circuit ckt;
    protected WorkEnv wrk;
    protected EnvVars env;

    /** Creates a new instance of ISRC */
    public ISrcInstance() {
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean load(Mode mode) {
        double value;
        if (mode.contains(MODE.DCOP, MODE.DCTRANCURVE)
                && dcValue != 0) {
            if (env.isXspice()) {
                value = dcValue;
            } else {
                value = dcValue * wrk.getSrcFact();
            }
        } else {
            if (!initialized) {
                initParams();
            }
            value = computeValue(mode);
        }
        if (env.isXspice()) {
            value *= wrk.getSrcFact();
            value *= wrk.getAnalogRampFactor();
        } else {
            if (mode.contains(MODE.TRANOP)) {
                value *= wrk.getSrcFact();
            }
        }
        wrk.getRhsAt(posIndex).realPlusEq(value);
        wrk.getRhsAt(negIndex).realPlusEq(value);
        return true;
    }

    public boolean acLoad(Mode mode) {
        wrk.getRhsAt(posIndex).realPlusEq(acReal);
        wrk.getRhsAt(negIndex).realMinusEq(acReal);
        wrk.getRhsAt(posIndex).imagPlusEq(acImag);
        wrk.getRhsAt(negIndex).imagMinusEq(acImag);
        return true;
    }

    public boolean temperature() {
        acReal = acMag * Math.cos(MathLib.toRadians(acPhase));
        acImag = acMag * Math.sin(MathLib.toRadians(acPhase));
        return true;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        if (acMag == 0) {
            acMag = 1;
        }
        if (dcValue == 0) {
            StandardLog.warning("Source " + getInstName()
                    + " has no DC value, transient time 0 value used");
        }
        /*else {
            StandardLog.warning("Source " + getInstName()
                    + " has no value, DC 0 assumed");
        }
         * *
         */
        return true;
    }

    public boolean unSetup() {
        initialized = false;
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return timeStep;
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}
