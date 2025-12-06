package com.simpfi.ui.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.ui.Button;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.TextBox;

/**
 * A UI panel used for adjusting the map view. This class extends {@link Panel}.
 */
public class MapViewPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public MapViewPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		List<TextBox> textboxes = new ArrayList<TextBox>();

		TextBox simulationSpeedTextbox = createTextboxWithLabel("Simulation Speed", this,
			Constants.DEFAULT_SIMULATION_SPEED, true, true, "How fast the simulation is running");
		simulationSpeedTextbox.attachListener(value -> Settings.config.SIMULATION_SPEED = Double.parseDouble(value));
		textboxes.add(simulationSpeedTextbox);

		TextBox normalStrokeSizeTextbox = createTextboxWithLabel("Normal Stroke Size", this,
			Constants.DEFAULT_NORMAL_STROKE_SIZE, true, true, "Thin stroke size for all other elements.");
		normalStrokeSizeTextbox.attachListener(value -> Settings.config.NORMAL_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(normalStrokeSizeTextbox);

		TextBox laneStrokeSizeTextbox = createTextboxWithLabel("Lane Stroke Size", this,
			Constants.DEFAULT_LANE_STROKE_SIZE, true, true, "Stroke size for drawing lanes.");
		laneStrokeSizeTextbox.attachListener(value -> Settings.config.LANE_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(laneStrokeSizeTextbox);

		TextBox junctionStrokeSizeTextbox = createTextboxWithLabel("Junction Stroke Size", this,
			Constants.DEFAULT_JUNCTION_STROKE_SIZE, true, true, "Stroke size for drawing junction borders.");
		junctionStrokeSizeTextbox
			.attachListener(value -> Settings.config.JUNCTION_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(junctionStrokeSizeTextbox);

		Button resetButton = new Button("Reset to defaults");
		resetButton.addActionListener(e -> {
			for (TextBox textbox : textboxes) {
				textbox.resetToDefault();
			}
		});

		this.add(resetButton);
	}

	private TextBox createTextboxWithLabel(String text, JComponent parent, double defaultValue, Boolean mustBeDouble,
		Boolean mustBePositive, String tooltip) {
		Label label = new Label(text);
		TextBox textbox = new TextBox(defaultValue, mustBeDouble, mustBePositive);
		textbox.setToolTipText(tooltip);

		parent.add(label);
		parent.add(textbox);

		return textbox;
	}

	// /** Scale of the map. */
	// public double SCALE = Constants.DEFAULT_SCALE;
	//
	// /** Position of the map (top left corner). */
	// public Point OFFSET = Constants.DEFAULT_OFFSET;
	//
	// /** Dash length for drawing lane dividers. */
	// public double LANE_DIVIDER_DASH_LENGTH =
	// Constants.DEFAULT_LANE_DIVIDER_DASH_LENGTH;
	// /** Stroke size for drawing lane dividers. */
	// public double LANE_DIVIDER_STROKE_SIZE =
	// Constants.DEFAULT_LANE_DIVIDER_STROKE_SIZE;
	//
	// /** How big the traffic lights are. */
	// public double TRAFFIC_LIGHT_RADIUS = Constants.DEFAULT_TRAFFIC_LIGHT_RADIUS;
	//
	// /** Vehicles are drawn bigger than other elements to enhance visibility. */
	// public double VEHICLE_UPSCALE = Constants.DEFAULT_VEHICLE_UPSCALE;
	// /** When zooming, how much of scale is changed each time. */
	// public double SCALE_STEP = Constants.DEFAULT_SCALE_STEP;
	//
	// /** When moving map, how much of map is moved each time. */
	// public double OFFSET_STEP = Constants.DEFAULT_OFFSET_STEP;
	//
	// /** Color of the default vehicle. */
	// public Color NORMAL_VEHICLE_COLOR = Constants.DEFAULT_NORMAL_VEHICLE_COLOR;
	// /** Color of the truck. */
	// public Color TRUCK_COLOR = Constants.DEFAULT_TRUCK_COLOR;
	// /** Color of the bus. */
	// public Color BUS_COLOR = Constants.DEFAULT_BUS_COLOR;
	// /** Color of the motorcycle. */
	// public Color MOTORCYCLE_COLOR = Constants.DEFAULT_MOTORCYCLE_COLOR;
	// /** Color of the emergency vehicle. */
	// public Color EMERGENCY_COLOR = Constants.DEFAULT_EMERGENCY_COLOR;
	// /** Default color. */
	// public Color NORMAL_COLOR = Constants.DEFAULT_NORMAL_COLOR;
	// /** Color of the highlighted route. */
	// public Color HIGHLIGHTED_ROUTE_COLOR =
	// Constants.DEFAULT_HIGHLIGHTED_ROUTE_COLOR;
	// /** Color of the lane. */
	// public Color LANE_COLOR = Constants.DEFAULT_LANE_COLOR;
	// /** Color of the lane divider. */
	// public Color LANE_DIVIDER_COLOR = Constants.DEFAULT_LANE_DIVIDER_COLOR;
	// /** Color of the junction. */
	// public Color JUNCTION_COLOR = Constants.DEFAULT_JUNCTION_COLOR;

}
