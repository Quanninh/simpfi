package com.simpfi.ui.panel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JColorChooser;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.ui.Button;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;
import com.simpfi.ui.TextBox;

/**
 * A UI panel used for adjusting the map view. This class extends {@link Panel}.
 */
public class MapViewPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public MapViewPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		ScrollPane scrollPane = new ScrollPane();

		System.out.println("Generating textboxes");
		List<TextBox> textboxes = generateTextboxes(scrollPane);
		System.out.println("Generating buttons");
		generateColorButtons(scrollPane);

		Button resetButton = new Button("Reset to defaults");
		resetButton.addActionListener(e -> {
			for (TextBox textbox : textboxes) {
				textbox.resetToDefault();
			}
			Settings.config.resetColorDefaults();
		});

		this.add(scrollPane);
		this.add(resetButton);
	}

	private TextBox createTextboxWithLabel(String text, ScrollPane parent, double defaultValue, Boolean mustBeDouble,
		Boolean mustBePositive, String tooltip) {
		Label label = new Label(text);
		TextBox textbox = new TextBox(defaultValue, mustBeDouble, mustBePositive);
		textbox.setToolTipText(tooltip);

		parent.addItem(label);
		parent.addItem(textbox);

		return textbox;
	}

	private List<TextBox> generateTextboxes(ScrollPane scrollPane) {
		List<TextBox> textboxes = new ArrayList<TextBox>();

		TextBox simulationSpeedTextbox = createTextboxWithLabel("Simulation Speed", scrollPane,
			Constants.DEFAULT_SIMULATION_SPEED, true, true, "How fast the simulation is running");
		simulationSpeedTextbox.attachListener(value -> Settings.config.SIMULATION_SPEED = Double.parseDouble(value));
		textboxes.add(simulationSpeedTextbox);

		TextBox normalStrokeSizeTextbox = createTextboxWithLabel("Normal Stroke Size", scrollPane,
			Constants.DEFAULT_NORMAL_STROKE_SIZE, true, true, "Thin stroke size for all other elements.");
		normalStrokeSizeTextbox.attachListener(value -> Settings.config.NORMAL_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(normalStrokeSizeTextbox);

		TextBox laneStrokeSizeTextbox = createTextboxWithLabel("Lane Stroke Size", scrollPane,
			Constants.DEFAULT_LANE_STROKE_SIZE, true, true, "Stroke size for drawing lanes.");
		laneStrokeSizeTextbox.attachListener(value -> Settings.config.LANE_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(laneStrokeSizeTextbox);

		TextBox junctionStrokeSizeTextbox = createTextboxWithLabel("Junction Stroke Size", scrollPane,
			Constants.DEFAULT_JUNCTION_STROKE_SIZE, true, true, "Stroke size for drawing junction borders.");
		junctionStrokeSizeTextbox
			.attachListener(value -> Settings.config.JUNCTION_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(junctionStrokeSizeTextbox);

		// TextBox scaleTextbox = createTextboxWithLabel("Scale", scrollPane,
		// Constants.DEFAULT_SCALE, true, true,
		// "Scale of the map.");
		// scaleTextbox.attachListener(value -> Settings.config.SCALE =
		// Double.parseDouble(value));
		// textboxes.add(scaleTextbox);
		//
		// TextBox offsetXTextbox = createTextboxWithLabel("Offset X", scrollPane,
		// Constants.DEFAULT_OFFSET.getX(), true,
		// true, "X coordinates of the map (top left corner).");
		// offsetXTextbox.attachListener(value ->
		// Settings.config.OFFSET.setX(Double.parseDouble(value)));
		// textboxes.add(offsetXTextbox);
		//
		// TextBox offsetYTextbox = createTextboxWithLabel("Offset Y", scrollPane,
		// Constants.DEFAULT_OFFSET.getY(), true,
		// true, "Y coordinates of the map (top left corner).");
		// offsetYTextbox.attachListener(value ->
		// Settings.config.OFFSET.setY(Double.parseDouble(value)));
		// textboxes.add(offsetYTextbox);

		TextBox dashLengthTextbox = createTextboxWithLabel("Dash Length", scrollPane,
			Constants.DEFAULT_LANE_DIVIDER_DASH_LENGTH, true, true, "Dash length for drawing lane dividers.");
		dashLengthTextbox.attachListener(value -> Settings.config.LANE_DIVIDER_DASH_LENGTH = Double.parseDouble(value));
		textboxes.add(dashLengthTextbox);

		TextBox laneDividerStrokeSizeTextbox = createTextboxWithLabel("Lane Divider Stroke Size", scrollPane,
			Constants.DEFAULT_LANE_DIVIDER_STROKE_SIZE, true, true, "Dash length for drawing lane dividers.");
		laneDividerStrokeSizeTextbox
			.attachListener(value -> Settings.config.LANE_DIVIDER_STROKE_SIZE = Double.parseDouble(value));
		textboxes.add(laneDividerStrokeSizeTextbox);

		TextBox trafficLightSizeTextbox = createTextboxWithLabel("Traffic Light Size", scrollPane,
			Constants.DEFAULT_TRAFFIC_LIGHT_RADIUS, true, true, "How big the traffic lights are.");
		trafficLightSizeTextbox
			.attachListener(value -> Settings.config.TRAFFIC_LIGHT_RADIUS = Double.parseDouble(value));
		textboxes.add(trafficLightSizeTextbox);

		TextBox vehicleUpscaleTextbox = createTextboxWithLabel("Vehicle Upscale", scrollPane,
			Constants.DEFAULT_VEHICLE_UPSCALE, true, true,
			"Vehicles are drawn bigger than other elements to enhance visibility.");
		vehicleUpscaleTextbox.attachListener(value -> Settings.config.VEHICLE_UPSCALE = Double.parseDouble(value));
		textboxes.add(vehicleUpscaleTextbox);

		TextBox scaleStepTextbox = createTextboxWithLabel("Scale Step", scrollPane, Constants.DEFAULT_SCALE_STEP, true,
			true, "When zooming with keyboard, how much of scale is changed each time.");
		scaleStepTextbox.attachListener(value -> Settings.config.SCALE_STEP = Double.parseDouble(value));
		textboxes.add(scaleStepTextbox);

		TextBox offsetStepTextbox = createTextboxWithLabel("Offset Step", scrollPane, Constants.DEFAULT_OFFSET_STEP,
			true, true, "When moving map with arrow keys, how much of map is moved each time.");
		offsetStepTextbox.attachListener(value -> Settings.config.OFFSET_STEP = Double.parseDouble(value));
		textboxes.add(offsetStepTextbox);

		return textboxes;
	}

	private List<Button> generateColorButtons(ScrollPane scrollPane) {
		List<Button> buttons = new ArrayList<Button>();

		Button normalVehicleColorButton = new Button("Normal Vehicle Color");
		normalVehicleColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the default vehicle.",
				Constants.DEFAULT_NORMAL_VEHICLE_COLOR);
			Settings.config.NORMAL_VEHICLE_COLOR = color;
		});
		scrollPane.addItem(normalVehicleColorButton);
		buttons.add(normalVehicleColorButton);

		Button truckColorButton = new Button("Truck Color");
		truckColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the truck.", Constants.DEFAULT_TRUCK_COLOR);
			Settings.config.TRUCK_COLOR = color;
		});
		scrollPane.addItem(truckColorButton);
		buttons.add(truckColorButton);

		Button busColorButton = new Button("Bus Color");
		busColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the bus.", Constants.DEFAULT_BUS_COLOR);
			Settings.config.BUS_COLOR = color;
		});
		scrollPane.addItem(busColorButton);
		buttons.add(busColorButton);

		Button motorcycleColorButton = new Button("Motorcycle Color");
		motorcycleColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the motorcycle.",
				Constants.DEFAULT_MOTORCYCLE_COLOR);
			Settings.config.MOTORCYCLE_COLOR = color;
		});
		scrollPane.addItem(motorcycleColorButton);
		buttons.add(motorcycleColorButton);

		Button emergencyColorButton = new Button("Emergency Vehicle Color");
		emergencyColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the emergency vehicle.",
				Constants.DEFAULT_EMERGENCY_COLOR);
			Settings.config.EMERGENCY_COLOR = color;
		});
		scrollPane.addItem(emergencyColorButton);
		buttons.add(emergencyColorButton);

		Button normalColorButton = new Button("Default Color");
		normalColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Default Color.", Constants.DEFAULT_NORMAL_COLOR);
			Settings.config.NORMAL_COLOR = color;
		});
		scrollPane.addItem(normalColorButton);
		buttons.add(normalColorButton);

		Button highlightedRouteColorButton = new Button("Highlighted Route Color");
		highlightedRouteColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the highlighted route.",
				Constants.DEFAULT_HIGHLIGHTED_ROUTE_COLOR);
			Settings.config.HIGHLIGHTED_ROUTE_COLOR = color;
		});
		scrollPane.addItem(highlightedRouteColorButton);
		buttons.add(highlightedRouteColorButton);

		Button highlightedRoadFilterColorButton = new Button("Highlighted Road Filter Color");
		highlightedRoadFilterColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the highlighted road filter.",
				Constants.DEFAULT_HIGHLIGHTED_ROAD_FILTER_COLOR);
			Settings.config.HIGHLIGHTED_ROAD_FILTER_COLOR = color;
		});
		scrollPane.addItem(highlightedRoadFilterColorButton);
		buttons.add(highlightedRoadFilterColorButton);

		Button highlightedTrafficLightColorButton = new Button("Highlighted Traffic Light Color");
		highlightedTrafficLightColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the highlighted traffic light.",
				Constants.DEFAULT_HIGHLIGHTED_TRAFFIC_LIGHT_COLOR);
			Settings.config.HIGHLIGHTED_TRAFFIC_LIGHT_COLOR = color;
		});
		scrollPane.addItem(highlightedTrafficLightColorButton);
		buttons.add(highlightedTrafficLightColorButton);

		Button highlightedConnectionColorButton = new Button("Highlighted Connection Color");
		highlightedConnectionColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the highlighted connection.",
				Constants.DEFAULT_HIGHLIGHTED_CONNECTION_COLOR);
			Settings.config.HIGHLIGHTED_CONNECTION_COLOR = color;
		});
		scrollPane.addItem(highlightedConnectionColorButton);
		buttons.add(highlightedConnectionColorButton);

		Button laneColorButton = new Button("Lane Color");
		laneColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the lane.", Constants.DEFAULT_LANE_COLOR);
			Settings.config.LANE_COLOR = color;
		});
		scrollPane.addItem(laneColorButton);
		buttons.add(laneColorButton);

		Button laneDividerColorButton = new Button("Lane Divider Color");
		laneDividerColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the lane divider.",
				Constants.DEFAULT_LANE_DIVIDER_COLOR);
			Settings.config.LANE_DIVIDER_COLOR = color;
		});
		scrollPane.addItem(laneDividerColorButton);
		buttons.add(laneDividerColorButton);

		Button junctionColorButton = new Button("Junction Color");
		junctionColorButton.addActionListener(e -> {
			Color color = JColorChooser.showDialog(null, "Color of the junction.", Constants.DEFAULT_JUNCTION_COLOR);
			Settings.config.JUNCTION_COLOR = color;
		});
		scrollPane.addItem(junctionColorButton);
		buttons.add(junctionColorButton);

		return buttons;
	}

}
