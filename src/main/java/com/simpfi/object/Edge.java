package com.simpfi.object;

import java.util.Arrays;
import java.util.List;

/**
 * Creates Edge class (may includes {@link de.tudresden.sumo.cmd.Edge} in the
 * future).
 */
public class Edge {

	String id;
	Junction conjunctionFrom;
	Junction conjunctinTo;
	Lane[] lanes;
	int lanesSize;

	public Edge(String id, Junction from, Junction to, Lane[] lanes) {
		this.id = id;
		this.conjunctionFrom = from;
		this.conjunctinTo = to;
		this.lanes = lanes;
		this.lanesSize = this.lanes.length;
	}

	public String getId() {
		return id;
	}

	public Lane[] getLanes() {
		return lanes;
	}

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
	 */
	@Override
	public String toString() {
		return "Edge [edgeId=" + id + ", conjunctionFrom=" + conjunctionFrom + ", conjunctinTo=" + conjunctinTo
			+ ", lanes=" + Arrays.toString(lanes) + "]";
	}

}
