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

import org.pmedv.blackboard.components.Diode;
import org.pmedv.blackboard.components.Diode.DiodeType;

/**
 * Container object for a {@link Diode}
 * 
 * @author Matthias Pueski (24.10.2010)
 * 
 */
@XmlType(propOrder = { "value","type" })
@XmlAccessorType(XmlAccessType.FIELD)
public class DiodeBean extends LineBean {

	private String value;
	private DiodeType type;

	public DiodeBean() {
		super();
	}

	public DiodeBean(int x1, int y1, int x2, int y2, int index, String value, DiodeType type) {
		super(x1, y1, x2, y2, index);
		this.value = value;
		this.type = type;
	}

	public DiodeBean(Diode diode) {
		this.name = diode.getName();
		this.start = new PointBean(diode.getStart());
		this.end = new PointBean(diode.getEnd());
		this.layer = diode.getLayer();
		this.value = diode.getValue();
		this.index = diode.getIndex();
		this.type = diode.getType();
	}

	public DiodeBean(PointBean start, PointBean end, int index, String value) {
		super(start, end, index);
		this.value = value;
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
	
	/**
	 * @return the type
	 */
	public DiodeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DiodeType type) {
		this.type = type;
	}
}
