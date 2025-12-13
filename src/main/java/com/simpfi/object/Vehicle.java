package com.simpfi.object;

import java.awt.Color;

import com.simpfi.config.Settings;
import com.simpfi.util.Point;

import java.util.ArrayList;
import java.util.List;

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

    private double maxSpeed;
    private double acceleration;
    private double distance;
    private List<String> route;

    /**
	 * The active state of the vehicle. A vehicle is considered inactive if it has
	 * completed its journey.
	 */
	private Boolean isActive;
	
	/** Lights and states */
	private boolean headlightsOn = true;
	private boolean brakeOn = false;
	private boolean turningLeft = false;
	private boolean turningRight = false;
	private boolean emergencyFlashing = false;

	public enum Turn {
        LEFT,
        RIGHT,
        STRAIGHT,
        NONE
    }
	private Turn nextTurn = Turn.NONE;




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
	 * @param speed the speed
	 */

	public Vehicle(String id, Point point, String roadID, String type, double angle, double width, double height, double speed, double maxSpeed, double acceleration, double distance, List<String> route) {
		this.id = id;
		this.position = point;
		this.roadID = roadID;
		this.angle = angle;
		this.width = width;
		this.height = height;
		this.speed = speed;

		this.type = null;

        this.route = route;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.distance = distance;

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

    public double getMaxSpeed() { return maxSpeed; }

    public double getAcceleration() { return acceleration; }

    public double getDistance() { return distance; }

    public List<String> getRoute() { return route; }


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
		case "truck" -> Settings.config.TRUCK_COLOR;
		case "bus" -> Settings.config.BUS_COLOR;
		case "motorcycle" -> Settings.config.MOTORCYCLE_COLOR;
		case "emergency" -> Settings.config.EMERGENCY_COLOR;
		default -> Settings.config.NORMAL_VEHICLE_COLOR;
		};
	}
	/**
	 * Returns the edge derived from RoadID.
	 *
	 * @return the {@code Edge} object
	 */
	public Edge getEdgeFromRoadID(){
		for (Edge e : Settings.network.getEdges()) {
			if (e.getId().equals(this.roadID)) {
				return e;
			}
		}
		return null;
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

	/**
	 * Returns whether the vehicle's headlights are currently on.
	 * 
	 * @return {@code true} if headlights are on, {@code false} otherwise
	 */
	public boolean headlightsOn() {
		return headlightsOn;
	}


	/**
	 * Sets the state of the vehicle's headlights.
	 * 
	 * @param state {@code true} to turn headlights on, {@code false} to turn them off
	 */
	public void setHeadLightsOn(boolean state){
		this.headlightsOn = state;
	}

	/**
	 * Returns whether the vehicle is currently braking.
	 * 
	 * @return {@code true} if brakes are applied, {@code false} otherwise
	 */
	public boolean isBraking(){
		return brakeOn;
	}

	/**
	 * Sets the braking state of the vehicle.
	 * 
	 * @param state {@code true} to apply brakes, {@code false} to release them
	 */
	public void setBrake(boolean state){
		this.brakeOn = state;
	}

	/**
	 * Returns whether the vehicle is currently turning left.
	 * 
	 * @return {@code true} if the left turn signal is active, {@code false} otherwise
	 */
	public boolean isTurningLeft(){
		return turningLeft;
	}

	/**
	 * Sets the left turn signal state.
	 * 
	 * @param state {@code true} to indicate a left turn, {@code false} otherwise
	 */
	public void setTurningLeft(boolean state){
		this.turningLeft = state;
	}

	/**
	 * Returns whether the vehicle is currently turning right.
	 * 
	 * @return {@code true} if the right turn signal is active, {@code false} otherwise
	 */
	public boolean isTurningRight(){
		return turningRight;
	}

	/**
	 * Sets the right turn signal state.
	 * 
	 * @param state {@code true} to indicate a right turn, {@code false} otherwise
	 */
	public void setTurningRight(boolean state){
		this.turningRight = state;
	}

	/**
	 * Returns whether the vehicle's emergency lights are flashing.
	 * 
	 * @return {@code true} if emergency lights are on, {@code false} otherwise
	 */
	public boolean isEmergencyFlashing(){
		return emergencyFlashing;
	}

	/**
	 * Sets the state of the vehicle's emergency lights.
	 * 
	 * @param state {@code true} to turn on emergency lights, {@code false} to turn them off
	 */
	public void setEmergencyFlashing(boolean state){
		this.emergencyFlashing = state;
	}

	/**
	 * Returns the next turn that the vehicle will take.
	 * 
	 * @return a {@link Turn} object representing the next turn
	 */
	public Turn getNextTurn() { 
		return nextTurn; 
	}

	/**
	 * Sets the next turn for the vehicle.
	 * 
	 * @param t a {@link Turn} object representing the next turn
	 */
    public void setNextTurn(Turn t) { 
		this.nextTurn = t; 
	}
}
