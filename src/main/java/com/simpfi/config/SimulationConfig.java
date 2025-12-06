package com.simpfi.config;

import com.simpfi.util.Point;

/**
 * Holds configuration parameters that control how the simulation is displayed
 * and executed. This includes scaling factors, coordinate offsets, simulation
 * timestep, and playback speed.
 */
public class SimulationConfig {

	/** Scale of the map. */
	public double SCALE = Constants.DEFAULT_SCALE;

	/** Position of the map (top left corner). */
	public Point OFFSET = new Point(Constants.DEFAULT_OFFSET_X, Constants.DEFAULT_OFFSET_Y);

	/** Timestep (in ms) is the time between two simulation step. */
	public double TIMESTEP = 0.1;

	/**
	 * How fast the simulation is running (will add feature to change this in the
	 * future).
	 */
	public double SIMULATION_SPEED = 2;

	/** The currently chosen route. */
	public String HIGHLIGHTED_ROUTE = "";

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

}
