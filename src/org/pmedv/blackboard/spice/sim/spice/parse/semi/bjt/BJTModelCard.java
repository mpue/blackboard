/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.bjt.BJTModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public abstract class BJTModelCard<I extends Instance, L extends BJTModelValues> extends ModelCard<I, L> {

    public BJTModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public void setModelType(L model) {
        if (type.equals("NPN")) {
            model.setType(N_TYPE);
        } else if (type.equals("PNP")) {
            model.setType(P_TYPE);
        }
    }

    @Override
    public void initModelValues(L model) {
        setModelType(model);
        super.initModelValues(model);
    }
}
