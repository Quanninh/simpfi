package com.simpfi.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public MapPanel() {
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g); // clears background
		Graphics2D g2D = (Graphics2D) g;

		g2D.setColor(Color.BLACK);
		g2D.drawLine(10, 10, 80, 80);
		g2D.drawRect(20, 20, 60, 40);
	}
	
}
