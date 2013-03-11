/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.model.semi.hfet;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFETValues extends SpiceDevice{
    protected int drnIndex;
    protected int gateIndex;
    protected int srcIndex;
    protected double icVDS;
    protected double icVGS;
    protected double temp;

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
