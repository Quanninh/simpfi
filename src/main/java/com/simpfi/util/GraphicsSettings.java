package com.simpfi.util;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class GraphicsSettings {

	private Color color;
	private Stroke stroke;
	private AffineTransform transform;

	public GraphicsSettings(Color color, Stroke stroke, AffineTransform transform) {
		this.color = color;
		this.stroke = stroke;
		this.transform = transform;
	}

	public Color getColor() {
		return color;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public AffineTransform getTransform() {
		return transform;
	}

}
