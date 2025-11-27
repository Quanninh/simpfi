package com.simpfi.ui.panel;

import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;

// TODO: Auto-generated Javadoc
/**
 * The Class InjectPanel.
 */
public class InjectPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vehicle control. */
	private final VehicleController vehicleControl;
	
	/** The route dropdown. */
	private Dropdown<String> routeDropdown;

	/**
	 * Instantiates a new inject panel.
	 *
	 * @param conn the conn
	 */
	public InjectPanel(SumoConnectionManager conn) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] vehicleTypes = getAllVehiclesTypes();
		String[] routeIds = getAllRouteIds();

		Dropdown<String> vehicleTypeDropdown = createDropdownWithLabel("Vehicle Type:", vehicleTypes);
		routeDropdown = createDropdownWithLabel("Route:", routeIds);

		vehicleControl = new VehicleController(conn);

		Button addVehicleBtn = new Button("Adding vehicle");
		addVehicleBtn.addActionListener(e -> addVehicle(vehicleTypeDropdown, routeDropdown));

		addVehicleBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(addVehicleBtn);
	}

	/**
	 * Sthidk.
	 */
	public void sthidk() {
		Settings.config.HIGHLIGHTED_ROUTE = (String) routeDropdown.getSelectedItem();
	}

	/**
	 * Adds the vehicle.
	 *
	 * @param vehicleTypeDropdown the vehicle type dropdown
	 * @param routeDropdown the route dropdown
	 */
	private void addVehicle(Dropdown<String> vehicleTypeDropdown, Dropdown<String> routeDropdown) {
		try {
			String userChoiceVehicleType = vehicleTypeDropdown.getSelectedItem().toString();
			String userChoiceRoute = routeDropdown.getSelectedItem().toString();
			String vehicleIds = VehicleController.generateVehicleIDs();
			vehicleControl.addVehicle(vehicleIds, userChoiceRoute, userChoiceVehicleType);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Creates the dropdown with label.
	 *
	 * @param label the label
	 * @param items the items
	 * @return the dropdown
	 */
	private Dropdown<String> createDropdownWithLabel(String label, String[] items) {
		this.add(new Label(label));

		Dropdown<String> dropdown = new Dropdown<String>(items);
		dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(dropdown);

		return dropdown;
	}

	/**
	 * Gets the all vehicles types.
	 *
	 * @return the all vehicles types
	 */
	private String[] getAllVehiclesTypes() {
		List<VehicleType> allVehicles = Settings.network.getVehicleTypes();
		String[] vehicleTypes = new String[allVehicles.size()];

		for (int i = 0; i < allVehicles.size(); i++) {
			vehicleTypes[i] = allVehicles.get(i).getId();
		}

		return vehicleTypes;
	}

	/**
	 * Gets the all route ids.
	 *
	 * @return the all route ids
	 */
	private String[] getAllRouteIds() {
		List<Route> allRoutes = Settings.network.getRoutes();
		String[] routeIds = new String[allRoutes.size()];

		for (int i = 0; i < allRoutes.size(); i++) {
			routeIds[i] = allRoutes.get(i).getId();
		}

		return routeIds;
	}

}
