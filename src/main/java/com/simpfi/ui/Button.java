package com.simpfi.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;


/**
 * Creates the Button class which inherits {@link javax.swing.JButton} to better
 * customise buttons in the User Interface.
 */
public class Button extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor to set the text, defined background color and font in a
	 * Button.
	 * 
	 * @param text the text set to display in the button.
	 */
	public Button(String text) {
		this.setText(text);
		this.setBackground(new Color(251, 232, 237));
		this.setFont(new Font("Arial", Font.PLAIN, 14));
	};

}
