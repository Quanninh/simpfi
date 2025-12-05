package com.simpfi.ui;

import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import com.simpfi.config.Constants;

/**
 * Custom CheckBox class that inherits {@link javax.swing.JCheckBox}.
 * Used to represent a checkbox in the map.
 */
public class CheckBox extends JCheckBox {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor used to create a check box with a given text and a checked state.
	 * 
	 * @param text the text goes with the check box
	 * @param selected boolean value whether the box is ticked or not
	 */
	public CheckBox(String text, boolean selected) {
		super(text, selected);
		init();
	}

	/**Some settings for the checkboxes to appear more attractively on the user interface.*/
	private void init() {
		this.setFont(Constants.DEFAULT_FONT);
        this.setFocusable(false);
	}


	/**
	 * Method used to set the state of the check box.
	 * 
	 * @param checked the state of the check box
	 */
	public void setChecked(boolean checked) {
		this.setSelected(checked);
	}


	/**
	 * Returns the state of the check box.
	 *
	 * @return {@code true} if the check box is selected; {@code false} otherwise
	 */
	public boolean isChecked() {
		return this.isSelected();
	}

	/**
	 * Method to attach an {@link ItemListener} for state changes.
	 */
	public void onChange(ItemListener listener) {
		this.addItemListener(listener);
	}

}
