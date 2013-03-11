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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.model.AbstractBaseTableModel;
import org.pmedv.core.services.ResourceService;

public class DataSheetTableModel extends AbstractBaseTableModel {

	private static final long serialVersionUID = 5619267194849565835L;

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private static final Log log = LogFactory.getLog(DataSheetTableModel.class);

	private ArrayList<DatasheetBean> sheets;

	private final String[]		columnNames	= { 
			resources.getResourceByKey("DataSheetTableModel.name"), 
			resources.getResourceByKey("DataSheetTableModel.description"), 
			resources.getResourceByKey("DataSheetTableModel.location") 
	};

	public DataSheetTableModel() {

		sheets = new ArrayList<DatasheetBean>();
		columns = 3;
		data = new Object[0][columns];
		rows = 0;

	}

	public DataSheetTableModel(ArrayList<DatasheetBean> sheets) {

		setDatasheetBeans(sheets);
	}

	public void setDatasheetBeans(ArrayList<DatasheetBean> sheets) {

		log.info("Loading " + sheets.size() + " sheets.");

		this.sheets = sheets;

		columns = 3;
		data = new Object[sheets.size()][columns];

		rows = sheets.size();

		int index = 0;

		for (DatasheetBean sheet : sheets) {

			data[index][0] = sheet.getName();
			data[index][1] = sheet.getDescription();
			data[index][2] = sheet.getLocation();

			index++;

		}

		fireTableDataChanged();

	}

	public void addDatasheetBean(DatasheetBean sheet) {

		sheets.add(sheet);

		resizeTable(sheets.size(), columns);

		rows = sheets.size();

		data[sheets.size() - 1][0] = sheet.getName();
		data[sheets.size() - 1][1] = sheet.getDescription();
		data[sheets.size() - 1][2] = sheet.getLocation();

		fireTableDataChanged();

	}

	public void updateRow(int row, DatasheetBean p) {

		data[row][0] = p.getName();
		data[row][1] = p.getDescription();
		data[row][2] = p.getLocation();

		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
		fireTableCellUpdated(row, 2);

		fireTableDataChanged();

	}

	public void removeDatasheetBean(DatasheetBean sheet) {

		sheets.remove(sheet);

		data = new Object[sheets.size()][3];
		rows = sheets.size();

		int index = 0;

		for (DatasheetBean p : sheets) {

			data[index][0] = p.getName();
			data[index][1] = p.getDescription();
			data[index][2] = p.getLocation();

			index++;
		}

		fireTableDataChanged();

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {

		case 1:
		case 2:
		case 3:
			return String.class;

		}

		return super.getColumnClass(columnIndex);

	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * @return the sheets
	 */
	public ArrayList<DatasheetBean> getDatasheetBeans() {

		return sheets;
	}
}
