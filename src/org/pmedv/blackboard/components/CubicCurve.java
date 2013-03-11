package org.pmedv.blackboard.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.pmedv.blackboard.ShapeStyle;

public class CubicCurve extends Part implements Shape {

	private CubicCurve2D.Float curve;
	private ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();

	private ShapeStyle style;
	private Stroke stroke;

	Point2D.Float p1;
	Point2D.Float p2;
	Point2D.Float ctrl1;
	Point2D.Float ctrl2;

	public CubicCurve(Point2D.Float p1, Point2D.Float p2, Point2D.Float ctrl1, Point2D.Float ctrl2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.ctrl1 = ctrl1;
		this.ctrl2 = ctrl2;
		curve = new CubicCurve2D.Float(p1.x, p1.y, ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, p2.x, p2.y);
		points.add(p1);
		points.add(p2);
		points.add(ctrl1);
		points.add(ctrl2);
		color = Color.BLACK;
		opacity = 1.0f;
		style = ShapeStyle.OUTLINED;
		width = getBoundingBox().width;
		height = getBoundingBox().height;
	}

	@Override
	public ShapeStyle getStyle() {
		return style;
	}

	@Override
	public void setStyle(ShapeStyle style) {
		this.style = style;
	}

	@Override
	public BasicStroke getStroke() {
		return (BasicStroke) stroke;
	}

	@Override
	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);
		g2.setColor(color);
//		g2.setStroke(stroke);
		curve.ctrlx1 = ctrl1.x + getxLoc();
		curve.ctrly1 = ctrl1.y + getyLoc();
		curve.ctrlx2 = ctrl2.x + getxLoc();
		curve.ctrly2 = ctrl2.y + getyLoc();
		curve.x1 = p1.x + getxLoc();
		curve.y1 = p1.y + getyLoc();
		curve.x2 = p2.x + getxLoc();
		curve.y2 = p2.y + getyLoc();		
		g2.draw(curve);
	}

	@Override
	protected void drawHandles(Graphics2D g2, int handleSize) {
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

		g2.draw(getBoundingBox());
		
		g2.setColor(Color.RED);
		g2.fillOval((int) ctrl1.x+getxLoc(), (int) ctrl1.y+getyLoc(), handleSize, handleSize);
		g2.fillOval((int) ctrl2.x+getxLoc(), (int) ctrl2.y+getyLoc(), handleSize, handleSize);
		
		g2.setColor(Color.GREEN);
		g2.fillOval((int) p1.x+getxLoc(), (int) p1.y+getyLoc(), handleSize, handleSize);
		g2.fillOval((int) p2.x+getxLoc(), (int) p2.y+getyLoc(), handleSize, handleSize);
		
		
	}

	@Override
	public Rectangle getBoundingBox() {
		return curve.getBounds();
	}

}
