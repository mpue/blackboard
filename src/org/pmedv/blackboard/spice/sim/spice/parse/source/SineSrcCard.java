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
public abstract class SineSrcCard extends IndependentSourceCard {

    protected String offset;
    protected String amplitude;
    protected String freq;
    protected String delayTime;
    protected String theta;
    public static final String SINE_STRING = "SIN\\s*\\(\\s*"
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING
            + "(\\s+" + DOUBLE_VALUE_STRING + "(\\s+"
            + DOUBLE_VALUE_STRING + "(\\s+" + DOUBLE_VALUE_STRING
            + ")?)?)?\\s*\\)";
    public static final Pattern SINE_PATTERN = Pattern.compile(SINE_STRING);

    public SineSrcCard(String cardString) throws ParserException {
        super(cardString);
    }

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getTheta() {
        return theta;
    }

    public void setTheta(String theta) {
        this.theta = theta;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = SINE_PATTERN.matcher(cardString);
        if (m.find()) {
            /*
            System.out.println("SIN Src");
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println("group " + i + " = " + m.group(i));
            }
             */
            offset = m.group(1);
            amplitude = m.group(10);
            freq = m.group(20);
            delayTime = m.group(30);
            theta = m.group(40);
            super.parse(cardString.substring(0, m.start() - 1) + cardString.substring(m.end()));
        } else {
            throw new ParserException();
        }
    }
}
