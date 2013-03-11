/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcExp;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.ExpSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class IExpCard extends ExpSrcCard {

    public IExpCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "I";
    }

    @Override
    public ISrcExp createInstance(Deck deck) {
        ISrcExp instance = new ISrcExp();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(ISrcExp instance) {
        instance.setInitValue(parseDouble(initValue));
        instance.setPulsedValue(parseDouble(pulsedValue));
        instance.setRiseDelay(parseDouble(riseDelay));
        instance.setRiseConst(parseDouble(riseConst));
        instance.setFallDelay(parseDouble(fallDelay));
        instance.setFallConst(parseDouble(fallConst));
    }
}
