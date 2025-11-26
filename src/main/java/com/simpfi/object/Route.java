package com.simpfi.object;

import java.util.Arrays;
import java.util.List;

/**
 * Creates Route class (may includes {@link de.tudresden.sumo.cmd.Route} in the
 * future).
 */
public class Route {

	private String id;
	private Edge[] edges;

	public Route(String id, Edge[] edges) {
		this.id = id;
		this.edges = edges;
	}

	public String getId() {
		return id;
	}

	public Edge[] getEdges() {
		return edges;
	}
	
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
	 */
	@Override
	public String toString() {
		return "Route [id=" + id + ", edges=" + Arrays.toString(edges) + "]";
	}

}
