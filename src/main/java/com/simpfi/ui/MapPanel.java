package com.simpfi.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import com.simpfi.config.Constants;
import com.simpfi.object.Edge;
import com.simpfi.object.Lane;
import com.simpfi.util.Point;
import com.simpfi.util.XMLReader;

public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private double scale = 3;
	private Point topLeftPos = new Point(-800, -200);

	public MapPanel() {

	}

	@Override
	public void paint(Graphics g) {
		// Clear
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(2));

		XMLReader xmlReader = new XMLReader();

		List<Edge> edges = new ArrayList<>();
		try {
			edges = xmlReader.parse(Constants.NETWORK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Edge e : edges) {
			drawEdge(g2D, e);
		}
	}

	private void drawEdge(Graphics2D g, Edge e) {
		for (int i = 0; i < e.getLanesSize(); i++) {
			drawLane(g, e.getLanes()[i]);
		}
	}

	private void drawLane(Graphics2D g, Lane l) {
		Point[] shape = l.getShape();
		int size = l.getShapeSize();

		if (size < 2) {
			return;
		}

		for (int i = 0; i < size - 1; i++) {
			Point p1 = translateCoords(shape[i]);
			Point p2 = translateCoords(shape[i + 1]);
			g.setColor(Color.BLUE);
			g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
				(int) p2.getY());
			g.setColor(Color.BLACK);
		}
	}

	private Point translateCoords(Point before) {
		Point after = new Point();
		after.setX(before.getX() * scale - topLeftPos.getX());
		after.setY(before.getY() * scale * -1 - topLeftPos.getY());
		return after;
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
