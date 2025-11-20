package com.simpfi.object;

import java.util.Arrays;

import com.simpfi.util.Point;

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

	@Override
	public String toString() {
		return "Lane [laneId=" + laneId + ", shape=" + Arrays.toString(shape)
			+ "]";
	}
	
}
