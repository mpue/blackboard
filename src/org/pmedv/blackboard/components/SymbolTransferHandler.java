/**

	Weberknecht cmsmanager - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
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
package org.pmedv.blackboard.components;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.models.SymbolTableModel;

public class SymbolTransferHandler extends TransferHandler implements Transferable {

	private static final long serialVersionUID = 2269615379995670093L;

	private static final DataFlavor flavors[] = { DataFlavor.imageFlavor };

	private SymbolBean symbol;
	private JTable sourceTable;

	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	public boolean canImport(JComponent comp, DataFlavor flavor[]) {
		if (!(comp instanceof JTable)) {
			return false;
		}
		else {
			JTable table = (JTable) comp;
			
			/**
			 * Prevent from copy drag into the same table
			 */
			
			if (table == sourceTable) 
				return false;
			
		}
		for (int i = 0, n = flavor.length; i < n; i++) {
			for (int j = 0, m = flavors.length; j < m; j++) {
				if (flavor[i].equals(flavors[j])) {
					return true;
				}
			}
		}
		return false;
	}

	public Transferable createTransferable(JComponent comp) {

		symbol = null;
		
		if (comp instanceof JTable) {
			
			JTable table = (JTable) comp;

			/**
			 * Save source in order to prevent dragging into the source itself.
			 */
			
			this.sourceTable = table;			
			
			int index = table.convertRowIndexToModel(table.getSelectedRow());
			SymbolTableModel model = (SymbolTableModel)table.getModel();
			
			SymbolBean t = model.getSymbols().get(index);				
			symbol = t;				
			
			return this;

		} 
		return null;
	}

	public boolean importData(JComponent comp, Transferable t) {
		
		if (comp instanceof JTable) {
			
			if (t.isDataFlavorSupported(flavors[0])) {
				try {
					symbol = (SymbolBean) t.getTransferData(flavors[0]);
					
					return true;
				} 
				catch (UnsupportedFlavorException ignored) {
				} 
				catch (IOException ignored) {
				}
			}
		} 
		
		return false;
	}

	// Transferable
	public Object getTransferData(DataFlavor flavor) {
		if (isDataFlavorSupported(flavor)) {
			return symbol;
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavors[0].equals(flavor);
	}
}
