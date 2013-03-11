/*
 * bjt.java
 *
 * Created on August 10, 2006, 4:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.bjt;

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
import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.MOSUtils;

import javolution.lang.MathLib;
import javolution.util.StandardLog;
import ktb.math.Bool;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class BJT1Instance extends BJTModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Inverse of earlyVoltF */
    protected double invEarlyVoltF;

    /* Inverse of earlyVoltR */
    protected double invEarlyVoltR;

    /* Inverse of rollOffF */
    protected double invRollOffF;

    /* Inverse of rollOffR */
    protected double invRollOffR;

    /* Collector conductance */
    protected double collectorConduct;

    /* Emitter conductance */
    protected double emitterConduct;

    /* Internal collector node index */
    protected int colPrmIndex;

    /* Internal base node index */
    protected int basePrmIndex;

    /* Internal emitter node index */
    protected int emitPrmIndex;

    /* Temperature corrected saturation current */
    protected double tSatCur;

    /* Temperature corrected forward beta */
    protected double tBetaF;

    /* Temperature corrected reverse beta */
    protected double tBetaR;

    /* Temperature corrected B - E leakage current */
    protected double tBEleakCur;

    /* Temperature corrected B - C leakage current */
    protected double tBCleakCur;

    /* Temperature corrected B - E capacitance */
    protected double tBEcap;

    /* Temperature corrected B - E potential */
    protected double tBEpot;

    /* Temperature corrected B - C capacitance */
    protected double tBCcap;

    /* Temperature corrected B - C potential */
    protected double tBCpot;

    /* Temperature corrected join point in diode curve */
    protected double tDepCap;

    /* Temperature corrected polynomial coefficient */
    protected double tf1;

    /* Temperature corrected polynomial coefficient */
    protected double tf4;

    /* Temperature corrected polynomial coefficient */
    protected double tf5;

    /* Temperature corrected critical voltage */
    protected double tVcrit;
    protected double transitTimeVBCFactor;
    protected double excessPhaseFactor;
    protected double c2;
    protected double c4;
    protected double capbe;
    protected double capbc;
    protected double capcs;
    protected double capbx;
    protected double f2;
    protected double f3;
    protected double f6;
    protected double f7;
    private Complex colColPrmNode;
    private Complex baseBasePrmNode;
    private Complex emitEmitPrmNode;
    private Complex colPrmColNode;
    private Complex colPrmBasePrmNode;
    private Complex colPrmEmitPrmNode;
    private Complex basePrmBaseNode;
    private Complex basePrmColPrmNode;
    private Complex basePrmEmitPrmNode;
    private Complex emitPrmEmitNode;
    private Complex emitPrmColPrmNode;
    private Complex emitPrmBasePrmNode;
    private Complex colColNode;
    private Complex baseBaseNode;
    private Complex emitEmitNode;
    private Complex colPrmColPrmNode;
    private Complex basePrmBasePrmNode;
    private Complex emitPrmEmitPrmNode;
    private Complex substSubstNode;
    private Complex colPrmSubstNode;
    private Complex substColPrmNode;
    private Complex baseColPrmNode;
    private Complex colPrmBaseNode;
    private StateVector vbeStates;
    private StateVector vbcStates;
    private StateVector ccStates;
    private StateVector cbStates;
    private StateVector gpiStates;
    private StateVector gmuStates;
    private StateVector gmStates;
    private StateVector goStates;
    private StateVector qbeStates;
    private StateVector cqbeStates;
    private StateVector qbcStates;
    private StateVector cqbcStates;
    private StateVector qcsStates;
    private StateVector cqcsStates;
    private StateVector qbxStates;
    private StateVector cqbxStates;
    private StateVector gxStates;
    private StateVector cexbcStates;
    private StateVector geqcbStates;
    private StateVector gccsStates;
    private StateVector geqbxStates;
    Real gccs = Real.zero();
    Real gEqbx = Real.zero();
    Real gEq = Real.zero();
    Real iEq = Real.zero();
    Bool icheck = Bool.FALSE();
    Bool ichk1 = Bool.TRUE();

    /** Creates a new instance of bjt */
    public BJT1Instance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        if (collectorResist == 0) {
            colPrmIndex = colIndex;
        } else if (colPrmIndex == 0) {
            colPrmIndex = ckt.makeVoltNode(instName + "#collector").getIndex();
        }
        if (baseResist == 0) {
            basePrmIndex = baseIndex;
        } else if (basePrmIndex == 0) {
            basePrmIndex = ckt.makeVoltNode(instName + "#base").getIndex();
        }
        if (emitterResist == 0) {
            emitPrmIndex = emitIndex;
        } else if (emitPrmIndex == 0) {
            emitPrmIndex = ckt.makeVoltNode(instName + "#emitter").getIndex();
        }
        if (leakBEcurrent == 0 && c2 != 0) {
            leakBEcurrent = c2 * satCur;
        }
        if (leakBCcurrent == 0 && c4 != 0) {
            leakBCcurrent = c4 * satCur;
        }
        if (minBaseResist == 0) {
            minBaseResist = baseResist;
        }
        if (earlyVoltF != 0) {
            invEarlyVoltF = 1 / earlyVoltF;
        }
        if (rollOffF != 0) {
            invRollOffF = 1 / rollOffF;
        }
        if (earlyVoltR != 0) {
            invEarlyVoltR = 1 / earlyVoltR;
        }
        if (rollOffR != 0) {
            invRollOffR = 1 / rollOffR;
        }
        if (collectorResist != 0) {
            collectorConduct = 1 / collectorResist;
        }
        if (emitterResist != 0) {
            emitterConduct = 1 / emitterResist;
        }
        if (transitTimeFVBC != 0) {
            transitTimeVBCFactor = 1 / (transitTimeFVBC * 1.44);
        }
        excessPhaseFactor = (excessPhase / (180 / MathLib.PI)) * transitTimeF;
        if (depletionCapCoeff != 0) {
            if (depletionCapCoeff > 0.9999) {
                depletionCapCoeff = 0.9999;
                StandardLog.warning(" model " + getModelName()
                        + " parameter fc limited to 0.9999");
            }
        } else {
            depletionCapCoeff = 0.5;
        }
        colColPrmNode = wrk.aquireNode(colIndex, colPrmIndex);
        baseBasePrmNode = wrk.aquireNode(baseIndex, basePrmIndex);
        emitEmitPrmNode = wrk.aquireNode(emitIndex, emitPrmIndex);
        colPrmColNode = wrk.aquireNode(colPrmIndex, colIndex);
        colPrmBasePrmNode = wrk.aquireNode(colPrmIndex, basePrmIndex);
        colPrmEmitPrmNode = wrk.aquireNode(colPrmIndex, emitPrmIndex);
        basePrmBaseNode = wrk.aquireNode(basePrmIndex, baseIndex);
        basePrmColPrmNode = wrk.aquireNode(basePrmIndex, colPrmIndex);
        basePrmEmitPrmNode = wrk.aquireNode(basePrmIndex, emitPrmIndex);
        emitPrmEmitNode = wrk.aquireNode(emitPrmIndex, emitIndex);
        emitPrmColPrmNode = wrk.aquireNode(emitPrmIndex, colPrmIndex);
        emitPrmBasePrmNode = wrk.aquireNode(emitPrmIndex, basePrmIndex);
        colColNode = wrk.aquireNode(colIndex, colIndex);
        baseBaseNode = wrk.aquireNode(baseIndex, baseIndex);
        emitEmitNode = wrk.aquireNode(emitIndex, emitIndex);
        colPrmColPrmNode = wrk.aquireNode(colPrmIndex, colPrmIndex);
        basePrmBasePrmNode = wrk.aquireNode(basePrmIndex, basePrmIndex);
        emitPrmEmitPrmNode = wrk.aquireNode(emitPrmIndex, emitPrmIndex);
        substSubstNode = wrk.aquireNode(substIndex, substIndex);
        colPrmSubstNode = wrk.aquireNode(colPrmIndex, substIndex);
        substColPrmNode = wrk.aquireNode(substIndex, colPrmIndex);
        baseColPrmNode = wrk.aquireNode(baseIndex, colPrmIndex);
        colPrmBaseNode = wrk.aquireNode(colPrmIndex, baseIndex);

        vbeStates = stateTable.createRow();
        vbcStates = stateTable.createRow();
        ccStates = stateTable.createRow();
        cbStates = stateTable.createRow();
        gpiStates = stateTable.createRow();
        gmuStates = stateTable.createRow();
        gmStates = stateTable.createRow();
        goStates = stateTable.createRow();
        qbeStates = stateTable.createRow();
        cqbeStates = stateTable.createRow();
        qbcStates = stateTable.createRow();
        cqbcStates = stateTable.createRow();
        qcsStates = stateTable.createRow();
        cqcsStates = stateTable.createRow();
        qbxStates = stateTable.createRow();
        cqbxStates = stateTable.createRow();
        gxStates = stateTable.createRow();
        cexbcStates = stateTable.createRow();
        geqcbStates = stateTable.createRow();
        gccsStates = stateTable.createRow();
        geqbxStates = stateTable.createRow();
        return true;
    }

    public boolean unSetup() {
        if (colPrmIndex != colIndex) {
            ckt.deleteNode(colPrmIndex);
            colPrmIndex = 0;
        }
        if (basePrmIndex != baseIndex) {
            ckt.deleteNode(basePrmIndex);
            basePrmIndex = 0;
        }
        if (emitPrmIndex != emitIndex) {
            ckt.deleteNode(emitPrmIndex);
            emitPrmIndex = 0;
        }
        return true;
    }

    public boolean load(Mode mode) {
        double gm = 0;
        double gmu = 0;
        double go = 0;
        double gpi = 0;
        double gx = 0;
        double vbc = 0;
        double vbe = 0;
        double vbx = 0;
        double vce = 0;
        double vcs = 0;
        double cb = 0;
        double cc = 0;
        double vt = this.temp * KoverQ;
        double geqcb = 0;
        double csat = tSatCur * area;
        double gcpr = collectorConduct * area;
        double gepr = emitterConduct * area;
        boolean bypass = false;
        icheck.setFalse();
        if (mode.contains(MODE.INIT_SMSIG)) {
            vbe = vbeStates.get(0);
            vbc = vbcStates.get(0);
            vbx = type * (wrk.getRhsOldRealAt(baseIndex)
                    - wrk.getRhsOldRealAt(colPrmIndex));
            vcs = type * (wrk.getRhsOldRealAt(substIndex)
                    - wrk.getRhsOldRealAt(colPrmIndex));
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vbe = vbeStates.get(0);
            vbc = vbcStates.get(0);
            vbx = type * (wrk.getRhsOldRealAt(baseIndex)
                    - wrk.getRhsOldRealAt(colPrmIndex));
            vcs = type * (wrk.getRhsOldRealAt(substIndex)
                    - wrk.getRhsOldRealAt(colPrmIndex));
            if (mode.contains(MODE.TRAN) && mode.isUseIC()) {
                vbx = type * (icVBE - icVCE);
                vcs = 0;
            }
        } else if (mode.contains(MODE.INIT_JCT)
                && mode.contains(MODE.TRANOP) && mode.isUseIC()) {
            vbe = type * icVBE;
            vce = type * icVCE;
            vbc = vbe - vce;
            vbx = vbc;
        } else if (mode.contains(MODE.INIT_JCT) && !off) {
            vbe = tVcrit;
        } else if (mode.contains(MODE.INIT_JCT)
                || (mode.contains(MODE.INIT_FIX) && off)) {
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(0);
                vbeStates.set(0, vbeStates.get(0));
                vbe = (1 + xfact) * vbeStates.get(0)
                        - xfact * vbeStates.get(2);
                vbcStates.set(0, vbcStates.get(0));
                vbc = (1 + xfact) * vbcStates.get(0)
                        - xfact * vbcStates.get(2);
                ccStates.set(0, ccStates.get(0));
                cbStates.set(0, cbStates.get(0));
                gpiStates.set(0, gpiStates.get(0));
                gmuStates.set(0, gmuStates.get(0));
                gmStates.set(0, gmStates.get(0));
                goStates.set(0, goStates.get(0));
                gxStates.set(0, gxStates.get(0));
            } else {
                vbe = type * (wrk.getRhsOldRealAt(basePrmIndex)
                        - wrk.getRhsOldRealAt(emitPrmIndex));
                vbc = type * (wrk.getRhsOldRealAt(basePrmIndex)
                        - wrk.getRhsOldRealAt(colPrmIndex));
            }
            double delvbe = vbe - vbeStates.get(0);
            double delvbc = vbc - vbcStates.get(0);
            vbx = type * (wrk.getRhsOldRealAt(baseIndex) - wrk.getRhsOldRealAt(colPrmIndex));
            vcs = type * (wrk.getRhsOldRealAt(substIndex) - wrk.getRhsOldRealAt(colPrmIndex));
            double cchat = ccStates.get(0) + (gmStates.get(0) + goStates.get(0)) * delvbe
                    - (goStates.get(0) + gmuStates.get(0)) * delvbc;
            double cbhat = cbStates.get(0) + gpiStates.get(0) * delvbe
                    + gmuStates.get(0) * delvbc;
            if (env.isBypass() && (!(mode.contains(MODE.INIT_PRED)))
                    && (MathLib.abs(delvbe) < (env.getRelTol() * MathLib.max(MathLib.abs(vbe),
                    MathLib.abs(vbeStates.get(0))) + env.getVoltTol()))) {
                if ((MathLib.abs(delvbc) < env.getRelTol() * MathLib.max(MathLib.abs(vbc),
                        MathLib.abs(vbcStates.get(0)))
                        + env.getVoltTol())) {
                    if ((MathLib.abs(cchat - ccStates.get(0))
                            < env.getRelTol() * MathLib.max(MathLib.abs(cchat),
                            MathLib.abs(ccStates.get(0)))
                            + env.getAbsTol())) {
                        if ((MathLib.abs(cbhat - cbStates.get(0))
                                < env.getRelTol() * MathLib.max(MathLib.abs(cbhat),
                                MathLib.abs(cbStates.get(0)))
                                + env.getAbsTol())) {
                            vbe = vbeStates.get(0);
                            vbc = vbcStates.get(0);
                            cc = ccStates.get(0);
                            cb = cbStates.get(0);
                            gpi = gpiStates.get(0);
                            gmu = gmuStates.get(0);
                            gm = gmStates.get(0);
                            go = goStates.get(0);
                            gx = gxStates.get(0);
                            geqcb = geqcbStates.get(0);
                            gccs.set(gccsStates.get(0));
                            gEqbx.set(geqbxStates.get(0));
                            bypass = true;
                        }
                    }
                }
            }
            if (!bypass) {
                // Limit nonlinear branch voltages
                ichk1.setFalse();
                vbe = MOSUtils.pnjLimit(vbe, vbeStates.get(0), vt, tVcrit, icheck);
                vbc = MOSUtils.pnjLimit(vbc, vbcStates.get(0), vt, tVcrit, ichk1);
                if (ichk1.isFalse()) {
                    icheck.setFalse();
                }
            }
        }
        if (!bypass) {
            // Determine dc current and derivitives
            double arg;
            double cbc;
            double cbcn;
            double cbe;
            double cben;
            double gbc;
            double gbcn;
            double gbe;
            double gben;
            double vtn = vt * emissionCoeffF;
            if (vbe >= -3 * vtn) {
                double evbe = MathLib.exp(vbe / vtn);
                cbe = csat * (evbe - 1);
                gbe = csat * evbe / vtn;
            } else {
                arg = 3 * vtn / (vbe * MathLib.E);
                arg = arg * arg * arg;
                cbe = -csat * (1 + arg);
                gbe = csat * 3 * arg / vbe;
            }
            double vte = leakBEemissionCoeff * vt;
            double _c2 = tBEleakCur * area;
            if (_c2 == 0) {
                cben = 0;
                gben = 0;
            } else {
                if (vbe >= -3 * vte) {
                    double evben = MathLib.exp(vbe / vte);
                    cben = _c2 * (evben - 1);
                    gben = _c2 * evben / vte;
                } else {
                    arg = 3 * vte / (vbe * MathLib.E);
                    arg = arg * arg * arg;
                    cben = -_c2 * (1 + arg);
                    gben = _c2 * 3 * arg / vbe;
                }
            }
            gben += env.getGMin();
            cben += env.getGMin() * vbe;
            vtn = vt * emissionCoeffR;
            if (vbc >= -3 * vtn) {
                double evbc = MathLib.exp(vbc / vtn);
                cbc = csat * (evbc - 1);
                gbc = csat * evbc / vtn;
            } else {
                arg = 3 * vtn / (vbc * MathLib.E);
                arg = arg * arg * arg;
                cbc = -csat * (1 + arg);
                gbc = csat * 3 * arg / vbc;
            }
            double vtc = leakBCemissionCoeff * vt;
            double _c4 = tBCleakCur * areab;
            if (_c4 == 0) {
                cbcn = 0;
                gbcn = 0;
            } else {
                if (vbc >= -3 * vtc) {
                    double evbcn = MathLib.exp(vbc / vtc);
                    cbcn = _c4 * (evbcn - 1);
                    gbcn = _c4 * evbcn / vtc;
                } else {
                    arg = 3 * vtc / (vbc * MathLib.E);
                    arg = arg * arg * arg;
                    cbcn = -_c4 * (1 + arg);
                    gbcn = _c4 * 3 * arg / vbc;
                }
            }
            gbcn += env.getGMin();
            cbcn += env.getGMin() * vbc;
            // Determine base charge terms
            double q2;
            double qb;
            double sqarg;
            double q1 = 1 / (1 - invEarlyVoltF * vbc - invEarlyVoltR * vbe);
            double dqbdvc;
            double dqbdve;
            double oikr = invRollOffR / area;
            double oik = invRollOffF / area;
            if (oik == 0 && oikr == 0) {
                qb = q1;
                dqbdve = q1 * qb * invEarlyVoltR;
                dqbdvc = q1 * qb * invEarlyVoltF;
            } else {
                q2 = oik * cbe + oikr * cbc;
                arg = MathLib.max(0, 1 + 4 * q2);
                sqarg = 1;
                if (arg != 0) {
                    sqarg = MathLib.sqrt(arg);
                }
                qb = q1 * (1 * sqarg) / 2;
                dqbdve = q1 * (qb * invEarlyVoltR + oik * gbe / sqarg);
                dqbdvc = q1 * (qb * invEarlyVoltF + oikr * gbc / sqarg);
            }
            // Weil's approx. for excess phase applied with backward-Euler integration
            double arg1;
            double arg2;
            double arg3;
            double cex = cbe;
            double gex = gbe;
            double td = excessPhaseFactor;
            if (mode.contains(MODE.TRAN, MODE.AC) && td != 0) {
                arg1 = tmprl.getDelta() / td;
                arg2 = 3 * arg1;
                arg1 = arg2 * arg1;
                double denom = 1 + arg1 + arg2;
                arg3 = arg1 / denom;
                if (mode.contains(MODE.INIT_TRAN)) {
                    cexbcStates.set(1, cbe / qb);
                    cexbcStates.set(2, cexbcStates.get(0));
                }
                cc = (cexbcStates.get(0) * (1 + tmprl.getDelta()
                        / tmprl.getOldDeltaAt(0) + arg2)
                        - cexbcStates.get(2) * tmprl.getDelta()
                        / tmprl.getOldDeltaAt(0)) / denom;
                cex = cbe * arg3;
                gex = gbe * arg3;
                cexbcStates.set(0, cc + cex / qb);
            }
            // Determine dc incremental conductances
            cc = cc + (cex - cbc) / qb - cbc / tBetaR - cbcn;
            cb = cbe / tBetaF + cben + cbc / tBetaR + cbcn;
            double rbpr = minBaseResist / area;
            double rbpi = baseResist / area - rbpr;
            gx = rbpr + rbpi / qb;
            double xjrb = baseCurrentHalfResist * area;
            if (xjrb != 0) {
                arg1 = MathLib.max(cb / xjrb, 1e-9);
                arg2 = (-1 + MathLib.sqrt(1 + 14.59025 * arg1)) / 2.4317 / MathLib.sqrt(arg1);
                arg1 = MathLib.tan(arg2);
                gx = rbpr + 3 * rbpi * (arg1 - arg2) / arg2 / arg1 / arg1;
            }
            if (gx != 0) {
                gx = 1 / gx;
            }
            gpi = gbe / tBetaF + gben;
            gmu = gbc / tBetaR + gbcn;
            go = (gbc + (cex - cbc) * dqbdvc / qb) / qb;
            gm = (gex - (cex - cbc) * dqbdve / qb) / qb - go;
            if (mode.contains(MODE.TRAN, MODE.AC)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())
                    || mode.contains(MODE.INIT_SMSIG)) {
                // Charge storage elements
                double argtf;
                double xtf = transitTimeBiasCoeffF;
                double ovtf = transitTimeVBCFactor;
                double xjtf = transitTimeHighCurrentF * area;
                double tf = transitTimeF;
                if (tf != 0 && vbe > 0) {
                    argtf = 0;
                    arg2 = 0;
                    arg3 = 0;
                    if (xtf != 0) {
                        argtf = xtf;
                        if (ovtf != 0) {
                            argtf = argtf * MathLib.exp(vbc * ovtf);
                        }
                        arg2 = argtf;
                        if (xjtf != 0) {
                            double _temp = cbe / (cbe + xjtf);
                            argtf = argtf * _temp * _temp;
                            arg2 = argtf * (3 - _temp - _temp);
                        }
                        arg3 = cbe * argtf * ovtf;
                    }
                    cbe = cbe * (1 + argtf) / qb;
                    gbe = (gbe * (1 + arg2) - cbe * dqbdve) / qb;
                    geqcb = tf * (arg3 - cbe * dqbdvc) / qb;
                }
                double sarg;
                double _f1;
                double _f2;
                double _f3;
                double czbe = tBEcap * area;
                double pe = tBEpot;
                double xme = junctionExpBE;
                double fcpe = tDepCap;
                double czcs = capCS * areac;
                if (vbe < fcpe) {
                    arg = 1 - vbe / pe;
                    sarg = MathLib.exp(-xme * MathLib.log(arg));
                    qbeStates.set(0, tf * cbe + pe * czbe
                            * (1 - arg * sarg) / (1 - xme));
                    capbe = tf * gbe + czbe * sarg;
                } else {
                    _f1 = tf1;
                    _f2 = this.f2;
                    _f3 = this.f3;
                    double czbef2 = czbe / _f2;
                    qbeStates.set(0, tf * cbe + czbe * _f1 + czbef2
                            * (_f3 * (vbe - fcpe) + (xme / (pe + pe)) * (vbe * vbe - fcpe * fcpe)));
                    capbe = tf * gbe + czbef2 * (_f3 + xme * vbe / pe);
                }
                double fcpc = tf4;
                _f1 = tf5;
                _f2 = f6;
                _f3 = f7;
                double tr = transitTimeR;
                double pc = tBCpot;
                double xmc = junctionExpBC;
                double cdis = baseFractionBCcap;
                double ctot = tBCcap * area;
                double czbc = ctot * cdis;
                double czbx = ctot - czbc;
                if (vbc < fcpc) {
                    arg = 1 - vbc / pc;
                    sarg = MathLib.exp(-xmc * MathLib.log(arg));
                    qbcStates.set(0, tr * cbc + pc * czbc * (1 - arg * sarg) / (1 - xmc));
                    capbc = tr * gbc + czbc * sarg;
                } else {
                    double czbcf2 = czbc / _f2;
                    qbcStates.set(0, tr * cbc + czbc * _f1 + czbcf2
                            * (_f3 * (vbc - fcpc) + (xmc / (pc + pc)) * (vbc * vbc - fcpc * fcpc)));
                    capbc = tr * gbc + czbcf2 * (_f3 + xmc * vbc / pc);
                }
                if (vbx < fcpc) {
                    arg = 1 - vbx / pc;
                    sarg = MathLib.exp(-xmc * MathLib.log(arg));
                    qbxStates.set(0, pc * czbx * (1 - arg * sarg) / (1 - xmc));
                    capbx = czbx * sarg;
                } else {
                    double czbxf2 = czbx / _f2;
                    qbxStates.set(0, czbx * _f1 + czbxf2
                            * (_f3 * (vbx - fcpc) + (xmc / (pc + pc)) * (vbx * vbx - fcpc * fcpc)));
                    capbx = czbxf2 * (_f3 + xmc * vbx / pc);
                }
                double ps = potentialSubstrate;
                double xms = exponentialSubstrate;
                if (vcs < 0) {
                    arg = 1 - vcs / ps;
                    sarg = MathLib.exp(-xms * MathLib.log(arg));
                    qcsStates.set(0, ps * czcs * (1 - arg * sarg)
                            / (1 - xms));
                    capcs = czcs * sarg;
                } else {
                    qcsStates.set(0, vcs * czcs * (1 + xms * vcs
                            / (2 * ps)));
                    capcs = czcs * (1 + xms * vcs / ps);
                }
                // Store small - signal parameters
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        cqbeStates.set(0, capbe);
                        cqbcStates.set(0, capbc);
                        cqcsStates.set(0, capcs);
                        cqbxStates.set(0, capbx);
                        cexbcStates.set(0, geqcb);
                        return true;
                    }
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qbeStates.set(1, qbeStates.get(0));
                        qbcStates.set(1, qbcStates.get(0));
                        qbxStates.set(1, qbxStates.get(0));
                        qcsStates.set(1, qcsStates.get(0));
                    }
                    stateTable.integrate(gEq, iEq, capbe, qbeStates, cqbeStates);
                    geqcb = geqcb * tmprl.getAgAt(0);
                    gpi = gpi + gEq.get();
                    cb = cb + cqbeStates.get(0);
                    if (!stateTable.integrate(gEq, iEq, capbc, qbcStates, cqbcStates)) {
                        return false;
                    }
                    gmu = gmu + gEq.get();
                    cb = cb + cqbcStates.get(0);
                    cc = cc - cqbcStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqbeStates.set(1, cqbeStates.get(0));
                        cqbcStates.set(1, cqbcStates.get(0));
                    }
                }
            }
            if ((!mode.contains(MODE.INIT_FIX) || !off) && icheck.isFalse()) {
                ckt.setNonConverged(true);
            }
            // Charge storage for C - S and B - X junctions
            if (mode.contains(MODE.TRAN, MODE.AC)) {
                if (!stateTable.integrate(gccs, iEq, capcs, qcsStates, cqcsStates)) {
                    return false;
                }
                if (!stateTable.integrate(gEqbx, iEq, capbx, qbxStates, cqbxStates)) {
                    return false;
                }
                if (mode.contains(MODE.INIT_TRAN)) {
                    cqbxStates.set(1, cqbxStates.get(0));
                    cqcsStates.set(1, cqcsStates.get(0));
                }
            }
            vbeStates.set(0, vbe);
            vbcStates.set(0, vbc);
            ccStates.set(0, cc);
            cbStates.set(0, cb);
            gpiStates.set(0, gpi);
            gmuStates.set(0, gmu);
            gmStates.set(0, gm);
            goStates.set(0, go);
            gxStates.set(0, gx);
            geqcbStates.set(0, geqcb);
            gccsStates.set(0, gccs.get());
            geqbxStates.set(0, gEqbx.get());
        }
        // Load current excitation vector
        double ceqcs = type * (cqcsStates.get(0) - vcs * gccs.get());
        double ceqbx = type * (cqbxStates.get(0) - vbx * gEqbx.get());
        double ceqbe = type * (cc + cb - vbe * (gm + go + gpi) + vbc * (go - geqcb));
        double ceqbc = type * (-cc + vbe * (gm + go) - vbc * (gmu + go));
        wrk.getRhsAt(baseIndex).realPlusEq(m * -ceqbx);
        wrk.getRhsAt(colPrmIndex).realPlusEq(m * (ceqcs + ceqbx + ceqbc));
        wrk.getRhsAt(basePrmIndex).realPlusEq(m * (-ceqbe - ceqbc));
        wrk.getRhsAt(emitPrmIndex).realPlusEq(m * ceqbe);
        wrk.getRhsAt(substIndex).realPlusEq(m * -ceqcs);
        // Load y matrix
        colColNode.realPlusEq(m * gcpr);
        baseBaseNode.realPlusEq(m * (gx + gEqbx.get()));
        emitEmitNode.realPlusEq(m * gepr);
        colPrmColPrmNode.realPlusEq(m * (gmu + go + gcpr + gccs.get() + gEqbx.get()));
        basePrmBasePrmNode.realPlusEq(m * (gx + gpi + gmu + geqcb));
        emitPrmEmitPrmNode.realPlusEq(m * (gpi + gepr + gm + go));
        colColPrmNode.realPlusEq(m * -gcpr);
        baseBasePrmNode.realPlusEq(m * -gx);
        emitEmitPrmNode.realPlusEq(m * -gepr);
        colPrmColNode.realPlusEq(m * -gcpr);
        colPrmBasePrmNode.realPlusEq(m * (-gmu + gm));
        colPrmEmitPrmNode.realPlusEq(m * (-gm - go));
        basePrmBaseNode.realPlusEq(m * -gx);
        basePrmColPrmNode.realPlusEq(m * (-gmu - geqcb));
        basePrmEmitPrmNode.realPlusEq(m * -gpi);
        emitPrmEmitNode.realPlusEq(m * -gepr);
        emitPrmColPrmNode.realPlusEq(m * (-go + geqcb));
        emitPrmBasePrmNode.realPlusEq(m * (-gpi - gm - geqcb));
        substSubstNode.realPlusEq(m * gccs.get());
        colPrmSubstNode.realPlusEq(m * -gccs.get());
        substColPrmNode.realPlusEq(m * -gccs.get());
        baseColPrmNode.realPlusEq(m * -gEqbx.get());
        colPrmBaseNode.realPlusEq(m * -gEqbx.get());
        return true;
    }

    public boolean acLoad(Mode mode) {
        double arg;
        double gcpr = collectorConduct * area;
        double gepr = emitterConduct * area;
        double gpi = gpiStates.get(0);
        double gmu = gmuStates.get(0);
        double gm = gmStates.get(0);
        double go = goStates.get(0);
        double xgm = 0;
        double td = excessPhaseFactor;
        if (td != 0) {
            arg = td * tmprl.getOmega();
            gm = gm + go;
            xgm = -gm * MathLib.sin(arg);
            gm = gm * MathLib.cos(arg) - go;
        }
        double gx = gxStates.get(0);
        double xcpi = cqbeStates.get(0) * tmprl.getOmega();
        double xcmu = cqbcStates.get(0) * tmprl.getOmega();
        double xcbx = cqbxStates.get(0) * tmprl.getOmega();
        double xccs = cqcsStates.get(0) * tmprl.getOmega();
        double xcmcb = cexbcStates.get(0) * tmprl.getOmega();
        colColNode.realPlusEq(m * (gcpr));
        baseBaseNode.realPlusEq(m * (gx));
        baseBaseNode.imagPlusEq(m * (xcbx));
        emitEmitNode.realPlusEq(m * (gepr));
        colPrmColPrmNode.realPlusEq(m * (gmu + go + gcpr));
        colPrmColPrmNode.imagPlusEq(m * (xcmu + xccs + xcbx));
        basePrmBasePrmNode.realPlusEq(m * (gx + gpi + gmu));
        basePrmBasePrmNode.imagPlusEq(m * (xcpi + xcmu + xcmcb));
        emitPrmEmitPrmNode.realPlusEq(m * (gpi + gepr + gm + go));
        emitPrmEmitPrmNode.imagPlusEq(m * (xcpi + xgm));
        colColPrmNode.realPlusEq(m * (-gcpr));
        baseBasePrmNode.realPlusEq(m * (-gx));
        emitEmitPrmNode.realPlusEq(m * (-gepr));
        colPrmColNode.realPlusEq(m * (-gcpr));
        colPrmBasePrmNode.realPlusEq(m * (-gmu + gm));
        colPrmBasePrmNode.imagPlusEq(m * (-xcmu + xgm));
        colPrmEmitPrmNode.realPlusEq(m * (-gm - go));
        colPrmEmitPrmNode.imagPlusEq(m * (-xgm));
        basePrmBaseNode.realPlusEq(m * (-gx));
        basePrmColPrmNode.realPlusEq(m * (-gmu));
        basePrmColPrmNode.imagPlusEq(m * (-xcmu - xcmcb));
        basePrmEmitPrmNode.realPlusEq(m * (-gpi));
        basePrmEmitPrmNode.imagPlusEq(m * (-xcpi));
        emitPrmEmitNode.realPlusEq(m * (-gepr));
        emitPrmColPrmNode.realPlusEq(m * (-go));
        emitPrmColPrmNode.imagPlusEq(m * (xcmcb));
        emitPrmBasePrmNode.realPlusEq(m * (-gpi - gm));
        emitPrmBasePrmNode.imagPlusEq(m * (-xcpi - xgm - xcmcb));
        substSubstNode.imagPlusEq(m * (xccs));
        colPrmSubstNode.imagPlusEq(m * (-xccs));
        substColPrmNode.imagPlusEq(m * (-xccs));
        baseColPrmNode.imagPlusEq(m * (-xcbx));
        colPrmBaseNode.imagPlusEq(m * (-xcbx));
        return true;
    }

    public boolean convTest(Mode mode) {
        double vbe = type * (wrk.getRhsOldRealAt(basePrmIndex)
                - wrk.getRhsOldRealAt(emitPrmIndex));
        double vbc = type * (wrk.getRhsOldRealAt(basePrmIndex)
                - wrk.getRhsOldRealAt(colPrmIndex));
        double delvbe = vbe - vbeStates.get(0);
        double delvbc = vbc - vbcStates.get(0);
        double cchat = ccStates.get(0) + (gmStates.get(0) + goStates.get(0)) * delvbe
                - (goStates.get(0) + gmuStates.get(0)) * delvbc;
        double cbhat = cbStates.get(0) + gpiStates.get(0) * delvbe + gmuStates.get(0)
                * delvbc;
        double cc = ccStates.get(0);
        double cb = cbStates.get(0);
        double tol = env.getRelTol() * MathLib.max(MathLib.abs(cchat), MathLib.abs(cc)) + env.getAbsTol();
        if (MathLib.abs(cchat - cc) > tol) {
            return false;
        }
        tol = env.getRelTol() * MathLib.max(MathLib.abs(cbhat), MathLib.abs(cb))
                + env.getAbsTol();
        if (MathLib.abs(cbhat - cb) > tol) {
            return false;
        }
        return true;
    }

    public boolean loadInitCond() {
        if (icVBE == 0) {
            icVBE = wrk.getRhsAt(baseIndex).minus(wrk.getRhsAt(emitIndex)).getReal();
        }
        if (icVCE == 0) {
            icVCE = wrk.getRhsAt(colIndex).minus(wrk.getRhsAt(emitIndex)).getReal();
        }
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        timeStep = stateTable.terr(qbeStates, cqbeStates, timeStep);
        timeStep = stateTable.terr(qbcStates, cqbcStates, timeStep);
        return stateTable.terr(qcsStates, cqcsStates, timeStep);
    }

    public boolean temperature() {
        if (tnom == 0) {
            tnom = env.getNomTemp();
        }
        double fact1 = tnom / REF_TEMP;
        double xfc = MathLib.log(1 - depletionCapCoeff);
        f2 = MathLib.exp((1 + junctionExpBE) * xfc);
        f3 = 1 - depletionCapCoeff * (1 + junctionExpBE);
        f6 = MathLib.exp((1 + junctionExpBC) * xfc);
        f7 = 1 - depletionCapCoeff * (1 + junctionExpBC);
        if (dtemp == 0) {
            temp = env.getTemp() + dtemp;
        }
        double vt = temp * KoverQ;
        double fact2 = temp / REF_TEMP;
        double egfet = 1.16 - (7.02e-4 * temp * temp)
                / (temp + 1108);
        double arg = -egfet / (2 * BOLTZMANN * temp)
                + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact = -2 * vt * (1.5 * MathLib.log(fact2) + CHARGE * arg);
        double ratlog = MathLib.log(temp / tnom);
        double ratio1 = temp / tnom - 1;
        double factlog = ratio1 * energyGap / vt
                + tempExpIS * ratlog;
        double factor = MathLib.exp(factlog);
        tSatCur = satCur * factor;
        double bfactor = MathLib.exp(ratlog * betaExp);
        tBetaF = betaF * bfactor;
        tBetaR = betaR * bfactor;
        tBEleakCur = leakBEcurrent
                * MathLib.exp(factlog / leakBEemissionCoeff) / bfactor;
        tBCleakCur = leakBCcurrent
                * MathLib.exp(factlog / leakBCemissionCoeff) / bfactor;
        double pbo = (potentialBE - pbfact) / fact1;
        double gmaold = (potentialBE - pbo) / pbo;
        tBEcap = depletionCapBE
                / (1 + junctionExpBE
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tBEpot = fact2 * pbo + pbfact;
        double gmanew = (tBEpot - pbo) / pbo;
        tBEcap *= 1 + junctionExpBE
                * (4e-4 * (temp - REF_TEMP) - gmanew);
        pbo = (potentialBC - pbfact) / fact1;
        gmaold = (potentialBC - pbo) / pbo;
        tBCcap = depletionCapBC
                / (1 + junctionExpBC
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tBCpot = fact2 * pbo + pbfact;
        gmanew = (tBCpot - pbo) / pbo;
        tBCcap *= 1 + junctionExpBC
                * (4e-4 * (temp - REF_TEMP) - gmanew);
        tDepCap = depletionCapCoeff * tBEpot;
        tf1 = tBEpot * (1 - MathLib.exp((1
                - junctionExpBE) * xfc))
                / (1 - junctionExpBE);
        tf4 = depletionCapCoeff * tBCpot;
        tf5 = tBCpot * (1 - MathLib.exp((1
                - junctionExpBC) * xfc))
                / (1 - junctionExpBC);
        tVcrit = vt * MathLib.log(vt / (ROOT2 * tSatCur * area));
        return true;
    }

    public boolean accept(Mode mode) {
        return true;
    }
}
