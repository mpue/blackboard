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

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Rotateable;

/**
 * @author Matthias Pueski (29.05.2011)
 *
 */
public class RotateCWEdit extends AbstractUndoableEdit {
	
	private static final long serialVersionUID = 41602875213297005L;

	private Rotateable part;
	
	public RotateCWEdit(Rotateable part) {
		this.part = part;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		part.rotateCCW();
		editor.updateStatusBar();				
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		part.rotateCW();
		editor.updateStatusBar();	
	}
	
	@Override
	public String getPresentationName() {
		return "Rotate CW";
	}
}
