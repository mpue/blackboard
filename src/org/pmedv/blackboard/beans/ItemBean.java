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

import org.pmedv.blackboard.components.Item;

/**
 * Container object for an {@link Item}
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
@XmlType(propOrder = { "xLoc", "yLoc", "index", "color", "layer","name","width","height" })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ItemBean {
	protected int xLoc;
	protected int yLoc;
	protected int index;
	protected ColorBean color;
	protected int layer;
	protected String name;
	protected int width;
	protected int height;
	
	public ItemBean() {
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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
