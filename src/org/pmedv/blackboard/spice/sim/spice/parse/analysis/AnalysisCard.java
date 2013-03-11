/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.analysis;

import org.pmedv.blackboard.spice.sim.spice.analysis.Analysis;
import org.pmedv.blackboard.spice.sim.spice.parse.Card;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;

/**
 *
 * @author Kristopher T. Beck
 */
public abstract class AnalysisCard extends Card {

    public AnalysisCard(String cardString) throws ParserException {
        super(cardString);
    }

    public abstract void parse(String cardString) throws ParserException;

    public abstract Analysis createAnalysis();

    public abstract Text toText();

    @Override
    public String toString() {
        return toText().toString();
    }
}
