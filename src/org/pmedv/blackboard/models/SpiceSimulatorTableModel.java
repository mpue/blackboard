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
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.model.AbstractBaseTableModel;
import org.pmedv.core.services.ResourceService;

public class SpiceSimulatorTableModel extends AbstractBaseTableModel {

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private static final Log log = LogFactory.getLog(SpiceSimulatorTableModel.class);

	private List<SpiceSimulator> simulators;

	private final String[]		columnNames	= { 
			resources.getResourceByKey("SpiceSimulatorTableModel.name"), 
			resources.getResourceByKey("SpiceSimulatorTableModel.path"),
			resources.getResourceByKey("SpiceSimulatorTableModel.default"),
			
	};

	public SpiceSimulatorTableModel() {

		simulators = new ArrayList<SpiceSimulator>();
		columns = 3;
		data = new Object[0][columns];
		rows = 0;

	}

	public SpiceSimulatorTableModel(List<SpiceSimulator> simulators) {
		setSimulators(simulators);
	}

	public void setSimulators(List<SpiceSimulator> simulators) {

		log.info("Loading " + simulators.size() + " simulators.");

		this.simulators = simulators;

		columns = 3;
		data = new Object[simulators.size()][columns];

		rows = simulators.size();

		int index = 0;

		for (SpiceSimulator sim : simulators) {

			data[index][0] = sim.getName();
			data[index][1] = sim.getPath();
			data[index][2] = sim.isDefaultSimulator();

			index++;

		}

		fireTableDataChanged();

	}

	public void addSimulator(SpiceSimulator sim) {

		simulators.add(sim);

		resizeTable(simulators.size(), columns);

		rows = simulators.size();

		data[simulators.size() - 1][0] = sim.getName();
		data[simulators.size() - 1][1] = sim.getPath();
		data[simulators.size() - 1][2] = sim.isDefaultSimulator();

		fireTableDataChanged();

	}

	public void updateRow(int row, SpiceSimulator s) {

		data[row][0] = s.getName();
		data[row][1] = s.getPath();
		data[row][2] = s.isDefaultSimulator();

		fireTableCellUpdated(row, 0);
		fireTableCellUpdated(row, 1);
		fireTableCellUpdated(row, 2);

		fireTableDataChanged();

	}

	public void removeSimulator(SpiceSimulator sim) {

		simulators.remove(sim);

		data = new Object[simulators.size()][3];
		rows = simulators.size();

		int index = 0;

		for (SpiceSimulator p : simulators) {

			data[index][0] = p.getName();
			data[index][1] = p.getPath();
			data[index][2] = p.isDefaultSimulator();

			index++;
		}

		fireTableDataChanged();

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {

		case 0:
		case 1:		
			return String.class;
		case 2:
			return Boolean.class;

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
	public List<SpiceSimulator> getSimulators() {
		return simulators;
	}
}
