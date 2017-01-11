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
package org.pmedv.blackboard.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.pmedv.blackboard.Colors;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;

public class BoardGen {

	private static final int RASTER = 16;
	private static final int PADSIZE = 8;
	private static final int RASTER2 = 3*RASTER;
	/**
	 * Generates the standard background image for the breadboard
	 * 
	 * @return the BufferedImage containing the breadboard
	 * 
	 */
	public static BufferedImage generateBoard(int width, int height,
			BoardType type, boolean solderSide) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);
		if (type != null && type.equals(BoardType.BLANK)) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(Colors.EPOXYD);
		}
		g2.fillRect(0, 0, width, height);
		if (type == null || !type.equals(BoardType.BLANK)) {
			if (type != null && type.equals(BoardType.STRIPES)) {
				g2.setColor(Colors.COPPER);
				for (int y = RASTER / 2; y < height - RASTER / 2; y += RASTER) {
					g2.fillRect(0, y + 3, width, RASTER - 5);
				}
			}
			if (type != null && type.equals(BoardType.HOLESSTRIPS)) {
				g2.setColor(Colors.COPPER);
				for (int y = RASTER / 2; y < height - RASTER / 2; y += RASTER) {
					g2.fillRect(0, y + 3, width, RASTER - 5);
				}

			}
			if (type != null && type.equals(BoardType.HOLES) && solderSide) {
				g2.setColor(Colors.COPPER);
				for (int x = RASTER; x < width; x += RASTER) {
					for (int y = RASTER; y < height; y += RASTER) {
						g2.fillOval(x - 2 - PADSIZE / 2, y - 2 - PADSIZE / 2,
								PADSIZE + 4, PADSIZE + 4);
					}
				}

			}
			g2.setColor(Color.BLACK);
			for (int x = RASTER; x < width; x += RASTER) {
				for (int y = RASTER; y < height; y += RASTER) {
					g2.fillOval(x - PADSIZE / 2, y - PADSIZE / 2, PADSIZE, PADSIZE);
				}
			}
			g2.setColor(Color.LIGHT_GRAY);
			for (int x = RASTER; x < width; x += RASTER) {
				for (int y = RASTER; y < height; y += RASTER) {
					g2.fillOval(x - PADSIZE / 2 + 1, y - PADSIZE / 2 + 1, PADSIZE - 2, PADSIZE - 2);
				}
			}

			if (type != null && type.equals(BoardType.HOLESSTRIPS)) {
				g2.setColor(Colors.EPOXYD);
				for (int x = RASTER2+7; x < width; x += RASTER2) {
					for (int y = 1; y < height; y += 1) {
						g2.fillRect(x , y, 2, PADSIZE - 2);
					}
				}
			}

		}
		return image;
	}
}
