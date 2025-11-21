package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Vehicle;

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
		return (Double) conn.do_job_get(Vehicle.getSpeed(vId));
	}

	public String getRoadID(String vId) throws Exception {
		return (String) conn.do_job_get(Vehicle.getRoadID(vId));
	}

	public double[] getPosition(String vId) throws Exception {
		return (double[]) conn.do_job_get(Vehicle.getPosition(vId));
	}
	
	public void addVehicle(String vType, String routeID) {
		// Add vehicle to network
		conn.do_job_get(Vehicle.add(vType, routeID));
	}
	
	/*
	 * add vehicle
	 * tl: set state
	 * 
	 */

}
