package com.simpfi.ui.panel;

import java.util.List;

import javax.swing.*;
import javax.swing.JTextField;

import com.simpfi.config.Settings;
import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Dropdown;
import com.simpfi.ui.Panel;

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

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private VehicleController vehicleController;

    // Aktuell inspiziertes Fahrzeug
    private String currentVehicleId;

    private JTextField speedField;
    private JTextField maxSpeedField;
    private JTextField accelField;

    private JLabel modeLabel;
    private JLabel instructionLabel;

    enum Mode {
        PanMODE, SelectMODE
    }
    private Mode currentMode = Mode.PanMODE;

    public InspectPanel(SumoConnectionManager conn) {

        this.vehicleController = new VehicleController(conn);

         this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        modeLabel = new JLabel("Current Mode: PAN");
        this.add(modeLabel);

        instructionLabel = new JLabel("Click 'Change Mode' to enter Select Mode.");
        this.add(instructionLabel);

        // Mode-Change Button
        JButton changeModeButton = new JButton("Change Mode");
        changeModeButton.addActionListener(e -> toggleMode());
        this.add(changeModeButton);

        // Platz für spätere Input-Felder (Speed, Accel usw.)
        // speedField = new JTextField(10);
        // this.add(speedField);

    }
    private void toggleMode() {
        if (currentMode == Mode.PanMODE) {
            currentMode = Mode.SelectMODE;
            modeLabel.setText("Current Mode: SELECT");
            instructionLabel.setText("Select Mode: Click a vehicle on the map to inspect it.");
        } else {
            currentMode = Mode.PanMODE;
            modeLabel.setText("Current Mode: PAN");
            instructionLabel.setText("Pan Mode: Drag the map freely.");
        }

        // Wenn du später MapPanel hast:
        // mapPanel.setMode(currentMode);
    }


    public Mode getCurrentMode() {
        return currentMode;
    }
}
