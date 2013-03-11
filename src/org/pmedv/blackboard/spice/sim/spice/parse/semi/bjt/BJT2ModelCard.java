/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt;

import org.pmedv.blackboard.spice.sim.spice.model.semi.bjt.BJT2Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.bjt.BJT2ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 * @author Kristopher T. Beck
 */
public class BJT2ModelCard extends BJTModelCard<BJT2Instance, BJT2ModelValues> {

    public BJT2ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public BJT2Instance createInstance() {
        BJT2Instance instance = new BJT2Instance();
        initModelValues(instance);
        return instance;
    }

//    public
    @Override
    public void setProperty(BJT2ModelValues model, String name, String value) {
        if (name.equals("SUBS")) {
            model.setSubs(Integer.parseInt(value));
        } else if (name.equals("IS")) {
            model.setSatCur(parseDouble(value));
        } else if (name.equals("ISS")) {
            model.setSubSatCur(parseDouble(value));
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
            model.setCapSub(parseDouble(value));
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
        } else if (name.equals("TRE1")) {
            model.setReTempCoeff1(parseDouble(value));
        } else if (name.equals("TRE2")) {
            model.setReTempCoeff2(parseDouble(value));
        } else if (name.equals("TRC1")) {
            model.setRcTempCoeff1(parseDouble(value));
        } else if (name.equals("TRC2")) {
            model.setRcTempCoeff2(parseDouble(value));
        } else if (name.equals("TRB1")) {
            model.setRbTempCoeff1(parseDouble(value));
        } else if (name.equals("TRB2")) {
            model.setRbTempCoeff2(parseDouble(value));
        } else if (name.equals("TRBM1")) {
            model.setRbmTempCoeff1(parseDouble(value));
        } else if (name.equals("TRBM2")) {
            model.setRbmTempCoeff2(parseDouble(value));
        }
    }
}
