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
package org.pmedv.blackboard.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.beans.DatasheetList;
import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.blackboard.provider.DataSheetProvider;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.components.FilterPanel;

/**
 * The <code>DatasheetPanel</code> is the main panel for datasheet
 * management. It allows the user to view, add and remove datasheets.
 * 
 * @see DatasheetBean
 * @see DatasheetDialog
 * @see DatasheetList
 * @see DataSheetProvider
 * 
 * @author Matthias Pueski (24.09.2011)
 *
 */
public class DatasheetPanel extends JPanel {

	private static final long serialVersionUID = 7537741922732745170L;

	// the panel for filtering available sheets
	private FilterPanel filterPanel;
	// the scroll pane containing the sheets table	
	private JScrollPane scrollPane;
	// table containing datasheets
	private AlternatingLineTable datasheetTable;
	
	private JButton addSheetButton;
	private JButton removeSheetButton;
	private JButton importFolderButton;
	
	public DatasheetPanel() {		
		super(new BorderLayout());
		initializeComponents();		
	}

	private void initializeComponents() {

		filterPanel = new FilterPanel();
		add(filterPanel, BorderLayout.NORTH);		
		datasheetTable = new AlternatingLineTable();
		scrollPane = new JScrollPane(datasheetTable);
		add(scrollPane, BorderLayout.CENTER);
		
		addSheetButton = new JButton();
		removeSheetButton = new JButton();
		importFolderButton = new JButton();
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(addSheetButton);
		buttonPanel.add(removeSheetButton);
		buttonPanel.add(importFolderButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}

	public FilterPanel getFilterPanel() {
		return filterPanel;
	}

	public AlternatingLineTable getDatasheetTable() {
		return datasheetTable;
	}

	public JButton getAddSheetButton() {
		return addSheetButton;
	}

	public JButton getRemoveSheetButton() {
		return removeSheetButton;
	}
	
	public JButton getImportFolderButton() {
		return importFolderButton;
	}
}
