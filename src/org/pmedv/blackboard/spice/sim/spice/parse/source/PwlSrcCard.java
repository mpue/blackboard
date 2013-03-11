/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.util.FastTable;

/**
 *
 * @author owenbad
 */
public abstract class PwlSrcCard extends IndependentSourceCard {

    protected FastTable<String> array;
    public static final String TIME_VALUE_STRING = "("
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING + ")";
    public static final String PWL_STRING = "PWL\\s*\\(([a-zA-Z0-9\\.\\- ]+)\\)";
    public static final Pattern TIME_VALUE_PATTERN = Pattern.compile(TIME_VALUE_STRING);
    public static final Pattern PWL_PATTERN = Pattern.compile(PWL_STRING);

    public PwlSrcCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = PWL_PATTERN.matcher(cardString);
        if (m.find()) {
            /*            System.out.println("PWL Src");
            for (int i = 0; i <= m.groupCount(); i++) {
            System.out.println("group " + i + " = " + m.group(i));
            }
             */
            Matcher m2 = TIME_VALUE_PATTERN.matcher(m.group(1));
            if (m2.find()) {
                /*                for (int i = 0; i <= m2.groupCount(); i++) {
                System.out.println("group2 " + i + " = " + m2.group(i));
                }
                 */
                array = new FastTable<String>();
                array.add(m2.group(2));
                array.add(m2.group(11));
                while (m2.find()) {
                    /*                for (int i = 0; i <= m2.groupCount(); i++) {
                    System.out.println("group2 " + i + " = " + m2.group(i));
                    }
                     */
                    array.add(m2.group(2));
                    array.add(m2.group(11));
                }
            }
            super.parse(cardString.substring(0, m.start() - 1) + cardString.substring(m.end()));
        } else {
            throw new ParserException();
        }
    }
}
