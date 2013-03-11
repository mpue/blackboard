/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.discrete;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;

import javolution.lang.MathLib;
import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class MutInstance extends MutValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private Temporal tmprl;
    /* Mutual inductance factor */
    private double factor;

    /* Reference to coupled inductor 1 */
    private IndInstance ind1;

    /* Reference to coupled inductor 2 */
    private IndInstance ind2;
    private Complex br1br2Node;
    private Complex br2br1Node;

    public IndInstance getInd1() {
        return ind1;
    }

    public void setInd1(IndInstance ind1) {
        this.ind1 = ind1;
    }

    public IndInstance getInd2() {
        return ind2;
    }

    public void setInd2(IndInstance ind2) {
        this.ind2 = ind2;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        tmprl = ckt.getTemporal();
        ind1 = (IndInstance) ckt.findInstance(ind1Name);
        ind2 = (IndInstance) ckt.findInstance(ind2Name);
        br1br2Node = wrk.aquireNode(ind1.getBrEqIndex(),
                ind2.getBrEqIndex());
        br2br1Node = wrk.aquireNode(ind2.getBrEqIndex(),
                ind1.getBrEqIndex());
        return true;
    }

    public boolean unSetup() {
        return true;
    }

    public boolean load(Mode mode) {
        if (!mode.contains(MODE.DC, MODE.INIT_PRED)) {
            ind1.getFluxStates().set(0, ind1.getFluxStates().get(0)
                    + factor * wrk.getRhsOldRealAt(ind2.getBrEqIndex()));
            ind2.getFluxStates().set(0, ind2.getFluxStates().get(0)
                    + factor * wrk.getRhsOldRealAt(ind1.getBrEqIndex()));
        }
        br1br2Node.realMinusEq(factor * tmprl.getAgAt(0));
        br2br1Node.realMinusEq(factor * tmprl.getAgAt(0));
        return true;
    }

    public boolean acLoad(Mode mode) {
        double val = tmprl.getOmega() * factor;
        br1br2Node.imagMinusEq(val);
        br2br1Node.imagMinusEq(val);
        return true;
    }

    public boolean temperature() {
        factor = coupling * MathLib.sqrt(ind1.getInduct() * ind2.getInduct());
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return timeStep;
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}
