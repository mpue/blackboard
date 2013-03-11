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
package org.pmedv.blackboard.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.components.Part;

/**
 * Container object for a {@link Part}
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Part", propOrder = {"xLoc", "yLoc", "index", "color", "layer","name","width","height", "filename", "rotation","value"})
public class PartBean {
	
	protected int xLoc;
	protected int yLoc;
	protected int index;
	protected ColorBean color;
	protected int layer;
	protected String name;
	protected int width;
	protected int height;
	
	private String filename;
	protected String value;
	protected int rotation = 0;
	
	public PartBean() {
	}

	public PartBean(Part part) {
		filename = part.getFilename();
		rotation = part.getRotation();
		xLoc = part.getXLoc();
		yLoc = part.getYLoc();
		if (part.getColor() != null)
			color = new ColorBean(part.getColor());
		index = part.getIndex();
		name = part.getName();
		value = part.getValue();
		layer = part.getLayer();
	}	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			PartBean part = (PartBean) obj;
			if (part.getXLoc() == getXLoc() && part.getYLoc() == getYLoc() && part.getFilename().equals(getFilename()))
				return true;
		}
		return false;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	 * @return the name
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param name the name to set
	 */
	public void setFilename(String name) {
		this.filename = name;
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
