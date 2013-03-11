/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcAM;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.AmSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class IAMCard extends AmSrcCard {

    public IAMCard(String cardString) throws ParserException {
        super(cardString);
        prefix="I";
    }

    @Override
    public ISrcAM createInstance(Deck deck) {
        ISrcAM instance = new ISrcAM();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(ISrcAM instance) {
        instance.setOffset(parseDouble(offset));
        instance.setAmplitude(parseDouble(amplitude));
        instance.setCarrierFreq(parseDouble(carrierFreq));
        instance.setModulFreq(parseDouble(modulFreq));
        instance.setDelayTime(parseDouble(delayTime));
    }
}
