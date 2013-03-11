/**

	BlackBoard BreadBoard Editor
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JTable;

/**
 * <p>
 * The <code>ColorIconFactory</code> generates icons for color display
 * inside a {@link JTable} for example.
 * </p>
 * <p>
 * Every icon is put into the cache after it has been generated. 
 * This way each icon is only created once for a color.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
public final class ColorIconFactory {
	
	private static final HashMap<Color, ImageIcon> colorCache = new HashMap<Color, ImageIcon>();
	
	/**
	 * Gets a size x size pixel wide icon containing the desired {@link Color}. If the icon for this
	 * color already exists, it is taken from the cache, otherwise it will be generated
	 * and put into the cache afterwards. 
	 * 
	 * @param color The color to create the icon for
	 * @param size The size of the icon
	 * 
	 * @return an {@link ImageIcon} containing only pixels of the desired {@link Color}
	 */
	public static final ImageIcon getIcon(final Color color, final int size) {
		
		if (!colorCache.containsKey(color)) {
			final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			final Graphics g = image.getGraphics();
			g.setColor(color);
			g.fillRect(0, 0, size, size);			
			final ImageIcon icon = new ImageIcon(image);
			colorCache.put(color, icon);
			return icon;
		}
		else {
			return colorCache.get(color);
		}
		
	}
	
}
