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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceType;
import org.pmedv.core.model.AbstractBaseTableModel;

public class ModelImportTableModel extends AbstractBaseTableModel {

	private static final Log log = LogFactory.getLog(ModelImportTableModel.class);

	private List<Model> models;

	private final String[] columnNames = { "Name", "SpiceType","Import" };

	public ModelImportTableModel() {

		models = new ArrayList<Model>();
		columns = 3;
		data = new Object[0][columns];
		rows = 0;

	}

	public ModelImportTableModel(List<Model> list) {
		setModels(list);
	}

	public void setModels(List<Model> models) {

		log.info("Loading " + models.size() + " models.");

		this.models = models;

		columns = 3;
		data = new Object[models.size()][columns];

		rows = models.size();

		int index = 0;

		for (Model model : models) {
			data[index][0] = model.getName();
			data[index][1] = model.getType();
			data[index][2] = model.isShouldImport();
			index++;
		}

		fireTableDataChanged();

	}

	public void addModel(Model model) {

		models.add(model);
		resizeTable(models.size(), columns);
		rows = models.size();
		data[models.size() - 1][0] = model.getName();
		data[models.size() - 1][1] = model.getType();
		data[models.size() - 1][2] = model.isShouldImport();
		fireTableDataChanged();

	}

	public void updateRow(int row, Model model) {

		data[row][0] = model.getName();
		data[row][1] = model.getType();
		data[row][2] = model.isShouldImport();
		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
		fireTableCellUpdated(row, 2);
		fireTableDataChanged();

	}

	public void removeModel(Model model) {

		models.remove(model);
		data = new Object[models.size()][columns];
		rows = models.size();

		int index = 0;

		for (Model m : models) {

			data[index][0] = m.getName();
			data[index][1] = m.getType();
			data[index][2] = m.isShouldImport();
			
			index++;
		}

		fireTableDataChanged();

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return SpiceType.class;
		case 2:
			return Boolean.class;			
		default:
			return Object.class;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 2);
	}
	
	@Override
	public synchronized Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0 : return models.get(rowIndex).getName();
			case 1 : return models.get(rowIndex).getType();
			case 2 : return models.get(rowIndex).isShouldImport();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 2 :
				Boolean shouldImport = (Boolean)aValue;
				models.get(rowIndex).setShouldImport(shouldImport);
				break;
				
			default : break;
		}
		
	}	
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public List<Model> getModels() {
		return models;
	}
}
