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
public class IndModelValues extends IndValues implements Model {

    /* Temperature at which inductance was measured */
    protected double tnom;

    /* First temperature coefficient */
    protected double tempCoeff1;

    /* Second temperature coefficient */
    protected double tempCoeff2;

    /* Cross section of inductor */
    protected double cSect;

    /* Mean length of magnetic path */
    protected double length;

    /* Relative magnetic permeability */
    protected double mu;

    public double getcSect() {
        return cSect;
    }

    public void setcSect(double cSect) {
        this.cSect = cSect;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
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

    public double getTnom() {
        return tnom;
    }

    public void setTnom(double tnom) {
        this.tnom = tnom;
    }
}
