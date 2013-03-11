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

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.ShapePropertiesPanel;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;


/**
 * Tis command selects all visible items
 * 
 * @see BoardEditorModel
 * @see BoardEditor
 * 
 * @author Matthias Pueski (18.06.2011)
 *
 */
public class SelectAllCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	
	public SelectAllCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SelectAllCommand.name"));		
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SelectAllCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		editor.setSelectedItem(null);
		editor.getSelectedItems().clear();
		
		for (Layer layer : editor.getModel().getLayers()) {
			if (layer.isVisible() && !layer.isLocked()) {				
				for (Item item : layer.getItems()) {
					editor.getSelectedItems().add(item);
				}				
			}
		}

		AppContext.getContext().getBean(ShapePropertiesPanel.class).getObjectField()
			.setText(editor.getSelectedItems().size() + " " + resources.getResourceByKey("ShapePropertiesPanel.items"));
		
		// switch to select mode for convenience
		AppContext.getContext().getBean(SetSelectModeCommand.class).execute(null);		
		editor.updateEditCommands();
		
		BoardUtil.syncItemState(editor.getSelectedItems());
		
		editor.refresh();
		
	}
	
}
