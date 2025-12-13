package com.simpfi.sumo.wrapper;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.simpfi.config.Settings;
import com.simpfi.object.Route;

public class VehicleInjectionController {
	private static final Logger logger = Logger.getLogger(VehicleInjectionController.class.getName());

	private final VehicleController vehicleController;
	private final Random rand = new Random();

	public VehicleInjectionController(VehicleController vc) {
		this.vehicleController = vc;
	}

	/**
	 * Inject N vehicles on random routes
	 */
	public void injectRandom(int count, String vType) {
		List<Route> routes = Settings.network.getRoutes();
		for (int i = 0; i < count; i++) {
			Route r = routes.get(rand.nextInt(routes.size()));
			addSingle(VehicleController.generateVehicleID(), r.getId(), vType);
		}
	}

	/**
	 * Inject N vehicles on a specific route
	 */
	public void injectOnRoute(String routeId, int count, String vType) {
		for (int i = 0; i < count; i++) {
			addSingle(VehicleController.generateVehicleID(), routeId, vType);
		}
	}

	/**
	 * Inject a single vehicle on a specific route
	 */
	public void addSingle(String vid, String routeId, String vType) {
		try {
			vehicleController.addVehicle(vid, routeId, vType);
		} catch (Exception e) {
			logger.log(Level.SEVERE, String.format("Failed to inject vehicle=%s route=%s", vid, routeId), e);
		}
	}
}