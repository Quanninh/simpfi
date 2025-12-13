package com.simpfi.util;

import com.simpfi.config.Settings;

/**
 * Create 2-dimensional coordinates to better represent object classes such as
 * Lane &amp; Junction.
 */
public class Point {

	/** The x coordinate. */
	private double x;

	/** The y coordinate. */
	private double y;

	/**
	 * Instantiates a new point.
	 */
	public Point() {
		this(0, 0);
	}

	/**
	 * Overloaded constructor creating a new {@code Point} using provided x and y
	 * coordinates.
	 *
	 * @param x x coordinate for the new point
	 * @param y y coordinate for the new point
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Returns the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(double y) {
		this.y = y;
	}

	// Modify y
	public void modifyY(double x) {
		this.y += x;
	}

	/**
	 * Method to calculate the distance of two points.
	 *
	 * @param p1 first point
	 * @param p2 second point
	 * @return distance between p1 and p2
	 */
	public static double distance(Point p1, Point p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Add a point to another point.
	 *
	 * @param other the other point
	 * @return the sum
	 */
	public Point add(Point other) {
		return new Point(x + other.getX(), y + other.getY());
	}

	/**
	 * Returns a new point that is scaled by a provided factor.
	 *
	 * @param scale factor
	 * @return the new point manipulated by scale
	 */
	public Point scale(double scale) {
		return new Point(x * scale, y * scale);
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Point.
	 *
	 * @return the representation of Point
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Converts the real-world coordinate to the map coordinate.
	 * 
	 * @return the map coordinate
	 */
	public Point fromWorldToMap() {
		Point after = new Point();

		// -1 here to flip the Y-axis, because Y increases downward in graphics
		// coordinates
		after.setX(x * Settings.config.SCALE - Settings.config.OFFSET.getX());
		after.setY(y * Settings.config.SCALE * -1 - Settings.config.OFFSET.getY());

		return after;
	}

	public Point fromMapToWorld() {
		Point after = new Point();

		after.setX((x + Settings.config.OFFSET.getX()) / Settings.config.SCALE);
		after.setY(-(y + Settings.config.OFFSET.getY()) / Settings.config.SCALE);

		return after;
	}

}
