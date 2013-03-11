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
package org.pmedv.blackboard.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.pmedv.core.util.ImageUtils;



public class RotateTest {
	
	private static final String outputImage = "D:\\devel\\j2ee_workspace\\BlackBoard\\resources\\ic_socket_2x8_rotated.png";

	public static void main(String[] args) {
		
		URL u = null;

		u = Thread.currentThread().getContextClassLoader().getResource("ic_socket_2x8.png");

		try {			
			BufferedImage image = ImageIO.read(u);		
			image = ImageUtils.rotate90CW(image);			
			ImageIO.write(image, "PNG", new File(outputImage));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	
}
