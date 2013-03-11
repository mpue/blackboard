/*
 * VSrcCard.java
 * 
 * Created on Oct 31, 2007, 7:52:17 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.IndependentSourceCard;

/**
 * @author Kristopher T. Beck
 */
public class VSrcCard extends IndependentSourceCard {

    public VSrcCard() {
        nodeCount = 2;
        prefix = "V";
    }

    public VSrcCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public VSrcInstance createInstance(Deck deck) {
        VSrcInstance instance = new VSrcInstance();
        initSrcValues(instance, deck);
        return instance;
    }
}
