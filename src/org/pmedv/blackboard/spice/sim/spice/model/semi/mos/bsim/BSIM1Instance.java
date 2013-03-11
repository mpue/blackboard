/*
 * bsim1.java
 *
 * Created on August 28, 2006, 1:55 PM
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
public class BSIM1Instance extends BSIM1ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    protected int drnPrmIndex;

    /* Internal source node index */
    protected int srcPrmIndex;

    /* Source conductance */
    protected double srcConductance;

    /* Drain conductance */
    protected double drnConductance;
    protected double von;
    protected double vdsat;
    /* Device _mode : 1 = normal, -1 = inverse */
    protected int _mode;

    /* Flat band voltage at given L and W */
    protected double vfb;

    /* Bulk effect coefficient 1 */
    protected double k1;

    /* Bulk effect coefficient 2 */
    protected double k2;

    /* Drain induced barrier lowering */
    protected double eta;

    /* Vbs dependence of eta */
    protected double etaB;

    /* Vds dependence of eta */
    protected double etaD;

    /* Beta at vds = 0 and vgs = vth */
    protected double betaZero;

    /* Vbs dependence of betaZero */
    protected double betaZeroB;

    /* Beta at vds = vdd and vgs = vth */
    protected double betaVdd;

    /* Vbs dependence of betaVdd */
    protected double betaVddB;

    /* Vds dependence of betaVdd */
    protected double betaVddD;

    /* Mobility degradation due to gate field */
    protected double ugs;

    /* Vbs dependence of ugs */
    protected double ugsB;

    /* Drift velocity saturation due to vds */
    protected double uds;

    /* Vbs dependence of uds */
    protected double udsB;

    /* Vds dependence of uds */
    protected double udsD;

    /* Slope of subthreshold current with vgs */
    protected double subthSlope;

    /* Vbs dependence of subthreshold slope */
    protected double subthSlopeB;

    /* Vds dependence of subthreshold slope */
    protected double subthSlopeD;

    /* Adjusted gate drain overlap capacitance */
    protected double gDoverlapCap;

    /* Adjusted gate source overlap capacitance */
    protected double gSoverlapCap;

    /* Adjusted gate bulk overlap capacitance */
    protected double gtBlkOverlapCap;
    protected double cox;
    private Complex drnDrnNode;
    private Complex srcSrcNode;
    private Complex drnPrmDrnPrmNode;
    private Complex drnDrnPrmNode;
    private Complex gateDrnPrmNode;
    private Complex srcPrmSrcNode;
    private Complex blkSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPrmGateNode;
    //    private Complex srcPrmSrcNode;
    private Complex srcPrmBlkNode;
    private Complex gateGateNode;
    private Complex blkBlkNode;
    private Complex srcPrmSrcPrmNode;
    private Complex gateBlkNode;
    private Complex srcPrmDrnPrmNode;
    private Complex drnPrmBlkNode;
    private Complex srcPrmGateNode;
    private Complex blkGateNode;
    private Complex drnPrmSrcPrmNode;
    private Complex blkDrnPrmNode;
    private Complex gateSrcPrmNode;
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
    double gm = 0;
    double gds = 0;
    double gmbs = 0;
    double qg;
    double qb;
    double qd;
    double cgdb = 0;
    double cgsb = 0;
    double cggb;
    double cbgb = 0;
    double cbdb = 0;
    double cbsb = 0;
    double cdgb = 0;
    double cddb = 0;
    double cdsb = 0;
    private Real geq = Real.zero();
    private Bool check = Bool.FALSE();
    private Real ceq = Real.zero();
