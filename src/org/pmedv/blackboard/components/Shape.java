package org.pmedv.blackboard.components;

import java.awt.BasicStroke;

import org.pmedv.blackboard.ShapeStyle;

public interface Shape {

	public ShapeStyle getStyle();

	public void setStyle(ShapeStyle style);
	
	public BasicStroke getStroke();

	public void setStroke(BasicStroke stroke);
	
}
