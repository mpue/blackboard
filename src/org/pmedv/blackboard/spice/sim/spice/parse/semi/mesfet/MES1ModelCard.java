/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet;

import org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet.MES1Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet.MES1ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class MES1ModelCard extends MESModelCard<MES1Instance, MES1ModelValues> {

    public MES1ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public MES1Instance createInstance() {
        MES1Instance instance = new MES1Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(MES1ModelValues model, String name, String value) {
        if (name.equals("VTO")) {
            model.setVto(parseDouble(value));
        } else if (name.equals("ALPHA")) {
            model.setAlpha(parseDouble(value));
        } else if (name.equals("BETA")) {
            model.setBeta(parseDouble(value));
        } else if (name.equals("LAMBDA")) {
            model.setLambda(parseDouble(value));
        } else if (name.equals("B")) {
            model.setB(parseDouble(value));
        } else if (name.equals("RD")) {
            model.setDrnResist(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setSrcResist(parseDouble(value));
        } else if (name.equals("CGS")) {
            model.setCapGS(parseDouble(value));
        } else if (name.equals("CGD")) {
            model.setCapGD(parseDouble(value));
        } else if (name.equals("PB")) {
            model.setGatePotential(parseDouble(value));
        } else if (name.equals("IS")) {
            model.setGateSatCurrent(parseDouble(value));
        } else if (name.equals("FC")) {
            model.setDepletionCapCoeff(parseDouble(value));
        } else if (name.equals("KF")) {
            model.setfNcoef(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setfNexp(parseDouble(value));
        }
    }
}
