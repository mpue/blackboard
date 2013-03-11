/*
 * DefaultNode.java
 *
 * Created on June 9, 2006, 5:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.node;

import org.pmedv.blackboard.spice.sim.node.Node;

import ktb.math.numbers.Complex;
/**
 *
 * @author Kristopher T. Beck
 */

public abstract class SpiceNode extends Node<Complex> {

    protected Complex ic;
    protected double ns;
    protected boolean icGiven = false;
    protected boolean nsGiven = false;

    /** Creates a new instance of DefaultNode */
    public SpiceNode() {
    }

    public Complex getIC() {
        return ic;
    }

    public double getNS() {
        return ns;
    }

    public void setNSGiven(boolean nsGiven) {
        this.nsGiven = nsGiven;
    }

    public void setICGiven(boolean icGiven) {
        this.icGiven = icGiven;
    }

    public boolean isICGiven() {
        return icGiven;
    }

    public boolean isNSGiven() {
        return nsGiven;
    }

    public void setIC(Complex ic) {
        this.ic = ic;
    }

    public void setNS(double ns) {
        this.ns = ns;
    }
}
