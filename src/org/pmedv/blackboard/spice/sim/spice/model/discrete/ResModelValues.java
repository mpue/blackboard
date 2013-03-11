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
public class ResModelValues extends ResValues implements Model {

    protected double tc1;
    protected double tc2;

    /* Sheet resistance in  ohms / square */
    protected double sheetRes;

    /* Amount by which device is narrower than drawn */
    protected double narrow;

    /* Amount by which device is shorter than drawn */
    protected double shorter;

    /* Flicker noise coefficient */
    protected double fNcoef;

    /* Flicker noise exponent */
    protected double fNexp;

    /* Not Used */
    protected double tNom;

    ;

    public double getfNcoef() {
        return fNcoef;
    }

    public void setfNcoef(double fNcoef) {
        this.fNcoef = fNcoef;
    }

    public double getfNexp() {
        return fNexp;
    }

    public void setfNexp(double fNexp) {
        this.fNexp = fNexp;
    }

    public double getNarrow() {
        return narrow;
    }

    public void setNarrow(double narrow) {
        this.narrow = narrow;
    }

    public double getSheetRes() {
        return sheetRes;
    }

    public void setSheetRes(double sheetRes) {
        this.sheetRes = sheetRes;
    }

    public double getShorter() {
        return shorter;
    }

    public void setShorter(double shorter) {
        this.shorter = shorter;
    }

    public double gettNom() {
        return tNom;
    }

    public void settNom(double tNom) {
        this.tNom = tNom;
    }

    public double getTc1() {
        return tc1;
    }

    public void setTc1(double tc1) {
        this.tc1 = tc1;
    }

    public double getTc2() {
        return tc2;
    }

    public void setTc2(double tc2) {
        this.tc2 = tc2;
    }
}
