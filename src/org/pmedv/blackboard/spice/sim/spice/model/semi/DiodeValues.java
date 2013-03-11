/*
 * DiodeValues.java
 * 
 * Created on Nov 13, 2007, 3:19:34 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class DiodeValues extends SpiceDevice {

    protected int posIndex;
    protected int negIndex;

    /* On/Off state */
    protected boolean off;

    /* Area factor */
    protected double area = 1;

    /* Perimeter */
    protected double pj = 0;

    /* Multiplier */
    protected double m = 1;

    /* Initial condition */
    protected double initCond;

    /* Temperature of the instance */
    protected double temp;

    /* Delta temperature of instance */
    protected double dtemp;

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getDtemp() {
        return dtemp;
    }

    public void setDtemp(double dtemp) {
        this.dtemp = dtemp;
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

    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public double getPj() {
        return pj;
    }

    public void setPj(double pj) {
        this.pj = pj;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
