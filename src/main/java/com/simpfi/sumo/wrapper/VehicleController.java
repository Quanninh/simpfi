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

	/** The connection to SUMO. */
	private SumoTraciConnection connection;

	/**
	 * The vehicle counter. Increments after each vehicle is added to the
	 * simulation.
	 */
	public static int vehicleCounter = 0;

	/** The vehicle map, mapping each vehicle ID to a Vehicle object. */
	private static Map<String, Vehicle> vehicleMap = new HashMap<>();

	/**
	 * Instantiates a new vehicle controller.
	 *
	 * @param conn the connection to SUMO
	 */
	public VehicleController(SumoConnectionManager conn) {
		this.connection = conn.getConnection();
	}

	/**
	 * Returns the all vehicle IDs.
	 *
	 * @return all vehicle IDs
	 * @throws Exception if the TraCI connection fails
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllVehicleIds() throws Exception {
		return (List<String>) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getIDList());
	}

	/**
	 * Returns the speed.
	 *
	 * @param vId the vehicle ID
	 * @return the speed of the vehicle
	 * @throws Exception if the TraCI connection fails
	 */
	public double getSpeed(String vId) throws Exception {
		return (double) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getSpeed(vId));
	}

	/**
	 * Returns the road ID.
	 *
	 * @param vId the vehicle ID
	 * @return the road ID
	 * @throws Exception if the TraCI connection fails
	 */
	public String getRoadID(String vId) throws Exception {
		return (String) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getRoadID(vId));
	}

	/**
	 * Returns the position of the vehicle.
	 *
	 * @param vId the vehicle ID
	 * @return the position
	 * @throws Exception if the TraCI connection fails
	 */
	public Point getPosition(String vId) throws Exception {
		SumoPosition2D pos = (SumoPosition2D) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getPosition(vId));
		return new Point(pos.x, pos.y);
	}

	/**
	 * Returns the angle of the vehicle.
	 *
	 * @param vId the vehicle ID
	 * @return the angle
	 * @throws Exception if the TraCI connection fails
	 */
	public double getAngle(String vId) throws Exception {
		return (double) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getAngle(vId));
	}

	/**
	 * Returns the type ID of the vehicle.
	 *
	 * @param vId the vehicle ID
	 * @return the type ID
	 * @throws Exception if the TraCI connection fails
	 */
	public String getTypeID(String vId) throws Exception {
		return (String) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getTypeID(vId));
	}

	/**
	 * Returns the width of the vehicle.
	 *
	 * @param vId the vehicle ID
	 * @return the width
	 * @throws Exception if the TraCI connection fails
	 */
	public double getWidth(String vId) throws Exception {
		return (double) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getWidth(vId));
	}

	/**
	 * Gets the height of the vehicle.
	 *
	 * @param vId the vehicle ID
	 * @return the height
	 * @throws Exception if the TraCI connection fails
	 */
	public double getHeight(String vId) throws Exception {
		return (double) connection.do_job_get(de.tudresden.sumo.cmd.Vehicle.getHeight(vId));
	}

	/**
	 * Returns the vehicles list.
	 *
	 * @return the vehicles list
	 */
	public static List<Vehicle> getVehicles() {
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			vehicleList.add(entry.getValue());
		}
		return vehicleList;
	}

	/**
	 * Disable all vehicles in code, not in simulation.
	 */
	public static void disableAllVehicles() {
		for (Map.Entry<String, Vehicle> entry : vehicleMap.entrySet()) {
			entry.getValue().setIsActive(false);
		}
	}

	/**
	 * Updates the vehicle map.
	 *
	 * @param vehicleToUpdate the new vehicle to be updated
	 */
	public static void updateVehicleMap(Vehicle vehicleToUpdate) {
		vehicleToUpdate.setIsActive(true);
		vehicleMap.put(vehicleToUpdate.getID(), vehicleToUpdate);
	}

	/**
	 * Generate new unique vehicle ID.
	 *
	 * @return the unique vehicle ID
	 */
	public static String generateVehicleID() {
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
	 * @throws Exception if the TraCI connection fails
	 */
	public void addVehicle(String vehicleID, String routeID, String vType) throws Exception {
		System.out.println("Add vehicle " + vehicleID + " on route " + routeID + " of type " + vType);

		connection.do_job_set(de.tudresden.sumo.cmd.Vehicle.addFull(vehicleID, routeID, vType, "now", "random", "last",
			"max", "current", "random", "current", "", "", "", 0, 0));
	}

}
