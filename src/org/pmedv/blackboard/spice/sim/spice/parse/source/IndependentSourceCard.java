/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source;

import org.pmedv.blackboard.spice.sim.spice.model.sources.IndependentLinearSource;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public abstract class IndependentSourceCard extends SourceCard {

    public String dc;
    public String acmag;
    public String acphase;
    public String df1mag;
    public String df1phase;
    public String df2mag;
    public String df2phase;

    public IndependentSourceCard() {
    }

    public IndependentSourceCard(String cardString) throws ParserException {
        super(cardString);
    }

    public String getAcmag() {
        return acmag;
    }

    public void setAcmag(String acmag) {
        this.acmag = acmag;
    }

    public String getAcphase() {
        return acphase;
    }

    public void setAcphase(String acphase) {
        this.acphase = acphase;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getDf1mag() {
        return df1mag;
    }

    public void setDf1mag(String df1mag) {
        this.df1mag = df1mag;
    }

    public String getDf1phase() {
        return df1phase;
    }

    public void setDf1phase(String df1phase) {
        this.df1phase = df1phase;
    }

    public String getDf2mag() {
        return df2mag;
    }

    public void setDf2mag(String df2mag) {
        this.df2mag = df2mag;
    }

    public String getDf2phase() {
        return df2phase;
    }

    public void setDf2phase(String df2phase) {
        this.df2phase = df2phase;
    }

    protected void initSrcValues(IndependentLinearSource instance, Deck deck) {
        instance.setInstName(instName);
        instance.setPosIndex(deck.getNodeIndex(posNodeName));
        instance.setNegIndex(deck.getNodeIndex(negNodeName));
        if (dc != null) {
            instance.setDcValue(parseDouble(dc));
        }
        if (acmag != null) {
            instance.setAcMag(parseDouble(acmag));
            if (acphase != null) {
                instance.setAcPhase(parseDouble(acphase));
            }
        }
        if (df1mag != null) {
            instance.setdF1Mag(parseDouble(df1mag));
            if (df1phase != null) {
                instance.setdF1Phase(parseDouble(df1phase));
            }
        }
        if (df2mag != null) {
            instance.setdF2Mag(parseDouble(df2mag));
            if (df2phase != null) {
                instance.setdF2Phase(parseDouble(df2phase));
            }
        }
    }

    public void parse(String cardString) throws ParserException {
        String[] elements = cardString.replaceAll(",|=", " ").split(" ");
        instName = elements[0];
        posNodeName = elements[1];
        negNodeName = elements[2];
        int length = elements.length;
        int i = 3;
        if (i < length && isNumber(elements[i])) {
            dc = elements[i++];
        }

        for (; i < length; i++) {
            if (elements[i].equals("DC")) {
                dc = elements[++i];
            } else if (elements[i].equals("AC")) {
                if (i + 1 < length && isNumber(elements[i + 1])) {
                    acmag = elements[++i];
                    if (i + 1 < length && isNumber(elements[i + 1])) {
                        acphase = elements[++i];
                    }
                }
            } else if (elements[i].equals("DISTOF1")) {
                if (i + 1 < length && isNumber(elements[i + 1])) {
                    df1mag = elements[++i];
                    if (i + 1 < length && isNumber(elements[i + 1])) {
                        df1phase = elements[++i];
                    }
                }
            } else if (elements[i].equals("DISTOF2")) {
                if (i + 1 < length && isNumber(elements[i + 1])) {
                    df2mag = elements[++i];
                    if (i + 1 < length && isNumber(elements[i + 1])) {
                        df2phase = elements[++i];
                    }
                }
            }
        }
    }
}
