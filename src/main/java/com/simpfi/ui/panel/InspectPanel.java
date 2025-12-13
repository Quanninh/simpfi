package com.simpfi.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import com.simpfi.config.Settings;
import com.simpfi.object.Vehicle;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Label;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;
import com.simpfi.ui.TextBox;

/**
 * A UI panel used for inspecting vehicles. This class extends {@link Panel}.
 */

public class InspectPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private VehicleController vehicleController;
	private SumoConnectionManager sumoConnectionManager;
	private MapPanel mapPanel;

	private List<Vehicle> selectedVehicles = new ArrayList<>();

	private Label modeLabel;
	private Label instructionLabel;
	private Label groupByLabel;
	private DefaultListModel<String> vehicleListModel;
	private JList<String> vehicleList;
	private Button changeModeButton;
	private Button selectAllButton;
	private Button clearButton;
	private Dropdown<String> groupByDropdown;
	private List<TextBox> vehicleTextBoxes;
	private ScrollPane statsScrollPane;
	private List<Label> vehicleStaticLabels;
	private Timer speedUpdateTimer;

	enum Mode {
		PanMODE, SelectMODE
	}

	private Mode currentMode = Mode.PanMODE;

	public InspectPanel(SumoConnectionManager conn, MapPanel mapPanel) {
		this.sumoConnectionManager = conn;
		this.vehicleController = new VehicleController(conn);
		this.mapPanel = mapPanel;

		this.setLayout(new BorderLayout());

		// content
		Panel contentPanel = new Panel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		// Panel für Liste, Buttons und Dropdown
		Panel listPanel = new Panel();
		listPanel.setLayout(new BorderLayout());

		// Fahrzeugliste in ScrollPane
		vehicleListModel = new DefaultListModel<>();
		vehicleList = new JList<>(vehicleListModel); // JList weil scroll liste mit auswahl
		vehicleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		vehicleList.addListSelectionListener(e -> {
			int listIndex = vehicleList.getSelectedIndex();
			if (listIndex == -1)
				return;

			String value = vehicleListModel.get(listIndex);

			// Header ignorieren
			if (value.startsWith("---")) {
				vehicleList.clearSelection(); // deselect
				return;
			}

			// neuer Index in selectedVehicles
			int vehicleIndex = getVehicleIndexFromListIndex(listIndex);
			updateStatsFields(vehicleIndex);
		});

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.addItem(vehicleList);
		scrollPane.setPreferredSize(new Dimension(200, 150));
		listPanel.add(scrollPane, BorderLayout.CENTER);

		// Panel rechts für SelectAll, Clear und Dropdown
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		selectAllButton = new Button("Select All");
		selectAllButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectAllButton.addActionListener(e -> {
			// Liste vorher leeren
			selectedVehicles.clear();
			vehicleListModel.clear();

			// all vehicles aus SUMO holen
			List<Vehicle> allVehicles = VehicleController.getVehicles();
			selectedVehicles.addAll(allVehicles);

			// IDs zur JList hinzufügen
			for (Vehicle v : allVehicles) {
				vehicleListModel.addElement(v.getID());
			}
			// Alle Einträge auswählen
			if (!allVehicles.isEmpty()) {
				vehicleList.setSelectionInterval(0, allVehicles.size() - 1);
			}
		});

		buttonPanel.add(selectAllButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		clearButton = new Button("Clear");
		clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clearButton.addActionListener(e -> {
			// Select All simulieren
			selectedVehicles.clear();
			vehicleListModel.clear();
			List<Vehicle> allVehicles = VehicleController.getVehicles();
			selectedVehicles.addAll(allVehicles);
			for (Vehicle v : allVehicles) {
				vehicleListModel.addElement(v.getID());
			}
			if (!allVehicles.isEmpty()) {
				vehicleList.setSelectionInterval(0, allVehicles.size() - 1);
			}

			selectedVehicles.clear();
			vehicleListModel.clear();
		});

		buttonPanel.add(clearButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		// GroupBy Label über dem Dropdown
		groupByLabel = new Label("Group By:");
		groupByLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // linksbündig
		buttonPanel.add(groupByLabel);

		// GroupBy Dropdown
		groupByDropdown = new Dropdown<>(new String[] { "None", "Vehicle Type", "Color", "Speed", "Route" });
		groupByDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // nur eine Zeile hoch
		groupByDropdown.setAlignmentX(Component.CENTER_ALIGNMENT); // linksbündig
		groupByDropdown.addActionListener(e -> groupVehicles()); // groupVehicles aufrufen
		buttonPanel.add(groupByDropdown);

		// Abstand nach unten
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		listPanel.add(buttonPanel, BorderLayout.EAST);

		// Ganzes ListPanel zum Content Panel hinzufügen
		contentPanel.add(listPanel);

		// Stats Panel als ScrollPane
		statsScrollPane = new ScrollPane();
		statsScrollPane.setPreferredSize(new Dimension(300, 200));
		vehicleTextBoxes = new ArrayList<>();
		vehicleStaticLabels = new ArrayList<>();

		// Vehicle Type Label
		statsScrollPane.addItem(new Label("Vehicle Type"));
		Label typeLabel = new Label("-");
		typeLabel.setFont(typeLabel.getFont().deriveFont(Font.BOLD));
		statsScrollPane.addItem(typeLabel);
		vehicleStaticLabels.add(typeLabel);
		Panel statsWrapper = new Panel();
		statsWrapper.add(statsScrollPane);
		contentPanel.add(statsWrapper);

		// Color Label
		statsScrollPane.addItem(new Label("Color"));
		Label colorLabel = new Label("R:0 G:0 B:0");
		statsScrollPane.addItem(colorLabel);
		vehicleStaticLabels.add(colorLabel);

		// stats felder initialisieren

		// Speed Label
		statsScrollPane.addItem(new Label("Speed"));
		Label speedLabel = new Label("0.0");
		statsScrollPane.addItem(speedLabel);
		vehicleStaticLabels.add(speedLabel);

		// Max Speed Label
		statsScrollPane.addItem(new Label("Max Speed"));
		Label maxSpeedLabel = new Label("0.0");
		statsScrollPane.addItem(maxSpeedLabel);
		vehicleStaticLabels.add(maxSpeedLabel);

		// Acceleration Label
		statsScrollPane.addItem(new Label("Acceleration"));
		Label accelerationLabel = new Label("0.0");
		statsScrollPane.addItem(accelerationLabel);
		vehicleStaticLabels.add(accelerationLabel);

		// Distance Traveled Label
		statsScrollPane.addItem(new Label("Distance Traveled"));
		Label distanceLabel = new Label("0.0");
		statsScrollPane.addItem(distanceLabel);
		vehicleStaticLabels.add(distanceLabel);

		// Route Label
		statsScrollPane.addItem(new Label("Route"));
		Label routeLabel = new Label("n/a");
		statsScrollPane.addItem(routeLabel);
		vehicleStaticLabels.add(routeLabel);

		this.add(contentPanel, BorderLayout.CENTER);

		// bottom Panel mit change mode, current mode und instructions
		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

		changeModeButton = new Button("Change Mode");
		changeModeButton.addActionListener(e -> toggleMode());
		bottomPanel.add(changeModeButton);

		modeLabel = new Label("Current Mode: PAN");
		bottomPanel.add(modeLabel);

		instructionLabel = new Label("Click 'Change Mode' to enter Select Mode.");
		bottomPanel.add(instructionLabel);

		this.add(bottomPanel, BorderLayout.SOUTH);

		// MouseListener für Map Clicks
		mapPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentMode != Mode.SelectMODE)
					return;

				Point2D world = screenToWorld(e.getX(), e.getY());
				Vehicle nearest = findNearestVehicle(world.getX(), world.getY(), 20.0);
				if (nearest != null)
					addVehicleToInspect(nearest);
			}
		});

		// update Speed every 500ms
		speedUpdateTimer = new Timer(500, e -> updateLiveSpeedOnly());
		speedUpdateTimer.start();

	}

	private void toggleMode() {
		if (currentMode == Mode.PanMODE) {
			currentMode = Mode.SelectMODE;
			modeLabel.setText("Current Mode: SELECT");
			instructionLabel.setText("Click a vehicle on the map to inspect it.");
		} else {
			currentMode = Mode.PanMODE;
			modeLabel.setText("Current Mode: PAN");
			instructionLabel.setText("Drag the map.");
		}
	}

	private Point2D screenToWorld(int sx, int sy) {
		double wx = (sx + Settings.config.OFFSET.getX()) / Settings.config.SCALE;
		double wy = -(sy + Settings.config.OFFSET.getY()) / Settings.config.SCALE;
		return new Point2D.Double(wx, wy);
	}

	private Vehicle findNearestVehicle(double x, double y, double maxDistance) {
		Vehicle nearest = null;
		double bestDist = Double.MAX_VALUE;

		// Benutze statische Methode getVehicles()
		for (Vehicle v : VehicleController.getVehicles()) {
			double dx = v.getPosition().getX() - x;
			double dy = v.getPosition().getY() - y;
			double dist = Math.sqrt(dx * dx + dy * dy);

			if (dist < bestDist) {
				bestDist = dist;
				nearest = v;
			}
		}

		if (bestDist > maxDistance)
			return null;
		return nearest;
	}

	private void addVehicleToInspect(Vehicle v) {
		// Prüfen Fahrzeug mit derselben ID schon in der Liste ist
		// stream anschauen
		boolean alreadyAdded = selectedVehicles.stream().anyMatch(vehicle -> vehicle.getID().equals(v.getID()));

		if (!alreadyAdded) {
			selectedVehicles.add(v);
			vehicleListModel.addElement(v.getID());
		}
	}

	// jedes mal wenn man auf ein selected vehicle geht
	private void updateStatsFields(int vehicleIndex) {

		if (vehicleIndex < 0 || vehicleIndex >= selectedVehicles.size())
			return;
		Vehicle v = selectedVehicles.get(vehicleIndex);

		// Vehicle Type
		vehicleStaticLabels.get(0).setText(v.getType().getId());

		// Color
		Color c = v.getVehicleColor();
		if (c != null) {
			vehicleStaticLabels.get(1).setText("R:" + c.getRed() + " G:" + c.getGreen() + " B:" + c.getBlue());
		}

		// Speed (Initialwert, danach live vom Timer)
		vehicleStaticLabels.get(2).setText(String.format("%.2f", v.getSpeed()));

		// Max Speed
		vehicleStaticLabels.get(3).setText(String.format("%.2f", v.getMaxSpeed()));

		// Acceleration
		vehicleStaticLabels.get(4).setText(String.format("%.2f", v.getAcceleration()));

		// Distance
		vehicleStaticLabels.get(5).setText(String.format("%.2f", v.getDistance()));

		// Route
		vehicleStaticLabels.get(6).setText(String.join(" -> ", v.getRoute()));

	}

	// für group by methode
	private void groupVehicles() {
		String criteria = (String) groupByDropdown.getSelectedItem();

		// Bei None keine Sortierung und keine Header
		if (criteria.equals("None")) {
			vehicleListModel.clear();
			for (Vehicle v : selectedVehicles) {
				vehicleListModel.addElement(v.getID());
			}
			return;
		}

		// Ansonsten sortieren und Header einfügen
		switch (criteria) {
		case "Vehicle Type":
			selectedVehicles.sort((v1, v2) -> v1.getType().getId().compareTo(v2.getType().getId()));
			break;

		case "Color":
			selectedVehicles.sort((v1, v2) -> {
				Color c1 = v1.getVehicleColor();
				Color c2 = v2.getVehicleColor();
				return Integer.compare(c1.getRGB(), c2.getRGB());
			});
			break;

		case "Speed":
			selectedVehicles.sort((v1, v2) -> Double.compare(v1.getSpeed(), v2.getSpeed()));
			break;

		case "Route":
			selectedVehicles.sort((v1, v2) -> {
				String r1 = String.join("->", v1.getRoute());
				String r2 = String.join("->", v2.getRoute());
				return r1.compareTo(r2);
			});
			break;
		}

		// Refresh und Kategorien(Header) anzeigen
		vehicleListModel.clear();
		String lastGroup = "";

		for (Vehicle v : selectedVehicles) {
			String currentGroup = "";

			switch (criteria) {
			case "Vehicle Type":
				currentGroup = v.getType().getId();
				break;
			case "Color":
				currentGroup = "RGB: " + v.getVehicleColor().getRGB();
				break;
			case "Speed":
				currentGroup = String.format("%.1f km/h", v.getSpeed());
				break;
			case "Route":
				currentGroup = String.join(" -> ", v.getRoute());
				break;
			}

			// Bei neuem Block Überschrift einfügen
			if (!currentGroup.equals(lastGroup)) {
				vehicleListModel.addElement("--- " + currentGroup + " ---");
				lastGroup = currentGroup;
			}

			vehicleListModel.addElement(v.getID());
		}
	}

	// Methode um herauszufinden welche Zeilen die Header sind
	// für Vehicle list
	private int getVehicleIndexFromListIndex(int listIndex) {
		int count = -1;
		for (int i = 0; i <= listIndex; i++) {
			if (!vehicleListModel.get(i).startsWith("---")) {
				count++;
			}
		}
		return count;
	}

	private void updateLiveSpeedOnly() {
		int listIndex = vehicleList.getSelectedIndex();
		if (listIndex == -1)
			return;

		String value = vehicleListModel.get(listIndex);
		if (value.startsWith("---"))
			return;

		int vehicleIndex = getVehicleIndexFromListIndex(listIndex);
		if (vehicleIndex < 0 || vehicleIndex >= selectedVehicles.size())
			return;

		Vehicle v = selectedVehicles.get(vehicleIndex);

		try {
			double liveSpeed = vehicleController.getSpeed(v.getID());
			vehicleStaticLabels.get(2).setText(String.format("%.2f", liveSpeed));

		} catch (Exception ex) {
		}
	}

}
