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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * This is the abstract base class for all items inside this application.
 * 
 * @see {@link Resistor}
 * @see {@link Line}
 * @see {@link Part}
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
@XmlType(propOrder = { "width", "height", "xLoc", "yLoc", "layer", "name" })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Item implements Cloneable, Comparable<Item>, Drawable {
	
	protected static final Font f = new Font("Arial", Font.BOLD, 16);
	
	protected static final int HANDLE_SIZE = 8;
	
	protected int width;
	protected int height;
	@XmlTransient
	protected int oldWidth;
	@XmlTransient
	protected int oldHeight;	
	private int xLoc;
	private int yLoc;
	@XmlTransient
	protected int oldXLoc;
	@XmlTransient
	protected int oldYLoc;
	@XmlTransient
	protected Integer index;
	@XmlTransient
	protected Color color;
	protected String name;
	protected int layer;
	@XmlTransient
	protected float opacity = 1.0f;
	@XmlTransient
	boolean resizable;	
	@XmlTransient
	boolean mirror = false;
	@XmlTransient
	boolean sticky = false;
	
	protected Item() {
		index = 0;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * @return the oldWidth
	 */
	public int getOldWidth() {
		return oldWidth;
	}

	/**
	 * @param oldWidth the oldWidth to set
	 */
	public void setOldWidth(int oldWidth) {
		this.oldWidth = oldWidth;
	}

	/**
	 * @return the oldHeight
	 */
	public int getOldHeight() {
		return oldHeight;
	}

	/**
	 * @param oldHeight the oldHeight to set
	 */
	public void setOldHeight(int oldHeight) {
		this.oldHeight = oldHeight;
	}

	/**
	 * @return the xLoc
	 */
	public int getXLoc() {
		return getxLoc();
	}

	/**
	 * @param loc the xLoc to set
	 */
	public void setXLoc(int loc) {
		setxLoc(loc);
	}

	/**
	 * @return the yLoc
	 */
	public int getYLoc() {
		return getyLoc();
	}

	/**
	 * @param loc the yLoc to set
	 */
	public void setYLoc(int loc) {
		setyLoc(loc);
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

	/**
	 * Checks if a given point with the coordinates (x,y) is inside the bounding
	 * rectangle of this item
	 * 
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @return
	 */
	public boolean isInside(int x, int y) {		
		Rectangle r = getBoundingBox();	
		return x >= r.getX() && x <= r.getX() + r.getWidth() && y >= r.getY() && y <= r.getY() + r.getHeight();
	}
	
	
	public boolean isInsideNorthWestHandle(float x, float y) {
		
		int handleX = getxLoc()-HANDLE_SIZE/2;
		int handleY = getyLoc()-HANDLE_SIZE/2;
		
		return (x >= handleX && x <= handleX+HANDLE_SIZE &&
				y >= handleY && y <= handleY+HANDLE_SIZE);
		
	}

	public boolean isInsideNorthEastHandle(float x, float y) {
		
		int handleX = getxLoc()+width-HANDLE_SIZE/2;
		int handleY = getyLoc()-HANDLE_SIZE/2;
		
		return (x >= handleX && x <= handleX+HANDLE_SIZE &&
				y >= handleY && y <= handleY+HANDLE_SIZE);
		
	}

	public boolean isInsideSouthWestHandle(float x, float y) {
		
		int handleX = getxLoc()-HANDLE_SIZE/2;
		int handleY = getyLoc()+height-HANDLE_SIZE/2;
		
		return (x >= handleX && x <= handleX+HANDLE_SIZE &&
				y >= handleY && y <= handleY+HANDLE_SIZE);
		
	}
	
	public boolean isInsideSouthEastHandle(float x, float y) {
		
		int handleX = getxLoc()+width-HANDLE_SIZE/2;
		int handleY = getyLoc()+height-HANDLE_SIZE/2;
		
		return (x >= handleX && x <= handleX+HANDLE_SIZE &&
				y >= handleY && y <= handleY+HANDLE_SIZE);
		
	}	

	public abstract void draw(Graphics g);

	protected abstract void drawHandles(Graphics2D g2, int handleSize);

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			Item item = (Item) obj;
			return index == item.getIndex();				
		}
		return false;
	}

	@Override
	public int compareTo(Item o) {		
		return getIndex().compareTo(o.getIndex());		
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return the oldXLoc
	 */
	public int getOldXLoc() {
		return oldXLoc;
	}

	/**
	 * @param oldXLoc the oldXLoc to set
	 */
	public void setOldXLoc(int oldXLoc) {
		this.oldXLoc = oldXLoc;
	}

	/**
	 * @return the oldYLoc
	 */
	public int getOldYLoc() {
		return oldYLoc;
	}

	/**
	 * @param oldYLoc the oldYLoc to set
	 */
	public void setOldYLoc(int oldYLoc) {
		this.oldYLoc = oldYLoc;
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
	 * @return the resizable
	 */
	public boolean isResizable() {
		return resizable;
	}
	
	/**
	 * @param resizable the resizable to set
	 */
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public abstract Rectangle getBoundingBox();
	
	public boolean isMirror() {
		return mirror;
	}

	public void setMirror(boolean mirror) {
		this.mirror = mirror;
	}

	/**
	 * @return the sticky
	 */
	public boolean isSticky() {
		return sticky;
	}

	/**
	 * @param sticky the sticky to set
	 */
	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	@Override
	public void drawItem(Graphics g) {
		draw(g);		
	}

	public int getyLoc() {
		return yLoc;
	}

	public void setyLoc(int yLoc) {
		this.yLoc = yLoc;
	}

	public int getxLoc() {
		return xLoc;
	}

	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}
}
