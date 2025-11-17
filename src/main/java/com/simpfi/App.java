package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import com.simpfi.config.Constants;
import com.simpfi.ui.Frame;
import com.simpfi.ui.MapPanel;
import com.simpfi.ui.Panel;
import com.simpfi.util.XMLReader;

public class App {
	public static void main(String[] args) throws Exception {
		Frame myFrame = new Frame();

		JPanel controlPanel = new Panel();
		JPanel infoPanel = new Panel();
		JPanel mapPanel = new MapPanel();

		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		myFrame.add(controlPanel, BorderLayout.NORTH);
		myFrame.add(infoPanel, BorderLayout.EAST);
		myFrame.add(mapPanel, BorderLayout.CENTER);

		controlPanel.setBackground(Color.RED);
		infoPanel.setBackground(Color.GREEN);
		mapPanel.setBackground(Color.WHITE);

		myFrame.setVisible(true);

		XMLReader xmlReader = new XMLReader();
		xmlReader.parse(Constants.NETWORK);
	}
}
