package com.simpfi.ui;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import com.simpfi.config.Constants;

// TODO: Auto-generated Javadoc
/**
 * Implements the Dropdown component on the UI.
 * This class inherits {@link javax.swing.JPanel}.
 *
 * @param <E> the element type
 */
public class Dropdown<E> extends JComboBox<E> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dropdown.
	 *
	 * @param items the items
	 */
	public Dropdown(E[] items) {
		this.setModel(new DefaultComboBoxModel<E>(items));

		Dimension pref = this.getPreferredSize();
		pref.setSize(Short.MAX_VALUE, pref.getHeight());
		this.setMaximumSize(pref);

		this.setFont(Constants.DEFAULT_FONT);
	}

}
