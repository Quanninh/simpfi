package com.simpfi.sumo.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simpfi.object.Vehicle;
import com.simpfi.util.Point;

import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

// TODO: Auto-generated Javadoc
/**
 * Wrapper Class for {@link de.tudresden.sumo.cmd.Vehicle}.
 */
public class VehicleController {

	/** The conn. */
	private SumoTraciConnection conn;
	
	/** The vehicle counter. */
	public static int vehicleCounter = 0;
	
	/** The vehicle map. */
	private static Map<String, Vehicle> vehicleMap = new HashMap<>();

	/**
	 * Instantiates a new vehicle controller.
	 *
	 * @param conn the conn
	 */
	public VehicleController(SumoConnectionManager conn) {
		this.conn = conn.getConnection();
	}

	/**
	 * Gets the all vehicle I ds.
	 *
	 * @return the all vehicle I ds
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllVehicleIDs() throws Exception {
		return (List<String>) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getIDList());
	}

	/**
	 * Gets the speed.
	 *
	 * @param vId the v id
	 * @return the speed
	 * @throws Exception the exception
	 */
	public double getSpeed(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getSpeed(vId));
	}

	/**
	 * Gets the road ID.
	 *
	 * @param vId the v id
	 * @return the road ID
	 * @throws Exception the exception
	 */
	public String getRoadID(String vId) throws Exception {
		return (String) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getRoadID(vId));
	}

	/**
	 * Gets the position.
	 *
	 * @param vId the v id
	 * @return the position
	 * @throws Exception the exception
	 */
	public Point getPosition(String vId) throws Exception {
		SumoPosition2D pos = (SumoPosition2D) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(vId));
		return new Point(pos.x, pos.y);
	}

	/**
	 * Gets the angle.
	 *
	 * @param vId the v id
	 * @return the angle
	 * @throws Exception the exception
	 */
	public double getAngle(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getAngle(vId));
	}

	/**
	 * Gets the type ID.
	 *
	 * @param vId the v id
	 * @return the type ID
	 * @throws Exception the exception
	 */
	public String getTypeID(String vId) throws Exception {
		return (String) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getTypeID(vId));
	}

	/**
	 * Gets the width.
	 *
	 * @param vId the v id
	 * @return the width
	 * @throws Exception the exception
	 */
	public double getWidth(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getWidth(vId));
	}

	/**
	 * Gets the height.
	 *
	 * @param vId the v id
	 * @return the height
	 * @throws Exception the exception
	 */
	public double getHeight(String vId) throws Exception {
		return (double) conn.do_job_get(de.tudresden.sumo.cmd.Vehicle.getHeight(vId));
	}

	/**
	 * Gets the vehicles.
	 *
	 * @return the vehicles
	 */
	public static List<Vehicle> getVehicles() {
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			vehicleList.add(entry.getValue());
		}
		return vehicleList;
	}

	/**
	 * Disable all vehicles.
	 */
	public static void disableAllVehicles() {
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			entry.getValue().setIsActive(false);
		}
	}

	/**
	 * Sets the vehicles.
	 *
	 * @param vehicleToUpdate the new vehicles
	 */
	public static void setVehicles(Vehicle vehicleToUpdate) {
		vehicleToUpdate.setIsActive(true);
		vehicleMap.put(vehicleToUpdate.getID(), vehicleToUpdate);
	}

	/**
	 * Generate vehicle I ds.
	 *
	 * @return the string
	 */
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
