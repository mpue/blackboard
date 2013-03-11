/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.output;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.output.ACPlotOutput;
import org.pmedv.blackboard.spice.sim.spice.output.DCTranCurvePlotOutput;
import org.pmedv.blackboard.spice.sim.spice.output.PlotOutput;
import org.pmedv.blackboard.spice.sim.spice.output.TranPlotOutput;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;


/**
 *
 * @author owenbad
 */
public class PlotCard extends OutputCard {

    public static final String PATTERN_STRING = "\\.PLOT\\s+(\\w+)(.+)";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public PlotCard(String cardString) throws ParserException {
        this.cardString = cardString;
        parseOutputs(cardString);
    }

    @Override
    public PlotOutput createOutput(Deck deck) {
        PlotOutput output = null;
        if (type.startsWith("AC")) {
            output = new ACPlotOutput(deck.getTitle());
        } else if (type.startsWith("TRAN")) {
            output = new TranPlotOutput(deck.getTitle());
        } else if (type.startsWith("DC")) {
            output = new DCTranCurvePlotOutput(deck.getTitle());
        }
        initParameters(output, deck);
        return output;
    }

    @Override
    public void parseOutputs(String cardString) throws ParserException {
        Matcher m = PATTERN.matcher(cardString);
        if (m.find()) {
            if (m.group(1).equals("AC")) {
                type = "AC_PLOT";
            }
            if (m.group(1).equals("TRAN")) {
                type = "TRAN_PLOT";
            } else if (m.group(1).equals("DC")) {
                type = "DC_PLOT";
            }
            super.parseOutputs(cardString);
        }
    }
}
