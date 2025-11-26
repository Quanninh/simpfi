package com.simpfi.sumo.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudresden.sumo.cmd.Trafficlight;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Trafficlight}.
 */
public class TrafficLightController {

	private final SumoTraciConnection conn;
	private static Map<String, String> liveTrafficLightStates = new HashMap<>();

	public TrafficLightController(SumoConnectionManager conn) {
		this.conn = conn.getConnection();
	}

	@SuppressWarnings("unchecked")
	public List<String> getIDList() throws Exception {
		return (List<String>) conn.do_job_get(Trafficlight.getIDList());
	}

	public String getState(String tlId) throws Exception {
		return (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(tlId));
	}

	public void setDuration(String tlId, double duration) throws Exception {
		conn.do_job_get(Trafficlight.setPhaseDuration(tlId, duration));
	}

	public static void updateTrafficLightState(String id, String state) {
		liveTrafficLightStates.put(id, state);
	}

	public static Map<String, String> getLiveTrafficLightStates() {
		return liveTrafficLightStates;
	}

}