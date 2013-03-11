/*
 * mos1.java
 *
 * Created on August 28, 2006, 1:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.node.Node;
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
public class MOS1Instance extends MOSModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain index */
    protected int drnPrmIndex;

    /* Internal source index */
    protected int srcPrmIndex;
    /* Operating temperature */
    protected double dtemp;
    protected double oxideCapFactor;
    /* Temperature corrected vbi */
    protected double tVbi;
    protected double von;
    protected double vdsat;
    protected double srcVcrit;
    protected double drnVcrit;
    protected double cd;
    protected double cbs;
    protected double cbd;
    protected double gmbs;
    protected double gm;
    protected double gds;
    protected double gbd;
    protected double gbs;
    protected double capbd;
    protected double capbs;
    // Zero voltage bulk - source capacitance
    private double czbs;
    // Zero voltage bulk - drain capacitance
    private double czbd;
    // Zero voltage bulk - drain sidewall capacitance
    private double czbdsw;
    // Zero voltage bulk - source sidewall capacitance
    private double czbssw;
    protected double f2d;
    protected double f3d;
    protected double f4d;
    protected double f2s;
    protected double f3s;
    protected double f4s;
    /* Device _mode : 1 = normal, -1 = inverse */
    protected int _mode = 1;
    protected double cgs;
    protected double cgd;
    protected double cgb;

    /* Source Conductance */
    protected double srcConductance;

    /* Drain  conductance */
    protected double drnConductance;
    /* Temperature corrected transconductance */
    protected double tTransconductance;

    /* Temperature corrected surface mobility */
    protected double tSurfMob;

    /* Temperature corrected phi */
    protected double tPhi;

    /* Temperature corrected vto */
    protected double tVto;

    /* Temperature corrected saturation current */
    protected double tSatCur;

    /* Temperature corrected saturation current density */
    protected double tSatCurDens;

    /* Temperature corrected B - D capacitance */
    protected double tCbd;

    /* Temperature corrected B - S capacitance */
    protected double tCbs;

    /* Temperature corrected bulk bottom capacitance */
    protected double tCj;

    /* Temperature corrected bulk side capacitance */
    protected double tCjsw;

    /* Temperature corrected bulk potential */
    protected double tBlkPot;

    /* Temperature corrected depletion capacitance */
    protected double tDepCap;
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
    private Real ceq = Real.zero();
    private Real ceqgb = Real.zero();
    private Real ceqgd = Real.zero();
    private Real ceqgs = Real.zero();
    private Real gcgb = Real.zero();
    private Real gcgd = Real.zero();
    private Real gcgs = Real.zero();
    private Real geq = Real.zero();
    private Bool check = Bool.TRUE();

    /** Creates a new instance of mos1 */
    public MOS1Instance() {
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
        // load matrix
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
            cdhat = cd - gbd * delvbd + gmbs * delvbs
                    + gm * delvgs + gds * delvds;
        } else {
            cdhat = cd - (gbd - gmbs) * delvbd - gm * delvgd
                    + gds * delvds;
        }
        double cbhat = this.cbs + cbd + gbd * delvbd + gbs * delvbs;
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

    public boolean load(Mode mode) {
        double arg;
        double cdhat;
        double idrain = 0;
        double delvbd = 0;
        double delvbs = 0;
        double vbd = 0;
        double vbs = 0;
        double vds = 0;
        double _vdsat = 0;
        double vgb = 0;
        double vgd = 0;
        double vgs = 0;
        double _von = 0;
        double vt = KoverQ * temp;
        double xfact = 0;
        double effectiveLength = l - 2 * latDiff;
        double gateSrcOverlapCap = gateSrcOverlapCapFactor * m * w;
        double gateDrnOverlapCap = gateDrnOverlapCapFactor * m * w;
        double gateBlkOverlapCap = gateBlkOverlapCapFactor * m * effectiveLength;
        boolean bypass = false;
        if (mode.contains(MODE.INIT_FLOAT, MODE.INIT_PRED, MODE.INIT_SMSIG,
                MODE.INIT_TRAN) || (mode.contains(MODE.INIT_FIX) && !off)) {
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
            delvbs = vbs - vbsStates.get(0);
            delvbd = vbd - vbdStates.get(0);
            double delvgs = vgs - vgsStates.get(0);
            double delvds = vds - vdsStates.get(0);
            double delvgd = vgd - vgdo;
            if (_mode >= 0) {
                cdhat = cd - gbd * delvbd + gmbs * delvbs + gm * delvgs + gds * delvds;
            } else {
                cdhat = cd - (gbd - gmbs) * delvbd - gm * delvgd + gds * delvds;
            }
            double cbhat = cbs + cbd + gbd * delvbd + gbs * delvbs;
            double tempv = (MathLib.max(MathLib.abs(cbhat),
                    MathLib.abs(cbs + cbd)) + env.getAbsTol());
            //? if this condition works do the same for the bjt's
            if (env.isBypass()
                    && !mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN, MODE.INIT_SMSIG)
                    && (MathLib.abs(cbhat - (cbs + cbd)) < env.getRelTol() * tempv)
                    && (MathLib.abs(delvbs) < (env.getRelTol()
                    * MathLib.max(MathLib.abs(vbs),
                    MathLib.abs(vbsStates.get(0)))
                    + env.getVoltTol()))
                    && (MathLib.abs(delvbd) < (env.getRelTol()
                    * MathLib.max(MathLib.abs(vbd),
                    MathLib.abs(vbdStates.get(0)))
                    + env.getVoltTol()))
                    && (MathLib.abs(delvgs) < (env.getRelTol()
                    * MathLib.max(MathLib.abs(vgs),
                    MathLib.abs(vgsStates.get(0)))
                    + env.getVoltTol()))
                    && (MathLib.abs(delvds) < (env.getRelTol()
                    * MathLib.max(MathLib.abs(vds),
                    MathLib.abs(vdsStates.get(0)))
                    + env.getVoltTol()))
                    && (MathLib.abs(cdhat - cd) < (env.getRelTol()
                    * MathLib.max(MathLib.abs(cdhat),
                    MathLib.abs(cd))
                    + env.getAbsTol()))) {
                //? debug
                System.out.println("multicondition worked");
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
            if (!bypass) {
                _von = type * this.von;
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
                if ((vds == 0 && vgs == 0 && vbs == 0
                        && mode.contains(MODE.TRAN,
                        MODE.DCOP, MODE.DCTRANCURVE))
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
            if (tSatCurDens == 0 || drnArea == 0 || srcArea == 0) {
                drnSatCur = m * tSatCur;
                srcSatCur = m * tSatCur;
            } else {
                drnSatCur = tSatCurDens * m * drnArea;
                srcSatCur = tSatCurDens * m * srcArea;
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
                // Normal _mode
                _mode = 1;
            } else {
                // Inverse _mode
                _mode = -1;
            }
            double sarg;
            if ((_mode == 1 ? vbs : vbd) <= 0) {
                sarg = MathLib.sqrt(tPhi - (_mode == 1 ? vbs : vbd));
            } else {
                sarg = MathLib.sqrt(tPhi);
                sarg = sarg - (_mode == 1 ? vbs : vbd) / (sarg + sarg);
                sarg = MathLib.max(0, sarg);
            }
            _von = (tVbi * type) + gamma * sarg;
            double vgst = (_mode == 1 ? vgs : vgd) - _von;
            _vdsat = MathLib.max(vgst, 0);
            if (sarg <= 0) {
                arg = 0;
            } else {
                arg = gamma / (sarg + sarg);
            }
            if (vgst <= 0) {
                idrain = 0;
                gm = 0;
                gds = 0;
                gmbs = 0;
            } else {
                double beta = tTransconductance * m * w / effectiveLength;
                double betap = beta * (1 + lambda * (vds * _mode));
                if (vgst <= (vds * _mode)) {
                    idrain = betap * vgst * vgst * 0.5;
                    gm = betap * vgst;
                    gds = lambda * beta * vgst * vgst * 0.5;
                    gmbs = gm * arg;
                } else {
                    // Linear region
                    idrain = betap * (vds * _mode) * (vgst - 0.5 * (vds * _mode));
                    gm = betap * (vds * _mode);
                    gds = betap * (vgst - (vds * _mode))
                            + lambda * beta
                            * (vds * _mode)
                            * (vgst - 0.5 * (vds * _mode));
                    gmbs = gm * arg;
                }
            }
            this.von = type * _von;
            this.vdsat = type * _vdsat;
            cd = _mode * idrain - cbd;
            double sargsw;
            if (mode.contains(MODE.TRAN, MODE.TRANOP, MODE.INIT_SMSIG)) {
                if (mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN)
                        || MathLib.abs(delvbs) >= env.getRelTol() * MathLib.max(MathLib.abs(vbs),
                        MathLib.abs(vbsStates.get(0)))
                        + env.getVoltTol()) {
                    if (cbs != 0 || czbssw != 0) {
                        if (vbs < tDepCap) {
                            arg = 1 - vbs / tBlkPot;
                            if (blkJctBotGradingCoeff == blkJctSideGradingCoeff) {
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
                    } else {
                        qbsStates.set(0, 0);
                        capbs = 0;
                    }
                }
                if (mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN)
                        || MathLib.abs(delvbd) >= env.getRelTol() * MathLib.max(MathLib.abs(vbd),
                        MathLib.abs(vbdStates.get(0)))
                        + env.getVoltTol()) {
                    if (cbd != 0 || czbdsw != 0) {
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
                            qbdStates.set(0, tBlkPot * (czbd * (1 - arg * sarg)
                                    / (1 - blkJctBotGradingCoeff) + czbdsw
                                    * (1 - arg * sargsw) / (1 - blkJctSideGradingCoeff)));
                            capbd = czbd * sarg + czbdsw * sargsw;
                        } else {
                            qbdStates.set(0, f4d + vbd * (f2d + vbd * f3d / 2));
                            capbd = f2d + vbd * f3d;
                        }
                    } else {
                        qbdStates.set(0, 0);
                        capbd = 0;
                    }
                }
                if (mode.contains(MODE.TRAN) || (mode.contains(MODE.INIT_TRAN)
                        && !mode.isUseIC())) {
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
            double oxideCap = oxideCapFactor * effectiveLength * m * w;
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
                if (mode.contains(MODE.TRANOP, MODE.INIT_SMSIG)) {
                    capgs.set(2 * capgs.get() + gateSrcOverlapCap);
                    capgd.set(2 * capgd.get() + gateDrnOverlapCap);
                    capgb.set(2 * capgb.get() + gateBlkOverlapCap);
                } else {
                    capgs.set((capgs.get() + capgsStates.get(1) + gateSrcOverlapCap));
                    capgd.set((capgd.get() + capgdStates.get(1) + gateDrnOverlapCap));
                    capgb.set(capgb.get() + capgbStates.get(1) + gateBlkOverlapCap);
                }
                if (mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN)) {
                    qgsStates.set(0,
                            (1 + xfact) * qgsStates.get(1) - xfact * qgsStates.get(2));
                    qgdStates.set(0,
                            (1 + xfact) * qgdStates.get(1) - xfact * qgdStates.get(2));
                    qgbStates.set(0,
                            (1 + xfact) * qgbStates.get(1) - xfact * qgbStates.get(2));
                } else {
                    if (mode.contains(MODE.TRAN)) {
                        qgsStates.set(0, (vgs - vgs1) * capgs.get()
                                + qgsStates.get(1));
                        qgdStates.set(0, (vgd - vgd1) * capgd.get()
                                + qgdStates.get(1));
                        qgbStates.set(0, (vgb - vgb1) * capgb.get()
                                + qgbStates.get(1));
                    } else {
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
            if (!stateTable.integrate(gcgb, ceqgb, capgb.get(), qgbStates, cqgbStates)) {
                return false;
            }
            // Store charge storage for meyer's capacitors in table
            ceqgs.set(ceqgs.get() - gcgs.get() * vgs + tmprl.getAgAt(0)
                    * qgsStates.get(0));
            ceqgd.set(ceqgd.get() - gcgd.get() * vgd + tmprl.getAgAt(0)
                    * qgdStates.get(0));
            ceqgb.set(ceqgb.get() - gcgb.get() * vgb + tmprl.getAgAt(0)
                    * qgbStates.get(0));
        }
        // Load current vector
        int xnrm;
        int xrev;
        double cdreq;
        double ceqbs = type * (cbs - gbs * vbs);
        double ceqbd = type * (cbd - gbd * vbd);
        if (_mode >= 0) {
            xnrm = 1;
            xrev = 0;
            cdreq = type * (idrain - gds * vds - gm * vgs - gmbs * vbs);
        } else {
            xnrm = 0;
            xrev = 1;
            cdreq = -type * (idrain - gds * (-vds)
                    - gm * vgd - gmbs * vbd);
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
        drnDrnPrmNode.realPlusEq(-drnConductance);
        gateBlkNode.realMinusEq(gcgb);
        gateDrnPrmNode.realMinusEq(gcgd);
        gateSrcPrmNode.realMinusEq(gcgs);
        srcSrcPrmNode.realPlusEq(-srcConductance);
        blkGateNode.realMinusEq(gcgb);
        blkDrnPrmNode.realMinusEq(gbd);
        blkSrcPrmNode.realMinusEq(gbs);
        drnPrmDrnNode.realPlusEq(-drnConductance);
        drnPrmGateNode.realPlusEq((xnrm - xrev) * gm - gcgd.get());
        drnPrmBlkNode.realPlusEq(-gbd + (xnrm - xrev) * gmbs);
        drnPrmSrcPrmNode.realPlusEq(-gds - xnrm * (gm + gmbs));
        srcPrmGateNode.realPlusEq(-(xnrm - xrev) * gm - gcgs.get());
        srcPrmSrcNode.realPlusEq(-srcConductance);
        srcPrmBlkNode.realPlusEq(-gbs - (xnrm - xrev) * gmbs);
        srcPrmDrnPrmNode.realPlusEq(-gds - xrev * (gm + gmbs));
        /*        System.out.printf(" loading %s at time %g\n", instName, tmprl.getTime());
        System.out.printf("%g %g %g %g %g\n", drnConductance,
        gcgd.get() + gcgs.get() + gcgb.get(), srcConductance,
        gbd, gbs);
        System.out.printf("%g %g %g %g %g\n", -gcgb.get(), 0, 0,
        gds, gm);
        System.out.printf("%g %g %g %g %g\n", gds, gmbs,
        gcgd.get(), -gcgs.get(), -gcgd.get());
        System.out.printf("%g %g %g %g %g\n", -gcgs.get(), -gcgd.get(), 0, -gcgs.get(), 0);
         */
        return true;
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
        Node tmp;
        if ((drnResistance != 0 || sheetResistance != 0) && drnPrmIndex == 0) {
            if ((tmp = ckt.makeVoltNode(getInstName() + "#drain")) == null) {
                return false;
            }
            drnPrmIndex = tmp.getIndex();
            /*            if (env.isCopyNodeSets()) {
            Node tmpNode;
            String tmpName;
            if(ckt.inst2Node(this, 1, tmpNode, tmpName) != 0) {
            if (tmpNode.isNSGiven()) {
            tmp.setNS(tmpNode.getNS());
            // tmp.setNSGiven(tmpNode.isNSGiven());
            }
            }
            }
             */
        } else {
            drnPrmIndex = drnIndex;
        }
        if ((srcResistance != 0 || sheetResistance != 0) && srcPrmIndex == 0) {
            if ((tmp = ckt.makeVoltNode(getInstName() + "#source")) == null) {
                return false;
            }
            srcPrmIndex = tmp.getIndex();
            /*
            if (env.isCopyNodeSets()) {
            Node tmpNode;
            String tmpName;
            if(ckt.inst2Node(this, 3, tmpNode, tmpName) != 0) {
            if (tmpNode.isNSGiven()) {
            tmp.setNS(tmpNode.getNS());
            // tmp.setNSGiven(tmpNode.isNSGiven());
            }
            }
            }
             * */
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
        oxideCapFactor = EPS_SiO2 / oxideThickness;
        if (transconductance == 0) {
            transconductance = surfaceMobility * oxideCapFactor * 1e-4;
        }
        if (substrateDoping != 0) {
            if (substrateDoping * 1e6 > 1.45e16) {
                if (phi == 0) {
                    phi = 2 * vtnom * MathLib.log(substrateDoping
                            * 1e6 / 1.45e16);
                    phi = MathLib.max(0.1, phi);
                }
                double fermis = type * 0.5 * phi;
                double fermig = type * gateType * 0.5 * egfet1;
                double wkfng = 3.25 + 0.5 * egfet1 - fermig;
                double wkfngs = wkfng - (3.25 + 0.5 * egfet1 + fermis);
                if (gamma == 0) {
                    gamma = MathLib.sqrt(2 * EPS_SI
                            * CHARGE * substrateDoping * 1e6)
                            / oxideCapFactor;
                }
                if (vt0 == 0) {
                    double vfb = wkfngs - surfaceStateDensity * 1e4 * CHARGE / oxideCapFactor;
                    vt0 = vfb + type * (gamma * MathLib.sqrt(phi) + phi);
                }
            } else {
                substrateDoping = 0;
                StandardLog.severe(getModelName() + ": Nsub < Ni");
                return false;
            }
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
        if (l - 2 * latDiff <= 0) {
            StandardLog.warning("Front: " + getModelName() + ": effective channel length less than zero");
        }
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
        capfact = (1 + blkJctBotGradingCoeff
                * (4e-4 * (temp - REF_TEMP) - gmanew));
        tCbd *= capfact;
        tCbs *= capfact;
        tCj *= capfact;
        capfact = (1 + blkJctSideGradingCoeff
                * (4e-4 * (temp - REF_TEMP) - gmanew));
        tCjsw *= capfact;
        tDepCap = fwdCapDepCoeff * tBlkPot;
        if (tSatCurDens == 0 || drnArea == 0 || srcArea == 0) {
            srcVcrit = drnVcrit =
                    vt * MathLib.log(vt / (ROOT2 * m * tSatCur));
        } else {
            drnVcrit =
                    vt * MathLib.log(vt / (ROOT2 * m * tSatCurDens * drnArea));
            srcVcrit =
                    vt * MathLib.log(vt / (ROOT2 * m * tSatCurDens * srcArea));
        }
        if (capBD != 0) {
            czbd = tCbd * m;
        } else {
            if (blkCapFactor != 0) {
                czbd = tCj * m * drnArea;
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
        return stateTable.terr(qgbStates, cqgbStates, timeStep);
    }

    public boolean accept(Mode mode) {
        return true;
    }
}
