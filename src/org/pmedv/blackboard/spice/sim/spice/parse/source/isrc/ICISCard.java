/*
 * ICISCard.java
 * 
 * Created on Oct 14, 2007, 2:01:50 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ICISInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.SourceCard;

/**
 * 
 * @author Kristopher T. Beck
 */
public class ICISCard extends SourceCard {

    private String contName;
    private String coeff;

    public ICISCard() {
        nodeCount = 4;
        prefix = "F";
    }

    public ICISCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getCoeff() {
        return coeff;
    }

    public void setCoeff(String coeff) {
        this.coeff = coeff;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public ICISInstance createInstance(Deck deck) {
        ICISInstance instance = new ICISInstance();
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        instance.setContName(contName);
        instance.setCoeff(parseDouble(coeff));
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        contName = elements[3];
        coeff = elements[4];
    }

    @Override
    protected void initNodes() {
        nodes[0] = posNodeName;
        nodes[1] = negNodeName;
    }
}
