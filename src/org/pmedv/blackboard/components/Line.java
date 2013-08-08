/**

	BlackBoard breadboard designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */
package org.pmedv.blackboard.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlTransient;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.beans.LineBean;
import org.pmedv.core.preferences.Preferences;

/**
 * <p>
 * A line is the basic drawing element of BlackBoard. It is used to draw wires
 * and basic shapes for example. Furthermore it is used as a base class for the
 * {@link Resistor} and the {@link Diode} element.
 * </p>
 * <p>
 * A line is classified by a starting {@link Point} and an end point, its color
 * and it's thickness. Two lines are said to be equal if both the starting point
 * and the end point are equal.
 * </p>
 * <p>
 * Wires in BlackBoard also have a type of starting and ending point. Refer
 * {@link LineEdgeType} for more information.
 * <p>
 * This class contains also some basic geometric operations needed for line
 * operations such at determining the slope of a line or if some point lays on a
 * specific line.
 * </p>
 * 
 * @author Matthias Pueski
 * 
 */
public class Line extends Item implements Shape {

	public static final int HANDLE_SIZE = 12;
	public static final int ENDPOINT_SIZE = 6;
	public static final int SIMPLE_ARROW_SIZE = 12;
	private static final int TOLERANCE = 8;

	private LineEdgeType startType;
	private LineEdgeType endType;

	private BasicStroke stroke;

	private Point start;
	private Point end;

	private Point oldstart;
	private Point oldEnd;
	
	private int netIndex = -1;

	private final Point midPoint = new Point();

	private static BufferedImage wireImage;

