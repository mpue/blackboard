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
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.LayerTableModel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;

public class MoveLayerDownCommand extends AbstractCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	
	public MoveLayerDownCommand() {
		putValue(Action.NAME, resources.getResourceByKey("MoveLayerDownCommand.name"));		
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("MoveLayerDownCommand.description"));		
		putValue(Action.SMALL_ICON,resources.getIcon("icon.down"));
	}
	
	@Override
	public void execute(ActionEvent e) {

		LayerTableModel model = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel().getLayerModel();
		AlternatingLineTable layerTable = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel().getLayerTable();
		
		int index = layerTable.convertRowIndexToModel(layerTable.getSelectedRow());
		
		if (index < model.getLayer().size() - 1) {
			Layer layer = model.getLayer().get(index);		
			Layer nextLayer = model.getLayer().get(index + 1);			
			int oldIndex = nextLayer.getIndex();
			
			for (Item item : layer.getItems()) {				
				item.setLayer(oldIndex);				
			}
			for (Item item : nextLayer.getItems()) {				
				item.setLayer(layer.getIndex());				
			}
			
			nextLayer.setIndex(layer.getIndex());
			layer.setIndex(oldIndex);			
		}
		
		model.sortLayers();
		model.fireTableDataChanged();
	
		EditorUtils.getCurrentActiveEditor().repaint();
		EditorUtils.getCurrentActiveEditor().setFileState(FileState.DIRTY);
	}
	
	
}
