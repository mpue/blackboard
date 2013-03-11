/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.jfet;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class JFETValues extends SpiceDevice {

    /* Drain node index */
    protected int drnIndex;

    /* Gate node index */
    protected int gateIndex;

    /* Source node index */
    protected int srcIndex;
    /* On/Off state */
    protected boolean off;

    /* Area factor */
    protected double area = 1;

    /* Parallel multiplier */
    protected double m = 1;

    /* Initial condition voltage D - S */
    protected double icVDS;

    /* Initial condition voltage G - S */
    protected double icVGS;

    /* Operating temperature */
    protected double temp;

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getDrnIndex() {
        return drnIndex;
    }

    public void setDrnIndex(int drnIndex) {
        this.drnIndex = drnIndex;
    }

    public int getGateIndex() {
        return gateIndex;
    }

    public void setGateIndex(int gateIndex) {
        this.gateIndex = gateIndex;
    }

    public double getIcVDS() {
        return icVDS;
    }

    public void setIcVDS(double icVDS) {
        this.icVDS = icVDS;
    }

    public double getIcVGS() {
        return icVGS;
    }

    public void setIcVGS(double icVGS) {
        this.icVGS = icVGS;
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

    public int getSrcIndex() {
        return srcIndex;
    }

    public void setSrcIndex(int srcIndex) {
        this.srcIndex = srcIndex;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
