/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.hfet;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.model.semi.hfet.HFET1Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.hfet.HFET1ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFET1ModelCard extends HFETModelCard<HFET1Instance, HFET1ModelValues> {

    public HFET1ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public HFET1Instance createInstance() {
        HFET1Instance instance = new HFET1Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(HFET1ModelValues model, String name, String value) {
        if (name.equals("VTO")) {
                model.setVto(parseDouble(value));
            } else if (name.equals("LAMBDA")) {
                model.setLambda(parseDouble(value));
            } else if (name.equals("RD")) {
                model.setRd(parseDouble(value));
            } else if (name.equals("RS")) {
                model.setRs(parseDouble(value));
            } else if (name.equals("RG")) {
                model.setRg(parseDouble(value));
            } else if (name.equals("RDI")) {
                model.setRdi(parseDouble(value));
            } else if (name.equals("RSI")) {
                model.setRsi(parseDouble(value));
            } else if (name.equals("RGS")) {
                model.setRgs(parseDouble(value));
            } else if (name.equals("RGD")) {
                model.setRgd(parseDouble(value));
            } else if (name.equals("RI")) {
                model.setRi(parseDouble(value));
            } else if (name.equals("RF")) {
                model.setRf(parseDouble(value));
            } else if (name.equals("ETA")) {
                model.setEta(parseDouble(value));
            } else if (name.equals("M")) {
                model.setM(parseDouble(value));
            } else if (name.equals("MC")) {
                model.setMc(parseDouble(value));
            } else if (name.equals("GAMMA")) {
                model.setGamma(parseDouble(value));
            } else if (name.equals("SIGMA0")) {
                model.setSigma0(parseDouble(value));
            } else if (name.equals("VSIGMAT")) {
                model.setVsigmat(parseDouble(value));
            } else if (name.equals("VSIGMA")) {
                model.setVsigma(parseDouble(value));
            } else if (name.equals("MU")) {
                model.setMu(parseDouble(value));
            } else if (name.equals("DI")) {
                model.setDi(parseDouble(value));
            } else if (name.equals("DELTA")) {
                model.setDelta(parseDouble(value));
            } else if (name.equals("VS")) {
                model.setVs(parseDouble(value));
            } else if (name.equals("NMAX")) {
                model.setNmax(parseDouble(value));
            } else if (name.equals("DELTAD")) {
                model.setDeltad(parseDouble(value));
            } else if (name.equals("JS1D")) {
                model.setJs1d(parseDouble(value));
            } else if (name.equals("JS2D")) {
                model.setJs2d(parseDouble(value));
            } else if (name.equals("JS1S")) {
                model.setJs1s(parseDouble(value));
            } else if (name.equals("JS2S")) {
                model.setJs2s(parseDouble(value));
            } else if (name.equals("M1D")) {
                model.setM1d(parseDouble(value));
            } else if (name.equals("M2D")) {
                model.setM2d(parseDouble(value));
            } else if (name.equals("M1S")) {
                model.setM1s(parseDouble(value));
            } else if (name.equals("M2S")) {
                model.setM2s(parseDouble(value));
            } /*?else if (name.equals("EPSI")) {
                model.setEpsi(parseDouble(value));
            } */else if (name.equals("A1")) {
                model.setA1(parseDouble(value));
            } else if (name.equals("A2")) {
                model.setA2(parseDouble(value));
            } else if (name.equals("MV1")) {
                model.setMv1(parseDouble(value));
            } else if (name.equals("P")) {
                model.setP(parseDouble(value));
            } else if (name.equals("KAPPA")) {
                model.setKappa(parseDouble(value));
            } else if (name.equals("DELF")) {
                model.setDelf(parseDouble(value));
            } else if (name.equals("FGDS")) {
                model.setFgds(parseDouble(value));
            } else if (name.equals("TF")) {
                model.setTf(parseDouble(value) + CtoK);
            } else if (name.equals("CDS")) {
                model.setCds(parseDouble(value));
            } else if (name.equals("PHIB")) {
                model.setPhib(parseDouble(value) * CHARGE);
            } else if (name.equals("TALPHA")) {
                model.setTalpha(parseDouble(value));
            } else if (name.equals("MT1")) {
                model.setMt1(parseDouble(value));
            } else if (name.equals("MT2")) {
                model.setMt2(parseDouble(value));
            } else if (name.equals("CK1")) {
                model.setCk1(parseDouble(value));
            } else if (name.equals("CK2")) {
                model.setCk2(parseDouble(value));
            } else if (name.equals("CM1")) {
                model.setCm1(parseDouble(value));
            } else if (name.equals("CM2")) {
                model.setCm2(parseDouble(value));
            } else if (name.equals("CM3")) {
                model.setCm3(parseDouble(value));
            } else if (name.equals("ASTAR")) {
                model.setAstar(parseDouble(value));
            } else if (name.equals("ETA1")) {
                model.setEta1(parseDouble(value));
            } else if (name.equals("D1")) {
                model.setD1(parseDouble(value));
            } else if (name.equals("VT1")) {
                model.setVt1(parseDouble(value));
            } else if (name.equals("ETA2")) {
                model.setEta2(parseDouble(value));
            } else if (name.equals("D2")) {
                model.setD2(parseDouble(value));
            } else if (name.equals("VT2")) {
                model.setVt2(parseDouble(value));
            } else if (name.equals("GGR")) {
                model.setGgr(parseDouble(value));
            } else if (name.equals("DEL")) {
                model.setDel(parseDouble(value));
            } else if (name.equals("GATEMOD")) {
                model.setGatemod(Integer.parseInt(value));
            } else if (name.equals("KLAMBDA")) {
                model.setKlambda(parseDouble(value));
            } else if (name.equals("KMU")) {
                model.setKmu(parseDouble(value));
            } else if (name.equals("KVTO")) {
                model.setKvto(parseDouble(value));
            }
    }
}
