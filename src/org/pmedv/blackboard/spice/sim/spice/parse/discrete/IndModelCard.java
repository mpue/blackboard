/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.IndInstance;
import org.pmedv.blackboard.spice.sim.spice.model.discrete.IndModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public class IndModelCard extends ModelCard<IndInstance, IndModelValues> {

    public IndModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public void setProperty(IndModelValues model, String name, String value) {
        if (name.equals("IND")) {
            model.setInduct(parseDouble(value));
        } else if (name.equals("CSECT")) {
            model.setcSect(parseDouble(value));
        } else if (name.equals("LENGTH")) {
            model.setLength(parseDouble(value));
        } else if (name.equals("TC1")) {
            model.setTempCoeff1(parseDouble(value));
        } else if (name.equals("TC2")) {
            model.setTempCoeff2(parseDouble(value));
        } else if (name.equals("TNOM")) {
            model.setTnom(parseDouble(value));
        } else if (name.equals("NT")) {
            model.setNt(parseDouble(value));
        } else if (name.equals("MU")) {
            model.setMu(parseDouble(value));
        }
    }

    @Override
    public IndInstance createInstance() {
        IndInstance instance = new IndInstance();
        initModelValues(instance);
        return instance;
    }
}
