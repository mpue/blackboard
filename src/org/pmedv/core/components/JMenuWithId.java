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
package org.pmedv.core.components;

import javax.swing.JMenu;

/**
 * This derivative of JMenu allows an JMenu to be associated with an id, 
 * this is nessesary because of the perspective mechanism. Since every perspective
 * has its own menu contribution it must be determined which menu belongs to which perspective.
 * 
 * @see org.pmedv.core.commands.OpenPerspectiveCommand
 * 
 * 
 * @author Matthias Puski
 *
 */


public class JMenuWithId extends JMenu {

	private static final long serialVersionUID = -8170102466296811020L;

	private String id;

	public JMenuWithId(String text) {
		super(text);		
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	

}
