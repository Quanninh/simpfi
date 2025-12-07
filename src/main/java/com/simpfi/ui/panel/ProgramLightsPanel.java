package com.simpfi.ui.panel;

import java.awt.Button;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;

import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.Lane;
import com.simpfi.object.TrafficLight;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.TextBox;
import com.simpfi.ui.TextBox.SettingsType;

import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;

/**
 * A UI panel used for controlling the Lights. This class extends {@link Panel}.
 */
public class ProgramLightsPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The traffic light controller */
	private final TrafficLightController trafficLightController;
	
	/** The drop down contain a list of junctions which contain traffic light */
	private Dropdown<String> junctionWithTrafficLightDropDown;
	/**
	 * The drop down contain a list of connection 
	 * light
	 */
	private Dropdown<String> connectionDropDown;
	
	/**
	 * The drop down contain a list of all incoming fromLane string 
	 */
	private static Dropdown<String> stateOfLane;

	/**
	 * The drop down contain a list of all incoming fromLane string 
	 */
	private static String[] allStringConnection;
	
	/**
	 * Assume the first junction Id user will choose is J0
	 * Or I we can, we let the firstJunction or some logic here to make the clean code
	 */
	private String junctionIdUserChoose = "J0";
	/**
	 * Assume the first junction Id user will choose is J0
	 * Or I we can, we let the firstJunction or some logic here to make the clean code
	 */
	private String laneIdUserChoose = "J0";
	
	/**
	 * Assume the first junction Id user will choose is J0
	 * Or I we can, we let the firstJunction or some logic here to make the clean code
	 */
	private String phaseUserChoose = "0";
	
	

	

	public ProgramLightsPanel(SumoConnectionManager conn) throws Exception
	{
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String[] allJunctionIDs = getAllTrafficLightJunctionID();
		
		// Assume it show the first junctions. And in the future, I will let 
		// the junction in the setting
		String firstJunctionIDs = allJunctionIDs[0];
		List<Connection> allConnections = getAllConnection(firstJunctionIDs);
		allStringConnection = getAllStringConnection(allConnections);
//		SumoTLSProgram program = getProgramFromTrafficLight(firstJunctionIDs);
//		Dropdown<String> stateOfLane = getStateofLane(allConnections, laneIdUserChoose, program.phases.get(Integer.parseInt(phaseUserChoose)));
		
		// Create Drop down
		junctionWithTrafficLightDropDown = createDropdownWithLabel("Select intersection", allJunctionIDs);
		connectionDropDown = createDropdownWithLabel("Select Connection ", allStringConnection);
		
		stateOfLane = createDropdownWithLabel("State of Lane", this.getAllStateofLane());
		
		// add Action listener 
		junctionWithTrafficLightDropDown.addActionListener(e -> {
			junctionIdUserChoose = (String) junctionWithTrafficLightDropDown.getSelectedItem();
			showLaneInformation(junctionIdUserChoose);	
		});
		// Text Box for changing the duration
		TextBox durationTextBox = new TextBox(true, SettingsType.DURATION, Settings.config.DURATION);
		
		// Button for apply the change
		trafficLightController = new TrafficLightController(conn);
		Button applyChangeButton = new Button("Apply");
		applyChangeButton.addActionListener(e -> {
			try {
				applyChangeListener(junctionIdUserChoose);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		
		this.add(durationTextBox);
		this.add(applyChangeButton);
	}
	
	/**
	 * Creates a dropdown along with a label.
	 *
	 * @param label the label text
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
	 * Set the highlighted route variable in {@link Settings} to the currently
	 * chosen junction in the dropdown.
	 */
	public void setHighlightedIntersectionTrafficLight() {
		Settings.config.HIGHLIGHTED_TRAFFIC_LIGHT = (String) junctionWithTrafficLightDropDown.getSelectedItem();
	}
//	
//	/**
//	 * Set the highlighted route variable in {@link Settings} to the currently
//	 * chosen junction in the dropdown.
//	 */
	public void setHighlightedConnection() {
		Settings.config.HIGHLIGHTED_CONNECTION = (String) connectionDropDown.getSelectedItem();
	}
//	
//	/**
//	 * Set the highlighted route variable in {@link Settings} to the currently
//	 * chosen junction in the dropdown.
//	 */
//	public void setHighlightedToLane() {
//		Settings.config.HIGHLIGHTED_TO_LANE = (String) toLaneDropDown.getSelectedItem();
//	}
	

	/**
	 * */
	public String[] getAllTrafficLightJunctionID() {
		List<TrafficLight> allTrafficLights = Settings.network.getTrafficLights();
		String[] allTrafficLightJunctionIDs = new String[allTrafficLights.size()];
		for (int i = 0; i < allTrafficLights.size(); i++) {
			allTrafficLightJunctionIDs[i] = allTrafficLights.get(i).getJunction().getId();
		}
		return allTrafficLightJunctionIDs;
	}
	/**
	 * */
	public List<Connection> getAllConnection(String junctionID)
	{
		List<TrafficLight> allTrafficLight = Settings.network.getTrafficLights();
		TrafficLight trafficLight = TrafficLight.searchforTrafficLight(junctionID, allTrafficLight);
		return trafficLight.getConnections();
	}
	
	/**
	 * */
	public String[] getAllFromLaneID(List<Connection> allConnections) {
		String[] allFromLaneID = new String[allConnections.size()];
		for (int i = 0; i < allConnections.size(); i++) {
			Lane fromLane = allConnections.get(i).getFromLane();
			allFromLaneID[i] = fromLane.getLaneId();
		}
		return allFromLaneID;
	}
	
	/**
	 * */
	public String[] getAllStringConnection(List<Connection> allConnections) {
		String[] allStringConnection = new String[allConnections.size()];
		for (int i = 0; i < allConnections.size(); i++) {
			allStringConnection[i] = allConnections.get(i).getFromLane().getLaneId();
		}
		return allStringConnection;
	}
	
	// It is better if I show on UI that yellow instead of Y
	// But in this code we need to convert Yellow -> y
	// So assume all the state is Y R G 
	// i will update it in the future
	public String[] getAllStateofLane() {
		String[] allStateofLane = new String[3];
		allStateofLane[0] = "y";
		allStateofLane[1] = "r";
		allStateofLane[2] = "g";
		return allStateofLane;
	}
	/**
	 * */
	 public void showLaneInformation(String JunctionID){
		List<Connection> allConnections = getAllConnection(JunctionID);
		allStringConnection = getAllStringConnection(allConnections);
		
		connectionDropDown.removeAllItems();
		for (String connectionString : allStringConnection) {
			connectionDropDown.addItem(connectionString);
		}
		
//		toLaneDropDown.removeAllItems();
//		for (String toLaneID : alltoLaneIDs) {
//			toLaneDropDown.addItem(toLaneID);
//		}
	 }
	 
	 /** 
	  * Get the state of specific Lane
	 * @throws Exception */
	 
	 public char getStateofLane (List<Connection> allConnections ,String laneId, SumoTLSPhase phase) {
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
		 SumoTLSController trafficLightCompleted = trafficLightController.getCompletedTrafficLightDefinition(trafficLightID);
		 String programName = trafficLightController.getProgramName(trafficLightID);
		 return trafficLightCompleted.get(programName);
		 
	 }
	 
	 /**
	 * @throws Exception  */
	 public void applyChangeListener(String trafficLightID) throws Exception {
		 
		 // Change the state of the program
		 
		 SumoTLSProgram program = this.getProgramFromTrafficLight(trafficLightID);
		 
		 // Suppose I want to change the duration of first phrase
		 ArrayList<SumoTLSPhase> phases = program.phases;
		 SumoTLSPhase firstPhase = phases.get(0);
		 firstPhase.duration = Settings.config.DURATION;
		 
		// Put TLS into external control mode
		 System.out.println(trafficLightController.getPhase(trafficLightID));
		 trafficLightController.setPhase(trafficLightID, 2); // empty string = external control
		 System.out.println(trafficLightController.getPhase(trafficLightID));
		 // Here I want to switch to the phase 2 of traffic Light
		
		 
		 
//		 for (int i = 0; i < phases.size(); i++) {
//			 System.out.println("Phase " + i + " duration: " + phases.get(i).duration + " "+phases.get(i).phasedef);
//			}
//		 
		 // Set a new program to the trafficLightController
		 trafficLightController.setCompleteTrafficLight(trafficLightID, program);
	 }
}
