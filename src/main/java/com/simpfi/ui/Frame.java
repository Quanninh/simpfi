package com.simpfi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1120, 660);
		this.setVisible(true);
		this.setTitle("Simpfi - Traffic Simulation");
		this.setLayout(new BorderLayout(10, 10));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) screenSize.getWidth() / 2 - this.getWidth() / 2,
				(int) screenSize.getHeight() / 2 - this.getHeight() / 2);

		JPanel panelTop = new JPanel();
		JPanel panelRight = new JPanel();
		JPanel panelCenter = new JPanel();

		panelTop.setBackground(Color.RED);
		panelRight.setBackground(Color.GREEN);
		panelCenter.setBackground(Color.BLUE);

		panelTop.setPreferredSize(new Dimension(100, 100));
		panelRight.setPreferredSize(new Dimension(100, 100));
		panelCenter.setPreferredSize(new Dimension(100, 100));

		this.add(panelTop, BorderLayout.NORTH);
		this.add(panelRight, BorderLayout.EAST);
		this.add(panelCenter, BorderLayout.CENTER);
	}

}
