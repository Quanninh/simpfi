package com.simpfi.object;

import java.util.Arrays;
import java.util.List;

import com.simpfi.util.Point;

// TODO: Auto-generated Javadoc
/**
 * Creates Lane class (may includes {@link de.tudresden.sumo.cmd.Lane} in the
 * future).
 */
public class Lane {

	/** The lane id. */
	private String laneId;
	
	/** The shape. */
	private Point[] shape;
	
	/** The shape size. */
	private int shapeSize;

	/**
	 * Instantiates a new lane.
	 *
	 * @param laneId the lane id
	 * @param shape the shape
	 */
	public Lane(String laneId, Point[] shape) {
		this.laneId = laneId;
		this.shape = shape;
		this.shapeSize = this.shape.length;
	}
	
	/**
	 * Instantiates a new lane.
	 *
	 * @param laneId the lane id
	 */
	public Lane(String laneId)
	{
		this.laneId = laneId;
	}

	/**
	 * Gets the lane id.
	 *
	 * @return the lane id
	 */
	public String getLaneId() {
		return laneId;
	}

	/**
	 * Gets the shape.
	 *
	 * @return the shape
	 */
	public Point[] getShape() {
		return shape;
	}

	/**
	 * Gets the shape size.
	 *
	 * @return the shape size
	 */
	public int getShapeSize() {
		return shapeSize;
	}
	
	/**
	 * Search for lane.
	 *
	 * @param id the id
	 * @param edges the edges
	 * @return the lane
	 */
	public static Lane searchForLane(String id, List<Edge> edges) {
		String edgeId = id.split("_")[0];
		Edge edge = Edge.searchForEdge(edgeId, edges);
		Lane[] lanes = edge.getLanes();
		for (int i = 0; i < lanes.length; i++) {
			if (lanes[i].getLaneId().equals(id)) {
				return lanes[i];
			}
		}
		return null;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Lane.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Lane [laneId=" + laneId + ", shape=" + Arrays.toString(shape) + "]";
	}

}
