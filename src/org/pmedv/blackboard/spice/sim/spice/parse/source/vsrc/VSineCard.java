/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcSine;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.SineSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class VSineCard extends SineSrcCard {

    public VSineCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "V";
    }

    @Override
    public VSrcSine createInstance(Deck deck) {
        VSrcSine instance = new VSrcSine();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(VSrcSine instance) {
        instance.setOffset(parseDouble(offset));
        instance.setAmplitude(parseDouble(amplitude));
        instance.setFrequency(parseDouble(freq));
        instance.setDelayTime(parseDouble(delayTime));
        instance.setTheta(parseDouble(theta));
    }
}
