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

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Resistor;

/**
 * The {@link Layer} is a container for {@link Part} , {@link Resistor} and
 * {@link Line} objects. A board typically contains three {@link Layer} objects.
 * One for the bottom, one for the top and one for the parts.
 * 
 * @author Matthias Pueski (14.06.2011)
 * 
 */
@XmlType(propOrder = { "index", "name", "visible","locked","opacity","color" })
@XmlAccessorType(XmlAccessType.FIELD)
public class LayerBean {
	
	private int index;
	private boolean visible = true;
	private boolean locked  = false; 
	private String name;
	private float opacity;
	private ColorBean color = new ColorBean(Color.BLACK);
	
	public LayerBean(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public LayerBean() {
	}

	public LayerBean(Layer layer) {
		this.index = layer.getIndex();
		this.name = layer.getName();
		this.visible = layer.isVisible();
		this.locked = layer.isLocked();
		this.opacity = layer.getOpacity();
		this.color = new ColorBean(layer.getColor());
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
			Layer layer = (Layer) obj;
			if (layer.getIndex() == index)
				return true;
		}
		return false;
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

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the opacity
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * @param opacity the opacity to set
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	/**
	 * @return the color
	 */
	public ColorBean getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(ColorBean color) {
		this.color = color;
	}
	
	
}

