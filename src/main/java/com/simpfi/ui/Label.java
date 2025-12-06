package com.simpfi.ui;

import java.awt.Dimension;

import javax.swing.JLabel;

import com.simpfi.config.Constants;

/**
 * Configures a label on the UI. This class inherits {@link javax.swing.JPanel}.
 */

public class Label extends JLabel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new label.
	 *
	 * @param string the string
	 */
	public Label(String string) {
		this.setText(string);
		this.setFont(Constants.FONT);

		this.setMaximumSize(new Dimension(Short.MAX_VALUE, this.getPreferredSize().height));
	}

}
