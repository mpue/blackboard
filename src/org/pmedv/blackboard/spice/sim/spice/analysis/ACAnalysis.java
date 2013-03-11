/*
 * ACAnalysis.java
 *
 * Created on March 30, 2006, 11:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.f
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.EnumSet;

import org.pmedv.blackboard.spice.sim.output.Statistics;
import org.pmedv.blackboard.spice.sim.output.StopWatch;
import org.pmedv.blackboard.spice.sim.spice.EnumConsts.MODE;
import org.pmedv.blackboard.spice.sim.spice.output.ACPlotOutput;

import javolution.util.FastList;
import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class ACAnalysis extends Analysis {

    private double startFreq;
    private double stopFreq;

    /* Multiplier for decade/octave stepping */
    private double freqDelta;

    /* Previous frequency */
    private double saveFreq;
    private STEP_MODE stepType;
    private int steps;
    public static final String AC_TIME = "acTime";
    private StopWatch acTimer;
    private static final EnumSet acMode = EnumSet.of(MODE.AC);

    public static enum STEP_MODE {

        DECADE, OCTAVE, LINEAR, START, STOP, STEPS
    };
//    private boolean pause;

    /** Creates a new instance of ACAnalysis */
    public ACAnalysis() {
        name = "AC Analysis";
        desc = name;
        acTimer = Statistics.addTime(AC_TIME);
    }

    public double getFreqDelta() {
        return freqDelta;
    }

    public void setFreqDelta(double freqDelta) {
        this.freqDelta = freqDelta;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        if (steps <= 0) {
            return;
        }
        this.steps = steps;
    }

    public double getSaveFreq() {
        return saveFreq;
    }

    public void setSaveFreq(double saveFreq) {
        this.saveFreq = saveFreq;
    }

    public double getStartFreq() {
        return startFreq;
    }

    public void setStartFreq(double startFreq) {
        this.startFreq = startFreq;
    }

    public STEP_MODE getStepType() {
        return stepType;
    }

    public void setStepType(STEP_MODE stepType) {
        this.stepType = stepType;
    }

    public double getAcStopFreq() {
        return stopFreq;
    }

    public void setStopFreq(double stopFreq) {
        if (stopFreq <= 0.0) {
            stopFreq = 0;
            return;
        }
        this.stopFreq = stopFreq;
    }

    public boolean analyze(boolean restart) {
        double freq = 0;
        double freqTol;
//X        ckt.setCurAnal(ANAL.DC);
//X        ckt.setAnalInit(true);
        if (saveFreq == 0 || restart) {
            if (steps < 1) {
                steps = 1;
            }
            switch (stepType) {
                case DECADE:
                    freqDelta = Math.exp(Math.log(10.0) / steps);
                    break;
                case OCTAVE:
                    freqDelta = Math.exp(Math.log(2.0) / steps);
                    break;
                case LINEAR:
                    if (steps - 1 > 1) {
                        freqDelta = (stopFreq - startFreq)
                                / (steps - 1);
                    } else {
                        freqDelta = 0;
                    }
                    break;
                default:
                    return false;
            }
            /*X
            xckt.doDCOperations(true);
            xckt.dump(MODE.DCOP, 0.0);
            xckt.opSave(true, 0.0);
            } else */
            if (!doDCOperations()) {
                StandardLog.severe("AC operating point failed");
                fireNonConvEvent();
                return false;
            }
            mode.setModes(initSmSig);
            ckt.load(mode);
            if (env.isKeepOpInfo()) {
                String tmp = name;
                name = "AC Operating Point";
                beginOutput();
                update();
                endOutput();
                name = tmp;
            }
            beginOutput("Frequency");
            if (stepType != STEP_MODE.LINEAR) {
                for (FastList.Node<AnalysisEventListener> n = listeners.head(),
                        end = listeners.tail(); (n = n.getNext()) != end;) {
                    AnalysisEventListener l = n.getValue();
                    //!
                    if (l instanceof ACPlotOutput) {
                        ((ACPlotOutput)l).setLogarithmic();
                    }
                }
            }
            freq = startFreq;
        } else {
            beginOutput();
            freq = saveFreq;
            saveFreq = 0;
        }
        switch (stepType) {
            case DECADE:
            case OCTAVE:
                freqTol = freqDelta * stopFreq * env.getRelTol();
                break;
            case LINEAR:
                freqTol = freqDelta * env.getRelTol();
                break;
            default:
                return false;
        }
        //X ckt.setAnalInit(true);
        //X            ckt.setCurAnal(ANAL.AC);
        sweep:
        while (freq <= stopFreq + freqTol) {
            if (pause) {
                saveFreq = freq;
                StandardLog.info("User paused");
            }
            tmprl.setOmega(2.0 * Math.PI * freq);
            mode.setModes(acMode);
            if (!acIterate()) {
                return false;
            }
            update(freq);
            //X                ckt.setCurAnal(ANAL.AC);
            switch (stepType) {
                case DECADE:
                case OCTAVE:
                    freq *= freqDelta;
                    if (freqDelta == 1) {
                        break sweep;
                    }
                    break;
                case LINEAR:
                    freq += freqDelta;
                    if (freqDelta == 0) {
                        break sweep;
                    }
                    break;
                default:
                    return false;
            }
        }
        acTimer.stop();
        endOutput();
        return true;
    }
}
