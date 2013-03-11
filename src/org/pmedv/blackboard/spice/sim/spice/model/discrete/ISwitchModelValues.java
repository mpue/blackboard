/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISwitchModelValues extends ISwitchValues implements Model {

    /* Switch "on" resistance */
    protected double onResist;

    /* Switch "off" resistance */
    protected double offResist;

    /* Switching threshold current */
    protected double iThreshold;

    /* Switching hysteresis current */
    protected double iHysteresis;

    public double getOffResist() {
        return offResist;
    }

    public void setOffResist(double offResistance) {
        this.offResist = offResistance;
    }

    public double getOnResist() {
        return onResist;
    }

    public void setOnResist(double onResist) {
        this.onResist = onResist;
    }

    public double getIHysteresis() {
        return iHysteresis;
    }

    public void setIHysteresis(double iHysteresis) {
        this.iHysteresis = iHysteresis;
    }

    public double getIThreshold() {
        return iThreshold;
    }

    public void setIThreshold(double iThreshold) {
        this.iThreshold = iThreshold;
    }
}
