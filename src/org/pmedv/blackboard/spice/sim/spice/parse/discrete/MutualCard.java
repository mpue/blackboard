/*
 * MutualCard.java
 * 
 * Created on Oct 15, 2007, 1:29:33 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.MutInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.util.StandardLog;

/**
 * @author Kristopher T. Beck
 */
public class MutualCard extends InstanceCard {

    private String ind1Name;
    private String ind2Name;
    private String coupling;

    public MutualCard() {
        nodeCount = 2;
        prefix = "K";
    }

    public MutualCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public MutInstance createInstance(Deck deck) {
        MutInstance instance = new MutInstance();
        instance.setInstName(instName);
        if (deck.getInstanceCard(ind1Name) == null) {
            StandardLog.severe(instance.getInstName() + ": coupling to non-existant"
                    + " inductor " + ind1Name);
            return null;
        } else {
            instance.setInd1Name(ind1Name);
        }
        if (deck.getInstanceCard(ind2Name) == null) {
            StandardLog.severe(instance.getInstName() + ": coupling to"
                    + " non-existant inductor " + ind2Name);
            return null;
        } else {
            instance.setInd2Name(ind2Name);
        }
        instance.setCoupling(parseDouble(coupling));
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        ind1Name = elements[1];
        ind2Name = elements[2];
        coupling = elements[3];

    }

    @Override
    protected void initNodes() {
    }
}
