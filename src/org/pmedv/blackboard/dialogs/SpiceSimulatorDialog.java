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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import org.pmedv.blackboard.models.SpiceSimulatorTableModel;
import org.pmedv.blackboard.panels.SpiceSimulatorPanel;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
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
public class SpiceSimulatorDialog extends AbstractNiceDialog {

	private static final ResourceService resources = ctx.getBean(ResourceService.class);	

	private SpiceSimulatorPanel simulatorPanel;
	private SpiceSimulatorTableModel model;
	private static String title = resources.getResourceByKey("SpiceSimulatorDialog.title");
	private static String subTitle = resources.getResourceByKey("SpiceSimulatorDialog.subtitle");
	private static ImageIcon icon = resources.getIcon("icon.dialog.datasheet");
	
	private SimulatorProvider simulatorProvider;
	
	public SpiceSimulatorDialog() {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class),null);
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
					
	}

}
