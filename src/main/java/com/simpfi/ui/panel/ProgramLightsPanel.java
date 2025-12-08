package com.simpfi.ui.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.Lane;
import com.simpfi.object.TrafficLight;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Panel;
import com.simpfi.ui.TextBox;

import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;

/**
 * A UI panel used for controlling the Lights. This class extends {@link Panel}.
 */
public class ProgramLightsPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The traffic light controller. */
	private final TrafficLightController trafficLightController;

	/** The drop down contain a list of junctions which contain traffic light. */
	private Dropdown<String> tlJunctionDropDown;
	/** The drop down contain a list of connection light. */
	private Dropdown<String> connectionDropDown;
	/** The drop down contain a list of all incoming fromLane string. */
	private Dropdown<String> phaseDropDown;

	/** The list of all connections. */
	private String[] allStringConnection;
	/** The list of all phases. */
	private String[] allPhaseString;

	private String userJunctionId = "J0";
	private int phaseUserChoose = 0;

	public ProgramLightsPanel(SumoConnectionManager conn) throws Exception {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		trafficLightController = new TrafficLightController(conn);

		String[] allTLJunctionIDs = getAllTrafficLightJunctionID();

		// Default selection = first junction
		String firstJunctionIDs = allTLJunctionIDs[0];
		List<Connection> allConnections = getAllConnection(firstJunctionIDs);
		allStringConnection = getAllStringConnection(allConnections);

		generateDropdowns(allTLJunctionIDs, firstJunctionIDs);
		generateButtons();
	}

	private void generateDropdowns(String[] allTLJunctionIDs, String firstJunctionIDs) throws Exception {
		tlJunctionDropDown = Dropdown.createDropdownWithLabel("Select intersection", allTLJunctionIDs, this);
		connectionDropDown = Dropdown.createDropdownWithLabel("Select Connection ", allStringConnection, this);
		phaseDropDown = Dropdown.createDropdownWithLabel("Phase", getAllPhaseString(firstJunctionIDs), this);

		tlJunctionDropDown.addActionListener(e -> {
			userJunctionId = (String) tlJunctionDropDown.getSelectedItem();
			showLaneInformation(userJunctionId);
			try {
				showPhaseInformation(userJunctionId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		phaseDropDown.addActionListener(e -> {
			Object selection = phaseDropDown.getSelectedItem();
			if (selection == null)
				return;
			try {
				phaseUserChoose = Integer.parseInt(selection.toString().trim());
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		});
	}

	private void generateButtons() {
		Button applySwitchPhaseButton = new Button("Switch Phase");
		applySwitchPhaseButton.addActionListener(e -> {
			try {
				applySwitchPhaseListener(userJunctionId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		this.add(applySwitchPhaseButton);

		TextBox durationTextBox = new TextBox(Settings.config.DURATION, true, true);
		this.add(durationTextBox);

		Button applyChangeDurationPhaseButton = new Button("Change Duration");
		applyChangeDurationPhaseButton.addActionListener(e -> {
			try {
				applyChangeDuration(userJunctionId, phaseUserChoose,
					Double.parseDouble(durationTextBox.getTextboxValue()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		this.add(applyChangeDurationPhaseButton);
	}

	/**
	 * Set the highlighted route variable in {@link Settings} to the currently
	 * chosen junction in the dropdown.
	 */
	public void setHighlightedIntersectionTrafficLight() {
		Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT = (String) tlJunctionDropDown.getSelectedItem();
	}

	//
	// /**
	// * Set the highlighted route variable in {@link Settings} to the currently
	// * chosen junction in the dropdown.
	// */
	public void setHighlightedConnection() {
		Settings.highlight.HIGHLIGHTED_CONNECTION = (String) connectionDropDown.getSelectedItem();
	}

	public String[] getAllTrafficLightJunctionID() {
		List<TrafficLight> allTrafficLights = Settings.network.getTrafficLights();
		String[] allTrafficLightJunctionIDs = new String[allTrafficLights.size()];

		for (int i = 0; i < allTrafficLights.size(); i++) {
			allTrafficLightJunctionIDs[i] = allTrafficLights.get(i).getJunction().getId();
		}
		return allTrafficLightJunctionIDs;
	}

	public List<Connection> getAllConnection(String junctionID) {
		List<TrafficLight> allTrafficLight = Settings.network.getTrafficLights();
		TrafficLight trafficLight = TrafficLight.searchforTrafficLight(junctionID, allTrafficLight);

		return trafficLight.getConnections();
	}

	// private String[] getAllFromLaneID(List<Connection> allConnections) {
	// String[] allFromLaneID = new String[allConnections.size()];
	//
	// for (int i = 0; i < allConnections.size(); i++) {
	// Lane fromLane = allConnections.get(i).getFromLane();
	// allFromLaneID[i] = fromLane.getLaneId();
	// }
	// return allFromLaneID;
	// }

	public String[] getAllStringConnection(List<Connection> allConnections) {
		String[] allStringConnection = new String[allConnections.size()];
		for (int i = 0; i < allConnections.size(); i++) {
			allStringConnection[i] = allConnections.get(i).getFromLane().getLaneId();
		}
		return allStringConnection;
	}

	public String[] getAllPhaseString(String tlId) throws Exception {
		SumoTLSProgram program = this.getProgramFromTrafficLight(tlId);

		if (program == null) {
			// empty dropdown
			System.out.println("TLS program not ready yet for " + tlId);
			return new String[0];
		}

		String[] allPhaseNumber = new String[program.phases.size()];

		for (int i = 0; i < program.phases.size(); i++) {
			allPhaseNumber[i] = String.valueOf(i);
		}

		return allPhaseNumber;
	}

	public void showLaneInformation(String JunctionID) {
		List<Connection> allConnections = getAllConnection(JunctionID);
		allStringConnection = getAllStringConnection(allConnections);

		connectionDropDown.removeAllItems();
		for (String connectionString : allStringConnection) {
			connectionDropDown.addItem(connectionString);
		}
	}

	public void showPhaseInformation(String JunctionID) throws Exception {
		allPhaseString = this.getAllPhaseString(JunctionID);

		phaseDropDown.removeAllItems();
		for (String phaseString : allPhaseString) {
			phaseDropDown.addItem(phaseString);
		}
	}

	public char getStateofLane(List<Connection> allConnections, String laneId, SumoTLSPhase phase) {
		String stateofPhase = phase.phasedef;

		for (int i = 0; i < allConnections.size(); i++) {
			Lane fromLane = allConnections.get(i).getFromLane();
			if (fromLane.getLaneId().equals(laneId)) {
				return stateofPhase.charAt(i);
			}
		}

		return 0;
	}

	public SumoTLSProgram getProgramFromTrafficLight(String trafficLightID) throws Exception {
		SumoTLSController trafficLightCompleted = trafficLightController
			.getCompletedTrafficLightDefinition(trafficLightID);
		String programName = trafficLightController.getProgramName(trafficLightID);

		return trafficLightCompleted.get(programName);

	}

	public void applySwitchPhaseListener(String trafficLightID) throws Exception {
		trafficLightController.setPhase(trafficLightID, phaseUserChoose);
	}

	public void applyChangeDuration(String trafficLightID, int phaseNumber, double duration) throws Exception {
		SumoTLSProgram program = this.getProgramFromTrafficLight(trafficLightID);

		ArrayList<SumoTLSPhase> listOfPhases = program.phases;
		SumoTLSPhase phase = listOfPhases.get(phaseNumber);
		phase.duration = duration;

		trafficLightController.setCompleteTrafficLight(trafficLightID, program);
	}
}
