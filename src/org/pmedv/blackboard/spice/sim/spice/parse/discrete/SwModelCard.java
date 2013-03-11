/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.VSwitchInstance;
import org.pmedv.blackboard.spice.sim.spice.model.discrete.VSwitchModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public class SwModelCard extends ModelCard<VSwitchInstance, VSwitchModelValues> {

    public SwModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public VSwitchInstance createInstance() {
        VSwitchInstance instance = new VSwitchInstance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(VSwitchModelValues model, String name, String value) {
        if (name.equals("VT")) {
            model.setvThreshold(parseDouble(value));
        } else if (name.equals("VH")) {
            model.setvHysteresis(parseDouble(value));
        } else if (name.equals("RON")) {
            model.setOnResist(parseDouble(value));
        } else if (name.equals("ROFF")) {
            model.setOffResist(parseDouble(value));
        }
    }
}
