/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;

import javolution.lang.MathLib;

/**
 *
 * @author Kristopher T. Beck
 */
public class VSrcSFFM extends VSrcInstance {

    private Temporal tmprl;
    private double offset;
    private double amplitude;
    private double carrierFreq;
    private double modulFreq;
    private double signalFreq;
    private double phasec;
    private double phases;
    private double _phasec;
    private double _phases;

    public VSrcSFFM() {
    }

    @Override
    public boolean init(Circuit ckt) {
        tmprl = ckt.getTemporal();
        return super.init(ckt);
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getCarrierFreq() {
        return carrierFreq;
    }

    public void setCarrierFreq(double carrierFreq) {
        this.carrierFreq = carrierFreq;
        initialized = false;
    }

    public double getModulFreq() {
        return modulFreq;
    }

    public void setModulationIndex(double modulFreq) {
        this.modulFreq = modulFreq;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getPhasec() {
        return phasec;
    }

    public void setPhasec(double phasec) {
        this.phasec = phasec;
        initialized = false;
    }

    public double getPhases() {
        return phases;
    }

    public void setPhases(double phases) {
        this.phases = phases;
        initialized = false;
    }

    public double getSignalFreq() {
        return signalFreq;
    }

    public void setSignalFreq(double signalFreq) {
        this.signalFreq = signalFreq;
        initialized = false;
    }

    @Override
    protected double computeValue(Mode mode) {
        double time;
        double value;
        if (mode.contains(MODE.DC)) {
            time = 0;
        } else {
            time = tmprl.getTime();
        }
        value = offset + amplitude
                * Math.sin((2 * Math.PI * carrierFreq * time + _phasec)
                + modulFreq * Math.sin(2.0 * Math.PI * signalFreq * time + _phases));
        return value;
    }

    @Override
    protected void initParams() {
        if (carrierFreq == 0) {
            carrierFreq = 1 / tmprl.getFinalTime();
        }
        if (signalFreq == 0) {
            signalFreq = 1 / tmprl.getFinalTime();
        }
        _phasec = MathLib.toRadians(phasec);
        _phases = MathLib.toRadians(phases);
        initialized = true;
    }
}
