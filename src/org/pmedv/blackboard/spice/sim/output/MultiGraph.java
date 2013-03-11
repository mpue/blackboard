/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.output;

import javax.swing.JPanel;

import javolution.util.FastList;
import javolution.util.FastSet;
import ktb.math.numbers.Real;

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

/**
 *
 * @author Kristopher T. Beck
 */
public class MultiGraph extends JPanel {

    private XYSeriesCollection series;
    public JFreeChart chart;
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;

    /**
     *  Creates a new MultiGraph object
     */
    public MultiGraph(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation) {
        this.series = new XYSeriesCollection();
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        chart = ChartFactory.createXYLineChart(title,
                xAxisLabel, yAxisLabel, series, PlotOrientation.VERTICAL, true,
                true, false);
        NumberAxis xAxis = new NumberAxis(xAxisLabel);
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis(yAxisLabel);
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        XYPlot plot = new XYPlot(series, xAxis, yAxis, renderer);
        plot.setOrientation(orientation);
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 200.0);
        final ChartPanel chartPanel = new ChartPanel(chart);
        //final JPanel content = new JPanel(new BorderLayout());
        chartPanel.setMinimumDrawWidth( 0 );
        chartPanel.setMinimumDrawHeight( 0 );
        chartPanel.setMaximumDrawWidth( 1920 );
        chartPanel.setMaximumDrawHeight( 1200 );
        add(chartPanel);
        // chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    }

    public void addDataSet(String name) {
        series.addSeries(new XYSeries(name));
    }

    public void addDataSets(FastSet<String> set) {
        for (FastSet.Record r = set.head(), end = set.tail(); (r = r.getNext()) != end;) {
            String str = set.valueOf(r);
            addDataSet(str);
        }
    }

    public void setLogarithmic() {
        chart.getXYPlot().setDomainAxis(new LogarithmicAxis(xAxisLabel));
    }

    public void update(double refValue, FastList<Real> data) {
        for (int i = 0; i < series.getSeriesCount(); i++) {
            XYSeries s = series.getSeries(i);
            s.add(refValue, data.get(i).get());
        }
//        chart.fireChartChanged();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        chart.setTitle(title);
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
        chart.getXYPlot().getDomainAxis().setLabel(xAxisLabel);
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
        chart.getXYPlot().getRangeAxis().setLabel(yAxisLabel);
    }

}
