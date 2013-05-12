/**

	BreadBoard Editor
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

package org.pmedv.blackboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.commands.AddItemCommand;
import org.pmedv.blackboard.commands.SetSelectModeCommand;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Box;
import org.pmedv.blackboard.components.Ellipse;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.LineEdgeType;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Pin;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.components.WireConnection;
import org.pmedv.blackboard.components.WireConnection.WireConnectionType;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.spice.Net;
import org.pmedv.blackboard.tools.ConnectionUtils;
import org.pmedv.blackboard.tools.PartFactory;
import org.pmedv.blackboard.tools.StrokeFactory;
import org.pmedv.blackboard.tools.StrokeFactory.StrokeType;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

/**
 * Convenience utility class for common {@link BoardEditor} tasks.
 * 
 * @author Matthias Pueski (28.12.2010)
 * 
 */
public class BoardUtil {

	// some convenient strokes
	public static final BasicStroke stroke_0_6f = new BasicStroke(0.6f);
	public static final BasicStroke stroke_1_0f = new BasicStroke(1.0f);
	public static final BasicStroke stroke_1_5f = new BasicStroke(1.5f);
	public static final BasicStroke stroke_2_0f = new BasicStroke(2.0f);
	public static final BasicStroke stroke_2_5f = new BasicStroke(2.5f);
	public static final BasicStroke stroke_3_0f = new BasicStroke(3.0f);
	public static final BasicStroke stroke_4_0f = new BasicStroke(4.0f);

	/**
	 * Snaps the current given location to a given grid
	 * 
	 * @param location the location to be snapped
	 * @param raster the raster size
	 * @param tolerance the tolerance to cosult while snapping
	 * @return
	 */
	public static int snap(int location, int raster) {

		int toleranceWindow = (raster / 2);

		if (location > 0) {
			if ((location % raster) > toleranceWindow) {
				location = location + (raster - (location % raster));
			}
			else {
				location = location - (location % raster);
			}
		}
		else {
			if ((location % raster) < toleranceWindow) {
				location = location + (raster - (location % raster)) - raster;
			}
			else {
				location = location - (location % raster) - raster;
			}
		}
		return location;
	}

	/**
	 * Draws a grid with a specified raster size
	 * 
	 * @param g the graphics context
	 * @param minorRaster raster size in pixels
	 */
	public static void drawGrid(Graphics g, int minorRaster, int width, int height, boolean dots) {

		if (dots) {
			g.setColor(Color.BLACK);
			for (int x = 0; x < width; x += minorRaster) {
				for (int y = 0; y < height; y += minorRaster) {
					g.fillOval(x, y, 1, 1);
				}
			}

		}
		else {
			int majorRaster = 16;

			if (minorRaster < 16 && minorRaster >= 2) {
				g.setColor(Colors.LIGHTER_GRAY);

				for (int i = minorRaster; i < width; i += minorRaster) {
					g.drawLine(i, 0, i, height);
				}
				for (int i = minorRaster; i < height; i += minorRaster) {
					g.drawLine(0, i, width, i);
				}

			}

			g.setColor(Color.LIGHT_GRAY);

			for (int i = majorRaster; i < width; i += majorRaster) {
				g.drawLine(i, 0, i, height);
			}
			for (int i = majorRaster; i < height; i += majorRaster) {
				g.drawLine(0, i, width, i);
			}

		}

	}

	/**
	 * Rotates a 2-dimensional point with the coordinates x,y arount another
	 * point xC,yC with the specified angle.
	 * 
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 * @param xC the x-coordinate of the rotation center
	 * @param yC the y-coordinate of the rotation center
	 * @param angle the angle of rotation in degrees
	 * 
	 * @return the rotated {@link Point}
	 */
	public static Point rotatePoint(int x, int y, int xC, int yC, float angle) {
		float rot = (float) Math.toRadians(angle);
		double dx = xC + (x - xC) * Math.cos(rot) + (y - yC) * Math.sin(rot);
		double dy = yC - (x - xC) * Math.sin(rot) + (y - yC) * Math.cos(rot);		
		int x_ = (int) dx;
		int y_ = (int) dy;
		return new Point(x_, y_);
	}

