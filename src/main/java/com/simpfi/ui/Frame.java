package com.simpfi.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Custom Frame class that inherits {@link javax.swing.JFrame}.
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor is customed to match our software's style and place the frame in
	 * the center.
	 */
	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1120, 660);
		this.setTitle("Simpfi - Traffic Simulation");
		this.setLayout(new BorderLayout(0, 0));

		// Set Frame location to center
		setLocationRelativeTo(null);

		setVisible(true);
	}

}