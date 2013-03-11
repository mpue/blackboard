package org.pmedv.blackboard.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.beans.EllipseBean;

/**
 * @author Matthias Pueski (24.06.2011)
 *
 */
public class Ellipse extends Part implements Shape  {

	private BasicStroke stroke;
	private ShapeStyle style;
	private int startAngle = 0;
	private int rotation = 360;
	
	public Ellipse() {
		super();
		style = ShapeStyle.OUTLINED;
		this.resizable = true;
	}

	public Ellipse(EllipseBean ellipseBean) {
		this.color = new Color(ellipseBean.getColor().getRed(), ellipseBean.getColor().getGreen(), ellipseBean.getColor().getBlue());
		this.setxLoc(ellipseBean.getXLoc());
		this.setyLoc(ellipseBean.getYLoc());
		this.oldXLoc = this.getxLoc();
		this.oldYLoc = this.getyLoc();
		this.height = ellipseBean.getHeight();
		this.width = ellipseBean.getWidth();
		this.oldHeight = this.height;
		this.oldWidth = this.width;
		this.index = ellipseBean.getIndex();
		this.layer = ellipseBean.getLayer();
		if (ellipseBean.getThickness() > 0.0f) {
			if (ellipseBean.getMiterLimit() < 1) ellipseBean.setMiterLimit(1);
			this.stroke = 
				new BasicStroke(ellipseBean.getThickness(),
					ellipseBean.getEndCap(),
					ellipseBean.getLineJoin(),
					ellipseBean.getMiterLimit(),
					ellipseBean.getDashArray(),
					ellipseBean.getDashPhase());
		}
		else {
			setStroke(new BasicStroke(3.0f));
		}
		this.style = ellipseBean.getStyle();
		this.startAngle = ellipseBean.getStartAngle();
		this.rotation = ellipseBean.getRotation();
		this.resizable = true;
	}
	
	@Override
	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		g2d.setColor(color);
		
		if (style == null || style.equals(ShapeStyle.FILLED)) {
			g2d.fillArc(getxLoc(), getyLoc(), width, height,startAngle, rotation);
		}
		else if (style.equals(ShapeStyle.OUTLINED)) {
			g2d.setStroke(stroke);
			g2d.drawArc(getxLoc(), getyLoc(), width, height,startAngle, rotation);
		}
		
	}
	
	@Override
	public String toString() {
		return "[Ellipse, location="+getxLoc()+":"+getyLoc()+", height="+height+", width="+width+", index="+index+", layer="+layer+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null != obj && this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			Ellipse box = (Ellipse) obj;
			if (box.getXLoc() == getXLoc() && box.getYLoc() == getYLoc() && box.getIndex() == index)
				return true;
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Ellipse ellipse = new Ellipse();
		ellipse.setColor(getColor());
		ellipse.setHeight(getHeight());
		ellipse.setIndex(getIndex());
		ellipse.setName(getName());
		ellipse.setValue(getValue());
		ellipse.setRotation(getRotation());
		ellipse.setWidth(getWidth());
		ellipse.setXLoc(getXLoc());
		ellipse.setYLoc(getYLoc());
		ellipse.setLayer(getLayer());
		ellipse.setStroke(getStroke());
		ellipse.setStyle(getStyle());
		ellipse.setRotation(getRotation());
		ellipse.setStartAngle(getStartAngle());
		return ellipse;
	}	
	
	public BasicStroke getStroke() {
		return stroke;
	}

	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public ShapeStyle getStyle() {
		return style;
	}

	public void setStyle(ShapeStyle style) {
		this.style = style;
	}

	/**
	 * @return the startAngle
	 */
	public int getStartAngle() {
		return startAngle;
	}

	/**
	 * @param startAngle the startAngle to set
	 */
	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	

}
