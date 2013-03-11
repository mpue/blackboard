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
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.beans.DiodeBean;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.util.ImageUtils;

/**
 * <p>
 * This class represents a diode. 
 * </p>
 * <p>
 * Diodes behave similar as lines. The can be stretched rotated, copied,
 * deleted and so on.   
 * </p>
 * 
 * @author Matthias Pueski (21.3.2012)
 * 
 */
public class Diode extends Line {
	
	public enum DiodeType {
		DO35,
		DO41,
		DO7,
		SOD57,
	}
	
	// the value of this diode
	private String value;
	// the image for the body of the diode
	private BufferedImage body;
	// the horizontal flipped image for the body of the diode
	private BufferedImage flippedBody;

	
	protected int nameXOffset = -8;
	protected int nameYOffset = -8;
	
	protected static BufferedImage solderPointImage;
	private static BufferedImage wireImage;

	protected DiodeType type;
	
	private static final Map<DiodeType,BufferedImage> bodyImageMap = new HashMap<DiodeType, BufferedImage>();
	
	static {
		
		try {
			solderPointImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/SolderPoint.png"));
			wireImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/w_Wire_0.6mm_1_r.png"));			
			BufferedImage bodyImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/d_DO35_nowire.png"));			
			bodyImageMap.put(DiodeType.DO35, bodyImage);
			bodyImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/d_DO41_nowire.png"));			
			bodyImageMap.put(DiodeType.DO41, bodyImage);
			bodyImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/d_DO7_nowire.png"));			
			bodyImageMap.put(DiodeType.DO7, bodyImage);
			bodyImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/d_SOD57_nowire.png"));			
			bodyImageMap.put(DiodeType.SOD57, bodyImage);			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Creates a new diode
	 * 
	 * @param x1 start point x
	 * @param y1 start point y
	 * @param x2 stop point x
	 * @param y2 stop point y
	 * @param index the index of the diode, this is obsolete, just pass zero
	 * @param value the value of the diode
	 * 
	 */
	public Diode(int x1, int y1, int x2, int y2, int index, String value, DiodeType type) {
		super(x1, y1, x2, y2, index);
		this.value = value;
		this.type = type;
		this.body = bodyImageMap.get(type);
		this.flippedBody = ImageUtils.flipHorizontal(body);
	}

	/**
	 * Creates a new diode from a {@link diodeBean}, this is usually
	 * called from the {@link diodeFactory} in order to create a diode
	 * from loading a board.
	 * 
	 * @param diodeBean
	 */
	public Diode(DiodeBean diodeBean) {
		super(new Point(diodeBean.getStart().getX(), diodeBean.getStart().getY()), new Point(diodeBean.getEnd().getX(),
				diodeBean.getEnd().getY()), 0);
		this.value = diodeBean.getValue();
		this.name = diodeBean.getName();
		this.layer = diodeBean.getLayer();
		this.type = diodeBean.getType();
		this.body = bodyImageMap.get(diodeBean.getType());
		this.flippedBody = ImageUtils.flipHorizontal(body);
	}

	/**
	 * Creates a new diode
	 * 
	 * @param start the start point
	 * @param end the end point
	 * @param index the index, obsolete, just pass zero
	 * @param value the value of the diode
	 */
	public Diode(Point start, Point end, int index, String value) {
		super(start, end, index);
		this.value = value;
		this.type = DiodeType.DO35;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	
	/**
	 * @return the type
	 */
	public DiodeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DiodeType type) {
		this.type = type;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.DARK_GRAY);
		g2d.setStroke(BoardUtil.stroke_2_5f);
		
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
			g2d.drawImage(wireImage, (int) (getMidPoint().getX() - getLength()/2) + i*16, (int) getMidPoint().getY() - 8, null);	
		}
		
		// draw body of the diode		
		// Small hack : we need to draw the body flipped if the slope is negative, since the rotation angle calculation is slightly wrong ;)
		if (getEnd().getX() < getStart().getX())
			g2d.drawImage(flippedBody, (int) getMidPoint().getX() - body.getWidth() / 2, (int) getMidPoint().getY() - body.getHeight() / 2, null);
		else		
			g2d.drawImage(body, (int) getMidPoint().getX() - body.getWidth() / 2, (int) getMidPoint().getY() - body.getHeight() / 2, null);
		
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
		
		if (showValues != null && showValues.equals(Boolean.valueOf(true))) {
			g2d.drawString(value, (int)getMidPoint().getX()+20,(int)getMidPoint().getY()-30);
		}
		
	}

	/**
	 * @return the bodyimagemap
	 */
	public static Map<DiodeType, BufferedImage> getBodyimagemap() {
		return bodyImageMap;
	}
}
