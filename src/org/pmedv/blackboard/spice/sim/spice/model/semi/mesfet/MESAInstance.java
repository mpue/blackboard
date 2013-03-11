/*
 * mesa.java
 *
 * Created on August 28, 2006, 2:31 PM
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
public class MESAInstance extends MESAModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    /* Internal drain node index */
    private int drnPrmIndex;

    /* Internal gate node index */
    private int gatePrmIndex;

    /* Internal source node index */
    private int srcPrmIndex;
    private int srcPrmPrmIndex;
    private int drnPrmPrmIndex;
    private double length = 1e-6;
    private double width = 20e-6;
    /* Drain temperature */
    private double td;

    /* Source temperature */
    private double ts;

    /* Temperature difference */
    private double dtemp;
    private double tVto;
    private double tLambda;
    private double tLambdahf;
    private double tEta;
    private double tMu;
    private double tPhib;
    private double tTheta;
    private double tRsi;
    private double tRdi;
    private double tRs;
    private double tRd;
    private double tRg;
    private double tRi;
    private double tRf;
    private double tGi;
    private double tGf;
    private double drnConduct;
    private double srcConduct;
    private double gateConduct;
//    private int mode;
    private double csatfs;
    private double csatfd;
    private double ggrwl;
    private double gchi0;
    private double isatb0;
    private double imax;
    private double cf;
    private double fl;
    private double delf;
    private double gds0;
    private double gm0;
    private double gm1;
    private double gm2;
    private double delidvds0;
    private double delidvds1;
    private double delidgch0;
    private double n0;
    private double nsb0;
    private double vcrits;
    private double vcritd;
