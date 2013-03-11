/**

	BreadBoard Breadboard Designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2012 Matthias Pueski
	
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

package org.pmedv.blackboard.tools;

import java.util.HashMap;

import org.pmedv.blackboard.components.Resistor;

/**
 * <p>
 * The {@link ESeries} class represents the value series for any {@link Resistor}
 * from E3 to E96. We do not calculate the values by formula here because 
 * the E24 values to not correspond to the rounding rules due to historical 
 * reasons.  
 * </p>
 * <p>
 * The E192 series is still missing though. 
 * </p>
 * @see <a href="http://de.wikipedia.org/wiki/E-Reihe">Wikipedia Link</a>
 * 
 * @author Matthias Pueski
 *
 */
public class ESeries {
	
	public static enum Range {
		E3,  // > 20%
		E6,  // 20%
		E12, // 10%
		E24, // 5%
		E48, // 2%
		E96  // 1%
	}
	
	public static final HashMap<Range, Float[]> RANGES     = new HashMap<Range, Float[]>(); 
	public static final HashMap<Range, Float>   TOLERANCES = new HashMap<Range, Float>();
	public static final HashMap<Float, String>  VALUES     = new HashMap<Float, String>();
	
	static {
		
		final Float[] e3  = { 1.0f, 2.2f, 4.7f };
		final Float[] e6  = { 1.0f, 1.5f, 2.2f, 3.3f, 4.7f, 6.8f }; 
		final Float[] e12 = { 1.0f, 1.2f, 1.5f, 1.8f, 2.2f, 2.7f, 
							  3.3f, 3.9f, 4.7f, 5.6f, 6.8f, 8.2f }; 
		final Float[] e24 = { 1.0f, 1.1f, 1.2f, 1.3f, 1.5f, 1.6f,
							  1.8f, 2.0f, 2.2f, 2.4f, 2.7f, 3.0f,
							  3.3f, 3.6f, 3.9f, 4.3f, 4.7f, 5.1f,
							  5.6f, 6.2f, 6.8f, 7.5f, 8.2f, 9.1f };
		
		final Float[] e48 = { 1.00f, 1.05f, 1.10f, 1.15f, 1.21f, 1.27f,
							  1.33f, 1.40f, 1.47f, 1.54f, 1.62f, 1.69f,
							  1.78f, 1.87f, 1.96f, 2.05f, 2.15f, 2.26f,
							  2.37f, 2.49f, 2.61f, 2.74f, 2.87f, 3.01f,
							  3.16f, 3.32f, 3.48f, 3.65f, 3.83f, 4.02f,
							  4.22f, 4.42f, 4.64f, 4.87f, 5.11f, 5.36f, 
							  5.62f, 5.90f, 6.19f, 6.49f, 6.81f, 7.15f, 
							  7.50f, 7.87f, 8.25f, 8.66f, 9.09f, 9.53f };
		
		final Float[] e96 = { 1.00f, 1.02f, 1.05f, 1.07f, 1.10f, 1.13f,
							  1.15f, 1.18f, 1.21f, 1.24f, 1.27f, 1.30f,
							  1.33f, 1.37f, 1.40f, 1.43f, 1.47f, 1.50f,
							  1.54f, 1.58f, 1.62f, 1.65f, 1.69f, 1.74f,
							  1.78f, 1.82f, 1.87f, 1.91f, 1.96f, 2.00f, 
							  2.05f, 2.10f, 2.15f, 2.21f, 2.26f, 2.32f, 
							  2.37f, 2.43f, 2.49f, 2.55f, 2.61f, 2.67f,
							  2.74f, 2.80f, 2.87f, 2.94f, 3.01f, 3.09f,
							  3.16f, 3.24f, 3.32f, 3.40f, 3.48f, 3.57f,
							  3.65f, 3.74f, 3.83f, 3.92f, 4.02f, 4.12f,
							  4.22f, 4.32f, 4.42f, 4.53f, 4.64f, 4.75f,
							  4.87f, 4.99f, 5.11f, 5.23f, 5.36f, 5.49f,
							  5.62f, 5.76f, 5.90f, 6.04f, 6.19f, 6.34f,
							  6.49f, 6.65f, 6.81f, 6.98f, 7.15f, 7.32f,
							  7.50f, 7.68f, 7.87f, 8.06f, 8.25f, 8.45f,
							  8.66f, 8.87f, 9.09f, 9.31f, 9.53f, 9.76f };
		
		RANGES.put(Range.E3, e3);
		RANGES.put(Range.E6, e6);
		RANGES.put(Range.E12, e12);
		RANGES.put(Range.E24, e24);
		RANGES.put(Range.E48, e48);
		RANGES.put(Range.E96, e96);

		TOLERANCES.put(Range.E96,1f);
		TOLERANCES.put(Range.E48,2f);
		TOLERANCES.put(Range.E24,5f);
		TOLERANCES.put(Range.E12,10f);
		TOLERANCES.put(Range.E6,20f);
		TOLERANCES.put(Range.E3,21f);

		VALUES.put(1f,"E96");
		VALUES.put(2f,"E48");
		VALUES.put(5f,"E24");
		VALUES.put(10f,"E12");
		VALUES.put(20f,"E6");
		VALUES.put(21f,"E3");
		
	}
	
}
