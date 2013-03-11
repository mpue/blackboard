/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.jfet;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.model.semi.jfet.JFET1Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.jfet.JFET1ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class JFET1ModelCard extends JFETModelCard<JFET1Instance, JFET1ModelValues> {

    public JFET1ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public JFET1Instance createInstance() {
        JFET1Instance instance = new JFET1Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(JFET1ModelValues model, String name, String value) {
        if (name.equals("TNOM")) {
            model.setTnom(parseDouble(value) + CtoK);
        } else if (name.equals("VTO")) {
            model.setVto(parseDouble(value));
        } else if (name.equals("BETA")) {
            model.setBeta(parseDouble(value));
        } else if (name.equals("LAMBDA")) {
            model.setLambda(parseDouble(value));
        } else if (name.equals("RD")) {
            model.setDrnResist(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setSrcResist(parseDouble(value));
        } else if (name.equals("CGS")) {
            model.setCapGS(parseDouble(value));
        } else if (name.equals("CGD")) {
            model.setCapGD(parseDouble(value));
        } else if (name.equals("PB")) {
            model.setPhi(parseDouble(value));
        } else if (name.equals("IS")) {
            model.setGateSatCurrent(parseDouble(value));
        } else if (name.equals("FC")) {
            model.setDepCapCoeff(parseDouble(value));
        } else if (name.equals("KF")) {
            model.setfNcoef(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setfNexp(parseDouble(value));
            /* Modification for Sydney University JFET model */
        } else if (name.equals("B")) {
            model.setB(parseDouble(value));
        }

    }
}
