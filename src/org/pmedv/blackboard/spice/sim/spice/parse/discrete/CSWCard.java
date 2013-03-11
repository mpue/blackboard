/*
 * CSWCard.java
 * 
 * Created on Oct 14, 2007, 2:05:15 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.ISwitchInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/* *
 * @author Kristopher T. Beck
 */
public class CSWCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String contName;
    private boolean on;

    public CSWCard() {
        nodeCount = 2;
        prefix = "W";
    }

    public CSWCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getNegNodeName() {
        return negNodeName;
    }

    public void setNegNodeName(String negNodeName) {
        this.negNodeName = negNodeName;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String getPosNodeName() {
        return posNodeName;
    }

    public void setPosNodeName(String posNodeName) {
        this.posNodeName = posNodeName;
    }

    public ISwitchInstance createInstance(Deck deck) {
        CSWModelCard card = (CSWModelCard) deck.getModelCard(modelName);
        ISwitchInstance instance = card.createInstance();
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        instance.setContName(contName);
        instance.setICStateOn(on);
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        contName = elements[3];
        modelName = elements[4];
        if (elements.length == 6) {
            if (elements[5].equals("ON")) {
                on = true;
            } else if (elements[5].equals("OFF")) {
                on = false;
            }
        }
    }

    @Override
    protected void initNodes() {
        nodes[0]=posNodeName;
        nodes[1]=negNodeName;
    }
}
