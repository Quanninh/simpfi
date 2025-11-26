package com.simpfi.config;

import java.awt.Color;
import java.awt.Font;

/**
 * Defines fixed values for file paths and drawing properties to ensure
 * consistency when plotting maps and running programs.
 */
public class Constants {

	/**
	 * SUMO MAP FILES
	 */
	public static final String SUMO_NETWORK = "src/main/resources/SumoConfig/simpfi_new.net.xml";
	public static final String SUMO_ROUTE = "src/main/resources/SumoConfig/simpfi_new.rou.xml";
	public static final String SUMO_CONFIG = "src/main/resources/SumoConfig/simpfi_new.sumocfg";

	/**
	 * UI CONFIG
	 */
	public static final int ROUNDED_CORNERS = 20;
	public static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 14);

	/**
	 * MAP DRAWING
	 */
	public static final double DEFAULT_STROKE_SIZE = 1;
	public static final double LANE_STROKE_SIZE = 4;
	public static final double JUNCTION_STROKE_SIZE = 1.5;
	public static final double LANE_DIVIDER_DASH_LENGTH = 4;
	public static final double LANE_DIVIDER_STROKE_SIZE = 0.35;

	public static final double TRAFFIC_LIGHT_RADIUS = 1.5;

	public static final double DEFAULT_SCALE = 2.8;
	public static final double VEHICLE_UPSCALE = 2;
	// When zooming, how much of scale is changed each time
	public static final double SCALE_STEP = 0.1;

	public static final double DEFAULT_OFFSET_X = -750;
	public static final double DEFAULT_OFFSET_Y = -250;
	// When moving map, how much of map is moved each time
	public static final double OFFSET_STEP = 10;

	public static final Color DEFAULT_VEHICLE_COLOR = Color.GREEN;
	public static final Color TRUCK_COLOR = Color.GRAY;
	public static final Color BUS_COLOR = Color.YELLOW;
	public static final Color MOTORCYCLE_COLOR = Color.MAGENTA;
	public static final Color EMERGENCY_COLOR = Color.RED;

}
