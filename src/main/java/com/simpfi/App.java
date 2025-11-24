package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.config.Settings;
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

	public static void main(String[] args) throws Exception {
		long stepMs = (long) (Settings.TIMESTEP * 1000);

		SumoConnectionManager sim = null;
		try {
			sim = establishConnection();
			generateUI(sim);

			RouteXMLReader routeXmlReader = new RouteXMLReader(
				Constants.SUMO_ROUTE);
			System.out.println(routeXmlReader.parseRoute().toString());
			System.out.println(routeXmlReader.parseVehicleType().toString());

			long next = System.currentTimeMillis();
			while (true) {

				do_step(sim);
				retrieveData(sim);
				mapPanel.repaint();

				next += stepMs;
				long sleep = next - System.currentTimeMillis();
				if (sleep > 0)
					Thread.sleep((long) (sleep / Settings.SIMULATION_SPEED));
				//Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sim != null) {
				sim.close();
			}
		}
	}

	private static void do_step(SumoConnectionManager sim) throws Exception {
		sim.doStep();
	}

	private static SumoConnectionManager establishConnection()
		throws Exception {
		SumoConnectionManager sim = new SumoConnectionManager(
			Constants.SUMO_CONFIG);
		return sim;
	}

	private static void retrieveData(SumoConnectionManager sim)
		throws Exception {
		VehicleController vehicleController = new VehicleController(sim);
		TrafficLightController trafficLightController = new TrafficLightController(
			sim);
		List<Vehicle> updatedVehicles = new ArrayList<Vehicle>();

		for (String vid : vehicleController.getAllVehicleIDs()) {
			Point pos = vehicleController.getPosition(vid);
			// double speed = vehicleController.getSpeed(vid);
			String edge = vehicleController.getRoadID(vid);
			// double angle = vehicleController.getAngle(vid);
			String type = vehicleController.getTypeID(vid);
			
			Vehicle v = new Vehicle(vid, pos, edge, type);

			updatedVehicles.add(v);
			mapPanel.updateVehicles(updatedVehicles);
		}

		for (String tl : trafficLightController.getIDList()) {
			String light_state = trafficLightController.getState(tl);
			mapPanel.updateTrafficLightState(tl, light_state); // It should be
																// TrafficLightController
			//System.out.printf("light state=%s", light_state);
		}
	}

	private static void generateUI(SumoConnectionManager conn) {
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
