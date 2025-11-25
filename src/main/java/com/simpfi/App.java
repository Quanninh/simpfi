package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;

import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.config.Settings;
import com.formdev.flatlaf.FlatLightLaf;
import com.simpfi.config.Constants;
import com.simpfi.object.Vehicle;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.ControlPanel;
import com.simpfi.ui.Frame;
import com.simpfi.ui.MapPanel;
import com.simpfi.ui.Panel;
import com.simpfi.util.Point;
import com.simpfi.util.reader.RouteXMLReader;

import de.tudresden.sumo.cmd.Simulation;

import java.util.List;

/**
 * App Class contains the {@code main} function and is used to run the software.
 * 
 * Flow of actions:
 * 
 * 1. Initialize the User Interface.
 * 
 * 2. Establish the TraCI connection.
 * 
 * 3. Read XML files and parse essential components of the traffic.
 * 
 * 4. Start the simulation loop.
 */
public class App {
	private static MapPanel mapPanel;
	private static VehicleController vehicleController;
	private static TrafficLightController trafficLightController;

	public static void main(String[] args) {
		long stepMs = (long) (Settings.TIMESTEP * 1000);

		SumoConnectionManager connection = null;
		try {
			connection = establishConnection();
			generateUI(connection);

			vehicleController = new VehicleController(connection);
			trafficLightController = new TrafficLightController(connection);

			while (true) {
				long next = System.currentTimeMillis() + stepMs;

				doStep(connection);
				retrieveData(connection);
				mapPanel.repaint();

				long sleep = next - System.currentTimeMillis();
				if (sleep > 0) {
					Thread.sleep((long) (sleep / Settings.SIMULATION_SPEED));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private static void doStep(SumoConnectionManager sim) throws Exception {
		sim.doStep();
	}

	private static SumoConnectionManager establishConnection()
		throws Exception {
		SumoConnectionManager conn = new SumoConnectionManager(
			Constants.SUMO_CONFIG);
		return conn;
	}

	private static void retrieveData(SumoConnectionManager sim)
		throws Exception {

//		List<Vehicle> updatedVehicles = new ArrayList<Vehicle>();

		Settings.disableAllVehicles();
		for (String vid : vehicleController.getAllVehicleIDs()) {
			Point pos = vehicleController.getPosition(vid);
			double speed = vehicleController.getSpeed(vid);
			String edge = vehicleController.getRoadID(vid);
			double angle = vehicleController.getAngle(vid);
			String type = vehicleController.getTypeID(vid);
			double width = vehicleController.getWidth(vid);
			double height = vehicleController.getHeight(vid);

			Vehicle v = new Vehicle(vid, pos, edge, type, angle, width, height);

//			updatedVehicles.add(v);
			Settings.setVehicles(v);
		}
		

		for (String tl : trafficLightController.getIDList()) {
			String light_state = trafficLightController.getState(tl);
			Settings.updateTrafficLightState(tl, light_state);
			// It should be TrafficLightController
			// System.out.printf("light state=%s", light_state);
		}
	}

	private static void generateUI(SumoConnectionManager conn) {
		FlatLightLaf.setup();
		Frame myFrame = new Frame();

		ControlPanel controlPanel = new ControlPanel(conn);
		Panel infoPanel = new Panel();
		mapPanel = new MapPanel();

		myFrame.add(controlPanel, BorderLayout.NORTH);
		myFrame.add(infoPanel, BorderLayout.EAST);
		myFrame.add(mapPanel, BorderLayout.CENTER);

		infoPanel.setBackground(Color.GREEN);
		mapPanel.setBackground(Color.WHITE);

		// ALWAYS PUT THIS AT THE END
		myFrame.setVisible(true);
	}
}
