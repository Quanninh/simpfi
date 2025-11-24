package com.simpfi.config;

import java.util.ArrayList;
import java.util.List;

import com.simpfi.ui.MapPanel;
import com.simpfi.util.Point;
import com.simpfi.util.reader.NetworkXMLReader;
import com.simpfi.util.reader.RouteXMLReader;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Route;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.VehicleType;

/**
 *
 * Settings Class loads and stores all network and route information required
 * for SUMO and is also used to initialize and update changes to scale and
 * offset settings of the software.
 *
 * @see {@link com.simpfi.ui.MapPanel}, {@link com.simpfi.ui.ControlPanel},
 *      {@link com.simpfi.ui.TextBox}.
 */
public class Settings {

	public static double SETTINGS_SCALE = Constants.DEFAULT_SCALE;
	public static Point SETTINGS_OFFSET = new Point();
	public static int vehicleCounter = 0;

	private static List<Edge> parsedEdges;
	private static List<Junction> parsedJunctions;
	private static List<VehicleType> parsedVehicleTypes;
	private static List<Route> parsedRoutes;
	private static List<TrafficLight> parsedTrafficLights;
	private static List<String> parsedVehicleIds;

	/**
	 * Constructor loads all network and route data defined in {@link Constants}
	 *
	 * Parsing of all junctions and edges using {@link NetworkXMLReader} Parsing
	 * of vehicle types, routes, and IDs using {@link RouteXMLReader}
	 */
	static {
		System.out.println("Initializing Settings");
		try {
			NetworkXMLReader networkXmlReader = new NetworkXMLReader(
				Constants.SUMO_NETWORK);
			RouteXMLReader routeXmlReader = new RouteXMLReader(
				Constants.SUMO_ROUTE);

			parsedJunctions = networkXmlReader.parseJunction();
			parsedEdges = networkXmlReader.parseEdge(parsedJunctions);
			parsedVehicleTypes = routeXmlReader.parseVehicleType();
			parsedRoutes = routeXmlReader.parseRoute();
			parsedTrafficLights = networkXmlReader.parseTrafficLight(parsedJunctions, parsedEdges);
			// this.parse_vid = routeXmlReader.parseVehicleID();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Settings done");
	}

	/**
	 * Getter for {@link Junction} objects.
	 *
	 * @return all parsed Junctions
	 */
	public static List<Junction> getJunctions() {
		return parsedJunctions;
	}

	/**
	 * Getter for {@link Edge} objects.
	 *
	 * @return all parsed Edges
	 */
	public static List<Edge> getEdges() {
		return parsedEdges;
	}

	/**
	 * Getter for {@link VehicleType} objects.
	 *
	 * @return all parsed vehicle types
	 */
	public static List<VehicleType> getVehicleTypes() {
		return parsedVehicleTypes;
	}

	/**
	 * Getter for {@link Route} objects.
	 *
	 * @return all parsed routes
	 */
	public static List<Route> getRoutes() {
		return parsedRoutes;
	}

	/**
	 * Getter for {@link TrafficLight} objects.
	 *
	 * @return all parsed traffic lights
	 */
	public static List<TrafficLight> getTrafficLights() {
		return parsedTrafficLights;
	}

	// public List<String> getVehicleIDs(){
	// return this.parse_vid;
	// }

	/**
	 * Adds a value to the scale.
	 * 
	 * @param change the value users want to add.
	 */
	public static void modifyScale(double change) {
		SETTINGS_SCALE += change;
	}

	/**
	 * Adds a value to the offset x.
	 * 
	 * @param change the value users want to add.
	 */
	public static void modifyOffsetX(double change) {
		SETTINGS_OFFSET.setX(SETTINGS_OFFSET.getX() + change);
	}

	/**
	 * Adds a value to the offset y.
	 * 
	 * @param change the value users want to add.
	 */
	public static void modifyOffsetY(double change) {
		SETTINGS_OFFSET.setY(SETTINGS_OFFSET.getY() + change);
	}

	/**
	 * Replace scale with a new value.
	 * 
	 * @param newValue the new value users want to set.
	 */
	public static void changeScale(double newValue) {
		SETTINGS_SCALE = newValue;
	}

	/**
	 * Replace offset x with a new value.
	 * 
	 * @param newValue the new value users want to set.
	 */
	public static void changeOffsetX(double newValue) {
		SETTINGS_OFFSET.setX(newValue);
	}

	/**
	 * Replace offset y with a new value.
	 * 
	 * @param newValue the new value users want to set.
	 */
	public static void changeOffsetY(double newValue) {
		SETTINGS_OFFSET.setY(newValue);
	}

	// public static List<String> generate_vID() {
	// List<String> vehicle_ids = new ArrayList<>();
	// for (int i = 0; i < vehicleCounter; i++) {
	// String id = "v_" + i;
	// vehicle_ids.add(id);
	// }
	// return vehicle_ids;
	// }

	public static List<String> generateVehicleIDs() {
		List<VehicleType> types = getVehicleTypes();
		List<Route> routes = getRoutes();
		int n = Math.min(types == null ? 0 : types.size(),
			routes == null ? 0 : routes.size());
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			ids.add("v_" + i);
		}
		return ids;
	}

}
