/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.model.txline;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author Kristopher T. Beck
 */
public class URCModelValues extends URCValues implements Model{

    /* Propagation constant */
    double k = 1.5;

    /* Max frequence  */
    double fmax = 1e9;

    /* Resistance per unit length */
    double rPerL = 1000;

    /* Capacitance per unit length */
    double cPerL = 1e-12;

    /* Diode saturation current per unit length */
    double iSatPerL;

    /* Diode resistance per unit length */
    double rsPerL;

    public double getcPerL() {
        return cPerL;
    }

    public void setcPerL(double cPerL) {
        this.cPerL = cPerL;
    }

    public double getFmax() {
        return fmax;
    }

    public void setFmax(double fmax) {
        this.fmax = fmax;
    }

    public double getiSatPerL() {
        return iSatPerL;
    }

    public void setiSatPerL(double iSatPerL) {
        this.iSatPerL = iSatPerL;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getrPerL() {
        return rPerL;
    }

    public void setrPerL(double rPerL) {
        this.rPerL = rPerL;
    }

    public double getRsPerL() {
        return rsPerL;
    }

    public void setRsPerL(double rsPerL) {
        this.rsPerL = rsPerL;
    }

}
