package com.simpfi.ui.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.Dimension;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;

import javax.swing.SwingUtilities;

import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.TrafficLight;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.InformationPopUp;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;
import com.simpfi.ui.TextArea;
import com.simpfi.ui.TextBox;
import com.simpfi.ui.panel.StatisticsPanel;
import com.simpfi.object.TrafficStatistics;

import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;

/**
 * A UI panel used for controlling the Lights. This class extends {@link Panel}.
 */
public class ProgramLightsPanel extends Panel {

	/** Logger. */
    private static final Logger logger = Logger.getLogger(ProgramLightsPanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The traffic light controller. */
	private final TrafficLightController trafficLightController;

	/** The drop down contain a list of junctions which contain traffic light. */
	private Dropdown<String> tlJunctionDropDown;
	private Dropdown<String> connectionDropDown;
	
	/** The drop down contain a list of connection light. */
	private InformationPopUp infoDialog;
	private TextArea infoDialogTextArea;
	
	
	/** The drop down contain a list of all incoming fromLane string. */
	private Dropdown<String> phaseDropDown;
	

	/** The list of all connections. */
	private String[] allStringConnection;
	
	/** The list of all phases. */
	private String[] allPhaseString;

	private String userTrafficLightJunctionId = "J0";
	private int phaseUserChoose = 0;
	// I am not sure I need to using it, I mean this one can help me make the clean code
	private double remainingDuration;
	private StatisticsPanel sp;
	private TrafficStatistics stats;
	
	/** Indicates whether the traffic lights are in adaptive mode. */
	public boolean isAdaptiveMode = false;

	/** Button on choosing Traffic Light state: static or adaptive */
	private Button changingAdaptiveModeOrStaticMode = null;

	/** Label displaying the current traffic light mode. */
	private Label modeOfTraffic;

	/** Text area showing feedback about adaptive traffic control impacts. */
	public TextArea textArea;


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
		generateFeedbackCard();
	}

	public void setStatisticsPanel(StatisticsPanel statisticsPanel){
		this.sp = statisticsPanel;
	}

	public void setStats(TrafficStatistics stats){
		this.stats = stats;
	}

	private void generateDropdowns(String[] allTLJunctionIDs, String firstJunctionIDs) throws Exception {
		tlJunctionDropDown = Dropdown.createDropdownWithLabel("Select intersection", allTLJunctionIDs, this);

		phaseDropDown = Dropdown.createDropdownWithLabel("Phase", getAllPhaseString(firstJunctionIDs), this);
		
		connectionDropDown = Dropdown.createDropdownWithLabel("All Connection", allStringConnection, this);

		tlJunctionDropDown.addActionListener(e -> {
			userTrafficLightJunctionId = (String) tlJunctionDropDown.getSelectedItem();
			// Update highlighted traffic light
			Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT = TrafficLight
				.searchforTrafficLight(userTrafficLightJunctionId, Settings.network.getTrafficLights());
			addAllLaneToDropDown(userTrafficLightJunctionId);
			try {
				addAllPhaseToDropDown(userTrafficLightJunctionId);
			} catch (Exception e1) {
				logger.log(Level.SEVERE,"Failed to show all phases",e1);
			}
		});

		phaseDropDown.addActionListener(e -> {
			Object selection = phaseDropDown.getSelectedItem();
			if (selection == null)
				return;
			try {
				phaseUserChoose = Integer.parseInt(selection.toString().trim());
			} catch (NumberFormatException ex) {
				logger.log(Level.SEVERE,"Failed to parse the phase that user chose", ex);
			}
		});

		// Add a listener to update highlighted connection when connection dropdown changes
		connectionDropDown.addActionListener(e -> {
			Object selectedConnection = connectionDropDown.getSelectedItem();
			if (selectedConnection != null && Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT != null) {
				Settings.highlight.HIGHLIGHTED_CONNECTION = Connection.searchforConnection(
					((String) selectedConnection).split(" -> ")[0],
					Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT.getConnections());
			}
		});
	}

