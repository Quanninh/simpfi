package com.simpfi.object;

public class Edge {

	String edgeId;
	Junction conjunctionFrom;
	Junction conjunctinTo;
	Lane[] lanes;
	
	
	public Edge(String id, Junction from, Junction to, Lane[] l) {
		edgeId = id;
		conjunctionFrom = from;
		conjunctinTo = to;
		lanes = l;
	}
	
}
