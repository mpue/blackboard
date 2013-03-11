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
public class VSrcAM extends VSrcInstance {

    private Temporal tmprl;
    private double offset;
    private double amplitude;
    private double modulFreq;
    private double carrierFreq;
    private double delayTime;
    private double phasec;
    private double phases;
    private double _phasec;
    private double _phases;

    public VSrcAM() {
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
    }

    public double getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(double delayTime) {
        this.delayTime = delayTime;
    }

    public double getModulFreq() {
        return modulFreq;
    }

    public void setModulFreq(double modulFreq) {
        this.modulFreq = modulFreq;
        initialized = false;
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

    @Override
    protected double computeValue(Mode mode) {
        double time;
        double value;
        if (mode.contains(MODE.DC)) {
            time = 0;
        } else {
            time = tmprl.getTime();
        }
        time -= delayTime;
        if (time <= 0) {
            value = 0;
        } else {
            value = amplitude * (offset + MathLib.sin(2.0 * MathLib.PI * modulFreq * time + _phasec))
                    * MathLib.sin(2 * MathLib.PI * carrierFreq * time + _phases);
        }
        return value;
    }

    @Override
    protected void initParams() {
        if (modulFreq == 0) {
            modulFreq = 1 / tmprl.getFinalTime();
        }
        _phasec = MathLib.toRadians(phasec);
        _phases = MathLib.toRadians(phases);
        initialized = true;
    }
}
