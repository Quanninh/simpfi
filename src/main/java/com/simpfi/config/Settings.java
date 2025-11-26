package com.simpfi.config;

/**
 *
 * Settings Class loads and stores all network and route information required
 * for SUMO and is also used to initialize and update changes to scale and
 * offset settings of the software.
 *
 * @see com.simpfi.ui.panel.MapPanel
 * @see com.simpfi.ui.panel.ControlPanel
 * @see com.simpfi.ui.TextBox
 */
public class Settings {

	public static SimulationConfig config = new SimulationConfig();
	public static Network network = new Network();

}