	public static Point rotatePoint(double x, double y, int xC, int yC, float angle) {
		float rot = (float) Math.toRadians(angle);
		double dx = xC + (x - xC) * Math.cos(rot) + (y - yC) * Math.sin(rot);
		double dy = yC - (x - xC) * Math.sin(rot) + (y - yC) * Math.cos(rot);		
		int x_ = (int) dx;
		int y_ = (int) dy;
		return new Point(x_, y_);
	}

	
	public static void syncItemState(BoardEditorModel model) {		
		for (Layer layer : model.getLayers()) {
			syncItemState(layer.getItems());
		}		
	}
	
	public static void syncItemState(ArrayList<Item> items) {

		for (Item item : items) {

			if (item instanceof Part || item instanceof Box || item instanceof Ellipse || item instanceof Symbol) {
				item.setOldXLoc(item.getXLoc());
				item.setOldYLoc(item.getYLoc());
				item.setOldWidth(item.getWidth());
				item.setOldHeight(item.getHeight());
			}
			if (item instanceof Line) {
				Line line = (Line) item;
				line.setOldstart(new Point(line.getStart()));
				line.setOldEnd(new Point(line.getEnd()));
			}
			if (item instanceof Symbol) {

				Symbol symbol = (Symbol) item;

				for (Item subItem : symbol.getItems()) {
					// restore position
					if (subItem instanceof Line) {
						Line line = (Line) subItem;
						line.setOldstart(new Point(line.getStart()));
						line.setOldEnd(new Point(line.getEnd()));
					}
					else {
						subItem.setOldXLoc(subItem.getXLoc());
						subItem.setOldYLoc(subItem.getYLoc());
						subItem.setOldWidth(subItem.getWidth());
						subItem.setOldHeight(subItem.getHeight());
					}

				}

			}

		}

	}

	/**
	 * <p>
	 * Calculates the next free designator for a new {@link Item} to be added.
	 * </p>
	 * <p>
	 * This function checks all {@link Item} objects of a
	 * {@link BoardEditorModel} and determines the name for the next item.
	 * </p>
	 * <p>
	 * Example:
	 * </p>
	 * <p>
	 * If there are already parts named R1, R2 and R3, the next name would be
	 * R4.
	 * </p>
	 * 
	 * @param model The model to check for existing items
	 * 
	 * @return A new name for an item to be added
	 */
	public static String getNextFreeDesignator(BoardEditorModel model, String designator) {
		if (designator == null)
			return "X";
		int maxDesignator = 0;
		for (Layer layer : model.getLayers()) {
			for (Item item : layer.getItems()) {
				if (item.getName() != null && item.getName().startsWith(designator)) {
					int designatorIndex = 0;
					String sDesignatorIndex = item.getName().substring(designator.length());
					try {
						designatorIndex = Integer.valueOf(sDesignatorIndex);
					}
					catch (NumberFormatException e1) {
						designatorIndex = 0;
					}
					if (designatorIndex > maxDesignator)
						maxDesignator = designatorIndex;
				}
			}
		}
		maxDesignator++;

		return designator + maxDesignator;
	}

