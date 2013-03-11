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
class CapValues extends SpiceDevice {

    protected int posIndex;
    protected int negIndex;

    protected double capAC = 0;
    protected double initCond = 0;
    protected double width;
    protected double length;
    protected double scale = 1;
    protected double m = 1;
    protected double temp;
    protected double dTemp;

    public double getCapAC() {
        return capAC;
    }

    public void setCapAC(double capAC) {
        this.capAC = capAC;
    }

    public double getDTemp() {
        return dTemp;
    }

    public void setDTemp(double dTemp) {
        this.dTemp = dTemp;
    }

    public double getInitCond() {
        return initCond;
    }

    public void setInitCond(double initCond) {
        this.initCond = initCond;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

}
