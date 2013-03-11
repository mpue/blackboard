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
import org.pmedv.blackboard.components.Line;

/**
 * {@link UndoableEdit} for the move line event.
 * 
 * @author Matthias Pueski (29.05.2011)
 *
 */
public class MoveLineEdit extends AbstractUndoableEdit {

	private static final long serialVersionUID = 268168440492800806L;
	
	private Line line;
	
	private int oldStartX;
	private int oldStartY;
	private int oldStopX;
	private int oldStopY;
	
	private int newStartX;
	private int newStartY;
	private int newStopX;
	private int newStopY;
	
	private boolean chained;
	
	public MoveLineEdit(Line line, int oldStartX, int oldStartY, 
							       int newStartX, int newStartY, 
							       int oldStopX, int oldStopY, 
							       int newStopX, int newStopY,
							       boolean chained) {
		this.line = line;
		this.oldStartX = oldStartX;
		this.oldStartY = oldStartY;
		this.oldStopX = oldStopX;
		this.oldStopY = oldStopY;
		this.newStartX = newStartX;
		this.newStartY = newStartY;
		this.newStopX = newStopX;
		this.newStopY = newStopY;
		this.chained = chained;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		if (!chained)
			super.undo();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		line.setStart(new Point(oldStartX,oldStartY));
		line.setEnd(new Point(oldStopX,oldStopY));
		editor.refresh();
		editor.setFileState(FileState.DIRTY);
	}

	@Override
	public void redo() throws CannotRedoException {
		if (!chained)
			super.redo();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		line.setStart(new Point(newStartX,newStartY));
		line.setEnd(new Point(newStopX, newStopY));
		editor.refresh();
		editor.setFileState(FileState.DIRTY);
	}
	
	@Override
	public String getPresentationName() {
		return "Move line";
	}
	
	
	
}
