package com.simpfi.object;

public class Edge {

	String edgeId;
	Junction conjunctionFrom;
	Junction conjunctinTo;
	Lane[] lanes;
	int lanesSize;

	public Edge(String edgeId, Junction from, Junction to, Lane[] lanes) {
		this.edgeId = edgeId;
		this.conjunctionFrom = from;
		this.conjunctinTo = to;
		this.lanes = lanes;
		this.lanesSize = this.lanes.length;
	}

	public String getEdgeId() {
		return edgeId;
	}

	public Lane[] getLanes() {
		return lanes;
	}

	public int getLanesSize() {
		return lanesSize;
	}

}
