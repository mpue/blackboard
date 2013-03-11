/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.analysis.Analysis;

/**
 *
 * @author Kristopher T. Beck
 */
public class VSrcPWL extends VSrcInstance {

    private Temporal tmprl;
    private double[] times;
    private double[] values;

    public VSrcPWL() {
    }

    @Override
    public boolean init(Circuit ckt) {
        tmprl = ckt.getTemporal();
        return super.init(ckt);
    }

    public double[] getTimes() {
        return times;
    }

    public void setTimes(double[] times) {
        this.times = times;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    @Override
    protected double computeValue(Mode mode) {
        double time = 0;
        double value;
        int functionOrder = times.length;
        repeat:
        do {
            if (mode.contains(MODE.DC)) {
                time = 0;
            } else {
                time = tmprl.getTime();
            }
            if (time < times[0]) {
                value = (values[0]);
                break;
            }
            for (int i = 0; i < functionOrder - 1; i++) {
                if (Analysis.approx(times[i], time, 3)) {
                    value = values[i];
                    break repeat;
                } else if (times[i] < time && times[i + 1] > time) {
                    value = (values[i] + time - times[i])
                            / (times[i + 1] - times[i])
                            * (values[i + 1] - values[i]);
                    break repeat;
                }
            }
            value = values[functionOrder - 1];
            break;
        } while (false);
        return value;
    }

    @Override
    public boolean accept(Mode mode) {
        if (!mode.contains(MODE.TRAN, MODE.TRANOP)) {
            return true;
        }
        if (tmprl.getTime() < times[0]) {
            if (tmprl.isBreak() && tmprl.addBreak(times[0])) {
                return true;
            }
        }
        for (int i = 0; i < times.length - 1; i++) {
            if (tmprl.isBreak() && Analysis.approx(times[i], tmprl.getTime(), 3)) {
                if (!tmprl.addBreak(times[i + 1])) {
                    return false;
                }
            }
        }
        return true;
    }
}
