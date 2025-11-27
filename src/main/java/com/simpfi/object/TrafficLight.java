package com.simpfi.object;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simpfi.sumo.wrapper.TrafficLightController;

// TODO: Auto-generated Javadoc
/**
 * Creates TrafficLight class.
 */
public class TrafficLight {
	
	/** The type. */
	private String type;
	
	/** The junction. */
	private Junction junction;
	
	/** The lanes. */
	private Lane[] lanes;
	
	/** The phase. */
	private Phase[] phase;
	
	/** The connections. */
	private List<Connection> connections = new ArrayList<Connection>();

	/**
	 * Instantiates a new traffic light.
	 *
	 * @param junction the junction
	 * @param type the type
	 * @param lanes the lanes
	 */
	public TrafficLight(Junction junction, String type, Lane[] lanes) {
		this.junction = junction;
		this.type = type;
		this.lanes = lanes;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the phase.
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
	 * Gets the junction.
	 *
	 * @return the junction
	 */
	public Junction getJunction() {
		return junction;
	} 

	/**
	 * Gets the lanes.
	 *
	 * @return the lanes
	 */
	public Lane[] getLanes() {
		return lanes;
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
	 * Gets the TL state.
	 *
	 * @return the TL state
	 */
	public String getTLState() {
		String defaultState = this.getPhase()[0].getState();
		return TrafficLightController.getLiveTrafficLightStates().getOrDefault(this.getJunction().getId(),
			defaultState);
	}
	

	/**
	 * Gets the connections.
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
	 * Gets the traffic light color.
	 *
	 * @param signal the signal
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
		return "TrafficLight [type=" + type + ", junction=" + junction + ", lanes=" + Arrays.toString(lanes)
			+ ", phase=" + Arrays.toString(phase) + "]";
	}
	
	

}
