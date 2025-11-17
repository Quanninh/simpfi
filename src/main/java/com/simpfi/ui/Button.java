package com.simpfi.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class Button extends JButton {

	private static final long serialVersionUID = 1L;

	public Button(String text) {
		this.setText(text);
		this.setBackground(new Color(251, 232, 237));
		this.setFont(new Font("Arial", Font.PLAIN, 18));
	}

}
