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
import com.simpfi.object.VehicleType;




/**
 *
 * Settings Class loads and stores all network and route information required for SUMO and is also
 * used to initialize and update changes to scale and offset settings of the software.
 *
 * @see {@link com.simpfi.ui.MapPanel}, {@link com.simpfi.ui.ControlPanel},
 *      {@link com.simpfi.ui.TextBox}.
 */
public class Settings {

	public static double SETTINGS_SCALE = Constants.DEFAULT_SCALE;
	public static Point SETTINGS_OFFSET = new Point();
	public static int vehicleCounter = 0;

	private List<Edge> parse_edge;
	private List<Junction> parse_junction;
	private List<VehicleType> parse_vehicleType;
	private List<Route> parse_route;
	private List<String> parse_vid;

    /**
     * Constructor loads all network and route data defined in {@link Constants}
     *
     * Parsing of all junctions and edges using {@link NetworkXMLReader}
     * Parsing of vehicle types, routes, and IDs using {@link RouteXMLReader}
     * */
	public Settings (){
		try {
		    NetworkXMLReader networkXmlReader = new NetworkXMLReader(Constants.SUMO_NETWORK);
		    RouteXMLReader routeXmlReader = new RouteXMLReader(Constants.SUMO_ROUTE);
		    this.parse_junction = networkXmlReader.parseJunction();
		    this.parse_edge = networkXmlReader.parseEdge(this.parse_junction);
		    this.parse_vehicleType = routeXmlReader.parseVehicleType();
		    this.parse_route = routeXmlReader.parseRoute();
			this.parse_vid = routeXmlReader.parseVehicleID();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Getter for {@link Junction} objects.
     *
     * @return all parsed Junctions
     */
	public List<Junction> getJunctions(){
		return this.parse_junction;
	}

    /**
     * Getter for {@link Edge} objects.
     *
     * @return all parsed Edges
     * */
	public List<Edge> getEdges(){
		return this.parse_edge;
	}

    /**
     * Getter for {@link VehicleType} objects.
     *
     * @return all parsed vehicle types
     * */
	public List<VehicleType> getVehicleType(){
		return this.parse_vehicleType;
	}

    /**
     * Getter for {@link Route} objects.
     *
     * @return all parsed routes
     * */
	public List<Route> getRoutes() {
		return this.parse_route;
	}

	public List<String> getVehicleIDs(){
		return this.parse_vid;
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

	public static List<String> generate_vID() {
		List<String> vehicle_ids = new ArrayList<>();
		for (int i = 0; i < vehicleCounter; i++) {
			String id = "v_" + i;
			vehicle_ids.add(id);
		}
		return vehicle_ids;
	}

}
