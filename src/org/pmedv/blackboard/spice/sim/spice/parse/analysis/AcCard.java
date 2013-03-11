/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.analysis.ACAnalysis;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;

/**
 *
 * @author Kristopher T. Beck
 */
public class AcCard extends AnalysisCard {

    private String analType;
    private String steps;
    private String startFreq;
    private String stopFreq;
    public static final String PATTERN_STRING = "^\\.AC\\s+(\\w+)\\s+"
            + DOUBLE_VALUE_STRING + "\\s+"
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING + "$";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public AcCard(String cardString) throws ParserException {
        super(cardString);
        parse(cardString);
    }

    @Override
    public ACAnalysis createAnalysis() {
        ACAnalysis anal = new ACAnalysis();
        if (analType.equals("DEC")) {
            anal.setStepType(ACAnalysis.STEP_MODE.DECADE);
        } else if (analType.equals("OCT")) {
            anal.setStepType(ACAnalysis.STEP_MODE.OCTAVE);
        } else if (analType.equals("LIN")) {
            anal.setStepType(ACAnalysis.STEP_MODE.LINEAR);
        }
        anal.setSteps(Integer.parseInt(steps));
        anal.setStartFreq(parseDouble(startFreq));
        anal.setStopFreq(parseDouble(stopFreq));
        return anal;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        Matcher m = PATTERN.matcher(cardString);
        if (m.find()) {
/*            System.out.println("AC Card");
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println("group " + i + " = " + m.group(i));
            }
*/
            analType = m.group(1);
            steps = m.group(2);
            startFreq = m.group(11);
            stopFreq = m.group(20);
        } else {
            throw new ParserException();
        }
    }

    public String getAnalType() {
        return analType;
    }

    public void setAnalType(String analType) {
        this.analType = analType;
    }

    public String getStartFreq() {
        return startFreq;
    }

    public void setStartFreq(String startFreq) {
        this.startFreq = startFreq;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getStopFreq() {
        return stopFreq;
    }

    public void setStopFreq(String stopFreq) {
        this.stopFreq = stopFreq;
    }

    public Text toText() {
        return Text.intern(".AC " + analType + " " + steps
                + " " + startFreq + " " + stopFreq);
    }
}
