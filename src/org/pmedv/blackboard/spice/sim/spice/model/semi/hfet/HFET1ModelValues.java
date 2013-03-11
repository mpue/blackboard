/*
 * HFET1ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:20:10 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.hfet;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFET1ModelValues extends HFETModelValues {

    protected double rg;
    protected double rgs = 90;
    protected double rgd = 90;
    protected double ri;
    protected double rf;
    protected double js1d = 1;
    protected double js2d = 1.15e6;
    protected double js1s = 1;
    protected double js2s = 1.15e6;
    protected double m1d = 1.32;
    protected double m2d = 6.9;
    protected double m1s = 1.32;
    protected double m2s = 6.9;
    protected double a1;
    protected double a2;
    protected double mv1 = 3;
    protected double kappa;
    protected double delf;
    protected double fgds;
    protected double tf;
    protected double cds;
    protected double phib = 0.5 * CHARGE;
    protected double talpha = 1200;
    protected double mt1 = 3.5;
    protected double mt2 = 9.9;
    protected double ck1 = 1;
    protected double ck2;
    protected double cm1 = 3;
    protected double cm2;
    protected double cm3 = 0.17;
    protected double astar = 4.0e4;
    protected int gatemod;

    public HFET1ModelValues() {
        m = 3;
        ggr = 40;
    }

    public double getA1() {
        return a1;
    }

    public void setA1(double a1) {
        this.a1 = a1;
    }

    public double getA2() {
        return a2;
    }

    public void setA2(double a2) {
        this.a2 = a2;
    }

    public double getAstar() {
        return astar;
    }

    public void setAstar(double astar) {
        this.astar = astar;
    }

    public double getCds() {
        return cds;
    }

    public void setCds(double cds) {
        this.cds = cds;
    }

    public double getCk1() {
        return ck1;
    }

    public void setCk1(double ck1) {
        this.ck1 = ck1;
    }

    public double getCk2() {
        return ck2;
    }

    public void setCk2(double ck2) {
        this.ck2 = ck2;
    }

    public double getCm1() {
        return cm1;
    }

    public void setCm1(double cm1) {
        this.cm1 = cm1;
    }

    public double getCm2() {
        return cm2;
    }

    public void setCm2(double cm2) {
        this.cm2 = cm2;
    }

    public double getCm3() {
        return cm3;
    }

    public void setCm3(double cm3) {
        this.cm3 = cm3;
    }

    public double getDelf() {
        return delf;
    }

    public void setDelf(double delf) {
        this.delf = delf;
    }

    public double getFgds() {
        return fgds;
    }

    public void setFgds(double fgds) {
        this.fgds = fgds;
    }

    public double getJs1d() {
        return js1d;
    }

    public void setJs1d(double js1d) {
        this.js1d = js1d;
    }

    public double getJs1s() {
        return js1s;
    }

    public void setJs1s(double js1s) {
        this.js1s = js1s;
    }

    public double getJs2d() {
        return js2d;
    }

    public void setJs2d(double js2d) {
        this.js2d = js2d;
    }

    public double getJs2s() {
        return js2s;
    }

    public void setJs2s(double js2s) {
        this.js2s = js2s;
    }

    public double getKappa() {
        return kappa;
    }

    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    public double getM1d() {
        return m1d;
    }

    public void setM1d(double m1d) {
        this.m1d = m1d;
    }

    public double getM1s() {
        return m1s;
    }

    public void setM1s(double m1s) {
        this.m1s = m1s;
    }

    public double getM2d() {
        return m2d;
    }

    public void setM2d(double m2d) {
        this.m2d = m2d;
    }

    public double getM2s() {
        return m2s;
    }

    public void setM2s(double m2s) {
        this.m2s = m2s;
    }

    public double getMt1() {
        return mt1;
    }

    public void setMt1(double mt1) {
        this.mt1 = mt1;
    }

    public double getMt2() {
        return mt2;
    }

    public void setMt2(double mt2) {
        this.mt2 = mt2;
    }

    public double getMv1() {
        return mv1;
    }

    public void setMv1(double mv1) {
        this.mv1 = mv1;
    }

    public double getPhib() {
        return phib;
    }

    public void setPhib(double phib) {
        this.phib = phib;
    }

    public double getRf() {
        return rf;
    }

    public void setRf(double rf) {
        this.rf = rf;
    }

    public double getRg() {
        return rg;
    }

    public void setRg(double rg) {
        this.rg = rg;
    }

    public double getRgd() {
        return rgd;
    }

    public void setRgd(double rgd) {
        this.rgd = rgd;
    }

    public double getRgs() {
        return rgs;
    }

    public void setRgs(double rgs) {
        this.rgs = rgs;
    }

    public double getRi() {
        return ri;
    }

    public void setRi(double ri) {
        this.ri = ri;
    }

    public double getTalpha() {
        return talpha;
    }

    public void setTalpha(double talpha) {
        this.talpha = talpha;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }

    public int getGatemod() {
        return gatemod;
    }

    public void setGatemod(int gatemod) {
        this.gatemod = gatemod;
    }
}
