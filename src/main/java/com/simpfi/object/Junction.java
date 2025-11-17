package com.simpfi.object;

import com.simpfi.util.Point;

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

}
