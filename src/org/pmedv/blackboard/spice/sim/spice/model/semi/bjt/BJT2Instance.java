/*
 * bjt2.java
 *
 * Created on August 28, 2006, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.bjt;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;
import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

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
public class BJT2Instance extends BJT2ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Inverse of earlyVoltF */
    private double invEarlyVoltF;

    /* Inverse of earlyVoltR */
    private double invEarlyVoltR;

    /* Inverse of rollOffF */
    private double invRollOffF;

    /* Inverse of rollOffR */
    private double invRollOffR;

    /* Collector conductance */
    private double collectorConduct;

    /* Emitter conductance */
    private double emitterConduct;

    /* Internal collector node index */
    private int colPrmIndex;

    /* Internal base node index */
    private int basePrmIndex;

    /* Internal emitter node index */
    private int emitPrmIndex;

    /* Temperature corrected saturation current */
    private double tSatCur;

    /* Temperature corrected forward beta */
    private double tBetaF;

    /* Temperature corrected reverse beta */
    private double tBetaR;

    /* Temperature corrected B - E leakage current */
    private double tBEleakCur;

    /* Temperature corrected B - C leakage current */
    private double tBCleakCur;

    /* Temperature corrected B - E capacitance */
    private double tBEcap;

    /* Temperature corrected B - E potential */
    private double tBEpot;

    /* Temperature corrected B - C capacitance */
    private double tBCcap;

    /* Temperature corrected B - C potential */
    private double tBCpot;

    /* Temperature corrected join point in diode curve */
    private double tDepCap;

    /* Temperature corrected polynomial coefficient */
    private double tf1;

    /* Temperature corrected polynomial coefficient */
    private double tf4;

    /* Temperature corrected polynomial coefficient */
    private double tf5;

    /* Temperature corrected critical voltage */
    private double tVcrit;
    private double transitTimeVBCFactor;
    private double excessPhaseFactor;
    private double c2;
    private double c4;
    private double capbe;
    private double capbc;
    private double capcs;
    private double capbx;
    private double f2;
    private double f3;
    private double f6;
    private double f7;
    // BJT2 Only
    /**
     * Index of node which substrate is connected to
     * Substrate connection is either base prime
     * or collector prime depending on whether       
     * the device is VERTICAL or LATERAL
     */
    private int substConIndex;

    /* Temperature corrected substrate saturation current */
    private double tSubSatCur;

    /* Emitter conductance */
    private double tEmitterConduct;

    /* Collector conductance */
    private double tCollectorConduct;

    /* Temperature corrected base resistance */
    private double tBaseResist;

    /* Temperature corrected base resistance */
    private double tMinBaseResist;

    /* Temperature corrected Substrate capacitance */
    private double tSubcap;

    /* Temperature corrected Substrate potential */
    private double tSubpot;

    /* Temperature corrected substrate critical voltage */
    private double tSubVcrit;
    private double capsub;
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
    private Complex substConSubstNode;
    private Complex substSubstConNode;
    private Complex substConSubstConNode;
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
    private StateVector qsubStates;
    private StateVector cqsubStates;
    private StateVector qbxStates;
    private StateVector cqbxStates;
    private StateVector gxStates;
    private StateVector cexbcStates;
    private StateVector geqcbStates;
    private StateVector gcsubStates;
    private StateVector geqbxStates;
    private StateVector vsubStates;
    private StateVector cdsubStates;
    private StateVector gdsubStates;

    /** Creates a new instance of bjt2 */
    public BJT2Instance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        if (subs == 0 || (subs != VERTICAL && subs != LATERAL)) {
            if (type == N_TYPE) {
                subs = VERTICAL;
            } else {
                subs = LATERAL;
            }
        }
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
        qsubStates = stateTable.createRow();
        cqsubStates = stateTable.createRow();
        qbxStates = stateTable.createRow();
        cqbxStates = stateTable.createRow();
        gxStates = stateTable.createRow();
        cexbcStates = stateTable.createRow();
        geqcbStates = stateTable.createRow();
        gcsubStates = stateTable.createRow();
        geqbxStates = stateTable.createRow();
        vsubStates = stateTable.createRow();
        cdsubStates = stateTable.createRow();
        gdsubStates = stateTable.createRow();
        if (areab == 0) {
            areab = area;
        }
        if (areac == 0) {
            areac = area;
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
        excessPhaseFactor = (excessPhase
                / (180 / MathLib.PI)) * transitTimeF;
        if (depletionCapCoeff != 0) {
            if (depletionCapCoeff > 0.9999) {
                depletionCapCoeff = 0.9999;
                StandardLog.warning("Front: BJT2 model " + getModelName()
                        + ", parameter fc limited to 0.9999");
            }
        } else {
            depletionCapCoeff = 0.5;
        }
        if (collectorResist == 0) {
            colPrmIndex = colIndex;
        } else if (colPrmIndex == 0) {
            colPrmIndex = ckt.makeVoltNode(getInstName() + "collector").getIndex();
        }
        if (baseResist == 0) {
            basePrmIndex = baseIndex;
        } else if (basePrmIndex == 0) {
            basePrmIndex = ckt.makeVoltNode(getInstName() + "base").getIndex();
        }
        if (emitterResist == 0) {
            emitPrmIndex = emitIndex;
        } else if (emitPrmIndex == 0) {
            emitPrmIndex = ckt.makeVoltNode(getInstName() + "emitter").getIndex();
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
        if (subs == LATERAL) {
            substConIndex = basePrmIndex;
            substConSubstConNode = basePrmBasePrmNode;
        } else {
            substConIndex = colPrmIndex;
            substConSubstConNode = colPrmColPrmNode;
        }
        substConSubstNode = wrk.aquireNode(substConIndex, substIndex);
        substSubstConNode = wrk.aquireNode(substIndex, substConIndex);
        baseColPrmNode = wrk.aquireNode(baseIndex, colPrmIndex);
        colPrmBaseNode = wrk.aquireNode(colPrmIndex, baseIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double gcpr = tCollectorConduct * area;
        double gepr = tEmitterConduct * area;
        double gpi = gpiStates.get(0);
        double gmu = gmuStates.get(0);
        double gm = gmStates.get(0);
        double go = goStates.get(0);
        double xgm = 0;
        double td = excessPhaseFactor;
        if (td != 0) {
            double arg = td * tmprl.getOmega();
            gm = gm + go;
            xgm = -gm * MathLib.sin(arg);
            gm = gm * MathLib.cos(arg) - go;
        }
        double gx = gxStates.get(0);
        double xcpi = cqbeStates.get(0) * tmprl.getOmega();
        double xcmu = cqbcStates.get(0) * tmprl.getOmega();
        double xcbx = cqbxStates.get(0) * tmprl.getOmega();
        double xcsub = cqsubStates.get(0) * tmprl.getOmega();
        double xcmcb = cexbcStates.get(0) * tmprl.getOmega();
        colColNode.realPlusEq(m * (gcpr));
        baseBaseNode.realPlusEq(m * (gx));
        baseBaseNode.imagPlusEq(m * (xcbx));
        emitEmitNode.realPlusEq(m * (gepr));
        colPrmColPrmNode.realPlusEq(m * (gmu + go + gcpr));
        colPrmColPrmNode.imagPlusEq(m * (xcmu + xcbx));
        substConSubstConNode.imagPlusEq(m * (xcsub));
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
        substSubstNode.imagPlusEq(m * (xcsub));
        substConSubstNode.imagPlusEq(m * (-xcsub));
        substSubstConNode.imagPlusEq(m * (-xcsub));
        baseColPrmNode.imagPlusEq(m * (-xcbx));
        colPrmBaseNode.imagPlusEq(m * (-xcbx));
        return true;
    }

    public boolean convTest(Mode mode) {
        double vbe = type * (wrk.getRhsOldAt(basePrmIndex).getReal()
                - wrk.getRhsOldAt(emitPrmIndex).getReal());
        double vbc = type * (wrk.getRhsOldAt(basePrmIndex).getReal()
                - wrk.getRhsOldAt(colPrmIndex).getReal());
        double delvbe = vbe - vbeStates.get(0);
        double delvbc = vbc - vbcStates.get(0);
        double cchat = ccStates.get(0) + (gmStates.get(0) + goStates.get(0)) * delvbe
                - (goStates.get(0) + gmuStates.get(0)) * delvbc;
        double cbhat = cbStates.get(0) + gpiStates.get(0) * delvbe + gmuStates.get(0)
                * delvbc;
        double cc = ccStates.get(0);
        double cb = cbStates.get(0);
        // Check convergence
        double tol = env.getRelTol() * MathLib.max(MathLib.abs(cchat), MathLib.abs(cc)) + env.getAbsTol();
        if (MathLib.abs(cchat - cc) > tol) {
            ckt.setNonConverged(true);
            return true;
        } else {
            tol = env.getRelTol() * MathLib.max(MathLib.abs(cbhat), MathLib.abs(cb))
                    + env.getAbsTol();
            if (MathLib.abs(cbhat - cb) > tol) {
                ckt.setNonConverged(true);
                return true;
            }
        }
        return true;
    }

    public boolean loadInitCond() {
        if (icVBE == 0) {
            icVBE =
                    wrk.getRhsAt(baseIndex).getReal()
                    - wrk.getRhsAt(emitIndex).getReal();
        }
        if (icVCE == 0) {
            icVCE =
                    wrk.getRhsAt(colIndex).getReal()
                    - wrk.getRhsAt(emitIndex).getReal();
        }
        return true;
    }
    Real ceq = Real.zero();
    Real gcsub = Real.zero();
    Real geq = Real.zero();
    Real geqbx = Real.zero();
    Bool icheck = Bool.TRUE();
    Bool ichk1 = Bool.TRUE();

    public boolean load(Mode mode) {
        double cb = 0;
        double cc = 0;
        double geqcb;
        double gm = 0;
        double gmu = 0;
        double go = 0;
        double gpi = 0;
        double gx = 0;
        double vbc = 0;
        double vbe = 0;
        double vbx = 0;
        double vce;
        double vsub = 0;
        double gdsub = 0;
        double cdsub = 0;
        double vt = this.temp * KoverQ;
        geqcb = 0;
        boolean bypass = false;
        if (mode.contains(MODE.INIT_SMSIG)) {
            vbe = vbeStates.get(0);
            vbc = vbcStates.get(0);
            vbx = type * (wrk.getRhsOldAt(baseIndex).getReal()
                    - wrk.getRhsOldAt(colPrmIndex).getReal());
            vsub = type * subs * (wrk.getRhsOldAt(substIndex).getReal()
                    - wrk.getRhsOldAt(substConIndex).getReal());
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vbe = vbeStates.get(1);
            vbc = vbcStates.get(1);
            vbx = type * (wrk.getRhsOldAt(baseIndex).getReal()
                    - wrk.getRhsOldAt(colPrmIndex).getReal());
            vsub = type * subs * (wrk.getRhsOldAt(substIndex).getReal()
                    - wrk.getRhsOldAt(substConIndex).getReal());
            if (mode.contains(MODE.TRAN) && mode.isUseIC()) {
                vbx = type * (icVBE - icVCE);
            }
            vsub = 0;
        } else if (mode.contains(MODE.INIT_JCT)
                && mode.contains(MODE.TRANOP) && mode.isUseIC()) {
            vbe = type * icVBE;
            vce = type * icVCE;
            vbc = vbe - vce;
            vbx = vbc;
            vsub = 0;
        } else if (mode.contains(MODE.INIT_JCT) && !off) {
            vbe = tVcrit;
            vbc = 0;
            vsub = 0;
            vbx = 0;
        } else if (mode.contains(MODE.INIT_JCT)
                || (mode.contains(MODE.INIT_FIX) && off)) {
            vbe = 0;
            vbc = 0;
            vsub = 0;
            vbx = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vbeStates.set(0, vbeStates.get(1));
                vbe = (1 + xfact) * vbeStates.get(1)
                        - xfact * vbeStates.get(2);
                vbcStates.set(0, vbcStates.get(1));
                vbc = (1 + xfact) * vbcStates.get(1)
                        - xfact * vbcStates.get(2);
                ccStates.set(0, ccStates.get(1));
                cbStates.set(0, cbStates.get(1));
                gpiStates.set(0, gpiStates.get(1));
                gmuStates.set(0, gmuStates.get(1));
                gmStates.set(0, gmStates.get(1));
                goStates.set(0, goStates.get(1));
                gxStates.set(0, gxStates.get(1));
                vsubStates.set(0, vsubStates.get(1));
                vsub = (1 + xfact) * vsubStates.get(1)
                        - xfact * vsubStates.get(2);
            } else {
                // Compute new nonlinear branch voltages
                vbe = type * (wrk.getRhsOldAt(basePrmIndex).getReal()
                        - wrk.getRhsOldAt(emitPrmIndex).getReal());
                vbc = type * (wrk.getRhsOldAt(basePrmIndex).getReal()
                        - wrk.getRhsOldAt(colPrmIndex).getReal());
                vsub = type * subs * (wrk.getRhsOldAt(substIndex).getReal()
                        - wrk.getRhsOldAt(substConIndex).getReal());
            }
            double delvbe = vbe - vbeStates.get(0);
            double delvbc = vbc - vbcStates.get(0);
            vbx = type * (wrk.getRhsOldAt(baseIndex).getReal()
                    - wrk.getRhsOldAt(colPrmIndex).getReal());
            vsub = type * subs * (wrk.getRhsOldAt(substIndex).getReal()
                    - wrk.getRhsOldAt(substConIndex).getReal());
            double cchat = ccStates.get(0) + (gmStates.get(0) + goStates.get(0)) * delvbe
                    - (goStates.get(0) + gmuStates.get(0)) * delvbc;
            double cbhat = cbStates.get(0) + gpiStates.get(0) * delvbe + gmuStates.get(0)
                    * delvbc;
            if (env.isBypass() && !mode.contains(MODE.INIT_PRED)
                    && (MathLib.abs(delvbe) < (env.getRelTol() * MathLib.max(MathLib.abs(vbe),
                    MathLib.abs(vbeStates.get(0)))
                    + env.getVoltTol()))) {
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
                            gcsub.set(gcsubStates.get(0));
                            geqbx.set(geqbxStates.get(0));
                            vsub = vsubStates.get(0);
                            gdsub = gdsubStates.get(0);
                            cdsub = cdsubStates.get(0);
                            bypass = true;
                        }
                    }
                }
            }
            if (!bypass) {
                // Limit nonlinear branch voltages
                vbe = MOSUtils.pnjLimit(vbe, vbeStates.get(0), vt, tVcrit, icheck);
                vbc = MOSUtils.pnjLimit(vbc, vbcStates.get(0), vt, tVcrit, ichk1);
                if (ichk1.isTrue()) {
                    icheck.setTrue();
                }
                vsub = MOSUtils.pnjLimit(vsub, vsubStates.get(0), vt,
                        tSubVcrit, ichk1);
                if (ichk1.isTrue()) {
                    icheck.setTrue();
                }
            }
        }
        if (!bypass) {
            //case NEXT1:
            double arg;
            double cbe;
            double gbe;
            double csat = tSatCur * area;
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
            double cben;
            double gben;
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
            double cbc;
            double gbc;
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
            double gbcn;
            double cbcn;
            double _c4;
            if (subs == VERTICAL) {
                _c4 = tBCleakCur * areab;
            } else {
                _c4 = tBCleakCur * areac;
            }
            double vtc = leakBCemissionCoeff * vt;
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
            double csubsat = tSubSatCur * area;
            if (vsub <= -3 * vt) {
                arg = 3 * vt / (vsub * MathLib.E);
                arg = arg * arg * arg;
                gdsub = csubsat * 3 * arg / vsub + env.getGMin();
                cdsub = -csubsat * (1 + arg) + env.getGMin() * vsub;
            } else {
                double evsub = MathLib.exp(MathLib.min(Double.MAX_EXPONENT, vsub / vt));
                gdsub = csubsat * evsub / vt + env.getGMin();
                cdsub = csubsat * (evsub - 1) + env.getGMin() * vsub;
            }
            // Determine base charge terms
            double dqbdvc;
            double dqbdve;
            double q2;
            double qb;
            double sarg;
            double sqarg;
            double q1 = 1 / (1 - invEarlyVoltF * vbc - invEarlyVoltR * vbe);
            double oik = invRollOffF / area;
            double oikr = invRollOffR / area;
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
                qb = q1 * (1 + sqarg) / 2;
                dqbdve = q1 * (qb * invEarlyVoltR + oik * gbe / sqarg);
                dqbdvc = q1 * (qb * invEarlyVoltF + oikr * gbc / sqarg);
            }
            // Weil's approx. for excess phase applied with backward-euler integration
            cc = 0;
            double cex = cbe;
            double gex = gbe;
            double arg1;
            double arg2;
            double arg3;
            double td = excessPhaseFactor;
            if (mode.contains(MODE.TRAN, MODE.AC) && td != 0) {
                arg1 = tmprl.getDelta() / td;
                arg2 = 3 * arg1;
                arg1 = arg2 * arg1;
                double denom = 1 + arg1 + arg2;
                arg3 = arg1 / denom;
                if (mode.contains(MODE.INIT_TRAN)) {
                    cexbcStates.set(1, cbe / qb);
                    cexbcStates.set(2, cexbcStates.get(1));
                }
                cc = (cexbcStates.get(1) * (1 + tmprl.getDelta()
                        / tmprl.getOldDeltaAt(1) + arg2)
                        - cexbcStates.get(2) * tmprl.getDelta()
                        / tmprl.getOldDeltaAt(1)) / denom;
                cex = cbe * arg3;
                gex = gbe * arg3;
                cexbcStates.set(0, cc + cex / qb);
            }
            // Determine dc incremental conductances
            cc = cc + (cex - cbc) / qb - cbc / tBetaR - cbcn;
            cb = cbe / tBetaF + cben + cbc / tBetaR + cbcn;
            double rbpr = tMinBaseResist / area;
            double rbpi = tBaseResist / area - rbpr;
            double xjrb = baseCurrentHalfResist * area;
            gx = rbpr + rbpi / qb;
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
                double tf = transitTimeF;
                double tr = transitTimeR;
                double czbe = tBEcap * area;
                double pe = tBEpot;
                double xme = junctionExpBE;
                double cdis = baseFractionBCcap;
                double ctot;
                double czsub;
                if (subs == VERTICAL) {
                    ctot = tBCcap * areab;
                } else {
                    ctot = tBCcap * areac;
                }
                double czbc = ctot * cdis;
                double czbx = ctot - czbc;
                double pc = tBCpot;
                double xmc = junctionExpBC;
                double fcpe = tDepCap;
                if (subs == VERTICAL) {
                    czsub = tSubcap * areac;
                } else {
                    czsub = tSubcap * areab;
                }
                double ps = tSubpot;
                double xms = exponentialSubstrate;
                double xtf = transitTimeBiasCoeffF;
                double ovtf = transitTimeVBCFactor;
                double xjtf = transitTimeHighCurrentF * area;
                if (tf != 0 && vbe > 0) {
                    double argtf = 0;
                    arg2 = 0;
                    arg3 = 0;
                    if (xtf != 0) {
                        argtf = xtf;
                        if (ovtf != 0) {
                            argtf = argtf * MathLib.exp(vbc * ovtf);
                        }
                        arg2 = argtf;
                        if (xjtf != 0) {
                            double tmp = cbe / (cbe + xjtf);
                            argtf = argtf * tmp * tmp;
                            arg2 = argtf * (3 - tmp - tmp);
                        }
                        arg3 = cbe * argtf * ovtf;
                    }
                    cbe = cbe * (1 + argtf) / qb;
                    gbe = (gbe * (1 + arg2) - cbe * dqbdve) / qb;
                    geqcb = tf * (arg3 - cbe * dqbdvc) / qb;
                }
                double f1;
                double _f2;
                double _f3;
                if (vbe < fcpe) {
                    arg = 1 - vbe / pe;
                    sarg = MathLib.exp(-xme * MathLib.log(arg));
                    qbeStates.set(0, tf * cbe + pe * czbe
                            * (1 - arg * sarg) / (1 - xme));
                    capbe = tf * gbe + czbe * sarg;
                } else {
                    f1 = tf1;
                    _f2 = this.f2;
                    _f3 = this.f3;
                    double czbef2 = czbe / _f2;
                    qbeStates.set(0, tf * cbe + czbe * f1 + czbef2
                            * (_f3 * (vbe - fcpe) + (xme / (pe + pe)) * (vbe * vbe - fcpe * fcpe)));
                    capbe = tf * gbe + czbef2 * (_f3 + xme * vbe / pe);
                }
                double fcpc = tf4;
                f1 = tf5;
                _f2 = f6;
                _f3 = f7;
                if (vbc < fcpc) {
                    arg = 1 - vbc / pc;
                    sarg = MathLib.exp(-xmc * MathLib.log(arg));
                    qbcStates.set(0, tr * cbc + pc * czbc * (1 - arg * sarg) / (1 - xmc));
                    capbc = tr * gbc + czbc * sarg;
                } else {
                    double czbcf2 = czbc / _f2;
                    qbcStates.set(0, tr * cbc + czbc * f1 + czbcf2
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
                    qbxStates.set(0, czbx * f1 + czbxf2
                            * (_f3 * (vbx - fcpc) + (xmc / (pc + pc)) * (vbx * vbx - fcpc * fcpc)));
                    capbx = czbxf2 * (_f3 + xmc * vbx / pc);
                }
                if (vsub < 0) {
                    arg = 1 - vsub / ps;
                    sarg = MathLib.exp(-xms * MathLib.log(arg));
                    qsubStates.set(0, ps * czsub * (1 - arg * sarg)
                            / (1 - xms));
                    capsub = czsub * sarg;
                } else {
                    qsubStates.set(0, vsub * czsub * (1 + xms * vsub
                            / (2 * ps)));
                    capsub = czsub * (1 + xms * vsub / ps);
                }
                // Store small - signal parameters
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        cqbeStates.set(0, capbe);
                        cqbcStates.set(0, capbc);
                        cqsubStates.set(0, capsub);
                        cqbxStates.set(0, capbx);
                        cexbcStates.set(0, geqcb);
                        return true;
                    }
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qbeStates.set(1, qbeStates.get(0));
                        qbcStates.set(1, qbcStates.get(0));
                        qbxStates.set(1, qbxStates.get(0));
                        qsubStates.set(1, qsubStates.get(0));
                    }
                    stateTable.integrate(geq, ceq, capbe, qbeStates, cqbeStates);
                    geqcb = geqcb * tmprl.getAgAt(0);
                    gpi = gpi + geq.get();
                    cb = cb + cqbeStates.get(0);
                    stateTable.integrate(geq, ceq, capbc, qbcStates, cqbcStates);
                    gmu = gmu + geq.get();
                    cb = cb + cqbcStates.get(0);
                    cc = cc - cqbcStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqbeStates.set(1, cqbeStates.get(0));
                        cqbcStates.set(1, cqbcStates.get(0));
                    }
                }
            }
            if ((!mode.contains(MODE.INIT_FIX) || !off) && icheck.isTrue()) {
                ckt.setNonConverged(true);
            }
            // Charge storage for B - S and B - X junctions
            if (mode.contains(MODE.TRAN, MODE.AC)) {
                stateTable.integrate(gcsub, ceq, capsub, qsubStates, cqsubStates);
                stateTable.integrate(geqbx, ceq, capbx, qbxStates, cqbxStates);
                if (mode.contains(MODE.INIT_TRAN)) {
                    cqbxStates.set(1, cqbxStates.get(0));
                    cqsubStates.set(1, cqsubStates.get(0));
                }
            }
            //case NEXT2:
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
            gcsubStates.set(0, gcsub.get());
            geqbxStates.set(0, geqbx.get());
            vsubStates.set(0, vsub);
            gdsubStates.set(0, gdsub);
            cdsubStates.set(0, cdsub);
        }
        // load current excitation vector
        double geqsub = gcsub.get() + gdsub;
        double ceqsub = type * subs * (cqsubStates.get(0) + cdsub - vsub * geqsub);
        double gcpr = tCollectorConduct * area;
        double gepr = tEmitterConduct * area;
        double ceqbx = type * (cqbxStates.get(0) - vbx * geqbx.get());
        double ceqbe = type * (cc + cb - vbe * (gm + go + gpi) + vbc * (go - geqcb));
        double ceqbc = type * (-cc + vbe * (gm + go) - vbc * (gmu + go));
        /*        if (tmprl.getTime() > 7 && tmprl.getTime() < 7.1) {
        System.out.println(tmprl.getTime());
        System.out.printf("subs %-10d cqsub %-10g cdsub %-10g vsub %-10g geqsub %-10g\n", subs, cqsubStates.get(0), cdsub, vsub, geqsub);
        System.out.printf("gm %-10g  gmu %-10g gcpr %-10g gepr %-10g go %-10g\n", gm, gmu, gcpr, gepr, go);
        System.out.printf("gx %-10g geqsub %-10g gpi %-10g geqcb %-10g geqbx %-10g\n",gx,geqsub,gpi, geqcb, geqbx.get());
        System.out.printf("ceqbx %-10g ceqbc %-10g ceqsub %-10g ceqbe %-10g\n", ceqbx, ceqbc, ceqsub, ceqbe);
        System.out.println();
        }
         */
        wrk.getRhsAt(baseIndex).realPlusEq(m * -ceqbx);
        wrk.getRhsAt(colPrmIndex).realPlusEq(m * (ceqbx + ceqbc));
        wrk.getRhsAt(substConIndex).realPlusEq(m * ceqsub);
        wrk.getRhsAt(basePrmIndex).realPlusEq(m * (-ceqbe - ceqbc));
        wrk.getRhsAt(emitPrmIndex).realPlusEq(m * ceqbe);
        wrk.getRhsAt(substIndex).realPlusEq(m * -ceqsub);
        // Load y matrix
        colColNode.realPlusEq(m * gcpr);
        baseBaseNode.realPlusEq(m * (gx + geqbx.get()));
        emitEmitNode.realPlusEq(m * gepr);
        colPrmColPrmNode.realPlusEq(m * (gmu + go + gcpr + geqbx.get()));
        substConSubstConNode.realPlusEq(m * geqsub);
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
        emitPrmColPrmNode.realPlusEq(m * -go + geqcb);
        emitPrmBasePrmNode.realPlusEq(m * (-gpi - gm - geqcb));
        substSubstNode.realPlusEq(m * geqsub);
        substConSubstNode.realPlusEq(m * -geqsub);
        substSubstConNode.realPlusEq(m * -geqsub);
        baseColPrmNode.realPlusEq(m * -geqbx.get());
        colPrmBaseNode.realPlusEq(m * -geqbx.get());
        return true;
    }

    public boolean unSetup() {
        if (colPrmIndex != 0 && colPrmIndex != colIndex) {
            ckt.deleteNode(colPrmIndex);
            colPrmIndex = 0;
        }
        if (basePrmIndex != 0 && basePrmIndex != baseIndex) {
            ckt.deleteNode(basePrmIndex);
            basePrmIndex = 0;
        }
        if (emitPrmIndex != 0 && emitPrmIndex != emitIndex) {
            ckt.deleteNode(emitPrmIndex);
            emitPrmIndex = 0;
        }
        return true;
    }

    public boolean temperature() {
        if (tnom == 0) {
            tnom = env.getNomTemp();
        }
        if (temp == 0) {
            temp = env.getTemp() + this.dtemp;
        }
        double vt = temp * KoverQ;
        double fact2 = temp / REF_TEMP;
        double egfet = 1.16 - (7.02e-4 * temp * temp) / (temp + 1108);
        double arg = -egfet / (2 * BOLTZMANN * temp)
                + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact = -2 * vt * (1.5 * MathLib.log(fact2) + CHARGE * arg);
        double ratlog = MathLib.log(temp / tnom);
        double ratio1 = temp / tnom - 1;
        double factlog = ratio1 * energyGap / vt
                + tempExpIS * ratlog;
        double factor = MathLib.exp(factlog);
        tSatCur = satCur * factor;
        tSubSatCur = subSatCur * factor;
        double bfactor = MathLib.exp(ratlog * betaExp);
        tBetaF = betaF * bfactor;
        tBetaR = betaR * bfactor;
        tBEleakCur = leakBEcurrent
                * MathLib.exp(factlog / leakBEemissionCoeff) / bfactor;
        tBCleakCur = leakBCcurrent
                * MathLib.exp(factlog / leakBCemissionCoeff) / bfactor;
        double _dtemp = temp - tnom;
        if (emitterResist != 0) {
            factor = 1 + (reTempCoeff1) * _dtemp
                    + (reTempCoeff2) * _dtemp * _dtemp;
            tEmitterConduct = 1 / (emitterResist * factor);
        }
        if (collectorResist != 0) {
            factor = 1 + (rcTempCoeff1) * _dtemp
                    + (rcTempCoeff2) * _dtemp * _dtemp;
            tCollectorConduct = 1 / (collectorResist * factor);
        }
        factor = 1 + (rbTempCoeff1) * _dtemp
                + (rbTempCoeff2) * _dtemp * _dtemp;
        tBaseResist = baseResist * factor;
        factor = 1 + (rbmTempCoeff1) * _dtemp
                + (rbmTempCoeff2) * _dtemp * _dtemp;
        tMinBaseResist = minBaseResist * factor;
        double fact1 = tnom / REF_TEMP;
        double pbo = (potentialBE - pbfact) / fact1;
        double gmaold = (potentialBE - pbo) / pbo;
        tBEcap = depletionCapBE / (1 + junctionExpBE
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tBEpot = fact2 * pbo + pbfact;
        double gmanew = (tBEpot - pbo) / pbo;
        tBEcap *= 1 + junctionExpBE
                * (4e-4 * (temp - REF_TEMP) - gmanew);
        pbo = (potentialBC - pbfact) / fact1;
        gmaold = (potentialBC - pbo) / pbo;
        tBCcap = depletionCapBC / (1 + junctionExpBC
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tBCpot = fact2 * pbo + pbfact;
        gmanew = (tBCpot - pbo) / pbo;
        tBCcap *= 1 + junctionExpBC
                * (4e-4 * (temp - REF_TEMP) - gmanew);
        pbo = (potentialSubstrate - pbfact) / fact1;
        gmaold = (potentialSubstrate - pbo) / pbo;
        tSubcap = capSub / (1 + exponentialSubstrate
                * (4e-4 * (tnom - REF_TEMP) - gmaold));
        tSubpot = fact2 * pbo + pbfact;
        gmanew = (tSubpot - pbo) / pbo;
        tSubcap *= 1 + exponentialSubstrate
                * (4e-4 * (temp - REF_TEMP) - gmanew);
        tDepCap = depletionCapCoeff * tBEpot;
        double xfc = MathLib.log(1 - depletionCapCoeff);
        f2 = MathLib.exp((1 + junctionExpBE) * xfc);
        f3 = 1 - depletionCapCoeff * (1 + junctionExpBE);
        f6 = MathLib.exp((1 + junctionExpBC) * xfc);
        f7 = 1 - depletionCapCoeff * (1 + junctionExpBC);
        tf1 = tBEpot * (1 - MathLib.exp((1 - junctionExpBE) * xfc))
                / (1 - junctionExpBE);
        tf4 = depletionCapCoeff * tBCpot;
        tf5 = tBCpot * (1 - MathLib.exp((1 - junctionExpBC) * xfc))
                / (1 - junctionExpBC);
        tVcrit = vt * MathLib.log(vt / (ROOT2 * tSatCur * area));
        tSubVcrit = vt * MathLib.log(vt / (ROOT2 * tSubSatCur * area));
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        timeStep = stateTable.terr(qbeStates, cqbeStates, timeStep);
        timeStep = stateTable.terr(qbcStates, cqbcStates, timeStep);
        return stateTable.terr(qsubStates, cqsubStates, timeStep);
    }

    public boolean accept(Mode mode) {
        return true;
    }
}
