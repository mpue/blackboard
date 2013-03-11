/*
 * mos2.java
 *
 * Created on August 28, 2006, case 2:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.StateTable;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.StateTable.StateVector;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;

import javolution.lang.MathLib;
import javolution.util.StandardLog;
import ktb.math.Bool;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOS2Instance extends MOS2ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;
    private double xd;
    private double oxideCapFactor;

    /* Device mode : 1 = normal, -1 = inverse */
    private int _mode = 1;
    private double cgs;
    private double cgd;
    private double cgb;
    private double sens;

    /* Difference temperature from circuit temperature */
    private double dtemp;

    /* Temperature corrected transconductance */
    private double tTransconductance;

    /* Temperature corrected surface mobility */
    private double tSurfMob;

    /* Temperature corrected phi */
    private double tPhi;

    /* Temperature corrected vto */
    private double tVto;

    /* Temperature corrected saturation current */
    private double tSatCur;

    /* Temperature corrected saturation current density */
    private double tSatCurDens;

    /* Temperature corrected B - D Capacitance */
    private double tCbd;

    /* Temperature corrected B - S Capacitance */
    private double tCbs;

    /* Temperature corrected Bulk bottom Capacitance */
    private double tCj;

    /* Temperature corrected Bulk side Capacitance */
    private double tCjsw;

    /* Temperature corrected Bulk potential */
    private double tBlkPot;

    /* Temperature corrected transition point in curve matching Fc * Vj */
    private double tDepCap;

    /* Temperature corrected vbi */
    private double tVbi;

    /* Source conductance */
    private double srcConductance;

    /* Drain conductance */
    private double drnConductance;
    private double von;
    private double vdsat;
    private double srcVcrit;
    private double drnVcrit;
    private double cd;
    private double cbs;
    private double cbd;
    private double gmbs;
    private double gm;
    private double gds;
    private double gbd;
    private double gbs;
    private double capbd;
    private double capbs;
    // Zero voltage bulk - source capacitance
    private double czbs;
    // Zero voltage bulk - drain capacitance
    private double czbd;
    // Zero voltage bulk - drain sidewall capacitance
    private double czbdsw;
    // Zero voltage bulk - source sidewall capacitance
    private double czbssw;
    private double f2d;
    private double f3d;
    private double f4d;
    private double f2s;
    private double f3s;
    private double f4s;
    private Complex drnDrnNode;
    private Complex gateGateNode;
    private Complex srcSrcNode;
    private Complex blkBlkNode;
    private Complex drnPrmDrnPrmNode;
    private Complex srcPrmSrcPrmNode;
    private Complex drnDrnPrmNode;
    private Complex gateBlkNode;
    private Complex gateDrnPrmNode;
    private Complex gateSrcPrmNode;
    private Complex srcSrcPrmNode;
    private Complex blkDrnPrmNode;
    private Complex blkSrcPrmNode;
    private Complex drnPrmSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex blkGateNode;
    private Complex drnPrmGateNode;
    private Complex srcPrmGateNode;
    private Complex srcPrmSrcNode;
    private Complex drnPrmBlkNode;
    private Complex srcPrmBlkNode;
    private Complex srcPrmDrnPrmNode;

    /* bulk - drain voltage */
    private StateVector vbdStates;

    /* bulk - source voltage */
    private StateVector vbsStates;

    /* gate - source voltage */
    private StateVector vgsStates;

    /* drain - source voltage */
    private StateVector vdsStates;

    /* gate - source capacitor value */
    private StateVector capgsStates;

    /* gate - source capacitor charge */
    private StateVector qgsStates;

    /* gate - source capacitor current */
    private StateVector cqgsStates;

    /* gate - drain capacitor value */
    private StateVector capgdStates;

    /* gate - drain capacitor charge */
    private StateVector qgdStates;

    /* gate - drain capacitor current */
    private StateVector cqgdStates;

    /* gate - bulk capacitor value */
    private StateVector capgbStates;

    /* gate - bulk capacitor charge */
    private StateVector qgbStates;

    /* gate - bulk capacitor current */
    private StateVector cqgbStates;

    /* bulk - drain capacitor charge */
    private StateVector qbdStates;

    /* bulk - drain capacitor current */
    private StateVector cqbdStates;

    /* bulk - source capacitor charge */
    private StateVector qbsStates;

    /* bulk - source capacitor current */
    private StateVector cqbsStates;
    private Real capgs = Real.zero();
    private Real capgd = Real.zero();
    private Real capgb = Real.zero();
    private Real ceqgb = Real.zero();
    private Real ceqgd = Real.zero();
    private Real ceqgs = Real.zero();
    private Real gcgb = Real.zero();
    private Real gcgd = Real.zero();
    private Real gcgs = Real.zero();
    private Real geq = Real.zero();
    private Real ceq = Real.zero();
    private Bool check = Bool.FALSE();
    private double[] a4 = new double[4];
    private double[] b4 = new double[4];
    private double[] x4 = new double[8];
    private double[] poly4 = new double[8];

    /** Creates a new instance of mos2 */
    public MOS2Instance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        if (m == 0) {
            m = env.getDefaultMosM();
        }
        if (l == 0) {
            l = env.getDefaultMosL();
        }
        if (w == 0) {
            w = env.getDefaultMosW();
        }
        if (drnArea == 0) {
            drnArea = env.getDefaultMosAD();
        }
        if (srcArea == 0) {
            srcArea = env.getDefaultMosAS();
        }
        vbdStates = stateTable.createRow();
        vbsStates = stateTable.createRow();
        vgsStates = stateTable.createRow();
        vdsStates = stateTable.createRow();
        capgsStates = stateTable.createRow();
        qgsStates = stateTable.createRow();
        cqgsStates = stateTable.createRow();
        capgdStates = stateTable.createRow();
        qgdStates = stateTable.createRow();
        cqgdStates = stateTable.createRow();
        capgbStates = stateTable.createRow();
        qgbStates = stateTable.createRow();
        cqgbStates = stateTable.createRow();
        qbdStates = stateTable.createRow();
        cqbdStates = stateTable.createRow();
        qbsStates = stateTable.createRow();
        cqbsStates = stateTable.createRow();
        if ((drnResistance != 0 || sheetResistance != 0) && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "internal#drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if ((srcResistance != 0 || sheetResistance != 0) && srcPrmIndex == 0) {
            srcPrmIndex = ckt.makeVoltNode(getInstName() + "internal#source").getIndex();
        } else {
            srcPrmIndex = srcIndex;
        }
        if (drnResistance != 0) {
            drnConductance = m / drnResistance;
        } else if (sheetResistance != 0) {
            drnConductance = m / (sheetResistance * drnSquares);
        }
        if (srcResistance != 0) {
            srcConductance = m / srcResistance;
        } else if (sheetResistance != 0) {
            srcConductance = m / (sheetResistance * srcSquares);
        }
        if (l - 2 * latDiff <= 0) {
            StandardLog.warning(getInstName() + ": effective channel length less than zero");
        }
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        gateGateNode = wrk.aquireNode(gateIndex, gateIndex);
        srcSrcNode = wrk.aquireNode(srcIndex, srcIndex);
        blkBlkNode = wrk.aquireNode(blkIndex, blkIndex);
        drnPrmDrnPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmIndex);
        srcPrmSrcPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmIndex);
        drnDrnPrmNode = wrk.aquireNode(drnIndex, drnPrmIndex);
        gateBlkNode = wrk.aquireNode(gateIndex, blkIndex);
        gateDrnPrmNode = wrk.aquireNode(gateIndex, drnPrmIndex);
        gateSrcPrmNode = wrk.aquireNode(gateIndex, srcPrmIndex);
        srcSrcPrmNode = wrk.aquireNode(srcIndex, srcPrmIndex);
        blkDrnPrmNode = wrk.aquireNode(blkIndex, drnPrmIndex);
        blkSrcPrmNode = wrk.aquireNode(blkIndex, srcPrmIndex);
        drnPrmSrcPrmNode = wrk.aquireNode(drnPrmIndex, srcPrmIndex);
        drnPrmDrnNode = wrk.aquireNode(drnPrmIndex, drnIndex);
        blkGateNode = wrk.aquireNode(blkIndex, gateIndex);
        drnPrmGateNode = wrk.aquireNode(drnPrmIndex, gateIndex);
        srcPrmGateNode = wrk.aquireNode(srcPrmIndex, gateIndex);
        srcPrmSrcNode = wrk.aquireNode(srcPrmIndex, srcIndex);
        drnPrmBlkNode = wrk.aquireNode(drnPrmIndex, blkIndex);
        srcPrmBlkNode = wrk.aquireNode(srcPrmIndex, blkIndex);
        srcPrmDrnPrmNode = wrk.aquireNode(srcPrmIndex, drnPrmIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        int xnrm;
        int xrev;
        if (_mode < 0) {
            xnrm = 0;
            xrev = 1;
        } else {
            xnrm = 1;
            xrev = 0;
        }
        // Meyer's model parameters
        double effectiveLength = l - 2 * latDiff;
        double gateSrcOverlapCap = gateSrcOverlapCapFactor * m * w;
        double gateDrnOverlapCap = gateDrnOverlapCapFactor * m * w;
        double gateBlkOverlapCap = gateBlkOverlapCapFactor * m * effectiveLength;
        capgs.set((2 * capgsStates.get(0) + gateSrcOverlapCap));
        capgd.set((2 * capgdStates.get(0) + gateDrnOverlapCap));
        capgb.set((2 * capgbStates.get(0) + gateBlkOverlapCap));
        double xgs = capgs.get() * tmprl.getOmega();
        double xgd = capgd.get() * tmprl.getOmega();
        double xgb = capgb.get() * tmprl.getOmega();
        double xbd = capbd * tmprl.getOmega();
        double xbs = capbs * tmprl.getOmega();
        // Load matrix
        gateGateNode.imagPlusEq(xgd + xgs + xgb);
        blkBlkNode.imagPlusEq(xgb + xbd + xbs);
        drnPrmDrnPrmNode.imagPlusEq(xgd + xbd);
        srcPrmSrcPrmNode.imagPlusEq(xgs + xbs);
        gateBlkNode.imagMinusEq(xgb);
        gateDrnPrmNode.imagMinusEq(xgd);
        gateSrcPrmNode.imagMinusEq(xgs);
        blkGateNode.imagMinusEq(xgb);
        blkDrnPrmNode.imagMinusEq(xbd);
        blkSrcPrmNode.imagMinusEq(xbs);
        drnPrmGateNode.imagMinusEq(xgd);
        drnPrmBlkNode.imagMinusEq(xbd);
        srcPrmGateNode.imagMinusEq(xgs);
        srcPrmBlkNode.imagMinusEq(xbs);
        drnDrnNode.realPlusEq(drnConductance);
        srcSrcNode.realPlusEq(srcConductance);
        blkBlkNode.realPlusEq(gbd + gbs);
        drnPrmDrnPrmNode.realPlusEq(drnConductance
                + gds + gbd + xrev * (gm + gmbs));
        srcPrmSrcPrmNode.realPlusEq(srcConductance
                + gds + gbs + xnrm * (gm + gmbs));
        drnDrnPrmNode.realMinusEq(drnConductance);
        srcSrcPrmNode.realMinusEq(srcConductance);
        blkDrnPrmNode.realMinusEq(gbd);
        blkSrcPrmNode.realMinusEq(gbs);
        drnPrmDrnNode.realMinusEq(drnConductance);
        drnPrmGateNode.realPlusEq((xnrm - xrev) * gm);
        drnPrmBlkNode.realPlusEq(-gbd + (xnrm - xrev) * gmbs);
        drnPrmSrcPrmNode.realMinusEq(gds + xnrm * (gm + gmbs));
        srcPrmGateNode.realMinusEq((xnrm - xrev) * gm);
        srcPrmSrcNode.realMinusEq(srcConductance);
        srcPrmBlkNode.realMinusEq(gbs + (xnrm - xrev) * gmbs);
        srcPrmDrnPrmNode.realMinusEq(gds + xrev * (gm + gmbs));
        return true;
    }

    public boolean convTest(Mode mode) {
        double vbs = type * (wrk.getRhsAt(blkIndex).getReal()
                - wrk.getRhsAt(srcPrmIndex).getReal());
        double vgs = type * (wrk.getRhsAt(gateIndex).getReal()
                - wrk.getRhsAt(srcPrmIndex).getReal());
        double vds = type * (wrk.getRhsAt(drnPrmIndex).getReal()
                - wrk.getRhsAt(srcPrmIndex).getReal());
        double vbd = vbs - vds;
        double vgd = vgs - vds;
        double vgdo = vgsStates.get(0) - vdsStates.get(0);
        double delvbs = vbs - vbsStates.get(0);
        double delvbd = vbd - vbdStates.get(0);
        double delvgs = vgs - vgsStates.get(0);
        double delvds = vds - vdsStates.get(0);
        double delvgd = vgd - vgdo;
        double cdhat;
        if (_mode >= 0) {
            cdhat = cd - gbd * delvbd + gmbs * delvbs + gm * delvgs + gds * delvds;
        } else {
            cdhat = cd - (gbd - gmbs) * delvbd - gm * delvgd + gds * delvds;
        }
        double cbhat = cbs + cbd + gbd * delvbd + gbs * delvbs;
        double tol = env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd))
                + env.getAbsTol();
        if (MathLib.abs(cdhat - cd) >= tol) {
            return false;
        } else {
            tol = env.getRelTol() * MathLib.max(MathLib.abs(cbhat),
                    MathLib.abs(cbs + cbd)) + env.getAbsTol();
            if (MathLib.abs(cbhat - (cbs + cbd)) > tol) {
                return false;
            }
        }
        return true;
    }

    public boolean loadInitCond() {
        if (icVBS == 0) {
            icVBS =
                    wrk.getRhsAt(blkIndex).getReal()
                    - wrk.getRhsAt(srcIndex).getReal();
        }
        if (icVDS == 0) {
            icVDS =
                    wrk.getRhsAt(drnIndex).getReal()
                    - wrk.getRhsAt(srcIndex).getReal();
        }
        if (icVGS == 0) {
            icVGS =
                    wrk.getRhsAt(gateIndex).getReal()
                    - wrk.getRhsAt(srcIndex).getReal();
        }
        return true;
    }
    static double[] sig1 = new double[]{1, -1, 1, -1};
    static double[] sig2 = new double[]{1, 1, -1, -1};

    /*
     *     Evaluate saturation voltage and derivatives
     *     according to baum's theory of scattering velocity
     *     saturation
     */
    private double evalVdsat(double vgsx, double vbin, double eta, double phiMinVbs, double xv, double gammad, double sarg3, double _vdsat) {
        double fi;
        double p3;
        double p4;
        double ro;
        double y3;
        double v1 = (vgsx - vbin) / eta + phiMinVbs;
        double v2 = phiMinVbs;
        double a1 = gammad / 0.75;
        double b1 = -2 * (v1 + xv);
        double c1 = -2 * gammad * xv;
        double d1 = 2 * v1 * (v2 + xv) - v2 * v2 - 4 / 3 * gammad * sarg3;
        double a = -b1;
        double b = a1 * c1 - 4 * d1;
        double c = -d1 * (a1 * a1 - 4 * b1) - c1 * c1;
        double r = -a * a / 3 + b;
        double s = 2 * a * a * a / 27 - a * b / 3 + c;
        double r3 = r * r * r;
        double s2 = s * s;
        double p = s2 / 4 + r3 / 27;
        double p0 = MathLib.abs(p);
        double p2 = MathLib.sqrt(p0);
        if (p < 0) {
            ro = MathLib.sqrt(s2 / 4 + p0);
            ro = MathLib.log(ro) / 3;
            ro = MathLib.exp(ro);
            fi = MathLib.atan(-2 * p2 / s);
            y3 = 2 * ro * MathLib.cos(fi / 3) - a / 3;
        } else {
            p3 = (-s / 2 + p2);
            p3 = MathLib.exp(MathLib.log(MathLib.abs(p3)) / 3);
            p4 = (-s / 2 - p2);
            p4 = MathLib.exp(MathLib.log(MathLib.abs(p4)) / 3);
            y3 = p3 + p4 - a / 3;
        }
        int iknt = 0;
        double a3 = MathLib.sqrt(a1 * a1 / 4 - b1 + y3);
        double b3 = MathLib.sqrt(y3 * y3 / 4 - d1);
        for (int i = 1; i <= 4; i++) {
            a4[i - 1] = a1 / 2 + sig1[i - 1] * a3;
            b4[i - 1] = y3 / 2 + sig2[i - 1] * b3;
            double delta4 = a4[i - 1] * a4[i - 1] / 4 - b4[i - 1];
            if (delta4 < 0) {
                continue;
            }
            iknt = iknt + 1;
            double tmp = MathLib.sqrt(delta4);
            x4[iknt - 1] = -a4[i - 1] / 2 + tmp;
            iknt = iknt + 1;
            x4[iknt - 1] = -a4[i - 1] / 2 - tmp;
        }
        int jknt = 0;
        double xvalid = 0;
        for (int j = 1; j <= iknt; j++) {
            if (x4[j - 1] <= 0) {
                continue;
            }
            poly4[j - 1] = x4[j - 1] * x4[j - 1] * x4[j - 1] * x4[j - 1] + a1 * x4[j - 1]
                    * x4[j - 1] * x4[j - 1];
            poly4[j - 1] = poly4[j - 1] + b1 * x4[j - 1] * x4[j - 1] + c1 * x4[j - 1] + d1;
            if (MathLib.abs(poly4[j - 1]) > 1e-6) {
                continue;
            }
            jknt = jknt + 1;
            if (jknt <= 1) {
                xvalid = x4[j - 1];
            }
            if (x4[j - 1] > xvalid) {
                continue;
            }
            xvalid = x4[j - 1];
        }
        if (jknt > 0) {
            _vdsat = xvalid * xvalid - phiMinVbs;
        }
        return _vdsat;
    }

    private static enum KEY {

        INIT, CASE1, CASE2, CASE3, CASE4, CASE5,
        CASE6, CASE7, CASE8, NEXT1, NEXT2, LOAD, DONE
    };

    public boolean load(Mode mode) {
        double arg;
        double cdhat;
        double idrain = 0;
        double sarg = 0;
        double sargsw;
        double vbd = 0;
        double vbs = 0;
        double vds = 0;
        double _vdsat = 0;
        double vgb = 0;
        double vgd = 0;
        double vgs = 0;
        double _von = 0;
        double xfact = 0;
        double beta1 = 0;
        double dsrgdb = 0;
        double d2sdb2;
        double sqrtPhi = 0;
        double sqrtPhi3 = 0;
        double bSrg = 0;
        double d2bdb2;
        double factor = 0;
        double dbrgdb = 0;
        double eta = 0;
        double vbin = 0;
        double dgddb2;
        double dgddvb = 0;
        double dgdvds = 0;
        double gamasd = 0;
        double ddxwd;
        double gammad = 0;
        double vth = 0;
        double cfs;
        double cdonco;
        double xn = 0;
        double argg = 0;
        double vgst = 0;
        double sarg3 = 0;
        double sbiarg = 0;
        double dgdvbs = 0;
        double body = 0;
        double gdbdv = 0;
        double dodvbs = 0;
        double dodvds = 0;
        double dxndvd = 0;
        double dxndvb = 0;
//        double udenom;
        double dudvgs = 0;
        double dudvds = 0;
        double dudvbs = 0;
        double argv;
        double ufact = 0;
        double ueff = 0;
        double dsdvgs = 0;
        double dsdvbs = 0;
        double bsarg = 0;
        double dbsrdb = 0;
        double bodys = 0;
        double gdbdvs = 0;
        double sargv = 0;
        double xlfact;
        double dldsat = 0;
        double dldvgs = 0;
        double dldvds = 0;
        double dldvbs = 0;
//        double dfact;
        double clfact = 0;
        double tmp;
        double xlamda = lambda;
        double lvbs = _mode == 1 ? vbs : vbd;
        double lvds = _mode * vds;
        double lvgs = _mode == 1 ? vgs : vgd;
        double phiMinVbs = tPhi - lvbs;
        double effectiveLength = l - 2 * latDiff;
        double gateSrcOverlapCap = gateSrcOverlapCapFactor * m * w;
        double gateDrnOverlapCap = gateDrnOverlapCapFactor * m * w;
        double gateBlkOverlapCap = gateBlkOverlapCapFactor * m * effectiveLength;
        double beta = tTransconductance * w * m / effectiveLength;
        double oxideCap = oxideCapFactor * effectiveLength * m * w;
        double vt = KoverQ * temp;
        boolean bypass = false;
        if (mode.contains(MODE.INIT_FLOAT, MODE.INIT_PRED, MODE.INIT_SMSIG, MODE.INIT_TRAN)
                || (mode.contains(MODE.INIT_FIX) && !off)) {
            if (mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN)) {
                xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vbsStates.set(0, vbsStates.get(1));
                vbs = (1 + xfact) * (vbsStates.get(1)) - (xfact * (vbsStates.get(2)));
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * (vgsStates.get(1)) - (xfact * (vgsStates.get(2)));
                vdsStates.set(0, vdsStates.get(1));
                vds = (1 + xfact) * (vdsStates.get(1)) - (xfact * (vdsStates.get(2)));
                vbdStates.set(0, vbsStates.get(0) - vdsStates.get(0));
            } else {
                vbs = type * (wrk.getRhsOldAt(blkIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmIndex).getReal());
                vgs = type * (wrk.getRhsOldAt(gateIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmIndex).getReal());
                vds = type * (wrk.getRhsOldAt(drnPrmIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmIndex).getReal());
            }
            vbd = vbs - vds;
            vgd = vgs - vds;
            double vgdo = vgsStates.get(0) - vdsStates.get(0);
            double delvbs = vbs - vbsStates.get(0);
            double delvbd = vbd - vbdStates.get(0);
            double delvgs = vgs - vgsStates.get(0);
            double delvds = vds - vdsStates.get(0);
            double delvgd = vgd - vgdo;
            if (_mode >= 0) {
                cdhat = cd - gbd * delvbd + gmbs * delvbs + gm * delvgs + gds * delvds;
            } else {
                cdhat = cd + (gmbs - gbd) * delvbd - gm * delvgd + gds * delvds;
            }
            double cbhat = cbs + cbd + gbd * delvbd + gbs * delvbs;
            double tempv = MathLib.max(MathLib.abs(cbhat),
                    MathLib.abs(cbs + cbd)) + env.getAbsTol();
            if (!mode.contains(MODE.INIT_PRED,
                    MODE.INIT_TRAN, MODE.INIT_SMSIG) && env.isBypass()) {
                if (MathLib.abs(cbhat - (cbs + cbd)) < env.getRelTol() * tempv) {
                    if ((MathLib.abs(delvbs) < (env.getRelTol() * MathLib.max(MathLib.abs(vbs),
                            MathLib.abs(vbsStates.get(0)))
                            + env.getVoltTol()))) {
                        if ((MathLib.abs(delvbd) < (env.getRelTol() * MathLib.max(MathLib.abs(vbd),
                                MathLib.abs(vbdStates.get(0)))
                                + env.getVoltTol()))) {
                            if ((MathLib.abs(delvgs) < (env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                                    MathLib.abs(vgsStates.get(0)))
                                    + env.getVoltTol()))) {
                                if ((MathLib.abs(delvds) < (env.getRelTol() * MathLib.max(MathLib.abs(vds),
                                        MathLib.abs(vdsStates.get(0)))
                                        + env.getVoltTol()))) {
                                    if ((MathLib.abs(cdhat - cd)
                                            < env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol())) {
                                        vbs = vbsStates.get(0);
                                        vbd = vbdStates.get(0);
                                        vgs = vgsStates.get(0);
                                        vds = vdsStates.get(0);
                                        vgd = vgs - vds;
                                        vgb = vgs - vbs;
                                        idrain = _mode * (cd + cbd);
                                        if (mode.contains(MODE.TRAN, MODE.TRANOP)) {
                                            capgs.set((capgsStates.get(0) + capgsStates.get(1) + gateSrcOverlapCap));
                                            capgd.set((capgdStates.get(0) + capgdStates.get(1) + gateDrnOverlapCap));
                                            capgb.set((capgbStates.get(0) + capgbStates.get(1) + gateBlkOverlapCap));
                                        }
                                        bypass = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!bypass) {
                _von = type * this.von;
                // Limiting to keep voltages from changing too fast
                if (vdsStates.get(0) >= 0) {
                    vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), _von);
                    vds = vgs - vgd;
                    vds = MOSUtils.vdsLimit(vds, vdsStates.get(0));
                    vgd = vgs - vds;
                } else {
                    vgd = MOSUtils.fetLimit(vgd, vgdo, _von);
                    vds = vgs - vgd;
                    if (!(env.isFixLimit())) {
                        vds = -MOSUtils.vdsLimit(-vds, -(vdsStates.get(0)));
                    }
                    vgs = vgd + vds;
                }
                if (vds >= 0) {
                    vbs = MOSUtils.pnjLimit(vbs, vbsStates.get(0), vt, srcVcrit, check);
                    vbd = vbs - vds;
                } else {
                    vbd = MOSUtils.pnjLimit(vbd, vbdStates.get(0), vt, drnVcrit, check);
                    vbs = vbd + vds;
                }
            }
        } else {
            if (mode.contains(MODE.INIT_JCT) && !off) {
                vds = type * icVDS;
                vgs = type * icVGS;
                vbs = type * icVBS;
                if (vds == 0 && vgs == 0 && vbs == 0
                        && (mode.contains(MODE.TRAN, MODE.DCOP, MODE.DCTRANCURVE)
                        || !mode.isUseIC())) {
                    vbs = -1;
                    vgs = type * tVto;
                    vds = 0;
                }
            } else {
                vbs = vgs = vds = 0;
            }
        }
        if (!bypass) {
            KEY key = KEY.INIT;
            vbd = vbs - vds;
            vgd = vgs - vds;
            vgb = vgs - vbs;
            // Evaluate ideal diode current and correspoinding conductance
            double drnSatCur;
            double srcSatCur;
            if (tSatCurDens == 0 || drnArea == 0 || srcArea == 0) {
                drnSatCur = m * tSatCur;
                srcSatCur = m * tSatCur;
            } else {
                drnSatCur = m * tSatCurDens * drnArea;
                srcSatCur = m * tSatCurDens * srcArea;
            }
            if (vbs <= -3 * vt) {
                gbs = env.getGMin();
                cbs = gbs * vbs - srcSatCur;
            } else {
                double evbs = MathLib.exp(MathLib.min(Double.MAX_EXPONENT, vbs / vt));
                gbs = srcSatCur * evbs / vt + env.getGMin();
                cbs = srcSatCur * (evbs - 1) + env.getGMin() * vbs;
            }
            if (vbd <= -3 * vt) {
                gbd = env.getGMin();
                cbd = gbd * vbd - drnSatCur;
            } else {
                double evbd = MathLib.exp(MathLib.min(Double.MAX_EXPONENT, vbd / vt));
                gbd = drnSatCur * evbd / vt + env.getGMin();
                cbd = drnSatCur * (evbd - 1) + env.getGMin() * vbd;
            }
            if (vds >= 0) {
                // Normal mode
                _mode = 1;
            } else {
                // Inverse mode
                _mode = -1;
            }
            lvbs = _mode == 1 ? vbs : vbd;
            lvds = _mode * vds;
            lvgs = _mode == 1 ? vgs : vgd;
            phiMinVbs = tPhi - lvbs;
            // Evaluate drain current, derivatives and charges associated with the gate, channel and bulk
            if (lvbs <= 0) {
                sarg = MathLib.sqrt(phiMinVbs);
                dsrgdb = -0.5 / sarg;
                d2sdb2 = 0.5 * dsrgdb / phiMinVbs;
            } else {
                sqrtPhi = MathLib.sqrt(tPhi);
                sqrtPhi3 = tPhi * sqrtPhi;
                sarg = sqrtPhi / (1 + 0.5 * lvbs / tPhi);
                tmp = sarg / sqrtPhi3;
                dsrgdb = -0.5 * sarg * tmp;
                d2sdb2 = -dsrgdb * tmp;
            }
            if ((lvbs - lvds) <= 0) {
                bSrg = MathLib.sqrt(phiMinVbs + lvds);
                dbrgdb = -0.5 / bSrg;
                d2bdb2 = 0.5 * dbrgdb / (phiMinVbs + lvds);
            } else {
                sqrtPhi = MathLib.sqrt(tPhi);
                sqrtPhi3 = tPhi * sqrtPhi;
                bSrg = sqrtPhi / (1 + 0.5 * (lvbs - lvds) / tPhi);
                tmp = bSrg / sqrtPhi3;
                dbrgdb = -0.5 * bSrg * tmp;
                d2bdb2 = -dbrgdb * tmp;
            }
            // Calculate threshold voltage (von)
            // Narrow - channel effect
            factor = 0.125 * narrowFactor * 2 * MathLib.PI * EPS_SI
                    / oxideCap * effectiveLength;
            eta = 1 + factor;
            vbin = tVbi * type + factor * phiMinVbs;
            if (gamma > 0 || substrateDoping > 0) {
                double xwd = xd * bSrg;
                double xws = xd * sarg;
                // Short - channel effect with vds
                double argss = 0;
                double argsd = 0;
                double dbargs = 0;
                double dbargd = 0;
                dgdvds = 0;
                dgddb2 = 0;
                double argd = 0;
                double args = 0;
                double argxs = 0;
                double argxd = 0;
                if (junctionDepth > 0) {
                    tmp = 2 / junctionDepth;
                    argxs = 1 + xws * tmp;
                    argxd = 1 + xwd * tmp;
                    args = MathLib.sqrt(argxs);
                    argd = MathLib.sqrt(argxd);
                    tmp = 0.5 * junctionDepth / effectiveLength;
                    argss = tmp * (args - 1);
                    argsd = tmp * (argd - 1);
                }
                gamasd = gamma * (1 - argss - argsd);
                double dbxwd = xd * dbrgdb;
                double dbxws = xd * dsrgdb;
                if (junctionDepth > 0) {
                    tmp = 0.5 / effectiveLength;
                    dbargs = tmp * dbxws / args;
                    dbargd = tmp * dbxwd / argd;
                    double dasdb2 = -xd * (d2sdb2 + dsrgdb * dsrgdb
                            * xd / (junctionDepth * argxs))
                            / (effectiveLength * args);
                    double daddb2 = -xd * (d2bdb2 + dbrgdb * dbrgdb
                            * xd / (junctionDepth * argxd))
                            / (effectiveLength * argd);
                    dgddb2 = -0.5 * gamma * (dasdb2 + daddb2);
                }
                dgddvb = -gamma * (dbargs + dbargd);
                if (junctionDepth > 0) {
                    ddxwd = -dbxwd;
                    dgdvds = -gamma * 0.5 * ddxwd / (effectiveLength * argd);
                }
            } else {
                gamasd = gamma;
                gammad = gamma;
                dgddvb = 0;
                dgdvds = 0;
                dgddb2 = 0;
            }
            _von = vbin + gamasd * sarg;
            vth = _von;
            _vdsat = 0;
            vgst = lvgs - _von;
            //        case CASE7://line1050:
            idrain = 0;
            gm = 0;
            gmbs = 0;
            gds = 0;
            if (fastSurfaceStateDensity != 0 && oxideCap != 0) {
                cfs = CHARGE * fastSurfaceStateDensity * 1e4;
                cdonco = -(gamasd * dsrgdb + dgddvb * sarg) + factor;
                xn = 1 + cfs / oxideCap * m
                        * w * effectiveLength + cdonco;
                tmp = vt * xn;
                _von = _von + tmp;
                argg = 1 / tmp;
            } else if (lvgs <= vbin) {
                // Cutoff region
                key = KEY.CASE7;//line1050;
            }
//                    case CASE2://line410:
            ufact = 1;
            ueff = surfaceMobility * 1e-4;
            dudvgs = 0;
            dudvds = 0;
            dudvbs = 0;
            if (key == KEY.INIT) {
                // Compute some more useful quantities
                sarg3 = sarg * sarg * sarg;
                sbiarg = MathLib.sqrt(tBlkPot);
                gammad = gamasd;
                dgdvbs = dgddvb;
                body = bSrg * bSrg * bSrg - sarg3;
                gdbdv = 2 * gammad * (bSrg * bSrg * dbrgdb - sarg * sarg * dsrgdb);
                dodvbs = -factor + dgdvbs * sarg + gammad * dsrgdb;
//                key = KEY.CASE1;//line400;
                if (fastSurfaceStateDensity != 0 && oxideCap != 0) {
                    dxndvb = 2 * dgdvbs * dsrgdb + gammad * d2sdb2 + dgddb2 * sarg;
                    dodvbs = dodvbs + vt * dxndvb;
                    dxndvd = dgdvds * dsrgdb;
                    dodvds = dgdvds * sarg + vt * dxndvd;
//                    key = KEY.CASE1;
                }
                if (fastSurfaceStateDensity != 0 && oxideCap == 0) {
                    //do nothing.
                } else {
                    // Evaluate effective mobility and its derivatives
                    //case CASE1://line400:
                    if (oxideCap > 0) {
                        double udenom = vgst;
                        tmp = critField * 100 * EPS_SI / oxideCapFactor;
                        if (udenom > tmp) {
                            ufact = MathLib.exp(critFieldExp * MathLib.log(tmp / udenom));
                            ueff = surfaceMobility * 1e-4 * ufact;
                            dudvgs = -ufact * critFieldExp / udenom;
                            dudvds = 0;
                            dudvbs = critFieldExp * ufact * dodvbs / vgst;
                        }
                    }
                }
            }/*
             *     Evaluate saturation voltage and derivatives according to
             *     grove - frohman equation
             */
            //case CASE3://line500:
            double vgsx = lvgs;
            gammad = gamasd / eta;
            dgdvbs = dgddvb;
            if (fastSurfaceStateDensity != 0 && oxideCap != 0) {
                vgsx = MathLib.max(lvgs, _von);
            }
            if (gammad > 0) {
                double gammd2 = gammad * gammad;
                argv = (vgsx - vbin) / eta + phiMinVbs;
                if (argv <= 0) {
                    _vdsat = 0;
                    dsdvgs = 0;
                    dsdvbs = 0;
                } else {
                    arg = MathLib.sqrt(1 + 4 * argv / gammd2);
                    _vdsat = (vgsx - vbin) / eta + gammd2 * (1 - arg) / 2;
                    _vdsat = MathLib.max(_vdsat, 0);
                    dsdvgs = (1 - 1 / arg) / eta;
                    dsdvbs = (gammad * (1 - arg) + 2 * argv / (gammad * arg))
                            / eta * dgdvbs + 1 / arg + factor * dsdvgs;
                }
            } else {
                _vdsat = (vgsx - vbin) / eta;
                _vdsat = MathLib.max(_vdsat, 0);
                dsdvgs = 1;
                dsdvbs = 0;
            }
            if (maxDriftVel > 0) {
                double xv = maxDriftVel * effectiveLength / ueff;
                _vdsat = evalVdsat(vgsx, vbin, eta, phiMinVbs, xv, gammad, sarg3, _vdsat);
            }
            // Evaluate effective channel length and its derivatives
            dldvgs = 0;
            dldvds = 0;
            dldvbs = 0;
            if (lvds != 0) {
                gammad = gamasd;
                if ((lvbs - _vdsat) <= 0) {
                    bsarg = MathLib.sqrt(_vdsat + phiMinVbs);
                    dbsrdb = -0.5 / bsarg;
                } else {
                    sqrtPhi = MathLib.sqrt(tPhi);
                    sqrtPhi3 = tPhi * sqrtPhi;
                    bsarg = sqrtPhi / (1 + 0.5 * (lvbs - _vdsat) / tPhi);
                    dbsrdb = -0.5 * bsarg * bsarg / sqrtPhi3;
                }
                bodys = bsarg * bsarg * bsarg - sarg3;
                gdbdvs = 2 * gammad * (bsarg * bsarg * dbsrdb - sarg * sarg * dsrgdb);
                if (maxDriftVel <= 0) {
                    if (substrateDoping != 0 && xlamda <= 0) {
                        argv = (lvds - _vdsat) / 4;
                        sargv = MathLib.sqrt(1 + argv * argv);
                        arg = MathLib.sqrt(argv + sargv);
                        xlfact = xd / (effectiveLength * lvds);
                        xlamda = xlfact * arg;
                        dldsat = lvds * xlamda / (8 * sargv);
                        dldvgs = dldsat * dsdvgs;
                        dldvds = -xlamda + dldsat;
                        dldvbs = dldsat * dsdvbs;
                    }
                } else {
                    argv = (vgsx - vbin) / eta - _vdsat;
                    double xdv = xd / MathLib.sqrt(channelCharge);
                    double xlv = maxDriftVel * xdv / (2 * ueff);
                    double vqchan = argv - gammad * bsarg;
                    double dqdsat = -1 + gammad * dbsrdb;
                    double vl = maxDriftVel * effectiveLength;
                    double dfunds = vl * dqdsat - ueff * vqchan;
                    double dfundg = (vl - ueff * _vdsat) / eta;
                    double dfundb = -vl * (1 + dqdsat - factor / eta) + ueff
                            * (gdbdvs - dgdvbs * bodys / 1.5) / eta;
                    dsdvgs = -dfundg / dfunds;
                    dsdvbs = -dfundb / dfunds;
                    if (substrateDoping != 0 && xlamda <= 0) {
                        argv = lvds - _vdsat;
                        argv = MathLib.max(argv, 0);
                        double xls = MathLib.sqrt(xlv * xlv + argv);
                        dldsat = xdv / (2 * xls);
                        xlfact = xdv / (effectiveLength * lvds);
                        xlamda = xlfact * (xls - xlv);
                        dldsat = dldsat / effectiveLength;
                        dldvgs = dldsat * dsdvgs;
                        dldvds = -xlamda + dldsat;
                        dldvbs = dldsat * dsdvbs;
                    }
                }
            }
//                    case CASE5:
            // Limit channel shortening at punch - through
            double xwb = xd * sbiarg;
            double xld = effectiveLength - xwb;
            clfact = 1 - xlamda * lvds;
            dldvds = -xlamda - dldvds;
            double xleff = effectiveLength * clfact;
            double deltal = xlamda * lvds * effectiveLength;
            if (substrateDoping == 0) {
                xwb = 0.25e-6;
            }
            if (xleff < xwb) {
                xleff = xwb / (1 + (deltal - xld) / xwb);
                clfact = xleff / effectiveLength;
                double dfact = xleff * xleff / (xwb * xwb);
                dldvgs = dfact * dldvgs;
                dldvds = dfact * dldvds;
                dldvbs = dfact * dldvbs;
            }
            // Evaluate effective beta (effective kp)
            beta1 = beta * ufact / clfact;
            // Test for mode of operation and branch appropriately
            gammad = gamasd;
            dgdvbs = dgddvb;
            key = KEY.DONE;
            if (lvds <= 1e-10) {
                if (lvgs <= _von) {
                    if (fastSurfaceStateDensity == 0 || oxideCap == 0) {
                        gds = 0;
                    } else {
                        gds = beta1 * (_von - vbin - gammad * sarg) * MathLib.exp(argg
                                * (lvgs - _von));
                    }
                } else {
                    gds = beta1 * (lvgs - vbin - gammad * sarg);
                }
            } else {
                if (fastSurfaceStateDensity != 0 && oxideCap != 0) {
                    if (lvgs > _von) {
                        key = KEY.CASE6;//line900;
                    } else if (lvgs > vbin) {
                        key = KEY.CASE6;//line900;
                    }
                } else if (lvgs > _von) {
                    key = KEY.CASE6;//line900;
                } else if (_vdsat <= 0) {// Subthreshold region
                    gds = 0;
                } else {
                    double vdson = MathLib.min(_vdsat, lvds);
                    if (lvds > _vdsat) {
                        bSrg = bsarg;
                        dbrgdb = dbsrdb;
                        body = bodys;
                        gdbdv = gdbdvs;
                    }
                    double cdson = beta1 * ((_von - vbin - eta * vdson * 0.5) * vdson - gammad * body / 1.5);
                    double didvds = beta1 * (_von - vbin - eta * vdson - gammad * bSrg);
                    double gdson = -cdson * dldvds / clfact - beta1 * dgdvds * body / 1.5;
                    if (lvds < _vdsat) {
                        gdson = gdson + didvds;
                    }
                    double gbson = -cdson * dldvbs / clfact + beta1
                            * (dodvbs * vdson + factor * vdson - dgdvbs * body / 1.5 - gdbdv);
                    if (lvds > _vdsat) {
                        gbson = gbson + didvds * dsdvbs;
                    }
                    double expg = MathLib.exp(argg * (lvgs - _von));
                    idrain = cdson * expg;
                    gm = idrain * argg;
                    if (lvds > _vdsat) {
                        gm = gm + didvds * dsdvgs * expg;
                    }
                    tmp = gm * (lvgs - _von) / xn;
                    gds = gdson * expg - gm * dodvds - tmp * dxndvd;
                    gmbs = gbson * expg - gm * dodvbs - tmp * dxndvb;
                    key = KEY.DONE;//doneval;
                }
            }
            if (key == KEY.CASE6) {
//                    case CASE6://line900:
                if (lvds <= _vdsat) {
                    // Linear region
                    idrain = beta1 * ((lvgs - vbin - eta * lvds / 2) * lvds - gammad * body / 1.5);
                    arg = idrain * (dudvgs / ufact - dldvgs / clfact);
                    gm = arg + beta1 * lvds;
                    arg = idrain * (dudvds / ufact - dldvds / clfact);
                    gds = arg + beta1 * (lvgs - vbin - eta
                            * lvds - gammad * bSrg - dgdvds * body / 1.5);
                    arg = idrain * (dudvbs / ufact - dldvbs / clfact);
                    gmbs = arg - beta1 * (gdbdv + dgdvbs * body / 1.5 - factor * lvds);
                } else {
                    // Saturation region
                    idrain = beta1 * ((lvgs - vbin - eta
                            * _vdsat / 2) * _vdsat - gammad * bodys / 1.5);
                    arg = idrain * (dudvgs / ufact - dldvgs / clfact);
                    gm = arg + beta1 * _vdsat + beta1 * (lvgs
                            - vbin - eta * _vdsat - gammad * bsarg) * dsdvgs;
                    gds = -idrain * dldvds / clfact - beta1 * dgdvds * bodys / 1.5;
                    arg = idrain * (dudvbs / ufact - dldvbs / clfact);
                    gmbs = arg - beta1 * (gdbdvs + dgdvbs * bodys / 1.5 - factor
                            * _vdsat) + beta1 * (lvgs - vbin - eta * _vdsat - gammad * bsarg) * dsdvbs;
                }
            }
            // Finish special cases
            if (key == KEY.CASE7) {
            }
            this.von = type * _von;
            this.vdsat = type * _vdsat;
            // Compute drain current source
            cd = _mode * idrain - cbd;
            if (mode.contains(MODE.TRAN, MODE.TRANOP, MODE.INIT_SMSIG)) {
                // Charge storage elements
                if (vbs < tDepCap) {
                    arg = 1 - vbs / tBlkPot;
                    // Use default 0.5 coeff. for max efficiency
                    if (blkJctBotGradingCoeff == blkJctSideGradingCoeff) {
                        if (blkJctBotGradingCoeff == 0.5) {
                            sarg = sargsw = 1 / MathLib.sqrt(arg);
                        } else {
                            sarg = sargsw = MathLib.exp(-blkJctBotGradingCoeff * MathLib.log(arg));
                        }
                    } else {
                        if (blkJctBotGradingCoeff == 0.5) {
                            sarg = 1 / MathLib.sqrt(arg);
                        } else {
                            sarg = MathLib.exp(-blkJctBotGradingCoeff
                                    * MathLib.log(arg));
                        }
                        if (blkJctSideGradingCoeff == 0.5) {
                            sargsw = 1 / MathLib.sqrt(arg);
                        } else {
                            sargsw = MathLib.exp(-blkJctSideGradingCoeff
                                    * MathLib.log(arg));
                        }
                    }
                    qbsStates.set(0, tBlkPot * (czbs
                            * (1 - arg * sarg) / (1 - blkJctBotGradingCoeff) + czbssw
                            * (1 - arg * sargsw)
                            / (1 - blkJctSideGradingCoeff)));
                    capbs = czbs * sarg + czbssw * sargsw;
                } else {
                    qbsStates.set(0, f4s + vbs * (f2s + vbs * (f3s / 2)));
                    capbs = f2s + f3s * vbs;
                }
                if (vbd < tDepCap) {
                    arg = 1 - vbd / tBlkPot;
                    if (blkJctBotGradingCoeff == 0.5 && blkJctSideGradingCoeff == 0.5) {
                        sarg = sargsw = 1 / MathLib.sqrt(arg);
                    } else {
                        if (blkJctBotGradingCoeff == 0.5) {
                            sarg = 1 / MathLib.sqrt(arg);
                        } else {
                            sarg = MathLib.exp(-blkJctBotGradingCoeff
                                    * MathLib.log(arg));
                        }
                        if (blkJctSideGradingCoeff == 0.5) {
                            sargsw = 1 / MathLib.sqrt(arg);
                        } else {
                            sargsw = MathLib.exp(-blkJctSideGradingCoeff
                                    * MathLib.log(arg));
                        }
                    }
                    qbdStates.set(0, tBlkPot * (czbd
                            * (1 - arg * sarg) / (1 - blkJctBotGradingCoeff) + czbdsw
                            * (1 - arg * sargsw) / (1 - blkJctSideGradingCoeff)));
                    capbd = czbd * sarg + czbdsw * sargsw;
                } else {
                    qbdStates.set(0, f4d + vbd * (f2d + vbd * f3d / 2));
                    capbd = f2d + vbd * f3d;
                }
                if (mode.contains(MODE.TRAN)) {
                    if (!stateTable.integrate(geq, ceq, capbd, qbdStates, cqbdStates)) {
                        return false;
                    }
                    gbd += geq.get();
                    cbd += cqbdStates.get(0);
                    cd -= cqbdStates.get(0);
                    if (!stateTable.integrate(geq, ceq, capbs, qbsStates, cqbsStates)) {
                        return false;
                    }
                    gbs += geq.get();
                    cbs += cqbsStates.get(0);
                }
            }
            if ((!off || !mode.contains(MODE.INIT_FIX, MODE.INIT_SMSIG)) && check.isTrue()) {
                ckt.setNonConverged(true);
            }
            vbsStates.set(0, vbs);
            vbdStates.set(0, vbd);
            vgsStates.set(0, vgs);
            vdsStates.set(0, vds);
            // Meyer's capacitor model
            if (mode.contains(MODE.TRAN, MODE.TRANOP, MODE.INIT_SMSIG)) {
                if (_mode > 0) {
                    MOSUtils.qMeyer(vgs, vgd, vgb, _von, _vdsat,
                            capgs, capgd, capgb, tPhi, oxideCap);
                } else {
                    MOSUtils.qMeyer(vgd, vgs, vgb, _von, _vdsat,
                            capgd, capgs, capgb, tPhi, oxideCap);
                }
                capgsStates.set(0, capgs.get());
                capgdStates.set(0, capgd.get());
                capgbStates.set(0, capgb.get());
                double vgs1 = vgsStates.get(1);
                double vgd1 = vgs1 - vdsStates.get(1);
                double vgb1 = vgs1 - vbsStates.get(1);
                if (mode.contains(MODE.TRANOP)) {
                    capgs.set(2 * capgs.get() + gateSrcOverlapCap);
                    capgd.set(2 * capgd.get() + gateDrnOverlapCap);
                    capgb.set(2 * capgb.get() + gateBlkOverlapCap);
                } else {
                    capgs.set(capgs.get() + capgsStates.get(1) + gateSrcOverlapCap);
                    capgd.set(capgd.get() + capgdStates.get(1) + gateDrnOverlapCap);
                    capgb.set(capgb.get() + capgbStates.get(1) + gateBlkOverlapCap);
                }
                if (mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN)) {
                    qgsStates.set(0, (1 + xfact) * qgsStates.get(1) - xfact * qgsStates.get(2));
                    qgdStates.set(0, (1 + xfact) * qgdStates.get(1) - xfact * qgdStates.get(2));
                    qgbStates.set(0, (1 + xfact) * qgbStates.get(1) - xfact * qgbStates.get(2));
                } else {
                    if (mode.contains(MODE.TRAN)) {
                        qgsStates.set(0, (vgs - vgs1) * capgs.get() + qgsStates.get(1));
                        qgdStates.set(0, (vgd - vgd1) * capgd.get() + qgdStates.get(1));
                        qgbStates.set(0, (vgb - vgb1) * capgb.get() + qgbStates.get(1));
                    } else {
                        qgsStates.set(0, capgs.get() * vgs);
                        qgdStates.set(0, capgd.get() * vgd);
                        qgbStates.set(0, capgb.get() * vgb);
                    }
                }
            }
        }
        if (mode.contains(MODE.INIT_TRAN) || !mode.contains(MODE.TRANOP)) {
            gcgs.setZero();
            ceqgs.setZero();
            gcgd.setZero();
            ceqgd.setZero();
            gcgb.setZero();
            ceqgb.setZero();
        } else {
            if (capgs.isZero()) {
                cqgsStates.set(0, 0);
            }
            if (capgd.isZero()) {
                cqgdStates.set(0, 0);
            }
            if (capgb.isZero()) {
                cqgbStates.set(0, 0);
            } // Calculate equivalent conductances and currents for meyer's capacitors
            if (!stateTable.integrate(gcgs, ceqgs, capgs.get(), qgsStates, cqgsStates)) {
                return false;
            }
            if (!stateTable.integrate(gcgd, ceqgd, capgd.get(), qgdStates, cqgdStates)) {
                return false;
            }
            if (!stateTable.integrate(gcgb, ceqgb, capgb.get(), qgbStates, cqgbStates)) {
                return false;
            }
            ceqgs.set(ceqgs.get() - gcgs.get() * vgs + tmprl.getAgAt(0)
                    * qgsStates.get(0));
            ceqgd.set(ceqgd.get() - gcgd.get() * vgd + tmprl.getAgAt(0)
                    * qgdStates.get(0));
            ceqgb.set(ceqgb.get() - gcgb.get() * vgb + tmprl.getAgAt(0)
                    * qgbStates.get(0));
        }
// Load current vector
        double ceqbs = type * (cbs - (gbs) * vbs);
        double ceqbd = type * (cbd - (gbd) * vbd);
        int xnrm;
        int xrev;
        double cdreq;
        if (_mode >= 0) {
            xnrm = 1;
            xrev = 0;
            cdreq = type * (idrain - gds * vds - gm * vgs - gmbs * vbs);
        } else {
            xnrm = 0;
            xrev = 1;
            cdreq = -(type) * (idrain - gds * (-vds) - gm * vgd - gmbs * vbd);
        }
        wrk.getRhsAt(gateIndex).realMinusEq(type * (ceqgs.get() + ceqgb.get() + ceqgd.get()));
        wrk.getRhsAt(blkIndex).realMinusEq(ceqbs + ceqbd - type * ceqgb.get());
        wrk.getRhsAt(drnPrmIndex).realPlusEq(ceqbd - cdreq + type * ceqgd.get());
        wrk.getRhsAt(srcPrmIndex).realPlusEq(cdreq + ceqbs + type * ceqgs.get());
        drnDrnNode.realPlusEq(drnConductance);
        gateGateNode.realPlusEq(gcgd.get() + gcgs.get() + gcgb.get());
        srcSrcNode.realPlusEq(srcConductance);
        blkBlkNode.realPlusEq(gbd + gbs + gcgb.get());
        drnPrmDrnPrmNode.realPlusEq(drnConductance + gds
                + gbd + xrev * (gm + gmbs) + gcgd.get());
        srcPrmSrcPrmNode.realPlusEq(srcConductance + gds
                + gbs + xnrm * (gm + gmbs) + gcgs.get());
        drnDrnPrmNode.realMinusEq(drnConductance);
        gateBlkNode.realMinusEq(gcgb);
        gateDrnPrmNode.realMinusEq(gcgd);
        gateSrcPrmNode.realMinusEq(gcgs);
        srcSrcPrmNode.realMinusEq(srcConductance);
        blkGateNode.realMinusEq(gcgb);
        blkDrnPrmNode.realMinusEq(gbd);
        blkSrcPrmNode.realMinusEq(gbs);
        drnPrmDrnNode.realMinusEq(drnConductance);
        drnPrmGateNode.realPlusEq((xnrm - xrev) * gm - gcgd.get());
        drnPrmBlkNode.realPlusEq((-gbd + (xnrm - xrev) * gmbs));
        drnPrmSrcPrmNode.realMinusEq(gds + xnrm * (gm + gmbs));
        srcPrmGateNode.realMinusEq((xnrm - xrev) * gm + gcgs.get());
        srcPrmSrcNode.realMinusEq(srcConductance);
        srcPrmBlkNode.realMinusEq(gbs + (xnrm - xrev) * gmbs);
        srcPrmDrnPrmNode.realMinusEq(gds + xrev * (gm + gmbs));
/*        System.out.printf(" loading %s at time %g\n", instName, tmprl.getTime());
        System.out.printf("%g %g %g %g %g\n", drnConductance,
                gcgd.get() + gcgs.get() + gcgb.get(), srcConductance,
                gbd, gbs);
        System.out.printf("%g %g %g %g %g\n", -gcgb.get(), 0, 0,
                gds, gm);
        System.out.printf("%g %g %g %g %g\n", gds, gmbs,
                gcgd.get(), -gcgs.get(), -gcgd.get());
        System.out.printf("%g %g %g %g %g\n", -gcgs.get(), -gcgd.get(), 0, -gcgs.get(), 0);
//
        /*
        System.out.println("gateIndex = " + (type * (ceqgs.get() + ceqgb.get() + ceqgd.get())));;
        System.out.println("blkIndex = " + (ceqbs + ceqbd - type * ceqgb.get()));;
        System.out.println("drnPrmIndex = " + (ceqbd - cdreq + type * ceqgd.get()));;
        System.out.println("srcPrmIndex = " + (cdreq + ceqbs + type * ceqgs.get()));;
        System.out.println("drnDrnNode = " + drnConductance);
        System.out.println("gateGateNode = " + (gcgd.get() + gcgs.get() + gcgb.get()));
        System.out.println("srcSrcNode = " + (srcConductance));
        System.out.println("blkBlkNode = " + (gbd + gbs + gcgb.get()));
        System.out.println("drnPrmDrnPrmNode = " + (drnConductance + gds
        + gbd + xrev * (gm + gmbs) + gcgd.get()));
        System.out.println("srcPrmSrcPrmNode = " + (srcConductance + gds
        + gbs + xnrm * (gm + gmbs) + gcgs.get()));
        System.out.println("drnDrnPrmNode = " + (drnConductance));
        System.out.println("gateBlkNode = " + (gcgb));
        System.out.println("gateDrnPrmNode = " + (gcgd));
        System.out.println("gateSrcPrmNode = " + (gcgs));
        System.out.println("srcSrcPrmNode = " + (srcConductance));
        System.out.println("blkGateNode = " + (gcgb));
        System.out.println("blkDrnPrmNode = " + (gbd));
        System.out.println("blkSrcPrmNode = " + (gbs));
        System.out.println("drnPrmDrnNode = " + (drnConductance));
        System.out.println("drnPrmGateNode = " + ((xnrm - xrev) * gm - gcgd.get()));
        System.out.println("drnPrmBlkNode = " + ((-gbd + (xnrm - xrev) * gmbs)));
        System.out.println("drnPrmSrcPrmNode = " + (gds + xnrm * (gm + gmbs)));
        System.out.println("srcPrmGateNode = " + ((xnrm - xrev) * gm + gcgs.get()));
        System.out.println("srcPrmSrcNode = " + (srcConductance));
        System.out.println("srcPrmBlkNode = " + (gbs + (xnrm - xrev) * gmbs));
        System.out.println("srcPrmDrnPrmNode = " + (gds + xrev * (gm + gmbs)));
         */
        return true;
    }

    public boolean temperature() {
        if (tnom == 0) {
            tnom = env.getNomTemp();
        }
        double fact1 = tnom / REF_TEMP;
        double vtnom = tnom * KoverQ;
        double kt1 = BOLTZMANN * tnom;
        double egfet1 = 1.16 - (7.02e-4 * tnom * tnom) / (tnom + 1108);
        double arg1 = -egfet1 / (kt1 + kt1) + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact1 = -2 * vtnom * (1.5 * MathLib.log(fact1) + CHARGE * arg1);
        oxideCapFactor = EPS_SiO2 / oxideThickness;
        if (transconductance == 0) {
            transconductance = surfaceMobility * 1e-4 * oxideCapFactor;
        }
        if (substrateDoping != 0) {
            if (substrateDoping * 1e6 > 1.45e16) {
                if (phi == 0) {
                    phi = 2 * vtnom * MathLib.log(substrateDoping
                            * 1e6 / 1.45e16);
                    phi = MathLib.max(.1, phi);
                }
                double fermis = type * 0.5 * phi;
                double fermig = type * gateType * 0.5 * egfet1;
                double wkfng = 3.25 + 0.5 * egfet1 - fermig;
                double wkfngs = wkfng - (3.25 + 0.5 * egfet1 + fermis);
                if (gamma == 0) {
                    gamma = MathLib.sqrt(2 * EPS_SI
                            * CHARGE * substrateDoping
                            * 1e6) / oxideCapFactor;
                }
                if (vt0 == 0) {
                    double vfb = wkfngs - surfaceStateDensity
                            * 1e4 * CHARGE / oxideCapFactor;
                    vt0 = vfb + type * (gamma * MathLib.sqrt(phi) + phi);
                }
                xd = MathLib.sqrt((EPS_SI + EPS_SI)
                        / (CHARGE * substrateDoping * 1e6));
            } else {
                substrateDoping = 0;
                StandardLog.severe(getModelName() + ": Nsub < Ni");
                return false;
            }
        }
        if (blkCapFactor == 0) {
            blkCapFactor = MathLib.sqrt(EPS_SI * CHARGE
                    * substrateDoping * 1e6 / (2 * blkJctPotential));
        }
        if (temp == 0) {
            temp = env.getTemp() + dtemp;
        }
        double vt = temp * KoverQ;
        double ratio = temp / tnom;
        double fact2 = temp / REF_TEMP;
        double kt = temp * BOLTZMANN;
        double egfet = 1.16 - (7.02e-4 * temp * temp) / (temp + 1108);
        double arg = -egfet / (kt + kt) + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact = -2 * vt * (1.5 * MathLib.log(fact2) + CHARGE * arg);
        double ratio4 = ratio * MathLib.sqrt(ratio);
        tTransconductance = transconductance / ratio4;
        tSurfMob = surfaceMobility / ratio4;
        double phio = (phi - pbfact1) / fact1;
        tPhi = fact2 * phio + pbfact;
        tVbi = vt0 - type * (gamma * MathLib.sqrt(phi)) + 0.5 * (egfet1 - egfet) + type * 0.5 * (tPhi - phi);
        tVto = tVbi + type * gamma * MathLib.sqrt(tPhi);
        tSatCur = jctSatCur * MathLib.exp(-egfet / vt + egfet1 / vtnom);
        tSatCurDens = jctSatCurDensity * MathLib.exp(-egfet / vt + egfet1 / vtnom);
        double pbo = (blkJctPotential - pbfact1) / fact1;
        double gmaold = (blkJctPotential - pbo) / pbo;
        double capfact = 1 / (1 + blkJctBotGradingCoeff
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tCbd = capBD * capfact;
        tCbs = capBS * capfact;
        tCj = blkCapFactor * capfact;
        capfact = 1 / (1 + blkJctSideGradingCoeff
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tCjsw = sideWallCapFactor * capfact;
        tBlkPot = fact2 * pbo + pbfact;
        double gmanew = (tBlkPot - pbo) / pbo;
        capfact = (1 + blkJctBotGradingCoeff * (4e-4 * (temp - REF_TEMP) - gmanew));
        tCbd *= capfact;
        tCbs *= capfact;
        tCj *= capfact;
        capfact = (1 + blkJctSideGradingCoeff
                * (4e-4 * (temp - REF_TEMP) - gmanew));
        tCjsw *= capfact;
        tDepCap = fwdCapDepCoeff * tBlkPot;
        if ((tSatCurDens == 0) || (drnArea == 0) || (srcArea == 0)) {
            srcVcrit = drnVcrit =
                    vt * MathLib.log(vt / (ROOT2 * m * tSatCur));
        } else {
            drnVcrit = vt * MathLib.log(vt / (ROOT2 * m * tSatCurDens * drnArea));
            srcVcrit = vt * MathLib.log(vt / (ROOT2 * m * tSatCurDens * srcArea));
        }
        if (capBD != 0) {
            czbd = tCbd * m;
        } else {
            if (blkCapFactor != 0) {
                czbd = tCj * drnArea * m;
            } else {
                czbd = 0;
            }
        }
        if (sideWallCapFactor != 0) {
            czbdsw = tCjsw * drnPerimeter * m;
        } else {
            czbdsw = 0;
        }
        arg = 1 - fwdCapDepCoeff;
        double sarg = MathLib.exp((-blkJctBotGradingCoeff) * MathLib.log(arg));
        double sargsw = MathLib.exp((-blkJctSideGradingCoeff) * MathLib.log(arg));
        f2d = czbd * (1 - fwdCapDepCoeff
                * (1 + blkJctBotGradingCoeff)) * sarg / arg + czbdsw * (1 - fwdCapDepCoeff
                * (1 + blkJctSideGradingCoeff)) * sargsw / arg;
        f3d = czbd * blkJctBotGradingCoeff * sarg / arg
                / tBlkPot + czbdsw * blkJctSideGradingCoeff * sargsw / arg
                / tBlkPot;
        f4d = czbd * tBlkPot * (1 - arg * sarg)
                / (1 - blkJctBotGradingCoeff) + czbdsw * tBlkPot * (1 - arg * sargsw)
                / (1 - blkJctSideGradingCoeff) - f3d / 2
                * (tDepCap * tDepCap) - tDepCap * f2d;
        if (capBS != 0) {
            czbs = tCbs * m;
        } else {
            if (blkCapFactor != 0) {
                czbs = tCj * srcArea * m;
            } else {
                czbs = 0;
            }
        }
        if (sideWallCapFactor != 0) {
            czbssw = tCjsw * srcPerimeter * m;
        } else {
            czbssw = 0;
        }
        arg = 1 - fwdCapDepCoeff;
        sarg = MathLib.exp((-blkJctBotGradingCoeff) * MathLib.log(arg));
        sargsw = MathLib.exp((-blkJctSideGradingCoeff) * MathLib.log(arg));
        f2s = czbs * (1 - fwdCapDepCoeff
                * (1 + blkJctBotGradingCoeff)) * sarg / arg + czbssw * (1 - fwdCapDepCoeff
                * (1 + blkJctSideGradingCoeff))
                * sargsw / arg;
        f3s = czbs * blkJctBotGradingCoeff * sarg / arg
                / tBlkPot + czbssw * blkJctSideGradingCoeff * sargsw / arg
                / tBlkPot;
        f4s = czbs * tBlkPot * (1 - arg * sarg)
                / (1 - blkJctBotGradingCoeff) + czbssw * tBlkPot * (1 - arg * sargsw)
                / (1 - blkJctSideGradingCoeff) - f3s / 2
                * (tDepCap * tDepCap) - tDepCap * f2s;
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        timeStep = stateTable.terr(qgsStates, cqgsStates, timeStep);
        timeStep = stateTable.terr(qgdStates, cqgdStates, timeStep);
        return stateTable.terr(qgbStates, cqgbStates, timeStep);
    }

    public boolean unSetup() {
        if (srcPrmIndex != 0 && srcPrmIndex != srcIndex) {
            ckt.deleteNode(srcPrmIndex);
            srcPrmIndex = 0;
        }
        if (drnPrmIndex != 0 && drnPrmIndex != drnIndex) {
            ckt.deleteNode(drnPrmIndex);
            drnPrmIndex = 0;
        }
        return true;
    }

    public boolean accept(Mode mode) {
        return true;

    }
}
