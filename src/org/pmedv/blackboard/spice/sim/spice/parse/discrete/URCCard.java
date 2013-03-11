/*
 * URCCard.java
 * 
 * Created on Oct 15, 2007, 1:26:43 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.txline.URCInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 * @author Kristopher T. Beck
 */
public class URCCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String gndNodeName;
    private String length;
    private String lumps;

    public URCCard() {
        nodeCount = 3;
        prefix = "U";
    }

    public URCCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getGndNodeName() {
        return gndNodeName;
    }

    public void setGndNodeName(String gndNodeName) {
        this.gndNodeName = gndNodeName;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLumps() {
        return lumps;
    }

    public void setLumps(String lumps) {
        this.lumps = lumps;
    }

    public String getNegNodeName() {
        return negNodeName;
    }

    public void setNegNodeName(String negNodeName) {
        this.negNodeName = negNodeName;
    }

    public String getPosNodeName() {
        return posNodeName;
    }

    public void setPosNodeName(String posNodeName) {
        this.posNodeName = posNodeName;
    }

    public URCInstance createInstance(Deck deck) {
        URCInstance instance = (URCInstance) deck.getModelCard(modelName).createInstance();
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        instance.setGndIndex(deck.getNodeIndex(gndNodeName));
        instance.setLength(parseDouble(length));
        if (lumps != null) {
            instance.setLumps(Integer.parseInt(lumps));
        }
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        gndNodeName = elements[3];
        modelName = elements[4];
        length = elements[6];
        if (elements.length > 7 && elements[7].equals("N")) {
            lumps = elements[8];
        }
    }

    @Override
    protected void initNodes() {
        nodes[0] = posNodeName;
        nodes[1] = negNodeName;
        nodes[2] = gndNodeName;
    }
}
