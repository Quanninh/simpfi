package com.simpfi.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.simpfi.util.Point;

public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private Graphics2D g2D;

	private double scale = 1;
	private Point topLeftPos = new Point();

	public MapPanel() {

	}

	@Override
	public void paint(Graphics g) {
		// Clear
		super.paintComponent(g);
		g2D = (Graphics2D) g;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Point getTopLeftPos() {
		return topLeftPos;
	}

	public void setTopLeftPos(Point topLeftPos) {
		this.topLeftPos = topLeftPos;
	}

}