//    double[] rVals = new double[3];

    /** Creates a new instance of bsim1 */
    public BSIM1Instance() {
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
        if (sheetResistance != 0 && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if (sheetResistance != 0 && srcPrmIndex == 0) {
            if (srcPrmIndex == 0) {
                srcPrmIndex = ckt.makeVoltNode(getInstName() + "source").getIndex();
            }
        } else {
            srcPrmIndex = srcIndex;
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
        srcPrmSrcNode = wrk.aquireNode(srcIndex, srcPrmIndex);
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
        //    Charge parameters
        cggb = cggbStates.get(0);
        cgsb = cgsbStates.get(0);
        cgdb = cgdbStates.get(0);
        cbgb = cbgbStates.get(0);
        cbsb = cbsbStates.get(0);
        cbdb = cbdbStates.get(0);
        cdgb = cdgbStates.get(0);
        cdsb = cdsbStates.get(0);
        cddb = cddbStates.get(0);
        double omega = tmprl.getOmega();
        double xcdgb = (cdgb - gDoverlapCap) * omega;
        double xcddb = (cddb + capbd + gDoverlapCap) * omega;
        double xcdsb = cdsb * omega;
        double xcsgb = -(cggb + cbgb + cdgb + gSoverlapCap) * omega;
        double xcsdb = -(cgdb + cbdb + cddb) * omega;
        double xcssb = (capbs + gSoverlapCap - (cgsb + cbsb + cdsb)) * omega;
        double xcggb = (cggb + gDoverlapCap + gSoverlapCap
                + gtBlkOverlapCap) * omega;
        double xcgdb = (cgdb - gDoverlapCap) * omega;
        double xcgsb = (cgsb - gSoverlapCap) * omega;
        double xcbgb = (cbgb - gtBlkOverlapCap) * omega;
        double xcbdb = (cbdb - capbd) * omega;
        double xcbsb = (cbsb - capbs) * omega;
        gateGateNode.imagPlusEq(m * xcggb);
        blkBlkNode.imagPlusEq(m * (-xcbgb - xcbdb - xcbsb));
        drnPrmDrnPrmNode.imagPlusEq(m * xcddb);
        srcPrmSrcPrmNode.imagPlusEq(m * xcssb);
        gateBlkNode.imagPlusEq(m * (-xcggb - xcgdb - xcgsb));
        gateDrnPrmNode.imagPlusEq(m * xcgdb);
        gateSrcPrmNode.imagPlusEq(m * xcgsb);
        blkGateNode.imagPlusEq(m * xcbgb);
        blkDrnPrmNode.imagPlusEq(m * xcbdb);
        blkSrcPrmNode.imagPlusEq(m * xcbsb);
        drnPrmGateNode.imagPlusEq(m * xcdgb);
        drnPrmBlkNode.imagPlusEq(m * (-xcdgb - xcddb - xcdsb));
        drnPrmSrcPrmNode.imagPlusEq(m * xcdsb);
        srcPrmGateNode.imagPlusEq(m * xcsgb);
        srcPrmBlkNode.imagPlusEq(m * (-xcsgb - xcsdb - xcssb));
        srcPrmDrnPrmNode.imagPlusEq(m * xcsdb);
        drnDrnNode.realPlusEq(m * gdpr);
        srcSrcNode.realPlusEq(m * gspr);
        blkBlkNode.realPlusEq(m * (gbd + gbs));
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + gbd + xrev * (gm + gmbs)));
        srcPrmSrcPrmNode.realPlusEq(m * (gspr + gds + gbs + xnrm * (gm + gmbs)));
        drnDrnPrmNode.realMinusEq(m * gdpr);
        srcPrmSrcNode.realMinusEq(m * gspr);
        blkDrnPrmNode.realMinusEq(m * gbd);
        blkSrcPrmNode.realMinusEq(m * gbs);
        drnPrmDrnNode.realMinusEq(m * gdpr);
        drnPrmGateNode.realPlusEq(m * (xnrm - xrev) * gm);
        drnPrmBlkNode.realPlusEq(m * (-gbd + (xnrm - xrev) * gmbs));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - xnrm * (gm + gmbs)));
        srcPrmGateNode.realPlusEq(m * (-(xnrm - xrev) * gm));
        srcPrmSrcNode.realMinusEq(m * gspr);
        srcPrmBlkNode.realPlusEq(m * (-gbs - (xnrm - xrev) * gmbs));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds - xrev * (gm + gmbs)));
        return true;
    }

    public boolean convTest(Mode mode) {
        if (!off || !mode.contains(MODE.INIT_FIX)) {
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
                cdhat = cdStates.get(0) - gbdStates.get(0) * delvbd
                        + gmbsStates.get(0) * delvbs
                        + gmStates.get(0) * delvgs
                        + gdsStates.get(0) * delvds;
            } else {
                cdhat = cdStates.get(0) - (gbdStates.get(0)
                        - gmbsStates.get(0)) * delvbd
                        - gmStates.get(0) * delvgd
                        + gdsStates.get(0) * delvds;
            }
            double cbhat = cbsStates.get(0) + cbdStates.get(0)
                    + gbdStates.get(0) * delvbd
                    + gbsStates.get(0) * delvbs;
            double cd = cdStates.get(0);
            double cbs = cbsStates.get(0);
            double cbd = cbdStates.get(0);
            //  Check convergence
            double tol = env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol();
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

    /* This routine evaluates the drain current, its derivatives and the
     * charges associated with the gate, bulk and drain terminal
     * using the B1 (Berkeley Short - Channel IGFET Instance) Equations.
     */
    double evaluate(Mode mode, double vds, double vbs, double vgs) {
        double dC1dVbs;
        double dC2dVbs;
        double _vdsSat = 0;
        double argl1;
        double argl2;
        double args1;
        double _vth0;
        double arg1;
        double arg2;
        double arg3;
        double arg5;
        double ent;
        double vCom;
        double vgb;
        double vGBvFB;
        double vdsPinchoff;
        double entSquare;
        double vGSvthSquare;
        double argl3;
        double argl4;
        double argl5;
        double argl6;
        double argl7;
        double argl8;
        double argl9;
        double dEntdVds;
        double dEntdVbs;
        double cgbb;
        double cdbb;
        double cbbb;
        boolean chargeComputationNeeded;
        if (mode.contains(MODE.AC, MODE.TRAN)) {
            chargeComputationNeeded = true;
        } else if (mode.contains(MODE.INIT_SMSIG)) {
            chargeComputationNeeded = true;
        } else if ((mode.contains(MODE.TRANOP)) && (mode.isUseIC())) {
            chargeComputationNeeded = true;
        } else {
            chargeComputationNeeded = false;
        }
        double _vfb = vfb;
        double _phi = this.phi;
        double _k1 = this.k1;
        double _k2 = this.k2;
        double _vdd = vdd;
        double dUgsdVbs;
        double _ugs = ugs + ugsB * vbs;
        if (_ugs <= 0) {
            _ugs = 0;
            dUgsdVbs = 0;
        } else {
            dUgsdVbs = ugsB;
        }
        double leff;
        double dUdsdVbs;
        double dUdsdVds;
        double _uds = uds + udsB * vbs + udsD * (vds - _vdd);
        if (_uds <= 0) {
            _uds = 0;
            dUdsdVbs = dUdsdVds = 0;
        } else {
            leff = l * 1.e6 - deltaL;
            _uds = _uds / leff;
            dUdsdVbs = udsB / leff;
            dUdsdVds = udsD / leff;
        }
        double dEtadVds;
        double dEtadVbs;
        double _eta = eta + etaB * vbs + etaD * (vds - _vdd);
        if (_eta <= 0) {
            _eta = 0;
            dEtadVds = dEtadVbs = 0;
        } else if (_eta > 1) {
            _eta = 1;
            dEtadVds = dEtadVbs = 0;
        } else {
            dEtadVds = etaD;
            dEtadVbs = etaB;
        }
        double vpb;
        if (vbs < 0) {
            vpb = _phi - vbs;
        } else {
            vpb = _phi;
        }
        double sqrtVpb = MathLib.sqrt(vpb);
        double _vth = _vfb + _phi + _k1 * sqrtVpb - _k2 * vpb - _eta * vds;
        double _von = _vth;
        double dVthdVds = -_eta - dEtadVds * vds;
        double dVthdVbs = _k2 - 0.5 * _k1 / sqrtVpb - dEtadVbs * vds;
        double vgsVth = vgs - _vth;
        double g = 1. - 1. / (1.744 + 0.8364 * vpb);
        double a = 1. + 0.5 * g * _k1 / sqrtVpb;
        a = MathLib.max(a, 1);
        double arg = MathLib.max((1 + _ugs * vgsVth), 1);
        double dGdVbs = -0.8364 * (1 - g) * (1 - g);
        double dAdVbs = 0.25 * _k1 / sqrtVpb * (2 * dGdVbs + g / vpb);
        double drnCurrent = 0;
        double beta0;
        double dBeta0dVds;
        double dBeta0dVbs;
        double dBetaVddDVbs;
        double dBetaVds0dVbs;

        if (vgsVth < 0) {
            // Cutoff
            drnCurrent = 0;
            gm = 0;
            gds = 0;
            gmbs = 0;
        } else {
            // Quadratic Interpolation for beta0
            double betaVds0 = (betaZero + betaZeroB * vbs);
            double _betaVdd = (betaVdd + betaVddB * vbs);
            double dBetaVddDVds = MathLib.max(betaVddD, 0);
            if (vds > _vdd) {
                beta0 = _betaVdd + dBetaVddDVds * (vds - _vdd);
                dBeta0dVds = dBetaVddDVds;
                dBeta0dVbs = betaVddB;
            } else {
                double vddSquare = _vdd * _vdd;
                double c1 = (-_betaVdd + betaVds0 + dBetaVddDVds * _vdd) / vddSquare;
                double c2 = 2 * (_betaVdd - betaVds0) / _vdd - dBetaVddDVds;
                dBetaVds0dVbs = betaZeroB;
                dBetaVddDVbs = betaVddB;
                dC1dVbs = (dBetaVds0dVbs - dBetaVddDVbs) / vddSquare;
                dC2dVbs = dC1dVbs * (-2) * _vdd;
                beta0 = (c1 * vds + c2) * vds + betaVds0;
                dBeta0dVds = 2 * c1 * vds + c2;
                dBeta0dVbs = dC1dVbs * vds * vds + dC2dVbs * vds + dBetaVds0dVbs;
            }
            double beta = beta0 / arg;
            double dBetadVgs = -beta * _ugs / arg;
            double dBetadVds = dBeta0dVds / arg - dBetadVgs * dVthdVds;
            double dBetadVbs = dBeta0dVbs / arg + beta * _ugs * dVthdVbs / arg
                    - beta * vgsVth * dUgsdVbs / arg;
            double vc = _uds * vgsVth / a;
            if (vc < 0) {
                vc = 0;
            }
            double term1 = MathLib.sqrt(1 + 2 * vc);
            double _k = 0.5 * (1 + vc + term1);
            _vdsSat = MathLib.max(vgsVth / (a * MathLib.sqrt(_k)), 0);
            if (vds < _vdsSat) {
                // Triode Region
                argl1 = MathLib.max((1 + _uds * vds), 1);
                argl2 = vgsVth - 0.5 * a * vds;
                drnCurrent = beta * argl2 * vds / argl1;
                gm = (dBetadVgs * argl2 * vds + beta * vds) / argl1;
                gds = (dBetadVds * argl2 * vds + beta
                        * (vgsVth - vds * dVthdVds - a * vds)
                        - drnCurrent * (vds * dUdsdVds + _uds)) / argl1;
                gmbs = (dBetadVbs * argl2 * vds + beta * vds
                        * (-dVthdVbs - 0.5 * vds * dAdVbs)
                        - drnCurrent * vds * dUdsdVbs) / argl1;
            } else {
                // Pinchoff (Saturation) Region
                args1 = 1 + 1 / term1;
                double dVcdVgs = _uds / a;
                double dVcdVds = vgsVth * dUdsdVds / a - dVcdVgs * dVthdVds;
                double dVcdVbs = (vgsVth * dUdsdVbs - _uds
                        * (dVthdVbs + vgsVth * dAdVbs / a)) / a;
                double dKdVc = 0.5 * args1;
                double dKdVgs = dKdVc * dVcdVgs;
                double dKdVds = dKdVc * dVcdVds;
                double dKdVbs = dKdVc * dVcdVbs;
                double args2 = vgsVth / a / _k;
                double args3 = args2 * vgsVth;
                drnCurrent = 0.5 * beta * args3;
                gm = 0.5 * args3 * dBetadVgs + beta * args2
                        - drnCurrent * dKdVgs / _k;
                gds = 0.5 * args3 * dBetadVds - beta * args2 * dVthdVds
                        - drnCurrent * dKdVds / _k;
                gmbs = 0.5 * dBetadVbs * args3 - beta * args2 * dVthdVbs
                        - drnCurrent * (dAdVbs / a + dKdVbs / _k);
            }
        }
        //Subthreshold Computation:
        double n0 = subthSlope;
        double vCut = -40. * n0 * VT0;
        if (n0 < 200) {
            // Subthreshold slope
            double nB = subthSlopeB;
            double nD = subthSlopeD;
            double n = n0 + nB * vbs + nD * vds;
            if (n < 0.5) {
                n = 0.5;
            }
            double wArg1 = MathLib.exp(-vds / VT0);
            double wDS = 1 - wArg1;
            double wGS = MathLib.exp(vgsVth / (n * VT0));
            double vtSquare = VT0 * VT0;
            double wArg2 = 6.04965 * vtSquare * betaZero;
            double iLimit = 4.5 * vtSquare * betaZero;
            double iExp = wArg2 * wGS * wDS;
            drnCurrent = drnCurrent + iLimit * iExp / (iLimit + iExp);
            double temp1 = iLimit / (iLimit + iExp);
            temp1 = temp1 * temp1;
            double temp3 = iLimit / (iLimit + wGS * wArg2);
            temp3 = temp3 * temp3 * wArg2 * wGS;
            gm = gm + temp1 * iExp / (n * VT0);
            gds = gds + temp3 * (-wDS / n / VT0 * (dVthdVds
                    + vgsVth * nD / n) + wArg1 / VT0);
            gmbs = gmbs - temp1 * iExp * (dVthdVbs + vgsVth * nB / n)
                    / (n * VT0);
        }
        //Charge Computation
        if (drnCurrent < 0) {
            drnCurrent = 0;
        }
        if (gm < 0) {
            gm = 0;
        }
        if (gds < 0) {
            gds = 0;
        }
        if (gmbs < 0) {
            gmbs = 0;
        }
        double wLCox = cox * (l - deltaL * 1.e-6) * (w - deltaW * 1.e-6) * 1.e4;
        while (true) {
            if (!chargeComputationNeeded) {
                qg = 0;
                qd = 0;
                qb = 0;
                cggb = 0;
                cgsb = 0;
                cgdb = 0;
                cdgb = 0;
                cdsb = 0;
                cddb = 0;
                cbgb = 0;
                cbsb = 0;
                cbdb = 0;
                break;
            }
            g = 1. - 1. / (1.744 + 0.8364 * vpb);
            a = 1. + 0.5 * g * _k1 / sqrtVpb;
            a = MathLib.max(a, 1);
            // arg = 1 + Ugs * Vgs_Vth;
            dGdVbs = -0.8364 * (1 - g) * (1 - g);
            dAdVbs = 0.25 * _k1 / sqrtVpb * (2 * dGdVbs + g / vpb);
            _phi = MathLib.max(0.1, _phi);
            if (channelChargePartitionFlag >= 1) {
                // 0 / 100 partitioning for drain / source chArges at the saturation region
                _vth0 = _vfb + _phi + _k1 * sqrtVpb;
                vgsVth = vgs - _vth0;
                arg1 = a * vds;
                arg2 = vgsVth - 0.5 * arg1;
                arg3 = vds - arg1;
                arg5 = arg1 * arg1;
                dVthdVbs = -0.5 * _k1 / sqrtVpb;
                dAdVbs = 0.5 * _k1 * (0.5 * g / vpb - 0.8364 * (1 - g) * (1 - g))
                        / sqrtVpb;
                ent = MathLib.max(arg2, 1e-8);
                dEntdVds = -0.5 * a;
                dEntdVbs = -dVthdVbs - 0.5 * vds * dAdVbs;
                vCom = vgsVth * vgsVth / 6 - 1.25e-1 * arg1
                        * vgsVth + 2.5e-2 * arg5;
                vdsPinchoff = MathLib.max(vgsVth / a, 0);
                vgb = vgs - vbs;
                vGBvFB = vgb - _vfb;
                if (vGBvFB < 0) {
                    // Accumulation region
                    qg = wLCox * vGBvFB;
                    qb = -qg;
                    qd = 0.;
                    cggb = wLCox;
                    cgdb = 0.;
                    cgsb = 0.;
                    cbgb = -wLCox;
                    cbdb = 0.;
                    cbsb = 0.;
                    cdgb = 0.;
                    cddb = 0.;
                    cdsb = 0.;
                    break;
                } else if (vgs < _vth0) {
                    // Subthreshold region
                    qg = 0.5 * wLCox * _k1 * _k1 * (-1
                            + MathLib.sqrt(1 + 4 * vGBvFB / (_k1 * _k1)));
                    qb = -qg;
                    qd = 0;
                    cggb = wLCox / MathLib.sqrt(1 + 4 * vGBvFB / (_k1 * _k1));
                    cgdb = cgsb = 0.;
                    cbgb = -cggb;
                    cbdb = cbsb = cdgb = cddb = cdsb = 0;
                    break;
                } else if (vds < vdsPinchoff) {
                    // triode region
                    entSquare = ent * ent;
                    argl1 = 1.2e1 * entSquare;
                    argl2 = 1 - a;
                    argl3 = arg1 * vds;
                    // Argl4 = Vcom / Ent / EntSquare
                    if (ent > 1e-8) {
                        argl5 = arg1 / ent;
                        // Argl6 = Vcom / EntSquare
                    } else {
                        argl5 = 2;
                        argl6 = 4 / 1.5e1;
                    }
                    argl7 = argl5 / 1.2e1;
                    argl8 = 6 * ent;
                    argl9 = 0.125 * argl5 * argl5;
                    qg = wLCox * (vgs - _vfb - _phi - 0.5 * vds + vds * argl7);
                    qb = wLCox * (-_vth0 + _vfb + _phi + 0.5 * arg3 - arg3 * argl7);
                    qd = -wLCox * (0.5 * vgsVth - 0.75 * arg1
                            + 0.125 * arg1 * argl5);
                    cggb = wLCox * (1 - argl3 / argl1);
                    cgdb = wLCox * (-0.5 + arg1 / argl8 - argl3 * dEntdVds
                            / argl1);
                    cgbb = wLCox * (vds * vds * dAdVbs * ent - argl3 * dEntdVbs)
                            / argl1;
                    cgsb = -(cggb + cgdb + cgbb);
                    cbgb = wLCox * argl3 * argl2 / argl1;
                    cbdb = wLCox * argl2 * (0.5 - arg1 / argl8 + argl3 * dEntdVds
                            / argl1);
                    cbbb = -wLCox * (dVthdVbs + 0.5 * vds * dAdVbs + vds
                            * vds * ((1 - 2 * a) * dAdVbs * ent - argl2
                            * a * dEntdVbs) / argl1);
                    cbsb = -(cbgb + cbdb + cbbb);
                    cdgb = -wLCox * (0.5 - argl9);
                    cddb = wLCox * (0.75 * a - 0.25 * a * arg1 / ent
                            + argl9 * dEntdVds);
                    cdbb = wLCox * (0.5 * dVthdVbs + vds * dAdVbs
                            * (0.75 - 0.25 * argl5) + argl9 * dEntdVbs);
                    cdsb = -(cdgb + cddb + cdbb);
                    break;
                } else if (vds >= vdsPinchoff) {
                    // Saturation region
                    args1 = 1 / (3 * a);
                    qg = wLCox * (vgs - _vfb - _phi - vgsVth * args1);
                    qb = wLCox * (_vfb + _phi - _vth0 + (1 - a) * vgsVth * args1);
                    qd = 0;
                    cggb = wLCox * (1 - args1);
                    cgdb = 0;
                    cgbb = wLCox * args1 * (dVthdVbs + vgsVth * dAdVbs / a);
                    cgsb = -(cggb + cgdb + cgbb);
                    cbgb = wLCox * (args1 - 1 / 3);
                    cbdb = 0;
                    cbbb = -wLCox * ((2 / 3 + args1) * dVthdVbs
                            + vgsVth * args1 * dAdVbs / a);
                    cbsb = -(cbgb + cbdb + cbbb);
                    cdgb = 0;
                    cddb = 0;
                    cdsb = 0;
                    break;
                }
                break;
            } else {
                // ChannelChargePartionFlag  < = 0
                // 40 / 60 partitioning for drain / source chArges at the saturation region
                double co4v15 = 4. / 15.;
                _vth0 = _vfb + _phi + _k1 * sqrtVpb;
                vgsVth = vgs - _vth0;
                arg1 = a * vds;
                arg2 = vgsVth - 0.5 * arg1;
                arg3 = vds - arg1;
                arg5 = arg1 * arg1;
                dVthdVbs = -0.5 * _k1 / sqrtVpb;
                dAdVbs = 0.5 * _k1 * (0.5 * g / vpb - 0.8364 * (1 - g) * (1 - g)) / sqrtVpb;
                ent = MathLib.max(arg2, 1e-8);
                dEntdVds = -0.5 * a;
                dEntdVbs = -dVthdVbs - 0.5 * vds * dAdVbs;
                vCom = vgsVth * vgsVth / 6 - 1.25e-1 * arg1 * vgsVth + 2.5e-2 * arg5;
                vdsPinchoff = MathLib.max(vgsVth / a, 0);
                vgb = vgs - vbs;
                vGBvFB = vgb - _vfb;
                if (vGBvFB < 0) {
                    // Accumulation Region
                    qg = wLCox * vGBvFB;
                    qb = -qg;
                    qd = 0.;
                    cggb = wLCox;
                    cgdb = 0.;
                    cgsb = 0.;
                    cbgb = -wLCox;
                    cbdb = 0.;
                    cbsb = 0.;
                    cdgb = 0.;
                    cddb = 0.;
                    cdsb = 0.;
                    break;
                } else if (vgs < _vth0) {
                    // Subthreshold Region
                    qg = 0.5 * wLCox * _k1 * _k1 * (-1 + MathLib.sqrt(1 + 4 * vGBvFB / (_k1 * _k1)));
                    qb = -qg;
                    qd = 0.;
                    cggb = wLCox / MathLib.sqrt(1 + 4 * vGBvFB / (_k1 * _k1));
                    cgdb = cgsb = 0.;
                    cbgb = -cggb;
                    cbdb = cbsb = cdgb = cddb = cdsb = 0;
                    break;
                } else if (vds < vdsPinchoff) {
                    // Triode region
                    vGSvthSquare = vgsVth * vgsVth;
                    entSquare = ent * ent;
                    argl1 = 1.2e1 * entSquare;
                    argl2 = 1 - a;
                    argl3 = arg1 * vds;
                    argl4 = vCom / ent / entSquare;
                    if (ent > 1e-8) {
                        argl5 = arg1 / ent;
                        argl6 = vCom / entSquare;
                    } else {
                        argl5 = 2;
                        argl6 = 4 / 1.5e1;
                    }
                    argl7 = argl5 / 1.2e1;
                    argl8 = 6 * ent;
                    qg = wLCox * (vgs - _vfb - _phi - 0.5 * vds + vds * argl7);
                    qb = wLCox * (-_vth0 + _vfb + _phi + 0.5 * arg3 - arg3 * argl7);
                    qd = -wLCox * (0.5 * (vgsVth - arg1) + arg1 * argl6);
                    cggb = wLCox * (1 - argl3 / argl1);
                    cgdb = wLCox * (-0.5 + arg1 / argl8 - argl3 * dEntdVds / argl1);
                    cgbb = wLCox * (vds * vds * dAdVbs * ent - argl3 * dEntdVbs) / argl1;
                    cgsb = -(cggb + cgdb + cgbb);
                    cbgb = wLCox * argl3 * argl2 / argl1;
                    cbdb = wLCox * argl2 * (0.5 - arg1 / argl8 + argl3 * dEntdVds / argl1);
                    cbbb = -wLCox * (dVthdVbs + 0.5 * vds * dAdVbs
                            + vds * vds * ((1 - 2 * a) * dAdVbs * ent
                            - argl2 * a * dEntdVbs) / argl1);
                    cbsb = -(cbgb + cbdb + cbbb);
                    cdgb = -wLCox * (0.5 + arg1 * (4 * vgsVth - 1.5 * arg1) / argl1
                            - 2 * arg1 * argl4);
                    cddb = wLCox * (0.5 * a + 2 * arg1 * dEntdVds * argl4 - a * (2 * vGSvthSquare
                            - 3 * arg1 * vgsVth + 0.9 * arg5) / argl1);
                    cdbb = wLCox * (0.5 * dVthdVbs + 0.5 * vds * dAdVbs
                            + 2 * arg1 * dEntdVbs * argl4 - vds
                            * (2 * vGSvthSquare * dAdVbs - 4 * a
                            * vgsVth * dVthdVbs - 3 * arg1 * vgsVth
                            * dAdVbs + 1.5 * a * arg1 * dVthdVbs + 0.9
                            * arg5 * dAdVbs) / argl1);
                    cdsb = -(cdgb + cddb + cdbb);
                    break;
                } else if (vds >= vdsPinchoff) {
                    // Saturation region
                    args1 = 1 / (3 * a);
                    qg = wLCox * (vgs - _vfb - _phi - vgsVth * args1);
                    qb = wLCox * (_vfb + _phi - _vth0 + (1 - a) * vgsVth * args1);
                    qd = -co4v15 * wLCox * vgsVth;
                    cggb = wLCox * (1 - args1);
                    cgdb = 0;
                    cgbb = wLCox * args1 * (dVthdVbs + vgsVth * dAdVbs / a);
                    cgsb = -(cggb + cgdb + cgbb);
                    cbgb = wLCox * (args1 - 1 / 3);
                    cbdb = 0;
                    cbbb = -wLCox * ((2 / 3 + args1) * dVthdVbs + vgsVth * args1 * dAdVbs / a);
                    cbsb = -(cbgb + cbdb + cbbb);
                    cdgb = -co4v15 * wLCox;
                    cddb = 0;
                    cdbb = co4v15 * wLCox * dVthdVbs;
                    cdsb = -(cdgb + cddb + cdbb);
                    break;
                }
            }
        }
        gm = MathLib.max(gm, 0);
        gds = MathLib.max(gds, 0);
        gmbs = MathLib.max(gmbs, 0);
        von = _von;
        vdsat = _vdsSat;
        return MathLib.max(drnCurrent, 0);
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
        double drnSatCurrent = 0;
        double srcSatCurrent = 0;
        double arg = 0;
        double capbd = 0;
        double capbs = 0;
        double cbd = 0;
        double cbhat = 0;
        double cbs = 0;
        double cd = 0;
        double idrain = 0;
        double cdhat = 0;
        double cdreq = 0;
        double ceqqb = 0;
        double ceqqd = 0;
        double ceqqg = 0;
        double czbd = 0;
        double czbdsw = 0;
        double czbs = 0;
        double czbssw = 0;
        double delvbd = 0;
        double delvbs = 0;
        double delvds = 0;
        double delvgd = 0;
        double delvgs = 0;
        double evbd = 0;
        double evbs = 0;
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
        double sarg = 0;
        double sargsw = 0;
        double vbd = 0;
        double vbs = 0;
        double vcrit = 0;
        double vds = 0;
        double vgb = 0;
        double vgd = 0;
        double vgdo = 0;
        double vgs = 0;
        double xfact = 0;
        double xnrm = 0;
        double xrev = 0;
        double csgb = 0;
        double cssb = 0;
        double csdb = 0;
        double phiB = 0;
        double phiBSW = 0;
        double mJ = 0;
        double mJSW = 0;
        double argsw = 0;
        double qgate = 0;
        double qbulk = 0;
        double qdrn = 0;
        double qsrc = 0;
        double cqgate = 0;
        double cqbulk = 0;
        double cqdrn = 0;
        boolean bypass = false;
        double tempv = 0;
        double effectiveLength = l - deltaL * 1.e-6;
        if ((drnSatCurrent = drnArea * jctSatCurDensity) < 1e-15) {
            drnSatCurrent = 1e-15;
        }
        if ((srcSatCurrent = srcArea * jctSatCurDensity) < 1e-15) {
            srcSatCurrent = 1e-15;
        }
        double _gateSrcOverlapCap = this.gateSrcOverlapCap * w;
        double _gateDrnOverlapCap = this.gateDrnOverlapCap * w;
        double _gateBlkOverlapCap = this.gateBlkOverlapCap * effectiveLength;
        double _von = type * this.von;
        double _vdsat = type * this.vdsat;
        double _vt0 = type * this.vt0;
        KEY key = KEY.INIT;
        boolean byPass = false;
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
                    && (mode.contains(MODE.TRAN, MODE.AC,
                    MODE.DCOP, MODE.DCTRANCURVE)
                    || !mode.isUseIC())) {
                vbs = -1;
                vgs = _vt0;
                vds = 0;
            }
        } else if ((mode.contains(MODE.INIT_JCT) && mode.contains(MODE.INIT_FIX))
                && off) {
            vbs = vgs = vds = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vbsStates.set(0, vbsStates.get(1));
                vbs = (1 + xfact) * vbsStates.get(1) - (xfact * vbsStates.get(2));
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * vgsStates.get(1) - (xfact * vgsStates.get(2));
                vdsStates.set(0, vdsStates.get(1));
                vds = (1 + xfact) * vdsStates.get(1) - (xfact * vdsStates.get(2));
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
                cdhat = cdStates.get(0) - gbdStates.get(0) * delvbd
                        + gmbsStates.get(0) * delvbs + gmStates.get(0) * delvgs
                        + gdsStates.get(0) * delvds;
            } else {
                cdhat = cdStates.get(0) - (gbdStates.get(0) - gmbsStates.get(0)) * delvbd
                        - gmStates.get(0) * delvgd + gdsStates.get(0) * delvds;
            }
            cbhat = cbsStates.get(0) + cbdStates.get(0)
                    + gbdStates.get(0) * delvbd
                    + gbsStates.get(0) * delvbs;
            tempv = MathLib.max(MathLib.abs(cbhat), MathLib.abs(cbsStates.get(0)
                    + cbdStates.get(0))) + env.getAbsTol();
            if ((!(mode.contains(MODE.INIT_PRED))) && (env.isBypass())) {
                if ((MathLib.abs(delvbs) < (env.getRelTol() * MathLib.max(MathLib.abs(vbs), MathLib.abs(vbsStates.get(0))) + env.getVoltTol()))) {
                    if ((MathLib.abs(delvbd) < (env.getRelTol() * MathLib.max(MathLib.abs(vbd), MathLib.abs(vbdStates.get(0))) + env.getVoltTol()))) {
                        if (MathLib.abs(delvgs) < (env.getRelTol() * MathLib.max(MathLib.abs(vgs), MathLib.abs(vgsStates.get(0))) + env.getVoltTol())) {
                            if ((MathLib.abs(delvds) < (env.getRelTol() * MathLib.max(MathLib.abs(vds), MathLib.abs(vdsStates.get(0))) + env.getVoltTol()))) {
                                if ((MathLib.abs(cdhat - cdStates.get(0)) < env.getRelTol() * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cdStates.get(0))) + env.getAbsTol())) {
                                    if ((MathLib.abs(cbhat - (cbsStates.get(0) + cbdStates.get(0))) < env.getRelTol() * tempv)) {
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
                                        if ((mode.contains(MODE.TRAN, MODE.AC))
                                                || (mode.contains(MODE.TRANOP)
                                                && mode.isUseIC())) {
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
                                            byPass = true;
//                                            key = KEY.CASE1;//line755;
                                        } else {
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
                        vbs = MOSUtils.pnjLimit(vbs, vbsStates.get(0),
                                VT0, vcrit, check);
                        vbd = vbs - vds;
                    } else {
                        vcrit = VT0 * MathLib.log(VT0 / (ROOT2 * drnSatCurrent));
                        vbd = MOSUtils.pnjLimit(vbd, vbdStates.get(0),
                                VT0, vcrit, check);
                        vbs = vbd + vds;
                    }
                }
            }
        }
        if (!bypass) {
            // Determine DC current and derivatives
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
        }
        // line 400
        if (vds >= 0) {
            // Normal _mode
            _mode = 1;
        } else {
            // Inverse _mode
            _mode = -1;
        }
        /*
         * Calculate drain current, derivatives, charge
         * and capacitances for gate,drain, and bulk
         */
        if (vds >= 0) {
            idrain = evaluate(mode, vds, vbs, vgs);
            qgate = qg;
            qbulk = qb;
            qdrn = qd;

        } else {
            idrain = evaluate(mode, -vds, vbd, vgd);
            qgate = qg;
            qbulk = qb;
            qsrc = qd;
            csgb = cdgb;
            cssb = cddb;
            csdb = cdsb;
        }
        this.von = type * _von;
        this.vdsat = type * _vdsat;
        //  Compute drain current source
        cd = _mode * idrain - cbd;
        if (mode.contains(MODE.TRAN, MODE.AC, MODE.INIT_SMSIG)
                || (mode.contains(MODE.TRANOP)
                && mode.isUseIC())) {
            //  Charge storage elements

            //   bulk - drain and bulk - source depletion capacitances

            // Zero bias drain junction capacitance
            czbd = unitAreaJctCap * drnArea;
            // Zero bias source junction capacitance
            czbs = unitAreaJctCap * srcArea;
            // Zero bias drain junction sidewall capacitance
            czbdsw = unitLengthSidewallJctCap * drnPerimeter;
            // Zero bias source junction sidewall capacitance
            czbssw = unitLengthSidewallJctCap * srcPerimeter;
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
                        vbs * (czbs + czbssw) + vbs * vbs * (czbs * mJ * 0.5 / phiB
                        + czbssw * mJSW * 0.5 / phiBSW));
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
                        vbd * (czbd + czbdsw) + vbd * vbd * (czbd * mJ * 0.5 / phiB
                        + czbdsw * mJSW * 0.5 / phiBSW));
                capbd = czbd + czbdsw + vbd * (czbd * mJ / phiB
                        + czbdsw * mJSW / phiBSW);
            }
        }
        //  Check convergence
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
        // Bulk and channel charge plus overlaps */
