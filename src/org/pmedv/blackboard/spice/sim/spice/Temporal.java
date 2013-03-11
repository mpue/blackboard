/*
 * this.java
 *
 * Created on July 3, 2006, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice;

import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class Temporal {

    private int order = 1;
    private int maxOrder = 2;
    private double time;
    private double finalTime;
    private double step;
    private double maxStep;
    private double initTime;
    private int timeIndex;
    private int breakIndex;//X
    private double rampTime;
    private boolean _break;
    private double[] breakPoints = new double[2];
    private double minBreak;
    private double lastBreak = Double.MAX_VALUE;//X
    private double currentBreak = Double.MAX_VALUE;//X
    private double[] timePoints;
    private double sizeIncr;
    private int timeIncr;
    private double omega;
    private double delMin;
    private double delta;
    private double[] deltaOld = new double[7];
    private double saveDelta;
    private double[] ag = new double[7];
    private double[] agp = new double[7];

    /**
     * Creates a new instance of Temporal
     */
    public Temporal() {
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setRampTime(double rampTime) {
        this.rampTime = rampTime;
    }

    public double getRampTime() {
        return rampTime;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public double getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(double maxStep) {
        this.maxStep = maxStep;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setStep(long step) {
        this.step = step * 1e-9;
    }

    public double getOmega() {
        return omega;
    }

    public double getOmega(double freq) {
        return 2 * Math.PI * freq;
    }

    public void setOmega(double omega) {
        this.omega = omega;
    }

    public boolean isBreak() {
        return _break;
    }

    public void setBreak(boolean _break) {
        this._break = _break;
    }

    public double[] getBreakPoints() {
        return breakPoints;
    }

    public void setBreakPoints(double[] breakPoints) {
        this.breakPoints = breakPoints;
    }

    public double getBreakAt(int i) {
        return breakPoints[i];
    }

    public boolean addBreak(double time) {
//        System.out.println("adding break");
        if (time < this.time) {
            StandardLog.error("Breakpoint is in the past!");
            return false;
        }
        for (int i = 0; i < breakPoints.length; i++) {
            if (breakPoints[i] > time) {
                if (breakPoints[i] - time <= minBreak) {
                    StandardLog.finest(String.format("[t:%e] \t %e replaces %e\n", this.time, time, breakPoints[i]));
                    breakPoints[i] = time;
                    breakDump();
                    return true;
                }
                if (time - breakPoints[i - 1] <= minBreak) {
                    StandardLog.finest(String.format("[t:%e] \t %e skipped\n", this.time, time));
                    breakDump();
                    return true;
                }
                double[] tmp = new double[breakPoints.length + 1];
                System.arraycopy(breakPoints, 0, tmp, 0, i);
                tmp[i] = time;
                StandardLog.finest(String.format("[t:%e] \t %e added\n", time, time));
                System.arraycopy(breakPoints, i, tmp, i + 1, breakPoints.length - i);
                breakPoints = tmp;
                breakDump();
                return true;
            }
        }
        if (time - breakPoints[breakPoints.length - 1] <= minBreak) {
            StandardLog.finest(String.format("[t:%e] \t %e skipped (at the end)\n", this.time, time));
            breakDump();
            return true;
        }
        double[] tmp = new double[breakPoints.length + 1];

        StandardLog.finest(String.format("[t:%e] \t %e added at end\n", this.time, time));
        System.arraycopy(breakPoints, 0, tmp, 0, breakPoints.length);
        tmp[tmp.length - 1] = time;
        breakPoints = tmp;
        breakDump();
        return true;
    }

    public void setBreakAt(int i, double breakPoint) {
        breakPoints[i] = breakPoint;
    }

    public double getMinBreak() {
        return minBreak;
    }

    public void setMinBreak(double minBreak) {
        this.minBreak = minBreak;
    }

    public double getCurrentBreak() {
        return currentBreak;
    }

    public void setCurrentBreak(double currentBreak) {
        this.currentBreak = currentBreak;
    }

    public void setBreakIndex(int breakIndex) {
        this.breakIndex = breakIndex;
    }

    public double getLastBreak() {
        return lastBreak;
    }

    public void setLastBreak(double lastBreak) {
        this.lastBreak = lastBreak;
    }

    public int getBreakIndex() {
        return breakIndex;
    }

    public double getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(double finalTime) {
        this.finalTime = finalTime;
    }

    public void setFinalTime(long finalTime) {
        this.finalTime = finalTime * 1e-9;
    }

    public double getInitTime() {
        return initTime;
    }

    public void setInitTime(double initTime) {
        this.initTime = initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime * 1e-9;
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }

    public double getTimePointAt(int i) {
        return timePoints[i];
    }

    public void setTimePointAt(int i, double timePoint) {
        this.timePoints[i] = timePoint;
    }

    public double[] getTimePoints() {
        return timePoints;
    }

    public void setTimePoints(double[] timePoints) {
        this.timePoints = timePoints;
    }

    public int incrTimeIndex() {
        return ++this.timeIndex;
    }

    public void setTimeIncr(int timeIncr) {
        this.timeIncr = timeIncr;
    }

    public int getTimeIncr() {
        return timeIncr;
    }

    public void setIncrSize(double sizeIncr) {
        this.sizeIncr = sizeIncr;
    }

    public double getIncrSize() {
        return sizeIncr;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getOldDeltaAt(int i) {
        return deltaOld[i];
    }

    public void setOldDeltaAt(int i, double deltaOld) {
        this.deltaOld[i] = deltaOld;
    }

    public double getSaveDelta() {
        return saveDelta;
    }

    public void setSaveDelta(double saveDelta) {
        this.saveDelta = saveDelta;
    }

    public double getDelMin() {
        return delMin;
    }

    public void setDelMin(double delMin) {
        this.delMin = delMin;
    }

    public void breakDump() {
        for (int i = 0; i < breakPoints.length; i++) {
            StandardLog.finest("Breakpoint table entry " + i
                    + " is " + breakPoints[i]);
        }
    }

    public double[] getAg() {
        return ag;
    }

    public double getAgAt(int i) {
        return ag[i];
    }

    public double getAgpAt(int i) {
        return agp[i];
    }

    public void setAg(double[] array) {
        this.ag = array;
    }

    public void setAgAt(int i, double ag) {
        this.ag[i] = ag;
    }

    public void setAgp(double[] array) {
        this.agp = array;
    }

    public void setAgpAt(int i, double agp) {
        this.agp[i] = agp;
    }

    public boolean clrBreak0() {
        if (breakPoints.length > 2) {
            double[] tmp = new double[breakPoints.length - 1];
            System.arraycopy(breakPoints, 1, tmp, 0, breakPoints.length - 1);
            breakPoints = tmp;
        } else {
            breakPoints[0] = breakPoints[1];
            breakPoints[1] = finalTime;
        }
//        System.out.println("clearing break");
        breakDump();
        return true;
    }
}
