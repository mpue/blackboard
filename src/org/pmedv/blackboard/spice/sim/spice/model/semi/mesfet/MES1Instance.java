/*
 * mes.java
 *
 * Created on August 28, 2006, 2:30; PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet;

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
import ktb.math.Bool;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class MES1Instance extends MES1ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;

    /* Parallel multiplier */
    private double m = 1.0;
    private int mode;
    private double drnConduct;
    private double srcConduct;
    private double f1;
    private double f2;
    private double f3;
    private double vcrit;
    private Complex drnDrnPrmNode;
    private Complex gateDrnPrmNode;
    private Complex gateSrcPrmNode;
    private Complex srcSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPrmGateNode;
    private Complex drnPrmSrcPrmNode;
    private Complex srcPrmGateNode;
    private Complex srcPrmSrcNode;
    private Complex srcPrmDrnPrmNode;
    private Complex drnDrnNode;
    private Complex gateGateNode;
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
    private double cgs;
    private double cgd;
    private Real ceq = Real.zero();
    private Real geq = Real.zero();
    private Bool iCheck = Bool.FALSE();
    private Bool iChk1 = Bool.TRUE();

    public MES1Instance() {
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
        double vds = 0;
        double vgd = 0;
        double vgs = 0;
        double cg = 0;
        double _cgd = 0;
        double cd = 0;
        double gds = 0;
        double ggd = 0;
        double ggs = 0;
        double gm = 0;
        double cdhat = 0.0;
        double idrain;
        double cghat = 0.0;
        boolean converged = true;
        boolean bypass = false;
        double _beta = this.beta * area;
        double csat = gateSatCurrent * area;
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
        } else if (mode.contains(MODE.INIT_JCT)
                || (mode.contains(MODE.INIT_FIX) && off)) {
            vgs = 0;
            vgd = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(1);
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * vgsStates.get(1)
                        - xfact * vgsStates.get(2);
                vgdStates.set(0, vgdStates.get(1));
                vgd = (1 + xfact) * vgdStates.get(1)
                        - xfact * vgdStates.get(2);
                cgStates.set(0, cgStates.get(1));
                cdStates.set(0, cdStates.get(1));
                cgdStates.set(0, cgdStates.get(1));
                gmStates.set(0, gmStates.get(1));
                gdsStates.set(0, gdsStates.get(1));
                ggsStates.set(0, ggsStates.get(1));
                ggdStates.set(0, ggdStates.get(1));
            } else {
                // Compute nonlinear branch voltages
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
            if (env.isBypass() && !mode.contains(MODE.INIT_PRED)
                    && (MathLib.abs(delvgs) < env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                    MathLib.abs(vgsStates.get(0)))
                    + env.getVoltTol())) {
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
                            ggs = ggsStates.get(0);
                            ggd = ggdStates.get(0);
                            bypass = true;
                        }
                    }
                }
            }
            if (!bypass) {
                vgs = MOSUtils.pnjLimit(vgs, vgsStates.get(0), VT0, vcrit, iCheck);
                vgd = MOSUtils.pnjLimit(vgd, vgdStates.get(0), VT0, vcrit, iChk1);
                if (iChk1.isTrue()) {
                    iCheck.setTrue();
                }
                vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), vto);
                vgd = MOSUtils.fetLimit(vgd, vgdStates.get(0), vto);
            }
        }
        if (!bypass) {
            // Determine dc current and derivatives
            double arg;
            vds = vgs - vgd;
            if (vgs <= -3 * VT0) {
                arg = 3 * VT0 / (vgs * MathLib.E);
                arg = arg * arg * arg;
                cg = -csat * (1 + arg) + env.getGMin() * vgs;
                ggs = csat * 3 * arg / vgs + env.getGMin();
            } else {
                double evgs = MathLib.exp(vgs / VT0);
                ggs = csat * evgs / VT0 + env.getGMin();
                cg = csat * (evgs - 1) + env.getGMin() * vgs;
            }
            if (vgd <= -3 * VT0) {
                arg = 3 * VT0 / (vgd * MathLib.E);
                arg = arg * arg * arg;
                _cgd = -csat * (1 + arg) + env.getGMin() * vgd;
                ggd = csat * 3 * arg / vgd + env.getGMin();
            } else {
                double evgd = MathLib.exp(vgd / VT0);
                ggd = csat * evgd / VT0 + env.getGMin();
                _cgd = csat * (evgd - 1) + env.getGMin() * vgd;
            }
            cg = cg + _cgd;
            // Compute drain current and derivitives for normal mode
            double denom;
            double invdenom;
            double lfact;
            double prod;
            double afact;
            double betap;
            if (vds >= 0) {
                double vgst = vgs - vto;
                // Normal mode, cutoff region
                if (vgst <= 0) {
                    idrain = 0;
                    gm = 0;
                    gds = 0;
                } else {
                    prod = 1 + lambda * vds;
                    betap = _beta * prod;
                    denom = 1 + b * vgst;
                    invdenom = 1 / denom;
                    if (vds >= (3 / alpha)) {
                        // Normal mode, saturation region
                        idrain = betap * vgst * vgst * invdenom;
                        gm = betap * vgst * (1 + denom) * invdenom * invdenom;
                        gds = lambda * _beta * vgst * vgst
                                * invdenom;
                    } else {
                        // Normal mode, linear region
                        afact = 1 - alpha * vds / 3;
                        lfact = 1 - afact * afact * afact;
                        idrain = betap * vgst * vgst * invdenom * lfact;
                        gm = betap * vgst * (1 + denom) * invdenom * invdenom
                                * lfact;
                        gds = _beta * vgst * vgst * invdenom * (alpha
                                * afact * afact * prod + lfact
                                * lambda);
                    }
                }
            } else {
                // Compute drain current and derivitives for inverse mode
                double vgdt = vgd - vto;
                if (vgdt <= 0) {
                    // Inverse mode, cutoff region
                    idrain = 0;
                    gm = 0;
                    gds = 0;
                } else {
                    // Inverse mode, saturation region
                    prod = 1 - lambda * vds;
                    betap = _beta * prod;
                    denom = 1 + b * vgdt;
                    invdenom = 1 / denom;
                    if (-vds >= (3 / alpha)) {
                        idrain = -betap * vgdt * vgdt * invdenom;
                        gm = -betap * vgdt * (1 + denom) * invdenom * invdenom;
                        gds = lambda * _beta * vgdt * vgdt * invdenom - gm;
                    } else {
                        // Inverse mode, linear region
                        afact = 1 + alpha * vds / 3;
                        lfact = 1 - afact * afact * afact;
                        idrain = -betap * vgdt * vgdt * invdenom * lfact;
                        gm = -betap * vgdt * (1 + denom) * invdenom
                                * invdenom * lfact;
                        gds = _beta * vgdt * vgdt * invdenom * (alpha
                                * afact * afact * prod + lfact
                                * lambda) - gm;
                    }
                }
            }
            // Compute equivalent drain current source
            cd = idrain - _cgd;
            if (mode.contains(MODE.TRAN, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                // Charge storage elements
                cgs = capGS * area;
                cgd = capGD * area;
                double phib = gatePotential;
                double vgs1 = vgsStates.get(1);
                double vgd1 = vgdStates.get(1);
                double vcap = 1 / alpha;
                double qgga = qgg(vgs, vgd, phib, vcap, vto);//, cgsna, cgdna);
                double capgs = cgs;
                double capgd = cgd;
                double qggb = qgg(vgs1, vgd, phib, vcap, vto);//, cgsnb, cgdnb);
                double qggc = qgg(vgs, vgd1, phib, vcap, vto);//, cgsnc, cgdnc);
                double qggd = qgg(vgs1, vgd1, phib, vcap, vto);//, cgsnd, cgdnd);
                if (mode.contains(MODE.INIT_TRAN)) {
                    qgsStates.set(1, qgga);
                    qgdStates.set(1, qgga);
                }
                qgsStates.set(0, qgsStates.get(1) + 0.5 * (qgga - qggb + qggc - qggd));
                qgdStates.set(0, qgdStates.get(1) + 0.5 * (qgga - qggc + qggb - qggd));
                // Store small - signal parameters
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        qgsStates.set(0, capgs);
                        qgdStates.set(0, capgd);
                        return true;
                    }
                    // Transient analysis
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
                    _cgd = _cgd + cqgdStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqgsStates.set(1, cqgsStates.get(0));
                        cqgdStates.set(1, cqgdStates.get(0));
                    }
                }
            }
            // Check convergence
            if (!mode.contains(MODE.INIT_FIX) || !mode.isUseIC()) {
                if ((iCheck.isTrue())
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
            ggsStates.set(0, ggs);
            ggdStates.set(0, ggd);
        }
        // Load current vector
        double ceqgd = type * (_cgd - ggd * vgd);
        double ceqgs = type * ((cg - _cgd) - ggs * vgs);
        double cdreq = type * ((cd + _cgd) - gds * vds - gm * vgs);
        wrk.getRhsAt(gateIndex).realPlusEq(m * (-ceqgs - ceqgd));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (-cdreq + ceqgd));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqgs));
        // Load y matrix
        double gdpr = drnConduct * area;
        double gspr = srcConduct * area;
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

    private double qgg(double vgs, double vgd, double phib, double vcap,
            double vto) {
        double veroot = MathLib.sqrt((vgs - vgd) * (vgs - vgd) + vcap * vcap);
        double veff1 = 0.5 * (vgs + vgd + veroot);
        double veff2 = veff1 - veroot;
        double del = 0.2;
        double vnroot = MathLib.sqrt((veff1 - vto) * (veff1 - vto) + del * del);
        double vnew1 = 0.5 * (veff1 + vto + vnroot);
        double vnew3 = vnew1;
        double vmax = 0.5;
        double ext;
        if (vnew1 < vmax) {
            ext = 0;
        } else {
            vnew1 = vmax;
            ext = (vnew3 - vmax) / MathLib.sqrt(1 - vmax / phib);
        }
        double qroot = MathLib.sqrt(1 - vnew1 / phib);
        double qggval = cgs * (2 * phib * (1 - qroot) + ext) + cgd * veff2;
        double par1 = 0.5 * (1 + (veff1 - vto) / vnroot);
        double cfact = (vgs - vgd) / veroot;
        double cplus = 0.5 * (1 + cfact);
        double cminus = cplus - cfact;
        cgs = cgs / qroot * par1 * cplus + cgd * cminus;
        cgd = cgs / qroot * par1 * cminus + cgd * cplus;
        return qggval;
    }

    public boolean unSetup() {
        if (srcPrmIndex != 0 && srcPrmIndex != srcIndex) {
            ckt.deleteNode(srcPrmIndex);
        }
        srcPrmIndex = 0;
        if (drnPrmIndex != 0 && drnPrmIndex != drnIndex) {
            ckt.deleteNode(drnPrmIndex);
        }
        drnPrmIndex = 0;
        return true;
    }

    public boolean temperature() {
        depletionCap = depletionCapCoeff * gatePotential;
        double xfc = (1 - depletionCapCoeff);
        double temp = MathLib.sqrt(xfc);
        f1 = gatePotential * (1 - temp) / (1 - .5);
        f2 = temp * temp * temp;
        f3 = 1 - depletionCapCoeff * (1 + .5);
        vcrit = VT0 * MathLib.log(VT0 / (ROOT2 * gateSatCurrent));
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
