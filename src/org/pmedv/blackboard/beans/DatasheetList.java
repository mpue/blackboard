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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * The <code>DatasheetList</code> contains a collection of all
 * available {@link DatasheetBean} objects. It is being retrieved
 * at application start and can be changed at any time.
 * </p>
 * <p>
 * A {@link DatasheetBean} can resist at its original place and
 * can be copied as well to the local datasheet storage.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
@XmlRootElement
@XmlType(propOrder = {"datasheets"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DatasheetList {

	private ArrayList<DatasheetBean> datasheets;

	public DatasheetList() {
		datasheets = new ArrayList<DatasheetBean>();
	}
	
	/**
	 * Gets the available datasheets.
	 * 
	 * @return
	 */
	public ArrayList<DatasheetBean> getDatasheets() {
		return datasheets;
	}

	/**
	 * Sets the available datasheets.
	 * 
	 * @param datasheets
	 */
	public void setDatasheets(ArrayList<DatasheetBean> datasheets) {
		this.datasheets = datasheets;
	}
	
	
	
}
