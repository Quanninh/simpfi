package com.simpfi.ui.panel;

import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.ui.CheckBox;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;

/**
 * A panel for filtering vehicles. This class extends {@link Panel}.
 */
public class FilterPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list of vehicle-type checkboxes */
	private CheckBox[] vehicleTypeOptions;

	/** 
	 * Instantiates a new filter panel.
	 *
	 * @param conn the connection manager (unused but kept for consistency)
	 */
	public FilterPanel(SumoConnectionManager conn) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] vehicleTypes = getAllVehiclesTypesAsStrings();
		vehicleTypeOptions = new CheckBox[vehicleTypes.length];

		// Filter out vehicles by type
		this.add(new Label("Vehicles: "));
		for (int i = 0; i < vehicleTypes.length; i++) {
			CheckBox option = new CheckBox(vehicleTypes[i], true);
			option.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(option);
			vehicleTypeOptions[i] = option;
		}

		// Attach listeners to checkboxes to update filter when toggled
		attachCheckboxListeners();
	}

	/**
	 * Attaches change listeners to all vehicle-type checkboxes.
	 * When a checkbox is toggled, updates the corresponding VehicleType's filterFlag.
	 */
	private void attachCheckboxListeners() {
		for (CheckBox option : vehicleTypeOptions) {
			option.onChange(e -> {
				boolean checked = option.isChecked();
				// Set flag on the VehicleType object
				for (VehicleType vt : Settings.network.getVehicleTypes()) {
					if (vt.getId().equals(option.getText())) {
						vt.setFilterFlag(checked);
						break;
					}
				}
			});
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
}