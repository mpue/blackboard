/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;

import javolution.lang.MathLib;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISrcAM extends ISrcInstance {

    private Temporal tmprl;
    private double offsetValue;
    private double amplitude;
    private double modulFreq;
    private double carrierFreq;
    private double delayTime;
    private double phasec;
    private double phases;
    private double _phasec;
    private double _phases;

    public ISrcAM() {
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

    public double getOffsetValue() {
        return offsetValue;
    }

    public void setOffset(double offsetValue) {
        this.offsetValue = offsetValue;
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
        double value;
        double time;
        if (mode.contains(MODE.DC)) {
            time = 0;
        } else {
            time = tmprl.getTime();
        }
        time -= delayTime;
        if (time <= 0) {
            value = 0;
        } else {
            value = amplitude * (offsetValue + Math.sin(2.0 * Math.PI * modulFreq * time + _phases))
                    * Math.sin(2 * Math.PI * carrierFreq * time + _phases);
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
