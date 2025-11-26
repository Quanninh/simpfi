package com.simpfi;

import java.awt.BorderLayout;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Vehicle;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Frame;
import com.simpfi.ui.TabbedPane;
import com.simpfi.ui.panel.FilterPanel;
import com.simpfi.ui.panel.InjectPanel;
import com.simpfi.ui.panel.InspectPanel;
import com.simpfi.ui.panel.MapPanel;
import com.simpfi.ui.panel.MapViewPanel;
import com.simpfi.ui.panel.ProgramLightsPanel;
import com.simpfi.ui.panel.StatisticsPanel;
import com.simpfi.util.Point;

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

	static StatisticsPanel statisticsPanel;
	static InjectPanel injectPanel;
	static MapViewPanel mapViewPanel;
	static ProgramLightsPanel programLightPanel;
	static FilterPanel filterPanel;
	static InspectPanel inspectPanel;

	static TabbedPane sidePane;

	public static void main(String[] args) {
		long stepMs = (long) (Settings.config.TIMESTEP * 1000);

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
				injectPanel.sthidk();
				mapPanel.repaint();

				long sleep = next - System.currentTimeMillis();
				if (sleep > 0) {
					Thread.sleep((long) (sleep / Settings.config.SIMULATION_SPEED));
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

	private static SumoConnectionManager establishConnection() throws Exception {
		SumoConnectionManager conn = new SumoConnectionManager(Constants.SUMO_CONFIG);
		return conn;
	}

	private static void retrieveData(SumoConnectionManager sim) throws Exception {
		VehicleController.disableAllVehicles();

		for (String vid : vehicleController.getAllVehicleIDs()) {
			Point pos = vehicleController.getPosition(vid);
			// double speed = vehicleController.getSpeed(vid);
			String edge = vehicleController.getRoadID(vid);
			double angle = vehicleController.getAngle(vid);
			String type = vehicleController.getTypeID(vid);
			double width = vehicleController.getWidth(vid);
			double height = vehicleController.getHeight(vid);

			Vehicle v = new Vehicle(vid, pos, edge, type, angle, width, height);

			VehicleController.setVehicles(v);
		}

		for (String tl : trafficLightController.getIDList()) {
			String light_state = trafficLightController.getState(tl);
			TrafficLightController.updateTrafficLightState(tl, light_state);
		}
	}

	private static void generateUI(SumoConnectionManager conn) {
		uiSetup();
		Frame myFrame = new Frame();

		mapPanel = new MapPanel();

		statisticsPanel = new StatisticsPanel();
		injectPanel = new InjectPanel(conn);
		mapViewPanel = new MapViewPanel();
		programLightPanel = new ProgramLightsPanel();
		filterPanel = new FilterPanel();
		inspectPanel = new InspectPanel();

		sidePane = new TabbedPane();

		sidePane.addTab("Statistics", statisticsPanel);
		sidePane.addTab("Inject", injectPanel);
		sidePane.addTab("Map View", mapViewPanel);
		sidePane.addTab("Program Lights", programLightPanel);
		sidePane.addTab("Filter", filterPanel);
		sidePane.addTab("Inspect", inspectPanel);

		myFrame.add(mapPanel, BorderLayout.CENTER);
		myFrame.add(sidePane, BorderLayout.WEST);

		// ALWAYS PUT THIS AT THE END
		myFrame.setVisible(true);
	}

	private static void uiSetup() {
		FlatLightLaf.registerCustomDefaultsSource("themes");
		FlatLightLaf.setup();

		UIManager.put("Button.arc", Constants.ROUNDED_CORNERS);
		UIManager.put("Component.arc", Constants.ROUNDED_CORNERS);
		UIManager.put("ProgressBar.arc", Constants.ROUNDED_CORNERS);
		UIManager.put("TextComponent.arc", Constants.ROUNDED_CORNERS);
		UIManager.put("Component.arrowType", "chevron");
		UIManager.put("Component.focusWidth", 3);
		UIManager.put("TabbedPane.showTabSeparators", true);
	}
}
