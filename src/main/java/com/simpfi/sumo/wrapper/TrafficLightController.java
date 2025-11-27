/*
 * 
 */
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

/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Trafficlight}.
 */
public class TrafficLightController {

	/** The connection to SUMO. */
	private final SumoTraciConnection connection;

	/**
	 * The live traffic light states which stores the states of all traffic lights.
	 */
	private static Map<String, String> liveTrafficLightStates = new HashMap<>();

	/**
	 * Instantiates a new traffic light controller.
	 *
	 * @param conn the connection to SUMO
	 * @throws Exception if the TraCI connection fails
	 */
	public TrafficLightController(SumoConnectionManager conn) throws Exception {
		this.connection = conn.getConnection();
		addConnectionToTrafficLight();
	}

	/**
	 * Returns the ID list of all traffic lights.
	 *
	 * @return the ID list
	 * @throws Exception if the TraCI connection fails
	 */
	@SuppressWarnings("unchecked")
	public List<String> getIDList() throws Exception {
		return (List<String>) connection.do_job_get(Trafficlight.getIDList());
	}

	/**
	 * Returns the trafic light state.
	 *
	 * @param tlId the traffic light ID
	 * @return the state of the traffic light
	 * @throws Exception if the TraCI connection fails
	 */
	public String getState(String tlId) throws Exception {
		return (String) connection.do_job_get(Trafficlight.getRedYellowGreenState(tlId));
	}

	/**
	 * Sets the duration.
	 *
	 * @param tlId     the traffic light ID
	 * @param duration the duration
	 * @throws Exception if the TraCI connection fails
	 */
	public void setDuration(String tlId, double duration) throws Exception {
		connection.do_job_get(Trafficlight.setPhaseDuration(tlId, duration));
	}

	/**
	 * Update the traffic light state.
	 *
	 * @param tlId  the traffic light ID
	 * @param state the state
	 */
	public static void updateTrafficLightState(String tlId, String state) {
		liveTrafficLightStates.put(tlId, state);
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
	 * Returns the controlled links.
	 *
	 * @param tlId the traffic light ID
	 * @return the list of controlled links
	 * @throws Exception if the TraCI connection fails
	 */
	@SuppressWarnings("unchecked")
	public List<SumoLink> getControlledLinks(String tlId) throws Exception {
		return (List<SumoLink>) connection.do_job_get(Trafficlight.getControlledLinks(tlId));
	}

	/**
	 * Adds the connection to the traffic light.
	 *
	 * @throws Exception if the TraCI connection fails
	 */
	public void addConnectionToTrafficLight() throws Exception {
		List<TrafficLight> allTrafficLights = Settings.network.getTrafficLights();
		List<Edge> edges = Settings.network.getEdges();

		for (TrafficLight tl : allTrafficLights) {
			String idJunction = tl.getJunction().getId();
			List<SumoLink> controlledLinks = this.getControlledLinks(idJunction);
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