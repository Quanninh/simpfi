package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Trafficlight;
import java.util.List;
/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Trafficlight}.
 */
public class TrafficLightController {

	private final SumoTraciConnection conn;

	public TrafficLightController(SumoConnectionManager conn) {
		this.conn = conn.getConnection();
	}

	public List<String> getIDList()throws Exception {
		return (List<String>) conn.do_job_get(Trafficlight.getIDList());
	}

	public String getState(String tlId) throws Exception {
		return (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(tlId));
	}
	
	public String setDuration(String tlId, double duration) throws Exception {
		return (String) conn
			.do_job_get(Trafficlight.setPhaseDuration(tlId, duration));
	}
	
}