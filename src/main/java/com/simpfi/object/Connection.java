package com.simpfi.object;

import java.util.List;

/**
 * Represents a directed connection between two lanes in the network. Vehicles
 * can move from one lane to another.
 */
public class Connection {

	/** The lane at which the connection begins. */
	private Lane fromLane;

	/** The lane at which the connection ends. */
	private Lane toLane;

	/**
	 * Instantiates a new connection.
	 *
	 * @param fromLane the lane at which the connection begins
	 * @param toLane   the lane at which the connection ends
	 */
	public Connection(Lane fromLane, Lane toLane) {
		this.fromLane = fromLane;
		this.toLane = toLane;
	}

	/**
	 * Gets the lane at which the connection begins.
	 *
	 * @return the lane at which the connection begins
	 */
	public Lane getFromLane() {
		return fromLane;
	}

	/**
	 * Sets the lane at which the connection begins.
	 *
	 * @param fromLane the new lane at which the connection begins
	 */
	public void setFromLane(Lane fromLane) {
		this.fromLane = fromLane;
	}

	/**
	 * Gets the lane at which the connection ends.
	 *
	 * @return the lane at which the connection ends
	 */
	public Lane getToLane() {
		return toLane;
	}

	/**
	 * Sets the lane at which the connection ends.
	 *
	 * @param toLane the new lane at which the connection ends
	 */
	public void setToLane(Lane toLane) {
		this.toLane = toLane;
	}

	/**
	 * Sets the lane at which the connection ends.
	 *
	 * @param toLane the new lane at which the connection ends
	 */
	public static Connection searchforConnection(String fromLaneID, List<Connection> allConnections) {
		for (int i = 0; i < allConnections.size(); i++) {
			if (allConnections.get(i).fromLane.getLaneId().equals(fromLaneID)) {
				return allConnections.get(i);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return fromLane.getLaneId() + "  " + toLane.getLaneId();
	}

	
}
