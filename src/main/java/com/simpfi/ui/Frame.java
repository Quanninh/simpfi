package com.simpfi.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1120, 660);
		this.setTitle("Simpfi - Traffic Simulation");
		this.setLayout(new BorderLayout(10, 10));

		// Set Frame location to center
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) screenSize.getWidth() / 2 - this.getWidth() / 2,
			(int) screenSize.getHeight() / 2 - this.getHeight() / 2);

		setVisible(true);
	}

	public void Test_Button(ActionEvent evt) {
		System.out.println(evt.getActionCommand());
	}

	public void ShowInformation(ActionEvent evt) {

	}
}