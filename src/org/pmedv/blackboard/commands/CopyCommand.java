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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;


public class CopyCommand extends AbstractCommand {

	private static final long serialVersionUID = -652122685358514590L;
	
	public CopyCommand() {
		putValue(Action.NAME, resources.getResourceByKey("CopyCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.copy"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("CopyCommand.description"));	
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (editor.getSelectedItem() != null) {
			AppContext.getClipboard().copy(editor.getSelectedItem());
		}
		if (editor.getSelectedItems() != null && editor.getSelectedItems().size() > 0)
			AppContext.getClipboard().copyAll(editor.getSelectedItems());
	
		editor.updateEditCommands();
	}

}
