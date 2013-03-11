/*
 * mos3.java
 *
 * Created on August 28, 2006, 2:35 PM
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
public class MOS3Instance extends MOS3ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;

    /* Conductance of source */
    private double srcConductance;

    /* Conductance of drain */
    private double drnConductance;

    /* Vcrit for positive vds */
    private double srcVcrit;

    /* Vcrit for negative vds */
    private double drnVcrit;
    private double narrowFactor;
    private double alpha;
    private double oxideCapFactor;
    private double coeffDepLayWidth;
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
    /* Device mode : 1 = normal, -1 = inverse */
    private int _mode = 1;
    private double cgs;
    private double cgd;
    private double cgb;
    /* Temperature difference */
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

    /* Temperature corrected B - D capacitance */
    private double tCbd;

    /* Temperature corrected B - S capacitance */
    private double tCbs;

    /* Temperature corrected bulk bottom capacitance */
    private double tCj;

    /* Temperature corrected bulk side capacitance */
    private double tCjsw;

    /* Temperature corrected bulk potential */
    private double tBlkPot;

    /* Temperature corrected transition point in the curve matching Fc * Vj */
    private double tDepCap;

    /* Temperature corrected Vbi */
    private double tVbi;
    private double von;
    private double vdsat;
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
    private StateVector vbdStates;
    private StateVector vbsStates;
    private StateVector vgsStates;
    private StateVector vdsStates;
    /* meyer capacitances */
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
    /* Diode capacitances */

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
    private Real ieq = Real.zero();
    private Bool check = Bool.TRUE();

    /** Creates a new instance of mos3 */
    public MOS3Instance() {
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
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "internal #drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if ((srcResistance != 0 || sheetResistance != 0) && srcPrmIndex == 0) {
            srcPrmIndex = ckt.makeVoltNode(getInstName() + "internal #source").getIndex();
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
        if (l - 2 * latDiff + lengthAdjust <= 0) {
            StandardLog.severe(getInstName() + ": effective channel length less than zero");
            return false;
        }
        if (w - 2 * widthNarrow + widthAdjust <= 0) {
            StandardLog.severe(getInstName() + ": effective channel width less than zero");
            return false;
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
// Charge parameters
        double effectiveWidth = w - 2 * widthNarrow + widthAdjust;
        double effectiveLength = l - 2 * latDiff + lengthAdjust;
        double gateSrcOverlapCap = gateSrcOverlapCapFactor * m * effectiveWidth;
        double gateDrnOverlapCap = gateDrnOverlapCapFactor * m * effectiveWidth;
        double gateBlkOverlapCap = gateBlkOverlapCapFactor * m * effectiveLength;
// Meyer's parameters
        capgs.set(2 * capgsStates.get(0) + gateSrcOverlapCap);
        capgd.set(2 * capgdStates.get(0) + gateDrnOverlapCap);
        capgb.set(2 * capgbStates.get(0) + gateBlkOverlapCap);
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
        // Check convergence
        double tol = env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd))
                + env.getAbsTol();
        if (MathLib.abs(cdhat - cd) >= tol) {
            return false;
        } else {
            tol = env.getRelTol()
                    * MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbs + cbd)) + env.getAbsTol();
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

    private static enum KEY {

        CASE1, CASE2, CASE3, DONE
    };

    public boolean load(Mode mode) {
        double cdhat;
        double idrain = 0;
        double cdreq;
        double vbd = 0;
        double vbs = 0;
        double vds = 0;
        double _vdsat = 0;
        double vgb = 0;
        double vgd = 0;
        double vgdo;
        double vgs = 0;
        double _von = 0;
        double xfact = 0;
//        double fbody = 0;
//        double onfbdy = 0;
        double vt = KoverQ * temp;
        double effectiveWidth = w - 2 * widthNarrow + widthAdjust;
        double effectiveLength = l - 2 * latDiff + lengthAdjust;
        double gateSrcOverlapCap = gateSrcOverlapCapFactor * m * effectiveWidth;
        double gateDrnOverlapCap = gateDrnOverlapCapFactor * m * effectiveWidth;
        double gateBlkOverlapCap = gateBlkOverlapCapFactor * m * effectiveLength;
        double beta = tTransconductance * m * effectiveWidth / effectiveLength;
        double oxideCap = oxideCapFactor * effectiveLength * m * effectiveWidth;
        double oneOverXl = 1 / effectiveLength;
        double _eta = this.eta * 8.15e-22 / (oxideCapFactor
                * effectiveLength * effectiveLength * effectiveLength);
        boolean bypass = false;
        if ((mode.contains(MODE.INIT_FLOAT, MODE.INIT_PRED, MODE.INIT_SMSIG, MODE.INIT_TRAN))
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
            vgdo = vgsStates.get(0) - vdsStates.get(0);
            double delvbs = vbs - vbsStates.get(0);
            double delvbd = vbd - vbdStates.get(0);
            double delvgs = vgs - vgsStates.get(0);
            double delvds = vds - vdsStates.get(0);
            double delvgd = vgd - vgdo;
            if (_mode >= 0) {
                cdhat = cd - gbd * delvbd + gmbs * delvbs + gm * delvgs + gds * delvds;
            } else {
                cdhat = cd - (gbd - gmbs) * delvbd - gm * delvgd + gds * delvds;
            }
            double cbhat = cbs + cbd + gbd * delvbd + gbs * delvbs;
            double tempv = MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbs + cbd)) + env.getAbsTol();
            if ((!(mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN, MODE.INIT_SMSIG))) && (env.isBypass())) {
                if ((MathLib.abs(cbhat - (cbs + cbd)) < env.getRelTol() * tempv)) {
                    if ((MathLib.abs(delvbs) < (env.getRelTol() * MathLib.max(MathLib.abs(vbs), MathLib.abs(vbsStates.get(0)))
                            + env.getVoltTol()))) {
                        if ((MathLib.abs(delvbd) < (env.getRelTol() * MathLib.max(MathLib.abs(vbd),
                                MathLib.abs(vbdStates.get(0))) + env.getVoltTol()))) {
                            if ((MathLib.abs(delvgs) < (env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                                    MathLib.abs(vgsStates.get(0))) + env.getVoltTol()))) {
                                if ((MathLib.abs(delvds) < (env.getRelTol() * MathLib.max(MathLib.abs(vds),
                                        MathLib.abs(vdsStates.get(0))) + env.getVoltTol()))) {
                                    if ((MathLib.abs(cdhat - cd)
                                            < env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol())) {
                                        bypass = true;
                                        vbs = vbsStates.get(0);
                                        vbd = vbdStates.get(0);
                                        vgs = vgsStates.get(0);
                                        vds = vdsStates.get(0);
                                        vgd = vgs - vds;
                                        vgb = vgs - vbs;
                                        idrain = _mode * (cd + cbd);
                                        if (mode.contains(MODE.TRAN, MODE.TRANOP)) {
                                            capgs.set(capgsStates.get(0) + capgsStates.get(1) + gateSrcOverlapCap);
                                            capgd.set(capgdStates.get(0) + capgdStates.get(1) + gateDrnOverlapCap);
                                            capgb.set(capgbStates.get(0) + capgbStates.get(1) + gateBlkOverlapCap);
                                        }
                                        //                                key = KEY.DONE;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!bypass) {
                _von = type * this.von;
                // Node Limiting
                if (vdsStates.get(0) >= 0) {
                    vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), _von);
                    vds = vgs - vgd;
                    vds = MOSUtils.vdsLimit(vds, vdsStates.get(0));
                    vgd = vgs - vds;
                } else {
                    vgd = MOSUtils.fetLimit(vgd, vgdo, _von);
                    vds = vgs - vgd;
                    if (!env.isFixLimit()) {
                        vds = -MOSUtils.vdsLimit(-vds, -(vdsStates.get(0)));
                    }
                    vgs = vgd + vds;
                }
                if (vds >= 0) {
                    vbs = MOSUtils.pnjLimit(vbs, vbsStates.get(0),
                            vt, srcVcrit, check);
                    vbd = vbs - vds;
                } else {
                    vbd = MOSUtils.pnjLimit(vbd, vbdStates.get(0),
                            vt, drnVcrit, check);
                    vbs = vbd + vds;
                }
            }
        } else {
            if (mode.contains(MODE.INIT_JCT) && !off) {
                vds = type * icVDS;
                vgs = type * icVGS;
                vbs = type * icVBS;
                if (vds == 0 && vgs == 0 && vbs == 0
                        && mode.contains(MODE.TRAN, MODE.DCOP, MODE.DCTRANCURVE)
                        || !mode.isUseIC()) {
                    vbs = -1;
                    vgs = type * tVto;
                    vds = 0;
                }
            } else {
                vbs = vgs = vds = 0;
            }
        }
        if (!bypass) {
            vbd = vbs - vds;
            vgd = vgs - vds;
            vgb = vgs - vbs;
            double drnSatCur;
            double srcSatCur;
            double arg;
            if (tSatCurDens == 0 || drnArea == 0 || srcArea == 0) {
                drnSatCur = m * tSatCur;
                srcSatCur = m * tSatCur;
            } else {
                drnSatCur = m * tSatCurDens * drnArea;
                srcSatCur = m * tSatCurDens * srcArea;
            }
            if (vbs <= -3 * vt) {
                arg = 3 * vt / (vbs * MathLib.E);
                arg = arg * arg * arg;
                cbs = -srcSatCur * (1 + arg) + env.getGMin() * vbs;
                gbs = srcSatCur * 3 * arg / vbs + env.getGMin();
            } else {
                double evbs = MathLib.exp(MathLib.min(Double.MAX_EXPONENT, vbs / vt));
                gbs = srcSatCur * evbs / vt + env.getGMin();
                cbs = srcSatCur * (evbs - 1) + env.getGMin() * vbs;
            }
            if (vbd <= -3 * vt) {
                arg = 3 * vt / (vbd * MathLib.E);
                arg = arg * arg * arg;
                cbd = -drnSatCur * (1 + arg) + env.getGMin() * vbd;
                gbd = drnSatCur * 3 * arg / vbd + env.getGMin();
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
            double phiVbs = 0;
            double sqrtPhibs = 0;
            double dsqdvb = 0;
            double sqrtPhi3;
            if ((_mode == 1 ? vbs : vbd) <= 0) {
                phiVbs = tPhi - (_mode == 1 ? vbs : vbd);
                sqrtPhibs = MathLib.sqrt(phiVbs);
                dsqdvb = -0.5 / sqrtPhibs;
            } else {
                double sqrtPhi = MathLib.sqrt(tPhi);
                sqrtPhi3 = tPhi * sqrtPhi;
                sqrtPhibs = sqrtPhi / (1 + (_mode == 1 ? vbs : vbd)
                        / (tPhi + tPhi));
                phiVbs = sqrtPhibs * sqrtPhibs;
                dsqdvb = -phiVbs / (sqrtPhi3 + sqrtPhi3);
            }
            double arga;
            double argb;
            double argc;
            double fshort;
            double dfsdvb = 0;
            if (junctionDepth != 0 && coeffDepLayWidth != 0) {
                double wps = coeffDepLayWidth * sqrtPhibs;
                double oneOverXj = 1 / junctionDepth;
                double xjonxl = junctionDepth * oneOverXl;
                double djonxj = latDiff * oneOverXj;
                double wponxj = wps * oneOverXj;
                double coeff0 = 0.0631353e0;
                double coeff1 = 0.8013292e0;
                double coeff2 = -0.01110777e0;
                double wconxj = coeff0 + coeff1 * wponxj + coeff2 * wponxj * wponxj;
                arga = wconxj + djonxj;
                argc = wponxj / (1 + wponxj);
                argb = MathLib.sqrt(1 - argc * argc);
                fshort = 1 - xjonxl * (arga * argb - djonxj);
                double dwpdvb = coeffDepLayWidth * dsqdvb;
                double dadvb = (coeff1 + coeff2 * (wponxj + wponxj)) * dwpdvb * oneOverXj;
                double dbdvb = -argc * argc * (1 - argc) * dwpdvb / (argb * wps);
                dfsdvb = -xjonxl * (dadvb * argb + arga * dbdvb);
            } else {
                fshort = 1;
                dfsdvb = 0;
            }
            double gammas = gamma * fshort;
            double fbodys = 0.5 * gammas / (sqrtPhibs + sqrtPhibs);
            double fbody = fbodys + narrowFactor / effectiveWidth;
            double onfbdy = 1 / (1 + fbody);
            double dfbdvb = -fbodys * dsqdvb / sqrtPhibs + fbodys * dfsdvb / fshort;
            double qbonco = gammas * sqrtPhibs + narrowFactor * phiVbs / effectiveWidth;
            double dqbdvb = gammas * dsqdvb + gamma * dfsdvb * sqrtPhibs
                    - narrowFactor / effectiveWidth;
            double vbix = tVbi * type - _eta * (_mode * vds);
            double vth = vbix + qbonco;
            double dvtdvd = -_eta;
            double dvtdvb = dqbdvb;
            _von = vth;
            double xn = 0;
            double dxndvb = 0;
            double dvodvb = 0;
            double dvodvd = 0;
            if (fastSurfaceStateDensity != 0) {
                double csonco = CHARGE * fastSurfaceStateDensity
                        * 1e4 * effectiveLength * effectiveWidth * m / oxideCap;
                double cdonco = qbonco / (phiVbs + phiVbs);
                xn = 1 + csonco + cdonco;
                _von = vth + vt * xn;
                dxndvb = dqbdvb / (phiVbs + phiVbs) - qbonco * dsqdvb / (phiVbs * sqrtPhibs);
                dvodvd = dvtdvd;
                dvodvb = dvtdvb + vt * dxndvb;
            }
            KEY key = KEY.CASE1;
            double vgsx = 0;
            double onfg;
            double fgate = 0;
            double dfgdvg = 0;
            double dfgdvd = 0;
            double dfgdvb = 0;
            double dvsdvg = 0;
            double dvsdvb = 0;
            double dvsdvd = 0;
            double onvdsc = 0;
            double dvsdga;
            double vdsx;
            double fdrain = 0;
            double dfddvg = 0;
            double dfddvb = 0;
            double dfddvd = 0;
            double delxl = 0;
            double dldvd = 0;
            double dldem;
            double ddldvg = 0;
            double ddldvd = 0;
            double ddldvb = 0;
            if ((_mode == 1 ? vgs : vgd) <= _von) {
                idrain = 0;
                gm = 0;
                gds = 0;
                gmbs = 0;
                key = KEY.DONE;//innerline1000;
            } else {
                //if (key == KEY.INIT) {
                vgsx = MathLib.max((_mode == 1 ? vgs : vgd), _von);
                onfg = 1 + theta * (vgsx - vth);
                fgate = 1 / onfg;
                double us = tSurfMob * 1e-4 * fgate;
                dfgdvg = -theta * fgate * fgate;
                dfgdvd = -dfgdvg * dvtdvd;
                dfgdvb = -dfgdvg * dvtdvb;
                // Saturation voltage
                _vdsat = (vgsx - vth) * onfbdy;
                if (maxDriftVel <= 0) {
                    dvsdvg = onfbdy;
                    dvsdvd = -dvsdvg * dvtdvd;
                    dvsdvb = -dvsdvg * dvtdvb - _vdsat * dfbdvb * onfbdy;
                } else {
                    double vdsc = effectiveLength * maxDriftVel / us;
                    onvdsc = 1 / vdsc;
                    arga = (vgsx - vth) * onfbdy;
                    argb = MathLib.sqrt(arga * arga + vdsc * vdsc);
                    _vdsat = arga + vdsc - argb;
                    dvsdga = (1 - arga / argb) * onfbdy;
                    dvsdvg = dvsdga - (1 - vdsc / argb) * vdsc * dfgdvg * onfg;
                    dvsdvd = -dvsdvg * dvtdvd;
                    dvsdvb = -dvsdvg * dvtdvb - arga * dvsdga * dfbdvb;
                }
                // Current factors in linear region
                vdsx = MathLib.min((_mode * vds), _vdsat);
                if (vdsx == 0) {
                    //case CASE4://line900:
                    beta = beta * fgate;
                    idrain = 0;
                    gm = 0;
                    gds = beta * (vgsx - vth);
                    gmbs = 0;
                    if (fastSurfaceStateDensity != 0
                            && (_mode == 1 ? vgs : vgd) < _von) {
                        gds *= MathLib.exp(((_mode == 1 ? vgs : vgd) - _von) / (vt * xn));
                    }
                    key = KEY.DONE;
                } else {
                    double cdo = vgsx - vth - 0.5 * (1 + fbody) * vdsx;
                    double dcodvb = -dvtdvb - 0.5 * dfbdvb * vdsx;
                    // Normalized drain current
                    double cdnorm = cdo * vdsx;
                    gm = vdsx;
                    if ((_mode * vds) > _vdsat) {
                        gds = -dvtdvd * vdsx;
                    } else {
                        gds = vgsx - vth - (1 + fbody + dvtdvd) * vdsx;
                    }
                    gmbs = dcodvb * vdsx;
                    // Drain current without velocity saturation effect
                    double cd1 = beta * cdnorm;
                    beta = beta * fgate;
                    idrain = beta * cdnorm;
                    gm = beta * gm + dfgdvg * cd1;
                    gds = beta * gds + dfgdvd * cd1;
                    gmbs = beta * gmbs + dfgdvb * cd1;
                    // Velocity saturation factor
                    if (maxDriftVel > 0) {
                        fdrain = 1 / (1 + vdsx * onvdsc);
                        double fd2 = fdrain * fdrain;
                        arga = fd2 * vdsx * onvdsc * onfg;
                        dfddvg = -dfgdvg * arga;
                        if ((_mode * vds) > _vdsat) {
                            dfddvd = -dfgdvd * arga;
                        } else {
                            dfddvd = -dfgdvd * arga - fd2 * onvdsc;
                        }
                        dfddvb = -dfgdvb * arga;
                        // Drain current
                        gm = fdrain * gm + dfddvg * idrain;
                        gds = fdrain * gds + dfddvd * idrain;
                        gmbs = fdrain * gmbs + dfddvb * idrain;
                        idrain = fdrain * idrain;
                        beta = beta * fdrain;
                    }
                    // Channel length modulation
                    if ((_mode * vds) <= _vdsat) {
                        if ((maxDriftVel > 0) || (alpha == 0) || (env.isBadMos3())) {
                            key = KEY.CASE3;
                        } else {
                            arga = (_mode * vds) / _vdsat;
                            delxl = MathLib.sqrt(kappa * alpha * _vdsat / 8);
                            dldvd = 4 * delxl * arga * arga * arga / _vdsat;
                            arga *= arga;
                            arga *= arga;
                            delxl *= arga;
                            ddldvg = 0;
                            ddldvd = -dldvd;
                            ddldvb = 0;
                            key = KEY.CASE2;
                        }
                    } else {
                        if (maxDriftVel <= 0) {
                            key = KEY.CASE1;
                        } else if (alpha == 0) {
                            key = KEY.CASE3;
                        } else {
                            double idsat = idrain;
                            double gdsat = idsat * (1 - fdrain) * onvdsc;
                            gdsat = MathLib.max(1e-12, gdsat);
                            double gdoncd = gdsat / idsat;
                            double gdonfd = gdsat / (1 - fdrain);
                            double gdonfg = gdsat * onfg;
                            double dgdvg = gdoncd * gm - gdonfd * dfddvg + gdonfg * dfgdvg;
                            double dgdvd = gdoncd * gds - gdonfd * dfddvd + gdonfg * dfgdvd;
                            double dgdvb = gdoncd * gmbs - gdonfd * dfddvb + gdonfg * dfgdvb;
                            double emax;
                            if (env.isBadMos3()) {
                                emax = idsat * oneOverXl / gdsat;
                            } else {
                                emax = kappa * idsat * oneOverXl / gdsat;
                            }
                            double emoncd = emax / idsat;
                            double emongd = emax / gdsat;
                            double demdvg = emoncd * gm - emongd * dgdvg;
                            double demdvd = emoncd * gds - emongd * dgdvd;
                            double demdvb = emoncd * gmbs - emongd * dgdvb;
                            arga = 0.5 * emax * alpha;
                            argc = kappa * alpha;
                            argb = MathLib.sqrt(arga * arga + argc * ((_mode * vds) - _vdsat));
                            delxl = argb - arga;
                            if (argb != 0) {
                                dldvd = argc / (argb + argb);
                                dldem = 0.5 * (arga / argb - 1) * alpha;
                            } else {
                                dldvd = 0;
                                dldem = 0;
                            }
                            ddldvg = dldem * demdvg;
                            ddldvd = dldem * demdvd - dldvd;
                            ddldvb = dldem * demdvb;
                            key = KEY.CASE2;
                        }
                    }
                }
            }
            double gds0 = 0;
            switch (key) {
                case CASE1://line510:
                    if (env.isBadMos3()) {
                        delxl = MathLib.sqrt(kappa * ((_mode * vds) - _vdsat)
                                * alpha);
                        dldvd = 0.5 * delxl / ((_mode * vds) - _vdsat);
                    } else {
                        delxl = MathLib.sqrt(kappa * alpha
                                * ((_mode * vds) - _vdsat + (_vdsat / 8)));
                        dldvd = 0.5 * delxl / ((_mode * vds) - _vdsat + (_vdsat / 8));
                    }
                    ddldvg = 0;
                    ddldvd = -dldvd;
                    ddldvb = 0;
                // Punch through approximation
                case CASE2://line520:
                    if (delxl > (0.5 * effectiveLength)) {
                        delxl = effectiveLength - (effectiveLength * effectiveLength
                                / (4 * delxl));
                        arga = 4 * (effectiveLength - delxl) * (effectiveLength - delxl)
                                / (effectiveLength * effectiveLength);
                        ddldvg = ddldvg * arga;
                        ddldvd = ddldvd * arga;
                        ddldvb = ddldvb * arga;
                        dldvd = dldvd * arga;
                    }
                    // Saturation region
                    double dlonxl = delxl * oneOverXl;
                    double xlfact = 1 / (1 - dlonxl);
                    idrain = idrain * xlfact;
                    double diddl = idrain / (effectiveLength - delxl);
                    gm = gm * xlfact + diddl * ddldvg;
                    gmbs = gmbs * xlfact + diddl * ddldvb;
                    gds0 = diddl * ddldvd;
                    gm = gm + gds0 * dvsdvg;
                    gmbs = gmbs + gds0 * dvsdvb;
                    gds = gds * xlfact + diddl * dldvd + gds0 * dvsdvd;
                // Finish strong inversion case
                case CASE3://line700:
                    if ((_mode == 1 ? vgs : vgd) < _von) {
                        // Weak inversion
                        double onxn = 1 / xn;
                        double ondvt = onxn / vt;
                        double wfact = MathLib.exp(((_mode == 1 ? vgs : vgd) - _von) * ondvt);
                        idrain = idrain * wfact;
                        double gms = gm * wfact;
                        double gmw = idrain * ondvt;
                        gm = gmw;
                        if ((_mode * vds) > _vdsat) {
                            gm = gm + gds0 * dvsdvg * wfact;
                        }
                        gds = gds * wfact + (gms - gmw) * dvodvd;
                        gmbs = gmbs * wfact + (gms - gmw) * dvodvb - gmw
                                * ((_mode == 1 ? vgs : vgd) - _von) * onxn * dxndvb;
                    }
            }
            //case CASE5://innerline1000:
            this.von = type * _von;
            this.vdsat = type * _vdsat;
            cd = _mode * idrain - cbd;
            double sarg;
            double sargsw;
            if (mode.contains(MODE.TRAN, MODE.TRANOP, MODE.INIT_SMSIG)) {
                if (vbs < tDepCap) {
                    arg = 1 - vbs / tBlkPot;
                    if (blkJctBotGradingCoeff
                            == blkJctSideGradingCoeff) {
                        if (blkJctBotGradingCoeff == 0.5) {
                            sarg = sargsw = 1 / MathLib.sqrt(arg);
                        } else {
                            sarg = sargsw =
                                    MathLib.exp(-blkJctBotGradingCoeff
                                    * MathLib.log(arg));
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
                    if (blkJctBotGradingCoeff == 0.5
                            && blkJctSideGradingCoeff == 0.5) {
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
                    if (!stateTable.integrate(geq, ieq, capbd, qbdStates, cqbdStates)) {
                        return false;
                    }
                    gbd += geq.get();
                    cbd += cqbdStates.get(0);
                    cd -= cqbdStates.get(0);
                    if (!stateTable.integrate(geq, ieq, capbs, qbsStates, cqbsStates)) {
                        return false;
                    }
                    gbs += geq.get();
                    cbs += cqbsStates.get(0);
                }
            }
            if ((!off || !mode.contains(MODE.INIT_FIX, MODE.INIT_SMSIG))
                    && check.isTrue()) {
                ckt.setNonConverged(true);
            }
            vbsStates.set(0, vbs);
            vbdStates.set(0, vbd);
            vgsStates.set(0, vgs);
            vdsStates.set(0, vds);
            // Meyer's capacitor
            if (mode.contains(MODE.TRAN, MODE.TRANOP, MODE.INIT_SMSIG)) {
                // Calculate meyer's capacitors
                if (_mode > 0) {
                    MOSUtils.qMeyer(vgs, vgd, vgb, _von, _vdsat,
                            capgs, capgd, capgb,
                            tPhi, oxideCap);
                } else {
                    MOSUtils.qMeyer(vgd, vgs, vgb, _von, _vdsat,
                            capgd, capgs, capgb,
                            tPhi, oxideCap);
                }
                capgsStates.set(0, capgs.get());
                capgdStates.set(0, capgd.get());
                capgbStates.set(0, capgb.get());
                double vgs1 = vgsStates.get(1);
                double vgd1 = vgs1 - vdsStates.get(1);
                double vgb1 = vgs1 - vbsStates.get(1);
                if (mode.contains(MODE.TRANOP)) {
                    capgs.set(2 * capgsStates.get(0) + gateSrcOverlapCap);
                    capgd.set(2 * capgdStates.get(0) + gateDrnOverlapCap);
                    capgb.set(2 * capgbStates.get(0) + gateBlkOverlapCap);
                } else {
                    capgs.set(capgsStates.get(0) + capgsStates.get(1) + gateSrcOverlapCap);
                    capgd.set(capgdStates.get(0) + capgdStates.get(1) + gateDrnOverlapCap);
                    capgb.set(capgbStates.get(0) + capgbStates.get(1) + gateBlkOverlapCap);
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
                        // Tran op only
                        qgsStates.set(0, vgs * capgs.get());
                        qgdStates.set(0, vgd * capgd.get());
                        qgbStates.set(0, vgb * capgb.get());
                    }
                }
            }
        }
        if (mode.contains(MODE.INIT_TRAN) || !mode.contains(MODE.TRAN)) {
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
            }
            // Calculate equivalent conductances and currents for meyer's capacitors
            if (!stateTable.integrate(gcgs, ceqgs, capgs.get(), qgsStates, cqgsStates)) {
                return false;
            }
            if (!stateTable.integrate(gcgd, ceqgd, capgd.get(), qgdStates, cqgdStates)) {
                return false;
            }
            if (!stateTable.integrate(gcgb, ceqgb, capgb.get(), qgbStates, cqbsStates)) {
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
        if (_mode >= 0) {
            xnrm = 1;
            xrev = 0;
            cdreq = type * (idrain - gds * vds
                    - gm * vgs - gmbs * vbs);
        } else {
            xnrm = 0;
            xrev = 1;
            cdreq = -(type) * (idrain - gds * (-vds)
                    - gm * vgd - gmbs * vbd);
        }
        wrk.getRhsAt(gateIndex).realMinusEq((type * (ceqgs.get() + ceqgb.get() + ceqgd.get())));
        wrk.getRhsAt(blkIndex).realMinusEq((ceqbs + ceqbd - type * ceqgb.get()));
        wrk.getRhsAt(drnPrmIndex).realPlusEq((ceqbd - cdreq + type * ceqgd.get()));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(cdreq + ceqbs + type * ceqgs.get());
        drnDrnNode.realPlusEq((drnConductance));
        gateGateNode.realPlusEq(((gcgd.get() + gcgs.get() + gcgb.get())));
        srcSrcNode.realPlusEq((srcConductance));
        blkBlkNode.realPlusEq((gbd + gbs + gcgb.get()));
        drnPrmDrnPrmNode.realPlusEq(drnConductance + gds
                + gbd + xrev * (gm + gmbs) + gcgd.get());
        srcPrmSrcPrmNode.realPlusEq(srcConductance + gds
                + gbs + xnrm * (gm + gmbs) + gcgs.get());
        drnDrnPrmNode.realPlusEq(-drnConductance);
        gateBlkNode.realMinusEq(gcgb);
        gateDrnPrmNode.realMinusEq(gcgd);
        gateSrcPrmNode.realMinusEq(gcgs);
        srcSrcPrmNode.realPlusEq(-srcConductance);
        blkGateNode.realMinusEq(gcgb);
        blkDrnPrmNode.realMinusEq(gbd);
        blkSrcPrmNode.realMinusEq(gbs);
        drnPrmDrnNode.realPlusEq(-drnConductance);
        drnPrmGateNode.realPlusEq(((xnrm - xrev) * gm - gcgd.get()));
        drnPrmBlkNode.realPlusEq(-gbd + (xnrm - xrev) * gmbs);
        drnPrmSrcPrmNode.realPlusEq(-gds - xnrm * (gm + gmbs));
        srcPrmGateNode.realPlusEq(-(xnrm - xrev) * gm - gcgs.get());
        srcPrmSrcNode.realPlusEq(-srcConductance);
        srcPrmBlkNode.realPlusEq(-gbs - (xnrm - xrev) * gmbs);
        srcPrmDrnPrmNode.realPlusEq(-gds - xrev * (gm + gmbs));
        return true;
    }

    public boolean unSetup() {
        if (drnPrmIndex != 0 && drnPrmIndex != drnIndex) {
            ckt.deleteNode(drnPrmIndex);
            drnPrmIndex = 0;
        }
        if (srcPrmIndex != 0 && srcPrmIndex != srcIndex) {
            ckt.deleteNode(srcPrmIndex);
            srcPrmIndex = 0;
        }
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
        double nifact = (tnom / 300) * MathLib.sqrt(tnom / 300);
        nifact *= MathLib.exp(0.5 * egfet1 * ((1 / (double) 300) - (1 / tnom))
                / KoverQ);
        double niTemp = 1.45e16 * nifact;
        oxideCapFactor = EPS_SiO2 / oxideThickness;
        if (transconductance == 0) {
            transconductance = surfaceMobility
                    * oxideCapFactor * 1e-4;
        }
        if (substrateDoping != 0) {
            if (substrateDoping * 1e6 > niTemp) {
                if (phi == 0) {
                    phi = 2 * vtnom
                            * MathLib.log(substrateDoping
                            * 1e6 / niTemp);
                    phi = MathLib.max(0.1, phi);
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
                    double vfb = wkfngs - surfaceStateDensity * 1e4 * CHARGE / oxideCapFactor;
                    vt0 = vfb + type * (gamma * MathLib.sqrt(phi) + phi);
                }
                alpha = (EPS_SI + EPS_SI) / (CHARGE * substrateDoping * 1e6);
                coeffDepLayWidth = MathLib.sqrt(alpha);
            } else {
                substrateDoping = 0;
                StandardLog.severe(getModelName() + ": Nsub < Ni ");
                return false;
            }
        }
        narrowFactor = delta * 0.5 * MathLib.PI * EPS_SI / oxideCapFactor;
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
        tVbi = delvt0 + vt0 - type
                * (gamma * MathLib.sqrt(phi))
                + 0.5 * (egfet1 - egfet) + type * 0.5 * (tPhi - phi);
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
        capfact = (1 + blkJctBotGradingCoeff
                * (4e-4 * (temp - REF_TEMP) - gmanew));
        tCbd *= capfact;
        tCbs *= capfact;
        tCj *= capfact;
        capfact = (1 + blkJctSideGradingCoeff
                * (4e-4 * (temp - REF_TEMP) - gmanew));
        tCjsw *= capfact;
        tDepCap = fwdCapDepCoeff * tBlkPot;
        if (jctSatCurDensity == 0 || drnArea == 0 || srcArea == 0) {
            srcVcrit = drnVcrit =
                    vt * MathLib.log(vt / (ROOT2 * m * tSatCur));
        } else {
            drnVcrit = vt * MathLib.log(vt / (ROOT2
                    * m * tSatCurDens * drnArea));
            srcVcrit = vt * MathLib.log(vt / (ROOT2
                    * m * tSatCurDens * srcArea));
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
                * (1 + blkJctSideGradingCoeff))
                * sargsw / arg;
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
        return stateTable.terr(qgbStates, cqgdStates, timeStep);
    }

    public boolean accept(Mode mode) {
        return true;
    }
}
