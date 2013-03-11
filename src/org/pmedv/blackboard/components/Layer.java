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
package org.pmedv.blackboard.components;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.pmedv.blackboard.beans.LayerBean;

/**
 * The {@link Layer} is a container for {@link Part} , {@link Resistor} and
 * {@link Line} objects. A board typically contains three {@link Layer} objects.
 * One for the bottom, one for the top and one for the parts.
 * 
 * @author Matthias Pueski (14.06.2011)
 * 
 */
public class Layer implements Comparable<Layer> {
	
	private ArrayList<Item> items;
	private Integer index;
	private boolean visible = true;
	private boolean locked  = false;
	private String name;
	private float opacity = 1.0f;
	private Color color = Color.BLACK;
	
	private BufferedImage image;
	
	public Layer(int index, String name) {
		items = new ArrayList<Item>();
		this.index = index;
		this.name = name;
	}

	public Layer(int index, String name, Color color) {
		items = new ArrayList<Item>();
		this.index = index;
		this.name = name;
		this.color = color;
	}
	
	
	public Layer(LayerBean layerBean) {
		items = new ArrayList<Item>();
		this.name = layerBean.getName();
		this.index = layerBean.getIndex();
		this.visible = layerBean.isVisible();
		this.locked = layerBean.isLocked();
		this.opacity = layerBean.getOpacity();
		this.color = new Color(layerBean.getColor().getRed(),layerBean.getColor().getGreen(),layerBean.getColor().getBlue());
	}

	/**
	 * @return the items
	 */
	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			Layer layer = (Layer) obj;
			if (layer.getName().equals(name))
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
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return name.toString();
	}
	
	@Override
	public int compareTo(Layer o) {		
		return getIndex().compareTo(o.getIndex());		
	}
	
}
