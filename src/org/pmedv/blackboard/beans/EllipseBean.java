package org.pmedv.blackboard.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.components.Ellipse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ellipse", propOrder = { "xLoc", "yLoc", "index", "color", "layer", "width","height","thickness","dashArray","dashPhase","endCap","lineJoin","miterLimit","style","startAngle","rotation" })

public class EllipseBean  {
	
	protected int xLoc;
	protected int yLoc;
	protected int index;
	protected ColorBean color;
	protected int layer;
	
	private int width;
	private int height;
	
	private float thickness;
	private ShapeStyle style;
	
	private float[] dashArray;
	private float dashPhase;
	private int endCap;
	private int lineJoin;
	private float miterLimit;
	
	private int startAngle;
	private int rotation;	
	
	public EllipseBean() {		
	}
	
	public EllipseBean(Ellipse ellipse) {
		this.color = new ColorBean(ellipse.getColor());
		this.xLoc = ellipse.getXLoc();
		this.yLoc = ellipse.getYLoc();
		this.height = ellipse.getHeight();
		this.width = ellipse.getWidth();
		this.index = ellipse.getIndex();
		this.layer = ellipse.getLayer();
		this.thickness = ellipse.getStroke().getLineWidth();
		this.dashArray = ellipse.getStroke().getDashArray();
		this.dashPhase = ellipse.getStroke().getDashPhase();
		this.endCap = ellipse.getStroke().getEndCap();
		this.lineJoin = ellipse.getStroke().getLineJoin();
		this.miterLimit = ellipse.getStroke().getMiterLimit();

		this.style = ellipse.getStyle();
		this.startAngle = ellipse.getStartAngle();
		this.rotation = ellipse.getRotation();
	}
	
	
	
	public int getXLoc() {
		return xLoc;
	}

	public void setXLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	public int getYLoc() {
		return yLoc;
	}

	public void setYLoc(int yLoc) {
		this.yLoc = yLoc;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ColorBean getColor() {
		return color;
	}

	public void setColor(ColorBean color) {
		this.color = color;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
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

	/**
	 * @return the dashArray
	 */
	public float[] getDashArray() {
		return dashArray;
	}

	/**
	 * @param dashArray the dashArray to set
	 */
	public void setDashArray(float[] dashArray) {
		this.dashArray = dashArray;
	}

	/**
	 * @return the dashPhase
	 */
	public float getDashPhase() {
		return dashPhase;
	}

	/**
	 * @param dashPhase the dashPhase to set
	 */
	public void setDashPhase(float dashPhase) {
		this.dashPhase = dashPhase;
	}

	/**
	 * @return the endCap
	 */
	public int getEndCap() {
		return endCap;
	}

	/**
	 * @param endCap the endCap to set
	 */
	public void setEndCap(int endCap) {
		this.endCap = endCap;
	}

	/**
	 * @return the lineJoin
	 */
	public int getLineJoin() {
		return lineJoin;
	}

	/**
	 * @param lineJoin the lineJoin to set
	 */
	public void setLineJoin(int lineJoin) {
		this.lineJoin = lineJoin;
	}

	/**
	 * @return the miterLimit
	 */
	public float getMiterLimit() {
		return miterLimit;
	}

	/**
	 * @param miterLimit the miterLimit to set
	 */
	public void setMiterLimit(float miterLimit) {
		this.miterLimit = miterLimit;
	}
	
	

}
