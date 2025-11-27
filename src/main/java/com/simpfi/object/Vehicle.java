package com.simpfi.object;

import java.awt.Color;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.util.Point;

// TODO: Auto-generated Javadoc
/**
 * Creates Vehicle class.
 */
public class Vehicle {

	/** The id. */
	private String id;
	
	/** The position. */
	private Point position;
	
	/** The speed. */
	private double speed;
	
	/** The road ID. */
	private String roadID;
	
	/** The type. */
	private VehicleType type;
	
	/** The angle. */
	private double angle;
	
	/** The width. */
	private double width;
	
	/** The height. */
	private double height;

	/** The is active. */
	private Boolean isActive;

    /**
     * Overloaded Constructor assigning provided parameters
     * Compares the provided type ID to the IDs of all {@link VehicleType} objects in
     * {@code Settings.network}.
     *
     * @param id the id
     * @param point the point
     * @param roadID the road ID
     * @param type the type
     * @param angle the angle
     * @param width the width
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Gets the road ID.
	 *
	 * @return the road ID
	 */
	public String getRoadID() {
		return roadID;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public VehicleType getType() {
		return type;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Gets the checks if is active.
	 *
	 * @return the checks if is active
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets the checks if is active.
	 *
	 * @param isActive the new checks if is active
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the vehicle color.
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

}
