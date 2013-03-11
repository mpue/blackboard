/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mos;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.MOSModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public abstract class MOSModelCard<I extends Instance, L extends MOSModelValues> extends ModelCard<I, L> {

    public MOSModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public void initModelValues(L model) {
        setModelType(model);
        super.initModelValues(model);
    }

    public void setModelType(L model) {
        if (type.equals("NMOS")) {
            model.setType(N_TYPE);
        } else if (type.equals("PMOS")) {
            model.setType(P_TYPE);
        }
    }
}
