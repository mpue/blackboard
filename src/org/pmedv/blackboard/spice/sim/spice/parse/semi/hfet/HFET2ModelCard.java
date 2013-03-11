/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.hfet;

import org.pmedv.blackboard.spice.sim.spice.model.semi.hfet.HFET2Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.hfet.HFET2ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFET2ModelCard extends HFETModelCard<HFET2Instance, HFET2ModelValues> {

    public HFET2ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public HFET2Instance createInstance() {
        HFET2Instance instance = new HFET2Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(HFET2ModelValues model, String name, String value) {
        if (name.equals("CF")) {
            model.setCf(parseDouble(value));
        } else if (name.equals("D1")) {
            model.setD1(parseDouble(value));
        } else if (name.equals("D2")) {
            model.setD2(parseDouble(value));
        } else if (name.equals("DEL")) {
            model.setDel(parseDouble(value));
        } else if (name.equals("DELTA")) {
            model.setDelta(parseDouble(value));
        } else if (name.equals("DELTAD")) {
            model.setDeltad(parseDouble(value));
        } else if (name.equals("DI")) {
            model.setDi(parseDouble(value));
        } /*? epsi is generally the constant EPS_SI
        else if (name.equals("EPSI")) {
        model.setEpsi(parseDouble(value));
        } */ else if (name.equals("ETA")) {
            model.setEta(parseDouble(value));
        } else if (name.equals("ETA1")) {
            model.setEta1(parseDouble(value));
        } else if (name.equals("ETA2")) {
            model.setEta2(parseDouble(value));
        } else if (name.equals("GAMMA")) {
            model.setGamma(parseDouble(value));
        } else if (name.equals("GGR")) {
            model.setGgr(parseDouble(value));
        } else if (name.equals("JS")) {
            model.setJs(parseDouble(value));
        } else if (name.equals("KLAMBDA")) {
            model.setKlambda(parseDouble(value));
        } else if (name.equals("KMU")) {
            model.setKmu(parseDouble(value));
        } else if (name.equals("KNMAX")) {
            model.setKnmax(parseDouble(value));
        } else if (name.equals("KVTO")) {
            model.setKvto(parseDouble(value));
        } else if (name.equals("LAMBDA")) {
            model.setLambda(parseDouble(value));
        } else if (name.equals("M")) {
            model.setM(parseDouble(value));
        } else if (name.equals("MC")) {
            model.setMc(parseDouble(value));
        } else if (name.equals("MU")) {
            model.setMu(parseDouble(value));
        } else if (name.equals("N")) {
            model.setN(parseDouble(value));
        } else if (name.equals("NMAX")) {
            model.setNmax(parseDouble(value));
        } else if (name.equals("P")) {
            model.setP(parseDouble(value));
        } else if (name.equals("RD")) {
            model.setRd(parseDouble(value));
        } else if (name.equals("RDI")) {
            model.setRdi(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setRs(parseDouble(value));
        } else if (name.equals("RSI")) {
            model.setRsi(parseDouble(value));
        } else if (name.equals("SIGMA0")) {
            model.setSigma0(parseDouble(value));
        } else if (name.equals("VS")) {
            model.setVs(parseDouble(value));
        } else if (name.equals("VSIGMA")) {
            model.setVsigma(parseDouble(value));
        } else if (name.equals("VSIGMAT")) {
            model.setVsigmat(parseDouble(value));
        } else if (name.equals("VT1")) {
            model.setVt1(parseDouble(value));
        } else if (name.equals("VT2")) {
            model.setVt2(parseDouble(value));
        } else if (name.equals("VTO")) {
            model.setVto(parseDouble(value));
        }
    }
}
