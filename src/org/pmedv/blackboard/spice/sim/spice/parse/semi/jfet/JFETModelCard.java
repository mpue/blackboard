/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.parse.semi.jfet;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.jfet.JFETModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public abstract class JFETModelCard <I extends Instance, L extends JFETModelValues> extends ModelCard<I, L>{

    public JFETModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public void setModelType(L model) {
        if (name.equals("NJF")) {
            model.setType(N_TYPE);
        } else if (name.equals("PJF")) {
            model.setType(P_TYPE);
        }
    }
}
