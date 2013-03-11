/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import java.io.PrintStream;

import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;
import org.pmedv.blackboard.spice.sim.spice.analysis.TransientAnalysis;
import org.pmedv.blackboard.spice.sim.spice.model.sources.SourceElement;

import javolution.util.FastList;
import ktb.math.numbers.Complex;

/**
 *
 * @author owenbad
 */
public class TranPrintOutput extends PrintOutput {

    public TranPrintOutput(String title) {
        super(title);
    }

    public TranPrintOutput(String title, PrintStream out) {
        super(title, out);
    }

    @Override
    public void update(AnalysisEvent evt) {
        if (evt.getAnalysis() instanceof TransientAnalysis) {
            out.format("%-10s %-20g", index, evt.refValue);
            for (FastList.Node<SourceElement> n = srcElements.head(),
                    end = srcElements.tail(); (n = n.getNext()) != end;) {
                SourceElement src = n.getValue();
                Complex c = src.getValue();
                out.format("%-20g", c.getReal());
            }
        }
        out.println();
        index++;
    }

    @Override
    public void beginOutput(AnalysisEvent evt) {
        out.println(analName);
        out.println("------------------");
        out.format("%-10s %-10s", "Index", "Time");
        for (FastList.Node<SourceElement> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            SourceElement src = n.getValue();
            out.format("%-20s", src.getName());
        }
        out.println();
        index = 0;
    }
}
