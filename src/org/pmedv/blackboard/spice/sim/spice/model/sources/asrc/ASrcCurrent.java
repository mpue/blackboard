/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.model.sources.asrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentElement;
import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentInstance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageElement;

import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class ASrcCurrent extends ASrcInstance implements CurrentInstance{

    public class CurInst extends CurrentElement implements ASRCElement{

        public CurInst() {
            super();
        }

        private Complex posContNode;
        private Complex negContNode;

        public boolean init() {
            posContNode = wrk.aquireNode(posIndex, contBranch);
            negContNode = wrk.aquireNode(negIndex, contBranch);
            return true;
        }

        public boolean acload(double deriv) {
            posContNode.realPlusEq(deriv);
            negContNode.realMinusEq(deriv);
            return true;
        }

        public boolean load(double deriv) {
            posContNode.realPlusEq(deriv);
            negContNode.realMinusEq(deriv);
            return true;
        }
    }

    public class VoltNode extends VoltageElement {

        public VoltNode() {
            super();
        }

        private Complex posPtrNode;
        private Complex negPtrNode;

        public boolean init() {
            posPtrNode = wrk.aquireNode(posIndex, node1Index);
            negPtrNode = wrk.aquireNode(negIndex, node1Index);
            return true;
        }

        public boolean acload(double deriv) {
            posPtrNode.realPlusEq(deriv);
            negPtrNode.realMinusEq(deriv);
            return true;
        }

        public boolean load(double deriv) {
            posPtrNode.realPlusEq(deriv);
            negPtrNode.realMinusEq(deriv);
            return true;
        }
    }
}
