/*
 * DcTrCurv.java
 *
 * Created on April 28, 2006, 7:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.EnumSet;

import org.pmedv.blackboard.spice.sim.spice.Constants;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.discrete.ResInstance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.isrcs.ISrcInstance;
import org.pmedv.blackboard.spice.sim.spice.model.sources.vsrcs.VSrcInstance;

import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class DcTrCurv extends Analysis {

    public static int NEST_LEVEL = 2;
    public static int TEMP_CODE = 1023;
    private static final EnumSet<MODE> floatMode = EnumSet.of(MODE.DCTRANCURVE, MODE.INIT_FLOAT);
    private static final EnumSet<MODE> jctMode = EnumSet.of(MODE.DCTRANCURVE, MODE.INIT_JCT);
    private static final EnumSet<MODE> predMode = EnumSet.of(MODE.DCTRANCURVE, MODE.INIT_PRED);

    public static class TransSrcs {

        public double start;
        public double stop;
        public double step;
        public double valueSave;
        public boolean gSave;
        public String name;
        public Instance inst;
        public int type;
        public int set;
    }
    public TransSrcs[] trvcs;// = new TransSrcs[NEST_LEVEL];
    int nestLevel;
    int nestState;
    /*
    public static enum DCT {

    START1, STOP1, STEP1, NAME1, TYPE1, START2, STOP2,
    STEP2, NAME2, TYPE2
    };
     */

    /** Creates a new instance of DcTrCurv */
    public DcTrCurv() {
        name = "DC TransCurve";
    }

    public TransSrcs[] getTrvcs() {
        return trvcs;
    }

    public void setTrvcs(TransSrcs[] trvcs) {
        this.trvcs = trvcs;
    }

    public boolean analyze(boolean restart) {
        int l;
        TransSrcs nest = null;
        boolean firstTime = true;
        String varUid = null;
        if (!restart && nestState >= 0) {
            l = nestState;
            restartOutput();
        } else {
            tmprl.setTime(0);
            tmprl.setDelta(trvcs[0].step);
            tmprl.setOrder(1);
            for (int i = 0; i < 7; i++) {
                tmprl.setOldDeltaAt(i, tmprl.getDelta());
            }
            mode.setModes(jctMode);
            for (l = 0; l <= nestLevel; l++) {
                nest = trvcs[l];
                if (nest.name.equals("temp")) {
                    nest.valueSave = env.getTemp();
                    nest.type = TEMP_CODE;
                    env.setTemp(nest.start + Constants.CtoK);
                    if (l == 0) {
                        varUid = "temp-sweep";
                    }
                    ckt.temperature();
                } else {
                    Instance dev = ckt.findInstance(nest.name);
                    if (dev instanceof ResInstance) {
                        ResInstance rinst = (ResInstance) dev;
                        nest.inst = rinst;
                        nest.valueSave = rinst.getResist();
                        rinst.setResist(nest.start);
                        if (l == 0) {
                            varUid = "res-sweep";
                        }
                        ckt.temperature();
                    } else if (dev instanceof VSrcInstance) {
                        VSrcInstance vinst = (VSrcInstance) dev;
                        nest.inst = vinst;
                        nest.valueSave = vinst.getDcValue();
                        vinst.setDcValue(nest.start);
                        if (l == 0) {
                            varUid = "v-sweep";
                        }
                    } else if (dev instanceof ISrcInstance) {
                        ISrcInstance iInst = (ISrcInstance) dev;
                        nest.inst = iInst;
                        nest.valueSave = iInst.getDcValue();
                        iInst.setDcValue(nest.start);
                        if (l == 0) {
                            varUid = "i-sweep";
                        }
                    } else {
                        StandardLog.severe("DCtrCurv: source / ResModel "
                                + nest.name + "not in circuit");
                        return false;
                    }
                    break;
                }
            }
            beginOutput(varUid);
        }
        l = 0;
        loop:
        while (true) {
            nextstep:
            do {
                nest = trvcs[l];
                if (nest.inst instanceof VSrcInstance) {
                    if ((((VSrcInstance) nest.inst).getDcValue())
                            * Math.signum(nest.step)
                            - Math.signum(nest.step) * nest.stop
                            > Constants.EPS_ZERO * 1e+03) {
                        l++;
                        firstTime = true;
                        mode.setModes(jctMode);
                        if (l > nestLevel) {
                            break loop;
                        }
                        break nextstep;
                    }
                } else if (nest.inst instanceof ISrcInstance) {
                    if ((((ISrcInstance) nest.inst).getDcValue())
                            * Math.signum(nest.step)
                            - Math.signum(nest.step) * nest.stop
                            > Constants.EPS_ZERO * 1e+03) {
                        l++;
                        firstTime = true;
                        mode.setModes(jctMode);
                        if (l > nestLevel) {
                            break loop;
                        }
                        break nextstep;
                    }

                } else if (nest.inst instanceof ResInstance) {
                    if ((((ResInstance) nest.inst).getResist())
                            * Math.signum(nest.step)
                            - Math.signum(nest.step) * nest.stop
                            > Constants.EPS_ZERO * 1e+03) {
                        l++;
                        firstTime = true;
                        mode.setModes(jctMode);
                        if (l > nestLevel) {
                            break loop;
                        }
                        break nextstep;
                    }
                } else if (nest.type == TEMP_CODE) {
                    if (((env.getTemp()) - Constants.ZERO_CELSIUS) * Math.signum(nest.step)
                            - Math.signum(nest.step) * nest.stop
                            > Constants.EPS_ZERO * 1e+03) {
                        l++;
                        firstTime = true;
                        mode.setModes(jctMode);
                        if (l > nestLevel) {
                            break loop;
                        }
                        break nextstep;
                    }
                }
                while (l > 0) {
                    l--;
                    nest = trvcs[l];
                    if (nest.inst instanceof VSrcInstance) {
                        ((VSrcInstance) nest.inst).setDcValue(nest.start);
                    } else if (nest.inst instanceof ISrcInstance) {
                        ((ISrcInstance) nest.inst).setDcValue(nest.start);
                    } else if (nest.type == TEMP_CODE) {
                        env.setTemp(nest.start + Constants.ZERO_CELSIUS);
                        ckt.temperature();
                    } else if (nest.inst instanceof ResInstance) {
                        ((ResInstance) nest.inst).setResist(nest.start);
                        nest.inst.load(mode);
                    }
                }
                double[] tmp = stateTable.getColumnAt(tmprl.getMaxOrder() + 1);

                for (int j = tmprl.getMaxOrder(); j >= 0; j--) {
                    stateTable.setColumnAt(j + 1, stateTable.getColumnAt(j));
                }
                stateTable.setColumnAt(0, tmp);
                if (!iterate(env.getDcTrcvMaxIter())) {
                    if (!doDCOperations()) {
                        return false;
                    }
                }
                /*
                } else {
                if(nest.inst instanceof VSrcInstance) {
                wrk.setStep(((VSrcInstance)(nest.inst)).getDcValue());
                } else if(nest.inst instanceof ISrcInstance) {
                wrk.setStep(((ISrcInstance)nest.inst).getDcValue());
                } else if(nest.inst instanceof ResInstance) {
                wrk.setStep(((ResInstance)nest.inst).getResist());
                } else if(nest.type == TEMP_CODE) {
                wrk.setStep(env.getTemp() - Constants.ZERO_CELSIUS);
                }
                if(firstTime) {
                converged = xckt.doDCOperations(true);
                xckt.dump(MODE.DCOP, wrk.getStep());
                xckt.opSave(false, wrk.getStep());
                if(!converged)
                return(converged);
                } else {
                converged = getNi().iterate(env.getDcTrcvMaxIter());
                xckt.callHybrids();
                if((!converged) || !(xckt.getOutputQueue().isEmpty())) {
                converged = xckt.doDCOperations(false);
                xckt.dump(MODE.DCTRANCURVE, wrk.getStep());
                xckt.opSave(false, wrk.getStep());
                if(!converged)
                return(converged);
                }
                }
                }
                }
                 */
                mode.setModes(predMode);
                if (nest.inst instanceof VSrcInstance) {
                    tmprl.setTime(((VSrcInstance) nest.inst).getDcValue());
                } else if (nest.inst instanceof ISrcInstance) {
                    tmprl.setTime(((ISrcInstance) nest.inst).getDcValue());
                } else if (nest.inst instanceof ResInstance) {
                    tmprl.setTime(((ResInstance) nest.inst).getResist());
                } else {
                    tmprl.setTime(env.getTemp() - Constants.ZERO_CELSIUS);
                }
                update(tmprl.getTime());
                if (firstTime) {
                    firstTime = false;
                    System.arraycopy(stateTable.getColumnAt(0), 0, stateTable.getColumnAt(1), 0, stateTable.getSize());
                }
            } while (false);

            if (nest.inst instanceof VSrcInstance) {
                ((VSrcInstance) nest.inst).setDcValue(((VSrcInstance) nest.inst).getDcValue() + nest.step);
            } else if (nest.inst instanceof ISrcInstance) {
                ((ISrcInstance) nest.inst).setDcValue(((ISrcInstance) nest.inst).getDcValue() + nest.step);
            } else if (nest.inst instanceof ResInstance) {
                ((ResInstance) nest.inst).setResist(((ResInstance) nest.inst).getResist() + nest.step);
                nest.inst.load(mode);
            } else if (nest.type == TEMP_CODE) {
                tmprl.setTime(env.getTemp() + nest.step);
                ckt.temperature();
            }
            if (pause) {
                nestState = l;
                return false;
            }
        }
        for (int i = 0; i <= nestLevel; i++) {
            nest = trvcs[i];
            if (nest.inst instanceof VSrcInstance) {
                ((VSrcInstance) nest.inst).setDcValue(nest.valueSave);
            } else if (nest.inst instanceof ISrcInstance) {
                ((ISrcInstance) nest.inst).setDcValue(nest.valueSave);
            } else if (nest.inst instanceof ResInstance) {
                ((ResInstance) nest.inst).setResist(nest.valueSave);
                nest.inst.load(mode);
            } else if (nest.type == TEMP_CODE) {
                env.setTemp(nest.valueSave);
                ckt.temperature();
            }
        }
        endOutput();
        return true;
    }
}
