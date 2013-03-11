/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOSValues extends SpiceDevice {

    /* Drain node index */
    protected int drnIndex;

    /* Gate node index */
    protected int gateIndex;

    /* Source node index */
    protected int srcIndex;

    /* Bulk node index */
    protected int blkIndex;

    /* Initial condition B - S voltage */
    protected double icVBS;

    /* Initial condition D - S voltage */
    protected double icVDS;

    /* Initial condition G - S voltage */
    protected double icVGS;

    /* Parallel device multiplier */
    protected double m = 1;

    /* Length of channel region */
    protected double l;

    /* Width of channel region */
    protected double w;

    /* Area of drain diffusion */
    protected double drnArea;

    /* Area of source diffusion */
    protected double srcArea;

    /* Length of drain in squares */
    protected double drnSquares = 1;

    /* Length of source in squares */
    protected double srcSquares = 1;

    /* Drain perimeter */
    protected double drnPerimeter;

    /* Source perimeter */
    protected double srcPerimeter;

    /* On/off state */
    protected boolean off;

    /* Operating temperature */
    protected double temp;

    public int getBlkIndex() {
        return blkIndex;
    }

    public void setBlkIndex(int blkIndex) {
        this.blkIndex = blkIndex;
    }

    public double getDrnArea() {
        return drnArea;
    }

    public void setDrnArea(double drnArea) {
        this.drnArea = drnArea;
    }

    public int getDrnIndex() {
        return drnIndex;
    }

    public void setDrnIndex(int drnIndex) {
        this.drnIndex = drnIndex;
    }

    public double getDrnPerimeter() {
        return drnPerimeter;
    }

    public void setDrnPerimeter(double drnPerimeter) {
        this.drnPerimeter = drnPerimeter;
    }

    public int getGateIndex() {
        return gateIndex;
    }

    public void setGateIndex(int gateIndex) {
        this.gateIndex = gateIndex;
    }

    public double getIcVBS() {
        return icVBS;
    }

    public void setIcVBS(double icVBS) {
        this.icVBS = icVBS;
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

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
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

    public double getSrcArea() {
        return srcArea;
    }

    public void setSrcArea(double srcArea) {
        this.srcArea = srcArea;
    }

    public int getSrcIndex() {
        return srcIndex;
    }

    public void setSrcIndex(int srcIndex) {
        this.srcIndex = srcIndex;
    }

    public double getSrcPerimeter() {
        return srcPerimeter;
    }

    public void setSrcPerimeter(double srcPerimeter) {
        this.srcPerimeter = srcPerimeter;
    }

    public double getDrnSquares() {
        return drnSquares;
    }

    public void setDrnSquares(double drnSquares) {
        this.drnSquares = drnSquares;
    }

    public double getSrcSquares() {
        return srcSquares;
    }

    public void setSrcSquares(double srcSquares) {
        this.srcSquares = srcSquares;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }
}
