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
public class ISrcPulse extends ISrcInstance {

    private Temporal tmprl;
    private double initValue;
    private double pulsedValue;
    private double delayTime;
    private double riseTime;
    private double fallTime;
    private double pulseWidth;
    private double period;
    private double phase;
    private double deltaT;

    public ISrcPulse() {
    }

    @Override
    public boolean init(Circuit ckt) {
        tmprl = ckt.getTemporal();
        return super.init(ckt);
    }

    public double getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(double delayTime) {
        this.delayTime = delayTime;
    }

    public double getFallTime() {
        return fallTime;
    }

    public void setFallTime(double fallTime) {
        this.fallTime = fallTime;
        initialized = false;
    }

    public double getInitValue() {
        return initValue;
    }

    public void setInitValue(double initValue) {
        this.initValue = initValue;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
        initialized = false;
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
        initialized = false;
    }

    public double getPulseWidth() {
        return pulseWidth;
    }

    public void setPulseWidth(double pulseWidth) {
        this.pulseWidth = pulseWidth;
        initialized = false;
    }

    public double getPulsedValue() {
        return pulsedValue;
    }

    public void setPulsedValue(double pulsedValue) {
        this.pulsedValue = pulsedValue;
    }

    public double getRiseTime() {
        return riseTime;
    }

    public void setRiseTime(double riseTime) {
        this.riseTime = riseTime;
        initialized = false;
    }

    @Override
    public boolean accept(Mode mode) {
        if (!mode.contains(MODE.INIT_TRAN) && !mode.contains(MODE.TRANOP)) {
            return true;
        } else {
            double time = 0;
            double baseTime = 0;
            time += deltaT;
            time = tmprl.getTime() - delayTime;
            if (time >= period) {
                baseTime = period * MathLib.floor(time / period);
            }
            time -= baseTime;
            if (time <= 0.0 || time >= riseTime + pulseWidth + fallTime) {
                if (tmprl.isBreak() && MathLib.abs(time - 0.0) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + riseTime + delayTime);
                } else if (tmprl.isBreak() && MathLib.abs((riseTime + pulseWidth + fallTime) - time) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + period + delayTime);
                } else if (tmprl.isBreak() && (time == -delayTime)) {
                    tmprl.setCurrentBreak(baseTime + delayTime);
                } else if (tmprl.isBreak() && MathLib.abs(period - time) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + riseTime + period);
                }
            } else if (time >= riseTime && time <= riseTime + pulseWidth) {
                if (tmprl.isBreak() && MathLib.abs(time - riseTime) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + riseTime + pulseWidth);
                } else if (tmprl.isBreak() && MathLib.abs((riseTime + pulseWidth) - time) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + riseTime + pulseWidth + fallTime);
                }
            } else if (time > 0 && time < riseTime) {
                if (tmprl.isBreak() && MathLib.abs(time - 0) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + riseTime);
                } else if (tmprl.isBreak() && MathLib.abs(time - riseTime) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + riseTime + pulseWidth);
                }
            } else { /* time > TR + PW && < TR + PW + TF */
                if (tmprl.isBreak() && MathLib.abs(time - (riseTime + pulseWidth)) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + riseTime + pulseWidth + fallTime);
                } else if (tmprl.isBreak() && MathLib.abs(time - (riseTime + pulseWidth + fallTime)) <= 1e-7 * pulseWidth) {
                    tmprl.setCurrentBreak(baseTime + delayTime + period);
                }
            }
        }
        return true;
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
        double baseTime = 0;
        time += deltaT;
        time -= delayTime;
        if (time > period) {
            baseTime = period * MathLib.floor(time / period);
        }
        time -= baseTime;
        if (time <= 0 || time >= riseTime + pulseWidth + fallTime) {
            value = initValue;
        } else if (time >= riseTime && time <= riseTime + pulseWidth) {
            value = pulsedValue;
        } else if (time > 0 && time < riseTime) {
            value = initValue + (pulsedValue - initValue) * (time) / riseTime;
        } else { // time > TR + PW && < TR + PW + TF
            value = pulsedValue + (initValue - pulsedValue) * (time - (riseTime + pulseWidth)) / fallTime;
        }
        return value;
    }

    @Override
    protected void initParams() {
        if (riseTime == 0) {
            riseTime = tmprl.getStep();
        }
        if (fallTime == 0) {
            fallTime = tmprl.getStep();
        }
        if (pulseWidth == 0) {
            pulseWidth = tmprl.getFinalTime();
        }
        if (period == 0) {
            period = tmprl.getFinalTime();
        }
        double _phase = MathLib.toRadians(phase);
        _phase = -(2 * MathLib.PI * MathLib.floor(_phase / (2 * MathLib.PI)));
        deltaT = (_phase / (2 * MathLib.PI)) * period;
        initialized = true;
    }
}
