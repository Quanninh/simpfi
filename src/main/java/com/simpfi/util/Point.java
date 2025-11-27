package com.simpfi.util;

// TODO: Auto-generated Javadoc
/**
 * Create 2-dimensional coordinates to better represent object classes such as
 * Lane &amp; Junction.
 */
public class Point {

	/** The x. */
	private double x;
	
	/** The y. */
	private double y;

	/**
	 * Instantiates a new point.
	 */
	public Point() {
		this(0, 0);
	}

    /**
     * Overloaded constructor creating a new {@code Point} using provided x and y coordinates.
     *
     * @param x x coordinate for the new point
     * @param y y coordinate for the new point
     */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x.
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
	 * Gets the y.
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

    /**
     * Method to calculate the distance of two {@code Points}.
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
	 * Adds the.
	 *
	 * @param other the other
	 * @return the point
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
	 * @return the string
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
