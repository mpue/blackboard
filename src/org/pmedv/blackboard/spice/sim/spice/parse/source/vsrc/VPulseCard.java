/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcPulse;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.PulseSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class VPulseCard extends PulseSrcCard {

    public VPulseCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "V";
    }

    @Override
    public VSrcPulse createInstance(Deck deck) {
        VSrcPulse instance = new VSrcPulse();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(VSrcPulse instance) {
        instance.setInitValue(parseDouble(initValue));
        instance.setPulsedValue(parseDouble(pulsedValue));
        instance.setDelayTime(parseDouble(delayTime));
        instance.setRiseTime(parseDouble(riseTime));
        instance.setFallTime(parseDouble(fallTime));
        instance.setPulseWidth(parseDouble(pulseWidth));
        instance.setPeriod(parseDouble(period));
    }
}
