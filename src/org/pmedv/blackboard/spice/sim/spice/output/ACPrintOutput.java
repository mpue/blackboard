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
public class ACPrintOutput extends PrintOutput {

    int index;

    public ACPrintOutput(String title) {
        super(title);
    }

    public ACPrintOutput(String title, PrintStream out) {
        super(title, out);
    }

    @Override
    public void update(AnalysisEvent evt) {
        out.format("%-10d %-20g", index, evt.refValue);
        for (FastList.Node<SourceElement> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            SourceElement src = n.getValue();
            Complex c = src.getValue();
            out.format("%-20g %-20g", c.getReal(), c.getImag());
        }
        out.println();
        index++;
    }

    @Override
    public void beginOutput(AnalysisEvent evt) {
        out.println(evt.getAnalysis().getName());
        out.println("------------------");
        out.format("%-10s %-20s ", "Index", "Frequency");
        for (FastList.Node<SourceElement> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            SourceElement src = n.getValue();
            Complex c = src.getValue();
            out.format("%-40s ", src.getName());
        }
        out.println();
        index = 0;
    }
}
