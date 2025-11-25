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
		return Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX())
			+ (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
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
