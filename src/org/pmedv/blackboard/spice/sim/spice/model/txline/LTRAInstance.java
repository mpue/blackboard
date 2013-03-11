/*
 * ltra.java
 *
 * Created on August 28, 2006, 2:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.txline;

import java.util.EnumSet;

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
public class LTRAInstance extends SpiceDevice implements Instance {

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

    /* Branch equation index for end 1 */
    protected int brEq1;

    /* Branch equation index for end 2 */
    protected int brEq2;

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

    /* Previous values of v1 */
    protected double[] v1;

    /* Previous values of i1 */
    protected double[] i1;

    /* Previous values of v2 */
    protected double[] v2;

    /* Previous values of i2 */
    protected double[] i2;

    /* Size of above lists */
    protected int listSize;
    private Complex ibr1Ibr1Node;
    private Complex ibr1Ibr2Node;
    private Complex ibr1Pos1Node;
    private Complex ibr1Neg1Node;
    private Complex ibr1Pos2Node;
    private Complex ibr1Neg2Node;
    private Complex ibr2Ibr1Node;
    private Complex ibr2Ibr2Node;
    private Complex ibr2Pos1Node;
    private Complex ibr2Neg1Node;
    private Complex ibr2Pos2Node;
    private Complex ibr2Neg2Node;
    private Complex neg1Ibr1Node;
    private Complex neg2Ibr2Node;
    private Complex pos1Ibr1Node;
    private Complex pos2Ibr2Node;
    private Complex pos1Pos1Node;
    private Complex neg1Neg1Node;
    private Complex pos2Pos2Node;
    private Complex neg2Neg2Node;
    /* First value of h1dash at current timepoint */
    protected double h1dashFirstVal;

    /* First value of h2 at current timepoint */
    protected double h2FirstVal;

    /* First needed value of h3dash at current timepoint */
    protected double h3dashFirstVal;

    /* Previous h1 values */
    protected double[] h1dashValues;
    /* Previous h2 values */
    protected double[] h2Values;

    /* Previous h3dash values */
    protected double[] h3dashValues;
    protected double h2FirstOthVal;
    protected double h3dashFirstOthVal;
    protected double[] h1dashOthVals;
    protected double[] h2OthVals;
    protected double[] h3dashOthVals;
    /* First needed coeff. of h1dash for the current timepoint */
    protected double h1dashFirstCoeff;

    /* First needed coeff. of h2 for the current timepoint */
    protected double h2FirstCoeff;

    /* First needed coeff. of h3dash for the current timepoint */
    protected double h3dashFirstCoeff;

    /* List of other coefficients for h1dash */
    protected double[] h1dashCoeffs;
    /* List of other coefficients for h2 */
    protected double[] h2Coeffs;

    /* Size of above lists */
    protected int modelListSize;
    /* List of other coefficients for h3dash */
    protected double[] h3dashCoeffs;

    /* Resistance R */
    protected double resist;
    /* Conductance G */
    protected double conduct;
    /* Capacitance C */
    protected double capac;
    /* Inductance L */
    protected double induct;
    /* Length l */
    protected double length;

    /* Propagation delay T */
    protected double td;

    /* Impedance Z */
    protected double imped;

    /* Admittance Y */
    protected double admit;
    protected double alpha;
    protected double beta;
    protected double attenuation;
    protected double cByR;
    protected double rclSqrt;
    protected double integH1dash;
    protected double integH2;
    protected double integH3dash;

    /* Normalized length - historical significance only */
    protected double nl = 0.25;

    /* Frequency at which nl is measured - historical significance only */
    protected double f = 1e9;

    /* cosh(l * sqrt(G * R)), used for DC anal */
    protected double coshlrootGR;

    /* sqrt(R) * sinh(l * sqrt(G * R)) / sqrt(G) */
    protected double rRsLrGRorG;

    /* sqrt(G) * sinh(l * sqrt(G * R)) / sqrt(R) */
    protected double rGsLrGRorR;

    /* Auxiliary index for h2 and h3dash */
    protected int auxIndex;

    /* Separate reltol for checking st. lines */
    protected double stLineReltol;

    /* Separate reltol for truncation of impulse responses */
    protected double chopReltol;

    /* Separate abstol for checking st.  lines */
    protected double stLineAbstol;

    /* Separate abstol for truncation of impulse responses */
    protected double chopAbstol;

    /* Use N - R iterations for calculating step in trunc */
    protected boolean truncNR;

    /* Worry about errors in impulse response calculations due to large steps */
    protected boolean truncDontCut;

    /* Maximum safe step for impulse response calculations */
    protected double maxSafeStep;

    /* Full control, half control or no control */
    protected int lteConType;

    /* How to interpolate for delayed timepoint */
    protected int howToInterp;

    /* Print debug output */
    protected boolean printFlag;
    protected int stepLimit;

    /* Absolute deriv. tol. for breakpoint setting */
    protected double abstol = 1;

    /* Relative deriv. tol. for breakpoint setting */
    protected double reltol = 1;
    protected double curTime;
    protected int timeIndex;
    protected double[] timePoints;
    /* Model types */
    protected int specialCase;
    static final int LTRA = 0;
    static final int FULLCONTROL = 26;
    static final int HALFCONTROL = 27;
    static final int NOCONTROL = 28;
    static final int PRINT = 29;
    static final int NOPRINT = 30;
    static final int STEPLIMIT = 32;
    static final int NOSTEPLIMIT = 33;
    static final int LININTERP = 34;
    static final int QUADINTERP = 35;
    static final int MIXEDINTERP = 36;
    static final int RLC = 37;
    static final int RC = 38;
    static final int RG = 39;
    static final int LC = 40;
    static final int RL = 41;
    static final int STLINEREL = 42;
    static final int STLINEABS = 43;
    static final int CHOPREL = 44;
    static final int CHOPABS = 45;
    static final int TRUNCNR = 46;
    static final int TRUNCDONTCUT = 47;

    enum Interpolate {

        LININTERP, QUADINTERP, MIXEDINTERP
    }

    enum SpecialCase {

        RLC, RC, RG, LC, RL
    }

    enum Control {

        FULLCONTROL, HALFCONTROL, NOCONTROL
    }

    /** Creates a new instance of ltra */
    public LTRAInstance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        tmprl = ckt.getTemporal();
        if (resist == 0) {
            StandardLog.warning(getModelName() + ": lossy line series resistance not given, assumed zero");
            return false;
        }
        if (stLineReltol == 0) {
            stLineReltol = env.getRelTol();
        }
        if (stLineAbstol == 0) {
            stLineAbstol = env.getAbsTol();
        }
        if (howToInterp == 0) {
            if (env.isTryToCompact()) {
                howToInterp = LININTERP;
                StandardLog.warning(getModelName() + ": using linear interpolation because trytocompact option specified");
            } else {
                howToInterp = QUADINTERP;
            }
        }
        if (stepLimit != NOSTEPLIMIT) {
            stepLimit = STEPLIMIT;
        }
        if (lteConType == 0) {
            lteConType = NOCONTROL;
        }
        if (induct == 0) {
            StandardLog.warning(getModelName() + ": lossy line series inductance not given, assumed zero");
        }
        if (capac == 0) {
            StandardLog.severe(getModelName() + ": lossy line parallel capacitance not given, assumed zero");
        }
        if (length == 0) {
            StandardLog.severe(getModelName() + ": lossy line length must be given");
            return false;
        }
        if (resist == 0 && conduct == 0
                && capac != 0 && induct != 0) {
            specialCase = LC;
            StandardLog.info(getModelName() + ": lossless line");
        }
        if (resist != 0 && conduct == 0
                && capac != 0 && induct != 0) {
            specialCase = RLC;
            StandardLog.info(getModelName() + ": RLC line");
        }
        if (resist != 0 && conduct == 0
                && capac != 0 && induct == 0) {
            specialCase = RC;
            StandardLog.info(getModelName() + ": RC line");
        }
        if (resist != 0 && conduct == 0
                && capac == 0 && induct != 0) {
            specialCase = RL;
            StandardLog.severe(getModelName() + ": RL line not supported yet");
            return false;
        }
        if (resist != 0 && conduct != 0
                && capac == 0 && induct == 0) {
            specialCase = RG;
            StandardLog.info(getModelName() + ": RG line");
        }
        if (conduct != 0 && (capac != 0 || induct != 0)) {
            specialCase = LTRA;
            StandardLog.severe(getModelName() + ": Nonzero G (except RG) line not supported yet");
            return false;
        }
        if ((resist == 0 ? 0 : 1) + (conduct == 0 ? 0 : 1) + (induct == 0 ? 0 : 1)
                + (capac == 0 ? 0 : 1) <= 1) {
            StandardLog.severe(getModelName() + ": At least two of R, L, G, C must be specified and nonzero");
            return false;
        }
        if (brEq1 == 0) {
            brEq1 = ckt.makeVoltNode(getInstName() + "i1").getIndex();
        }
        if (brEq2 == 0) {
            brEq2 = ckt.makeVoltNode(getInstName() + "i2").getIndex();
        }
        ibr1Pos1Node = wrk.aquireNode(brEq1, posIndex1);
        ibr1Neg1Node = wrk.aquireNode(brEq1, negIndex1);
        ibr1Pos2Node = wrk.aquireNode(brEq1, posIndex2);
        ibr1Neg2Node = wrk.aquireNode(brEq1, negIndex2);
        ibr1Ibr1Node = wrk.aquireNode(brEq1, brEq1);
        ibr1Ibr2Node = wrk.aquireNode(brEq1, brEq2);
        ibr2Pos1Node = wrk.aquireNode(brEq2, posIndex1);
        ibr2Neg1Node = wrk.aquireNode(brEq2, negIndex1);
        ibr2Pos2Node = wrk.aquireNode(brEq2, posIndex2);
        ibr2Neg2Node = wrk.aquireNode(brEq2, negIndex2);
        ibr2Ibr1Node = wrk.aquireNode(brEq2, brEq1);
        ibr2Ibr2Node = wrk.aquireNode(brEq2, brEq2);
        pos1Ibr1Node = wrk.aquireNode(posIndex1, brEq1);
        neg1Ibr1Node = wrk.aquireNode(negIndex1, brEq1);
        pos2Ibr2Node = wrk.aquireNode(posIndex2, brEq2);
        neg2Ibr2Node = wrk.aquireNode(negIndex2, brEq2);
        pos1Pos1Node = wrk.aquireNode(posIndex1, posIndex1);
        neg1Neg1Node = wrk.aquireNode(negIndex1, negIndex1);
        pos2Pos2Node = wrk.aquireNode(posIndex2, posIndex2);
        neg2Neg2Node = wrk.aquireNode(negIndex2, negIndex2);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double y0R = 0;
        double y0I = 0;
        double lambdaT = 0;
        double lambdaI = 0;
        switch (specialCase) {
            case LC:
                y0R = admit;
                y0I = 0;
                lambdaI = MathLib.sqrt(induct * capac) * tmprl.getOmega();
                lambdaT = 0;
                break;
            case RLC:
                double theta = 0.5 * MathLib.atan(resist / (tmprl.getOmega() * induct));
                double mag = MathLib.sqrt(tmprl.getOmega() * capac
                        / MathLib.sqrt(resist * resist
                        + tmprl.getOmega() * tmprl.getOmega() * induct
                        * induct));
                y0R = mag * MathLib.cos(theta);
                y0I = mag * MathLib.sin(theta);
                theta = MathLib.PI / 2 - theta;
                mag *= MathLib.sqrt(resist * resist
                        + tmprl.getOmega() * tmprl.getOmega() * induct
                        * induct);
                lambdaT = mag * MathLib.cos(theta);
                lambdaI = mag * MathLib.sin(theta);
                break;
            case RC:
                y0R = y0I = MathLib.sqrt(0.5 * tmprl.getOmega() * cByR);
                lambdaT = lambdaI =
                        MathLib.sqrt(0.5 * tmprl.getOmega() * resist * capac);
                break;
            case RG:
                EnumSet<MODE> savemode = mode.getModes();
                mode.add(MODE.DC);
                load(mode);
                mode.setModes(savemode);
                break;
            default:
                return false;
        }
        double expArg_r = -lambdaT * length;
        double expArg_i = -lambdaI * length;
        double explambda_r = MathLib.exp(expArg_r) * MathLib.cos(expArg_i);
        double explambda_i = MathLib.exp(expArg_r) * MathLib.sin(expArg_i);
        double y0exp_r = y0R * explambda_r - y0I * explambda_i;
        double y0exp_i = y0R * explambda_i + y0I * explambda_r;
        ibr1Pos1Node.realPlusEq(y0R);
        ibr1Pos1Node.imagPlusEq(y0I);
        ibr1Neg1Node.realMinusEq(y0R);
        ibr1Neg1Node.imagMinusEq(y0I);
        ibr1Ibr1Node.realMinusEq(1.0);
        ibr1Pos2Node.realMinusEq(y0exp_r);
        ibr1Pos2Node.imagMinusEq(y0exp_i);
        ibr1Neg2Node.realPlusEq(y0exp_r);
        ibr1Neg2Node.imagPlusEq(y0exp_i);
        ibr1Ibr2Node.realMinusEq(explambda_r);
        ibr1Ibr2Node.imagMinusEq(explambda_i);
        ibr2Pos2Node.realPlusEq(y0R);
        ibr2Pos2Node.imagPlusEq(y0I);
        ibr2Neg2Node.realMinusEq(y0R);
        ibr2Neg2Node.imagMinusEq(y0I);
        ibr2Ibr2Node.realMinusEq(1.0);
        ibr2Pos1Node.realMinusEq(y0exp_r);
        ibr2Pos1Node.imagMinusEq(y0exp_i);
        ibr2Neg1Node.realPlusEq(y0exp_r);
        ibr2Neg1Node.imagPlusEq(y0exp_i);
        ibr2Ibr1Node.realMinusEq(explambda_r);
        ibr2Ibr1Node.imagMinusEq(explambda_i);
        pos1Ibr1Node.realPlusEq(1.0);
        neg1Ibr1Node.realMinusEq(1.0);
        pos2Ibr2Node.realPlusEq(1.0);
        neg2Ibr2Node.realMinusEq(1.0);
        return true;
    }

    private boolean check(double a, double b, double c) {
        return MathLib.max(MathLib.max(a, b), c)
                - MathLib.min(MathLib.min(a, b), c) >= MathLib.abs(50
                * (env.getRelTol() / 3.0 * (a + b + c) + env.getAbsTol()));
    }

    public boolean accept(Mode mode) {
        double _v1,
                _v2,
                v3,
                v4;
        double v5,
                v6,
                d1,
                d2,
                d3,
                d4;
        boolean compact = true;
        int size = 0;
        if (mode.contains(MODE.INIT_TRAN)) {
            size = 10;
            h1dashCoeffs = new double[modelListSize];
            h2Coeffs = new double[modelListSize];
            h3dashCoeffs = new double[modelListSize];
        }
        if (tmprl.getTimeIndex() >= modelListSize) {
            modelListSize += tmprl.getIncrSize();
            h1dashCoeffs = new double[modelListSize];
            h2Coeffs = new double[modelListSize];
            h3dashCoeffs = new double[modelListSize];
            if (mode.contains(MODE.INIT_TRAN)) {
                listSize = 10;
                v1 = new double[listSize];
                i1 = new double[listSize];
                v2 = new double[listSize];
                i2 = new double[listSize];
            }
            if (timeIndex >= listSize) {
                listSize += tmprl.getIncrSize();
                v1 = new double[listSize];
                i1 = new double[listSize];
                i2 = new double[listSize];
                v2 = new double[listSize];
            }
            v1[timeIndex] = wrk.getRhsOldRealAt(posIndex1) - wrk.getRhsOldRealAt(negIndex1);
            v2[timeIndex] = wrk.getRhsOldRealAt(posIndex2) - wrk.getRhsOldRealAt(negIndex2);
            i1[timeIndex] = wrk.getRhsOldRealAt(brEq1);
            i2[timeIndex] = wrk.getRhsOldRealAt(brEq2);
            if (env.isTryToCompact() && (timeIndex >= 2)) {
                double t1 = timePoints[timeIndex - 2];
                double t2 = timePoints[timeIndex - 1];
                double t3 = timePoints[timeIndex];
                if (compact) {
                    compact = straightLineCheck(t1, v1[timeIndex - 2],
                            t2, v1[timeIndex - 1], t3, v1[timeIndex],
                            stLineReltol, stLineAbstol);
                }
                if (compact) {
                    compact = straightLineCheck(t1, v2[timeIndex - 2],
                            t2, v2[timeIndex - 1],
                            t3, v2[timeIndex],
                            stLineReltol, stLineAbstol);
                }
                if (compact) {
                    compact = straightLineCheck(t1, i1[timeIndex - 2],
                            t2, i1[timeIndex - 1],
                            t3, i1[timeIndex],
                            stLineReltol, stLineAbstol);
                }
                if (compact) {
                    compact = straightLineCheck(t1, i2[timeIndex - 2],
                            t2, i2[timeIndex - 1],
                            t3, i2[timeIndex],
                            stLineReltol, stLineAbstol);
                }
            }
        }
        if (timeIndex > 0) {
            if (false) {//?
                _v1 = (v1[timeIndex] + i1[timeIndex] * imped) * attenuation;
                _v2 = (v1[timeIndex - 1] + i1[timeIndex - 1] * imped) * attenuation;
                v3 = (v2[timeIndex] + i2[timeIndex] * imped) * attenuation;
                v4 = (v2[timeIndex - 1] + i2[timeIndex - 1] * imped) * attenuation;
                if ((MathLib.abs(_v1 - _v2) >= 50 * env.getRelTol()
                        * MathLib.max(MathLib.abs(_v1), MathLib.abs(_v2)) + 50 * env.getVoltTol())
                        || (MathLib.abs(v3 - v4) >= 50 * env.getRelTol()
                        * MathLib.max(MathLib.abs(v3), MathLib.abs(v4)) + 50 * env.getVoltTol())) {
                    if (!tmprl.addBreak((timePoints[timeIndex - 1]) + td)) {
                        tmprl.breakDump();
                    }
                }
            } else {
                _v1 = (v1[timeIndex] + i1[timeIndex] * imped) * attenuation;
                _v2 = (v1[timeIndex - 1] + i1[timeIndex - 1] * imped) * attenuation;
                v3 = timeIndex < 2 ? _v2 : (v1[timeIndex - 2]
                        + i1[timeIndex - 2] * imped) * attenuation;
                v4 = (v2[timeIndex] + i2[timeIndex] * imped) * attenuation;
                v5 = (v2[timeIndex - 1] + i2[timeIndex - 1] * imped) * attenuation;
                v6 = timeIndex < 2 ? v5 : (v2[timeIndex - 2]
                        + i2[timeIndex - 2] * imped) * attenuation;
                d1 = (_v1 - _v2) / (timePoints[timeIndex] - timePoints[timeIndex - 1]);
                d2 = (_v2 - v3) / (timePoints[timeIndex - 1] - timePoints[timeIndex - 2]);
                d3 = (v4 - v5) / (timePoints[timeIndex] - timePoints[timeIndex - 1]);
                d4 = (v5 - v6) / (timePoints[timeIndex - 1] - timePoints[timeIndex - 2]);
                boolean tmpTest = (MathLib.abs(d1 - d2) >= reltol * MathLib.max(MathLib.abs(d1), MathLib.abs(d2))
                        + abstol) && check(_v1, _v2, v3);
                if (tmpTest || ((MathLib.abs(d3 - d4) >= reltol * MathLib.max(MathLib.abs(d3), MathLib.abs(d4))
                        + abstol) && check(v4, v5, v6))) {
                    tmprl.addBreak(timePoints[timeIndex - 1] + td);
                    StandardLog.info(String.format("breakpoints set at %14.14g at "
                            + "%14.14g at time %14.14g", tmprl.getTime() + td,
                            timePoints[timeIndex - 1] + td, tmprl.getTime()));
                    StandardLog.info(String.format("d1 through d4 are %14.14g "
                            + "%14.14g %14.14g %14.14g\n", d1, d2, d3, d4));
                }
            }
        }
        if (env.isTryToCompact() && compact && (timeIndex >= 2)) {
            v1[timeIndex - 1] = v1[timeIndex];
            v2[timeIndex - 1] = v2[timeIndex];
            i1[timeIndex - 1] = i1[timeIndex];
            i2[timeIndex - 1] = i2[timeIndex];
            timePoints[timeIndex - 1] = timePoints[timeIndex];
            timeIndex--;
            StandardLog.info("compacted at time =" + timePoints[timeIndex]);
        }
        return true;
    }

    public boolean load(Mode mode) {
        double t1 = 0;
        double t2 = 0;
        double t3 = 0;
        double qf1 = 0;
        double qf2 = 0;
        double qf3 = 0;
        double lf2 = 0;
        double lf3 = 0;
        double v1d = 0;
        double v2d = 0;
        double i1d = 0;
        double i2d = 0;
        double tmp1 = 0;
        double tmp2 = 0;
        int isaved = 0;
        boolean tdover = false;
        int i;
        if (mode.contains(MODE.DC)) {
            switch (specialCase) {
                case RG:
                    tmp1 = length * MathLib.sqrt(resist * conduct);
                    tmp2 = MathLib.exp(-tmp1);
                    tmp1 = MathLib.exp(tmp1);
                    coshlrootGR = 0.5 * (tmp1 + tmp2);
                    if (conduct <= 1.0e-10) {
                        rRsLrGRorG = length * resist;
                    } else {
                        rRsLrGRorG = 0.5 * (tmp1 - tmp2)
                                * MathLib.sqrt(resist / conduct);
                    }
                    if (resist <= 1.0e-10) {
                        rGsLrGRorR = length * conduct;
                    } else {
                        rGsLrGRorR = 0.5 * (tmp1 - tmp2)
                                * MathLib.sqrt(conduct / resist);
                    }
                    break;
                case RC:
                case LC:
                case RLC:
                    break;
                default:
                    return false;
            }
        } else {
            if (mode.contains(MODE.INIT_TRAN) || mode.contains(MODE.INIT_PRED)) {
                switch (specialCase) {
                    case RLC:
                    case LC:
                        if (tmprl.getTime() > td) {
                            tdover = true;
                        } else {
                            tdover = false;
                        }
                    default:
                        break;
                }
                switch (specialCase) {
                    case RLC:
                        rlcCoeffsSetup();
                    case LC:
                        if (tdover) {
                            for (i = timeIndex; i >= 0; i--) {
                                if (timePoints[i] < tmprl.getTime() - td) {
                                    break;
                                }
                            }
                            if (i == timeIndex) {
                                StandardLog.info("load: Warning: timestep larger than delay of line");
                                StandardLog.info("Time now: " + tmprl.getTime());
                            }
                            if (i == timeIndex) {
                                i--;
                            }
                            if (i == -1) {
                                StandardLog.info("load: mistake: cannot find delayed timepoint");
                                return false;
                            }
                            isaved = i;
                            t2 = timePoints[i];
                            t3 = timePoints[i + 1];
                            if (i != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)) {
                                t1 = timePoints[i - 1];
                                c1 = qf1;
                                c2 = qf2;
                                c3 = qf3;
                                quadInterp(tmprl.getTime() - td, t1, t2, t3);
                                qf1 = c1;
                                qf2 = c2;
                                qf3 = c3;
                            }
                            if (i == 0 || howToInterp == MIXEDINTERP
                                    || howToInterp == LININTERP) {
                                c1 = lf2;
                                c2 = lf3;
                                linInterp(tmprl.getTime() - td, t2, t3);
                                lf2 = c1;
                                lf3 = c2;
                            }
                        }
                        break;
                    case RC:
                        rcCoeffsSetup();
                        break;
                    case RG:
                        break;
                    default:
                        return false;
                }
            }
        }
        if (mode.contains(MODE.DC) || specialCase == RG) {
            switch (specialCase) {
                case RG:
                    ibr1Pos1Node.realPlusEq(1.0);
                    ibr1Neg1Node.realMinusEq(1.0);
                    ibr1Pos2Node.realMinusEq(coshlrootGR);
                    ibr1Neg2Node.realPlusEq(coshlrootGR);
                    ibr1Ibr2Node.realPlusEq((1 + env.getGMin()) * rRsLrGRorG);
                    ibr2Ibr2Node.realPlusEq(coshlrootGR);
                    ibr2Pos2Node.realMinusEq((1 + env.getGMin()) * rGsLrGRorR);
                    ibr2Neg2Node.realPlusEq((1 + env.getGMin()) * rGsLrGRorR);
                    ibr2Ibr1Node.realPlusEq(1.0);
                    pos1Ibr1Node.realPlusEq(1.0);
                    neg1Ibr1Node.realMinusEq(1.0);
                    pos2Ibr2Node.realPlusEq(1.0);
                    neg2Ibr2Node.realMinusEq(1.0);
                    input1 = input2 = 0;
                    break;
                case LC:
                case RLC:
                case RC:
                    pos1Ibr1Node.realPlusEq(1.0);
                    neg1Ibr1Node.realMinusEq(1.0);
                    pos2Ibr2Node.realPlusEq(1.0);
                    neg2Ibr2Node.realMinusEq(1.0);
                    ibr1Ibr1Node.realPlusEq(1.0);
                    ibr1Ibr2Node.realPlusEq(1.0);
                    ibr2Pos1Node.realPlusEq(1.0);
                    ibr2Pos2Node.realMinusEq(1.0);
                    ibr2Ibr1Node.realMinusEq(resist * length);
                    input1 = input2 = 0;
                    break;
                default:
                    return false;
            }
        } else {
            if (mode.contains(MODE.INIT_TRAN)) {
                if (!mode.isUseIC()) {
                    initVolt1 =
                            (wrk.getRhsOldRealAt(posIndex1) - wrk.getRhsOldRealAt(negIndex1));
                    initVolt2 =
                            (wrk.getRhsOldRealAt(posIndex2) - wrk.getRhsOldRealAt(negIndex2));
                    initCur1 = wrk.getRhsOldRealAt(brEq1);
                    initCur2 = wrk.getRhsOldRealAt(brEq2);
                }
            }
            switch (specialCase) {
                case RLC:
                    tmp1 = admit * h1dashFirstCoeff;
                    ibr1Pos1Node.realPlusEq(tmp1);
                    ibr1Neg1Node.realMinusEq(tmp1);
                    ibr2Pos2Node.realPlusEq(tmp1);
                    ibr2Neg2Node.realMinusEq(tmp1);
                case LC:
                    ibr1Pos1Node.realPlusEq(admit);
                    ibr1Neg1Node.realMinusEq(admit);
                    ibr1Ibr1Node.realMinusEq(1.0);
                    pos1Ibr1Node.realPlusEq(1.0);
                    neg1Ibr1Node.realMinusEq(1.0);
                    ibr2Pos2Node.realPlusEq(admit);
                    ibr2Neg2Node.realMinusEq(admit);
                    ibr2Ibr2Node.realMinusEq(1.0);
                    pos2Ibr2Node.realPlusEq(1.0);
                    neg2Ibr2Node.realMinusEq(1.0);
                    break;
                case RC:
                    ibr1Ibr1Node.realMinusEq(1.0);
                    pos1Ibr1Node.realPlusEq(1.0);
                    neg1Ibr1Node.realMinusEq(1.0);
                    ibr2Ibr2Node.realMinusEq(1.0);
                    pos2Ibr2Node.realPlusEq(1.0);
                    neg2Ibr2Node.realMinusEq(1.0);
                    tmp1 = h1dashFirstCoeff;
                    ibr1Pos1Node.realPlusEq(tmp1);
                    ibr1Neg1Node.realMinusEq(tmp1);
                    ibr2Pos2Node.realPlusEq(tmp1);
                    ibr2Neg2Node.realMinusEq(tmp1);
                    tmp1 = h2FirstCoeff;
                    ibr1Ibr2Node.realMinusEq(tmp1);
                    ibr2Ibr1Node.realMinusEq(tmp1);
                    tmp1 = h3dashFirstCoeff;
                    ibr1Pos2Node.realMinusEq(tmp1);
                    ibr1Neg2Node.realPlusEq(tmp1);
                    ibr2Pos1Node.realMinusEq(tmp1);
                    ibr2Neg1Node.realPlusEq(tmp1);
                    break;
                default:
                    return false;
            }
            if (mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN)) {
                input1 = input2 = 0;
                double max = 0;
                double min = 0;
                switch (specialCase) {
                    case LC:
                    case RLC:
                        if (tdover) {
                            if (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)) {
                                v1d = v1[isaved - 1] * qf1 + v1[isaved] * qf2 + v1[isaved + 1] * qf3;
                                max = MathLib.max(v1[isaved - 1], v1[isaved]);
                                max = MathLib.max(max, v1[isaved + 1]);
                                min = MathLib.min(v1[isaved - 1], v1[isaved]);
                                min = MathLib.min(min, v1[isaved + 1]);
                            }
                            if (howToInterp == LININTERP || isaved == 0
                                    || (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)
                                    && (v1d > max || v1d < min))) {
                                if ((isaved != 0) && (howToInterp == QUADINTERP)) {
                                    StandardLog.info("load: warning: interpolated v1 is out of range after timepoint " + timeIndex);
                                    StandardLog.info(String.format("values: %1.8g %1.8g "
                                            + "%1.8g; interpolated: %1.8g\n", v1[isaved - 1],
                                            v1[isaved], v1[isaved + 1], v1d));
                                    StandardLog.info(String.format("timepoints are: %1.8g "
                                            + "%1.8g %1.8g %1.8g", t1, t2, t3, tmprl.getTime() - td));
                                } else {
                                    v1d = v1[isaved] * lf2 + v1[isaved + 1] * lf3;
                                }
                            }
                            if (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)) {
                                i1d = i1[isaved - 1] * qf1 + i1[isaved] * qf2 + i1[isaved + 1] * qf3;
                                max = MathLib.max(i1[isaved - 1], i1[isaved]);
                                max = MathLib.max(max, i1[isaved + 1]);
                                min = MathLib.min(i1[isaved - 1], i1[isaved]);
                                min = MathLib.min(min, i1[isaved + 1]);
                            }
                            if (howToInterp == LININTERP || isaved == 0
                                    || (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)
                                    && (i1d > max || i1d < min))) {
                                if (isaved != 0 && howToInterp == QUADINTERP) {
                                    StandardLog.info("load: warning: interpolated i1 is out of range after timepoint " + timeIndex);
                                    StandardLog.info(String.format("values: %1.8g %1.8g "
                                            + "%1.8g; interpolated: %1.8g\n", i1[isaved - 1],
                                            i1[isaved], i1[isaved + 1], i1d));
                                    StandardLog.info(String.format("timepoints are: %1.8g %1.8g %1.8g "
                                            + "%1.8g\n", t1, t2, t3, tmprl.getTime() - td));
                                } else {
                                    i1d = i1[isaved] * lf2 + i1[isaved + 1] * lf3;
                                }
                            }
                            if (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)) {
                                v2d = v2[isaved - 1] * qf1 + v2[isaved] * qf2 + v2[isaved + 1] * qf3;
                                max = MathLib.max(v2[isaved - 1], v2[isaved]);
                                max = MathLib.max(max, v2[isaved + 1]);
                                min = MathLib.min(v2[isaved - 1], v2[isaved]);
                                min = MathLib.min(min, v2[isaved + 1]);
                            }
                            if (howToInterp == LININTERP || isaved == 0
                                    || (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)
                                    && (v2d > max || v2d < min))) {
                                if (isaved != 0 && howToInterp == QUADINTERP) {
                                    StandardLog.info("load: warning: interpolated v2 is"
                                            + "out of range after timepoint " + timeIndex);
                                    StandardLog.info(String.format("values: %1.8g %1.8g "
                                            + "%1.8g; interpolated: %1.8g\n", v2[isaved - 1],
                                            v2[isaved], v2[isaved + 1], v2d));
                                    StandardLog.info(String.format("timepoints are: %1.8g"
                                            + "%1.8g %1.8g %1.8g", t1, t2, t3, tmprl.getTime() - td));
                                } else {
                                    v2d = v2[isaved] * lf2 + v2[isaved + 1] * lf3;
                                }
                            }
                            if (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)) {
                                i2d = i2[isaved - 1] * qf1 + i2[isaved] * qf2 + i2[isaved + 1] * qf3;
                                max = MathLib.max(i2[isaved - 1], i2[isaved]);
                                max = MathLib.max(max, i2[isaved + 1]);
                                min = MathLib.min(i2[isaved - 1], i2[isaved]);
                                min = MathLib.min(min, i2[isaved + 1]);
                            }
                            if (howToInterp == LININTERP || isaved == 0
                                    || (isaved != 0 && (howToInterp == QUADINTERP
                                    || howToInterp == MIXEDINTERP)
                                    && (i2d > max || i2d < min))) {
                                if (isaved != 0 && howToInterp == QUADINTERP) {
                                    StandardLog.info("load: warning: interpolated i2 is out of range after timepoint " + timeIndex);
                                    StandardLog.info(String.format("values: %1.8g %1.8g %1.8g;"
                                            + "interpolated: %1.8g", i2[isaved - 1],
                                            i2[isaved], i2[isaved + 1], i2d));
                                    StandardLog.info(String.format("timepoints are: %1.8g"
                                            + "%1.8g %1.8g %1.8g", t1, t2, t3, tmprl.getTime() - td));
                                } else {
                                    i2d = i2[isaved] * lf2 + i2[isaved + 1] * lf3;
                                }
                            }
                        }
                        break;
                    case RC:
                        break;
                    default:
                        return false;
                }
                switch (specialCase) {
                    case RLC:
                        tmp1 = tmp2 = 0;
                        for (i = timeIndex; i > 0; i--) {
                            if (h1dashCoeffs[i] != 0) {
                                tmp1 += h1dashCoeffs[i] * (v1[i] - initVolt1);
                                tmp2 += h1dashCoeffs[i] * (v2[i] - initVolt2);
                            }
                        }
                        tmp1 += initVolt1 * integH1dash;
                        tmp2 += initVolt2 * integH1dash;
                        tmp1 -= initVolt1 * h1dashFirstCoeff;
                        tmp2 -= initVolt2 * h1dashFirstCoeff;
                        input1 -= tmp1 * admit;
                        input2 -= tmp2 * admit;
                        tmp1 = tmp2 = 0;
                        if (tdover) {
                            tmp1 = (i2d - initCur2) * h2FirstCoeff;
                            tmp2 = (i1d - initCur1) * h2FirstCoeff;
                            for (i = auxIndex; i > 0; i--) {
                                if (h2Coeffs[i] != 0) {
                                    tmp1 += h2Coeffs[i] * (i2[i] - initCur2);
                                    tmp2 += h2Coeffs[i] * (i1[i] - initCur1);
                                }
                            }
                        }
                        tmp1 += initCur2 * integH2;
                        tmp2 += initCur1 * integH2;
                        input1 += tmp1;
                        input2 += tmp2;
                        tmp1 = tmp2 = 0;
                        if (tdover) {
                            tmp1 = (v2d - initVolt2) * h3dashFirstCoeff;
                            tmp2 = (v1d - initVolt1) * h3dashFirstCoeff;
                            for (i = auxIndex; i > 0; i--) {
                                if (h3dashCoeffs[i] != 0) {
                                    tmp1 += h3dashCoeffs[i] * (v2[i] - initVolt2);
                                    tmp2 += h3dashCoeffs[i] * (v1[i] - initVolt1);
                                }
                            }
                        }
                        tmp1 += initVolt2 * integH3dash;
                        tmp2 += initVolt1 * integH3dash;
                        input1 += admit * tmp1;
                        input2 += admit * tmp2;
                    case LC:
                        if (!tdover) {
                            input1 += attenuation * (initVolt2 * admit + initCur2);
                            input2 += attenuation * (initVolt1 * admit + initCur1);
                        } else {
                            input1 += attenuation * (v2d * admit + i2d);
                            input2 += attenuation * (v1d * admit + i1d);
                        }
                        break;
                    case RC:
                        tmp1 = 0;
                        tmp2 = 0;
                        for (i = timeIndex; i > 0; i--) {
                            if (h1dashCoeffs[i] != 0) {
                                tmp1 += h1dashCoeffs[i] * (v1[i] - initVolt1);
                                tmp2 += h1dashCoeffs[i] * (v2[i] - initVolt2);
                            }
                        }
                        tmp1 += initVolt1 * integH1dash;
                        tmp2 += initVolt2 * integH1dash;
                        tmp1 -= initVolt1 * h1dashFirstCoeff;
                        tmp2 -= initVolt2 * h1dashFirstCoeff;
                        input1 -= tmp1;
                        input2 -= tmp2;
                        tmp1 = tmp2 = 0;
                        for (i = timeIndex; i > 0; i--) {
                            if (h2Coeffs[i] != 0) {
                                tmp1 += h2Coeffs[i] * (i2[i] - initCur2);
                                tmp2 += h2Coeffs[i] * (i1[i] - initCur1);
                            }
                        }
                        tmp1 += initCur2 * integH2;
                        tmp2 += initCur1 * integH2;
                        tmp1 -= initCur2 * h2FirstCoeff;
                        tmp2 -= initCur1 * h2FirstCoeff;
                        input1 += tmp1;
                        input2 += tmp2;
                        tmp1 = tmp2 = 0;
                        for (i = timeIndex; i > 0; i--) {
                            if (h3dashCoeffs[i] != 0) {
                                tmp1 += h3dashCoeffs[i] * (v2[i] - initVolt2);
                                tmp2 += h3dashCoeffs[i] * (v1[i] - initVolt1);
                            }
                        }
                        tmp1 += initVolt2 * integH3dash;
                        tmp2 += initVolt1 * integH3dash;
                        tmp1 -= initVolt2 * h3dashFirstCoeff;
                        tmp2 -= initVolt1 * h3dashFirstCoeff;
                        input1 += tmp1;
                        input2 += tmp2;
                        break;
                    default:
                        return false;
                }
            }
            wrk.getRhsAt(brEq1).realPlusEq(input1);
            wrk.getRhsAt(brEq2).realPlusEq(input2);
        }
        return true;
    }
    double c1;
    double c2;
    double c3;
    /* Quad interpolation */

    private boolean quadInterp(double t, double t1, double t2, double t3) {
        if (t == t1) {
            c1 = 1.0;
            c2 = 0;
            c3 = 0;
            return false;
        }
        if (t == t2) {
            c1 = 0;
            c2 = 1.0;
            c3 = 0;
            return false;
        }
        if (t == t3) {
            c1 = 0;
            c2 = 0;
            c3 = 1.0;
            return false;
        }
        if (t2 - t1 == 0 || t3 - t2 == 0 || t1 - t3 == 0) {
            return true;
        }
        double f1 = (t - t2) * (t - t3);
        double f2 = (t - t1) * (t - t3);
        double f3 = (t - t1) * (t - t2);
        if (t2 - t1 == 0) {
            f1 = 0;
            f2 = 0;
        } else {
            f1 /= t1 - t2;
        }
        f2 /= t2 - t1;
        if (t3 - t2 == 0) {
            f2 = 0;
            f3 = 0;
        } else {
            f2 /= t2 - t3;
        }
        f3 /= t2 - t3;
        if (t3 - t1 == 0) {
            f1 = 0;
            f2 = 0;
        } else {
            f1 /= t1 - t3;
        }
        f3 /= t1 - t3;
        c1 = f1;
        c2 = f2;
        c3 = f3;
        return false;
    }
    /* Linear interpolation */

    private boolean linInterp(double t, double t1, double t2) {
        double tmp;
        if (t1 == t2) {
            return true;
        }
        if (t == t1) {
            c1 = 1.0;
            c2 = 0;
            return false;
        }
        if (t == t2) {
            c1 = 0;
            c2 = 1.0;
            return false;
        }
        tmp = (t - t1) / (t2 - t1);
        c2 = tmp;
        c1 = 1 - tmp;
        return false;
    }

    private double integLinFunc(double loLimit, double hiLimit, double loValue,
            double hiValue, double t1, double t2) {
        double width = t2 - t1;
        if (width == 0) {
            return 0;
        }
        double m = (hiValue - loValue) / width;
        return ((hiLimit - loLimit) * loValue + 0.5 * m * ((hiLimit - t1) * (hiLimit - t1) - (loLimit - t1) * (loLimit - t1)));
    }

    private double twiceIntegLinFunc(double loLimit, double hiLimit, double otherLoLimit,
            double loValue, double hiValue, double t1, double t2) {
        double width = t2 - t1;
        if (width == 0) {
            return (0);
        }
        double m = (hiValue - loValue) / width;
        double tmp1 = hiLimit - t1;
        double tmp2 = loLimit - t1;
        double tmp3 = otherLoLimit - t1;
        double tmp = loValue * ((hiLimit - otherLoLimit) * (hiLimit - otherLoLimit)
                - (loLimit - otherLoLimit) * (loLimit - otherLoLimit));
        tmp += m * ((tmp1 * tmp1 * tmp1 - tmp2 * tmp2 * tmp2) / 3.0
                - tmp3 * tmp3 * (hiLimit - loLimit));
        return tmp * 0.5;
    }

    private double thriceIntegLinFunc(double loLimit, double hiLimit, double secondLoLimit,
            double thirdLoLimit, double loValue, double hiValue, double t1, double t2) {
        double width = t2 - t1;
        if (width == 0) {
            return (0);
        }
        double m = (hiValue - loValue) / width;
        double tmp1 = hiLimit - t1;
        double tmp2 = loLimit - t1;
        double tmp3 = secondLoLimit - t1;
        double tmp4 = thirdLoLimit - t1;
        double tmp5 = hiLimit - thirdLoLimit;
        double tmp6 = loLimit - thirdLoLimit;
        double tmp7 = secondLoLimit - thirdLoLimit;
        double tmp8 = hiLimit - loLimit;
        double tmp9 = hiLimit - secondLoLimit;
        double tmp10 = loLimit - secondLoLimit;
        double tmp = loValue * ((tmp5 * tmp5 * tmp5 - tmp6 * tmp6 * tmp6) / 3
                - tmp7 * tmp5 * tmp8);
        tmp += m * (((tmp1 * tmp1 * tmp1 * tmp1 - tmp2 * tmp2 * tmp2 * tmp2) * 0.25
                - tmp3 * tmp3 * tmp3 * tmp8) / 3 - tmp4 * tmp4 * 0.5 * (tmp9 * tmp9
                - tmp10 * tmp10));
        return tmp * 0.5;
    }

    private double bessI0(double x) {
        double ax = MathLib.abs(x);
        double ans;
        double y;
        if (ax < 3.75) {
            y = x / 3.75;
            y *= y;
            ans = 1.0 + y * (3.5156229 + y * (3.0899424 + y * (1.2067492 + y * (0.2659732 + y * (0.360768e-1 + y * 0.45813e-2)))));
        } else {
            y = 3.75 / ax;
            ans = (MathLib.exp(ax) / MathLib.sqrt(ax)) * (0.39894228 + y * (0.1328592e-1 + y * (0.225319e-2 + y * (-0.157565e-2 + y * (0.916281e-2 + y * (-0.2057706e-1 + y * (0.2635537e-1 + y * (-0.1647633e-1 + y * 0.392377e-2))))))));
        }
        return ans;
    }

    private double bessI1(double x) {
        double ax = MathLib.abs(x);
        double ans;
        double y;
        if (ax < 3.75) {
            y = x / 3.75;
            y *= y;
            ans = ax * (0.5 + y * (0.87890594 + y * (0.51498869 + y * (0.15084934 + y * (0.2658733e-1 + y * (0.301532e-2 + y * 0.32411e-3))))));
        } else {
            y = 3.75 / ax;
            ans = 0.2282967e-1 + y * (-0.2895312e-1 + y * (0.1787654e-1 - y * 0.420059e-2));
            ans = 0.39894228 + y * (-0.3988024e-1 + y * (-0.362018e-2 + y * (0.163801e-2 + y * (-0.1031555e-1 + y * ans))));
            ans *= MathLib.exp(ax) / MathLib.sqrt(ax);
        }
        return x < 0 ? -ans : ans;
    }

    private double bessI1xOverX(double x) {
        double ax = MathLib.abs(x);
        double ans;
        double y;
        if (ax < 3.75) {
            y = x / 3.75;
            y *= y;
            ans = 0.5 + y * (0.87890594 + y * (0.51498869 + y * (0.15084934 + y * (0.2658733e-1 + y * (0.301532e-2 + y * 0.32411e-3)))));
        } else {
            y = 3.75 / ax;
            ans = 0.2282967e-1 + y * (-0.2895312e-1 + y * (0.1787654e-1 - y * 0.420059e-2));
            ans = 0.39894228 + y * (-0.3988024e-1 + y * (-0.362018e-2 + y * (0.163801e-2 + y * (-0.1031555e-1 + y * ans))));
            ans *= MathLib.exp(ax) / (ax * MathLib.sqrt(ax));
        }
        return ans;
    }

    private double rlcH1dashFunc(double time, double t, double alpha, double beta) {
        if (alpha == 0) {
            return 0;
        }
        double expArg = -beta * time;
        double besselArg = alpha * time;
        return (bessI1(besselArg) - bessI0(besselArg)) * alpha * MathLib.exp(expArg);
    }

    private double rlcH2Func(double time, double t, double alpha, double beta) {
        double besselArg;
        if (alpha == 0) {
            return 0;
        }
        if (time < t) {
            return 0;
        }
        if (time != t) {
            besselArg = alpha * MathLib.sqrt(time * time - t * t);
        } else {
            besselArg = 0;
        }
        double expArg = -beta * time;
        return alpha * alpha * t * MathLib.exp(expArg) * bessI1xOverX(besselArg);
    }

    private double rlcH3dashFunc(double time, double t, double alpha, double beta) {
        double besselArg;
        if (alpha == 0) {
            return 0;
        }
        if (time < t) {
            return 0;
        }
        double expArg = -beta * time;
        if (time != t) {
            besselArg = alpha * MathLib.sqrt(time * time - t * t);
        } else {
            besselArg = 0;
        }
        return (alpha * time * bessI1xOverX(besselArg) - bessI0(besselArg))
                * alpha * MathLib.exp(expArg);
    }

    private double rlcH1dashTwiceIntegFunc(double time, double beta) {
        if (beta == 0) {
            return time;
        }
        double arg = beta * time;
        if (arg == 0) {
            return 0;
        }
        return (bessI1(arg) + bessI0(arg)) * time * MathLib.exp(-arg) - time;
    }

    private double rlcH3dashIntegFunc(double time, double t, double beta) {
        if (time <= t) {
            return 0;
        }
        if (beta == 0) {
            return 0;
        }
        double expArg = -beta * time;
        double besselArg = beta * MathLib.sqrt(time * time - t * t);
        return MathLib.exp(expArg) * bessI0(besselArg) - MathLib.exp(-beta * t);
    }

    private double rcH1dashTwiceIntegFunc(double time, double cByR) {
        return MathLib.sqrt(4 * cByR * time / MathLib.PI);
    }

    private double rcH2TwiceIntegFunc(double time, double rclSqrt) {
        if (time != 0) {
            double tmp = rclSqrt / (4 * time);
            return (time + rclSqrt * 0.5) * erfc(MathLib.sqrt(tmp)) - MathLib.sqrt(time * rclSqrt / MathLib.PI) * MathLib.exp(-tmp);
        } else {
            return 0;
        }
    }

    private double rcH3dashTwiceIntegFunc(double time, double cByR, double rclSqrt) {
        if (time != 0) {
            double tmp = rclSqrt / (4 * time);
            tmp = 2 * MathLib.sqrt(time / MathLib.PI) * MathLib.exp(-tmp) - MathLib.sqrt(rclSqrt) * erfc(MathLib.sqrt(tmp));
            return MathLib.sqrt(cByR) * tmp;
        } else {
            return 0;
        }
    }

    private void rcCoeffsSetup() {
        double _curTime = tmprl.getTime();
        boolean doh1 = true;
        boolean doh2 = true;
        boolean doh3 = true;

        if (modelListSize <= timeIndex) {
            StandardLog.info("coeffSetup: not enough space in coefflist");
        }
        int _auxIndex = timeIndex;

        double delta1 = _curTime - timePoints[_auxIndex];
        //double loLimit1 = 0;
        double hiLimit1 = delta1;
        double h1LoValue1 = 0;
        double h1HiValue1 = MathLib.sqrt(4 * cByR * hiLimit1 / MathLib.PI);
        double h1tmp1 = h1HiValue1 / delta1;
        h1dashFirstCoeff = h1tmp1;
        double h1relVal = MathLib.abs(h1tmp1 * chopReltol);
        double tmp = rclSqrt / (4 * hiLimit1);
        double tmp2 = (tmp >= 100 ? 0 : erfc(MathLib.sqrt(tmp)));
        double tmp3 = MathLib.exp(-tmp);
        double tmp4 = MathLib.sqrt(rclSqrt);
        double tmp5 = MathLib.sqrt(cByR);
        double h2loValue1 = 0;
        double h2hiValue1 = (hiLimit1 != 0 ? (hiLimit1 + rclSqrt * 0.5) * tmp2
                - MathLib.sqrt(hiLimit1 * rclSqrt / MathLib.PI) * tmp3 : 0);
        double h2tmp1 = h2hiValue1 / delta1;
        h2FirstCoeff = h2tmp1;
        double h2relVal = MathLib.abs(h2tmp1 * chopReltol);
        double h3loValue1 = 0;
        tmp = 2 * MathLib.sqrt(hiLimit1 / MathLib.PI) * tmp3 - tmp4 * tmp2;
        double h3hiValue1 = (hiLimit1 != 0 ? (tmp5 * tmp) : 0);
        double h3tmp1 = h3hiValue1 / delta1;
        h3dashFirstCoeff = h3tmp1;
        double h3relVal = MathLib.abs(h3tmp1 * chopReltol);
        double h3hiValue2 = 0;
        double h3tmp2 = 0;

        for (int i = _auxIndex; i > 0; i--) {
            //double delta2 = delta1;
            //double loLimit2 = loLimit1;
            double hiLimit2 = hiLimit1;
            delta1 = timePoints[i] - timePoints[i - 1];
            //  loLimit1 = hiLimit2;
            hiLimit1 = _curTime - timePoints[i - 1];
            if (doh1) {
                double h1loValue2 = h1LoValue1;
                double h1hiValue2 = h1HiValue1;
                double h1tmp2 = h1tmp1;
                h1LoValue1 = h1hiValue2;
                h1HiValue1 = MathLib.sqrt(4 * cByR * hiLimit1 / MathLib.PI);
                h1tmp1 = (h1HiValue1 - h1LoValue1) / delta1;
                h1dashCoeffs[i] = h1tmp1 - h1tmp2;
                if (MathLib.abs(h1dashCoeffs[i]) < h1relVal) {
                    doh1 = false;
                }
            } else {
                h1dashCoeffs[i] = 0;
            }
            if (doh2 || doh3) {
                tmp = rclSqrt / (4 * hiLimit1);
                tmp2 = (tmp >= 100 ? 0 : erfc(MathLib.sqrt(tmp)));
                tmp3 = MathLib.exp(-tmp);
            }
            if (doh2) {
                //double h2loValue2 = h2loValue1;
                double h2hiValue2 = h2hiValue1;
                double h2tmp2 = h2tmp1;
                h2loValue1 = h2hiValue2;
                h2hiValue1 = (hiLimit1 != 0 ? (hiLimit1 + rclSqrt * 0.5) * tmp2 - MathLib.sqrt(hiLimit1 * rclSqrt / MathLib.PI) * tmp3 : 0);
                h2tmp1 = (h2hiValue1 - h2loValue1) / delta1;
                h2Coeffs[i] = h2tmp1 - h2tmp2;
                if (MathLib.abs(h2Coeffs[i]) < h2relVal) {
                    doh2 = false;
                }
            } else if (doh3) {
                //double h3loValue2 = h3loValue1;
                h3loValue1 = h3hiValue2;
                tmp = 2 * MathLib.sqrt(hiLimit1 / MathLib.PI) * tmp3 - tmp4 * tmp2;
                h3hiValue1 = (hiLimit1 != 0 ? (tmp5 * tmp) : 0);
                h3tmp1 = (h3hiValue1 - h3loValue1) / delta1;
                h3dashCoeffs[i] = h3tmp1 - h3tmp2;
                if (MathLib.abs(h3dashCoeffs[i]) < h3relVal) {
                    doh3 = false;
                }
            } else {
                h3dashCoeffs[i] = 0;
            }
        }
    }
