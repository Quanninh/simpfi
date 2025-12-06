package com.simpfi.ui.panel;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.Edge;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.ui.CheckBox;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;


/**
 * A panel for filtering vehicles. This class extends {@link Panel}.
 */
public class FilterPanel extends Panel implements MouseListener{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list of vehicle-type checkboxes */
	private CheckBox[] vehicleTypeOptions;

	/** The list of edge checkboxes */
	private CheckBox[] edgeOptions;

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

		// Filter out vehicles by edges they are on
		String[] edges = getAllEdgesAsStrings();
		edgeOptions = new CheckBox[edges.length];

		// Add UI componets on the map
		this.add(new Label("Edges: "));
		for (int i = 0; i < edges.length; i++) {
			CheckBox edgeOption = new CheckBox(edges[i], true);
			edgeOption.setAlignmentX(Component.LEFT_ALIGNMENT);
			edgeOption.addMouseListener(this);
			edgeOptions[i] = edgeOption;
		}
		ScrollPane edgeScrollPane = new ScrollPane();
		edgeScrollPane.addCheckBoxLists(edgeOptions);
		this.add(edgeScrollPane);

		// // Attach listeners to Edge checkboxes to update filter when toggled
		// attachCheckboxListenersForEdges();
	}

	/**
	 * Method used to change the VehicleType's filterFlag state if its check box is toggled.
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

	// /**
	//  * Method used to change the Edge's filterFlag state if its check box is toggled.
	//  */
	// private void attachCheckboxListenersForEdges() {
	// 	for (CheckBox edgeOption : edgeOptions) {
	// 		edgeOption.onChange(e -> {
	// 			boolean checked = edgeOption.isChecked();
	// 			// Set flag on the Edge object
	// 			for (Edge edge : Settings.network.getEdges()) {
	// 				if (edge.getId().equals(edgeOption.getText())) {
	// 					edge.setFilterFlag(checked);
	// 					break;
	// 				}
	// 			}
	// 		});
	// 	}
	// }

	@Override
	public void mouseEntered(MouseEvent e) {
		// Invoked when the mouse enters a component
		CheckBox chosenCheckBox = (CheckBox) e.getSource();
		Settings.config.HIGHLIGHTED_EDGE_FILTER = chosenCheckBox.getText();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Invoked when the mouse exits a component
		Settings.config.HIGHLIGHTED_EDGE_FILTER = ""; // No edge is highlighted
	}

	@Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

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
	 * Returns all edges as strings.
	 *
	 * @return the all edges
	 */
	private String[] getAllEdgesAsStrings() {
		List<Edge> allEdges = Settings.network.getEdges();
		String[] edges = new String[allEdges.size()];

		for (int i = 0; i < allEdges.size(); i++) {
			edges[i] = allEdges.get(i).getId();
		}

		return edges;
	}
}