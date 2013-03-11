/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet.MESModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public abstract class MESModelCard<I extends Instance, L extends MESModelValues> extends ModelCard<I, L> {

    public MESModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public void initModelValues(L model) {
        setModelType(model, type);
        super.initModelValues(model);
    }

    public void setModelType(MESModelValues model, String type) {
        if (type.equals("NMF")) {
            model.setType(N_TYPE);
        } else if (type.equals("PMF")) {
            model.setType(P_TYPE);
        }
    }
}
