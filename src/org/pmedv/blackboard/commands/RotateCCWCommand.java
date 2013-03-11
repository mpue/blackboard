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
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Rotateable;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * This command rotates an {@link Item} counter-clockwise. 
 * </p>
 * <p>
 * If the item is a {@link Symbol}and it is broken up, the applied transform will be lost.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
public class RotateCCWCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	private static final Log log = LogFactory.getLog(RotateCCWCommand.class);
	
	public RotateCCWCommand() {
		putValue(Action.NAME, resources.getResourceByKey("RotateCCWCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.rotateccw"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("RotateCCWCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, 0));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();				
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		UndoManager undoManager = editor.getUndoManager();
		
		if (editor.getSelectedItem() != null) {
			if (editor.getSelectedItem() instanceof Rotateable) {
				Rotateable part = (Rotateable) editor.getSelectedItem();
				part.rotateCCW();				
				if (!undoManager.addEdit(new RotateCCWEdit(part))) {
					log.info("Could not add edit : "+this.getClass());
				}								
				editor.refresh();
			}
		}
		
		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
		
	}

	
}
