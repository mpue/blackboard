/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.jfet;

/**
 *
 * @author Kristopher T. Beck
 */
public class JFET2ModelValues extends JFETModelValues {

    protected double acgam;

    /* D-S junction capacitance*/
    protected double capDS;

    /* Thermal current reduction */
    protected double delta;

    /* Drain feedback modulation*/
    protected double hfeta;

    /* AC source feedback vgd modulation */
    protected double hfe1;

    /* AC source feedback vgs modulation */
    protected double hfe2;

    /* AC drain feedback vgs modulation */
    protected double hfg1;

    /* AC drain feedback vgd modulation */
    protected double hfg2;

    /* Subthreshold vds modulation */
    protected double mvst;

    /* Saturation index vgs modulation */
    protected double mxi;

    /* Diode junction breakdown current */
    protected double ibd;

    /* DC drain feedback */
    protected double lfgam;

    /* DC drain feedback vgs modulation */
    protected double lfg1;

    /* DC drain feedback vgd modulation */
    protected double lfg2;

    /* Gate junction factor*/
    protected double n = 1;

    /* Controlled resistance power law */
    protected double p = 2;

    /* Controlled current power law  */
    protected double q = 2;

    /* Thermal relaxation time constant */
    protected double taud;

    /* Drain feedback relaxation time constant */
    protected double taug;

    /* Diode junction breakdown potential */
    protected double vbd = 1;

    /* Subthreshold exponential coeff */
    protected double vst;

    /* Velocity saturation index */
    protected double xi = 1000;

    /* CQS pinch-off reduction */
    protected double xc;

    /* AC drain feedback */
    protected double hfgam;

    /* Velocity saturation rate */
    protected double z = 1;
    protected double version = 2;

    public double getAcgam() {
        return acgam;
    }

    public void setAcgam(double acgam) {
        this.acgam = acgam;
    }

    public double getCapDS() {
        return capDS;
    }

    public void setCapDS(double capds) {
        this.capDS = capds;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getHfe1() {
        return hfe1;
    }

    public void setHfe1(double hfe1) {
        this.hfe1 = hfe1;
    }

    public double getHfe2() {
        return hfe2;
    }

    public void setHfe2(double hfe2) {
        this.hfe2 = hfe2;
    }

    public double getHfeta() {
        return hfeta;
    }

    public void setHfeta(double hfeta) {
        this.hfeta = hfeta;
    }

    public double getHfg1() {
        return hfg1;
    }

    public void setHfg1(double hfg1) {
        this.hfg1 = hfg1;
    }

    public double getHfg2() {
        return hfg2;
    }

    public void setHfg2(double hfg2) {
        this.hfg2 = hfg2;
    }

    public double getHfgam() {
        return hfgam;
    }

    public void setHfgam(double hfgam) {
        this.hfgam = hfgam;
    }

    public double getIbd() {
        return ibd;
    }

    public void setIbd(double ibd) {
        this.ibd = ibd;
    }

    public double getLfg1() {
        return lfg1;
    }

    public void setLfg1(double lfg1) {
        this.lfg1 = lfg1;
    }

    public double getLfg2() {
        return lfg2;
    }

    public void setLfg2(double lfg2) {
        this.lfg2 = lfg2;
    }

    public double getLfgam() {
        return lfgam;
    }

    public void setLfgam(double lfgam) {
        this.lfgam = lfgam;
    }

    public double getMvst() {
        return mvst;
    }

    public void setMvst(double mvst) {
        this.mvst = mvst;
    }

    public double getMxi() {
        return mxi;
    }

    public void setMxi(double mxi) {
        this.mxi = mxi;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public double getTaud() {
        return taud;
    }

    public void setTaud(double taud) {
        this.taud = taud;
    }

    public double getTaug() {
        return taug;
    }

    public void setTaug(double taug) {
        this.taug = taug;
    }

    public double getVbd() {
        return vbd;
    }

    public void setVbd(double vbd) {
        this.vbd = vbd;
    }

    public double getVst() {
        return vst;
    }

    public void setVst(double vst) {
        this.vst = vst;
    }

    public double getXc() {
        return xc;
    }

    public void setXc(double xc) {
        this.xc = xc;
    }

    public double getXi() {
        return xi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }
}
