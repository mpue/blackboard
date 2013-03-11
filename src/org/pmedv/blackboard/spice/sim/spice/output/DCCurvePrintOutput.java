/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import java.io.PrintStream;

import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;
import org.pmedv.blackboard.spice.sim.spice.model.sources.SourceElement;

import javolution.util.FastList;
import ktb.math.numbers.Complex;

/**
 *
 * @author owenbad
 */
public class DCCurvePrintOutput extends PrintOutput {

    public DCCurvePrintOutput(String title) {
        super(title);
        refName = "Sweep";
    }

    public DCCurvePrintOutput(String title, PrintStream out) {
        super(title, out);
    }

    @Override
    public void update(AnalysisEvent evt) {
        out.format("%-10d %-20g", index, evt.refValue);
        for (FastList.Node<SourceElement> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            SourceElement src = n.getValue();
            Complex c = src.getValue();
            out.format("%-20g", c.getReal());
        }
        out.println();
        index++;
    }
}
