package org.pmedv.blackboard.beans;

import java.util.ArrayList;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.components.Box;
import org.pmedv.blackboard.components.Connections;
import org.pmedv.blackboard.components.Ellipse;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Pin;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.provider.BaseElement;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Symbol", propOrder = { "value","rotation","xLoc", "yLoc", "index", "color", 
			    "layer","name","category","width","height","lines","ellipses","boxes","textparts",
			    "mirrorVertical","mirrorHorizontal","connections","customModel","model",
			    "type","filename","properties"})
public class SymbolBean extends BaseElement {

	protected String value;
	protected int rotation = 0;
	protected int xLoc;
	protected int yLoc;
	protected int index;
	protected ColorBean color;
	protected int layer;
	protected String name;
	protected int width;
	protected int height;
	protected String category;
	protected boolean customModel = false;
	protected Model model;
	protected SpiceType type;
	private String filename;
	
	private Properties properties;
	
	private boolean mirrorVertical = false;
	private boolean mirrorHorizontal = false;
	
	private Connections connections = new Connections();
	
	private ArrayList<LineBean> lines;
	private ArrayList<EllipseBean> ellipses;	
	private ArrayList<BoxBean> boxes;
	private ArrayList<TextPartBean> textparts;
	
	@XmlTransient
	private String xmlContent;
	
	public SymbolBean() {
		lines = new ArrayList<LineBean>();
		ellipses = new ArrayList<EllipseBean>();
		boxes = new ArrayList<BoxBean>();
		textparts = new ArrayList<TextPartBean>();
	}
	
	public SymbolBean(Symbol symbol) {
		
		this();
		
		for (Item item : symbol.getItems()) {
			
			if (item instanceof Line) {
				Line line = (Line)item;
				lines.add(new LineBean(line));								
			}
			else if (item instanceof Ellipse) {
				Ellipse ellipse = (Ellipse)item;
				ellipses.add(new EllipseBean(ellipse));				
			}
			else if(item instanceof Box) {
				Box box = (Box)item;
				boxes.add(new BoxBean(box));
			}		
			else if (item instanceof TextPart) {
				TextPart text = (TextPart)item;
				textparts.add(new TextPartBean(text));
			}
		}
		
		for (Pin p : symbol.getConnections().getPin()) {
			connections.getPin().add(p);
		}
		
		this.xLoc = symbol.getXLoc();
		this.yLoc = symbol.getYLoc();
		this.rotation = symbol.getRotation();
		this.color = new ColorBean(symbol.getColor());
		this.index = symbol.getIndex();
		this.layer = symbol.getLayer();
		this.width = symbol.getWidth();
		this.height = symbol.getHeight();
		this.name = symbol.getName();
		this.value = symbol.getValue();
		this.category = symbol.getCategory();
		this.mirrorHorizontal = symbol.isMirrorHorizontal();
		this.mirrorVertical = symbol.isMirrorVertical();
		this.customModel = symbol.isCustomModel();
		this.model = symbol.getModel();
		this.type = symbol.getType();
		this.filename = symbol.getFilename();
		this.properties = symbol.getProperties();
	}

	public ArrayList<LineBean> getLines() {
		return lines;
	}

	public void setLines(ArrayList<LineBean> lines) {
		this.lines = lines;
	}

	public ArrayList<EllipseBean> getEllipses() {
		return ellipses;
	}

	public void setEllipses(ArrayList<EllipseBean> ellipses) {
		this.ellipses = ellipses;
	}

	public ArrayList<BoxBean> getBoxes() {
		return boxes;
	}

	public void setBoxes(ArrayList<BoxBean> boxes) {
		this.boxes = boxes;
	}

	/**
	 * @return the textparts
	 */
	public ArrayList<TextPartBean> getTextparts() {
		return textparts;
	}

	/**
	 * @param textparts the textparts to set
	 */
	public void setTextparts(ArrayList<TextPartBean> textparts) {
		this.textparts = textparts;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getXLoc() {
		return xLoc;
	}

	public void setXLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	public int getYLoc() {
		return yLoc;
	}

	public void setYLoc(int yLoc) {
		this.yLoc = yLoc;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ColorBean getColor() {
		return color;
	}

	public void setColor(ColorBean color) {
		this.color = color;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			SymbolBean symbol = (SymbolBean) obj;
			return symbol.getName().equals(name);				
		}
		return false;
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
		return "Symbol : "+name; 
	}
}