	public static void createRoundScale(final BoardEditorModel model, Point location, final int steps, int range, int startValue, final float offsetStart, int innerRadius, int outerRadius,
			float maxAngle, boolean showCaption, final Font captionFont, float thickness, boolean subSteps) {

		Point center = new Point(location.x, location.y);
		Point inner = new Point(location.x, location.y - innerRadius);
		Point textLoc = new Point(location.x, location.y - (outerRadius + 20));
		Point radial = new Point(location.x, location.y - outerRadius);

		float stepsize = maxAngle / steps;

		for (float f = maxAngle + offsetStart; f > 0.0f + offsetStart; f -= stepsize) {

			Point p1 = BoardUtil.rotatePoint(radial.x, radial.y, center.x, center.y, f);
			Point p2 = BoardUtil.rotatePoint(inner.x, inner.y, center.x, center.y, f);
			Point p3 = BoardUtil.rotatePoint(textLoc.x, textLoc.y, center.x, center.y, f);

			Line line = new Line((Point) p2.clone(), (Point) p1.clone(), 0);
			line.setStartType(LineEdgeType.STRAIGHT);
			line.setEndType(LineEdgeType.STRAIGHT);
			line.setStroke((BasicStroke) StrokeFactory.createStroke(thickness, StrokeType.BASIC));
			line.setColor(Color.BLACK);

			model.getLayer(0).getItems().add(line);

			if (showCaption) {
				TextPart p = new TextPart(String.valueOf(startValue), captionFont, Color.black, p3.x, p3.y,0);
				p.setxLoc(p.getxLoc() - p.getWidth() / 2);
				p.setyLoc(p.getyLoc() - p.getHeight() / 2);
				model.getLayer(0).getItems().add(p);
				startValue += range;
			}

		}

		float subStepSize = maxAngle / (steps * range);

		if (subSteps && range > 1) {
			for (float f = maxAngle + offsetStart; f > 0.0f + offsetStart; f -= subStepSize) {

				Point p1 = BoardUtil.rotatePoint(radial.x, radial.y, center.x, center.y, f);
				Point p2 = BoardUtil.rotatePoint(inner.x, inner.y - 10, center.x, center.y, f);

				Line line = new Line((Point) p2.clone(), (Point) p1.clone(), 0);
				line.setStartType(LineEdgeType.STRAIGHT);
				line.setEndType(LineEdgeType.STRAIGHT);
				line.setStroke((BasicStroke) StrokeFactory.createStroke(1.0f, StrokeType.BASIC));
				line.setColor(Color.BLACK);

				model.getLayer(0).getItems().add(line);
			}

		}

	}

	/**
	 * Convenience method for adding a {@link Part} to an {@link BoardEditor}
	 * 
	 * @param name the name of the {@link Part} to be added
	 * @param editor the {@link BoardEditor} to add the part to
	 */
	public static void addPart(String name, BoardEditor editor) {

		if (editor == null) {
			return;
		}

		int xLoc = 40;
		int yLoc = 40;

		// now get a new index
		int max = 0;

		for (Layer layer : editor.getModel().getLayers()) {
			for (Item item : layer.getItems()) {
				if (item.getIndex() > max)
					max = item.getIndex();
			}
		}

		max++;

		try {
			
			Part original = (Part) AppContext.getContext().getBean(PartFactory.class).getPart(name).clone();
			Part part = null;
			try {
				part = AppContext.getContext().getBean(PartFactory.class).createPart(original.getFilename());
			}
			catch (Exception e) {
				ErrorUtils.showErrorDialog(e);
				return;
			}
			
			part.setXLoc(xLoc);
			part.setYLoc(yLoc);
			part.setOldXLoc(xLoc);
			part.setOldYLoc(yLoc);
			part.setOldWidth(part.getWidth());
			part.setOldHeight(part.getOldHeight());

			part.setIndex(max);

			/**
			 * Part has a defined designator. Thus we need to check if there are
			 * any parts sharing that designator and get the maximum index of
			 * that part
			 */

			if (part.getDesignator() != null && part.getDesignator().length() > 0) {
				part.setName(BoardUtil.getNextFreeDesignator(editor.getModel(), part.getDesignator()));
			}
			else
				part.setName("");

			AddItemCommand cmd = new AddItemCommand();
			cmd.setItem(part);
			cmd.execute(null);

			editor.getSelectedItems().add(part);

			// Switch to select mode after insert
			AppContext.getContext().getBean(SetSelectModeCommand.class).execute(null);

			editor.setFileState(FileState.DIRTY);
			editor.updateStatusBar();

		}
		catch (CloneNotSupportedException e1) {
			return;
		}

	}

