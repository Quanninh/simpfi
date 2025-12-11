package com.simpfi.ui.panel;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import java.awt.BorderLayout;


import com.simpfi.config.Settings;
import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Panel;
import com.simpfi.object.Vehicle;


import com.simpfi.ui.panel.MapPanel;

/**
 * A UI panel used for inspecting vehicles. This class extends {@link Panel}.
 */
//UI:
// Button zum wechseln zwischen den Modi "Select Mode" und "Pan Mode" -> kurze instruction was die modi sind!
// user instruction und current mode change(kleines fenster ganz unten), wenn button gelickt wird
// instr -> sie sind nun im select modus, klicken sie auf das fahrzeug,das Sie inspecten möchten.
// Zum verlassen des Modus " " klicken Sie erneut auf change Mode Button


//LOGIK:
//auf Punkt auf der map klicken -> programm bezieht von SUMO alle fahrzeugdaten und berechnet, wo das
//näheste vehikel vom geklickten punkt aus gesehen ist.
//
//WICHTIG: Man soll MEHRERE verhicles uswählen können, die dann in einer liste aufgeführt werden
//choose vehicleS -> user stats ändern lassen -> confirm choices

// Button zum Modus changen zw SELECT MODE und PAN MODE
//
//
// BUTTON für SELECT ALL
// "group by" dropdown mit | vehicle type | color | (speed)
// fahzeuge werden nach dem ersten char ihres type sortiert



//Stats:
// ID
// Coordinates, + Lane, edge, ... ??
// Speed
// ------------------
// color
// destination

//WICHTIG!
// Immer code so schreiben, dass er executable ist
//Javax swing


public class InspectPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private VehicleController vehicleController;
    private SumoConnectionManager sumoConnectionManager;
    private MapPanel mapPanel; // Referenz auf MapPanel

    // Aktuell ausgewählte Fahrzeuge
    private List<Vehicle> selectedVehicles = new ArrayList<>();

    // UI
    private JTextField speedField;
    private JTextField maxSpeedField;
    private JTextField accelField;

    private JLabel modeLabel;
    private JLabel instructionLabel;
    private DefaultListModel<String> vehicleListModel;
    private JList<String> vehicleList;
    private JButton confirmButton;
    private JButton changeModeButton;

    enum Mode { PanMODE, SelectMODE }
    private Mode currentMode = Mode.PanMODE;

    public InspectPanel(SumoConnectionManager conn, MapPanel mapPanel) {
        this.sumoConnectionManager = conn;
        this.vehicleController = new VehicleController(conn);
        this.mapPanel = mapPanel;

        this.setLayout(new BorderLayout());

        // ======= CONTENT-BEREICH =======
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Fahrzeugliste
        vehicleListModel = new DefaultListModel<>();
        vehicleList = new JList<>(vehicleListModel);
        vehicleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleList.addListSelectionListener(e -> updateStatsFields());
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        contentPanel.add(scrollPane);

        // Stats-Felder
        JPanel statsPanel = new JPanel(new GridLayout(3,2));
        statsPanel.add(new JLabel("Speed:"));
        speedField = new JTextField();
        statsPanel.add(speedField);

        statsPanel.add(new JLabel("Max Speed:"));
        maxSpeedField = new JTextField();
        statsPanel.add(maxSpeedField);

        statsPanel.add(new JLabel("Acceleration:"));
        accelField = new JTextField();
        statsPanel.add(accelField);

        contentPanel.add(statsPanel);

        // Confirm Button
        confirmButton = new JButton("Confirm Changes");
        confirmButton.addActionListener(e -> confirmStats());
        contentPanel.add(confirmButton);

        this.add(contentPanel, BorderLayout.CENTER);

        // ======= BOTTOM-BEREICH =======
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
        // Prüfen, ob ein Fahrzeug mit derselben ID bereits in der Liste ist
        boolean alreadyAdded = selectedVehicles.stream()
                .anyMatch(vehicle -> vehicle.getID().equals(v.getID()));

        if (!alreadyAdded) {
            selectedVehicles.add(v);
            vehicleListModel.addElement(v.getID());
        }
    }

    private void updateStatsFields() {
        int index = vehicleList.getSelectedIndex();
        if (index == -1) return;
        Vehicle v = selectedVehicles.get(index);

        speedField.setText(String.valueOf(v.getSpeed()));
        // Optional: maxSpeedField und accelField, falls aus SUMO lesbar
    }

    private void confirmStats() {
        int index = vehicleList.getSelectedIndex();
        if (index == -1) return;

        Vehicle v = selectedVehicles.get(index);
        String vehicleID = v.getID();

        try {
            double newSpeed = Double.parseDouble(speedField.getText());
            double newMaxSpeed = Double.parseDouble(maxSpeedField.getText());
            double newAccel = Double.parseDouble(accelField.getText());

            // Änderungen direkt in SUMO
            sumoConnectionManager.getConnection().do_job_set(
                    de.tudresden.sumo.cmd.Vehicle.setSpeed(vehicleID, newSpeed)
            );
            sumoConnectionManager.getConnection().do_job_set(
                    de.tudresden.sumo.cmd.Vehicle.setMaxSpeed(vehicleID, newMaxSpeed)
            );
            sumoConnectionManager.getConnection().do_job_set(
                    de.tudresden.sumo.cmd.Vehicle.setAccel(vehicleID, newAccel)
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to update vehicle in SUMO: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public Mode getCurrentMode() {
        return currentMode;
    }
}
