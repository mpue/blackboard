/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;


/**
 *
 * @author owenbad
 */
public abstract class AmSrcCard extends IndependentSourceCard {

    protected String offset;
    protected String amplitude;
    protected String carrierFreq;
    protected String modulFreq;
    protected String delayTime;
    public static final String AM_STRING = "AM\\s*\\(\\s*"
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING
            + "\\s+" + DOUBLE_VALUE_STRING + "\\s+"
            + DOUBLE_VALUE_STRING + "(\\s+" + DOUBLE_VALUE_STRING
            + ")?\\s*" + "\\)";
    public static final Pattern AM_PATTERN = Pattern.compile(AM_STRING);

    public AmSrcCard(String cardString) throws ParserException {
        super(cardString);
    }

    public String getModulFreq() {
        return modulFreq;
    }

    public void setModulFreq(String ModulFreq) {
        this.modulFreq = ModulFreq;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public String getCarrierFreq() {
        return carrierFreq;
    }

    public void setCarrierFreq(String carrierFreq) {
        this.carrierFreq = carrierFreq;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = AM_PATTERN.matcher(cardString);
        if (m.find()) {
            System.out.println("AM Src");
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println("group " + i + " = " + m.group(i));
            }
            offset = m.group(1);
            amplitude = m.group(2);
            carrierFreq = m.group(3);
            modulFreq = m.group(4);
            delayTime = m.group(5);
            super.parse(cardString.substring(0, m.start() - 1) + cardString.substring(m.end()));
        } else {
            throw new ParserException();
        }
    }
}
