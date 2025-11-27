package com.simpfi.ui;

import javax.swing.JButton;

import com.simpfi.config.Constants;

/**
 * Creates the Button class which inherits {@link javax.swing.JButton} to better
 * customise buttons in the User Interface.
 */
public class Button extends JButton {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor to set the text, defined background color and font in a Button.
	 * 
	 * @param text the text set to display in the button.
	 */
	public Button(String text) {
		this.setText(text);
		this.setFont(Constants.DEFAULT_FONT);
	};

}
