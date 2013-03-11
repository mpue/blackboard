/*
 * jfet2.java
 *
 * Created on August 28, 2006, 2:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.jfet;

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
public class JFET2Instance extends JFET2ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;
    /* Temperature difference */
    private double dtemp;

    /* Temperature corrected saturation current */
    private double tSatCur;

    /* Temperature corrected gate potential */
    private double tGatePot;

    /* Temperature corrected G - S capacitance */
    private double tCGS;

    /* Temperature corrected G - D capacitance */
    private double tCGD;

    /* Joining point of the forward bias depletion capacitance */
    private double corDepCap;

    /* Critical voltage */
    private double vcrit;

    /* Coefficient of capacitance polynomial */
    private double f1;
    private double drnConduct;
    private double srcConduct;
    private double f2;
    private double f3;
    /* Velocity saturation potential */
    private double xiwoo;

    /* Dual Power - law parameter */
    private double d3;

    /* Capacitance model transition parameter */
    private double alpha;

    /* Built-in junction potential */
    private double vbi = tGatePot;
    private double nvt = temp * KoverQ * n;
    /* Zero bias cgs */
    private double cgs = tCGS;

    /* Zero bias cgd */
    private double cgd = tCGD;

    /* Saturation index parameter */
    private double za;
    private Complex drnDrnPrmNode;
    private Complex gtDrnPrmNode;
    private Complex gtSrcPrmNode;
    private Complex srcSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPrmGtNode;
    private Complex drnPrmSrcPrmNode;
    private Complex srcPrmGtNode;
    private Complex srcPrmSrcNode;
    private Complex srcPrmDrnPrmNode;
    private Complex drnDrnNode;
    private Complex gtGtNode;
    private Complex srcSrcNode;
    private Complex drnPrmDrnPrmNode;
    private Complex srcPrmSrcPrmNode;
    private StateVector vgsStates;
    private StateVector vgdStates;
    private StateVector cgStates;
    private StateVector cdStates;
    private StateVector cgdStates;
    private StateVector gmStates;
    private StateVector gdsStates;
    private StateVector ggsStates;
    private StateVector ggdStates;
    private StateVector qgsStates;
    private StateVector cqgsStates;
    private StateVector qgdStates;
    private StateVector cqgdStates;
    private StateVector qdsStates;
    private StateVector cqdsStates;
    private StateVector paveStates;
    private StateVector vtrapStates;
    private StateVector vgstrapStates;
    private double igs;
    private double igd;
    private double ggs;
    private double ggd;
    private double gm;
    private double gds;
    private double czgs;
    private double czgd;
    private Real ceq = Real.zero();
    private Real geq = Real.zero();
    private Bool icheck = Bool.TRUE();
    private Bool ichk1 = Bool.TRUE();


    /*  Creates a new instance of jfet2 */
    public JFET2Instance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        vgsStates = stateTable.createRow();
        vgdStates = stateTable.createRow();
        cgStates = stateTable.createRow();
        cdStates = stateTable.createRow();
        cgdStates = stateTable.createRow();
        gmStates = stateTable.createRow();
        gdsStates = stateTable.createRow();
        ggsStates = stateTable.createRow();
        ggdStates = stateTable.createRow();
        qgsStates = stateTable.createRow();
        cqgsStates = stateTable.createRow();
        qgdStates = stateTable.createRow();
        cqgdStates = stateTable.createRow();
        qdsStates = stateTable.createRow();
        cqdsStates = stateTable.createRow();
        paveStates = stateTable.createRow();
        vtrapStates = stateTable.createRow();
        vgstrapStates = stateTable.createRow();
        if (srcResist != 0 && srcPrmIndex == 0) {
            srcConduct = 1 / srcResist;
            srcPrmIndex = ckt.makeVoltNode(getInstName() + "source").getIndex();
        } else {
            srcPrmIndex = srcIndex;
            srcConduct = 0;
        }
        if (drnResist != 0 && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "drain").getIndex();
            drnConduct = 1 / drnResist;
        } else {
            drnConduct = 0;
            drnPrmIndex = drnIndex;
        }
        drnDrnPrmNode = wrk.aquireNode(drnIndex, drnPrmIndex);
        gtDrnPrmNode = wrk.aquireNode(gateIndex, drnPrmIndex);
        gtSrcPrmNode = wrk.aquireNode(gateIndex, srcPrmIndex);
        srcSrcPrmNode = wrk.aquireNode(srcIndex, srcPrmIndex);
        drnPrmDrnNode = wrk.aquireNode(drnPrmIndex, drnIndex);
        drnPrmGtNode = wrk.aquireNode(drnPrmIndex, gateIndex);
        drnPrmSrcPrmNode = wrk.aquireNode(drnPrmIndex, srcPrmIndex);
        srcPrmGtNode = wrk.aquireNode(srcPrmIndex, gateIndex);
        srcPrmSrcNode = wrk.aquireNode(srcPrmIndex, srcIndex);
        srcPrmDrnPrmNode = wrk.aquireNode(srcPrmIndex, drnPrmIndex);
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        gtGtNode = wrk.aquireNode(gateIndex, gateIndex);
        srcSrcNode = wrk.aquireNode(srcIndex, srcIndex);
        drnPrmDrnPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmIndex);
        srcPrmSrcPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double omega = tmprl.getOmega();
        gm = gmStates.get(0);
        gds = gdsStates.get(0);
        ggs = ggsStates.get(0);
        double xgs = qgsStates.get(0) * omega;
        ggd = ggdStates.get(0);
        double xgd = qgdStates.get(0) * omega;
        double vgs = vgsStates.get(0);
        double vgd = vgdStates.get(0);
        double cd = cdStates.get(0);
        //        PSacload(vgs, vgd, cd, tmprl.getOmega(), gm, xgm, gds, xgds);
        double arg;
        double vds = vgs - vgd;
        double _lfg2 = lfg2 * vgd;
        double _hfg2 = hfg2 * vgd;
        double _hfe2 = hfe2 * vgs;
        double _hfgam = hfgam - hfg1 * vgs + _hfg2;
        double eta = hfeta - hfe1 * vgd + _hfe2;
        double lfga = lfgam - lfg1 * vgs + _lfg2 + _lfg2;
        double gmo = gm / (1 - lfga + lfg1 * vgd);
        double wtg = taug * omega;
        double wtgdet = 1 + wtg * wtg;
        double gwtgdet = gmo / wtgdet;
        double gdsi = (arg = _hfgam - lfga) * gwtgdet;
        double gdsr = arg * gmo - gdsi;
        double gmi = (eta + lfg1 * vgd) * gwtgdet + gdsi;
        double xgds = wtg * gdsi;
        double _gds = gds + gdsr;
        double xgm = -wtg * gmi;
        double _gm = gmi + gmo * (1 - eta - _hfgam);
        double _delta = delta / area;
        double wtd = taud * omega;
        double wtddet = 1 + wtd * wtd;
        double fac = _delta * cd;
        double del = 1 / (1 - fac * vds);
        double dd = (del - 1) / wtddet;
        double dr = del - dd;
        double di = wtd * dd;
        double cdsqr = fac * cd * del * wtd / wtddet;
        gm = dr * _gm - di * xgm;
        xgm = di * _gm + dr * xgm;
        gds = dr * _gds - di * xgds + cdsqr * wtd;
        xgds = di * _gds + dr * xgds + cdsqr;
        xgds += qdsStates.get(0) * tmprl.getOmega();
        double gdpr = drnConduct * area;
        double gspr = srcConduct * area;
        drnPrmDrnPrmNode.imagPlusEq(m * (xgds));
        srcPrmSrcPrmNode.imagPlusEq(m * (xgds + xgm));
        drnPrmGtNode.imagPlusEq(m * (xgm));
        drnPrmSrcPrmNode.imagMinusEq(m * (xgds + xgm));
        srcPrmGtNode.imagMinusEq(m * (xgm));
        srcPrmDrnPrmNode.imagMinusEq(m * (xgds));
        drnDrnNode.realPlusEq(m * gdpr);
        gtGtNode.realPlusEq(m * (ggd + ggs));
        gtGtNode.imagPlusEq(m * (xgd + xgs));
        srcSrcNode.realPlusEq(m * gspr);
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + ggd));
        drnPrmDrnPrmNode.imagPlusEq(m * (xgd));
        srcPrmSrcPrmNode.realPlusEq(m * (gspr + gds + gm + ggs));
        srcPrmSrcPrmNode.imagPlusEq(m * (xgs));
        drnDrnPrmNode.realMinusEq(m * gdpr);
        gtDrnPrmNode.realMinusEq(m * ggd);
        gtDrnPrmNode.imagMinusEq(m * (xgd));
        gtSrcPrmNode.realMinusEq(m * ggs);
        gtSrcPrmNode.imagMinusEq(m * (xgs));
        srcSrcPrmNode.realMinusEq(m * (gspr));
        drnPrmDrnNode.realMinusEq(m * gdpr);
        drnPrmGtNode.realPlusEq(m * (-ggd + gm));
        drnPrmGtNode.imagMinusEq(m * (xgd));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGtNode.realPlusEq(m * (-ggs - gm));
        srcPrmGtNode.imagMinusEq(m * (xgs));
        srcPrmSrcNode.realMinusEq(m * (gspr));
        srcPrmDrnPrmNode.realMinusEq(m * gds);
        return true;
    }

    public boolean loadInitCond() {
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
        double _capgd = 0;
        double _capgs = 0;
        double cd = 0;
        double cdhat = 0.0;
        double cg = 0;
        double _cgd = 0;
        double cghat = 0.0;
        double _ggd = 0;
        double _ggs = 0;
        double vds = 0;
        double vgd = 0;
        double vgs = 0;
        boolean converged = true;
        boolean bypass = false;
        if (mode.contains(MODE.INIT_SMSIG)) {
            vgs = vgsStates.get(0);
            vgd = vgdStates.get(0);
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vgs = vgsStates.get(1);
            vgd = vgdStates.get(1);
        } else if (mode.contains(MODE.INIT_JCT)
                && mode.contains(MODE.TRANOP)
                && mode.isUseIC()) {
            vds = type * icVDS;
            vgs = type * icVGS;
            vgd = vgs - vds;
        } else if (mode.contains(MODE.INIT_JCT) && !off) {
            vgs = -1;
            vgd = -1;
        } else if (mode.contains(MODE.INIT_JCT) || mode.contains(MODE.INIT_FIX) && off) {
            vgs = 0;
            vgd = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * vgsStates.get(1) - xfact * vgsStates.get(2);
                vgdStates.set(0, vgdStates.get(1));
                vgd = (1 + xfact) * vgdStates.get(1) - xfact * vgdStates.get(2);
                cgStates.set(0, cgStates.get(1));
                cdStates.set(0, cdStates.get(1));
                cgdStates.set(0, cgdStates.get(1));
                gmStates.set(0, gmStates.get(1));
                gdsStates.set(0, gdsStates.get(1));
                ggsStates.set(0, ggsStates.get(1));
                ggdStates.set(0, ggdStates.get(1));
            } else {
                //   Compute new nonlinear branch voltages
                vgs = type
                        * (wrk.getRhsOldAt(gateIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmIndex).getReal());
                vgd = type
                        * (wrk.getRhsOldAt(gateIndex).getReal()
                        - wrk.getRhsOldAt(drnPrmIndex).getReal());
            }
            double delvgs = vgs - vgsStates.get(0);
            double delvgd = vgd - vgdStates.get(0);
            double delvds = delvgs - delvgd;
            cghat = cgStates.get(0)
                    + ggdStates.get(0) * delvgd
                    + ggsStates.get(0) * delvgs;
            cdhat = cdStates.get(0)
                    + gmStates.get(0) * delvgs
                    + gdsStates.get(0) * delvds
                    - ggdStates.get(0) * delvgd;
            //   Bypass if solution has not changed
            if (env.isBypass() && (!mode.contains(MODE.INIT_PRED)
                    && (MathLib.abs(delvgs) < env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                    MathLib.abs(vgsStates.get(0)))
                    + env.getVoltTol()))) {
                if ((MathLib.abs(delvgd) < env.getRelTol() * MathLib.max(MathLib.abs(vgd),
                        MathLib.abs(vgdStates.get(0)))
                        + env.getVoltTol())) {
                    if ((MathLib.abs(cghat - cgStates.get(0)) < env.getRelTol() * MathLib.max(MathLib.abs(cghat),
                            MathLib.abs(cgStates.get(0)))
                            + env.getAbsTol())) {
                        if ((MathLib.abs(cdhat - cdStates.get(0)) < env.getRelTol() * MathLib.max(MathLib.abs(cdhat),
                                MathLib.abs(cdStates.get(0)))
                                + env.getAbsTol())) {
                            vgs = vgsStates.get(0);
                            vgd = vgdStates.get(0);
                            vds = vgs - vgd;
                            cg = cgStates.get(0);
                            cd = cdStates.get(0);
                            _cgd = cgdStates.get(0);
                            gm = gmStates.get(0);
                            gds = gdsStates.get(0);
                            _ggs = ggsStates.get(0);
                            _ggd = ggdStates.get(0);
                            bypass = true;
                        }
                    }
                }
            }
            if (!bypass) {
                //   Limit nonlinear branch voltages
                vgs = MOSUtils.pnjLimit(vgs, vgsStates.get(0),
                        (temp * KoverQ), vcrit, icheck);
                vgd = MOSUtils.pnjLimit(vgd, vgdStates.get(0),
                        (temp * KoverQ), vcrit, ichk1);
                if (ichk1.isFalse()) {
                    icheck.setFalse();
                }
                vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), vto);
                vgd = MOSUtils.fetLimit(vgd, vgdStates.get(0), vto);
            }
        }
        if (!bypass) {
            //   Determine dc current and derivatives
            vds = vgs - vgd;
            if (vds < 0.0) {
                cd = -psIds(mode, vgd, vgs);
                _cgd = igs;
                cg = igd;
                _ggd = ggs;
                _ggs = ggd;
                gds += gm;
                gm = -gm;
            } else {
                cd = psIds(mode, vgs, vgd);
                cg = igs;
                _cgd = igd;
                _ggs = ggs;
                _ggd = ggd;
            }
            cg = cg + _cgd;
            cd = cd - _cgd;
            if (mode.contains(MODE.TRAN, MODE.AC, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                //    Charge storage elements
                double _capds = capDS * area;
                //             PScharge(vgs, vgd, _capgs, _capgd);
                czgs = cgs * this.area;
                czgd = cgd * this.area;
                double phib = vbi;
                double gac = acgam;
                if (!mode.contains(MODE.TRAN)) {
                    double st = qgg(vgs, vgd, gac, phib, alpha, vto, corDepCap, xc);
                    qgsStates.set(0, st);
                    qgdStates.set(0, st);
                    qgsStates.set(1, st);
                    qgdStates.set(1, st);
                    _capgs = czgs;
                    _capgd = czgd;

                } else {
                    double qgga = qgg(vgs, vgd, gac, phib, alpha, vto, corDepCap, xc);
                    double cgsna = czgs;
                    double cgdna = czgd;
                    double qggb = qgg(vgsStates.get(1), vgd, gac, phib, alpha, vto, corDepCap, xc);
                    double cgdnb = czgd;
                    double qggc = qgg(vgs, vgdStates.get(1), gac, phib, alpha, vto, corDepCap, xc);
                    double cgsnc = czgs;
                    double qggd = qgg(vgsStates.get(1), vgdStates.get(1), gac, phib, alpha, vto, corDepCap, xc);
                    qgsStates.set(0, qgsStates.get(1) + 0.5 * (qgga - qggb + qggc - qggd));
                    qgdStates.set(0, qgdStates.get(1) + 0.5 * (qgga - qggc + qggb - qggd));
                    _capgs = 0.5 * (cgsna + cgsnc);
                    _capgd = 0.5 * (cgdna + cgdnb);
                }
                qdsStates.set(0, _capds * vds);
                //    Store small - signal parameters
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        qgsStates.set(0, _capgs);
                        qgdStates.set(0, _capgd);
                        qdsStates.set(0, _capds);
                        return true;
                    }
                    //    Transient analysis
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qgsStates.set(1, qgsStates.get(0));
                        qgdStates.set(1, qgdStates.get(0));
                        qdsStates.set(1, qdsStates.get(0));
                    }
                    stateTable.integrate(geq, ceq, _capgs, qgsStates, cqgsStates);
                    _ggs = _ggs + geq.get();
                    cg = cg + cqgsStates.get(0);
                    stateTable.integrate(geq, ceq, _capgd, qgdStates, cqgdStates);
                    _ggd = _ggd + geq.get();
                    cg = cg + cqgdStates.get(0);
                    cd = cd - cqgdStates.get(0);
                    _cgd = _cgd + cqgdStates.get(0);
                    stateTable.integrate(geq, ceq, _capds, qdsStates, cqdsStates);
                    cd = cd + cqdsStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqgsStates.set(1, cqgsStates.get(0));
                        cqgdStates.set(1, cqgdStates.get(0));
                        cqdsStates.set(1, cqdsStates.get(0));
                    }
                }
            }
            //   Check convergence
            if (!mode.contains(MODE.INIT_FIX) || !mode.isUseIC()) {
                if (icheck.isFalse()
                        || (MathLib.abs(cghat - cg) >= env.getRelTol()
                        * MathLib.max(MathLib.abs(cghat), MathLib.abs(cg)) + env.getAbsTol())
                        || (MathLib.abs(cdhat - cd) > env.getRelTol()
                        * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol())) {
                    converged = false;
                }
            }
            vgsStates.set(0, vgs);
            vgdStates.set(0, vgd);
            cgStates.set(0, cg);
            cdStates.set(0, cd);
            cgdStates.set(0, _cgd);
            gmStates.set(0, gm);
            gdsStates.set(0, gds);
            ggsStates.set(0, _ggs);
            ggdStates.set(0, _ggd);
        }
        //   Load current vector
        double gdpr = drnConduct * area;
        double gspr = srcConduct * area;
        double ceqgd = type * (_cgd - _ggd * vgd);
        double ceqgs = type * ((cg - _cgd) - _ggs * vgs);
        double cdreq = type * ((cd + _cgd) - gds * vds - gm * vgs);
        wrk.getRhsAt(gateIndex).realPlusEq(m * (-ceqgs - ceqgd));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (-cdreq + ceqgd));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqgs));
        //   Load y matrix
        drnDrnPrmNode.realPlusEq(m * (-gdpr));
        gtDrnPrmNode.realPlusEq(m * (-_ggd));
        gtSrcPrmNode.realPlusEq(m * (-_ggs));
        srcSrcPrmNode.realPlusEq(m * (-gspr));
        drnPrmDrnNode.realPlusEq(m * (-gdpr));
        drnPrmGtNode.realPlusEq(m * (gm - _ggd));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGtNode.realPlusEq(m * (-_ggs - gm));
        srcPrmSrcNode.realPlusEq(m * (-gspr));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds));
        drnDrnNode.realPlusEq(m * (gdpr));
        gtGtNode.realPlusEq(m * (_ggd + _ggs));
        srcSrcNode.realPlusEq(m * (gspr));
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + _ggd));
        srcPrmSrcPrmNode.realPlusEq(m * (gspr + gds + gm + _ggs));
        return converged;
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

    public boolean temperature() {
        tnom = env.getNomTemp();
        double vtnom = KoverQ * tnom;
        double fact1 = tnom / REF_TEMP;
        double kt1 = BOLTZMANN * tnom;
        double egfet1 = 1.16 - (7.02e-4 * tnom * tnom)
                / (tnom + 1108);
        double arg1 = -egfet1 / (kt1 + kt1) + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact1 = -2 * vtnom * (1.5 * MathLib.log(fact1) + CHARGE * arg1);
        double pbo = (phi - pbfact1) / fact1;
        double gmaold = (phi - pbo) / pbo;
        double cjfact = 1 / (1 + .5 * (4e-4 * (tnom - REF_TEMP) - gmaold));
        if (depCapCoeff > 0.95) {
            StandardLog.warning("Front: " + getModelName()
                    + ": Depletion cap. coefficient too large, limited to .95");
            depCapCoeff = 0.95;
        }
        double xfc = MathLib.log(1 - depCapCoeff);
        f2 = MathLib.exp((1 + 0.5) * xfc);
        f3 = 1 - depCapCoeff * (1 + 0.5);
        dtemp = 0.0;
        if (temp == 0) {
            temp = env.getTemp() + dtemp;
        }
        double vt = temp * KoverQ;
        double fact2 = temp / REF_TEMP;
        double ratio1 = temp / tnom - 1;
        tSatCur = gateSatCurrent * MathLib.exp(ratio1 * 1.11 / vt);
        tCGS = capGS * cjfact;
        tCGD = capGD * cjfact;
        double kt = BOLTZMANN * temp;
        double egfet = 1.16 - (7.02e-4 * temp * temp)
                / (temp + 1108);
        double arg = -egfet / (kt + kt) + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact = -2 * vt * (1.5 * MathLib.log(fact2) + CHARGE * arg);
        tGatePot = fact2 * pbo + pbfact;
        double gmanew = (tGatePot - pbo) / pbo;
        double cjfact1 = 1 + .5 * (4e-4 * (temp - REF_TEMP) - gmanew);
        tCGS *= cjfact1;
        tCGD *= cjfact1;
        corDepCap = depCapCoeff * tGatePot;
        f1 = tGatePot * (1 - MathLib.exp((1 - .5) * xfc)) / (1 - .5);
        vcrit = vt * MathLib.log(vt / (ROOT2 * tSatCur));
        double woo = (vbi - vto);
        xiwoo = xi * woo;
        za = MathLib.sqrt(1 + z) / 2;
        alpha = xiwoo * xiwoo / (xi + 1) / (xi + 1) / 4;
        d3 = p / q / MathLib.pow(woo, (p - q));
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        timeStep = stateTable.terr(qgsStates, cqgsStates, timeStep);
        return stateTable.terr(qgdStates, qgdStates, timeStep);
    }

    private double psIds(Mode mode, double vgs, double vgd) {
        double fx = -10.0; // not too small else fatal rounding error in (rpt - a_rpt)
        double mx = 40.0;  // Maximum exponential argument
        double emx = 2.353852668370199842e17;  //exp(MX)
        double idrain = 0;
        double arg;
        double zz;

        double gMin = env.getGMin();
        double vT = this.nvt;
        double isat = this.tSatCur * area;
        if ((arg = vgs / vT) > fx) {
            if (arg < mx) {
                zz = isat * MathLib.exp(arg);
                ggs = zz / vT + gMin;
                igs = zz - isat + gMin * vgs;
            } else {
                zz = isat * emx;
                ggs = zz / vT + gMin;
                igs = zz * (arg - mx + 1) - isat + gMin * vgs;
            }
        } else {
            ggs = gMin;
            igs = -isat + gMin * vgs;
        }
        if ((arg = vgd / vT) > fx) {
            if (arg < mx) {
                ggd = (zz = isat * MathLib.exp(arg)) / vT + gMin;
                igd = zz - isat + gMin * vgd;
            } else {
                ggd = (zz = isat * emx) / vT + gMin;
                igd = zz * (arg - mx + 1) - isat + gMin * vgd;
            }
        } else {
            ggd = gMin;
            igd = -isat + gMin * vgd;
        }
        double _ibd = this.ibd * area;
        if ((arg = -vgs / vbd) > fx) {
            if (arg < mx) {
                ggs += (zz = _ibd * MathLib.exp(arg)) / vbd;
                igs -= zz - _ibd;
            } else {
                ggs += (zz = _ibd * emx) / vbd;
                igs -= zz * ((arg - mx) + 1) - _ibd;
            }
        } else {
            igs += _ibd;
        }
        if ((arg = -vgd / vbd) > fx) {
            if (arg < mx) {
                ggd += (zz = _ibd * MathLib.exp(arg)) / vbd;
                igd -= zz - _ibd;
            } else {
                ggd += (zz = _ibd * emx) / vbd;
                igd -= zz * ((arg - mx) + 1) - _ibd;
            }
        } else {
            igd += _ibd;
        }
        gm = 0;
        gds = 0;
        double vdst = vgs - vgd;
        double stepofour = tmprl.getDelta() * 0.4;
        // Include rate dependent threshold modulation
        double h;
        double vgdtrap;
        double vgstrap;
        if (mode.contains(MODE.TRAN)) {
            h = MathLib.pow(taug / (taug + stepofour), 4);
            vgdtrap = h * vtrapStates.get(1) + (1 - h) * vgd;
            vtrapStates.set(0, vgdtrap);
            vgstrap = h * vgstrapStates.get(1) + (1 - h) * vgs;
            vgstrapStates.set(0, vgstrap);
        } else {
            h = 0;
            vgstrap = vgs;
            vgdtrap = vgd;
            vtrapStates.set(0, vgd);
            vgstrapStates.set(0, vgs);
        }
        double vgst = vgs - vto;
        vgst -= (lfgam - lfg1 * vgstrap + lfg2 * vgdtrap) * vgdtrap;
        double dvgs = vgstrap - vgs;
        double eta = hfeta - hfe1 * vgdtrap + hfe2 * vgstrap;
        vgst += eta * dvgs;
        double dvgd = vgdtrap - vgd;
        double gam = -hfg1 * vgstrap + hfg2 * vgdtrap;
        vgst += gam * dvgd;
        double vgt;
        double subfac;
        double _vst = this.vst * (1 + mvst * vdst);
        if (vgst > fx * _vst) {
            if (vgst > (arg = mx * _vst)) { // Numerically large
                subfac = emx + 1;
                vgt = (emx / subfac) * (vgst - arg) + arg;
            } else {// limit gate bias exponentially
                subfac = 1 + MathLib.exp(vgst / _vst);
                vgt = _vst * MathLib.log(subfac);
            }
            double mQ = q;
            double pmQ = p - mQ;
            double dvpd_dvdst = d3 * MathLib.pow(vgt, pmQ);
            double vdp = vdst * dvpd_dvdst;
            double vsatFac = vgt / (mxi * vgt + xiwoo);
            double vsat = vgt / (1 + vsatFac);
            double aa = za * vdp + vsat / 2.0;
            double a_aa = aa - vsat;
            arg = vsat * vsat * z / 4.0;
            double rpt = MathLib.sqrt(aa * aa + arg);
            double a_rpt = MathLib.sqrt(a_aa * a_aa + arg);
            double vdt = (rpt - a_rpt);
            double dvdt_dvdp = za * (aa / rpt - a_aa / a_rpt);
            double dvdt_dvgt = (vdt - vdp * dvdt_dvdp) * (1 + mxi * vsatFac * vsatFac) / (1 + vsatFac) / vgt;
            gds = MathLib.pow(vgt - vdt, mQ - 1);
            gm = MathLib.pow(vgt, mQ - 1);
            idrain = vdt * gds + vgt * (gm - gds);
            gds *= mQ;
            gm *= mQ;
            gm += gds * dvdt_dvgt;
            gds *= dvdt_dvdp;
            gm += gds * pmQ * vdp / vgt;
            gds *= dvpd_dvdst;
            arg = 1 - 1 / subfac;
            if (_vst != 0) {
                gds += gm * this.vst * mvst * (vgt - vgst * arg) / _vst;
                gm *= arg;
            } else {
                // Extreme cut - off
                idrain = gm = gds = 0.0e0;
            }
        }
        gds += gm * (arg = h * gam
                + (1 - h) * (hfe1 * dvgs - hfg2 * dvgd + 2 * lfg2 * vgdtrap - lfg1 * vgstrap + lfgam));
        gm *= 1 - h * eta + (1 - h) * (hfe2 * dvgs - hfg1 * dvgd + lfg1 * vgdtrap) - arg;
        // Apply channel length modulation and beta scaling
        double _beta = this.beta * area;
        gm *= (arg = _beta * (1 + lambda * vdst));
        gds = _beta * lambda * idrain + gds * arg;
        idrain *= arg;
        double pAverage;
        double _delta = delta / area;
        if (mode.contains(MODE.TRAN)) {
            h = taud / (taud + stepofour);
            h *= h;
            h *= h;
            pAverage = h * paveStates.get(1) + (1 - h) * vdst * idrain;
            paveStates.set(0, pAverage);
        } else {
            pAverage = vdst * idrain;
            paveStates.set(0, pAverage);
            paveStates.set(1, pAverage);
            h = 0;
        }
        double pfac = 1 + pAverage * _delta;
        idrain /= pfac;
        gm = gm * (arg = (h * _delta * paveStates.get(1) + 1) / pfac / pfac);
        gds = gds * arg - (1 - h) * _delta * idrain * idrain;
        return idrain;
    }

    private double qgg(double vgs, double vgd, double gamma, double pb,
            double alpha, double vto, double corDepCap, double xc) {
        double qrt;
        double ext;
        double _cgso;
        double cpm;
        double vds = vgs - vgd;
        double d1_xc = 1 - xc;
        double vert = MathLib.sqrt(vds * vds + alpha);
        double veff = 0.5 * (vgs + vgd + vert) + gamma * vds;
        double vnr = d1_xc * (veff - vto);
        double vnrt = MathLib.sqrt(vnr * vnr + 0.04);
        double vnew = veff + 0.5 * (vnrt - vnr);
        if (vnew < corDepCap) {
            ext = 0;
            qrt = MathLib.sqrt(1 - vnew / pb);
            _cgso = 0.5 * czgs / qrt * (1 + xc + d1_xc * vnr / vnrt);
        } else {
            double vx = 0.5 * (vnew - corDepCap);
            double par = 1 + vx / (pb - corDepCap);
            qrt = MathLib.sqrt(1 - corDepCap / pb);
            ext = vx * (1 + par) / qrt;
            _cgso = 0.5 * czgs / qrt * (1 + xc + d1_xc * vnr / vnrt) * par;
        }
        double cplus = 0.5 * (1 + (cpm = vds / vert));
        double cminus = cplus - cpm;
        cgs = _cgso * (cplus + gamma) + czgd * (cminus + gamma);
        cgd = _cgso * (cminus - gamma) + czgd * (cplus - gamma);
        return (czgs * ((pb + pb) * (1 - qrt) + ext) + czgd * (veff - vert));
    }
//not used

    private void psInstanceinit() {
        double woo = (vbi - vto);
        xiwoo = xi * woo;
        za = MathLib.sqrt(1 + z) / 2;
        alpha = xiwoo * xiwoo / (xi + 1) / (xi + 1) / 4;
        d3 = p / q / MathLib.pow(woo, (p - q));
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean convTest(Mode mode) {
        return true;
    }
}
