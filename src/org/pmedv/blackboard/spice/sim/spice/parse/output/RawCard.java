/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.output;

import org.pmedv.blackboard.spice.sim.spice.output.RawOutput;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;

/**
 *
 * @author owenbad
 */
public class RawCard extends OutputCard {

    public RawCard(String cardString) {
        super(cardString);
        type = "SAVE";
    }

    @Override
    public RawOutput createOutput(Deck deck) {
        RawOutput output = new RawOutput(deck.getTitle());
        initParameters(output, deck);
        return output;
    }
}
