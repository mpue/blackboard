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
package org.pmedv.blackboard.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.model.AbstractBaseTableModel;
import org.pmedv.core.services.ResourceService;

public class LayerTableModel extends AbstractBaseTableModel {

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	private static final long	serialVersionUID	= 1545402161525947344L;
	private static final Log	log			= LogFactory.getLog(LayerTableModel.class);

	private ArrayList<Layer>		layers;
	
	private final String[]		columnNames	= { 
			resources.getResourceByKey("LayerTableModel.layer"), 
			resources.getResourceByKey("LayerTableModel.visible"),
			resources.getResourceByKey("LayerTableModel.locked"),
			resources.getResourceByKey("LayerTableModel.color")
	};
	
	public LayerTableModel() {

		layers = new ArrayList<Layer>();
		columns = 4;
		data = new Object[0][columns];
		rows = 0;
	}

	public LayerTableModel(ArrayList<Layer> layers) {
		setLayers(layers);
	}

	public void setLayers(ArrayList<Layer> layers) {

		this.layers = layers;
		
		if (layers != null) {
			log.info("layers : "+layers.size());

			columns = 4;
			data = new Object[layers.size()][columns];

			rows = layers.size();

			int index = 0;

			for (Layer l : layers) {

				data[index][0] = l.getName();
				data[index][1] = l.isVisible();
				data[index][2] = l.isLocked();
				data[index][3] = l.getColor();

				index++;

			}
			
		}

		fireTableDataChanged();

	}

	public void addLayer(Layer layer) {

		layers.add(layer);

		resizeTable(layers.size(), columns);

		rows = layers.size();

		data[layers.size() - 1][0] = layer.getName();
		data[layers.size() - 1][1] = layer.isVisible();
		data[layers.size() - 1][2] = layer.isLocked();
		data[layers.size() - 1][3] = layer.getColor();

		fireTableDataChanged();

	}

	public void updateRow(int row, Layer l) {

		data[row][0] = l.getName();
		data[row][1] = l.isVisible();
		data[row][1] = l.isLocked();
		data[row][1] = l.getColor();

		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
		fireTableCellUpdated(row, 2);
		fireTableCellUpdated(row, 3);
		
		fireTableDataChanged();

	}

	public void removeLayer(Layer layer) {

		layers.remove(layer);
		data = new Object[layers.size()][4];
		rows = layers.size();

		int index = 0;

		for (Layer l : layers) {

			data[index][0] = l.getName();
			data[index][1] = l.isVisible();
			data[index][2] = l.isLocked();
			data[index][3] = l.getColor();

			index++;

		}

		fireTableDataChanged();

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {
			case 0: return String.class;
			case 1: 
			case 2: return Boolean.class;
			case 3: return Color.class;
		}

		return super.getColumnClass(columnIndex);

	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * @return the layers
	 */
	public ArrayList<Layer> getLayer() {
	
		return layers;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	@Override
	public synchronized Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0 : return layers.get(rowIndex).getName();
			case 1 : return layers.get(rowIndex).isVisible();
			case 2 : return layers.get(rowIndex).isLocked();
			case 3 : return layers.get(rowIndex).getColor();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0 : 
				String name = (String)aValue;
				if (EditorUtils.getCurrentActiveEditor() != null)
					EditorUtils.getCurrentActiveEditor().setFileState(FileState.DIRTY);
				layers.get(rowIndex).setName(name);
				break;
			case 1 :
				Boolean visible = (Boolean)aValue;
				layers.get(rowIndex).setVisible(visible);
				if (EditorUtils.getCurrentActiveEditor() != null)
					EditorUtils.getCurrentActiveEditor().setFileState(FileState.DIRTY);				
				break;
			case 2 :
				Boolean locked = (Boolean)aValue;
				layers.get(rowIndex).setLocked(locked);
				if (EditorUtils.getCurrentActiveEditor() != null)
					EditorUtils.getCurrentActiveEditor().setFileState(FileState.DIRTY);				
				break;
				
			default : break;
		}
		if (EditorUtils.getCurrentActiveEditor() != null)
			EditorUtils.getCurrentActiveEditor().updateStatusBar();
		
	}

	public void sortLayers() {
		Collections.sort(layers);
	}
	
}
