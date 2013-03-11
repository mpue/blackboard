/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.source.isrc;

import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcPWL;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.source.PwlSrcCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class IPWLCard extends PwlSrcCard {

    public IPWLCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public ISrcPWL createInstance(Deck deck) {
        ISrcPWL instance = new ISrcPWL();
        initSrcValues(instance, deck);
        initAuxValues(instance);
        return instance;
    }

    protected void initAuxValues(ISrcPWL instance) {
        int size = array.size() / 2;
        double[] times = new double[size];
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            times[i] = parseDouble(array.get(i * 2));
            values[i + 1] = parseDouble(array.get(i * 2 + 1));
        }
        instance.setTimes(times);
        instance.setValues(values);
    }
}
