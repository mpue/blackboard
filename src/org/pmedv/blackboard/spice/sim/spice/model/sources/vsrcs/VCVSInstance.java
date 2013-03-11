/*
 * vcvs.java
 *
 * Created on September 24, 2006, 3:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageInstance;

import ktb.math.numbers.Complex;
/**
 *
 * @author Kristopher T. Beck
 */

public class VCVSInstance extends VCVSValues implements VoltageInstance {

    private Circuit ckt;
    private WorkEnv wrk;
    /* Branch equation index for voltage source */
    protected int branchIndex;

    private Complex posIbrNode;
    private Complex negIbrNode;
    private Complex ibrPosNode;
    private Complex ibrNegNode;
    private Complex ibrContPosNode;
    private Complex ibrContNegNode;

    /** Creates a new instance of vcvs */
    public VCVSInstance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        Node tmp;
        if (branchIndex == 0) {
            tmp = ckt.makeCurrentNode(getInstName() + "#branch");
            if (tmp == null) {
                return false;
            }
            branchIndex = tmp.getIndex();
        }
        posIbrNode = wrk.aquireNode(posIndex, branchIndex);
        negIbrNode = wrk.aquireNode(negIndex, branchIndex);
        ibrNegNode = wrk.aquireNode(branchIndex, negIndex);
        ibrPosNode = wrk.aquireNode(branchIndex, posIndex);
        ibrContPosNode = wrk.aquireNode(branchIndex, contPosIndex);
        ibrContNegNode = wrk.aquireNode(branchIndex, contNegIndex);
        return true;
    }

    public boolean unSetup() {
        if (branchIndex != 0) {
            ckt.deleteNode(branchIndex);
            branchIndex = 0;
        }
        return true;
    }

    public boolean load(Mode mode) {
        posIbrNode.realPlusEq(1.0);
        negIbrNode.realMinusEq(1.0);
        ibrPosNode.realPlusEq(1.0);
        ibrNegNode.realMinusEq(1.0);
        ibrContPosNode.realMinusEq(coeff);
        ibrContNegNode.realPlusEq(coeff);
        return true;
    }

    public int findBranch(String name) {
        Node tmp;
        if (getInstName().equals(name)) {
            if (branchIndex == 0) {
                tmp = ckt.makeCurrentNode(getInstName() + "#branch");
                if (tmp == null) {
                    return 0;
                }
                branchIndex = tmp.getIndex();
            }
            return (branchIndex);
        }
        return 0;
    }

    public boolean pzLoad(Complex s) {
        posIbrNode.realPlusEq(1.0);
        negIbrNode.realMinusEq(1.0);
        ibrPosNode.realPlusEq(1.0);
        ibrNegNode.realMinusEq(1.0);
        ibrContPosNode.realMinusEq(coeff);
        ibrContNegNode.realPlusEq(coeff);
        return true;
    }

        public boolean accept(Mode mode) {
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
