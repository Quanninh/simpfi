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
	private static double valueX = Settings.SETTINGS_OFFSET.getX();
	private static double valueY = Settings.SETTINGS_OFFSET.getY();
	
	
	public Button(String text) {
		this.setText(text);
		this.setBackground(new Color(251, 232, 237));
		this.setFont(new Font("Arial", Font.PLAIN, 18));	
	};
	

	public void increasingScale() {
		valueScale += 0.1;
		Settings.SETTINGS_SCALE = valueScale;
	}
	
	public void decreasingScale()
	{
		valueScale -= 0.1;
		Settings.SETTINGS_SCALE = valueScale;
	}
	
	public void moveUp()
	{
		valueY += 100;
		Settings.SETTINGS_OFFSET.setY(valueY);
	}
	
	public void moveDown()
	{
		valueY -= 100;
		Settings.SETTINGS_OFFSET.setY(valueY);
	}
	
	public void moveRight()
	{
		valueX -= 100;
		Settings.SETTINGS_OFFSET.setX(valueX);
	}
	
	public void moveLeft()
	{
		valueX += 100;
		Settings.SETTINGS_OFFSET.setX(valueX);
	}
	
	public double getValueScale()
	{
		return valueScale;
	}
	
	public double getValueX()
	{
		return valueX;
	}
	
	public double getValueY()
	{
		return valueY;
	}

}
