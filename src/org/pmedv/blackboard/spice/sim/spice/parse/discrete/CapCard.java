/*
 * CapCard.java
 * 
 * Created on Oct 14, 2007, 1:58:06 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.CapInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
/**
 * @author Kristopher T. Beck
 */
public class CapCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String accap;
    private String length;
    private String width;
    private String temp;
    private String dtemp;
    private String m;
    private String scale;
    private String ic;

    public CapCard() {
        nodeCount = 2;
        prefix = "C";
    }

    public CapCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getAccap() {
        return accap;
    }

    public void setAccap(String accap) {
        this.accap = accap;
    }

    public String getDtemp() {
        return dtemp;
    }

    public void setDtemp(String dtemp) {
        this.dtemp = dtemp;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
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

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public CapInstance createInstance(Deck deck) {
        CapInstance instance = null;
        if (modelName != null && !modelName.isEmpty()) {
            instance = (CapInstance) deck.getModelCard(modelName).createInstance();
        } else {
            instance = new CapInstance();
        }
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        if (accap != null) {
            instance.setCapAC(parseDouble(accap));
        }
        if (length != null) {
            instance.setLength(parseDouble(length));
        }
        if (width != null) {
            instance.setWidth(parseDouble(width));
        }
        if (temp != null) {
            instance.setTemp(parseDouble(temp));
        }
        if (dtemp != null) {
            instance.setDTemp(parseDouble(dtemp));
        }
        if (m != null) {
            instance.setM(parseDouble(m));
        }
        if (scale != null) {
            instance.setScale(parseDouble(scale));
        }
        if (ic != null) {
            instance.setInitCond(parseDouble(ic));
        }
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        if (elements[3].startsWith(prefix)) {
            modelName = elements[3];
        } else {
            accap = elements[3];
        }
        if (elements.length > 4) {
            for (int i = 4; i < elements.length; i++) {
                if (elements[i].startsWith(prefix)) {
                    modelName = elements[i];
                } else if (elements[i].equals("L")) {
                    length = elements[++i];
                } else if (elements[i].equals("W")) {
                    width = elements[++i];
                } else if (elements[i].equals("TEMP")) {
                    temp = elements[++i];
                } else if (elements[i].equals("DTEMP")) {
                    dtemp = elements[++i];
                } else if (elements[i].equals("M")) {
                    m = elements[++i];
                } else if (elements[i].equals("SCALE")) {
                    scale = elements[++i];
                } else if (elements[i].equals("IC")) {
                    ic = elements[++i];
                }
            }
        }
    }

    @Override
    protected void initNodes() {
        nodes[0] = posNodeName;
        nodes[1] = negNodeName;
    }
}
