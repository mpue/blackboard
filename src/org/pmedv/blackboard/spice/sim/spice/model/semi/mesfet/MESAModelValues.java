/*
 * MES2Vars.java
 * 
 * Created on Nov 13, 2007, 3:21:59 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet;

/**
 *
 * @author Kristopher T. Beck
 */
public class MESAModelValues extends MESModelValues {

    protected double vs = 1.5e5;
    protected double gateResist;
    protected double ri;
    protected double rf;
    protected double rdi;
    protected double rsi;
    protected double phib;
    protected double phib1;
    protected double astar = 4.0e4;
    protected double ggr = 40;
    protected double del = 0.04;
    protected double xchi = 0.033;
    protected double eta = 1.73;
    /* Parallel Multiplier */
    protected double m = 2.5;
    protected double mc = 3.0;
    protected double sigma0 = 0.081;
    protected double vsigmat = 1.01;
    protected double vsigma = 0.1;
    protected double mu = 0.23;
    protected double theta;
    protected double mu1;
    protected double mu2;
    protected double d = 0.12e-6;
    protected double nd = 2.0e23;
    protected double du = 0.035e-6;
    protected double ndu = 1e22;
    protected double th = 0.01e-6;
    protected double ndelta = 6e24;
    protected double delta = 5.0;
    protected double tc;
    protected double n = 1;
    protected double tvto;
    protected double tlambda;
    protected double teta0;
    protected double teta1;
    protected double tmu = 300.15;
    protected double xtm0;
    protected double xtm1;
    protected double xtm2;
    protected double ks;
    protected double vsg;
    protected double lambdahf;
    protected double tf;
    protected double flo;
    protected double delfo;
    protected double ag;
    protected double tc1;
    protected double tc2;
    protected double zeta = 1;
    protected double level = 2;
    protected double nmax = 2e16;
    protected double gamma = 3.0;
    protected double epsi;
    protected double cbs = 1;
    protected double cas = 1;

    public MESAModelValues() {
        vto = -1.26;
        beta = 0.0085;
        lambda = 0.045;
    }

    public double getAg() {
        return ag;
    }

    public void setAg(double ag) {
        this.ag = ag;
    }

    public double getAstar() {
        return astar;
    }

    public void setAstar(double astar) {
        this.astar = astar;
    }

    public double getCas() {
        return cas;
    }

    public void setCas(double cas) {
        this.cas = cas;
    }

    public double getCbs() {
        return cbs;
    }

    public void setCbs(double cbs) {
        this.cbs = cbs;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getDel() {
        return del;
    }

    public void setDel(double del) {
        this.del = del;
    }

    public double getDelfo() {
        return delfo;
    }

    public void setDelfo(double delfo) {
        this.delfo = delfo;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDu() {
        return du;
    }

    public void setDu(double du) {
        this.du = du;
    }

    public double getEpsi() {
        return epsi;
    }

    public void setEpsi(double epsi) {
        this.epsi = epsi;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public double getFlo() {
        return flo;
    }

    public void setFlo(double flo) {
        this.flo = flo;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getGateResist() {
        return gateResist;
    }

    public void setGateResist(double gateResist) {
        this.gateResist = gateResist;
    }

    public double getGgr() {
        return ggr;
    }

    public void setGgr(double ggr) {
        this.ggr = ggr;
    }

    public double getKs() {
        return ks;
    }

    public void setKs(double ks) {
        this.ks = ks;
    }

    public double getLambdahf() {
        return lambdahf;
    }

    public void setLambdahf(double lambdahf) {
        this.lambdahf = lambdahf;
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

    public double getMu1() {
        return mu1;
    }

    public void setMu1(double mu1) {
        this.mu1 = mu1;
    }

    public double getMu2() {
        return mu2;
    }

    public void setMu2(double mu2) {
        this.mu2 = mu2;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getNd() {
        return nd;
    }

    public void setNd(double nd) {
        this.nd = nd;
    }

    public double getNdelta() {
        return ndelta;
    }

    public void setNdelta(double ndelta) {
        this.ndelta = ndelta;
    }

    public double getNdu() {
        return ndu;
    }

    public void setNdu(double ndu) {
        this.ndu = ndu;
    }

    public double getNmax() {
        return nmax;
    }

    public void setNmax(double nmax) {
        this.nmax = nmax;
    }

    public double getPhib() {
        return phib;
    }

    public void setPhib(double phib) {
        this.phib = phib;
    }

    public double getPhib1() {
        return phib1;
    }

    public void setPhib1(double phib1) {
        this.phib1 = phib1;
    }

    public double getRdi() {
        return rdi;
    }

    public void setRdi(double rdi) {
        this.rdi = rdi;
    }

    public double getRf() {
        return rf;
    }

    public void setRf(double rf) {
        this.rf = rf;
    }

    public double getRi() {
        return ri;
    }

    public void setRi(double ri) {
        this.ri = ri;
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

    public double getTc() {
        return tc;
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    public double getTc1() {
        return tc1;
    }

    public void setTc1(double tc1) {
        this.tc1 = tc1;
    }

    public double getTc2() {
        return tc2;
    }

    public void setTc2(double tc2) {
        this.tc2 = tc2;
    }

    public double getTeta0() {
        return teta0;
    }

    public void setTeta0(double teta0) {
        this.teta0 = teta0;
    }

    public double getTeta1() {
        return teta1;
    }

    public void setTeta1(double teta1) {
        this.teta1 = teta1;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }

    public double getTh() {
        return th;
    }

    public void setTh(double th) {
        this.th = th;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getTlambda() {
        return tlambda;
    }

    public void setTlambda(double tlambda) {
        this.tlambda = tlambda;
    }

    public double getTmu() {
        return tmu;
    }

    public void setTmu(double tmu) {
        this.tmu = tmu;
    }

    public double getTvto() {
        return tvto;
    }

    public void setTvto(double tvto) {
        this.tvto = tvto;
    }

    public double getVs() {
        return vs;
    }

    public void setVs(double vs) {
        this.vs = vs;
    }

    public double getVsg() {
        return vsg;
    }

    public void setVsg(double vsg) {
        this.vsg = vsg;
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

    public double getXchi() {
        return xchi;
    }

    public void setXchi(double xchi) {
        this.xchi = xchi;
    }

    public double getXtm0() {
        return xtm0;
    }

    public void setXtm0(double xtm0) {
        this.xtm0 = xtm0;
    }

    public double getXtm1() {
        return xtm1;
    }

    public void setXtm1(double xtm1) {
        this.xtm1 = xtm1;
    }

    public double getXtm2() {
        return xtm2;
    }

    public void setXtm2(double xtm2) {
        this.xtm2 = xtm2;
    }

    public double getZeta() {
        return zeta;
    }

    public void setZeta(double zeta) {
        this.zeta = zeta;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }
}
