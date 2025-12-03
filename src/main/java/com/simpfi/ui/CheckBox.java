package com.simpfi.ui;

import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import com.simpfi.config.Constants;

/**
 * Custom CheckBox class that inherits {@link javax.swing.JCheckBox}.
 * Used to represent a checkbox in the map.
 */
public class CheckBox extends JCheckBox {

	private static final long serialVersionUID = 1L;

	// public CheckBox() {
	// 	super();
	// 	init();
	// }

	// public CheckBox(String text) {
	// 	super(text);
	// 	init();
	// }

	public CheckBox(String text, boolean selected) {
		super(text, selected);
		init();
	}

	private void init() {
		this.setFont(Constants.DEFAULT_FONT);
        this.setFocusable(false);
	}

	public void setChecked(boolean checked) {
		this.setSelected(checked);
	}

	public boolean isChecked() {
		return this.isSelected();
	}

	/**
	 * Convenience method to attach an {@link ItemListener} for state changes.
	 */
	public void onChange(ItemListener listener) {
		this.addItemListener(listener);
	}

}
