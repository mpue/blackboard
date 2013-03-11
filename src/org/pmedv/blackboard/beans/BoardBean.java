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
package org.pmedv.blackboard.beans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;

/**
 * This is the container object for the {@link BoardEditorModel} data.
 * We need this in order to serialize the {@link BoardEditorModel} as xml 
 * file to the disk.
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
@XmlRootElement
@XmlType(propOrder = {"textParts","parts","lines","resistors","diodes","boxes","ellipses","symbols","layers","width", "height","type","backgroundImage"})
@XmlAccessorType(XmlAccessType.FIELD)

public class BoardBean {
	
	private ArrayList<TextPartBean> textParts;
	private ArrayList<PartBean> parts;
	private ArrayList<LineBean> lines;
	private ArrayList<ResistorBean> resistors;
	private ArrayList<DiodeBean> diodes;
	private ArrayList<LayerBean> layers;
	private ArrayList<BoxBean> boxes;
	private ArrayList<EllipseBean> ellipses;
	private ArrayList<SymbolBean> symbols;
	
	private int width;
	private int height;
	private BoardType type;
	private String backgroundImage;
	
	public BoardBean() {
		textParts = new ArrayList<TextPartBean>();
		parts = new ArrayList<PartBean>();
		lines = new ArrayList<LineBean>();
		resistors = new ArrayList<ResistorBean>();
		diodes = new ArrayList<DiodeBean>();
		layers = new ArrayList<LayerBean>();
		boxes = new ArrayList<BoxBean>();
		ellipses = new ArrayList<EllipseBean>();
		symbols = new ArrayList<SymbolBean>();
	}
	
	/**
	 * @return the textParts
	 */
	public ArrayList<TextPartBean> getTextParts() {
		return textParts;
	}


	/**
	 * @param textParts the textParts to set
	 */
	public void setTextParts(ArrayList<TextPartBean> textParts) {
		this.textParts = textParts;
	}


	/**
	 * @return the parts
	 */
	public ArrayList<PartBean> getParts() {
	
		return parts;
	}

	
	/**
	 * @param parts the parts to set
	 */
	public void setParts(ArrayList<PartBean> parts) {
	
		this.parts = parts;
	}

	
	/**
	 * @return the lines
	 */
	public ArrayList<LineBean> getLines() {
	
		return lines;
	}

	
	/**
	 * @param lines the lines to set
	 */
	public void setLines(ArrayList<LineBean> lines) {
	
		this.lines = lines;
	}


	public ArrayList<ResistorBean> getResistors() {
		return resistors;
	}

	public void setResistors(ArrayList<ResistorBean> resistors) {
		this.resistors = resistors;
	}
	
	/**
	 * @return the diodes
	 */
	public ArrayList<DiodeBean> getDiodes() {
		return diodes;
	}

	/**
	 * @param diodes the diodes to set
	 */
	public void setDiodes(ArrayList<DiodeBean> diodes) {
		this.diodes = diodes;
	}

	public ArrayList<BoxBean> getBoxes() {
		return boxes;
	}

	public void setBoxes(ArrayList<BoxBean> shapes) {
		this.boxes = shapes;
	}

	/**
	 * @return the ellipses
	 */
	public ArrayList<EllipseBean> getEllipses() {
		return ellipses;
	}

	/**
	 * @param ellipses the ellipses to set
	 */
	public void setEllipses(ArrayList<EllipseBean> ellipses) {
		this.ellipses = ellipses;
	}

	
	
	public ArrayList<SymbolBean> getSymbols() {
		return symbols;
	}

	public void setSymbols(ArrayList<SymbolBean> symbols) {
		this.symbols = symbols;
	}

	/**
	 * @return the layers
	 */
	public ArrayList<LayerBean> getLayers() {
		return layers;
	}


	/**
	 * @param layers the layers to set
	 */
	public void setLayers(ArrayList<LayerBean> layers) {
		this.layers = layers;
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
	 * @return the type
	 */
	public BoardType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(BoardType type) {
		this.type = type;
	}


	/**
	 * @return the backgroundImage
	 */
	public String getBackgroundImage() {
		return backgroundImage;
	}


	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	

}
