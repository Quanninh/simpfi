package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;

import com.simpfi.config.Constants;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.ControlPanel;
import com.simpfi.ui.Frame;
import com.simpfi.ui.MapPanel;
import com.simpfi.ui.Panel;
import com.simpfi.util.reader.RouteXMLReader;

public class App {
	public static void main(String[] args) throws Exception {
		MapPanel mapPanel = generateUI();
		SumoConnectionManager sim = establishConnection();

		RouteXMLReader routeXmlReader = new RouteXMLReader(
			Constants.SUMO_ROUTE);
		System.out.println(routeXmlReader.parseRoute().toString());
		System.out.println(routeXmlReader.parseVehicleType().toString());

		while (true) {
			// retrieveData(sim);
			mapPanel.repaint();
		}
	}

	private static SumoConnectionManager establishConnection()
		throws Exception {
		SumoConnectionManager sim = new SumoConnectionManager(
			Constants.SUMO_CONFIG);
		return sim;
	}

	private static void retrieveData(SumoConnectionManager sim)
		throws Exception {
		double stepLen = 0.1;
		long stepMs = (long) (stepLen * 1000);
		VehicleController vehicleController = new VehicleController(sim);
		TrafficLightController trafficLightController = new TrafficLightController(
			sim);

		try {
			long next = System.currentTimeMillis();
			for (int k = 0; k < 600; k++) {

				sim.doStep(); // <-- now works

				// double time = ((Double) sim.getConnection()
				// .do_job_get(Simulation.getCurrentTime())) / 1000.0;

				for (String vid : vehicleController.getAllVehicleIDs()) {
					double speed = vehicleController.getSpeed(vid);
					String edge = vehicleController.getRoadID(vid);
					// System.out.printf("t=%.1fs id=%s v=%.2f m/s edge=%s%n",
					// time, vid, speed, edge);
					System.out.printf("id=%s v=%.2f m/s edge=%s%n", vid, speed,
						edge);
				}

				next += stepMs;
				// long sleep = next - System.currentTimeMillis();
				// if (sleep > 0) Thread.sleep(sleep);
				Thread.sleep(100);
			}

		} finally {
			sim.close();
		}
	}

	private static MapPanel generateUI() {
		Frame myFrame = new Frame();

		ControlPanel controlPanel = new ControlPanel();
		Panel infoPanel = new Panel();
		MapPanel mapPanel = new MapPanel();

		myFrame.add(controlPanel, BorderLayout.NORTH);
		myFrame.add(infoPanel, BorderLayout.EAST);
		myFrame.add(mapPanel, BorderLayout.CENTER);

		infoPanel.setBackground(Color.GREEN);
		mapPanel.setBackground(Color.WHITE);

		// ALWAYS PUT THIS AT THE END
		myFrame.setVisible(true);

		return mapPanel;
	}
}
