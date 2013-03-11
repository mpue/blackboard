/*
 * hfet1.java
 *
 * Created on August 28, 2006, 1:37; PM
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
public class HFET1Instance extends HFET1ModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    private int drnPrmIndex;
    private int gatePrmIndex;
    private int srcPrmIndex;
    private int drnPrmPrmIndex;
    private int srcPrmPrmIndex;
    private double length = 1e-6;
    private double width = 20e-6;
    private double dtemp;
    private double tVto;
    private double tMu;
    private double tLambda;
    private double tLambdahf;
    private boolean off;
    private int mode;
    private double n0;
    private double n01;
    private double n02;
    private double gchi0;
    private double cf;
    private double is1d;
    private double is2d;
    private double is1s;
    private double is2s;
    private double hFETAiso;
    private double imax;
    private double vcrit;
    private double ggrwl;
    private double drnConduct;
    private double srcConduct;
    private double gateConduct;
    private double gi;
    private double gf;
    private double deltaSqr;
    private Complex drnDrnPrmNode;
    private Complex gatePrmSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPrmSrcPrmNode;
    private Complex srcPrmSrcNode;
    private Complex drnDrnNode;
    private Complex srcSrcNode;
    private Complex srcPrmSrcPrmNode;
    private Complex drnPrmPrmDrnPrmNode;
    private Complex drnPrmPrmGatePrmNode;
    private Complex srcPrmPrmSrcPrmPrmNode;
    private Complex srcPrmSrcPrmPrmNode;
    private Complex gatePrmSrcPrmPrmNode;
    private Complex gateGatePrmNode;
    private Complex gatePrmDrnPrmNode;
    private Complex srcSrcPrmNode;
    private Complex drnPrmGatePrmNode;
    private Complex srcPrmGatePrmNode;
    private Complex srcPrmDrnPrmNode;
    private Complex gatePrmGatePrmNode;
    private Complex drnPrmDrnPrmNode;
    private Complex drnPrmDrnPrmPrmNode;
    private Complex gatePrmDrnPrmPrmNode;
    private Complex drnPrmPrmDrnPrmPrmNode;
    private Complex srcPrmPrmSrcPrmNode;
    private Complex srcPrmPrmGatePrmNode;
    private Complex gateGateNode;
    private Complex gatePrmGateNode;
    private StateVector vgsStates;
    private StateVector vgdStates;
    private StateVector cgStates;
    private StateVector cdStates;
    private StateVector cgdStates;
    private StateVector cgsStates;
    private StateVector gmStates;
    private StateVector gdsStates;
    private StateVector ggsStates;
    private StateVector ggdStates;
    private StateVector qgsStates;
    private StateVector cqgsStates;
    private StateVector qgdStates;
    private StateVector cqgdStates;
    private StateVector vgsppStates;
    private StateVector ggsppStates;
    private StateVector cgsppStates;
    private StateVector vgdppStates;
    private StateVector ggdppStates;
    private StateVector cgdppStates;
    private StateVector qdsStates;
    private StateVector cqdsStates;
    private StateVector gmgStates;
    private StateVector gmdStates;
    private double iso;
    private double iLeak;
    private double gLeak;
    private double idrain;
    private double gm;
    private double gds;
    private double capgs;
    private double capgd;
    private double cgd;
    private double gmg;
    private double gmd;
    private double cgs;
    private double ggs;

    public HFET1Instance() {
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
        cgsStates = stateTable.createRow();
        gmStates = stateTable.createRow();
        gdsStates = stateTable.createRow();
        ggsStates = stateTable.createRow();
        ggdStates = stateTable.createRow();
        qgsStates = stateTable.createRow();
        cqgsStates = stateTable.createRow();
        qgdStates = stateTable.createRow();
        cqgdStates = stateTable.createRow();
        vgsppStates = stateTable.createRow();
        ggsppStates = stateTable.createRow();
        cgsppStates = stateTable.createRow();
        vgdppStates = stateTable.createRow();
        ggdppStates = stateTable.createRow();
        cgdppStates = stateTable.createRow();
        qdsStates = stateTable.createRow();
        cqdsStates = stateTable.createRow();
        gmgStates = stateTable.createRow();
        gmdStates = stateTable.createRow();
        if (vto == 0) {
            if (type == N_TYPE) {
                vto = 0.15;
            } else {
                vto = -0.15;
            }
        }
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
        if (tf == 0) {
            tf = env.getTemp();
        }
        if (m == 0) {
            m = 1.0;
        }
        if (rs != 0 && srcPrmIndex == 0) {
            srcPrmIndex = ckt.makeVoltNode(getInstName() + "source").getIndex();
        } else {
            srcPrmIndex = srcIndex;
        }
        if (rd != 0 && drnPrmIndex == 0) {
            drnPrmIndex = ckt.makeVoltNode(getInstName() + "drain").getIndex();
        } else {
            drnPrmIndex = drnIndex;
        }
        if (rg != 0 && gatePrmIndex == 0) {
            gatePrmIndex = ckt.makeVoltNode(getInstName() + "gate").getIndex();
        } else {
            gatePrmIndex = gateIndex;
        }
        if (rf != 0 && drnPrmPrmIndex == 0) {
            drnPrmPrmIndex = ckt.makeVoltNode(getInstName() + "gd").getIndex();
        } else {
            drnPrmPrmIndex = drnPrmIndex;
        }
        if (ri != 0 && srcPrmPrmIndex == 0) {
            srcPrmPrmIndex = ckt.makeVoltNode(getInstName() + "gs").getIndex();
        } else {
            srcPrmPrmIndex = srcPrmIndex;
        }
        if (rd != 0) {
            drnConduct = 1 / rd;
        } else {
            drnConduct = 0;
        }
        if (rs != 0) {
            srcConduct = 1 / this.rs;
        } else {
            srcConduct = 0;
        }
        if (rg != 0) {
            gateConduct = 1 / rg;
        } else {
            gateConduct = 0;
        }
        if (ri != 0) {
            gi = 1 / ri;
        } else {
            gi = 0;
        }
        if (rf != 0) {
            gf = 1 / rf;
        } else {
            gf = 0;
        }
        deltaSqr = delta * delta;
        vto *= type;
        if (vt2 == 0) {
            vt2 = vto;
        }
        if (vt1 == 0) {
            vt1 = vto + CHARGE * nmax * di / EPS_SI;
        }
        drnDrnPrmNode = wrk.aquireNode(drnIndex, drnPrmIndex);
        gatePrmDrnPrmNode = wrk.aquireNode(gatePrmIndex, drnPrmIndex);
        gatePrmSrcPrmNode = wrk.aquireNode(gatePrmIndex, srcPrmIndex);
        srcSrcPrmNode = wrk.aquireNode(srcIndex, srcPrmIndex);
        drnPrmDrnNode = wrk.aquireNode(drnPrmIndex, drnIndex);
        drnPrmGatePrmNode = wrk.aquireNode(drnPrmIndex, gatePrmIndex);
        drnPrmSrcPrmNode = wrk.aquireNode(drnPrmIndex, srcPrmIndex);
        srcPrmGatePrmNode = wrk.aquireNode(srcPrmIndex, gatePrmIndex);
        srcPrmSrcNode = wrk.aquireNode(srcPrmIndex, srcIndex);
        srcPrmDrnPrmNode = wrk.aquireNode(srcPrmIndex, drnPrmIndex);
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        gatePrmGatePrmNode = wrk.aquireNode(gatePrmIndex, gatePrmIndex);
        srcSrcNode = wrk.aquireNode(srcIndex, srcIndex);
        drnPrmDrnPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmIndex);
        srcPrmSrcPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmIndex);
        drnPrmDrnPrmPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmPrmIndex);
        drnPrmPrmDrnPrmNode = wrk.aquireNode(drnPrmPrmIndex, drnPrmIndex);
        drnPrmPrmGatePrmNode = wrk.aquireNode(drnPrmPrmIndex, gatePrmIndex);
        gatePrmDrnPrmPrmNode = wrk.aquireNode(gatePrmIndex, drnPrmPrmIndex);
        drnPrmPrmDrnPrmPrmNode = wrk.aquireNode(drnPrmPrmIndex, drnPrmPrmIndex);
        srcPrmSrcPrmPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmPrmIndex);
        srcPrmPrmSrcPrmNode = wrk.aquireNode(srcPrmPrmIndex, srcPrmIndex);
        srcPrmPrmGatePrmNode = wrk.aquireNode(srcPrmPrmIndex, gatePrmIndex);
        gatePrmSrcPrmPrmNode = wrk.aquireNode(gatePrmIndex, srcPrmPrmIndex);
        srcPrmPrmSrcPrmPrmNode = wrk.aquireNode(srcPrmPrmIndex, srcPrmPrmIndex);
        gateGateNode = wrk.aquireNode(gateIndex, gateIndex);
        gateGatePrmNode = wrk.aquireNode(gateIndex, gatePrmIndex);
        gatePrmGateNode = wrk.aquireNode(gatePrmIndex, gateIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        gm = gmStates.get(0);
        gds = gdsStates.get(0);
        double xds = cds * tmprl.getOmega();
        ggs = ggsStates.get(0);
        double xgs = qgsStates.get(0) * tmprl.getOmega();
        double ggd = ggdStates.get(0);
        double xgd = qgdStates.get(0) * tmprl.getOmega();
        double ggspp = ggsppStates.get(0);
        double ggdpp = ggdppStates.get(0);
        double f = 0;
        if (kappa != 0 && delf != 0.0) {
            f = tmprl.getOmega() / 2 / MathLib.PI;
        }
        gds = gds * (1 + 0.5 * kappa * (1 + MathLib.tanh((f - fgds) / delf)));
        drnDrnNode.realPlusEq(m * (drnConduct));
        srcSrcNode.realPlusEq(m * (srcConduct));
        gatePrmGatePrmNode.realPlusEq(m * (ggd + ggs + ggspp + ggdpp + gateConduct));
        drnPrmDrnPrmNode.realPlusEq(m * (gds + ggd + drnConduct + gf));
        srcPrmSrcPrmNode.realPlusEq(m * (gds + gm + ggs + srcConduct + gi));
        srcPrmPrmSrcPrmPrmNode.realPlusEq(m * (gi + ggspp));
        drnPrmPrmDrnPrmPrmNode.realPlusEq(m * (gf + ggdpp));
        drnDrnPrmNode.realMinusEq(m * (drnConduct));
        drnPrmDrnNode.realMinusEq(m * (drnConduct));
        srcSrcPrmNode.realMinusEq(m * (srcConduct));
        srcPrmSrcNode.realMinusEq(m * (srcConduct));
        gatePrmDrnPrmNode.realMinusEq(m * (ggd));
        drnPrmGatePrmNode.realPlusEq(m * (gm - ggd));
        gatePrmSrcPrmNode.realMinusEq(m * (ggs));
        srcPrmGatePrmNode.realPlusEq(m * (-ggs - gm));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
        srcPrmSrcPrmPrmNode.realMinusEq(m * (gi));
        srcPrmPrmSrcPrmNode.realMinusEq(m * (gi));
        gatePrmSrcPrmPrmNode.realMinusEq(m * (ggspp));
        srcPrmPrmGatePrmNode.realMinusEq(m * (ggspp));
        drnPrmDrnPrmPrmNode.realMinusEq(m * (gf));
        drnPrmPrmDrnPrmNode.realMinusEq(m * (gf));
        gatePrmDrnPrmPrmNode.realMinusEq(m * (ggdpp));
        drnPrmPrmGatePrmNode.realMinusEq(m * (ggdpp));
        gateGateNode.realPlusEq(m * (gateConduct));
        gateGatePrmNode.realMinusEq(m * (gateConduct));
        gatePrmGateNode.realMinusEq(m * (gateConduct));
        gatePrmGatePrmNode.imagPlusEq(m * (xgd + xgs));
        drnPrmPrmDrnPrmPrmNode.imagPlusEq(m * (xgd));
        srcPrmPrmSrcPrmPrmNode.imagPlusEq(m * (xgs));
        gatePrmDrnPrmPrmNode.imagMinusEq(m * (xgd));
        gatePrmSrcPrmPrmNode.imagMinusEq(m * (xgs));
        drnPrmPrmGatePrmNode.imagMinusEq(m * (xgd));
        srcPrmPrmGatePrmNode.imagMinusEq(m * (xgs));
        drnPrmDrnPrmNode.imagPlusEq(m * (xds));
        srcPrmSrcPrmNode.imagPlusEq(m * (xds));
        drnPrmSrcPrmNode.imagMinusEq(m * (xds));
        srcPrmDrnPrmNode.imagMinusEq(m * (xds));
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
    private Real ceq = Real.zero();
    private Real geq = Real.zero();
    private Bool icheck = Bool.FALSE();

    public boolean load(Mode mode) {
        double cd = 0;
        double cdhat = 0.0;
        double cg;
        double cghat = 0.0;
        double ggd = 0;
        double vds = 0;
        double vgd = 0;
        double vgs = 0;
        double vgspp = 0;
        double vgdpp = 0;
        double cgspp = 0;
        double cgdpp = 0;
        double ggspp = 0;
        double ggdpp = 0;
        boolean inverse = false;
        boolean converged = true;
        boolean bypass = false;
        double vt = KoverQ * this.temp;
        if (mode.contains(MODE.INIT_SMSIG)) {
            vgs = vgsStates.get(0);
            vgd = vgdStates.get(0);
            vgspp = vgsppStates.get(0);
            vgdpp = vgdppStates.get(0);
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vgs = vgsStates.get(1);
            vgd = vgdStates.get(1);
            vgspp = vgsppStates.get(1);
            vgdpp = vgdppStates.get(1);
        } else if (mode.contains(MODE.INIT_JCT)
                && mode.contains(MODE.TRANOP)
                && mode.isUseIC()) {
            vds = type * icVDS;
            vgs = type * icVGS;
            vgd = vgs - vds;
            vgspp = vgs;
            vgdpp = vgd;
        } else if (mode.contains(MODE.INIT_JCT) && !off) {
            vgs = -1;
            vgd = -1;
            vgspp = 0;
            vgdpp = 0;
        } else if ((mode.contains(MODE.INIT_JCT)
                || mode.contains(MODE.INIT_FIX)) && off) {
            vgs = 0;
            vgd = 0;
            vgspp = 0;
            vgdpp = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                double xfact = tmprl.getDelta() / tmprl.getOldDeltaAt(2);
                vgsStates.set(0, vgsStates.get(1));
                vgs = (1 + xfact) * vgsStates.get(1)
                        - xfact * vgsStates.get(2);
                vgsppStates.set(0, vgsppStates.get(1));
                vgspp = (1 + xfact) * vgsppStates.get(1)
                        - xfact * vgsppStates.get(2);
                vgdStates.set(0, vgdStates.get(1));
                vgd = (1 + xfact) * vgdStates.get(1)
                        - xfact * vgdStates.get(2);
                vgdppStates.set(0, vgdppStates.get(1));
                vgdpp = (1 + xfact) * vgdppStates.get(1)
                        - xfact * vgdppStates.get(2);
                cgStates.set(0, cgStates.get(1));
                cdStates.set(0, cdStates.get(1));
                cgdStates.set(0, cgdStates.get(1));
                cgsStates.set(0, cgsStates.get(1));
                cgsppStates.set(0, cgsppStates.get(1));
                cgdppStates.set(0, cgdppStates.get(1));
                gmStates.set(0, gmStates.get(1));
                gdsStates.set(0, gdsStates.get(1));
                ggsStates.set(0, ggsStates.get(1));
                ggsppStates.set(0, ggsppStates.get(1));
                ggdStates.set(0, ggdStates.get(1));
                ggdppStates.set(0, ggdppStates.get(1));
                gmgStates.set(0, gmgStates.get(1));
                gmdStates.set(0, gmdStates.get(1));
            } else {
                // Compute nonlinear branch voltages
                vgs = type
                        * (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmIndex).getReal());
                vgd = type
                        * (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(drnPrmIndex).getReal());
                vgspp = type
                        * (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmPrmIndex).getReal());
                vgdpp = type
                        * (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(drnPrmPrmIndex).getReal());
            }
            double delvgs = vgs - vgsStates.get(0);
            double delvgd = vgd - vgdStates.get(0);
            double delvds = delvgs - delvgd;
            double delvgspp = vgspp - vgsppStates.get(0);
            double delvgdpp = vgdpp - vgdppStates.get(0);
            cghat = cgStates.get(0)
                    + gmgStates.get(0) * delvgs
                    - gmdStates.get(0) * delvds
                    + ggdStates.get(0) * delvgd
                    + ggsStates.get(0) * delvgs
                    + ggdppStates.get(0) * delvgdpp
                    + ggsppStates.get(0) * delvgspp;
            cdhat = cdStates.get(0)
                    + gmStates.get(0) * delvgs
                    + gdsStates.get(0) * delvds
                    - ggdStates.get(0) * delvgd
                    - (gmgStates.get(0) * delvgs
                    - gmdStates.get(0) * delvds);
            if (env.isBypass() && (!(mode.contains(MODE.INIT_PRED))
                    && (MathLib.abs(delvgs) < env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                    MathLib.abs(vgsStates.get(0)))
                    + env.getVoltTol()))) {
                if ((MathLib.abs(delvgd) < env.getRelTol() * MathLib.max(MathLib.abs(vgd),
                        MathLib.abs(vgdStates.get(0)))
                        + env.getVoltTol())) {
                    if ((MathLib.abs(delvgspp) < env.getRelTol() * MathLib.max(MathLib.abs(vgspp),
                            MathLib.abs(vgsppStates.get(0)))
                            + env.getVoltTol())) {
                        if ((MathLib.abs(delvgdpp) < env.getRelTol() * MathLib.max(MathLib.abs(vgdpp),
                                MathLib.abs(vgdppStates.get(0)))
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
                                    vgspp = vgsppStates.get(0);
                                    vgdpp = vgdppStates.get(0);
                                    cg = cgStates.get(0);
                                    cd = cdStates.get(0);
                                    cgd = cgdStates.get(0);
                                    cgs = cgsStates.get(0);
                                    cgdpp = cgdppStates.get(0);
                                    cgspp = cgsppStates.get(0);
                                    gm = gmStates.get(0);
                                    gds = gdsStates.get(0);
                                    ggs = ggsStates.get(0);
                                    ggd = ggdStates.get(0);
                                    ggdpp = ggdppStates.get(0);
                                    ggspp = ggsppStates.get(0);
                                    gmg = gmgStates.get(0);
                                    gmd = gmdStates.get(0);
                                    bypass = true;
                                }
                            }
                        }
                    }
                }
            }
            if (!bypass) {
                // Limit nonlinear branch voltages
                vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), tVto);
                vgd = MOSUtils.fetLimit(vgd, vgdStates.get(0), tVto);
            }
        }
        if (!bypass) {
            // Determine dc current and derivatives
            vds = vgs - vgd;
            if (gatemod == 0) {
                double arg;
                double earg;
                if (is1s == 0 || is2s == 0) {
                    cgs = 0;
                    ggs = 0;
                } else {
                    leak(vt, vgs, rgs, is1s, is2s, m1s, m2s);
                    cgs = iLeak;
                    ggs = gLeak;
                }
                arg = -vgs * del / vt;
                earg = MathLib.exp(arg);
                cgs += ggrwl * vgs * earg;
                ggs += ggrwl * earg * (1 - arg);
                if (is1d == 0 || is2d == 0) {
                    cgd = 0;
                    ggd = 0;
                } else {
                    leak(vt, vgd, rgd, is1d, is2d, m1d, m2d);
                    cgd = iLeak;
                    ggd = gLeak;
                }
                arg = -vgd * del / vt;
                earg = MathLib.exp(arg);
                cgd += ggrwl * vgd * earg;
                ggd += ggrwl * earg * (1 - arg);
            } else {
                ggd = 0;
            }
            if (vds < 0) {
                vds = -vds;
                inverse = true;
            }
            hfeta(vds > 0 ? vgs : vgd, vds);
            cg = cgs + cgd;
            if (inverse) {
                idrain = -idrain;
                vds = -vds;
                double _tmp = capgs;
                capgs = capgd;
                capgd = _tmp;
            }
            // Compute equivalent drain current source
            cd = idrain - cgd;
            if (mode.contains(MODE.TRAN, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                // Charge storage elements
                double vgs1 = vgsppStates.get(1);
                double vgd1 = vgdppStates.get(1);
                double vds1 = vgsStates.get(1) - vgdStates.get(1);
                if (mode.contains(MODE.INIT_TRAN)) {
                    qgsStates.set(1, capgs * vgspp);
                    qgdStates.set(1, capgd * vgdpp);
                    qdsStates.set(1, cds * vds);
                }
                qgsStates.set(0, qgsStates.get(1) + capgs * (vgspp - vgs1));
                qgdStates.set(0, qgdStates.get(1) + capgd * (vgdpp - vgd1));
                qdsStates.set(0, qdsStates.get(1) + cds * (vds - vds1));
                // Store small - signal parameters
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        qgsStates.set(0, capgs);
                        qgdStates.set(0, capgd);
                        qdsStates.set(0, cds);
                        return true;
                    }
                    // Transient analysis
                    if (mode.contains(MODE.INIT_TRAN)) {
                        qgsStates.set(1, qgsStates.get(0));
                        qgdStates.set(1, qgdStates.get(0));
                        qdsStates.set(1, qdsStates.get(0));
                    }
                    stateTable.integrate(geq, ceq, capgs, qgsStates, cqgsStates);
                    ggspp = geq.get();
                    cgspp = cqgsStates.get(0);
                    cg = cg + cgspp;
                    stateTable.integrate(geq, ceq, capgd, qgdStates, cqgdStates);
                    ggdpp = geq.get();
                    cgdpp = cqgdStates.get(0);
                    cg = cg + cgdpp;
                    cd = cd - cgdpp;
                    stateTable.integrate(geq, ceq, cds, qdsStates, cqdsStates);
                    gds += geq.get();
                    cd += cqdsStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqgsStates.set(1, cqgsStates.get(0));
                        cqgdStates.set(1, cqgdStates.get(0));
                        cqdsStates.set(1, cqdsStates.get(0));
                    }
                }
            }
            // Check convergence
            if (!mode.contains(MODE.INIT_FIX) || !mode.isUseIC()) {
                if ((icheck.isFalse()) || (MathLib.abs(cghat - cg) >= env.getRelTol()
                        * MathLib.max(MathLib.abs(cghat), MathLib.abs(cg)) + env.getAbsTol())
                        || (MathLib.abs(cdhat - cd) > env.getRelTol()
                        * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol())) {
                    converged = false;
                }
            }
            vgsStates.set(0, vgs);
            vgdStates.set(0, vgd);
            vgsppStates.set(0, vgspp);
            vgdppStates.set(0, vgdpp);
            cgStates.set(0, cg);
            cdStates.set(0, cd);
            cgdStates.set(0, cgd);
            cgsStates.set(0, cgs);
            cgsppStates.set(0, cgspp);
            cgdppStates.set(0, cgdpp);
            gmStates.set(0, gm);
            gdsStates.set(0, gds);
            ggsStates.set(0, ggs);
            ggdStates.set(0, ggd);
            ggsppStates.set(0, ggspp);
            ggdppStates.set(0, ggdpp);
            gmgStates.set(0, gmg);
            gmdStates.set(0, gmd);
        }
        double ceqgd = type * (cgd + cgdpp - ggd * vgd - gmg * vgs - gmd * vds - ggdpp * vgdpp);
        double ceqgs = type * (cgs + cgspp - ggs * vgs - ggspp * vgspp);
        double cdreq = type * (cd + cgd + cgdpp - gds * vds - gm * vgs);
        wrk.getRhsAt(gatePrmIndex).realPlusEq(m * (-ceqgs - ceqgd));
        ceqgd = type * (cgd - ggd * vgd - gmg * vgs - gmd * vds);
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (-cdreq + ceqgd));
        ceqgd = type * (cgdpp - ggdpp * vgdpp);
        wrk.getRhsAt(drnPrmPrmIndex).realPlusEq(m * ceqgd);
        ceqgs = type * (cgs - ggs * vgs);
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqgs));
        ceqgs = type * (cgspp - ggspp * vgspp);
        wrk.getRhsAt(srcPrmPrmIndex).realPlusEq(m * ceqgs);
        // Load y matrix
        drnDrnNode.realPlusEq(m * (drnConduct));
        srcSrcNode.realPlusEq(m * (srcConduct));
        gatePrmGatePrmNode.realPlusEq(m * (ggd + ggs + ggspp + ggdpp + gmg + gateConduct));
        drnPrmDrnPrmNode.realPlusEq(m * (gds + ggd - gmd + drnConduct + gf));
        srcPrmSrcPrmNode.realPlusEq(m * (gds + gm + ggs + srcConduct + gi));
        srcPrmPrmSrcPrmPrmNode.realPlusEq(m * (gi + ggspp));
        drnPrmPrmDrnPrmPrmNode.realPlusEq(m * (gf + ggdpp));
        drnDrnPrmNode.realMinusEq(m * (drnConduct));
        drnPrmDrnNode.realMinusEq(m * (drnConduct));
        srcSrcPrmNode.realMinusEq(m * (srcConduct));
        srcPrmSrcNode.realMinusEq(m * (srcConduct));
        gatePrmDrnPrmNode.realPlusEq(m * (-ggd + gmd));
        drnPrmGatePrmNode.realPlusEq(m * (gm - ggd - gmg));
        gatePrmSrcPrmNode.realMinusEq(m * (ggs + gmg + gmd));
        srcPrmGatePrmNode.realPlusEq(m * (-ggs - gm));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm + gmg + gmd));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
        srcPrmSrcPrmPrmNode.realMinusEq(m * (gi));
        srcPrmPrmSrcPrmNode.realMinusEq(m * (gi));
        gatePrmSrcPrmPrmNode.realMinusEq(m * (ggspp));
        srcPrmPrmGatePrmNode.realMinusEq(m * (ggspp));
        drnPrmDrnPrmPrmNode.realMinusEq(m * (gf));
        drnPrmPrmDrnPrmNode.realMinusEq(m * (gf));
        gatePrmDrnPrmPrmNode.realMinusEq(m * (ggdpp));
        drnPrmPrmGatePrmNode.realMinusEq(m * (ggdpp));
        gateGateNode.realPlusEq(m * (gateConduct));
        gateGatePrmNode.realMinusEq(m * (gateConduct));
        gatePrmGateNode.realMinusEq(m * (gateConduct));
        return converged;
    }

    private void leak(double vt, double v, double rs, double is1, double is2,
            double m1, double m2) {
        double gmin = env.getGMin();
        double _vt1 = vt * m1;
        double _vt2 = vt * m2;
        if (v > -10 * _vt1) {
            double dvdi0;
            double iaprox;
            double iaprox1;
            double iaprox2;
            double v0;
            double vteff = _vt1 + _vt2;
            double iseff = is2 * MathLib.pow((is1 / is2), (m1 / (m1 + m2)));
            if (rs > 0) {
                double unorm = (v + rs * is1) / _vt1 + MathLib.log(rs * is1 / _vt1);
                iaprox1 = _vt1 * diode(unorm) / rs - is1;
                unorm = (v + rs * iseff) / vteff + MathLib.log(rs * iseff / vteff);
                iaprox2 = vteff * diode(unorm) / rs - iseff;
            } else {
                iaprox1 = is1 * (MathLib.exp(v / _vt1) - 1);
                iaprox2 = iseff * (MathLib.exp(v / vteff) - 1);
            }
            if ((iaprox1 * iaprox2) != 0.0) {
                iaprox = 1 / (1 / iaprox1 + 1. / iaprox2);
            } else {
                iaprox = 0.5 * (iaprox1 + iaprox2);
            }
            dvdi0 = rs + _vt1 / (iaprox + is1) + _vt2 / (iaprox + is2);
            v0 = rs * iaprox;
            v0 += _vt1 * MathLib.log(iaprox / is1 + 1) + _vt2 * MathLib.log(iaprox / is2 + 1);
            /* il = _max(-is1, iaprox + (v - v0) / dvdi0) * 0.99999; */
            iLeak = MathLib.max(-is1, iaprox + (v - v0) / dvdi0) * 0.99999;
            gLeak = 1 / (rs + _vt1 / (iLeak + is1) + _vt2 / (iLeak + is2));
        } else {
            gLeak = gmin;
            iLeak = gLeak * v - is1;
        }
    }

    private void hfeta(double vgs, double vds) {
        double nsm;
        double a;
        double c;
        double d;
        double e;
        double f;
        double g;
        double h;
        double _p;
        double q;
        double nsc = 0.0;
        double nsn = 0.0;
        double _tmp;
        double vsate = 0.0;
        double vdse;
        double delvgtevgt = 0.0;
        double delvgtvgs = 0.0;
        double delvsatevgt = 0.0;
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
            cgd = 1;
        } else {
            c = MathLib.pow(nsm / nmax, gamma);
            q = MathLib.pow(1 + c, 1.0 / gamma);
            double ns = nsm / q;
            double gchi = gchi0 * ns;
            double gch = gchi / (1 + gchi * rt);
            double gchim = gchi0 * nsm;
            h = MathLib.sqrt(1 + 2 * gchim * rsi + vgte * vgte / (vl * vl));
            _p = 1 + gchim * rsi + h;
            double isatm = gchim * vgte / _p;
            g = MathLib.pow(isatm / imax, gamma);
            double isat = isatm / MathLib.pow(1 + g, 1 / gamma);
            vsate = isat / gch;
            d = MathLib.pow(vds / vsate, m);
            e = MathLib.pow(1 + d, 1.0 / m);
            double delidgch = vds * (1 + tLambda * vds) / e;
            idrain = gch * delidgch;
            double delidvsate = idrain * d / vsate / (1 + d);
            double delidvds = gch * (1 + 2 * tLambda * vds) / e - idrain
                    * MathLib.pow(vds / vsate, m - 1) / (vsate * (1 + d));
            a = 1 + gchi * rt;
            double delgchgchi = 1.0 / (a * a);
            double delgchins = gchi0;
            double delnsnsm = ns / nsm * (1 - c / (1 + c));
            delvgtevgt = 0.5 * (1 + u / t);
            double delnsmvgt = n0 / etavth / (1.0 / b + 0.5);
            if (eta2 != 0 && d2 != 0) {
                delnsmvgt = nsc * (nsc * delnsmvgt + nsn * nsn / (eta2 * vt)) / ((nsc + nsn) * (nsc + nsn));
            }
            double delvsateisat = 1.0 / gch;
            double delisatisatm = isat / isatm * (1 - g / (1 + g));
            double delisatmvgte = gchim * (_p - vgte * vgte / (vl * vl * h)) / (_p * _p);
            double delvsategch = -vsate / gch;
            double delisatmgchim = vgte * (_p - gchim * rsi * (1 + 1.0 / h)) / (_p * _p);
            delvgtvgs = 1 - vds * sigma0 / vsigma * s / ((1 + s) * (1 + s));
            _p = delgchgchi * delgchins * delnsnsm * delnsmvgt;
            delvsatevgt = (delvsateisat * delisatisatm * (delisatmvgte * delvgtevgt
                    + delisatmgchim * gchi0 * delnsmvgt) + delvsategch * _p);
            g = delidgch * _p + delidvsate * delvsatevgt;
            gm = g * delvgtvgs;
            gds = delidvds + g * sigma;
            // Capacitance calculations
            _tmp = eta1 * vt;
            double cg1 = 1 / (d1 / EPS_SI + _tmp * MathLib.exp(-(vgs - vt1) / _tmp));
            double cgc = width * length * (CHARGE * delnsnsm * delnsmvgt * delvgtvgs + cg1);
            vdse = vds * MathLib.pow(1 + MathLib.pow(vds / vsate, mc), -1.0 / mc);
            a = (vsate - vdse) / (2 * vsate - vdse);
            a = a * a;
            _tmp = 2.0 / 3.0;
            _p = this.p + (1 - this.p) * MathLib.exp(-vds / vsate);
            capgs = cf + 2 * _tmp * cgc * (1 - a) / (1 + _p);
            a = vsate / (2 * vsate - vdse);
            a = a * a;
            capgd = cf + 2 * _p * _tmp * cgc * (1 - a) / (1 + _p);
        }
        if (gatemod != 0) {
            // Gate - drain current calculation
            double vkneet = ck1 * vsate + ck2;
            double vmax = cm1 * vsate + cm2;
            a = MathLib.pow(vds / vmax, mt2);
            b = MathLib.pow(1 + a, 1 / mt2);
            vdse = vds / b;
            c = MathLib.pow(vdse / vkneet, mt1);
            d = MathLib.pow(1 + c, 1 / mt1);
            double td = this.temp + talpha * vdse * vdse / d;
            e = KoverQ * td * m2d;
            _p = phib / (BOLTZMANN * td);
            f = MathLib.exp(-_p);
            q = (vgs - vdse) / e;
            g = MathLib.exp(q);
            h = iso * td * td * f * g;
            cgd = h - iso * this.temp * this.temp * MathLib.exp(-phib / (BOLTZMANN * this.temp));
            double delcgdvgs = h / e;
            double delcgdtd = h * (_p - q + 2) / td;
            double deltdvdse = talpha * vdse * (2 - c / (1 + c)) / d;
            double deltdvkneet = (td - this.temp) * c / ((1 + c) * vkneet);
            double delvdsevmax = vdse * a / ((1 + a) * vmax);
            double delvdsevds = (1 - a / (1 + a)) / b;
            _tmp = delvsatevgt * delvgtvgs;
            double dvdsevgs = delvdsevmax * cm1 * _tmp;
            double dtdvgs = deltdvdse * dvdsevgs + deltdvkneet * ck1 * _tmp;
            gmg = delcgdvgs + delcgdtd * dtdvgs;
            _tmp = delvsatevgt * sigma;
            double dvdsevds = delvdsevds + delvdsevmax * cm1 * _tmp;
            double dtdvds = deltdvdse * dvdsevds + deltdvkneet * ck1 * _tmp;
            gmd = -delcgdvgs * dvdsevds + delcgdtd * dtdvds;
        } else {
            gmg = 0;
            gmd = 0;
        }
        if (gatemod != 0) {
            // Gate - source current calculation
            double evgs;
            double vtn = vt * m2s;
            double csat = iso * this.temp * this.temp * MathLib.exp(-phib / (BOLTZMANN * this.temp));
            if (vgs <= -5 * vt) {
                ggs = -csat / vgs + env.getGMin();
                cgs = ggs * vgs;
            } else {
                evgs = MathLib.exp(vgs / vtn);
                ggs = csat * evgs / vtn + env.getGMin();
                cgs = csat * (evgs - 1) + env.getGMin() * vgs;
            }
        }
        if (gatemod != 0 && (a1 != 0.0 || a2 != 0.0)) {
            // Correction current calculations
            double vmax = cm3 * vsate;
            a = MathLib.pow(vds / vmax, mv1);
            b = MathLib.pow(1 + a, 1 / mv1);
            vdse = vds / b;
            double delvdsevmax = vdse * a / ((1 + a) * vmax);
            double delvdsevds = (1 - a / (1 + a)) / b;
            double dvdsevgs = delvdsevmax * cm3 * delvsatevgt * delvgtvgs;
            double dvdsevds = delvdsevds + delvdsevmax * cm3 * delvsatevgt * sigma;
            c = vgte * vdse;
            d = 1 + a2 * c;
            e = vdse * delvgtevgt;
            f = a2 * cgd;
            idrain += a1 * (d * cgd - cgs);
            gds += a1 * (d * gmd + f * (vgte * dvdsevds + e * sigma));
            gm += a1 * (d * gmg + f * (vgte * dvdsevgs + e * delvgtvgs) - ggs);
        }
    }

    private double diode(double u) {
        double U0 = -2.303;
        double A = 2.221;
        double B = 6.804;
        double C = 1.685;
        double it;
        double beta = 0;
        double expu = MathLib.exp(u);
        if (u <= U0) {
            it = expu * (1 - expu);
        } else {
            beta = 0.5 * (u - U0);
            it = u + A * MathLib.exp((U0 - u) / B) - MathLib.log(beta + MathLib.sqrt(beta * beta + 0.25 * C * C));
        }
        double ut = it + MathLib.log(it);
        beta = u - ut;
        double c = 1 + it;
        double i = it * (1 + beta / c + 0.5 * beta * beta / c / c / c);
        return i;
    }

    public boolean pzLoad(Complex s) {
        gm = gmStates.get(0);
        gds = gdsStates.get(0);
        double xds = cds * tmprl.getOmega();
        ggs = ggsStates.get(0);
        double xgs = qgsStates.get(0);
        double ggd = ggdStates.get(0);
        double xgd = qgdStates.get(0);
        double ggspp = ggsppStates.get(0);
        double ggdpp = ggdppStates.get(0);
        double f = 0;
        if (kappa != 0 && delf != 0.0) {
            f = tmprl.getOmega() / 2 / MathLib.PI;
        }
        gds = gds * (1 + 0.5 * kappa * (1 + MathLib.tanh((f - fgds) / delf)));
        drnDrnNode.realPlusEq(m * (drnConduct));
        srcSrcNode.realPlusEq(m * (srcConduct));
        gatePrmGatePrmNode.realPlusEq(m * (ggd + ggs + ggspp + ggdpp + gateConduct));
        drnPrmDrnPrmNode.realPlusEq(m * (gds + ggd + drnConduct + gf));
        srcPrmSrcPrmNode.realPlusEq(m * (gds + gm + ggs + srcConduct + gi));
        srcPrmPrmSrcPrmPrmNode.realPlusEq(m * (gi + ggspp));
        drnPrmPrmDrnPrmPrmNode.realPlusEq(m * (gf + ggdpp));
        drnDrnPrmNode.realMinusEq(m * (drnConduct));
        drnPrmDrnNode.realMinusEq(m * (drnConduct));
        srcSrcPrmNode.realMinusEq(m * (srcConduct));
        srcPrmSrcNode.realMinusEq(m * (srcConduct));
        gatePrmDrnPrmNode.realMinusEq(m * (ggd));
        drnPrmGatePrmNode.realPlusEq(m * (gm - ggd));
        gatePrmSrcPrmNode.realMinusEq(m * (ggs));
        srcPrmGatePrmNode.realPlusEq(m * (-ggs - gm));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
        srcPrmSrcPrmPrmNode.realMinusEq(m * (gi));
        srcPrmPrmSrcPrmNode.realMinusEq(m * (gi));
        gatePrmSrcPrmPrmNode.realMinusEq(m * (ggspp));
        srcPrmPrmGatePrmNode.realMinusEq(m * (ggspp));
        drnPrmDrnPrmPrmNode.realMinusEq(m * (gf));
        drnPrmPrmDrnPrmNode.realMinusEq(m * (gf));
        gatePrmDrnPrmPrmNode.realMinusEq(m * (ggdpp));
        drnPrmPrmGatePrmNode.realMinusEq(m * (ggdpp));
        gateGateNode.realPlusEq(m * (gateConduct));
        gateGatePrmNode.realMinusEq(m * (gateConduct));
        gatePrmGateNode.realMinusEq(m * (gateConduct));
        gatePrmGatePrmNode.realPlusEq(m * ((xgd + xgs) * s.getReal()));
        gatePrmGatePrmNode.imagPlusEq(m * ((xgd + xgs) * s.getReal()));
        drnPrmPrmDrnPrmPrmNode.realPlusEq(m * (xgd * s.getReal()));
        drnPrmPrmDrnPrmPrmNode.imagPlusEq(m * (xgd * s.getReal()));
        srcPrmPrmSrcPrmPrmNode.realPlusEq(m * (xgs * s.getReal()));
        srcPrmPrmSrcPrmPrmNode.imagPlusEq(m * (xgs * s.getReal()));
        gatePrmDrnPrmPrmNode.realMinusEq(m * (xgd * s.getReal()));
        gatePrmDrnPrmPrmNode.imagMinusEq(m * (xgd * s.getReal()));
        gatePrmSrcPrmPrmNode.realMinusEq(m * (xgs * s.getReal()));
        gatePrmSrcPrmPrmNode.imagMinusEq(m * (xgs * s.getReal()));
        drnPrmPrmGatePrmNode.realMinusEq(m * (xgd * s.getReal()));
        drnPrmPrmGatePrmNode.imagMinusEq(m * (xgd * s.getReal()));
        srcPrmPrmGatePrmNode.realMinusEq(m * (xgs * s.getReal()));
        srcPrmPrmGatePrmNode.imagMinusEq(m * (xgs * s.getReal()));
        drnPrmDrnPrmNode.realPlusEq(m * (xds * s.getReal()));
        drnPrmDrnPrmNode.imagPlusEq(m * (xds * s.getReal()));
        srcPrmSrcPrmNode.realPlusEq(m * (xds * s.getReal()));
        srcPrmSrcPrmNode.imagPlusEq(m * (xds * s.getReal()));
        drnPrmSrcPrmNode.realMinusEq(m * (xds * s.getReal()));
        drnPrmSrcPrmNode.imagMinusEq(m * (xds * s.getReal()));
        srcPrmDrnPrmNode.realMinusEq(m * (xds * s.getReal()));
        srcPrmDrnPrmNode.imagMinusEq(m * (xds * s.getReal()));
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
        if (gatePrmIndex != 0 && gatePrmIndex != gateIndex) {
            ckt.deleteNode(gatePrmIndex);
        }
        gatePrmIndex = 0;
        if (drnPrmPrmIndex != 0 && drnPrmPrmIndex != drnPrmIndex) {
            ckt.deleteNode(drnPrmPrmIndex);
        }
        drnPrmPrmIndex = 0;
        if (srcPrmPrmIndex != 0 && srcPrmPrmIndex != srcPrmIndex) {
            ckt.deleteNode(srcPrmPrmIndex);
        }
        srcPrmPrmIndex = 0;
        return true;
    }

    public boolean temperature() {
        if (this.temp == 0) {
            this.temp = env.getTemp() + dtemp;
        }
        double vt = KoverQ * this.temp;
        tLambda = lambda + klambda * (this.temp - env.getNomTemp());
        tMu = mu - kmu * (this.temp - env.getNomTemp());
        tVto = vto - kvto * (this.temp - env.getNomTemp());
        n0 = EPS_SI * eta * vt / 2 / CHARGE / (di + deltad);
        n01 = EPS_SI * eta1 * vt / 2 / CHARGE / d1;
        if (eta2 != 0) {
            n02 = EPS_SI * eta2 * vt / 2 / CHARGE / d2;
        } else {
            n02 = 0.0;
        }
        gchi0 = CHARGE * width * tMu / length;
        cf = 0.5 * EPS_SI * width;
        imax = CHARGE * nmax * vs * width;
        is1d = js1d * width * length / 2;
        is2d = js2d * width * length / 2;
        is1s = js1s * width * length / 2;
        is2s = js2s * width * length / 2;
        iso = astar * width * length / 2;
        ggrwl = ggr * length * width / 2;
        double _temp = MathLib.exp(this.temp / tf);
        fgds = fgds * _temp;
        delf = delf * _temp;
        if (gatemod == 0) {
            if (is1s != 0) {
                vcrit = vt * MathLib.log(vt / (ROOT2 * is1s));
            } else {
                vcrit = Double.MAX_VALUE;
            }
        } else {
            if (iso != 0.0) {
                vcrit = vt * MathLib.log(vt / (ROOT2 * iso));
            } else {
                vcrit = Double.MAX_VALUE;
            }
        }
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
