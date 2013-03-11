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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Symbol;

/**
 * {@link UndoableEdit} for the move event of an item
 * 
 * @author Matthias Pueski (29.05.2011)
 *
 */
public class MoveItemEdit extends AbstractUndoableEdit {

	private static final long serialVersionUID = 268168440492800806L;
	
	private Item item;
	
	private boolean chained;
	
	private int oldX;
	private int oldY;
	private int newX;
	private int newY;
	
	public MoveItemEdit(Item item, int oldX, int oldY, int newX, int newY, boolean chained) {
		this.item = item;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.chained = chained;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		if (!chained)
			super.undo();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();		
		item.setXLoc(oldX);
		item.setYLoc(oldY);
		
		if (item instanceof Symbol) {
			
			Symbol symbol = (Symbol)item;
			
			for (Item subItem : symbol.getItems()) {

				if (subItem instanceof Line) {
					Line line = (Line) subItem;
					line.setStart(new Point((int) line.getStart().getX() - (newX - oldX), (int) line.getStart().getY() - (newY - oldY)));
					line.setEnd(new Point((int) line.getEnd().getX() - (newX - oldX), (int) line.getEnd().getY() - (newY - oldY)));
					line.setOldstart(new Point(line.getStart()));
					line.setOldEnd(new Point(line.getEnd()));
				}
				else {
					subItem.setXLoc(subItem.getXLoc() - (newX - oldX));
					subItem.setYLoc(subItem.getYLoc() - (newY - oldY));
					subItem.setOldXLoc(subItem.getXLoc());
					subItem.setOldYLoc(subItem.getYLoc());
					subItem.setOldWidth(subItem.getWidth());
					subItem.setOldHeight(subItem.getHeight());
				}

			}
			
		}
		
		editor.refresh();
		editor.setFileState(FileState.DIRTY);
	}

	@Override
	public void redo() throws CannotRedoException {
		if (!chained)
			super.redo();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		item.setXLoc(newX);
		item.setYLoc(newY);
		
		if (item instanceof Symbol) {
			
			Symbol symbol = (Symbol)item;
			
			for (Item subItem : symbol.getItems()) {

				if (subItem instanceof Line) {
					Line line = (Line) subItem;
					line.setStart(new Point((int) line.getStart().getX() - (oldX - newX), (int) line.getStart().getY() - (oldY - newY)));
					line.setEnd(new Point((int) line.getEnd().getX() - (oldX - newX), (int) line.getEnd().getY() - (oldY - newY)));
					line.setOldstart(new Point(line.getStart()));
					line.setOldEnd(new Point(line.getEnd()));
				}
				else {
					subItem.setXLoc(subItem.getXLoc() - (oldX - newX));
					subItem.setYLoc(subItem.getYLoc() - (oldY - newY));
					subItem.setOldXLoc(subItem.getXLoc());
					subItem.setOldYLoc(subItem.getYLoc());
					subItem.setOldWidth(subItem.getWidth());
					subItem.setOldHeight(subItem.getHeight());
				}

			}
			
		}
		
		editor.refresh();
		editor.setFileState(FileState.DIRTY);
	}
	
	
	@Override
	public String getPresentationName() {
		return "Move item";
	}
	
	
	
}
