package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;

public class SumoConnectionManager {

	private SumoTraciConnection conn;

	public SumoConnectionManager(String cfg) throws Exception {
		// Launch SUMO externally using default PATH
		ProcessBuilder pb = new ProcessBuilder("sumo", "-c", cfg,
			"--remote-port", "9999", "--step-length", "0.1");
		pb.inheritIO();
		pb.start();

		// Wait for SUMO to open TraCI port
		Thread.sleep(6000);

		// Connect TraCI client to running SUMO
		conn = new SumoTraciConnection(9999);

		conn.runServer();
		conn.setOrder(1);

		System.out.println("SUMO launched and TraCI connected.");
	}

	public void doStep() throws Exception {
		conn.do_timestep();
	}

	public SumoTraciConnection getConnection() {
		return conn;
	}

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
