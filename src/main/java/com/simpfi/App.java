package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import com.simpfi.config.Constants;
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
			retrieveData(sim);
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

		myFrame.add(controlPanel, BorderLayout.NORTH);
		myFrame.add(infoPanel, BorderLayout.EAST);
		myFrame.add(mapPanel, BorderLayout.CENTER);

		controlPanel.setBackground(Color.RED);
		infoPanel.setBackground(Color.GREEN);
		mapPanel.setBackground(Color.WHITE);

		TextBox scaleTB = new TextBox(true, SettingsType.SCALE,
			Constants.DEFAULT_SCALE);
		TextBox offsetXTB = new TextBox(true, SettingsType.OFFSET_X, -500);
		TextBox offsetYTB = new TextBox(true, SettingsType.OFFSET_Y, -200);

		controlPanel.add(scaleTB);
		controlPanel.add(offsetXTB);
		controlPanel.add(offsetYTB);
		
		// Implement button for increasing the button
		Button button1 = new Button("Increasing");
		button1.addActionListener(e -> button1.increasingScale());
		controlPanel.add(button1);
		scaleTB.setText(""+ button1.getValueScale());
		
		Button button2 = new Button("Decreasing");
		button2.addActionListener(e -> button2.decreasingScale());
		controlPanel.add(button2);
		scaleTB.setText("" + button2.getValueScale());
		
		

		// ALWAYS PUT THIS AT THE END
		myFrame.setVisible(true);

		return mapPanel;
	}
}
