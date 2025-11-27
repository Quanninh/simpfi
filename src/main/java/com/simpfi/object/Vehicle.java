package com.simpfi.object;

import java.awt.Color;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.util.Point;

/**
 * Creates Vehicle class.
 */
public class Vehicle {

	/** The vehicle id. */
	private String id;

	/** The vehicle position. */
	private Point position;

	/** The vehicle speed. */
	private double speed;

	/** The road ID that the vehicle is on. */
	private String roadID;

	/** The vehicle type. */
	private VehicleType type;

	/** The vehicle angle. */
	private double angle;

	/** The vehicle width. */
	private double width;

	/** The vehicle height. */
	private double height;

	/** The active state of the vehicle. */
	private Boolean isActive;

	/**
	 * Overloaded Constructor assigning provided parameters. Compares the provided
	 * type ID to the IDs of all {@link VehicleType} objects in
	 * {@code Settings.network}.
	 *
	 * @param id     the id
	 * @param point  the point
	 * @param roadID the road ID
	 * @param type   the type
	 * @param angle  the angle
	 * @param width  the width
	 * @param height the height
	 */

	public Vehicle(String id, Point point, String roadID, String type, double angle, double width, double height) {
		this.id = id;
		this.position = point;
		this.roadID = roadID;
		this.angle = angle;
		this.width = width;
		this.height = height;

		this.type = null;

		for (VehicleType vType : Settings.network.getVehicleTypes()) {
			if (vType.getId().equals(type)) {
				this.type = vType;
				break;
			}
		}
		if (this.type == null) {
			System.err.println("Invalid vehicle type: " + type);
		}

		isActive = false;
	}

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the position.
	 *
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Returns the speed.
	 *
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Returns the road ID.
	 *
	 * @return the road ID
	 */
	public String getRoadID() {
		return roadID;
	}

	/**
	 * Returns the angle.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Returns the type.
	 *
	 * @return the type
	 */
	public VehicleType getType() {
		return type;
	}

	/**
	 * Returns the width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Returns the height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Returns the checks if vehicle is active.
	 *
	 * @return the checks if vehicle is active
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets the active state of vehicle.
	 *
	 * @param isActive the active state of vehicle
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Returns the vehicle color based on type. Vehicle colors are defined in
	 * {@link com.simpfi.config.Constants}
	 *
	 * @return the vehicle color
	 */
	public Color getVehicleColor() {
		return switch (type.getId()) {
		case "truck" -> Constants.TRUCK_COLOR;
		case "bus" -> Constants.BUS_COLOR;
		case "motorcycle" -> Constants.MOTORCYCLE_COLOR;
		case "emergency" -> Constants.EMERGENCY_COLOR;
		default -> Constants.DEFAULT_VEHICLE_COLOR;
		};
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Vehicle.
	 *
	 * @return the representation of Vehicle
	 */
	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", position=" + position + ", speed=" + speed + ", roadID=" + roadID + ", type="
			+ type + ", isActive=" + isActive + "]";
	}

}