	private void generateButtons() {
		Button showInformationOfTrafficLightButton = new Button("Show Information");
		showInformationOfTrafficLightButton.addActionListener(e -> {
			try {
				showInformationTrafficLightToDialog(userTrafficLightJunctionId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, "Failed to show information of traffic lights", e1);
			}
		});
		this.add(showInformationOfTrafficLightButton);
		
		Button applySwitchPhaseButton = new Button("Switch Phase");
		applySwitchPhaseButton.addActionListener(e -> {
			try {
				applySwitchPhaseListener(userTrafficLightJunctionId);
			} catch (Exception e1) {
				logger.log(Level.SEVERE, "Failed to apply the switch-phase listener", e1);
			}
		});
		this.add(applySwitchPhaseButton);

		TextBox durationTextBox = new TextBox(Settings.config.DURATION, true, true);
		this.add(durationTextBox);

		Button applyChangeDurationPhaseButton = new Button("Change Duration");
		applyChangeDurationPhaseButton.addActionListener(e -> {
			try {
				applyChangeDuration(userTrafficLightJunctionId, phaseUserChoose,
					Double.parseDouble(durationTextBox.getTextboxValue()));
			} catch (Exception e1) {
				logger.log(Level.SEVERE, "Failed to apply changing phase duration", e1);
			}
		});
		this.add(applyChangeDurationPhaseButton);
		
		changingAdaptiveModeOrStaticMode = new Button("Change to Adaptive Mode");
		changingAdaptiveModeOrStaticMode.addActionListener(e -> {
			actionForChangingMode();
		});
		this.add(changingAdaptiveModeOrStaticMode);
		
		modeOfTraffic = new Label("Current Mode: Static");
		this.add(modeOfTraffic);
	}

	// /**
	//  * Set the highlighted route variable in {@link Settings} to the currently
	//  * chosen junction in the dropdown.
	//  */
	// public void setHighlightedIntersectionTrafficLight() {
	// 	Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT = TrafficLight
	// 		.searchforTrafficLight((String) tlJunctionDropDown.getSelectedItem(), Settings.network.getTrafficLights());
	// }

	
	//  /**
	//  * Set the highlighted route variable in {@link Settings} to the currently
	//  * chosen junction in the dropdown.
	//  */
	// public void setHighlightedConnection() {
	// 	Settings.highlight.HIGHLIGHTED_CONNECTION = Connection.searchforConnection(
	// 		((String) connectionDropDown.getSelectedItem()).split(" -> ")[0],
	// 		Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT.getConnections());
	// }

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

	/* this one is change to getAllconnection */
	// private String[] getAllFromLaneID(List<Connection> allConnections) {
	// String[] allFromLaneID = new String[allConnections.size()];
	//
	// for (int i = 0; i < allConnections.size(); i++) {
	// Lane fromLane = allConnections.get(i).getFromLane();
	// allFromLaneID[i] = fromLane.getLaneId();
	// }
	// return allFromLaneID;
	// }

	// I will delete it in the future
	public String[] getAllStringConnection(List<Connection> allConnections) {
		String[] allStringConnection = new String[allConnections.size()];
		for (int i = 0; i < allConnections.size(); i++) {
			
			Connection connection = allConnections.get(i);
			StringBuilder sb = new StringBuilder();
		    sb.append(connection.getFromLane().getLaneId()).append(" -> ").append(connection.getToLane().getLaneId());
//			System.out.println(allConnections.get(i).getFromLane().getLaneId() + " " + allConnections.get(i).getToLane().getLaneId());
		    allStringConnection[i] = sb.toString();
		    
		}
		return allStringConnection;
	}

	public String[] getAllPhaseString(String tlId) throws Exception {
		SumoTLSProgram program = trafficLightController.getProgramFromTrafficLight(tlId);

		if (program == null) {
			// empty dropdown
			logger.log(Level.INFO,"TLS program not ready yet for {0}",tlId);
			return new String[0];
		}

		String[] allPhaseNumber = new String[program.phases.size()];

		for (int i = 0; i < program.phases.size(); i++) {
			allPhaseNumber[i] = String.valueOf(i);
		}
		return allPhaseNumber;
	}

	// Will be change the style in the future
	public void addAllLaneToDropDown(String JunctionID) {
		List<Connection> allConnections = getAllConnection(JunctionID);
		allStringConnection = getAllStringConnection(allConnections);

		connectionDropDown.removeAllItems();
		for (String connectionString : allStringConnection) {
			connectionDropDown.addItem(connectionString);
		}
	}

