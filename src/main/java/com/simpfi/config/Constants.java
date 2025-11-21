package com.simpfi.config;
/** 
 * Defines fixed values for file paths and drawing properties to ensure consistency when plotting maps and running programs.
*/
public class Constants {

	/**
	 * SUMO MAP FILES
	 */
	public static final String SUMO_NETWORK = "src/main/resources/SumoConfig/simpfi.net.xml";
	public static final String SUMO_ROUTE = "src/main/resources/SumoConfig/simpfi.rou.xml";
	public static final String SUMO_CONFIG = "src/main/resources/SumoConfig/simpfi.sumocfg";

	/**
	 * MAP DRAWING
	 */
	public static final float STROKE_SIZE = 10;
	public static final double DEFAULT_SCALE = 3;

}