	/**
	 * Creates a netlist for a given editor, the netlist contains
	 * a list of connected wires.
	 * 
	 * @param model The {@link BoardEditorModel} to create the netlist for
	 * 
	 * @return a list of connected {@link Net} objects
	 */
	public static ArrayList<Net> createNetList(BoardEditorModel model) {
		
		ArrayList<Line> lines = new ArrayList<Line>();
		
		for (Layer layer : model.getLayers()) {
			
			for (Item item : layer.getItems()) {
				
				if (item instanceof Line) {					
					Line line = (Line) item;					
					lines.add(line);
				}
				
			}
			
		}
		
		ArrayList<Net> nets = new ArrayList<Net>();
		
		for(Layer layer : model.getLayers()) {
			
			for(Item item : layer.getItems()) {

				if (item instanceof Line) {
					
					Line line = (Line)item;
					Net net = new Net();
					net.setLines(ConnectionUtils.getConnectedLines(line, lines));
					
					if (!nets.contains(net) && net.getLines().size() > 0) {
						nets.add(net);
					}
					
				}
				
			}
			
		}
		
		int netIndex = 1;
		
		for (Net net : nets) {
			
			for (Line line : net.getLines()) {				
				line.setNetIndex(netIndex);					
			}
			net.setIndex(netIndex);			
			netIndex++;
		}
				
		return nets;
		
	}
	
	/**
	 * Convenience method to gather a list of {@link Symbol} objects of a 
	 * {@link BoardEditorModel}
	 * 
	 * @param model The model to get the symbols for
	 * 
	 * @return A list of symbols
	 */
	public static List<Symbol> getSymbols(BoardEditorModel model) {

		List<Symbol> symbols = new LinkedList<Symbol>();

		for (Layer layer : model.getLayers()) {

			for (Item item : layer.getItems()) {

				if (item instanceof Symbol) {

					Symbol symbol = (Symbol) item;
					symbols.add(symbol);
				}
			}
		}

		return symbols;
	}
	
	/**
	 * Convenience method to gather a list of {@link TextPart} objects of a 
	 * {@link BoardEditorModel}
	 * 
	 * @param model The model to get the text parts for
	 * 
	 * @return A list of {@link TextPart} objects
	 */
	public static List<TextPart> getTextParts(BoardEditorModel model) {

		List<TextPart> textParts = new LinkedList<TextPart>();

		for (Layer layer : model.getLayers()) {

			for (Item item : layer.getItems()) {

				if (item instanceof TextPart) {

					TextPart part = (TextPart) item;
					textParts.add(part);
				}
			}
		}

		return textParts;
	}
	
	/**
	 * Determines which pin belongs to which net. Each net has an index. This function
	 * checks all {@link Pin} objects of all {@link Symbol} objects and sets the according 
	 * {@link Net} index. 
	 * 
	 * @param model The model to determine the {@link Pin} indices for 
	 * @param nets  The list of {@link Net} objetcs to associate with the {@link Pin} objects
	 */
	public static void createPinIndices(BoardEditorModel model,List<Net> nets) {
		
		List<Symbol> symbols = getSymbols(model); 
		
		for (Net net : nets) {
			for (Line line : net.getLines()) {
				
				for (Symbol symbol : symbols) {
					for (Pin pin : symbol.getConnections().getPin()) {
						
						if (symbol.getRotation() != 0) {
							float rot = symbol.getRotation();
							
							if (rot == 90 || rot == 270) {
								rot -= 180;
							}
							
							// we need to rotate the points of the lines of the rotated symbol to determine the correct locations.
							Point p = BoardUtil.rotatePoint(pin.getX(),pin.getY(), 0, 0, rot);
							
							if (line.containsPoint((float)p.getX()+symbol.getxLoc(),(float)p.getY()+symbol.getYLoc(),2)) {
								pin.setNetIndex(line.getNetIndex());
							}
							
						}
						else {
							if (line.containsPoint(pin.getX()+symbol.getxLoc(),pin.getY()+symbol.getYLoc(),2)) {
								pin.setNetIndex(line.getNetIndex());
							}						
						}
						
					}					
				}
				
			}
		}
	}	
	
