/*
 * MESFETCard.java
 * 
 * Created on Oct 15, 2007, 2:05:02 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet.MESFETValues;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 * @author Kristopher T. Beck
 */
public class MESFETCard extends InstanceCard {

    private String drnNodeName;
    private String gateNodeName;
    private String srcNodeName;
    private String area;
    private String icvds;
    private String icvgs;
    private String off;

    public MESFETCard() {
        nodeCount = 3;
        prefix = "Z";
    }

    public MESFETCard(String cardString) throws ParserException {
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

    public String getDrnNodeName() {
        return drnNodeName;
    }

    public void setDrnNodeName(String drnNodeName) {
        this.drnNodeName = drnNodeName;
    }

    public String getGateNodeName() {
        return gateNodeName;
    }

    public void setGateNodeName(String gateNodeName) {
        this.gateNodeName = gateNodeName;
    }

    public String getIcvds() {
        return icvds;
    }

    public void setIcvds(String icvds) {
        this.icvds = icvds;
    }

    public String getIcvgs() {
        return icvgs;
    }

    public void setIcvgs(String icvgs) {
        this.icvgs = icvgs;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getSrcNodeName() {
        return srcNodeName;
    }

    public void setSrcNodeName(String srcNodeName) {
        this.srcNodeName = srcNodeName;
    }

    @Override
    public Instance createInstance(Deck deck) {
        MESFETValues instance = (MESFETValues) deck.getModelCard(modelName).createInstance();
        instance.setInstName(instName);
        instance.setDrnIndex(deck.getNodeIndex(drnNodeName));
        instance.setGateIndex(deck.getNodeIndex(gateNodeName));
        instance.setSrcIndex(deck.getNodeIndex(srcNodeName));
        if (area != null) {
            instance.setArea(parseDouble(area));
        }
        if (icvds != null) {
            instance.setIcVDS(parseDouble(icvds));
            instance.setIcVGS(parseDouble(icvgs));
        }
        if (off != null && off.equals("true")) {
            instance.setOff(true);
        }
        return (Instance) instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        drnNodeName = elements[1];
        gateNodeName = elements[2];
        srcNodeName = elements[3];
        modelName = elements[4];
        if (elements.length > 5) {
            for (int i = 5; i < elements.length; i++) {
                if (elements[i].equals("AREA")) {
                    area = elements[++i];
                } else if (elements[i].equals("IC")) {
                    icvds = elements[++i];
                    icvgs = elements[++i];
                } else if (elements[i].equals("OFF")) {
                    off = "true";
                }
            }
        }
    }

    @Override
    protected void initNodes() {
        nodes[0] = drnNodeName;
        nodes[1] = gateNodeName;
        nodes[2] = srcNodeName;
    }
}
