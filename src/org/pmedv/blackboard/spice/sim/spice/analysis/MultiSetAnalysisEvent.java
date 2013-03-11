/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.List;

import javolution.util.FastList;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class MultiSetAnalysisEvent extends AnalysisEvent {

    List<String> dataIds;
    FastList<Real> data;

    public MultiSetAnalysisEvent(Analysis source, List<String> data) {
        super(source);
        this.dataIds = data;
    }

    MultiSetAnalysisEvent(Analysis source, double regValue, FastList<Real> data) {
        super(source);
        this.data = data;

    }

    MultiSetAnalysisEvent(Analysis source) {
        super(source);
    }

    public FastList<Real> getData() {
        return data;
    }

    public void setData(FastList<Real> data) {
        this.data = data;
    }

    public List<String> getDataIds() {
        return dataIds;
    }

    public void setDataIds(List<String> dataIds) {
        this.dataIds = dataIds;
    }
}
