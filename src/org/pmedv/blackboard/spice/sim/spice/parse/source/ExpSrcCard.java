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
public abstract class ExpSrcCard extends IndependentSourceCard {

    protected String initValue;
    protected String pulsedValue;
    protected String riseDelay;
    protected String riseConst;
    protected String fallDelay;
    protected String fallConst;
    public static final String EXP_STRING = "EXP\\s*\\(\\s*"
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING
            + "(\\s+" + DOUBLE_VALUE_STRING + "(\\s+"
            + DOUBLE_VALUE_STRING + "(\\s+" + DOUBLE_VALUE_STRING
            + "(\\s+" + DOUBLE_VALUE_STRING + ")?)?)?)?\\s*\\)";
    public static final Pattern EXP_PATTERN = Pattern.compile(EXP_STRING);

    public ExpSrcCard(String cardString) throws ParserException {
        super(cardString);
    }

    public String getFallConst() {
        return fallConst;
    }

    public void setFallConst(String fallConst) {
        this.fallConst = fallConst;
    }

    public String getFallDelay() {
        return fallDelay;
    }

    public void setFallDelay(String fallDelay) {
        this.fallDelay = fallDelay;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public String getPulsedValue() {
        return pulsedValue;
    }

    public void setPulsedValue(String pulsedValue) {
        this.pulsedValue = pulsedValue;
    }

    public String getRiseConst() {
        return riseConst;
    }

    public void setRiseConst(String riseConst) {
        this.riseConst = riseConst;
    }

    public String getRiseDelay() {
        return riseDelay;
    }

    public void setRiseDelay(String riseDelay) {
        this.riseDelay = riseDelay;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = EXP_PATTERN.matcher(cardString);
        if (m.find()) {
/*            System.out.println("EXP Src");
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println("group " + i + " = " + m.group(i));
            }
 * 
 */
            initValue = m.group(1);
            pulsedValue = m.group(10);
            riseDelay = m.group(20);
            riseConst = m.group(30);
            fallDelay = m.group(40);
            fallConst = m.group(50);
            super.parse(cardString.substring(0, m.start() - 1) + cardString.substring(m.end()));
        } else {
            throw new ParserException();
        }
    }
}
