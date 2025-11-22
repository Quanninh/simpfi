package com.simpfi.config;

import com.simpfi.util.Point;

public class Settings {

	public static double SETTINGS_SCALE = Constants.DEFAULT_SCALE;
	public static Point SETTINGS_OFFSET = new Point();

	public static void modifyScale(double change) {
		SETTINGS_SCALE += change;
	}
	/**
	 * Adds a value to the offset x.
	 * @param change the value users want to add.
	 */
	public static void modifyOffsetX(double change) {
		SETTINGS_OFFSET.setX(SETTINGS_OFFSET.getX() + change);
	}
	/**
	 * Adds a value to the offset y.
	 * @param change the value users want to add.
	 */
	public static void modifyOffsetY(double change) {
		SETTINGS_OFFSET.setY(SETTINGS_OFFSET.getY() + change);
	}
	/**
	 * Replace scale with a new value. 
	 * @param newValue the new value users want to set.
	 */
	public static void changeScale(double newValue) {
		SETTINGS_SCALE = newValue;
	}
	/**
	 * Replace offset x with a new value. 
	 * @param newValue the new value users want to set.
	 */
	public static void changeOffsetX(double newValue) {
		SETTINGS_OFFSET.setX(newValue);
	}
	/**
	 * Replace offset y with a new value. 
	 * @param newValue the new value users want to set.
	 */
	public static void changeOffsetY(double newValue) {
		SETTINGS_OFFSET.setY(newValue);
	}

	public void generate_vID(MapPanel.vehicleCounter){
		System.out.println()
	}
	
}
