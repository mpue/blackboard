/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.ResInstance;
import org.pmedv.blackboard.spice.sim.spice.model.discrete.ResModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public class ResModelCard extends ModelCard<ResInstance, ResModelValues> {

    public ResModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public void setProperty(ResModelValues instance, String key, String value) {
        if (key.equals("RSH")) {
            instance.setSheetRes(parseDouble(value));
        } else if (key.equals("NARROW")) {
            instance.setNarrow(parseDouble(value));
        } else if (key.equals("SHORT")) {
            instance.setShorter(parseDouble(value));
        } else if (key.equals("TC1")) {
            instance.setTc1(parseDouble(value));
        } else if (key.equals("TC2")) {
            instance.setTc2(parseDouble(value));
        } else if (key.equals("DEFW")) {
            instance.setWidth(parseDouble(value));
        } else if (key.equals("TNOM")) {
            instance.setTemp(parseDouble(value));
        }
    }

    @Override
    public ResInstance createInstance() {
        ResInstance instance = new ResInstance();
        initModelValues(instance);
        return instance;
    }
}
