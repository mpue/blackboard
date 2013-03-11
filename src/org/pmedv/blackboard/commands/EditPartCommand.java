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

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.core.commands.AbstractCommand;

public class EditPartCommand extends AbstractCommand {
	
	private static final long serialVersionUID = -1204971682381964487L;

	public EditPartCommand() {
		putValue(Action.NAME, "Edit part");
		putValue(Action.SHORT_DESCRIPTION, "Edits the parts captions.");
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (editor.getSelectedItems().size() > 1) {
			return;
		}
		else {
			if (editor.getSelectedItem() != null && editor.getSelectedItem() instanceof Part) {

				Part p = (Part)editor.getSelectedItem();
				
				if (!editor.isEditPart()) {
					p.setEditing(true);
					editor.setEditPart(true);
					putValue(Action.NAME, "Finish editing");					
				}
				else {
					p.setEditing(false);
					editor.setEditPart(false);
					putValue(Action.NAME, "Edit part");										
				}
				editor.updateEditCommands();
				editor.notifyListeners(EventType.EDITOR_CHANGED);
				editor.repaint();
			}
				
		}
		editor.setFileState(FileState.DIRTY);
	}
}
