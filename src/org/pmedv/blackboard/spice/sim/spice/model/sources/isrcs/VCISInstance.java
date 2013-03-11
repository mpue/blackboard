/*
 * VCCSrs.java
 *
 * Created on April 8, 2006, 3:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentInstance;

import ktb.math.numbers.Complex;
/**
 *
 * @author Kristopher T. Beck
 */

public class VCISInstance extends VCISValues implements CurrentInstance {

    private Circuit ckt;
    private WorkEnv wrk;
    private Complex posContPosNode;
    private Complex posContNegNode;
    private Complex negContNegNode;
    private Complex negContPosNode;
    /**
     * Creates a new instance of VCCSrs
     */

    public VCISInstance() {
    }

    public boolean load(Mode mode) {
        posContPosNode.plusEq(coeff);
        negContNegNode.plusEq(coeff);
        posContNegNode.minusEq(coeff);
        negContPosNode.minusEq(coeff);
        return true;
    }

    public boolean pzLoad(Complex c) {
        posContPosNode.plusEq(coeff);
        negContNegNode.plusEq(coeff);
        posContNegNode.minusEq(coeff);
        negContPosNode.minusEq(coeff);
        return true;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        posContPosNode = wrk.aquireNode(posIndex, contPosIndex);
        posContNegNode = wrk.aquireNode(posIndex, contNegIndex);
        negContPosNode = wrk.aquireNode(negIndex, contPosIndex);
        negContNegNode = wrk.aquireNode(negIndex, contNegIndex);
        return true;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean unSetup() {
        return true;
    }

    public boolean acLoad(Mode mode) {
        return true;
    }

    public boolean temperature() {
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
