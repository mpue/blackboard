/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javolution.lang.MathLib;

/**
 *
 * @author Kristopher T. Beck
 */
public class Parser {

    public static final double T = 1e12;
    public static final double G = 1e9;
    public static final double MEG = 1e6;
    public static final double K = 1e3;
    public static final double C = 1e-2;
    public static final double M = 1e-3;
    public static final double U = 1e-6;
    public static final double N = 1e-9;
    public static final double P = 1e-12;
    public static final double F = 1e-15;
    public static final String DOUBLE_STRING = "([+-]?((\\.[0-9]+)|([0-9]+(\\.[0-9]*)?))([eE][+-]?[0-9]+)?)";
    public static final String DOUBLE_VALUE_STRING = "("+DOUBLE_STRING +
            "(T|G|MEG|K|C|M|U|N|P|F)?(\\w+)?)";
    public static final String PROPERTY_STRING = "(([a-zA-Z0-9\\.]+)\\s*=\\s*([a-zA-Z0-9\\.\\-]+))";
    public static final String SRC_ELEMENT_PATTERN_STRING = "((V[IAMR]?|I)\\((\\w+)(, ?(\\w+))?\\))";
    public static final Pattern SRC_ELEMENT_PATTERN = Pattern.compile(SRC_ELEMENT_PATTERN_STRING);
    public static final Pattern DOUBLE_VALUE_PATTERN = Pattern.compile(DOUBLE_VALUE_STRING);
    public static final Pattern PROPERTY_PATTERN = Pattern.compile(PROPERTY_STRING);

    public static boolean isNumber(String str) {
        return str.matches(DOUBLE_VALUE_STRING);
    }

    public static double parseDouble(String str) {
        if (str == null) {
            return 0;
        }
        Matcher m = DOUBLE_VALUE_PATTERN.matcher(str);
        String mult;
        if (m.find()) {
/*            System.out.println("Double parse");
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println("group " + i + " = " + m.group(i));
            }
   */
            double value = Double.parseDouble(m.group(2));
            mult = m.group(8);
            if (mult != null) {
                mult = mult.toUpperCase();
                if (mult.startsWith("MIL")) {
                    value *= MathLib.pow(25.4, -6);
                } else if (mult.startsWith("T")) {
                    return value *= T;
                } else if (mult.startsWith("G")) {
                    return value *= G;
                } else if (mult.startsWith("MEG")) {
                    return value *= MEG;
                } else if (mult.startsWith("K")) {
                    return value *= K;
                } else if (mult.startsWith("C")) {
                    return value *= C;
                } else if (mult.startsWith("M")) {
                    return value *= M;
                } else if (mult.startsWith("U")) {
                    return value *= U;
                } else if (mult.startsWith("N")) {
                    return value *= N;
                } else if (mult.startsWith("P")) {
                    return value *= P;
                } else if (mult.startsWith("F")) {
                    return value *= F;
                }
            }
            return value;
        } else {
            throw new NumberFormatException();
        }
    }
}
