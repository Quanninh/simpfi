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

//next:
//Button ""

public class InspectPanel extends Panel {

    enum Mode {
        PanMODE, SelectMODE
    }
    private JLabel modeLabel;
    private Mode currentMode = Mode.PanMODE;



	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}
