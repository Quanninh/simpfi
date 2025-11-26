package com.simpfi.ui;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import com.simpfi.config.Constants;

/**
 * Implements the Dropdown component on the UI.
 * This class inherits {@link javax.swing.JPanel}.
 */
public class Dropdown<E> extends JComboBox<E> {

	private static final long serialVersionUID = 1L;

	public Dropdown(E[] items) {
		this.setModel(new DefaultComboBoxModel<E>(items));

		Dimension pref = this.getPreferredSize();
		pref.setSize(Short.MAX_VALUE, pref.getHeight());
		this.setMaximumSize(pref);

		this.setFont(Constants.DEFAULT_FONT);
	}

}
