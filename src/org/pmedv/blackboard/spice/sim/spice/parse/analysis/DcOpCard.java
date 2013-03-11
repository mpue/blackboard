/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.analysis.DcOpAnalysis;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;

/**
 *
 * @author Kristopher T. Beck
 */
public class DcOpCard extends AnalysisCard {

    public static final String PATTERN_STRING = "^\\.OP$";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public DcOpCard(String cardString) throws ParserException{
        super(cardString);
    }

    @Override
    public DcOpAnalysis createAnalysis() {
        return new DcOpAnalysis();
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = PATTERN.matcher(cardString);
        if(!m.find()){
            throw new ParserException();
        }
    }

    @Override
    public Text toText() {
        return Text.intern(".OP");
    }
}
