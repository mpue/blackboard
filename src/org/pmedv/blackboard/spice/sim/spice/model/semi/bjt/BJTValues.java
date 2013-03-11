/*
 * BJTValues.java
 * 
 * Created on Nov 13, 2007, 3:18:04 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.bjt;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class BJTValues extends SpiceDevice {


    /* Instance name */
    //protected String name;

    /* Collector node index */
    protected int colIndex;

    /* Base node index */
    protected int baseIndex;

    /* Emitter node index */
    protected int emitIndex;

    /* Substrate node index */
    protected int substIndex;

    /* Emitter area factor */
    protected double area = 1;

    /* Base area factor */
    protected double areab = 1;

    /* Collector area factor */
    protected double areac = 1;

    /* Parallel multiplier */
    protected double m = 1;

    /* On/Off state */
    protected boolean off;

    /* Initial condition voltage base - emitter */
    protected double icVBE;

    /* Initial condition voltage collector - emitter */
    protected double icVCE;

    /* Instance temperature */
    protected double temp;

    /* Instance delta temperature from circuit */
    protected double dtemp;

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getAreab() {
        return areab;
    }

    public void setAreab(double areab) {
        this.areab = areab;
    }

    public double getAreac() {
        return areac;
    }

    public void setAreac(double areac) {
        this.areac = areac;
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public double getDtemp() {
        return dtemp;
    }

    public void setDtemp(double dtemp) {
        this.dtemp = dtemp;
    }

    public int getEmitIndex() {
        return emitIndex;
    }

    public void setEmitIndex(int emitIndex) {
        this.emitIndex = emitIndex;
    }

    public double getIcVBE() {
        return icVBE;
    }

    public void setIcVBE(double icVBE) {
        this.icVBE = icVBE;
    }

    public double getIcVCE() {
        return icVCE;
    }

    public void setIcVCE(double icVCE) {
        this.icVCE = icVCE;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public int getSubstIndex() {
        return substIndex;
    }

    public void setSubstIndex(int substIndex) {
        this.substIndex = substIndex;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
