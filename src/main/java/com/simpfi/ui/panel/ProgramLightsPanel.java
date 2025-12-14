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
	
	/** The drop down contain a list of connection in traffic light. */
	private Dropdown<String> connectionDropDown;
	
	/** The Dialog show information of specific traffic light. */
	private InformationPopUp infoDialog;
	
	/** The text area for information inside the dialog. */
	private TextArea infoDialogTextArea;
	
	/** The drop down contain a list of all phase number of traffic light. */
	private Dropdown<String> phaseDropDown;
	
	/** The list of all connections. */
	private String[] allStringConnection;
	
	/** The list of all phases number string. */
	private String[] allPhaseString;

	/** The user's choose for traffic light junction. */
	private String userTrafficLightJunctionId = "J0";
	
	/** The user's choose for phase. */
	private int phaseUserChoose = 0;
	
	/** The remaining duration. */
	private double remainingDuration;
	
	private StatisticsPanel sp; // This one is not use ??
	private TrafficStatistics stats;

	/** Indicates whether the traffic lights are in adaptive mode. */
	public boolean isAdaptiveMode = false;

	/** Button on choosing Traffic Light state: static or adaptive */
	private Button changingAdaptiveModeOrStaticMode = null;

	/** Label displaying the current traffic light mode. */
	private Label modeOfTraffic;

	/** Text area showing feedback about adaptive traffic control impacts. */
	public TextArea textArea;

	/**
	 *  Instantiates a new program light panel.
	 * @param conn
	 * @throws Exception
	 */
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
	
	/**
	 * Generate the all drop Down.
	 * @param allTLJunctionIDs all of the traffic light junction IDs
	 * @param firstJunctionIDs initial junction ID
	 * @throws Exception if the TraCI connection fails
	 */

	private void generateDropdowns(String[] allTLJunctionIDs, String firstJunctionID) throws Exception {
		tlJunctionDropDown = Dropdown.createDropdownWithLabel("Select intersection", allTLJunctionIDs, this);

		phaseDropDown = Dropdown.createDropdownWithLabel("Phase", getAllPhaseString(firstJunctionID), this);
		
		connectionDropDown = Dropdown.createDropdownWithLabel("All Connection", allStringConnection, this);

		tlJunctionDropDown.addActionListener(e -> {
			userTrafficLightJunctionId = (String) tlJunctionDropDown.getSelectedItem();
			// Update highlighted traffic light
			Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT = TrafficLight
				.searchforTrafficLight(userTrafficLightJunctionId, Settings.network.getTrafficLights());
			addAllConnectionToDropDown(userTrafficLightJunctionId);
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
	
	/**
	 * Create all the buttons
	 */

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

	/**
	 * Get all the traffic light IDs.
	 * @return all the traffic light IDs
	 */
	public String[] getAllTrafficLightJunctionID() {
		List<TrafficLight> allTrafficLights = Settings.network.getTrafficLights();
		String[] allTrafficLightJunctionIDs = new String[allTrafficLights.size()];

		for (int i = 0; i < allTrafficLights.size(); i++) {
			allTrafficLightJunctionIDs[i] = allTrafficLights.get(i).getJunction().getId();
		}
		return allTrafficLightJunctionIDs;
	}
	/**
	 * Get all the connection in specific traffic light.
	 * @param trafficLightJunctionID the id of traffic light junction
	 * @return all the connection in traffic light
	 */

	public List<Connection> getAllConnection(String trafficLightJunctionID) {
		List<TrafficLight> allTrafficLight = Settings.network.getTrafficLights();
		TrafficLight trafficLight = TrafficLight.searchforTrafficLight(trafficLightJunctionID, allTrafficLight);

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

	/**
	 * Get all the connection string and show it to the drop down as a string builder.
	 * @param allConnections list all of connection
	 * @return All the connection string builder ( From Lane -> To Lane )
	 */
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
	/**
	 * Get all phase's number in traffic light. 
	 * @param tlId the traffic light ID
	 * @return all phase's number
	 * @throws Exception if the TraCI connection fails
	 */

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

	/**
	 * The action listener to add all the connection to drop down.
	 * @param trafficLightID traffic light ID
	 */
	public void addAllConnectionToDropDown(String trafficLightID) {
		List<Connection> allConnections = getAllConnection(trafficLightID);
		allStringConnection = getAllStringConnection(allConnections);

		connectionDropDown.removeAllItems();
		for (String connectionString : allStringConnection) {
			connectionDropDown.addItem(connectionString);
		}
	}
	/**
	 * The action listener to add all phase to drop down.
	 * @param trafficLightID traffic light ID
	 * @throws Exception if the TraCI connection fails
	 */

	public void addAllPhaseToDropDown(String trafficLightID) throws Exception {
		allPhaseString = this.getAllPhaseString(trafficLightID);

		phaseDropDown.removeAllItems();
		for (String phaseString : allPhaseString) {
			phaseDropDown.addItem(phaseString);
		}
	}
	/**
	 * The action listener to show information of traffic light to dialog.
	 * @param trafficLightID traffic light ID
	 * @throws Exception if the TraCI connection fails
	 */
	
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
	        char signal = trafficLightController.getStateOfLane(allConnections, fromLaneString, phase);

	        sb.append("From: ").append(fromLaneString)
	          .append(" → To: ").append(toLaneString)
	          .append(" | Signal: ").append(signal)
	          .append("\n");
	    }

	    infoDialogTextArea.setText(sb.toString());
	}
	/**
	 * The listener to apply switch phase.
	 * @param trafficLightID traffic light ID
	 * @throws Exception if the TraCI connection fails
	 */

	public void applySwitchPhaseListener(String trafficLightID) throws Exception {
		trafficLightController.setPhase(trafficLightID, phaseUserChoose);
	}
	/**
	 * The listener to change the duration.
	 * @param trafficLightID traffic light ID
	 * @param phaseNumber the phase number that user want to change
	 * @param duration the duration that user want to change
	 * @throws Exception if the TraCI connection fails
	 */

	public void applyChangeDuration(String trafficLightID, int phaseNumber, double duration) throws Exception {
		// In the future I will make a class get duration with parameter phaseNumber and trafficLightID
		SumoTLSProgram program = trafficLightController.getProgramFromTrafficLight(trafficLightID);

		ArrayList<SumoTLSPhase> listOfPhases = program.phases;
		SumoTLSPhase phase = listOfPhases.get(phaseNumber);
		phase.duration = duration;
		showImpactOfTimingChange();

		trafficLightController.setCompleteTrafficLight(trafficLightID, program);
	}
	/**
	 * The action listener for changing the mode of the traffic.
	 */
	
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
	
	/**
	 * Show remain duration in current phase of specific traffic light.
	 * @param trafficLightID traffic light ID
	 * @param currentTime the current time
	 * @return the remain duration
	 * @throws Exception if the TraCI connection fails
	 */
	public Double showRemainingDuration(String trafficLightID, double currentTime) throws Exception {
		// In the future I will make a class get duration with parameter phaseNumber and trafficLightID
		double nextSwitch = trafficLightController.getNextSwitch(trafficLightID);
		return nextSwitch - currentTime;
	}
	
	/**
	 * Update the remaining duration time of the specific phase of traffic light.
	 * @param trafficLightID traffic light ID
	 * @param phaseIndex the phase that we need to update
	 * @param remainingTime the remaining time
	 */
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

	/**
	 * Get the user selected traffic light ID.
	 * @return the user traffic light id
	 */
	public String getSelectedTrafficLightID() {
	    return userTrafficLightJunctionId;
	}

	/**
	 * get the user selected phase.
	 * @return phase that user choose
	 */
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
		int totalCongestion = stats.getCongestedEdges(5.0).size();
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
