/*
 * .java
 *
 * Created on August 25, 2006, 2:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi;

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
public class DiodeInstance extends DiodeModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    private EnvVars env;
    private StateTable stateTable;
    private Temporal tmprl;
    protected int posPrmIndex;

    /* Stores the diode's capacitance */
    protected double cap;

    /* Conductance corresponding to ohmic */
    protected double conductance;

    /* Saturation current exponential */
    protected double saturationCurrentExp = 3;

    /* Temperature corrected junction potential */
    protected double tJctPot;

    /* Temperature corrected junction capacitance */
    protected double tJctCap;

    /* Temperature corrected sidewall junction potential */
    protected double tJctSWPot;

    /* Temperature corrected sidewall junction capacitance */
    protected double tJctSWCap;

    /* Temperature corrected transit time */
    protected double tTransitTime;

    /* Temperature corrected grading coefficient (MJ) */
    protected double tGradingCoeff;

    /* Temperature corrected series conductance */
    protected double tConductance;

    /* Temperature corrected transition point in curve matching (fc * vj) */
    protected double tDepCap;

    /* Temperature corrected side wall saturation current */
    protected double tSatSWCur;

    /* Temperature corrected Vcrit */
    protected double tVcrit;

    /* Temperature corrected f1 */
    protected double tF1;

    /* Temperature corrected breakdown voltage */
    protected double tBrkdwnV;

    /* Coeff. for capacitance equation */
    protected double tF2;

    /* Coeff. for capacitance equation */
    protected double tF3;

    /* Coeff. for capacitance equation */
    protected double tF2SW;

    /* Coeff. for capacitance equation */
    protected double tF3SW;
    private Complex posPosPrmNode;
    private Complex negPosPrmNode;
    private Complex posPrmPosNode;
    private Complex posPrmNegNode;
    private Complex posPosNode;
    private Complex negNegNode;
    private Complex posPrmPosPrmNode;
    private StateVector voltageStates;
    private StateVector currentStates;
    private StateVector conductStates;
    private StateVector capChargeStates;
    private StateVector capCurrentStates;
    Bool check = Bool.TRUE();

    /** Creates a new instance of  */
    public DiodeInstance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        env = ckt.getEnv();
        stateTable = ckt.getStateTable();
        tmprl = ckt.getTemporal();
        voltageStates = stateTable.createRow();
        currentStates = stateTable.createRow();
        conductStates = stateTable.createRow();
        capChargeStates = stateTable.createRow();
        capCurrentStates = stateTable.createRow();
        if (resist == 0) {
            posPrmIndex = posIndex;
        } else if (posPrmIndex == 0) {
            posPrmIndex = ckt.makeVoltNode(getInstName() + "internal").getIndex();
            /*?            if (env.isCopyNodeSets()) {
            if (ckt.inst2Node(this, 1, tmpNode, tmpName)== true) {
            if (tmpNode.isNSGiven())
            tmp.setNS(tmpNode.getNS());
            }
            }
             * */
        }
        // Limit grading coeff to max of 0.9
        if (gradingCoeff > 0.9) {
            StandardLog.warning(getModelName() + ": grading coefficient too"
                    + " large, limited to 0.9");
            gradingCoeff = 0.9;
        }
        // Limit activation energy to min of 0.1
        if (activationEnergy < 0.1) {
            StandardLog.warning(getModelName() + ": activation energy too"
                    + " small, limited to 0.1");
            activationEnergy = 0.1;
        }
        // Limit depletion cap coeff to 0.95
        if (depletionCapCoeff > 0.95) {
            StandardLog.warning(getModelName() + ": coefficient Fc too large,"
                    + " limited to 0.95");
            depletionCapCoeff = 0.95;
        }
        // Limit sidewall depletion cap coeff to 0 0.95
        if (depletionSWcapCoeff > 0.95) {
            StandardLog.warning(getModelName() + ": coefficient Fcs too large,"
                    + " limited to 0.95");
            depletionSWcapCoeff = 0.95;
        }
        if (resist != 0) {
            conductance = 1 / resist;
        }
        posPosPrmNode = wrk.aquireNode(posIndex, posPrmIndex);
        negPosPrmNode = wrk.aquireNode(negIndex, posPrmIndex);
        posPrmPosNode = wrk.aquireNode(posPrmIndex, posIndex);
        posPrmNegNode = wrk.aquireNode(posPrmIndex, negIndex);
        posPosNode = wrk.aquireNode(posIndex, posIndex);
        negNegNode = wrk.aquireNode(negIndex, negIndex);
        posPrmPosPrmNode = wrk.aquireNode(posPrmIndex, posPrmIndex);
        return true;
    }

    public boolean acLoad(Mode mode) {
        double gspr = tConductance * area * m;
        double geq = conductStates.get(0);
        double xceq = capCurrentStates.get(0) * tmprl.getOmega();
        posPosNode.realPlusEq(gspr);
        negNegNode.realPlusEq(geq);
        negNegNode.imagPlusEq(xceq);
        posPrmPosPrmNode.realPlusEq(geq + gspr);
        posPrmPosPrmNode.imagPlusEq(xceq);
        posPosPrmNode.realMinusEq(gspr);
        negPosPrmNode.realMinusEq(geq);
        negPosPrmNode.imagMinusEq(xceq);
        posPrmPosNode.realMinusEq(gspr);
        posPrmNegNode.realMinusEq(geq);
        posPrmNegNode.imagMinusEq(xceq);
        return true;
    }

    public boolean convTest(Mode mode) {
        double vd = wrk.getRhsOldAt(posPrmIndex).getReal()
                - wrk.getRhsOldAt(negIndex).getReal();
        double delvd = vd - voltageStates.get(0);
        double cdhat = currentStates.get(0)
                + conductStates.get(0) * delvd;
        double cd = currentStates.get(0);
        double tol = env.getRelTol()
                * MathLib.max(MathLib.abs(cdhat), MathLib.abs(cd)) + env.getAbsTol();
        if (MathLib.abs(cdhat - cd) > tol) {
            return false;
        }
        return true;
    }

    public boolean loadInitCond() {
        if (initCond == 0) {
            initCond =
                    wrk.getRhsAt(posIndex).getReal()
                    - wrk.getRhsAt(negIndex).getReal();
        }
        return true;
    }

    public boolean load(Mode mode) {
        double cd = 0;
        double gd = 0;
        double vd = 0;
        boolean bypass = false;
        double gspr = tConductance * area * m;
        double vt = KoverQ * temp;
        double vte = emissionCoeff * vt;
        if (mode.contains(MODE.INIT_SMSIG)) {
            vd = voltageStates.get(0);
        } else if (mode.contains(MODE.INIT_TRAN)) {
            vd = voltageStates.get(1);
        } else if (mode.contains(MODE.INIT_JCT)
                && mode.contains(MODE.TRANOP) && mode.isUseIC()) {
            vd = initCond;
        } else if (mode.contains(MODE.INIT_JCT) && off) {
            vd = 0;
        } else if (mode.contains(MODE.INIT_JCT)) {
            vd = tVcrit;
        } else if (mode.contains(MODE.INIT_FIX) && off) {
            vd = 0;
        } else {
            if (mode.contains(MODE.INIT_PRED)) {
                voltageStates.set(0, voltageStates.get(1));
                currentStates.set(0, currentStates.get(1));
                conductStates.set(0, conductStates.get(1));
            } else {
                vd = wrk.getRhsOldAt(posPrmIndex).getReal()
                        - wrk.getRhsOldAt(negIndex).getReal();
            }
            double delvd = vd - voltageStates.get(0);
            double cdhat = currentStates.get(0)
                    + conductStates.get(0) * delvd;
            if (!mode.contains(MODE.INIT_PRED) && env.isBypass()) {
                double tol = env.getVoltTol() + env.getRelTol()
                        * MathLib.max(MathLib.abs(vd), MathLib.abs(voltageStates.get(0)));
                if (MathLib.abs(delvd) < tol) {
                    tol = env.getRelTol() * MathLib.max(MathLib.abs(cdhat),
                            MathLib.abs(currentStates.get(0)))
                            + env.getAbsTol();
                    if (MathLib.abs(cdhat - currentStates.get(0)) < tol) {
                        vd = voltageStates.get(0);
                        cd = currentStates.get(0);
                        gd = conductStates.get(0);
                        bypass = true;
                    }
                }
            }
            if (!bypass) {
                // Limit new junction voltage
                if (breakdownVoltage != 0 && (vd < MathLib.min(0, -tBrkdwnV + 10 * vte))) {
                    double vdTemp = -(vd + tBrkdwnV);
                    vdTemp = MOSUtils.pnjLimit(vdTemp, -voltageStates.get(0) + tBrkdwnV, vte, tVcrit, check);
                    vd = -(vdTemp + tBrkdwnV);
                } else {
                    vd = MOSUtils.pnjLimit(vd, voltageStates.get(0),
                            vte, tVcrit, check);
                }
            }
        }
        if (!bypass) {
            //case NEXT1:
            double arg;
            double sarg;
            double sqrtIkr;
            double ikrAreaM;
            double csat = (tSatCur * area + tSatSWCur * pj) * m;
            if (vd >= -3 * vte) {
                //  Forward
                double evd = MathLib.exp(vd / vte);
                cd = csat * (evd - 1) + env.getGMin() * vd;
                gd = csat * evd / vte + env.getGMin();
                if (forwardKneeCurrent != 0 && (forwardKneeCurrent > 0) && (cd > 1e-18)) {
                    gd = gd - env.getGMin();
                    cd = cd - env.getGMin() * vd;
                    double ikfAreaM = forwardKneeCurrent * area * m;
                    double sqrtIkf = MathLib.sqrt(cd / ikfAreaM);
                    gd = ((1 + sqrtIkf) * gd - cd * gd / (2 * sqrtIkf * ikfAreaM)) / (1 + 2 * sqrtIkf + cd / ikfAreaM) + env.getGMin();
                    cd = cd / (1 + sqrtIkf) + env.getGMin() * vd;
                }
            } else if (breakdownVoltage == 0 || vd >= -tBrkdwnV) {
                //  Reverse
                arg = 3 * vte / (vd * MathLib.E);
                arg = arg * arg * arg;
                cd = -csat * 1 + arg + env.getGMin() * vd;
                gd = csat * 3 * arg / vd + env.getGMin();
                if (reverseKneeCurrent != 0 && (reverseKneeCurrent > 0) && (cd < -1e-18)) {
                    gd = gd - env.getGMin();
                    cd = cd - env.getGMin() * vd;
                    ikrAreaM = reverseKneeCurrent * area * m;
                    sqrtIkr = MathLib.sqrt(cd / (-ikrAreaM));
                    gd = ((1 + sqrtIkr) * gd + cd * gd / (2 * sqrtIkr * ikrAreaM)) / (1 + 2 * sqrtIkr - cd / ikrAreaM) + env.getGMin();
                    cd = cd / (1 + sqrtIkr) + env.getGMin() * vd;
                }
            } else {
                // Breakdown
                double evrev = MathLib.exp(-(tBrkdwnV + vd) / vte);
                cd = -csat * evrev + env.getGMin() * vd;
                gd = csat * evrev / vte + env.getGMin();
                if (reverseKneeCurrent != 0 && (reverseKneeCurrent > 0) && (cd < -1e-18)) {
                    gd = gd - env.getGMin();
                    cd = cd - env.getGMin() * vd;
                    ikrAreaM = reverseKneeCurrent * area * m;
                    sqrtIkr = MathLib.sqrt(cd / (-ikrAreaM));
                    gd = ((1 + sqrtIkr) * gd + cd * gd / (2 * sqrtIkr * ikrAreaM)) / (1 + 2 * sqrtIkr - cd / ikrAreaM);
                    cd = cd / (1 + sqrtIkr);
                }
            }
            if (mode.contains(MODE.TRAN, MODE.AC, MODE.INIT_SMSIG)
                    || (mode.contains(MODE.TRANOP) && mode.isUseIC())) {
                // Charge storage elements
                double czero = tJctCap * area * m;
                double czeroSW = tJctSWCap * pj * m;
                if (vd < tDepCap) {
                    arg = 1 - vd / tJctPot;
                    double argSW = 1 - vd / tJctSWPot;
                    sarg = MathLib.exp(-tGradingCoeff * MathLib.log(arg));
                    double sargSW = MathLib.exp(-gradingSWCoeff * MathLib.log(argSW));
                    capChargeStates.set(0, tTransitTime * cd
                            + tJctPot * czero * (1 - arg * sarg) / (1 - tGradingCoeff)
                            + tJctSWPot * czeroSW * (1 - argSW * sargSW)
                            / (1 - gradingSWCoeff));
                    cap = tTransitTime * gd + czero * sarg + czeroSW * sargSW;
                } else {
                    double czof2 = czero / tF2;
                    double czof2SW = czeroSW / tF2SW;
                    capChargeStates.set(0, tTransitTime * cd
                            + czero * tF1 + czof2 * (tF3
                            * (vd - tDepCap) + (tGradingCoeff
                            / (tJctPot + tJctPot)) * (vd * vd
                            - tDepCap * tDepCap)) + czof2SW
                            * (tF3SW * (vd - tDepCap)
                            + (gradingSWCoeff / (tJctSWPot
                            + tJctSWPot)) * (vd * vd - tDepCap * tDepCap)));
                    cap = tTransitTime * gd
                            + czof2 * (tF3 + tGradingCoeff * vd / tJctPot)
                            + czof2SW * (tF3SW + gradingSWCoeff * vd / tJctSWPot);
                }
                if (!mode.contains(MODE.TRANOP) || !mode.isUseIC()) {
                    if (mode.contains(MODE.INIT_SMSIG)) {
                        capCurrentStates.set(0, cap);
                        return true;
                    }
                    if (mode.contains(MODE.INIT_TRAN)) {
                        capChargeStates.set(1, capChargeStates.get(0));
                    }
                    Real gEq = Real.zero();
                    Real iEq = Real.zero();
                    stateTable.integrate(gEq, iEq, cap, capChargeStates, capCurrentStates);
                    gd = gd + gEq.get();
                    cd = cd + capCurrentStates.get(0);
                    if (mode.contains(MODE.INIT_TRAN)) {
                        capCurrentStates.set(1, capCurrentStates.get(0));
                    }
                }
            }
            if ((!mode.contains(MODE.INIT_FIX) || !off) && check.isTrue()) {
                ckt.setNonConverged(true);
            }
            //case NEXT2:
            voltageStates.set(0, vd);
            currentStates.set(0, cd);
            conductStates.set(0, gd);
        }
        //case LOAD:
        double cdeq = cd - gd * vd;
        wrk.getRhsAt(negIndex).realPlusEq(cdeq);
        wrk.getRhsAt(posPrmIndex).realMinusEq(cdeq);
        posPosNode.realPlusEq(gspr);
        negNegNode.realPlusEq(gd);
        posPrmPosPrmNode.realPlusEq(gd + gspr);
        posPosPrmNode.realMinusEq(gspr);
        negPosPrmNode.realMinusEq(gd);
        posPrmPosNode.realMinusEq(gspr);
        posPrmNegNode.realMinusEq(gd);
        return true;
    }

    public boolean pzLoad(Complex s) {
        double gspr = tConductance * area * m;
        double geq = conductStates.get(0);
        double xceq = capCurrentStates.get(0);
        posPosNode.realPlusEq(gspr);
        negNegNode.realPlusEq(geq + xceq * s.getReal());
        negNegNode.imagPlusEq(xceq * s.getReal());
        posPrmPosPrmNode.realPlusEq(geq + gspr + xceq * s.getReal());
        posPrmPosPrmNode.imagPlusEq(xceq * s.getReal());
        posPosPrmNode.realMinusEq(gspr);
        negPosPrmNode.realMinusEq(geq + xceq * s.getReal());
        negPosPrmNode.imagMinusEq(xceq * s.getReal());
        posPrmPosNode.realMinusEq(gspr);
        posPrmNegNode.realMinusEq(geq + xceq * s.getReal());
        posPrmNegNode.imagMinusEq(xceq * s.getReal());
        return true;
    }

    public boolean unSetup() {
        if (posPrmIndex != posIndex) {
            ckt.deleteNode(posPrmIndex);
            posPrmIndex = 0;
        }
        return true;
    }

    public boolean temperature() {
        if(tnom == 0){
            tnom = env.getNomTemp();
        }
        double vtnom = KoverQ * tnom;
        double xfc = MathLib.log(1 - depletionCapCoeff);
        double xfcs = MathLib.log(1 - depletionSWcapCoeff);
        if (temp == 0) {
            temp = env.getTemp() + dtemp;
        }
        // Junction grading temperature adjustment
        double difference = temp - tnom;
        double factor = 1 + (gradCoeffTemp1 * difference)
                + (gradCoeffTemp2 * difference * difference);
        tGradingCoeff = gradingCoeff * factor;
        // Temperature corrected grading coefficient 0 0.9
        if (tGradingCoeff > 0.9) {
            StandardLog.warning(getInstName() + ": Temperature corrected grading coefficient too large, limited to 0.9");
            tGradingCoeff = 0.9;
        }
        double vt = KoverQ * temp;
        double fact2 = temp / REF_TEMP;
        double egfet = 1.16 - (7.02e-4 * temp * temp)
                / (temp + 1108);
        double arg = -egfet / (2 * BOLTZMANN * temp)
                + 1.1150877 / (BOLTZMANN * REF_TEMP + REF_TEMP);
        double pbfact = -2 * vt * (1.5 * MathLib.log(fact2) + CHARGE * arg);
        double egfet1 = 1.16 - (7.02e-4 * tnom * tnom)
                / (tnom + 1108);
        double arg1 = -egfet1 / (BOLTZMANN * 2 * tnom)
                + 1.1150877 / (2 * BOLTZMANN * REF_TEMP);
        double fact1 = tnom / REF_TEMP;
        double pbfact1 = -2 * vtnom * (1.5 * MathLib.log(fact1) + CHARGE * arg1);
        double pbo = (junctionPot - pbfact1) / fact1;
        double gmaold = (junctionPot - pbo) / pbo;
        tJctCap = junctionCap
                / (1 + tGradingCoeff
                * (400e-6 * (tnom - REF_TEMP) - gmaold));
        tJctPot = pbfact + fact2 * pbo;
        double gmanew = (tJctPot - pbo) / pbo;
        tJctCap *= 1 + tGradingCoeff
                * (400e-6 * (temp - REF_TEMP) - gmanew);
        double pboSW = (junctionSWPot - pbfact1) / fact1;
        double gmaSWold = (junctionSWPot - pboSW) / pboSW;
        tJctSWCap = junctionSWCap
                / (1 + gradingSWCoeff
                * (400e-6 * (tnom - REF_TEMP) - gmaSWold));
        tJctSWPot = pbfact + fact2 * pboSW;
        double gmaSWnew = (tJctSWPot - pboSW) / pboSW;
        tJctSWCap *= 1 + gradingSWCoeff
                * (400e-6 * (temp - REF_TEMP) - gmaSWnew);
        tSatCur = satCur * MathLib.exp(((temp / tnom) - 1)
                * activationEnergy / (emissionCoeff * vt)
                + saturationCurrentExp / emissionCoeff
                * MathLib.log(temp / tnom));
        tSatSWCur = satSWCur * MathLib.exp(((temp / tnom) - 1)
                * activationEnergy / (emissionCoeff * vt)
                + saturationCurrentExp / emissionCoeff
                * MathLib.log(temp / tnom));
        // Recompute for f1 after temperature adjustment
        tF1 = tJctPot
                * (1 - MathLib.exp((1 - tGradingCoeff) * xfc))
                / (1 - tGradingCoeff);
        // Recompute for Depletion Capacitance after temperature adjustment
        tDepCap = depletionCapCoeff * tJctPot;
        // Recompute for vCrit after temperature adjustment
        double vte = emissionCoeff * vt;
        tVcrit = vte * MathLib.log(vte / (ROOT2 * tSatCur * area));
        // Compute breakdown voltage after temperature adjustment
        boolean matched = false;
        double cbv;
        double xbv;
        double xcbv = 0;
        double tol;
        if (breakdownVoltage != 0) {
            cbv = breakdownCurrent * area * m;
            if (cbv < tSatCur * area * m
                    * breakdownVoltage / vt) {
                cbv = tSatCur * area * m
                        * breakdownVoltage / vt;
                StandardLog.warning(String.format(": breakdown current increased to %g to resolve",
                        cbv));
                StandardLog.warning(getInstName());
                StandardLog.warning("incompatibility with specified saturation current");
                xbv = breakdownVoltage;
            } else {
                tol = env.getRelTol() * cbv;
                xbv = breakdownVoltage - vt * MathLib.log(1 + cbv
                        / (tSatCur * area * m));
                for (int iter = 0; iter < 25; iter++) {
                    xbv = breakdownVoltage - vt * MathLib.log(cbv
                            / (tSatCur * area * m) + 1 - xbv / vt);
                    xcbv = tSatCur * area * m
                            * (MathLib.exp((breakdownVoltage - xbv) / vt) - 1 + xbv / vt);
                    if (MathLib.abs(xcbv - cbv) <= tol) {
                        matched = true;
                    }
                    break;
                }
                if (!matched) {
                    StandardLog.warning(String.format(": unable to match forward and reverse diode"
                            + " regions: bv = %g, ibv = %g",
                            xbv, xcbv));
                    StandardLog.warning(getInstName());
                }
            }
            if (matched) {
                tBrkdwnV = xbv;
            }
        }
        // Transit time temperature adjustment
        difference = temp - tnom;
        factor = 1 + (tranTimeTemp1 * difference)
                + (tranTimeTemp2 * difference * difference);
        tTransitTime = transitTime * factor;
        // Series resistance temperature adjustment
        tConductance = conductance;
        if (resist != 0) {
            difference = temp - tnom;
            factor = 1 + (resistTemp1) * difference
                    + (resistTemp2 * difference * difference);
            tConductance = conductance / factor;
        }
        tF2 = MathLib.exp((1 + tGradingCoeff) * xfc);
        tF3 = 1 - depletionCapCoeff * (1 + tGradingCoeff);
        tF2SW = MathLib.exp((1 + gradingSWCoeff) * xfcs);
        tF3SW = 1 - depletionSWcapCoeff * (1 + gradingSWCoeff);
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return stateTable.terr(capChargeStates, capCurrentStates, timeStep);
    }

    public boolean accept(Mode mode) {
        return true;
    }
}
