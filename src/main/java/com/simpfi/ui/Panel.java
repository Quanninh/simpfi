package com.simpfi.ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Custom Panel class that inherits {@link javax.swing.JPanel}.
 */
public class Panel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new panel.
	 */
	public Panel() {
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

}
