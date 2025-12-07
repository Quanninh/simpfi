package com.simpfi.ui.panel;

import com.simpfi.ui.Panel;

import com.simpfi.object.TrafficStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * A UI panel used for displaying simulation statistics. This class extends
 * {@link Panel}.
 */
public class StatisticsPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private TrafficStatistics stats;
	private DefaultCategoryDataset speedDataset;
	private JLabel avrSpeedLabel;
	private ChartPanel chartPanel;

	public StatisticsPanel(TrafficStatistics stats){
		this.stats = stats;
		initUI();
	}

	private void initUI(){
		setLayout(new BorderLayout());
		speedDataset = new DefaultCategoryDataset();
		
		// Draw line chart
		JFreeChart chart = ChartFactory.createLineChart(
			"Average Speed Over Time",
			"Step",
			"Speed(km/h)",
			speedDataset
		);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(300, 400));
		add(chartPanel, BorderLayout.CENTER);

		avrSpeedLabel = new JLabel("Average speed: 0 km/h");
		JLabel summaryPanel = new JLabel();
		summaryPanel.add(avrSpeedLabel);
		add(summaryPanel, BorderLayout.SOUTH);
	}

	/** Update chart at each simulation timestep */
	public void updatePanel(int step){
		double avrSpeed = stats.getAverageSpeed(); 
		speedDataset.addValue(avrSpeed, "Average speed", String.valueOf(step));
		avrSpeedLabel.setText(String.format("Average speed: %.2f km/h", avrSpeed));
		chartPanel.repaint();
	}
}
