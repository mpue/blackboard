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
package org.pmedv.blackboard.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.beans.ResistorBean;
import org.pmedv.blackboard.tools.ResistorFactory;
import org.pmedv.core.preferences.Preferences;

/**
 * <p>
 * This class represents a resistor. Resistors are created by the
 * {@link ResistorFactory}. The factory takes care of the value of the resistor
 * and creates an according body image too.
 * </p>
 * <p>
 * Resistors behave similar as lines. The can be stretched rotated, copied,
 * deleted and so on.
 * </p>
 * 
 * @author Matthias Pueski (24.10.2010)
 * 
 */
public class Resistor extends Line {
	// the value of this resistor
	private int value;
	// the tolerance of the resistor
	private float tolerance;
	// the image for the body of the resistor
	private BufferedImage body;
	// default dimension for the resistor
	private static final int BODY_WIDTH = 64;
	private static final int BODY_HEIGHT = 16;

	protected int nameXOffset = -8;
	protected int nameYOffset = -8;
	
	protected static BufferedImage solderPointImage;
	private static BufferedImage wireImage;
	
	static {
		try {
			solderPointImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/SolderPoint.png"));
			wireImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/w_Wire_0.6mm_1_r.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
	/**
	 * Creates a new resistor
	 * 
	 * @param x1 start point x
	 * @param y1 start point y
	 * @param x2 stop point x
	 * @param y2 stop point y
	 * @param index the index of the resistor, this is obsolete, just pass zero
	 * @param value the value of the resistor
	 * 
	 */
	public Resistor(int x1, int y1, int x2, int y2, int index, int value, float tolerance) {
		super(x1, y1, x2, y2, index);
		this.value = value;
		this.tolerance = tolerance;
	}

	/**
	 * Creates a new resistor from a {@link ResistorBean}, this is usually
	 * called from the {@link ResistorFactory} in order to create a resistor
	 * from loading a board.
	 * 
	 * @param resistorBean
	 */
	public Resistor(ResistorBean resistorBean) {
		super(new Point(resistorBean.getStart().getX(), resistorBean.getStart().getY()), new Point(resistorBean.getEnd().getX(),
				resistorBean.getEnd().getY()), 0);
		this.value = resistorBean.getValue();
		this.tolerance = resistorBean.getTolerance();
		this.name = resistorBean.getName();
		this.layer = resistorBean.getLayer();
		this.body = ResistorFactory.getInstance().getImageForValue(value, resistorBean.getTolerance());
	}

	/**
	 * Creates a new resistor
	 * 
	 * @param start the start point
	 * @param end the end point
	 * @param index the index, obsolete, just pass zero
	 * @param value the value of the resistor
	 */
	public Resistor(Point start, Point end, int index, int value) {
		super(start, end, index);
		this.value = value;
		this.tolerance = 5.0f;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * @return the tolerance
	 */
	public float getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance the tolerance to set
	 */
	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return the body
	 */
	public BufferedImage getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(BufferedImage body) {
		this.body = body;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.DARK_GRAY);
		g2d.setStroke(BoardUtil.stroke_2_5f);
		
//		g2d.drawLine((int)getStart().getX(),(int)getStart().getY(), 
//			       (int)getEnd().getX(),  (int)getEnd().getY());

//		g2d.fillOval((int)getStart().getX()-ENDPOINT_SIZE/2,(int)getStart().getY()-ENDPOINT_SIZE/2, ENDPOINT_SIZE,ENDPOINT_SIZE);
//		g2d.fillOval((int)getEnd().getX()-ENDPOINT_SIZE/2,(int)getEnd().getY()-ENDPOINT_SIZE/2, ENDPOINT_SIZE,ENDPOINT_SIZE);
		
		if (mirror) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));			
			g2d.drawImage(solderPointImage, (int)getStart().getX() - 8,(int)getStart().getY() - 8, null);	
			g2d.drawImage(solderPointImage, (int)getEnd().getX() - 8,(int)getEnd().getY() - 8, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		}
		else {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		}
		
		g.setColor(Color.RED);
		// save current transform
		AffineTransform saveXform = g2d.getTransform();
		// if there's any slope we need to transform the graphics by the slope's
		// angle
		if (getSlope() != 0) {
			AffineTransform transform = new AffineTransform();
			transform.translate((int) getMidPoint().getX(), (int) getMidPoint().getY());
			transform.rotate(getSlopeAngleInRadians());
			transform.translate((int) -getMidPoint().getX(), (int) -getMidPoint().getY());
			g2d.transform(transform);
		}
		
		// draw wire
		int steps = (int)(getLength() / 16);
		for (int i = 0; i < steps;i++) {
			g2d.drawImage(wireImage, (int) (getMidPoint().getX() - getLength()/2) + i*16, (int) getMidPoint().getY() - BODY_HEIGHT / 2, null);	
		}
		
		
		// draw body of the resistor		
		g2d.drawImage(body, (int) getMidPoint().getX() - BODY_WIDTH / 2, (int) getMidPoint().getY() - BODY_HEIGHT / 2, null);
		
		// restore saved transform
		if (getSlope() != 0) {
			g2d.setTransform(saveXform);
		}
		
		// draw name if available
		
		g.setColor(Color.BLACK);
		g.setFont(f);
		
		Boolean showNames = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showNames");
		
		if (showNames != null && showNames.equals(Boolean.valueOf(true))) {
			if (name != null){
				g2d.drawString(name, (int)getMidPoint().getX()+nameXOffset,(int)getMidPoint().getY()+nameYOffset);
			}			
		}
		
		// draw values if available
		
		Boolean showValues = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showValues");
		
		String valueString = null;
		
		if (value >= 1000 && value < 1000000) {
			valueString = String.valueOf((float)value/1000)+"k";
		}
		else if (value >= 1000000) {
			valueString = String.valueOf((float)value/1000000)+"M";
		}
		else {
			valueString = String.valueOf(value);
		}
		
		if (showValues != null && showValues.equals(Boolean.valueOf(true))) {
			g2d.drawString(valueString, (int)getMidPoint().getX()+20,(int)getMidPoint().getY()-30);
		}
		
	}
}