	static {
		try {
			wireImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/w_Wire_0.6mm_1.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@XmlTransient
	private final ArrayList<WireConnection> wireConnections = new ArrayList<WireConnection>();
	
	public Line(Point start, Point end, int index) {

		this.start = start;
		this.end = end;
		this.index = index;
		this.oldstart = new Point(start.x, start.y);
		this.oldEnd = new Point(end.x, end.y);
	}

	public Line(int x1, int y1, int x2, int y2, int index) {

		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);

		this.start = p1;
		this.end = p2;
		this.oldstart = new Point(x1, y1);
		this.oldEnd = new Point(x2, y2);
		this.index = index;

	}

	public Line(LineBean lineBean) {
		this(lineBean.getStart().getX(), lineBean.getStart().getY(), lineBean.getEnd().getX(), lineBean.getEnd().getY(), 0);
		if (lineBean.getColor() != null)
			setColor(new Color(lineBean.getColor().getRed(), lineBean.getColor().getGreen(), lineBean.getColor().getBlue()));
		setLayer(lineBean.getLayer());
		setStartType(lineBean.getStartType());
		setEndType(lineBean.getEndType());
		if (lineBean.getThickness() > 0.0f) {
			if (lineBean.getMiterLimit() < 1)
				lineBean.setMiterLimit(1);
			this.stroke = new BasicStroke(lineBean.getThickness(), lineBean.getEndCap(), lineBean.getLineJoin(), lineBean.getMiterLimit(), lineBean.getDashArray(), lineBean.getDashPhase());
		}
		else
			setStroke(new BasicStroke(3.0f));
	}

	/**
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Point start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Point end) {
		this.end = end;
	}

	@Override
	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

//		g2d.setColor(getColor());
//		g2d.setStroke(getStroke());

		final Boolean realistic = (Boolean) Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.realisticWires");

		if (!realistic) {
			g2d.drawLine((int) getStart().getX(), (int) getStart().getY(), (int) getEnd().getX(), (int) getEnd().getY());
		}
		else {
			// save current transform
			AffineTransform saveXform = g2d.getTransform();
			// if there's any slope we need to transform the graphics by the
			// slope's
			// angle
			if (getSlope() != 0) {
				AffineTransform transform = new AffineTransform();
				transform.translate((int) getMidPoint().getX(), (int) getMidPoint().getY());
				transform.rotate(getSlopeAngleInRadians());
				transform.translate((int) -getMidPoint().getX(), (int) -getMidPoint().getY());
				g2d.transform(transform);
			}

			// draw wire
			int steps = (int) (getLength() / 16);
			for (int i = 0; i < steps; i++) {
				g2d.drawImage(wireImage, (int) (getMidPoint().getX() - getLength() / 2) + i * 16, (int) getMidPoint().getY() - 8, null);
			}

			// restore saved transform
			if (getSlope() != 0) {
				g2d.setTransform(saveXform);
			}

		}

		if (!realistic) {

			BasicStroke stroke = (BasicStroke)g2d.getStroke();
			
			int size = (int) (stroke.getLineWidth()*3.0);
			
			if (getStartType() != null) {

				if (getStartType() == LineEdgeType.ROUND_DOT) {
					g2d.fillOval((int) getStart().getX() - size / 2, (int) getStart().getY() - size / 2, size, size);
				}
				else if (getStartType() == LineEdgeType.SIMPLE_ARROW) {
					drawSimpleStartArrow(g2d);
				}

			}
			if (getEndType() != null) {

				if (getEndType() == LineEdgeType.ROUND_DOT) {
					g2d.fillOval((int) getEnd().getX() - size / 2, (int) getEnd().getY() - size / 2, size, size);
				}
				else if (getEndType() == LineEdgeType.SIMPLE_ARROW) {
					drawSimpleEndArrow(g2d);
				}

			}

		}

	}

	public void drawFat(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setStroke(BoardUtil.stroke_4_0f);
		g2d.setColor(Color.GREEN);
		g2d.drawLine((int) getStart().getX(), (int) getStart().getY(), (int) getEnd().getX(), (int) getEnd().getY());

		if (getStartType() != null) {

			if (getStartType() == LineEdgeType.ROUND_DOT) {
				g2d.fillOval((int) getStart().getX() - ENDPOINT_SIZE / 2, (int) getStart().getY() - ENDPOINT_SIZE / 2, ENDPOINT_SIZE, ENDPOINT_SIZE);
			}
			else if (getStartType() == LineEdgeType.SIMPLE_ARROW) {
				drawSimpleStartArrow(g2d);
			}

		}
		if (getEndType() != null) {

			if (getEndType() == LineEdgeType.ROUND_DOT) {
				g2d.fillOval((int) getEnd().getX() - ENDPOINT_SIZE / 2, (int) getEnd().getY() - ENDPOINT_SIZE / 2, ENDPOINT_SIZE, ENDPOINT_SIZE);
			}
			else if (getEndType() == LineEdgeType.SIMPLE_ARROW) {
				drawSimpleEndArrow(g2d);
			}

		}
	}

	private void drawSimpleStartArrow(Graphics2D g2d) {
		AffineTransform saveXform = executeTransform(g2d, getStart());

		// draw arrow

		if (getEnd().getX() < getStart().getX() && getEnd().getY() == getStart().getY()) {
			g2d.drawLine((int) getStart().getX(), (int) getStart().getY(), (int) getStart().getX() - ENDPOINT_SIZE, (int) getStart().getY() - ENDPOINT_SIZE / 2);
			g2d.drawLine((int) getStart().getX(), (int) getStart().getY(), (int) getStart().getX() - ENDPOINT_SIZE, (int) getStart().getY() + ENDPOINT_SIZE / 2);
		}
		else {
			g2d.drawLine((int) getStart().getX(), (int) getStart().getY(), (int) getStart().getX() + ENDPOINT_SIZE, (int) getStart().getY() - ENDPOINT_SIZE / 2);
			g2d.drawLine((int) getStart().getX(), (int) getStart().getY(), (int) getStart().getX() + ENDPOINT_SIZE, (int) getStart().getY() + ENDPOINT_SIZE / 2);

		}

		restoreTransform(g2d, saveXform);
	}

	private void drawSimpleEndArrow(Graphics2D g2d) {

		AffineTransform saveXform = executeTransform(g2d, getEnd());

		if (getEnd().getX() < getStart().getX() && getEnd().getY() == getStart().getY()) {
			g2d.drawLine((int) getEnd().getX(), (int) getEnd().getY(), (int) getEnd().getX() + ENDPOINT_SIZE, (int) getEnd().getY() - ENDPOINT_SIZE / 2);
			g2d.drawLine((int) getEnd().getX(), (int) getEnd().getY(), (int) getEnd().getX() + ENDPOINT_SIZE, (int) getEnd().getY() + ENDPOINT_SIZE / 2);
		}
		else {
			g2d.drawLine((int) getEnd().getX(), (int) getEnd().getY(), (int) getEnd().getX() - ENDPOINT_SIZE, (int) getEnd().getY() - ENDPOINT_SIZE / 2);
			g2d.drawLine((int) getEnd().getX(), (int) getEnd().getY(), (int) getEnd().getX() - ENDPOINT_SIZE, (int) getEnd().getY() + ENDPOINT_SIZE / 2);
		}

		restoreTransform(g2d, saveXform);
	}

	private void restoreTransform(Graphics2D g2d, AffineTransform saveXform) {
		// restore saved transform
		if (getSlope() != 0) {
			g2d.setTransform(saveXform);
		}
	}

	private AffineTransform executeTransform(Graphics2D g2d, Point p) {
		float slope = (float) ((getEnd().getY() - getStart().getY()) / (getEnd().getX() - getStart().getX()));

		float rotation = (float) Math.toDegrees(Math.atan(slope));

		if (getEnd().getX() < getStart().getX())
			rotation -= 180;

		AffineTransform saveXform = g2d.getTransform();
		// if there's any slope we need to transform the graphics by the slope's
		// angle
		if (getSlope() != 0) {
			AffineTransform transform = new AffineTransform();
			transform.translate((int) p.getX(), (int) p.getY());
			transform.rotate(Math.toRadians(rotation));
			transform.translate((int) -p.getX(), (int) -p.getY());
			g2d.transform(transform);
		}
		return saveXform;
	}

	@Override
	public int getWidth() {
		return (int) Math.abs(getStart().getX() - getEnd().getX());
	}

	@Override
	public int getHeight() {
		return (int) Math.abs(getStart().getY() - getEnd().getY());
	}

	@Override
	public int getXLoc() {
		return (int) getMidPoint().getX();
	}

	@Override
	public int getYLoc() {
		return (int) getMidPoint().getY();
	}

	/**
	 * Determines the slope of a line based on two points lying on the line
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double getSlope(double x1, double y1, double x2, double y2) {
		return (y2 - y1) / (x2 - x1);
	}

	public double getSlope() {
		return getSlope(getStart().getX(), getStart().getY(), getEnd().getX(), getEnd().getY());
	}

	/**
	 * Determines the axis interception of line based on a point and a slope
	 * 
	 * @param y2
	 * @param slope
	 * @param x2
	 * @return
	 */
	public float getAxisIntercept(float y2, float slope, float x2) {
		return y2 - (slope * x2);
	}

	public boolean containsPoint(float x, float y, int tolerance) {

		Point p = new Point();
		p.setLocation(x, y);

		if (getStart().equals(getEnd()))
			return false;

		return (distanceToSegment(getStart(), getEnd(), p) <= tolerance);

	}
	
	
	public boolean containsPoint(float x, float y) {

		Point p = new Point();
		p.setLocation(x, y);

		if (getStart().equals(getEnd()))
			return false;

		return (distanceToSegment(getStart(), getEnd(), p) <= TOLERANCE);

	}

	public boolean containsPoint(Point p) {

		if (getStart().equals(getEnd()))
			return false;

		return (distanceToSegment(getStart(), getEnd(), p) <= TOLERANCE);

	}

	public boolean containsPoint(Point p, int tolerance) {

		if (getStart().equals(getEnd()))
			return false;

		return (distanceToSegment(getStart(), getEnd(), p) <= tolerance);

	}
	
	
	public double getSlopeAngleInRadians() {
		return Math.atan(getSlope());
	}

	public double getInverseSlopeAngleInRadians() {
		return Math.atan(-getSlope());
	}

	/**
	 * Gets the mid point of this line segment
	 * 
	 * @return
	 */
	public Point getMidPoint() {

		int x = (int) ((getStart().getX() + getEnd().getX()) / 2);
		int y = (int) ((getStart().getY() + getEnd().getY()) / 2);

		midPoint.setLocation(x, y);

		return midPoint;

	}

	/**
	 * Returns the distance of p3 to the segment defined by p1,p2.
	 * 
	 * @see <a
	 *      href="http://local.wasp.uwa.edu.au/~pbourke/geometry/pointline/">Web
	 *      link</a>
	 * 
	 * @param p1 First point of the segment
	 * @param p2 Second point of the segment
	 * @param p3 Point to which we want to know the distance of the segment
	 *        defined by p1,p2
	 * @return The distance of p3 to the segment defined by p1,p2
	 */
	public static double distanceToSegment(Point p1, Point p2, Point p3) {

		final double xDelta = p2.getX() - p1.getX();
		final double yDelta = p2.getY() - p1.getY();

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException("p1 and p2 cannot be the same point");
		}

		final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point2D closestPoint;
		if (u < 0) {
			closestPoint = p1;
		}
		else if (u > 1) {
			closestPoint = p2;
		}
		else {
			closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
		}

		return closestPoint.distance(p3);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {

			Line line = (Line) obj;

			if (line.getStart().equals(start) && line.getEnd().equals(end) && line.getLayer() == layer)
				return true;

		}

		return false;

	}

