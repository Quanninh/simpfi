package com.simpfi.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.graph.Network;
import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.Phase;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.Vehicle;
import com.simpfi.util.Point;
import com.simpfi.util.reader.NetworkXMLReader;

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
		List<TrafficLight> trafficLights = new ArrayList<>();

		try {
			networkXmlReader = new NetworkXMLReader(Constants.SUMO_NETWORK);
			junctions = networkXmlReader.parseJunction();
			edges = networkXmlReader.parseEdge(junctions);
			trafficLights = networkXmlReader.parseTrafficLight(junctions, edges);
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

		for (TrafficLight tl : trafficLights) {
			try {
				drawTrafficLight(g2D, tl);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// System.out.println("Drawing Complete");
	}

	private void drawObject(Graphics2D g, Vehicle v) {

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

	// Draw TrafficLight
	private void drawTrafficLight(Graphics2D g, TrafficLight tl) {

		Junction junction = tl.getJunction();
		Phase currentPhase = tl.getPhase()[0];
		String state = currentPhase.getState();
//
//	    List<String> laneIds = junction.getIncomingLane();
//
//	    for (int i = 0; i < laneIds.size(); i++) {
//
//	        String laneId = laneIds.get(i);
//	        String edgeId = laneId.split("_")[0];
//	        
//	        Edge edge = 
//
//	        Lane lane = findLaneById(laneId);
//
//	        if (lane == null) {
//	            System.out.println("Warning: Lane not found: " + laneId);
//	            continue;
//	        }

		Lane[] lanes = tl.getLanes();

		for (int i = 0; i < lanes.length; i++) {
			Lane lane = lanes[i];
			Color color = Color.YELLOW;
			char signal = state.charAt(i);
			color = switch (signal) {
			case 'G' -> Color.GREEN;
			case 'g' -> Color.GREEN.darker();
			case 'y' -> Color.YELLOW;
			case 'r' -> Color.RED;
			default -> Color.BLACK;
			};

			Point[] shape = lane.getShape();
			Point end = translateCoords(shape[1]);

			int radius = 6;

			g.setColor(color);
			g.drawOval((int) end.getX() - radius, (int) end.getY() - radius, radius * 2, radius * 2);
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
		g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
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
