/*
 * hfet2.java
 *
 * Created on August 28, 2006, 2:19; PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.hfet;

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
import ktb.math.Bool;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFET2Instance extends HFET2ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    private int drnPrmIndex;
    private int srcPrmIndex;
    private double length = 1e-6;
    private double width = 20e-6;
    private double mM = 3.0;
    private double dtemp;
    private double tLambda;
    private double tMu;
    private double tNmax;
    private double tVto;
    private int mode;
    private int off;
    private double n0;
    private double n01;
    private double n02;
    private double gchi0;
    private double imax;
    private double vcrit;
    private double ggrlw;
    private double jslw;
    private double drnConduct;
    private double srcConduct;
    private double deltaSqr;
    private Complex drnDrnPrmNode;
    private Complex gateSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPriSrcPrmNode;
    private Complex srcPriHFET2ourceNode;
    private Complex drnDrnNode;
    private Complex srcSrcNode;
    private Complex srcPriSrcPrmNode;
    private Complex gateDrnPrmNode;
    private Complex srcSrcPrmNode;
    private Complex drnPrmGateNode;
    private Complex srcPrmGateNode;
    private Complex srcPrmDrnPrmNode;
    private Complex drnPrmDrnPrmNode;
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
    private Complex gateGateNode;
    private StateVector vgsStates;
    private double idrain;
    private double gm;
    private double gds;
    private double capgs;
    private double capgd;
    private Real ceq = Real.zero();
    private Real geq = Real.zero();
    private Bool icheck = Bool.TRUE();
    private Bool ichk1 = Bool.TRUE();

    public HFET2Instance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
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
        if (eta == 0) {
            if (type == N_TYPE) {
                eta = 1.28;
            } else {
                eta = 1.4;
            }
        }
        if (mu == 0) {
            if (type == N_TYPE) {
                mu = 0.4;
            } else {
                mu = 0.03;
            }
        }
        if (vs == 0) {
            if (type == N_TYPE) {
                vs = 1.5e5;
            } else {
                vs = 0.8e5;
            }
        }
        if (vto == 0) {
            if (type == N_TYPE) {
                vto = 0.15;
            } else {
                vto = -0.15;
            }
        }
        if ((srcPrmIndex = ckt.makeVoltNode(getInstName() + "source").getIndex()) == 0) {
            srcPrmIndex = srcIndex;
        }
        if (rd != 0 && drnPrmIndex == 0) {
            if ((drnPrmIndex = ckt.makeVoltNode(getInstName() + "drain").getIndex()) == 0) {
                drnPrmIndex = drnIndex;
            }
        }
        if (rd != 0) {
            drnConduct = 1 / rd;
        } else {
            drnConduct = 0;
        }
        if (rs != 0) {
            srcConduct = 1 / rs;
        } else {
            srcConduct = 0;
        }
        if (vt1 == 0) {
            vt1 = vto + CHARGE * nmax * di / EPS_SI;
        }
        if (vt2 == 0) {
            vt2 = vto;
        }
        deltaSqr = delta * delta;
        drnDrnPrmNode = wrk.aquireNode(drnIndex, drnPrmIndex);
        gateDrnPrmNode = wrk.aquireNode(gateIndex, drnPrmIndex);
        gateSrcPrmNode = wrk.aquireNode(gateIndex, srcPrmIndex);
        srcSrcPrmNode = wrk.aquireNode(srcIndex, srcPrmIndex);
        drnPrmDrnNode = wrk.aquireNode(drnPrmIndex, drnIndex);
        drnPrmGateNode = wrk.aquireNode(drnPrmIndex, gateIndex);
        drnPriSrcPrmNode = wrk.aquireNode(drnPrmIndex, srcPrmIndex);
        srcPrmGateNode = wrk.aquireNode(srcPrmIndex, gateIndex);
        srcPriHFET2ourceNode = wrk.aquireNode(srcPrmIndex, srcIndex);
        srcPrmDrnPrmNode = wrk.aquireNode(srcPrmIndex, drnPrmIndex);
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        gateGateNode = wrk.aquireNode(gateIndex, gateIndex);
        srcSrcNode = wrk.aquireNode(srcIndex, srcIndex);
        drnPrmDrnPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmIndex);
        srcPriSrcPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double gdpr = drnConduct;
        double gspr = srcConduct;
        gm = gmStates.get(0);
        gds = gdsStates.get(0);
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
        srcPriSrcPrmNode.realPlusEq(m * (gspr + gds + gm + ggs));
        srcPriSrcPrmNode.imagPlusEq(m * (xgs));
        drnDrnPrmNode.realMinusEq(m * (gdpr));
        gateDrnPrmNode.realMinusEq(m * (ggd));
        gateDrnPrmNode.imagMinusEq(m * (xgd));
        gateSrcPrmNode.realMinusEq(m * (ggs));
        gateSrcPrmNode.imagMinusEq(m * (xgs));
        srcSrcPrmNode.realMinusEq(m * (gspr));
        drnPrmDrnNode.realMinusEq(m * (gdpr));
        drnPrmGateNode.realPlusEq(m * (-ggd + gm));
        drnPrmGateNode.imagMinusEq(m * (xgd));
        drnPriSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGateNode.realPlusEq(m * (-ggs - gm));
        srcPrmGateNode.imagMinusEq(m * (xgs));
        srcPriHFET2ourceNode.realMinusEq(m * (gspr));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
        return true;
    }

    public boolean getic() {
        if (icVDS == 0) {
            icVDS = wrk.getRhsAt(drnIndex).getReal()
                    - wrk.getRhsAt(srcIndex).getReal();
        }
        if (icVGS == 0) {
            icVGS = wrk.getRhsAt(gateIndex).getReal()
                    - wrk.getRhsAt(srcIndex).getReal();
        }
        return true;
    }

    public boolean load(Mode mode) {
        double cd = 0;
        double cdhat = 0.0;
        double cg = 0;
        double cgd = 0;
        double cghat = 0.0;
        double ggd = 0;
        double ggs = 0;
        double vds = 0;
        double vgd = 0;
        double vgs = 0;
        boolean bypass = false;
        boolean converged = true;
        double vt = KoverQ * temp;
        if (mode.contains(MODE.INIT_SMSIG)) {
            vgs = vgsStates.get(0);
            vgd = vgdStates.get(0);
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vgs = vgsStates.get(1);
            vgd = vgdStates.get(1);
        } else if (mode.contains(MODE.INIT_JCT) && mode.contains(MODE.TRANOP)
                && mode.isUseIC()) {
            vds = type * icVDS;
            vgs = type * icVGS;
            vgd = vgs - vds;
        } else if (mode.contains(MODE.INIT_JCT) && off == 0) {
            vgs = -1;
            vgd = -1;
        } else if (mode.contains(MODE.INIT_JCT)
                || (mode.contains(MODE.INIT_FIX) && off != 0)) {
            vgs = 0;
            vgd = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(2);
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
                vgs = type
                        * (wrk.getRhsOldAt(gateIndex).getReal() - wrk.getRhsOldAt(srcPrmIndex).getReal());
                vgd = type
                        * (wrk.getRhsOldAt(gateIndex).getReal() - wrk.getRhsOldAt(drnPrmIndex).getReal());
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
                    MathLib.abs(vgsStates.get(0))) + env.getVoltTol())) {
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
            if (!bypass) {
                //  Limit nonlinear branch voltages
                ichk1.setTrue();
                vgs = MOSUtils.pnjLimit(vgs, vgsStates.get(0), VT0, vcrit, icheck);
                vgd = MOSUtils.pnjLimit(vgd, vgdStates.get(0), VT0, vcrit, ichk1);
                if (ichk1.isTrue()) {
                    icheck.setTrue();
                }
                vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), tVto);
                vgd = MOSUtils.fetLimit(vgd, vgdStates.get(0), tVto);
            }
        }
        if (!bypass) {
            cg = 0;
            cgd = 0;
            ggd = 0;
            ggs = 0;
            vds = vgs - vgd;
            double arg = -vgs * del / vt;
            double earg = MathLib.exp(arg);
            double vtn = n * vt;
            double expe = MathLib.exp(vgs / vtn);
            ggs = jslw * expe / vtn + ggrlw * earg * (1 - arg);
            cg = jslw * (expe - 1) + ggrlw * vgs * earg;
            arg = -vgd * del / vt;
            earg = MathLib.exp(arg);
            expe = MathLib.exp(vgd / vtn);
            ggd = jslw * expe / vtn + ggrlw * earg * (1 - arg);
            cgd = jslw * (expe - 1) + ggrlw * vgd * earg;
            cg += cgd;
            boolean inverse;
            if (vds < 0) {
                vds = -vds;
                inverse = true;
            } else {
                inverse = false;
            }
            hfeta(vds > 0 ? vgs : vgd, vds);
            if (inverse) {
                double _tmp;
                idrain = -idrain;
                vds = -vds;
                _tmp = capgs;
                capgs = capgd;
                capgd = _tmp;
            }
            cd = idrain - cgd;
            if ((mode.contains(MODE.TRAN, MODE.INIT_SMSIG) || mode.contains(MODE.TRANOP))
                    && mode.isUseIC()) {
                //    Charge storage elements
                double vgs1 = vgsStates.get(1);
                double vgd1 = vgdStates.get(1);
                if (mode.contains(MODE.INIT_TRAN)) {
                    qgsStates.set(1, capgs * vgs);
                    qgdStates.set(1, capgd * vgd);
                }
                qgsStates.set(0, qgsStates.get(1) + capgs * (vgs - vgs1));
                qgdStates.set(0, qgdStates.get(1) + capgd * (vgd - vgd1));
                //   Store small - signal parameters
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        qgsStates.set(0, capgs);
                        qgdStates.set(0, capgd);
                        return true;
                    }
                    //   Transient analysis
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
            //  Check convergence
            if (!mode.contains(MODE.INIT_FIX) || !mode.isUseIC()) {
                if ((icheck.isTrue()) || (MathLib.abs(cghat - cg) >= env.getRelTol()
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
        //    Load current vector
        double ceqgd = type * (cgd - ggd * vgd);
        double ceqgs = type * ((cg - cgd) - ggs * vgs);
        double cdreq = type * ((cd + cgd) - gds * vds - gm * vgs);
        wrk.getRhsAt(gateIndex).realPlusEq(m * (-ceqgs - ceqgd));
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (-cdreq + ceqgd));
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqgs));
        /*   Load y matrix */
        double gdpr = drnConduct;
        double gspr = srcConduct;
        drnDrnPrmNode.realPlusEq(m * (-gdpr));
        gateDrnPrmNode.realPlusEq(m * (-ggd));
        gateSrcPrmNode.realPlusEq(m * (-ggs));
        srcSrcPrmNode.realPlusEq(m * (-gspr));
        drnPrmDrnNode.realPlusEq(m * (-gdpr));
        drnPrmGateNode.realPlusEq(m * (gm - ggd));
        drnPriSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGateNode.realPlusEq(m * (-ggs - gm));
        srcPriHFET2ourceNode.realPlusEq(m * (-gspr));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds));
        drnDrnNode.realPlusEq(m * (gdpr));
        gateGateNode.realPlusEq(m * (ggd + ggs));
        srcSrcNode.realPlusEq(m * (gspr));
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + ggd));
        srcPriSrcPrmNode.realPlusEq(m * (gspr + gds + gm + ggs));
        return converged;
    }

    private void hfeta(double vgs, double vds) {
        double nsm;
        double nsc = 0.0;
        double nsn = 0.0;
        double vt = KoverQ * this.temp;
        double etavth = eta * vt;
        double vl = vs / tMu * length;
        double rt = rsi + rdi;
        double vgt0 = vgs - tVto;
        double s = MathLib.exp((vgt0 - vsigmat) / vsigma);
        double sigma = sigma0 / (1 + s);
        double vgt = vgt0 + sigma * vds;
        double u = 0.5 * vgt / vt - 1;
        double t = MathLib.sqrt(deltaSqr + u * u);
        double vgte = vt * (2 + u + t);
        double b = MathLib.exp(vgt / etavth);
        if (eta2 != 0 && d2 != 0) {
            nsc = n02 * MathLib.exp((vgt + tVto - vt2) / (eta2 * vt));
            nsn = 2 * n0 * MathLib.log(1 + 0.5 * b);
            nsm = nsn * nsc / (nsn + nsc);
        } else {
            nsm = 2 * n0 * MathLib.log(1 + 0.5 * b);
        }
        if (nsm < 1.0e-38) {
            idrain = 0;
            gm = 0.0;
            gds = 0.0;
            capgs = cf;
            capgd = cf;
            return;
        }
        double c = MathLib.pow(nsm / nmax, gamma);
        double q = MathLib.pow(1 + c, 1.0 / gamma);
        double ns = nsm / q;
        double gchi = gchi0 * ns;
        double gch = gchi / (1 + gchi * rt);
        double gchim = gchi0 * nsm;
        double h = MathLib.sqrt(1 + 2 * gchim * rsi + vgte * vgte / (vl * vl));
        double _p = 1 + gchim * rsi + h;
        double isatm = gchim * vgte / _p;
        double g = MathLib.pow(isatm / imax, gamma);
        double isat = isatm / MathLib.pow(1 + g, 1 / gamma);
        double vsate = isat / gch;
        double d = MathLib.pow(vds / vsate, m);
        double e = MathLib.pow(1 + d, 1.0 / m);
        double delidgch = vds * (1 + tLambda * vds) / e;
        idrain = gch * delidgch;
        double delidvsate = idrain * d / vsate / (1 + d);
        double delidvds = gch * (1 + 2 * tLambda * vds) / e - idrain
                * MathLib.pow(vds / vsate, m - 1) / (vsate * (1 + d));
        double a = 1 + gchi * rt;
        double delgchgchi = 1.0 / (a * a);
        double delgchins = gchi0;
        double delnsnsm = ns / nsm * (1 - c / (1 + c));
        double delvgtevgt = 0.5 * (1 + u / t);
        double delnsmvgt = n0 / etavth / (1.0 / b + 0.5);
        if (eta2 != 0 && d2 != 0) {
            delnsmvgt = nsc * (nsc * delnsmvgt + nsn * nsn / (eta2 * vt)) / ((nsc + nsn) * (nsc + nsn));
        }
        double delvsateisat = 1.0 / gch;
        double delisatisatm = isat / isatm * (1 - g / (1 + g));
        double delisatmvgte = gchim * (_p - vgte * vgte / (vl * vl * h)) / (_p * _p);
        double delvsategch = -vsate / gch;
        double delisatmgchim = vgte * (_p - gchim * rsi * (1 + 1.0 / h)) / (_p * _p);
        double delvgtvgs = 1 - vds * sigma0 / vsigma * s / ((1 + s) * (1 + s));
        _p = delgchgchi * delgchins * delnsnsm * delnsmvgt;
        double delvsatevgt = (delvsateisat * delisatisatm * (delisatmvgte * delvgtevgt
                + delisatmgchim * gchi0 * delnsmvgt) + delvsategch * _p);
        g = delidgch * _p + delidvsate * delvsatevgt;
        gm = g * delvgtvgs;
        gds = delidvds + g * sigma;
        // Capacitance calculations
        double _temp = eta1 * vt;
        double cg1 = 1 / (d1 / EPS_SI + _temp * MathLib.exp(-(vgs - vt1) / _temp));
        double cgc = width * length * (CHARGE * delnsnsm * delnsmvgt * delvgtvgs + cg1);
        double vdse = vds * MathLib.pow(1 + MathLib.pow(vds / vsate, mc), -1.0 / mc);
        a = (vsate - vdse) / (2 * vsate - vdse);
        a = a * a;
        _temp = 2.0 / 3.0;
        _p = this.p + (1 - this.p) * MathLib.exp(-vds / vsate);
        capgs = cf + 2 * _temp * cgc * (1 - a) / (1 + _p);
        a = vsate / (2 * vsate - vdse);
        a = a * a;
        capgd = cf + 2 * _p * _temp * cgc * (1 - a) / (1 + _p);
    }

    public boolean pzLoad(Complex s) {
        double gdpr = drnConduct;
        double gspr = srcConduct;
        gm = gmStates.get(0);
        gds = gdsStates.get(0);
        double ggs = ggsStates.get(0);
        double xgs = qgsStates.get(0);
        double ggd = ggdStates.get(0);
        double xgd = qgdStates.get(0);
        drnDrnNode.realPlusEq(m * (gdpr));
        gateGateNode.realPlusEq(m * (ggd + ggs));
        gateGateNode.realPlusEq(m * ((xgd + xgs) * s.getReal()));
        gateGateNode.imagPlusEq(m * ((xgd + xgs) * s.getReal()));
        srcSrcNode.realPlusEq(m * (gspr));
        drnPrmDrnPrmNode.realPlusEq(m * (gdpr + gds + ggd));
        drnPrmDrnPrmNode.realPlusEq(m * (xgd * s.getReal()));
        drnPrmDrnPrmNode.imagPlusEq(m * (xgd * s.getReal()));
        srcPriSrcPrmNode.realPlusEq(m * (gspr + gds + gm + ggs));
        srcPriSrcPrmNode.realPlusEq(m * (xgs * s.getReal()));
        srcPriSrcPrmNode.imagPlusEq(m * (xgs * s.getReal()));
        drnDrnPrmNode.realMinusEq(m * (gdpr));
        gateDrnPrmNode.realMinusEq(m * (ggd));
        gateDrnPrmNode.realMinusEq(m * (xgd * s.getReal()));
        gateDrnPrmNode.imagMinusEq(m * (xgd * s.getReal()));
        gateSrcPrmNode.realMinusEq(m * (ggs));
        gateSrcPrmNode.realMinusEq(m * (xgs * s.getReal()));
        gateSrcPrmNode.imagMinusEq(m * (xgs * s.getReal()));
        srcSrcPrmNode.realMinusEq(m * (gspr));
        drnPrmDrnNode.realMinusEq(m * (gdpr));
        drnPrmGateNode.realPlusEq(m * (-ggd + gm));
        drnPrmGateNode.realMinusEq(m * (xgd * s.getReal()));
        drnPrmGateNode.imagMinusEq(m * (xgd * s.getReal()));
        drnPriSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGateNode.realPlusEq(m * (-ggs - gm));
        srcPrmGateNode.realMinusEq(m * (xgs * s.getReal()));
        srcPrmGateNode.imagMinusEq(m * (xgs * s.getReal()));
        srcPriHFET2ourceNode.realMinusEq(m * (gspr));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
        return true;
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
        if (temp == 0) {
            temp = env.getTemp() + dtemp;
        }
        double vt = KoverQ * temp;
        double tdiff = temp - env.getNomTemp();
        tLambda = lambda + klambda * tdiff;
        tMu = mu - kmu * tdiff;
        tNmax = nmax - knmax * tdiff;
        tVto = type * vto - kvto * tdiff;
        jslw = js * length * width / 2;
        ggrlw = ggr * length * width / 2;
        n0 = EPS_SI * eta * vt / 2 / CHARGE / (di + deltad);
        n01 = EPS_SI * eta1 * vt / 2 / CHARGE / d1;
        if (eta2 != 0) {
            n02 = EPS_SI * eta2 * vt / 2 / CHARGE / d2;
        } else {
            n02 = 0.0;
        }
        gchi0 = CHARGE * width * tMu / length;
        imax = CHARGE * tNmax * vs * width;
        vcrit = vt * MathLib.log(vt / (ROOT2 * 1e-11));
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

    public boolean loadInitCond() {
        return true;
    }
}
