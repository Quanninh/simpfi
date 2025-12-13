package com.simpfi.sumo.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.Edge;
import com.simpfi.object.Lane;
import com.simpfi.object.Phase;
import com.simpfi.object.TrafficLight;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoLink;
import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;
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

	private static SumoTLSProgram prog;

	private VehicleController vehicleController;

	/**
	 * Instantiates a new traffic light controller.
	 *
	 * @param conn the connection to SUMO
	 * @throws Exception if the TraCI connection fails
	 */
	public TrafficLightController(SumoConnectionManager conn) throws Exception {
		this.connection = conn.getConnection();
		addConnectionToTrafficLight();
		vehicleController = new VehicleController(conn);
	}

	// Dedicated method to install a program
	public void installProgram(String tlId, Phase[] myPhases) throws Exception {
		for (Phase p : myPhases) {
			prog.add(new SumoTLSPhase((int) p.getDuration(), p.getState()));
		}
		this.setCompleteTrafficLight(tlId, prog);
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
		connection.do_job_set(Trafficlight.setPhaseDuration(tlId, duration));
	}

	public void setCompleteTrafficLight(String tlId, SumoTLSProgram program) throws Exception {
		connection.do_job_set(Trafficlight.setCompleteRedYellowGreenDefinition(tlId, program));
	}

	public void setPhase(String tlId, int index) throws Exception {
		connection.do_job_set(Trafficlight.setPhase(tlId, index));
	}

	public double getDuration(String tlId) throws Exception {
		return (Double) connection.do_job_get(Trafficlight.getPhaseDuration(tlId));
	}

	public String getProgramName(String tlId) throws Exception {
		return (String) connection.do_job_get(Trafficlight.getProgram(tlId));
	}

	public SumoTLSController getCompletedTrafficLightDefinition(String tlId) throws Exception {
		return (SumoTLSController) connection.do_job_get(Trafficlight.getCompleteRedYellowGreenDefinition(tlId));
	}

	public Integer getPhase(String tlId) throws Exception {
		return (Integer) connection.do_job_get(Trafficlight.getPhase(tlId));
	}

	public Double getNextSwitch(String tlId) throws Exception {
		return (Double) connection.do_job_get(Trafficlight.getNextSwitch(tlId));
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
	 * @param tlId the traffic light id which can be specify via junction id
	 * @return a list of class SumoLink, which is from
	 *         {@link de.tudresden.sumo.objects.SumoLink} which contains attribute
	 *         "from" and "to"
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

	/** * Show all the signal of the specific connection */
	public char getStateofLane(List<Connection> allConnections, String laneId, SumoTLSPhase phase) {
		String stateofPhase = phase.phasedef;

		for (int i = 0; i < allConnections.size(); i++) {
			Lane fromLane = allConnections.get(i).getFromLane();
			if (fromLane.getLaneId().equals(laneId)) {
				return stateofPhase.charAt(i);
			}
		}
		return 0;
	}

	/** Get the program from the specific traffic light */
	public SumoTLSProgram getProgramFromTrafficLight(String trafficLightID) throws Exception {
		SumoTLSController trafficLightCompleted = this.getCompletedTrafficLightDefinition(trafficLightID);
		String programName = this.getProgramName(trafficLightID);
		return trafficLightCompleted.get(programName);
	}

	// Update the duration if the number of lane is larger than 3
	public void updateTrafficLightByNumberOfVehicle(List<TrafficLight> trafficLights) throws Exception {
		for (int i = 0; i < trafficLights.size(); i++) {
			String trafficLightID = trafficLights.get(i).getJunction().getId();
			SumoTLSProgram program = getProgramFromTrafficLight(trafficLightID);
			SumoTLSPhase currentPhase = program.phases.get(program.currentPhaseIndex);
			Phase defaultPhase = TrafficLight.searchforTrafficLight(trafficLightID, trafficLights)
					.getPhase()[program.currentPhaseIndex];

			List<Connection> allConnection = trafficLights.get(i).getConnections();
			int flag = 0; // The flag shows if the traffic light, there exists a lane with high cogestion
							// or not
			for (int j = 0; j < allConnection.size(); j++) {
				Lane fromLane = allConnection.get(j).getFromLane();
				int numberofVehicleInLane = vehicleController.getVehicleNumberInLane(fromLane.getLaneId());
				if (numberofVehicleInLane >= 3) {
					flag = 1;
					break;
				}
			}
			if (flag == 1) {
				currentPhase.duration = defaultPhase.getDuration() + 5;
			} else {
				currentPhase.duration = defaultPhase.getDuration();
			}
			this.setCompleteTrafficLight(trafficLightID, program);
		}
	}

	public void setDefaultTrafficLight(List<TrafficLight> trafficLights) throws Exception {
		for (int i = 0; i < trafficLights.size(); i++) {
			String trafficLightID = trafficLights.get(i).getJunction().getId();
			SumoTLSProgram program = getProgramFromTrafficLight(trafficLightID);
			SumoTLSPhase currentPhase = program.phases.get(program.currentPhaseIndex);
			Phase defaultPhase = TrafficLight.searchforTrafficLight(trafficLightID, trafficLights)
					.getPhase()[program.currentPhaseIndex];
			currentPhase.duration = defaultPhase.getDuration();
			this.setCompleteTrafficLight(trafficLightID, program);
		}
	}
}