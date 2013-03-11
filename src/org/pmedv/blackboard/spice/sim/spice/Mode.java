/*
 * Mode.java
 *
 * Created on June 28, 2006, 4:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice;

import java.util.EnumSet;

import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;


/**
 *
 * @author Kristopher T. Beck
 */
public class Mode {

    EnumSet<MODE> modes;
    public static final EnumSet<MODE> initFlags = EnumSet.of(MODE.INIT_JCT, MODE.INIT_FIX, MODE.INIT_FLOAT, MODE.INIT_SMSIG, MODE.INIT_PRED, MODE.INIT_TRAN);
    private boolean useIC;

    /** Creates a new instance of Mode */
    public EnumSet<MODE> getModes() {
        return modes;
    }

    public Mode(EnumSet<MODE> modes) {
        this.modes = modes;
    }

    public void add(MODE mode) {
        modes.add(mode);
    }

    public void setUseIC(boolean useIC) {
        this.useIC = useIC;
    }

    public void setModes(EnumSet<MODE> modes) {
        this.modes = modes;
    }

    public void setModes(MODE mode1) {
        modes = EnumSet.of(mode1);
    }

    public void setModes(MODE mode1, MODE mode2) {
        modes = EnumSet.of(mode1, mode2);
    }

    public void setModes(MODE mode1, MODE mode2, MODE mode3) {
        modes = EnumSet.of(mode1, mode2, mode3);
    }

    public void setModes(MODE mode1, MODE mode2, MODE mode3, MODE mode4) {
        modes = EnumSet.of(mode1, mode2, mode3, mode4);
    }

    public void setModes(MODE mode1, MODE mode2, MODE mode3, MODE mode4, MODE mode5) {
        modes = EnumSet.of(mode1, mode2, mode3, mode4, mode5);
    }

    public boolean contains(MODE mode1, MODE mode2) {
        if (modes.contains(mode1) || modes.contains(mode2)) {
            return true;
        }
        return false;
    }

    public boolean contains(MODE mode1, MODE mode2, MODE mode3) {
        if (modes.contains(mode1) || modes.contains(mode2)
                || modes.contains(mode3)) {
            return true;
        }
        return false;
    }

    public boolean contains(MODE mode1, MODE mode2, MODE mode3, MODE mode4) {
        if (modes.contains(mode1) || modes.contains(mode2)
                || modes.contains(mode3) || modes.contains(mode4)) {
            return true;
        }
        return false;
    }

    public boolean contains(MODE mode1, MODE mode2, MODE mode3, MODE mode4, MODE mode5) {
        if (modes.contains(mode1) || modes.contains(mode2)
                || modes.contains(mode3) || modes.contains(mode4)
                || modes.contains(mode5)) {
            return true;
        }
        return false;
    }

    public boolean contains(MODE mode1) {
        if (mode1 == MODE.DC) {
            return contains(MODE.DCOP, MODE.TRANOP, MODE.DCTRANCURVE);
        }
        return modes.contains(mode1);
    }

    public boolean containsAll(MODE mode1, MODE mode2) {
        return modes.containsAll(EnumSet.of(mode1, mode2));
    }

    public boolean containsAll(MODE mode1, MODE mode2, MODE mode3) {
        return modes.containsAll(EnumSet.of(mode1, mode2, mode3));
    }

    public boolean containsAll(MODE mode1, MODE mode2, MODE mode3, MODE mode4) {
        return modes.containsAll(EnumSet.of(mode1, mode2, mode3, mode4));
    }

    public boolean containsAll(MODE mode1, MODE mode2, MODE mode3, MODE mode4, MODE mode5) {
        return modes.containsAll(EnumSet.of(mode1, mode2, mode3, mode4, mode5));
    }

    public boolean isUseIC() {
        return useIC;
    }

    public boolean containsAll(EnumSet<MODE> set) {
        return modes.containsAll(set) ? true : false;
    }

    public void removeAllInitFlags() {
        modes.removeAll(initFlags);
    }
}
