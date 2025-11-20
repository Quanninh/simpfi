package com.simpfi.ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;

public class TextBox extends JTextField {

	public enum SettingsType {
		SCALE, OFFSET_X, OFFSET_Y,
	}

	private static final long serialVersionUID = 1L;

	private double defaultValue;
	private Boolean mustBeDouble;
	private SettingsType type;

	public TextBox(Boolean mustBeDouble, SettingsType type,
		double defaultValue) {
		this.mustBeDouble = mustBeDouble;
		this.defaultValue = defaultValue;
		this.type = type;
		this.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				onTextChange();
			}

			public void removeUpdate(DocumentEvent e) {
				onTextChange();
			}

			public void insertUpdate(DocumentEvent e) {
				onTextChange();
			}

		});

		this.setText("" + defaultValue);
		this.setColumns(16);
	}

	public void onTextChange() {
		if (!mustBeDouble) {
			return;
		}

		double value = 1;
		try {
			value = Double.parseDouble(this.getText());
		} catch (Exception e) {
			value = defaultValue;
		}

		if (type == SettingsType.SCALE) {
			Settings.changeScale(value);
		}

		if (type == SettingsType.OFFSET_X) {
			Settings.changeOffsetX(value);
		}

		if (type == SettingsType.OFFSET_Y) {
			Settings.changeOffsetY(value);
		}
	}

}
