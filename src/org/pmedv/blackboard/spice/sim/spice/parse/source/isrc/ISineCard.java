/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcSine;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.SineSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISineCard extends SineSrcCard {

    public ISineCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "I";
    }

    @Override
    public ISrcSine createInstance(Deck deck) {
        ISrcSine instance = new ISrcSine();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(ISrcSine instance) {
        instance.setOffset(parseDouble(offset));
        instance.setAmplitude(parseDouble(amplitude));
        instance.setFrequency(parseDouble(freq));
        instance.setDelayTime(parseDouble(delayTime));
        instance.setTheta(parseDouble(theta));
    }
}
