package com.simpfi.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
		
		Button ButtonIncreasingScale = new Button("+");
		ButtonIncreasingScale.addActionListener(e -> {
			Settings.modifyScale(0.1);
			scaleTB.setText("" + Settings.SETTINGS_SCALE);
		});
		
		this.add(ButtonIncreasingScale);

		Button ButtonDecreasingScale = new Button("-");
		ButtonDecreasingScale.addActionListener(e -> {
			Settings.modifyScale(-0.1);
			scaleTB.setText("" + Settings.SETTINGS_SCALE);
		});
		
		this.add(ButtonDecreasingScale);
		
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
		
		this.add(moveMapPanel);
	}
	
	

}
