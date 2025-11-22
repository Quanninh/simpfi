package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Vehicle;
import com.simpfi.util.Point;

import java.util.List;

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
		double[] points = (double[]) conn.do_job_get(Vehicle.getPosition(vId));
		return new Point(points[0], points[1]);
	}

	public double getAngle(String vId) throws Exception {
		return (double) conn.do_job_get(Vehicle.getAngle(vId));
	}

	public String getTypeID(String vId) throws Exception {
		return (String) conn.do_job_get(Vehicle.getTypeID(vId));
	}

	public void addVehicle(String vehicleID, String routeID, String vType)
		throws Exception {
		// Add vehicle to network
		// double now = conn.do_job_get(SumoTraciConnection.getCurrentTime());
		conn.do_job_get(
			Vehicle.add(vehicleID, vType, routeID, 0, 0.0, 0.0, (byte) 0));
	}

	/*
	 * add vehicle tl: set state
	 * 
	 */

}
