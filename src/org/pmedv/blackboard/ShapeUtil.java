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
package org.pmedv.blackboard;

import java.awt.Rectangle;

public class ShapeUtil {

	/**
	 * Checks if one rectangle collides another disregarding their rotation,
	 * in fact we assume, that they both have a rotation of 0.
	 * 
	 * @param r1 
	 * @param r2
	 * 
	 * @return true if the two guys collide
	 */
	public static boolean collides(Rectangle r1, Rectangle r2) {
				
		if ((r1.getY() +  r1.getHeight()  >= r2.getY() &&           // r1 bottom collides top of the r2
			 r1.getY() <  r2.getY() && 	
			 r1.getX() +  r1.getWidth()  >= r2.getX() && // r1 is between the left and the right 
			 r1.getX() <= r2.getX()+  r2.getWidth()) ) {
			 return true;
		}
		else if ((r1.getY() <= r2.getY() +  r2.getHeight()    && // r1 top collides bottom of the r2
			 r1.getY() +  r1.getHeight() > r2.getY() && 	
			 r1.getX() +  r1.getWidth()   >= r2.getX() && // r1 is between the left and the right 
			 r1.getX() <= r2.getX() +  r2.getWidth())) {
			return true;	
		}
		else if ((r1.getX() +  r1.getWidth()  >= r2.getX() && // r1 collides left side of the r2
			r1.getX() <  r2.getX() && 	
			r1.getY() +  r1.getHeight()  >= r2.getY() && // r1 is between the top and bottom of the r2 
			r1.getY() <= r2.getY()+  r2.getHeight()) ) {
			return true;
		}
		else if ((r1.getX() <= r2.getX() +  r2.getWidth()    && // r1 collides right side of the r2
			r1.getX() +  r1.getWidth() > r2.getX() && 	
			r1.getY() +  r1.getHeight()   >= r2.getY() && // r1 is between the top and bottom of the r2  
			r1.getY() <= r2.getY() +  r2.getHeight())) {
			return true;	
		}
		
		return false;
		
	}
	 
}
