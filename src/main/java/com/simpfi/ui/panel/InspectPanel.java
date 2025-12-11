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
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Panel;
import com.simpfi.ui.ScrollPane;
import com.simpfi.ui.TextBox;
import com.simpfi.object.Vehicle;


/**
 * A UI panel used for inspecting vehicles. This class extends {@link Panel}.
 */
//Confirm changes doesnt work yet, because it only changes speed, which cant be changed due to predefined routes
//of vehicles

public class InspectPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private VehicleController vehicleController;
    private SumoConnectionManager sumoConnectionManager;
    private MapPanel mapPanel;

    private List<Vehicle> selectedVehicles = new ArrayList<>();

    private JLabel modeLabel;
    private JLabel instructionLabel;
    private JLabel groupByLabel;
    private DefaultListModel<String> vehicleListModel;
    private JList<String> vehicleList;
    private JButton confirmButton;
    private JButton changeModeButton;
    private JButton selectAllButton;
    private JButton clearButton;
    private JComboBox<String> groupByDropdown;
    private List<TextBox> vehicleTextBoxes;
    private ScrollPane statsScrollPane;
    private List<JLabel> vehicleStaticLabels;



    enum Mode { PanMODE, SelectMODE }
    private Mode currentMode = Mode.PanMODE;

    public InspectPanel(SumoConnectionManager conn, MapPanel mapPanel) {
        this.sumoConnectionManager = conn;
        this.vehicleController = new VehicleController(conn);
        this.mapPanel = mapPanel;

        this.setLayout(new BorderLayout());

        // content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Panel für Liste, Buttons und Dropdown
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

    // Fahrzeugliste in ScrollPane
        vehicleListModel = new DefaultListModel<>();
        vehicleList = new JList<>(vehicleListModel);
        vehicleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        vehicleList.addListSelectionListener(e -> {
            int listIndex = vehicleList.getSelectedIndex();
            if (listIndex == -1) return;

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

        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel rechts für SelectAll, Clear und Dropdown
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        selectAllButton = new JButton("Select All");
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
        buttonPanel.add(Box.createRigidArea(new Dimension(0,5)));

        clearButton = new JButton("Clear");
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

            //alles löschen
            selectedVehicles.clear();
            vehicleListModel.clear();
        });

        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));

// GroupBy Label über dem Dropdown
        groupByLabel = new JLabel("Group By:");
        groupByLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // linksbündig
        buttonPanel.add(groupByLabel);

// GroupBy Dropdown
        groupByDropdown = new JComboBox<>(new String[]{"None","Vehicle Type", "Color", "Speed"});
        groupByDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); // nur eine Zeile hoch
        groupByDropdown.setAlignmentX(Component.CENTER_ALIGNMENT); // linksbündig
        groupByDropdown.addActionListener(e -> groupVehicles());
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

// stats-felder initialisieren
        TextBox speedTextbox = createTextboxWithLabel("Speed", statsScrollPane, 0.0, true, true, "Speed of the vehicle");
        vehicleTextBoxes.add(speedTextbox);

// Max Speed Label
        statsScrollPane.addItem(new JLabel("Max Speed"));
        JLabel maxSpeedLabel = new JLabel("0.0");
        statsScrollPane.addItem(maxSpeedLabel);
        vehicleStaticLabels.add(maxSpeedLabel);

// Acceleration Label
        statsScrollPane.addItem(new JLabel("Acceleration"));
        JLabel accelerationLabel = new JLabel("0.0");
        statsScrollPane.addItem(accelerationLabel);
        vehicleStaticLabels.add(accelerationLabel);

// Distance Traveled Label
        statsScrollPane.addItem(new JLabel("Distance Traveled"));
        JLabel distanceLabel = new JLabel("0.0");
        statsScrollPane.addItem(distanceLabel);
        vehicleStaticLabels.add(distanceLabel);

// Route Label
        statsScrollPane.addItem(new JLabel("Route"));
        JLabel routeLabel = new JLabel("");
        statsScrollPane.addItem(routeLabel);
        vehicleStaticLabels.add(routeLabel);

// Color Label
        statsScrollPane.addItem(new JLabel("Color"));
        JLabel colorLabel = new JLabel("R:0 G:0 B:0");
        statsScrollPane.addItem(colorLabel);
        vehicleStaticLabels.add(colorLabel);

// Vehicle Type Label
        statsScrollPane.addItem(new JLabel("Vehicle Type"));
        JLabel typeLabel = new JLabel("");
        statsScrollPane.addItem(typeLabel);
        vehicleStaticLabels.add(typeLabel);
        JPanel statsWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsWrapper.add(statsScrollPane);
        contentPanel.add(statsWrapper);

