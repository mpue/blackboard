/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.analysis.TransientAnalysis;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;

/**
 *
 * @author Kristopher T. Beck
 */
public class TransientCard extends AnalysisCard {

    private String step;
    private String stopTime;
    private String startTime;
    private String maxStep;
    public static final String PATTERN_STRING = "^\\.TRAN\\s+(" + DOUBLE_VALUE_STRING
            + ")\\s+(" + DOUBLE_VALUE_STRING + ")(\\s+("
            + DOUBLE_VALUE_STRING + ")(\\s+(" + DOUBLE_VALUE_STRING + "))?)?$";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public TransientCard(String cardString) throws ParserException {
        super(cardString);
        parse(cardString);
    }

    public String getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(String maxStep) {
        this.maxStep = maxStep;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public TransientAnalysis createAnalysis() {
        TransientAnalysis anal = new TransientAnalysis();
        anal.setStep(parseDouble(step));
        anal.setStopTime(parseDouble(stopTime));
        if (startTime != null) {
            anal.setStartTime(parseDouble(startTime));
            if (maxStep != null) {
                anal.setMaxStep(parseDouble(maxStep));
            }
        }
        return anal;
    }

    @Override
    public void parse(String cardString) throws ParserException {
    	
    	String[] tokens = cardString.split(" ");

    	if (tokens.length == 3) {
        	step = tokens[1];
        	stopTime = tokens[2];    		
    	}
    	else if (tokens.length == 4) {
        	step = tokens[1];
        	stopTime = tokens[2];    		
        	startTime = tokens[3];

    	}
    	else if (tokens.length == 5) {
        	step = tokens[1];
        	stopTime = tokens[2];    		
        	startTime = tokens[3];    		
        	maxStep = tokens[4];    		
    	}
    	else throw new ParserException("too much tokens");

//        Matcher m = PATTERN.matcher(cardString);
//        if (m.find()) {
///*            for(int i=0;i<m.groupCount();i++){
//                System.out.println("group " + i+" = "+m.group(i));
//            }
//*/
//            step = m.group(1);
//            stopTime = m.group(10);
//            startTime = m.group(20);
//            maxStep = m.group(29);
//            
//            
//        } else {
//            throw new ParserException();
//        }
    }

    @Override
    public Text toText() {
        return Text.intern(".TRAN " + step + " " + stopTime + " " + startTime + " " + maxStep);
    }
}
