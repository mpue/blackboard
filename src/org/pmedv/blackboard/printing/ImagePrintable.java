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
package org.pmedv.blackboard.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * <p>
 * First of all : The Java Printing API ist the biggest mess I've ever seen.
 * It is extremely difficult to get the desired printing results with it.
 * But maybe I just don't understand the whole thing. No matter what, it works 
 * somehow and must be seen as experimental as long as someone comes along the way
 * who is able to fix the BlackBoard printing thing.
 * </p>
 * <p>
 * This {@link Printable} creates an image printing for the current board
 * to be printed. 
 * </p>
 * 
 * @author Matthias Pueski (23.10.2010)
 * 
 */

public class ImagePrintable implements Printable {

	private BufferedImage image;
	
	public ImagePrintable(BufferedImage image) {
		this.image = image;
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0)
			return Printable.NO_SUCH_PAGE;

		Graphics2D g2d = (Graphics2D) graphics;

		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		AffineTransform at = new AffineTransform();
			
		float widthIntMillimeters = (float) ((float)image.getWidth() / 16 * 2.54f);		
		float printSizeInMillimeters = (float)((float)image.getWidth()/72*2.54f)*10;			
		float scale = widthIntMillimeters/ printSizeInMillimeters;
		
		// 2.2 is some weird correction factor (which I got by trial and error), to get the board image centered at the page
		double pageWidth = pageFormat.getWidth()*2.2;
		double pageHeight = pageFormat.getHeight()*2.2;
		
		double imageWidth = ((double)image.getWidth());
		double imageHeight = ((double)image.getHeight());
		
		double startX = (pageWidth / 2)  - (imageWidth / 2); 
		double startY = (pageHeight / 2) - (imageHeight / 2);
		
		at.translate(startX, startY);		
		g2d.scale(scale, scale);		
				
		g2d.drawRenderedImage(image, at);
		
		g2d.fillOval((int)(pageWidth/2), (int)(pageHeight/2), 5, 5);
		
		return Printable.PAGE_EXISTS;

	}

}
