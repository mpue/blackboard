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
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

/**
 * This command breaks up a {@link Symbol} into its {@link Item} objects.
 * 
 * @author Matthias Pueski (29.05.2011)
 * 
 */
public class BreakSymbolCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = 4872183651001556891L;

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);

	public BreakSymbolCommand() {
		putValue(Action.NAME, resources.getResourceByKey("BreakSymbolCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.breaksymbol"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("BreakSymbolCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		if (editor.getSelectedItem() != null) {

			if (editor.getSelectedItem() instanceof Symbol) {

				Symbol symbol = (Symbol) editor.getSelectedItem();
				editor.getModel().getCurrentLayer().getItems().addAll(symbol.getItems());				
				symbol.getItems().clear();
				Layer layer = editor.getModel().getLayer(symbol.getLayer()); 				
				layer.getItems().remove(symbol);
				editor.setSelectedItem(null);				
				
			}

		}

		editor.setFileState(FileState.DIRTY);
		editor.refresh();
		editor.updateStatusBar();

	}

}
