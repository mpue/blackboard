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

import org.pmedv.blackboard.components.Resistor;

/**
 * Container object for a {@link Resistor}
 * 
 * @author Matthias Pueski (24.10.2010)
 * 
 */
@XmlType(propOrder = { "value","tolerance"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ResistorBean extends LineBean {

	private int value;

	private float tolerance;
	
	public ResistorBean() {
		super();
	}

	public ResistorBean(int x1, int y1, int x2, int y2, int index, int value) {
		super(x1, y1, x2, y2, index);
		this.value = value;
	}

	public ResistorBean(Resistor resistor) {
		this.name = resistor.getName();
		this.start = new PointBean(resistor.getStart());
		this.end = new PointBean(resistor.getEnd());
		this.layer = resistor.getLayer();
		this.value = resistor.getValue();
		this.index = resistor.getIndex();
		this.tolerance = resistor.getTolerance();
	}

	public ResistorBean(PointBean start, PointBean end, int index, int value) {
		super(start, end, index);
		this.value = value;
		this.tolerance = 5.0f;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the tolerance
	 */
	public float getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance the tolerance to set
	 */
	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}
	
	
}
