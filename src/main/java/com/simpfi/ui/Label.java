package com.simpfi.ui;

import java.awt.Dimension;
import javax.swing.JLabel;

import com.simpfi.config.Constants;

public class Label extends JLabel {

	private static final long serialVersionUID = 1L;

	public Label(String string) {
		this.setText(string);
		this.setFont(Constants.DEFAULT_FONT);
		
		this.setMaximumSize(new Dimension(Short.MAX_VALUE, this.getPreferredSize().height));
	}

}
