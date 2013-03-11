/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi;

import org.pmedv.blackboard.spice.sim.spice.model.semi.DiodeInstance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.DiodeModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author owenbad
 */
public class DiodeModelCard extends ModelCard<DiodeInstance, DiodeModelValues> {

    boolean firstIK = true;

    public DiodeModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public void setProperty(DiodeModelValues model, String name, String value) {
        if (name.equals("BV")) {
            model.setBreakdownVoltage(parseDouble(value));
        } else if (name.equals("IBV")) {
            model.setBreakdownCurrent(parseDouble(value));
        } else if ((name.equals("IK") && firstIK) || name.equals("IKF")) {
            model.setForwardKneeCurrent(parseDouble(value));
            firstIK = false;
        } else if (name.equals("IK")) {
            model.setReverseKneeCurrent(parseDouble(value));
        } else if (name.equals("IS") || name.equals("JS")) {
            model.setSatCur(parseDouble(value));
        } else if (name.equals("JSW")) {
            model.setSatSWCur(parseDouble(value));
        } else if (name.equals("N")) {
            model.setEmissionCoeff(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setResist(parseDouble(value));
        } else if (name.equals("CJO")) {
            model.setJunctionCap(parseDouble(value));
        } else if (name.equals("CJP") || name.equals("CJSW")) {
            model.setJunctionSWCap(parseDouble(value));
        } else if (name.equals("FC")) {
            model.setDepletionCapCoeff(parseDouble(value));
        } else if (name.equals("FCS")) {
            model.setDepletionSWcapCoeff(parseDouble(value));
        } else if (name.equals("M") || name.equals("MJ")) {
            model.setGradingCoeff(parseDouble(value));
        } else if (name.equals("MJSW")) {
            model.setGradingSWCoeff(parseDouble(value));
        } else if (name.equals("VJ")) {
            model.setJunctionPot(parseDouble(value));
        } else if (name.equals("PHP")) {
            model.setJunctionSWPot(parseDouble(value));
        } else if (name.equals("TT")) {
            model.setTransitTime(parseDouble(value));
        } else if (name.equals("EG")) {
            model.setActivationEnergy(parseDouble(value));
        } else if (name.equals("TM1")) {
            model.setGradCoeffTemp1(parseDouble(value));
        } else if (name.equals("TM2")) {
            model.setGradCoeffTemp2(parseDouble(value));
        } else if (name.equals("TNOM")) {
            model.setNomTemp(parseDouble(value));
        } else if (name.equals("TRS")) {
            model.setResistTemp1(parseDouble(value));
        } else if (name.equals("TRS2")) {
            model.setResistTemp2(parseDouble(value));
        } else if (name.equals("TTT1")) {
            model.setTranTimeTemp1(parseDouble(value));
        } else if (name.equals("TTT2")) {
            model.setTranTimeTemp2(parseDouble(value));
        } else if (name.equals("XTI")) {
            model.settSatCur(parseDouble(value));
        } else if (name.equals("KF")) {
            model.setfNcoef(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setfNexp(parseDouble(value));
        }
    }

    @Override
    public DiodeInstance createInstance() {
        return new DiodeInstance();
    }
}
