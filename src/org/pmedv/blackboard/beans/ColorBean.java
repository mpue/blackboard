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

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Container object for the {@link Color}
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Color", propOrder = { "red", "green", "blue" })
public class ColorBean {

	/**
	 * @param blue the blue to set
	 */
	public void setBlue(int blue) {
		this.blue = blue;
	}

	private int red;
	private int green;
	private int blue;

	public ColorBean() {

	}

	public ColorBean(Color color) {
		if (color != null) {
			this.red = color.getRed();
			this.green = color.getGreen();
			this.blue = color.getBlue();
		}
	}

	public ColorBean(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	/**
	 * @return the red
	 */
	public int getRed() {
		return red;
	}

	/**
	 * @param red the red to set
	 */
	public void setRed(int red) {
		this.red = red;
	}

	/**
	 * @return the green
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * @param green the green to set
	 */
	public void setGreen(int green) {
		this.green = green;
	}

	/**
	 * @return the blue
	 */
	public int getBlue() {
		return blue;
	}

}
