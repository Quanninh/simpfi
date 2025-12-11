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

    /** The minimum value of the Range Slider. */
    private int min;

    /** The maximum value of the Range Slider. */
    private int max;

    /** The current value that the lower-value thumb holds. */
    private int lowValue;

    /** The current value that the upper-value thumb holds. */
    private int highValue;

    /** The height of the track measured in pixels.
     *  Also used to make the track rounded.
     * 
    */
    private final int trackHeight = 6;

    /** The radius of each thumb. */
    private final int thumbRadius = 8;
    // space left/right for thumbs
    /** Padding used to keep the Range Slider away from the edges. */
    private final int padding = 12;

    /** The perferred width for the Range Slider. */
    private final int preferredWidth = 340;

    /** The perferred height for the Range Slider. */
    private final int preferredHeight = 48;

    /** The states of the Range Slider when users interact with it. */
    private enum Dragging { NONE, LOW, HIGH }
    private Dragging dragging = Dragging.NONE;

    /** A list of listeners.*/
    private final List<ChangeListener> listeners = new ArrayList<>();

    /** The mouse offset inside thumb for smooth and continuous dragging. */
    private int mouseOffset = 0;
    /** The minimal difference between low and high thumbs. */
    private final int minGap = 1;

    /** The default stroke. */
	private final BasicStroke defaultStroke = new BasicStroke(
		(float) (Settings.config.NORMAL_STROKE_SIZE * Settings.config.SCALE), BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_ROUND);

    /** Instantiate a Range Slider, set the positions of two thumbs to the two ends initially. 
     *  Also set the preferred size of the Range Slider.
     * 
     * @param min the minimum value of the Range Slider
     * @param max the maximum value of the Range Slider
     * 
     */
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

    /** Get the low value.
     * 
     * @return the low value
     */
    public int getLowValue() { return lowValue; }

    /** Get the high value.
     * 
     * @return the high value
     */
    public int getHighValue() { return highValue; }

    /** Set the low value.
     * 
     * @param val the value to set to the low value
     */
    public void setLowValue(int val) { lowValue = clamp(val); ensureOrder(); repaint(); fireChange(); }

    /** Set the high value.
     * 
     * @param val the value to set to the high value
     */
    public void setHighValue(int val) { highValue = clamp(val); ensureOrder(); repaint(); fireChange(); }

    /**
     * Method to attach a {@link ChangeListener} for state changes.
     * 
     * @param l the ChangeListener to add
     */
    public void addChangeListener(ChangeListener l) { listeners.add(l); }

    /**
     * Method to remove a {@link ChangeListener}.
     * 
     * @param l the ChangeListener to remove
     */
    public void removeChangeListener(ChangeListener l) { listeners.remove(l); }

    /**
     * Notifies all registered {@link ChangeListener}s that the slider's
     * values have changed.
     */
    private void fireChange() {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener l : listeners) l.stateChanged(e);
    }
    
    /** Method to ensure the passed value stay within the required range.
     * 
     * @param val the value to 
     */
    private int clamp(int val) { return Math.max(min, Math.min(max, val)); }
    
    /**
     * Method to ensure the low value and the high value stays within the correct order
     * and keep the defined minimum gap.
     */
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

    /**
     * Overrides the built-in method paintComponent from {@link java.awt.Component} to draw the Range Slider.
     * 
     * @param g the {@link Graphics}
    */
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
        // (x0, trackY) is top-left corner; x1 - x0 is width; trackHeight is height; the next two trackHeights make the shape rounded 
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
        g2D.drawString(minS, x0 - g2D.getFontMetrics().stringWidth(minS)/2, trackY + 25);
        g2D.drawString(maxS, x1 - g2D.getFontMetrics().stringWidth(maxS)/2, trackY + 25);

        // draw thumbs
        drawThumb(g2D, lowX, h/2, lowValue);
        drawThumb(g2D, highX, h/2, highValue);

        g2D.dispose();
    }

    /**
     * Method used to draw the thumb of the Range Slider.
     * 
     * @param g the {@link Graphics2D}
     * @param cx the value on the x-axis of the thumb
     * @param cy the value on the y-axis of the thumb
     * @param value the value drawn on the thumb
     */
    private void drawThumb(Graphics2D g, int cx, int cy, int value) {
        // outer border
        g.setColor(Color.BLACK);
        // first two arguements is the top-left coordinate of the rectangle bounding the oval
        g.fillOval(cx - thumbRadius - 1, cy - thumbRadius - 1, thumbRadius*2 + 2, thumbRadius*2 + 2);
        // inner thumb
        g.setColor(Color.WHITE);
        g.fillOval(cx - thumbRadius, cy - thumbRadius, thumbRadius*2, thumbRadius*2);
        // label value above thumb
        String s = String.valueOf(value);
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        // Move the string to the left by half of its String Width => Center the thumb
        int sx = cx - fm.stringWidth(s)/2;
        // Make the string slightly above the thumb
        int sy = cy - thumbRadius - 6;
        g.drawString(s, sx, sy);
    }

    /**
     * Method to map the value to the corresponding coordinate.
     * 
     * @param val the value
     */
    private int valueToX(int val) {
        int x0 = padding;
        int x1 = getWidth() - padding;
        double frac = (double)(val - min) / (double)(max - min);
        return x0 + (int)Math.round(frac * (x1 - x0));
    }

    /**
     * Method to map the coordinate to the corresponding value.
     * 
     * @param x the coordinate, value on the x-axis
     */
    private int xToValue(int x) {
        int x0 = padding;
        int x1 = getWidth() - padding;
        double frac = (double)(x - x0) / (double)(x1 - x0);
        int val = min + (int)Math.round(frac * (max - min));
        return clamp(val);
    }

    /**
     * Overrides the method mousePressed() to compute the suitable {@code dragging} value.
     * 
     * @param e the mouse event 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        //int my = e.getY();
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

    /**
     * Overrides the method mouseReleased() to set the value of {@code dragging}
     * to {@code Dragging.NONE} when users release the mouse.
     * 
     * @param e the mouse event 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = Dragging.NONE;
    }

    /**
     * Overrides the method mouseDragged() to update the thumb positions
     * and repaint the Range Slider accordingly.
     * 
     * @param e the mouse event
     */
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