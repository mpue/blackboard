/*
 * icvs.java
 *
 * Created on September 24, 2006, 2:58 PM
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

import javolution.util.StandardLog;
import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class ICVSInstance extends ICVSValues implements VoltageInstance {

    private Circuit ckt;
    private WorkEnv wrk;
    protected int branchIndex;
    protected int contBranchIndex;

    private Complex posIbrNode;
    private Complex negIbrNode;
    private Complex ibrPosNode;
    private Complex ibrNegNode;
    private Complex ibrContBrNode;

    /** Creates a new instance of icvs */
    public ICVSInstance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        Node tmp;
        if (branchIndex == 0) {
            if ((tmp = ckt.makeCurrentNode(getInstName() + "#branch")) == null) {
                return false;
            }
            branchIndex = tmp.getIndex();
        }
        contBranchIndex = ckt.findBranch(contName);
        if (contBranchIndex == 0) {
            StandardLog.severe(getInstName() + ": unknown controlling source " + contName);
            return false;
        }
        posIbrNode = wrk.aquireNode(posIndex, branchIndex);
        negIbrNode = wrk.aquireNode(negIndex, branchIndex);
        ibrNegNode = wrk.aquireNode(branchIndex, negIndex);
        ibrPosNode = wrk.aquireNode(branchIndex, posIndex);
        ibrContBrNode = wrk.aquireNode(branchIndex, contBranchIndex);
        return true;
    }

    public boolean unSetup() {
        if (branchIndex != 0) {
            ckt.deleteNode(branchIndex);
            branchIndex = 0;
        }
        return true;
    }

    public int findBranch(String name) {
        Node tmp;
        if (getInstName().equals(name)) {
            if (branchIndex == 0) {
                if ((tmp = ckt.makeCurrentNode(getInstName() + "#branch")) == null) {
                    return 0;
                }
                branchIndex = tmp.getIndex();
            }
            return (branchIndex);
        }
        return 0;
    }

    public boolean load(Mode mode) {
        posIbrNode.realPlusEq(1.0);
        negIbrNode.realMinusEq(1.0);
        ibrPosNode.realPlusEq(1.0);
        ibrNegNode.realMinusEq(1.0);
        ibrContBrNode.realMinusEq(coeff);
        return true;
    }

    public boolean pzLoad(Complex s) {
        posIbrNode.realPlusEq(1.0);
        negIbrNode.realMinusEq(1.0);
        ibrPosNode.realPlusEq(1.0);
        ibrNegNode.realMinusEq(1.0);
        ibrContBrNode.realPlusEq(coeff);
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
