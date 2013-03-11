/*
 * MES1ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:22:29 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet;

/**
 *
 * @author Kristopher T. Beck
 */
public class MES1ModelValues extends MESModelValues {

    protected double b = 0.3;
    protected double capGS;
    protected double capGD;
    protected double gatePotential = 1;
    protected double gateSatCurrent = 1e-14;
    protected double depletionCapCoeff = 0.5;
    protected double depletionCap;
    protected double fNcoef;
    protected double fNexp = 1;

    public MES1ModelValues() {
        beta = 2.5e-3;
        vto = -2;
        alpha = 2;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getCapGD() {
        return capGD;
    }

    public void setCapGD(double capGD) {
        this.capGD = capGD;
    }

    public double getCapGS() {
        return capGS;
    }

    public void setCapGS(double capGS) {
        this.capGS = capGS;
    }

    public double getDepletionCap() {
        return depletionCap;
    }

    public void setDepletionCap(double depletionCap) {
        this.depletionCap = depletionCap;
    }

    public double getDepletionCapCoeff() {
        return depletionCapCoeff;
    }

    public void setDepletionCapCoeff(double depletionCapCoeff) {
        this.depletionCapCoeff = depletionCapCoeff;
    }

    public double getfNcoef() {
        return fNcoef;
    }

    public void setfNcoef(double fNcoef) {
        this.fNcoef = fNcoef;
    }

    public double getfNexp() {
        return fNexp;
    }

    public void setfNexp(double fNexp) {
        this.fNexp = fNexp;
    }

    public double getGatePotential() {
        return gatePotential;
    }

    public void setGatePotential(double gatePotential) {
        this.gatePotential = gatePotential;
    }

    public double getGateSatCurrent() {
        return gateSatCurrent;
    }

    public void setGateSatCurrent(double gateSatCurrent) {
        this.gateSatCurrent = gateSatCurrent;
    }
}
