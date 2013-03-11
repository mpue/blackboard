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
package org.pmedv.blackboard.commands;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.xml.bind.Marshaller;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.tools.PartFactory;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;

/**
 * The <code>ConvertToPartCommand</code> converts one or more selected {@link Item} objects
 * to a part object, which can be used later much easier.
 * 
 * @author Matthias Pueski (26.09.2011)
 *
 */
public class ConvertToPartCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -1410589465955713058L;
	
	public ConvertToPartCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ConvertToPartCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.convertpart"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ConvertToPartCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T,  InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		setEnabled(false);
	}	
	
	@Override
	public void execute(ActionEvent e) {
		
		final ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
		
		// need a name for the part
		
		String partName = JOptionPane.showInputDialog(win, resources.getResourceByKey("ConvertToPartCommand.message"));

		// no name? skip
		
		if (partName == null || partName.length() < 1)
			return;
		
		// which editor do we have?

		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		// first we need to check all items and determine min_x, min_y, max_x, max_y as well as height and width
		
		int min_x = Integer.MAX_VALUE;
		int min_y = Integer.MAX_VALUE;
		int max_x = Integer.MIN_VALUE;
		int max_y = Integer.MIN_VALUE;

		ArrayList<Item> items = editor.getSelectedItems();
		
		for (Item item :items) {
			
			if (item instanceof Line) {
				
				Line line = (Line)item;				
				Rectangle bb = line.getBoundingBox();
				
				int x1 = (int)bb.getLocation().getX();
				int y1 = (int)bb.getLocation().getY();				
				int x2 = x1 + (int)bb.getWidth();
				int y2 = y1 + (int)bb.getHeight();
				
				if (x1 < min_x)
					min_x = x1;
				if (y1 < min_y)
					min_y = y1;
				if (x2 > max_x)
					max_x = x2;
				if (y2 > max_y)
					max_y = y2;
			}
			else {
				if (item.getXLoc()+item.getWidth() > max_x)
					max_x = item.getXLoc() + item.getWidth();
				if (item.getYLoc()+item.getHeight() > max_y)
					max_y = item.getYLoc() + item.getHeight();
				if (item.getXLoc() < min_x)
					min_x = item.getXLoc();
				if (item.getYLoc() < min_y)
					min_y = item.getYLoc();							
			}
			
		}
				
		min_x -= 16;
		min_y -= 16;
		max_x += 16;
		max_y += 16;
		
		int width  = max_x - min_x;
		int height = max_y - min_y;
		
		// now create an empty image and render desired items to it.
		
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);		
		Graphics2D g2d = (Graphics2D)bi.getGraphics();
		
		// some nice anti aliasing...
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
			
		// sort items by z-index
		Collections.sort(items);		
		
		for (Item item :items) {
			
			// preserve old position and set new position and render, since we need to convert the coordinates
			// of the item to the newly calculated drawing area

			if (item instanceof Line) {
				
				Line line = (Line)item;
				
				line.getOldstart().setLocation(line.getStart().getX(), line.getStart().getY());
				line.getOldEnd().setLocation(line.getEnd().getX(), line.getEnd().getY());
				
				line.getStart().setLocation(line.getStart().getX() - min_x, line.getStart().getY() - min_y);
				line.getEnd().setLocation(line.getEnd().getX() - min_x, line.getEnd().getY() - min_y);
			}
			else {
			
				item.setOldXLoc(item.getXLoc());
				item.setOldYLoc(item.getYLoc());

				item.setXLoc(item.getXLoc() - min_x);
				item.setYLoc(item.getYLoc() - min_y);				
			}
			
			item.setOpacity(item.getOpacity());

			if (item instanceof Line) {
				Line line = (Line) item;
				if (line.getStroke() != null)
					g2d.setStroke(line.getStroke());
				else
					g2d.setStroke(BoardUtil.stroke_3_0f);
				g2d.setColor(item.getColor());
			}
			
			item.drawItem(g2d);
			
			// restore position
			if (item instanceof Line) {
				
				Line line = (Line)item;
				
				line.getStart().setLocation(line.getOldstart().getX(),line.getOldstart().getY());
				line.getEnd().setLocation(line.getOldEnd().getX(), line.getOldEnd().getY());				
			}
			else {
				item.setXLoc(item.getOldXLoc());
				item.setYLoc(item.getOldYLoc());				
			}
			
		}
			
			
		// now store the rendered image to the parts/images directory
		
		File workDir = new File(".");
		File partDir = new File(workDir + "/parts/");
		File imageDir = new File(workDir + "/parts/images/");
		File outputImage = new File(imageDir+"/"+partName+".png");
		File outputPart = new File(partDir+"/"+partName+".xml");
		
		try {
			ImageIO.write(bi, "PNG", outputImage);
		}
		catch (IOException e1) {
			ErrorUtils.showErrorDialog(e1);
		}

		// create according part
		
		Part newPart = new Part();

		newPart.setFilename(partName+".xml");
		newPart.setImage(bi);
		newPart.setIndex(0);
		newPart.setLayer(0);
		newPart.setAuthor(System.getProperty("user.name"));
		newPart.setLicense("");
		newPart.setColor(Color.WHITE);
		newPart.setDescription(partName);
		newPart.setDesignator("P");
		newPart.setHeight(height);
		newPart.setWidth(width);
		newPart.setImageName(outputImage.getName());
		newPart.setPackageType("symbol");
		newPart.setXLoc(0);
		newPart.setYLoc(0);
		newPart.setValue("");
		newPart.setName(partName);
		newPart.setResizable(false);
		newPart.setRotation(0);
		newPart.setOpacity(1.0f);
		
		// marshall to xml file

		try {
			Marshaller m = AppContext.getContext().getBean(PartFactory.class).getMarshaller();
			m.marshal(newPart, new FileOutputStream(outputPart));
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
			return;
		}

		AppContext.getContext().getBean(PartDialog.class).getModel().addPart(newPart);
		
		// remove original items and replace with created part 
		
		if (editor.getSelectedItems().size() > 1) {						
			editor.clearSelectionBorder();
			
			int result = JOptionPane.showConfirmDialog(AppContext.getContext().getBean(ApplicationWindow.class),
					resources.getResourceByKey("ConvertToPartCommand.keepOriginal"), resources.getResourceByKey("msg.warning"),
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if (result == JOptionPane.NO_OPTION) {
				for (Item item : editor.getSelectedItems()) {
					for (Layer layer : editor.getModel().getLayers())
						layer.getItems().remove(item);					
				}

			}
			else {
				// if original is kept, we place the part with an offset
				min_x += 16;
				min_y += 16;				
			}
			
			editor.getSelectedItems().clear();
			
			newPart.setXLoc(min_x);
			newPart.setYLoc(min_y);
			
			editor.getModel().getCurrentLayer().getItems().add(newPart);
			editor.setSelectedItem(newPart);
			editor.refresh();
		}
		
	}

}
