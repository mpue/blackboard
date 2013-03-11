/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class MESFETValues extends SpiceDevice{

    /* Drain index */
    protected int drnIndex;

    /* Gate index */
    protected int gateIndex;

    /* Source index */
    protected int srcIndex;

    /* Area factor for the mesfet */
    protected double area = 1.0;

    /* On/Off state for mesfet */
    protected boolean off;

    /* Initial condition voltage D - S */
    protected double icVDS;

    /* Initial condition voltage G - S */
    protected double icVGS;

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

}
