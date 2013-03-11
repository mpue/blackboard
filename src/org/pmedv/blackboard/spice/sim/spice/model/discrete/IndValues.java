/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class IndValues extends SpiceDevice {

    protected int posIndex;
    protected int negIndex;
    protected double induct;
    protected double m = 1;
    protected double temp;
    protected double dtemp;
    protected double scale = 1;
    protected double nt;
    protected double initCond;

    public double getDtemp() {
        return dtemp;
    }

    public void setDtemp(double dtemp) {
        this.dtemp = dtemp;
    }

    public double getInduct() {
        return induct;
    }

    public void setInduct(double induct) {
        this.induct = induct;
    }

    public double getInitCond() {
        return initCond;
    }

    public void setInitCond(double initCond) {
        this.initCond = initCond;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public int getNegIndex() {
        return negIndex;
    }

    public void setNegIndex(int negIndex) {
        this.negIndex = negIndex;
    }

    public double getNt() {
        return nt;
    }

    public void setNt(double nt) {
        this.nt = nt;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

}
