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

import java.util.ArrayList;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;

/**
 * {@link UndoableEdit} for the move event of an item
 * 
 * @author Matthias Pueski (29.05.2011)
 *
 */
public class MoveMultipleItemsEdit extends AbstractUndoableEdit {

	private static final long serialVersionUID = 268168440492800806L;
	
	private ArrayList<UndoableEdit> edits;
	
	
	public MoveMultipleItemsEdit(ArrayList<UndoableEdit> edits) {
		this.edits = edits;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		for (UndoableEdit edit : edits) {
			edit.undo();
		}
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();				
		BoardUtil.syncItemState(editor.getModel());
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		for (UndoableEdit edit : edits) {
			edit.redo();
		}
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();				
		BoardUtil.syncItemState(editor.getModel());
	}
	
	
	@Override
	public String getPresentationName() {
		return "Move items";
	}
	
	
	
}
