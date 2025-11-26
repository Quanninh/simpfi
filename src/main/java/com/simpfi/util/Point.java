package com.simpfi.util;

/**
 * Create 2-dimensional coordinates to better represent object classes such as
 * Lane &amp; Junction.
 */
public class Point {

	private double x;
	private double y;

	public Point() {
		this(0, 0);
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public static double distance(Point p1, Point p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	public Point add(Point other) {
		return new Point(x + other.getX(), y + other.getY());
	}

	public Point scale(double scale) {
		return new Point(x * scale, y * scale);
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Point.
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
