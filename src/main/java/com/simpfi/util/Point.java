package com.simpfi.util;

/**
 * Create 2-dimensional coordinates to better represent object classes such as
 * Lane & Junction.
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

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Point.
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
