/*
 * ResCard.java
 * 
 * Created on Oct 14, 2007, 1:54:27 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.ResInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class ResCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String resist;
    private String length;
    private String width;
    private String temp;
    private String dtemp;
    private String m;
    private String scale;
    private String ac;
    private boolean noisy;

    public ResCard() {
        nodeCount = 2;
        prefix = "R";
    }

    public ResCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getDtemp() {
        return dtemp;
    }

    public void setDtemp(String dtemp) {
        this.dtemp = dtemp;
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

    public boolean isNoisy() {
        return noisy;
    }

    public void setNoisy(boolean noisy) {
        this.noisy = noisy;
    }

    public String getPosNodeName() {
        return posNodeName;
    }

    public void setPosNodeName(String posNodeName) {
        this.posNodeName = posNodeName;
    }

    public String getResist() {
        return resist;
    }

    public void setResist(String resist) {
        this.resist = resist;
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

    public ResInstance createInstance(Deck deck) {
        ResInstance instance = null;
        if (modelName != null) {
            ResModelCard card = (ResModelCard) deck.getModelCard(modelName);
            instance = card.createInstance();
        } else {
            instance = new ResInstance();
            instance.setResist(parseDouble(resist));
        }
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        if (length != null) {
            instance.setLength(parseDouble(length));
        }
        if (width != null) {
            instance.setWidth(parseDouble(width));
        }
        if (temp != null) {
            instance.setWidth(parseDouble(temp));
        }
        if (dtemp != null) {
            instance.setdTemp(parseDouble(dtemp));
        }
        if (m != null) {
            instance.setM(parseDouble(m));
        }
        if (scale != null) {
            instance.setScale(parseDouble(scale));
        }
        if (ac != null) {
            instance.setAcResist(parseDouble(ac));
        }
        instance.setNoisy(noisy);
        return instance;
    }

    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        String str = elements[3];
        if (str.startsWith(prefix)) {
            modelName = str;
        } else {
            resist = str;
        }
        if (elements.length > 4) {
            for (int i = 4; i < elements.length; i++) {
                if (elements[i].startsWith(prefix)) {
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
                } else if (elements[i].equals("AC")) {
                    ac = elements[++i];
                } else if (elements[i].equals("NOISY")) {
                    noisy = elements[++i].equals("1") ? true : false;
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