//            case CASE2://line850:
        /* Initialize to clear charge conductance and current
        ceqqg = ceqqb = ceqqd = 0;
        gcdgb = gcddb = gcdsb = 0;
        gcsgb = gcsdb = gcssb = 0;
        gcggb = gcgdb = gcgsb = 0;
        gcbgb = gcbdb = gcbsb = 0;
        key = KEY.DONE;//line900;*/
        if (!mode.contains(MODE.TRAN, MODE.AC)
                && (!mode.contains(MODE.TRANOP)
                || !mode.isUseIC())
                && !mode.contains(MODE.INIT_SMSIG)) {
            key = KEY.CASE2;
        } else {
            if (_mode > 0) {
                double qgd;
                double qgs;
                double qgb;
                double ag0 = tmprl.getAgAt(0);
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
                double ag0 = tmprl.getAgAt(0);
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
            }
            if (!byPass) {
                qgStates.set(0, qgate);
                qdStates.set(0, qdrn - qbdStates.get(0));
                qbStates.set(0, qbulk + qbdStates.get(0) + qbsStates.get(0));
                // Store small signal parameters
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
            }
        }
        key = KEY.CASE3;//line860;
        //        case CASE3://line860:
        // Evaluate equivalent charge current
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
        //  Load current vector
        double ceqbs = type * (cbs - (gbs - env.getGMin()) * vbs);
        double ceqbd = type * (cbd - (gbd - env.getGMin()) * vbd);
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
            cdreq = -(type) * (idrain + gds * vds - gm * vgd - gmbs * vbd);
        }
        wrk.getRhsAt(gateIndex).realMinusEq(m * ceqqg);
        wrk.getRhsAt(blkIndex).realMinusEq(m * (ceqbs + ceqbd + ceqqb));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (ceqbd - cdreq - ceqqd));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqbs + ceqqg + ceqqb + ceqqd));
        //  Load y matrix
        drnDrnNode.realPlusEq(m * (drnConductance));
        gateGateNode.realPlusEq(m * (gcggb));
        srcSrcNode.realPlusEq(m * (srcConductance));
        blkBlkNode.realPlusEq(m * (gbd + gbs - gcbgb - gcbdb - gcbsb));
        drnPrmDrnPrmNode.realPlusEq(m * (drnConductance + gds + gbd + xrev * (gm + gmbs) + gcddb));
        srcPrmSrcPrmNode.realPlusEq(m * (srcConductance + gds + gbs + xnrm * (gm + gmbs) + gcssb));
        drnDrnPrmNode.realPlusEq(m * (-drnConductance));
        gateBlkNode.realPlusEq(m * (-gcggb - gcgdb - gcgsb));
        gateDrnPrmNode.realPlusEq(m * (gcgdb));
        gateSrcPrmNode.realPlusEq(m * (gcgsb));
        srcPrmSrcNode.realPlusEq(m * (-srcConductance));
        blkGateNode.realPlusEq(m * (gcbgb));
        blkDrnPrmNode.realPlusEq(m * (-gbd + gcbdb));
        blkSrcPrmNode.realPlusEq(m * (-gbs + gcbsb));
        drnPrmDrnNode.realPlusEq(m * (-drnConductance));
        drnPrmGateNode.realPlusEq(m * ((xnrm - xrev) * gm + gcdgb));
        drnPrmBlkNode.realPlusEq(m * (-gbd + (xnrm - xrev) * gmbs - gcdgb - gcddb - gcdsb));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - xnrm * (gm + gmbs) + gcdsb));
        srcPrmGateNode.realPlusEq(m * (-(xnrm - xrev) * gm + gcsgb));
        srcPrmSrcNode.realPlusEq(m * (-srcConductance));
        srcPrmBlkNode.realPlusEq(m * (-gbs - (xnrm - xrev) * gmbs - gcsgb - gcsdb - gcssb));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds - xrev * (gm + gmbs) + gcsdb));
        return true;
    }

    private void mosCap(double vgd, double vgs, double vgb) {
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
        if (blkJctPotential < 0.1) {
            blkJctPotential = 0.1;
        }
        if (sidewallJctPotential < 0.1) {
            sidewallJctPotential = 0.1;
        }
        double _cox = 3.453e-13 / (oxideThickness * 1e-4);
        this.cox = _cox;
        double effChanLength = l - deltaL * 1e-6;
        if (effChanLength <= 0) {
            StandardLog.severe("B1: mosfet " + getModelName() + ", model "
                    + getInstName() + ": Effective channel length <= 0");
            return false;
        }
        double effChanWidth = w - deltaW * 1e-6;
        if (effChanWidth <= 0) {
            StandardLog.severe("B1: mosfet " + getModelName() + ", model "
                    + getInstName() + ": Effective channel width <= 0");
            return false;
        }
        gDoverlapCap = effChanWidth * gateDrnOverlapCap;
        gSoverlapCap = effChanWidth * gateSrcOverlapCap;
        gtBlkOverlapCap = l * gateBlkOverlapCap;
        if ((drnConductance = sheetResistance * drnSquares) != 0) {
            drnConductance = 1 / drnConductance;
        }
        if ((srcConductance = sheetResistance * srcSquares) != 0) {
            srcConductance = 1. / srcConductance;
        }
        double lEffMicron = effChanLength * 1.e6;
        double wEffMicron = effChanWidth * 1.e6;
        double coxWoverL = _cox * wEffMicron / lEffMicron;
        vfb = vfb0 + vfbL / lEffMicron + vfbW / wEffMicron;
        this.phi = phi0 + phiL / lEffMicron + phiW / wEffMicron;
        k1 = k10 + k1L / lEffMicron + k1W / wEffMicron;
        k2 = k20 + k2L / lEffMicron + k2W / wEffMicron;
        eta = eta0 + etaL / lEffMicron + etaW / wEffMicron;
        etaB = etaB0 + etaBl / lEffMicron + etaBw / wEffMicron;
        etaD = etaD0 + etaDl / lEffMicron + etaDw / wEffMicron;
        betaZero = mobZero;
        betaZeroB = mobZeroB0 + mobZeroBl / lEffMicron + mobZeroBw / wEffMicron;
        ugs = ugs0 + ugsL / lEffMicron + ugsW / wEffMicron;
        ugsB = ugsB0 + ugsBL / lEffMicron + ugsBW / wEffMicron;
        uds = uds0 + udsL / lEffMicron + udsW / wEffMicron;
        udsB = udsB0 + udsBL / lEffMicron + udsBW / wEffMicron;
        udsD = udsD0 + udsDL / lEffMicron + udsDW / wEffMicron;
        betaVdd = mobVdd0 + mobVddl / lEffMicron + mobVddw / wEffMicron;
        betaVddB = mobVddB0 + mobVddBl / lEffMicron + mobVddBw / wEffMicron;
        betaVddD = mobVddD0 + mobVddDl / lEffMicron + mobVddDw / wEffMicron;
        subthSlope = subthSlope0 + subthSlopeL / lEffMicron + subthSlopeW / wEffMicron;
        subthSlopeB = subthSlopeB0 + subthSlopeBL / lEffMicron + subthSlopeBW / wEffMicron;
        subthSlopeD = subthSlopeD0 + subthSlopeDL / lEffMicron + subthSlopeDW / wEffMicron;
        if (phi < 0.1) {
            phi = 0.1;
        }
        if (k1 < 0) {
            k1 = 0;
        }
        if (k2 < 0) {
            k2 = 0;
        }
        vt0 = vfb + phi + k1 * MathLib.sqrt(phi) - k2 * phi;
        von = vt0;
        betaZero = betaZero * coxWoverL;
        betaZeroB = betaZeroB * coxWoverL;
        betaVdd = betaVdd * coxWoverL;
        betaVddB = betaVddB * coxWoverL;
        betaVddD = MathLib.max(betaVddD * coxWoverL, 0);
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
}
