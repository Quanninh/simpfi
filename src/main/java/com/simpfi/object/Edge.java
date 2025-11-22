package com.simpfi.object;

import java.util.Arrays;
/**
 * Creates Edge class (may inherits {@link de.tudresden.sumo.cmd.Edge} in the future)
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
	 * Overrides the built-in method toString() to provide a human-readable representation of Edge.
	 */
	@Override
	public String toString() {
		return "Edge [edgeId=" + id + ", conjunctionFrom=" + conjunctionFrom
			+ ", conjunctinTo=" + conjunctinTo + ", lanes="
			+ Arrays.toString(lanes) + "]";
	}

}
