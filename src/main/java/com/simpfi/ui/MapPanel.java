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
		List<Junction> junctions = new ArrayList<>();
		try {
			junctions = xmlReader.parseJunction(Constants.NETWORK);
			edges = xmlReader.parseEdge(Constants.NETWORK, junctions);
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

	private void drawObject(Graphics2D g, Edge e) {
		for (int i = 0; i < e.getLanesSize(); i++) {
			drawObject(g, e.getLanes()[i]);
		}
	}

	private void drawObject(Graphics2D g, Lane l) {
		// if (l.getLaneId().charAt(0) != ':') return;
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
			System.out.println("Drawing Lane: " + l.getLaneId());
		}
	}

	private void drawObject(Graphics2D g, Junction j) {
		// if (l.getLaneId().charAt(0) != ':') return;
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
			
			g.setColor(Color.RED);
			g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
				(int) p2.getY());
			g.setColor(Color.BLACK);
			System.out.println("Drawing Junction: " + j.getId());
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
