/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcSFFM;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.SffmSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class ISFFMCard extends SffmSrcCard {

    public ISFFMCard(String cardString) throws ParserException {
        super(cardString);
        prefix = "I";
    }

    @Override
    public ISrcSFFM createInstance(Deck deck) {
        ISrcSFFM instance = new ISrcSFFM();
        initAuxValues(instance);
        initSrcValues(instance, deck);
        return instance;
    }

    protected void initAuxValues(ISrcSFFM instance) {
        instance.setOffset(parseDouble(offset));
        instance.setAmplitude(parseDouble(amplitude));
        instance.setCarrierFreq(parseDouble(carrierFreq));
        instance.setModulIndex(parseDouble(modulationIndex));
        instance.setSignalFreq(parseDouble(signalFreq));
    }
}
