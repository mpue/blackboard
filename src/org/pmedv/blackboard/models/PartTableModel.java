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
import org.pmedv.blackboard.components.Part;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.model.AbstractBaseTableModel;
import org.pmedv.core.services.ResourceService;

public class PartTableModel extends AbstractBaseTableModel {

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private static final long	serialVersionUID	= 1545402161525947344L;
	private static final Log	log			= LogFactory.getLog(PartTableModel.class);

	private ArrayList<Part>		parts;
	
	private final String[]		columnNames	= { 
			resources.getResourceByKey("PartTableModel.name"), 
			resources.getResourceByKey("PartTableModel.description"), 
			resources.getResourceByKey("PartTableModel.packagetype") 
	};

	public PartTableModel() {

		parts = new ArrayList<Part>();
		columns = 3;
		data = new Object[0][columns];
		rows = 0;

	}

	public PartTableModel(ArrayList<Part> parts) {

		setParts(parts);
	}

	public void setParts(ArrayList<Part> parts) {

		log.info("Loading " + parts.size() + " parts.");

		this.parts = parts;

		columns = 3;
		data = new Object[parts.size()][columns];

		rows = parts.size();

		int index = 0;

		for (Part p : parts) {

			data[index][0] = p.getName();

			data[index][1] = p.getDescription();
			data[index][2] = p.getPackageType();

			index++;

		}

		fireTableDataChanged();

	}

	public void addPart(Part part) {

		parts.add(part);

		resizeTable(parts.size(), columns);

		rows = parts.size();

		data[parts.size() - 1][0] = part.getName();
		data[parts.size() - 1][1] = part.getDescription();
		data[parts.size() - 1][2] = part.getPackageType();

		fireTableDataChanged();

	}

	public void updateRow(int row, Part p) {

		data[row][0] = p.getName();
		data[row][1] = p.getDescription();
		data[row][2] = p.getPackageType();

		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
		fireTableCellUpdated(row, 2);

		fireTableDataChanged();

	}

	public void removePart(Part part) {

		parts.remove(part);
		data = new Object[parts.size()][3];
		rows = parts.size();

		int index = 0;

		for (Part p : parts) {

			data[index][0] = p.getName();
			data[index][1] = p.getDescription();
			data[index][2] = p.getPackageType();

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
	 * @return the parts
	 */
	public ArrayList<Part> getParts() {
	
		return parts;
	}
}
