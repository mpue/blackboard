/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs;

import org.pmedv.blackboard.spice.sim.spice.model.sources.DependentLinearSource;

/**
 *
 * @author Kristopher T. Beck
 */
public class ICVSValues extends DependentLinearSource {

    protected String contName;
    protected double coeff;

    public double getCoeff() {
        return coeff;
    }

    public void setCoeff(double coeff) {
        this.coeff = coeff;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }    
}
