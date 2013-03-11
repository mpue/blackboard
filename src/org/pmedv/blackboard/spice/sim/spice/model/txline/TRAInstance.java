/*
 * tra.java
 *
 * Created on August 28, 2006, 1:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.txline;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.SpiceDevice;

import javolution.lang.MathLib;
import javolution.util.StandardLog;
import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class TRAInstance extends SpiceDevice implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private Temporal tmprl;
    /* Positive node index of end 1 */
    protected int posIndex1;

    /* Negative node index of end 1 */
    protected int negIndex1;

    /* Positive node index of end 2 */
    protected int posIndex2;

    /* Negative node index of end 2 */
    protected int negIndex2;

    /* Internal node index of end 1 */
    protected int intIndex1;

    /* Internal node index of end 2 */
    protected int intIndex2;

    /* Impedance */
    protected double imped;

    /* Conductance */
    protected double conduct;

    /* Propagation delay */
    protected double propDelay;

    /* Normalized length */
    protected double normLength = 0.25;

    /* Frequency at which nl is measured */
    protected double freq = 1e9;

    /* Accumulated excitation for port 1 */
    protected double input1;

    /* Accumulated excitation for port 2 */
    protected double input2;

    /* Initial condition:  voltage on port 1 */
    protected double initVolt1;

    /* Initial condition:  current at port 1 */
    protected double initCur1;

    /* Initial condition:  voltage on port 2 */
    protected double initVolt2;

    /* Initial condition:  current at port 2 */
    protected double initCur2;

    /* Relative deriv. tol. for breakpoint setting */
    protected double reltol = 1;

    /* Absolute deriv. tol. for breakpoint setting */
    protected double abstol = 1;

    /* Delayed excitation values */
    protected double[] delays = new double[15];

    /* Size of active delayed table */
    protected int sizeDelay;

    /* Allocated size of delayed table */
    protected int allocDelay = 4;

    /* Branch equation index for end 1 */
    protected int brq1Index;

    /* Branch equation index for end 2 */
    protected int brq2Index;
    private Complex ibr1Ibr2Node;
    private Complex ibr1Int1Node;
    private Complex ibr1Neg1Node;
    private Complex ibr1Neg2Node;
    private Complex ibr1Pos2Node;
    private Complex ibr2Ibr1Node;
    private Complex ibr2Int2Node;
    private Complex ibr2Neg1Node;
    private Complex ibr2Neg2Node;
    private Complex ibr2Pos1Node;
    private Complex int1Ibr1Node;
    private Complex int1Int1Node;
    private Complex int1Pos1Node;
    private Complex int2Ibr2Node;
    private Complex int2Int2Node;
    private Complex int2Pos2Node;
    private Complex neg1Ibr1Node;
    private Complex neg2Ibr2Node;
    private Complex pos1Int1Node;
    private Complex pos1Pos1Node;
    private Complex pos2Int2Node;
    private Complex pos2Pos2Node;

    /** Creates a new instance of tra */
    public TRAInstance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        tmprl = ckt.getTemporal();
        if (brq1Index == 0) {
            brq1Index = ckt.makeVoltNode(getInstName() + "i1").getIndex();
        }
        if (brq2Index == 0) {
            brq2Index = ckt.makeVoltNode(getInstName() + "i2").getIndex();
        }
        if (intIndex1 == 0) {
            intIndex1 = ckt.makeVoltNode(getInstName() + "int1").getIndex();
        }
        if (intIndex2 == 0) {
            intIndex2 = ckt.makeVoltNode(getInstName() + "int2").getIndex();
        }
        ibr1Ibr2Node = wrk.aquireNode(brq1Index, brq2Index);
        ibr1Int1Node = wrk.aquireNode(brq1Index, intIndex1);
        ibr1Neg1Node = wrk.aquireNode(brq1Index, negIndex1);
        ibr1Neg2Node = wrk.aquireNode(brq1Index, negIndex2);
        ibr1Pos2Node = wrk.aquireNode(brq1Index, posIndex2);
        ibr2Ibr1Node = wrk.aquireNode(brq2Index, brq1Index);
        ibr2Int2Node = wrk.aquireNode(brq2Index, intIndex2);
        ibr2Neg1Node = wrk.aquireNode(brq2Index, negIndex1);
        ibr2Neg2Node = wrk.aquireNode(brq2Index, negIndex2);
        ibr2Pos1Node = wrk.aquireNode(brq2Index, posIndex1);
        int1Ibr1Node = wrk.aquireNode(intIndex1, brq1Index);
        int1Int1Node = wrk.aquireNode(intIndex1, intIndex1);
        int1Pos1Node = wrk.aquireNode(intIndex1, posIndex1);
        int2Ibr2Node = wrk.aquireNode(intIndex2, brq2Index);
        int2Int2Node = wrk.aquireNode(intIndex2, intIndex2);
        int2Pos2Node = wrk.aquireNode(intIndex2, posIndex2);
        neg1Ibr1Node = wrk.aquireNode(negIndex1, brq1Index);
        neg2Ibr2Node = wrk.aquireNode(negIndex2, brq2Index);
        pos1Int1Node = wrk.aquireNode(posIndex1, intIndex1);
        pos1Pos1Node = wrk.aquireNode(posIndex1, posIndex1);
        pos2Int2Node = wrk.aquireNode(posIndex2, intIndex2);
        pos2Pos2Node = wrk.aquireNode(posIndex2, posIndex2);
        if (imped == 0) {
            StandardLog.severe(getInstName() + ": transmission line z0 must be given");
            return false;
        }
        return true;
    }

    public boolean unSetup() {
        if (brq1Index != 0) {
            ckt.deleteNode(brq1Index);
            brq1Index = 0;
        }
        if (brq2Index != 0) {
            ckt.deleteNode(brq2Index);
            brq2Index = 0;
        }
        if (intIndex1 != 0) {
            ckt.deleteNode(intIndex1);
            intIndex1 = 0;
        }
        if (intIndex2 != 0) {
            ckt.deleteNode(intIndex2);
            intIndex2 = 0;
        }
        return true;
    }

    public boolean accept(Mode mode) {
        if ((tmprl.getTime() - propDelay) > delays[6]) {
            int i = 2;
            while (i < sizeDelay && (tmprl.getTime() - propDelay > delays[3 * i])) {
                i++;
            }
            i -= 2;
            for (int j = i; j <= sizeDelay; j++) {
                int k = 3 * (j - i);
                int l = 3 * j;
                delays[k] = delays[l];
                delays[k + 1] = delays[l + 1];
                delays[k + 2] = delays[l + 2];
            }
            sizeDelay -= i;
        }
        if ((tmprl.getTime() - delays[3 * sizeDelay]) > tmprl.getMinBreak()) {
            if (allocDelay <= sizeDelay) {
                allocDelay += 5;
                delays = new double[(allocDelay + 1) * 3];
            }
            sizeDelay++;
            delays[3 * sizeDelay] = tmprl.getTime();
            delays[1 + 3 * sizeDelay] = (wrk.getRhsOldAt(posIndex2).getReal()
                    - wrk.getRhsOldAt(negIndex2).getReal())
                    + wrk.getRhsOldAt(brq2Index).getReal()
                    * imped;
            delays[2 + 3 * sizeDelay] =
                    (wrk.getRhsOldAt(posIndex1).getReal()
                    - wrk.getRhsOldAt(negIndex1).getReal())
                    + wrk.getRhsOldAt(brq1Index).getReal()
                    * imped;
            if (false) {//?
                int v1 = (int) delays[1 + 3 * sizeDelay];
                int v2 = (int) delays[1 + 3 * (sizeDelay - 1)];
                int v3 = (int) delays[2 + 3 * sizeDelay];
                int v4 = (int) delays[2 + 3 * (sizeDelay - 1)];
                if ((MathLib.abs(v1 - v2) >= 50 * env.getRelTol()
                        * MathLib.max(MathLib.abs(v1), MathLib.abs(v2)) + 50 * env.getVoltTol())
                        || (MathLib.abs(v3 - v4) >= 50 * env.getRelTol()
                        * MathLib.max(MathLib.abs(v3), MathLib.abs(v4)) + 50 * env.getVoltTol())) {
                    if (!tmprl.addBreak(tmprl.getTime() + propDelay)
                            || tmprl.addBreak(delays[3 * sizeDelay - 3] + propDelay)) {
                        tmprl.breakDump();
                    }
                }
            } else {
                int v1 = (int) delays[1 + 3 * sizeDelay];
                int v2 = (int) delays[1 + 3 * (sizeDelay - 1)];
                int v3 = (int) delays[1 + 3 * (sizeDelay - 2)];
                int v4 = (int) delays[2 + 3 * sizeDelay];
                int v5 = (int) delays[2 + 3 * (sizeDelay - 1)];
                int v6 = (int) delays[2 + 3 * (sizeDelay - 2)];
                int d1 = (int) ((v1 - v2) / tmprl.getOldDeltaAt(0));
                int d2 = (int) ((v2 - v3) / tmprl.getOldDeltaAt(1));
                int d3 = (int) ((v4 - v5) / tmprl.getOldDeltaAt(0));
                int d4 = (int) ((v5 - v6) / tmprl.getOldDeltaAt(1));
                if ((MathLib.abs(d1 - d2) >= reltol * MathLib.max(MathLib.abs(d1),
                        MathLib.abs(d2))
                        + abstol)
                        || (MathLib.abs(d3 - d4) >= reltol * MathLib.max(MathLib.abs(d3),
                        MathLib.abs(d4))
                        + abstol)) {
                    tmprl.addBreak((delays[3 * sizeDelay - 3]) + propDelay);
                }
            }
        }
        return true;
    }

    public boolean acLoad(Mode mode) {
        double real = MathLib.cos(-tmprl.getOmega() * propDelay);
        double imag = MathLib.sin(-tmprl.getOmega() * propDelay);
        pos1Pos1Node.realPlusEq(conduct);
        pos1Int1Node.realMinusEq(conduct);
        neg1Ibr1Node.realMinusEq(1);
        pos2Pos2Node.realPlusEq(conduct);
        neg2Ibr2Node.realMinusEq(1);
        int1Pos1Node.realMinusEq(conduct);
        int1Int1Node.realPlusEq(conduct);
        int1Ibr1Node.realPlusEq(1);
        int2Int2Node.realPlusEq(conduct);
        int2Ibr2Node.realPlusEq(1);
        ibr1Neg1Node.realMinusEq(1);
        ibr1Pos2Node.realMinusEq(real);
        ibr1Pos2Node.imagMinusEq(imag);
        ibr1Neg2Node.realPlusEq(real);
        ibr1Neg2Node.imagPlusEq(imag);
        ibr1Int1Node.realPlusEq(1);
        ibr1Ibr2Node.realMinusEq(real * imped);
        ibr1Ibr2Node.imagMinusEq(imag * imped);
        ibr2Pos1Node.realMinusEq(real);
        ibr2Pos1Node.imagMinusEq(imag);
        ibr2Neg1Node.realPlusEq(real);
        ibr2Neg1Node.imagPlusEq(imag);
        ibr2Neg2Node.realMinusEq(1);
        ibr2Int2Node.realPlusEq(1);
        ibr2Ibr1Node.realMinusEq(real * imped);
        ibr2Ibr1Node.imagMinusEq(imag * imped);
        pos2Int2Node.realMinusEq(conduct);
        int2Pos2Node.realMinusEq(conduct);
        return true;
    }

    @Override
    public boolean load(Mode mode) {
        pos1Pos1Node.realPlusEq(conduct);
        pos1Int1Node.realMinusEq(conduct);
        neg1Ibr1Node.realMinusEq(1);
        pos2Pos2Node.realPlusEq(conduct);
        neg2Ibr2Node.realMinusEq(1);
        int1Pos1Node.realMinusEq(conduct);
        int1Int1Node.realPlusEq(conduct);
        int1Ibr1Node.realPlusEq(1);
        int2Int2Node.realPlusEq(conduct);
        int2Ibr2Node.realPlusEq(1);
        ibr1Neg1Node.realMinusEq(1);
        ibr1Int1Node.realPlusEq(1);
        ibr2Neg2Node.realMinusEq(1);
        ibr2Int2Node.realPlusEq(1);
        pos2Int2Node.realMinusEq(conduct);
        int2Pos2Node.realMinusEq(conduct);
        if (mode.contains(MODE.DC)) {
            ibr1Pos2Node.realMinusEq(1);
            ibr1Neg2Node.realPlusEq(1);
            ibr1Ibr2Node.realMinusEq((1 - env.getGMin()) * imped);
            ibr2Pos1Node.realMinusEq(1);
            ibr2Neg1Node.realPlusEq(1);
            ibr2Ibr1Node.realMinusEq((1 - env.getGMin()) * imped);
        } else {
            if (mode.contains(MODE.INIT_TRAN)) {
                if (mode.isUseIC()) {
                    input1 = initVolt2 + initCur2 * imped;
                    input2 = initVolt1 + initCur1 * imped;
                } else {
                    input1 = (wrk.getRhsOldAt(posIndex2).getReal() - wrk.getRhsOldAt(negIndex2).getReal()) + (wrk.getRhsOldAt(brq2Index).getReal() * imped);
                    input2 = (wrk.getRhsOldAt(posIndex1).getReal() - wrk.getRhsOldAt(negIndex1).getReal()) + (wrk.getRhsOldAt(brq1Index).getReal() * imped);
                }
                delays[0] = -2 * propDelay;
                delays[3] = -propDelay;
                delays[6] = 0;
                delays[1] = delays[4] =
                        delays[7] = input1;
                delays[2] = delays[5] = delays[8] = input2;
                sizeDelay = 2;
            } else {
                if (mode.contains(MODE.INIT_PRED)) {
                    int i = 2;
                    while (i < sizeDelay && delays[3] * i <= tmprl.getTime() - propDelay) {
                        i++;
                    }
                    double t1 = delays[3 * (i - 2)];
                    double t2 = delays[3 * (i - 1)];
                    double t3 = delays[3 * i];
                    if ((t2 - t1) == 0 || (t3 - t2) == 0) {
                        return true;
                    }
                    double f1 = (tmprl.getTime() - propDelay - t2)
                            * (tmprl.getTime() - propDelay - t3);
                    double f2 = (tmprl.getTime() - propDelay - t1)
                            * (tmprl.getTime() - propDelay - t3);
                    double f3 = (tmprl.getTime() - propDelay - t1)
                            * (tmprl.getTime() - propDelay - t2);
                    if ((t2 - t1) == 0) {
                        f1 = 0;
                        f2 = 0;
                    } else {
                        f1 /= (t1 - t2);
                    }
                    f2 /= (t2 - t1);
                    if ((t3 - t2) == 0) {
                        f2 = 0;
                        f3 = 0;
                    } else {
                        f2 /= (t2 - t3);
                    }
                    f3 /= (t2 - t3);
                    if ((t3 - t1) == 0) {
                        f1 = 0;
                        f2 = 0;
                    } else {
                        f1 /= (t1 - t3);
                    }
                    f3 /= (t1 - t3);
                    input1 = f1 * delays[3 * (i - 2) + 1] + f2 * delays[3 * (i - 1) + 1] + f3 * delays[3 * (i) + 1];
                    input2 = f1 * delays[3 * (i - 2) + 2] + f2 * delays[3 * (i - 1) + 2] + f3 * delays[3 * i + 2];
                }
            }
            wrk.getRhsAt(brq1Index).realPlusEq(input1);
            wrk.getRhsAt(brq2Index).realPlusEq(input2);
        }
        return true;
    }

    public boolean temperature() {
        if (propDelay == 0) {
            propDelay = normLength / freq;
        }
        conduct = 1 / imped;
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        double v1 = (wrk.getRhsOldAt(posIndex2).getReal() - wrk.getRhsOldAt(negIndex2).getReal()) + wrk.getRhsOldAt(brq2Index).getReal() * imped;
        double v2 = delays[1 + 3 * (sizeDelay)];
        double v3 = delays[1 + 3 * (sizeDelay - 1)];
        double v4 = (wrk.getRhsOldAt(posIndex1).getReal() - wrk.getRhsOldAt(negIndex1).getReal()) + wrk.getRhsOldAt(brq1Index).getReal() * imped;
        double v5 = delays[2 + 3 * (sizeDelay)];
        double v6 = delays[2 + 3 * (sizeDelay - 1)];
        double d1 = (v1 - v2) / tmprl.getOldDeltaAt(1);
        double d2 = (v2 - v3) / tmprl.getOldDeltaAt(2);
        double d3 = (v4 - v5) / tmprl.getOldDeltaAt(1);
        double d4 = (v5 - v6) / tmprl.getOldDeltaAt(2);
        if ((MathLib.abs(d1 - d2) >= reltol * MathLib.max(MathLib.abs(d1), MathLib.abs(d2))
                + abstol)
                || (MathLib.abs(d3 - d4) >= reltol * MathLib.max(MathLib.abs(d3), MathLib.abs(d4))
                + abstol)) {
            double tmp = delays[3 * sizeDelay] + propDelay;
            tmp -= tmprl.getTime();
            timeStep = MathLib.min(timeStep, tmp);
        }
        return timeStep;
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}
