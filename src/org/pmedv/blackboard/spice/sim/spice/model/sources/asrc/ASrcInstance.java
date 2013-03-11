/*
 * asrc.java
 *
 * Created on August 28, 2006, 1:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources.asrc;

import java.util.Arrays;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.StateTable;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.StateTable.StateVector;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentElement;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageElement;

import javolution.lang.MathLib;
import javolution.util.FastTable;
import javolution.util.StandardLog;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class ASrcInstance extends ASrcValues implements Instance {

    protected Circuit ckt;
    protected WorkEnv wrk;
    protected EnvVars env;
    protected StateTable stateTable;
    protected Temporal tmprl;
    /* Positive node index */
    protected int posIndex;

    /* Negative node Index */
    protected int negIndex;

    /* Branch equation index  for v source */
    protected int branchIndex;
    protected Real rhs = Real.zero();
    /* Previous value */
    protected double prevValue;
    protected double[] values, derivs;
    /* Store rhs and derivatives for ac anal */
    protected double[] acValues;

    /* Controlling current branch index */
    protected int contBranch;
    protected FastTable<ASRCElement> elements = new FastTable();
    protected StateVector vOldStates;
    protected StateVector contVOldStates;

    public int getNegIndex() {
        return negIndex;
    }

    public void setNegIndex(int negIndex) {
        this.negIndex = negIndex;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

    /** Creates a new instance of asrc */
    public ASrcInstance() {
    }

    public boolean evaluate() {
        return false;
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        stateTable = ckt.getStateTable();
        vOldStates = stateTable.createRow();
        contVOldStates = stateTable.createRow();
        ASRCElement elem;
        for (int i = 0; i < elements.size(); i++) {
            elem = elements.get(i);
            if (elem instanceof CurrentElement) {
                contBranch = ckt.findBranch(((CurrentElement) elem).getContName());
                if (contBranch == 0) {
                    StandardLog.severe(getInstName() + ": unknown controlling "
                            + "source " + ((CurrentElement) elem).getContName());
                    return false;
                }
            }
            elem.init();
        }
        return true;
    }

    public boolean acLoad(Mode mode) {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).acload(acValues[i]);
        }
        return true;
    }

    public boolean convTest(Mode mode) {
        int size = elements.size();
        if (values.length < size) {
            values = new double[size];
            derivs = new double[size];
        }
        for (int i = 0; i < size; i++) {
            ASRCElement elem = elements.get(i);
            if (elem instanceof CurrentElement) {
                int _branch = ckt.findBranch(((CurrentElement) elem).getContName());
                values[i] = wrk.getRhsOldAt(_branch).getReal();
            } else {
                int nodeNum = ((VoltageElement) elem).getNode1Index();
                values[i] = wrk.getRhsOldAt(nodeNum).getReal();
            }
        }
//?!        env.getGMin(),  values, derivs;
        if (evaluate()) {
            double diff = MathLib.abs(prevValue - rhs.get());
            double tol;
            if (this instanceof ASrcVoltage) {
                tol = env.getRelTol()
                        * MathLib.max(MathLib.abs(rhs.get()), MathLib.abs(prevValue)) + env.getVoltTol();
            } else {
                tol = env.getRelTol()
                        * MathLib.max(MathLib.abs(rhs.get()), MathLib.abs(prevValue)) + env.getAbsTol();
            }
            if (diff <= tol) {
                return true;
            }
        }
        return false;
    }

    public boolean load(Mode mode) {
        int size = elements.size();
        boolean result = true;
        if (values.length < size) {
            values = new double[size];
            derivs = new double[size];
        }
        for (int i = 0; i < size; i++) {
            ASRCElement elem = elements.get(i);
            if (elem instanceof CurrentElement) {
                int _branch = ckt.findBranch(((CurrentElement) elem).getContName());
                values[i] = wrk.getRhsOldAt(_branch).getReal();
            } else {
                int nodeNum = ((VoltageElement) elem).getNode1Index();
                values[i] = wrk.getRhsOldAt(nodeNum).getReal();
            }
        }
//?        env.getGMin(), values, derivs
        if (evaluate()) {
            prevValue = rhs.get();
            if (mode.contains(MODE.INIT_SMSIG)) {
                acValues = Arrays.copyOf(derivs, size);
            }
            for (int i = 0; i < size; i++) {
                rhs.minusEq(values[i] * derivs[i]);
                elements.get(i).load(derivs[i]);
            }
            if (this instanceof ASrcCurrent) {
                wrk.getRhsAt(this.branchIndex).realPlusEq(rhs);
            } else {
                wrk.getRhsAt(posIndex).realMinusEq(rhs);
            }
            wrk.getRhsAt(negIndex).realPlusEq(rhs);
            if (mode.contains(MODE.INIT_SMSIG)) {
                acValues[acValues.length - 1] = rhs.get();
            }
        } else {
            return false;
        }
        return result;
    }

    public boolean unSetup() {
        if (branchIndex != 0) {
            ckt.deleteNode(branchIndex);
        }
        branchIndex = 0;
        return true;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean temperature() {
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return timeStep;
    }

    public boolean loadInitCond() {
        return true;
    }
}
