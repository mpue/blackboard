/*
 * Circuit.java
 *
 * Created on May 30, 2006, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model;

import org.pmedv.blackboard.spice.sim.Device;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
//import ktb.eda.spice.EnumConsts.ENHTYPE;
/**
 *
 * @author Kristopher T. Beck
 */

public class SpiceCircuit extends Circuit {

    protected int troubleNode;
    protected Device troubleElt;
    protected boolean init;
    protected boolean analInit;
    protected boolean reportConvProbs;
    protected boolean convLimitEnabled;
    protected boolean useIC;
    /**
     * Creates a new instance of Circuit
     */

    public SpiceCircuit() {
    }

    public void setAnalInit(boolean analInit) {
        this.analInit = analInit;
    }

    public boolean isAnalInit() {
        return analInit;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public boolean isInit() {
        return init;
    }

    public void setReportConvProbs(boolean reportConvProbs) {
        this.reportConvProbs = reportConvProbs;
    }

    public boolean isReportConvProbs() {
        return reportConvProbs;
    }
/*
    public void reportConvProb(ENHTYPE type, String name, String msg) {
        StandardLog.warning(String.format("WARNING: Convergence problems at " +
                type.toString() + " (" + name + ").  " + msg));
    }
*/
    public void setTroubleNode(int troubleNode) {
        this.troubleNode = troubleNode;
    }

    public int getTroubleNode() {
        return troubleNode;
    }

    public Device getTroubleDevice() {
        return troubleElt;
    }

    public void setTroubleDevice(Device troubleElt) {
        this.troubleElt = troubleElt;
    }

    /*
    public boolean noise(NZ_MODE mode, NZ_OPER operation, NData data,
            double outNdens){
        int i;
        FastTable<Real> outData;
        Real refVal = null;
        outNdens = 0.0;
        FrontEnd frontEnd = FrontEnd.getDefault();
        FastTable<String> nameList;
        for (Device dev: devices){
            dev.noise(mode, operation, data, outNdens);
            switch (operation) {
                case OPEN:
                    switch (mode) {
                        case DENS:
                            data.addPlot(frontEnd.createUID(instName,
                                    "onoise_spectrum", UID.OTHER));
                            data.addPlot(frontEnd.createUID(instName,
                                    "inoise_spectrum", UID.OTHER));
                            // data.setOutPVector(new double[data.getNumPlots()][]);
                            break;
                        case INT_NOIZ:
                            data.addPlot(frontEnd.createUID(instName,
                                    "onoise_total", UID.OTHER));
                            data.addPlot(frontEnd.createUID(instName,
                                    "inoise_total", UID.OTHER));
//         data.outpVector = new double[data.numPlots][data.getOutNumber()];
                            break;
                        default:
                            return false;
                    }
                    break;
                case CALC:
                    switch (mode) {
                        case DENS:
                            if ((((Noise)curJob).getStpsSm() == 0)
                            || data.isPrtSummary()) {
                                data.addOutValue(outNdens);
                                data.addOutValue(outNdens * data.getGainSqInv());
                                refVal = Real.valueOf(data.getFreq());
                                data.getRunDesc().outPData(refVal,
                                        data.getOutPVector());
                            }
                            break;
                        case INT_NOIZ:
                            data.addOutValue(data.getOutNoiz());
                            data.addOutValue(data.getInNoise());
                            data.getRunDesc().outPData(refVal,
                                    data.getOutPVector());
                            break;
                        default:
                            return false;
                    }
                    break;
                case CLOSE:
                    data.getRunDesc().outEndPlot();
                    data.reset();
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
 */
    /*
    public boolean distor(DISTOR mode) {
        Distor cv = (Distor)curJob;
        int i;
        int size;
        switch(mode) {
        case SETUP:
            for (Device dev: devices){
                dev.distor(mode);
            }
            break;
        case TWOF1:
        case THRF1:
        case F1PF2:
        case F1MF2:
        case TWOF1MF2:
            wrk.clearA();
            for (i = 0; i < devices.size(); i++)
                devices.get(i).distor(mode);
            break;
        case RHSF1:
            cv.setF2Given(false);
        case RHSF2:
            double mag = 0.0;
            double phase = 0.0;
            wrk.clearA();
            VSRC vInst;
            for(Device dev: devices){
                if(dev instanceof VSRC){
                    vInst = (VSRC)dev;
                    if (vInst.isDGiven()) {
                        if (vInst.isDF2Given())
                            cv.setF2Given(true);
                        if (vInst.isDF1Given() && (mode ==
                                DISTOR.RHSF1)) {
                            mag = vInst.getDF1Mag();
                            phase = vInst.getDF1Phase();
                        } else if (vInst.isDF2Given() && (mode ==
                                DISTOR.RHSF2)) {
                            mag = vInst.getDF2Mag();
                            phase = vInst.getDF2Phase();
                        }
                        if ((vInst.isDF1Given() && mode == DISTOR.RHSF1) ||
                                (vInst.isDF2Given() && mode == DISTOR.RHSF2)) {
                            wrk.setAReal(vInst.getBranchIndex(), 0.5 * mag *
                                    MathLib.cos(MathLib.PI *
                                    phase / 180.0));
                            wrk.setAImag(vInst.getBranchIndex(), 0.5 * mag *
                                    MathLib.sin(MathLib.PI *
                                    phase / 180.0));
                        }
                    }
                }
                if(dev instanceof ISRC) {
                    ISRC iInst = (ISRC)dev;
                    if (iInst.isDGiven()) {
                        if (iInst.isDF2Given())
                            cv.setF2Given(true);
                        if ((iInst.isDF1Given()) && (mode == DISTOR.RHSF1)) {
                            mag = iInst.getDF1Mag();
                            phase = iInst.getDF1Mag();
                        } else if ((iInst.isDF2Given()) &&
                                (mode == DISTOR.RHSF2)) {
                            mag = iInst.getDF2Mag();
                            phase = iInst.getDF2Phase();
                        }
                        if ((iInst.isDF1Given() && mode == DISTOR.RHSF1)
                                || (iInst.isDF2Given() && mode == DISTOR.RHSF2)) {
                            wrk.setAReal(iInst.getPosrcIndex(), -0.5 * mag
     * MathLib.cos(MathLib.PI *
                                    phase / 180.0));
                            wrk.setAReal(iInst.getNegateIndex(), 0.5 * mag *
                                    MathLib.cos(MathLib.PI *
                                    phase / 180.0));
                            wrk.setAImag(iInst.getPosrcIndex(), -0.5 * mag *
                                    MathLib.sin(MathLib.PI *
                                    phase / 180.0));
                            wrk.setAImag(iInst.getNegateIndex(), 0.5 * mag *
                                    MathLib.sin(MathLib.PI *
                                    phase / 180.0));
                        }
                    }
                }
            }
            break;
        default:
            return false;
            break;
        }
     
    }
     */
}
