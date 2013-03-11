/*
 * VSrcSine.java
 * 
 * Created on Nov 13, 2007, 3:28:50 PM
 * 
 * To change this template, choose Tools | Template Manager
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
public class VSrcSine extends VSrcInstance {

    private Temporal tmprl;
    private double offsetVolt;
    private double amplitude;
    private double frequency;
    private double delay;
    private double theta;
    private double phase;
    private double _phase;

    public VSrcSine() {
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
        return delay;
    }

    public void setDelayTime(double delay) {
        this.delay = delay;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
        initialized = false;
    }

    public double getOffset() {
        return offsetVolt;
    }

    public void setOffset(double offsetVolt) {
        this.offsetVolt = offsetVolt;
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
        initialized = false;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
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
        time -= delay;
        if (time <= 0) {
            value = offsetVolt;
        } else {
            value = offsetVolt + amplitude * Math.sin(frequency * time * 2.0 * Math.PI + _phase)
                    * Math.exp(-time * theta);
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
