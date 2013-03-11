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
class ResValues extends SpiceDevice {

    protected int posIndex;
    protected int negIndex;
    protected double resist;
    protected double acResist;//? rename admittance
    protected double scale = 1;
    protected double m = 1;
    protected double width;
    protected double length;
    protected double temp;
    protected double dTemp;
    protected boolean noisy = true;

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getAcResist() {
        return acResist;
    }

    public void setAcResist(double acResist) {
        this.acResist = acResist;
    }

    public double getdTemp() {
        return dTemp;
    }

    public void setdTemp(double dTemp) {
        this.dTemp = dTemp;
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

    public boolean isNoisy() {
        return noisy;
    }

    public void setNoisy(boolean noisy) {
        this.noisy = noisy;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

    public double getResist() {
        return resist;
    }

    public void setResist(double resist) {
        this.resist = resist;
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