	public float getLength() {
		return (float) Math.sqrt((getHeight() * getHeight()) + (getWidth() * getWidth()));
	}

	public void drawHandles(Graphics2D g2, int handleSize) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2.drawRect((int) getStart().getX() - handleSize / 2, (int) getStart().getY() - handleSize / 2, handleSize, handleSize);
		g2.drawRect((int) getEnd().getX() - handleSize / 2, (int) getEnd().getY() - handleSize / 2, handleSize, handleSize);
	}

	public boolean isInsideStartHandle(float x, float y) {

		if (x >= (int) getStart().getX() - HANDLE_SIZE / 2 && x <= (int) getStart().getX() + HANDLE_SIZE / 2 && y >= (int) getStart().getY() - HANDLE_SIZE / 2
				&& y <= (int) getStart().getY() + HANDLE_SIZE / 2)
			return true;

		return false;

	}

	public boolean isInsideEndHandle(float x, float y) {

		if (x >= (int) getEnd().getX() - HANDLE_SIZE / 2 && x <= (int) getEnd().getX() + HANDLE_SIZE / 2 && y >= (int) getEnd().getY() - HANDLE_SIZE / 2
				&& y <= (int) getEnd().getY() + HANDLE_SIZE / 2)
			return true;

		return false;

	}

	public Point getOldstart() {
		return oldstart;
	}

	public void setOldstart(Point oldstart) {
		this.oldstart = oldstart;
	}

	public Point getOldEnd() {
		return oldEnd;
	}

	public void setOldEnd(Point oldEnd) {
		this.oldEnd = oldEnd;
	}

	public LineEdgeType getStartType() {
		return startType;
	}

	public void setStartType(LineEdgeType startType) {
		this.startType = startType;
	}

	public LineEdgeType getEndType() {
		return endType;
	}

	public void setEndType(LineEdgeType endType) {
		this.endType = endType;
	}

	/**
	 * @return the stroke
	 */
	public BasicStroke getStroke() {
		return stroke;
	}

	/**
	 * @param stroke the stroke to set
	 */
	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.blackboard.components.Item#getBoundingBox()
	 */
	public Rectangle getBoundingBox() {

		int min_x = (int) Math.min(getStart().getX(), getEnd().getX());
		int min_y = (int) Math.min(getStart().getY(), getEnd().getY());
		int max_x = (int) Math.max(getStart().getX(), getEnd().getX());
		int max_y = (int) Math.max(getStart().getY(), getEnd().getY());

		int height = max_y - min_y;
		int width = max_x - min_x;

		return new Rectangle(min_x, min_y, width, height);

	}

	// Basically a line is a shape also, but it has no style

	@Override
	public ShapeStyle getStyle() {
		return null;
	}

	@Override
	public void setStyle(ShapeStyle style) {
	}

	/**
	 * @return the netIndex
	 */
	public int getNetIndex() {
		return netIndex;
	}

	/**
	 * @param netIndex the netIndex to set
	 */
	public void setNetIndex(int netIndex) {
		this.netIndex = netIndex;
	}

	/**
	 * @return the wireConnections
	 */
	public ArrayList<WireConnection> getWireConnections() {
		return wireConnections;
	}
	
	@Override
	public String toString() {
		return "[("+start.x+","+start.x+") | ("+end.x+","+end.y+")]";
	}
	
	
}
