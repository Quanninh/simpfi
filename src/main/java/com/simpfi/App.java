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
 * App Class as main application class contains the {@code main} function and is
 * used to run the software.
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

	/** The map panel. */
	private static MapPanel mapPanel;

	/** The vehicle controller. */
	private static VehicleController vehicleController;

	/** The traffic light controller. */
	private static TrafficLightController trafficLightController;

	/** The statistics panel. */
	static StatisticsPanel statisticsPanel;

	/** The inject panel. */
	static InjectPanel injectPanel;

	/** The map view panel. */
	static MapViewPanel mapViewPanel;

	/** The program light panel. */
	static ProgramLightsPanel programLightPanel;

	/** The filter panel. */
	static FilterPanel filterPanel;

	/** The inspect panel. */
	static InspectPanel inspectPanel;

	/** The side pane. */
	static TabbedPane sidePane;

	/**
	 * Main function and starting point of application. Sets up TraCI connection,
	 * initializes UI and controllers and runs the simulation loop.
	 *
	 * @param args the arguments
	 */
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
				injectPanel.setHighlightedRoute();
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

	/**
	 * Do step.
	 *
	 * @param sim the SumoConnectionManager
	 * @throws Exception if the connection fails
	 */
	private static void doStep(SumoConnectionManager sim) throws Exception {
		sim.doStep();
	}

	/**
	 * Establishes the SUMO connection.
	 *
	 * @return new SumoConnectionManager
	 * @throws Exception if the connection can't be established
	 */
	private static SumoConnectionManager establishConnection() throws Exception {
		SumoConnectionManager conn = new SumoConnectionManager(Constants.SUMO_CONFIG);
		return conn;
	}

	/**
	 * Retrieves updated vehicle and traffic light data and updates the UI and
	 * controllers.
	 *
	 * @param sim the connection manager
	 * @throws Exception if the connection fails
	 */
	private static void retrieveData(SumoConnectionManager sim) throws Exception {
		VehicleController.disableAllVehicles();

		for (String vid : vehicleController.getAllVehicleIds()) {
			Point pos = vehicleController.getPosition(vid);
			// double speed = vehicleController.getSpeed(vid);
			String edge = vehicleController.getRoadID(vid);
			double angle = vehicleController.getAngle(vid);
			String type = vehicleController.getTypeID(vid);
			double width = vehicleController.getWidth(vid);
			double height = vehicleController.getHeight(vid);

			Vehicle v = new Vehicle(vid, pos, edge, type, angle, width, height);

			VehicleController.updateVehicleMap(v);
		}

		for (String tl : trafficLightController.getIDList()) {
			String light_state = trafficLightController.getState(tl);
			TrafficLightController.updateTrafficLightState(tl, light_state);
		}
	}

	/**
	 * Sets up the UI including panels and panes.
	 *
	 * @param conn the connection manager used by the UI
	 */
	private static void generateUI(SumoConnectionManager conn) {
		uiSetup();
		Frame myFrame = new Frame();

		mapPanel = new MapPanel();

		statisticsPanel = new StatisticsPanel();
		injectPanel = new InjectPanel(conn);
		mapViewPanel = new MapViewPanel();
		programLightPanel = new ProgramLightsPanel();
		filterPanel = new FilterPanel(conn);
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

	/**
	 * Sets up UI theme using {@link FlatLightLaf}.
	 */
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
