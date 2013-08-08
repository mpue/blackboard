package org.pmedv.blackboard.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.bind.annotation.XmlTransient;

import org.pmedv.blackboard.beans.BoxBean;
import org.pmedv.blackboard.beans.EllipseBean;
import org.pmedv.blackboard.beans.LineBean;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.beans.TextPartBean;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceType;
import org.pmedv.core.preferences.Preferences;

/**
 * @author Matthias Pueski (25.09.2011)
 *
 */
public class Symbol extends Part {

	private ArrayList<Item> items;

	private static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);
	
	private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 10);
	
	private String category;

	private boolean mirrorVertical = false;
	private boolean mirrorHorizontal = false;
	
	private boolean customModel = false;
	
	private Model model;
	
	private SpiceType type;
	
	private Properties properties;
	

	public Symbol() {
		super();
		items = new ArrayList<Item>();
	}
	
	@XmlTransient
	private String xmlContent;
	
	public Symbol(SymbolBean symbolBean) {
		
		this();
		
		for (LineBean lineBean : symbolBean.getLines()) {
			items.add(new Line(lineBean));
		}
		
		for (EllipseBean ellipseBean : symbolBean.getEllipses()) {
			items.add(new Ellipse(ellipseBean));
		}
		
		for (BoxBean boxBean : symbolBean.getBoxes()) {
			items.add(new Box(boxBean));
		}
		
		for (TextPartBean textBean : symbolBean.getTextparts()) {			
			TextPart part = new TextPart(textBean);
			items.add(part);
		}
		
		for (Pin p : symbolBean.getConnections().getPin()) {
			getConnections().getPin().add(p);
		}
		
		this.setxLoc(symbolBean.getXLoc());
		this.setyLoc(symbolBean.getYLoc());
		this.oldXLoc = getxLoc();
		this.oldYLoc = getyLoc();
		this.rotation = symbolBean.getRotation();
		this.color = new Color(symbolBean.getColor().getRed(), symbolBean.getColor().getGreen(), symbolBean.getColor().getBlue());
		this.index = symbolBean.getIndex();
		this.layer = symbolBean.getLayer();
		this.width = symbolBean.getWidth();
		this.height = symbolBean.getHeight();
		this.category = symbolBean.getCategory();
		this.name = symbolBean.getName();
		this.value = symbolBean.getValue();
		this.mirrorHorizontal = symbolBean.isMirrorHorizontal();
		this.mirrorVertical = symbolBean.isMirrorVertical();
		this.xmlContent = symbolBean.getXmlContent();
		this.customModel = symbolBean.isCustomModel();
		this.model = symbolBean.getModel();
		this.type = symbolBean.getType();
		this.filename = symbolBean.getFilename();
		this.properties = symbolBean.getProperties();
	}

	public void draw(Graphics g, boolean showPins, boolean showPinNames, boolean showPinNumbers, boolean showMidpoints, boolean showOrigins) {
	
		Graphics2D g2d = (Graphics2D)g;

		int midPointX = getxLoc() + width/2;
		int midPointY = getyLoc() + height/2;
		
		int xC = getxLoc();
		int yC = getyLoc();

		// save current transform
		AffineTransform saveXform = g2d.getTransform();
		// if there's any slope we need to transform the graphics by the slope's
		// angle
		if (rotation != 0) {
			AffineTransform transform = new AffineTransform();
			transform.translate((int) xC, (int) yC);
			transform.rotate(Math.toRadians(rotation));
			transform.translate((int) -xC, (int) -yC);
			g2d.transform(transform);
		}
		
		if (mirrorHorizontal) {
			AffineTransform transform = new AffineTransform();
			transform.translate((int) xC, (int) yC);
			transform.scale(-1, 1);			
			transform.translate((int) -xC, (int) -yC);
			transform.translate(-width, 0);
			g2d.transform(transform);
		}
		if (mirrorVertical) {
			AffineTransform transform = new AffineTransform();
			transform.translate((int) xC, (int) yC);
			transform.scale(1, -1);
			transform.translate(0, -height);
			transform.translate((int) -xC, (int) -yC);
			g2d.transform(transform);			
		}
		
		// draw symbol
		for (Item item : items) {
			g2d.setColor(item.getColor());
			
			if (item instanceof Shape) {
				Shape shape = (Shape)item;
				g2d.setStroke(shape.getStroke());
			}
			item.draw(g2d);
		}
		

		g2d.setFont(DEFAULT_FONT);
		g2d.setColor(Color.GREEN);
		g2d.setStroke(DEFAULT_STROKE);
		
		if (showOrigins) {
			// draw origin
			g2d.drawLine(getxLoc()-4, getYLoc(), getXLoc()+4, getYLoc());
			g2d.drawLine(getxLoc(), getYLoc()-4, getXLoc(), getYLoc()+4);			
		}
		if (showMidpoints) {
			// draw midpoint
			g2d.drawOval(midPointX-2, midPointY-2, 4,4);			
		}
		g2d.setColor(Color.RED);
				
		if (showPins) {
			// draw pins
			for (Pin p : getConnections().getPin()) {
				g2d.fillOval(p.getX() - 2 + getXLoc(), p.getY() - 2 + getYLoc(), 4, 4);
			}			
		}
			
		for (Pin p : getConnections().getPin()) {
			
			StringBuffer pinCaption = new StringBuffer();
			
			if (showPinNumbers) {
				pinCaption.append(p.getNum());
			}
			if (showPinNames) {
				pinCaption.append("(" + p.getName() + ")");
			}
			
			int xoffset = 10;
			int yoffset = 10;
			
			switch(p.getOrientation()) {
				case TOP_LEFT : 
					xoffset = -10;
					yoffset = -10;
					break;
				case TOP_RIGHT : 
					xoffset = -10;
					yoffset =  10;
					break;
				case BOTTOM_LEFT : 
					xoffset = -10;
					yoffset =  10;
					break;
				case BOTTOM_RIGHT : 
					xoffset =  10;
					yoffset = 10;
					break;
				default : break;			
			}
		
			g2d.drawString(pinCaption.toString() , p.getX() + xoffset + getXLoc(), p.getY() + yoffset + getYLoc());
		}
		
		// restore saved transform
		if (rotation != 0 || mirrorHorizontal || mirrorVertical) {
			g2d.setTransform(saveXform);
		}

	}
	
	@Override
	public void draw(Graphics g) {
		Boolean showPinNames = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showPinNames");
		Boolean showPinNumbers = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showPinNumbers");
		Boolean showOrigins = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showOrigins");
		Boolean showMidpoints = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showMidpoints"); 
		Boolean showPins = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.drawPins");
		draw(g,showPins,showPinNames,showPinNumbers,showMidpoints,showOrigins);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {		
		
		Symbol symbol = new Symbol();
		symbol.setColor(getColor());
		symbol.setDescription(getDescription());
		symbol.setFilename(getFilename());
		symbol.setHeight(getHeight());
		symbol.setImage(getImage());
		symbol.setImageName(getImageName());
		symbol.setIndex(getIndex());
		symbol.setName(getName());
		symbol.setValue(getValue());
		symbol.setPackageType(getPackageType());
		symbol.setRotation(getRotation());
		symbol.setWidth(getWidth());
		symbol.setXLoc(getXLoc());
		symbol.setYLoc(getYLoc());
		symbol.setLayer(getLayer());
		symbol.setMirrorHorizontal(isMirrorHorizontal());
		symbol.setMirrorVertical(isMirrorVertical());
		symbol.setType(getType());
		
		// Copy connections
		for (Pin p : getConnections().getPin()) {			
			Pin pin = new Pin(p.getX(),p.getY());
			pin.setNum(p.getNum());
			pin.setName(p.getName());			
			// skip netindex because we surely need a new one			
			symbol.getConnections().getPin().add(pin);			
		}
		
		for (Item item : items) {
			
			if (item instanceof Line) {				

				Line line = (Line)item;
				
				Line newLine = new Line((int)line.getStart().getX(),(int)line.getStart().getY(),(int)line.getEnd().getX(),(int) line.getEnd().getY(),1);
				
				newLine.setStroke(line.getStroke());
				newLine.setStartType(line.getStartType());
				newLine.setEndType(line.getEndType());
				newLine.setColor(line.getColor());
				
				symbol.getItems().add(newLine);				
			}
			else {
				symbol.getItems().add((Item)item.clone());
			}
			
		}		
		
		return symbol;
	}
	
	/**
	 * @return the items
	 */
	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return the mirrorVertical
	 */
	public boolean isMirrorVertical() {
		return mirrorVertical;
	}

	/**
	 * @param mirrorVertical the mirrorVertical to set
	 */
	public void setMirrorVertical(boolean mirrorVertical) {
		this.mirrorVertical = mirrorVertical;
	}

	/**
	 * @return the mirrorHorizontal
	 */
	public boolean isMirrorHorizontal() {
		return mirrorHorizontal;
	}

	/**
	 * @param mirrorHorizontal the mirrorHorizontal to set
	 */
	public void setMirrorHorizontal(boolean mirrorHorizontal) {
		this.mirrorHorizontal = mirrorHorizontal;
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
	
	/**
	 * @return the customModel
	 */
	public boolean isCustomModel() {
		return customModel;
	}

	/**
	 * @param customModel the customModel to set
	 */
	public void setCustomModel(boolean customModel) {
		this.customModel = customModel;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * @return the type
	 */
	public SpiceType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SpiceType type) {
		this.type = type;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return name;	
	}
	
}
