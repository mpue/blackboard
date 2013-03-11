/*
 * ICVSCard.java
 * 
 * Created on Oct 14, 2007, 2:02:13 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.ICVSInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.SourceCard;

/**
 * @author Kristopher T. Beck
 */
public class ICVSCard extends SourceCard {

    private String contName;
    private String coeff;

    public ICVSCard(String cardString) throws ParserException {
        super(cardString);
        nodeCount = 2;
        prefix = "H";
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

    public ICVSInstance createInstance(Deck deck) {
        ICVSInstance instance = new ICVSInstance();
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
}
