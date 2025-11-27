package com.simpfi.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * The Class GraphicsSettings which saves the configurations of
 * {@link Graphics2D}
 */
public class GraphicsSettings {

	/** The color. */
	private Color color;

	/** The stroke. */
	private Stroke stroke;

	/** The transform. */
	private AffineTransform transform;

	/**
	 * Instantiates a new graphics settings.
	 *
	 * @param color     the color
	 * @param stroke    the stroke
	 * @param transform the transform
	 */
	public GraphicsSettings(Color color, Stroke stroke, AffineTransform transform) {
		this.color = color;
		this.stroke = stroke;
		this.transform = transform;
	}

	/**
	 * Returns the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the stroke.
	 *
	 * @return the stroke
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * Returns the transform.
	 *
	 * @return the transform
	 */
	public AffineTransform getTransform() {
		return transform;
	}

}
