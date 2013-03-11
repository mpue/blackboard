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
package org.pmedv.blackboard.spice.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.blackboard.spice.panels.SpiceSimulatorPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class SpiceSimulatorDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = 1429247746599672970L;
	private SpiceSimulatorPanel simulatorPanel;

	private SpiceSimulator simulator;

	public SpiceSimulatorDialog(String title, String subTitle, ImageIcon icon, SpiceSimulator simulator) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), simulator);
	}

	@Override
	protected void initializeComponents() {

		setSize(new Dimension(360, 300));
		
		simulatorPanel = new SpiceSimulatorPanel();
		
		getContentPanel().add(simulatorPanel);

		if (getUserObject() != null && getUserObject() instanceof SpiceSimulator) {
			this.simulator = (SpiceSimulator) getUserObject();
			simulatorPanel.getNameTextField().setText(simulator.getName());
			simulatorPanel.getNameTextField().setEditable(false);
			simulatorPanel.getPathTextField().setText(simulator.getPath());
			simulatorPanel.getParamTextField().setText(simulator.getParameters());
			simulatorPanel.getDefaultCheckBox().setSelected(simulator.isDefaultSimulator());
		}
		else {
			simulatorPanel.getNameTextField().setEditable(true);			
		}

		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (simulator == null) {
					simulator = new SpiceSimulator();
				}

				simulator.setName(simulatorPanel.getNameTextField().getText());
				simulator.setPath(simulatorPanel.getPathTextField().getText());
				simulator.setParameters(simulatorPanel.getParamTextField().getText());
				simulator.setDefaultSimulator(simulatorPanel.getDefaultCheckBox().isSelected());

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
	 * @return the simulator
	 */
	public SpiceSimulator getSimulator() {
		return simulator;
	}

}
