/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.bsim;

import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim.BSIM1Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim.BSIM1ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOSModelCard;

/**
 * 
 * @author Kristopher T. Beck
 */
public class BSIM1ModelCard extends MOSModelCard<BSIM1Instance, BSIM1ModelValues> {

    public BSIM1ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public BSIM1Instance createInstance() {
        BSIM1Instance instance = new BSIM1Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(BSIM1ModelValues model, String name, String value) {
        if (name.equals("VFB0")) {
            model.setVfb0(parseDouble(value));
        } else if (name.equals("VFBL")) {
            model.setVfbL(parseDouble(value));
        } else if (name.equals("VFBW")) {
            model.setVfbW(parseDouble(value));
        } else if (name.equals("PHI0")) {
            model.setPhi0(parseDouble(value));
        } else if (name.equals("PHIL")) {
            model.setPhiL(parseDouble(value));
        } else if (name.equals("PHIW")) {
            model.setPhiW(parseDouble(value));
        } else if (name.equals("K10")) {
            model.setK10(parseDouble(value));
        } else if (name.equals("K1L")) {
            model.setK1L(parseDouble(value));
        } else if (name.equals("K1W")) {
            model.setK1W(parseDouble(value));
        } else if (name.equals("K20")) {
            model.setK20(parseDouble(value));
        } else if (name.equals("K2L")) {
            model.setK2L(parseDouble(value));
        } else if (name.equals("K2W")) {
            model.setK2W(parseDouble(value));
        } else if (name.equals("ETA0")) {
            model.setEta0(parseDouble(value));
        } else if (name.equals("ETAL")) {
            model.setEtaL(parseDouble(value));
        } else if (name.equals("ETAW")) {
            model.setEtaW(parseDouble(value));
        } else if (name.equals("ETAB0")) {
            model.setEtaB0(parseDouble(value));
        } else if (name.equals("ETABL")) {
            model.setEtaBl(parseDouble(value));
        } else if (name.equals("ETABW")) {
            model.setEtaBw(parseDouble(value));
        } else if (name.equals("ETAD0")) {
            model.setEtaD0(parseDouble(value));
        } else if (name.equals("ETADL")) {
            model.setEtaDl(parseDouble(value));
        } else if (name.equals("ETADW")) {
            model.setEtaDw(parseDouble(value));
        } else if (name.equals("DELTAL")) {
            model.setDeltaL(parseDouble(value));
        } else if (name.equals("DELTAW")) {
            model.setDeltaW(parseDouble(value));
        } else if (name.equals("MOBZERO")) {
            model.setMobZero(parseDouble(value));
        } else if (name.equals("MOBZEROB0")) {
            model.setMobZeroB0(parseDouble(value));
        } else if (name.equals("MOBZEROBL")) {
            model.setMobZeroBl(parseDouble(value));
        } else if (name.equals("MOBZEROBW")) {
            model.setMobZeroBw(parseDouble(value));
        } else if (name.equals("MOBVDD0")) {
            model.setMobVdd0(parseDouble(value));
        } else if (name.equals("MOBVDDL")) {
            model.setMobVddl(parseDouble(value));
        } else if (name.equals("MOBVDDW")) {
            model.setMobVddw(parseDouble(value));
        } else if (name.equals("MOBVDDB0")) {
            model.setMobVddB0(parseDouble(value));
        } else if (name.equals("MOBVDDBL")) {
            model.setMobVddBl(parseDouble(value));
        } else if (name.equals("MOBVDDBW")) {
            model.setMobVddBw(parseDouble(value));
        } else if (name.equals("MOBVDDD0")) {
            model.setMobVddD0(parseDouble(value));
        } else if (name.equals("MOBVDDDL")) {
            model.setMobVddDl(parseDouble(value));
        } else if (name.equals("MOBVDDDW")) {
            model.setMobVddDw(parseDouble(value));
        } else if (name.equals("UGS0")) {
            model.setUgs0(parseDouble(value));
        } else if (name.equals("UGSL")) {
            model.setUgsL(parseDouble(value));
        } else if (name.equals("UGSW")) {
            model.setUgsW(parseDouble(value));
        } else if (name.equals("UGSB0")) {
            model.setUgsB0(parseDouble(value));
        } else if (name.equals("UGSBL")) {
            model.setUgsBL(parseDouble(value));
        } else if (name.equals("UGSBW")) {
            model.setUgsBW(parseDouble(value));
        } else if (name.equals("UDS0")) {
            model.setUds0(parseDouble(value));
        } else if (name.equals("UDSL")) {
            model.setUdsL(parseDouble(value));
        } else if (name.equals("UDSW")) {
            model.setUdsW(parseDouble(value));
        } else if (name.equals("UDSB0")) {
            model.setUdsB0(parseDouble(value));
        } else if (name.equals("UDSBL")) {
            model.setUdsBL(parseDouble(value));
        } else if (name.equals("UDSBW")) {
            model.setUdsBW(parseDouble(value));
        } else if (name.equals("UDSD0")) {
            model.setUdsD0(parseDouble(value));
        } else if (name.equals("UDSDL")) {
            model.setUdsDL(parseDouble(value));
        } else if (name.equals("UDSDW")) {
            model.setUdsDW(parseDouble(value));
        } else if (name.equals("N00")) {
            model.setSubthSlope0(parseDouble(value));
        } else if (name.equals("N0L")) {
            model.setSubthSlopeL(parseDouble(value));
        } else if (name.equals("N0W")) {
            model.setSubthSlopeW(parseDouble(value));
        } else if (name.equals("NB0")) {
            model.setSubthSlopeB0(parseDouble(value));
        } else if (name.equals("NBL")) {
            model.setSubthSlopeBL(parseDouble(value));
        } else if (name.equals("NBW")) {
            model.setSubthSlopeBW(parseDouble(value));
        } else if (name.equals("ND0")) {
            model.setSubthSlopeD0(parseDouble(value));
        } else if (name.equals("NDL")) {
            model.setSubthSlopeDL(parseDouble(value));
        } else if (name.equals("NDW")) {
            model.setSubthSlopeDW(parseDouble(value));
        } else if (name.equals("TOX")) {
            model.setOxideThickness(parseDouble(value));
        } else if (name.equals("TEMP")) {
            model.setTemp(parseDouble(value));
        } else if (name.equals("VDD")) {
            model.setVdd(parseDouble(value));
        } else if (name.equals("CGSO")) {
            model.setGateSrcOverlapCap(parseDouble(value));
        } else if (name.equals("CGDO")) {
            model.setGateDrnOverlapCap(parseDouble(value));
        } else if (name.equals("CGBO")) {
            model.setGateBlkOverlapCap(parseDouble(value));
        } else if (name.equals("XPART")) {
            model.setChannelChargePartitionFlag((int) parseDouble(value));
        } else if (name.equals("RSH")) {
            model.setSheetResistance(parseDouble(value));
        } else if (name.equals("JS")) {
            model.setJctSatCurDensity(parseDouble(value));
        } else if (name.equals("PB")) {
            model.setBlkJctPotential(parseDouble(value));
        } else if (name.equals("MJ")) {
            model.setBlkJctBotGradingCoeff(parseDouble(value));
        } else if (name.equals("PBSW")) {
            model.setSidewallJctPotential(parseDouble(value));
        } else if (name.equals("MJSW")) {
            model.setBlkJctSideGradingCoeff(parseDouble(value));
        } else if (name.equals("CJ")) {
            model.setUnitAreaJctCap(parseDouble(value));
        } else if (name.equals("CJSW")) {
            model.setUnitLengthSidewallJctCap(parseDouble(value));
        } else if (name.equals("DEFWIDTH")) {
            model.setDefaultWidth(parseDouble(value));
        } else if (name.equals("DELLENGTH")) {
            model.setDeltaLength(parseDouble(value));
        } else if (name.equals("AF")) {
            model.setfNexp(parseDouble(value));
        } else if (name.equals("KF")) {
            model.setfNcoef(parseDouble(value));
        }
    }
}
