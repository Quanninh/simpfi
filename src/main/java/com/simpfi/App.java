package com.simpfi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import com.simpfi.config.Constants;
import com.simpfi.ui.Frame;
import com.simpfi.ui.MapPanel;
import com.simpfi.ui.Panel;
import com.simpfi.ui.TextBox;
import com.simpfi.ui.TextBox.SettingsType;
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

		TextBox scaleTB = new TextBox(true, SettingsType.SCALE, Constants.DEFAULT_SCALE);
		TextBox offsetXTB = new TextBox(true,SettingsType.OFFSET_X, -800);
		TextBox offsetYTB = new TextBox(true,SettingsType.OFFSET_Y, -200);
		
		controlPanel.add(scaleTB);
		controlPanel.add(offsetXTB);
		controlPanel.add(offsetYTB);
		
		// ALWAYS PUT THIS AT THE END
		myFrame.setVisible(true);
		
		while (true) {
			mapPanel.repaint();
		}
	}
}
