/**

	BlackBoard BreadBoard Designer
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
package org.pmedv.core.model;

import javax.swing.table.AbstractTableModel;

import org.pmedv.core.util.ArrayUtils;



public class AbstractBaseTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;

	protected Object[][] data;
	
	protected volatile int columns;
	protected volatile int rows;
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public synchronized int getColumnCount() {
		return columns;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public synchronized int getRowCount() {
		return rows;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public synchronized Object getValueAt(final int rowIndex, final int columnIndex) {		 		
		return data[rowIndex][columnIndex];		
	}	
	
	/**
	 * Resizes the data table according to the new given size, new
	 * cells will be filled with an empty object for beautifying the table
	 * 
	 * @param rows the new row size of the data array
	 * @param cols the new column size of the data array
	 * 
	 */
	protected synchronized void resizeTable(final int rows, final int cols) {

		data = (Object[][]) ArrayUtils.resizeArray(data, rows);

		for (int i = 0; i < data.length; i++) {
			if (data[i] == null)
				data[i] = new Object[cols];
			else
				data[i] = (Object[]) ArrayUtils.resizeArray(data[i], cols);
		}
		
		for (int row = 0;row < data.length; row++) {
			for (int column = 0; column < data[row].length;column++) {
				if (data[row][column] == null) {
					data[row][column] = new Object();
				}
			}
		}	
		

	}
	

}
