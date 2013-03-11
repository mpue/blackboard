/*
 * MOS2ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:23:43 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOS2ModelValues extends MOSModelValues {

    protected double fastSurfaceStateDensity;
    protected double narrowFactor;
    protected double critFieldExp;
    protected double maxDriftVel;
    protected double junctionDepth;
    protected double channelCharge = 1;
    protected double critField = 1e4;

    public MOS2ModelValues() {
        blkJctSideGradingCoeff = 0.33;
    }

    public double getChannelCharge() {
        return channelCharge;
    }

    public void setChannelCharge(double channelCharge) {
        this.channelCharge = channelCharge;
    }

    public double getCritField() {
        return critField;
    }

    public void setCritField(double critField) {
        this.critField = critField;
    }

    public double getCritFieldExp() {
        return critFieldExp;
    }

    public void setCritFieldExp(double critFieldExp) {
        this.critFieldExp = critFieldExp;
    }

    public double getFastSurfaceStateDensity() {
        return fastSurfaceStateDensity;
    }

    public void setFastSurfaceStateDensity(double fastSurfaceStateDensity) {
        this.fastSurfaceStateDensity = fastSurfaceStateDensity;
    }

    public double getJunctionDepth() {
        return junctionDepth;
    }

    public void setJunctionDepth(double junctionDepth) {
        this.junctionDepth = junctionDepth;
    }

    public double getMaxDriftVel() {
        return maxDriftVel;
    }

    public void setMaxDriftVel(double maxDriftVel) {
        this.maxDriftVel = maxDriftVel;
    }

    public double getNarrowFactor() {
        return narrowFactor;
    }

    public void setNarrowFactor(double narrowFactor) {
        this.narrowFactor = narrowFactor;
    }
}
