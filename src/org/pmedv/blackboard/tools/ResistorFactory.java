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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.pmedv.blackboard.Colors;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.core.context.AppContext;

/**
 * @author Matthias Pueski (24.10.2010)
 * 
 */

public class ResistorFactory {

	private static final ResistorFactory INSTANCE = new ResistorFactory();

	private static final HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();
	
	private static final HashMap<Float, Color> toleranceMap = new HashMap<Float, Color>();
	
	static {
		
		colorMap.put(0, Color.BLACK);
		colorMap.put(1, Colors.BROWN);
		colorMap.put(2, Color.RED);
		colorMap.put(3, Color.ORANGE);
		colorMap.put(4, Color.YELLOW);
		colorMap.put(5, Color.GREEN);
		colorMap.put(6, Color.BLUE);
		colorMap.put(7, Colors.VIOLET);
		colorMap.put(8, Color.GRAY);
		colorMap.put(9, Color.WHITE);
		
		toleranceMap.put(0.5f, Color.GREEN);
		toleranceMap.put(1f, Colors.BROWN);
		toleranceMap.put(2f, Color.RED);
		toleranceMap.put(5f, Colors.GOLD);
		toleranceMap.put(10f, Color.GRAY);
		toleranceMap.put(20f, null);
		toleranceMap.put(21f, null);
	}
	
	
	protected ResistorFactory() {
	};

	public Resistor getResistor(int value, float tolerance) {
		Resistor resistor = new Resistor(32, 32, 96, 32, 0, value, tolerance);
		resistor.setBody(getImageForValue(value,5.0f));	
		resistor.setLayer(BoardEditorModel.PART_LAYER);
		return resistor;
	}

	public static final ResistorFactory getInstance() {
		return INSTANCE;
	}

	private BufferedImage getBaseImage() {

		try {
			BufferedImage image = ImageIO.read(new FileInputStream(AppContext.getWorkingDir()+"/parts/images/r_ohne_reflex.png"));
			return image;
		}
		catch (IOException e) {
			return null;
		}
		
	}

	private BufferedImage getReflectionImage(boolean toleranceRing) {

		BufferedImage image = null;
		
		try {
			if (toleranceRing)
				image = ImageIO.read(new FileInputStream(AppContext.getWorkingDir()+"/parts/images/r_nur_reflex_und_schatten.png"));
			else
				image = ImageIO.read(new FileInputStream(AppContext.getWorkingDir()+"/parts/images/r_nur_reflex_und_schatten_ot.png"));
			return image;
		}
		catch (IOException e) {
			return null;
		}
		
	}
	
	public BufferedImage getImageForValue(int value, Float tolerance) {		
		return createRings(getBaseImage(), value, tolerance);
	}
	
	private Color[] getRingColors(int value) {
		Color[] colors = new Color[3];
		
		String strVal = String.valueOf(value);

		if (strVal.length() == 1) {
			
			Integer first  = Integer.valueOf(strVal);
			
			colors[0] = colorMap.get(first);
			colors[1] = colorMap.get(0);
			colors[2] = Colors.GOLD;
			
		}
		
		else if (strVal.length() == 2) {
			
			Integer first  = Integer.valueOf(strVal.substring(0,1));
			Integer second = Integer.valueOf(strVal.substring(1,2));
			
			colors[0] = colorMap.get(first);
			colors[1] = colorMap.get(second);
			colors[2] = colorMap.get(0);
			
		}
		else {

			Integer first  = Integer.valueOf(strVal.substring(0,1));
			Integer second = Integer.valueOf(strVal.substring(1,2));
			Integer third  = strVal.length() - 2;
			
			colors[0] = colorMap.get(first);
			colors[1] = colorMap.get(second);
			colors[2] = colorMap.get(third);
			
		}
		
		
		return colors;
	}
	
	private BufferedImage createRings(BufferedImage image, int value, Float tolerance) {
		
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(alpha);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		Color[] colors = getRingColors(value);
		
		g2d.setColor(colors[0]);		
		g2d.fillRect(21,2,3,image.getHeight()-5);		
		g2d.setColor(colors[1]);		
		g2d.fillRect(27,2,3,image.getHeight()-5);
		g2d.setColor(colors[2]);		
		g2d.fillRect(33,2,3,image.getHeight()-5);		
		
		// Tolerance ring
		
		Color toleranceColor = toleranceMap.get(tolerance);
		
		if (toleranceColor != null) {
			g2d.setColor(toleranceColor);		
			g2d.fillRect(40,2,3,image.getHeight()-5);			
		}
		
		g2d.drawImage(getReflectionImage(toleranceColor != null),0,0,null);
		
		return image;
	}
	

	
}
