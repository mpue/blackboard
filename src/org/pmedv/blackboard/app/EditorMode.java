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
package org.pmedv.blackboard.app;


/**
 * The {@link EditorMode} determines whether we are in select or in draw mode
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
public enum EditorMode {
	
	SELECT,
	DRAW_LINE,
	DRAW_RECTANGLE,
	DRAW_ELLIPSE,
	CHECK_CONNECTIONS,
	CROP,
	DRAW_MEASURE,
	MOVE

}
