/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcAM;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.AmSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class VAMCard extends AmSrcCard {

    public VAMCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "V";
    }

    @Override
    public VSrcAM createInstance(Deck deck) {
        VSrcAM instance = new VSrcAM();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(VSrcAM instance) {
        instance.setOffset(parseDouble(offset));
        instance.setAmplitude(parseDouble(amplitude));
        instance.setCarrierFreq(parseDouble(carrierFreq));
        instance.setModulFreq(parseDouble(modulFreq));
        instance.setDelayTime(parseDouble(delayTime));
    }
}
