package com.simpfi.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.simpfi.config.Settings;

public class Button extends JButton {

	private static final long serialVersionUID = 1L;
	private static double valueScale = Settings.SETTINGS_SCALE;
	public Button(String text) {
		this.setText(text);
		this.setBackground(new Color(251, 232, 237));
		this.setFont(new Font("Arial", Font.PLAIN, 18));	
	};
	

	public void increasingScale() {
		valueScale += 10;
		Settings.SETTINGS_SCALE = valueScale;
	}
	
	public void decreasingScale()
	{
		valueScale -= 10;
		Settings.SETTINGS_SCALE = valueScale;
	}
	
	public double getValueScale()
	{
		return valueScale;
	}

}
