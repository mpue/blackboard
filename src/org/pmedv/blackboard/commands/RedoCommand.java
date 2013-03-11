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
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.springframework.context.ApplicationContext;


public class RedoCommand extends AbstractCommand implements EditorChangedListener {

	private static final long serialVersionUID = -3761492937412800675L;	
	private static final Log log = LogFactory.getLog(RedoCommand.class);
	
	public RedoCommand() {
		putValue(Action.NAME, resources.getResourceByKey("RedoCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.redo"));
		putValue(Action.SHORT_DESCRIPTION,  resources.getResourceByKey("RedoCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}	
	
	@Override
	public void execute(ActionEvent e) {		

		ApplicationContext ctx = AppContext.getContext();	
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();		
		UndoManager undoManager = editor.getUndoManager();
		
		try {				
			undoManager.redo();
		}
		catch (CannotRedoException r) {
			log.warn("Cannot redo.");
		}
		
		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());

	}

	@Override
	public void editorChanged(EditorChangedEvent event) {

		if (event.getEditor() == null) {
			setEnabled(false);			
		}
		else {
			setEnabled(event.getEditor().getUndoManager().canRedo());
		}
		
	}

}
