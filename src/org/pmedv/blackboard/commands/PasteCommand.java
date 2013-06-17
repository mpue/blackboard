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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.components.TextPart.TextType;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

public class PasteCommand extends AbstractCommand {

	private static final long serialVersionUID = -1410589465955713058L;
	private static final Log log = LogFactory.getLog(PasteCommand.class);

	public PasteCommand() {
		putValue(Action.NAME, resources.getResourceByKey("PasteCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.paste"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("PasteCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		List<Item> items = AppContext.getClipboard().paste();

		editor.clearSelectionBorder();
		editor.getSelectedItems().clear();
		editor.setSelectedItem(null);

		ArrayList<Item> pastedItems = new ArrayList<Item>();
		
		for (Item item : items) {

			try {
				Item i = (Item) item.clone();
				
				/**
				 * Determine next free index
				 */
				int maxIndex = 0;
				for (Layer layer : editor.getModel().getLayers()) {
					for (Item _item : layer.getItems()) {
						if (_item.getIndex() > maxIndex) {
							maxIndex = _item.getIndex();
						}
					}
				}
				maxIndex++;
				i.setIndex(maxIndex);	
				
				editor.getSelectedItems().add(i);
				
				if (i instanceof Line) {
					Line line = (Line) i;
					line.setStart(new Point((int) line.getStart().getX() + 32, (int) line.getStart().getY() + 32));
					line.setEnd(new Point((int) line.getEnd().getX() + 32, (int) line.getEnd().getY() + 32));
					line.setOldstart(new Point(line.getStart()));
					line.setOldEnd(new Point(line.getEnd()));					
				}
				else {
					i.setXLoc(i.getXLoc() + 32);
					i.setYLoc(i.getYLoc() + 32);
					i.setOldXLoc(i.getXLoc());
					i.setOldYLoc(i.getYLoc());
					i.setOldWidth(i.getWidth());
					i.setOldHeight(i.getHeight());
				}

				editor.getModel().getLayer(i.getLayer()).getItems().add(i);
				
				
				if (i instanceof Resistor) {					
					String designator = BoardUtil.getNextFreeDesignator(editor.getModel(),"R");
					i.setName(designator);										
				}
				else if(i instanceof Part && !(i instanceof Symbol)) {
					Part part = (Part)i;
					String designator = BoardUtil.getNextFreeDesignator(editor.getModel(),part.getDesignator());
					i.setName(designator);
				}
				
				if (i instanceof Symbol) {
					
					Symbol symbol = (Symbol)i;

					String designator = "X";
					
					if (null != symbol.getName() && symbol.getName().length() > 1) {
						 
						designator = String.valueOf(symbol.getName().charAt(0));						

						 if (!symbol.getName().equalsIgnoreCase("gnd")) {
							 String name = BoardUtil.getNextFreeDesignator(editor.getModel(), designator);
							 symbol.setName(name);
						 }						 
					}

					
					for (Item subItem : symbol.getItems()) {

						if (subItem instanceof Line) {
							Line line = (Line) subItem;
							line.setStart(new Point((int) line.getStart().getX() + 32, (int) line.getStart().getY() + 32));
							line.setEnd(new Point((int) line.getEnd().getX() + 32, (int) line.getEnd().getY() + 32));
							line.setOldstart(new Point(line.getStart()));
							line.setOldEnd(new Point(line.getEnd()));
						}
						else {
							subItem.setXLoc(subItem.getXLoc() + 32);
							subItem.setYLoc(subItem.getYLoc() + 32);
							subItem.setOldXLoc(subItem.getXLoc());
							subItem.setOldYLoc(subItem.getYLoc());
							subItem.setOldWidth(subItem.getWidth());
							subItem.setOldHeight(subItem.getHeight());
						}

						if (subItem instanceof TextPart) {
							
							TextPart text = (TextPart)subItem;
							
							if (text.getType() != null) {

								if (null != symbol.getName() && text.getType().equals(TextType.NAME)) {
									text.setText(symbol.getName());
								}
								if (null != symbol.getValue() && text.getType().equals(TextType.VALUE)) {
									text.setText(symbol.getValue());
								}
								
							}
							
						}						

					}
					
				}
				pastedItems.add(i);
			}
			catch (CloneNotSupportedException e1) {
				ErrorUtils.showErrorDialog(e1);
			}

		}
		
		// Switch to select after paste
		AppContext.getContext().getBean(SetSelectModeCommand.class).execute(null);

		UndoManager undoManager = editor.getUndoManager();				
		if (!undoManager.addEdit(new PasteEdit(pastedItems))) {
			log.info("Could not add edit "+this.getClass());
		}
		editor.setFileState(FileState.DIRTY);

		AppContext.getContext().getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		AppContext.getContext().getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
		
		if (pastedItems.size() == 1) {
			editor.getSelectedItems().clear();
			editor.setSelectedItem(pastedItems.get(0));
		}
		
		editor.refresh();
	}

}
