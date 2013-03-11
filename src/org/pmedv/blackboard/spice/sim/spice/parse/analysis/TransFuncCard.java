/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.analysis.TransFunc;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;

/**
 *
 * @author Kristopher T. Beck
 */
public class TransFuncCard extends AnalysisCard {

    private String outSrcName;
    private String inSrcName;
    public static final String PATTERN_STRING = "^\\.TF\\s+(\\w+)\\s+(\\w+)$";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public TransFuncCard(String cardString) throws ParserException {
        super(cardString);
        parse(cardString);
    }

    public String getInSrcName() {
        return inSrcName;
    }

    public void setInSrcName(String inSrcName) {
        this.inSrcName = inSrcName;
    }

    public String getOutSrcName() {
        return outSrcName;
    }

    public void setOutSrcName(String outSrcName) {
        this.outSrcName = outSrcName;
    }

    @Override
    public TransFunc createAnalysis() {
        TransFunc anal = new TransFunc();
        anal.setOutSrcStr(outSrcName);
        anal.setInSrcStr(inSrcName);
        return anal;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = PATTERN.matcher(cardString);
        if (m.find()) {
            outSrcName = m.group(1);
            inSrcName = m.group(2);
        } else {
            throw new ParserException();
        }
    }

    @Override
    public Text toText() {
        return Text.intern(".TF " + outSrcName + " " + inSrcName);
    }
}
