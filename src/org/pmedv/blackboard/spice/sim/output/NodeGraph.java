/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.output;

import java.awt.BorderLayout;

import ktb.math.numbers.Complex;
import ktb.math.numbers.Numeric;
import ktb.math.numbers.Real;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.spice.analysis.Analysis;
import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEvent;

/**
 *
 * @author Kristopher T. Beck
 */
public class NodeGraph extends Graph{

    String nodeName;
    Node node;
    private XYSeries series;

    /**
     * Creates a new NodeGraph object
     */
    public NodeGraph(String nodeName){
        this.nodeName = nodeName;
        this.series = new XYSeries("Data");
        final XYSeriesCollection dataSet = new XYSeriesCollection(this.series);
        chart = ChartFactory.createXYLineChart(nodeName,
                "Time", "Value", dataSet, PlotOrientation.VERTICAL, true,
                true, false);
        ValueAxis axis = chart.getXYPlot().getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        setValueRange(lowRange, highRange);
        final ChartPanel chartPanel = new ChartPanel(chart);
        this.setLayout(new BorderLayout());
        add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    }

    public void update(AnalysisEvent evt) {
        if (node == null) {
            Analysis a = (Analysis) evt.getSource();
            node = a.getCircuit().getNode(nodeName);
        }
        Numeric n = node.getValue();
        double value = 0;
        if (n instanceof Real) {
            value = ((Real) n).get();
        } else if (n instanceof Complex) {
            value = ((Complex) n).getReal();
        }
        series.add(evt.getRefValue(), value);
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
