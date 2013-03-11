/*
 * MOS9ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:24:56 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOS9ModelValues extends MOSModelValues {

    /* Length adjustment */
    protected double lengthAdjust;

    /* Effective width reduction */
    protected double widthNarrow;

    /* Width adjustment */
    protected double widthAdjust;

    /* vtO adjustment */
    protected double delvt0;
    protected double eta;
    protected double junctionDepth;
    protected double coeffDepLayWidth;
    protected double narrowFactor;
    protected double delta;
    protected double fastSurfaceStateDensity;
    protected double theta;
    protected double maxDriftVel;
    protected double alpha;
    protected double kappa = 0.2;

    public MOS9ModelValues() {
        blkJctSideGradingCoeff = 0.33;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getCoeffDepLayWidth() {
        return coeffDepLayWidth;
    }

    public void setCoeffDepLayWidth(double coeffDepLayWidth) {
        this.coeffDepLayWidth = coeffDepLayWidth;
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

    public double getNarrowFactor() {
        return narrowFactor;
    }

    public void setNarrowFactor(double narrowFactor) {
        this.narrowFactor = narrowFactor;
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