//?

    private void rlcCoeffsSetup() {
        double _curTime = tmprl.getTime();
        boolean doh1 = true,
                doh2 = true,
                doh3 = true;
        int i;
        int _auxIndex;
        if (modelListSize <= timeIndex) {
            StandardLog.info("rlcCoeffsSetup: not enough space in coefflist");
        }
        if (td == 0) {
            _auxIndex = timeIndex;
        } else {
            if (_curTime - td <= 0) {
                _auxIndex = 0;
            } else {
                boolean exact = false;
                for (i = timeIndex; i >= 0; i--) {
                    if (_curTime - timePoints[i] == td) {
                        exact = true;
                        break;
                    }
                    if (_curTime - timePoints[i] > td) {
                        break;
                    }
                }
                if ((i < 0) || ((i == 0) && (exact == true))) {
                    StandardLog.info("coeffSetup: i <= 0: some mistake!");
                }
                if (exact == true) {
                    _auxIndex = i - 1;
                } else {
                    _auxIndex = i;
                }
            }
        }
        double loLimit1;
        double hiLimit1;
        double delta1;
        double besselArg = 0;
        double h2loValue1 = 0;
        double h3loValue1 = 0;
        double expArg;
        double expTerm;
        double h2hiValue1 = 0;
        double h3hiValue1 = 0;
        double h1relVal;
        double h2relVal = 0;
        double h3relVal = 0;
        double h2tmp1 = 0;
        double h3tmp1 = 0;
        double alphaSqrtTerm = 0;
        double expBetaTterm = 0;
        double bessi1OverXterm;
        double bessi0term;
        if (_auxIndex != 0) {
            loLimit1 = td;
            hiLimit1 = _curTime - timePoints[_auxIndex];
            delta1 = hiLimit1 - loLimit1;
            h2loValue1 = rlcH2Func(td, td, alpha, beta);
            besselArg = (hiLimit1 > td) ? alpha * MathLib.sqrt(hiLimit1 * hiLimit1 - td * td) : 0;
            expArg = -beta * hiLimit1;
            expTerm = MathLib.exp(expArg);
            bessi1OverXterm = bessI1xOverX(besselArg);
            alphaSqrtTerm = alpha * alpha * td;
            h2hiValue1 =
                    ((alpha == 0) || (hiLimit1 < td)) ? 0 : alphaSqrtTerm * expTerm * bessi1OverXterm;
            h2tmp1 = twiceIntegLinFunc(loLimit1, hiLimit1, loLimit1, h2loValue1,
                    h2hiValue1, loLimit1, hiLimit1) / delta1;
            h2FirstCoeff = h2tmp1;
            h2relVal = MathLib.abs(chopReltol * h2tmp1);
            h3loValue1 = 0;
            bessi0term = bessI0(besselArg);
            expBetaTterm = MathLib.exp(-beta * td);
            h3hiValue1 =
                    ((hiLimit1 <= td) || (beta == 0)) ? 0 : expTerm * bessi0term - expBetaTterm;
            h3tmp1 = integLinFunc(loLimit1, hiLimit1, h3loValue1,
                    h3hiValue1, loLimit1, hiLimit1) / delta1;
            h3dashFirstCoeff = h3tmp1;
            h3relVal = MathLib.abs(h3tmp1 * chopReltol);
        } else {
            h2FirstCoeff = h3dashFirstCoeff = 0;
        }
        loLimit1 = 0;
        hiLimit1 = _curTime - timePoints[timeIndex];
        delta1 = hiLimit1 - loLimit1;
        expArg = -beta * hiLimit1;
        expTerm = MathLib.exp(expArg);
        double h1loValue1 = 0;
        double h1hiValue1 =
                (beta == 0) ? hiLimit1 : ((hiLimit1 == 0) ? 0 : (bessI1(-expArg) + bessI0(-expArg)) * hiLimit1 * expTerm - hiLimit1);
        double h1tmp1 = h1hiValue1 / delta1;
        h1dashFirstCoeff = h1tmp1;
        h1relVal = MathLib.abs(h1tmp1 * chopReltol);
        double loLimit2 = 0;
        double hiLimit2 = 0;
        double delta2;
        double h1tmp2;
        double h1loValue2;
        double h1hiValue2;
        double h2tmp2;
        double h2loValue2;
        double h2hiValue2;
        double h3tmp2;
        double h3loValue2;
        double h3hiValue2;
        for (i = timeIndex; i > 0; i--) {
            if (doh1 || doh2 || doh3) {
                loLimit2 = loLimit1;
                hiLimit2 = hiLimit1;
                delta2 = delta1;
                loLimit1 = hiLimit2;
                hiLimit1 = _curTime - timePoints[i - 1];
                delta1 = timePoints[i] - timePoints[i - 1];
                expArg = -beta * hiLimit1;
                expTerm = MathLib.exp(expArg);
            }
            if (doh1) {
                h1loValue2 = h1loValue1;
                h1hiValue2 = h1hiValue1;
                h1tmp2 = h1tmp1;
                h1loValue1 = h1hiValue2;
                h1hiValue1 =
                        (beta == 0) ? hiLimit1 : ((hiLimit1 == 0) ? 0 : (bessI1(-expArg) + bessI0(-expArg)) * hiLimit1 * expTerm - hiLimit1);
                h1tmp1 = (h1hiValue1 - h1loValue1) / delta1;
                h1dashCoeffs[i] = h1tmp1 - h1tmp2;
                if (MathLib.abs(h1dashCoeffs[i]) <= h1relVal) {
                    doh1 = false;
                }
            } else {
                h1dashCoeffs[i] = 0;
            }
            if (i <= _auxIndex) {
                if (doh2 || doh3) {
                    besselArg = (hiLimit1 > td) ? alpha * MathLib.sqrt(hiLimit1 * hiLimit1 - td * td) : 0;
                }
                if (doh2) {
                    h2loValue2 = h2loValue1;
                    h2hiValue2 = h2hiValue1;
                    h2tmp2 = h2tmp1;
                    h2loValue1 = h2hiValue2;
                    bessi1OverXterm = bessI1xOverX(besselArg);
                    h2hiValue1 =
                            ((alpha == 0) || (hiLimit1 < td)) ? 0 : alphaSqrtTerm * expTerm * bessi1OverXterm;
                    h2tmp1 = twiceIntegLinFunc(loLimit1, hiLimit1, loLimit1,
                            h2loValue1, h2hiValue1, loLimit1, hiLimit1) / delta1;
                    h2Coeffs[i] = h2tmp1 - h2tmp2 + integLinFunc(loLimit2, hiLimit2,
                            h2loValue2, h2hiValue2, loLimit2, hiLimit2);
                    if (MathLib.abs(h2Coeffs[i]) <= h2relVal) {
                        doh2 = false;
                    }
                } else {
                    h2Coeffs[i] = 0;
                }
                if (doh3) {
                    h3loValue2 = h3loValue1;
                    h3hiValue2 = h3hiValue1;
                    h3tmp2 = h3tmp1;
                    h3loValue1 = h3hiValue2;
                    bessi0term = bessI0(besselArg);
                    h3hiValue1 = (hiLimit1 <= td
                            || (beta == 0)) ? 0 : expTerm * bessi0term - expBetaTterm;
                    h3tmp1 = integLinFunc(loLimit1, hiLimit1, h3loValue1, h3hiValue1, loLimit1, hiLimit1) / delta1;
                    h3dashCoeffs[i] = h3tmp1 - h3tmp2;
                    if (MathLib.abs(h3dashCoeffs[i]) <= h3relVal) {
                        doh3 = false;
                    }
                } else {
                    h3dashCoeffs[i] = 0;
                }
            }
        }
        auxIndex = _auxIndex;
    }

    public boolean straightLineCheck(double x1, double y1, double x2, double y2,
            double x3, double y3, double reltol, double abstol) {
        double quadArea1 = (MathLib.abs(y2) + MathLib.abs(y1)) * 0.5 * MathLib.abs(x2 - x1);
        double quadArea2 = (MathLib.abs(y3) + MathLib.abs(y2)) * 0.5 * MathLib.abs(x3 - x2);
        double quadArea3 = (MathLib.abs(y3) + MathLib.abs(y1)) * 0.5 * MathLib.abs(x3 - x1);
        double trArea = MathLib.abs(quadArea3 - quadArea1 - quadArea2);
        double area = quadArea1 + quadArea2;
        if (area * reltol + abstol > trArea) {
            return true;
        } else {
            return false;
        }
    }

    private double secDiriv(double i, double a, double b, double c) {
        double oof = (i == timeIndex + 1 ? curTime : timePoints[(int) i]);
        return ((c - b) / (oof - timePoints[(int) i - 1])
                - (b - a) / (timePoints[(int) i - 1] - timePoints[(int) i - 2]))
                / (oof - timePoints[(int) i - 2]);
    }

    private double lteCalculate(double curTime) {
        double h1dashTfirstCoeff;
        double h2TfirstCoeff = 0;
        double h3dashTfirstCoeff = 0;
        double dashdash;
        double hiLimit1;
        double loLimit1;
        double hiValue1;
        double loValue1;
        double f1i;
        double g1i;
        double eq1LTE = 0;
        double eq2LTE = 0;
        int _auxIndex = 0;
        boolean tdOver = false;
        switch (specialCase) {
            case LC:
            case RG:
                return 0;
            case RLC:
                if (curTime > td) {
                    int i;
                    tdOver = true;
                    int exact = 0;
                    for (i = timeIndex; i >= 0; i--) {
                        if (curTime - timePoints[i] == td) {
                            exact = 1;
                            break;
                        }
                        if (curTime - timePoints[i] > td) {
                            break;
                        }
                    }
                    if ((i < 0) || ((i == 0) && (exact == 1))) {
                        StandardLog.info("lteCalculate: i <= 0: some mistake!");
                    }
                    if (exact == 1) {
                        _auxIndex = i - 1;
                    } else {
                        _auxIndex = i;
                    }
                } else {
                    tdOver = false;
                }
                hiLimit1 = curTime - timePoints[timeIndex];
                loLimit1 = 0;
                hiValue1 = rlcH1dashTwiceIntegFunc(hiLimit1, beta);
                loValue1 = 0;
                f1i = hiValue1;
                g1i = integLinFunc(loLimit1, hiLimit1, loValue1, hiValue1,
                        loLimit1, hiLimit1);
                h1dashTfirstCoeff = 0.5 * f1i
                        * (curTime - timePoints[timeIndex]) - g1i;
                if (tdOver) {
                    hiLimit1 = curTime - timePoints[_auxIndex];
                    loLimit1 = timePoints[timeIndex] - timePoints[_auxIndex];
                    loLimit1 = MathLib.max(td, loLimit1);
                    hiValue1 = rlcH2Func(hiLimit1, td, alpha, beta);
                    loValue1 = rlcH2Func(loLimit1, td, alpha, beta);
                    f1i = twiceIntegLinFunc(loLimit1, hiLimit1, loLimit1, loValue1, hiValue1, loLimit1,
                            hiLimit1);
                    g1i = thriceIntegLinFunc(loLimit1, hiLimit1, loLimit1, loLimit1, loValue1,
                            hiValue1, loLimit1, hiLimit1);
                    h2TfirstCoeff = 0.5 * f1i * (curTime - td - timePoints[_auxIndex]) - g1i;
                    hiValue1 = rlcH3dashIntegFunc(hiLimit1, td, beta);
                    loValue1 = rlcH3dashIntegFunc(loLimit1, td, beta);
                    f1i = integLinFunc(loLimit1, hiLimit1, loValue1, hiValue1, loLimit1,
                            hiLimit1);
                    g1i = twiceIntegLinFunc(loLimit1, hiLimit1, loLimit1, loValue1,
                            hiValue1, loLimit1, hiLimit1);
                    h3dashTfirstCoeff = 0.5 * f1i * (curTime - td - timePoints[_auxIndex]) - g1i;
                }
                dashdash = secDiriv(timeIndex + 1, v1[timeIndex - 1], v1[timeIndex], wrk.getRhsOldRealAt(posIndex1)
                        - wrk.getRhsOldRealAt(negIndex1));
                eq1LTE += admit * MathLib.abs(dashdash * h1dashTfirstCoeff);
                if (tdOver) {
                    dashdash = secDiriv(_auxIndex + 1, v1[_auxIndex - 1],
                            v1[_auxIndex], v1[_auxIndex + 1]);
                    eq2LTE += admit * MathLib.abs(dashdash * h3dashTfirstCoeff);
                }
                dashdash = secDiriv(timeIndex + 1, v2[timeIndex - 1], v2[timeIndex], wrk.getRhsOldRealAt(posIndex2)
                        - wrk.getRhsOldRealAt(negIndex2));
                eq2LTE += admit * MathLib.abs(dashdash * h1dashTfirstCoeff);
                if (tdOver) {
                    dashdash = secDiriv(_auxIndex + 1, v2[_auxIndex - 1], v2[_auxIndex], v2[_auxIndex + 1]);
                    eq1LTE += admit * MathLib.abs(dashdash
                            * h3dashTfirstCoeff);
                }
                if (tdOver) {
                    dashdash = secDiriv(_auxIndex + 1, i1[_auxIndex - 1], i1[_auxIndex], i1[_auxIndex + 1]);
                }
                eq2LTE += MathLib.abs(dashdash * h2TfirstCoeff);
                if (tdOver) {
                    dashdash = secDiriv(_auxIndex + 1, i2[_auxIndex - 1], i2[_auxIndex], i2[_auxIndex + 1]);
                }
                eq1LTE += MathLib.abs(dashdash * h2TfirstCoeff);
                break;
            case RC:
                hiLimit1 = curTime - timePoints[timeIndex];
                loLimit1 = 0;
                hiValue1 = rcH1dashTwiceIntegFunc(hiLimit1, cByR);
                loValue1 = 0;
                f1i = hiValue1;
                g1i = integLinFunc(loLimit1, hiLimit1, loValue1, hiValue1, loLimit1, hiLimit1);
                h1dashTfirstCoeff = 0.5 * f1i * (curTime - timePoints[timeIndex]) - g1i;
                hiValue1 = rcH2TwiceIntegFunc(hiLimit1, rclSqrt);
                loValue1 = 0;
                f1i = hiValue1;
                g1i = integLinFunc(loLimit1, hiLimit1, loValue1, hiValue1, loLimit1, hiLimit1);
                h1dashTfirstCoeff = 0.5 * f1i * (curTime - timePoints[timeIndex]) - g1i;
                hiValue1 = rcH2TwiceIntegFunc(hiLimit1, rclSqrt);
                loValue1 = 0;
                f1i = hiValue1;
                g1i = integLinFunc(loLimit1, hiLimit1, loValue1,
                        hiValue1, loLimit1, hiLimit1);
                h1dashTfirstCoeff = 0.5 * f1i * (curTime
                        - timePoints[timeIndex]) - g1i;
                dashdash = secDiriv(timeIndex + 1, v1[timeIndex - 1],
                        v1[timeIndex], wrk.getRhsOldRealAt(posIndex1)
                        - wrk.getRhsOldRealAt(negIndex1));
                eq1LTE += MathLib.abs(dashdash * h1dashTfirstCoeff);
                eq2LTE += MathLib.abs(dashdash * h3dashTfirstCoeff);
                dashdash = secDiriv(timeIndex + 1, v2[timeIndex - 1],
                        v2[timeIndex], wrk.getRhsOldRealAt(posIndex2)
                        - wrk.getRhsOldRealAt(negIndex2));
                eq2LTE += MathLib.abs(dashdash * h1dashTfirstCoeff);
                eq1LTE += MathLib.abs(dashdash * h3dashTfirstCoeff);
                dashdash = secDiriv(timeIndex + 1, i1[timeIndex - 1],
                        i1[timeIndex], wrk.getRhsOldRealAt(brEq1));
                eq2LTE += MathLib.abs(dashdash * h2TfirstCoeff);
                dashdash = secDiriv(timeIndex + 1, i2[timeIndex - 1],
                        i2[timeIndex], wrk.getRhsOldRealAt(brEq2));
                eq1LTE += MathLib.abs(dashdash * h2TfirstCoeff);
                break;
            default:
                return 0;
        }
        StandardLog.info(String.format("%s: LTE / input for Eq1 at time %g is: %g",
                getInstName(), curTime, eq1LTE / input1));
        StandardLog.info(String.format("%s: LTE / input for Eq2 at time %g is: %g",
                getInstName(), curTime, eq2LTE / input1));
        return (MathLib.abs(eq1LTE) + MathLib.abs(eq2LTE));
    }

    public boolean unSetup() {
        if (brEq1 != 0) {
            ckt.deleteNode(brEq1);
            brEq1 = 0;
        }
        if (brEq2 != 0) {
            ckt.deleteNode(brEq2);
            brEq2 = 0;
        }
        return true;
    }

    public boolean temperature() {
        switch (specialCase) {
            case LC:
                imped = MathLib.sqrt(induct / capac);
                admit = 1 / imped;
                td = MathLib.sqrt(induct * capac) * length;
                attenuation = 1.0;
                break;
            case RLC:
                imped = MathLib.sqrt(induct / capac);
                admit = 1 / imped;
                td = MathLib.sqrt(induct * capac) * length;
                alpha = 0.5 * (resist / induct);
                beta = alpha;
                attenuation = MathLib.exp(-beta * td);
                if (alpha > 0) {
                    integH1dash = -1.0;
                    integH2 = 1.0 - attenuation;
                    integH3dash = -attenuation;
                } else if (alpha == 0) {
                    integH1dash = integH2 = integH3dash = 0;
                } else {
                    StandardLog.info("temp: error: alpha < 0");
                }
                h1dashCoeffs = null;
                h2Coeffs = null;
                h3dashCoeffs = null;
                if (!truncDontCut) {
                    int maxiter = 50;
                    double xbig = td + 9 * td;
                    double xsmall = td;
                    double xmid = 0.5 * (xbig + xsmall);
                    double y1small = rlcH2Func(xsmall, td, alpha, beta);
                    double y2small = rlcH3dashFunc(xsmall, td, beta, beta);
                    int iters = 0;
                    while (true) {
                        iters++;
                        double y1big = rlcH2Func(xbig, td, alpha, beta);
                        double y1mid = rlcH2Func(xmid, td, alpha, beta);
                        double y2big = rlcH3dashFunc(xbig, td, beta, beta);
                        double y2mid = rlcH3dashFunc(xmid, td, beta, beta);
                        int done = straightLineCheck(xbig, y1big, xmid, y1mid, xsmall,
                                y1small, stLineReltol, stLineAbstol)
                                && straightLineCheck(xbig, y1big, xmid, y1mid, xsmall,
                                y1small, stLineReltol, stLineAbstol) ? 2 : 1;
                        if (done == 2 || iters > maxiter) {
                            break;
                        }
                        xbig = xmid;
                        xmid = 0.5 * (xbig + xsmall);
                    }
                    maxSafeStep = xbig - td;
                }
                break;
            case RC:
                cByR = capac / resist;
                rclSqrt = resist * capac * length * length;
                integH1dash = 0;
                integH2 = 1.0;
                integH3dash = 0;
                h1dashCoeffs = null;
                h2Coeffs = null;
                h3dashCoeffs = null;
                break;
            case RG:
                break;
            default:
                return false;
        }
        v1 = null;
        i1 = null;
        v2 = null;
        i2 = null;
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        int maxiter = 2;
        int iterations = 0;
        switch (specialCase) {
            case LC:
            case RLC:
                if (stepLimit == STEPLIMIT) {
                    timeStep = MathLib.min(timeStep, td);
                } else {
                    double _i1 = ((wrk.getRhsOldRealAt(posIndex2)
                            - wrk.getRhsOldRealAt(negIndex2))
                            * admit + wrk.getRhsOldRealAt(brEq2)) * attenuation;
                    double _i2 = (v2[timeIndex] * admit + i2[timeIndex]) * attenuation;
                    double i3 = (v2[timeIndex - 1] * admit + i2[timeIndex - 1]) * attenuation;
                    double i4 = ((wrk.getRhsOldRealAt(posIndex1)
                            - wrk.getRhsOldRealAt(negIndex1))
                            * admit + wrk.getRhsOldRealAt(brEq1)) * attenuation;
                    double i5 = (v1[timeIndex] * admit + i1[timeIndex]) * attenuation;
                    double i6 = (v1[timeIndex - 1] * admit + i1[timeIndex - 1]) * attenuation;
                    double d1 = (_i1 - _i2) / (tmprl.getTime() - timePoints[timeIndex]);
                    double d2 = (_i2 - i3) / (timePoints[timeIndex] - timePoints[timeIndex - 1]);
                    double d3 = (i4 - i5) / (tmprl.getTime() - timePoints[timeIndex]);
                    double d4 = (i5 - i6) / (timePoints[timeIndex] - timePoints[timeIndex - 1]);
                    if ((MathLib.abs(d1 - d2) >= reltol * MathLib.max(MathLib.abs(d1), MathLib.abs(d2))
                            + abstol)
                            || (MathLib.abs(d3 - d4) >= reltol * MathLib.max(MathLib.abs(d3), MathLib.abs(d4))
                            + abstol)) {
                        timeStep = MathLib.min(timeStep, td);
                    }
                }
                break;
            case RC:
            case RG:
                break;
            default:
                return 0;
        }
        if (specialCase == RLC && !truncDontCut) {
            timeStep = MathLib.min(timeStep, maxSafeStep);
        }
        if (lteConType != NOCONTROL) {
            switch (specialCase) {
                case RLC:
                case RC:
                    double tolerance = env.getTrTol() * (env.getRelTol() * (MathLib.abs(input1) + MathLib.abs(input2)) + env.getAbsTol());
                    double current_lte = lteCalculate(tmprl.getTime());
                    if (current_lte >= tolerance) {
                        if (truncNR) {
                            double x = tmprl.getTime();
                            double y = current_lte;
                            while (true) {
                                double deriv_delta = 01 * (x - timePoints[timeIndex]);
                                if (deriv_delta <= 0) {
                                    StandardLog.info("trunc: error: timestep is now less than zero");
                                }
                                double deriv = lteCalculate(x + deriv_delta) - y;
                                deriv /= deriv_delta;
                                double change = (tolerance - y) / deriv;
                                x += change;
                                if (maxiter == 0) {
                                    if (MathLib.abs(change) <= MathLib.abs(deriv_delta)) {
                                        break;
                                    }
                                } else {
                                    iterations++;
                                    if (iterations >= maxiter) {
                                        break;
                                    }
                                }
                                y = lteCalculate(x);
                            }
                            double tmp = x - timePoints[timeIndex];
                            timeStep = MathLib.min(timeStep, tmp);
                        } else {
                            timeStep *= 0.5;
                        }
                    }
                    break;
                case RG:
                case LC:
                    break;
                default:
                    return 0;
            }
        }
        if (timeStep >= td) {
            StandardLog.info("trunc: Warning: Timestep bigger than delay of line " + getModelName());
        }
        return timeStep;
    }

    private double erfc(double x) {
        double sqrtPi = MathLib.sqrt(MathLib.PI);
        x = MathLib.abs(x);
        double n = 1.0;
        double xSq = 2.0 * x * x;
        double sum1 = 0;

        if (x > 3.23) {
            double tmp1 = MathLib.exp(-x * x) / (sqrtPi * x);
            double sum2 = tmp1;

            while (sum1 != sum2) {
                sum1 = sum2;
                tmp1 = -1.0 * (tmp1 / xSq);
                sum2 += tmp1;
                n += 2.0;
            }
            return sum2;
        } else {
            double temp1 = (2.0 / sqrtPi) * MathLib.exp(-x * x) * x;
            double sum2 = temp1;
            while (sum1 != sum2) {
                n += 2.0;
                sum1 = sum2;
                temp1 *= xSq / n;
                sum2 += temp1;
            }
            return 1.0 - sum2;
        }
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}
