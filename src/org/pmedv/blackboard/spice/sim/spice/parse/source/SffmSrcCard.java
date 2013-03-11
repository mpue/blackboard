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
public abstract class SffmSrcCard extends IndependentSourceCard {

    protected String offset;
    protected String amplitude;
    protected String carrierFreq;
    protected String modulationIndex;
    protected String signalFreq;
    public static final String SFFM_STRING = "SFFM\\s*\\(\\s*"
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING
            + "(\\s+" + DOUBLE_VALUE_STRING + "(\\s+"
            + DOUBLE_VALUE_STRING + "(\\s+" + DOUBLE_VALUE_STRING
            + ")?)?)?\\s*\\)";
    public static final Pattern SFFM_PATTERN = Pattern.compile(SFFM_STRING);

    public SffmSrcCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = SFFM_PATTERN.matcher(cardString);
        if (m.find()) {
            /*
            System.out.println("SFFM Src");
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println("group " + i + " = " + m.group(i));
            }
             * 
             */
            offset = m.group(1);
            amplitude = m.group(10);
            carrierFreq = m.group(20);
            modulationIndex = m.group(30);
            signalFreq = m.group(40);
            super.parse(cardString.substring(0, m.start() - 1) + cardString.substring(m.end()));
        } else {
            throw new ParserException();
        }
    }
}
