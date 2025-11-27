package com.simpfi.sumo.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.Edge;
import com.simpfi.object.Lane;
import com.simpfi.object.TrafficLight;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoLink;
import it.polito.appeal.traci.SumoTraciConnection;

// TODO: Auto-generated Javadoc
/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Trafficlight}.
 */
public class TrafficLightController {

	/** The conn. */
	private final SumoTraciConnection conn;

/** The live traffic light states. */
//	private List<Connection> connections;
	private static Map<String, String> liveTrafficLightStates = new HashMap<>();

	/**
	 * Instantiates a new traffic light controller.
	 *
	 * @param conn the conn
	 * @throws Exception the exception
	 */
	public TrafficLightController(SumoConnectionManager conn) throws Exception {
		this.conn = conn.getConnection();
		addConnectionToTrafficLight();
	}

	/**
	 * Gets the ID list.
	 *
	 * @return the ID list
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getIDList() throws Exception {
		return (List<String>) conn.do_job_get(Trafficlight.getIDList());
	}

	/**
	 * Gets the state.
	 *
	 * @param tlId the tl id
	 * @return the state
	 * @throws Exception the exception
	 */
	public String getState(String tlId) throws Exception {
		return (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(tlId));
	}

	/**
	 * Sets the duration.
	 *
	 * @param tlId the tl id
	 * @param duration the duration
	 * @throws Exception the exception
	 */
	public void setDuration(String tlId, double duration) throws Exception {
		conn.do_job_get(Trafficlight.setPhaseDuration(tlId, duration));
	}

	/**
	 * Update traffic light state.
	 *
	 * @param id the id
	 * @param state the state
	 */
	public static void updateTrafficLightState(String id, String state) {
		liveTrafficLightStates.put(id, state);
	}

	/**
	 * Gets the live traffic light states.
	 *
	 * @return the live traffic light states
	 */
	public static Map<String, String> getLiveTrafficLightStates() {
		return liveTrafficLightStates;
	}

	/**
	 * Controlled links.
	 *
	 * @param tlId the tl id
	 * @return the list
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public List<SumoLink> controlledLinks(String tlId) throws Exception {
		return (List<SumoLink>) conn.do_job_get(Trafficlight.getControlledLinks(tlId));
	}

	/**
	 * Adds the connection to traffic light.
	 *
	 * @throws Exception the exception
	 */
	public void addConnectionToTrafficLight() throws Exception {
		List<TrafficLight> allTrafficLights = Settings.network.getTrafficLights();
		List<Edge> edges = Settings.network.getEdges();

		for (TrafficLight tl : allTrafficLights) {
			String idJunction = tl.getJunction().getId();
			List<SumoLink> controlledLinks = this.controlledLinks(idJunction);
			List<Connection> connections = new ArrayList<Connection>();

			for (int i = 0; i < controlledLinks.size(); i++) {
				SumoLink signalLinks = controlledLinks.get(i);
				
				String incomingLaneString = signalLinks.from;
				Lane fromLane = Lane.searchForLane(incomingLaneString, edges);
				
				String outgoingLaneString = signalLinks.to;
				Lane toLane = Lane.searchForLane(outgoingLaneString, edges);
				
				Connection connection = new Connection(fromLane, toLane);
				connections.add(connection);
			}
			
			tl.setConnections(connections);
		}

	}

}