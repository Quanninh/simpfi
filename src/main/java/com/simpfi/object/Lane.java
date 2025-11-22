package com.simpfi.object;

import java.util.Arrays;

import com.simpfi.util.Point;
/**
 * Creates Lane class (may includes {@link de.tudresden.sumo.cmd.Lane} in the future).
 */
public class Lane {

	private String laneId;
	private Point[] shape;
	private int shapeSize;

	public Lane(String laneId, Point[] shape) {
		this.laneId = laneId;
		this.shape = shape;
		this.shapeSize = this.shape.length;
	}

	public String getLaneId() {
		return laneId;
	}

	public Point[] getShape() {
		return shape;
	}

	public int getShapeSize() {
		return shapeSize;
	}
	/**
	 * Overrides the built-in method toString() to provide a human-readable representation of Lane.
	 */
	@Override
	public String toString() {
		return "Lane [laneId=" + laneId + ", shape=" + Arrays.toString(shape)
			+ "]";
	}

}
