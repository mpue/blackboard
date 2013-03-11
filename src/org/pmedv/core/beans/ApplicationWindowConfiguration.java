/**

	BlackBoard BreadBoard Designer
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
package org.pmedv.core.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationWindowConfiguration", propOrder = {
	"title",
	"lastPerspectiveID",
	"x",
	"y",
	"width",
	"height",
	"statusbarVisible",
	"maximized",
	"dividerLocation",
	"symbolDividerLocation"
})

public class ApplicationWindowConfiguration {
	
	private String title;	
	private String lastPerspectiveID;
	private int x;
	private int y;
	private int height;
	private int width;
	private boolean statusbarVisible;
	private boolean maximized;
	private int dividerLocation;
	private int symbolDividerLocation;
		
	/**
	 * @return the lastPerspectiveID
	 */
	public String getLastPerspectiveID() {
		return lastPerspectiveID;
	}

	/**
	 * @param lastPerspectiveID the lastPerspectiveID to set
	 */
	public void setLastPerspectiveID(String lastPerspectiveID) {
		this.lastPerspectiveID = lastPerspectiveID;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	
	/**
	 * @return the x
	 */
	public int getX() {
	
		return x;
	}

	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
	
		this.x = x;
	}

	
	/**
	 * @return the y
	 */
	public int getY() {
	
		return y;
	}

	
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
	
		this.y = y;
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
	 * @return the statusbarVisible
	 */
	public boolean isStatusbarVisible() {
	
		return statusbarVisible;
	}

	
	/**
	 * @return the maximized
	 */
	public boolean isMaximized() {
		return maximized;
	}

	/**
	 * @param maximized the maximized to set
	 */
	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	/**
	 * @param statusbarVisible the statusbarVisible to set
	 */
	public void setStatusbarVisible(boolean statusbarVisible) {
	
		this.statusbarVisible = statusbarVisible;
	}

	public int getDividerLocation() {
		return dividerLocation;
	}

	public void setDividerLocation(int dividerLocation) {
		this.dividerLocation = dividerLocation;
	}

	public int getSymbolDividerLocation() {
		return symbolDividerLocation;
	}

	public void setSymbolDividerLocation(int symbolDividerLocation) {
		this.symbolDividerLocation = symbolDividerLocation;
	}

}
