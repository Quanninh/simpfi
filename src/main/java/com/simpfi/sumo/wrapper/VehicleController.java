package com.simpfi.sumo.wrapper;

import java.util.List;

import com.simpfi.util.Point;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Vehicle}.
 */
public class VehicleController {

	private SumoTraciConnection conn;

	public VehicleController(SumoConnectionManager conn) {
		this.conn = conn.getConnection();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllVehicleIDs() throws Exception {
		return (List<String>) conn.do_job_get(Vehicle.getIDList());
	}

	public double getSpeed(String vId) throws Exception {
		return (double) conn.do_job_get(Vehicle.getSpeed(vId));
	}

	public String getRoadID(String vId) throws Exception {
		return (String) conn.do_job_get(Vehicle.getRoadID(vId));
	}

	public Point getPosition(String vId) throws Exception {
		SumoPosition2D pos = (SumoPosition2D) conn
			.do_job_get(Vehicle.getPosition(vId));
		return new Point(pos.x, pos.y);
	}

	public double getAngle(String vId) throws Exception {
		return (double) conn.do_job_get(Vehicle.getAngle(vId));
	}

	public String getTypeID(String vId) throws Exception {
		return (String) conn.do_job_get(Vehicle.getTypeID(vId));
	}

	public double getWidth(String vId) throws Exception {
		return (double) conn.do_job_get(Vehicle.getWidth(vId));
	}

	public double getHeight(String vId) throws Exception {
		return (double) conn.do_job_get(Vehicle.getHeight(vId));
	}

	/**
	 * Used to add a new vehicle to the list.
	 * 
	 * @param vehicleID id of the new vehicle, must be unique.
	 * @param routeID   id of the route that the new vehicle is on.
	 * @param vType     the type of the new vehicle.
	 */
	public void addVehicle(String vehicleID, String routeID, String vType)
		throws Exception {
		// Add vehicle to network
		System.out.println("Add vehicle " + vehicleID + " on route " + routeID
			+ " of type " + vType);
		conn.do_job_set(
			Vehicle.addFull(vehicleID, routeID, vType, "now", "random", "last",
				"max", "current", "random", "current", "", "", "", 0, 0));
	}

	/*
	 * add vehicle tl: set state
	 * 
	 */

}
