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
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.panels.LayersPanel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;

public class DeleteLayerCommand extends AbstractCommand {

	private static final long serialVersionUID = -4060436618149711784L;
	
	public DeleteLayerCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DeleteLayerCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.removelayer"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddLayerCommand.description"));
	}	
	
	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
		
		String message = resources.getResourceByKey("DeleteLayerCommand.warning");
		int result = JOptionPane.showConfirmDialog(win, message, resources.getResourceByKey("msg.warning"), JOptionPane.YES_NO_OPTION);

		LayersPanel layerPanel = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel();
		
		if (result == JOptionPane.YES_OPTION) {
			int rows[] = layerPanel.getLayerTable().getSelectedRows();

			ArrayList<Layer> deletedLayers = new ArrayList<Layer>();
			
			for (int i = 0; i < rows.length; i++) {
				int index = layerPanel.getLayerTable().convertRowIndexToModel(rows[i]);
				Layer current = layerPanel.getLayerModel().getLayer().get(index);
				deletedLayers.add(current);
			}

			for (Layer current : deletedLayers) {					
				layerPanel.getLayerModel().removeLayer(current);
				editor.getModel().getLayers().remove(current);
			}
			
			editor.updateStatusBar();
			editor.notifyListeners(EventType.EDITOR_CHANGED);
			editor.setFileState(FileState.DIRTY);
			
		}		
		
	}

}
