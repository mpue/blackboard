/*
 * VSRC.java
 *
 * Created on May 10, 2006, 1:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.sources.IndependentLinearSource;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageInstance;

import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class VSrcInstance extends IndependentLinearSource implements VoltageInstance {

    protected Circuit ckt;
    protected WorkEnv wrk;
    private EnvVars env;
    /* Branch equation index */
    protected int branchIndex;
    protected Complex ibrIbrNode;
    protected Complex ibrPosNode;
    protected Complex ibrNegNode;
    protected Complex posIbrNode;
    protected Complex negIbrNode;

    /** Creates a new instance of VSRC */
    public VSrcInstance() {
    }
    private String names[] = {"V+", "V-"};

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        if (branchIndex == 0) {
            branchIndex = ckt.makeCurrentNode(instName
                    + "#branch").getIndex();
        }
        posIbrNode = wrk.aquireNode(posIndex, branchIndex);
        negIbrNode = wrk.aquireNode(negIndex, branchIndex);
        ibrNegNode = wrk.aquireNode(branchIndex, negIndex);
        ibrPosNode = wrk.aquireNode(branchIndex, posIndex);
        return true;
    }

    public boolean unSetup() {
        if (branchIndex != 0) {
            ckt.deleteNode(branchIndex);
            branchIndex = 0;
        }
        initialized = false;
        return true;
    }

    public boolean load(Mode mode) {
        double value;
        posIbrNode.plusEq(1.0);
        negIbrNode.minusEq(1.0);
        ibrPosNode.plusEq(1.0);
        ibrNegNode.minusEq(1.0);
        if (mode.contains(MODE.DCOP, MODE.DCTRANCURVE) && dcValue != 0) {
            if (env.isXspice()) {
                value = dcValue;
            } else {
                value = wrk.getSrcFact() * dcValue;
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
        wrk.getRhsAt(branchIndex).realPlusEq(value);
        return true;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean acLoad(Mode mode) {
        posIbrNode.plusEq(1.0);
        negIbrNode.minusEq(1.0);
        ibrPosNode.plusEq(1.0);
        ibrNegNode.minusEq(1.0);
        wrk.getRhsAt(branchIndex).realPlusEq(acReal);
        wrk.getRhsAt(branchIndex).realPlusEq(acImag);
        return true;
    }

    public int findBranch(String name) {
        Node tmp = null;
        if (getInstName().equals(name)) {
            if (branchIndex == 0) {
                tmp = ckt.makeCurrentNode(name + "#branch");
                branchIndex = tmp.getIndex();
            }
            return branchIndex;
        }
        return 0;
    }

    public boolean pzLoad(Complex c) {
        if (acMag == 0) {
            posIbrNode.realPlusEq(1.0);
            negIbrNode.realMinusEq(1.0);
            ibrPosNode.realMinusEq(1.0);
            ibrNegNode.realPlusEq(1.0);
        } else {
            posIbrNode.realPlusEq(1.0);
            negIbrNode.realMinusEq(1.0);
            ibrIbrNode.realPlusEq(1.0);
        }
        return true;
    }

    public boolean pzSetup(int state) {
        Node tmp;
        if (branchIndex == 0) {
            tmp = ckt.makeCurrentNode(instName + "#branch");
            if (tmp == null) {
                return false;
            }
            branchIndex = tmp.getIndex();
        }
        posIbrNode = wrk.aquireNode(posIndex, branchIndex);
        negIbrNode = wrk.aquireNode(negIndex, branchIndex);
        ibrNegNode = wrk.aquireNode(branchIndex, negIndex);
        ibrPosNode = wrk.aquireNode(branchIndex, posIndex);
        ibrIbrNode = wrk.aquireNode(branchIndex, branchIndex);
        return true;
    }

    public boolean temperature() {
        if (dcValue == 0) {
            /*? no DC value - either have a transient value, or none
            if (isFuncTGiven()) {
            Logger.getLogger("global").warning(getInstName()
            + ": no DC value, transient time 0 value used");
            } else {
            Logger.getLogger("global").warning(getInstName()
            + ": has no value, DC 0 assumed");
            }
             *
             */
        }
        double radians = acPhase * Math.PI / 180.0;
        acReal = acMag * Math.cos(radians);
        acImag = acMag * Math.sin(radians);
        return true;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
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
