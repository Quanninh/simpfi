package com.simpfi.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simpfi.util.Point;

/**
 * Creates Junction class (may includes {@link de.tudresden.sumo.cmd.Junction}
 * in the future).
 */
public class Junction {

	/** The junction id. */
	private String id;

	/** The junction type. */
	private String type;

	/** The junction shape. */
	private Point[] shape;

	/** The size of the junction shape. */
	private int shapeSize;

	/** The lanes which ends at the junction. */
	private List<String> incomingLane = new ArrayList<String>();

	/**
	 * Instantiates a new junction.
	 *
	 * @param id    the id
	 * @param type  the type
	 * @param shape the shape
	 */
	public Junction(String id, String type, Point[] shape) {
		this.id = id;
		this.shape = shape;
		this.type = type;
		this.shapeSize = this.shape.length;
	}

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the shape.
	 *
	 * @return the shape
	 */
	public Point[] getShape() {
		return shape;
	}

	/**
	 * Returns the shape size.
	 *
	 * @return the shape size
	 */
	public int getShapeSize() {
		return shapeSize;
	}

	/**
	 * Returns the incoming lane.
	 *
	 * @return the incoming lane
	 */
	public List<String> getIncomingLane() {
		return incomingLane;
	}

	/**
	 * Adds lane to the incoming lanes list.
	 *
	 * @param lane the lane
	 */
	public void addIncomingLane(String lane) {
		incomingLane.add(lane);
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Junction.
	 *
	 * @return the representation of Junction
	 */
	@Override
	public String toString() {
		return "Junction [id=" + id + ", type=" + type + ", shape=" + Arrays.toString(shape) + ", shapeSize="
			+ shapeSize + ", incomingLane=" + incomingLane + "]";
	}

}
