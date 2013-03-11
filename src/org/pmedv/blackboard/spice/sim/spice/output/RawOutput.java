/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;

/**
 *
 * @author owenbad
 */
public class RawOutput extends SpiceOutput {

    public RawOutput(String title) {
        super(title);
    }

    public void update(AnalysisEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void acUpdate(AnalysisEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void beginOutput(AnalysisEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void endOutput(AnalysisEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void restartOutput(AnalysisEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
