package com.simpfi.ui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.simpfi.config.Constants;
import com.simpfi.config.Settings;
import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.TrafficLight;
import com.simpfi.object.Vehicle;
import com.simpfi.sumo.wrapper.VehicleController;
import com.simpfi.ui.Panel;
import com.simpfi.util.Point;

/**
 * Custom MapPanel class that inherits {@link com.simpfi.ui.Panel}. Used to draw
 * objects on the user interface such as vehicles, edges, lanes, etc.
 */
public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final BasicStroke defaultStroke = new BasicStroke(
		(float) (Constants.DEFAULT_STROKE_SIZE * Settings.config.SCALE), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

	public MapPanel() {
		initializeMapControl();
	}

	/**
	 * Overrides paintComponent method from {@link java.awt.Component}. Parses
	 * objects in the XML files and draw them on the panel. Graphics is replaced by
	 * Graphics2D for more advanced drawing features.
	 * 
	 * @param g where elements are drawn.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(defaultStroke);

		for (Edge e : Settings.network.getEdges()) {
			drawObject(g2D, e);
		}
		for (Junction j : Settings.network.getJunctions()) {
			drawObject(g2D, j);
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
	 * Used to draw a Vehicle on the User Interface.
	 * 
	 * @param g where the vehicle is drawn on.
	 * @param v the vehicle that is passed to the method.
	 */
	private void drawObject(Graphics2D g, Vehicle v) {
		if (v == null || !v.getIsActive()) {
			return;
		}

		Point pos = translateCoords(v.getPosition());
		int width = (int) (v.getWidth() * Settings.config.SCALE * Constants.VEHICLE_UPSCALE);
		int height = (int) (v.getHeight() * Settings.config.SCALE * Constants.VEHICLE_UPSCALE);

		g.setColor(v.getVehicleColor());

		double angle = v.getAngle();

		AffineTransform oldTransform = g.getTransform();
		g.rotate(Math.toRadians(angle), pos.getX(), pos.getY());

		g.fillRect((int) pos.getX() - width / 2, (int) pos.getY() - height / 2, width, height);

		g.setColor(Color.BLACK);
		// g.drawRect((int) pos.getX() - width / 2, (int) pos.getY() - height / 2,
		// width, height);

		g.setTransform(oldTransform);
	}

	/**
	 * Used to draw an Edge on the User Interface.
	 * 
	 * @param g where the edge is drawn on.
	 * @param e the edge that is passed to the method.
	 */
	private void drawObject(Graphics2D g, Edge e) {
		Lane[] lanes = e.getLanes();
		int laneSize = e.getLanesSize();
		for (int i = 0; i < laneSize; i++) {
			drawObject(g, lanes[i]);
		}

		if (laneSize <= 1) {
			return;
		}

		// draw lane dividers
		Stroke oldStroke = g.getStroke();
		drawLaneDividers(g, lanes, laneSize);
		g.setStroke(oldStroke);
	}

	private void drawLaneDividers(Graphics2D g, Lane[] lanes, int laneSize) {
		float dashLength = (float) (Constants.LANE_DIVIDER_DASH_LENGTH * Settings.config.SCALE);
		float[] dashPattern = { dashLength, dashLength };
		g.setStroke(new BasicStroke((float) (Constants.LANE_DIVIDER_STROKE_SIZE * Settings.config.SCALE),
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
		g.setColor(Color.WHITE);

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
	}

	/**
	 * Used to draw a Lane on the User Interface.
	 * 
	 * @param g where the lane is drawn on.
	 * @param l the lane that is passed to the method.
	 */
	private void drawObject(Graphics2D g, Lane l) {
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

		AffineTransform oldTransform = g.getTransform();
		Stroke oldStroke = g.getStroke();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		g.drawPolyline(xPoints, yPoints, size);

		g.setColor(Color.BLACK);
		g.setTransform(oldTransform);
		g.setStroke(oldStroke);
	}

	/**
	 * Used to draw a traffic light on the User Interface.
	 *
	 * @param g  where the traffic light is drawn on.
	 * @param tl traffic light object that is passed to the method.
	 */
	private void drawObject(Graphics2D g, TrafficLight tl) {
		String state = tl.getTLState();
		Lane[] lanes = tl.getLanes();

		for (int i = 0; i < lanes.length; i++) {
			Lane lane = lanes[i];
			char signal = state.charAt(i);
			Color color = TrafficLight.getTrafficLightColor(signal);

			Point[] shape = lane.getShape();
			Point end = translateCoords(shape[1]);

			int radius = (int) (Constants.TRAFFIC_LIGHT_RADIUS * Settings.config.SCALE);
			drawCircle(g, end, radius, color);
		}
	}

	/**
	 * Used to draw a Junction on the User Interface.
	 * 
	 * @param g where the junction is drawn on.
	 * @param j the junction that is passed to the method.
	 */
	// Draw Junction
	private void drawObject(Graphics2D g, Junction j) {
		Stroke oldStroke = g.getStroke();

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

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke((float) (Constants.JUNCTION_STROKE_SIZE * Settings.config.SCALE)));

		g.fillPolygon(xPoints, yPoints, size);
		g.drawPolygon(xPoints, yPoints, size);

		g.setColor(Color.BLACK);
		g.setStroke(oldStroke);
	}

	private void drawCircle(Graphics2D g, Point center, int radius, Color color) {
		g.setColor(color);
		g.fillOval((int) center.getX() - radius, (int) center.getY() - radius, radius * 2, radius * 2);
		g.setColor(Color.BLACK);
	}

	/**
	 * Used to convert the real-world coordinate to the graphics coordinate.
	 * 
	 * @param before the real-world coordinate.
	 * @return the graphics coordinate value.
	 */
	private Point translateCoords(Point before) {
		Point after = new Point();

		// -1 here to flip the Y-axis, because Y increases downward in graphics
		// coordinates
		after.setX(before.getX() * Settings.config.SCALE - Settings.config.OFFSET.getX());
		after.setY(before.getY() * Settings.config.SCALE * -1 - Settings.config.OFFSET.getY());

		return after;
	}

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
	}

}
