package org.pmedv.blackboard.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.components.TextPart.TextType;

/**
 * @author Matthias Pueski (16.06.2011)
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TextPart", propOrder = { "xLoc", "yLoc", "index", "color", "layer","name","width","height","rotation","value","text", "font", "fontsize", "type" })
public class TextPartBean {
	
	private int xLoc;
	private int yLoc;
	private int index;
	private ColorBean color;
	private int layer;
	private String name;
	private int width;
	private int height;
	private String value;
	private int rotation;
	private String text;
	private String font;
	private int fontsize;
	private TextType type;
	
	public TextPartBean() {		
	}
	
	public TextPartBean(TextPart part) {
		this.text = part.getText();
		this.font = part.getFont().getFontName();
		this.xLoc = part.getXLoc();
		this.yLoc = part.getYLoc();
		this.rotation = part.getRotation();
		this.fontsize = part.getFont().getSize();
		this.color = new ColorBean(part.getColor());
		this.index = part.getIndex();
		this.layer = part.getLayer();
		this.type = part.getType();
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the font
	 */
	public String getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(String font) {
		this.font = font;
	}

	/**
	 * @return the fontsize
	 */
	public int getFontsize() {
		return fontsize;
	}

	/**
	 * @param fontsize the fontsize to set
	 */
	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}

	/**
	 * @return the xLoc
	 */
	public int getXLoc() {
		return xLoc;
	}

	/**
	 * @param xLoc the xLoc to set
	 */
	public void setXLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	/**
	 * @return the yLoc
	 */
	public int getYLoc() {
		return yLoc;
	}

	/**
	 * @param yLoc the yLoc to set
	 */
	public void setYLoc(int yLoc) {
		this.yLoc = yLoc;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the color
	 */
	public ColorBean getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(ColorBean color) {
		this.color = color;
	}

	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
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
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
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

	public TextType getType() {
		return type;
	}

	public void setType(TextType type) {
		this.type = type;
	}
	

	
}
