package com.simpfi.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

import com.simpfi.object.TrafficStatistics;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;

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
	public StatisticsPanel(TrafficStatistics stats) {
		this.stats = stats;
		initUI();
	}

	/**
	 * Initializes the user interface components, including charts, labels, and
	 * layout.
	 * <p>
	 * This method creates three main sections in the panel:
	 * <ul>
	 * <li>Speed Overview: Line chart and label showing average speed over time</li>
	 * <li>Traffic Density: Bar chart showing vehicle counts per edge and top
	 * congestion hotspots</li>
	 * <li>Travel Time Analysis: Histogram showing distribution of travel times</li>
	 * </ul>
	 * </p>
	 */
	private void initUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setMaximumSize(new Dimension(330, this.getMaximumSize().height));
		this.setPreferredSize(this.getMaximumSize());

		// Label content = new Label("");
		// content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		// content.setBackground(new Color(245, 0, 0));

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPreferredSize(this.getPreferredSize());
		// scroll.setBorder(null);
		// scroll.getVerticalScrollBar().setUnitIncrement(16);

		Font titleFont = new Font("SansSerif", Font.BOLD, 16);
		Font textFont = new Font("SansSerif", Font.PLAIN, 13);

		speedDataset = new DefaultCategoryDataset();

		// Draw Average Speed chart
		JFreeChart chart = ChartFactory.createLineChart("Average Speed Over Time", "Step", "Speed(km/h)", speedDataset);

		speedChartPanel = new ChartPanel(chart);
		speedChartPanel.setPreferredSize(new Dimension(200, 220));

		avrSpeedLabel = new JLabel("Average speed: 0 km/h");
		avrSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		avrSpeedLabel.setFont(textFont);

		scrollPane.addItem(createCard("Speed Overview", speedChartPanel, avrSpeedLabel, titleFont));

		// Draw Vehicle density chart
		vehicleDataset = new DefaultCategoryDataset();
		JFreeChart vehicleChart = ChartFactory.createBarChart("Vehicle Density per Edge", "Edge", "Density",
			vehicleDataset);
		densityChartPanel = new ChartPanel(vehicleChart);
		densityChartPanel.setPreferredSize(new Dimension(200, 220));

		// Congestion Hotspot
		hotspotLabel = new JLabel("Hotspot: none");
		hotspotLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hotspotLabel.setFont(textFont);
		scrollPane.addItem(createCard("Traffic Density", densityChartPanel, hotspotLabel, titleFont));

		// Travel Time Distribution
		travelTimeDataset = new HistogramDataset();
		JFreeChart travelChart = ChartFactory.createHistogram("Travel Time Distribution", "Travel Time (s)",
			"Frequency", travelTimeDataset, PlotOrientation.VERTICAL, true, true, false);
		travelTimeChartPanel = new ChartPanel(travelChart);
		travelTimeChartPanel.setPreferredSize(new Dimension(200, 220));
		travelTimeLabel = new JLabel("Travel Time Histogram");
		travelTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		travelTimeLabel.setFont(textFont);
		scrollPane.addItem(createCard("Travel Time Analysis", travelTimeChartPanel, travelTimeLabel, titleFont));
		this.add(scrollPane);
	}

	/**
	 * Creates a card panel containing a title, a chart, and a label.
	 * <p>
	 * Each card is used to organize a specific type of statistic in the scrollable
	 * panel.
	 * </p>
	 *
	 * @param title     the title of the card
	 * @param chart     the chart component to display
	 * @param label     the label to display below the chart
	 * @param titleFont the font to use for the title
	 * @return a {@link JPanel} containing the chart, label, and title
	 */
	private JPanel createCard(String title, JComponent chart, JLabel label, Font titleFont) {
		Panel card = new Panel();
		card.setLayout(new BorderLayout(10, 10));
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
			BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		card.setBackground(Color.WHITE);

		Label header = new Label(title);
		header.setFont(titleFont);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		card.add(header, BorderLayout.NORTH);

		JPanel chartHolder = new JPanel(new BorderLayout());
		chartHolder.setOpaque(false);
		chartHolder.add(chart, BorderLayout.CENTER);
		card.add(chartHolder, BorderLayout.CENTER);

		card.add(label, BorderLayout.SOUTH);

		card.setMaximumSize(new Dimension(300, 350));
		card.setAlignmentX(Component.LEFT_ALIGNMENT);

		return card;
	}

	/**
	 * Updates the panel for a specific simulation step.
	 * <p>
	 * Updates include:
	 * <ul>
	 * <li>Average speed chart and label</li>
	 * <li>Vehicle density chart</li>
	 * <li>Top congestion hotspots label</li>
	 * <li>Travel time histogram</li>
	 * </ul>
	 * </p>
	 *
	 * @param step the current simulation step
	 */
	public void updatePanel(int step) {
		// Averate Speed
		double avrSpeed = stats.getAverageSpeed();
		speedDataset.addValue(avrSpeed, "Average speed", String.valueOf(step));
		avrSpeedLabel.setText(String.format("Average speed: %.2f km/h", avrSpeed));

		// Vehicle Density
		Map<String, Integer> densityMap = stats.getEdgeDensity();
		vehicleDataset.clear();
		for (var e : densityMap.entrySet()) {
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
	 * This helper method is used internally to structure the layout of chart
	 * sections.
	 * </p>
	 *
	 * @param chart the chart component
	 * @param label the label component
	 * @return a {@link JPanel} containing both the chart and label
	 */
	private JPanel wrapPanel(JComponent chart, JLabel label) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(chart, BorderLayout.CENTER);
		p.add(label, BorderLayout.SOUTH);
		return p;
	}
}
