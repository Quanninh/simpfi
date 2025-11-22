package com.simpfi.object;
import com.simpfi.util.Point;
/**
 * Creates Vehicle class.
 */
public class Vehicle {

	private String id;
	private Point p;
	private double speed;
	private String roadID;
	private String type;
	private double angle;

	public Vehicle(String id, Point point, double speed, String roadID, String type, double angle){
		this.id = id;
		this.p = point;
		this.speed = speed;
		this.roadID = roadID;
		this.type = type;
		this.angle = angle;
	}

	public String getID(){
		return this.id;
	}

	public Point getPosition(){
		return this.p;
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
