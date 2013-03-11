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
public class ISrcSine extends ISrcInstance {

    private Temporal tmprl;
    private double offset;
    private double amplitude;
    private double frequency;
    private double delayTime;
    private double theta;
    private double phase;
    private double _phase;

    public ISrcSine() {
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

    public double getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(double delayTime) {
        this.delayTime = delayTime;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
        initialized = false;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
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
            value = offset;
        } else {
            value = offset + amplitude * Math.sin(frequency * time * 2.0 * Math.PI
                    + _phase) * Math.exp(-time * theta);
        }
        return value;
    }

    @Override
    protected void initParams() {
        if (frequency == 0) {
            frequency = 1 / tmprl.getFinalTime();
        }
        _phase = MathLib.toRadians(phase);
        initialized = true;
    }
}
