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

	

	// /** The vehicle controller. */
	// private final VehicleController vehicleController;

	/**
	 * Instantiates a new filter panel.
	 *
	 * @param conn the conn
	 */
	public FilterPanel(SumoConnectionManager conn) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String[] vehicleTypes = getAllVehiclesTypesAsStrings();

		// Filter out vehicles by type
		this.add(new Label("Vehicles: "));
		for(String i : vehicleTypes){
			CheckBox option = new CheckBox(i, true);
			option.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(option);

		}

	}

	// // Mark unselected vehicles as inactive
	// private void markUnselectedVehicles(VehicleController vehicleController, )

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
// public static final Map<Color, String> COLOR_MAP = new HashMap<>();

// 	static {
// 		COLOR_MAP.put(Constants.DEFAULT_VEHICLE_COLOR, "Yellow");
// 		COLOR_MAP.put(Constants.TRUCK_COLOR, "Gray");
// 		COLOR_MAP.put(Constants.BUS_COLOR, "Aqua");
// 		COLOR_MAP.put(Constants.MOTORCYCLE_COLOR, "Magneta");
// 		COLOR_MAP.put(Constants.EMERGENCY_COLOR, "Orange");
// 	}
// 	/**
// 	 * Method used to convert Color to String.
// 	 */
// 	public String getColorString(Color c){
// 		return COLOR_MAP.get(c);
// 	}
// private String[] getAllVehiclesColorsAsStrings() {
// 		List<VehicleType> allVehicles = Settings.network.getVehicleTypes();
// 		String[] vehicleColors = new String[allVehicles.size()];

// 		for (int i = 0; i < allVehicles.size(); i++) {
// 			vehicleColors[i] = switch(allVehicles.get(i).getId()){
// 				case "truck" -> getColor(Constants.TRUCK_COLOR);
// 				case "bus" -> getColor(Constants.BUS_COLOR);
// 				case "motorcycle" -> getColor(Constants.MOTORCYCLE_COLOR);
// 				case "emergency" -> getColor(Constants.EMERGENCY_COLOR);
// 				default -> getColor(Constants.DEFAULT_VEHICLE_COLOR);
// 		};

// 		return vehicleColors;
// 	}