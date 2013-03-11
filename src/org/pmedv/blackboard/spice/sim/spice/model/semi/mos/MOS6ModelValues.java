/*
 * MOS6ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:24:27 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOS6ModelValues extends MOSModelValues {

    protected double kv = 2;

    /* Drain linear conductance factor */
    protected double nv = 0.5;
    protected double kc = 5e-5;

    /* Saturation current coeff.*/
    protected double nc = 1;

    /* Threshold voltage coeff.*/
    protected double nvth = 0.5;

    /* Saturation current modification parameter */
    protected double ps;

    /* Secondary back - gate effect parameter */
    protected double gamma1;
    protected double sigma;
    protected double lamda0;
    protected double lamda1;

    public double getGamma1() {
        return gamma1;
    }

    public void setGamma1(double gamma1) {
        this.gamma1 = gamma1;
    }

    public double getKc() {
        return kc;
    }

    public void setKc(double kc) {
        this.kc = kc;
    }

    public double getKv() {
        return kv;
    }

    public void setKv(double kv) {
        this.kv = kv;
    }

    public double getLamda0() {
        return lamda0;
    }

    public void setLamda0(double lamda0) {
        this.lamda0 = lamda0;
    }

    public double getLamda1() {
        return lamda1;
    }

    public void setLamda1(double lamda1) {
        this.lamda1 = lamda1;
    }

    public double getNc() {
        return nc;
    }

    public void setNc(double nc) {
        this.nc = nc;
    }

    public double getNv() {
        return nv;
    }

    public void setNv(double nv) {
        this.nv = nv;
    }

    public double getNvth() {
        return nvth;
    }

    public void setNvth(double nvth) {
        this.nvth = nvth;
    }

    public double getPs() {
        return ps;
    }

    public void setPs(double ps) {
        this.ps = ps;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
}
