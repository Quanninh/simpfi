package com.simpfi.object;

import java.util.Arrays;

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

	@Override
	public String toString() {
		return "Route [id=" + id + ", edges=" + Arrays.toString(edges) + "]";
	}

}
