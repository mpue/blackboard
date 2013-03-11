/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISwitchValues extends SpiceDevice {

    /* Positive index */
    protected int posIndex;

    /* Negative index */
    protected int negIndex;

    /* branch of controlling current  Number*/
    protected int contBranch;

    /* controlling source Name */
    protected String contName;
    protected boolean icStateOn = false;

    public int getContBranch() {
        return contBranch;
    }

    public void setContBranch(int contBranch) {
        this.contBranch = contBranch;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public int getNegIndex() {
        return negIndex;
    }

    public void setNegIndex(int negIndex) {
        this.negIndex = negIndex;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

    public boolean isICStateOn() {
        return icStateOn;
    }

    public void setICStateOn(boolean isOn) {
        this.icStateOn = isOn;
    }
}