// Confirm Button
        confirmButton = new JButton("Confirm Changes");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(e -> confirmStats());
        contentPanel.add(Box.createRigidArea(new Dimension(0,5)));
        contentPanel.add(confirmButton);

        this.add(contentPanel, BorderLayout.CENTER);


        // bottom Panel mit change mode, current mode und instructions
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        changeModeButton = new JButton("Change Mode");
        changeModeButton.addActionListener(e -> toggleMode());
        bottomPanel.add(changeModeButton);

        modeLabel = new JLabel("Current Mode: PAN");
        bottomPanel.add(modeLabel);

        instructionLabel = new JLabel("Click 'Change Mode' to enter Select Mode.");
        bottomPanel.add(instructionLabel);

        this.add(bottomPanel, BorderLayout.SOUTH);

        // MouseListener für Map Clicks
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentMode != Mode.SelectMODE) return;

                Point2D world = screenToWorld(e.getX(), e.getY());
                Vehicle nearest = findNearestVehicle(world.getX(), world.getY(), 20.0);
                if (nearest != null) addVehicleToInspect(nearest);
            }
        });
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
            double dist = Math.sqrt(dx*dx + dy*dy);

            if (dist < bestDist) {
                bestDist = dist;
                nearest = v;
            }
        }

        if (bestDist > maxDistance) return null;
        return nearest;
    }

    private void addVehicleToInspect(Vehicle v) {
        // Prüfen Fahrzeug mit derselben ID schon in der Liste ist
        boolean alreadyAdded = selectedVehicles.stream()
                .anyMatch(vehicle -> vehicle.getID().equals(v.getID()));

        if (!alreadyAdded) {
            selectedVehicles.add(v);
            vehicleListModel.addElement(v.getID());
        }
    }

    private void updateStatsFields(int vehicleIndex) {
        if (vehicleIndex < 0 || vehicleIndex >= selectedVehicles.size()) return;
        Vehicle v = selectedVehicles.get(vehicleIndex);

        // Speed (editierbar)
        vehicleTextBoxes.get(0).setText(String.format("%.2f", v.getSpeed()));

        // Max Speed
        vehicleStaticLabels.get(0).setText(String.format("%.2f", v.getMaxSpeed()));

        // Acceleration
        vehicleStaticLabels.get(1).setText(String.format("%.2f", v.getAcceleration()));

        // Distance Traveled
        vehicleStaticLabels.get(2).setText(String.format("%.2f", v.getDistance()));

        // Route
        List<String> edges = v.getRoute(); // Annahme: getRoute() liefert List<String>
        vehicleStaticLabels.get(3).setText(String.join(" -> ", edges));

        // Color
        Color c = v.getVehicleColor();
        if (c != null) {
            vehicleStaticLabels.get(4).setText("R:" + c.getRed() + " G:" + c.getGreen() + " B:" + c.getBlue());
        }

        // Vehicle Type
        vehicleStaticLabels.get(5).setText(v.getType().getId());
    }


    private void confirmStats() {
        int listIndex = vehicleList.getSelectedIndex();
        if (listIndex == -1) return;

        String value = vehicleListModel.get(listIndex);
        if (value.startsWith("---")) {
            JOptionPane.showMessageDialog(this, "Please select a valid vehicle, not a header.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int vehicleIndex = getVehicleIndexFromListIndex(listIndex);
        Vehicle v = selectedVehicles.get(vehicleIndex);
        String vehicleID = v.getID();

        try {
            double newSpeed = Double.parseDouble(vehicleTextBoxes.get(0).getText());

            // Speed set
            sumoConnectionManager.getConnection().do_job_set(
                    de.tudresden.sumo.cmd.Vehicle.setSpeed(vehicleID, newSpeed)
            );

            JOptionPane.showMessageDialog(this, "Vehicle speed updated successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to update vehicle in SUMO: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

//für group by methode
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
                selectedVehicles.sort((v1, v2) ->
                        v1.getType().toString().compareTo(v2.getType().toString()));
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
        }

        // Refresh und Kategorien(Header) anzeigen
        vehicleListModel.clear();
        String lastGroup = "";

        for (Vehicle v : selectedVehicles) {
            String currentGroup = "";

            switch (criteria) {
                case "Vehicle Type": currentGroup = v.getType().getId(); break;
                case "Color": currentGroup = "RGB: " + v.getVehicleColor().getRGB(); break;
                case "Speed": currentGroup = String.format("%.1f km/h", v.getSpeed()); break;
            }

            // Bei neuem Block  Überschrift einfügen
            if (!currentGroup.equals(lastGroup)) {
                vehicleListModel.addElement("--- " + currentGroup + " ---");
                lastGroup = currentGroup;
            }

            vehicleListModel.addElement(v.getID());
        }
    }


    private TextBox createTextboxWithLabel(String labelText, ScrollPane parent, double defaultValue,
                                           Boolean mustBeDouble, Boolean mustBePositive, String tooltip) {
        JLabel label = new JLabel(labelText);
        TextBox textbox = new TextBox(defaultValue, mustBeDouble, mustBePositive);
        textbox.setToolTipText(tooltip);

        parent.addItem(label);
        parent.addItem(textbox);

        return textbox;
    }

    //Methode um herauszufinden welche Zeilen die Header sind
    //für Vehicle list
    private int getVehicleIndexFromListIndex(int listIndex) {
        int count = -1;
        for (int i = 0; i <= listIndex; i++) {
            if (!vehicleListModel.get(i).startsWith("---")) {
                count++;
            }
        }
        return count;
    }


    public Mode getCurrentMode() {
        return currentMode;
    }
}