//    private double sigma;
    private double vpo;
    private double vpou;
    private double vpod;
    private double deltaSqr;
    private Complex drnDrnPrmNode;
    private Complex gatePrmSrcPrmNode;
    private Complex drnPrmDrnNode;
    private Complex drnPrmSrcPrmNode;
    private Complex srcPrmSrcNode;
    private Complex drnDrnNode;
    private Complex srcSrcNode;
    private Complex srcPrmSrcPrmNode;
    private Complex gatePrmGateNode;
    private Complex srcPrmPrmSrcPrmPrmNode;
    private Complex srcPrmSrcPrmPrmNode;
    private Complex gatePrmSrcPrmPrmNode;
    private Complex drnPrmPrmDrnPrmNode;
    private Complex drnPrmPrmGatePrmNode;
    private Complex drnPrmDrnPrmNode;
    private Complex drnPrmPrmDrnPrmPrmNode;
    private Complex gateGateNode;
    private Complex gatePrmGatePrmNode;
    private Complex gatePrmDrnPrmNode;
    private Complex drnPrmGatePrmNode;
    private Complex srcPrmGatePrmNode;
    private Complex srcSrcPrmNode;
    private Complex srcPrmDrnPrmNode;
    private Complex gateGatePrmNode;
    private Complex srcPrmPrmSrcPrmNode;
    private Complex srcPrmPrmGatePrmNode;
    private Complex drnPrmDrnPrmPrmNode;
    private Complex gatePrmDrnPrmPrmNode;
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
    private double cdrain;
    private double gm;
    private double gds;
    private double capgs;
    private double capgd;
    private Real ceq = Real.zero();
    private Real geq = Real.zero();
    private Bool icheck = Bool.TRUE();
    private Bool ichk1 = Bool.TRUE();

    public MESAInstance() {
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
        if (phib == 0) {
            phib = 0.5 * CHARGE;
        }
        if (tf == 0) {
            tf = env.getTemp();
        }
        if (epsi == 0) {
            epsi = 12.244 * 8.85418e-12;
        }
        if (m == 0) {
            m = 1.0;
        }
        if (td == 0) {
            td = env.getTemp() + dtemp;
        }
        if (ts == 0) {
            ts = env.getTemp() + dtemp;
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
        if (gateResist != 0 && gatePrmIndex == 0) {
            gatePrmIndex = ckt.makeVoltNode(getInstName() + "gate").getIndex();
        } else {
            gatePrmIndex = gateIndex;
        }
        if (ri != 0 && srcPrmPrmIndex == 0) {
            srcPrmPrmIndex = ckt.makeVoltNode(getInstName() + "gs").getIndex();
        } else {
            srcPrmPrmIndex = srcPrmIndex;
        }
        if (rf != 0 && drnPrmPrmIndex == 0) {
            drnPrmPrmIndex = ckt.makeVoltNode(getInstName() + "gd").getIndex();
        } else {
            drnPrmPrmIndex = drnPrmIndex;
        }
        drnDrnNode = wrk.aquireNode(drnIndex, drnIndex);
        drnPrmDrnPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmIndex);
        drnPrmPrmDrnPrmPrmNode = wrk.aquireNode(drnPrmPrmIndex, drnPrmPrmIndex);
        gateGateNode = wrk.aquireNode(gateIndex, gateIndex);
        gatePrmGatePrmNode = wrk.aquireNode(gatePrmIndex, gatePrmIndex);
        srcSrcNode = wrk.aquireNode(srcIndex, srcIndex);
        srcPrmSrcPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmIndex);
        srcPrmPrmSrcPrmPrmNode = wrk.aquireNode(srcPrmPrmIndex, srcPrmPrmIndex);
        drnDrnPrmNode = wrk.aquireNode(drnIndex, drnPrmIndex);
        drnPrmDrnNode = wrk.aquireNode(drnPrmIndex, drnIndex);
        gatePrmDrnPrmNode = wrk.aquireNode(gatePrmIndex, drnPrmIndex);
        drnPrmGatePrmNode = wrk.aquireNode(drnPrmIndex, gatePrmIndex);
        gatePrmSrcPrmNode = wrk.aquireNode(gatePrmIndex, srcPrmIndex);
        srcPrmGatePrmNode = wrk.aquireNode(srcPrmIndex, gatePrmIndex);
        srcSrcPrmNode = wrk.aquireNode(srcIndex, srcPrmIndex);
        srcPrmSrcNode = wrk.aquireNode(srcPrmIndex, srcIndex);
        drnPrmSrcPrmNode = wrk.aquireNode(drnPrmIndex, srcPrmIndex);
        srcPrmDrnPrmNode = wrk.aquireNode(srcPrmIndex, drnPrmIndex);
        gatePrmGateNode = wrk.aquireNode(gatePrmIndex, gateIndex);
        gateGatePrmNode = wrk.aquireNode(gateIndex, gatePrmIndex);
        srcPrmPrmSrcPrmNode = wrk.aquireNode(srcPrmPrmIndex, srcPrmIndex);
        srcPrmSrcPrmPrmNode = wrk.aquireNode(srcPrmIndex, srcPrmPrmIndex);
        srcPrmPrmGatePrmNode = wrk.aquireNode(srcPrmPrmIndex, gatePrmIndex);
        gatePrmSrcPrmPrmNode = wrk.aquireNode(gatePrmIndex, srcPrmPrmIndex);
        drnPrmPrmDrnPrmNode = wrk.aquireNode(drnPrmPrmIndex, drnPrmIndex);
        drnPrmDrnPrmPrmNode = wrk.aquireNode(drnPrmIndex, drnPrmPrmIndex);
        drnPrmPrmGatePrmNode = wrk.aquireNode(drnPrmPrmIndex, gatePrmIndex);
        gatePrmDrnPrmPrmNode = wrk.aquireNode(gatePrmIndex, drnPrmPrmIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double _lambda;
        double f = tmprl.getOmega() / 2 / MathLib.PI;
        if (delf == 0) {
            _lambda = tLambda;
        } else {
            _lambda = tLambda + 0.5 * (tLambdahf - tLambda)
                    * (1 + MathLib.tanh((f - fl) / delf));
        }
        double vds = vgsStates.get(0) - vgdStates.get(0);
        double delidgch = delidgch0 * (1 + _lambda * vds);
        double delidvds = delidvds0 * (1 + 2 * _lambda * vds) - delidvds1;
        gm = (delidgch * gm0 + gm1) * gm2;
        gds = delidvds + gds0;
        double ggspp = ggsppStates.get(0);
        double ggdpp = ggdppStates.get(0);
        double ggs = ggsStates.get(0);
        double xgs = qgsStates.get(0) * tmprl.getOmega();
        double ggd = ggdStates.get(0);
        double xgd = qgdStates.get(0) * tmprl.getOmega();
        drnDrnNode.realPlusEq(m * (drnConduct));
        srcSrcNode.realPlusEq(m * (srcConduct));
        gateGateNode.realPlusEq(m * (gateConduct));
        srcPrmPrmSrcPrmPrmNode.realPlusEq(m * (tGi + ggspp));
        drnPrmPrmDrnPrmPrmNode.realPlusEq(m * (tGf + ggdpp));
        drnDrnPrmNode.realMinusEq(m * (drnConduct));
        drnPrmDrnNode.realMinusEq(m * (drnConduct));
        srcSrcPrmNode.realMinusEq(m * (srcConduct));
        srcPrmSrcNode.realMinusEq(m * (srcConduct));
        gateGatePrmNode.realMinusEq(m * (gateConduct));
        gatePrmGateNode.realMinusEq(m * (gateConduct));
        gatePrmDrnPrmNode.realPlusEq(m * (-ggd));
        gatePrmSrcPrmNode.realPlusEq(m * (-ggs));
        drnPrmGatePrmNode.realPlusEq(m * (gm - ggd));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGatePrmNode.realPlusEq(m * (-ggs - gm));
        srcPrmDrnPrmNode.realPlusEq(m * (-gds));
        gatePrmGatePrmNode.realPlusEq(m * (ggd + ggs + gateConduct + ggspp + ggdpp));
        drnPrmDrnPrmNode.realPlusEq(m * (gds + ggd + drnConduct + tGf));
        srcPrmSrcPrmNode.realPlusEq(m * (gds + gm + ggs + srcConduct + tGi));
        srcPrmSrcPrmPrmNode.realMinusEq(m * (tGi));
        srcPrmPrmSrcPrmNode.realMinusEq(m * (tGi));
        gatePrmSrcPrmPrmNode.realMinusEq(m * (ggspp));
        srcPrmPrmGatePrmNode.realMinusEq(m * (ggspp));
        drnPrmDrnPrmPrmNode.realMinusEq(m * (tGf));
        drnPrmPrmDrnPrmNode.realMinusEq(m * (tGf));
        gatePrmDrnPrmPrmNode.realMinusEq(m * (ggdpp));
        drnPrmPrmGatePrmNode.realMinusEq(m * (ggdpp));
        srcPrmPrmSrcPrmPrmNode.imagPlusEq(m * (xgs));
        drnPrmPrmDrnPrmPrmNode.imagPlusEq(m * (xgd));
        gatePrmGatePrmNode.imagPlusEq(m * (xgd + xgs));
        gatePrmDrnPrmPrmNode.imagMinusEq(m * (xgd));
        drnPrmPrmGatePrmNode.imagMinusEq(m * (xgd));
        gatePrmSrcPrmPrmNode.imagMinusEq(m * (xgs));
        srcPrmPrmGatePrmNode.imagMinusEq(m * (xgs));
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
        double cd = 0;
        double cdhat = 0.0;
        double cg;
        double cgd = 0;
        double cgs = 0;
        double cghat = 0.0;
        double ggd = 0;
        double ggs = 0;
        double ggspp = 0;
        double cgspp = 0;
        double ggdpp = 0;
        double cgdpp = 0;
        double vds = 0;
        double vgd = 0;
        double vgs = 0;
        double vgspp = 0;
        double vgdpp = 0;
//        double von;
        boolean inverse = false;
        boolean converged = true;
        double vtd = KoverQ * td;
        double vts = KoverQ * ts;
        double vted = n * vtd;
        double vtes = n * vts;
        boolean bypass = false;
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
            vds = icVDS;
            vgs = icVGS;
            vgd = vgs - vds;
            vgspp = vgs;
            vgdpp = vgd;
        } else if (mode.contains(MODE.INIT_JCT)
                && !off) {
            vgs = -1;
            vgd = -1;
            vgspp = 0;
            vgdpp = 0;
        } else if (mode.contains(MODE.INIT_JCT)
                || mode.contains(MODE.INIT_FIX) && off) {
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
                gmStates.set(0, gmStates.get(1));
                gdsStates.set(0, gdsStates.get(1));
                ggsStates.set(0, ggsStates.get(1));
                ggdStates.set(0, ggdStates.get(1));
                ggsppStates.set(0, ggsppStates.get(1));
                cgsppStates.set(0, cgsppStates.get(1));
                ggdppStates.set(0, ggdppStates.get(1));
                cgdppStates.set(0, cgdppStates.get(1));
            } else {
                //   Compute new nonlinear branch voltages
                vgs = (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmIndex).getReal());
                vgd = (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(drnPrmIndex).getReal());
                vgspp = (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(srcPrmPrmIndex).getReal());
                vgdpp = (wrk.getRhsOldAt(gatePrmIndex).getReal()
                        - wrk.getRhsOldAt(drnPrmPrmIndex).getReal());
            }
            double delvgs = vgs - vgsStates.get(0);
            double delvgd = vgd - vgdStates.get(0);
            double delvds = delvgs - delvgd;
            double delvgspp = vgspp - vgsppStates.get(0);
            double delvgdpp = vgdpp - vgdppStates.get(0);
            cghat = cgStates.get(0)
                    + ggdStates.get(0) * delvgd
                    + ggsStates.get(0) * delvgs
                    + ggsppStates.get(0) * delvgspp
                    + ggdppStates.get(0) * delvgdpp;
            cdhat = cdStates.get(0)
                    + gmStates.get(0) * delvgs
                    + gdsStates.get(0) * delvds
                    - ggdStates.get(0) * delvgd;
            if (env.isBypass()
                    && !mode.contains(MODE.INIT_PRED)
                    && (MathLib.abs(delvgs) < env.getRelTol() * MathLib.max(MathLib.abs(vgs),
                    MathLib.abs(vgsStates.get(0)))
                    + env.getVoltTol())) {
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
                                    cgs = cgsStates.get(0);
                                    cgd = cgdStates.get(0);
                                    gm = gmStates.get(0);
                                    gds = gdsStates.get(0);
                                    ggs = ggsStates.get(0);
                                    ggd = ggdStates.get(0);
                                    ggspp = ggsppStates.get(0);
                                    cgspp = cgsppStates.get(0);
                                    ggdpp = ggdppStates.get(0);
                                    cgdpp = cgdppStates.get(0);
                                    bypass = true;
                                }
                            }
                        }
                    }
                }
            }
            if (!bypass) {
                //   Limit nonlinear branch voltages
                vgs = MOSUtils.pnjLimit(vgs, vgsStates.get(0), vtes,
                        vcrits, icheck);
                vgd = MOSUtils.pnjLimit(vgd, vgdStates.get(0), vted,
                        vcritd, ichk1);
                if (ichk1.isFalse()) {
                    icheck.setFalse();
                }
                vgs = MOSUtils.fetLimit(vgs, vgsStates.get(0), tVto);
                vgd = MOSUtils.fetLimit(vgd, vgdStates.get(0), tVto);
                if (srcPrmPrmIndex == srcPrmIndex) {
                    vgspp = vgs;
                }
                if (drnPrmPrmIndex == drnPrmIndex) {
                    vgdpp = vgd;
                }
            }
        }
        if (!bypass) {
            //    Determine dc current and derivatives
            vds = vgs - vgd;
            double arg = -vgs * del / vts;
            double earg = MathLib.exp(arg);
            double evgs = MathLib.exp(vgs / vtes);
            ggs = csatfs * evgs / vtes + ggrwl * earg * (1 - arg) + env.getGMin();
            cgs = csatfs * (evgs - 1) + ggrwl * vgs * earg + env.getGMin() * vgs;
            cg = cgs;
            arg = -vgd * del / vtd;
            earg = MathLib.exp(arg);
            double evgd = MathLib.exp(vgd / vted);
            ggd = csatfd * evgd / vted + ggrwl * earg * (1 - arg) + env.getGMin();
            cgd = csatfd * (evgd - 1) + ggrwl * vgd * earg
                    + env.getGMin() * vgd;
            cg = cg + cgd;
            if (vds < 0) {
                vds = -vds;
                inverse = true;
            }
            double von = tVto + ks * (wrk.getRhsOldAt(srcPrmIndex).getReal() - vsg);
            if (level == 2) {
                mesa1(inverse ? vgd : vgs, vds, von);
            } else if (level == 3) {
                mesa2(inverse ? vgd : vgs, vds, von);
            } else if (level == 4) {
                mesa3(inverse ? vgd : vgs, vds, von);
            }
            if (inverse) {
                cdrain = -cdrain;
                vds = -vds;
                double tmp = capgs;
                capgs = capgd;
                capgd = tmp;
            }
            //    Compute equivalent drain current source
            cd = cdrain - cgd;
            if (mode.contains(MODE.TRAN, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                //     Charge storage elements
                double vgs1 = vgsppStates.get(1);
                double vgd1 = vgdppStates.get(1);
                if (mode.contains(MODE.INIT_TRAN)) {
                    qgsStates.set(1, capgs * vgspp);
                    qgdStates.set(1, capgd * vgdpp);
                }
                qgsStates.set(0, qgsStates.get(1) + capgs * (vgspp - vgs1));
                qgdStates.set(0, qgdStates.get(1) + capgd * (vgdpp - vgd1));
                //    Store small - signal parameters
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
                    ggspp = geq.get();
                    cgspp = cqgsStates.get(0);
                    cg = cg + cgspp;
                    stateTable.integrate(geq, ceq, capgd, qgdStates, cqgdStates);
                    ggdpp = geq.get();
                    cgdpp = cqgdStates.get(0);
                    cg = cg + cgdpp;
                    cd = cd - cgdpp;
                    if (mode.contains(MODE.INIT_TRAN)) {
                        cqgsStates.set(1, cqgsStates.get(0));
                        cqgdStates.set(1, cqgdStates.get(0));
                    }
                }
            }
            //   Check convergence
            if (!mode.contains(MODE.INIT_FIX) | !mode.isUseIC()) {
                if (icheck.isTrue() || (MathLib.abs(cghat - cg) >= env.getRelTol()
                        * MathLib.max(MathLib.abs(cghat), MathLib.abs(cg)) + env.getAbsTol())
                        || (MathLib.abs(cdhat - cd) > env.getRelTol()
                        * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol())) {
                    converged = false;
                }
            }
            vgsStates.set(0, vgs);
            vgsppStates.set(0, vgspp);
            vgdStates.set(0, vgd);
            vgdppStates.set(0, vgdpp);
            cgStates.set(0, cg);
            cdStates.set(0, cd);
            cgdStates.set(0, cgd);
            cgsStates.set(0, cgs);
            gmStates.set(0, gm);
            gdsStates.set(0, gds);
            ggsStates.set(0, ggs);
            ggdStates.set(0, ggd);
            ggsppStates.set(0, ggspp);
            cgsppStates.set(0, cgspp);
            ggdppStates.set(0, ggdpp);
            cgdppStates.set(0, cgdpp);
        }
        //     Load current vector
        double ccorr = ag * (cgs - cgd);
        double ceqgd = cgd + cgdpp - ggd * vgd - ggdpp * vgdpp;
        double ceqgs = cgs + cgspp - ggs * vgs - ggspp * vgspp;
        double cdreq = ((cd + cgd + cgdpp) - gds * vds - gm * vgs);
        wrk.getRhsAt(gatePrmIndex).realPlusEq(m * (-ceqgs - ceqgd));
        ceqgd = (cgd - ggd * vgd);
        wrk.getRhsAt(drnPrmIndex).realPlusEq(m * (-cdreq + ceqgd + ccorr));
        ceqgd = (cgdpp - ggdpp * vgdpp);
        wrk.getRhsAt(drnPrmPrmIndex).realPlusEq(ceqgd);
        ceqgs = (cgs - ggs * vgs);
        wrk.getRhsAt(srcPrmIndex).realPlusEq(m * (cdreq + ceqgs - ccorr));
        ceqgs = (cgspp - ggspp * vgspp);
        wrk.getRhsAt(srcPrmPrmIndex).realPlusEq(ceqgs);
        //     Load y matrix
        drnDrnNode.realPlusEq(m * (drnConduct));
        srcSrcNode.realPlusEq(m * (srcConduct));
        gateGateNode.realPlusEq(m * (gateConduct));
        srcPrmPrmSrcPrmPrmNode.realPlusEq(m * (tGi + ggspp));
        drnPrmPrmDrnPrmPrmNode.realPlusEq(m * (tGf + ggdpp));
        gatePrmGatePrmNode.realPlusEq(m * (ggd + ggs + gateConduct + ggspp + ggdpp));
        drnPrmDrnPrmNode.realPlusEq(m * (gds + ggd + drnConduct + tGf));
        srcPrmSrcPrmNode.realPlusEq(m * (gds + gm + ggs + srcConduct + tGi));
        drnDrnPrmNode.realMinusEq(m * (drnConduct));
        drnPrmDrnNode.realMinusEq(m * (drnConduct));
        srcSrcPrmNode.realMinusEq(m * (srcConduct));
        srcPrmSrcNode.realMinusEq(m * (srcConduct));
        gateGatePrmNode.realMinusEq(m * (gateConduct));
        gatePrmGateNode.realMinusEq(m * (gateConduct));
        gatePrmDrnPrmNode.realMinusEq(m * (ggd));
        gatePrmSrcPrmNode.realMinusEq(m * (ggs));
        drnPrmGatePrmNode.realPlusEq(m * (gm - ggd));
        drnPrmSrcPrmNode.realPlusEq(m * (-gds - gm));
        srcPrmGatePrmNode.realPlusEq(m * (-ggs - gm));
        srcPrmDrnPrmNode.realMinusEq(m * (gds));
        srcPrmSrcPrmPrmNode.realMinusEq(m * (tGi));
        srcPrmPrmSrcPrmNode.realMinusEq(m * (tGi));
        gatePrmSrcPrmPrmNode.realMinusEq(m * (ggspp));
        srcPrmPrmGatePrmNode.realMinusEq(m * (ggspp));
        drnPrmDrnPrmPrmNode.realMinusEq(m * (tGf));
        drnPrmPrmDrnPrmNode.realMinusEq(m * (tGf));
        gatePrmDrnPrmPrmNode.realMinusEq(m * (ggdpp));
        drnPrmPrmGatePrmNode.realMinusEq(m * (ggdpp));
        return converged;
    }

    private void mesa1(double vgs, double vds, double von) {
        double sqrt1;
        double delnsvgte;
        double vt = KoverQ * ts;
        double etavth = eta * vt;
        double rt = rsi + rdi;
        double vgt0 = vgs - von;
        double s = MathLib.exp((vgt0 - vsigmat) / vsigma);
        double _sigma = sigma0 / (1 + s);
        double vgt = vgt0 + _sigma * vds;
        double _mu = this.mu + theta * vgt;
        double vl = vs / _mu * length;
        double _beta = this.beta / (vpo + 3 * vl);
        double u = vgt / vt - 1;
        double t = MathLib.sqrt(deltaSqr + u * u);
        double vgte = 0.5 * vt * (2 + u + t);
        double a = 2 * _beta * vgte;
        double b = MathLib.exp(-vgt / etavth);
        if (vgte > vpo) {
            sqrt1 = 0;
        } else {
            sqrt1 = MathLib.sqrt(1 - vgte / vpo);
        }
        double ns = 1.0 / (1.0 / nd / this.d / (1 - sqrt1) + 1.0 / n0 * b);
        if (ns < 1.0e-38) {
            cdrain = 0;
            gm = 0.0;
            gds = 0.0;
            capgs = cf;
            capgd = cf;
            return;
        }
        double gchi = gchi0 * _mu * ns;
        double gch = gchi / (1 + gchi * rt);
        double f = MathLib.sqrt(1 + 2 * a * rsi);
        double _d = 1 + a * rsi + f;
        double e = 1 + tc * vgte;
        double isata = a * vgte / (_d * e);
        double isatb = isatb0 * _mu * MathLib.exp(vgt / etavth);
        double isat = isata * isatb / (isata + isatb);
        double vsate = isat / gch;
        double vdse = vds * MathLib.pow(1 + MathLib.pow(vds / vsate, mc), -1.0 / mc);
        double _m = this.m + alpha * vgte;
        double g = MathLib.pow(vds / vsate, _m);
        double h = MathLib.pow(1 + g, 1.0 / _m);
        delidgch0 = vds / h;
        double delidgch = delidgch0 * (1 + lambda * vds);
        cdrain = gch * delidgch;
        double tmp;
        if (vgt > vpo) {
            tmp = 0;
        } else {
            tmp = MathLib.sqrt(1 - vgt / vpo);
        }
        double cgc = width * length * EPS_GAAS / (tmp + b) / this.d;
        double c = (vsate - vdse) / (2 * vsate - vdse);
        c = c * c;
        capgs = cf + 2.0 / 3.0 * cgc * (1 - c);
        c = vsate / (2 * vsate - vdse);
        c = c * c;
        capgd = cf + 2.0 / 3.0 * cgc * (1 - c);
        tmp = 1 + gchi * rt;
        double delgchgchi = 1.0 / (tmp * tmp);
        double delgchins = gchi0 * _mu;
        double delnsvgt = ns * ns * 1.0 / n0 / etavth * b;
        double q = 1 - sqrt1;
        if (sqrt1 == 0) {
            delnsvgte = 0;
        } else {
            delnsvgte = 0.5 * ns * ns / (vpo * nd * this.d * sqrt1 * q * q);
        }
        double delvgtevgt = 0.5 * (1 + u / t);
        delidvds0 = gch / h;
        if (vds != 0.0) {
            delidvds1 = cdrain * MathLib.pow(vds / vsate, _m - 1) / (vsate * (1 + g));
        } else {
            delidvds1 = 0.0;
        }
        double delidvds = delidvds0 * (1 + 2 * lambda * vds) - delidvds1;
        double delidvsate = cdrain * g / (vsate * (1 + g));
        double delvsateisat = 1.0 / gch;
        double r = isata + isatb;
        r = r * r;
        double delisatisata = isatb * isatb / r;
        double v = 1.0 + 1.0 / f;
        double ddevgte = 2 * _beta * rsi * v * e + _d * tc;
        tmp = _d * _d * e * e;
        double delisatavgte = (2 * a * _d * e - a * vgte * ddevgte) / tmp;
        double delisatabeta = 2 * vgte * vgte * (_d * e - a * e * rsi * v) / tmp;
        double delisatisatb = isata * isata / r;
        double delvsategch = -vsate / gch;
        double dvgtvgs = 1 - sigma0 * vds * s / vsigma / ((1 + s) * (1 + s));
        tmp = gchi0 * ns * theta;
        double dgchivgt = delgchins * (delnsvgte * delvgtevgt + delnsvgt) + tmp;
        double dvgtevds = delvgtevgt * _sigma;
        double dgchivds = delgchins * (delnsvgte * dvgtevds + delnsvgt * _sigma) + tmp * _sigma;
        tmp = delisatabeta * 3 * _beta * vl * theta / (_mu * (vpo + 3 * vl));
        double disatavgt = delisatavgte * delvgtevgt + tmp;
        double disatavds = delisatavgte * dvgtevds + tmp * _sigma;
        double disatbvgt = isatb / etavth + isatb / _mu * theta;
        double p = delgchgchi * dgchivgt;
        double w = delgchgchi * dgchivds;
        double dvsatevgt = delvsateisat * (delisatisata * disatavgt + delisatisatb * disatbvgt) + delvsategch * p;
        double dvsatevds = delvsateisat * (delisatisata * disatavds + delisatisatb * disatbvgt * _sigma) + delvsategch * w;
        double gmmadd;
        double gdsmadd;
        if (alpha != 0) {
            if (vds == 0) {
                gmmadd = 0;
            } else {
                gmmadd = cdrain * (MathLib.log(1 + g) / (_m * _m) - g * MathLib.log(vds / vsate) / (_m * (1 + g)))
                        * alpha * delvgtevgt;
            }
            gdsmadd = gmmadd * _sigma;
        } else {
            gmmadd = 0;
        }
        gdsmadd = 0;
        gm0 = p;
        gm1 = delidvsate * dvsatevgt;
        gm2 = dvgtvgs;
        g = delidgch * p + gm1;
        gm = (g + gmmadd) * dvgtvgs;
        gds0 = delidvsate * dvsatevds + delidgch * w + gdsmadd;
        gds = delidvds + gds0;
    }

    private void mesa2(double vgs, double vds, double von) {
        double nsa;
        double ca;
        double r;
        double delnsavgte;
        double vt = KoverQ * ts;
        double etavth = eta * vt;
        double rt = rsi + rdi;
        double vgt0 = vgs - von;
        double s = MathLib.exp((vgt0 - vsigmat) / vsigma);
        double _sigma = sigma0 / (1 + s);
        double vgt = vgt0 + _sigma * vds;
        double t = vgt / vt - 1;
        double q = MathLib.sqrt(deltaSqr + t * t);
        double vgte = 0.5 * vt * (2 + t + q);
        double a = 2 * beta * vgte;
        if (vgt > vpod) {
            if (vgte > vpo) {
                nsa = ndelta * th + ndu * du;
                ca = EPS_GAAS / du;
                delnsavgte = 0;
            } else {
                r = MathLib.sqrt((vpo - vgte) / vpou);
                nsa = ndelta * th + ndu * du * (1 - r);
                ca = EPS_GAAS / du / r;
                delnsavgte = ndu * du / vpou / 2.0 / r;
            }
        } else {
            if (vpod - vgte < 0) {
                nsa = ndelta * th * (1 - du / th);
                ca = EPS_GAAS / du;
                delnsavgte = 0;
            } else {
                r = MathLib.sqrt(1 + ndu / ndelta * (vpod - vgte) / vpou);
                nsa = ndelta * th * (1 - du / th * (r - 1));
                ca = EPS_GAAS / du / r;
                delnsavgte = du * ndu / 2.0 / vpou / r;
            }
        }
        double b = MathLib.exp(vgt / etavth);
        double cb = EPS_GAAS / (du + th) * b;
        double nsb = nsb0 * b;
        double delnsbvgt = nsb / etavth;
        double ns = nsa * nsb / (nsa + nsb);
        if (ns < 1.0e-38) {
            cdrain = 0;
            gm = 0.0;
            gds = 0.0;
            capgs = cf;
            capgd = cf;
            return;
        }
        double gchi = gchi0 * ns;
        double gch = gchi / (1 + gchi * rt);
        double f = MathLib.sqrt(1 + 2 * a * rsi);
        double _d = 1 + a * rsi + f;
        double e = 1 + tc * vgte;
        double isata = a * vgte / _d / e;
        double isatb = isatb0 * b;
        double isat = isata * isatb / (isata + isatb);
        double vsate = isat / gch;
        double vdse = vds * MathLib.pow(1 + MathLib.pow(vds / vsate, mc), -1.0 / mc);
        double g = MathLib.pow(vds / vsate, m);
        double h = MathLib.pow(1 + g, 1.0 / m);
        delidgch0 = vds / h;
        double delidgch = delidgch0 * (1 + lambda * vds);
        cdrain = gch * delidgch;
        double cgc = width * length * ca * cb / (ca + cb);
        double c = (vsate - vdse) / (2 * vsate - vdse);
        c = c * c;
        capgs = cf + 2.0 / 3.0 * cgc * (1 - c);
        c = vsate / (2 * vsate - vdse);
        c = c * c;
        capgd = cf + 2.0 / 3.0 * cgc * (1 - c);
        c = vgt / vt - 1;
        double delvgtevgt = 0.5 * (1 + t / q);
        delidvds0 = gch / h;
        if (vds != 0.0) {
            delidvds1 = cdrain * MathLib.pow(vds / vsate, m - 1) / vsate / (1 + g);
        } else {
            delidvds1 = 0.0;
        }
        double delidvds = delidvds0 * (1 + 2 * lambda * vds)
                - delidvds1;
        double delgchgchi = 1.0 / (1 + gchi * rt) / (1 + gchi * rt);
        double delgchins = gchi0;
        r = nsa + nsb;
        r = r * r;
        double delnsvgt = (nsb * nsb * delvgtevgt * delnsavgte + nsa * nsa * delnsbvgt) / r;
        double delidvsate = cdrain * g / vsate / (1 + g);
        double delvsateisat = 1.0 / gch;
        r = isata + isatb;
        r = r * r;
        double delisatisata = isatb * isatb / r;
        double ddevgte = 2 * beta * rsi * (1 + 1.0 / f) * e + _d * tc;
        double delisatavgte = (2 * a * _d * e - a * vgte * ddevgte) / _d / _d / e / e;
        double delisatisatb = isata * isata / r;
        double delisatbvgt = isatb / etavth;
        double delvsategch = -vsate / gch;
        double delvgtvgs = 1 - sigma0 * vds * s / vsigma / (1 + s) / (1 + s);
        double p = delgchgchi * delgchins * delnsvgt;
        double delvsatevgt = delvsateisat * (delisatisata * delisatavgte * delvgtevgt
                + delisatisatb * delisatbvgt) + delvsategch * p;
        gm0 = p;
        gm1 = delidvsate * delvsatevgt;
        gm2 = delvgtvgs;
        g = delidgch * p + gm1;
        gm = g * delvgtvgs;
        gds0 = g * _sigma;
        gds = delidvds + gds0;
    }

    private void mesa3(double vgs, double vds, double von) {
        double vt = KoverQ * ts;
        double etavth = eta * vt;
        double vl = vs / mu * length;
        double rt = rsi + rdi;
        double vgt0 = vgs - von;
        double s = MathLib.exp((vgt0 - vsigmat) / vsigma);
        double _sigma = sigma0 / (1 + s);
        double vgt = vgt0 + _sigma * vds;
        double u = 0.5 * vgt / vt - 1;
        double t = MathLib.sqrt(deltaSqr + u * u);
        double vgte = vt * (2 + u + t);
        double b = MathLib.exp(vgt / etavth);
        double nsm = 2 * n0 * MathLib.log(1 + 0.5 * b);
        if (nsm < 1.0e-38) {
            cdrain = 0;
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
        double p = 1 + gchim * rsi + h;
        double isatm = gchim * vgte / p;
        double g = MathLib.pow(isatm / imax, gamma);
        double isat = isatm / MathLib.pow(1 + g, 1 / gamma);
        double vsate = isat / gch;
        double vdse = vds * MathLib.pow(1 + MathLib.pow(vds / vsate, mc), -1.0 / mc);
        double _d = MathLib.pow(vds / vsate, m);
        double e = MathLib.pow(1 + _d, 1.0 / m);
        double delidgch = vds * (1 + lambda * vds) / e;
        cdrain = gch * delidgch;
        double cgcm = 1.0 / (1 / cas * this.d / epsi
                + 1 / cbs * etavth / CHARGE / n0 * MathLib.exp(-vgt / etavth));
        double cgc = width * length * cgcm / MathLib.pow(1 + c, 1 + 1.0 / gamma);
        double a = (vsate - vdse) / (2 * vsate - vdse);
        a = a * a;
        double temp = 2.0 / 3.0;
        capgs = cf + temp * cgc * (1 - a);
        a = vsate / (2 * vsate - vdse);
        a = a * a;
        capgd = cf + temp * cgc * (1 - a);
        double delidvsate = (cdrain) * _d / vsate / (1 + _d);
        double delidvds = gch * (1 + 2 * lambda * vds) / e - (cdrain)
                * MathLib.pow(vds / vsate, m - 1) / (vsate * (1 + _d));
        a = 1 + gchi * rt;
        double delgchgchi = 1.0 / (a * a);
        double delgchins = gchi0;
        double delnsnsm = ns / nsm * (1 - c / (1 + c));
        double delnsmvgt = n0 / etavth / (1.0 / b + 0.5);
        double delvgtevgt = 0.5 * (1 + u / t);
        double delvsateisat = 1.0 / gch;
        double delisatisatm = isat / isatm * (1 - g / (1 + g));
        double delisatmvgte = gchim * (p - vgte * vgte / (vl * vl * h)) / (p * p);
        double delvsategch = -vsate / gch;
        double delisatmgchim = vgte * (p - gchim * rsi * (1 + 1.0 / h)) / (p * p);
        double delvgtvgs = 1 - vds * sigma0 / vsigma * s / ((1 + s) * (1 + s));
        p = delgchgchi * delgchins * delnsnsm * delnsmvgt;
        double delvsatevgt = (delvsateisat * delisatisatm * (delisatmvgte * delvgtevgt
                + delisatmgchim * gchi0 * delnsmvgt) + delvsategch * p);
        g = delidgch * p + delidvsate * delvsatevgt;
        gm = g * delvgtvgs;
        gds = delidvds + g * _sigma;
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
        if (srcPrmPrmIndex != 0 && srcPrmPrmIndex != srcPrmIndex) {
            ckt.deleteNode(srcPrmPrmIndex);
        }
        srcPrmPrmIndex = 0;
        if (drnPrmPrmIndex != 0 && drnPrmPrmIndex != drnPrmIndex) {
            ckt.deleteNode(drnPrmPrmIndex);
        }
        drnPrmPrmIndex = 0;
        return true;
    }
//    double EPS_GAAS = 12.244 * 8.85418e-12;

    public boolean temperature() {
        double temp;
        double vt;
        if (lambdahf == 0) {
            lambdahf = lambda;
        }
        if (level == 2) {
            vpo = CHARGE * nd * this.d * this.d
                    / 2 / EPS_GAAS;
        } else {
            vpou = CHARGE * ndu * du * du
                    / 2 / EPS_GAAS;
            vpod = CHARGE * ndelta * th
                    * (2 * du + th) / 2 / EPS_GAAS;
            vpo = vpou + vpod;
        }
        deltaSqr = delta * delta;
        vt = KoverQ * ts;
        if (mu1 == 0 && mu2 == 0) {
            tMu = mu * MathLib.pow(ts / tmu, xtm0);
        } else {
            double muimp = mu * MathLib.pow(ts / tmu, xtm0);
            double mupo = mu1 * MathLib.pow(tmu / ts, xtm1)
                    + mu2 * MathLib.pow(tmu / ts, xtm2);
            tMu = 1 / (1 / muimp + 1 / mupo);
        }
        tTheta = theta;
        tPhib = phib - phib1 * (ts - env.getNomTemp());
        tVto = vto - tvto * (ts - env.getNomTemp());
        imax = CHARGE * nmax * vs * width;
        if (level == 2) {
            gchi0 = CHARGE * width / length;
        } else {
            gchi0 = CHARGE * width / length * tMu;
        }
        beta = 2 * EPS_GAAS * vs * zeta * width / this.d;
        tEta = eta * (1 + ts / teta0) + teta1 / ts;
        tLambda = lambda * (1 - ts / tlambda);
        tLambdahf = lambdahf * (1 - ts / tlambda);
        double _d;
        if (level == 3) {
            _d = du;
        } else {
            _d = this.d;
        }
        if (level == 4) {
            n0 = epsi * tEta * vt / 2 / CHARGE / _d;
        } else {
            n0 = EPS_GAAS * tEta * vt / CHARGE / _d;
        }
        nsb0 = EPS_GAAS * tEta * vt / CHARGE / (du + th);
        isatb0 = CHARGE * n0 * vt * width / length;
        if (level == 4) {
            cf = 0.5 * epsi * width;
        } else {
            cf = 0.5 * EPS_GAAS * width;
        }
        csatfs = 0.5 * astar * ts
                * ts * MathLib.exp(-tPhib / (BOLTZMANN * ts))
                * length * width;
        csatfd = 0.5 * astar * td
                * td * MathLib.exp(-tPhib / (BOLTZMANN * td))
                * length * width;
        ggrwl = ggr * length * width
                * MathLib.exp(xchi * (ts - env.getNomTemp()));
        if (csatfs != 0) {
            vcrits = vt * MathLib.log(vt / (ROOT2 * csatfs));
        } else {
            vcrits = Double.MAX_VALUE;
        }
        if (csatfd != 0) {
            double vtd = KoverQ * td;
            vcritd = vtd * MathLib.log(vtd / (ROOT2 * csatfd));
        } else {
            vcritd = Double.MAX_VALUE;
        }
        temp = MathLib.exp(ts / tf);
        fl = flo * temp;
        delf = delfo * temp;
        if (rdi != 0.0) {
            tRdi = rdi * (1
                    + tc1 * (td - env.getNomTemp())
                    + tc2 * (td - env.getNomTemp()) * (td - env.getNomTemp()));
        } else {
            tRdi = 0;
        }
        if (rsi != 0.0) {
            tRsi = rsi * (1
                    + tc1 * (ts - env.getNomTemp())
                    + tc2 * (ts - env.getNomTemp()) * (ts - env.getNomTemp()));
        } else {
            tRsi = 0;
        }
        if (gateResist != 0.0) {
            tRg = gateResist * (1
                    + tc1 * (ts - env.getNomTemp())
                    + tc2 * (ts - env.getNomTemp()) * (ts - env.getNomTemp()));
        } else {
            tRg = 0;
        }
        if (srcResist != 0.0) {
            tRs = srcResist * (1
                    + tc1 * (ts - env.getNomTemp())
                    + tc2 * (ts - env.getNomTemp()) * (ts - env.getNomTemp()));
        } else {
            tRs = 0;
        }
        if (drnResist != 0.0) {
            tRd = drnResist * (1
                    + tc1 * (td - env.getNomTemp())
                    + tc2 * (td - env.getNomTemp()) * (td - env.getNomTemp()));
        } else {
            tRd = 0;
        }
        if (ri != 0.0) {
            tRi = ri * (1
                    + tc1 * (ts - env.getNomTemp())
                    + tc2 * (ts - env.getNomTemp()) * (ts - env.getNomTemp()));
        } else {
            tRi = 0;
        }
        if (rf != 0.0) {
            tRf = rf * (1
                    + tc1 * (td - env.getNomTemp())
                    + tc2 * (td - env.getNomTemp()) * (td - env.getNomTemp()));
        } else {
            tRf = 0;
        }
        if (tRd != 0) {
            drnConduct = 1 / tRd;
        } else {
            drnConduct = 0;
        }
        if (tRs != 0) {
            srcConduct = 1 / tRs;
        } else {
            srcConduct = 0;
        }
        if (tRg != 0) {
            gateConduct = 1 / tRg;
        } else {
            gateConduct = 0;
        }
        if (tRi != 0) {
            tGi = 1 / tRi;
        } else {
            tGi = 0;
        }
        if (tRf != 0) {
            tGf = 1 / tRf;
        } else {
            tGf = 0;
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
