package com.simpfi.sumo.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.object.Edge;
import com.simpfi.object.Road;

import it.polito.appeal.traci.SumoTraciConnection;

/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Edge}.
 */
public class EdgeController {

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

	/** Get all edge ids on the network */
	@SuppressWarnings("unchecked")
	public List<String> getEdgeIDs() throws Exception {
		return (List<String>) connection.do_job_get(de.tudresden.sumo.cmd.Edge.getIDList());
	}

	/** Get the number of vehicles currently on a specific edge */
	public int getEdgeVehicleCount(String edgeID) throws Exception {
		return (Integer) connection.do_job_get(de.tudresden.sumo.cmd.Edge.getLastStepVehicleNumber(edgeID));
	}

}
