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
package org.pmedv.core.html;

public interface Attribute {
	
	public static final String ATT_CLASS = " class";
	public static final String ATT_STYLE = " style";
	public static final String ATT_ID 	 = " id";
	public static final String ATT_TITLE = " title";
	public static final String ATT_ALIGN = " align";
	public static final String ATT_VALIGN = " valign";
	public static final String ATT_HEIGHT = " height";
	public static final String ATT_WIDTH  = " width";
	public static final String ATT_BORDER = " border";
	public static final String ATT_BGCOLOR = " bgcolor";
	public static final String ATT_COLSPAN = " colspan";
	public static final String ATT_CELLSPACING = " cellspacing";
	public static final String ATT_CELLPADDING = " cellpadding";
	public static final String ATT_HREF = " href";
	public static final String ATT_REL = " rel";
	public static final String ATT_TARGET =" target";
	public static final String ATT_NAME = "name";
	

	public static interface Align {
		public static String CENTER = "center";
		public static String LEFT   = "left";
		public static String RIGHT  = "right";
	}
	
}
