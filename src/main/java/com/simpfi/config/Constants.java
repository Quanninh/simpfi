package com.simpfi.config;

import java.awt.Color;
import java.awt.Font;

import com.simpfi.util.Point;

/**
 * Defines fixed values for file paths and drawing properties to ensure
 * consistency when plotting maps and running programs. These values are not
 * allowed to be changed in the program and can only be altered here.
 */
public class Constants {

	/**
	 * ***************************************************************************
	 * SUMO MAP FILES
	 * 
	 * <b>Note:</b> Can replace all the simpfi_new files with the simpfi files for
	 * another network (will add an in-app function for this in the future)
	 * ***************************************************************************.
	 */

	/** SUMO .net.xml file path. */
	public static final String SUMO_NETWORK = "src/main/resources/SumoConfig/simpfi_new.net.xml";
	/*
	 * public static final String SUMO_NETWORK =
	 * "src/main/resources/SumoConfig/simpfi.net.xml";
	 */

	/** SUMO .rou.xml file path. */
	public static final String SUMO_ROUTE = "src/main/resources/SumoConfig/simpfi_new.rou.xml";
	/*
	 * public static final String SUMO_ROUTE =
	 * "src/main/resources/SumoConfig/simpfi.rou.xml";
	 */

	/** SUMO .config file path. */
	public static final String SUMO_CONFIG = "src/main/resources/SumoConfig/simpfi_new.sumocfg";
	/*
	 * public static final String SUMO_CONFIG =
	 * "src/main/resources/SumoConfig/simpfi.sumocfg";
	 */

	/**
	 * ***************************************************************************
	 * UI CONFIGURATIONS Configurations for UI elements
	 * ***************************************************************************.
	 */

	/** How rounded (in px) the corners of all elements are. */
	public static final int ROUNDED_CORNERS = 20;

	/** Default font for all elements. */
	public static final Font FONT = new Font("SansSerif", Font.PLAIN, 14);

	/**
	 * ***************************************************************************
	 * SIMULATION Configurations needed for simulation
	 * ***************************************************************************.
	 */

	/** Timestep (in ms) is the time between two simulation step. */
	public static double DEFAULT_TIMESTEP = 0.1;

	/** How fast the simulation is running */
	public static double DEFAULT_SIMULATION_SPEED = 2;

	/**
	 * ***************************************************************************
	 * MAP DRAWING Configurations needed for drawing of map
	 * ***************************************************************************.
	 */

	/** Thin stroke size for all other elements. */
	public static final double DEFAULT_NORMAL_STROKE_SIZE = 1;
	/** Stroke size for drawing lanes so that they fit and have no gaps. */
	public static final double DEFAULT_LANE_STROKE_SIZE = 4;
	/** Stroke size for drawing junction borders so that they fit each other. */
	public static final double DEFAULT_JUNCTION_STROKE_SIZE = 1.5;
	/** Dash length for drawing lane dividers. */
	public static final double DEFAULT_LANE_DIVIDER_DASH_LENGTH = 4;
	/** Stroke size for drawing lane dividers. */
	public static final double DEFAULT_LANE_DIVIDER_STROKE_SIZE = 0.35;

	/** How big the traffic lights are. */
	public static final double DEFAULT_TRAFFIC_LIGHT_RADIUS = 1.5;

	/** Default scale of map so when the program launches the map is centered. */
	public static final double DEFAULT_SCALE = 2.8;
	/** Vehicles are drawn bigger than other elements to enhance visibility. */
	public static final double DEFAULT_VEHICLE_UPSCALE = 2;
	/** When zooming, how much of scale is changed each time. */
	public static final double DEFAULT_SCALE_STEP = 0.1;
	/** The minimal when zooming in the scale */
	public static final double MIN_SCALE_VALUE = 1;

	/** Default offset of map so when the program launches the map is centered. */
	public static final Point DEFAULT_OFFSET = new Point(-750, -250);
	/** When moving map, how much of map is moved each time. */
	public static final double DEFAULT_OFFSET_STEP = 10;

	/** Color of the default vehicle. */
	public static final Color DEFAULT_NORMAL_VEHICLE_COLOR = new Color(225, 247, 12);
	/** Color of the truck. */
	public static final Color DEFAULT_TRUCK_COLOR = Color.GRAY;
	/** Color of the bus. */
	public static final Color DEFAULT_BUS_COLOR = new Color(12, 239, 255);
	/** Color of the motorcycle. */
	public static final Color DEFAULT_MOTORCYCLE_COLOR = Color.MAGENTA;
	/** Color of the emergency vehicle. */
	public static final Color DEFAULT_EMERGENCY_COLOR = new Color(245, 130, 31);
	/** Default color. */
	public static final Color DEFAULT_NORMAL_COLOR = Color.BLACK;
	/** Color of the highlighted route. */
<<<<<<< HEAD
	public static final Color DEFAULT_HIGHLIGHTED_ROUTE_COLOR = Color.PINK;
=======
	public static final Color HIGHLIGHTED_ROUTE_COLOR = Color.PINK;
	/** Color of the highlighted edge. */
	public static final Color HIGHLIGHTED_ROAD_FILTER_COLOR = Color.ORANGE;
>>>>>>> 87cb08fb15477d8ac9556a6478f053e266ccf6b7
	/** Color of the lane. */
	public static final Color DEFAULT_LANE_COLOR = Color.BLACK;
	/** Color of the lane divider. */
	public static final Color DEFAULT_LANE_DIVIDER_COLOR = Color.BLACK;
	/** Color of the junction. */
	public static final Color DEFAULT_JUNCTION_COLOR = Color.BLACK;

}
