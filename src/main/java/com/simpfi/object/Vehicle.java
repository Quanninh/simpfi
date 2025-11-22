package com.simpfi.object;
/**
 * Creates Vehicle class.
 */
public class Vehicle {

	private String id;
	private double x;
	private double y;
	private double speed;
	private String roadID;
	private String type;
	private double angle;

	public Vehicle(String id, double x, double y, double speed, String roadID, String type, double angle){
		this.id = id;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.roadID = roadID;
		this.type = type;
		this.angle = angle;
	}

	public String getID(){
		return this.id;
	}

	public double[] getPosition(){
		double[] coordinate = {this.x, this.y};
		return coordinate;
	}

	public double getSpeed(){
		return this.speed;
	}

	public String getRoadID(){
		return this.roadID;
	}

	public double getAngle(){
		return this.angle;
	}

	public String getType(){
		return this.type;
	}
}
