/*
 * BSIM2ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:17:00 PM
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
public class BSIM2ModelValues extends BSIMValues implements Model {

    protected double vfb0 = -1.0;
    protected double vfbL;
    protected double vfbW;
    protected double phi0 = 0.75;
    protected double phiL;
    protected double phiW;
    protected double k10 = 0.8;
    protected double k1L;
    protected double k1W;
    protected double k20;
    protected double k2L;
    protected double k2W;
    protected double eta00;
    protected double eta0L;
    protected double eta0W;
    protected double etaB0;
    protected double etaBL;
    protected double etaBW;
    protected double deltaL;
    protected double deltaW;
    protected double mob00 = 400.0;
    protected double mob0B0;
    protected double mob0BL;
    protected double mob0BW;
    protected double mobs00 = 500.0;
    protected double mobs0L;
    protected double mobs0W;
    protected double mobsB0;
    protected double mobsBL;
    protected double mobsBW;
    protected double mob200 = 1.5;
    protected double mob20L;
    protected double mob20W;
    protected double mob2B0;
    protected double mob2BL;
    protected double mob2BW;
    protected double mob2G0;
    protected double mob2GL;
    protected double mob2GW;
    protected double mob300 = 10;
    protected double mob30L;
    protected double mob30W;
    protected double mob3B0;
    protected double mob3BL;
    protected double mob3BW;
    protected double mob3G0;
    protected double mob3GL;
    protected double mob3GW;
    protected double mob400;
    protected double mob40L;
    protected double mob40W;
    protected double mob4B0;
    protected double mob4BL;
    protected double mob4BW;
    protected double mob4G0;
    protected double mob4GL;
    protected double mob4GW;
    protected double ua00 = 0.2;
    protected double ua0L;
    protected double ua0W;
    protected double uaB0;
    protected double uaBL;
    protected double uaBW;
    protected double ub00;
    protected double ub0L;
    protected double ub0W;
    protected double ubB0;
    protected double ubBL;
    protected double ubBW;
    protected double u100 = 0.1;
    protected double u10L;
    protected double u10W;
    protected double u1B0;
    protected double u1BL;
    protected double u1BW;
    protected double u1D0;
    protected double u1DL;
    protected double u1DW;
    protected double n00 = 1.4;
    protected double n0L;
    protected double n0W;
    protected double nB0 = 0.5;
    protected double nBL;
    protected double nBW;
    protected double nD0;
    protected double nDL;
    protected double nDW;
    protected double vof00 = 1.8;
    protected double vof0L;
    protected double vof0W;
    protected double vofB0;
    protected double vofBL;
    protected double vofBW;
    protected double vofD0;
    protected double vofDL;
    protected double vofDW;
    protected double ai00;
    protected double ai0L;
    protected double ai0W;
    protected double aiB0;
    protected double aiBL;
    protected double aiBW;
    protected double bi00;
    protected double bi0L;
    protected double bi0W;
    protected double biB0;
    protected double biBL;
    protected double biBW;
    protected double vghigh0 = 0.2;
    protected double vghighL;
    protected double vghighW;
    protected double vglow0 = -0.15;
    protected double vglowL;
    protected double vglowW;
    protected double tox = 0.03;
    protected double vdd = 5.0;
    protected double vgg = 5.0;
    protected double vbb = 5.0;
    protected double gateSrcOverlapCap;
    protected double gateDrnOverlapCap;
    protected double gateBlkOverlapCap;
    protected double sidewallJctPotential;
    protected double unitAreaJctCap;
    protected double unitLengthSidewallJctCap;
    protected double defaultWidth = 10.0;
    protected double deltaLength;
    protected double channelChargePartitionFlag;

    public BSIM2ModelValues() {
    }

    public double getAi00() {
        return ai00;
    }

    public void setAi00(double ai00) {
        this.ai00 = ai00;
    }

    public double getAi0L() {
        return ai0L;
    }

    public void setAi0L(double ai0L) {
        this.ai0L = ai0L;
    }

    public double getAi0W() {
        return ai0W;
    }

    public void setAi0W(double ai0W) {
        this.ai0W = ai0W;
    }

    public double getAiB0() {
        return aiB0;
    }

    public void setAiB0(double aiB0) {
        this.aiB0 = aiB0;
    }

    public double getAiBL() {
        return aiBL;
    }

    public void setAiBL(double aiBL) {
        this.aiBL = aiBL;
    }

    public double getAiBW() {
        return aiBW;
    }

    public void setAiBW(double aiBW) {
        this.aiBW = aiBW;
    }

    public double getBi00() {
        return bi00;
    }

    public void setBi00(double bi00) {
        this.bi00 = bi00;
    }

    public double getBi0L() {
        return bi0L;
    }

    public void setBi0L(double bi0L) {
        this.bi0L = bi0L;
    }

    public double getBi0W() {
        return bi0W;
    }

    public void setBi0W(double bi0W) {
        this.bi0W = bi0W;
    }

    public double getBiB0() {
        return biB0;
    }

    public void setBiB0(double biB0) {
        this.biB0 = biB0;
    }

    public double getBiBL() {
        return biBL;
    }

    public void setBiBL(double biBL) {
        this.biBL = biBL;
    }

    public double getBiBW() {
        return biBW;
    }

    public void setBiBW(double biBW) {
        this.biBW = biBW;
    }

    public double getChannelChargePartitionFlag() {
        return channelChargePartitionFlag;
    }

    public void setChannelChargePartitionFlag(double channelChargePartitionFlag) {
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

    public double getEta00() {
        return eta00;
    }

    public void setEta00(double eta00) {
        this.eta00 = eta00;
    }

    public double getEta0L() {
        return eta0L;
    }

    public void setEta0L(double eta0L) {
        this.eta0L = eta0L;
    }

    public double getEta0W() {
        return eta0W;
    }

    public void setEta0W(double eta0W) {
        this.eta0W = eta0W;
    }

    public double getEtaB0() {
        return etaB0;
    }

    public void setEtaB0(double etaB0) {
        this.etaB0 = etaB0;
    }

    public double getEtaBL() {
        return etaBL;
    }

    public void setEtaBL(double etaBL) {
        this.etaBL = etaBL;
    }

    public double getEtaBW() {
        return etaBW;
    }

    public void setEtaBW(double etaBW) {
        this.etaBW = etaBW;
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

    public double getMob00() {
        return mob00;
    }

    public void setMob00(double mob00) {
        this.mob00 = mob00;
    }

    public double getMob0B0() {
        return mob0B0;
    }

    public void setMob0B0(double mob0B0) {
        this.mob0B0 = mob0B0;
    }

    public double getMob0BL() {
        return mob0BL;
    }

    public void setMob0BL(double mob0BL) {
        this.mob0BL = mob0BL;
    }

    public double getMob0BW() {
        return mob0BW;
    }

    public void setMob0BW(double mob0BW) {
        this.mob0BW = mob0BW;
    }

    public double getMob200() {
        return mob200;
    }

    public void setMob200(double mob200) {
        this.mob200 = mob200;
    }

    public double getMob20L() {
        return mob20L;
    }

    public void setMob20L(double mob20L) {
        this.mob20L = mob20L;
    }

    public double getMob20W() {
        return mob20W;
    }

    public void setMob20W(double mob20W) {
        this.mob20W = mob20W;
    }

    public double getMob2B0() {
        return mob2B0;
    }

    public void setMob2B0(double mob2B0) {
        this.mob2B0 = mob2B0;
    }

    public double getMob2BL() {
        return mob2BL;
    }

    public void setMob2BL(double mob2BL) {
        this.mob2BL = mob2BL;
    }

    public double getMob2BW() {
        return mob2BW;
    }

    public void setMob2BW(double mob2BW) {
        this.mob2BW = mob2BW;
    }

    public double getMob2G0() {
        return mob2G0;
    }

    public void setMob2G0(double mob2G0) {
        this.mob2G0 = mob2G0;
    }

    public double getMob2GL() {
        return mob2GL;
    }

    public void setMob2GL(double mob2GL) {
        this.mob2GL = mob2GL;
    }

    public double getMob2GW() {
        return mob2GW;
    }

    public void setMob2GW(double mob2GW) {
        this.mob2GW = mob2GW;
    }

    public double getMob300() {
        return mob300;
    }

    public void setMob300(double mob300) {
        this.mob300 = mob300;
    }

    public double getMob30L() {
        return mob30L;
    }

    public void setMob30L(double mob30L) {
        this.mob30L = mob30L;
    }

    public double getMob30W() {
        return mob30W;
    }

    public void setMob30W(double mob30W) {
        this.mob30W = mob30W;
    }

    public double getMob3B0() {
        return mob3B0;
    }

    public void setMob3B0(double mob3B0) {
        this.mob3B0 = mob3B0;
    }

    public double getMob3BL() {
        return mob3BL;
    }

    public void setMob3BL(double mob3BL) {
        this.mob3BL = mob3BL;
    }

    public double getMob3BW() {
        return mob3BW;
    }

    public void setMob3BW(double mob3BW) {
        this.mob3BW = mob3BW;
    }

    public double getMob3G0() {
        return mob3G0;
    }

    public void setMob3G0(double mob3G0) {
        this.mob3G0 = mob3G0;
    }

    public double getMob3GL() {
        return mob3GL;
    }

    public void setMob3GL(double mob3GL) {
        this.mob3GL = mob3GL;
    }

    public double getMob3GW() {
        return mob3GW;
    }

    public void setMob3GW(double mob3GW) {
        this.mob3GW = mob3GW;
    }

    public double getMob400() {
        return mob400;
    }

    public void setMob400(double mob400) {
        this.mob400 = mob400;
    }

    public double getMob40L() {
        return mob40L;
    }

    public void setMob40L(double mob40L) {
        this.mob40L = mob40L;
    }

    public double getMob40W() {
        return mob40W;
    }

    public void setMob40W(double mob40W) {
        this.mob40W = mob40W;
    }

    public double getMob4B0() {
        return mob4B0;
    }

    public void setMob4B0(double mob4B0) {
        this.mob4B0 = mob4B0;
    }

    public double getMob4BL() {
        return mob4BL;
    }

    public void setMob4BL(double mob4BL) {
        this.mob4BL = mob4BL;
    }

    public double getMob4BW() {
        return mob4BW;
    }

    public void setMob4BW(double mob4BW) {
        this.mob4BW = mob4BW;
    }

    public double getMob4G0() {
        return mob4G0;
    }

    public void setMob4G0(double mob4G0) {
        this.mob4G0 = mob4G0;
    }

    public double getMob4GL() {
        return mob4GL;
    }

    public void setMob4GL(double mob4GL) {
        this.mob4GL = mob4GL;
    }

    public double getMob4GW() {
        return mob4GW;
    }

    public void setMob4GW(double mob4GW) {
        this.mob4GW = mob4GW;
    }

    public double getMobs00() {
        return mobs00;
    }

    public void setMobs00(double mobs00) {
        this.mobs00 = mobs00;
    }

    public double getMobs0L() {
        return mobs0L;
    }

    public void setMobs0L(double mobs0L) {
        this.mobs0L = mobs0L;
    }

    public double getMobs0W() {
        return mobs0W;
    }

    public void setMobs0W(double mobs0W) {
        this.mobs0W = mobs0W;
    }

    public double getMobsB0() {
        return mobsB0;
    }

    public void setMobsB0(double mobsB0) {
        this.mobsB0 = mobsB0;
    }

    public double getMobsBL() {
        return mobsBL;
    }

    public void setMobsBL(double mobsBL) {
        this.mobsBL = mobsBL;
    }

    public double getMobsBW() {
        return mobsBW;
    }

    public void setMobsBW(double mobsBW) {
        this.mobsBW = mobsBW;
    }

    public double getN00() {
        return n00;
    }

    public void setN00(double n00) {
        this.n00 = n00;
    }

    public double getN0L() {
        return n0L;
    }

    public void setN0L(double n0L) {
        this.n0L = n0L;
    }

    public double getN0W() {
        return n0W;
    }

    public void setN0W(double n0W) {
        this.n0W = n0W;
    }

    public double getnB0() {
        return nB0;
    }

    public void setnB0(double nB0) {
        this.nB0 = nB0;
    }

    public double getnBL() {
        return nBL;
    }

    public void setnBL(double nBL) {
        this.nBL = nBL;
    }

    public double getnBW() {
        return nBW;
    }

    public void setnBW(double nBW) {
        this.nBW = nBW;
    }

    public double getnD0() {
        return nD0;
    }

    public void setnD0(double nD0) {
        this.nD0 = nD0;
    }

    public double getnDL() {
        return nDL;
    }

    public void setnDL(double nDL) {
        this.nDL = nDL;
    }

    public double getnDW() {
        return nDW;
    }

    public void setnDW(double nDW) {
        this.nDW = nDW;
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

    public double getTox() {
        return tox;
    }

    public void setTox(double tox) {
        this.tox = tox;
    }

    public double getU100() {
        return u100;
    }

    public void setU100(double u100) {
        this.u100 = u100;
    }

    public double getU10L() {
        return u10L;
    }

    public void setU10L(double u10L) {
        this.u10L = u10L;
    }

    public double getU10W() {
        return u10W;
    }

    public void setU10W(double u10W) {
        this.u10W = u10W;
    }

    public double getU1B0() {
        return u1B0;
    }

    public void setU1B0(double u1B0) {
        this.u1B0 = u1B0;
    }

    public double getU1BL() {
        return u1BL;
    }

    public void setU1BL(double u1BL) {
        this.u1BL = u1BL;
    }

    public double getU1BW() {
        return u1BW;
    }

    public void setU1BW(double u1BW) {
        this.u1BW = u1BW;
    }

    public double getU1D0() {
        return u1D0;
    }

    public void setU1D0(double u1D0) {
        this.u1D0 = u1D0;
    }

    public double getU1DL() {
        return u1DL;
    }

    public void setU1DL(double u1DL) {
        this.u1DL = u1DL;
    }

    public double getU1DW() {
        return u1DW;
    }

    public void setU1DW(double u1DW) {
        this.u1DW = u1DW;
    }

    public double getUa00() {
        return ua00;
    }

    public void setUa00(double ua00) {
        this.ua00 = ua00;
    }

    public double getUa0L() {
        return ua0L;
    }

    public void setUa0L(double ua0L) {
        this.ua0L = ua0L;
    }

    public double getUa0W() {
        return ua0W;
    }

    public void setUa0W(double ua0W) {
        this.ua0W = ua0W;
    }

    public double getUaB0() {
        return uaB0;
    }

    public void setUaB0(double uaB0) {
        this.uaB0 = uaB0;
    }

    public double getUaBL() {
        return uaBL;
    }

    public void setUaBL(double uaBL) {
        this.uaBL = uaBL;
    }

    public double getUaBW() {
        return uaBW;
    }

    public void setUaBW(double uaBW) {
        this.uaBW = uaBW;
    }

    public double getUb00() {
        return ub00;
    }

    public void setUb00(double ub00) {
        this.ub00 = ub00;
    }

    public double getUb0L() {
        return ub0L;
    }

    public void setUb0L(double ub0L) {
        this.ub0L = ub0L;
    }

    public double getUb0W() {
        return ub0W;
    }

    public void setUb0W(double ub0W) {
        this.ub0W = ub0W;
    }

    public double getUbB0() {
        return ubB0;
    }

    public void setUbB0(double ubB0) {
        this.ubB0 = ubB0;
    }

    public double getUbBL() {
        return ubBL;
    }

    public void setUbBL(double ubBL) {
        this.ubBL = ubBL;
    }

    public double getUbBW() {
        return ubBW;
    }

    public void setUbBW(double ubBW) {
        this.ubBW = ubBW;
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

    public double getVbb() {
        return vbb;
    }

    public void setVbb(double vbb) {
        this.vbb = vbb;
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

    public double getVgg() {
        return vgg;
    }

    public void setVgg(double vgg) {
        this.vgg = vgg;
    }

    public double getVghigh0() {
        return vghigh0;
    }

    public void setVghigh0(double vghigh0) {
        this.vghigh0 = vghigh0;
    }

    public double getVghighL() {
        return vghighL;
    }

    public void setVghighL(double vghighL) {
        this.vghighL = vghighL;
    }

    public double getVghighW() {
        return vghighW;
    }

    public void setVghighW(double vghighW) {
        this.vghighW = vghighW;
    }

    public double getVglow0() {
        return vglow0;
    }

    public void setVglow0(double vglow0) {
        this.vglow0 = vglow0;
    }

    public double getVglowL() {
        return vglowL;
    }

    public void setVglowL(double vglowL) {
        this.vglowL = vglowL;
    }

    public double getVglowW() {
        return vglowW;
    }

    public void setVglowW(double vglowW) {
        this.vglowW = vglowW;
    }

    public double getVof00() {
        return vof00;
    }

    public void setVof00(double vof00) {
        this.vof00 = vof00;
    }

    public double getVof0L() {
        return vof0L;
    }

    public void setVof0L(double vof0L) {
        this.vof0L = vof0L;
    }

    public double getVof0W() {
        return vof0W;
    }

    public void setVof0W(double vof0W) {
        this.vof0W = vof0W;
    }

    public double getVofB0() {
        return vofB0;
    }

    public void setVofB0(double vofB0) {
        this.vofB0 = vofB0;
    }

    public double getVofBL() {
        return vofBL;
    }

    public void setVofBL(double vofBL) {
        this.vofBL = vofBL;
    }

    public double getVofBW() {
        return vofBW;
    }

    public void setVofBW(double vofBW) {
        this.vofBW = vofBW;
    }

    public double getVofD0() {
        return vofD0;
    }

    public void setVofD0(double vofD0) {
        this.vofD0 = vofD0;
    }

    public double getVofDL() {
        return vofDL;
    }

    public void setVofDL(double vofDL) {
        this.vofDL = vofDL;
    }

    public double getVofDW() {
        return vofDW;
    }

    public void setVofDW(double vofDW) {
        this.vofDW = vofDW;
    }
}
