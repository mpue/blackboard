/*
 * ICIS.java
 *
 * Created on May 23, 2006, 3:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentInstance;

import javolution.util.StandardLog;
import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class ICISInstance extends ICISValues implements CurrentInstance {

    private Circuit ckt;
    private WorkEnv wrk;
    protected int contBranchIndex;
    private Complex posContBrNode;
    private Complex negContBrNode;

    /**
     * Creates a new instance of ICIS
     */
    public ICISInstance() {
    }

    public boolean load(Mode mode) {
        posContBrNode.realPlusEq(coeff);
        negContBrNode.realMinusEq(coeff);
        return true;
    }

    public boolean pzLoad(Complex s) {
        posContBrNode.realPlusEq(coeff);
        negContBrNode.realMinusEq(coeff);
        return true;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        contBranchIndex = ckt.findBranch(getContName());
        if (contBranchIndex == 0) {
            StandardLog.severe(getInstName()
                    + " unknown controlling source " + getContName());
            return false;
        }
        posContBrNode = wrk.aquireNode(posIndex, contBranchIndex);
        negContBrNode = wrk.aquireNode(negIndex, contBranchIndex);
        return true;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean unSetup() {
        return true;
    }

    public boolean acLoad(Mode mode) {
        return true;
    }

    public boolean temperature() {
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
