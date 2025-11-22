package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Trafficlight;
/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Trafficlight}.
 */
public class TrafficLightController {

	private final SumoTraciConnection conn;

	public TrafficLightController(SumoConnectionManager conn) {
		this.conn = conn.getConnection();
	}

	public String getState(String tlId) throws Exception {
		return (String) conn
			.do_job_get(Trafficlight.getRedYellowGreenState(tlId));
	}

}