package com.simpfi;

import java.awt.BorderLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.TrafficStatistics;
import com.simpfi.object.Vehicle;
import com.simpfi.sumo.wrapper.EdgeController;
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
	private static final Logger logger = Logger.getLogger(App.class.getName());

	/** The map panel. */
	private static MapPanel mapPanel;

	/** The vehicle controller. */
	private static VehicleController vehicleController;

	/** The traffic light controller. */
	private static TrafficLightController trafficLightController;

	/** The edge controller */
	private static EdgeController edgeController;

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

	/** The step. */
	private static volatile int step = 0;
	

	/** * ID of the currently selected traffic light, shared across threads. */
	private static volatile String tlId;

	/** 
	 * Index of the currently active traffic light phase, shared across threads. 
	 */
	private static volatile int currentPhase;

	/** 
	 * Remaining time (in seconds) for the current traffic light phase, shared across threads. 
	 */
	private static volatile Double remaining;


	/**
	 * Main function and starting point of application. Sets up TraCI connection,
	 * initializes UI and controllers and runs the simulation loop.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		logger.log(Level.INFO, "Application started");

		SwingUtilities.invokeLater(() -> {
			try {
				SumoConnectionManager connection = establishConnection();

				vehicleController = new VehicleController(connection);
				trafficLightController = new TrafficLightController(connection);
				edgeController = new EdgeController(connection);

				TrafficStatistics trafficStatistic = new TrafficStatistics(edgeController, vehicleController);

				generateUI(connection, trafficStatistic);

				// statisticsPanel = new StatisticsPanel(trafficStatistic);
				startDataThread(trafficStatistic);
				startSimulationThread(connection, trafficStatistic);

			} catch (Exception e) {
				logger.log(Level.SEVERE, "Failed to continue the app", e);
			}
		});
	}

	/** Simulation Thread */
	private static void startSimulationThread(SumoConnectionManager conn, TrafficStatistics stats) {
		new Thread(() -> {
			long stepMs = (long) (Settings.config.TIMESTEP * 1000);

			while (true) {
				long next = System.currentTimeMillis() + stepMs;

				try {
					long start = System.nanoTime();

					doStep(conn);
					retrieveData(conn);
					stats.update(step);
					

					if (programLightPanel.isAdaptiveMode) {
						trafficLightController.updateTrafficLightByNumberOfVehicle(
							Settings.network.getTrafficLights()
						);
					} else {
						trafficLightController.setDefaultTrafficLight(
							Settings.network.getTrafficLights()
						);
					}

					// ===== Data for UI =====
					tlId = programLightPanel.getSelectedTrafficLightID();
					currentPhase = trafficLightController.getPhase(tlId);
					remaining = programLightPanel.showRemainingDuration(tlId, step * Settings.config.TIMESTEP);

					step++;

					long sleep = next - System.currentTimeMillis();
					if (sleep > 0) Thread.sleep(sleep);

				} catch (Exception e) {
					logger.log(Level.SEVERE, "Simulation thread failed", e);
				}
			}
		}, "SimulationThread").start();
	}

	/**
	 * Data Thread
	 */
	private static void startDataThread(TrafficStatistics stats){
		new Thread (() ->{
			int lastStep = -1;

			while(true){
				try{
					int currentStep = step;

					if (currentStep == lastStep){
						Thread.sleep(5);
						continue;
					}

					lastStep = currentStep;

					SwingUtilities.invokeLater(() -> {
						long uiStart = System.nanoTime();

						statisticsPanel.updatePanel(currentStep);

						programLightPanel.updateRemainingTime(tlId, currentPhase, remaining);

						if (currentStep % 10 == 0){
							mapPanel.updateVehicleStates(currentStep);
							programLightPanel.showImpactOfTimingChange();
						}

						mapPanel.repaint();
						//mapPanel.paintImmediately(0, 0, mapPanel.getWidth(), mapPanel.getHeight());
						long uiEnd = System.nanoTime();
						logger.log(Level.FINE, "UI frame time (ms): {0}", (uiEnd - uiStart) / 1_000_000.0);
					});

					Thread.sleep(33);
				}catch (Exception e){
					logger.log(Level.SEVERE, "Data/UI Thread failed", e);
				}
			}
		}, "DataThread").start();
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
		// VehicleController.disableAllVehicles();

		for (String vid : vehicleController.getAllVehicleIds()) {
			Point pos = vehicleController.getPosition(vid);
			String edge = vehicleController.getRoadID(vid);
			double angle = vehicleController.getAngle(vid);
			String type = vehicleController.getTypeID(vid);
			double width = vehicleController.getWidth(vid);
			double height = vehicleController.getHeight(vid);
			double speed = vehicleController.getSpeed(vid);
			double maxSpeed = vehicleController.getMaxSpeed(vid);
			double acceleration = vehicleController.getAcceleration(vid);
			double distance = vehicleController.getDistance(vid);
			List<String> route = vehicleController.getRoute(vid);

			Vehicle v = new Vehicle(vid, pos, edge, type, angle, width, height, speed, maxSpeed, acceleration, distance,
				route);

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
	 * @throws Exception
	 */
	private static void generateUI(SumoConnectionManager conn, TrafficStatistics stats) throws Exception {
		uiSetup();
		Frame myFrame = new Frame();

		mapPanel = new MapPanel();

		statisticsPanel = new StatisticsPanel(stats);
		injectPanel = new InjectPanel(conn);
		mapViewPanel = new MapViewPanel();
		programLightPanel = new ProgramLightsPanel(conn);
		programLightPanel.setStatisticsPanel(statisticsPanel);
		programLightPanel.setStats(stats);
		filterPanel = new FilterPanel();
		inspectPanel = new InspectPanel(conn, mapPanel);

		sidePane = new TabbedPane();
		mapPanel.setIgnoreRepaint(true);

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
