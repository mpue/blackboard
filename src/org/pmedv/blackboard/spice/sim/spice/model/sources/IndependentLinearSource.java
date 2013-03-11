/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources;

import org.pmedv.blackboard.spice.sim.spice.Mode;

/**
 *
 * @author owenbad
 */
public abstract class IndependentLinearSource extends SpiceSource {

    /* DC and TRANSIENT value of source */
    protected double dcValue;
    protected double acPhase;
    protected double acMag = 1;
    protected double acReal;
    protected double acImag;
    protected double dF1Mag;
    protected double dF2Mag;
    protected double dF1Phase;
    protected double dF2Phase;
    protected boolean initialized;

    public double getAcImag() {
        return acImag;
    }

    public void setAcImag(double acImag) {
        this.acImag = acImag;
    }

    public double getAcMag() {
        return acMag;
    }

    public void setAcMag(double acMag) {
        this.acMag = acMag;
    }

    public double getAcPhase() {
        return acPhase;
    }

    public void setAcPhase(double acPhase) {
        this.acPhase = acPhase;
    }

    public double getAcReal() {
        return acReal;
    }

    public void setAcReal(double acReal) {
        this.acReal = acReal;
    }

    public double getdF1Mag() {
        return dF1Mag;
    }

    public void setdF1Mag(double dF1Mag) {
        this.dF1Mag = dF1Mag;
    }

    public double getdF1Phase() {
        return dF1Phase;
    }

    public void setdF1Phase(double dF1Phase) {
        this.dF1Phase = dF1Phase;
    }

    public double getdF2Mag() {
        return dF2Mag;
    }

    public void setdF2Mag(double dF2Mag) {
        this.dF2Mag = dF2Mag;
    }

    public double getdF2Phase() {
        return dF2Phase;
    }

    public void setdF2Phase(double dF2Phase) {
        this.dF2Phase = dF2Phase;
    }

    public double getDcValue() {
        return dcValue;
    }

    public void setDcValue(double dcValue) {
        this.dcValue = dcValue;
    }

    protected double computeValue(Mode mode) {
        return dcValue;
    }

    protected void initParams() {
        initialized = true;
    }
}
