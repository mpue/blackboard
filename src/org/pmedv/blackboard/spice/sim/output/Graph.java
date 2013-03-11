/* Graph.java
/*
 *
 * Created on April 17, 2006, 1:48 AM
 *
/**
 *
 * @author Kristopher T. Beck
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.output;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ktb.math.numbers.Numeric;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.pmedv.blackboard.spice.sim.spice.analysis.AnalysisEventListener;

/**
 *
 * @author Kristopher T. Beck
 */
public abstract class Graph extends JPanel implements AnalysisEventListener {

    private XYSeries series;
    public JFreeChart chart;
    protected int lowRange = 0;
    protected int highRange = 200;

    /**
     *  Creates a new Graph object
     */
    protected Graph() {
    }

    public Graph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation) {
        this.series = new XYSeries("Data");
        final XYSeriesCollection dataSet = new XYSeriesCollection(this.series);
        chart = ChartFactory.createXYLineChart(title,
                "Time", "Value", dataSet, PlotOrientation.VERTICAL, true,
                true, false);
        NumberAxis xAxis = new NumberAxis(xAxisLabel);
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis(yAxisLabel);
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        XYPlot plot = new XYPlot(dataSet, xAxis, yAxis, renderer);
        plot.setOrientation(orientation);
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 200.0);
        final ChartPanel chartPanel = new ChartPanel(chart);
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    }

    public void setLogarithmic(String refName, Numeric ref) {
        chart.getXYPlot().setDomainAxis(new LogarithmicAxis(refName));
    }

    public void setValueRange(int low, int high) {
        lowRange = low;
        highRange = high;
        chart.getXYPlot().getRangeAxis().setRange(low, high);
    }
}
