package com.simpfi.object;

import java.util.Arrays;
import java.util.List;

/**
 * Creates Edge class (may includes {@link de.tudresden.sumo.cmd.Edge} in the
 * future).
 */
public class Edge {

	/** The edge id. */
	String id;

	/** The conjunction where the edge starts. */
	Junction conjunctionFrom;

	/** The conjunction where the edge ends. */
	Junction conjunctionTo;

	/** All lanes of the edge. */
	Lane[] lanes;

	/** Number of lanes of the edge. */
	int lanesSize;

	/**
	 * Instantiates a new edge.
	 *
	 * @param id    the id of the edge
	 * @param from  the conjunction where the edge starts
	 * @param to    the onjunction where the edge ends
	 * @param lanes all the lanes of the edge
	 */
	public Edge(String id, Junction from, Junction to, Lane[] lanes) {
		this.id = id;
		this.conjunctionFrom = from;
		this.conjunctionTo = to;
		this.lanes = lanes;
		this.lanesSize = this.lanes.length;
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
	 * Returns the lanes.
	 *
	 * @return the lanes
	 */
	public Lane[] getLanes() {
		return lanes;
	}

	/**
	 * Returns the lanes size.
	 *
	 * @return the lanes size
	 */
	public int getLanesSize() {
		return lanesSize;
	}

	/**
	 * Used to search over a list of edges to find one with the matched id.
	 * 
	 * @param id    id of the edge that users look for.
	 * @param edges given list of edges.
	 * @return the edge with the passed id, {@code null} if not found.
	 */
	public static Edge searchForEdge(String id, List<Edge> edges) {
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getId().equals(id)) {
				return edges.get(i);
			}
		}
		return null;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Edge.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Edge [edgeId=" + id + ", conjunctionFrom=" + conjunctionFrom + ", conjunctinTo=" + conjunctionTo
			+ ", lanes=" + Arrays.toString(lanes) + "]";
	}

}
