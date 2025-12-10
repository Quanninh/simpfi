package com.simpfi.ui.panel;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Panel;

/**
 * A panel for injecting vehicles. This class extends {@link Panel}.
 */
public class InjectPanel extends Panel {

	/** Logger. */
    private static final Logger logger = Logger.getLogger(InjectPanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vehicle controller. */
	private final VehicleController vehicleController;

	/** The vehicle type dropdown for user to select vehicle types to add. */
	private Dropdown<String> vehicleTypeDropdown;
	/** The route dropdown for user to select routes to add vehicle to. */
	private Dropdown<String> routeDropdown;

	/**
	 * Instantiates a new inject panel.
	 *
	 * @param conn the conn
	 */
	public InjectPanel(SumoConnectionManager conn) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] vehicleTypes = getAllVehiclesTypesAsStrings();
		String[] routeIds = getAllRouteIdsAsStrings();

		vehicleTypeDropdown = Dropdown.createDropdownWithLabel("Vehicle Type:", vehicleTypes, this);
		routeDropdown = Dropdown.createDropdownWithLabel("Route:", routeIds, this);

		vehicleController = new VehicleController(conn);

		Button addVehicleBtn = new Button("Adding vehicle");
		addVehicleBtn.addActionListener(e -> addVehicle());

		this.add(addVehicleBtn);
	}

	/**
	 * Set the highlighted route variable in {@link Settings} to the currently
	 * chosen route in the dropdown.
	 */
	public void setHighlightedRoute() {
		Settings.highlight.HIGHLIGHTED_ROUTE = Route.searchForRoute((String) routeDropdown.getSelectedItem(),
			Settings.network.getRoutes());
	}

	/**
	 * Adds a vehicle to the route.
	 */
	private void addVehicle() {
		try {
			String userChoiceVehicleType = vehicleTypeDropdown.getSelectedItem().toString();
			String userChoiceRoute = routeDropdown.getSelectedItem().toString();
			String vehicleIds = VehicleController.generateVehicleID();
			vehicleController.addVehicle(vehicleIds, userChoiceRoute, userChoiceVehicleType);
		} catch (Exception e1) {
			logger.log(Level.SEVERE,String.format("Failed to add vehicle (type=%s, route=%s)", 
        userChoiceVehicleType, userChoiceRoute),e1);
		}
	}

	/**
	 * Returns all vehicles types as strings.
	 *
	 * @return the all vehicles types
	 */
	private String[] getAllVehiclesTypesAsStrings() {
		List<VehicleType> allVehicles = Settings.network.getVehicleTypes();
		String[] vehicleTypes = new String[allVehicles.size()];

		for (int i = 0; i < allVehicles.size(); i++) {
			vehicleTypes[i] = allVehicles.get(i).getId();
		}

		return vehicleTypes;
	}

	/**
	 * Returns the all route ids as strings.
	 *
	 * @return the all route ids
	 */
	private String[] getAllRouteIdsAsStrings() {
		List<Route> allRoutes = Settings.network.getRoutes();
		String[] routeIds = new String[allRoutes.size()];

		for (int i = 0; i < allRoutes.size(); i++) {
			routeIds[i] = allRoutes.get(i).getId();
		}

		return routeIds;
	}

}
