/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import java.io.PrintStream;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.EnvVars;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;
import org.pmedv.blackboard.spice.sim.spice.analysis.NonConvergenceListener;
import org.pmedv.blackboard.spice.sim.spice.node.VoltageNode;

import javolution.lang.MathLib;
import javolution.util.FastTable;
import javolution.util.StandardLog;
import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class DefaultOutput extends SpiceOutput implements NonConvergenceListener {

    protected PrintStream out;
    protected PrintStream ncOut;
    protected FastTable<String> nodeNames;

    public DefaultOutput(String title) {
        super(title);
        ncOut = System.err;
        out = System.out;
    }

    public void setNcOut(PrintStream ncOut) {
        this.ncOut = ncOut;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public void update(AnalysisEvent evt) {
        WorkEnv wrk = evt.getAnalysis().getWrk();
        Circuit ckt = evt.getAnalysis().getCircuit();
        out.println("Node Voltages");
        out.println("------------------");
        out.format("%-30s %-20s\n", "Node", "Voltage");
        for (int i = 1; i < wrk.getSize(); i++) {
            out.format("%-30s %-20g\n", nodeNames.get(i), wrk.getRhsOldRealAt(i));
        }
    }

    public void nonConvergence(AnalysisEvent evt) {
        Circuit ckt = evt.getAnalysis().getCircuit();
        WorkEnv wrk = ckt.getWrk();
        EnvVars env = ckt.getEnv();
        ncOut.println("Last Complex Voltages");
        ncOut.println("------------------");
        ncOut.format("%-30s %-20s %-20s\n", "Node", "Last Voltage", "Previous Iter");
        nodeNames = ckt.getNodeNames();
        for (int i = 1; i < nodeNames.size(); i++) {
            Node node = ckt.getNode(i);
            double tol;
            if (node.getID().startsWith("#branch") || !node.getID().startsWith("#")) {
                double newd = wrk.getRhsOldRealAt(i);
                double old = wrk.getRhsRealAt(i);
                ncOut.format("\n%-30s %-20g %-20g", node.getID(), newd, old);
                if (node instanceof VoltageNode) {
                    tol = env.getRelTol() * (MathLib.max(MathLib.abs(old),
                            MathLib.abs(newd))) + env.getVoltTol();
                } else {
                    tol = env.getRelTol() * (MathLib.max(MathLib.abs(old),
                            MathLib.abs(newd))) + env.getAbsTol();
                }
                if (MathLib.abs(newd - old) > tol) {
                    ncOut.print(" *");
                }
            }
        }
    }

    public void acUpdate(AnalysisEvent evt) {
        Circuit ckt = evt.getAnalysis().getCircuit();
        WorkEnv wrk = ckt.getWrk();
        StandardLog.info("Node Voltages");
        StandardLog.info("------------------");
        out.format("%-30s %-20g %-20g", "Node", "Voltage real", "imaginary");
        for (int i = 1; i < wrk.getSize(); i++) {
            Complex c = wrk.getRhsOldAt(i);
            out.format("%-30s %-20g %-20g", nodeNames.get(i), c.getReal(), c.getImag());
        }
    }

    public void beginOutput(AnalysisEvent evt) {
        Circuit ckt = evt.getAnalysis().getCircuit();
        nodeNames = ckt.getNodeNames();
        if (out == null) {
            out = System.out;
        }
        if (ncOut == null) {
            ncOut = System.err;
        }
    }

    public void endOutput(AnalysisEvent evt) {
    }

    public void restartOutput(AnalysisEvent evt) {
    }
}
