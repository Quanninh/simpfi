package com.simpfi.config;

import com.simpfi.object.Connection;
import com.simpfi.object.Road;
import com.simpfi.object.Route;
import com.simpfi.object.TrafficLight;

public class Highlighted {

	/**
	 * ***************************************************************************
	 * HIGHLIGHTED STUFF
	 * ***************************************************************************
	 */

	/** The currently chosen route. */
	public Route HIGHLIGHTED_ROUTE = null;

	/** The currently chosen traffic light. */
	public TrafficLight HIGHLIGHTED_TRAFFIC_LIGHT = null;

	/** The currently chosen connection. */
	public Connection HIGHLIGHTED_CONNECTION = null;

	/** The currently chosen edge when entering the mouse (for filter panel). */
	public Road HIGHLIGHTED_ROAD_FILTER = null;

}
