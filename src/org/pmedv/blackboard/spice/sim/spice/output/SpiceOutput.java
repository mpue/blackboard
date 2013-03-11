/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import java.util.Date;

import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;
import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEventListener;
import org.pmedv.blackboard.spice.sim.spice.model.sources.SourceElement;

import javolution.util.FastList;

/**
 *
 * @author owenbad
 */
public abstract class SpiceOutput implements AnalysisEventListener {

    protected String title;
    protected String analName;
    protected String refName;
    protected Date date;
    protected FastList<SourceElement> srcElements = new FastList<SourceElement>();

    public SpiceOutput(String title) {
        this.title = title;
    }

    public void addElement(SourceElement element) {
        srcElements.add(element);
    }

    public abstract void update(AnalysisEvent evt);

    public abstract void beginOutput(AnalysisEvent evt);
}
