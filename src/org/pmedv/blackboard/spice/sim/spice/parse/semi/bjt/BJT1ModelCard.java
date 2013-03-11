/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt;

import org.pmedv.blackboard.spice.sim.spice.model.semi.bjt.BJT1Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.bjt.BJTModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 **/
public class BJT1ModelCard extends BJTModelCard<BJT1Instance, BJTModelValues> {

    public BJT1ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public BJT1Instance createInstance() {
        BJT1Instance instance = new BJT1Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(BJTModelValues model, String name, String value) {
        if (name.equals("IS")) {
            model.setSatCur(parseDouble(value));
        } else if (name.equals("BF")) {
            model.setBetaF(parseDouble(value));
        } else if (name.equals("NF")) {
            model.setEmissionCoeffF(parseDouble(value));
        } else if (name.equals("VAF")) {
            model.setEarlyVoltF(parseDouble(value));
        } else if (name.equals("IKF")) {
            model.setRollOffF(parseDouble(value));
        } else if (name.equals("ISE")) {
            model.setLeakBEcurrent(parseDouble(value));
        } else if (name.equals("NE")) {
            model.setLeakBEemissionCoeff(parseDouble(value));
        } else if (name.equals("BR")) {
            model.setBetaR(parseDouble(value));
        } else if (name.equals("NR")) {
            model.setEmissionCoeffR(parseDouble(value));
        } else if (name.equals("VAR")) {
            model.setEarlyVoltR(parseDouble(value));
        } else if (name.equals("IKR")) {
            model.setRollOffR(parseDouble(value));
        } else if (name.equals("ISC")) {
            model.setLeakBCcurrent(parseDouble(value));
        } else if (name.equals("NC")) {
            model.setLeakBCemissionCoeff(parseDouble(value));
        } else if (name.equals("RB")) {
            model.setBaseResist(parseDouble(value));
        } else if (name.equals("IRB")) {
            model.setBaseCurrentHalfResist(parseDouble(value));
        } else if (name.equals("RBM")) {
            model.setMinBaseResist(parseDouble(value));
        } else if (name.equals("RE")) {
            model.setEmitterResist(parseDouble(value));
        } else if (name.equals("RC")) {
            model.setCollectorResist(parseDouble(value));
        } else if (name.equals("CJE")) {
            model.setDepletionCapBE(parseDouble(value));
        } else if (name.equals("VJE")) {
            model.setPotentialBE(parseDouble(value));
        } else if (name.equals("MJE")) {
            model.setJunctionExpBE(parseDouble(value));
        } else if (name.equals("TF")) {
            model.setTransitTimeF(parseDouble(value));
        } else if (name.equals("XTF")) {
            model.setTransitTimeBiasCoeffF(parseDouble(value));
        } else if (name.equals("VTF")) {
            model.setTransitTimeFVBC(parseDouble(value));
        } else if (name.equals("ITF")) {
            model.setTransitTimeHighCurrentF(parseDouble(value));
        } else if (name.equals("PTF")) {
            model.setExcessPhase(parseDouble(value));
        } else if (name.equals("CJC")) {
            model.setDepletionCapBC(parseDouble(value));
        } else if (name.equals("VJC")) {
            model.setPotentialBC(parseDouble(value));
        } else if (name.equals("MJC")) {
            model.setJunctionExpBC(parseDouble(value));
        } else if (name.equals("XVJC")) {
            model.setBaseFractionBCcap(parseDouble(value));
        } else if (name.equals("TR")) {
            model.setTransitTimeR(parseDouble(value));
        } else if (name.equals("CJS")) {
            model.setCapCS(parseDouble(value));
        } else if (name.equals("VJS")) {
            model.setPotentialSubstrate(parseDouble(value));
        } else if (name.equals("MJS")) {
            model.setExponentialSubstrate(parseDouble(value));
        } else if (name.equals("XTB")) {
            model.setBetaExp(parseDouble(value));
        } else if (name.equals("EG")) {
            model.setEnergyGap(parseDouble(value));
        } else if (name.equals("XTI")) {
            model.setTempExpIS(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setFNcoef(parseDouble(value));
        } else if (name.equals("KF")) {
            model.setFNexp(parseDouble(value));
        } else if (name.equals("FC")) {
            model.setDepletionCapCoeff(parseDouble(value));
        } else if (name.equals("TNOM")) {
            model.setTnom(parseDouble(value));
        }
    }
}
