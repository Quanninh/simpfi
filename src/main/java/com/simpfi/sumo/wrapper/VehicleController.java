package com.simpfi.sumo.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.object.Vehicle;
import com.simpfi.util.Point;

import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Vehicle}.
 */
public class VehicleController {

	private SumoTraciConnection conn;
	public static int vehicleCounter = 0;
	private static Map<String, Vehicle> vehicleMap = new HashMap<>();

	public VehicleController(SumoConnectionManager conn) {
		this.conn = conn.getConnection();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllVehicleIDs() throws Exception {
		return (List<String>) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getIDList());
	}

	public double getSpeed(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getSpeed(vId));
	}

	public String getRoadID(String vId) throws Exception {
		return (String) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getRoadID(vId));
	}

	public Point getPosition(String vId) throws Exception {
		SumoPosition2D pos = (SumoPosition2D) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(vId));
		return new Point(pos.x, pos.y);
	}

	public double getAngle(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getAngle(vId));
	}

	public String getTypeID(String vId) throws Exception {
		return (String) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getTypeID(vId));
	}

	public double getWidth(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getWidth(vId));
	}

	public double getHeight(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getHeight(vId));
	}

	public static List<Vehicle> getVehicles() {
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			vehicleList.add(entry.getValue());
		}
		return vehicleList;
	}

	public static void disableAllVehicles() {
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			entry.getValue().setIsActive(false);
		}
	}

	public static void setVehicles(Vehicle vehicleToUpdate) {
		vehicleToUpdate.setIsActive(true);
		vehicleMap.put(vehicleToUpdate.getID(), vehicleToUpdate);
	}

	public static String generateVehicleIDs() {
		String ids = "v_" + vehicleCounter;
		vehicleCounter++;
		return ids;
	}

	/**
	 * Used to add a new vehicle to the list.
	 * 
	 * @param vehicleID id of the new vehicle, must be unique.
	 * @param routeID   id of the route that the new vehicle is on.
	 * @param vType     the type of the new vehicle.
	 * @throws Exception if adding of vehicle fails
	 */
	public void addVehicle(String vehicleID, String routeID, String vType) throws Exception {
		System.out.println("Add vehicle " + vehicleID + " on route " + routeID + " of type " + vType);
		conn.do_job_set(de.tudresden.sumo.cmd.Vehicle.addFull(vehicleID, routeID, vType, "now", "random", "last", "max",
			"current", "random", "current", "", "", "", 0, 0));
	}

}
