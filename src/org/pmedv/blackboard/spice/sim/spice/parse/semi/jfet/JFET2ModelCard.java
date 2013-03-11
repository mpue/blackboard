/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.jfet;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.model.semi.jfet.JFET2Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.jfet.JFET2ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class JFET2ModelCard extends JFETModelCard<JFET2Instance, JFET2ModelValues> {

    public JFET2ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public JFET2Instance createInstance() {
        JFET2Instance instance = new JFET2Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(JFET2ModelValues model, String name, String value) {
        if (name.equals("TNOM")) {
            model.setTnom(parseDouble(value) + CtoK);
        } else if (name.equals("ACGAM")) {
            model.setAcgam(parseDouble(value));
        } else if (name.equals("BETA")) {
            model.setBeta(parseDouble(value));
        } else if (name.equals("CDS")) {
            model.setCapDS(parseDouble(value));
        } else if (name.equals("CGD")) {
            model.setCapGD(parseDouble(value));
        } else if (name.equals("CGS")) {
            model.setCapGS(parseDouble(value));
        } else if (name.equals("DELTA")) {
            model.setDelta(parseDouble(value));
        } else if (name.equals("HFETA")) {
            model.setHfeta(parseDouble(value));
        } else if (name.equals("HFE1")) {
            model.setHfe1(parseDouble(value));
        } else if (name.equals("HFE2")) {
            model.setHfe2(parseDouble(value));
        } else if (name.equals("HFG1")) {
            model.setHfg1(parseDouble(value));
        } else if (name.equals("HFG2")) {
            model.setHfg2(parseDouble(value));
        } else if (name.equals("MVST")) {
            model.setMvst(parseDouble(value));
        } else if (name.equals("MXI")) {
            model.setMxi(parseDouble(value));
        } else if (name.equals("FC")) {
            model.setDepCapCoeff(parseDouble(value));
        } else if (name.equals("IBD")) {
            model.setIbd(parseDouble(value));
        } else if (name.equals("IS")) {
            model.setGateSatCurrent(parseDouble(value));
        } else if (name.equals("LAMBDA")) {
            model.setLambda(parseDouble(value));
        } else if (name.equals("LFGAM")) {
            model.setLfgam(parseDouble(value));
        } else if (name.equals("LFG1")) {
            model.setLfg1(parseDouble(value));
        } else if (name.equals("LFG2")) {
            model.setLfg2(parseDouble(value));
        } else if (name.equals("N")) {
            model.setN(parseDouble(value));
        } else if (name.equals("P")) {
            model.setP(parseDouble(value));
        } else if (name.equals("PB")) {
            model.setPhi(parseDouble(value));
        } else if (name.equals("Q")) {
            model.setQ(parseDouble(value));
        } else if (name.equals("RD")) {
            model.setDrnResist(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setSrcResist(parseDouble(value));
        } else if (name.equals("TAUD")) {
            model.setTaud(parseDouble(value));
        } else if (name.equals("TAUG")) {
            model.setTaug(parseDouble(value));
        } else if (name.equals("VBD")) {
            model.setVbd(parseDouble(value));
        } else if (name.equals("VER")) {
            model.setVersion(parseDouble(value));
        } else if (name.equals("VST")) {
            model.setVst(parseDouble(value));
        } else if (name.equals("VTO")) {
            model.setVto(parseDouble(value));
        } else if (name.equals("XC")) {
            model.setXc(parseDouble(value));
        } else if (name.equals("XI")) {
            model.setXi(parseDouble(value));
        } else if (name.equals("Z")) {
            model.setZ(parseDouble(value));
        } else if (name.equals("HFGAM")) {
            model.setHfgam(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setfNexp(parseDouble(value));
        } else if (name.equals("KF")) {
            model.setfNcoef(parseDouble(value));
        }
    }
}
