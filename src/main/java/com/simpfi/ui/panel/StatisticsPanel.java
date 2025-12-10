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

	/** The traffic statistics object used to fetch simulation data. */
	private TrafficStatistics stats;
	
	/** Dataset for the average speed line chart. */
	private DefaultCategoryDataset speedDataset;

	/** Dataset for the vehicle density bar chart. */
	private DefaultCategoryDataset vehicleDataset;

	/** Dataset for the travel time histogram. */
	private HistogramDataset travelTimeDataset;

	/** Label showing the current average speed. */
	private JLabel avrSpeedLabel;

	/** Label showing top congestion hotspots. */
	private JLabel hotspotLabel;

	/** Label describing the travel time histogram. */
	private JLabel travelTimeLabel;

	/** Chart panel for displaying average speed. */
	private ChartPanel speedChartPanel;

	/** Chart panel for displaying vehicle density. */
	private ChartPanel densityChartPanel;

	/** Chart panel for displaying travel time distribution. */
	private ChartPanel travelTimeChartPanel;

	/**
     * Constructs a StatisticsPanel with a given {@link TrafficStatistics} object.
     *
     * @param stats the TrafficStatistics instance providing simulation data
     */
	public StatisticsPanel(TrafficStatistics stats){
		this.stats = stats;
		initUI();
	}

	/**
     * Initializes the user interface components, including charts, labels, and layout.
     * <p>
     * This method creates three main sections in the panel:
     * <ul>
     *     <li>Speed Overview: Line chart and label showing average speed over time</li>
     *     <li>Traffic Density: Bar chart showing vehicle counts per edge and top congestion hotspots</li>
     *     <li>Travel Time Analysis: Histogram showing distribution of travel times</li>
     * </ul>
     * </p>
     */
	private void initUI(){
		setLayout(new BorderLayout());

		JLabel content = new JLabel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(new Color(245, 245, 245));

		JScrollPane scroll = new JScrollPane(content);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		add(scroll, BorderLayout.CENTER);

		Font titleFont = new Font("SansSerif", Font.BOLD, 16);
		Font textFont = new Font("SansSerif", Font.PLAIN, 13);

		speedDataset = new DefaultCategoryDataset();
		
		// Draw Average Speed chart
		JFreeChart chart = ChartFactory.createLineChart(
			"Average Speed Over Time",
			"Step",
			"Speed(km/h)",
			speedDataset
		);

		speedChartPanel = new ChartPanel(chart);
		speedChartPanel.setPreferredSize(new Dimension(400, 220));

		avrSpeedLabel = new JLabel("Average speed: 0 km/h");
		avrSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		avrSpeedLabel.setFont(textFont);

		content.add(createCard("Speed Overview", speedChartPanel, avrSpeedLabel, titleFont));

		// Draw Vehicle density chart
		vehicleDataset =  new DefaultCategoryDataset();
		JFreeChart vehicleChart = ChartFactory.createBarChart(
			"Vehicle Density per Edge",
			"Edge",
			"Density",
			vehicleDataset
		);
		densityChartPanel = new ChartPanel(vehicleChart);
		densityChartPanel.setPreferredSize(new Dimension(400, 220));

		// Congestion Hotspot
		hotspotLabel = new JLabel("Hotspot: none");
		hotspotLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hotspotLabel.setFont(textFont);
		content.add(createCard("Traffic Density", densityChartPanel, hotspotLabel, titleFont));

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
		travelTimeChartPanel.setPreferredSize(new Dimension(400, 220));
		travelTimeLabel = new JLabel("Travel Time Histogram");
		travelTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		travelTimeLabel.setFont(textFont);
		content.add(createCard("Travel Time Analysis", travelTimeChartPanel, travelTimeLabel, titleFont));
	}

	/**
     * Creates a card panel containing a title, a chart, and a label.
     * <p>
     * Each card is used to organize a specific type of statistic in the scrollable panel.
     * </p>
     *
     * @param title the title of the card
     * @param chart the chart component to display
     * @param label the label to display below the chart
     * @param titleFont the font to use for the title
     * @return a {@link JPanel} containing the chart, label, and title
     */
	private JPanel createCard(String title, JComponent chart, JLabel label, Font titleFont){
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout(10, 10));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.GRAY, 1),
			BorderFactory.createEmptyBorder(12, 12, 12, 12)
		));
		card.setBackground(Color.WHITE);

		JLabel header = new JLabel(title);
		header.setFont(titleFont);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		card.add(header, BorderLayout.NORTH);

		JPanel chartHolder = new JPanel(new BorderLayout());
		chartHolder.setOpaque(false);
		chartHolder.add(chart, BorderLayout.CENTER);
        card.add(chartHolder, BorderLayout.CENTER);

        card.add(label, BorderLayout.SOUTH);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        return card;
	}

	/**
     * Updates the panel for a specific simulation step.
     * <p>
     * Updates include:
     * <ul>
     *     <li>Average speed chart and label</li>
     *     <li>Vehicle density chart</li>
     *     <li>Top congestion hotspots label</li>
     *     <li>Travel time histogram</li>
     * </ul>
     * </p>
     *
     * @param step the current simulation step
     */
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
		SwingUtilities.invokeLater(() -> {
			double[] times = stats.getTravelTimesArray();
			
			if (times.length > 0) {
				HistogramDataset ds = new HistogramDataset();
				ds.addSeries("Travel Times", times, 20);
				
				travelTimeDataset = ds;
				travelTimeChartPanel.getChart().getXYPlot().setDataset(ds);
			}
			travelTimeChartPanel.repaint();
		});
		
		speedChartPanel.repaint();
		densityChartPanel.repaint();
	}

	/**
     * Wraps a chart and its corresponding label into a single JPanel.
     * <p>
     * This helper method is used internally to structure the layout of chart sections.
     * </p>
     *
     * @param chart the chart component
     * @param label the label component
     * @return a {@link JPanel} containing both the chart and label
     */
	private JPanel wrapPanel(JComponent chart, JLabel label){
		JPanel p = new JPanel(new BorderLayout());
		p.add(chart, BorderLayout.CENTER);
		p.add(label, BorderLayout.SOUTH);
		return p;
	}
}
