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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;

/**
 * A mouse drawing UI.
 * 
 * @author Piet Blok
 */
public class MouseDrawingUI extends
	GeneralLayerUI<JComponent, MouseDrawingUI.DrawingState> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Holds state information.
     */
    protected static class DrawingState {

	public Color lineColor = Color.RED;

	public ColoredLine coloredLine = null;

	public List<ColoredLine> lineList = new ArrayList<ColoredLine>();

	public Dimension preferred = null;

	public Rectangle innerArea = new Rectangle();

	public Rectangle newInnerArea = new Rectangle();

	public AffineTransform transform = new AffineTransform();

	public AffineTransform inverseTransform = new AffineTransform();

	private final JXLayer<? extends JComponent> layer;

	private double scale = 1.0;

	public DrawingState(JXLayer<? extends JComponent> layer) {
	    this.layer = layer;
	}

	public void addPoint(Point2D point) {
	    if (coloredLine == null) {
		coloredLine = new ColoredLine(lineColor);
		lineList.add(coloredLine);
	    }
	    updateSize();
	    coloredLine.addPoint(inverseTransform.transform(point, point));
	}

	public void clear() {
	    lineList.clear();
	}

	public Color getLineColor() {
	    return lineColor;
	}

	public void setLineColor(Color color) {
	    this.lineColor = color;
	}

	public void terminateLine() {
	    coloredLine = null;
	}

	/**
	 * Not implemented.
	 * <p>
	 * This is for future use and meant to ensure that the drawing scales
	 * with the scaling of the underlying component. But it interferes a bit
	 * with the current testing on other types of components.
	 * </p>
	 */
	public void updateSize() {
	    // updateSizeImpl();
	}

	private double getOffset(double scale, double imageSize, double center) {
	    return (center - (imageSize * scale / 2.0)) / scale;
	}

	@SuppressWarnings("unused")
	private void updateSizeImpl() {
	    boolean dirty = false;
	    SwingUtilities.calculateInnerArea(layer, newInnerArea);
	    if (!newInnerArea.equals(innerArea)) {
		innerArea.setRect(newInnerArea);
		dirty = true;
	    }
	    Dimension newPreferred = layer.getPreferredSize();
	    if (!newPreferred.equals(preferred)) {
		preferred = newPreferred;
		dirty = true;
	    }
	    if (dirty) {
		transform.setToIdentity();
		scale = Math.min(innerArea.getWidth() / preferred.getWidth(),
			innerArea.getHeight() / preferred.getHeight());
		transform.scale(scale, scale);

		transform.translate(getOffset(scale, preferred.getWidth(),
			innerArea.getCenterX()), getOffset(scale, preferred
			.getHeight(), innerArea.getCenterY()));

		try {
		    inverseTransform = transform.createInverse();
		} catch (NoninvertibleTransformException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * Defines one line.
     */
    protected static class ColoredLine {

	public final List<Point2D> path = new ArrayList<Point2D>();

	public final Color color;

	public ColoredLine(Color color) {
	    this.color = color;
	}

	public void addPoint(Point2D point) {
	    path.add(point);
	}

    }

    /**
     * Returns {@link Action}s for:
     * <ol>
     * <li>Clear the current drawing.</li>
     * <li>Set the color for the next line to be drawn.</li>
     * </ol>
     */
    public List<Action> getActions(final JXLayer<? extends JComponent> layer) {
	ArrayList<Action> actionList = new ArrayList<Action>();
	actionList.addAll(super.getActions(layer));
	/*
	 * Clear
	 */
	actionList.add(new AbstractAction("Clear") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent event) {
		MouseDrawingUI.this.getStateObject(layer).clear();
		setDirty(true);
	    }
	});
	/*
	 * Set line color
	 */
	actionList.add(new AbstractAction("Set line color") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent event) {
		DrawingState state = MouseDrawingUI.this.getStateObject(layer);
		Color color = state.getLineColor();
		color = JColorChooser.showDialog(layer,
			"Choose a new line color", color);
		if (color != null) {
		    state.setLineColor(color);
		}
	    }
	});

	return actionList;
    }

    @Override
    protected DrawingState createStateObject(JXLayer<? extends JComponent> layer) {
	return new DrawingState(layer);
    }

    protected void paintLayer(Graphics2D g2, JXLayer<? extends JComponent> layer) {
	super.paintLayer(g2, layer);
	DrawingState state = getStateObject(layer);
	state.updateSize();
	g2.transform(state.transform);
	g2.setStroke(new BasicStroke(4f / (float) state.scale));
	Line2D line = new Line2D.Double();
	for (ColoredLine coloredLine : state.lineList) {
	    g2.setColor(coloredLine.color);
	    Point2D oldPoint = null;
	    for (Point2D point : coloredLine.path) {
		if (oldPoint != null) {
		    line.setLine(oldPoint, point);
		    g2.draw(line);
		}
		oldPoint = point;
	    }
	}
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JXLayer<? extends JComponent> layer) {
	if (e.getID() == MouseEvent.MOUSE_RELEASED) {
	    DrawingState state = getStateObject(layer);
	    state.terminateLine();
	    setDirty(true);
	}
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e,
	    JXLayer<? extends JComponent> layer) {
	if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
	    DrawingState state = getStateObject(layer);
	    state.addPoint(SwingUtilities.convertPoint(e.getComponent(), e
		    .getPoint(), layer));
	    setDirty(true);
	}
    }

}
