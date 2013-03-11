/*
istor.java
Created on April 6, 2006, 3:59 AM
To change this template, choose Tools | Template Manager
and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;

import javolution.util.StandardLog;
import ktb.math.numbers.Complex;

/**
 * @author Kristopher T. Beck
 */
public class ResInstance extends ResModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    protected double conduct;
    protected double current;
    protected double acConduct;//? rename impedance
    //    protected double [][] nVar = new double[NDATA.NSTATVARS.ordinal()][NSRCS];
    //protected double ppnVar;
    private Complex posPosNode;
    private Complex negNegNode;
    private Complex posNegNode;
    private Complex negPosNode;

    public ResInstance() {
    }

    @Override
    public void setAcResist(double acResist) {
        super.setAcResist(acResist);
        acConduct = 1 / acResist;
    }

    @Override
    public void setResist(double resist) {
        super.setResist(resist);
        conduct = 1 / resist;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        if (tNom == 0) {
            tNom = env.getNomTemp();
        }
        if (temp == 0) {
            temp = env.getTemp();
        }
        if (resist == 0) {
            if (sheetRes != 0 && sheetRes != 0 && length != 0) {
                resist = sheetRes * (length - shorter);
            } else {
                StandardLog.warning(getInstName() + ": resistance = 0 ohm, set to 1000 ohm");
                resist = 1000;
            }
        }
        posPosNode = wrk.aquireNode(posIndex, posIndex);
        negNegNode = wrk.aquireNode(negIndex, negIndex);
        posNegNode = wrk.aquireNode(posIndex, negIndex);
        negPosNode = wrk.aquireNode(negIndex, posIndex);
        return true;
    }

    public boolean unSetup() {
        return true;
    }

    public boolean load(Mode mode) {
        current = (wrk.getRhsOldRealAt(posIndex) - wrk.getRhsOldRealAt(negIndex)) * conduct;
        double difference = (temp + dTemp) - 300.15;
        double factor = 1.0 + tc1 * difference + tc2 * difference * difference;
        double _m = m / factor;
        posPosNode.plusEq(_m * conduct);
        negNegNode.plusEq(_m * conduct);
        posNegNode.minusEq(_m * conduct);
        negPosNode.minusEq(_m * conduct);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double _m;
        double difference = (temp + dTemp) - 300.15;
        double factor = 1.0 + tc1 * difference + tc2 * difference * difference;
        _m = this.m / factor;
        if (acResist != 0) {
            posPosNode.plusEq(_m * acConduct);
            negNegNode.plusEq(_m * acConduct);
            posNegNode.minusEq(_m * acConduct);
            negPosNode.minusEq(_m * acConduct);
        } else {
            posPosNode.plusEq(_m * conduct);
            negNegNode.plusEq(_m * conduct);
            posNegNode.minusEq(_m * conduct);
            negPosNode.minusEq(_m * conduct);
        }
        return true;
    }

    public boolean temperature() {
        double difference = (temp + dTemp) - env.getNomTemp();
        double factor = 1.0 + tc1 * difference
                + tc2 * difference * difference;
        conduct = 1.0 / (resist * factor * scale);
        if (acResist != 0) {
            acConduct = (1.0 / (acResist * factor * scale));
        } else {
            acConduct = conduct;
        }
        acResist = resist;
        return true;
    }

    public boolean accept(Mode mode) {
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
