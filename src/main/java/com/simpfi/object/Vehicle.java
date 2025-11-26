package com.simpfi.object;

import java.awt.Color;

import com.simpfi.config.Constants;
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
	private double width;
	private double height;

	private Boolean isActive;

	public Vehicle(String id, Point point, String roadID, String type, double angle, double width, double height) {
		this.id = id;
		this.position = point;
		this.roadID = roadID;
		this.type = type;
		this.angle = angle;
		this.width = width;
		this.height = height;

		isActive = false;
	}

	public String getID() {
		return id;
	}

	public Point getPosition() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}

	public String getRoadID() {
		return roadID;
	}

	public double getAngle() {
		return angle;
	}

	public String getType() {
		return type;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Color getVehicleColor() {
		return switch (type) {
		case "truck" -> Constants.TRUCK_COLOR;
		case "bus" -> Constants.BUS_COLOR;
		case "motorcycle" -> Constants.MOTORCYCLE_COLOR;
		case "emergency" -> Constants.EMERGENCY_COLOR;
		default -> Constants.DEFAULT_VEHICLE_COLOR;
		};
	}

}
