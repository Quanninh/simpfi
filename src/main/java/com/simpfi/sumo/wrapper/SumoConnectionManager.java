package com.simpfi.sumo.wrapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.simpfi.config.Settings;

import it.polito.appeal.traci.SumoTraciConnection;

/**
 * Creates SumoConnectionManager class used to establish the connection between
 * Java programs and SUMO via TraCI.
 */
public class SumoConnectionManager {

	/** Logger. */
	private static final Logger logger = Logger.getLogger(SumoConnectionManager.class.getName());

	/** The connection to SUMO. */
	private SumoTraciConnection connection;

	/** The port to connect to SUMO. */
	private int port = 9999;
	/** The time (in ms) to wait for SUMO to start */
	private int waitForSumoMs = 5000;

	/**
	 * Initializes the Sumo Connection Manager.
	 * 
	 * @param cfg the path to SUMO configuration file (.sumocfg)
	 * @throws Exception if the TraCI connection fails
	 */
	public SumoConnectionManager(String cfg) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("sumo", "-c", cfg, "--start", "--quit-on-end", "--remote-port",
			String.valueOf(port), "--step-length", String.valueOf(Settings.config.TIMESTEP), "--extrapolate-departpos");
		pb.inheritIO();
		pb.start();

		Thread.sleep(waitForSumoMs);

		connection = new SumoTraciConnection(port);

		connection.runServer();
		connection.setOrder(1);

		logger.log(Level.INFO,"SUMO launched and TraCI connected.");
	}

	/**
	 * Moves forward one step in the simulation.
	 * 
	 * @throws Exception if the TraCI connection fails.
	 */
	public void doStep() throws Exception {
		connection.do_timestep();
	}

	/**
	 * Returns the connection.
	 *
	 * @return the connection
	 */
	public SumoTraciConnection getConnection() {
		return connection;
	}

	/**
	 * Close the TraCI connection.
	 */
	public void close() {
		try {
			if (connection != null)
				connection.close();
			logger.log(Level.INFO,"TraCI connection closed.");
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Closing is not done properly",e);
		}
	}
}
