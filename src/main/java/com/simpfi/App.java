package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.TrafficLightController;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Button;
import com.simpfi.ui.Frame;
import com.simpfi.ui.MapPanel;
import com.simpfi.ui.Panel;
import com.simpfi.ui.TextBox;
import com.simpfi.ui.TextBox.SettingsType;

public class App {
	public static void main(String[] args) throws Exception {
		MapPanel mapPanel = generateUI();
		SumoConnectionManager sim = establishConnection();

		while (true) {
			//retrieveData(sim);
			mapPanel.repaint();
		}
	}

	private static SumoConnectionManager establishConnection() throws Exception {
		SumoConnectionManager sim = new SumoConnectionManager(Constants.SUMO_CONFIG);
		return sim;
	}

	private static void retrieveData(SumoConnectionManager sim) throws Exception {
		double stepLen = 0.1;
		long stepMs = (long) (stepLen * 1000);
		VehicleController vehicleController = new VehicleController(sim);
		TrafficLightController trafficLightController = new TrafficLightController(sim);

		try {
			long next = System.currentTimeMillis();
			for (int k = 0; k < 600; k++) {

				sim.doStep(); // <-- now works

				// double time = ((Double) sim.getConnection()
				// .do_job_get(Simulation.getCurrentTime())) / 1000.0;

				for (String vid : vehicleController.getAllVehicleIDs()) {
					double speed = vehicleController.getSpeed(vid);
					String edge = vehicleController.getRoadID(vid);
					// System.out.printf("t=%.1fs id=%s v=%.2f m/s edge=%s%n",
					// time, vid, speed, edge);
					System.out.printf("id=%s v=%.2f m/s edge=%s%n", vid, speed, edge);
				}

				next += stepMs;
				// long sleep = next - System.currentTimeMillis();
				// if (sleep > 0) Thread.sleep(sleep);
				Thread.sleep(100);
			}

		} finally {
			sim.close();
		}
	}

	private static MapPanel generateUI() {
		Frame myFrame = new Frame();

		Panel controlPanel = new Panel();
		Panel infoPanel = new Panel();
		MapPanel mapPanel = new MapPanel();

		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
//		controlPanel.setLayout(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.insets = new Insets(10, 0, 0, 0);
//		gbc.anchor = GridBagConstraints.WEST;
//		gbc.fill = GridBagConstraints.NONE;


		myFrame.add(controlPanel, BorderLayout.NORTH);
		myFrame.add(infoPanel, BorderLayout.EAST);
		myFrame.add(mapPanel, BorderLayout.CENTER);

		controlPanel.setBackground(Color.RED);
		infoPanel.setBackground(Color.GREEN);
		mapPanel.setBackground(Color.WHITE);

		TextBox scaleTB = new TextBox(true, SettingsType.SCALE, Constants.DEFAULT_SCALE);
		TextBox offsetXTB = new TextBox(true, SettingsType.OFFSET_X, -800);
		TextBox offsetYTB = new TextBox(true, SettingsType.OFFSET_Y, -200);

//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridx = 1;
//		gbc.gridy = 0;
//		controlPanel.add(scaleTB, gbc);
		controlPanel.add(scaleTB);
		controlPanel.add(offsetXTB);
		controlPanel.add(offsetYTB);

		// Implement button for increasing the button
		Button ButtonIncreasingScale = new Button("+");
		ButtonIncreasingScale.addActionListener(e -> {
			Settings.modifyScale(0.1);
			scaleTB.setText("" + Settings.SETTINGS_SCALE);
		});
		
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridx = 2;
//		controlPanel.add(ButtonIncreasingScale, gbc);
		controlPanel.add(ButtonIncreasingScale);

		Button ButtonDecreasingScale = new Button("-");
		ButtonDecreasingScale.addActionListener(e -> {
			Settings.modifyScale(-0.1);
			scaleTB.setText("" + Settings.SETTINGS_SCALE);
		});
		
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridx = 0;
//		controlPanel.add(ButtonDecreasingScale, gbc);	
		controlPanel.add(ButtonDecreasingScale);
		
		Panel moveMapPanel = new Panel();
		moveMapPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		Button ButtonMoveUp = new Button("↑");
		ButtonMoveUp.addActionListener(e -> {
			Settings.modifyOffsetY(10);
			offsetYTB.setText("" + Settings.SETTINGS_OFFSET.getY());
		});
		gbc.gridx = 1;
		gbc.gridy = 0;
		moveMapPanel.add(ButtonMoveUp, gbc);

		Button ButtonMoveDown = new Button("↓");
		ButtonMoveDown.addActionListener(e -> {
			Settings.modifyOffsetY(-10);
			offsetYTB.setText("" + Settings.SETTINGS_OFFSET.getY());
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		moveMapPanel.add(ButtonMoveDown, gbc);
		
		Button ButtonMoveLeft = new Button("←");
		ButtonMoveLeft.addActionListener(e -> {
			Settings.modifyOffsetX(10);
			offsetXTB.setText("" + Settings.SETTINGS_OFFSET.getX());
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		moveMapPanel.add(ButtonMoveLeft, gbc);
		
		Button ButtonMoveRight = new Button("→");
		ButtonMoveRight.addActionListener(e -> {
			Settings.modifyOffsetX(-10);
			offsetXTB.setText("" + Settings.SETTINGS_OFFSET.getX());
		});
		gbc.gridx = 2;
		gbc.gridy = 1;
		moveMapPanel.add(ButtonMoveRight, gbc);
		
		controlPanel.add(moveMapPanel);

		
		// ALWAYS PUT THIS AT THE END
		myFrame.setVisible(true);

		return mapPanel;
	}
}
