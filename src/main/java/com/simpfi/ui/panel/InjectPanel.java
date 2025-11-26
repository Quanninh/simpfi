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

public class InjectPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final VehicleController vehicleControl;

	public InjectPanel(SumoConnectionManager conn) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] vehicleTypes = getAllVehiclesTypes();
		String[] routeIds = getAllRouteIds();

		Dropdown<String> vehicleTypeDropdown = createDropdownWithLabel("Vehicle Type:", vehicleTypes);
		Dropdown<String> routeDropdown = createDropdownWithLabel("Route:", routeIds);

		vehicleControl = new VehicleController(conn);

		Button addVehicleBtn = new Button("Adding vehicle");
		addVehicleBtn.addActionListener(e -> addVehicle(vehicleTypeDropdown, routeDropdown));

		addVehicleBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(addVehicleBtn);
	}

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

	private Dropdown<String> createDropdownWithLabel(String label, String[] items) {
		this.add(new Label(label));

		Dropdown<String> dropdown = new Dropdown<String>(items);
		dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(dropdown);

		return dropdown;
	}

	private String[] getAllVehiclesTypes() {
		List<VehicleType> allVehicles = Settings.network.getVehicleTypes();
		String[] vehicleTypes = new String[allVehicles.size()];

		for (int i = 0; i < allVehicles.size(); i++) {
			vehicleTypes[i] = allVehicles.get(i).getId();
		}

		return vehicleTypes;
	}

	private String[] getAllRouteIds() {
		List<Route> allRoutes = Settings.network.getRoutes();
		String[] routeIds = new String[allRoutes.size()];

		for (int i = 0; i < allRoutes.size(); i++) {
			routeIds[i] = allRoutes.get(i).getId();
		}

		return routeIds;
	}

}
