package com.simpfi.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.simpfi.config.Constants;

/**
 * Implements the Dropdown component on the UI. This class inherits
 * {@link javax.swing.JPanel}.
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

		this.setFont(Constants.FONT);
	}

	/**
	 * Creates a dropdown along with a label.
	 *
	 * @param label the label text
	 * @param items the items
	 * @return the dropdown
	 */
	public static Dropdown<String> createDropdownWithLabel(String label, String[] items, JComponent parent) {
		parent.add(new Label(label));

		Dropdown<String> dropdown = new Dropdown<String>(items);
		dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
		parent.add(dropdown);

		return dropdown;
	}

}
