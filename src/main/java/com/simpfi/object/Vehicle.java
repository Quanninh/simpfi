package com.simpfi.object;

import com.simpfi.util.Point;

/**
 * Creates Vehicle class.
 */
public class Vehicle {

	private String id;
	private Point position;
	private double speed;
	private String roadID;
	private String type;
	private double angle;

	public Vehicle(String id, Point point, String roadID,
		String type) {
		this.id = id;
		this.position = point;
		this.roadID = roadID;
		this.type = type;
	}

	public String getID() {
		return this.id;
	}

	public Point getPosition() {
		return this.position;
	}

	public double getSpeed() {
		return this.speed;
	}

	public String getRoadID() {
		return this.roadID;
	}

	public double getAngle() {
		return this.angle;
	}

	public String getType() {
		return this.type;
	}
}
