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

import org.pmedv.blackboard.app.UnsavedResource;
import org.pmedv.core.model.AbstractBaseTableModel;

public class UnsavedResourcesTableModel extends AbstractBaseTableModel {

	private ArrayList<UnsavedResource> unsavedResources;

	private final String[] columnNames	= {	"","" };

	public UnsavedResourcesTableModel() {

		unsavedResources = new ArrayList<UnsavedResource>();
		columns = 2;
		data = new Object[0][columns];
		rows = 0;

	}

	public UnsavedResourcesTableModel(ArrayList<UnsavedResource> unsaved) {
		setResources(unsaved);
	}

	public void setResources(ArrayList<UnsavedResource> unsaved) {

		this.unsavedResources = unsaved;

		columns = 2;
		data = new Object[unsaved.size()][columns];

		rows = unsaved.size();

		int index = 0;

		for (UnsavedResource resource : unsaved) {

			data[index][0] = resource;
			index++;

		}

		fireTableDataChanged();

	}

	public void addresource(UnsavedResource resource) {

		unsavedResources.add(resource);
		resizeTable(unsavedResources.size(), columns);
		rows = unsavedResources.size();
		data[unsavedResources.size() - 1][0] = resource;
		fireTableDataChanged();

	}

	public void updateRow(int row, UnsavedResource r) {

		data[row][0] = r;
		fireTableCellUpdated(row, 0);
		fireTableDataChanged();

	}

	public void removeResource(UnsavedResource r) {

		unsavedResources.remove(r);

		data = new Object[unsavedResources.size()][columns];
		rows = unsavedResources.size();

		int index = 0;

		for (UnsavedResource u : unsavedResources) {
			data[index][0] = u;
			index++;
		}

		fireTableDataChanged();

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {		
		switch (columnIndex) {
			case 0 : return Boolean.class;
			case 1 : return String.class;
			default : return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}
	
	@Override
	public synchronized Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0 : return unsavedResources.get(rowIndex).getSave();
			case 1 : return unsavedResources.get(rowIndex).getName();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0 :
				Boolean save = (Boolean)aValue;
				unsavedResources.get(rowIndex).setSave(save);
				break;
				
			default : break;
		}
		
	}	
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * @return the resources
	 */
	public ArrayList<UnsavedResource> getResources() {
		return unsavedResources;
	}
}
