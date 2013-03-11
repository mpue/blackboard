/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.bsim;

import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim.BSIM2Instance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim.BSIM2ModelValues;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOSModelCard;

/**
 *
 * @author Kristopher T. Beck
 */
public class BSIM2ModelCard extends MOSModelCard<BSIM2Instance, BSIM2ModelValues> {

    public BSIM2ModelCard(String cardString) throws ParserException {
        super(cardString);
    }

    public BSIM2Instance createInstance() {
        BSIM2Instance instance = new BSIM2Instance();
        initModelValues(instance);
        return instance;
    }

    @Override
    public void setProperty(BSIM2ModelValues model, String name, String value) {
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
        } else if (name.equals("ETA00")) {
            model.setEta00(parseDouble(value));
        } else if (name.equals("ETA0L")) {
            model.setEta0L(parseDouble(value));
        } else if (name.equals("ETA0W")) {
            model.setEta0W(parseDouble(value));
        } else if (name.equals("ETAB0")) {
            model.setEtaB0(parseDouble(value));
        } else if (name.equals("ETABL")) {
            model.setEtaBL(parseDouble(value));
        } else if (name.equals("ETABW")) {
            model.setEtaBW(parseDouble(value));
        } else if (name.equals("DELTAL")) {
            model.setDeltaL(parseDouble(value));
        } else if (name.equals("DELTAW")) {
            model.setDeltaW(parseDouble(value));
        } else if (name.equals("MOB00")) {
            model.setMob00(parseDouble(value));
        } else if (name.equals("MOB0B0")) {
            model.setMob0B0(parseDouble(value));
        } else if (name.equals("MOB0BL")) {
            model.setMob0BL(parseDouble(value));
        } else if (name.equals("MOB0BW")) {
            model.setMob0BW(parseDouble(value));
        } else if (name.equals("MOBS00")) {
            model.setMobs00(parseDouble(value));
        } else if (name.equals("MOBS0L")) {
            model.setMobs0L(parseDouble(value));
        } else if (name.equals("MOBS0W")) {
            model.setMobs0W(parseDouble(value));
        } else if (name.equals("MOBSB0")) {
            model.setMobsB0(parseDouble(value));
        } else if (name.equals("MOBSBL")) {
            model.setMobsBL(parseDouble(value));
        } else if (name.equals("MOBSBW")) {
            model.setMobsBW(parseDouble(value));
        } else if (name.equals("MOB200")) {
            model.setMob200(parseDouble(value));
        } else if (name.equals("MOB20L")) {
            model.setMob20L(parseDouble(value));
        } else if (name.equals("MOB20W")) {
            model.setMob20W(parseDouble(value));
        } else if (name.equals("MOB2B0")) {
            model.setMob2B0(parseDouble(value));
        } else if (name.equals("MOB2BL")) {
            model.setMob2BL(parseDouble(value));
        } else if (name.equals("MOB2BW")) {
            model.setMob2BW(parseDouble(value));
        } else if (name.equals("MOB2G0")) {
            model.setMob2G0(parseDouble(value));
        } else if (name.equals("MOB2GL")) {
            model.setMob2GL(parseDouble(value));
        } else if (name.equals("MOB2GW")) {
            model.setMob2GW(parseDouble(value));
        } else if (name.equals("MOB300")) {
            model.setMob300(parseDouble(value));
        } else if (name.equals("MOB30L")) {
            model.setMob30L(parseDouble(value));
        } else if (name.equals("MOB30W")) {
            model.setMob30W(parseDouble(value));
        } else if (name.equals("MOB3B0")) {
            model.setMob3B0(parseDouble(value));
        } else if (name.equals("MOB3BL")) {
            model.setMob3BL(parseDouble(value));
        } else if (name.equals("MOB3BW")) {
            model.setMob3BW(parseDouble(value));
        } else if (name.equals("MOB3G0")) {
            model.setMob3G0(parseDouble(value));
        } else if (name.equals("MOB3GL")) {
            model.setMob3GL(parseDouble(value));
        } else if (name.equals("MOB3GW")) {
            model.setMob3GW(parseDouble(value));
        } else if (name.equals("MOB400")) {
            model.setMob400(parseDouble(value));
        } else if (name.equals("MOB40L")) {
            model.setMob40L(parseDouble(value));
        } else if (name.equals("MOB40W")) {
            model.setMob40W(parseDouble(value));
        } else if (name.equals("MOB4B0")) {
            model.setMob4B0(parseDouble(value));
        } else if (name.equals("MOB4BL")) {
            model.setMob4BL(parseDouble(value));
        } else if (name.equals("MOB4BW")) {
            model.setMob4BW(parseDouble(value));
        } else if (name.equals("MOB4G0")) {
            model.setMob4G0(parseDouble(value));
        } else if (name.equals("MOB4GL")) {
            model.setMob4GL(parseDouble(value));
        } else if (name.equals("MOB4GW")) {
            model.setMob4GW(parseDouble(value));
        } else if (name.equals("UA00")) {
            model.setUa00(parseDouble(value));
        } else if (name.equals("UA0L")) {
            model.setUa0L(parseDouble(value));
        } else if (name.equals("UA0W")) {
            model.setUa0W(parseDouble(value));
        } else if (name.equals("UAB0")) {
            model.setUaB0(parseDouble(value));
        } else if (name.equals("UABL")) {
            model.setUaBL(parseDouble(value));
        } else if (name.equals("UABW")) {
            model.setUaBW(parseDouble(value));
        } else if (name.equals("UB00")) {
            model.setUb00(parseDouble(value));
        } else if (name.equals("UB0L")) {
            model.setUb0L(parseDouble(value));
        } else if (name.equals("UB0W")) {
            model.setUb0W(parseDouble(value));
        } else if (name.equals("UBB0")) {
            model.setUbB0(parseDouble(value));
        } else if (name.equals("UBBL")) {
            model.setUbBL(parseDouble(value));
        } else if (name.equals("UBBW")) {
            model.setUbBW(parseDouble(value));
        } else if (name.equals("U100")) {
            model.setU100(parseDouble(value));
        } else if (name.equals("U10L")) {
            model.setU10L(parseDouble(value));
        } else if (name.equals("U10W")) {
            model.setU10W(parseDouble(value));
        } else if (name.equals("U1B0")) {
            model.setU1B0(parseDouble(value));
        } else if (name.equals("U1BL")) {
            model.setU1BL(parseDouble(value));
        } else if (name.equals("U1BW")) {
            model.setU1BW(parseDouble(value));
        } else if (name.equals("U1D0")) {
            model.setU1D0(parseDouble(value));
        } else if (name.equals("U1DL")) {
            model.setU1DL(parseDouble(value));
        } else if (name.equals("U1DW")) {
            model.setU1DW(parseDouble(value));
        } else if (name.equals("N00")) {
            model.setN00(parseDouble(value));
        } else if (name.equals("N0L")) {
            model.setN0L(parseDouble(value));
        } else if (name.equals("N0W")) {
            model.setN0W(parseDouble(value));
        } else if (name.equals("NB0")) {
            model.setnB0(parseDouble(value));
        } else if (name.equals("NBL")) {
            model.setnBL(parseDouble(value));
        } else if (name.equals("NBW")) {
            model.setnBW(parseDouble(value));
        } else if (name.equals("ND0")) {
            model.setnD0(parseDouble(value));
        } else if (name.equals("NDL")) {
            model.setnDL(parseDouble(value));
        } else if (name.equals("NDW")) {
            model.setnDW(parseDouble(value));
        } else if (name.equals("VOF00")) {
            model.setVof00(parseDouble(value));
        } else if (name.equals("VOF0L")) {
            model.setVof0L(parseDouble(value));
        } else if (name.equals("VOF0W")) {
            model.setVof0W(parseDouble(value));
        } else if (name.equals("VOFB0")) {
            model.setVofB0(parseDouble(value));
        } else if (name.equals("VOFBL")) {
            model.setVofBL(parseDouble(value));
        } else if (name.equals("VOFBW")) {
            model.setVofBW(parseDouble(value));
        } else if (name.equals("VOFD0")) {
            model.setVofD0(parseDouble(value));
        } else if (name.equals("VOFDL")) {
            model.setVofDL(parseDouble(value));
        } else if (name.equals("VOFDW")) {
            model.setVofDW(parseDouble(value));
        } else if (name.equals("AI00")) {
            model.setAi00(parseDouble(value));
        } else if (name.equals("AI0L")) {
            model.setAi0L(parseDouble(value));
        } else if (name.equals("AI0W")) {
            model.setAi0W(parseDouble(value));
        } else if (name.equals("AIB0")) {
            model.setAiB0(parseDouble(value));
        } else if (name.equals("AIBL")) {
            model.setAiBL(parseDouble(value));
        } else if (name.equals("AIBW")) {
            model.setAiBW(parseDouble(value));
        } else if (name.equals("BI00")) {
            model.setBi00(parseDouble(value));
        } else if (name.equals("BI0L")) {
            model.setBi0L(parseDouble(value));
        } else if (name.equals("BI0W")) {
            model.setBi0W(parseDouble(value));
        } else if (name.equals("BIB0")) {
            model.setBiB0(parseDouble(value));
        } else if (name.equals("BIBL")) {
            model.setBiBL(parseDouble(value));
        } else if (name.equals("BIBW")) {
            model.setBiBW(parseDouble(value));
        } else if (name.equals("VGHIGH0")) {
            model.setVghigh0(parseDouble(value));
        } else if (name.equals("VGHIGHL")) {
            model.setVghighL(parseDouble(value));
        } else if (name.equals("VGHIGHW")) {
            model.setVghighW(parseDouble(value));
        } else if (name.equals("VGLOW0")) {
            model.setVglow0(parseDouble(value));
        } else if (name.equals("VGLOWL")) {
            model.setVglowL(parseDouble(value));
        } else if (name.equals("VGLOWW")) {
            model.setVglowW(parseDouble(value));
        } else if (name.equals("TOX")) {
            model.setTox(parseDouble(value));
        } else if (name.equals("TEMP")) {
            model.setTemp(parseDouble(value));
        } else if (name.equals("VDD")) {
            model.setVdd(parseDouble(value));
        } else if (name.equals("VGG")) {
            model.setVgg(parseDouble(value));
        } else if (name.equals("VBB")) {
            model.setVbb(parseDouble(value));
        } else if (name.equals("CGSO")) {
            model.setGateSrcOverlapCap(parseDouble(value));
        } else if (name.equals("CGDO")) {
            model.setGateDrnOverlapCap(parseDouble(value));
        } else if (name.equals("CGBO")) {
            model.setGateBlkOverlapCap(parseDouble(value));
        } else if (name.equals("XPART")) {
            model.setChannelChargePartitionFlag(Integer.parseInt(value));
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
