package com.simpfi.ui;

import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.TextBox.SettingsType;

/**
 * Custom ControlPanel class that inherits {@link com.simpfi.ui.Panel}.
 */
public class ControlPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private SumoConnectionManager conn;

	/**
	 * Constructor used to create the control panel displayed in the user
	 * interface. Run the program for visualization. The control panel has three
	 * main sets of components:
	 * 
	 * 1. Three textboxes correspond to scale, coordinate x and y.
	 * 
	 * 2. Buttons and keyboard shortcuts to zoom in, zoom out.
	 * 
	 * 3. Buttons and keyboard shortcuts to move in four directions: Left,
	 * Right, Up, Down.
	 * 
	 * <b>Note</b>: May update in the future.
	 */
	public ControlPanel(SumoConnectionManager conn) {

		this.conn = conn;

		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		TextBox scaleTB = new TextBox(true, SettingsType.SCALE,
			Constants.DEFAULT_SCALE);
		TextBox offsetXTB = new TextBox(true, SettingsType.OFFSET_X, -800);
		TextBox offsetYTB = new TextBox(true, SettingsType.OFFSET_Y, -200);

		this.add(scaleTB);
		this.add(offsetXTB);
		this.add(offsetYTB);

		initializeMapControl(scaleTB, offsetXTB, offsetYTB);

		// Create button for adding vehicle

		List<VehicleType> allVehicles = Settings.getVehicleTypes();

		String[] vehicleIDs = new String[allVehicles.size()];
		for (int i = 0; i < allVehicles.size(); i++) {
			vehicleIDs[i] = allVehicles.get(i).getId();
		}

		List<Route> allRoutes = Settings.getRoutes();

		String[] routeIds = new String[allRoutes.size()];
		for (int i = 0; i < allRoutes.size(); i++) {
			routeIds[i] = allRoutes.get(i).getId();
		}

		JComboBox<String> cb = new JComboBox<String>(vehicleIDs);
		JComboBox<String> cb2 = new JComboBox<String>(routeIds);

		this.add(new Label("Vehicle Type:"));
		this.add(cb);
		this.add(new Label("Route:"));
		this.add(cb2);

		System.out.println(cb.getSelectedItem());

		Button addingVehice = new Button("Adding vehicle");

		VehicleController vehicleControll = new VehicleController(conn);

		addingVehice.addActionListener(e -> {
			try {
				String userChoiceVehicleType = cb.getSelectedItem().toString();
				String userChoiceRoute = cb2.getSelectedItem().toString();
				String vehicleIds = Settings.generateVehicleIDs();
				vehicleControll.addVehicle(vehicleIds, userChoiceRoute,
					userChoiceVehicleType);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		this.add(addingVehice);

	}

	public void initializeMapControl(TextBox scaleTB, TextBox offsetXTB,
		TextBox offsetYTB) {
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();

		// Only for US keyboard
		KeyStroke zoomInKey = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
			InputEvent.CTRL_DOWN_MASK);
		KeyStroke zoomOutKey = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
			InputEvent.CTRL_DOWN_MASK);
		KeyStroke moveUpKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		KeyStroke moveDownKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
		KeyStroke moveRightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
		KeyStroke moveLeftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);

		inputMap.put(zoomInKey, "zoomIn");
		inputMap.put(zoomOutKey, "zoomOut");
		inputMap.put(moveUpKey, "moveUp");
		inputMap.put(moveDownKey, "moveDown");
		inputMap.put(moveLeftKey, "moveLeft");
		inputMap.put(moveRightKey, "moveRight");

		actionMap.put("zoomIn", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.modifyScale(0.1);
				scaleTB.setText(scaleTB.ValueTextBox(Settings.SETTINGS_SCALE));
			}
		});

		actionMap.put("zoomOut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.modifyScale(-0.1);
				scaleTB.setText(scaleTB.ValueTextBox(Settings.SETTINGS_SCALE));
			}
		});

		actionMap.put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.modifyOffsetY(-10);
				offsetYTB.setText(
					offsetYTB.ValueTextBox(Settings.SETTINGS_OFFSET.getY()));
			}

		});

		actionMap.put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.modifyOffsetY(10);
				offsetYTB.setText(
					offsetYTB.ValueTextBox(Settings.SETTINGS_OFFSET.getY()));
			}
		});

		actionMap.put("moveRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.modifyOffsetX(10);
				offsetXTB.setText(
					offsetXTB.ValueTextBox(Settings.SETTINGS_OFFSET.getX()));
			}
		});

		actionMap.put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.modifyOffsetX(-10);
				offsetXTB.setText(
					offsetXTB.ValueTextBox(Settings.SETTINGS_OFFSET.getX()));
			}
		});
	}
}
