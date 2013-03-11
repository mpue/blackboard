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
	MERCHANTABILITY or FITNESS FOR A SymbolICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */
package org.pmedv.blackboard.filter;

import java.util.regex.Pattern;

import javax.swing.RowFilter;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXTable;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.models.SymbolTableModel;

public class SymbolFilter extends AbstractBean {

	private RowFilter<Object, Object> searchFilter;
	private String filterString;
	private JXTable SymbolTable;

	public SymbolFilter(JXTable emailTable) {
		this.SymbolTable = emailTable;
	}

	public String getFilterString() {
		return filterString;
	}

	/**
	 * Sets the filter string
	 * 
	 * @param filterString the filterString to set
	 */
	public void setFilterString(String filterString) {
		String oldValue = getFilterString();
		this.filterString = filterString;
		updateSearchFilter();
		firePropertyChange("filterString", oldValue, getFilterString());
	}

	private void updateSearchFilter() {
		if (filterString != null) {
			searchFilter = createSearchFilter(".*" + filterString + ".*");
		}
		updateFilters();
	}

	private void updateFilters() {
		// set the filters to the table
		if (searchFilter != null) {
			SymbolTable.setRowFilter(searchFilter);
		}
	}

	private RowFilter<Object, Object> createSearchFilter(final String filterString) {
		return new RowFilter<Object, Object>() {

			@Override
			public boolean include(Entry<? extends Object, ? extends Object> entry) {

				boolean matches = false;

				SymbolTableModel model = (SymbolTableModel) entry.getModel();

				int modelIndex = ((Integer) entry.getIdentifier()).intValue();

				SymbolBean symbol = model.getSymbols().get(modelIndex);

				String name = symbol.getName();				
				if (name == null)
					name = "";
				String category = symbol.getCategory();
				if (category == null)
					category = "";

				Pattern p = Pattern.compile(filterString, Pattern.CASE_INSENSITIVE);
				matches = (p.matcher(name).matches() || p.matcher(category).matches());

				return matches;
			}
		};
	}

}
