package com.simpfi.config;

/**
 * Loads and stores all network and route information, initializes and updates
 * changes to simulation configuations.
 *
 * @see com.simpfi.config.SimulationConfig
 * @see com.simpfi.config.Network
 */
public class Settings {

	/** Configurations related to the simulation. */
	public static SimulationConfig config = new SimulationConfig();

	/** Network properties. */
	public static Network network = new Network();

	/** Highlighted Stuff. */
	public static Highlighted highlight = new Highlighted();

}
