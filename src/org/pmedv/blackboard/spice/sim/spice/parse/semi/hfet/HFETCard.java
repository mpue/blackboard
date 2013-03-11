/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.hfet;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.hfet.HFETValues;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class HFETCard extends InstanceCard {

    private String drnNodeName;
    private String gateNodeName;
    private String srcNodeName;
    private String icvds;
    private String icvgs;
    private String temp;

    public HFETCard() {
        nodeCount = 3;
        //TODO find prefix for het
        prefix = "";
    }

    public HFETCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
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

    public String getSrcNodeName() {
        return srcNodeName;
    }

    public void setSrcNodeName(String srcNodeName) {
        this.srcNodeName = srcNodeName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @Override
    public Instance createInstance(Deck deck) {
        HFETValues instance = (HFETValues) deck.getModelCard(modelName).createInstance();
        instance.setInstName(instName);
        instance.setDrnIndex(deck.getNodeIndex(drnNodeName));
        instance.setGateIndex(deck.getNodeIndex(gateNodeName));
        instance.setSrcIndex(deck.getNodeIndex(srcNodeName));
        if (icvds != null) {
            instance.setIcVDS(parseDouble(icvds));
            instance.setIcVGS(parseDouble(icvgs));
        } else if (temp != null) {
            instance.setTemp(parseDouble(temp));
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
                if (elements[i].equals("IC")) {
                    icvds = elements[++i];
                    icvgs = elements[++i];
                } else if (elements[i].equals("TEMP")) {
                    temp = elements[++i];
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
