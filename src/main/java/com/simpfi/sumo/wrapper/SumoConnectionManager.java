package com.simpfi.sumo.wrapper;

import it.polito.appeal.traci.SumoTraciConnection;

public class SumoConnectionManager {

	private SumoTraciConnection conn;

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
