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
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.model.AbstractBaseTableModel;
import org.pmedv.core.services.ResourceService;

public class SymbolTableModel extends AbstractBaseTableModel {

	private static final long serialVersionUID = 5619267194849565835L;

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private static final Log log = LogFactory.getLog(SymbolTableModel.class);

	private List<SymbolBean> symbols;

	private final String[]		columnNames	= { 
			resources.getResourceByKey("SymbolTableModel.symbol"), 
	};

	public SymbolTableModel() {

		symbols = new ArrayList<SymbolBean>();
		columns = 1;
		data = new Object[0][columns];
		rows = 0;

	}

	public SymbolTableModel(List<SymbolBean> symbols) {
		setSymbolBeans(symbols);
	}

	public void setSymbolBeans(List<SymbolBean> symbols) {

		log.info("Loading " + symbols.size() + " sheets.");

		this.symbols = symbols;

		columns = 1;
		data = new Object[symbols.size()][columns];

		rows = symbols.size();

		int index = 0;

		for (SymbolBean symbol : symbols) {

			data[index][0] = symbol;
			index++;

		}

		fireTableDataChanged();

	}

	public void addSymbol(SymbolBean symbol) {

		symbols.add(symbol);
		resizeTable(symbols.size(), columns);
		rows = symbols.size();
		data[symbols.size() - 1][0] = symbol;
		fireTableDataChanged();

	}

	public void updateRow(int row, SymbolBean s) {

		data[row][0] = s;
		fireTableCellUpdated(row, 0);
		fireTableDataChanged();

	}

	public void removeSymbol(SymbolBean symbol) {

		symbols.remove(symbol);

		data = new Object[symbols.size()][columns];
		rows = symbols.size();

		int index = 0;

		for (SymbolBean s : symbols) {
			data[index][0] = s;
			index++;
		}

		fireTableDataChanged();

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return SymbolBean.class;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * @return the sheets
	 */
	public List<SymbolBean> getSymbols() {
		return symbols;
	}
}
