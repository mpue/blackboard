/*
 * MOSModelValues.java
 * 
 * Created on Nov 13, 2007, 3:23:21 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOSModelValues extends MOSValues implements Model {

    /* Drain resistance */
    protected double drnResistance;

    /* Source resistance */
    protected double srcResistance;
    /* Device type : 1 = nmos,  -1 = pmos */
    protected int type = N_TYPE;
    protected double transconductance = 2e-5;
    protected double vt0;
    protected double phi = 0.6;
    protected double gamma;
    protected double lambda;
    protected double capBD;
    protected double capBS;
    protected double jctSatCur = 1e-14;
    protected double blkJctPotential = 0.8;
    protected double gateSrcOverlapCapFactor;
    protected double gateDrnOverlapCapFactor;
    protected double gateBlkOverlapCapFactor;
    protected double blkCapFactor;
    protected double sideWallCapFactor;
    protected double blkJctBotGradingCoeff = 0.5;
    protected double blkJctSideGradingCoeff = 0.33;
    protected double jctSatCurDensity;
    protected double oxideThickness = 1e-7;
    protected double latDiff;
    protected double sheetResistance;
    protected double surfaceMobility = 600;
    protected double fwdCapDepCoeff = 0.5;
    protected double substrateDoping;
    protected double surfaceStateDensity;
    protected int gateType = 1;
    protected double tnom;
    protected double fNcoef;
    protected double fNexp = 1;

    public double getBlkCapFactor() {
        return blkCapFactor;
    }

    public void setBlkCapFactor(double blkCapFactor) {
        this.blkCapFactor = blkCapFactor;
    }

    public double getBlkJctBotGradingCoeff() {
        return blkJctBotGradingCoeff;
    }

    public void setBlkJctBotGradingCoeff(double blkJctBotGradingCoeff) {
        this.blkJctBotGradingCoeff = blkJctBotGradingCoeff;
    }

    public double getBlkJctPotential() {
        return blkJctPotential;
    }

    public void setBlkJctPotential(double blkJctPotential) {
        this.blkJctPotential = blkJctPotential;
    }

    public double getBlkJctSideGradingCoeff() {
        return blkJctSideGradingCoeff;
    }

    public void setBlkJctSideGradingCoeff(double blkJctSideGradingCoeff) {
        this.blkJctSideGradingCoeff = blkJctSideGradingCoeff;
    }

    public double getCapBD() {
        return capBD;
    }

    public void setCapBD(double capBD) {
        this.capBD = capBD;
    }

    public double getCapBS() {
        return capBS;
    }

    public void setCapBS(double capBS) {
        this.capBS = capBS;
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

    public double getFwdCapDepCoeff() {
        return fwdCapDepCoeff;
    }

    public void setFwdCapDepCoeff(double fwdCapDepCoeff) {
        this.fwdCapDepCoeff = fwdCapDepCoeff;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getGateBlkOverlapCapFactor() {
        return gateBlkOverlapCapFactor;
    }

    public void setGateBlkOverlapCapFactor(double gateBlkOverlapCapFactor) {
        this.gateBlkOverlapCapFactor = gateBlkOverlapCapFactor;
    }

    public double getGateDrnOverlapCapFactor() {
        return gateDrnOverlapCapFactor;
    }

    public void setGateDrnOverlapCapFactor(double gateDrnOverlapCapFactor) {
        this.gateDrnOverlapCapFactor = gateDrnOverlapCapFactor;
    }

    public double getGateSrcOverlapCapFactor() {
        return gateSrcOverlapCapFactor;
    }

    public void setGateSrcOverlapCapFactor(double gateSrcOverlapCapFactor) {
        this.gateSrcOverlapCapFactor = gateSrcOverlapCapFactor;
    }

    public int getGateType() {
        return gateType;
    }

    public void setGateType(int gateType) {
        this.gateType = gateType;
    }

    public double getJctSatCur() {
        return jctSatCur;
    }

    public void setJctSatCur(double jctSatCur) {
        this.jctSatCur = jctSatCur;
    }

    public double getJctSatCurDensity() {
        return jctSatCurDensity;
    }

    public void setJctSatCurDensity(double jctSatCurDensity) {
        this.jctSatCurDensity = jctSatCurDensity;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getLatDiff() {
        return latDiff;
    }

    public void setLatDiff(double latDiff) {
        this.latDiff = latDiff;
    }

    public double getOxideThickness() {
        return oxideThickness;
    }

    public void setOxideThickness(double oxideThickness) {
        this.oxideThickness = oxideThickness;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public double getSheetResistance() {
        return sheetResistance;
    }

    public void setSheetResistance(double sheetResistance) {
        this.sheetResistance = sheetResistance;
    }

    public double getSideWallCapFactor() {
        return sideWallCapFactor;
    }

    public void setSideWallCapFactor(double sideWallCapFactor) {
        this.sideWallCapFactor = sideWallCapFactor;
    }

    public double getSubstrateDoping() {
        return substrateDoping;
    }

    public void setSubstrateDoping(double substrateDoping) {
        this.substrateDoping = substrateDoping;
    }

    public double getSurfaceMobility() {
        return surfaceMobility;
    }

    public void setSurfaceMobility(double surfaceMobility) {
        this.surfaceMobility = surfaceMobility;
    }

    public double getSurfaceStateDensity() {
        return surfaceStateDensity;
    }

    public void setSurfaceStateDensity(double surfaceStateDensity) {
        this.surfaceStateDensity = surfaceStateDensity;
    }

    public double getTnom() {
        return tnom;
    }

    public void setTnom(double tnom) {
        this.tnom = tnom;
    }

    public double getTransconductance() {
        return transconductance;
    }

    public void setTransconductance(double transconductance) {
        this.transconductance = transconductance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getVt0() {
        return vt0;
    }

    public void setVt0(double vt0) {
        this.vt0 = vt0;
    }

    public double getDrnResistance() {
        return drnResistance;
    }

    public void setDrnResistance(double drnResistance) {
        this.drnResistance = drnResistance;
    }

    public double getSrcResistance() {
        return srcResistance;
    }

    public void setSrcResistance(double srcResistance) {
        this.srcResistance = srcResistance;
    }
}
