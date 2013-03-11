/*
 * MOSCard.java
 * 
 * Created on Oct 14, 2007, 2:04:21 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mos;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.MOSModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 * @author Kristopher T. Beck
 */
public class MOSCard extends InstanceCard {

    private String drnNodeName;
    private String gateNodeName;
    private String srcNodeName;
    private String blkNodeName;
    private String icvds;
    private String icvgs;
    private String icvbs;
    private String m;
    private String length;
    private String width;
    private String areaDrain;
    private String areaSource;
    private String drnPerimeter;
    private String srcPerimeter;
    private String drnSquares;
    private String srcSquares;
    private String temp;
    private String off;

    public MOSCard() {
        nodeCount = 3;
        prefix = "M";
    }

    public MOSCard(String cardString) throws ParserException {
        this();
        this.cardString = cardString;
        parse(cardString);
    }

    public String getAreaDrain() {
        return areaDrain;
    }

    public void setAreaDrain(String areaDrain) {
        this.areaDrain = areaDrain;
    }

    public String getAreaSource() {
        return areaSource;
    }

    public void setAreaSource(String areaSource) {
        this.areaSource = areaSource;
    }

    public String getBlkNodeName() {
        return blkNodeName;
    }

    public void setBlkNodeName(String blkNodeName) {
        this.blkNodeName = blkNodeName;
    }

    public String getDrnNodeName() {
        return drnNodeName;
    }

    public void setDrnNodeName(String drnNodeName) {
        this.drnNodeName = drnNodeName;
    }

    public String getDrnPerimeter() {
        return drnPerimeter;
    }

    public void setDrnPerimeter(String drnPerimeter) {
        this.drnPerimeter = drnPerimeter;
    }

    public String getDrnSquares() {
        return drnSquares;
    }

    public void setDrnSquares(String drnSquares) {
        this.drnSquares = drnSquares;
    }

    public String getGateNodeName() {
        return gateNodeName;
    }

    public void setGateNodeName(String gateNodeName) {
        this.gateNodeName = gateNodeName;
    }

    public String getIcvbs() {
        return icvbs;
    }

    public void setIcvbs(String icvbs) {
        this.icvbs = icvbs;
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

    public String getSrcPerimeter() {
        return srcPerimeter;
    }

    public void setSrcPerimeter(String srcPerimeter) {
        this.srcPerimeter = srcPerimeter;
    }

    public String getSrcSquares() {
        return srcSquares;
    }

    public void setSrcSquares(String srcSquares) {
        this.srcSquares = srcSquares;
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

    @Override
    public Instance createInstance(Deck deck) {
        ModelCard card = deck.getModelCard(modelName);
        MOSModelValues instance = (MOSModelValues) card.createInstance();
        instance.setInstName(instName);
        instance.setDrnIndex(deck.getNodeIndex(drnNodeName));
        instance.setGateIndex(deck.getNodeIndex(gateNodeName));
        instance.setSrcIndex(deck.getNodeIndex(srcNodeName));
        instance.setBlkIndex(deck.getNodeIndex(blkNodeName));
        if (icvds != null) {
            instance.setIcVDS(parseDouble(icvds));
            instance.setIcVGS(parseDouble(icvgs));
            instance.setIcVBS(parseDouble(icvbs));
        }
        if (m != null) {
            instance.setM(parseDouble(m));
        }
        if (length != null) {
            instance.setL(parseDouble(length));
        }
        if (width != null) {
            instance.setW(parseDouble(width));
        }
        if (areaDrain != null) {
            instance.setDrnArea(parseDouble(areaDrain));
        }
        if (areaSource != null) {
            instance.setSrcArea(parseDouble(areaSource));
        }
        if (drnPerimeter != null) {
            instance.setDrnPerimeter(parseDouble(drnPerimeter));
        }
        if (srcPerimeter != null) {
            instance.setSrcPerimeter(parseDouble(srcPerimeter));
        }
        if (drnSquares != null) {
            instance.setDrnSquares(parseDouble(drnSquares));
        }
        if (srcSquares != null) {
            instance.setSrcSquares(parseDouble(srcSquares));
        }
        if (temp != null) {
            instance.setTemp(parseDouble(temp));
        }
        if (off != null) {
            instance.setOff(Boolean.parseBoolean("TRUE"));
        }
        return (Instance) instance;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        String[] elements=cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        drnNodeName = elements[1];
        gateNodeName = elements[2];
        srcNodeName = elements[3];
        blkNodeName = elements[4];
        modelName = elements[5];
        for (int i = 6; i < elements.length; i++) {
            String id =elements[i];
            if (id.equals("IC")) {
                icvds = elements[++i];
                icvgs = elements[++i];
                icvbs = elements[++i];
            } else if (id.equals("M")) {
                m = elements[++i];
            } else if (id.equals("L")) {
                length = elements[++i];
            } else if (id.equals("W")) {
                width = elements[++i];
            } else if (id.equals("AD")) {
                areaDrain = elements[++i];
            } else if (id.equals("AS")) {
                areaSource = elements[++i];
            } else if (id.equals("PD")) {
                drnPerimeter = elements[++i];
            } else if (id.equals("PS")) {
                srcNodeName = elements[++i];
            } else if (id.equals("NRD")) {
                drnSquares = elements[++i];
            } else if (id.equals("NRS")) {
                srcSquares = elements[++i];
            } else if (id.equals("TEMP")) {
                temp = elements[++i];
            } else if (id.equals("OFF")) {
                off = "TRUE";
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
