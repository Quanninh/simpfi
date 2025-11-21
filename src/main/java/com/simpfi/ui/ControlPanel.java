package com.simpfi.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.plaf.RootPaneUI;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.ui.TextBox.SettingsType;

public class ControlPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public ControlPanel() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		this.setBackground(Color.RED);

		TextBox scaleTB = new TextBox(true, SettingsType.SCALE, Constants.DEFAULT_SCALE);
		TextBox offsetXTB = new TextBox(true, SettingsType.OFFSET_X, -800);
		TextBox offsetYTB = new TextBox(true, SettingsType.OFFSET_Y, -200);

		this.add(scaleTB);
		this.add(offsetXTB);
		this.add(offsetYTB);
		
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();

		KeyStroke zoomInKey = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK); // Only for US keyboard
		KeyStroke zoomOutKey = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK);

		inputMap.put(zoomInKey, "zoomIn");
		inputMap.put(zoomOutKey, "zoomOut");

		actionMap.put("zoomIn", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Settings.modifyScale(0.1);
				System.out.println("Action work");
			}
		});

		actionMap.put("zoomOut", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Settings.modifyScale(-0.1);
				System.out.print("Action work");
			}
		});
		
		Button buttonIncreasingScale = new Button("+");
		buttonIncreasingScale.addActionListener(e -> {
			Settings.modifyScale(0.1);
			scaleTB.setText("" + Settings.SETTINGS_SCALE);
		});

		this.add(buttonIncreasingScale);

		Button buttonDecreasingScale = new Button("-");
		buttonDecreasingScale.addActionListener(e -> {
			Settings.modifyScale(-0.1);
			scaleTB.setText("" + Settings.SETTINGS_SCALE);
		});

		this.add(buttonDecreasingScale);

		Panel moveMapPanel = new Panel();
		moveMapPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		Button buttonMoveUp = new Button("↑");
		buttonMoveUp.addActionListener(e -> {
			Settings.modifyOffsetY(10);
			offsetYTB.setText("" + Settings.SETTINGS_OFFSET.getY());
		});
		gbc.gridx = 1;
		gbc.gridy = 0;
		moveMapPanel.add(buttonMoveUp, gbc);

		Button buttonMoveDown = new Button("↓");
		buttonMoveDown.addActionListener(e -> {
			Settings.modifyOffsetY(-10);
			offsetYTB.setText("" + Settings.SETTINGS_OFFSET.getY());
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		moveMapPanel.add(buttonMoveDown, gbc);

		Button buttonMoveLeft = new Button("←");
		buttonMoveLeft.addActionListener(e -> {
			Settings.modifyOffsetX(10);
			offsetXTB.setText("" + Settings.SETTINGS_OFFSET.getX());
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		moveMapPanel.add(buttonMoveLeft, gbc);

		Button buttonMoveRight = new Button("→");
		buttonMoveRight.addActionListener(e -> {
			Settings.modifyOffsetX(-10);
			offsetXTB.setText("" + Settings.SETTINGS_OFFSET.getX());
		});
		gbc.gridx = 2;
		gbc.gridy = 1;
		moveMapPanel.add(buttonMoveRight, gbc);

		this.add(moveMapPanel);
	}

}
