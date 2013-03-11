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

import javax.swing.Action;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.models.LayerTableModel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

/**
 * <p>
 * Tihis command clones a {@link Layer} with all its items 
 * and adds it to the layer list
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
public class DuplicateLayerCommand extends AbstractCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	
	public DuplicateLayerCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DuplicateLayerCommand.name"));		
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("DuplicateLayerCommand.description"));		
		putValue(Action.SMALL_ICON,resources.getIcon("icon.duplicate"));
	}
	
	@Override
	public void execute(ActionEvent e) {

		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		if (editor != null) {
			LayerTableModel model = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel().getLayerModel();
			AlternatingLineTable layerTable = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel().getLayerTable();

			// determine which layer has been selected
			int index = layerTable.convertRowIndexToModel(layerTable.getSelectedRow());			
			Layer oldlayer = model.getLayer().get(index);		
			
			// we need a new index
			int layerIndex = 0;

			for (Layer l : editor.getModel().getLayers()) {
				if (l.getIndex() > layerIndex)
					layerIndex = l.getIndex();
			}

			layerIndex++;
			
			// create a new layer
			Layer newLayer = new Layer(layerIndex,oldlayer.getName()+"1");

			// copy properties
			
			newLayer.setColor(oldlayer.getColor());
			newLayer.setLocked(oldlayer.isLocked());
			newLayer.setVisible(oldlayer.isVisible());
			newLayer.setOpacity(oldlayer.getOpacity());
			
			// copy existing data to layer
			
			for (Item item : oldlayer.getItems()) {
				
				try {
					newLayer.getItems().add((Item) item.clone());
				}
				catch (CloneNotSupportedException e1) {
					ErrorUtils.showErrorDialog(e1);
				}
				
			}
			
			// add new layer to the model
			model.addLayer(newLayer);			
			model.sortLayers();
			model.fireTableDataChanged();

			// notify editor about new layer and refresh
			editor.updateStatusBar();
			editor.notifyListeners(EventType.EDITOR_CHANGED);
			editor.setFileState(FileState.DIRTY);
			
			
		}
		
		
	}
	
	
}
