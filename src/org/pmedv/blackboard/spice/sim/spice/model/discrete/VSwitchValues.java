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
public class VSwitchValues extends SpiceDevice {

    /* Positive node Index */
    protected int posIndex;

    /* Negative node Index */
    protected int negIndex;

    /* Positive controlling node Index */
    protected int posCtrlIndex;

    /* Negative controlling node Index */
    protected int negCtrlIndex;

    /* Flag to indicate initial state */
    protected boolean icStateOn;

    public boolean isIcStateOn() {
        return icStateOn;
    }

    public void setIcStateOn(boolean icStateOn) {
        this.icStateOn = icStateOn;
    }

    public int getNegCtrlIndex() {
        return negCtrlIndex;
    }

    public void setNegCtrlIndex(int negCtrlIndex) {
        this.negCtrlIndex = negCtrlIndex;
    }

    public int getNegIndex() {
        return negIndex;
    }

    public void setNegIndex(int negIndex) {
        this.negIndex = negIndex;
    }

    public int getPosCtrlIndex() {
        return posCtrlIndex;
    }

    public void setPosCtrlIndex(int posCtrlIndex) {
        this.posCtrlIndex = posCtrlIndex;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

}
