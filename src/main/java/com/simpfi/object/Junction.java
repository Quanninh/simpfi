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

	private String id;
	private String type;
	private Point[] shape;
	private int shapeSize;
	private List<String> incomingLane = new ArrayList<String>();

	public Junction(String id, String type, Point[] shape) {
		this.id = id;
		this.shape = shape;
		this.type = type;
		this.shapeSize = this.shape.length;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public Point[] getShape() {
		return shape;
	}

	public int getShapeSize() {
		return shapeSize;
	}

	public List<String> getIncomingLane() {
		return incomingLane;
	}

	public void addIncomingLane(String lane) {
		incomingLane.add(lane);
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Junction.
	 */
	@Override
	public String toString() {
		return "Junction [id=" + id + ", type=" + type + ", shape=" + Arrays.toString(shape) + ", shapeSize="
			+ shapeSize + ", incomingLane=" + incomingLane + "]";
	}

}
