package com.simpfi.config;

import com.simpfi.util.Point;

public class Settings {

	public static double SETTINGS_SCALE = Constants.DEFAULT_SCALE;
	public static Point SETTINGS_OFFSET = new Point();

	public static void modifyScale(double change) {
		SETTINGS_SCALE += change;
	}

	public static void modifyOffsetX(double change) {
		SETTINGS_OFFSET.setX(SETTINGS_OFFSET.getX() + change);
	}

	public static void modifyOffsetY(double change) {
		SETTINGS_OFFSET.setY(SETTINGS_OFFSET.getY() + change);
	}

	public static void changeScale(double newValue) {
		SETTINGS_SCALE = newValue;
	}

	public static void changeOffsetX(double newValue) {
		SETTINGS_OFFSET.setX(newValue);
	}

	public static void changeOffsetY(double newValue) {
		SETTINGS_OFFSET.setY(newValue);
	}

}