	public void addAllPhaseToDropDown(String JunctionID) throws Exception {
		allPhaseString = this.getAllPhaseString(JunctionID);

		phaseDropDown.removeAllItems();
		for (String phaseString : allPhaseString) {
			phaseDropDown.addItem(phaseString);
		}
	}
	
	public void showInformationTrafficLightToDialog(String trafficLightID) throws Exception {

	    if (infoDialog == null  || !infoDialog.isDisplayable()) {
	        infoDialog = new InformationPopUp("Traffic Light Information", false);
	        infoDialog.setLocationRelativeTo(this);
	        // This one just demo the content Pane :))) 
            // infoDialog.setContentPane(this);
	        
	        infoDialogTextArea = new TextArea(false);
	        
	        ScrollPane sp = new ScrollPane();
	        sp.addItem(infoDialogTextArea);
	        
	        infoDialog.add(sp);
	        infoDialog.setVisible(true);
	    }

	    List<Connection> allConnections = getAllConnection(trafficLightID);
	    SumoTLSProgram program = trafficLightController.getProgramFromTrafficLight(trafficLightID);
	    int currentPhaseIndex = program.currentPhaseIndex;
	    SumoTLSPhase phase = program.phases.get(currentPhaseIndex);

	    StringBuilder sb = new StringBuilder();
	    sb.append("Traffic Light: ").append(trafficLightID).append("\n");
	    sb.append("Current Phase: ").append(currentPhaseIndex).append("\n\n");
	    sb.append(String.format(" Remaining: %d \n\n", (int) remainingDuration));

	    for (Connection c : allConnections) {
	        String fromLaneString = c.getFromLane().getLaneId();
	        String toLaneString = c.getToLane().getLaneId();
	        char signal = trafficLightController.getStateofLane(allConnections, fromLaneString, phase);

	        sb.append("From: ").append(fromLaneString)
	          .append(" → To: ").append(toLaneString)
	          .append(" | Signal: ").append(signal)
	          .append("\n");
	    }

	    infoDialogTextArea.setText(sb.toString());
	}

	public void applySwitchPhaseListener(String trafficLightID) throws Exception {
		trafficLightController.setPhase(trafficLightID, phaseUserChoose);
	}

	public void applyChangeDuration(String trafficLightID, int phaseNumber, double duration) throws Exception {
		// In the future I will make a class get duration with parameter phaseNumber and trafficLightID
		SumoTLSProgram program = trafficLightController.getProgramFromTrafficLight(trafficLightID);

		ArrayList<SumoTLSPhase> listOfPhases = program.phases;
		SumoTLSPhase phase = listOfPhases.get(phaseNumber);
		phase.duration = duration;
		showImpactOfTimingChange();

		trafficLightController.setCompleteTrafficLight(trafficLightID, program);
	}
	
	public void actionForChangingMode() {
		
		if (changingAdaptiveModeOrStaticMode.getText().equals("Change to Static Mode")) {
			isAdaptiveMode = false;
			changingAdaptiveModeOrStaticMode.setText("Change to Adaptive Mode");
			modeOfTraffic.setText("Current Mode: Static Mode");
			
		}
		else {
			isAdaptiveMode = true;
			changingAdaptiveModeOrStaticMode.setText("Change to Static Mode");
			modeOfTraffic.setText("Current Mode: Adaptive Mode");
		}
	}
	
	public Double showRemainingDuration(String trafficLightID, double currentTime) throws Exception {
		// In the future I will make a class get duration with parameter phaseNumber and trafficLightID
		double nextSwitch = trafficLightController.getNextSwitch(trafficLightID);
		return nextSwitch - currentTime;
	}
	
	public void updateRemainingTime(String trafficLightID, int phaseIndex, double remainingTime) {
		this.remainingDuration = remainingTime;
		
	    try {
	        if (infoDialog != null && infoDialog.isVisible()) {
	        	showInformationTrafficLightToDialog(trafficLightID);
	        }
	    } catch (Exception ex) {
	        logger.log(Level.SEVERE, "Problems with update the remaining time", ex);
	    }
	}

	// Using just for testing the duration in app.java
	public String getSelectedTrafficLightID() {
	    return userTrafficLightJunctionId;
	}

	public int getSelectedPhase() {
	    return phaseUserChoose;
	}

