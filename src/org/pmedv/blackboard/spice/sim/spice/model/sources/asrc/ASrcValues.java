/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.asrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.SpiceSource;

import ktb.math.func.Function;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class ASrcValues extends SpiceSource {

    Function<Real> function;

    public Function<Real> getFunction() {
        return function;
    }

    public void setFunction(Function<Real> function) {
        this.function = function;
    }
}
