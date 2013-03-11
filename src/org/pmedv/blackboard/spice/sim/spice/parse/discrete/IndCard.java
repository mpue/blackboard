/*
 * IndCard.java
 * 
 * Created on Oct 14, 2007, 1:59:49 PM
 * 
 * To change this template, choose Tools Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.discrete;

import org.pmedv.blackboard.spice.sim.spice.model.discrete.IndInstance;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/*
 * @author Kristopher T. Beck
 */
public class IndCard extends InstanceCard {

    private String posNodeName;
    private String negNodeName;
    private String induct;
    private String turns;
    private String m;
    private String scale;
    private String temp;
    private String dtemp;
    private String ic;

    public IndCard() {
        nodeCount = 2;
        prefix = "L";
    }

    public IndCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
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

    public String getInduct() {
        return induct;
    }

    public void setInduct(String induct) {
        this.induct = induct;
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

    public String getTurns() {
        return turns;
    }

    public void setTurns(String turns) {
        this.turns = turns;
    }

    public IndInstance createInstance(Deck deck) {
        IndInstance instance = null;
        if (modelName != null) {
            instance = (IndInstance) deck.getModelCard(modelName).createInstance();
        } else {
            instance = new IndInstance();
            instance.setInduct(parseDouble(induct));
        }
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        if (turns != null) {
            instance.setNt(parseDouble(turns));
        }
        if (m != null) {
            instance.setM(parseDouble(m));
        }
        if (scale != null) {
            instance.setScale(parseDouble(scale));
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
        return instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        String str = elements[3];
        if (str.startsWith(prefix)) {
            modelName = str;
        } else {
            induct = str;
        }
        if (elements.length > 4) {
            for (int i = 4; i < elements.length; i++) {
                if (elements[i].startsWith(prefix)) {
                    modelName = elements[i];
                } else if (elements[i].equals("NT")) {
                    turns = elements[++i];
                } else if (elements[i].equals("M")) {
                    m = elements[++i];
                } else if (elements[i].equals("SCALE")) {
                    scale = elements[++i];
                } else if (elements[i].equals("TEMP")) {
                    temp = elements[++i];
                } else if (elements[i].equals("DTEMP")) {
                    dtemp = elements[++i];
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
