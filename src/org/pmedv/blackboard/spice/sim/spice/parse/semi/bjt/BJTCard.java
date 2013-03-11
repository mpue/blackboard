/*
 * BJTCard.java
 * 
 * Created on Oct 14, 2007, 2:03:39 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.bjt.BJTValues;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 * @author Kristopher T. Beck
 */
public class BJTCard extends InstanceCard {

    private String colNodeName;
    private String baseNodeName;
    private String emitNodeName;
    private String substNodeName;
    private String area;
    private String areab;
    private String areac;
    private String off;
    private String m;
    private String icvce;
    private String icvbe;
    private String temp;
    private String dtemp;

    public BJTCard() {
        nodeCount = 3;
        prefix = "Q";
    }

    public BJTCard(String cardString) throws ParserException {
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

    public String getAreab() {
        return areab;
    }

    public void setAreab(String areab) {
        this.areab = areab;
    }

    public String getAreac() {
        return areac;
    }

    public void setAreac(String areac) {
        this.areac = areac;
    }

    public String getBaseNodeName() {
        return baseNodeName;
    }

    public void setBaseNodeName(String baseNodeName) {
        this.baseNodeName = baseNodeName;
    }

    public String getColNodeName() {
        return colNodeName;
    }

    public void setColNodeName(String colNodeName) {
        this.colNodeName = colNodeName;
    }

    public String getDtemp() {
        return dtemp;
    }

    public void setDtemp(String dtemp) {
        this.dtemp = dtemp;
    }

    public String getEmitNodeName() {
        return emitNodeName;
    }

    public void setEmitNodeName(String emitNodeName) {
        this.emitNodeName = emitNodeName;
    }

    public String getIcvbe() {
        return icvbe;
    }

    public void setIcvbe(String icvbe) {
        this.icvbe = icvbe;
    }

    public String getIcvce() {
        return icvce;
    }

    public void setIcvce(String icvce) {
        this.icvce = icvce;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getSubstNodeName() {
        return substNodeName;
    }

    public void setSubstNodeName(String substNodeName) {
        this.substNodeName = substNodeName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void parse(String cardString) {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        colNodeName = elements[1];
        baseNodeName = elements[2];
        emitNodeName = elements[3];
        modelName = elements[4];
        
        // TODO : Substrate node is currently not supported  
        
// TODO : WTF???        
//        int i = 5;
//        if (elements[4].startsWith(prefix)) {
//            nodeCount = 3;
//            modelName = elements[4];
//        } else {
//            i = 6;
//            nodeCount = 4;
//            substNodeName = elements[4];
//            modelName = elements[5];
//        }
//        for (; i < elements.length; i++) {
//            if (elements[i].equals("AREA")) {
//                area = elements[++i];
//            } else if (elements[i].equals("AREAB")) {
//                areab = elements[++i];
//            } else if (elements[i].equals("AREAC")) {
//                areac = elements[++i];
//            } else if (elements[i].equals("OFF")) {
//                off = "true";
//            } else if (elements[i].equals("M")) {
//                m = elements[++i];
//            } else if (elements[i].equals("IC")) {
//                icvbe = elements[++i];
//                icvce = elements[++i];
//            } else if (elements[i].equals("TEMP")) {
//                temp = elements[++i];
//            } else if (elements[i].equals("DTEMP")) {
//                dtemp = elements[++i];
//            }
//        }
    }

    @Override
    public Instance createInstance(Deck deck) {
        ModelCard mcard = deck.getModelCard(modelName);
        BJTValues instance = (BJTValues) mcard.createInstance();
        instance.setInstName(instName);
        instance.setColIndex(deck.getNodeIndex(colNodeName));
        instance.setBaseIndex(deck.getNodeIndex(baseNodeName));
        instance.setEmitIndex(deck.getNodeIndex(emitNodeName));
        //? sub ?
        if (area != null) {
            instance.setArea(parseDouble(area));
        }
        if (areab != null) {
            instance.setAreab(parseDouble(areab));
        }
        if (areac != null) {
            instance.setAreac(parseDouble(areac));
        }
        if (off != null && off.equals("true")) {
            instance.setOff(true);
        }
        if (m != null) {
            instance.setM(parseDouble(m));
        }
        if (icvbe != null && icvce != null) {
            instance.setIcVBE(parseDouble(icvbe));
            instance.setIcVCE(parseDouble(icvce));
        }
        if (temp != null) {
            instance.setTemp(parseDouble(temp));
        }
        if (dtemp != null) {
            instance.setDtemp(parseDouble(dtemp));
        }
        return (Instance) instance;
    }

    @Override
    protected void initNodes() {
        nodes[0] = colNodeName;
        nodes[1] = baseNodeName;
        nodes[2] = emitNodeName;
        if (nodeCount == 4) {
            nodes[3] = substNodeName;
        }
    }
}
