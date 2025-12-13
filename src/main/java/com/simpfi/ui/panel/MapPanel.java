package com.simpfi.ui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.simpfi.config.Settings;
import com.simpfi.object.Connection;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.Road;
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

	/** Logger. */
	private static final Logger logger = Logger.getLogger(MapPanel.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The default stroke. */
	private final BasicStroke defaultStroke = new BasicStroke(
		(float) (Settings.config.NORMAL_STROKE_SIZE * Settings.config.SCALE), BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_ROUND);

	/** Cached static layer (edges, junctions) */
	private BufferedImage staticLayer = null;

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

		// Render static layer (edges, junctions) once and cache it
		if (Settings.config.getStaticLayerDirty()) {
			renderStaticLayer();
			Settings.config.setStaticLayerDirty(false);
		}

		// Draw cached static layer
		if (staticLayer != null) {
			g2D.drawImage(staticLayer, 0, 0, null);
		}

		// Draw the highlighted Route in a different color (if any)
		if (Settings.highlight.HIGHLIGHTED_ROUTE != null) {
			for (Edge e : Settings.highlight.HIGHLIGHTED_ROUTE.getEdges()) {
				drawObject(g2D, e, Settings.config.HIGHLIGHTED_ROUTE_COLOR);
			}
		}

		// Draw the highlighted Road (filter hover) in a different color (if any)
		if (Settings.highlight.HIGHLIGHTED_ROAD_FILTER != null) {
			for (Edge e : Settings.highlight.HIGHLIGHTED_ROAD_FILTER.getEdgesWithSameBaseName()) {
				drawObject(g2D, e, Settings.config.HIGHLIGHTED_ROAD_FILTER_COLOR);
			}
		}

		if (Settings.highlight.HIGHLIGHTED_CONNECTION != null) {
			drawObject(g2D, Settings.highlight.HIGHLIGHTED_CONNECTION.getFromLane(),
				Settings.config.HIGHLIGHTED_CONNECTION_COLOR);
			drawObject(g2D, Settings.highlight.HIGHLIGHTED_CONNECTION.getToLane(),
				Settings.config.HIGHLIGHTED_CONNECTION_COLOR);
		}

		if (Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT != null) {
			drawObject(g2D, Settings.highlight.HIGHLIGHTED_TRAFFIC_LIGHT.getJunction(),
				Settings.config.HIGHLIGHTED_TRAFFIC_LIGHT_COLOR);
		}

		for (TrafficLight tl : Settings.network.getTrafficLights()) {
			try {
				drawObject(g2D, tl);
			} catch (Exception e1) {
				logger.log(Level.SEVERE,
					String.format("Failed to draw the traffic light (%s) in Map Panel!", tl.toString()), e1);
			}
		}
		for (Vehicle v : VehicleController.getVehicles()) {
			try {
				drawObject(g2D, v);
			} catch (Exception e) {
				logger.log(Level.SEVERE, String.format("Failed to draw the vehicle (%s) in Map Panel!", v.toString()),
					e);
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

		// Implement Lazy Drawing: only vehicles within the view are drawn
		Point position = v.getPosition().fromWorldToMap();
		// We use getWidth() here only for approximate threshold
		// If the vehicle is long, consider changing it to length/2
		int size = (int) (v.getWidth() * Settings.config.SCALE * Settings.config.VEHICLE_UPSCALE);
		// Skip if vehicle is off-screen
		if (position.getX() < -size || position.getX() > getWidth() + size || position.getY() < -size
			|| position.getY() > getHeight() + size) {
			return;
		}

		// We don't draw vehicles whose type is filtered out
		if (v.getType() != null && !v.getType().getFilterFlag()) {
			return;
		}

		// We don't draw vehicles which run on unselected roads
		if (v.getRoadID() != null && v.getRoadID().charAt(1) != 'J') {
			Road road = Settings.network.getRoadFromEdge(v.getEdgeFromRoadID());
			if (road != null && !road.getFilterFlag()) {
				return;
			}
		}

		// We don't draw vehicles which are not with the filtered speed range
		if (v.getSpeed() < Settings.highlight.LOWER_BOUND_LIMIT
			|| v.getSpeed() > Settings.highlight.UPPER_BOUND_LIMIT) {
			return;
		}

		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);

		double lengthMultipler = 1.5;
		double narrowWidth = 0.8;
		Point pos = v.getPosition().fromWorldToMap();
		int width = (int) (v.getWidth() * lengthMultipler * Settings.config.SCALE * Settings.config.VEHICLE_UPSCALE);
		int height = (int) (v.getHeight() * narrowWidth * Settings.config.SCALE * Settings.config.VEHICLE_UPSCALE);

		int x = (int) pos.getX() - width / 2;
		int y = (int) pos.getY() - height / 2;

		int drawX = -width / 2;
		int drawY = -height;

		int light = height / 6;
		int headlightFrontY = drawY + height / 6;

		int bodyLeft = drawX;
		int bodyRight = drawX + width;
		int bodyTop = drawY;
		int bodyBottom = drawY + height;
		int frontOffset = (int) (height / 2.0);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke(defaultStroke);
		g2.translate(pos.getX(), pos.getY());
		g2.rotate(Math.toRadians(v.getAngle() - 90));
		g2.translate(0, height / 2);

		// Draw vehicle body
		g2.setColor(v.getVehicleColor());
		g2.fillRoundRect(drawX, drawY, width, height, 8, 8);

		// 4 Windows

		Color windowColor = new Color(30, 30, 30, 180);

		// local bounding box
		int BX = drawX;
		int BY = drawY;
		int BW = width;
		int BH = height;

		int cx = BX + BW / 2; // center X
		int cy = BY + BH / 2; // center Y

		// SIZE FACTORS
		int inward = (int) (BH * 0.18); // inward small edge length
		int outward = (int) (BH * 0.40); // outward wide edge length

		// increase factor for width
		double widthFactor = 3.0;

		// FRONT WINDOW
		int fShortW = (int) (inward * widthFactor);
		int fLongW = (int) (outward * widthFactor);
		int fTopY = BY + (int) (BH * 0.05);
		int fBotY = fTopY + (int) (BH * 0.30);

		Polygon poly = new Polygon();
		poly.addPoint(cx - fLongW / 2, fTopY); // wide top-left
		poly.addPoint(cx + fLongW / 2, fTopY); // wide top-right
		poly.addPoint(cx + fShortW / 2, fBotY); // short bottom-right (toward center)
		poly.addPoint(cx - fShortW / 2, fBotY); // short bottom-left
		g2.setColor(windowColor);
		g2.fillPolygon(poly);

		// REAR WINDOW
		int rBotY = BY + BH - (int) (BH * 0.05);
		int rTopY = rBotY - (int) (BH * 0.30);

		poly = new Polygon();
		poly.addPoint(cx - fShortW / 2, rTopY); // short inner top-left
		poly.addPoint(cx + fShortW / 2, rTopY); // short inner top-right
		poly.addPoint(cx + fLongW / 2, rBotY); // wide bottom-right
		poly.addPoint(cx - fLongW / 2, rBotY); // wide bottom-left
		g2.fillPolygon(poly);

		// LEFT WINDOW (short edge faces center → right side)
		int lLeftX = BX + (int) (BW * 0.02);
		int lRightX = lLeftX + (int) (BW * 0.32); // vertical thickness

		int lShortW = inward;
		int lLongW = outward;

		int lTopY = cy - (int) (BH * 0.14);
		int lBotY = cy + (int) (BH * 0.14);

		poly = new Polygon();
		poly.addPoint(lLeftX, cy - (lLongW / 2)); // wide top-left
		poly.addPoint(lLeftX, cy + (lLongW / 2)); // wide bottom-left
		poly.addPoint(lRightX, cy + (lShortW / 2)); // short inward edge bottom
		poly.addPoint(lRightX, cy - (lShortW / 2)); // short inward edge top
		g2.fillPolygon(poly);

		// RIGHT WINDOW (short edge faces center → left side)
		int rRightX = BX + BW - (int) (BW * 0.05);
		int rLeftX = rRightX - (int) (BW * 0.32);

		poly = new Polygon();
		poly.addPoint(rLeftX, cy - (lShortW / 2)); // short inward edge top
		poly.addPoint(rLeftX, cy + (lShortW / 2)); // short inward edge bottom
		poly.addPoint(rRightX, cy + (lLongW / 2)); // wide bottom-right
		poly.addPoint(rRightX, cy - (lLongW / 2)); // wide top-right
		g2.fillPolygon(poly);

		// Draw Head Lights
		int lightSize = (int) (height * 0.20);

		int headlightX = bodyRight - lightSize - 2;
		int headlightY1 = bodyTop + (int) (height * 0.20);
		int headlightY2 = bodyTop + (int) (height * 0.70);

		if (v.headlightsOn()) {
			g2.setColor(new Color(255, 255, 200));
			g2.fillOval(headlightX, headlightY1, lightSize, lightSize);
			g2.fillOval(headlightX, headlightY2, lightSize, lightSize);
		}

		// Brake lights
		int brakeX = bodyLeft + 2;
		int brakeY1 = headlightY1;
		int brakeY2 = headlightY2;

		g2.setColor(v.isBraking() ? new Color(255, 60, 60) : new Color(150, 0, 0));
		g2.fillOval(brakeX, brakeY1, lightSize, lightSize);
		g2.fillOval(brakeX, brakeY2, lightSize, lightSize);

		// Turn signals
		g2.setColor(new Color(255, 150, 0));

		if (v.isTurningLeft()) {
			g2.fillOval(brakeX, brakeY1, lightSize, lightSize);
			g2.fillOval(brakeX, brakeY2, lightSize, lightSize);
		}
		if (v.isTurningRight()) {
			g2.fillOval(headlightX, headlightY1, lightSize, lightSize);
			g2.fillOval(headlightX, headlightY2, lightSize, lightSize);
		}
		g2.dispose();
		loadGraphicsSettings(g, oldSettings);
	}

	public void updateVehicleStates(int step) {
		boolean blink = (step / 10) % 2 == 0; // blinking every 10 steps

		for (Vehicle v : VehicleController.getVehicles()) {
			// Brake light if speed < 2
			v.setBrake(v.getSpeed() < 2);

			// Headlights always on
			v.setHeadLightsOn(true);

			// Turn signals based on nextTurn
			v.setTurningLeft(v.getNextTurn() == Vehicle.Turn.LEFT && blink);
			v.setTurningRight(v.getNextTurn() == Vehicle.Turn.RIGHT && blink);

			// Emergency flashing
			v.setEmergencyFlashing(v.getType().getId().equals("emergency") && v.isEmergencyFlashing() && blink);
		}
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
		float dashLength = (float) (Settings.config.LANE_DIVIDER_DASH_LENGTH * Settings.config.SCALE);
		float[] dashPattern = { dashLength, dashLength };

		g.setStroke(new BasicStroke((float) (Settings.config.LANE_DIVIDER_STROKE_SIZE * Settings.config.SCALE),
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
		g.setColor(Settings.config.LANE_DIVIDER_COLOR);

		for (int i = 0; i < laneSize - 1; i++) {
			Lane lane1 = lanes[i];
			Lane lane2 = lanes[i + 1];

			Point[] shape1 = lane1.getShape();
			Point[] shape2 = lane2.getShape();

			int pointsCnt = Math.min(lane1.getShapeSize(), lane2.getShapeSize());

			for (int j = 0; j < pointsCnt - 1; j++) {
				Point p11 = shape1[j].fromWorldToMap();
				Point p12 = shape2[j].fromWorldToMap();
				Point p21 = shape1[j + 1].fromWorldToMap();
				Point p22 = shape2[j + 1].fromWorldToMap();
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
			Point p = shape[i].fromWorldToMap();
			xPoints[i] = (int) p.getX();
			yPoints[i] = (int) p.getY();
		}

		float lineThickness = (float) (Settings.config.LANE_STROKE_SIZE * Settings.config.SCALE);

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
		// String previousLaneID = null;

		for (int i = 0; i < connections.size(); i++) {
			Connection connect = connections.get(i);

			char signal = state.charAt(i);
			Color color = TrafficLight.getTrafficLightColor(signal);

			Lane fromLane = connect.getFromLane();

			Point[] shape = fromLane.getShape();
			Point end = shape[shape.length - 1].fromWorldToMap();

			// If a lane contain 2 connection -> 2 traffic light. If
			// we care about this one again, we can comeback this code
			// if (fromLane.getLaneId().equals(previousLaneID))
			// {
			// end.modifyY(4);
			// }

			// previousLaneID = fromLane.getLaneId();
			int radius = (int) (Settings.config.TRAFFIC_LIGHT_RADIUS * Settings.config.SCALE);
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
	private void drawObject(Graphics2D g, Junction j, Color c) {
		GraphicsSettings oldSettings = saveCurrentGraphicsSettings(g);

		Point[] shape = j.getShape();
		int size = j.getShapeSize();

		if (size < 2) {
			return;
		}

		int[] xPoints = new int[size];
		int[] yPoints = new int[size];

		for (int i = 0; i < size; i++) {
			Point p = shape[i].fromWorldToMap();
			xPoints[i] = (int) p.getX();
			yPoints[i] = (int) p.getY();
		}

		g.setColor(Settings.config.JUNCTION_COLOR);
		g.setStroke(new BasicStroke((float) (Settings.config.JUNCTION_STROKE_SIZE * Settings.config.SCALE)));

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
	 * Render static layer (edges, junctions) and cache as BufferedImage. This is
	 * rendered once per zoom/pan operation.
	 */
	private void renderStaticLayer() {

		staticLayer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2D = staticLayer.createGraphics();
		g2D.setStroke(defaultStroke);
		g2D.setColor(getBackground());
		g2D.fillRect(0, 0, getWidth(), getHeight());

		// Draw edges (static, cached)
		for (Edge e : Settings.network.getEdges()) {
			drawObject(g2D, e, Color.BLACK);
		}

		// Draw junctions (static, cached)
		for (Junction j : Settings.network.getJunctions()) {
			drawObject(g2D, j, Settings.config.JUNCTION_COLOR);
		}

		g2D.dispose();
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
				Settings.config.modifyScale(Settings.config.SCALE_STEP);
				Settings.config.invalidateStaticLayer(); // Invalidate cache when zoom changes
			}
		});

		actionMap.put("zoomOut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyScale(-Settings.config.SCALE_STEP);
				Settings.config.invalidateStaticLayer(); // Invalidate cache when zoom changes
			}
		});

		actionMap.put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetY(-Settings.config.OFFSET_STEP);
				Settings.config.invalidateStaticLayer(); // Invalidate cache when pan changes
			}

		});

		actionMap.put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetY(Settings.config.OFFSET_STEP);
				Settings.config.invalidateStaticLayer(); // Invalidate cache when pan changes
			}
		});

		actionMap.put("moveRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetX(Settings.config.OFFSET_STEP);
				Settings.config.invalidateStaticLayer(); // Invalidate cache when pan changes
			}
		});

		actionMap.put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.config.modifyOffsetX(-Settings.config.OFFSET_STEP);
				Settings.config.invalidateStaticLayer(); // Invalidate cache when pan changes
			}
		});

		Mouse mouseAction = new Mouse();
		this.addMouseListener(mouseAction);
		this.addMouseMotionListener(mouseAction);
		this.addMouseWheelListener(mouseAction);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Settings.config.invalidateStaticLayer(); // Invalidate cache when users resize the frame
			}
		});
	}

}
