/**
 * Copyright (c) 2008-2009, Piet Blok
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the copyright holder nor the names of the
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pbjar.jxlayer.plaf.misc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;

/**
 * Shows a magnification glass on top of a component.
 * 
 * @author Piet Blok
 */
public class MagnifierUI extends
	GeneralLayerUI<JComponent, MagnifierUI.MagnifierState> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Holds state information.
     */
    protected static class MagnifierState {

	private int radius = radiusOptions[6];

	private double magnifyingFactor = magnificationOptions[5];

	private Point2D point = new Point2D.Double();

	/**
	 * Get the magnifying factor.
	 * 
	 * @return the magnifying factor
	 * @see #setMagnifyingFactor(double)
	 */
	public double getMagnifyingFactor() {
	    return magnifyingFactor;
	}

	/**
	 * Get the mouse point.
	 * 
	 * @return return the mouse point
	 * @see #setPoint(Point2D)
	 */
	public Point2D getPoint() {
	    return point;
	}

	/**
	 * Get the radius.
	 * 
	 * @return the radius
	 * @see #setRadius(int)
	 */
	public int getRadius() {
	    return radius;
	}

	/**
	 * Set the magnifying factor.
	 * 
	 * @param magnifyingFactor
	 *            the new magnifying factor
	 * @see #getMagnifyingFactor()
	 */
	public void setMagnifyingFactor(double magnifyingFactor) {
	    this.magnifyingFactor = magnifyingFactor;
	}

	/**
	 * Set the mouse point.
	 * 
	 * @param point
	 *            the new mouse point
	 * @see #getPoint()
	 */
	public void setPoint(Point2D point) {
	    this.point.setLocation(point);
	}

	/**
	 * Set the radius.
	 * 
	 * @param radius
	 *            the new radius
	 * @see #getRadius()
	 */
	public void setRadius(int radius) {
	    this.radius = radius;
	}

    }

    private static final Integer[] radiusOptions = new Integer[] { 20, 40, 60,
	    80, 100, 120, 140, 160, 180, 200 };

    private static final Double[] magnificationOptions = new Double[] { 0.25,
	    0.5, 0.75, 1.0, 2.0, 4.0, 8.0, 16.0 };

    /**
     * Return {@link Action}s that:
     * <ol>
     * <li>Set magnification factor.</li>
     * <li>Set the glass radius.</li>
     * </ol>
     */
    @Override
    public List<Action> getActions(final JXLayer<? extends JComponent> layer) {
	ArrayList<Action> actionList = new ArrayList<Action>();
	actionList.addAll(super.getActions(layer));
	/*
	 * Set the magnifying factor
	 */
	actionList.add(new AbstractAction("Set magnification factor") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent event) {
		MagnifierState state = getStateObject(layer);
		Double current = state.getMagnifyingFactor();
		int optionIndex = JOptionPane.showOptionDialog(layer,
			"Choose a magnification factor",
			"Choose magnification", JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE, null,
			magnificationOptions, current);
		if (optionIndex != JOptionPane.CLOSED_OPTION) {
		    state
			    .setMagnifyingFactor(magnificationOptions[optionIndex]);
		}
	    }
	});
	/*
	 * Set the glass radius
	 */
	actionList.add(new AbstractAction("Set glass radius") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent event) {
		MagnifierState state = getStateObject(layer);
		Integer current = state.getRadius();
		int optionIndex = JOptionPane.showOptionDialog(layer,
			"Choose a glass radius", "Choose glass radius",
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE, null, radiusOptions,
			current);
		if (optionIndex != JOptionPane.CLOSED_OPTION) {
		    state.setRadius(radiusOptions[optionIndex]);
		}
	    }
	});

	return actionList;
    }

    private Paint createPaint(Ellipse2D glass, boolean transparent) {
	Point2D center = new Point2D.Double(glass.getCenterX(), glass
		.getCenterY());
	float radius = (float) (glass.getCenterX() - glass.getX());
	Point2D focus = new Point2D.Double(center.getX() - 0.5 * radius, center
		.getY()
		- 0.5 * radius);
	Color[] colors = new Color[] {
		transparent ? new Color(255, 255, 255, 128) : Color.WHITE,
		transparent ? new Color(0, 255, 255, 32) : Color.CYAN };
	float[] fractions = new float[] { 0f, 1f };
	RadialGradientPaint paint = new RadialGradientPaint(center, radius,
		focus, fractions, colors,
		MultipleGradientPaint.CycleMethod.NO_CYCLE);
	return paint;
    }

    @Override
    protected MagnifierState createStateObject(JXLayer<? extends JComponent> layer) {
	return new MagnifierState();
    }

    @Override
    protected void paintLayer(Graphics2D g2, JXLayer<? extends JComponent> layer) {
	super.paintLayer(g2, layer);
	MagnifierState state = getStateObject(layer);
	Point2D point = state.getPoint();
	double scale = state.getMagnifyingFactor();
	double baseRadius = state.getRadius();
	double scaledRadius = (baseRadius / scale);
	double strokeAdjust = 0.5;
	double drawSize = 2 * (baseRadius + strokeAdjust);
	double clipSize = 2 * scaledRadius;
	Ellipse2D drawGlass = new Ellipse2D.Double(-strokeAdjust,
		-strokeAdjust, drawSize, drawSize);
	Ellipse2D clipGlass = new Ellipse2D.Double(0, 0, clipSize, clipSize);
	g2.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
		RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	g2.translate(point.getX() - baseRadius, point.getY() - baseRadius);
	Color oldColor = g2.getColor();
	g2.setPaint(createPaint(drawGlass, false));
	g2.fill(drawGlass);
	g2.setColor(oldColor);
	g2.draw(drawGlass);
	AffineTransform oldTransform = g2.getTransform();
	Shape oldClip = g2.getClip();
	g2.scale(scale, scale);
	g2.clip(clipGlass);
	g2.translate(scaledRadius - point.getX(), scaledRadius - point.getY());
	// layer.paint(g2);
	super.paintLayer(g2, layer);
	g2.setTransform(oldTransform);
	g2.setClip(oldClip);
	g2.setPaint(createPaint(drawGlass, true));
	g2.fill(drawGlass);
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JXLayer<? extends JComponent> layer) {
	super.processMouseEvent(e, layer);
	MagnifierState state = getStateObject(layer);
	state.setPoint(SwingUtilities.convertPoint(e.getComponent(), e
		.getPoint(), layer));
	setDirty(true);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e,
	    JXLayer<? extends JComponent> layer) {
	super.processMouseMotionEvent(e, layer);
	MagnifierState state = getStateObject(layer);
	state.setPoint(SwingUtilities.convertPoint(e.getComponent(), e
		.getPoint(), layer));
	setDirty(true);
    }
}
