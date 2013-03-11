/*
 * bsim2.java
 *
 * Created on August 28, 2006, 1:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.StateTable;
import org.pmedv.blackboard.spice.sim.spice.Temporal;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.KEY;
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
public class BSIM2Instance extends BSIM2ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;

    /* Source conductance */
    private double srcConductance;

    /* Drain conductance */
    private double drnConductance;
    private double von;
    private double vdsat;
    /* Device mode : 1 = normal, -1 = inverse */
    private int _mode;
    private int pad;
    private double cox;
    private double vtm;
    private double vdd2;
    private double vgg2;
    private double vbb2;
    private Complex ggNode;
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
    private StateVector cdStates;
    private StateVector idStates;
    private StateVector cbsStates;
    private StateVector ibsStates;
    private StateVector cbdStates;
    private StateVector ibdStates;
    private StateVector gmStates;
    private StateVector gdsStates;
    private StateVector gmbsStates;
    private StateVector gbdStates;
    private StateVector gbsStates;
    private StateVector qbStates;
    private StateVector cqbStates;
    private StateVector iqbStates;
    private StateVector qgStates;
    private StateVector cqgStates;
    private StateVector iqgStates;
    private StateVector qdStates;
    private StateVector cqdStates;
    private StateVector iqdStates;
    private StateVector cggbStates;
    private StateVector cgdbStates;
    private StateVector cgsbStates;
    private StateVector cbgbStates;
    private StateVector cbdbStates;
    private StateVector cbsbStates;
    private StateVector capbdStates;
    private StateVector iqbdStates;
    private StateVector cqbdStates;
    private StateVector capbsStates;
    private StateVector iqbsStates;
    private StateVector cqbsStates;
    private StateVector cdgbStates;
    private StateVector cddbStates;
    private StateVector cdsbStates;
    private StateVector vonoStates;
    private StateVector vdsatoStates;
    private StateVector qbsStates;
    private StateVector qbdStates;
    private BSIM2SizeDependParam pParam;
    private BSIM2SizeDependParam pSizeDependParamKnot;
    private Complex drnDrnNode;
    private double gm;
    private double gds;
    private double gmbs;
    private double qGate;
    private double qBase;
    private double qDrain;
    private double cggb;
    private double cgdb;
    private double cgsb;
    private double cbgb;
    private double cbdb;
    private double cbsb;
    private double cdgb;
    private double cddb;
    private double cdsb;

    /** Creates a new instance of bsim2 */
    public BSIM2Instance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        vbdStates = stateTable.createRow();
        vbsStates = stateTable.createRow();
        vgsStates = stateTable.createRow();
        vdsStates = stateTable.createRow();
        cdStates = stateTable.createRow();
        idStates = stateTable.createRow();
        cbsStates = stateTable.createRow();
        ibsStates = stateTable.createRow();
        cbdStates = stateTable.createRow();
        ibdStates = stateTable.createRow();
        gmStates = stateTable.createRow();
        gdsStates = stateTable.createRow();
        gmbsStates = stateTable.createRow();
        gbdStates = stateTable.createRow();
        gbsStates = stateTable.createRow();
        qbStates = stateTable.createRow();
        cqbStates = stateTable.createRow();
        iqbStates = stateTable.createRow();
        qgStates = stateTable.createRow();
        cqgStates = stateTable.createRow();
        iqgStates = stateTable.createRow();
        qdStates = stateTable.createRow();
        cqdStates = stateTable.createRow();
        iqdStates = stateTable.createRow();
        cggbStates = stateTable.createRow();
        cgdbStates = stateTable.createRow();
        cgsbStates = stateTable.createRow();
        cbgbStates = stateTable.createRow();
        cbdbStates = stateTable.createRow();
        cbsbStates = stateTable.createRow();
        capbdStates = stateTable.createRow();
        iqbdStates = stateTable.createRow();
        cqbdStates = stateTable.createRow();
        capbsStates = stateTable.createRow();
        iqbsStates = stateTable.createRow();
        cqbsStates = stateTable.createRow();
        cdgbStates = stateTable.createRow();
        cddbStates = stateTable.createRow();
        cdsbStates = stateTable.createRow();
        vonoStates = stateTable.createRow();
        vdsatoStates = stateTable.createRow();
        qbsStates = stateTable.createRow();
        qbdStates = stateTable.createRow();
        if (sheetResistance != 0 && drnSquares != 0 && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if (sheetResistance != 0 && srcSquares != 0
                && (srcPrmIndex == 0)) {
            if (srcPrmIndex == 0) {
                srcPrmIndex = ckt.makeVoltNode(getInstName() + "source").getIndex();
            }
        } else {
            srcPrmIndex = srcIndex;
        }
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        ggNode = wrk.aquireNode(gateIndex, gateIndex);
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
        double omega = tmprl.getOmega();
        if (_mode >= 0) {
            xnrm = 1;
            xrev = 0;
        } else {
            xnrm = 0;
            xrev = 1;
        }
        double gdpr = drnConductance;
        double gspr = srcConductance;
        gm = gmStates.get(0);
        gds = gdsStates.get(0);
        gmbs = gmbsStates.get(0);
        double gbd = gbdStates.get(0);
        double gbs = gbsStates.get(0);
        double capbd = capbdStates.get(0);
        double capbs = capbsStates.get(0);
        // Charge parameters
        cggb = cggbStates.get(0);
        cgsb = cgsbStates.get(0);
        cgdb = cgdbStates.get(0);
        cbgb = cbgbStates.get(0);
        cbsb = cbsbStates.get(0);
        cbdb = cbdbStates.get(0);
        cdgb = cdgbStates.get(0);
        cdsb = cdsbStates.get(0);
        cddb = cddbStates.get(0);
        double xcdgb = (cdgb - pParam.gDoverlapCap) * omega;
        double xcddb = (cddb + capbd + pParam.gDoverlapCap) * omega;
        double xcdsb = cdsb * omega;
        double xcsgb = -(cggb + cbgb + cdgb + pParam.gSoverlapCap) * omega;
        double xcsdb = -(cgdb + cbdb + cddb) * omega;
        double xcssb = (capbs + pParam.gSoverlapCap - (cgsb + cbsb + cdsb)) * omega;
        double xcggb = (cggb + pParam.gDoverlapCap + pParam.gSoverlapCap + pParam.gBoverlapCap) * omega;
        double xcgdb = (cgdb - pParam.gDoverlapCap) * omega;
        double xcgsb = (cgsb - pParam.gSoverlapCap) * omega;
        double xcbgb = (cbgb - pParam.gBoverlapCap) * omega;
        double xcbdb = (cbdb - capbd) * omega;
        double xcbsb = (cbsb - capbs) * omega;
        double _m = this.m;
        ggNode.imagPlusEq(_m * (xcggb));
        blkBlkNode.imagPlusEq(_m * (-xcbgb - xcbdb - xcbsb));
        drnPrmDrnPrmNode.imagPlusEq(_m * (xcddb));
        srcPrmSrcPrmNode.imagPlusEq(_m * (xcssb));
        gateBlkNode.imagPlusEq(_m * (-xcggb - xcgdb - xcgsb));
        gateDrnPrmNode.imagPlusEq(_m * (xcgdb));
        gateSrcPrmNode.imagPlusEq(_m * (xcgsb));
        blkGateNode.imagPlusEq(_m * (xcbgb));
        blkDrnPrmNode.imagPlusEq(_m * (xcbdb));
        blkSrcPrmNode.imagPlusEq(_m * (xcbsb));
        drnPrmGateNode.imagPlusEq(_m * (xcdgb));
        drnPrmBlkNode.imagPlusEq(_m * (-xcdgb - xcddb - xcdsb));
        drnPrmSrcPrmNode.imagPlusEq(_m * (xcdsb));
        srcPrmGateNode.imagPlusEq(_m * (xcsgb));
        srcPrmBlkNode.imagPlusEq(_m * (-xcsgb - xcsdb - xcssb));
        srcPrmDrnPrmNode.imagPlusEq(_m * (xcsdb));
        drnDrnNode.realPlusEq(_m * (gdpr));
        srcSrcNode.realPlusEq(_m * (gspr));
        blkBlkNode.realPlusEq(_m * (gbd + gbs));
        drnPrmDrnPrmNode.realPlusEq(_m * (gdpr + gds + gbd + xrev * (gm + gmbs)));
        srcPrmSrcPrmNode.realPlusEq(_m * (gspr + gds + gbs + xnrm * (gm + gmbs)));
        drnDrnPrmNode.realMinusEq(_m * (gdpr));
        srcSrcPrmNode.realMinusEq(_m * (gspr));
        blkDrnPrmNode.realMinusEq(_m * (gbd));
        blkSrcPrmNode.realMinusEq(_m * (gbs));
        drnPrmDrnNode.realMinusEq(_m * (gdpr));
        drnPrmGateNode.realPlusEq(_m * ((xnrm - xrev) * gm));
        drnPrmBlkNode.realPlusEq(_m * (-gbd + (xnrm - xrev) * gmbs));
        drnPrmSrcPrmNode.realPlusEq(_m * (-gds - xnrm * (gm + gmbs)));
        srcPrmGateNode.realPlusEq(_m * (-(xnrm - xrev) * gm));
        srcPrmSrcNode.realMinusEq(_m * (gspr));
        srcPrmBlkNode.realPlusEq(_m * (-gbs - (xnrm - xrev) * gmbs));
        srcPrmDrnPrmNode.realPlusEq(_m * (-gds - xrev * (gm + gmbs)));
        return true;
    }

    public boolean convTest(Mode mode) {
        double vbs = type * (wrk.getRhsOldAt(blkIndex).getReal()
                - wrk.getRhsOldAt(srcPrmIndex).getReal());
        double vgs = type * (wrk.getRhsOldAt(gateIndex).getReal()
                - wrk.getRhsOldAt(srcPrmIndex).getReal());
        double vds = type * (wrk.getRhsOldAt(drnPrmIndex).getReal()
                - wrk.getRhsOldAt(srcPrmIndex).getReal());
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
            cdhat =
                    cdStates.get(0)
                    - gbdStates.get(0) * delvbd
                    + gmbsStates.get(0) * delvbs
                    + gmStates.get(0) * delvgs
                    + gdsStates.get(0) * delvds;
        } else {
            cdhat =
                    cdStates.get(0)
                    - (gbdStates.get(0)
                    - gmbsStates.get(0)) * delvbd
                    - gmStates.get(0) * delvgd
                    + gdsStates.get(0) * delvds;
        }
        double cbhat =
                cbsStates.get(0)
                + cbdStates.get(0)
                + gbdStates.get(0) * delvbd
                + gbsStates.get(0) * delvbs;
        double cd = cdStates.get(0);
        double cbs = cbsStates.get(0);
        double cbd = cbdStates.get(0);
        // Check convergence
        double tol;
        if (!off || !mode.contains(MODE.INIT_FIX)) {
            tol = env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol();
            if (MathLib.abs(cdhat - cd) >= tol) {
                return false;
            }
            tol = env.getRelTol() * MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbs + cbd))
                    + env.getAbsTol();
            if (MathLib.abs(cbhat - (cbs + cbd)) > tol) {
                return false;
            }
        }
        return true;
    }

    private double evaluate(Mode mode, double vds, double vbs, double vgs) {
        double idrain = 0;
        double _vdsat = 0;
        double phisb;
        double u1;
        double t1s,
                vc,
                kk,
                sqrtKk;
        double dPhisb_dVb,
                dT1s_dVb,
                dVc_dVd;
        double dVc_dVg,
                dVc_dVb,
                dKk_dVc;
        double dVdsat_dVd = 0,
                dVdsat_dVg = 0,
                dVdsat_dVb = 0;
        double dUvert_dVg,
                dUvert_dVd,
                dUvert_dVb,
                inv_Kk;
        double dUtot_dVd,
                dUtot_dVb,
                dUtot_dVg,
                ai,
                bi,
                vgeff,
                vof;
        double vbseff,
                vgdt,
                qbulk,
                utot;
        double t1,
                t2,
                t3,
                t4,
                t5,
                arg1,
                arg2,
                exp0 = 0,
                exp1 = 0;
        double tmp,
                tmp2,
                tmp3,
                uvert,
                beta1,
                beta2,
                beta0;
        double t6,
                t7,
                t8,
                t9,
                n = 0,
                expArg,
                expArg1;
        double Beta,
                dQblk_dVb,
                dVgdt_dVg,
                dVgdt_dVd;
        double dVbseff_dVb,
                dVgdt_dVb,
                dQblk_dVd;
        double con1,
                con3,
                con4,
                sqrVghigh,
                sqrVglow,
                cubVghigh,
                cubVglow;
        double delta,
                coeffa,
                coeffb,
                coeffc,
                coeffd,
                inv_Uvert,
                inv_Utot;
        double inv_Vdsat,
                tanh,
                sqrsech,
                dBeta1_dVb,
                dU1_dVd,
                dU1_dVg,
                dU1_dVb;
        double betaeff,
                fR,
                dFR_dVd,
                dFR_dVg,
                dFR_dVb,
                betas,
                beta3,
                beta4;
        double dBeta_dVd,
                dBeta_dVg,
                dBeta_dVb,
                dVgeff_dVg,
                dVgeff_dVd,
                dVgeff_dVb;
        double dCon3_dVd,
                dCon3_dVb,
                dCon4_dVd,
                dCon4_dVb,
                dCoeffa_dVd,
                dCoeffa_dVb;
        double dCoeffb_dVd,
                dCoeffb_dVb,
                dCoeffc_dVd,
                dCoeffc_dVb;
        double dCoeffd_dVd,
                dCoeffd_dVb;
        boolean chargeComputationNeeded = true;
        int valuetypeflag;
        if ((mode.contains(MODE.AC, MODE.TRAN))
                || ((mode.contains(MODE.TRANOP)) && (mode.isUseIC()))
                || (mode.contains(MODE.INIT_SMSIG))) {
            chargeComputationNeeded = true;
        }
        if (vbs < vbb2) {
            vbs = vbb2;
        }
        if (vgs > vgg2) {
            vgs = vgg2;
        }
        if (vds > vdd2) {
            vds = vdd2;
        }
        // Threshold voltage
        if (vbs <= 0) {
            phisb = pParam.phi - vbs;
            dPhisb_dVb = -1;
            t1s = MathLib.sqrt(phisb);
            dT1s_dVb = -0.5 / t1s;
        } else {
            tmp = pParam.phi / (pParam.phi + vbs);
            phisb = pParam.phi * tmp;
            dPhisb_dVb = -tmp * tmp;
            t1s = pParam.Phis3 / (pParam.phi + 0.5 * vbs);
            dT1s_dVb = -0.5 * t1s * t1s / pParam.Phis3;
        }
        double eta = pParam.eta0 + pParam.etaB * vbs;
        double ua = pParam.ua0 + pParam.uaB * vbs;
        double ub = pParam.ub0 + pParam.ubB * vbs;
        double u1s = pParam.u10 + pParam.u1B * vbs;
        double vth = pParam.vfb + pParam.phi + pParam.k1 * t1s - pParam.k2 * phisb - eta * vds;
        double dVth_dVd = -eta;
        double dVth_dVb = pParam.k1 * dT1s_dVb + pParam.k2 - pParam.etaB * vds;
        double vgst = vgs - vth;
        tmp = 1 / (1.744 + 0.8364 * phisb);
        double gg = 1 - tmp;
        double dGg_dVb = 0.8364 * tmp * tmp * dPhisb_dVb;
        double t0 = gg / t1s;
        double tmp1 = 0.5 * t0 * pParam.k1;
        double aa = 1 + tmp1;
        double dAa_dVb = (aa - 1) * (dGg_dVb / gg - dT1s_dVb / t1s);
        double invAa = 1 / aa;
        double vgHigh = pParam.vghigh;
        double vgLow = pParam.vglow;
        if (vgst >= vgHigh || pParam.n0 == 0) {
            vgeff = vgst;
            dVgeff_dVg = 1;
            dVgeff_dVd = -dVth_dVd;
            dVgeff_dVb = -dVth_dVb;
        } else {
            vof = pParam.vof0 + pParam.vofB * vbs + pParam.vofD * vds;
            n = pParam.n0 + pParam.nB / t1s + pParam.nD * vds;
            tmp = 0.5 / (n * vtm);
            expArg1 = -vds / vtm;
            expArg1 = MathLib.max(expArg1, -30);
            exp1 = MathLib.exp(expArg1);
            tmp1 = 1 - exp1;
            tmp1 = MathLib.max(tmp1, 1e-18);
            tmp2 = 2 * aa * tmp1;
            if (vgst <= vgLow) {
                expArg = vgst * tmp;
                expArg = MathLib.max(expArg, -30);
                exp0 = MathLib.exp(0.5 * vof + expArg);
                vgeff = MathLib.sqrt(tmp2) * vtm * exp0;
                t0 = n * vtm;
                dVgeff_dVg = vgeff * tmp;
                dVgeff_dVd = dVgeff_dVg * (n / tmp1 * exp1 - dVth_dVd - vgst * pParam.nD / n + t0 * pParam.vofD);
                dVgeff_dVb = dVgeff_dVg * (pParam.vofB * t0 - dVth_dVb + pParam.nB * vgst / (n * t1s * t1s) * dT1s_dVb + t0 * invAa * dAa_dVb);
            } else {
                expArg = vgLow * tmp;
                expArg = MathLib.max(expArg, -30);
                exp0 = MathLib.exp(0.5 * vof + expArg);
                vgeff = MathLib.sqrt(2 * aa * (1 - exp1)) * vtm * exp0;
                con1 = vgHigh;
                con3 = vgeff;
                con4 = con3 * tmp;
                sqrVghigh = vgHigh * vgHigh;
                sqrVglow = vgLow * vgLow;
                cubVghigh = vgHigh * sqrVghigh;
                cubVglow = vgLow * sqrVglow;
                t0 = 2 * vgHigh;
                t1 = 2 * vgLow;
                t2 = 3 * sqrVghigh;
                t3 = 3 * sqrVglow;
                t4 = vgHigh - vgLow;
                t5 = sqrVghigh - sqrVglow;
                t6 = cubVghigh - cubVglow;
                t7 = con1 - con3;
                delta = (t1 - t0) * t6 + (t2 - t3) * t5 + (t0 * t3 - t1 * t2) * t4;
                delta = 1 / delta;
                coeffb = (t1 - con4 * t0) * t6 + (con4 * t2 - t3) * t5 + (t0 * t3 - t1 * t2) * t7;
                coeffc = (con4 - 1) * t6 + (t2 - t3) * t7 + (t3 - con4 * t2) * t4;
                coeffd = (t1 - t0) * t7 + (1 - con4) * t5 + (con4 * t0 - t1) * t4;
                coeffa = sqrVghigh * (coeffc + coeffd * t0);
                vgeff = (coeffa + vgst * (coeffb + vgst * (coeffc + vgst * coeffd))) * delta;
                dVgeff_dVg = (coeffb + vgst * (2 * coeffc + 3 * vgst * coeffd)) * delta;
                t7 = con3 * tmp;
                t8 = dT1s_dVb * pParam.nB / (t1s * t1s * n);
                t9 = n * vtm;
                dCon3_dVd = t7 * (n * exp1 / tmp1 - vgLow * pParam.nD / n + t9 * pParam.vofD);
                dCon3_dVb = t7 * (t9 * invAa * dAa_dVb + vgLow * t8 + t9 * pParam.vofB);
                dCon4_dVd = tmp * dCon3_dVd - t7 * pParam.nD / n;
                dCon4_dVb = tmp * dCon3_dVb + t7 * t8;
                dCoeffb_dVd = dCon4_dVd * (t2 * t5 - t0 * t6) + dCon3_dVd * (t1 * t2 - t0 * t3);
                dCoeffc_dVd = dCon4_dVd * (t6 - t2 * t4) + dCon3_dVd * (t3 - t2);
                dCoeffd_dVd = dCon4_dVd * (t0 * t4 - t5) + dCon3_dVd * (t0 - t1);
                dCoeffa_dVd = sqrVghigh * (dCoeffc_dVd + dCoeffd_dVd * t0);
                dVgeff_dVd = -dVgeff_dVg * dVth_dVd + (dCoeffa_dVd + vgst * (dCoeffb_dVd + vgst * (dCoeffc_dVd + vgst * dCoeffd_dVd))) * delta;
                dCoeffb_dVb = dCon4_dVb * (t2 * t5 - t0 * t6) + dCon3_dVb * (t1 * t2 - t0 * t3);
                dCoeffc_dVb = dCon4_dVb * (t6 - t2 * t4) + dCon3_dVb * (t3 - t2);
                dCoeffd_dVb = dCon4_dVb * (t0 * t4 - t5) + dCon3_dVb * (t0 - t1);
                dCoeffa_dVb = sqrVghigh * (dCoeffc_dVb + dCoeffd_dVb * t0);
                dVgeff_dVb = -dVgeff_dVg * dVth_dVb + (dCoeffa_dVb + vgst * (dCoeffb_dVb + vgst * (dCoeffc_dVb + vgst * dCoeffd_dVb))) * delta;
            }
        }
        if (vgeff > 0) {
            uvert = 1 + vgeff * (ua + vgeff * ub);
            uvert = MathLib.max(uvert, 0.2);
            inv_Uvert = 1 / uvert;
            t8 = ua + 2 * ub * vgeff;
            dUvert_dVg = t8 * dVgeff_dVg;
            dUvert_dVd = t8 * dVgeff_dVd;
            dUvert_dVb = t8 * dVgeff_dVb + vgeff * (pParam.uaB + vgeff * pParam.ubB);
            t8 = u1s * invAa * inv_Uvert;
            vc = t8 * vgeff;
            t9 = vc * inv_Uvert;
            dVc_dVg = t8 * dVgeff_dVg - t9 * dUvert_dVg;
            dVc_dVd = t8 * dVgeff_dVd - t9 * dUvert_dVd;
            dVc_dVb = t8 * dVgeff_dVb + pParam.u1B * vgeff * invAa * inv_Uvert - vc * invAa * dAa_dVb - t9 * dUvert_dVb;
            tmp2 = MathLib.sqrt(1 + 2 * vc);
            kk = 0.5 * (1 + vc + tmp2);
            inv_Kk = 1 / kk;
            dKk_dVc = 0.5 + 0.5 / tmp2;
            sqrtKk = MathLib.sqrt(kk);
            t8 = invAa / sqrtKk;
            _vdsat = vgeff * t8;
            _vdsat = MathLib.max(_vdsat, 1e-18);
            inv_Vdsat = 1 / _vdsat;
            t9 = 0.5 * _vdsat * inv_Kk * dKk_dVc;
            dVdsat_dVd = t8 * dVgeff_dVd - t9 * dVc_dVd;
            dVdsat_dVg = t8 * dVgeff_dVg - t9 * dVc_dVg;
            dVdsat_dVb = t8 * dVgeff_dVb - t9 * dVc_dVb - _vdsat * invAa * dAa_dVb;
            beta0 = pParam.beta0 + pParam.beta0B * vbs;
            betas = pParam.betas0 + pParam.betasB * vbs;
            beta2 = pParam.beta20 + pParam.beta2B * vbs + pParam.beta2G * vgs;
            beta3 = pParam.beta30 + pParam.beta3B * vbs + pParam.beta3G * vgs;
            beta4 = pParam.beta40 + pParam.beta4B * vbs + pParam.beta4G * vgs;
            beta1 = betas - (beta0 + vdd * (beta3 - vdd * beta4));
            t0 = vds * beta2 * inv_Vdsat;
            t0 = MathLib.min(t0, 30);
            t1 = MathLib.exp(t0);
            t2 = t1 * t1;
            t3 = t2 + 1;
            tanh = (t2 - 1) / t3;
            sqrsech = 4 * t2 / (t3 * t3);
            Beta = beta0 + beta1 * tanh + vds * (beta3 - beta4 * vds);
            t4 = beta1 * sqrsech * inv_Vdsat;
            t5 = vdd * tanh;
            dBeta_dVd = beta3 - 2 * beta4 * vds + t4 * (beta2 - t0 * dVdsat_dVd);
            dBeta_dVg = t4 * (pParam.beta2G * vds - t0 * dVdsat_dVg) + pParam.beta3G * (vds - t5) - pParam.beta4G * (vds * vds - vdd * t5);
            dBeta1_dVb = pParam.Arg;
            dBeta_dVb = pParam.beta0B + dBeta1_dVb * tanh + vds * (pParam.beta3B - vds * pParam.beta4B) + t4 * (pParam.beta2B * vds - t0 * dVdsat_dVb);
            if (vgst > vgLow) {
                if (vds <= _vdsat) {
                    // Triode region
                    t3 = vds * inv_Vdsat;
                    t4 = t3 - 1;
                    t2 = 1 - pParam.u1D * t4 * t4;
                    u1 = u1s * t2;
                    utot = uvert + u1 * vds;
                    utot = MathLib.max(utot, 0.5);
                    inv_Utot = 1 / utot;
                    t5 = 2 * u1s * pParam.u1D * inv_Vdsat * t4;
                    dU1_dVd = t5 * (t3 * dVdsat_dVd - 1);
                    dU1_dVg = t5 * t3 * dVdsat_dVg;
                    dU1_dVb = t5 * t3 * dVdsat_dVb + pParam.u1B * t2;
                    dUtot_dVd = dUvert_dVd + u1 + vds * dU1_dVd;
                    dUtot_dVg = dUvert_dVg + vds * dU1_dVg;
                    dUtot_dVb = dUvert_dVb + vds * dU1_dVb;
                    tmp1 = (vgeff - 0.5 * aa * vds);
                    tmp3 = tmp1 * vds;
                    betaeff = Beta * inv_Utot;
                    idrain = betaeff * tmp3;
                    t6 = idrain / betaeff * inv_Utot;
                    gds = t6 * (dBeta_dVd - betaeff * dUtot_dVd) + betaeff * (tmp1 + (dVgeff_dVd - 0.5 * aa) * vds);
                    gm = t6 * (dBeta_dVg - betaeff * dUtot_dVg) + betaeff * vds * dVgeff_dVg;
                    gmbs = t6 * (dBeta_dVb - betaeff * dUtot_dVb) + betaeff * vds * (dVgeff_dVb - 0.5 * vds * dAa_dVb);
                } else {
                    // Saturation
                    tmp1 = vgeff * invAa * inv_Kk;
                    tmp3 = 0.5 * vgeff * tmp1;
                    betaeff = Beta * inv_Uvert;
                    idrain = betaeff * tmp3;
                    t0 = idrain / betaeff * inv_Uvert;
                    t1 = betaeff * vgeff * invAa * inv_Kk;
                    t2 = idrain * inv_Kk * dKk_dVc;
                    if (pParam.ai0 != 0) {
                        ai = pParam.ai0 + pParam.aiB * vbs;
                        bi = pParam.bi0 + pParam.biB * vbs;
                        t5 = bi / (vds - _vdsat);
                        t5 = MathLib.min(t5, 30);
                        t6 = MathLib.exp(-t5);
                        fR = 1 + ai * t6;
                        t7 = t5 / (vds - _vdsat);
                        t8 = (1 - fR) * t7;
                        dFR_dVd = t8 * (dVdsat_dVd - 1);
                        dFR_dVg = t8 * dVdsat_dVg;
                        dFR_dVb = t8 * dVdsat_dVb + t6 * (pParam.aiB - ai * pParam.biB / (vds - _vdsat));
                        gds = (t0 * (dBeta_dVd - betaeff * dUvert_dVd) + t1 * dVgeff_dVd - t2 * dVc_dVd) * fR + idrain * dFR_dVd;
                        gm = (t0 * (dBeta_dVg - betaeff * dUvert_dVg) + t1 * dVgeff_dVg - t2 * dVc_dVg) * fR + idrain * dFR_dVg;
                        gmbs = (t0 * (dBeta_dVb - betaeff * dUvert_dVb) + t1 * dVgeff_dVb - t2 * dVc_dVb - idrain * invAa * dAa_dVb) * fR + idrain * dFR_dVb;
                        idrain = idrain * fR;
                    } else {
                        gds = t0 * (dBeta_dVd - betaeff * dUvert_dVd) + t1 * dVgeff_dVd - t2 * dVc_dVd;
                        gds = t0 * (dBeta_dVg - betaeff * dUvert_dVg) + t1 * dVgeff_dVg - t2 * dVc_dVg;
                        gmbs = t0 * (dBeta_dVb - betaeff * dUvert_dVb) + t1 * dVgeff_dVb - t2 * dVc_dVb - idrain * invAa * dAa_dVb;
                    }
                } // End of Saturation
            } else {
                t0 = exp0 * exp0;
                t1 = exp1;
                idrain = Beta * vtm * vtm * t0 * (1 - t1);
                t2 = idrain / Beta;
                t4 = n * vtm;
                t3 = idrain / t4;
                if ((vds > _vdsat) && pParam.ai0 != 0) {
                    ai = pParam.ai0 + pParam.aiB * vbs;
                    bi = pParam.bi0 + pParam.biB * vbs;
                    t5 = bi / (vds - _vdsat);
                    t5 = MathLib.min(t5, 30);
                    t6 = MathLib.exp(-t5);
                    fR = 1 + ai * t6;
                    t7 = t5 / (vds - _vdsat);
                    t8 = (1 - fR) * t7;
                    dFR_dVd = t8 * (dVdsat_dVd - 1);
                    dFR_dVg = t8 * dVdsat_dVg;
                    dFR_dVb = t8 * dVdsat_dVb + t6 * (pParam.aiB - ai * pParam.biB / (vds - _vdsat));
                } else {
                    fR = 1;
                    dFR_dVd = 0;
                    dFR_dVg = 0;
                    dFR_dVb = 0;
                }
                gds = (t2 * dBeta_dVd + t3 * (pParam.vofD * t4 - dVth_dVd - pParam.nD * vgst / n) + Beta * vtm * t0 * t1) * fR + idrain * dFR_dVd;
                gm = (t2 * dBeta_dVg + t3) * fR + idrain * dFR_dVg;
                gmbs = (t2 * dBeta_dVb + t3 * (pParam.vofB * t4 - dVth_dVb + pParam.nB * vgst / (n * t1s * t1s) * dT1s_dVb)) * fR + idrain * dFR_dVb;
                idrain *= fR;
            }
        } else {
            idrain = 0;
            gm = 0;
            gds = 0;
            gmbs = 0;
        }
        // Limiting of DC parameters
        gds = MathLib.max(gds, 1e-20);
        if (channelChargePartitionFlag > 1 || (!chargeComputationNeeded
                && channelChargePartitionFlag > -5)) {
            qGate = 0;
            qDrain = 0;
            qBase = 0;
            cggb = 0;
            cgsb = 0;
            cgdb = 0;
            cdgb = 0;
            cdsb = 0;
            cddb = 0;
            cbgb = 0;
            cbsb = 0;
            cbdb = 0;
        } else {
            if (vbs < 0) {
                vbseff = vbs;
                dVbseff_dVb = 1;
            } else {
                vbseff = pParam.phi - phisb;
            }
            dVbseff_dVb = -dPhisb_dVb;
            arg1 = vgs - vbseff - pParam.vfb;
            arg2 = arg1 - vgst;
            qbulk = pParam.One_Third_CoxWL * arg2;
            dQblk_dVb = pParam.One_Third_CoxWL * (dVth_dVb - dVbseff_dVb);
            dQblk_dVd = pParam.One_Third_CoxWL * dVth_dVd;
            if (arg1 <= 0) {
                qGate = pParam.CoxWL * arg1;
                qBase = -qGate;
                qDrain = 0;
                cggb = pParam.CoxWL;
                cgdb = 0;
                cgsb = -cggb * (1 - dVbseff_dVb);
                cdgb = 0;
                cddb = 0;
                cdsb = 0;
                cbgb = -pParam.CoxWL;
                cbdb = 0;
                cbsb = -cgsb;
            } else if (vgst <= 0) {
                t2 = arg1 / arg2;
                t3 = t2 * t2 * (pParam.CoxWL - pParam.Two_Third_CoxWL * t2);
                qGate = pParam.CoxWL * arg1 * (1 - t2 * (1 - t2 / 3));
                qBase = -qGate;
                qDrain = 0;
                cggb = pParam.CoxWL * (1 - t2 * (2 - t2));
                tmp = t3 * dVth_dVb - (cggb + t3) * dVbseff_dVb;
                cgdb = t3 * dVth_dVd;
                cgsb = -(cggb + cgdb + tmp);
                cdgb = 0;
                cddb = 0;
                cdsb = 0;
                cbgb = -cggb;
                cbdb = -cgdb;
                cbsb = -cgsb;
            } else {
                if (vgst < pParam.vghigh) {
                    uvert = 1 + vgst * (ua + vgst * ub);
                    uvert = MathLib.max(uvert, 0.2);
                    inv_Uvert = 1 / uvert;
                    dUvert_dVg = ua + 2 * ub * vgst;
                    dUvert_dVd = -dUvert_dVg * dVth_dVd;
                    dUvert_dVb = -dUvert_dVg * dVth_dVb + vgst * (pParam.uaB + vgst * pParam.ubB);
                    t8 = u1s * invAa * inv_Uvert;
                    vc = t8 * vgst;
                    t9 = vc * inv_Uvert;
                    dVc_dVg = t8 - t9 * dUvert_dVg;
                    dVc_dVd = -t8 * dVth_dVd - t9 * dUvert_dVd;
                    dVc_dVb = -t8 * dVth_dVb + pParam.u1B * vgst * invAa * inv_Uvert - vc * invAa * dAa_dVb - t9 * dUvert_dVb;
                    tmp2 = MathLib.sqrt(1 + 2 * vc);
                    kk = 0.5 * (1 + vc + tmp2);
                    inv_Kk = 1 / kk;
                    dKk_dVc = 0.5 + 0.5 / tmp2;
                    sqrtKk = MathLib.sqrt(kk);
                    t8 = invAa / sqrtKk;
                    _vdsat = vgst * t8;
                    t9 = 0.5 * _vdsat * inv_Kk * dKk_dVc;
                    dVdsat_dVd = -t8 * dVth_dVd - t9 * dVc_dVd;
                    dVdsat_dVg = t8 - t9 * dVc_dVg;
                    dVdsat_dVb = -t8 * dVth_dVb - t9 * dVc_dVb - _vdsat * invAa * dAa_dVb;
                }
                if (vds >= _vdsat) {
                    // Saturation region
                    cggb = pParam.Two_Third_CoxWL;
                    cgdb = -cggb * dVth_dVd + dQblk_dVd;
                    tmp = -cggb * dVth_dVb + dQblk_dVb;
                    cgsb = -(cggb + cgdb + tmp);
                    cbgb = 0;
                    cbdb = -dQblk_dVd;
                    cbsb = dQblk_dVd + dQblk_dVb;
                    cdgb = -0.4 * cggb;
                    tmp = -cdgb * dVth_dVb;
                    cddb = -cdgb * dVth_dVd;
                    cdsb = -(cdgb + cddb + tmp);
                    qBase = -qbulk;
                    qGate = pParam.Two_Third_CoxWL * vgst + qbulk;
                    qDrain = cdgb * vgst;
                } else {
                    // Linear region
                    t7 = vds / _vdsat;
                    t8 = vgst / _vdsat;
                    t6 = t7 * t8;
                    t9 = 1 - t7;
                    vgdt = vgst * t9;
                    t0 = vgst / (vgst + vgdt);
                    t1 = vgdt / (vgst + vgdt);
                    t5 = t0 * t1;
                    t2 = 1 - t1 + t5;
                    t3 = 1 - t0 + t5;
                    dVgdt_dVg = t9 + t6 * dVdsat_dVg;
                    dVgdt_dVd = t6 * dVdsat_dVd - t8 - t9 * dVth_dVd;
                    dVgdt_dVb = t6 * dVdsat_dVb - t9 * dVth_dVb;
                    qGate = pParam.Two_Third_CoxWL * (vgst + vgdt - vgdt * t0) + qbulk;
                    qBase = -qbulk;
                    qDrain = -pParam.One_Third_CoxWL * (0.2 * vgdt + 0.8 * vgst + vgdt * t1 + 0.2 * t5 * (vgdt - vgst));
                    cggb = pParam.Two_Third_CoxWL * (t2 + t3 * dVgdt_dVg);
                    tmp = dQblk_dVb + pParam.Two_Third_CoxWL * (t3 * dVgdt_dVb - t2 * dVth_dVb);
                    cgdb = pParam.Two_Third_CoxWL * (t3 * dVgdt_dVd - t2 * dVth_dVd) + dQblk_dVd;
                    cgsb = -(cggb + cgdb + tmp);
                    t2 = 0.8 - 0.4 * t1 * (2 * t1 + t0 + t0 * (t1 - t0));
                    t3 = 0.2 + t1 + t0 * (1 - 0.4 * t0 * (t1 + 3 * t0));
                    cdgb = -pParam.One_Third_CoxWL * (t2 + t3 * dVgdt_dVg);
                    tmp = pParam.One_Third_CoxWL * (t2 * dVth_dVb - t3 * dVgdt_dVb);
                    cddb = pParam.One_Third_CoxWL * (t2 * dVth_dVd - t3 * dVgdt_dVd);
                    cdsb = -(cdgb + tmp + cddb);
                    cbgb = 0;
                    cbdb = -dQblk_dVd;
                    cbsb = dQblk_dVd + dQblk_dVb;
                }
            }
        }
        valuetypeflag = (int) channelChargePartitionFlag;
        switch (valuetypeflag) {
            case 0:
                idrain = MathLib.max(idrain, 1e-50);
                break;
            case -1:
                idrain = MathLib.max(idrain, 1e-50);
                break;
            case -2:
                idrain = gm;
                break;
            case -3:
                idrain = gds;
                break;
            case -4:
                idrain = 1 / gds;
                break;
            case -5:
                idrain = gmbs;
                break;
            case -6:
                idrain = qGate / 1e-12;
                break;
            case -7:
                idrain = qBase / 1e-12;
                break;
            case -8:
                idrain = qDrain / 1e-12;
                break;
            case -9:
                idrain = -(qBase + qGate + qDrain) / 1e-12;
                break;
            case -10:
                idrain = cggb / 1e-12;
                break;
            case -11:
                idrain = cgdb / 1e-12;
                break;
            case -12:
                idrain = cgsb / 1e-12;
                break;
            case -13:
                idrain = -(cggb + cgdb + cgsb) / 1e-12;
                break;
            case -14:
                idrain = cbgb / 1e-12;
                break;
            case -15:
                idrain = cbdb / 1e-12;
                break;
            case -16:
                idrain = cbsb / 1e-12;
                break;
            case -17:
                idrain = -(cbgb + cbdb + cbsb) / 1e-12;
                break;
            case -18:
                idrain = cdgb / 1e-12;
                break;
            case -19:
                idrain = cddb / 1e-12;
                break;
            case -20:
                idrain = cdsb / 1e-12;
                break;
            case -21:
                idrain = -(cdgb + cddb + cdsb) / 1e-12;
                break;
            case -22:
                idrain = -(cggb + cdgb + cbgb) / 1e-12;
                break;
            case -23:
                idrain = -(cgdb + cddb + cbdb) / 1e-12;
                break;
            case -24:
                idrain = -(cgsb + cdsb + cbsb) / 1e-12;
                break;
            default:
                idrain = MathLib.max(idrain, 1e-50);
                break;
        }
        von = type * vth;
        this.vdsat = type * _vdsat;
        return idrain;
    }

    public boolean getic() {
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
    Bool check = Bool.TRUE();
    Real ceq = Real.zero();
    Real geq = Real.zero();

    public boolean load(Mode mode) {
        double drnSatCurrent;
        double srcSatCurrent;
        double arg;
        double capbd = 0;
        double capbs = 0;
        double cbd = 0;
        double cbhat;
        double cbs = 0;
        double cd;
        double idrain = 0;
        double cdhat;
        double cdreq;
        double ceqbd = 0;
        double ceqbs = 0;
        double ceqqb = 0;
        double ceqqd = 0;
        double ceqqg = 0;
        double czbd;
        double czbdsw;
        double czbs;
        double czbssw;
        double delvbd;
        double delvbs;
        double delvds;
        double delvgd;
        double delvgs;
        double evbd;
        double evbs;
        double gbd = 0;
        double gbs = 0;
        double gcbdb = 0;
        double gcbgb = 0;
        double gcbsb = 0;
        double gcddb = 0;
        double gcdgb = 0;
        double gcdsb = 0;
        double gcgdb = 0;
        double gcggb = 0;
        double gcgsb = 0;
        double gcsdb = 0;
        double gcsgb = 0;
        double gcssb = 0;
        double sarg;
        double sargsw;
        double vbd = 0;
        double vbs = 0;
        double vcrit;
        double vds = 0;
        double vgb = 0;
        double vgd = 0;
        double vgdo;
        double vgs = 0;
        double xfact;
        double xnrm;
        double xrev;
        double csgb = 0;
        double cssb = 0;
        double csdb = 0;
        double phiB;
        double phiBSW;
        double mJ;
        double mJSW;
        double argsw;
        double qgate = 0;
        double qbulk = 0;
        double qdrn = 0;
        double qsrc = 0;
        double cqgate;
        double cqbulk;
        double cqdrn;
        boolean bypass = false;
        double tempv;
        double effectiveLength = l - deltaL * 1.e-6;
        double _drnArea = this.drnArea;
        double _srcArea = this.srcArea;
        double _drnPerimeter = this.drnPerimeter;
        double _srcPerimeter = this.srcPerimeter;
        if ((drnSatCurrent = _drnArea * jctSatCurDensity) < 1e-15) {
            drnSatCurrent = 1e-15;
        }
        if ((srcSatCurrent = _srcArea * jctSatCurDensity) < 1e-15) {
            srcSatCurrent = 1e-15;
        }
        double _gateSrcOverlapCap = this.gateSrcOverlapCap * w;
        double _gateDrnOverlapCap = this.gateDrnOverlapCap * w;
        double _gateBlkOverlapCap = this.gateBlkOverlapCap * effectiveLength;
        double _von = type * this.von;
        double _vdsat = type * this.vdsat;
        double _vt0 = type * pParam.vt0;
        KEY key = KEY.CASE1;
        if (mode.contains(MODE.INIT_SMSIG)) {
            vbs = vbsStates.get(0);
            vgs = vgsStates.get(0);
            vds = vdsStates.get(0);
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vbs = vbsStates.get(1);
            vgs = vgsStates.get(1);
            vds = vdsStates.get(1);
        } else if (mode.contains(MODE.INIT_JCT) && !off) {
            vds = type * icVDS;
            vgs = type * icVGS;
            vbs = type * icVBS;
            if (vds == 0 && vgs == 0 && vbs == 0
                    && (mode.contains(MODE.TRAN,
                    MODE.AC, MODE.DCOP, MODE.DCTRANCURVE)
                    || !mode.isUseIC())) {
                vbs = -1;
                vgs = _vt0;
                vds = 0;
            }
        } else if (mode.contains(MODE.INIT_JCT, MODE.INIT_FIX) && off) {
            vbs = vgs = vds = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vbsStates.set(0, vbsStates.get(1));
                vbs = (1 + xfact) * (vbsStates.get(1)) - (xfact * (vbsStates.get(2)));
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * (vgsStates.get(1)) - (xfact * (vgsStates.get(2)));
                vdsStates.set(0, vdsStates.get(1));
                vds = (1 + xfact) * (vdsStates.get(1)) - (xfact * (vdsStates.get(2)));
                vbdStates.set(0, vbsStates.get(0) - vdsStates.get(0));
                cdStates.set(0, cdStates.get(1));
                cbsStates.set(0, cbsStates.get(1));
                cbdStates.set(0, cbdStates.get(1));
                gmStates.set(0, gmStates.get(1));
                gdsStates.set(0, gdsStates.get(1));
                gmbsStates.set(0, gmbsStates.get(1));
                gbdStates.set(0, gbdStates.get(1));
                gbsStates.set(0, gbsStates.get(1));
                cggbStates.set(0, cggbStates.get(1));
                cbgbStates.set(0, cbgbStates.get(1));
                cbsbStates.set(0, cbsbStates.get(1));
                cgdbStates.set(0, cgdbStates.get(1));
                cgsbStates.set(0, cgsbStates.get(1));
                cbdbStates.set(0, cbdbStates.get(1));
                cdgbStates.set(0, cdgbStates.get(1));
                cddbStates.set(0, cddbStates.get(1));
                cdsbStates.set(0, cdsbStates.get(1));
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
            delvbs = vbs - vbsStates.get(0);
            delvbd = vbd - vbdStates.get(0);
            delvgs = vgs - vgsStates.get(0);
            delvds = vds - vdsStates.get(0);
            delvgd = vgd - vgdo;
            if (_mode >= 0) {
                cdhat = cdStates.get(0)
                        - gbdStates.get(0) * delvbd
                        + gmbsStates.get(0) * delvbs
                        + gmStates.get(0) * delvgs
                        + gdsStates.get(0) * delvds;
            } else {
                cdhat =
                        cdStates.get(0)
                        - (gbdStates.get(0)
                        - gmbsStates.get(0)) * delvbd
                        - gmStates.get(0) * delvgd
                        + gdsStates.get(0) * delvds;
            }
            cbhat =
                    cbsStates.get(0)
                    + cbdStates.get(0)
                    + gbdStates.get(0) * delvbd
                    + gbsStates.get(0) * delvbs;
            tempv = MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbsStates.get(0) + cbdStates.get(0))) + env.getAbsTol();
            if (!mode.contains(MODE.INIT_PRED) && env.isBypass()) {
                if ((MathLib.abs(delvbs) < (env.getRelTol() * MathLib.max(MathLib.abs(vbs),
                        MathLib.abs(vbsStates.get(0))) + env.getVoltTol()))) {
                    if ((MathLib.abs(delvbd) < (env.getRelTol() * MathLib.max(MathLib.abs(vbd),
                            MathLib.abs(vbdStates.get(0))) + env.getVoltTol()))) {
                        if ((MathLib.abs(delvgs) < (env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                                MathLib.abs(vgsStates.get(0))) + env.getVoltTol()))) {
                            if ((MathLib.abs(delvds) < (env.getRelTol() * MathLib.max(MathLib.abs(vds),
                                    MathLib.abs(vdsStates.get(0))) + env.getVoltTol()))) {
                                if ((MathLib.abs(cdhat - cdStates.get(0))
                                        < env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cdStates.get(0))) + env.getAbsTol())) {
                                    if ((MathLib.abs(cbhat - (cbsStates.get(0)
                                            + cbdStates.get(0))) < env.getRelTol() * tempv)) {
                                        vbs = vbsStates.get(0);
                                        vbd = vbdStates.get(0);
                                        vgs = vgsStates.get(0);
                                        vds = vdsStates.get(0);
                                        vgd = vgs - vds;
                                        vgb = vgs - vbs;
                                        cd = cdStates.get(0);
                                        cbs = cbsStates.get(0);
                                        cbd = cbdStates.get(0);
                                        idrain = _mode * (cd + cbd);
                                        gm = gmStates.get(0);
                                        gds = gdsStates.get(0);
                                        gmbs = gmbsStates.get(0);
                                        gbd = gbdStates.get(0);
                                        gbs = gbsStates.get(0);
                                        bypass = true;
                                        if (mode.contains(MODE.TRAN, MODE.AC)
                                                || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                                            cggb = cggbStates.get(0);
                                            cgdb = cgdbStates.get(0);
                                            cgsb = cgsbStates.get(0);
                                            cbgb = cbgbStates.get(0);
                                            cbdb = cbdbStates.get(0);
                                            cbsb = cbsbStates.get(0);
                                            cdgb = cdgbStates.get(0);
                                            cddb = cddbStates.get(0);
                                            cdsb = cdsbStates.get(0);
                                            capbs = capbsStates.get(0);
                                            capbd = capbdStates.get(0);
                                            key = KEY.CASE1;//line755;
                                        } else {
                                            key = KEY.CASE2;//line850;
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
                    vds = -MOSUtils.vdsLimit(-vds, -(vdsStates.get(0)));
                    vgs = vgd + vds;
                }
                if (vds >= 0) {
                    vcrit = VT0 * MathLib.log(VT0 / (ROOT2 * srcSatCurrent));
                    vbs = MOSUtils.pnjLimit(vbs, vbsStates.get(0), VT0, vcrit, check); /* B2 test */
                    vbd = vbs - vds;
                } else {
                    vcrit = VT0 * MathLib.log(VT0 / (ROOT2 * drnSatCurrent));
                    vbd = MOSUtils.pnjLimit(vbd, vbdStates.get(0), VT0, vcrit, check); /* B2 test */
                    vbs = vbd + vds;
                }
            }
        }
        if (!bypass) {
            /* determine DC current and derivatives */
            vbd = vbs - vds;
            vgd = vgs - vds;
            vgb = vgs - vbs;
            if (vbs <= 0) {
                gbs = srcSatCurrent / VT0 + env.getGMin();
                cbs = gbs * vbs;
            } else {
                evbs = MathLib.exp(vbs / VT0);
                gbs = srcSatCurrent * evbs / VT0 + env.getGMin();
                cbs = srcSatCurrent * (evbs - 1) + env.getGMin() * vbs;
            }
            if (vbd <= 0) {
                gbd = drnSatCurrent / VT0 + env.getGMin();
                cbd = gbd * vbd;
            } else {
                evbd = MathLib.exp(vbd / VT0);
                gbd = drnSatCurrent * evbd / VT0 + env.getGMin();
                cbd = drnSatCurrent * (evbd - 1) + env.getGMin() * vbd;
            }
            if (vds >= 0) {
                // Normal mode
                _mode = 1;
            } else {
                // Inverse mode
                _mode = -1;
            }
            if (vds >= 0) {
                idrain = evaluate(mode, vds, vbs, vgs);
                qgate = qGate;
                qbulk = qBase;
                qdrn = qDrain;
            } else {
                idrain = evaluate(mode, -vds, vbd, vgd);
                qgate = qGate;
                qbulk = qBase;
                qsrc = qDrain;
                csgb = cdgb;
                cssb = cddb;
                csdb = cdsb;
            }
            // Compute drain current source
            cd = _mode * idrain - cbd;
            if (mode.contains(MODE.TRAN, MODE.AC, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                // Charge storage elements
                czbd = unitAreaJctCap * _drnArea;
                czbs = unitAreaJctCap * _srcArea;
                czbdsw = unitLengthSidewallJctCap * _drnPerimeter;
                czbssw = unitLengthSidewallJctCap * _srcPerimeter;
                phiB = blkJctPotential;
                phiBSW = sidewallJctPotential;
                mJ = blkJctBotGradingCoeff;
                mJSW = blkJctSideGradingCoeff;
                // Source bulk junction
                if (vbs < 0) {
                    arg = 1 - vbs / phiB;
                    argsw = 1 - vbs / phiBSW;
                    sarg = MathLib.exp(-mJ * MathLib.log(arg));
                    sargsw = MathLib.exp(-mJSW * MathLib.log(argsw));
                    qbsStates.set(0,
                            phiB * czbs * (1 - arg * sarg) / (1 - mJ) + phiBSW
                            * czbssw * (1 - argsw * sargsw) / (1 - mJSW));
                    capbs = czbs * sarg + czbssw * sargsw;
                } else {
                    qbsStates.set(0,
                            vbs * (czbs + czbssw) + vbs * vbs * (czbs * mJ * 0.5 / phiB + czbssw * mJSW * 0.5 / phiBSW));
                    capbs = czbs + czbssw + vbs * (czbs * mJ / phiB
                            + czbssw * mJSW / phiBSW);
                }
                // Drain bulk junction
                if (vbd < 0) {
                    arg = 1 - vbd / phiB;
                    argsw = 1 - vbd / phiBSW;
                    sarg = MathLib.exp(-mJ * MathLib.log(arg));
                    sargsw = MathLib.exp(-mJSW * MathLib.log(argsw));
                    qbdStates.set(0,
                            phiB * czbd * (1 - arg * sarg) / (1 - mJ) + phiBSW
                            * czbdsw * (1 - argsw * sargsw) / (1 - mJSW));
                    capbd = czbd * sarg + czbdsw * sargsw;
                } else {
                    qbdStates.set(0,
                            vbd * (czbd + czbdsw) + vbd * vbd * (czbd * mJ * 0.5 / phiB + czbdsw * mJSW * 0.5 / phiBSW));
                    capbd = czbd + czbdsw + vbd * (czbd * mJ / phiB
                            + czbdsw * mJSW / phiBSW);
                }
            }
            // Check convergence
            if ((!off || !mode.contains(MODE.INIT_FIX)) && check.isTrue()) {
                ckt.setNonConverged(true);
            }
            vbsStates.set(0, vbs);
            vbdStates.set(0, vbd);
            vgsStates.set(0, vgs);
            vdsStates.set(0, vds);
            cdStates.set(0, cd);
            cbsStates.set(0, cbs);
            cbdStates.set(0, cbd);
            gmStates.set(0, gm);
            gdsStates.set(0, gds);
            gmbsStates.set(0, gmbs);
            gbdStates.set(0, gbd);
            gbsStates.set(0, gbs);
            cggbStates.set(0, cggb);
            cgdbStates.set(0, cgdb);
            cgsbStates.set(0, cgsb);
            cbgbStates.set(0, cbgb);
            cbdbStates.set(0, cbdb);
            cbsbStates.set(0, cbsb);
            cdgbStates.set(0, cdgb);
            cddbStates.set(0, cddb);
            cdsbStates.set(0, cdsb);
            capbsStates.set(0, capbs);
            capbdStates.set(0, capbd);
            // bulk and channel charge plus overlaps
            if (!mode.contains(MODE.TRAN, MODE.AC)
                    && (!mode.contains(MODE.TRANOP) || !mode.isUseIC())
                    && !mode.contains(MODE.INIT_SMSIG)) {
                key = KEY.CASE2;//line850;
            }
        }
        while (key
                != KEY.DONE) {
            switch (key) {
                case CASE1://line755:
                    if (_mode > 0) {
                        double qgd;
                        double qgs;
                        double qgb;
                        double ag0;
                        ag0 = tmprl.getAgAt(0);
                        // Compute equivalent conductance
                        gcdgb = (cdgb - _gateDrnOverlapCap) * ag0;
                        gcddb = (cddb + capbd + _gateDrnOverlapCap) * ag0;
                        gcdsb = cdsb * ag0;
                        gcsgb = -(cggb + cbgb + cdgb + _gateSrcOverlapCap) * ag0;
                        gcsdb = -(cgdb + cbdb + cddb) * ag0;
                        gcssb = (capbs + _gateSrcOverlapCap
                                - (cgsb + cbsb + cdsb)) * ag0;
                        gcggb = (cggb + _gateDrnOverlapCap
                                + _gateSrcOverlapCap + _gateBlkOverlapCap) * ag0;
                        gcgdb = (cgdb - _gateDrnOverlapCap) * ag0;
                        gcgsb = (cgsb - _gateSrcOverlapCap) * ag0;
                        gcbgb = (cbgb - _gateBlkOverlapCap) * ag0;
                        gcbdb = (cbdb - capbd) * ag0;
                        gcbsb = (cbsb - capbs) * ag0;
                        // Compute total terminal charge
                        qgd = _gateDrnOverlapCap * vgd;
                        qgs = _gateSrcOverlapCap * vgs;
                        qgb = _gateBlkOverlapCap * vgb;
                        qgate = qgate + qgd + qgs + qgb;
                        qbulk = qbulk - qgb;
                        qdrn = qdrn - qgd;
                        qsrc = -(qgate + qbulk + qdrn);
                    } else {
                        double qgd;
                        double qgs;
                        double qgb;
                        double ag0;
                        ag0 = tmprl.getAgAt(0);
                        // Compute equivalent conductance
                        gcsgb = (cdgb - _gateSrcOverlapCap) * ag0;
                        gcssb = (cddb + capbs + _gateSrcOverlapCap) * ag0;
                        gcsdb = cdsb * ag0;
                        gcdgb = -(cggb + cbgb + cdgb + _gateDrnOverlapCap) * ag0;
                        gcdsb = -(cgsb + cbdb + cddb) * ag0;
                        gcddb = (capbd + _gateDrnOverlapCap
                                - (cgdb + cbsb + cdsb)) * ag0;
                        gcggb = (cggb + _gateSrcOverlapCap
                                + _gateDrnOverlapCap + _gateBlkOverlapCap) * ag0;
                        gcgsb = (cgsb - _gateSrcOverlapCap) * ag0;
                        gcgdb = (cgdb - _gateDrnOverlapCap) * ag0;
                        gcbgb = (cbgb - _gateBlkOverlapCap) * ag0;
                        gcbsb = (cbdb - capbs) * ag0;
                        gcbdb = (cbsb - capbd) * ag0;
                        // Compute total terminal charge
                        qgd = _gateSrcOverlapCap * vgd;
                        qgs = _gateDrnOverlapCap * vgs;
                        qgb = _gateBlkOverlapCap * vgb;
                        qgate = qgate + qgd + qgs + qgb;
                        qbulk = qbulk - qgb;
                        qsrc = qsrc - qgd;
                        qdrn = -(qgate + qbulk + qsrc);
                    }
                    if (bypass) {
                        key = KEY.CASE3;//line860;
                        break;
                    }
                    qgStates.set(0, qgate);
                    qdStates.set(0, qdrn - qbdStates.get(0));
                    qbStates.set(0, qbulk + qbdStates.get(0) + qbsStates.get(0));
                    //??? ck
                    if (!mode.contains(MODE.AC, MODE.TRAN)
                            && mode.contains(MODE.TRANOP) && mode.isUseIC()) {
                        key = KEY.CASE2;//line850;
                        break;
                    }
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        cggbStates.set(0, cggb);
                        cgdbStates.set(0, cgdb);
                        cgsbStates.set(0, cgsb);
                        cbgbStates.set(0, cbgb);
                        cbdbStates.set(0, cbdb);
                        cbsbStates.set(0, cbsb);
                        cdgbStates.set(0, cdgb);
                        cddbStates.set(0, cddb);
                        cdsbStates.set(0, cdsb);
                        capbdStates.set(0, capbd);
                        capbsStates.set(0, capbs);
                        return true;
                    }
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qbStates.set(1, qbStates.get(0));
                        qgStates.set(1, qgStates.get(0));
                        qdStates.set(1, qdStates.get(0));
                    }
                    stateTable.integrate(geq, ceq, 0, qbStates, cqbStates);
                    stateTable.integrate(geq, ceq, 0, qgStates, cqgStates);
                    stateTable.integrate(geq, ceq, 0, qdStates, cqdStates);
                    key = key.CASE3;//line860;
                    break;
                case CASE2://line850:
                    ceqqg = ceqqb = ceqqd = 0;
                    gcdgb = gcddb = gcdsb = 0;
                    gcsgb = gcsdb = gcssb = 0;
                    gcggb = gcgdb = gcgsb = 0;
                    gcbgb = gcbdb = gcbsb = 0;
                    key = KEY.DONE;//line900;
                    break;
                case CASE3://line860:
                    cqgate = iqgStates.get(0);
                    cqbulk = iqbStates.get(0);
                    cqdrn = iqdStates.get(0);
                    ceqqg = cqgate - gcggb * vgb + gcgdb * vbd + gcgsb * vbs;
                    ceqqb = cqbulk - gcbgb * vgb + gcbdb * vbd + gcbsb * vbs;
                    ceqqd = cqdrn - gcdgb * vgb + gcddb * vbd + gcdsb * vbs;
                    if (mode.contains(MODE.INIT_TRAN)) {
                        iqbStates.set(1, iqbStates.get(0));
                        iqgStates.set(1, iqgStates.get(0));
                        iqdStates.set(1, iqdStates.get(0));
                    }
                default:
                    key = KEY.DONE;
            }
        }
        // Load current vector
        ceqbs = type * (cbs - (gbs - env.getGMin()) * vbs);
        ceqbd = type * (cbd - (gbd - env.getGMin()) * vbd);
        ceqqg = type * ceqqg;
        ceqqb = type * ceqqb;
        ceqqd = type * ceqqd;
        if (_mode >= 0) {
            xnrm = 1;
            xrev = 0;
            cdreq = type * (idrain - gds * vds - gm * vgs - gmbs * vbs);
        } else {
            xnrm = 0;
            xrev = 1;
            cdreq = -type * (idrain + gds * vds - gm * vgd - gmbs * vbd);
        }

        wrk.getRhsAt(gateIndex).realMinusEq(m
                * ceqqg);

        wrk.getRhsAt(blkIndex).realMinusEq(m
                * (ceqbs
                + ceqbd + ceqqb));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (ceqbd - cdreq - ceqqd));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqbs + ceqqg + ceqqb + ceqqd));
        // Load y matrix
        drnDrnNode.realPlusEq(m * drnConductance);
        ggNode.realPlusEq(m * gcggb);
        srcSrcNode.realPlusEq(m * srcConductance);
        blkBlkNode.realPlusEq(m * (gbd + gbs - gcbgb - gcbdb - gcbsb));
        drnPrmDrnPrmNode.realPlusEq(m * (drnConductance + gds + gbd + xrev * (gm + gmbs) + gcddb));
        srcPrmSrcPrmNode.realPlusEq(m * (srcConductance + gds + gbs + xnrm * (gm + gmbs) + gcssb));
        drnDrnPrmNode.realPlusEq(m * -drnConductance);
        gateBlkNode.realPlusEq(m * (-gcggb - gcgdb - gcgsb));
        gateDrnPrmNode.realPlusEq(m * gcgdb);
        gateSrcPrmNode.realPlusEq(m * gcgsb);
        srcSrcPrmNode.realPlusEq(m * -srcConductance);
        blkGateNode.realPlusEq(m * gcbgb);
        blkDrnPrmNode.realPlusEq(m * (-gbd + gcbdb));
        blkSrcPrmNode.realPlusEq(m * (-gbs + gcbsb));
        drnPrmDrnNode.realPlusEq(m * -drnConductance);
        drnPrmGateNode.realPlusEq(m * ((xnrm - xrev) * gm + gcdgb));
        drnPrmBlkNode.realPlusEq(m * (-gbd + (xnrm - xrev) * gmbs - gcdgb - gcddb - gcdsb));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - xnrm * (gm + gmbs) + gcdsb));
        srcPrmGateNode.realPlusEq(m * (-(xnrm - xrev) * gm + gcsgb));
        srcPrmSrcNode.realPlusEq(m * -srcConductance);
        srcPrmBlkNode.realPlusEq(m * (-gbs - (xnrm - xrev) * gmbs - gcsgb - gcsdb - gcssb));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds - xrev * (gm + gmbs) + gcsdb));



        return true;
    }
    /* routine to calculate equivalent conductance and total terminal
     * charges
     */

    void mosCap(double vgd, double vgs, double vgb, double[] args,
            /*
            double GateDrnOverlapCap, double GateSrcOverlapCap,
            double GateBlkOverlapCap, double capbd, double capbs,
            double cggb, double cgdb, double cgsb, */
            double cbgb, double cbdb, double cbsb, double cdgb, double cddb,
            double cdsb, double[] rVals) {
        double gcggbPointer = rVals[0];


        double gcgdbPointer = rVals[1];


        double gcgsbPointer = rVals[2];


        double gcbgbPointer = rVals[3];


        double gcbdbPointer = rVals[4];


        double gcbsbPointer = rVals[5];


        double gcdgbPointer = rVals[6];


        double gcddbPointer = rVals[7];


        double gcdsbPointer = rVals[8];


        double gcsgbPointer = rVals[9];


        double gcsdbPointer = rVals[10];


        double gcssbPointer = rVals[11];


        double qGatePointer = rVals[12];


        double qBlkPointer = rVals[13];


        double qDrnPointer = rVals[14];


        double qSrcPointer = rVals[15];


        double qgd;


        double qgs;


        double qgb;


        double ag0;
        ag0 = tmprl.getAgAt(0);
        /* compute equivalent conductance */
        gcdgbPointer = (cdgb - args[0]) * ag0;
        gcddbPointer = (cddb + args[3] + args[0]) * ag0;
        gcdsbPointer = cdsb * ag0;
        gcsgbPointer = -(args[5] + cbgb + cdgb + args[1]) * ag0;
        gcsdbPointer = -(args[6] + cbdb + cddb) * ag0;
        gcssbPointer = (args[4] + args[1]
                - (args[7] + cbsb + cdsb)) * ag0;
        gcggbPointer = (args[5] + args[0]
                + args[1] + args[2]) * ag0;
        gcgdbPointer = (args[6] - args[0]) * ag0;
        gcgsbPointer = (args[7] - args[1]) * ag0;
        gcbgbPointer = (cbgb - args[2]) * ag0;
        gcbdbPointer = (cbdb - args[3]) * ag0;
        gcbsbPointer = (cbsb - args[4]) * ag0;
        qgd = args[0] * vgd;
        qgs = args[1] * vgs;
        qgb = args[2] * vgb;
        qGatePointer = qGatePointer + qgd + qgs + qgb;
        qBlkPointer = qBlkPointer - qgb;
        qDrnPointer = qDrnPointer - qgd;
        qSrcPointer = -(qGatePointer + qBlkPointer + qDrnPointer);


    }

    public boolean unSetup() {
        if (drnPrmIndex != 0 && drnPrmIndex != drnIndex) {
            ckt.deleteNode(drnPrmIndex);


        }
        drnPrmIndex = 0;


        if (srcPrmIndex != 0 && srcPrmIndex != srcIndex) {
            ckt.deleteNode(srcPrmIndex);


        }
        srcPrmIndex = 0;


        return true;


    }

    public boolean temperature() {
        BSIM2SizeDependParam _pSizeDependParamKnot,
                pLastKnot;


        double effectiveLength;


        double effectiveWidth;


        double coxWoverL,
                inv_L,
                inv_W,
                tmp;


        if (blkJctPotential < 0.1) {
            blkJctPotential = 0.1;


        }
        if (sidewallJctPotential < 0.1) {
            sidewallJctPotential = 0.1;


        }
        cox = 3.453e-13 / (tox * 1e-4);
        vdd2 = 2 * vdd;
        vgg2 = 2 * vgg;
        vbb2 = 2 * vbb;
        vtm = 8.625e-5 * (temp + 273);
        _pSizeDependParamKnot = null;
        pLastKnot = null;
        _pSizeDependParamKnot = this.pSizeDependParamKnot;


        boolean size_Not_Found = true;


        while (_pSizeDependParamKnot != null && size_Not_Found) {
            if (l == _pSizeDependParamKnot.Length
                    && w == _pSizeDependParamKnot.Width) {
                size_Not_Found = false;
                pParam = _pSizeDependParamKnot;


            } else {
                pLastKnot = _pSizeDependParamKnot;


            }
            _pSizeDependParamKnot = _pSizeDependParamKnot.pNext;


        }
        if (size_Not_Found) {
            pParam = new BSIM2SizeDependParam();


            if (pLastKnot == null) {
                _pSizeDependParamKnot = pParam;


            } else {
                pLastKnot.pNext = pParam;
                pParam.pNext = null;


            }
            effectiveLength = l - deltaL * 1e-6;
            effectiveWidth = w - deltaW * 1e-6;


            if (effectiveLength <= 0) {
                StandardLog.severe("B2: mosfet " + getModelName() + ", model "
                        + getInstName() + ": Effective channel length <= 0");


                return false;


            }
            if (effectiveWidth <= 0) {
                String[] namarray = new String[]{getModelName(), getInstName()};
                StandardLog.severe("B2: mosfet " + getModelName() + ", model "
                        + getInstName() + ": Effective channel width <= 0");


                return false;


            }
            inv_L = 1e-6 / effectiveLength;
            inv_W = 1e-6 / effectiveWidth;
            pParam.Width = w;
            pParam.Length = l;
            pParam.vfb = vfb0 + vfbW * inv_W + vfbL * inv_L;
            pParam.phi = phi0 + phiW * inv_W + phiL * inv_L;
            pParam.k1 = k10 + k1W * inv_W + k1L * inv_L;
            pParam.k2 = k20 + k2W * inv_W + k2L * inv_L;
            pParam.eta0 = eta00 + eta0W * inv_W + eta0L * inv_L;
            pParam.etaB = etaB0 + etaBW * inv_W + etaBL * inv_L;
            pParam.beta0 = mob00;
            pParam.beta0B = mob0B0 + mob0BW * inv_W + mob0BL * inv_L;
            pParam.betas0 = mobs00 + mobs0W * inv_W + mobs0L * inv_L;


            if (pParam.betas0 < 1.01 * pParam.beta0) {
                pParam.betas0 = 1.01 * pParam.beta0;


            }
            pParam.betasB = mobsB0 + mobsBW * inv_W + mobsBL * inv_L;
            tmp = (pParam.betas0 - pParam.beta0 - pParam.beta0B * vbb);


            if ((-pParam.betasB * vbb) > tmp) {
                pParam.betasB = -tmp / vbb;


            }
            pParam.beta20 = mob200 + mob20W * inv_W + mob20L * inv_L;
            pParam.beta2B = mob2B0 + mob2BW * inv_W + mob2BL * inv_L;
            pParam.beta2G = mob2G0 + mob2GW * inv_W + mob2GL * inv_L;
            pParam.beta30 = mob300 + mob30W * inv_W + mob30L * inv_L;
            pParam.beta3B = mob3B0 + mob3BW * inv_W + mob3BL * inv_L;
            pParam.beta3G = mob3G0 + mob3GW * inv_W + mob3GL * inv_L;
            pParam.beta40 = mob400 + mob40W * inv_W + mob40L * inv_L;
            pParam.beta4B = mob4B0 + mob4BW * inv_W + mob4BL * inv_L;
            pParam.beta4G = mob4G0 + mob4GW * inv_W + mob4GL * inv_L;
            coxWoverL = cox * effectiveWidth / effectiveLength;
            pParam.beta0 *= coxWoverL;
            pParam.beta0B *= coxWoverL;
            pParam.betas0 *= coxWoverL;
            pParam.betasB *= coxWoverL;
            pParam.beta30 *= coxWoverL;
            pParam.beta3B *= coxWoverL;
            pParam.beta3G *= coxWoverL;
            pParam.beta40 *= coxWoverL;
            pParam.beta4B *= coxWoverL;
            pParam.beta4G *= coxWoverL;
            pParam.ua0 = ua00 + ua0W * inv_W + ua0L * inv_L;
            pParam.uaB = uaB0 + uaBW * inv_W + uaBL * inv_L;
            pParam.ub0 = ub00 + ub0W * inv_W + ub0L * inv_L;
            pParam.ubB = ubB0 + ubBW * inv_W + ubBL * inv_L;
            pParam.u10 = u100 + u10W * inv_W + u10L * inv_L;
            pParam.u1B = u1B0 + u1BW * inv_W + u1BL * inv_L;
            pParam.u1D = u1D0 + u1DW * inv_W + u1DL * inv_L;
            pParam.n0 = n00 + n0W * inv_W + n0L * inv_L;
            pParam.nB = nB0 + nBW * inv_W + nBL * inv_L;
            pParam.nD = nD0 + nDW * inv_W + nDL * inv_L;


            if (pParam.n0 < 0) {
                pParam.n0 = 0;


            }
            pParam.vof0 = vof00 + vof0W * inv_W + vof0L * inv_L;
            pParam.vofB = vofB0 + vofBW * inv_W + vofBL * inv_L;
            pParam.vofD = vofD0 + vofDW * inv_W + vofDL * inv_L;
            pParam.ai0 = ai00 + ai0W * inv_W + ai0L * inv_L;
            pParam.aiB = aiB0 + aiBW * inv_W + aiBL * inv_L;
            pParam.bi0 = bi00 + bi0W * inv_W + bi0L * inv_L;
            pParam.biB = biB0 + biBW * inv_W + biBL * inv_L;
            pParam.vghigh = vghigh0 + vghighW * inv_W + vghighL * inv_L;
            pParam.vglow = vglow0 + vglowW * inv_W + vglowL * inv_L;
            pParam.CoxWL = cox * effectiveLength * effectiveWidth * 1e4;
            pParam.One_Third_CoxWL = pParam.CoxWL / 3;
            pParam.Two_Third_CoxWL = 2 * pParam.One_Third_CoxWL;
            pParam.gSoverlapCap = gateSrcOverlapCap * effectiveWidth;
            pParam.gDoverlapCap = gateDrnOverlapCap * effectiveWidth;
            pParam.gBoverlapCap = gateBlkOverlapCap * effectiveLength;
            pParam.SqrtPhi = MathLib.sqrt(pParam.phi);
            pParam.Phis3 = pParam.SqrtPhi * pParam.phi;
            pParam.Arg = pParam.betasB - pParam.beta0B - vdd * (pParam.beta3B - vdd * pParam.beta4B);


        }
        if ((drnConductance = sheetResistance * drnSquares) != 0) {
            drnConductance = 1. / drnConductance;


        }
        if ((srcConductance = sheetResistance * srcSquares) != 0) {
            srcConductance = 1. / srcConductance;


        }
        pParam.vt0 = pParam.vfb + pParam.phi + pParam.k1 * pParam.SqrtPhi - pParam.k2 * pParam.phi;


        this.von = pParam.vt0;


        return true;


    }

    public double truncateTimeStep(double timeStep) {
        double debugtemp = timeStep;
        timeStep = stateTable.terr(qbStates, cqbStates, timeStep);
        timeStep = stateTable.terr(qgStates, cqgStates, timeStep);
        timeStep = stateTable.terr(qdStates, cqdStates, timeStep);


        if (debugtemp != timeStep) {
            StandardLog.info(String.format("device %s reduces step from %g to %g",
                    getInstName(), debugtemp, timeStep));


        }
        return timeStep;


    }

    public boolean accept(Mode mode) {
        return true;


    }

    public boolean loadInitCond() {
        return true;

    }
}
