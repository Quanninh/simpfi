package com.simpfi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.simpfi.config.Constants;

public class Frame extends JFrame {

	
	private static final long serialVersionUID = 1L;

	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1120, 660);
		this.setTitle("Simpfi - Traffic Simulation");
		this.setLayout(new BorderLayout(10, 10));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) screenSize.getWidth() / 2 - this.getWidth() / 2,
				(int) screenSize.getHeight() / 2 - this.getHeight() / 2);

		JPanel panelTop = new JPanel();
		JPanel panelRight = new JPanel();
		JPanel panelCenter = new DrawingPanel();

		// Here we use FlowLayout for panel top
		panelTop.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		panelTop.setBackground(Color.RED);
		panelRight.setBackground(Color.GREEN);
		panelCenter.setBackground(Color.BLUE);

		panelTop.setPreferredSize(new Dimension(100, 100));
		panelRight.setPreferredSize(new Dimension(100, 100));
		panelCenter.setPreferredSize(new Dimension(100, 100));

		this.add(panelTop, BorderLayout.NORTH);
		this.add(panelRight, BorderLayout.EAST);
		this.add(panelCenter, BorderLayout.CENTER);

		// Add new button in panelTop
		JButton btn1 = new JButton("Btn 1");
		btn1.addActionListener(evt -> Test_Button(evt));
		btn1.setActionCommand(Constants.BUTTON1);
		panelTop.add(btn1);
		JButton btn2 = new JButton("Btn 2");
		btn2.addActionListener(evt -> Test_Button(evt));
		btn2.setActionCommand(Constants.BUTTON2);
		panelTop.add(btn2);
		JButton btn3 = new JButton("Btn 3");
		btn3.addActionListener(evt -> Test_Button(evt));
		btn3.setActionCommand(Constants.BUTTON3);
		panelTop.add(btn3);

		// Show the information in panelRight
		JLabel text1 = new JLabel("This is a text");
		panelRight.add(text1);

		setVisible(true);
	}

	public void Test_Button(ActionEvent evt) {
		System.out.println(evt.getActionCommand());
	}

	public void ShowInformation(ActionEvent evt) {

	}

//	public void paint(Graphics g)
//	{
//		Graphics2D g2D = (Graphics2D) g;
//		g2D.setColor(Color.BLACK);
//        g2D.drawLine(10, 10, 80, 80);
//        g2D.drawRect(20, 20, 60, 40);
//		this.setVisible(true);
//	}

	// Define your custom panel
	class DrawingPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			super.paintComponent(g); // clears background
			Graphics2D g2D = (Graphics2D) g;

			g2D.setColor(Color.BLACK);
			g2D.drawLine(10, 10, 80, 80);
			g2D.drawRect(20, 20, 60, 40);
		}
	}
}