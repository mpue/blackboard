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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.springframework.context.ApplicationContext;


public class UndoCommand extends AbstractCommand implements EditorChangedListener {

	private static final long serialVersionUID = -811608125040391612L;

	private static final Log log = LogFactory.getLog(UndoCommand.class);
	
	public UndoCommand() {
		putValue(Action.NAME, resources.getResourceByKey("UndoCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.undo"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("UndoCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}	
	
	@Override
	public void execute(ActionEvent e) {		
		ApplicationContext ctx = AppContext.getContext();		
		ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();		
		UndoManager undoManager = editor.getUndoManager();				
		
		try {
			
			try {
				undoManager.undo();	
			}
			catch (CannotUndoException c) {
				JOptionPane.showMessageDialog(win, resources.getResourceByKey("msg.cantundo"), 
												   resources.getResourceByKey("msg.error"), 
												   JOptionPane.ERROR_MESSAGE);		
			}
						
		}
		catch (CannotUndoException e1) {
			log.warn("Cannot undo.");
			e1.printStackTrace();
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
			setEnabled(event.getEditor().getUndoManager().canUndo());
		}
		
	}

}
