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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import org.pmedv.blackboard.commands.AddSimulatorCommand;
import org.pmedv.blackboard.commands.DeleteSimulatorCommand;
import org.pmedv.blackboard.commands.EditSimulatorCommand;
import org.pmedv.blackboard.commands.SetSimulatorDefaultCommand;
import org.pmedv.blackboard.models.SpiceSimulatorTableModel;
import org.pmedv.blackboard.panels.SpiceSimulatorPanel;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.blackboard.spice.SpiceSimulator.SimulatorType;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;

/**
 * This is the main simulator browser dialog, it displays a filterable list
 * of available {@link SpiceSimulator} objects.
 * 
 * 
 * @author Matthias Pueski (06.11.2012)
 *
 */
public class SpiceSimulatorManageDialog extends AbstractNiceDialog {

	private static final ResourceService resources = ctx.getBean(ResourceService.class);	

	private SpiceSimulatorPanel simulatorPanel;
	private SpiceSimulatorTableModel model;
	private static String title = resources.getResourceByKey("ManageSimulatorsCommand.name");
	private static String subTitle = resources.getResourceByKey("ManageSimulatorsCommand.description");
	private static ImageIcon icon = resources.getIcon("icon.dialog.datasheet");

	private JPopupMenu tablePopupMenu;
	
	private SimulatorProvider simulatorProvider;
	
	public SpiceSimulatorManageDialog() {
		super(title, subTitle, icon, true, true, true, true, AppContext.getContext().getBean(ApplicationWindow.class),null);
	}

	@Override
	protected void initializeComponents() {

		simulatorProvider = AppContext.getContext().getBean(SimulatorProvider.class);
		
		simulatorPanel = new SpiceSimulatorPanel();				
		setSize(new Dimension(900, 650));		
		getContentPanel().add(simulatorPanel);
		
		simulatorProvider.loadElements();
		model = new SpiceSimulatorTableModel(simulatorProvider.getElements());		
		simulatorPanel.getSimulatorTable().setModel(model);									
		
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getCancelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
			}
		});
		
		getNewButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppContext.getContext().getBean(AddSimulatorCommand.class).execute(null);				
			}
		});
		
		simulatorPanel.getSimulatorTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseEvent(e);				
			}
			
			public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
			};
		});
	
		tablePopupMenu = new JPopupMenu();
		
		tablePopupMenu.add(AppContext.getContext().getBean(EditSimulatorCommand.class));
		tablePopupMenu.add(AppContext.getContext().getBean(SetSimulatorDefaultCommand.class));
		tablePopupMenu.add(AppContext.getContext().getBean(DeleteSimulatorCommand.class));
	}

	private void handleMouseEvent(MouseEvent e) {

		if (model.getSimulators().size() >= 1) {			
			
			Point p = e.getPoint();			 
			// get the row index that contains that coordinate
			int rowNumber = simulatorPanel.getSimulatorTable().rowAtPoint( p ); 
			// Get the ListSelectionModel of the JTable
			ListSelectionModel selectionModel = simulatorPanel.getSimulatorTable().getSelectionModel();
			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			selectionModel.setSelectionInterval( rowNumber, rowNumber );

			int modelIndex = simulatorPanel.getSimulatorTable().convertRowIndexToModel(simulatorPanel.getSimulatorTable().getSelectedRow());					
			SpiceSimulator sim = model.getSimulators().get(modelIndex);

			AppContext.getContext().getBean(EditSimulatorCommand.class).setEnabled(!sim.getType().equals(SimulatorType.INTERNAL));
			AppContext.getContext().getBean(DeleteSimulatorCommand.class).setEnabled(!sim.getType().equals(SimulatorType.INTERNAL));				
			
			AppContext.getContext().getBean(EditSimulatorCommand.class).setSimulator(sim);
			AppContext.getContext().getBean(DeleteSimulatorCommand.class).setSimulator(sim);
			AppContext.getContext().getBean(SetSimulatorDefaultCommand.class).setSimulator(sim);
			
			
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){								
				if (!sim.getType().equals(SimulatorType.INTERNAL)) {
					AppContext.getContext().getBean(EditSimulatorCommand.class).execute(null);					
				}
			}					

			if (e.isPopupTrigger()) {
				tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());												
			}
		}
	}	
	
	/**
	 * @return the model
	 */
	public SpiceSimulatorTableModel getModel() {
		return model;
	}

}
