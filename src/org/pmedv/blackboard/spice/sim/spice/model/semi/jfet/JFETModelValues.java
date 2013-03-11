/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.jfet;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author owenbad
 */
public abstract class JFETModelValues extends JFETValues implements Model {

    protected int type = N_TYPE;
    protected double tnom;
    protected double beta = 1e-4;

    /* G-S junction capacitance*/
    protected double capGS;

    /* G-D junction capacitance*/
    protected double capGD;

    /* Channel length modulation */
    protected double lambda;

    /* Source resistance*/
    protected double srcResist;

    /* Drain resistance*/
    protected double drnResist;

    /* Gate junction potential*/
    protected double phi = 1;

    /* Threshold voltage*/
    protected double vto = -2;

    /* Gate junction saturation current */
    protected double gateSatCurrent = 1e-14;

    /* Forward bias junction fit parameter */
    protected double depCapCoeff = 0.5;

    /*Flicker Noise Exponent*/
    protected double fNexp = 1;

    /*Flicker Noise Coefficient*/
    protected double fNcoef = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
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

    public double getDepCapCoeff() {
        return depCapCoeff;
    }

    public void setDepCapCoeff(double depCapCoeff) {
        this.depCapCoeff = depCapCoeff;
    }

    public double getDrnResist() {
        return drnResist;
    }

    public void setDrnResist(double drnResist) {
        this.drnResist = drnResist;
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

    public double getGateSatCurrent() {
        return gateSatCurrent;
    }

    public void setGateSatCurrent(double gateSatCurrent) {
        this.gateSatCurrent = gateSatCurrent;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public double getSrcResist() {
        return srcResist;
    }

    public void setSrcResist(double srcResist) {
        this.srcResist = srcResist;
    }

    public double getTnom() {
        return tnom;
    }

    public void setTnom(double tnom) {
        this.tnom = tnom;
    }

    public double getVto() {
        return vto;
    }

    public void setVto(double vto) {
        this.vto = vto;
    }
}
