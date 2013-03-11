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

/**
 * The memento interface ist used to persist the state of application parts
 * like views and editors. Parts that implement this interface will be able
 * to persist thier state, when the application is being closed.
 * 
 * @author Matthias Pueski
 *
 */
public interface IMemento {
	
	/**
	 * Persists the state of the component, this method is called
	 * when the application is being closed
	 */
	public void saveState();
	
	
	/**
	 * Restores the state of the view after startup
	 */
	public void loadState();


}
