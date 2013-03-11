/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class CapModelValues extends CapValues implements Model {

    /* Temperature at which capacitance was measured */
    protected double tnom = 300;

    /* Linear temperature coefficient */
    protected double tempCoeff1;

    /* Quadratic temperature coefficient */
    protected double tempCoeff2;

    /* Unit Area Capacitance (F / M^2) */
    protected double cj;

    /* Unit Length Sidewall Capacitance (F / M) */
    protected double cjsw;

    /* Default width of a capacitor */
    protected double defWidth = 10.e-6;

    /* Default length of a capacitor */
    protected double defLength;

    /* Amount by which width are less than drawn */
    protected double narrow;

    /* Amount by which length are less than drawn */
    protected double shorter;

    /* Relative dielectric constant */
    protected double di;

    /* Insulator thickness */
    protected double thick;

    public double getCj() {
        return cj;
    }

    public void setCj(double cj) {
        this.cj = cj;
    }

    public double getCjsw() {
        return cjsw;
    }

    public void setCjsw(double cjsw) {
        this.cjsw = cjsw;
    }

    public double getDefLength() {
        return defLength;
    }

    public void setDefLength(double defLength) {
        this.defLength = defLength;
    }

    public double getDefWidth() {
        return defWidth;
    }

    public void setDefWidth(double defWidth) {
        this.defWidth = defWidth;
    }

    public double getDi() {
        return di;
    }

    public void setDi(double di) {
        this.di = di;
    }

    public double getNarrow() {
        return narrow;
    }

    public void setNarrow(double narrow) {
        this.narrow = narrow;
    }

    public double getShorter() {
        return shorter;
    }

    public void setShorter(double shorter) {
        this.shorter = shorter;
    }

    public double getTempCoeff1() {
        return tempCoeff1;
    }

    public void setTempCoeff1(double tempCoeff1) {
        this.tempCoeff1 = tempCoeff1;
    }

    public double getTempCoeff2() {
        return tempCoeff2;
    }

    public void setTempCoeff2(double tempCoeff2) {
        this.tempCoeff2 = tempCoeff2;
    }

    public double getThick() {
        return thick;
    }

    public void setThick(double thick) {
        this.thick = thick;
    }

    public double getTnom() {
        return tnom;
    }

    public void setTnom(double tnom) {
        this.tnom = tnom;
    }
}
