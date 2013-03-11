/*
 * StateTable.java
 *
 * Created on Aug 13, 2007, 1:19:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice;

import java.util.Arrays;

import javolution.lang.MathLib;
import javolution.util.FastList;
import javolution.util.StandardLog;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class StateTable {

    private EnvVars env;
    private Temporal tmprl;
    private double[][] states = new double[8][];
    private FastList<StateVector> rows = FastList.newInstance();
    private boolean modified;

    public StateTable(Circuit ckt) {
        env = ckt.getEnv();
        tmprl = ckt.getTemporal();
    }

    public double[][] getStates() {
        return states;
    }
//TODO check if i messed up row & col major

    public void initArrays() {
        int size = rows.size();
        states[0] = new double[size];
        states[1] = new double[size];
        states[2] = new double[size];
        states[3] = new double[size];
        states[4] = new double[size];
        states[5] = new double[size];
        states[6] = new double[size];
        states[7] = new double[size];
        modified = false;
        clear();
    }

    public void clear() {
        for (int i = 0; i < 8; i++) {
            clearColumn(i);
        }
    }

    public void clearColumn(int order) {
        Arrays.fill(states[order], 0);
    }

    public double getStateAt(int tag, int order) {
        if (modified) {
            initArrays();
        }
        return states[order][tag];
    }

    public void setStateAt(int tag, int order, double value) {
        states[order][tag] = value;
    }

    public void copySwapColumn(int src, int dest) {
        states[dest] = Arrays.copyOf(states[src], rows.size());
    }

    public double[] getColumnAt(int col) {
        return states[col];
    }

    public void setColumnAt(int col, double[] array) {
        states[col] = array;
    }

    public void swapColumns(int col1, int col2) {
        double[] tmp = states[col1];
        states[col1] = states[col2];
        states[col2] = tmp;
    }

    public int getSize() {
        return rows.size();
    }

    public class StateVector {

        private int tag = 0;

        public int getTag() {
            return tag;
        }

        public double[] get() {
            return states[tag];
        }

        public double get(int order) {
            if (modified) {
                initArrays();
            }
            return states[order][tag];
        }

        public void set(int order, double value) {
            states[order][tag] = value;
        }
    }

    public StateVector createRow() {
        StateVector row = new StateVector();
        rows.add(row);
        row.tag = rows.size() - 1;
        modified = true;
        return row;
    }

    public void deleteRow(StateVector row) {
        rows.remove(row);
        int tag = 0;
        for (FastList.Node<StateVector> r = rows.head(), end = rows.tail();
                (r = r.getNext()) != end;) {
            r.getValue().tag = tag++;
        }
        modified = true;
    }

    public boolean integrate(Real gEq, Real iEq, double cap, StateVector qcapStates, StateVector ccapStates) {
        int qcap = qcapStates.getTag();
        int ccap = ccapStates.getTag();
        switch (env.getIntegMethod()) {
            case TRAPEZOIDAL:
                switch (tmprl.getOrder()) {
                    case 1:
                        states[0][ccap] = tmprl.getAgAt(0) * states[0][qcap]
                                + tmprl.getAgAt(1) * states[1][qcap];
                        break;
                    case 2:
                        states[0][ccap] = -states[1][ccap]
                                * tmprl.getAgAt(1) + tmprl.getAgAt(0)
                                * (states[0][qcap] - states[1][qcap]);
                        break;
                    default:
                        StandardLog.warning("Illegal integration order");
                        return false;
                }
                break;
            case GEAR:
                states[0][ccap] = 0;
                switch (tmprl.getOrder()) {
                    case 6:
                        states[0][ccap] += tmprl.getAgAt(6) * states[6][qcap];
                    case 5:
                        states[0][ccap] += tmprl.getAgAt(5) * states[5][qcap];
                    case 4:
                        states[0][ccap] += tmprl.getAgAt(4) * states[4][qcap];
                    case 3:
                        states[0][ccap] += tmprl.getAgAt(3) * states[3][qcap];
                    case 2:
                        states[0][ccap] += tmprl.getAgAt(2) * states[2][qcap];
                    case 1:
                        states[0][ccap] += tmprl.getAgAt(1) * states[1][qcap];
                        states[0][ccap] += tmprl.getAgAt(0) * states[0][qcap];
                        break;
                    default:
                        StandardLog.warning("Inproper order: " + tmprl.getOrder());
                        return false;
                }
                break;
            default:
                StandardLog.warning("Unknown integration method");
                return false;
        }
        gEq.set(tmprl.getAgAt(0) * cap);
        iEq.set(states[0][ccap] - tmprl.getAgAt(0) * states[0][qcap]);
//        System.out.println("ag[0] = " + tmprl.getAgAt(0) + " ag[1] = " + tmprl.getAgAt(1));
//        System.out.println("ceq = " + iEq + " geq = " + gEq);
        return true;
    }
    private double gearCoeff[] = {
        .5,
        .2222222222,
        .1363636364,
        .096,
        .07299270073,
        .05830903790
    };
    private double trapCoeff[] = {
        .5,
        .08333333333
    };
    private double[] diff = new double[8];
    private double[] deltmp = new double[8];

    public double terr(StateVector qcapStates, StateVector ccapStates, double timeStep) {
        int qcap = qcapStates.tag;
        int ccap = ccapStates.tag;
//        System.out.println("timeStep = " + timeStep);
        double voltTol = env.getAbsTol() + env.getRelTol()
                * MathLib.max(MathLib.abs(
                states[0][ccap]), MathLib.abs(states[1][ccap]));
        double chargeTol = MathLib.max(MathLib.abs(states[0][qcap]),
                MathLib.abs(states[1][qcap]));
        chargeTol = env.getRelTol() * MathLib.max(chargeTol,
                env.getChgTol()) / tmprl.getDelta();
        double tol = MathLib.max(voltTol, chargeTol);
  //      System.out.println("state0ccap = " + states[0][ccap] + " state0qcap = " + states[0][qcap]);
//        System.out.println("voltTol = " + voltTol + " chargeTol = " + chargeTol + " tol =" + tol);

        for (int i = tmprl.getOrder() + 1; i >= 0; i--) {
            diff[i] = states[i][qcap];
        }
        for (int i = 0; i <= tmprl.getOrder(); i++) {
            deltmp[i] = tmprl.getOldDeltaAt(i);
        }
        int j = tmprl.getOrder();
        while (true) {
            for (int i = 0; i <= j; i++) {
                diff[i] = (diff[i] - diff[i + 1]) / deltmp[i];
            }
            if (--j < 0) {
                break;
            }
            for (int i = 0; i <= j; i++) {
                deltmp[i] = deltmp[i + 1] + tmprl.getOldDeltaAt(i);
            }
        }
        double factor = 0;
        switch (env.getIntegMethod()) {
            case GEAR:
                factor = gearCoeff[tmprl.getOrder() - 1];
                break;
            case TRAPEZOIDAL:
                factor = trapCoeff[tmprl.getOrder() - 1];
                break;
        }
        double del = env.getTrTol() * tol / MathLib.max(env.getAbsTol(),
                factor * MathLib.abs(diff[0]));
        if (tmprl.getOrder() == 2) {
            del = MathLib.sqrt(del);
        } else if (tmprl.getOrder() > 2) {
            del = MathLib.exp(MathLib.log(del) / tmprl.getOrder());
        }
        timeStep = MathLib.min(timeStep, del);
//        System.out.println("factor = " + factor + " del =" + del + " terr = " + timeStep);
        return timeStep;
    }
}
