package org.pmedv.blackboard.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.components.Box;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Box", propOrder = { "xLoc", "yLoc", "index", "color", "layer","width","height","thickness","dashArray","dashPhase","endCap","lineJoin","miterLimit","style" })

public class BoxBean  {
	
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
	
	public BoxBean() {		
	}
	
	public BoxBean(Box box) {
		this.color = new ColorBean(box.getColor());
		this.xLoc = box.getXLoc();
		this.yLoc = box.getYLoc();
		this.height = box.getHeight();
		this.width = box.getWidth();
		this.index = box.getIndex();
		this.layer = box.getLayer();
		this.thickness = box.getStroke().getLineWidth();
		this.dashArray = box.getStroke().getDashArray();
		this.dashPhase = box.getStroke().getDashPhase();
		this.endCap = box.getStroke().getEndCap();
		this.lineJoin = box.getStroke().getLineJoin();
		this.miterLimit = box.getStroke().getMiterLimit();
		this.style = box.getStyle();
	}
	
	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}

	/**
	 * @return the xLoc
	 */
	public int getXLoc() {
		return xLoc;
	}

	/**
	 * @param loc the xLoc to set
	 */
	public void setXLoc(int loc) {
		xLoc = loc;
	}

	/**
	 * @return the yLoc
	 */
	public int getYLoc() {
		return yLoc;
	}

	/**
	 * @param loc the yLoc to set
	 */
	public void setYLoc(int loc) {
		yLoc = loc;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			ItemBean item = (ItemBean) obj;
			if (index == item.getIndex())
				return true;
		}
		return false;
	}

	public ColorBean getColor() {
		return color;
	}

	public void setColor(ColorBean color) {
		this.color = color;
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
