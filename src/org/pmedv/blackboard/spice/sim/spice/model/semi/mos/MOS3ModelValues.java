/*
 * MOS3ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:24:03 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOS3ModelValues extends MOSModelValues {

    /* Lengtth adjustment */
    protected double lengthAdjust;
    /* Reduced effective width */
    protected double widthNarrow;
    /* Width adjustment */
    protected double widthAdjust;
    /* Adjusted vtO */
    protected double delvt0;
    protected double eta;
    protected double delta;
    protected double fastSurfaceStateDensity;
    protected double theta;
    protected double maxDriftVel;
    protected double junctionDepth;
    protected double kappa = 0.2;

    public MOS3ModelValues() {
        blkJctSideGradingCoeff = 0.33;
        oxideThickness = 1e-7;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDelvt0() {
        return delvt0;
    }

    public void setDelvt0(double delvt0) {
        this.delvt0 = delvt0;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
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

    public double getKappa() {
        return kappa;
    }

    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    public double getLengthAdjust() {
        return lengthAdjust;
    }

    public void setLengthAdjust(double lengthAdjust) {
        this.lengthAdjust = lengthAdjust;
    }

    public double getMaxDriftVel() {
        return maxDriftVel;
    }

    public void setMaxDriftVel(double maxDriftVel) {
        this.maxDriftVel = maxDriftVel;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getWidthAdjust() {
        return widthAdjust;
    }

    public void setWidthAdjust(double widthAdjust) {
        this.widthAdjust = widthAdjust;
    }

    public double getWidthNarrow() {
        return widthNarrow;
    }

    public void setWidthNarrow(double widthNarrow) {
        this.widthNarrow = widthNarrow;
    }
}
