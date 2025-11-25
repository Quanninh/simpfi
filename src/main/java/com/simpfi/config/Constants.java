package com.simpfi.config;

import java.awt.Color;

/**
 * Defines fixed values for file paths and drawing properties to ensure
 * consistency when plotting maps and running programs.
 */
public class Constants {

	/**
	 * SUMO MAP FILES
	 */
	public static final String SUMO_NETWORK = "src/main/resources/SumoConfig/simpfi.net.xml";
	public static final String SUMO_ROUTE = "src/main/resources/SumoConfig/simpfi.rou.xml";
	public static final String SUMO_CONFIG = "src/main/resources/SumoConfig/simpfi.sumocfg";

	/**
	 * UI CONFIG
	 */
	public static final int ROUNDED_CORNERS = 20;
	
	/**
	 * MAP DRAWING
	 */
	public static final double DEFAULT_STROKE_SIZE = 1;
	public static final double DEFAULT_SCALE = 3;
	public static final double LANE_STROKE_SIZE = 4;
	public static final double JUNCTION_STROKE_SIZE = 2;
	public static final double TRAFFIC_LIGHT_RADIUS = 1.5;
	public static final double VEHICLE_UPSCALE = 3;
	
	public static final Color DEFAULT_VEHICLE_COLOR = Color.GREEN;
	public static final Color TRUCK_COLOR = Color.GRAY;
	public static final Color BUS_COLOR = Color.YELLOW;
	public static final Color MOTORCYCLE_COLOR = Color.MAGENTA;
	public static final Color EMERGENCY_COLOR = Color.RED;

}
