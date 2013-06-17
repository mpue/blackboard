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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Line;

/**
 * {@link UndoableEdit} for the {@link AddItemCommand} if a line split is being created
 * 
 * @author Matthias Pueski (29.05.2011)
 *
 */
public class CreateSplitEdit extends AbstractUndoableEdit {

	private static final long serialVersionUID = 268168440492800806L;
	
	private Line newline;
	private Line orig;
	private Line l1;
	private Line l2;
	
	/**
	 * @param orig
	 * @param l1
	 * @param l2
	 */	
	public CreateSplitEdit(Line newline, Line orig, Line l1, Line l2) {
		this.newline = newline;
		this.orig = orig;
		this.l1 = l1;
		this.l2 = l2;
	}
	
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		editor.getModel().getLayer(l1.getLayer()).getItems().remove(l1);
		editor.getModel().getLayer(l2.getLayer()).getItems().remove(l2);
		editor.getModel().getLayer(orig.getLayer()).getItems().add(orig);
		editor.getModel().getLayer(newline.getLayer()).getItems().remove(newline);
		editor.clearSelectionBorder();
		editor.refresh();
		editor.updateStatusBar();				
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		editor.getModel().getLayer(l1.getLayer()).getItems().add(l1);
		editor.getModel().getLayer(l2.getLayer()).getItems().add(l2);
		editor.getModel().getLayer(orig.getLayer()).getItems().remove(orig);
		editor.getModel().getLayer(newline.getLayer()).getItems().add(newline);
		editor.clearSelectionBorder();
		editor.refresh();
		editor.updateStatusBar();				
	}
	
	
	@Override
	public String getPresentationName() {
		return "Add item";
	}
}
