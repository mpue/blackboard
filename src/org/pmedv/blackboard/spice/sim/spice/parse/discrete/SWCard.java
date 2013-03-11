/*
 * SWCard.java
 * 
 * Created on Oct 14, 2007, 2:04:47 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.VSwitchInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class SWCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String posContNodeName;
    private String negContNodeName;
    private String on;

    public SWCard() {
        nodeCount = 4;
        prefix = "S";
    }

    public SWCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getNegContNodeName() {
        return negContNodeName;
    }

    public void setNegContNodeName(String negContNodeName) {
        this.negContNodeName = negContNodeName;
    }

    public String getNegNodeName() {
        return negNodeName;
    }

    public void setNegNodeName(String negNodeName) {
        this.negNodeName = negNodeName;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getPosContNodeName() {
        return posContNodeName;
    }

    public void setPosContNodeName(String posContNodeName) {
        this.posContNodeName = posContNodeName;
    }

    public String getPosNodeName() {
        return posNodeName;
    }

    public void setPosNodeName(String posNodeName) {
        this.posNodeName = posNodeName;
    }

    public VSwitchInstance createInstance(Deck deck) {
        VSwitchInstance instance = (VSwitchInstance) deck.getModelCard(modelName).createInstance();
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        instance.setPosCtrlIndex(deck.getNodeIndex(posContNodeName));
        instance.setNegCtrlIndex(deck.getNodeIndex(negContNodeName));
        if (on != null) {
            if (on.equals("true")) {
                instance.setIcStateOn(true);
            } else if (on.equals("false")) {
                instance.setIcStateOn(false);
            }
        }
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        posContNodeName = elements[3];
        negContNodeName = elements[4];
        modelName = elements[5];
        if (elements.length == 7) {
            if (elements[4].equals("ON")) {
                on = "true";
            } else if (elements[4].equals("OFF")) {
                on = "false";
            }
        }

    }

    @Override
    protected void initNodes() {
        nodes[0] = posNodeName;
        nodes[1] = negNodeName;
        nodes[2] = posContNodeName;
        nodes[3] = negContNodeName;
    }
}
