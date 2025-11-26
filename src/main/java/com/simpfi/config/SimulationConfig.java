package com.simpfi.config;

import com.simpfi.util.Point;

public class SimulationConfig {

	public double SCALE = Constants.DEFAULT_SCALE;
	public Point OFFSET = new Point(Constants.DEFAULT_OFFSET_X, Constants.DEFAULT_OFFSET_Y);
	public double TIMESTEP = 0.1;
	public double SIMULATION_SPEED = 2;

	/**
	 * Adds a value to the scale.
	 * 
	 * @param change the value users want to add.
	 */
	public void modifyScale(double change) {
		SCALE += change;
	}

	/**
	 * Adds a value to the offset x.
	 * 
	 * @param change the value users want to add.
	 */
	public void modifyOffsetX(double change) {
		OFFSET.setX(OFFSET.getX() + change);
	}

	/**
	 * Adds a value to the offset y.
	 * 
	 * @param change the value users want to add.
	 */
	public void modifyOffsetY(double change) {
		OFFSET.setY(OFFSET.getY() + change);
	}

	/**
	 * Replace scale with a new value.
	 * 
	 * @param newValue the new value users want to set.
	 */
	public void changeScale(double newValue) {
		SCALE = newValue;
	}

	/**
	 * Replace offset x with a new value.
	 * 
	 * @param newValue the new value users want to set.
	 */
	public void changeOffsetX(double newValue) {
		OFFSET.setX(newValue);
	}

	/**
	 * Replace offset y with a new value.
	 * 
	 * @param newValue the new value users want to set.
	 */
	public void changeOffsetY(double newValue) {
		OFFSET.setY(newValue);
	}

}
