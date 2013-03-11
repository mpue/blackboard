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
public class VSwitchModelValues extends VSwitchValues implements Model {

    /* Switch "on" resistance */
    protected double onResist;

    /* Switch "off" resistance */
    protected double offResist;

    /* Switching threshold voltage */
    protected double vThreshold;

    /* Switching hysteresis voltage */
    protected double vHysteresis;

    public double getOffResist() {
        return offResist;
    }

    public void setOffResist(double offResist) {
        this.offResist = offResist;
    }

    public double getOnResist() {
        return onResist;
    }

    public void setOnResist(double onResist) {
        this.onResist = onResist;
    }

    public double getvHysteresis() {
        return vHysteresis;
    }

    public void setvHysteresis(double vHysteresis) {
        this.vHysteresis = vHysteresis;
    }

    public double getvThreshold() {
        return vThreshold;
    }

    public void setvThreshold(double vThreshold) {
        this.vThreshold = vThreshold;
    }
}
