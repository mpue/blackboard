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
public abstract class PulseSrcCard extends IndependentSourceCard {

    protected String initValue;
    protected String pulsedValue;
    protected String delayTime;
    protected String riseTime;
    protected String fallTime;
    protected String pulseWidth;
    protected String period;
    public static final String PULSE_STRING = "PULSE\\s*\\(\\s*("
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING
            + "(\\s+" + DOUBLE_VALUE_STRING + "(\\s+"
            + DOUBLE_VALUE_STRING + "(\\s+" + DOUBLE_VALUE_STRING
            + "(\\s+" + DOUBLE_VALUE_STRING + "(\\s+" + DOUBLE_VALUE_STRING
            + ")?)?)?)?)?\\s*)?\\s*" + "\\)";
    public static final Pattern PULSE_PATTERN = Pattern.compile(PULSE_STRING);

    public PulseSrcCard(String cardString) throws ParserException {
        super(cardString);
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getFallTime() {
        return fallTime;
    }

    public void setFallTime(String fallTime) {
        this.fallTime = fallTime;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPulseWidth() {
        return pulseWidth;
    }

    public void setPulseWidth(String pulseWidth) {
        this.pulseWidth = pulseWidth;
    }

    public String getPulsedValue() {
        return pulsedValue;
    }

    public void setPulsedValue(String pulsedValue) {
        this.pulsedValue = pulsedValue;
    }

    public String getRiseTime() {
        return riseTime;
    }

    public void setRiseTime(String riseTime) {
        this.riseTime = riseTime;
    }

    @Override
    public void parse(String cardString) throws ParserException {
//        super.parse(cardString);
        Matcher m = PULSE_PATTERN.matcher(cardString);
        if (m.find()) {
            /*
            System.out.println("Pulse Src");
            for (int i = 0; i < m.groupCount(); i++) {
            System.out.println("group " + i + " = " + m.group(i));
            }
             */
            initValue = m.group(2);
            pulsedValue = m.group(11);
            delayTime = m.group(21);
            riseTime = m.group(31);
            fallTime = m.group(41);
            pulseWidth = m.group(51);
            period = m.group(61);
            super.parse(cardString.substring(0, m.start() - 1) + cardString.substring(m.end()));
        } else {
            throw new ParserException();
        }
    }
}
