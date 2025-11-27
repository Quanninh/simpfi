package com.simpfi.object;

/**
 * Creates Phase class for {@link TrafficLight}.
 */
public class Phase {

	/** The phase duration. */
	private double duration;

	/** The phase state. */
	private String state;

	/**
	 * Instantiates a new phase.
	 *
	 * @param duration the duration
	 * @param state    the state
	 */
	public Phase(double duration, String state) {
		this.duration = duration;
		this.state = state;
	}

	/**
	 * Returns the duration.
	 *
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration the new duration
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * Returns the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Phase.
	 *
	 * @return the representation of Phase
	 */
	@Override
	public String toString() {
		return "Phase [duration=" + duration + ", state=" + state + "]";
	}

}
