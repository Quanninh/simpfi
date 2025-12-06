package com.simpfi.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates Road class, which stores a list of edges with the same base name 
 * and not included ":J" edges.
 */
public class Road{

    /** The list of edges with the same base name */
    List<Edge> edgesWithSameBaseName = new ArrayList<>();

    /** The base name (id) of edges */
    String id;

    /**
     * Instantiates a Road.
     * 
     * @param eBaseId the base name which selected edges have
     * @param edges list of edges with the same base name
     */
    public Road(String eBaseId, Edge[] edges){
        id = eBaseId;
        edgesWithSameBaseName = new ArrayList<>(Arrays.asList(edges));
    }

    /**
     * Gets the base ID of the road.
     * 
     * @return the base ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the base ID of the road.
     * 
     * @param baseId the base ID to set
     */
    public void setId(String baseId) {
        this.id = baseId;
    }

    /**
     * Gets the list of edges with the same base name.
     * 
     * @return the list of edges
     */
    public List<Edge> getEdgesWithSameBaseName() {
        return edgesWithSameBaseName;
    }

    /**
     * Sets the list of edges with the same base name.
     * 
     * @param edgesWithSameBaseName the list of edges to set
     */
    public void setEdgesWithSameBaseName(List<Edge> edgesWithSameBaseName) {
        this.edgesWithSameBaseName = edgesWithSameBaseName;
    }

    /**
     * Adds new edge to {@code edgesWithSameBaseName}
     * 
     * @param edge the added edge
     */
    public void addEdge(Edge edge){
        this.edgesWithSameBaseName.add(edge);
    }

    /**
	 * Used to search over a list of roads to find one with the matched id.
	 * 
	 * @param id    the id of the road to be searched for
	 * @param roads the list of roads
	 * @return the road with the passed id, {@code null} if not found
	 */
	public static Road searchForRoad(String id, List<Road> roads) {
		for (int i = 0; i < roads.size(); i++) {
			if (roads.get(i).getId().equals(id)) {
				return roads.get(i);
			}
		}
		return null;
	}
}