package com.simpfi.object;

import java.util.Arrays;
import java.util.List;

/**
 * Creates Route class (may include {@link de.tudresden.sumo.cmd.Route} in the
 * future).
 */
public class Route {

	/** The route id. */
	private String id;

	/** The route edges. */
	private Edge[] edges;

	/**
	 * Instantiates a new route.
	 *
	 * @param id    the id
	 * @param edges the edges
	 */
	public Route(String id, Edge[] edges) {
		this.id = id;
		this.edges = edges;
	}

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the edges.
	 *
	 * @return the edges
	 */
	public Edge[] getEdges() {
		return edges;
	}

	/**
	 * Search for a route in a list of routes.
	 *
	 * @param id     the id of the route to be searched for
	 * @param routes the list of routes
	 * @return the found route or {@code null} if not found
	 */
	public static Route searchForRoute(String id, List<Route> routes) {
		for (int i = 0; i < routes.size(); i++) {
			if (routes.get(i).getId().equals(id)) {
				return routes.get(i);
			}
		}
		return null;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Route.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Route [id=" + id + ", edges=" + Arrays.toString(edges) + "]";
	}

}
