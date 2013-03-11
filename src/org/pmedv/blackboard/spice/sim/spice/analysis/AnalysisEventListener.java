/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.analysis;

import java.util.EventListener;

/**
 *
 * @author Kristopher T. Beck
 */
public interface AnalysisEventListener extends EventListener{
    public void update(AnalysisEvent evt);
    public void beginOutput(AnalysisEvent evt);
    public void endOutput(AnalysisEvent evt);
    public void restartOutput(AnalysisEvent evt);
}
