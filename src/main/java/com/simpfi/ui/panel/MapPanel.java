package com.simpfi.ui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.Route;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.Vehicle;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Mouse;
import com.simpfi.ui.Panel;
import com.simpfi.util.GraphicsSettings;
import com.simpfi.util.Point;

/**
 * Custom MapPanel class that inherits {@link com.simpfi.ui.Panel}. Used to draw
 * objects on the user interface such as vehicles, edges, lanes, etc.
 */
public class MapPanel extends Panel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The default stroke. */
	private final BasicStroke defaultStroke = new BasicStroke(
		(float) (Constants.DEFAULT_STROKE_SIZE * Settings.config.SCALE), BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_ROUND);

	/**
	 * Instantiates a new map panel.
	 */
	public MapPanel() {
		initializeMapControl();
	}

	/**
	 * Overrides paintComponent method from {@link java.awt.Component}. Gets the
	 * information from {@code Settings.network} and draw them on the panel.
	 * Graphics is replaced by Graphics2D for more advanced drawing features.
	 * 
	 * @param g the {@link Graphics}
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(defaultStroke);

		for (Edge e : Settings.network.getEdges()) {
			drawObject(g2D, e, Color.BLACK);
		}

		for (Junction j : Settings.network.getJunctions()) {
			drawObject(g2D, j);
		}

		// Draw the highlighted Route in a different color
		Route highlightedRoute = Route.searchForRoute(Settings.config.HIGHLIGHTED_ROUTE, Settings.network.getRoutes());
		for (Edge e : highlightedRoute.getEdges()) {
			drawObject(g2D, e, Constants.HIGHLIGHTED_ROUTE_COLOR);
		}

		for (TrafficLight tl : Settings.network.getTrafficLights()) {
			try {
				drawObject(g2D, tl);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		for (Vehicle v : VehicleController.getVehicles()) {
			try {
				drawObject(g2D, v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Draws a {@link Vehicle} on the map.
	 * 
	 * @param g the {@link Graphics2D}
	 * @param v the {@link Vehicle}
	 */
	private void drawObject(Graphics2D g, Vehicle v) {
		// We don't draw inactive vehicles
		if (v == null || !v.getIsActive()) {
			return;
		}

		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);

		g.setColor(v.getVehicleColor());

		Point pos = translateCoords(v.getPosition());
		int width = (int) (v.getWidth() * Settings.config.SCALE * Constants.VEHICLE_UPSCALE);
		int height = (int) (v.getHeight() * Settings.config.SCALE * Constants.VEHICLE_UPSCALE);

		double angle = v.getAngle();
		g.rotate(Math.toRadians(angle), pos.getX(), pos.getY());

		g.fillRect((int) pos.getX() - width / 2, (int) pos.getY() - height / 2, width, height);

		// Draw the border of the vehicle
		// g.setColor(Constants.DEFAULT_COLOR);
		// g.drawRect((int) pos.getX() - width / 2, (int) pos.getY() - height / 2,
		// width, height);

		loadGraphicsSettings(g, oldSettings);
	}

	/**
	 * Draws an {@link Edge} on the map.
	 *
	 * @param g the {@link Graphics2D}
	 * @param e the edge
	 * @param c the color
	 */
	private void drawObject(Graphics2D g, Edge e, Color c) {
		Lane[] lanes = e.getLanes();
		int laneSize = e.getLanesSize();
		for (int i = 0; i < laneSize; i++) {
			drawObject(g, lanes[i], c);
		}

		if (laneSize <= 1) {
			return;
		}

		// Draw lane dividers
		drawLaneDividers(g, lanes, laneSize);
	}

	/**
	 * Draw lane dividers.
	 *
	 * @param g        the {@link Graphics2D}
	 * @param lanes    the lanes
	 * @param laneSize the lane size
	 */
	private void drawLaneDividers(Graphics2D g, Lane[] lanes, int laneSize) {
		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);

		/*
		 * same amount of line and no line
		 * 
		 * so it's like ----- ----- -----
		 * 
		 * if want to change -> multiply it by a value
		 */
		float dashLength = (float) (Constants.LANE_DIVIDER_DASH_LENGTH * Settings.config.SCALE);
		float[] dashPattern = { dashLength, dashLength };

		g.setStroke(new BasicStroke((float) (Constants.LANE_DIVIDER_STROKE_SIZE * Settings.config.SCALE),
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
		g.setColor(Constants.LANE_DIVIDER_COLOR);

		for (int i = 0; i < laneSize - 1; i++) {
			Lane lane1 = lanes[i];
			Lane lane2 = lanes[i + 1];

			Point[] shape1 = lane1.getShape();
			Point[] shape2 = lane2.getShape();

			int pointsCnt = Math.min(lane1.getShapeSize(), lane2.getShapeSize());

			for (int j = 0; j < pointsCnt - 1; j++) {
				Point p11 = translateCoords(shape1[j]);
				Point p12 = translateCoords(shape2[j]);
				Point p21 = translateCoords(shape1[j + 1]);
				Point p22 = translateCoords(shape2[j + 1]);
				Point from = p11.add(p12).scale(0.5);
				Point to = p21.add(p22).scale(0.5);
				g.drawLine((int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
			}
		}

		loadGraphicsSettings(g, oldSettings);
	}

	/**
	 * Draws a {@link Lane} on the map
	 *
	 * @param g the {@link Graphics2D}
	 * @param l the lane
	 * @param c the color
	 */
	private void drawObject(Graphics2D g, Lane l, Color c) {
		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);

		Point[] shape = l.getShape();
		int size = l.getShapeSize();

		if (size < 2) {
			return;
		}

		int[] xPoints = new int[size];
		int[] yPoints = new int[size];

		for (int i = 0; i < size; i++) {
			Point p = translateCoords(shape[i]);
			xPoints[i] = (int) p.getX();
			yPoints[i] = (int) p.getY();
		}

		float lineThickness = (float) (Constants.LANE_STROKE_SIZE * Settings.config.SCALE);

		g.setColor(c);
		g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		g.drawPolyline(xPoints, yPoints, size);

		loadGraphicsSettings(g, oldSettings);
	}

	/**
	 * Draws a {@link TrafficLight} on the map.
	 *
	 * @param g  the {@link Graphics2D}
	 * @param tl the traffic light
	 */
	private void drawObject(Graphics2D g, TrafficLight tl) {
		String state = tl.getTLState();
		List<Connection> connections = tl.getConnections();

		for (int i = 0; i < connections.size(); i++) {
			Connection connect = connections.get(i);

			char signal = state.charAt(i);
			Color color = TrafficLight.getTrafficLightColor(signal);

			Lane fromLane = connect.getFromLane();

			Point[] shape = fromLane.getShape();
			Point end = translateCoords(shape[shape.length - 1]);

			int radius = (int) (Constants.TRAFFIC_LIGHT_RADIUS * Settings.config.SCALE);
			drawCircle(g, end, radius, color);
		}
	}

	/**
	 * Draws a {@link Junction} on the map.
	 * 
	 * @param g the {@link Graphics2D}
	 * @param j the junction
	 */
	// Draw Junction
	private void drawObject(Graphics2D g, Junction j) {
		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);

		Point[] shape = j.getShape();
		int size = j.getShapeSize();

		if (size < 2) {
			return;
		}

		int[] xPoints = new int[size];
		int[] yPoints = new int[size];

		for (int i = 0; i < size; i++) {
			Point p = translateCoords(shape[i]);
			xPoints[i] = (int) p.getX();
			yPoints[i] = (int) p.getY();
		}

		g.setColor(Constants.JUNCTION_COLOR);
		g.setStroke(new BasicStroke((float) (Constants.JUNCTION_STROKE_SIZE * Settings.config.SCALE)));

		g.fillPolygon(xPoints, yPoints, size);
		g.drawPolygon(xPoints, yPoints, size);

		loadGraphicsSettings(g, oldSettings);
	}

	/**
	 * Draws a circle on the map.
	 *
	 * @param g      the {@link Graphics2D}
	 * @param center the center
	 * @param radius the radius
	 * @param color  the color
	 */
	private void drawCircle(Graphics2D g, Point center, int radius, Color color) {
		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);
		g.setColor(color);
		g.fillOval((int) center.getX() - radius, (int) center.getY() - radius, radius * 2, radius * 2);
		loadGraphicsSettings(g, oldSettings);
	}

	/**
	 * Converts the real-world coordinate to the map coordinate.
	 * 
	 * @param before the real-world coordinate
	 * @return the map coordinate
	 */
	private Point translateCoords(Point before) {
		Point after = new Point();

		// -1 here to flip the Y-axis, because Y increases downward in graphics
		// coordinates
		after.setX(before.getX() * Settings.config.SCALE - Settings.config.OFFSET.getX());
		after.setY(before.getY() * Settings.config.SCALE * -1 - Settings.config.OFFSET.getY());

		return after;
	}

	/**
	 * Save the current graphics settings.
	 *
	 * @param g the {@link Graphics2D}
	 * @return the graphics settings
	 */
	private GraphicsSettings saveCurrentGraphicsSettings(Graphics2D g) {
		return new GraphicsSettings(g.getColor(), g.getStroke(), g.getTransform());
	}

	/**
	 * Load the graphics settings.
	 *
	 * @param g        the {@link Graphics2D}
	 * @param settings the settings
	 */
	private void loadGraphicsSettings(Graphics2D g, GraphicsSettings settings) {
		g.setColor(settings.getColor());
		g.setStroke(settings.getStroke());
		g.setTransform(settings.getTransform());
	}

	/**
	 * Initialize map control.
	 */
	public void initializeMapControl() {
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();

		// Only for US keyboard
		KeyStroke zoomInKey = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK);
		KeyStroke zoomOutKey = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK);
		KeyStroke moveUpKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		KeyStroke moveDownKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
		KeyStroke moveRightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
		KeyStroke moveLeftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);

		inputMap.put(zoomInKey, "zoomIn");
		inputMap.put(zoomOutKey, "zoomOut");
		inputMap.put(moveUpKey, "moveUp");
		inputMap.put(moveDownKey, "moveDown");
		inputMap.put(moveLeftKey, "moveLeft");
		inputMap.put(moveRightKey, "moveRight");

		actionMap.put("zoomIn", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyScale(Constants.SCALE_STEP);
			}
		});

		actionMap.put("zoomOut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyScale(-Constants.SCALE_STEP);
			}
		});

		actionMap.put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetY(-Constants.OFFSET_STEP);
			}

		});

		actionMap.put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetY(Constants.OFFSET_STEP);
			}
		});

		actionMap.put("moveRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetX(Constants.OFFSET_STEP);
			}
		});

		actionMap.put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetX(-Constants.OFFSET_STEP);
			}
		});
		
		Mouse mouseAction = new Mouse();
		this.addMouseListener(mouseAction);
		this.addMouseMotionListener(mouseAction);
		this.addMouseWheelListener(mouseAction);
	}

}
