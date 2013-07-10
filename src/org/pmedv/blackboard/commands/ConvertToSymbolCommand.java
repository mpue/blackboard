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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

/**
 * The <code>ConvertToSymbolCommand</code> converts one or more selected {@link Item} objects
 * to a {@link Symbol} object, which can be used later much easier.
 * 
 * @author Matthias Pueski (26.09.2011)
 *
 */
public class ConvertToSymbolCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -1410589465955713058L;
	
	private static final Log log = LogFactory.getLog(ConvertToSymbolCommand.class);
	
	public ConvertToSymbolCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ConvertToSymbolCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.converttosymbol"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ConvertToSymbolCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		setEnabled(false);
	}	
	
	@Override
	public void execute(ActionEvent e) {

		// which editor do we have?
		
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		final List<Item> items = editor.getSelectedItems();
		final List<Item> preserveItems = new ArrayList<Item>();
		
		for (Item item : items) {
			if (item instanceof Symbol) {
				ErrorUtils.showErrorDialog(new IllegalStateException(resources.getResourceByKey("error.onlyshapes")));
				return;
			}
		}
		
		
		// first we need to check all items and determine min_x, min_y, max_x, max_y as well as height and width
		
		int min_x = Integer.MAX_VALUE;
		int min_y = Integer.MAX_VALUE;
		int max_x = Integer.MIN_VALUE;
		int max_y = Integer.MIN_VALUE;

		
		for (Item item : items) {
			
			if (item instanceof Line) {
				
				final Line line = (Line)item;				
				final Rectangle boundingBox = line.getBoundingBox();
				
				int x1 = (int)boundingBox.getLocation().getX();
				int y1 = (int)boundingBox.getLocation().getY();				
				int x2 = x1 + (int)boundingBox.getWidth();
				int y2 = y1 + (int)boundingBox.getHeight();
				
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
						
		int width  = max_x - min_x;
		int height = max_y - min_y;
		
		final Symbol symbol = new Symbol();
		symbol.setWidth(width);
		symbol.setHeight(height);
		
		// sort items by z-index
		Collections.sort(items);		

		symbol.setLayer(items.get(0).getLayer());
		
		for (final Item item : items) {
			
			// preserve old position and set new position and render, since we need to convert the coordinates
			// of the item to the newly calculated drawing area

			if (item instanceof Line) {
				
				final Line line = (Line)item;
				
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

			
			// restore position
			if (item instanceof Line) {
				
				final Line line = (Line)item;
				
				line.getStart().setLocation(line.getOldstart().getX(),line.getOldstart().getY());
				line.getEnd().setLocation(line.getOldEnd().getX(), line.getOldEnd().getY());				
			}
			else {
				item.setXLoc(item.getOldXLoc());
				item.setYLoc(item.getOldYLoc());				
			}
			
			symbol.getItems().add(item);
			
		}
			
		
		// now get a new index		
		int max = 0;
		
		for (final Layer layer : editor.getModel().getLayers()) {		
			for (Item item : layer.getItems()) {				
				if (item.getIndex() > max)
					max = item.getIndex();				
			}			
		}
		
		max++;		
		symbol.setIndex(max);		
		
		// remove original items and replace with created symbol 
		
		if (editor.getSelectedItems().size() > 1) {						
			editor.clearSelectionBorder();

			preserveItems.addAll(editor.getSelectedItems());
			
			for (Item item : editor.getSelectedItems()) {
				for (Layer layer : editor.getModel().getLayers())
					layer.getItems().remove(item);					
			}
			
			editor.getSelectedItems().clear();
			
			symbol.setXLoc(min_x);
			symbol.setYLoc(min_y);
			
			editor.getModel().getCurrentLayer().getItems().add(symbol);
			editor.setSelectedItem(symbol);
			
			UndoManager undoManager = editor.getUndoManager();				
			if (!undoManager.addEdit(new ConvertToSymbolEdit(preserveItems, symbol))) {
				log.info("Could not add edit "+this.getClass());
			}
			editor.setFileState(FileState.DIRTY);

			AppContext.getContext().getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
			AppContext.getContext().getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
			
			editor.refresh();
		}

	}

}
