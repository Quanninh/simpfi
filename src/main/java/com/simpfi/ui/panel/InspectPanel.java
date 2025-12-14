package com.simpfi.ui.panel;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.BorderLayout;

import com.simpfi.config.Settings;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;
import com.simpfi.ui.TextBox;
import com.simpfi.ui.Dropdown;
import com.simpfi.object.Vehicle;
import com.simpfi.ui.Label;


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


    enum Mode { PanMODE, SelectMODE }
    private Mode currentMode = Mode.PanMODE;

    public InspectPanel(SumoConnectionManager conn, MapPanel mapPanel) {
        this.sumoConnectionManager = conn;
        this.vehicleController = new VehicleController(conn);
        this.mapPanel = mapPanel;

        this.setLayout(new BorderLayout());
        //includes listpanel and statswrapper panel
        Panel contentPanel = new Panel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        // includes buttonpanel and scrollpane(lower box)
        Panel listPanel = new Panel();
        listPanel.setLayout(new BorderLayout());

        // vehicle list in ScrollPane(top)
        vehicleListModel = new DefaultListModel<>();
        vehicleList = new JList<>(vehicleListModel);
        vehicleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        vehicleList.addListSelectionListener(e -> {
            int listIndex = vehicleList.getSelectedIndex();
            if (listIndex == -1) return;

            String value = vehicleListModel.get(listIndex);

            // ignore header
            if (value.startsWith("---")) {
                vehicleList.clearSelection(); // deselect
                return;
            }

            // new index in selectedVehicles
            int vehicleIndex = getVehicleIndexFromListIndex(listIndex);
            updateStatsFields(vehicleIndex);
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.addItem(vehicleList);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel right for SelectAll, Clear and Dropdown
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        selectAllButton = new Button("Select All");
        selectAllButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectAllButton.addActionListener(e -> {

            selectedVehicles.clear();
            vehicleListModel.clear();

            List<Vehicle> allVehicles = VehicleController.getVehicles();
            selectedVehicles.addAll(allVehicles);

            // add IDs to JList
            for (Vehicle v : allVehicles) {
                vehicleListModel.addElement(v.getID());
            }

            if (!allVehicles.isEmpty()) {
                vehicleList.setSelectionInterval(0, allVehicles.size() - 1);
            }
        });

        buttonPanel.add(selectAllButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0,5)));

        clearButton = new Button("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.addActionListener(e -> {
            // simulate Select All
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
        buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

        groupByLabel = new Label("Group By:");
        groupByLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(groupByLabel);

// GroupBy Dropdown
        groupByDropdown = new Dropdown<>(new String[]{"None","Vehicle Type", "Color", "Speed", "Route"});
        groupByDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        groupByDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupByDropdown.addActionListener(e -> groupVehicles());
        buttonPanel.add(groupByDropdown);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        listPanel.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(listPanel);

// Stats Panel
        statsScrollPane = new ScrollPane();
        statsScrollPane.setPreferredSize(new Dimension(300, 200));
        vehicleTextBoxes = new ArrayList<>();
        vehicleStaticLabels = new ArrayList<>();

// Vehicle Type Label
        Label headerVehicleTypeLabel = new Label("Vehicle Type");
        headerVehicleTypeLabel.setFont(headerVehicleTypeLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerVehicleTypeLabel);

        Label typeLabel = new Label("-");
        statsScrollPane.addItem(typeLabel);
        vehicleStaticLabels.add(typeLabel);
        Panel statsWrapper = new Panel();
        statsWrapper.add(statsScrollPane);
        contentPanel.add(statsWrapper);

// Color Label
        Label headerColorLabel = new Label("Color");
        headerColorLabel.setFont(headerColorLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerColorLabel);

        Label colorLabel = new Label("R:0 G:0 B:0");
        statsScrollPane.addItem(colorLabel);
        vehicleStaticLabels.add(colorLabel);

// Speed Label
        Label headerSpeedLabel = new Label("Speed (km/h)");
        headerSpeedLabel.setFont(headerSpeedLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerSpeedLabel);

        Label speedLabel = new Label("0.0");
        statsScrollPane.addItem(speedLabel);
        vehicleStaticLabels.add(speedLabel);

// Max Speed Label
        Label headerMaxSpeedLabel = new Label("Max Speed (km/h)");
        headerMaxSpeedLabel.setFont(headerMaxSpeedLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerMaxSpeedLabel);

        Label maxSpeedLabel = new Label("0.0");
        statsScrollPane.addItem(maxSpeedLabel);
        vehicleStaticLabels.add(maxSpeedLabel);

// Acceleration Label
        Label headerAccelerationLabel = new Label("Acceleration (m/s^2)");
        headerAccelerationLabel.setFont(headerAccelerationLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerAccelerationLabel);

        Label accelerationLabel = new Label("0.0");
        statsScrollPane.addItem(accelerationLabel);
        vehicleStaticLabels.add(accelerationLabel);

// Distance Traveled Label
        Label headerDistanceLabel = new Label("Distance Traveled (meters)");
        headerDistanceLabel.setFont(headerDistanceLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerDistanceLabel);

        Label distanceLabel = new Label("0.0");
        statsScrollPane.addItem(distanceLabel);
        vehicleStaticLabels.add(distanceLabel);

// Route Label
        Label headerRouteLabel = new Label("Route");
        headerRouteLabel.setFont(headerRouteLabel.getFont().deriveFont(Font.BOLD));
        statsScrollPane.addItem(headerRouteLabel);

        Label routeLabel = new Label("n/a");
        statsScrollPane.addItem(routeLabel);
        vehicleStaticLabels.add(routeLabel);

        this.add(contentPanel, BorderLayout.CENTER);


        // bottom Panel wiht change mode, current mode und instructions
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

        //select mode feature realized by mouselistener
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentMode != Mode.SelectMODE) return;

                Point2D world = screenToWorld(e.getX(), e.getY());
                Vehicle nearest = findNearestVehicle(world.getX(), world.getY(), 20.0);
                if (nearest != null) addVehicleToInspect(nearest);
            }
        });

        //update Speed every 500ms
        speedUpdateTimer = new Timer(500, e -> updateLiveSpeedOnly());
        speedUpdateTimer.start();


    }

    /**
     * Toggles between Pan mode and Select mode for interacting with the map.
     * Updates the mode label and instructions accordingly.
     * Used by <code>changeModeButton</>.
     */
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

    /**
     * Toggles between Pan mode and Select mode for interacting with the map.
     * Updates the mode label and instructions accordingly.
     * @param sx screen x coordinate
     * @param sy screen y coordinate
     *
     * @return A point representing the world coordinates
     */
    private Point2D screenToWorld(int sx, int sy) {
        double wx = (sx + Settings.config.OFFSET.getX()) / Settings.config.SCALE;
        double wy = -(sy + Settings.config.OFFSET.getY()) / Settings.config.SCALE;
        return new Point2D.Double(wx, wy);
    }

    /**
     * Finds the nearest vehicle to the given world coordinates within a specified maximum distance.
     *
     * @param x world x coordinate previously calculated by {@link #screenToWorld(int, int)}
     * @param y world y coordinate previously calculated by {@link #screenToWorld(int, int)}
     * @param maxDistance Maximum search distance
     * @return The nearest Vehicle or null if no vehicle is within maxDistance
     */
    private Vehicle findNearestVehicle(double x, double y, double maxDistance) {
        Vehicle nearest = null;
        double bestDist = Double.MAX_VALUE;

        for (Vehicle v : VehicleController.getVehicles()) {
            double dx = v.getPosition().getX() - x;
            double dy = v.getPosition().getY() - y;
            double dist = Math.sqrt(dx*dx + dy*dy);

            if (dist < bestDist) {
                bestDist = dist;
                nearest = v;
            }
        }

        if (bestDist > maxDistance) return null;
        return nearest;
    }

    /**
     * Adds a vehicle to the list of selected vehicles for inspection.
     * Avoids adding duplicates based on vehicle ID.
     *
     * @param v The Vehicle to add
     */
    private void addVehicleToInspect(Vehicle v) {
        // if vehicle already in list
        boolean alreadyAdded = selectedVehicles.stream()
                .anyMatch(vehicle -> vehicle.getID().equals(v.getID()));

        if (!alreadyAdded) {
            selectedVehicles.add(v);
            vehicleListModel.addElement(v.getID());
        }
    }

    /**
     * Updates the stats panel with the current values of the selected vehicle.
     * Includes type, color, speed, max speed, acceleration, distance traveled, and route.
     *
     * @param vehicleIndex Index of the vehicle in the selectedVehicles list
     */
    private void updateStatsFields(int vehicleIndex) {

        if (vehicleIndex < 0 || vehicleIndex >= selectedVehicles.size()) return;
        Vehicle v = selectedVehicles.get(vehicleIndex);

        // Vehicle Type
        vehicleStaticLabels.get(0).setText(v.getType().getId());

        Color c = v.getVehicleColor();
        if (c != null) {
            vehicleStaticLabels.get(1).setText("R:" + c.getRed() + " G:" + c.getGreen() + " B:" + c.getBlue());
        }

        vehicleStaticLabels.get(2).setText(String.format("%.2f", v.getSpeed()));

        vehicleStaticLabels.get(3).setText(String.format("%.2f", v.getMaxSpeed()));

        vehicleStaticLabels.get(4).setText(String.format("%.2f", v.getAcceleration()));

        vehicleStaticLabels.get(5).setText(String.format("%.2f", v.getDistance()));

        vehicleStaticLabels.get(6).setText(String.join(" -> ", v.getRoute()));
    }

    /**
     * Grouping Method for sorting Vehicles by Vehicle Type, Color, Speed, Route or None.
     * Gets activated when an Item gets selected in the <code>groupByDropdown</>.
     * Also implements Headers for the corresponding group.
     *
     */
    private void groupVehicles() {
        String selected = (String) groupByDropdown.getSelectedItem();

        if (selected.equals("None")) {
            vehicleListModel.clear();
            for (Vehicle v : selectedVehicles) {
                vehicleListModel.addElement(v.getID());
            }
            return;
        }

        switch (selected) {
            case "Vehicle Type":
                selectedVehicles.sort((v1, v2) ->
                        v1.getType().getId().compareTo(v2.getType().getId()));
                break;

            case "Color":
                selectedVehicles.sort((v1, v2) -> {
                    Color c1 = v1.getVehicleColor();
                    Color c2 = v2.getVehicleColor();
                    return Integer.compare(c1.getRGB(), c2.getRGB());
                });
                break;

            case "Speed":
                selectedVehicles.sort((v1, v2) ->
                        Double.compare(v1.getSpeed(), v2.getSpeed()));
                break;

            case "Route":
                selectedVehicles.sort((v1, v2) -> {
                    String r1 = String.join("->", v1.getRoute());
                    String r2 = String.join("->", v2.getRoute());
                    return r1.compareTo(r2);
                });
                break;
        }
        // clear
        vehicleListModel.clear();
        String lastGroup = "";

        for (Vehicle v : selectedVehicles) {
            String currentGroup = "";

            switch (selected) {
                case "Vehicle Type": currentGroup = v.getType().getId(); break;
                case "Color": currentGroup = "RGB: " + v.getVehicleColor().getRGB(); break;
                case "Speed": currentGroup = String.format("%.1f km/h", v.getSpeed()); break;
                case "Route": currentGroup = String.join(" -> ", v.getRoute()); break;
            }

            // add header when new group
            if (!currentGroup.equals(lastGroup)) {
                vehicleListModel.addElement("--- " + currentGroup + " ---");
                lastGroup = currentGroup;
            }

            vehicleListModel.addElement(v.getID());
        }
    }

    /**
     * Translates the given listIndex indexes in the Vehilce-indexes because of headers.
     * Only increments count when object not header.
     *
     * @param listIndex JList index
     *
     * @return the index of the selectedVehicle List (count of elements without including headers)
     */
    private int getVehicleIndexFromListIndex(int listIndex) {
        int count = -1;
        for (int i = 0; i <= listIndex; i++) {
            if (!vehicleListModel.get(i).startsWith("---")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Updates the displayed speed of the selected vehicle in the stats (lower) panel.
     * Called by <code>speedUpdateTimer</> every 500ms.
     *
     */
    private void updateLiveSpeedOnly() {
        int listIndex = vehicleList.getSelectedIndex();
        if (listIndex == -1) return;

        String value = vehicleListModel.get(listIndex);
        if (value.startsWith("---")) return;

        int vehicleIndex = getVehicleIndexFromListIndex(listIndex);
        if (vehicleIndex < 0 || vehicleIndex >= selectedVehicles.size()) return;

        Vehicle v = selectedVehicles.get(vehicleIndex);

        try {
            double liveSpeed = vehicleController.getSpeed(v.getID());
            vehicleStaticLabels.get(2).setText(String.format("%.2f", liveSpeed));

        } catch (Exception ex) { }
    }

}
