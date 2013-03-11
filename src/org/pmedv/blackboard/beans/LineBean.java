/**

	BreadBoard Editor
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
package org.pmedv.blackboard.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.LineEdgeType;

/**
 * Container object for a {@link Line}
 * 
 * @author Matthias Pueski (14.06.2011)
 * 
 */
@XmlType(propOrder = {"name","xLoc", "yLoc", "index", "color", "layer", "start", "end", "startType", "endType","thickness","dashArray","dashPhase","endCap","lineJoin","miterLimit"})
@XmlAccessorType(XmlAccessType.FIELD)
public class LineBean  {

	protected String name;
	
	protected int xLoc;
	protected int yLoc;
	protected int index;
	protected ColorBean color;
	protected int layer;
	
	protected PointBean start;
	protected PointBean end;

	private LineEdgeType startType;
	private LineEdgeType endType;
	
	private float thickness;
	
	private float[] dashArray;
	private float dashPhase;
	private int endCap;
	private int lineJoin;
	private float miterLimit;
	
	public LineBean() {
	}

	public LineBean(PointBean start, PointBean end, int index) {
		this.start = start;
		this.end = end;
		this.index = index;
	}

	public LineBean(int x1, int y1, int x2, int y2, int index) {
		PointBean p1 = new PointBean(x1, y1);
		PointBean p2 = new PointBean(x2, y2);
		this.start = p1;
		this.end = p2;
		this.index = index;
	}

	public LineBean(Line line) {
		this.start = new PointBean(line.getStart());
		this.end = new PointBean(line.getEnd());
		this.color = new ColorBean(line.getColor());
		this.layer = line.getLayer();
		this.startType = line.getStartType();
		this.endType = line.getEndType();
		this.thickness = line.getStroke().getLineWidth();
		this.dashArray = line.getStroke().getDashArray();
		this.dashPhase = line.getStroke().getDashPhase();
		this.endCap = line.getStroke().getEndCap();
		this.lineJoin = line.getStroke().getLineJoin();
		this.miterLimit = line.getStroke().getMiterLimit();

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getxLoc() {
		return xLoc;
	}

	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	public int getyLoc() {
		return yLoc;
	}

	public void setyLoc(int yLoc) {
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

	/**
	 * @return the start
	 */
	public PointBean getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(PointBean start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public PointBean getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(PointBean end) {
		this.end = end;
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
	 * @return the thickness
	 */
	public float getThickness() {
		return thickness;
	}

	/**
	 * @param thickness the thickness to set
	 */
	public void setThickness(float thickness) {
		this.thickness = thickness;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			LineBean line = (LineBean) obj;
			if (line.getStart().equals(start) && line.getEnd().equals(end) && line.getLayer() == layer)
				return true;
		}
		return false;
	}
}
