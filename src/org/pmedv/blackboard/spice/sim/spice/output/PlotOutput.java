/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import org.pmedv.blackboard.spice.sim.output.MultiGraph;
import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;
import org.pmedv.blackboard.spice.sim.spice.model.sources.SourceElement;

import javolution.util.FastList;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;

/**
 *
 * @author owenbad
 */
public class PlotOutput extends SpiceOutput {

    MultiGraph graph;

    public PlotOutput(String title) {
        super(title);
    }

    public void update(AnalysisEvent evt) {
        FastList<Real> data = FastList.newInstance();
        for (FastList.Node<SourceElement> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            SourceElement src = n.getValue();
            Complex c = src.getValue();
            data.add(Real.valueOf(c.getReal()));
        }
        graph.update(evt.refValue, data);
    }

    public void beginOutput(AnalysisEvent evt) {
        String str = evt.getRefName();
        if (str != null && !str.isEmpty()) {
            refName = str;
            graph.setxAxisLabel(refName);
        }
        for (FastList.Node<SourceElement> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            SourceElement src = n.getValue();
            if (src != null && src.getName() != null) {
            	graph.addDataSet(src.getName());            	
            }
        }
    }

    public void endOutput(AnalysisEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void restartOutput(AnalysisEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
