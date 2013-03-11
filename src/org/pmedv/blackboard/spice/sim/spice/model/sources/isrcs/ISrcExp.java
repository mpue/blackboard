/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISrcExp extends ISrcInstance {

    private Temporal tmprl;
    private double initValue;
    private double pulsedValue;
    private double riseDelay;
    private double riseConst;
    private double fallDelay;
    private double fallConst;

    public ISrcExp() {
    }

    @Override
    public boolean init(Circuit ckt) {
        tmprl = ckt.getTemporal();
        return super.init(ckt);
    }

    public double getFallConst() {
        return fallConst;
    }

    public void setFallConst(double fallConst) {
        this.fallConst = fallConst;
        initialized = false;
    }

    public double getFallDelay() {
        return fallDelay;
    }

    public void setFallDelay(double fallDelay) {
        this.fallDelay = fallDelay;
        initialized = false;
    }

    public double getInitValue() {
        return initValue;
    }

    public void setInitValue(double initValue) {
        this.initValue = initValue;
    }

    public double getPulsedValue() {
        return pulsedValue;
    }

    public void setPulsedValue(double pulsedValue) {
        this.pulsedValue = pulsedValue;
    }

    public double getRiseConst() {
        return riseConst;
    }

    public void setRiseConst(double riseConst) {
        this.riseConst = riseConst;
        initialized = false;
    }

    public double getRiseDelay() {
        return riseDelay;
    }

    public void setRiseDelay(double riseDelay) {
        this.riseDelay = riseDelay;
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
        if (time <= riseDelay) {
            value = initValue;
        } else if (time <= fallDelay) {
            value = initValue + (pulsedValue - initValue) * (1 - Math.exp(-(time - riseDelay) / riseConst));
        } else {
            value = initValue + (pulsedValue - initValue) * (1 - Math.exp(-(time - riseDelay) / riseConst))
                    + (initValue - pulsedValue) * (1 - Math.exp(-(time - fallDelay) / fallConst));
        }
        return value;
    }

    @Override
    protected void initParams() {
        if (riseDelay == 0) {
            riseDelay = tmprl.getStep();
        }
        if (riseConst == 0) {
            riseConst = tmprl.getStep();
        }
        if (fallDelay == 0) {
            fallDelay = riseDelay + tmprl.getStep();
        }
        if (fallConst == 0) {
            fallConst = tmprl.getStep();
        }
        initialized = true;
    }
}
