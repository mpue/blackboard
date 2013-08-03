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
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.TextUtil;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.core.preferences.Preferences;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Part", propOrder = { "imageName", "description", "packageType", "rotation", "author", "license","value","designator","nameXOffset","nameYOffset","connections" })
public class Part extends Item implements Cloneable, Rotateable, Comparable<Item> {
	@XmlTransient
	protected BufferedImage image;
	@XmlTransient
	protected BufferedImage nameImage;
	@XmlTransient
	private boolean drawTransparent = false;
	@XmlTransient
	protected String filename;
	private String imageName;
	private String description;
	private String packageType;
	protected int rotation = 0;
	private String author;
	private String license;
	protected String value;
	private String designator;
	
	@XmlTransient
	float x1_;
	@XmlTransient
	float y1_;
	@XmlTransient
	float x2_;
	@XmlTransient
	float y2_;
	@XmlTransient
	float x3_;
	@XmlTransient
	float y3_;
	@XmlTransient
	float x4_;
	@XmlTransient
	float y4_;
	
	@XmlTransient
	private BufferedImage solderPointImage;
	
	@XmlTransient
	private String xmlContent;
	
	private Connections connections = new Connections();
	
	@XmlTransient
	private final ArrayList<WireConnection> wireConnections = new ArrayList<WireConnection>();

	
	protected int nameXOffset = -8;
	protected int nameYOffset = -8;
	
	@XmlTransient
	private boolean editing;
	
	public Part(BufferedImage image, int xLoc, int yLoc, int width, int height) {
		this(image, xLoc, yLoc);
		setWidth(width);
		setHeight(height);
	}

	public Part(BufferedImage image, int xLoc, int yLoc) {
		this(image);
		setXLoc(xLoc);
		setYLoc(yLoc); 
	}

	public Part(BufferedImage image) {
		this();
		this.image = image;
	}

	public Part() {
		resizable = false;
		try {
			solderPointImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/SolderPoint.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {
		
		int xC = getxLoc();
		int yC = getyLoc();
		
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2.setRenderingHints(rh);
		g2.setColor(Color.WHITE);
				
		// save current transform
		AffineTransform saveXform = g2.getTransform();
		// if there's any slope we need to transform the graphics by the slope's
		// angle
		if (rotation != 0) {
			AffineTransform transform = new AffineTransform();
			transform.translate((int) xC, (int) yC);
			transform.rotate(Math.toRadians(rotation));
			transform.translate((int) -xC, (int) -yC);
			g2.transform(transform);
		}
		// draw part

		if (mirror) {			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			if (connections.getPin().size() > 0) {				
				for (Pin pin : connections.getPin()) {		
					g2.drawImage(solderPointImage, pin.getX()+getXLoc()-8, pin.getY()+getYLoc()-8, null);						
				}				
			}
			BoardEditor editor = EditorUtils.getCurrentActiveEditor();
			
			boolean reduceAlpha = true;
			
			if (editor != null) {
				BoardEditorModel model = editor.getModel();
				
				for (Layer layer : model.getLayers()) {
					if (layer.getIndex() == this.layer) {
						if (layer.getName().equalsIgnoreCase("Bottom")) {
							reduceAlpha = false;
							break;
						}
					}
				}
			}
			if (reduceAlpha) {				
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			}
		}
		else {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));				
		}

		g2.drawImage(image, getXLoc(), getYLoc(), null);
		
		// restore saved transform
		if (rotation != 0) {
			g2.setTransform(saveXform);
		}
		
		g.setColor(Color.BLACK);
		g.setFont(f);
		
		Boolean showNames = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showNames");
		Boolean showValues = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showValues");
		
		if (showNames != null && showNames.equals(Boolean.valueOf(true)) && name != null) {
			g.drawString(name, getxLoc()+nameXOffset,getyLoc()+nameYOffset);
		}

		if (showValues != null && showValues.equals(Boolean.valueOf(true)) && value != null) {
			g.drawString(value, getXLoc()+getWidth()/2,getYLoc());
		}
		
		if (editing) {

			if (nameImage != null) {
				g.setColor(Color.RED);
				g.drawRect(getxLoc()+nameXOffset,getyLoc()+nameYOffset-nameImage.getHeight(),nameImage.getWidth(),nameImage.getHeight());				
			}
			
		}
		
		
		
	}
	
