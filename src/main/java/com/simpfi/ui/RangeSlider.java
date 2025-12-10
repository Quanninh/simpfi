package com.simpfi.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import com.simpfi.config.Settings;

/**
 * Simple RangeSlider: an axis with two draggable thumbs ({@code low} and {@code high}).
 * Designed to filter out vehicles within a specific range.
 * Values are integers between {@code min} and {@code max} inclusive.
 */
public class RangeSlider extends JComponent implements MouseListener, MouseMotionListener {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
    
    private int min;
    private int max;
    private int lowValue;
    private int highValue;

    private final int trackHeight = 6;
    private final int thumbRadius = 8;
    // space left/right for thumbs
    private final int padding = 12;
    private final int preferredWidth = 340;
    private final int preferredHeight = 48;

    private enum Dragging { NONE, LOW, HIGH }
    private Dragging dragging = Dragging.NONE;

    private final List<ChangeListener> listeners = new ArrayList<>();
    // Offset inside thumb for smooth and continuous dragging
    private int mouseOffset = 0;
    // minimal difference between low and high thumbs
    private final int minGap = 1;

    /** The default stroke. */
	private final BasicStroke defaultStroke = new BasicStroke(
		(float) (Settings.config.NORMAL_STROKE_SIZE * Settings.config.SCALE), BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_ROUND);

    public RangeSlider(int min, int max) {
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (min >= max) throw new IllegalArgumentException("min < max required");
        this.min = min;
        this.max = max;

        // Initialize two thumbs to be located on two ends at the start
        this.lowValue = min;
        this.highValue = max;
        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        setMinimumSize(new Dimension(120, preferredHeight));
    }

    /* --- Getters/Setters --- */
    public int getLowValue() { return lowValue; }
    public int getHighValue() { return highValue; }
    public void setLowValue(int val) { lowValue = clamp(val); ensureOrder(); repaint(); fireChange(); }
    public void setHighValue(int val) { highValue = clamp(val); ensureOrder(); repaint(); fireChange(); }

    public void addChangeListener(ChangeListener l) { listeners.add(l); }
    public void removeChangeListener(ChangeListener l) { listeners.remove(l); }

    private void fireChange() {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener l : listeners) l.stateChanged(e);
    }

    private int clamp(int v) { return Math.max(min, Math.min(max, v)); }
    // Check this again
    private void ensureOrder() {
        if (highValue < lowValue) {
            int tmp = highValue; highValue = lowValue; lowValue = tmp;
        }
        if (highValue - lowValue < minGap) {
            if (dragging == Dragging.LOW) lowValue = highValue - minGap;
            else if (dragging == Dragging.HIGH) highValue = lowValue + minGap;
        }
        lowValue = clamp(lowValue); highValue = clamp(highValue);
    }

    /* --- Rendering --- */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(defaultStroke);
        // Make the edges smoother
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        // check
        int trackY = h / 2 - trackHeight / 2;

        // track rect
        int x0 = padding;
        int x1 = w - padding;

        // draw baseline
        g2D.setColor(new Color(200, 200, 200));
        // (x0, trackY) is top-left corner; x1 - x0 is width; trackHeight is height, 
        g2D.fillRoundRect(x0, trackY, x1 - x0, trackHeight, trackHeight, trackHeight);

        // fill selected range
        int lowX = valueToX(lowValue);
        int highX = valueToX(highValue);
        g2D.setColor(new Color(66, 133, 244));
        g2D.fillRoundRect(lowX, trackY, highX - lowX, trackHeight, trackHeight, trackHeight);

        // draw ticks or min/max labels
        g2D.setColor(Color.DARK_GRAY);
        g2D.setFont(g2D.getFont().deriveFont(11f));
        String minS = String.valueOf(min);
        String maxS = String.valueOf(max);
        g2D.drawString(minS, x0 - g2D.getFontMetrics().stringWidth(minS)/2, trackY + 20);
        g2D.drawString(maxS, x1 - g2D.getFontMetrics().stringWidth(maxS)/2, trackY + 20);

        // draw thumbs
        drawThumb(g2D, lowX, h/2, lowValue);
        drawThumb(g2D, highX, h/2, highValue);

        g2D.dispose();
    }

    private void drawThumb(Graphics2D g2D, int cx, int cy, int value) {
        // outer border
        g2D.setColor(Color.BLACK);
        // first two arguements is the top-left coordinate of the rectangle bounding the oval
        g2D.fillOval(cx - thumbRadius - 1, cy - thumbRadius - 1, thumbRadius*2 + 2, thumbRadius*2 + 2);
        // inner thumb
        g2D.setColor(Color.WHITE);
        g2D.fillOval(cx - thumbRadius, cy - thumbRadius, thumbRadius*2, thumbRadius*2);
        // label value above thumb
        String s = String.valueOf(value);
        g2D.setColor(Color.BLACK);
        FontMetrics fm = g2D.getFontMetrics();
        // Move the string to the left by half of its String Width => Center the thumb
        int sx = cx - fm.stringWidth(s)/2;
        // Make the string slightly above the thumb
        int sy = cy - thumbRadius - 6;
        g2D.drawString(s, sx, sy);
    }

    /* --- Coordinate mapping --- */
    private int valueToX(int val) {
        int x0 = padding;
        int x1 = getWidth() - padding;
        double frac = (double)(val - min) / (double)(max - min);
        return x0 + (int)Math.round(frac * (x1 - x0));
    }

    private int xToValue(int x) {
        int x0 = padding;
        int x1 = getWidth() - padding;
        double frac = (double)(x - x0) / (double)(x1 - x0);
        int v = min + (int)Math.round(frac * (max - min));
        return clamp(v);
    }

    /* --- Mouse handling --- */

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int lx = valueToX(lowValue);
        int hx = valueToX(highValue);

        // compute distance to thumbs
        int dl = Math.abs(mx - lx);
        int dh = Math.abs(mx - hx);

        if (dl <= thumbRadius && dl <= dh) {
            dragging = Dragging.LOW;
            mouseOffset = mx - lx;
        } else if (dh <= thumbRadius) {
            dragging = Dragging.HIGH;
            mouseOffset = mx - hx;
        } else {
            dragging = Dragging.NONE;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = Dragging.NONE;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging == Dragging.NONE) return;
        int mx = e.getX() - mouseOffset;
        int val = xToValue(mx);
        if (dragging == Dragging.LOW) {
            lowValue = Math.min(val, highValue - minGap);
        } else {
            highValue = Math.max(val, lowValue + minGap);
        }
        ensureOrder();
        repaint();
        fireChange();
    }

    // unused but required by interfaces
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}