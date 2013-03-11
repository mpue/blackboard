package org.pmedv.blackboard.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.beans.BoxBean;

/**
 * @author Matthias Pueski (24.06.2011)
 *
 */
public class Box extends Part implements Shape {

	private BasicStroke stroke;
	private ShapeStyle style;
	
	public Box() {
		super();
		style = ShapeStyle.OUTLINED;
		this.resizable = true;
	}

	public Box(BoxBean boxBean) {
		this.color = new Color(boxBean.getColor().getRed(), boxBean.getColor().getGreen(), boxBean.getColor().getBlue());
		this.setxLoc(boxBean.getXLoc());
		this.setyLoc(boxBean.getYLoc());
		this.oldXLoc = this.getxLoc();
		this.oldYLoc = this.getyLoc();
		this.height = boxBean.getHeight();
		this.width = boxBean.getWidth();
		this.oldHeight = this.height;
		this.oldWidth = this.width;
		this.index = boxBean.getIndex();
		this.layer = boxBean.getLayer();	
		if (boxBean.getThickness() > 0.0f) {
			if (boxBean.getMiterLimit() < 1) boxBean.setMiterLimit(1);
			this.stroke = 
				new BasicStroke(boxBean.getThickness(),
						boxBean.getEndCap(),
						boxBean.getLineJoin(),
						boxBean.getMiterLimit(),
						boxBean.getDashArray(),
						boxBean.getDashPhase());
		}
		else {
			setStroke(new BasicStroke(3.0f));
		}
		this.style = boxBean.getStyle();
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
			g2d.fillRect(getxLoc(), getyLoc(), width, height);	
		}
		else if (style.equals(ShapeStyle.OUTLINED)) {
			g2d.setStroke(stroke);
			g2d.drawRect(getxLoc(), getyLoc(), width, height);
		}
		
	}
	
	@Override
	public String toString() {
		return "[Box, location="+getxLoc()+":"+getyLoc()+", height="+height+", width="+width+", index="+index+", layer="+layer+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null != obj && this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			Box box = (Box) obj;
			if (box.getXLoc() == getXLoc() && box.getYLoc() == getYLoc() && box.getIndex() == index)
				return true;
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Box box = new Box();
		box.setColor(getColor());
		box.setHeight(getHeight());
		box.setIndex(getIndex());
		box.setName(getName());
		box.setValue(getValue());
		box.setRotation(getRotation());
		box.setWidth(getWidth());
		box.setXLoc(getXLoc());
		box.setYLoc(getYLoc());
		box.setLayer(getLayer());
		box.setStroke(getStroke());
		box.setStyle(getStyle());
		return box;
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

}
