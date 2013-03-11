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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
    
	private static final long serialVersionUID = 3828426791540150947L;

	private Image image;
	
	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	public ImagePanel() {
		
	}

    public ImagePanel(Image image) {
    	this.image = image;
    }
    
    public ImagePanel(String filename) {
    	this.image = Toolkit.getDefaultToolkit().getImage(filename);    	
    }

    public ImagePanel(URL url) {
    	this.image = Toolkit.getDefaultToolkit().getImage(url);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

}
