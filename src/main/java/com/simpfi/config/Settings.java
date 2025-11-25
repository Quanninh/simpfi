package com.simpfi.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Route;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.Vehicle;
import com.simpfi.object.VehicleType;
import com.simpfi.util.Point;
import com.simpfi.util.reader.NetworkXMLReader;
import com.simpfi.util.reader.RouteXMLReader;

/**
 *
 * Settings Class loads and stores all network and route information required
 * for SUMO and is also used to initialize and update changes to scale and
 * offset settings of the software.
 *
 * @see com.simpfi.ui.MapPanel
 * @see com.simpfi.ui.ControlPanel
 * @see com.simpfi.ui.TextBox
 */
public class Settings {

	public static double SETTINGS_SCALE = Constants.DEFAULT_SCALE;
	public static Point SETTINGS_OFFSET = new Point();
	public static double TIMESTEP = 0.1;
	public static double SIMULATION_SPEED = 2;

	private static List<Edge> parsedEdges;
	private static List<Junction> parsedJunctions;
	private static List<VehicleType> parsedVehicleTypes;
	private static List<Route> parsedRoutes;
	private static List<TrafficLight> parsedTrafficLights;

	public static int vehicleCounter = 0;
	private static Map<String, Vehicle> vehicleMap = new HashMap<>();
	private static Map<String, String> liveTrafficLightStates = new HashMap<>();

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
			parsedTrafficLights = networkXmlReader
				.parseTrafficLight(parsedJunctions, parsedEdges);
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

	public static List<Vehicle> getVehicles() {
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			vehicleList.add(entry.getValue());
		}
		return vehicleList;
	}
	
	public static void disableAllVehicles() {
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			entry.getValue().setIsActive(false);
		}
	}

	public static void setVehicles(Vehicle vehicleToUpdate) {
		vehicleToUpdate.setIsActive(true);
		vehicleMap.put(vehicleToUpdate.getID(), vehicleToUpdate);
	}

	public static void updateTrafficLightState(String id, String state) {
		liveTrafficLightStates.put(id, state);
	}

	public static Map<String, String> getLiveTrafficLightStates() {
		return liveTrafficLightStates;
	}

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

	public static String generateVehicleIDs() {
		String ids = "v_" + vehicleCounter;
		vehicleCounter++;
		return ids;
	}

}
