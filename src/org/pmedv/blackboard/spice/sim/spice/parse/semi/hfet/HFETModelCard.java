/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.hfet;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.hfet.HFETModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public abstract class HFETModelCard<I extends Instance, L extends HFETModelValues> extends ModelCard<I, L> {

    public HFETModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public void setModelType(L model, String type) {
        if (type.equals("NMOS")) {
            model.setType(N_TYPE);
        } else if (type.equals("PMOS")) {
            model.setType(P_TYPE);
        }
    }

    @Override
    public void initModelValues(L model) {
        setModelType(model, type);
        super.initModelValues(model);
    }
}
