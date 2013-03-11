/*
 * HFET2ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:20:32 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.hfet;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFET2ModelValues extends HFETModelValues {

    protected double cf;
    protected double js;
    protected double knmax;
    protected double n = 5.0;

    public HFET2ModelValues() {
        m = 1;
    }

    public double getCf() {
        return cf;
    }

    public void setCf(double cf) {
        this.cf = cf;
    }

    public double getJs() {
        return js;
    }

    public void setJs(double js) {
        this.js = js;
    }

    public double getKnmax() {
        return knmax;
    }

    public void setKnmax(double knmax) {
        this.knmax = knmax;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }
}
