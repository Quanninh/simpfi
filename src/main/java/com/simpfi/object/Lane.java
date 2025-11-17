package com.simpfi.object;

import com.simpfi.util.Point;

public class Lane {

	String laneId;
	Point[] shape;
	
	public Lane(String laneId, Point[] shape) {
		this.laneId = laneId;
		this.shape = shape;
	}
	
}
