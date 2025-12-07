package com.simpfi.config;

import java.awt.Color;

import com.simpfi.util.Point;

/**
 * Holds configuration parameters that control how the simulation is displayed
 * and executed. This includes scaling factors, coordinate offsets, simulation
 * timestep, and playback speed.
 */
public class SimulationConfig {

	/** The currently chosen route. */
	public String HIGHLIGHTED_ROUTE = "";

	/** Scale of the map. */
	public double SCALE = Constants.DEFAULT_SCALE;

	/** Position of the map (top left corner). */
	public Point OFFSET = Constants.DEFAULT_OFFSET;

	/** Timestep (in ms) is the time between two simulation step. */
	public double TIMESTEP = Constants.DEFAULT_TIMESTEP;

	/** How fast the simulation is running */
	public double SIMULATION_SPEED = Constants.DEFAULT_SIMULATION_SPEED;

	/**
	 * ***************************************************************************
	 * MAP DRAWING Configurations needed for drawing of map
	 * ***************************************************************************.
	 */

	/** Thin stroke size for all other elements. */
	public double NORMAL_STROKE_SIZE = Constants.DEFAULT_NORMAL_STROKE_SIZE;
	/** Stroke size for drawing lanes so that they fit and have no gaps. */
	public double LANE_STROKE_SIZE = Constants.DEFAULT_LANE_STROKE_SIZE;
	/** Stroke size for drawing junction borders so that they fit each other. */
	public double JUNCTION_STROKE_SIZE = Constants.DEFAULT_JUNCTION_STROKE_SIZE;
	/** Dash length for drawing lane dividers. */
	public double LANE_DIVIDER_DASH_LENGTH = Constants.DEFAULT_LANE_DIVIDER_DASH_LENGTH;
	/** Stroke size for drawing lane dividers. */
	public double LANE_DIVIDER_STROKE_SIZE = Constants.DEFAULT_LANE_DIVIDER_STROKE_SIZE;

	/** How big the traffic lights are. */
	public double TRAFFIC_LIGHT_RADIUS = Constants.DEFAULT_TRAFFIC_LIGHT_RADIUS;

	/** Vehicles are drawn bigger than other elements to enhance visibility. */
	public double VEHICLE_UPSCALE = Constants.DEFAULT_VEHICLE_UPSCALE;
	/** When zooming, how much of scale is changed each time. */
	public double SCALE_STEP = Constants.DEFAULT_SCALE_STEP;

	/** When moving map, how much of map is moved each time. */
	public double OFFSET_STEP = Constants.DEFAULT_OFFSET_STEP;

	/** Color of the default vehicle. */
	public Color NORMAL_VEHICLE_COLOR = Constants.DEFAULT_NORMAL_VEHICLE_COLOR;
	/** Color of the truck. */
	public Color TRUCK_COLOR = Constants.DEFAULT_TRUCK_COLOR;
	/** Color of the bus. */
	public Color BUS_COLOR = Constants.DEFAULT_BUS_COLOR;
	/** Color of the motorcycle. */
	public Color MOTORCYCLE_COLOR = Constants.DEFAULT_MOTORCYCLE_COLOR;
	/** Color of the emergency vehicle. */
	public Color EMERGENCY_COLOR = Constants.DEFAULT_EMERGENCY_COLOR;
	/** Default color. */
	public Color NORMAL_COLOR = Constants.DEFAULT_NORMAL_COLOR;
	/** Color of the highlighted route. */
	public Color HIGHLIGHTED_ROUTE_COLOR = Constants.DEFAULT_HIGHLIGHTED_ROUTE_COLOR;
	/** Color of the highlighted edge. */
	public Color HIGHLIGHTED_ROAD_FILTER_COLOR = Color.ORANGE;
	/** Color of the lane. */
	public Color LANE_COLOR = Constants.DEFAULT_LANE_COLOR;
	/** Color of the lane divider. */
	public Color LANE_DIVIDER_COLOR = Constants.DEFAULT_LANE_DIVIDER_COLOR;
	/** Color of the junction. */
	public Color JUNCTION_COLOR = Constants.DEFAULT_JUNCTION_COLOR;

	/** The currently chosen edge when entering the mouse (for filter panel). */
	public String HIGHLIGHTED_ROAD_FILTER = "";

	/**
	 * Adds a value to the scale.
	 * 
	 * @param change the value users want to add
	 */
	public void modifyScale(double change) {
		SCALE += change;
		if (SCALE <= Constants.MIN_SCALE_VALUE)
			SCALE = Constants.MIN_SCALE_VALUE;
	}

	/**
	 * Adds a value to the offset x.
	 * 
	 * @param change the value users want to add
	 */
	public void modifyOffsetX(double change) {
		OFFSET.setX(OFFSET.getX() + change);
	}

	/**
	 * Adds a value to the offset y.
	 * 
	 * @param change the value users want to add
	 */
	public void modifyOffsetY(double change) {
		OFFSET.setY(OFFSET.getY() + change);
	}

	/**
	 * Replace scale with a new value.
	 * 
	 * @param newValue the new value users want to set
	 */
	public void changeScale(double newValue) {
		SCALE = newValue;
	}

	/**
	 * Replace offset x with a new value.
	 * 
	 * @param newValue the new value users want to set
	 */
	public void changeOffsetX(double newValue) {
		OFFSET.setX(newValue);
	}

	/**
	 * Replace offset y with a new value.
	 * 
	 * @param newValue the new value users want to set
	 */
	public void changeOffsetY(double newValue) {
		OFFSET.setY(newValue);
	}

	public void resetColorDefaults() {
		NORMAL_VEHICLE_COLOR = Constants.DEFAULT_NORMAL_VEHICLE_COLOR;
		TRUCK_COLOR = Constants.DEFAULT_TRUCK_COLOR;
		BUS_COLOR = Constants.DEFAULT_BUS_COLOR;
		MOTORCYCLE_COLOR = Constants.DEFAULT_MOTORCYCLE_COLOR;
		EMERGENCY_COLOR = Constants.DEFAULT_EMERGENCY_COLOR;
		NORMAL_COLOR = Constants.DEFAULT_NORMAL_COLOR;
		HIGHLIGHTED_ROUTE_COLOR = Constants.DEFAULT_HIGHLIGHTED_ROUTE_COLOR;
		HIGHLIGHTED_ROAD_FILTER_COLOR = Color.ORANGE;
		LANE_COLOR = Constants.DEFAULT_LANE_COLOR;
		LANE_DIVIDER_COLOR = Constants.DEFAULT_LANE_DIVIDER_COLOR;
		JUNCTION_COLOR = Constants.DEFAULT_JUNCTION_COLOR;
	}

}
