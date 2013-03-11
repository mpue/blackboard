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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationPerspectiveList", propOrder = {
    "perspectives"
})

public class ApplicationPerspectiveList {

	private ArrayList<ApplicationPerspective> perspectives;

	public ApplicationPerspectiveList() {
		perspectives = new ArrayList<ApplicationPerspective>();
	}
	
	/**
	 * @return the perspectives
	 */
	public ArrayList<ApplicationPerspective> getPerspectives() {
		return perspectives;
	}

	/**
	 * @param perspectives the perspectives to set
	 */
	public void setPerspectives(ArrayList<ApplicationPerspective> perspectives) {
		this.perspectives = perspectives;
	}
	
}
