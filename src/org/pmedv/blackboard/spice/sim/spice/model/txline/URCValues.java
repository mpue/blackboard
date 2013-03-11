/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.model.txline;

import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

/**
 *
 * @author Kristopher T. Beck
 */
public class URCValues extends SpiceDevice{

    /* Positive node index */
    int posIndex;

    /* Negative node Index */
    int negIndex;

    /* Ground node index */
    int gndIndex;

    /* Length of line */
    double length;

    /* Number of lumps in line */
    int lumps;

    String modName;

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }
    
    public int getGndIndex() {
        return gndIndex;
    }

    public void setGndIndex(int gndIndex) {
        this.gndIndex = gndIndex;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getLumps() {
        return lumps;
    }

    public void setLumps(int lumps) {
        this.lumps = lumps;
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

}
