package com.simpfi.object;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simpfi.sumo.wrapper.TrafficLightController;

/**
 * Creates TrafficLight class.
 */
public class TrafficLight {

	/** The type of the traffic light. Most of the traffic lights we used have 
	 * static type, which fixed cycle time and predefined duration  */
	private String type;

	/** Each traffic light links to only one junction */
	private Junction junction;


	/** The traffic light phase, which specify the status at a given time 
	 * of the traffic light*/
	private Phase[] phase;

	/** The traffic light connections. This one shows all the connections from 
	 * one Lane to another Lane. And each of this must specify the color of the traffic light*/
	private List<Connection> connections = new ArrayList<Connection>();

	/**
	 * Instantiates a new traffic light.
	 *
	 * @param junction the junction
	 * @param type     the type
	 */
	public TrafficLight(Junction junction, String type) {
		this.junction = junction;
		this.type = type;
	}

	/**
	 * Returns the phase.
	 *
	 * @return the phase
	 */
	public Phase[] getPhase() {
		return phase;
	}

	/**
	 * Sets the phase.
	 *
	 * @param phase the new phase
	 */
	public void setPhase(Phase[] phase) {
		this.phase = phase;
	}

	/**
	 * Returns the junction.
	 *
	 * @return the junction
	 */
	public Junction getJunction() {
		return junction;
	}

	/**
	 * Sets the junction.
	 *
	 * @param junction the new junction
	 */
	public void setJunction(Junction junction) {
		this.junction = junction;
	}

	/**
	 * Returns the TL state.
	 *
	 * @return the TL state
	 */
	public String getTLState() {
		String defaultState = this.getPhase()[0].getState();
		return TrafficLightController.getLiveTrafficLightStates().getOrDefault(this.getJunction().getId(),
			defaultState);
	}

	/**
	 * Returns the connections.
	 *
	 * @return the connections
	 */
	public List<Connection> getConnections() {
		return connections;
	}

	/**
	 * Sets the connections.
	 *
	 * @param connections the new connections
	 */
	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	/**
	 * Returns the traffic light color. Traffic light signals can be one of the
	 * letters ryGgsuoO but currently, only ryGg are implemented, the other signals
	 * are default to black.
	 *
	 * @param signal the signal of traffic light
	 * @return the traffic light color
	 */
	public static Color getTrafficLightColor(char signal) {
		return switch (signal) {
		case 'G' -> Color.GREEN;
		case 'g' -> Color.GREEN.darker();
		case 'y' -> Color.YELLOW;
		case 'r' -> Color.RED;
		default -> Color.BLACK;
		};
	}


	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of TrafficLight.
	 *
	 * @return the string
	 */

	@Override
	public String toString() {
		return "TrafficLight [type=" + type + ", junction=" + junction + ", phase=" + Arrays.toString(phase)
				+ ", connections=" + connections + "]";
	}


}
