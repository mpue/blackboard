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
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;


/**
 * The delete command deletes one or more selected item(s)
 * from the board.
 * 
 * @see BoardEditorModel
 * @see BoardEditor
 * 
 * @author Matthias Pueski (18.06.2011)
 *
 */
public class DeleteCommand extends AbstractCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	
	private static final Log log = LogFactory.getLog(DeleteCommand.class);
	
	public DeleteCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DeleteCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.delete"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("DeleteCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {

		ArrayList<Item> deletedItems = new ArrayList<Item>();
		
		ApplicationContext ctx = AppContext.getContext();					
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		UndoManager undoManager = editor.getUndoManager();
		
		try {

			if (editor.getSelectedItems().size() > 1) {						
				editor.clearSelectionBorder();
				for (Item item : editor.getSelectedItems()) {
					deletedItems.add(item);
					for (Layer layer : editor.getModel().getLayers())
						layer.getItems().remove(item);					
				}
				editor.getSelectedItems().clear();
				editor.refresh();
			}
			else {
				if (editor.getSelectedItem() == null)
					return;
				editor.clearSelectionBorder();
				deletedItems.add(editor.getSelectedItem());
				for (Layer layer : editor.getModel().getLayers())
					layer.getItems().remove(editor.getSelectedItem());
				editor.setSelectedItem(null);
				editor.refresh();
			}
			
			editor.setFileState(FileState.DIRTY);
			
			log.info("Deleting "+deletedItems.size()+" items.");

			DeleteEdit de = new DeleteEdit(deletedItems);
			
			if (!undoManager.addEdit(de)) {
				log.error("could not add edit to undo manager");
			}
						
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		
		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
		
	}

	
}
