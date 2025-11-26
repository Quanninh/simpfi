package com.simpfi.object;

import java.awt.Color;
import java.util.Arrays;

import com.simpfi.config.Settings;

/**
 * Creates TrafficLight class.
 */
public class TrafficLight {
	private String type;
	private Junction junction;
	private Lane[] lanes;
	private Phase[] phase;

	public TrafficLight() {

	}

	public TrafficLight(Junction junction, String type, Lane[] lanes) {
		this.junction = junction;
		this.type = type;
		this.lanes = lanes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Phase[] getPhase() {
		return phase;
	}

	public void setPhase(Phase[] phase) {
		this.phase = phase;
	}

	public Junction getJunction() {
		return junction;
	}

	public Lane[] getLanes() {
		return lanes;
	}

	public void setJunction(Junction junction) {
		this.junction = junction;
	}

	public String getTLState() {
		String defaultState = this.getPhase()[0].getState();
		return Settings.getLiveTrafficLightStates().getOrDefault(this.getJunction().getId(), defaultState);
	}

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
	 */
	@Override
	public String toString() {
		return "TrafficLight [type=" + type + ", junction=" + junction + ", lanes=" + Arrays.toString(lanes)
			+ ", phase=" + Arrays.toString(phase) + "]";
	}

}
