package com.simpfi.sumo.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.object.Edge;
import com.simpfi.object.Road;

import it.polito.appeal.traci.SumoTraciConnection;

// This will be implemented once we come to Statistics part

/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Edge}.
 */
public class EdgeController{

    /** The connection to SUMO. */
	private SumoTraciConnection connection;

    /** Hash map for mapping Edge ID list of its sub-edges with suffixes (if any) */
	Map<Edge, Road> edgeMap = new HashMap<>();

    /**
	 * Instantiates a new edge controller.
	 *
	 * @param conn the connection to SUMO
	 */
	public EdgeController(SumoConnectionManager conn) {
		this.connection = conn.getConnection();
	}
    
    
}
