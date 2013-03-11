/*
 * mos6.java
 *
 * Created on August 28, 2006, 2:36 PM
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
public class MOS6Instance extends MOS6ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;

    /* Source Conductance  */
    private double srcConductance;

    /* Drain conductance */
    private double drnConductance;

    /* Temperature difference */
    private double dtemp;

    /* Temperature corrected drain linear conductance factor */
    private double tKv;

    /* Temperature corrected saturation current factor */
    private double tKc;

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

    /* Temperature corrected transition point in curve matching fc * vj */
    private double tDepCap;

    /* Temperature corrected vbi */
    private double tVbi;
    private double von;
    private double vdsat;

    /* Vcrit for positive vds */
    private double srcVcrit;

    /* Vcrit for negative vds */
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
    /* Device mode : 1 = normal, -1 = inverse */
    private int _mode = 1;
    private double cgs;
    private double cgd;
    private double cgb;
    private double oxideCapFactor;
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
    private Real iEq = Real.zero();
    private Real gEq = Real.zero();
    private Bool check = Bool.TRUE();

    /** Creates a new instance of mos6 */
    public MOS6Instance() {
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
        if (lambda != 0) {
            lamda0 = lambda;
        }
        if ((drnResistance != 0 || (sheetResistance != 0
                && drnSquares != 0)) && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "#drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if ((srcResistance != 0 || (sheetResistance != 0
                && srcSquares != 0)) && srcPrmIndex == 0) {
            srcPrmIndex = ckt.makeVoltNode(getInstName() + "#source").getIndex();
        } else {
            srcPrmIndex = srcIndex;
        }
        if (drnResistance != 0) {
            drnConductance = 1 / drnResistance;
        } else if (sheetResistance != 0) {
            drnConductance = 1 / (sheetResistance * drnSquares);
        }
        if (srcResistance != 0) {
            srcConductance = 1 / srcResistance;
        } else if (sheetResistance != 0) {
            srcConductance = 1 / (sheetResistance * srcSquares);
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
                    * MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbs + cbd))
                    + env.getAbsTol();
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
        double vbd = 0;
        double vbs = 0;
        double vds = 0;
        double vgb = 0;
        double vgd = 0;
        double vgdo;
        double vgs = 0;
        double _von = 0;
        double xfact = 0;
        double vt = KoverQ * temp;
        double effectiveLength = l - 2 * latDiff;
        double gateSrcOverlapCap = gateSrcOverlapCapFactor * w;
        double gateDrnOverlapCap = gateDrnOverlapCapFactor * w;
        double gateBlkOverlapCap = gateBlkOverlapCapFactor * effectiveLength;
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
                vbdStates.set(0, vbsStates.get(0)
                        - vdsStates.get(0));
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
                cdhat = cd - gbd * delvbd + gmbs
                        * delvbs + gm * delvgs + gds * delvds;
            } else {
                cdhat = cd - (gbd - gmbs) * delvbd - gm * delvgd + gds * delvds;
            }
            double cbhat = cbs + cbd + gbd * delvbd + gbs * delvbs;
            double tempv = MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbs + cbd)) + env.getAbsTol();
            if (!mode.contains(MODE.INIT_PRED, MODE.INIT_TRAN, MODE.INIT_SMSIG) && env.isBypass()) {
                if ((MathLib.abs(cbhat - (cbs + cbd)) < env.getRelTol() * tempv)) {
                    if ((MathLib.abs(delvbs) < (env.getRelTol() * MathLib.max(MathLib.abs(vbs), MathLib.abs(vbsStates.get(0))) + env.getVoltTol()))) {
                        if ((MathLib.abs(delvbd) < (env.getRelTol() * MathLib.max(MathLib.abs(vbd), MathLib.abs(vbdStates.get(0))) + env.getVoltTol()))) {
                            if ((MathLib.abs(delvgs) < (env.getRelTol() * MathLib.max(MathLib.abs(vgs), MathLib.abs(vgsStates.get(0))) + env.getVoltTol()))) {
                                if ((MathLib.abs(delvds) < (env.getRelTol() * MathLib.max(MathLib.abs(vds), MathLib.abs(vdsStates.get(0))) + env.getVoltTol()))) {
                                    if ((MathLib.abs(cdhat - cd) < env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol())) {
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
                                    }
                                }
                            }
                        }
                    }
                }
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
                if ((vds == 0) && (vgs == 0) && (vbs == 0)
                        && mode.contains(MODE.TRAN,
                        MODE.DCOP, MODE.DCTRANCURVE)
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
                drnSatCur = tSatCur;
                srcSatCur = tSatCur;
            } else {
                drnSatCur = tSatCurDens * drnArea;
                srcSatCur = tSatCurDens * srcArea;
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
                _mode = 1;
            } else {
                _mode = -1;
            }
            double vonbm = 0;
            double vbsvbd = (_mode == 1 ? vbs : vbd);
            double sarg;
            if (vbsvbd <= 0) {
                sarg = MathLib.sqrt(tPhi - vbsvbd);
            } else {
                sarg = MathLib.sqrt(tPhi);
                sarg = sarg - vbsvbd / (sarg + sarg);
                sarg = MathLib.max(0, sarg);
            }
            double vdshere = vds * _mode;
            _von = (tVbi * type) + gamma * sarg - gamma1 * vbsvbd - sigma * vdshere;
            double vgon = (_mode == 1 ? vgs : vgd) - _von;
            double _vdsat = 0;
            if (vgon <= 0) {
                _vdsat = 0;
                idrain = 0;
                gm = 0;
                gds = 0;
                gmbs = 0;
            } else {
                if (sarg <= 0) {
                    arg = 0;
                } else {
                    if ((_mode == 1 ? vbs : vbd) <= 0) {
                        vonbm = gamma1 + gamma / (sarg + sarg);
                    } else {
                        vonbm = gamma1 + gamma / 2 / MathLib.sqrt(tPhi);
                    }
                }
                sarg = MathLib.log(vgon);
                _vdsat = kv * MathLib.exp(sarg * nv);
                double betac = tKc * w / effectiveLength;
                double idsat = betac * MathLib.exp(sarg * nc);
                double _lambda = lamda0 - lamda1 * vbsvbd;
                idrain = idsat * (1 + _lambda * vdshere);
                gm = idrain * nc / vgon;
                gds = gm * sigma + idsat * _lambda;
                gmbs = gm * vonbm - idsat * lamda1 * vdshere;
                if (_vdsat > vdshere) {
                    double vdst = vdshere / _vdsat;
                    double vdst2 = (2 - vdst) * vdst;
                    double vdstg = -vdst * nv / vgon;
                    double ivdst1 = idrain * (2 - vdst - vdst);
                    idrain = idrain * vdst2;
                    gm = gm * vdst2 + ivdst1 * vdstg;
                    gds = gds * vdst2 + ivdst1 * (1 / _vdsat + vdstg * sigma);
                    gmbs = gmbs * vdst2 + ivdst1 * vdstg * vonbm;
                }
            }
            this.von = type * _von;
            this.vdsat = type * _vdsat;
            cd = _mode * idrain - cbd;
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
                    qbsStates.set(0, tBlkPot * (czbs * (1 - arg * sarg)
                            / (1 - blkJctBotGradingCoeff) + czbssw * (1 - arg * sargsw)
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
                            sarg = MathLib.exp(-blkJctBotGradingCoeff * MathLib.log(arg));
                        }
                        if (blkJctSideGradingCoeff == 0.5) {
                            sargsw = 1 / MathLib.sqrt(arg);
                        } else {
                            sargsw = MathLib.exp(-blkJctSideGradingCoeff * MathLib.log(arg));
                        }
                    }
                    qbdStates.set(0, tBlkPot * (czbd * (1 - arg * sarg)
                            / (1 - blkJctBotGradingCoeff) + czbdsw * (1 - arg * sargsw) / (1 - blkJctSideGradingCoeff)));
                    capbd = czbd * sarg + czbdsw * sargsw;
                } else {
                    qbdStates.set(0, f4d + vbd * (f2d + vbd * f3d / 2));
                    capbd = f2d + vbd * f3d;
                }
            }
            if (mode.contains(MODE.TRAN)) {
                stateTable.integrate(gEq, iEq, capbd, qbdStates, cqbdStates);
                gbd += gEq.get();
                cbd += cqbdStates.get(0);
                cd -= cqbdStates.get(0);
                stateTable.integrate(gEq, iEq, capbs, qbsStates, cqbsStates);
                gbs += gEq.get();
                cbs += cqbsStates.get(0);
            }
            if ((!off || !mode.contains(MODE.INIT_FIX, MODE.INIT_SMSIG))
                    && check.isTrue()) {
                ckt.setNonConverged(true);
            }
            vbsStates.set(0, vbs);
            vbdStates.set(0, vbd);
            vgsStates.set(0, vgs);
            vdsStates.set(0, vds);
            double oxideCap = oxideCapFactor * effectiveLength * w;
            if (mode.contains(MODE.TRAN, MODE.TRANOP, MODE.INIT_SMSIG)) {
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
                if (mode.contains(MODE.TRANOP, MODE.INIT_SMSIG)) {
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
        double ceqbs = type * (cbs - gbs * vbs);
        double ceqbd = type * (cbd - gbd * vbd);
        int xnrm;
        int xrev;
        double cdreq;
        if (_mode >= 0) {
            xnrm = 1;
            xrev = 0;
            cdreq = type * (idrain - gds * vds
                    - gm * vgs - gmbs * vbs);
        } else {
            xnrm = 0;
            xrev = 1;
            cdreq = -type * (idrain - gds * -vds
                    - gm * vgd - gmbs * vbd);
        }
        wrk.getRhsAt(gateIndex).realMinusEq(m * (type * (ceqgs.get() + ceqgb.get() + ceqgd.get())));
        wrk.getRhsAt(blkIndex).realMinusEq(m * (ceqbs + ceqbd - type * ceqgb.get()));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (ceqbd - cdreq + type * ceqgd.get()));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqbs + type * ceqgs.get()));
        // Load y matrix
        drnDrnNode.realPlusEq(m * drnConductance);
        gateGateNode.realPlusEq(m * (gcgd.get() + gcgs.get() + gcgb.get()));
        srcSrcNode.realPlusEq(m * srcConductance);
        blkBlkNode.realPlusEq(m * (gbd + gbs + gcgb.get()));
        drnPrmDrnPrmNode.realPlusEq(m * (drnConductance + gds
                + gbd + xrev * (gm + gmbs) + gcgd.get()));
        srcPrmSrcPrmNode.realPlusEq(m * (srcConductance + gds
                + gbs + xnrm * (gm + gmbs) + gcgs.get()));
        drnDrnPrmNode.realPlusEq(m * -drnConductance);
        gateBlkNode.realMinusEq(m * gcgb.get());
        gateDrnPrmNode.realMinusEq(m * gcgd.get());
        gateSrcPrmNode.realMinusEq(m * gcgs.get());
        srcSrcPrmNode.realPlusEq(m * -srcConductance);
        blkGateNode.realMinusEq(m * gcgb.get());
        blkDrnPrmNode.realMinusEq(m * gbd);
        blkSrcPrmNode.realMinusEq(m * gbs);
        drnPrmDrnNode.realPlusEq(m * -drnConductance);
        drnPrmGateNode.realPlusEq(m * ((xnrm - xrev) * gm - gcgd.get()));
        drnPrmBlkNode.realPlusEq(m * (-gbd + (xnrm - xrev) * gmbs));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - xnrm) * (gm + gmbs));
        srcPrmGateNode.realPlusEq(m * (-(xnrm - xrev) * gm - gcgs.get()));
        srcPrmSrcNode.realPlusEq(m * -srcConductance);
        srcPrmBlkNode.realPlusEq(m * (-gbs - (xnrm - xrev) * gmbs));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds - xrev) * (gm + gmbs));
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
        if (kc == 0) {
            kc = 0.5 * surfaceMobility * oxideCapFactor * 1e-4;
        }
        if (substrateDoping != 0) {
            if (substrateDoping * 1e6 > 1.45e16) {
                if (phi == 0) {
                    phi = 2 * vtnom
                            * MathLib.log(substrateDoping
                            * 1e6 / 1.45e16);
                    phi = MathLib.max(0.1, phi);
                }
                double fermis = type * 0.5 * phi;
                double fermig = type * gateType * 0.5 * egfet1;
                double wkfng = 3.25 + 0.5 * egfet1 - fermig;
                double wkfngs = wkfng - (3.25 + 0.5 * egfet1 + fermis);
                if (gamma == 0) {
                    gamma = MathLib.sqrt(2 * EPS_SI
                            * CHARGE * substrateDoping
                            * 1e6)
                            / oxideCapFactor;
                }
                if (vt0 == 0) {
                    double vfb = wkfngs - surfaceStateDensity
                            * 1e4 * CHARGE / oxideCapFactor;
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
        tKv = kv;
        tKc = kc / ratio4;
        tSurfMob = surfaceMobility / ratio4;
        double phio = (phi - pbfact1) / fact1;
        tPhi = fact2 * phio + pbfact;
        tVbi = vt0 - type
                * (gamma * MathLib.sqrt(phi)) + 0.5 * (egfet1 - egfet) + type * 0.5 * (tPhi - phi);
        tVto = tVbi + type
                * gamma * MathLib.sqrt(tPhi);
        tSatCur = jctSatCur
                * MathLib.exp(-egfet / vt + egfet1 / vtnom);
        tSatCurDens = jctSatCurDensity
                * MathLib.exp(-egfet / vt + egfet1 / vtnom);
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
                    vt * MathLib.log(vt / (ROOT2 * tSatCur));
        } else {
            drnVcrit =
                    vt * MathLib.log(vt / (ROOT2
                    * tSatCurDens * drnArea));
            srcVcrit =
                    vt * MathLib.log(vt / (ROOT2
                    * tSatCurDens * srcArea));
        }
        if (capBD != 0) {
            czbd = tCbd;
        } else {
            if (blkCapFactor != 0) {
                czbd = tCj * drnArea;
            }
        }
        if (sideWallCapFactor != 0) {
            czbdsw = tCjsw * drnPerimeter;
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
            czbs = tCbs;
        } else {
            if (blkCapFactor != 0) {
                czbs = tCj * srcArea;
            }
        }
        if (sideWallCapFactor != 0) {
            czbssw = tCjsw * srcPerimeter;
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

    public boolean acLoad(Mode mode) {
        return true;
    }
}
