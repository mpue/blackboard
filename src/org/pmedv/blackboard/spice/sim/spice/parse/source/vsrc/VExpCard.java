/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcExp;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.ExpSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class VExpCard extends ExpSrcCard {

    public VExpCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "V";
    }

    @Override
    public VSrcExp createInstance(Deck deck) {
        VSrcExp instance = new VSrcExp();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(VSrcExp instance) {
        instance.setInitValue(parseDouble(initValue));
        instance.setPulsedValue(parseDouble(pulsedValue));
        instance.setRiseDelay(parseDouble(riseDelay));
        instance.setRiseConst(parseDouble(riseConst));
        instance.setFallDelay(parseDouble(fallDelay));
        instance.setFallConst(parseDouble(fallConst));
    }
}
