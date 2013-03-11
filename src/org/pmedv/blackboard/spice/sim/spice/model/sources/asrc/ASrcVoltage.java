/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.asrc;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentElement;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageElement;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageInstance;

import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class ASrcVoltage extends ASrcInstance implements VoltageInstance {

    @Override
    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        if (branchIndex == 0) {
            branchIndex = ckt.makeCurrentNode(getInstName() + "#branch").getIndex();
        }
        ((VasrcElem) elements.getFirst()).setFirst(true);
        return super.init(ckt);
    }

    public int findBranch(String name) {
        if (getInstName().equals(name)) {
            if (branchIndex == 0) {
                branchIndex = ckt.makeCurrentNode(getInstName() + "#branch").getIndex();
            }
            return branchIndex;
        }
        return 0;
    }

    public interface VasrcElem {

        void setFirst(boolean first);
    }

    public class CurInst extends CurrentElement implements VasrcElem {

        public CurInst() {
            super();
        }
        boolean vFirst;
        Complex posBrNode;
        Complex negBrNode;
        Complex brPosNode;
        Complex brNegNode;
        Complex brContNode;

        public boolean init() {
            if (vFirst) {
                posBrNode = wrk.aquireNode(posIndex, branchIndex);
                negBrNode = wrk.aquireNode(negIndex, branchIndex);
                brNegNode = wrk.aquireNode(branchIndex, negIndex);
                brPosNode = wrk.aquireNode(branchIndex, posIndex);
            }
            brContNode = wrk.aquireNode(branchIndex, contBranch);
            return true;
        }

        public boolean acload(double deriv) {
            if (vFirst) {
                posBrNode.realPlusEq(1.0);
                negBrNode.realMinusEq(1.0);
                brNegNode.realMinusEq(1.0);
                brPosNode.realPlusEq(1.0);
            }
            brContNode.realMinusEq(deriv);
            return true;
        }

        public boolean load(double deriv) {
            if (vFirst) {
                posBrNode.realPlusEq(1.0);
                negBrNode.realMinusEq(1.0);
                brNegNode.realMinusEq(1.0);
                brPosNode.realPlusEq(1.0);
            }
            brContNode.realMinusEq(deriv);
            return true;
        }

        public void setFirst(boolean first) {
            vFirst = first;
        }
    }

    public class VoltNode extends VoltageElement implements VasrcElem {

        public VoltNode() {
            super();
        }
        boolean vFirst;
        Complex posBrNode;
        Complex negBrNode;
        Complex brNegNode;
        Complex brPosNode;
        Complex brPtrNode;

        public boolean init() {
            if (vFirst) {
                posBrNode = wrk.aquireNode(posIndex, branchIndex);
                negBrNode = wrk.aquireNode(negIndex, branchIndex);
                brNegNode = wrk.aquireNode(branchIndex, negIndex);
                brPosNode = wrk.aquireNode(branchIndex, posIndex);
            }
            brPtrNode = wrk.aquireNode(branchIndex, node1Index);
            return true;
        }

        public boolean acload(double deriv) {
            if (vFirst) {
                posBrNode.realPlusEq(1.0);
                negBrNode.realMinusEq(1.0);
                brNegNode.realMinusEq(1.0);
                brPosNode.realPlusEq(1.0);
            }
            brPtrNode.realMinusEq(deriv);
            return true;
        }

        public boolean load(double deriv) {
            if (vFirst) {
                posBrNode.realPlusEq(1.0);
                negBrNode.realMinusEq(1.0);
                brNegNode.realMinusEq(1.0);
                brPosNode.realPlusEq(1.0);
            }
            brPtrNode.realMinusEq(deriv);
            return true;
        }

        public void setFirst(boolean first) {
            vFirst = first;
        }
    }
}
