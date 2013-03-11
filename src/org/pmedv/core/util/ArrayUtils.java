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
package org.pmedv.core.util;

public class ArrayUtils {
	
	/**
	 * Reallocates an array with a new size, and copies the contents of the old
	 * array to the new array.
	 * 
	 * @param oldArray the old array, to be reallocated.
	 * @param newSize  the new array size.
	 * 
	 * @return A new array with the same contents but the new size.
	 */
	public static synchronized Object resizeArray(Object oldArray, final int newSize) {
		
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		
		Class<?> elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
		
		int preserveLength = Math.min(oldSize, newSize);
		
		if (preserveLength > 0)
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		
		return newArray;
		
	}	

	/**
	 * Determines the number of rows for a given number of images in relation to a number of columns.
	 * This method is needed to create a well sized table for any number of elements.
	 * 
	 * @param numElements the number of elements
	 * @param cols the number of columns.
	 * 
	 * @return the number of rows
	 */
	public static  synchronized int getNumOfRows(final int numElements, final int cols) {
		return (numElements +((cols-(numElements%cols))%cols))/cols;	
	}	
	
}
