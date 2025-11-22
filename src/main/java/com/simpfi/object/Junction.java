package com.simpfi.object;

import java.util.Arrays;

import com.simpfi.util.Point;
/**
 * Creates Junction class (may includes {@link de.tudresden.sumo.cmd.Junction} in the future)
 */
public class Junction {

	private String id;
	private String type;
	private Point[] shape;
	private int shapeSize;

	public Junction() {

	}

	public Junction(String id, String type, Point[] shape) {
		this.id = id;
		this.shape = shape;
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
	/**
	 * Overrides the built-in method toString() to provide a human-readable representation of Junction.
	 */
	@Override
	public String toString() {
		return "Junction [id=" + id + ", type=" + type + ", shape="
			+ Arrays.toString(shape) + "]";
	}

}
