/*
 * VSrcPulse.java
 * 
 * Created on Nov 13, 2007, 3:28:15 PM
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
 * @author Adminis_trator
 */
public class VSrcPulse extends VSrcInstance {

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

    public VSrcPulse() {
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
    protected double computeValue(Mode mode) {
        double time;
        double value;
        if (mode.contains(MODE.DC)) {
            time = 0;
        } else {
            time = tmprl.getTime();
        }
        double baseTime = 0;
        time += deltaT;
        time -= delayTime;
        if (time > period) {
            baseTime = period * Math.floor(time / period);
        }
        time -= baseTime;
        if (time <= 0 || time >= riseTime + pulseWidth + fallTime) {
            value = initValue;
        } else if (time >= riseTime && time <= riseTime + pulseWidth) {
            value = pulsedValue;
        } else if (time > 0 && time < riseTime) {
            value = initValue + (pulsedValue - initValue) * (time) / riseTime;
        } else /* time > _tr + PW && < _tr + PW + TF */ {
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
        _phase = -(2 * Math.PI * Math.floor(_phase / (2 * Math.PI)));
        deltaT = (_phase / (2 * Math.PI)) * period;
        initialized = true;
    }

    @Override
    public boolean accept(Mode mode) {
        if (!mode.contains(MODE.TRAN, MODE.TRANOP)) {
            return true;
        }
        double time = 0;
        double baseTime = 0;
        time += deltaT;
        time = tmprl.getTime() - delayTime;
        if (time >= period) {
            baseTime = period * MathLib.floor(time / period);
            time -= baseTime;
        }
        double maxDiff = 1e-7 * pulseWidth;
        if (time <= 0 || time >= riseTime + pulseWidth + fallTime) {
            if (tmprl.isBreak() && MathLib.abs(time) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + riseTime + delayTime)) {
                    return false;
                }
            } else if (tmprl.isBreak() && MathLib.abs((riseTime + pulseWidth + fallTime) - time) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + period + delayTime)) {
                    return false;
                }
            } else if (tmprl.isBreak() && time == -delayTime) {
                if (!tmprl.addBreak(baseTime + delayTime)) {
                    return false;
                }
            } else if (tmprl.isBreak() && MathLib.abs(period - time) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + delayTime + riseTime + period)) {
                    return false;
                }
            }
        } else if (time >= riseTime && time <= riseTime + pulseWidth) {
            if (tmprl.isBreak() && MathLib.abs(time - riseTime) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + delayTime + riseTime + pulseWidth)) {
                    return false;
                } else if (tmprl.isBreak() && MathLib.abs((riseTime + pulseWidth) - time) <= maxDiff) {
                    if (!tmprl.addBreak(baseTime + delayTime + riseTime + pulseWidth + fallTime)) {
                        return false;
                    }
                }
            }
        } else if (time > 0 && time < riseTime) {
            if (tmprl.isBreak() && MathLib.abs(time - 0) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + delayTime + riseTime)) {
                    return false;
                }
            } else if (tmprl.isBreak() && MathLib.abs(time - riseTime) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + delayTime + riseTime + pulseWidth)) {
                    return false;
                }
            }
        } else {
            if (tmprl.isBreak() && MathLib.abs(time - (riseTime + pulseWidth)) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + delayTime + riseTime + pulseWidth + fallTime)) {
                    return false;
                }
            } else if (tmprl.isBreak() && MathLib.abs(time - (riseTime + pulseWidth + fallTime)) <= maxDiff) {
                if (!tmprl.addBreak(baseTime + delayTime + period)) {
                    return false;
                }
            }
        }
        return true;
    }
}
