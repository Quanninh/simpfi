package com.simpfi.ui;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.function.Consumer;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.simpfi.config.Constants;

/**
 * Custom TextBox class that inherits {@link javax.swing.JTextField}. The class
 * is created to mainly provide three types of textboxes: scale, coordinate x
 * and y.
 */
public class TextBox extends JTextField {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The default value for the textbox. */
	private Double defaultValue;

	/** Whether the values entered must be a double. */
	private Boolean mustBeDouble;

	/** Whether the values entered must be positive. */
	private Boolean mustBePositive;

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
	public TextBox(double defaultValue, Boolean mustBeDouble, Boolean mustBePositive) {
		this.defaultValue = defaultValue;
		this.mustBeDouble = mustBeDouble;
		this.mustBePositive = mustBePositive;

		this.setFont(Constants.FONT);

		this.setText(valueTextBox(defaultValue));
		this.setColumns(8);

		Dimension preferedSize = this.getPreferredSize();
		preferedSize.setSize(Short.MAX_VALUE, preferedSize.getHeight());
		this.setMaximumSize(preferedSize);
	}

	private String getTextboxValue() {
		if (!mustBeDouble) {
			return getText();
		}

		Double value;
		try {
			value = Double.parseDouble(this.getText());
		} catch (Exception e) {
			value = defaultValue;
		}

		if (mustBePositive && value <= 0) {
			value = defaultValue;
		}

		return value.toString();
	}

	public void resetToDefault() {
		setText(defaultValue.toString());
	}

	public void attachListener(Consumer<String> onChange) {
		this.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				onChange.accept(getTextboxValue());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				onChange.accept(getTextboxValue());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				onChange.accept(getTextboxValue());
			}
		});
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
