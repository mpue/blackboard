/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.EventObject;

/**
 *
 * @author Kristopher T. Beck
 */
public class AnalysisEvent extends EventObject {

    public String refName;
    public double refValue;
    public String msg;

    public AnalysisEvent(Analysis source) {
        super(source);

    }

    public AnalysisEvent(Analysis source, double refValue) {
        super(source);
        this.refValue = refValue;
    }


    public Analysis getAnalysis(){
        return (Analysis)getSource();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public double getRefValue() {
        return refValue;
    }

    public void setRefValue(double refValue) {
        this.refValue = refValue;
    }
    
}
