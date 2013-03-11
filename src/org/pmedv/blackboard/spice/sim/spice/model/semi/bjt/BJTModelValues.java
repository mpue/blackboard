/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.bjt;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class BJTModelValues extends BJTValues implements Model {

    protected int type = N_TYPE;
    protected double satCur = 1e-16;
    protected double betaF = 100;
    protected double betaR = 1;
    protected double emissionCoeffF = 1;
    protected double earlyVoltF;
    protected double rollOffF;
    protected double emissionCoeffR = 1;
    protected double earlyVoltR;
    protected double rollOffR;
    protected double leakBCcurrent;
    protected double leakBCemissionCoeff = 2;
    protected double leakBEcurrent;
    protected double leakBEemissionCoeff = 1.5;
    protected double baseResist;
    protected double baseCurrentHalfResist;
    protected double minBaseResist;
    protected double emitterResist;
    protected double collectorResist;
    protected double junctionExpBE = 0.33;
    protected double depletionCapBE;
    protected double potentialBE = 0.75;
    protected double transitTimeF;
    protected double transitTimeBiasCoeffF;
    protected double transitTimeFVBC;
    protected double transitTimeHighCurrentF;
    protected double excessPhase;
    protected double depletionCapBC;
    protected double potentialBC = 0.75;
    protected double junctionExpBC = 0.33;
    protected double baseFractionBCcap = 1;
    protected double transitTimeR;
    protected double capCS;
    protected double potentialSubstrate = 0.75;
    protected double exponentialSubstrate;
    protected double betaExp;
    protected double energyGap = 1.11;
    protected double tempExpIS = 3;
    protected double depletionCapCoeff;
    protected double fNcoef;
    protected double fNexp = 1;

    /* Nominal temperature */
    protected double tnom;

    public double getBaseCurrentHalfResist() {
        return baseCurrentHalfResist;
    }

    public void setBaseCurrentHalfResist(double baseCurrentHalfResist) {
        this.baseCurrentHalfResist = baseCurrentHalfResist;
    }

    public double getBaseFractionBCcap() {
        return baseFractionBCcap;
    }

    public void setBaseFractionBCcap(double baseFractionBCcap) {
        this.baseFractionBCcap = baseFractionBCcap;
    }

    public double getBaseResist() {
        return baseResist;
    }

    public void setBaseResist(double baseResist) {
        this.baseResist = baseResist;
    }

    public double getBetaExp() {
        return betaExp;
    }

    public void setBetaExp(double betaExp) {
        this.betaExp = betaExp;
    }

    public double getBetaF() {
        return betaF;
    }

    public void setBetaF(double betaF) {
        this.betaF = betaF;
    }

    public double getBetaR() {
        return betaR;
    }

    public void setBetaR(double betaR) {
        this.betaR = betaR;
    }

    public double getCapCS() {
        return capCS;
    }

    public void setCapCS(double capCS) {
        this.capCS = capCS;
    }

    public double getCollectorResist() {
        return collectorResist;
    }

    public void setCollectorResist(double collectorResist) {
        this.collectorResist = collectorResist;
    }

    public double getDepletionCapBC() {
        return depletionCapBC;
    }

    public void setDepletionCapBC(double depletionCapBC) {
        this.depletionCapBC = depletionCapBC;
    }

    public double getDepletionCapBE() {
        return depletionCapBE;
    }

    public void setDepletionCapBE(double depletionCapBE) {
        this.depletionCapBE = depletionCapBE;
    }

    public double getDepletionCapCoeff() {
        return depletionCapCoeff;
    }

    public void setDepletionCapCoeff(double depletionCapCoeff) {
        this.depletionCapCoeff = depletionCapCoeff;
    }

    public double getEarlyVoltF() {
        return earlyVoltF;
    }

    public void setEarlyVoltF(double earlyVoltF) {
        this.earlyVoltF = earlyVoltF;
    }

    public double getEarlyVoltR() {
        return earlyVoltR;
    }

    public void setEarlyVoltR(double earlyVoltR) {
        this.earlyVoltR = earlyVoltR;
    }

    public double getEmissionCoeffF() {
        return emissionCoeffF;
    }

    public void setEmissionCoeffF(double emissionCoeffF) {
        this.emissionCoeffF = emissionCoeffF;
    }

    public double getEmissionCoeffR() {
        return emissionCoeffR;
    }

    public void setEmissionCoeffR(double emissionCoeffR) {
        this.emissionCoeffR = emissionCoeffR;
    }

    public double getEmitterResist() {
        return emitterResist;
    }

    public void setEmitterResist(double emitterResist) {
        this.emitterResist = emitterResist;
    }

    public double getEnergyGap() {
        return energyGap;
    }

    public void setEnergyGap(double energyGap) {
        this.energyGap = energyGap;
    }

    public double getExcessPhase() {
        return excessPhase;
    }

    public void setExcessPhase(double excessPhase) {
        this.excessPhase = excessPhase;
    }

    public double getExponentialSubstrate() {
        return exponentialSubstrate;
    }

    public void setExponentialSubstrate(double exponentialSubstrate) {
        this.exponentialSubstrate = exponentialSubstrate;
    }

    public double getFNcoef() {
        return fNcoef;
    }

    public void setFNcoef(double fNcoef) {
        this.fNcoef = fNcoef;
    }

    public double getFNexp() {
        return fNexp;
    }

    public void setFNexp(double fNexp) {
        this.fNexp = fNexp;
    }

    public double getJunctionExpBC() {
        return junctionExpBC;
    }

    public void setJunctionExpBC(double junctionExpBC) {
        this.junctionExpBC = junctionExpBC;
    }

    public double getJunctionExpBE() {
        return junctionExpBE;
    }

    public void setJunctionExpBE(double junctionExpBE) {
        this.junctionExpBE = junctionExpBE;
    }

    public double getLeakBCcurrent() {
        return leakBCcurrent;
    }

    public void setLeakBCcurrent(double leakBCcurrent) {
        this.leakBCcurrent = leakBCcurrent;
    }

    public double getMinBaseResist() {
        return minBaseResist;
    }

    public void setMinBaseResist(double minBaseResist) {
        this.minBaseResist = minBaseResist;
    }

    public double getPotentialBC() {
        return potentialBC;
    }

    public void setPotentialBC(double potentialBC) {
        this.potentialBC = potentialBC;
    }

    public double getPotentialBE() {
        return potentialBE;
    }

    public void setPotentialBE(double potentialBE) {
        this.potentialBE = potentialBE;
    }

    public double getPotentialSubstrate() {
        return potentialSubstrate;
    }

    public void setPotentialSubstrate(double potentialSubstrate) {
        this.potentialSubstrate = potentialSubstrate;
    }

    public double getRollOffF() {
        return rollOffF;
    }

    public void setRollOffF(double rollOffF) {
        this.rollOffF = rollOffF;
    }

    public double getRollOffR() {
        return rollOffR;
    }

    public void setRollOffR(double rollOffR) {
        this.rollOffR = rollOffR;
    }

    public double getSatCur() {
        return satCur;
    }

    public void setSatCur(double satCur) {
        this.satCur = satCur;
    }

    public double getTempExpIS() {
        return tempExpIS;
    }

    public void setTempExpIS(double tempExpIS) {
        this.tempExpIS = tempExpIS;
    }

    public double getTnom() {
        return tnom;
    }

    public void setTnom(double tnom) {
        this.tnom = tnom;
    }

    public double getTransitTimeBiasCoeffF() {
        return transitTimeBiasCoeffF;
    }

    public void setTransitTimeBiasCoeffF(double transitTimeBiasCoeffF) {
        this.transitTimeBiasCoeffF = transitTimeBiasCoeffF;
    }

    public double getTransitTimeF() {
        return transitTimeF;
    }

    public void setTransitTimeF(double transitTimeF) {
        this.transitTimeF = transitTimeF;
    }

    public double getTransitTimeFVBC() {
        return transitTimeFVBC;
    }

    public void setTransitTimeFVBC(double transitTimeFVBC) {
        this.transitTimeFVBC = transitTimeFVBC;
    }

    public double getTransitTimeHighCurrentF() {
        return transitTimeHighCurrentF;
    }

    public void setTransitTimeHighCurrentF(double transitTimeHighCurrentF) {
        this.transitTimeHighCurrentF = transitTimeHighCurrentF;
    }

    public double getTransitTimeR() {
        return transitTimeR;
    }

    public void setTransitTimeR(double transitTimeR) {
        this.transitTimeR = transitTimeR;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLeakBCemissionCoeff() {
        return leakBCemissionCoeff;
    }

    public void setLeakBCemissionCoeff(double leakBCemissionCoeff) {
        this.leakBCemissionCoeff = leakBCemissionCoeff;
    }

    public double getLeakBEcurrent() {
        return leakBEcurrent;
    }

    public void setLeakBEcurrent(double leakBEcurrent) {
        this.leakBEcurrent = leakBEcurrent;
    }

    public double getLeakBEemissionCoeff() {
        return leakBEemissionCoeff;
    }

    public void setLeakBEemissionCoeff(double leakBEemissionCoeff) {
        this.leakBEemissionCoeff = leakBEemissionCoeff;
    }
}
