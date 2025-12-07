package com.simpfi.ui;

import java.text.DecimalFormat;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;

/**
 * Custom TextBox class that inherits {@link javax.swing.JTextField}. The class
 * is created to mainly provide three types of textboxes: scale, coordinate x
 * and y.
 */
public class TextBox extends JTextField {

	/**
	 * The Enum SettingsType.
	 */
	public enum SettingsType {
		/** Scale. */
		SCALE,
		/** Offset x. */
		OFFSET_X,
		/** Offset y. */
		OFFSET_Y,
		/** Duration */
		DURATION,
		/** State */
		STATE
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The default value for the textbox. */
	private double defaultValue;

	/** Whether the values entered must be a double. */
	private Boolean mustBeDouble;

	/** The settings type. */
	private SettingsType type;

	/**
	 * Constructor for TextBox: In addition to attributes assignment, it: Adds a
	 * document listener to update changes when users interact with values in
	 * Textboxes. Sets the inside text to defaultValue and defined value to column
	 * width.
	 * 
	 * @param mustBeDouble states wheter users enter double values or not.
	 * @param type         specifies the type of the textbox(SCALE, OFFSET_X,
	 *                     OFFSET_Y).
	 * @param defaultValue initilizes the value displayed in the text box.
	 */
	public TextBox(Boolean mustBeDouble, SettingsType type, double defaultValue) {
		this.mustBeDouble = mustBeDouble;
		this.defaultValue = defaultValue;
		this.type = type;

		this.setFont(Constants.DEFAULT_FONT);

		this.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateSettings();
			}

			public void removeUpdate(DocumentEvent e) {
				updateSettings();
			}

			public void insertUpdate(DocumentEvent e) {
				updateSettings();
			}

		});

		this.setText(valueTextBox(defaultValue));
		this.setColumns(8);
	}

	/**
	 * Make changes to the required values (SETTINGS_SCALE, SETTINGS_OFFSET) if
	 * mustBeDouble is set to true.
	 */
	public void updateSettings() {
		if (!mustBeDouble) {
			return;
		}

		double value;
		try {
			value = Double.parseDouble(this.getText());
		} catch (Exception e) {
			value = defaultValue;
		}

		// Depending on the settings type, different values are changed
		switch (type) {
		case SCALE:
			Settings.config.changeScale(value);
			break;
		case OFFSET_X:
			Settings.config.changeOffsetX(value);
			break;
		case OFFSET_Y:
			Settings.config.changeOffsetY(value);
			break;
		case DURATION:
			Settings.config.changeDuration(value);
			break;
		default:
			System.out.println("Unexpected TextBox");
			break;
		}
	}

	/**
	 * Converts a double value into a formatted string using a pattern: shows max 6
	 * digits after the decimal point.
	 *
	 * @param value the double value to be formatted
	 * @return string representing the formatted value
	 */
	public String valueTextBox(double value) {
		DecimalFormat df = new DecimalFormat("0.######");
		return df.format(value);
	}
}
