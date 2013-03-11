/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class MutValues extends SpiceDevice {

    protected String ind1Name;

    protected String ind2Name;
    /* Mutual inductance input by user */
    protected double coupling;

    public double getCoupling() {
        return coupling;
    }

    public void setCoupling(double coupling) {
        this.coupling = coupling;
    }

    public String getInd1Name() {
        return ind1Name;
    }

    public void setInd1Name(String ind1Name) {
        this.ind1Name = ind1Name;
    }

    public String getInd2Name() {
        return ind2Name;
    }

    public void setInd2Name(String ind2Name) {
        this.ind2Name = ind2Name;
    }
}