	/**
	 * Creates a UI card component containing a title and a scrollable text area.
	 * <p>
	 * This card is primarily used to display textual feedback or analysis results
	 * in a visually grouped and readable format. The text area is wrapped inside
	 * a scroll pane to support variable-length content.
	 * </p>
	 *
	 * @param title     the title displayed at the top of the card
	 * @param textArea  the {@link TextArea} used to display feedback text
	 * @param titleFont the font applied to the card title
	 * @return a {@link Panel} representing the formatted card
	 */

	private Panel createCard(String title, TextArea textArea, Font titleFont) {
		Panel card = new Panel();
		card.setLayout(new BorderLayout(10, 10));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.GRAY, 1),
			BorderFactory.createEmptyBorder(12, 12, 12, 12)
		));
		card.setBackground(Color.WHITE);

		Label header = new Label(title);
		header.setFont(titleFont);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		card.add(header, BorderLayout.NORTH);

		// Wrap TextArea in a scroll pane if necessary
		ScrollPane scroll = new ScrollPane();
		scroll.addItem(textArea);
		card.add(scroll, BorderLayout.CENTER);

		card.setMaximumSize(new Dimension(300, 200));
		card.setAlignmentX(Component.LEFT_ALIGNMENT);

		return card;
	}

	/**
	 * Computes and displays the impact of traffic light timing changes.
	 * <p>
	 * This method collects key performance indicators from
	 * {@link TrafficStatistics}, including:
	 * <ul>
	 *   <li>Average vehicle speed</li>
	 *   <li>Number of congested edges</li>
	 *   <li>Average travel time</li>
	 * </ul>
	 * The computed values are then forwarded to the UI for display.
	 * </p>
	 * <p>
	 * UI updates are executed on the Swing Event Dispatch Thread (EDT)
	 * to ensure thread safety.
	 * </p>
	 */
	public void showImpactOfTimingChange(){
		double avgSpeed = stats.getAverageSpeed();
		int totalCongestion = stats.getLastCongestedEdgeCount();
		double avgTravelTime = calculateAverageTimeTravel(stats.getTravelTimesArray());

		SwingUtilities.invokeLater(() -> {
			updateImpactDisplay(avgSpeed, totalCongestion, avgTravelTime);
		});
	}

	/**
	 * Updates the adaptive feedback text displayed in the feedback card.
	 * <p>
	 * This method formats simulation performance metrics into a human-readable
	 * text block and updates the associated {@link TextArea}.
	 * </p>
	 * <p>
	 * The update is dispatched on the Swing Event Dispatch Thread (EDT)
	 * to maintain Swing thread safety.
	 * </p>
	 *
	 * @param avgSpeed        the current average vehicle speed
	 * @param congestedCount  the number of congested edges
	 * @param avgTravelTime  the average travel time of vehicles
	 */

	public void updateImpactDisplay(double avgSpeed, int congestedCount, double avgTravelTime) {
		String feedback =
			"Impact of Timing Adjustment:\n" +
			"Average Speed: " + String.format("%.2f m/s", avgSpeed) + "\n" +
			"Congested Edges: " + congestedCount + "\n" +
			"Average Travel Time: " + String.format("%.2f sec", avgTravelTime);
		    SwingUtilities.invokeLater(() -> textArea.setText(feedback));

	}

	/** Compute average travel time */
	double calculateAverageTimeTravel(double[] travelTimes){
		if (travelTimes.length == 0) {return 0;}
		double sum = 0.0;
		if (travelTimes.length != 0){
			for (double i : travelTimes){
				sum += i;
			}
		}
		return sum/travelTimes.length;
	}

	/**
	 * Initializes and adds the adaptive feedback card to the panel.
	 * <p>
	 * This method creates a non-editable, word-wrapped {@link TextArea},
	 * embeds it into a formatted card using {@code createCard}, and
	 * attaches the card to the parent panel.
	 * </p>
	 * <p>
	 * This method should be called once during UI initialization.
	 * </p>
	 */

	private void generateFeedbackCard() {
		textArea = new TextArea(false);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		Font titleFont = new Font("SansSerif", Font.BOLD, 16);
		Panel feedbackCard = createCard("Adaptive Feedback", textArea, titleFont);
		this.add(feedbackCard);
	}
}
