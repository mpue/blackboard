/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mos;

import static org.pmedv.blackboard.spice.sim.spice.Constants.*;

import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.MOS1Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.MOSModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOS1ModelCard extends MOSModelCard<MOS1Instance, MOSModelValues> {

    public MOS1ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    @Override
    public MOS1Instance createInstance() {
        MOS1Instance instance = new MOS1Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(MOSModelValues model, String name, String value) {
        if (name.equals("VTO")) {
            model.setVt0(parseDouble(value));
        } else if (name.equals("KP")) {
            model.setTransconductance(parseDouble(value));
        } else if (name.equals("GAMMA")) {
            model.setGamma(parseDouble(value));
        } else if (name.equals("PHI")) {
            model.setPhi(parseDouble(value));
        } else if (name.equals("LAMBDA")) {
            model.setLambda(parseDouble(value));
        } else if (name.equals("RD")) {
            model.setDrnResistance(parseDouble(value));
        } else if (name.equals("RS")) {
            model.setSrcResistance(parseDouble(value));
        } else if (name.equals("CBD")) {
            model.setCapBD(parseDouble(value));
        } else if (name.equals("CBS")) {
            model.setCapBS(parseDouble(value));
        } else if (name.equals("IS")) {
            model.setJctSatCur(parseDouble(value));
        } else if (name.equals("PB")) {
            model.setBlkJctPotential(parseDouble(value));
        } else if (name.equals("CGSO")) {
            model.setGateSrcOverlapCapFactor(parseDouble(value));
        } else if (name.equals("CGDO")) {
            model.setGateDrnOverlapCapFactor(parseDouble(value));
        } else if (name.equals("CGBO")) {
            model.setGateBlkOverlapCapFactor(parseDouble(value));
        } else if (name.equals("CJ")) {
            model.setBlkCapFactor(parseDouble(value));
        } else if (name.equals("MJ")) {
            model.setBlkJctBotGradingCoeff(parseDouble(value));
        } else if (name.equals("CJSW")) {
            model.setSideWallCapFactor(parseDouble(value));
        } else if (name.equals("MJSW")) {
            model.setBlkJctSideGradingCoeff(parseDouble(value));
        } else if (name.equals("JS")) {
            model.setJctSatCurDensity(parseDouble(value));
        } else if (name.equals("TOX")) {
            model.setOxideThickness(parseDouble(value));
        } else if (name.equals("LD")) {
            model.setLatDiff(parseDouble(value));
        } else if (name.equals("RSH")) {
            model.setSheetResistance(parseDouble(value));
        } else if (name.equals("U0")) {
            model.setSurfaceMobility(parseDouble(value));
        } else if (name.equals("FC")) {
            model.setFwdCapDepCoeff(parseDouble(value));
        } else if (name.equals("NSS")) {
            model.setSurfaceStateDensity(parseDouble(value));
        } else if (name.equals("NSUB")) {
            model.setSubstrateDoping(parseDouble(value));
        } else if (name.equals("TPG")) {
            model.setGateType(Integer.parseInt(value));
        } else if (name.equals("TNOM")) {
            model.setTnom(parseDouble(value + CtoK));
        } else if (name.equals("KF")) {
            model.setfNcoef(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setfNexp(parseDouble(value));
        }
    }
}

