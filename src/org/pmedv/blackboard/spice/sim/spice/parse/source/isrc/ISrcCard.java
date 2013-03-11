/*
 * ISrc.java
 * 
 * Created on Oct 15, 2007, 1:31:05 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.IndependentSourceCard;

/**
 * 
 * @author Kristopher T. Beck
 */
public class ISrcCard extends IndependentSourceCard {

    public ISrcCard() {
        nodeCount = 2;
        prefix = "I";
    }

    public ISrcCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public ISrcInstance createInstance(Deck deck) {
        ISrcInstance instance = new ISrcInstance();
        initSrcValues(instance, deck);
        return instance;
    }
}
