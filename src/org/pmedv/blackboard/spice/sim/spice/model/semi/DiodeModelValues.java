/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class DiodeModelValues extends DiodeValues implements Model {

    /* Voltage at reverse breakdown */
    protected double breakdownVoltage;

    /* Current at above voltage */
    protected double breakdownCurrent = 1e-3;

    /* Forward Knee current */
    protected double forwardKneeCurrent = 1e-3;

    /* Reverse Knee current */
    protected double reverseKneeCurrent = 1e-3;

    /* Saturation current */
    protected double satCur = 1e-14;

    /* Sidewall saturation current */
    protected double satSWCur;

    /* Emission coefficient (N) */
    protected double emissionCoeff = 1;

    /* Ohmic series resistance */
    protected double resist;

    /* Junction Capacitance (Cj0) */
    protected double junctionCap;

    /* Sidewall Junction Capacitance (CJSW) */
    protected double junctionSWCap;

    /* Depletion Cap fraction coefficient */
    protected double depletionCapCoeff = 0.5;

    /* Depletion sidewall Cap fraction coefficient */
    protected double depletionSWcapCoeff = 0.5;

    /* Junction Potential  */
    protected double junctionPot = 1;

    /* Grading coefficient */
    protected double gradingCoeff = 0.5;

    /* Sidewall Junction Potential */
    protected double junctionSWPot = 1;

    /* Sidewall grading coefficient */
    protected double gradingSWCoeff = 0.33;

    /* Transit time  */
    protected double transitTime;

    /* Activation energy */
    protected double activationEnergy = 1.11;

    /* Grading coefficient 1st order temp. coeff.*/
    protected double gradCoeffTemp1;

    /* Grading coefficient 2nd order temp. coeff.*/
    protected double gradCoeffTemp2;

    /* Nominal temperature */
    protected double tnom;

    /* Series resistance 1st order temp. coeff. */
    protected double resistTemp1;

    /* Series resistance 2nd order temp. coeff. */
    protected double resistTemp2;

    /* Transit time 1st order coefficient */
    protected double tranTimeTemp1;

    /* Transit time 2nd order coefficient */
    protected double tranTimeTemp2;

    /* Temperature corrected saturation current */
    protected double tSatCur = 1e-14;
    protected double fNcoef;
    protected double fNexp = 1;

    public double getActivationEnergy() {
        return activationEnergy;
    }

    public void setActivationEnergy(double activationEnergy) {
        this.activationEnergy = activationEnergy;
    }

    public double getBreakdownCurrent() {
        return breakdownCurrent;
    }

    public void setBreakdownCurrent(double breakdownCurrent) {
        this.breakdownCurrent = breakdownCurrent;
    }

    public double getBreakdownVoltage() {
        return breakdownVoltage;
    }

    public void setBreakdownVoltage(double breakdownVoltage) {
        this.breakdownVoltage = breakdownVoltage;
    }

    public double getDepletionCapCoeff() {
        return depletionCapCoeff;
    }

    public void setDepletionCapCoeff(double depletionCapCoeff) {
        this.depletionCapCoeff = depletionCapCoeff;
    }

    public double getDepletionSWcapCoeff() {
        return depletionSWcapCoeff;
    }

    public void setDepletionSWcapCoeff(double depletionSWcapCoeff) {
        this.depletionSWcapCoeff = depletionSWcapCoeff;
    }

    public double getEmissionCoeff() {
        return emissionCoeff;
    }

    public void setEmissionCoeff(double emissionCoeff) {
        this.emissionCoeff = emissionCoeff;
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

    public double getForwardKneeCurrent() {
        return forwardKneeCurrent;
    }

    public void setForwardKneeCurrent(double forwardKneeCurrent) {
        this.forwardKneeCurrent = forwardKneeCurrent;
    }

    public double getGradCoeffTemp1() {
        return gradCoeffTemp1;
    }

    public void setGradCoeffTemp1(double gradCoeffTemp1) {
        this.gradCoeffTemp1 = gradCoeffTemp1;
    }

    public double getGradCoeffTemp2() {
        return gradCoeffTemp2;
    }

    public void setGradCoeffTemp2(double gradCoeffTemp2) {
        this.gradCoeffTemp2 = gradCoeffTemp2;
    }

    public double getGradingCoeff() {
        return gradingCoeff;
    }

    public void setGradingCoeff(double gradingCoeff) {
        this.gradingCoeff = gradingCoeff;
    }

    public double getGradingSWCoeff() {
        return gradingSWCoeff;
    }

    public void setGradingSWCoeff(double gradingSWCoeff) {
        this.gradingSWCoeff = gradingSWCoeff;
    }

    public double getJunctionCap() {
        return junctionCap;
    }

    public void setJunctionCap(double junctionCap) {
        this.junctionCap = junctionCap;
    }

    public double getJunctionPot() {
        return junctionPot;
    }

    public void setJunctionPot(double junctionPot) {
        this.junctionPot = junctionPot;
    }

    public double getJunctionSWCap() {
        return junctionSWCap;
    }

    public void setJunctionSWCap(double junctionSWCap) {
        this.junctionSWCap = junctionSWCap;
    }

    public double getJunctionSWPot() {
        return junctionSWPot;
    }

    public void setJunctionSWPot(double junctionSWPot) {
        this.junctionSWPot = junctionSWPot;
    }

    public double getNomTemp() {
        return tnom;
    }

    public void setNomTemp(double nomTemp) {
        this.tnom = nomTemp;
    }

    public double getResist() {
        return resist;
    }

    public void setResist(double resist) {
        this.resist = resist;
    }

    public double getResistTemp1() {
        return resistTemp1;
    }

    public void setResistTemp1(double resistTemp1) {
        this.resistTemp1 = resistTemp1;
    }

    public double getResistTemp2() {
        return resistTemp2;
    }

    public void setResistTemp2(double resistTemp2) {
        this.resistTemp2 = resistTemp2;
    }

    public double getReverseKneeCurrent() {
        return reverseKneeCurrent;
    }

    public void setReverseKneeCurrent(double reverseKneeCurrent) {
        this.reverseKneeCurrent = reverseKneeCurrent;
    }

    public double getSatCur() {
        return satCur;
    }

    public void setSatCur(double satCur) {
        this.satCur = satCur;
    }

    public double getSatSWCur() {
        return satSWCur;
    }

    public void setSatSWCur(double satSWCur) {
        this.satSWCur = satSWCur;
    }

    public double gettSatCur() {
        return tSatCur;
    }

    public void settSatCur(double tSatCur) {
        this.tSatCur = tSatCur;
    }

    public double getTranTimeTemp1() {
        return tranTimeTemp1;
    }

    public void setTranTimeTemp1(double tranTimeTemp1) {
        this.tranTimeTemp1 = tranTimeTemp1;
    }

    public double getTranTimeTemp2() {
        return tranTimeTemp2;
    }

    public void setTranTimeTemp2(double tranTimeTemp2) {
        this.tranTimeTemp2 = tranTimeTemp2;
    }

    public double getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(double transitTime) {
        this.transitTime = transitTime;
    }
}
