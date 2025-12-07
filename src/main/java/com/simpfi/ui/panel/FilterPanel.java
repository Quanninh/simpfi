package com.simpfi.ui.panel;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.Road;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.ui.CheckBox;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;

/**
 * A panel for filtering vehicles. This class extends {@link Panel}.
 */
public class FilterPanel extends Panel{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list of vehicle-type checkboxes */
	private CheckBox[] vehicleTypeOptions;

	/** The list of edge checkboxes */
	private CheckBox[] roadOptions;

	/**
	 * Instantiates a new filter panel.
	 *
	 * @param conn the connection manager (unused but kept for consistency)
	 */
	public FilterPanel(SumoConnectionManager conn) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Filter out vehicles by type
		String[] vehicleTypes = getAllVehiclesTypesAsStrings();
		vehicleTypeOptions = new CheckBox[vehicleTypes.length];

		// Add UI components on the map
		this.add(new Label("Vehicle Types: "));
		for (int i = 0; i < vehicleTypes.length; i++) {
			CheckBox option = new CheckBox(vehicleTypes[i], true);
			option.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(option);
			vehicleTypeOptions[i] = option;
		}

		// Attach listeners to VehicleType checkboxes to update filter when toggled
		attachCheckboxListenersForVehicleTypes();

		// Filter out vehicles by roads they are on
		String[] roads = getAllRoadsAsStrings();
		roadOptions = new CheckBox[roads.length];

		// Add UI componets on the map
		this.add(new Label("Roads: "));
		for (int i = 0; i < roads.length; i++) {
			CheckBox roadOption = new CheckBox(roads[i], true);
			roadOption.setAlignmentX(Component.LEFT_ALIGNMENT);
			// Use a MouseAdapter to highlight the road which is entered by users
            roadOption.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					CheckBox chosenCheckBox = (CheckBox) e.getSource();
					Settings.config.HIGHLIGHTED_ROAD_FILTER = chosenCheckBox.getText();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					Settings.config.HIGHLIGHTED_ROAD_FILTER = "";
				}
			});

			roadOptions[i] = roadOption;
		}
		ScrollPane roadScrollPane = new ScrollPane();
		roadScrollPane.addCheckBoxLists(roadOptions);
		this.add(roadScrollPane);
		
		// Attach listeners to Road checkboxes to update filter when toggled
		attachCheckboxListenersForRoads();
	}

	/**
	 * Method used to change the VehicleType's filterFlag state if its check box is
	 * toggled.
	 */
	private void attachCheckboxListenersForVehicleTypes() {
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
	 * Method used to change the Road's filterFlag state if its check box is toggled.
	 */
	private void attachCheckboxListenersForRoads() {
		for (CheckBox roadOption : roadOptions) {
			roadOption.onChange(e -> {
				boolean checked = roadOption.isChecked();
				// Set flag on the Road object
				for (Road road : Settings.network.getRoads()) {
					if (road.getId().equals(roadOption.getText())) {
						road.setFilterFlag(checked);
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

	/**
	 * Returns all roads as strings.
	 *
	 * @return the all roads
	 */
	private String[] getAllRoadsAsStrings() {
		List<Road> allRoads = Settings.network.getRoads();
		String[] roads = new String[allRoads.size()];

		for (int i = 0; i < allRoads.size(); i++) {
			roads[i] = allRoads.get(i).getId();
		}

		return roads;
	}
}