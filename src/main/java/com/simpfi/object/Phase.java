package com.simpfi.object;

/**
 * Creates Phase class for {@link TrafficLight}.
 */
public class Phase {
	private double duration;
	private String state;

	public Phase(double duration, String state) {
		this.duration = duration;
		this.state = state;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Phase.
	 */
	@Override
	public String toString() {
		return "Phase [duration=" + duration + ", state=" + state + "]";
	}

}
