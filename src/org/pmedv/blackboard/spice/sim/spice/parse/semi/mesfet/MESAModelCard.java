/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet.MESAInstance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet.MESAModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class MESAModelCard extends MESModelCard<MESAInstance, MESAModelValues> {

    public MESAModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public MESAInstance createInstance() {
        MESAInstance instance = new MESAInstance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(MESAModelValues model, String name, String value) {
        if (name.equals("VTO")) {
            model.setVto(parseDouble(value));
        } else if (name.equals("BETA")) {
            model.setBeta(parseDouble(value));
        } else if (name.equals("VS")) {
            model.setVs(parseDouble(value));
        } else if (name.equals("LAMBDA")) {
            model.setLambda(parseDouble(value));
        } else if (name.equals("RD")) {
            model.setDrnResist(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setSrcResist(parseDouble(value));
        } else if (name.equals("RG")) {
            model.setGateResist(parseDouble(value));
        } else if (name.equals("RI")) {
            model.setRi(parseDouble(value));
        } else if (name.equals("RF")) {
            model.setRf(parseDouble(value));
        } else if (name.equals("RDI")) {
            model.setRdi(parseDouble(value));
        } else if (name.equals("RSI")) {
            model.setRsi(parseDouble(value));
        } else if (name.equals("PHIB")) {
            model.setPhib(parseDouble(value) * CHARGE);
        } else if (name.equals("PHIB1")) {
            model.setPhib1(parseDouble(value) * CHARGE);
        } else if (name.equals("ASTAR")) {
            model.setAstar(parseDouble(value));
        } else if (name.equals("GGR")) {
            model.setGgr(parseDouble(value));
        } else if (name.equals("DEL")) {
            model.setDel(parseDouble(value));
        } else if (name.equals("XCHI")) {
            model.setXchi(parseDouble(value));
        } else if (name.equals("N")) {
            model.setN(parseDouble(value));
        } else if (name.equals("ETA")) {
            model.setEta(parseDouble(value));
        } else if (name.equals("M")) {
            model.setM(parseDouble(value));
        } else if (name.equals("MC")) {
            model.setMc(parseDouble(value));
        } else if (name.equals("ALPHA")) {
            model.setAlpha(parseDouble(value));
        } else if (name.equals("SIGMA0")) {
            model.setSigma0(parseDouble(value));
        } else if (name.equals("VSIGMAT")) {
            model.setVsigmat(parseDouble(value));
        } else if (name.equals("VSIGMA")) {
            model.setVsigma(parseDouble(value));
        } else if (name.equals("MU")) {
            model.setMu(parseDouble(value));
        } else if (name.equals("THETA")) {
            model.setTheta(parseDouble(value));
        } else if (name.equals("MU1")) {
            model.setMu1(parseDouble(value));
        } else if (name.equals("MU2")) {
            model.setMu2(parseDouble(value));
        } else if (name.equals("D")) {
            model.setD(parseDouble(value));
        } else if (name.equals("ND")) {
            model.setNd(parseDouble(value));
        } else if (name.equals("DU")) {
            model.setDu(parseDouble(value));
        } else if (name.equals("NDU")) {
            model.setNdu(parseDouble(value));
        } else if (name.equals("TH")) {
            model.setTh(parseDouble(value));
        } else if (name.equals("NDELTA")) {
            model.setNdelta(parseDouble(value));
        } else if (name.equals("DELTA")) {
            model.setDelta(parseDouble(value));
        } else if (name.equals("TC")) {
            model.setTc(parseDouble(value));
        } else if (name.equals("TVTO")) {
            model.setTvto(parseDouble(value));
        } else if (name.equals("TLAMBDA")) {
            model.setTlambda(parseDouble(value) + CtoK);
        } else if (name.equals("TETA0")) {
            model.setTeta0(parseDouble(value) + CtoK);
        } else if (name.equals("TETA1")) {
            model.setTeta1(parseDouble(value) + CtoK);
        } else if (name.equals("TMU")) {
            model.setTmu(parseDouble(value) + CtoK);
        } else if (name.equals("XTM0")) {
            model.setXtm0(parseDouble(value));
        } else if (name.equals("XTM1")) {
            model.setXtm1(parseDouble(value));
        } else if (name.equals("XTM2")) {
            model.setXtm2(parseDouble(value));
        } else if (name.equals("KS")) {
            model.setKs(parseDouble(value));
        } else if (name.equals("VSG")) {
            model.setVsg(parseDouble(value));
        } else if (name.equals("LAMBDAHF")) {
            model.setLambdahf(parseDouble(value));
        } else if (name.equals("TF")) {
            model.setTf(parseDouble(value) + CtoK);
        } else if (name.equals("FLO")) {
            model.setFlo(parseDouble(value));
        } else if (name.equals("DELFO")) {
            model.setDelfo(parseDouble(value));
        } else if (name.equals("AG")) {
            model.setAg(parseDouble(value));
        } else if (name.equals("TC1")) {
            model.setTc1(parseDouble(value));
        } else if (name.equals("TC2")) {
            model.setTc2(parseDouble(value));
        } else if (name.equals("ZETA")) {
            model.setZeta(parseDouble(value));
        } else if (name.equals("NMAX")) {
            model.setNmax(parseDouble(value));
        } else if (name.equals("GAMMA")) {
            model.setGamma(parseDouble(value));
        } else if (name.equals("EPSI")) {
            model.setEpsi(parseDouble(value));
        } else if (name.equals("CBS")) {
            model.setCbs(parseDouble(value));
        } else if (name.equals("CAS")) {
            model.setCas(parseDouble(value));
        } else if (name.equals("LEVEL")) {
            model.setLevel(parseDouble(value));
        }
    }
}
