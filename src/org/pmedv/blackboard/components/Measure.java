package org.pmedv.blackboard.components;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import org.pmedv.blackboard.tools.StrokeFactory;
import org.pmedv.blackboard.tools.StrokeFactory.StrokeType;


public class Measure extends Line {
	
	private String value = "none";
	
	public static final BasicStroke DEFAULT_STROKE = (BasicStroke) StrokeFactory.createStroke(3.0f, StrokeType.DASH_DOT); 
	
	public Measure(Point start, Point end, int index) {
		super(start,end, index);
		setStartType(LineEdgeType.STRAIGHT);
		setEndType(LineEdgeType.SIMPLE_ARROW);
		setStroke(DEFAULT_STROKE);
	}
	
	public Measure(int lineStartX, int lineStartY, int lineStopX, int lineStopY, int index) {
		super(lineStartX,lineStartY,lineStopX,lineStopY,index);
		setStartType(LineEdgeType.STRAIGHT);
		setEndType(LineEdgeType.SIMPLE_ARROW);
		setStroke(DEFAULT_STROKE);
	}

	@Override
	public void draw(Graphics g) {	
		super.draw(g);		
		Graphics2D g2d = (Graphics2D)g;		
		g2d.drawString(value, (int)getStart().getX()+10,(int)getStart().getY()+10);
		// g2d.drawString(name , (int)getStart().getX()+10,(int)getStart().getY()+20);
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
}
