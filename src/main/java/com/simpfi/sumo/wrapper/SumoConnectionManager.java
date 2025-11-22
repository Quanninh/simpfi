package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
/**
 * Creates SumoConnectionManager class used to establish the connection between Java programs and SUMO via TraCI.
 */
public class SumoConnectionManager {

	private SumoTraciConnection conn;
	/**
	 * Constructor used to initialize the Sumo Connection Manager.
	 * @param cfg path to SUMO configuration file(.sumocfg).
	 * @throws Exception if the TraCI connection fails.
	 */
	public SumoConnectionManager(String cfg) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("sumo", "-c", cfg, "--start",
			"--quit-on-end", "--remote-port", "9999", "--step-length", "0.1");
		pb.inheritIO();
		pb.start();

		Thread.sleep(6000);

		conn = new SumoTraciConnection(9999);

		conn.runServer();
		conn.setOrder(1);

		System.out.println("SUMO launched and TraCI connected.");
	}
	/**
	 * Updates states of objects in SUMO so that movements such as car running can be observed.
	 * @throws Exception if the TraCI connection fails.
	 */
	public void doStep() throws Exception {
		conn.do_timestep();
	}
	public SumoTraciConnection getConnection() {
		return conn;
	}
	/**
	 * Close the TraCI connection.
	 */
	public void close() {
		try {
			if (conn != null)
				conn.close();
			System.out.println("TraCI connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
