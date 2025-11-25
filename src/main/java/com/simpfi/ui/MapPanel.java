package com.simpfi.ui;

import java.awt.geom.AffineTransform;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.Vehicle;
import com.simpfi.util.Point;

/**
 * Custom MapPanel class that inherits {@link com.simpfi.ui.Panel}. Used to draw
 * objects on the user interface such as vehicles, edges, lanes, etc.
 */
public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public MapPanel() {
	}

	/**
	 * Overrides paint method from {@link java.awt.Component}. Parses objects in
	 * the XML files and draw them on the panel. Graphics is replaced by
	 * Graphics2D for more advanced drawing features.
	 * 
	 * @param g where elements are drawn.
	 */
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(
			(float) (Constants.DEFAULT_STROKE_SIZE * Settings.SETTINGS_SCALE)));

		for (Edge e : Settings.getEdges()) {
			drawObject(g2D, e);
		}
		for (Junction j : Settings.getJunctions()) {
			drawObject(g2D, j);
		}

		for (TrafficLight tl : Settings.getTrafficLights()) {
			try {
				drawTrafficLight(g2D, tl);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		for (Vehicle v : Settings.getVehicles()) {
			try {
				drawObject(g2D, v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Used to draw a Vehicle on the User Interface.
	 * 
	 * @param g where the vehicle is drawn on.
	 * @param v the vehicle that is passed to the method.
	 */

	// Apply Function Overloading for drawObject to draw Vehicle, Edge, Lane,
	// and Junction

	// Draw real-world vehicle shapes

	private void drawObject(Graphics2D g, Vehicle v) {
		if (v == null || !v.getIsActive()) {
			return;
		}

		Point pos = translateCoords(v.getPosition());
		int width = (int) (v.getWidth() * Settings.SETTINGS_SCALE
			* Constants.VEHICLE_UPSCALE);
		int height = (int) (v.getHeight() * Settings.SETTINGS_SCALE
			* Constants.VEHICLE_UPSCALE);

		g.setColor(getVehicleColor(v.getType()));

		double angle = v.getAngle();

		AffineTransform oldTransform = g.getTransform();
		g.rotate(Math.toRadians(angle), pos.getX(), pos.getY());

		g.fillRect((int) pos.getX() - width / 2, (int) pos.getY() - height / 2,
			width, height);

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(
			(float) (Constants.DEFAULT_STROKE_SIZE * Settings.SETTINGS_SCALE)));
		g.drawRect((int) pos.getX() - width / 2, (int) pos.getY() - height / 2,
			width, height);

		g.setTransform(oldTransform);
	}

	private Color getVehicleColor(String type) {
		return switch (type) {
		case "truck" -> Constants.TRUCK_COLOR;
		case "bus" -> Constants.BUS_COLOR;
		case "motorcycle" -> Constants.MOTORCYCLE_COLOR;
		case "emergency" -> Constants.EMERGENCY_COLOR;
		default -> Constants.DEFAULT_VEHICLE_COLOR;
		};
	}

	/**
	 * Used to draw an Edge on the User Interface.
	 * 
	 * @param g where the edge is drawn on.
	 * @param e the edge that is passed to the method.
	 */
	// Draw Edge
	private void drawObject(Graphics2D g, Edge e) {
		for (int i = 0; i < e.getLanesSize(); i++) {
			drawObject(g, e.getLanes()[i]);
		}
	}

	/**
	 * Used to draw a Lane on the User Interface.
	 * 
	 * @param g where the lane is drawn on.
	 * @param l the lane that is passed to the method.
	 */
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

			drawLine(g, p1, p2, Color.BLACK, Constants.LANE_STROKE_SIZE);
		}
	}

	// Draw TrafficLight, replaceTrafficLight to drawObject

	/**
	 * Used to draw a traffic light on the User Interface.
	 *
	 * @param g  where the traffic light is drawn on.
	 * @param tl traffic light object that is passed to the method.
	 */
	// Draw TrafficLight
	private void drawTrafficLight(Graphics2D g, TrafficLight tl) {
		String state = getTLState(tl);
		Lane[] lanes = tl.getLanes();

		for (int i = 0; i < lanes.length; i++) {
			Lane lane = lanes[i];
			char signal = state.charAt(i);
			Color color = getTrafficLightColor(signal);

			Point[] shape = lane.getShape();
			Point end = translateCoords(shape[1]);

			int radius = (int) (Constants.TRAFFIC_LIGHT_RADIUS
				* Settings.SETTINGS_SCALE);
			drawCircle(g, end, radius, color);
		}
	}

	private Color getTrafficLightColor(char signal) {
		return switch (signal) {
		case 'G' -> Color.GREEN;
		case 'g' -> Color.GREEN.darker();
		case 'y' -> Color.YELLOW;
		case 'r' -> Color.RED;
		default -> Color.BLACK;
		};
	}

	private String getTLState(TrafficLight tl) {
		// This one is Logic of the Phase So this logic should be place another
		// class :)))
		String defaultState = tl.getPhase()[0].getState();
		return Settings.getLiveTrafficLightStates()
			.getOrDefault(tl.getJunction().getId(), defaultState);
	}

	/**
	 * Used to draw a Junction on the User Interface.
	 * 
	 * @param g where the junction is drawn on.
	 * @param j the junction that is passed to the method.
	 */
	// Draw Junction
	private void drawObject(Graphics2D g, Junction j) {
		// We don't draw junctions anymore
		// Keep function for testing purposes and not breaking code

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

			drawLine(g, p1, p2, Color.PINK, Constants.JUNCTION_STROKE_SIZE);
		}
	}

	/**
	 * Used to draw a line on the User Interface.
	 * 
	 * @param g             where the line is drawn on.
	 * @param p1            start coordinate of the line.
	 * @param p2            end coordinate of the line.
	 * @param color         the color of the line.
	 * @param lineThickness the thickness of the line.
	 */
	private void drawLine(Graphics2D g, Point p1, Point p2, Color color,
		double lineThickness) {
		lineThickness *= Settings.SETTINGS_SCALE;

		AffineTransform oldTransform = g.getTransform();
		g.setColor(color);
		g = rotateToLine(g, p1, p2);
		g.fillRect((int) (p1.getX() - lineThickness / 2),
			(int) (p1.getY() - lineThickness / 2),
			(int) (Point.distance(p1, p2) + lineThickness),
			(int) lineThickness);
		// g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int)
		// p2.getY());

		g.setColor(Color.BLACK);
		g.setTransform(oldTransform);
	}

	private void drawCircle(Graphics2D g, Point center, int radius,
		Color color) {
		g.setColor(color);
		g.fillOval((int) center.getX() - radius, (int) center.getY() - radius,
			radius * 2, radius * 2);
		g.setColor(Color.BLACK);
	}

	private Graphics2D rotateToLine(Graphics2D g, Point p1, Point p2) {
		g.rotate(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()),
			p1.getX(), p1.getY());
		return g;
	}

	/**
	 * Used to convert the real-world coordinate to the graphics coordinate.
	 * 
	 * @param before the real-world coordinate.
	 * @return the graphics coordinate value.
	 */
	private Point translateCoords(Point before) {
		Point after = new Point();

		after.setX(before.getX() * Settings.SETTINGS_SCALE
			- Settings.SETTINGS_OFFSET.getX());

		// -1 here to flip the Y-axis, because Y increases downward in graphics
		// coordinates
		after.setY(before.getY() * Settings.SETTINGS_SCALE * -1
			- Settings.SETTINGS_OFFSET.getY());

		return after;
	}

}
