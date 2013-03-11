/*
 * jfet.java
 *
 * Created on August 28, 2006, 1:40 PM
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
public class JFET1Instance extends JFET1ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;

    /* Instance temperature difference */
    private double dtemp;

    /* Temperature corrected saturation current */
    private double tSatCur;

    /* Temperature corrected gate potential */
    private double tGatePot;

    /* Temperature corrected G - S capacitance */
    private double tCGS;

    /* Temperature corrected G - D capacitance */
    private double tCGD;

    /* Joining point of forward bias depletion capacitance */
    private double corDepCap;

    /* Critical voltage */
    private double vcrit;

    /* Coefficient of capacitance polynomial */
    private double f1;
    private double drnConduct;
    private double srcConduct;
    private double f2;
    private double f3;

    /* Internal derived doping profile parameter */
    private double bFac;

    private Complex drnDrnPrmNode;
    private Complex gateSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPrmSrcPrmNode;
    private Complex srcPrmSrcNode;
    private Complex drnDrnNode;
    private Complex srcSrcNode;
    private Complex srcPrmSrcPrmNode;
    private Complex gateDrnPrmNode;
    private Complex srcSrcPrmNode;
    private Complex drnPrmGateNode;
    private Complex srcPrmGateNode;
    private Complex srcPrmDrnPrmNode;
    private Complex gateGateNode;
    private Complex drnPrmDrnPrmNode;
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
    private Real ceq = Real.zero();
    private Real geq = Real.zero();
    private Bool icheck = Bool.FALSE();
    private Bool ichk1 = Bool.FALSE();

    public JFET1Instance() {
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
        if (drnResist != 0) {
            drnConduct = 1 / drnResist;
        } else {
            drnConduct = 0;
        }
        if (srcResist != 0) {
            srcConduct = 1 / srcResist;
        } else {
            srcConduct = 0;
        }
        if (srcResist != 0 && srcPrmIndex == 0) {
            srcPrmIndex = ckt.makeVoltNode(getInstName() + "source").getIndex();
        } else {
            srcPrmIndex = srcIndex;
        }
        if (drnResist != 0 && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if (drnResist != 0) {
            drnConduct = 1 / drnResist;
        } else {
            drnConduct = 0;
        }
        if (srcResist != 0) {
            srcConduct = 1 / srcResist;
        } else {
            srcConduct = 0;
        }
        drnDrnPrmNode = wrk.aquireNode(drnIndex, drnPrmIndex);
        gateDrnPrmNode = wrk.aquireNode(gateIndex, drnPrmIndex);
        gateSrcPrmNode = wrk.aquireNode(gateIndex, srcPrmIndex);
        srcSrcPrmNode = wrk.aquireNode(srcIndex, srcPrmIndex);
        drnPrmDrnNode = wrk.aquireNode(drnPrmIndex, drnIndex);
        drnPrmGateNode = wrk.aquireNode(drnPrmIndex, gateIndex);
        drnPrmSrcPrmNode = wrk.aquireNode(drnPrmIndex, srcPrmIndex);
        srcPrmGateNode = wrk.aquireNode(srcPrmIndex, gateIndex);
        srcPrmSrcNode = wrk.aquireNode(srcPrmIndex, srcIndex);
        srcPrmDrnPrmNode = wrk.aquireNode(srcPrmIndex, drnPrmIndex);
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        gateGateNode = wrk.aquireNode(gateIndex, gateIndex);
        srcSrcNode = wrk.aquireNode(srcIndex, srcIndex);
        drnPrmDrnPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmIndex);
        srcPrmSrcPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmIndex);
        return true;
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

    public boolean acLoad(Mode mode) {
        double gdpr = drnConduct * area;
        double gspr = srcConduct * area;
        double gm = gmStates.get(0);
        double gds = gdsStates.get(0);
        double ggs = ggsStates.get(0);
        double xgs = qgsStates.get(0) * tmprl.getOmega();
        double ggd = ggdStates.get(0);
        double xgd = qgdStates.get(0) * tmprl.getOmega();
        drnDrnNode.realPlusEq(m * (gdpr));
        gateGateNode.realPlusEq(m * (ggd + ggs));
        gateGateNode.imagPlusEq(m * (xgd + xgs));
        srcSrcNode.realPlusEq(m * (gspr));
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + ggd));
        drnPrmDrnPrmNode.imagPlusEq(m * (xgd));
        srcPrmSrcPrmNode.realPlusEq(m * (gspr + gds + gm + ggs));
        srcPrmSrcPrmNode.imagPlusEq(m * (xgs));
        drnDrnPrmNode.realMinusEq(m * (gdpr));
        gateDrnPrmNode.realMinusEq(m * (ggd));
        gateDrnPrmNode.imagMinusEq(m * (xgd));
        gateSrcPrmNode.realMinusEq(m * (ggs));
        gateSrcPrmNode.imagMinusEq(m * (xgs));
        srcSrcPrmNode.realMinusEq(m * (gspr));
        drnPrmDrnNode.realMinusEq(m * (gdpr));
        drnPrmGateNode.realPlusEq(m * (-ggd + gm));
        drnPrmGateNode.imagMinusEq(m * (xgd));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGateNode.realPlusEq(m * (-ggs - gm));
        srcPrmGateNode.imagMinusEq(m * (xgs));
        srcPrmSrcNode.realMinusEq(m * (gspr));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
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
        double capgd;
        double capgs;
        double cd = 0;
        double cdhat = 0.0;
        double idrain;
        double cg = 0;
        double cgd = 0;
        double cghat = 0.0;
        double gds = 0;
        double ggd = 0;
        double ggs = 0;
        double gm = 0;
        double sarg;
        double vds = 0;
        double vgd = 0;
        double vgs = 0;
        double _beta = this.beta * area;
        double gdpr = drnConduct * area;
        double gspr = srcConduct * area;
        double csat = tSatCur * area;
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
        } else if ((mode.contains(MODE.INIT_JCT)
                || mode.contains(MODE.INIT_FIX)) && off) {
            vgs = 0;
            vgd = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * vgsStates.get(1) - xfact
                        * vgsStates.get(2);
                vgdStates.set(0, vgdStates.get(1));
                vgd = (1 + xfact) * vgdStates.get(1) - xfact
                        * vgdStates.get(2);
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
            if (env.isBypass()
                    && !(mode.contains(MODE.INIT_PRED)
                    && (MathLib.abs(delvgs) < env.getRelTol()
                    * MathLib.max(MathLib.abs(vgs),
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
                            cgd = cgdStates.get(0);
                            gm = gmStates.get(0);
                            gds = gdsStates.get(0);
                            ggs = ggsStates.get(0);
                            ggd = ggdStates.get(0);
                            bypass = true;
                        }
                    }
                }
            }
            if(!bypass) {
                //   Limit nonlinear branch voltages
                ichk1.setTrue();
                vgs = MOSUtils.pnjLimit(vgs, vgsStates.get(0),
                        (temp * KoverQ), vcrit, icheck);
                vgd = MOSUtils.pnjLimit(vgd, vgdStates.get(0),
                        (temp * KoverQ), vcrit, ichk1);
                if (ichk1.isFalse()) {
                    icheck.setFalse();
                }
                vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0),
                        vto);
                vgd = MOSUtils.fetLimit(vgd, vgdStates.get(0),
                        vto);
            }
        }
        if (!bypass) {
            //    Determine dc current and derivatives
            vds = vgs - vgd;
            double vtTemp = temp * KoverQ;
            double arg;
            if (vgs < -3 * vtTemp) {
                arg = 3 * vtTemp / (vgs * MathLib.E);
                arg = arg * arg * arg;
                cg = -csat * (1 + arg) + env.getGMin() * vgs;
                ggs = csat * 3 * arg / vgs + env.getGMin();
            } else {
                double evgs = MathLib.exp(vgs / vtTemp);
                ggs = csat * evgs / vtTemp + env.getGMin();
                cg = csat * (evgs - 1) + env.getGMin() * vgs;
            }
            if (vgd < -3 * vtTemp) {
                arg = 3 * vtTemp / (vgd * MathLib.E);
                arg = arg * arg * arg;
                cgd = -csat * (1 + arg) + env.getGMin() * vgd;
                ggd = csat * 3 * arg / vgd + env.getGMin();
            } else {
                double evgd = MathLib.exp(vgd / vtTemp);
                ggd = csat * evgd / vtTemp + env.getGMin();
                cgd = csat * (evgd - 1) + env.getGMin() * vgd;
            }
            cg = cg + cgd;
            double betap;
            double vgdt;
            double vgst;
            // Modification for Sydney University JFET model
            double apart;
            double cpart;
            double bfac;
            if (vds >= 0) {
                vgst = vgs - vto;
                //  Compute drain current and derivatives for normal mode
                if (vgst <= 0) {
                    //  Normal mode, cutoff region
                    idrain = 0;
                    gm = 0;
                    gds = 0;
                } else {
                    betap = _beta * (1 + lambda * vds);
                    bfac = bFac;
                    if (vgst >= vds) {
                        //  Normal mode, linear region
                        apart = 2 * b + 3 * bfac * (vgst - vds);
                        cpart = vds * (vds * (bfac * vds - b) + vgst * apart);
                        idrain = betap * cpart;
                        gm = betap * vds * (apart + 3 * bfac * vgst);
                        gds = betap * (vgst - vds) * apart + _beta * lambda * cpart;
                    } else {
                        bfac = vgst * bfac;
                        gm = betap * vgst * (2 * b + 3 * bfac);
                        //  Normal mode, saturation region
                        cpart = vgst * vgst * (b + bfac);
                        idrain = betap * cpart;
                        gds = lambda * _beta * cpart;
                    }
                }
            } else {
                vgdt = vgd - vto;
                //  Compute drain current and derivatives for inverse mode
                if (vgdt <= 0) {
                    //  Inverse mode, cutoff region
                    idrain = 0;
                    gm = 0;
                    gds = 0;
                } else {
                    betap = _beta * (1 - lambda * vds);
                    bfac = bFac;
                    if (vgdt + vds >= 0) {
                        //  Inverse mode, linear region
                        apart = 2 * b + 3 * bfac * (vgdt + vds);
                        cpart = vds * (-vds * (-bfac * vds - b) + vgdt * apart);
                        idrain = betap * cpart;
                        gm = betap * vds * (apart + 3 * bfac * vgdt);
                        gds = betap * (vgdt + vds) * apart - _beta * lambda * cpart - gm;
                    } else {
                        bfac = vgdt * bfac;
                        gm = -betap * vgdt * (2 * b + 3 * bfac);
                        //  Inverse mode, saturation region
                        cpart = vgdt * vgdt * (b + bfac);
                        idrain = -betap * cpart;
                        gds = lambda * _beta * cpart - gm;
                    }
                }
            }
            //    Compute equivalent drain current source
            cd = idrain - cgd;
            if (mode.contains(MODE.TRAN, MODE.AC, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                //     Charge storage elements
                double czgs = tCGS * area;
                double czgd = tCGD * area;
                double twop = tGatePot + tGatePot;
                double fcpb2 = corDepCap * corDepCap;
                double czgsf2 = czgs / f2;
                double czgdf2 = czgd / f2;
                if (vgs < corDepCap) {
                    sarg = MathLib.sqrt(1 - vgs / tGatePot);
                    qgsStates.set(0, twop * czgs * (1 - sarg));
                    capgs = czgs / sarg;
                } else {
                    qgsStates.set(0, czgs * f1
                            + czgsf2 * (f3 * (vgs
                            - corDepCap) + (vgs * vgs - fcpb2)
                            / (twop + twop)));
                    capgs = czgsf2 * (f3 + vgs / twop);
                }
                if (vgd < corDepCap) {
                    sarg = MathLib.sqrt(1 - vgd / tGatePot);
                    qgdStates.set(0, twop * czgd * (1 - sarg));
                    capgd = czgd / sarg;
                } else {
                    qgdStates.set(0, czgd * f1
                            + czgdf2 * (f3 * (vgd
                            - corDepCap) + (vgd * vgd - fcpb2)
                            / (twop + twop)));
                    capgd = czgdf2 * (f3 + vgd / twop);
                }
                //    Store small - signal parameters
                if (!mode.contains(MODE.TRANOP)
                        || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        qgsStates.set(0, capgs);
                        qgdStates.set(0, capgd);
                        return true;
                    }
                    //    Transient analysis
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qgsStates.set(1, qgsStates.get(0));
                        qgdStates.set(1, qgdStates.get(0));
                    }
                    stateTable.integrate(geq, ceq, capgs, qgsStates, cqgsStates);
                    ggs = ggs + geq.get();
                    cg = cg + cqgsStates.get(0);
                    stateTable.integrate(geq, ceq, capgd, qgdStates, cqgdStates);
                    ggd = ggd + geq.get();
                    cg = cg + cqgdStates.get(0);
                    cd = cd - cqgdStates.get(0);
                    cgd = cgd + cqgdStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqgsStates.set(1, cqgsStates.get(0));
                        cqgdStates.set(1, cqgdStates.get(0));
                    }
                }
            }
            //   Check convergence
            if (!mode.contains(MODE.INIT_FIX) || !mode.isUseIC()) {
                if ((icheck.isFalse())
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
            cgdStates.set(0, cgd);
            gmStates.set(0, gm);
            gdsStates.set(0, gds);
            ggsStates.set(0, ggs);
            ggdStates.set(0, ggd);
        }
        //     Load current vector
        double ceqgd = type * (cgd - ggd * vgd);
        double ceqgs = type * ((cg - cgd) - ggs * vgs);
        double cdreq = type * ((cd + cgd) - gds * vds - gm * vgs);
        wrk.getRhsAt(gateIndex).realPlusEq(m * (-ceqgs - ceqgd));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (-cdreq + ceqgd));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqgs));
        //     Load y matrix
        drnDrnPrmNode.realPlusEq(m * (-gdpr));
        gateDrnPrmNode.realPlusEq(m * (-ggd));
        gateSrcPrmNode.realPlusEq(m * (-ggs));
        srcSrcPrmNode.realPlusEq(m * (-gspr));
        drnPrmDrnNode.realPlusEq(m * (-gdpr));
        drnPrmGateNode.realPlusEq(m * (gm - ggd));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGateNode.realPlusEq(m * (-ggs - gm));
        srcPrmSrcNode.realPlusEq(m * (-gspr));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds));
        drnDrnNode.realPlusEq(m * (gdpr));
        gateGateNode.realPlusEq(m * (ggd + ggs));
        srcSrcNode.realPlusEq(m * (gspr));
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + ggd));
        srcPrmSrcPrmNode.realPlusEq(m * (gspr + gds + gm + ggs));
        return converged;
    }

    public boolean temperature() {
        if (tnom == 0) {
            tnom = env.getNomTemp();
        }
        double vtnom = KoverQ * tnom;
        double fact1 = tnom / REF_TEMP;
        double kt1 = BOLTZMANN * tnom;
        double egfet1 = 1.16 - (7.02e-4 * tnom * tnom) / (tnom + 1108);
        double arg1 = -egfet1 / (kt1 + kt1) + 1.1150877 / (BOLTZMANN * (REF_TEMP + REF_TEMP));
        double pbfact1 = -2 * vtnom * (1.5 * MathLib.log(fact1) + CHARGE * arg1);
        double pbo = (phi - pbfact1) / fact1;
        double gmaold = (phi - pbo) / pbo;
        double cjfact = 1 / (1 + .5 * (4e-4 * (tnom - REF_TEMP) - gmaold));
        if (depCapCoeff > 0.95) {
            StandardLog.warning("Front: " + getModelName() + ": Depletion cap. coefficient too large, limited to .95");
            depCapCoeff = 0.95;
        }
        double xfc = MathLib.log(1 - depCapCoeff);
        f2 = MathLib.exp((1 + 0.5) * xfc);
        f3 = 1 - depCapCoeff * (1 + 0.5);
        // Modification for Sydney University JFET model
        bFac = (1 - b) / (phi - vto);
        // end Sydney University model
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
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        timeStep = stateTable.terr(qgsStates, cqgsStates, timeStep);
        timeStep = stateTable.terr(qgdStates, cqgdStates, timeStep);
        return timeStep;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean convTest(Mode mode) {
        return true;
    }
}
