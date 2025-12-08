package com.simpfi.ui.panel;

import com.simpfi.ui.Panel;
import com.simpfi.object.TrafficStatistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.plot.PlotOrientation;


import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * A UI panel used for displaying simulation statistics. This class extends
 * {@link Panel}.
 */
public class StatisticsPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private TrafficStatistics stats;

	private DefaultCategoryDataset speedDataset;
	private DefaultCategoryDataset vehicleDataset;
	private HistogramDataset travelTimeDataset;

	private JLabel avrSpeedLabel;
	private JLabel hotspotLabel;

	private ChartPanel speedChartPanel;
	private ChartPanel densityChartPanel;
	private ChartPanel travelTimeChartPanel;


	public StatisticsPanel(TrafficStatistics stats){
		this.stats = stats;
		initUI();
	}

	private void initUI(){
		setLayout(new GridLayout(4, 1));

		speedDataset = new DefaultCategoryDataset();
		
		// Draw Average Speed chart
		JFreeChart chart = ChartFactory.createLineChart(
			"Average Speed Over Time",
			"Step",
			"Speed(km/h)",
			speedDataset
		);

		speedChartPanel = new ChartPanel(chart);
		speedChartPanel.setPreferredSize(new Dimension(220, 150));
		add(wrapPanel(speedChartPanel));

		avrSpeedLabel = new JLabel("Average speed: 0 km/h");
		// JLabel summaryPanel = new JLabel();
		// summaryPanel.add(avrSpeedLabel);
		add(avrSpeedLabel);

		// Draw Vehicle density chart
		vehicleDataset =  new DefaultCategoryDataset();
		JFreeChart vehicleChart = ChartFactory.createBarChart(
			"Vehicle Density per Edge",
			"Edge",
			"Density",
			vehicleDataset
		);
		densityChartPanel = new ChartPanel(vehicleChart);
		densityChartPanel.setPreferredSize(new Dimension(220, 150));
		add(wrapPanel(densityChartPanel));

		// Congestion Hotspot
		hotspotLabel = new JLabel("Hotspot: none");
		add(hotspotLabel);

		// Travel Time Distribution
		travelTimeDataset = new HistogramDataset();
		JFreeChart travelChart = ChartFactory.createHistogram(
			"Travel Time Distribution",
			"Travel Time (s)",
			"Frequency",
			travelTimeDataset,
			PlotOrientation.VERTICAL,
			true,
			true,
			false
		);
		travelTimeChartPanel = new ChartPanel(travelChart);
		travelTimeChartPanel.setPreferredSize(new Dimension(220, 150));
		add(wrapPanel(travelTimeChartPanel));
	}

	private JPanel wrapPanel(JComponent chart){
        JPanel p = new JPanel(new BorderLayout());
        p.add(chart, BorderLayout.CENTER);
        return p;
    }

	/** Update chart at each simulation timestep */
	public void updatePanel(int step){
		// Averate Speed
		double avrSpeed = stats.getAverageSpeed(); 
		speedDataset.addValue(avrSpeed, "Average speed", String.valueOf(step));
		avrSpeedLabel.setText(String.format("Average speed: %.2f km/h", avrSpeed));

		// Vehicle Density
		Map<String, Integer> densityMap = stats.getEdgeDensity();
		vehicleDataset.clear();
		for (var e : densityMap.entrySet()){
			vehicleDataset.addValue(e.getValue(), "Density", e.getKey());
		}

		// Hotspots (top 3 edges with the highest density)
		String hotspots = stats.getTopCongestionHostspots(3);
		hotspotLabel.setText("Hotspots: " + hotspots);

		// Travel Time Distribution
		double[] times = stats.getTravelTimesArray();
		if (times.length > 0){
			travelTimeDataset = new HistogramDataset();
			travelTimeDataset.addSeries("Travel Times", times, 10);
			travelTimeChartPanel.getChart().getXYPlot().setDataset(travelTimeDataset);
			travelTimeChartPanel.repaint();
		}
		speedChartPanel.repaint();
		densityChartPanel.repaint();
	}
}
