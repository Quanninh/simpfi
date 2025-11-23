package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;

import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.config.Settings;
import com.simpfi.config.Constants;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.ControlPanel;
import com.simpfi.ui.Frame;
import com.simpfi.ui.MapPanel;
import com.simpfi.ui.Panel;
import com.simpfi.util.reader.RouteXMLReader;

import java.util.List;
/**
 * App Class contains the {@code main} function and is used to run the software.
 * Flow of actions:
 * 1. Initialize the User Interface.
 * 2. Establish the TraCI connection.
 * 3. Read XML files and parse essential components of the traffic.
 * 4. Start the simulation loop.
 */
public class App {
	
	public static void main(String[] args) throws Exception {
		SumoConnectionManager sim = null;
		try {
		    MapPanel mapPanel = generateUI();
		    sim = establishConnection();

			MapPanel mp = new MapPanel();

		    RouteXMLReader routeXmlReader = new RouteXMLReader(Constants.SUMO_ROUTE);
		    System.out.println(routeXmlReader.parseRoute().toString());
		    System.out.println(routeXmlReader.parseVehicleType().toString());

			Settings st = new Settings();
			List<String> vehicle_ids = mp.generate_vID();
			List<VehicleType> vType = st.getVehicleType();
			List<Route> route = st.getRoutes();

			VehicleController vehicle_control = new VehicleController(sim);

			for (int i = 0 ; i < vehicle_ids.size(); i++){
				String v = vehicle_ids.get(i);
				String vt = vType.get(i).getId();
				String r = route.get(i).getId();

				vehicle_control.addVehicle(v, vt, r);
			}

		    while (true) {
			    do_step(sim);
			    retrieveData(sim);
			    mapPanel.repaint();
		    }
	    }
		catch (Exception e){
			e.printStackTrace();
		}finally {
			if (sim != null){
				sim.close();
			}
		}
	}
	private static void do_step(SumoConnectionManager sim) throws Exception {
		sim.doStep();
	}

	private static SumoConnectionManager establishConnection()
		throws Exception {
		SumoConnectionManager sim = new SumoConnectionManager(Constants.SUMO_CONFIG);
		return sim;
	}

	private static void retrieveData(SumoConnectionManager sim) throws Exception {
		double stepLen = 0.1;
		long stepMs = (long) (stepLen * 1000);
		VehicleController vehicleController = new VehicleController(sim);
		TrafficLightController trafficLightController = new TrafficLightController(sim);

		
		long next = System.currentTimeMillis();

		// double time = ((Double) sim.getConnection()
		// .do_job_get(Simulation.getCurrentTime())) / 1000.0;

		for (String vid : vehicleController.getAllVehicleIDs()) {
			double speed = vehicleController.getSpeed(vid);
			String edge = vehicleController.getRoadID(vid);
			// System.out.printf("t=%.1fs id=%s v=%.2f m/s edge=%s%n",
			// time, vid, speed, edge);
			System.out.printf("id=%s v=%.2f m/s edge=%s%n", vid, speed,edge);
		}

		for (String tl : trafficLightController.getIDList()){
			String light_state = trafficLightController.getState(tl);
			System.out.printf("light state=%s", light_state);
		}

		next += stepMs;
		// long sleep = next - System.currentTimeMillis();
		// if (sleep > 0) Thread.sleep(sleep);
		Thread.sleep(100);
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