	/**
	 * Determines all connections of the board wires to a specific {@link Item}
	 * 
	 * @param selectedItem the item to determine the connections for
	 * @param model The model to perform the search on
	 */
	public static void findConnectionsToItem(Item selectedItem, BoardEditorModel model) {
		
		int tolerance = 4;
		
		if (selectedItem != null && selectedItem instanceof Part) {
	
			Part part = (Part)selectedItem;
			part.getWireConnections().clear();
	
			for (Pin p : part.getConnections().getPin()) {
			
				for (Layer layer : model.getLayers()) {
						
					for(Item otherItem : layer.getItems()) {
						
						if (otherItem instanceof Line) {
							
							Line otherLine = (Line)otherItem;
	
							if (part instanceof Symbol) {								
								Symbol symbol = (Symbol)part;								
								if (symbol.getItems().contains(otherLine)) {
									continue;
								}
							}
							
	
							float rot = part.getRotation();
							
							if (rot == 90 || rot == 270) {
								rot -= 180;
							}
							
							// we need to rotate the points of the lines of the rotated symbol to determine the correct locations.
							Point s = BoardUtil.rotatePoint(p.getX(),p.getY(), 0, 0, rot);
							s.setLocation(s.x + part.getxLoc(), s.y + part.getYLoc());
														
							if ((Math.abs(s.x - otherLine.getStart().x) <= tolerance &&
								 Math.abs(s.y - otherLine.getStart().y) <= tolerance)) {
								part.getWireConnections().add(new WireConnection(otherLine,WireConnectionType.START));
							}
							if ((Math.abs(s.x - otherLine.getEnd().x) <= tolerance &&
							     Math.abs(s.y - otherLine.getEnd().y) <= tolerance)) {
								part.getWireConnections().add(new WireConnection(otherLine,WireConnectionType.END));
							}
							
						}
						
					}
					
				}
	
			}
			
		}
		
	}	
	/**
	 * Determines all connections of the board wires to a specific {@link Line}
	 * 
	 * @param selectedItem the item to determine the connections for
	 * @param model The model to perform the search on
	 */
	public static void findConnectionsToLine(Line line, BoardEditorModel model) {
		
		int tolerance = 4;

		line.getWireConnections().clear();
	
		for (Layer layer : model.getLayers()) {
			
			for (Item item : layer.getItems()) {
				
				if (item instanceof Line) {					
	
					Line otherLine = (Line)item;
	
					if (!line.equals(otherLine)) {
	
						Point s = line.getStart();
						Point e = line.getEnd();
						
						// other line start coincides with line start
						if ((Math.abs(s.x - otherLine.getStart().x) <= tolerance &&
							 Math.abs(s.y - otherLine.getStart().y) <= tolerance)) {
							line.getWireConnections().add(new WireConnection(otherLine,WireConnectionType.START_START));
						}
						// other line start coincides with line end
						else if((Math.abs(e.x - otherLine.getStart().x) <= tolerance &&
								 Math.abs(e.y - otherLine.getStart().y) <= tolerance)) {
							line.getWireConnections().add(new WireConnection(otherLine,WireConnectionType.START_END));
							
						}
						// other line end coincides with line start
						else if ((Math.abs(s.x - otherLine.getEnd().x) <= tolerance &&
								  Math.abs(s.y - otherLine.getEnd().y) <= tolerance)) {
							line.getWireConnections().add(new WireConnection(otherLine,WireConnectionType.END_START));
						}
						// other line end coincides with line end
						else if((Math.abs(e.x - otherLine.getEnd().x) <= tolerance &&
								 Math.abs(e.y - otherLine.getEnd().y) <= tolerance)) {
							line.getWireConnections().add(new WireConnection(otherLine,WireConnectionType.END_END));								
						}
	
					}
					
				}
				
			}
		
		}
		
	}	
	
	public static Point mirrorTransform(Point point, JXLayer<?> zoomLayer,MouseEvent event) {
		point = SwingUtilities.convertPoint(event.getComponent(), point, zoomLayer);
		TransformUI ui = (TransformUI)(Object) zoomLayer.getUI();
		DefaultTransformModel model = (DefaultTransformModel) ui.getModel();
		AffineTransform at = model.getTransform((JXLayer<? extends JComponent>) zoomLayer);
		at.transform(point, point);
		return point;
	}
	
}
