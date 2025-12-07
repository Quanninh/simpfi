package com.simpfi.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Road;
import com.simpfi.object.Route;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.VehicleType;
import com.simpfi.util.reader.NetworkXMLReader;
import com.simpfi.util.reader.RouteXMLReader;

/**
 * Loads and stores all information related to the network from SUMO. This
 * includes edges, roads, junctions, vehicle types, routes, and traffic lights, and
 * makes them available for all other classes to access. This data is available
 * for all other classes through {@link Settings}.
 */
public class Network {

	/** List containing all edges. */
	private List<Edge> edges;

	/** List containing all junctions. */
	private List<Junction> junctions;

	/** List containing all vehicle types. */
	private List<VehicleType> vehicleTypes;

	/** List containing all routes. */
	private List<Route> routes;

	/** List containing all traffic lights. */
	private List<TrafficLight> trafficLights;

	/** HashMap to map an edge to a road */
	private static Map<Edge, Road> edgeToRoad = new HashMap<>();

	/**
	 * Constructor loads all network and route data defined in {@link Constants}.
	 *
	 * Parsing of all junctions and edges using {@link NetworkXMLReader} and parsing
	 * of vehicle types, routes, and IDs using {@link RouteXMLReader}. Saves all
	 * information to private attributes.
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
	 * Getter for {@link Road} objects, derived from {@link Edge} objects.
	 *
	 * @return all parsed Road
	 */
	public List<Road> getRoads() {
		List<Edge> allEdges = getEdges();
		List<Road> roads = new ArrayList<>();

		// Omit edges that belong to junction and group edges with suffix
		for (int i = 0; i < allEdges.size(); i++) {
			// Skip if the edges have "J" and suffix(".") in its name
			if(allEdges.get(i).getId().charAt(1) != 'J') {
				// If the edge is a sub-edge, add it to the list of corresponding edges 
				if(allEdges.get(i).getId().contains(".")){
					// Here we accept the convention that sub-edges will follow their edge
					Road updatedRoad = roads.get(roads.size() - 1).addEdge(allEdges.get(i));

					// Update the Hash Map
					Edge e = allEdges.get(i);
					edgeToRoad.put(e, updatedRoad);
					continue;
				}

				Edge e = allEdges.get(i);
				Road r =new Road(allEdges.get(i).getId(), new Edge[]{ e });
				roads.add(r);
				
				// Update the Hash Map
				edgeToRoad.put(e, r);
			}
		}
		return roads;
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

	public Road getRoadFromEdge(Edge e){
		boolean validEdge = edges.contains(e);
		if(validEdge && e.getId().charAt(1) != 'J') {return edgeToRoad.get(e);}
		else {return null; }
	}

}
