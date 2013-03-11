/*
 * JFET1ModelValues.java
 * 
 * Created on Nov 13, 2007, 3:20:51 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.jfet;

/**
 *
 * @author Kristopher T. Beck
 */
public class JFET1ModelValues extends JFETModelValues {

    /* Doping profile parameter */
    protected double b = 1.0;

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }
}
