/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;

import ktb.math.numbers.Complex;

/**
 *
 * @author owenbad
 */
public abstract class SourceElement {

    protected WorkEnv wrk;
    protected String name;

    public boolean init(Circuit ckt) {
        wrk = ckt.getWrk();
        return true;
    }

    public void setWrk(WorkEnv wrk) {
        this.wrk = wrk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Complex getValue();
}
