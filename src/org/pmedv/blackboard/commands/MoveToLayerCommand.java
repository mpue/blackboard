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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.preferences.Preferences;
import org.springframework.context.ApplicationContext;


/**
 * The <code>MoveToLayerCommand</code> moves the selected item(s) to a specific layer.
 * 
 * @author Matthias Pueski (17.07.2011)
 *
 */
public class MoveToLayerCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -652122685358514590L;
	
	private static final Log log = LogFactory.getLog(MoveToLayerCommand.class);
	
	public MoveToLayerCommand() {
		putValue(Action.NAME, resources.getResourceByKey("MoveToLayerCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.movetolayer"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("MoveToLayerCommand.description"));	
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();		
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		final String selectMsg = resources.getResourceByKey("MoveToLayerCommand.selectlayer");
		
		if (editor == null)
			return;
		
		Object[] layers = editor.getModel().getLayers().toArray();
				
		if (editor.getSelectedItems().size() > 1) {						
		
			Layer selectedLayer = (Layer) JOptionPane.showInputDialog(ctx.getBean(ApplicationWindow.class), selectMsg,
					selectMsg, JOptionPane.PLAIN_MESSAGE, null, layers,
					editor.getModel().getLayer(editor.getSelectedItems().get(0).getLayer()));
			
			editor.clearSelectionBorder();
			for (Item item : editor.getSelectedItems()) {
				moveItem(editor.getModel(), item, selectedLayer);
			}
		}
		else {
			if (editor.getSelectedItem() == null)
				return;
			editor.clearSelectionBorder();
			
			Layer selectedLayer = (Layer) JOptionPane.showInputDialog(ctx.getBean(ApplicationWindow.class), selectMsg,
					selectMsg, JOptionPane.PLAIN_MESSAGE, null, layers,
					editor.getModel().getLayer(editor.getSelectedItem().getLayer()));

			moveItem(editor.getModel(), editor.getSelectedItem(), selectedLayer);
			editor.setSelectedItem(null);
		}
		
		editor.setFileState(FileState.DIRTY);
		editor.updateStatusBar();
		
	}

	private void moveItem(BoardEditorModel model,Item item, Layer target) {							
		Layer source = model.getLayer(item.getLayer());		
		log.debug("moving item from layer "+source.getName()+" to "+target.getName());						
		target.getItems().add(item);
		source.getItems().remove(item);
		
		Boolean useLayerColor = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor");
		
		if (useLayerColor) {
			item.setColor(target.getColor());					
		}
		
		item.setLayer(target.getIndex());
	}


	
}