	@Override
	protected void drawHandles(Graphics2D g2, int handleSize) {

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

		Rectangle r = getBoundingBox();
		g2.draw(r);
		
		if (resizable) {

			Rectangle topLeftHandle     = new Rectangle(getxLoc()-handleSize/2, getyLoc()-handleSize/2, handleSize, handleSize);
			Rectangle topRightHandle    = new Rectangle(getxLoc()+width-handleSize/2, getyLoc()-handleSize/2, handleSize, handleSize);
			Rectangle bottomLeftHandle  = new Rectangle(getxLoc()-handleSize/2, getyLoc()+height-handleSize/2, handleSize, handleSize);
			Rectangle bottomRightHandle = new Rectangle(getxLoc()+width-handleSize/2, getyLoc()+height-handleSize/2, handleSize, handleSize);

			g2.setColor(Color.BLACK);
			
			g2.draw(topLeftHandle);
			g2.draw(topRightHandle);
			g2.draw(bottomLeftHandle);
			g2.draw(bottomRightHandle);
			
		}

	}
	
	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object obj) {
		if (null != obj && this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			Part part = (Part) obj;
			if (part.getXLoc() == getXLoc() && part.getYLoc() == getYLoc() && part.getIndex() == index)
				return true;
		}
		return false;
	}

	public void rotateCW() {
		rotation += 45;
		if (rotation == 360) {
			rotation = 0;			
		}
	}

	public void rotateCCW() {
		rotation -= 45;		
		if (rotation == -360) {
			rotation = 0;					
		}
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		nameImage = TextUtil.createImageFromFont(name, f,Color.BLACK);
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the packageType
	 */
	public String getPackageType() {
		return packageType;
	}

	/**
	 * @param packageType the packageType to set
	 */
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Part part = new Part();
		part.setColor(getColor());
		part.setDescription(getDescription());
		part.setFilename(getFilename());
		part.setHeight(getHeight());
		part.setImage(getImage());
		part.setImageName(getImageName());
		part.setIndex(getIndex());
		part.setName(getName());
		part.setValue(getValue());
		part.setPackageType(getPackageType());
		part.setRotation(getRotation());
		part.setWidth(getWidth());
		part.setXLoc(getXLoc());
		part.setYLoc(getYLoc());
		part.setLayer(getLayer());
		part.setDesignator(getDesignator());
		
		for (Pin pin : connections.getPin()) {			
			Pin newPin = new Pin(pin.getX(),pin.getY());
			newPin.setName(pin.getName());
			newPin.setNum(pin.getNum());			
			part.getConnections().getPin().add(newPin);
		}
		
		return part;
	}

	/**
	 * @return the drawTransparent
	 */
	public boolean isDrawTransparent() {
		return drawTransparent;
	}

	/**
	 * @param drawTransparent the drawTransparent to set
	 */
	public void setDrawTransparent(boolean drawTransparent) {
		this.drawTransparent = drawTransparent;
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
	 * @return the designator
	 */
	public String getDesignator() {
		return designator;
	}
	
	/**
	 * @return the connections
	 */
	public Connections getConnections() {
		return connections;
	}

	/**
	 * @param connections the connections to set
	 */
	public void setConnections(Connections connections) {
		this.connections = connections;
	}

	/**
	 * @param designator the designator to set
	 */
	public void setDesignator(String designator) {
		this.designator = designator;
	}
	
	/**
	 * @return the editing
	 */
	public boolean isEditing() {
		return editing;
	}

	/**
	 * @param editing the editing to set
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	/**
	 * @return the nameImage
	 */
	public BufferedImage getNameImage() {
		return nameImage;
	}

	/**
	 * @return the wireConnections
	 */
	public List<WireConnection> getWireConnections() {
		return wireConnections;
	}

	
	/**
	 * @return the xmlContent
	 */
	public String getXmlContent() {
		return xmlContent;
	}

	/**
	 * @param xmlContent the xmlContent to set
	 */
	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public Rectangle getBoundingBox() {
				
		/**
		 * We need to rotate all four coordinates like this
		 *
		 * x2 = x0+(x-x0)*cos(theta)+(y-y0)*sin(theta)
		 * y2 = y0-(x-x0)*sin(theta)+(y-y0)*cos(theta)
		 * 
		 * whereas x0,y0 is the center point rotating around
		 */

		int xC = getxLoc();
		int yC = getyLoc();
		
		// top left
		int x1 = getxLoc();
		int y1 = getyLoc();
		
		// top right
		int x2 = getxLoc() + width;
		int y2 = getyLoc();
		
		// bottom right
		int x3 = getxLoc() + width;
		int y3 = getyLoc() + height;
		
		// bottom left
		int x4 = getxLoc();
		int y4 = getyLoc() + height;
		
		float rot = (float) Math.toRadians(-rotation);
		
		// now transform all four corners
		x1_ = (float) (xC+(x1-xC)*Math.cos(rot)+(y1-yC)*Math.sin(rot));
		y1_ = (float) (yC-(x1-xC)*Math.sin(rot)+(y1-yC)*Math.cos(rot));
		
		x2_ = (float) (xC+(x2-xC)*Math.cos(rot)+(y2-yC)*Math.sin(rot));
		y2_ = (float) (yC-(x2-xC)*Math.sin(rot)+(y2-yC)*Math.cos(rot));
		
		x3_ = (float) (xC+(x3-xC)*Math.cos(rot)+(y3-yC)*Math.sin(rot));
		y3_ = (float) (yC-(x3-xC)*Math.sin(rot)+(y3-yC)*Math.cos(rot));
		
		x4_ = (float) (xC+(x4-xC)*Math.cos(rot)+(y4-yC)*Math.sin(rot));
		y4_ = (float) (yC-(x4-xC)*Math.sin(rot)+(y4-yC)*Math.cos(rot));
		
		// now we need to find min_x, min_y, max_x and max_y
		// bounding box is (min_x,min_y), (min_x,max_y), (max_x,max_y), (max_x,min_y)
		
		float min_x = Float.MAX_VALUE;
		float max_x = Float.MIN_VALUE;
		float min_y = Float.MAX_VALUE;
		float max_y = Float.MIN_VALUE;
		
		float[] xvalues = new float[4];
		
		xvalues[0] = x1_;
		xvalues[1] = x2_;
		xvalues[2] = x3_;
		xvalues[3] = x4_;
		
		for (int i = 0; i < 4;i++) {
			if (xvalues[i] < min_x)
				min_x = xvalues[i];
		}
		for (int i = 0; i < 4;i++) {
			if (xvalues[i] > max_x)
				max_x = xvalues[i];
		}
		
		float[] yvalues = new float[4];
		
		yvalues[0] = y1_;
		yvalues[1] = y2_;
		yvalues[2] = y3_;
		yvalues[3] = y4_;
		
		for (int i = 0; i < 4;i++) {
			if (yvalues[i] < min_y)
				min_y = yvalues[i];
		}
		for (int i = 0; i < 4;i++) {
			if (yvalues[i] > max_y)
				max_y = yvalues[i];
		}
		
		return new Rectangle((int)min_x, (int)min_y, (int)(max_x-min_x),(int) (max_y-min_y));
		
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(this.getClass().getSimpleName()+" pos("+getxLoc()+","+getyLoc()+") index : "+index);
		return s.toString();
	}
	
}
