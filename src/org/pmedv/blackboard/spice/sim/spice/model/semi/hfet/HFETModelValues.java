/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.hfet;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author owenbad
 */
public abstract class HFETModelValues extends HFETValues implements Model {

    protected int type = N_TYPE;
    protected double eta;
    protected double rd;
    protected double rdi;
    protected double rs;
    protected double rsi;
    protected double vto;
    protected double lambda = 0.15;
    protected double m;
    protected double mc = 3;
    protected double gamma = 3;
    protected double delta = 3;
    protected double nmax = 2e16;
    protected double deltad = 4.5e-9;
    protected double vs;
    protected double vsigma = 0.1;
    protected double vsigmat = 0.3;
    protected double sigma0 = 0.057;
    protected double vt1;
    protected double vt2;
    protected double kvto;
    protected double mu;
    protected double klambda;
    protected double kmu;
    protected double eta1 = 2;
    protected double d1 = 0.03e-6;
    protected double eta2 = 2;
    protected double d2 = 0.2e-6;
    protected double ggr;
    protected double del = 0.04;
    protected double di = 0.04e-6;
    protected double p = 1;

    public double getD1() {
        return d1;
    }

    public void setD1(double d1) {
        this.d1 = d1;
    }

    public double getD2() {
        return d2;
    }

    public void setD2(double d2) {
        this.d2 = d2;
    }

    public double getDel() {
        return del;
    }

    public void setDel(double del) {
        this.del = del;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDeltad() {
        return deltad;
    }

    public void setDeltad(double deltad) {
        this.deltad = deltad;
    }

    public double getDi() {
        return di;
    }

    public void setDi(double di) {
        this.di = di;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public double getEta1() {
        return eta1;
    }

    public void setEta1(double eta1) {
        this.eta1 = eta1;
    }

    public double getEta2() {
        return eta2;
    }

    public void setEta2(double eta2) {
        this.eta2 = eta2;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getGgr() {
        return ggr;
    }

    public void setGgr(double ggr) {
        this.ggr = ggr;
    }

    public double getKlambda() {
        return klambda;
    }

    public void setKlambda(double klambda) {
        this.klambda = klambda;
    }

    public double getKmu() {
        return kmu;
    }

    public void setKmu(double kmu) {
        this.kmu = kmu;
    }

    public double getKvto() {
        return kvto;
    }

    public void setKvto(double kvto) {
        this.kvto = kvto;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getMc() {
        return mc;
    }

    public void setMc(double mc) {
        this.mc = mc;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public double getNmax() {
        return nmax;
    }

    public void setNmax(double nmax) {
        this.nmax = nmax;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getRd() {
        return rd;
    }

    public void setRd(double rd) {
        this.rd = rd;
    }

    public double getRdi() {
        return rdi;
    }

    public void setRdi(double rdi) {
        this.rdi = rdi;
    }

    public double getRs() {
        return rs;
    }

    public void setRs(double rs) {
        this.rs = rs;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public double getSigma0() {
        return sigma0;
    }

    public void setSigma0(double sigma0) {
        this.sigma0 = sigma0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getVs() {
        return vs;
    }

    public void setVs(double vs) {
        this.vs = vs;
    }

    public double getVsigma() {
        return vsigma;
    }

    public void setVsigma(double vsigma) {
        this.vsigma = vsigma;
    }

    public double getVsigmat() {
        return vsigmat;
    }

    public void setVsigmat(double vsigmat) {
        this.vsigmat = vsigmat;
    }

    public double getVt1() {
        return vt1;
    }

    public void setVt1(double vt1) {
        this.vt1 = vt1;
    }

    public double getVt2() {
        return vt2;
    }

    public void setVt2(double vt2) {
        this.vt2 = vt2;
    }

    public double getVto() {
        return vto;
    }

    public void setVto(double vto) {
        this.vto = vto;
    }
}
