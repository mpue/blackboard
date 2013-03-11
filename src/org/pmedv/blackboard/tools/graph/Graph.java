package org.pmedv.blackboard.tools.graph;

import java.awt.Color;
import java.awt.Point;

public class Graph {
	
	private Color color;	
	private Point[] points;

	public Graph(Point[] points, Color color) {
		this.color = color;
		this.points = points;		
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the points
	 */
	public Point[] getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Point[] points) {
		this.points = points;
	}
	
}
