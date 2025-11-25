package com.simpfi.ui.panel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;

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
	
	private SumoConnectionManager conn;

	public InjectPanel(SumoConnectionManager conn) {
		this.conn = conn;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		List<VehicleType> allVehicles = Settings.getVehicleTypes();

		String[] vehicleTypes = new String[allVehicles.size()];
		for (int i = 0; i < allVehicles.size(); i++) {
			vehicleTypes[i] = allVehicles.get(i).getId();
		}

		List<Route> allRoutes = Settings.getRoutes();

		String[] routeIds = new String[allRoutes.size()];
		for (int i = 0; i < allRoutes.size(); i++) {
			routeIds[i] = allRoutes.get(i).getId();
		}

		this.add(new Label("Vehicle Type:"));

		Dropdown<String> vehicleTypeCB = new Dropdown<String>(vehicleTypes);
		this.add(vehicleTypeCB);

		this.add(new Label("Route:"));

		Dropdown<String> routeCB = new Dropdown<String>(routeIds);
		this.add(routeCB);

		Button addingVehice = new Button("Adding vehicle");

		VehicleController vehicleControl = new VehicleController(this.conn);

		addingVehice.addActionListener(e -> {
			try {
				String userChoiceVehicleType = vehicleTypeCB.getSelectedItem().toString();
				String userChoiceRoute = routeCB.getSelectedItem().toString();
				String vehicleIds = Settings.generateVehicleIDs();
				vehicleControl.addVehicle(vehicleIds, userChoiceRoute,
					userChoiceVehicleType);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		this.add(addingVehice);
	}
	
}
