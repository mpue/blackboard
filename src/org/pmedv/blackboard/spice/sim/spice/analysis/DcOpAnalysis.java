/*
 * DcOpAnalysis.java
 *
 * Created on April 28, 2006, 7:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.analysis;

import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class DcOpAnalysis extends Analysis {

    /** Creates a new instance of DcOpAnalysis */
    public DcOpAnalysis() {
        name = "Operating Point Analysis";
    }

    public boolean analyze(boolean notUsed) {
        beginOutput();
        if (!doDCOperations()) {
            StandardLog.severe("DC solution failed");
            fireNonConvEvent();
            return false;
        }
        mode.setModes(initSmSig);
        if (ckt.load(mode)) {
            update();
        } else {
            StandardLog.warning("Error: Circuit reload failed.");
            return false;
        }
        endOutput();
        return true;
    }
}
