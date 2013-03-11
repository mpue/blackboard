/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcPulse;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.PulseSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class IPulseCard extends PulseSrcCard {

    public IPulseCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "I";
    }

    @Override
    public ISrcPulse createInstance(Deck deck) {
        ISrcPulse instance = new ISrcPulse();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(ISrcPulse instance) {
        instance.setInitValue(parseDouble(initValue));
        instance.setPulsedValue(parseDouble(pulsedValue));
        instance.setDelayTime(parseDouble(delayTime));
        instance.setRiseTime(parseDouble(riseTime));
        instance.setFallTime(parseDouble(fallTime));
        instance.setPulseWidth(parseDouble(pulseWidth));
        instance.setPeriod(parseDouble(period));
    }
}
