/*
 * DiodeCard.java
 * 
 * Created on Oct 15, 2007, 1:04:44 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi;

import org.pmedv.blackboard.spice.sim.spice.model.semi.DiodeInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 * * @author Kristopher T. Beck
 */
public class DiodeCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String area;
    private String m;
    private String pj;
    private String temp;
    private String dtemp;
    private String ic;
    private String off;

    public DiodeCard() {
        nodeCount = 2;
        prefix = "D";
    }

    public DiodeCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getPj() {
        return pj;
    }

    public void setPj(String pj) {
        this.pj = pj;
    }

    public String getPosNodeName() {
        return posNodeName;
    }

    public void setPosNodeName(String posNodeName) {
        this.posNodeName = posNodeName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public DiodeInstance createInstance(Deck deck) {
        DiodeModelCard card = (DiodeModelCard) deck.getModelCard(modelName);
        DiodeInstance instance = card.createInstance();
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        if (area != null) {
            instance.setArea(parseDouble(area));
        }
        if (m != null) {
            instance.setM(parseDouble(m));
        }
        if (pj != null) {
            instance.setPj(parseDouble(pj));
        }
        if (temp != null) {
            instance.setTemp(parseDouble(temp));
        }
        if (dtemp != null) {
            instance.setDtemp(parseDouble(dtemp));
        }
        if (ic != null) {
            instance.setInitCond(parseDouble(ic));
        }
        if (off != null) {
            instance.setOff(true);
        }
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        modelName = elements[3];
        if (elements.length > 4) {
            for (int i = 4; i < elements.length; i++) {
                if (elements[i].equals("AREA")) {
                    area = elements[++i];
                } else if (elements[i].equals("M")) {
                    m = elements[++i];
                } else if (elements[i].equals("PJ")) {
                    pj = elements[++i];
                } else if (elements[i].equals("TEMP")) {
                    temp = elements[++i];
                } else if (elements[i].equals("DTEMP")) {
                    dtemp = elements[++i];
                } else if (elements[i].equals("IC")) {
                    ic = elements[++i];
                } else if (elements[i].equals("OFF")) {
                    off = "true";
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
