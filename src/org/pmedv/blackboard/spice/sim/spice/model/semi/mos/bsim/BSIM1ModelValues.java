/*
 * BSIM1ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:16:38 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class BSIM1ModelValues extends BSIMValues implements Model {

    protected double vfb0;
    protected double vfbL;
    protected double vfbW;
    protected double phi0;
    protected double phiL;
    protected double phiW;
    protected double k10;
    protected double k1L;
    protected double k1W;
    protected double k20;
    protected double k2L;
    protected double k2W;
    protected double eta0;
    protected double etaL;
    protected double etaW;
    protected double etaB0;
    protected double etaBl;
    protected double etaBw;
    protected double etaD0;
    protected double etaDl;
    protected double etaDw;
    protected double deltaL;
    protected double deltaW;
    protected double mobZero;
    protected double mobZeroB0;
    protected double mobZeroBl;
    protected double mobZeroBw;
    protected double mobVdd0;
    protected double mobVddl;
    protected double mobVddw;
    protected double mobVddB0;
    protected double mobVddBl;
    protected double mobVddBw;
    protected double mobVddD0;
    protected double mobVddDl;
    protected double mobVddDw;
    protected double ugs0;
    protected double ugsL;
    protected double ugsW;
    protected double ugsB0;
    protected double ugsBL;
    protected double ugsBW;
    protected double uds0;
    protected double udsL;
    protected double udsW;
    protected double udsB0;
    protected double udsBL;
    protected double udsBW;
    protected double udsD0;
    protected double udsDL;
    protected double udsDW;
    protected double subthSlope0;
    protected double subthSlopeL;
    protected double subthSlopeW;
    protected double subthSlopeB0;
    protected double subthSlopeBL;
    protected double subthSlopeBW;
    protected double subthSlopeD0;
    protected double subthSlopeDL;
    protected double subthSlopeDW;
    protected double vdd;
    protected int channelChargePartitionFlag;
    protected double gateSrcOverlapCap;
    protected double gateDrnOverlapCap;
    protected double gateBlkOverlapCap;
    protected double sidewallJctPotential;
    protected double unitAreaJctCap;
    protected double unitLengthSidewallJctCap;
    protected double defaultWidth;
    protected double deltaLength;

    public BSIM1ModelValues() {
    }

    public int getChannelChargePartitionFlag() {
        return channelChargePartitionFlag;
    }

    public void setChannelChargePartitionFlag(int channelChargePartitionFlag) {
        this.channelChargePartitionFlag = channelChargePartitionFlag;
    }

    public double getDefaultWidth() {
        return defaultWidth;
    }

    public void setDefaultWidth(double defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public double getDeltaL() {
        return deltaL;
    }

    public void setDeltaL(double deltaL) {
        this.deltaL = deltaL;
    }

    public double getDeltaLength() {
        return deltaLength;
    }

    public void setDeltaLength(double deltaLength) {
        this.deltaLength = deltaLength;
    }

    public double getDeltaW() {
        return deltaW;
    }

    public void setDeltaW(double deltaW) {
        this.deltaW = deltaW;
    }

    public double getEta0() {
        return eta0;
    }

    public void setEta0(double eta0) {
        this.eta0 = eta0;
    }

    public double getEtaB0() {
        return etaB0;
    }

    public void setEtaB0(double etaB0) {
        this.etaB0 = etaB0;
    }

    public double getEtaBl() {
        return etaBl;
    }

    public void setEtaBl(double etaBl) {
        this.etaBl = etaBl;
    }

    public double getEtaBw() {
        return etaBw;
    }

    public void setEtaBw(double etaBw) {
        this.etaBw = etaBw;
    }

    public double getEtaD0() {
        return etaD0;
    }

    public void setEtaD0(double etaD0) {
        this.etaD0 = etaD0;
    }

    public double getEtaDl() {
        return etaDl;
    }

    public void setEtaDl(double etaDl) {
        this.etaDl = etaDl;
    }

    public double getEtaDw() {
        return etaDw;
    }

    public void setEtaDw(double etaDw) {
        this.etaDw = etaDw;
    }

    public double getEtaL() {
        return etaL;
    }

    public void setEtaL(double etaL) {
        this.etaL = etaL;
    }

    public double getEtaW() {
        return etaW;
    }

    public void setEtaW(double etaW) {
        this.etaW = etaW;
    }

    public double getGateBlkOverlapCap() {
        return gateBlkOverlapCap;
    }

    public void setGateBlkOverlapCap(double gateBlkOverlapCap) {
        this.gateBlkOverlapCap = gateBlkOverlapCap;
    }

    public double getGateDrnOverlapCap() {
        return gateDrnOverlapCap;
    }

    public void setGateDrnOverlapCap(double gateDrnOverlapCap) {
        this.gateDrnOverlapCap = gateDrnOverlapCap;
    }

    public double getGateSrcOverlapCap() {
        return gateSrcOverlapCap;
    }

    public void setGateSrcOverlapCap(double gateSrcOverlapCap) {
        this.gateSrcOverlapCap = gateSrcOverlapCap;
    }

    public double getK10() {
        return k10;
    }

    public void setK10(double k10) {
        this.k10 = k10;
    }

    public double getK1L() {
        return k1L;
    }

    public void setK1L(double k1L) {
        this.k1L = k1L;
    }

    public double getK1W() {
        return k1W;
    }

    public void setK1W(double k1W) {
        this.k1W = k1W;
    }

    public double getK20() {
        return k20;
    }

    public void setK20(double k20) {
        this.k20 = k20;
    }

    public double getK2L() {
        return k2L;
    }

    public void setK2L(double k2L) {
        this.k2L = k2L;
    }

    public double getK2W() {
        return k2W;
    }

    public void setK2W(double k2W) {
        this.k2W = k2W;
    }

    public double getMobVdd0() {
        return mobVdd0;
    }

    public void setMobVdd0(double mobVdd0) {
        this.mobVdd0 = mobVdd0;
    }

    public double getMobVddB0() {
        return mobVddB0;
    }

    public void setMobVddB0(double mobVddB0) {
        this.mobVddB0 = mobVddB0;
    }

    public double getMobVddBl() {
        return mobVddBl;
    }

    public void setMobVddBl(double mobVddBl) {
        this.mobVddBl = mobVddBl;
    }

    public double getMobVddBw() {
        return mobVddBw;
    }

    public void setMobVddBw(double mobVddBw) {
        this.mobVddBw = mobVddBw;
    }

    public double getMobVddD0() {
        return mobVddD0;
    }

    public void setMobVddD0(double mobVddD0) {
        this.mobVddD0 = mobVddD0;
    }

    public double getMobVddDl() {
        return mobVddDl;
    }

    public void setMobVddDl(double mobVddDl) {
        this.mobVddDl = mobVddDl;
    }

    public double getMobVddDw() {
        return mobVddDw;
    }

    public void setMobVddDw(double mobVddDw) {
        this.mobVddDw = mobVddDw;
    }

    public double getMobVddl() {
        return mobVddl;
    }

    public void setMobVddl(double mobVddl) {
        this.mobVddl = mobVddl;
    }

    public double getMobVddw() {
        return mobVddw;
    }

    public void setMobVddw(double mobVddw) {
        this.mobVddw = mobVddw;
    }

    public double getMobZero() {
        return mobZero;
    }

    public void setMobZero(double mobZero) {
        this.mobZero = mobZero;
    }

    public double getMobZeroB0() {
        return mobZeroB0;
    }

    public void setMobZeroB0(double mobZeroB0) {
        this.mobZeroB0 = mobZeroB0;
    }

    public double getMobZeroBl() {
        return mobZeroBl;
    }

    public void setMobZeroBl(double mobZeroBl) {
        this.mobZeroBl = mobZeroBl;
    }

    public double getMobZeroBw() {
        return mobZeroBw;
    }

    public void setMobZeroBw(double mobZeroBw) {
        this.mobZeroBw = mobZeroBw;
    }

    public double getPhi0() {
        return phi0;
    }

    public void setPhi0(double phi0) {
        this.phi0 = phi0;
    }

    public double getPhiL() {
        return phiL;
    }

    public void setPhiL(double phiL) {
        this.phiL = phiL;
    }

    public double getPhiW() {
        return phiW;
    }

    public void setPhiW(double phiW) {
        this.phiW = phiW;
    }

    public double getSidewallJctPotential() {
        return sidewallJctPotential;
    }

    public void setSidewallJctPotential(double sidewallJctPotential) {
        this.sidewallJctPotential = sidewallJctPotential;
    }

    public double getSubthSlope0() {
        return subthSlope0;
    }

    public void setSubthSlope0(double subthSlope0) {
        this.subthSlope0 = subthSlope0;
    }

    public double getSubthSlopeB0() {
        return subthSlopeB0;
    }

    public void setSubthSlopeB0(double subthSlopeB0) {
        this.subthSlopeB0 = subthSlopeB0;
    }

    public double getSubthSlopeBL() {
        return subthSlopeBL;
    }

    public void setSubthSlopeBL(double subthSlopeBL) {
        this.subthSlopeBL = subthSlopeBL;
    }

    public double getSubthSlopeBW() {
        return subthSlopeBW;
    }

    public void setSubthSlopeBW(double subthSlopeBW) {
        this.subthSlopeBW = subthSlopeBW;
    }

    public double getSubthSlopeD0() {
        return subthSlopeD0;
    }

    public void setSubthSlopeD0(double subthSlopeD0) {
        this.subthSlopeD0 = subthSlopeD0;
    }

    public double getSubthSlopeDL() {
        return subthSlopeDL;
    }

    public void setSubthSlopeDL(double subthSlopeDL) {
        this.subthSlopeDL = subthSlopeDL;
    }

    public double getSubthSlopeDW() {
        return subthSlopeDW;
    }

    public void setSubthSlopeDW(double subthSlopeDW) {
        this.subthSlopeDW = subthSlopeDW;
    }

    public double getSubthSlopeL() {
        return subthSlopeL;
    }

    public void setSubthSlopeL(double subthSlopeL) {
        this.subthSlopeL = subthSlopeL;
    }

    public double getSubthSlopeW() {
        return subthSlopeW;
    }

    public void setSubthSlopeW(double subthSlopeW) {
        this.subthSlopeW = subthSlopeW;
    }

    public double getUds0() {
        return uds0;
    }

    public void setUds0(double uds0) {
        this.uds0 = uds0;
    }

    public double getUdsB0() {
        return udsB0;
    }

    public void setUdsB0(double udsB0) {
        this.udsB0 = udsB0;
    }

    public double getUdsBL() {
        return udsBL;
    }

    public void setUdsBL(double udsBL) {
        this.udsBL = udsBL;
    }

    public double getUdsBW() {
        return udsBW;
    }

    public void setUdsBW(double udsBW) {
        this.udsBW = udsBW;
    }

    public double getUdsD0() {
        return udsD0;
    }

    public void setUdsD0(double udsD0) {
        this.udsD0 = udsD0;
    }

    public double getUdsDL() {
        return udsDL;
    }

    public void setUdsDL(double udsDL) {
        this.udsDL = udsDL;
    }

    public double getUdsDW() {
        return udsDW;
    }

    public void setUdsDW(double udsDW) {
        this.udsDW = udsDW;
    }

    public double getUdsL() {
        return udsL;
    }

    public void setUdsL(double udsL) {
        this.udsL = udsL;
    }

    public double getUdsW() {
        return udsW;
    }

    public void setUdsW(double udsW) {
        this.udsW = udsW;
    }

    public double getUgs0() {
        return ugs0;
    }

    public void setUgs0(double ugs0) {
        this.ugs0 = ugs0;
    }

    public double getUgsB0() {
        return ugsB0;
    }

    public void setUgsB0(double ugsB0) {
        this.ugsB0 = ugsB0;
    }

    public double getUgsBL() {
        return ugsBL;
    }

    public void setUgsBL(double ugsBL) {
        this.ugsBL = ugsBL;
    }

    public double getUgsBW() {
        return ugsBW;
    }

    public void setUgsBW(double ugsBW) {
        this.ugsBW = ugsBW;
    }

    public double getUgsL() {
        return ugsL;
    }

    public void setUgsL(double ugsL) {
        this.ugsL = ugsL;
    }

    public double getUgsW() {
        return ugsW;
    }

    public void setUgsW(double ugsW) {
        this.ugsW = ugsW;
    }

    public double getUnitAreaJctCap() {
        return unitAreaJctCap;
    }

    public void setUnitAreaJctCap(double unitAreaJctCap) {
        this.unitAreaJctCap = unitAreaJctCap;
    }

    public double getUnitLengthSidewallJctCap() {
        return unitLengthSidewallJctCap;
    }

    public void setUnitLengthSidewallJctCap(double unitLengthSidewallJctCap) {
        this.unitLengthSidewallJctCap = unitLengthSidewallJctCap;
    }

    public double getVdd() {
        return vdd;
    }

    public void setVdd(double vdd) {
        this.vdd = vdd;
    }

    public double getVfb0() {
        return vfb0;
    }

    public void setVfb0(double vfb0) {
        this.vfb0 = vfb0;
    }

    public double getVfbL() {
        return vfbL;
    }

    public void setVfbL(double vfbL) {
        this.vfbL = vfbL;
    }

    public double getVfbW() {
        return vfbW;
    }

    public void setVfbW(double vfbW) {
        this.vfbW = vfbW;
    }
}
