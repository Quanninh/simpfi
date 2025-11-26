package com.simpfi.config;

import java.util.List;

import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Route;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.VehicleType;
import com.simpfi.util.reader.NetworkXMLReader;
import com.simpfi.util.reader.RouteXMLReader;

public class Network {

	private List<Edge> edges;
	private List<Junction> junctions;
	private List<VehicleType> vehicleTypes;
	private List<Route> routes;
	private List<TrafficLight> trafficLights;

	/**
	 * Constructor loads all network and route data defined in {@link Constants}
	 *
	 * Parsing of all junctions and edges using {@link NetworkXMLReader} Parsing of
	 * vehicle types, routes, and IDs using {@link RouteXMLReader}
	 */
	public Network() {
		NetworkXMLReader networkXmlReader;
		RouteXMLReader routeXmlReader;
		try {
			networkXmlReader = new NetworkXMLReader(Constants.SUMO_NETWORK);
			routeXmlReader = new RouteXMLReader(Constants.SUMO_ROUTE);

			junctions = networkXmlReader.parseJunction();
			edges = networkXmlReader.parseEdge(junctions);
			vehicleTypes = routeXmlReader.parseVehicleType();
			routes = routeXmlReader.parseRoute();
			trafficLights = networkXmlReader.parseTrafficLight(junctions, edges);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for {@link Junction} objects.
	 *
	 * @return all parsed Junctions
	 */
	public List<Junction> getJunctions() {
		return junctions;
	}

	/**
	 * Getter for {@link Edge} objects.
	 *
	 * @return all parsed Edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * Getter for {@link VehicleType} objects.
	 *
	 * @return all parsed vehicle types
	 */
	public List<VehicleType> getVehicleTypes() {
		return vehicleTypes;
	}

	/**
	 * Getter for {@link Route} objects.
	 *
	 * @return all parsed routes
	 */
	public List<Route> getRoutes() {
		return routes;
	}

	/**
	 * Getter for {@link TrafficLight} objects.
	 *
	 * @return all parsed traffic lights
	 */
	public List<TrafficLight> getTrafficLights() {
		return trafficLights;
	}

}
