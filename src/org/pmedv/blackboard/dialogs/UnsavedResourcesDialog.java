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
package org.pmedv.blackboard.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.pmedv.blackboard.app.UnsavedResource;
import org.pmedv.blackboard.models.UnsavedResourcesTableModel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class UnsavedResourcesDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = -6381666261079899232L;

	private ArrayList<UnsavedResource> resourcesToSave = new ArrayList<UnsavedResource>();
	
	private JTable resourcesTable;
	private UnsavedResourcesTableModel model;
	
	public UnsavedResourcesDialog(String title, String subTitle, ImageIcon icon, ArrayList<UnsavedResource> resources) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), resources);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeComponents() {

		setSize(new Dimension(600, 400));
		
		final JPanel panel = new JPanel(new BorderLayout());
		
		resourcesToSave = (ArrayList<UnsavedResource>)getUserObject();		
		model = new UnsavedResourcesTableModel(resourcesToSave);
		
		resourcesTable = new JTable(model);
		resourcesTable.setTableHeader(null);
		resourcesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resourcesTable.setFillsViewportHeight(true);
		
		JScrollPane scrollPane = new JScrollPane(resourcesTable);		
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		resourcesTable.getColumnModel().getColumn(0).setPreferredWidth(33);
		resourcesTable.getColumnModel().getColumn(1).setPreferredWidth(537);
		resourcesTable.setRowSelectionAllowed(false);
		resourcesTable.setColumnSelectionAllowed(false);
		resourcesTable.setCellSelectionEnabled(false);
		resourcesTable.setFocusable(false);
		resourcesTable.setRowHeight(30);
		resourcesTable.setShowGrid(false);
		resourcesTable.setIntercellSpacing(new Dimension(0, 0));
		
		getContentPanel().add(panel);
		setResizable(false);
		
		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				resourcesToSave.clear();
				
				for (UnsavedResource r : model.getResources()) {
					if (r.getSave()) {
						resourcesToSave.add(r);
					}
				}

				result = OPTION_OK;

				setVisible(false);
				dispose();
			}

		});

		getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				result = OPTION_CANCEL;
				setVisible(false);
				dispose();
			}

		});

	}

	/**
	 * @return the resourcesToSave
	 */
	public ArrayList<UnsavedResource> getResourcesToSave() {
		return resourcesToSave;
	}

	

}
