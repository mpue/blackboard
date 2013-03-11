/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.bjt;

/**
 *
 * @author Kristopher T. Beck
 */
public class BJT2ModelValues extends BJTModelValues {

    protected int subs;
    protected double subSatCur = 1e-16;
    protected double capSub;

    public double getCapSub() {
        return capSub;
    }

    public void setCapSub(double capSub) {
        this.capSub = capSub;
    }

    public double getSubSatCur() {
        return subSatCur;
    }

    public void setSubSatCur(double subSatCur) {
        this.subSatCur = subSatCur;
    }

    public int getSubs() {
        return subs;
    }

    public void setSubs(int subs) {
        this.subs = subs;
    }
    protected double reTempCoeff1;
    protected double reTempCoeff2;
    protected double rcTempCoeff1;
    protected double rcTempCoeff2;
    protected double rbTempCoeff1;
    protected double rbTempCoeff2;
    protected double rbmTempCoeff1;
    protected double rbmTempCoeff2;

    public BJT2ModelValues() {
        satCur = 1e-16;
        betaF = 100;
        emissionCoeffF = 1;
        leakBEemissionCoeff = 1.5;
        betaR = 1;
        emissionCoeffR = 1;
        leakBCemissionCoeff = 2;
        potentialBE = .75;
        junctionExpBE = .33;
        potentialBC = .75;
        junctionExpBC = .33;
        baseFractionBCcap = 1;
        potentialSubstrate = .75;
        energyGap = 1.11;
        tempExpIS = 3;
    }

    public double getRbTempCoeff1() {
        return rbTempCoeff1;
    }

    public void setRbTempCoeff1(double rbTempCoeff1) {
        this.rbTempCoeff1 = rbTempCoeff1;
    }

    public double getRbTempCoeff2() {
        return rbTempCoeff2;
    }

    public void setRbTempCoeff2(double rbTempCoeff2) {
        this.rbTempCoeff2 = rbTempCoeff2;
    }

    public double getRbmTempCoeff1() {
        return rbmTempCoeff1;
    }

    public void setRbmTempCoeff1(double rbmTempCoeff1) {
        this.rbmTempCoeff1 = rbmTempCoeff1;
    }

    public double getRbmTempCoeff2() {
        return rbmTempCoeff2;
    }

    public void setRbmTempCoeff2(double rbmTempCoeff2) {
        this.rbmTempCoeff2 = rbmTempCoeff2;
    }

    public double getRcTempCoeff1() {
        return rcTempCoeff1;
    }

    public void setRcTempCoeff1(double rcTempCoeff1) {
        this.rcTempCoeff1 = rcTempCoeff1;
    }

    public double getRcTempCoeff2() {
        return rcTempCoeff2;
    }

    public void setRcTempCoeff2(double rcTempCoeff2) {
        this.rcTempCoeff2 = rcTempCoeff2;
    }

    public double getReTempCoeff1() {
        return reTempCoeff1;
    }

    public void setReTempCoeff1(double reTempCoeff1) {
        this.reTempCoeff1 = reTempCoeff1;
    }

    public double getReTempCoeff2() {
        return reTempCoeff2;
    }

    public void setReTempCoeff2(double reTempCoeff2) {
        this.reTempCoeff2 = reTempCoeff2;
    }
}
