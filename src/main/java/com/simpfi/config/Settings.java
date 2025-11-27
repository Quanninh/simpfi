package com.simpfi.config;

/**
 * Settings Class loads and stores all network and route information required
 * for SUMO and is also used to initialize and update changes to scale and
 * offset settings of the software.
 *
 * @see com.simpfi.config.SimulationConfig
 * @see com.simpfi.config.Network
 */
public class Settings {

	/** Configurations related to the simulation */
	public static SimulationConfig config = new SimulationConfig();
	/** Network properties */
	public static Network network = new Network();

}
