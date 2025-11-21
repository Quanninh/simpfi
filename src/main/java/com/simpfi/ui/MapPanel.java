package com.simpfi.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.Vehicle;
import com.simpfi.util.Point;
import com.simpfi.util.reader.NetworkXMLReader;
import java.util.Map;

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
		g2D.setStroke(new BasicStroke(Constants.STROKE_SIZE));

		scale = Settings.SETTINGS_SCALE;
		topLeftPos = Settings.SETTINGS_OFFSET;
		// System.out.println("Current Scale: " + topLeftPos.getX() + " " +
		// topLeftPos.getY());

		NetworkXMLReader networkXmlReader = null;
		List<Edge> edges = new ArrayList<>();
		List<Junction> junctions = new ArrayList<>();

		try {
			networkXmlReader = new NetworkXMLReader(Constants.SUMO_NETWORK);
			junctions = networkXmlReader.parseJunction();
			edges = networkXmlReader.parseEdge(junctions);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		System.out.println("Edges:");
//		System.out.println(edges.toString());
//		System.out.println("Junctions:");
//		System.out.println(junctions.toString());
		for (Edge e : edges) {
			drawObject(g2D, e);
		}
		for (Junction j : junctions) {
			drawObject(g2D, j);
		}

		// System.out.println("Drawing Complete");
	}

	private void drawObject(Graphics2D g, Vehicle v) {
		// Draw real-world vehicle shapes
	private static final Map<String, double[]> vehicle_dimension = Map.of(
		"private", new double[]{1.8, 4.5},
		"truck", new double[]{2.5, 12.0},
		"bus", new double[]{2.5, 12.0},
		"motorcycle", new double[]{0.8, 2.2},
		"emergency", new double[]{1.8, 4.5}
	);
	private void drawObject(Graphics2D g, Vehicle v) {
		if (v == null){
			return;
		}
		Point pos = translateCoords(new Point(v.getPosition()[0], v.getPosition()[1]));
		double[] dims = vehicle_dimension.getOrDefault(v.getType(), new double[]{1.8, 4.5});
		int width = (int)(dims[0] * scale);
		int length = (int)(dims[1] * scale);

		Color color;
		switch(v.getType()){
			case "truck":
				color = Color.GRAY;
				break;
			case "bus":
				color = Color.YELLOW;
				break;
			case "motorcycle":
				color = Color.MAGENTA;
				break;
			case "emergency":
				color = Color.RED;
				break;
			default:
				color = Color.GREEN;
				break;
		}
		g.setColor(color);

		double angle = 0;
		try {
			angle = v.getAngle();
		}catch (Exception e){

		}
		g.rotate(Math.toRadians(-angle), pos.getX(), pos.getY());
		g.fillRect((int) pos.getX() - width/2, (int) pos.getY() - length/2, width, length);

		g.setColor(color);
		g.drawRect((int) pos.getX() - width/2, (int) pos.getY() - length/2, width, length);

		// Reset rotation
		g.rotate(Math.toRadians(angle), pos.getX(), pos.getY());
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

			drawLine(g, p1, p2, Color.BLACK);

			// System.out.println("Drawing Lane: " + l.getLaneId());
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
			// System.out.println("Drawing Junction: " + j.getId());
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
