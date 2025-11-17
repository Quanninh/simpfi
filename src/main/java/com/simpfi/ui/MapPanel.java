package com.simpfi.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.simpfi.config.Constants;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.util.Point;
import com.simpfi.util.XMLReader;

public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private double scale = 3;
	private Point topLeftPos = new Point(-800, -200);
	private float strokeSize = 2;

	public MapPanel() {

	}

	@Override
	public void paint(Graphics g) {
		// Clear
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(strokeSize));

		XMLReader xmlReader = null;
		List<Edge> edges = new ArrayList<>();
		List<Junction> junctions = new ArrayList<>();

		try {
			xmlReader = new XMLReader(Constants.NETWORK);
			junctions = xmlReader.parseJunction();
			edges = xmlReader.parseEdge(junctions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Edge e : edges) {
			drawObject(g2D, e);
		}
		for (Junction j : junctions) {
			drawObject(g2D, j);
		}
	}

	// Draw Edge
	private void drawObject(Graphics2D g, Edge e) {
		for (int i = 0; i < e.getLanesSize(); i++) {
			drawObject(g, e.getLanes()[i]);
		}
	}

	// Draw Lane
	private void drawObject(Graphics2D g, Lane l) {
		Point[] shape = l.getShape();
		int size = l.getShapeSize();

		if (size < 2) {
			return;
		}

		for (int i = 0; i < size - 1; i++) {
			Point p1 = translateCoords(shape[i]);
			Point p2 = translateCoords(shape[i + 1]);
			
			drawLine(g, p1, p2, Color.BLUE);
			
			System.out.println("Drawing Lane: " + l.getLaneId());
		}
	}

	// Draw Junction
	private void drawObject(Graphics2D g, Junction j) {
		Point[] shape = j.getShape();
		int size = j.getShapeSize();

		if (size < 2) {
			return;
		}

		for (int i = 0; i < size; i++) {
			Point p1 = translateCoords(shape[i]);
			Point p2;
			if (i < size - 1) {
				p2 = translateCoords(shape[i + 1]);
			} else {
				p2 = translateCoords(shape[0]);
			}

			drawLine(g, p1, p2, Color.RED);
			System.out.println("Drawing Junction: " + j.getId());
		}
	}
	
	private void drawLine(Graphics2D g, Point p1, Point p2, Color color) {
		g.setColor(color);
		g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
			(int) p2.getY());
		g.setColor(Color.BLACK);
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
