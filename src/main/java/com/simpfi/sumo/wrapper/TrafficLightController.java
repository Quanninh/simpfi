package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Trafficlight;
<<<<<<< HEAD
/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Trafficlight}.
 */
=======
import java.util.List;

>>>>>>> 159e818aa56c7b0166ff5d331813623d0c957e17
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

}