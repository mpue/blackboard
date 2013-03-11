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
import java.io.File;

import javax.swing.Action;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.core.commands.AbstractEditorCommand;

public class SaveAsCommand extends AbstractEditorCommand {
	
	private static final long serialVersionUID = -9194554270753712780L;

	private File currentFile;
	
	public SaveAsCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SaveAsCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.saveas"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SaveAsCommand.description"));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		// preserve old file just in case the user cancels the save as
		setCurrentFile(editor.getCurrentFile());
		editor.setCurrentFile(null);		
		new SaveBoardCommand().execute(null);
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}
	
}
