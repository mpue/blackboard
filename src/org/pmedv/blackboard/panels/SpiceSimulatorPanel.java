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

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.core.components.AlternatingLineTable;

/**
 * The <code>SpiceSimulatorPanel</code> is the main panel for simulator
 * management. It allows the user to view and manage installed spice simulators.
 * 
 * @see SpiceSimulator
 * @see SimulatorProvider
 * 
 * @author Matthias Pueski (6.11.2012)
 *
 */
public class SpiceSimulatorPanel extends JPanel {

	// the scroll pane containing the sheets table	
	private JScrollPane scrollPane;
	// table containing datasheets
	private AlternatingLineTable simulatorTable;
	
	public SpiceSimulatorPanel() {		
		super(new BorderLayout());
		initializeComponents();		
	}

	private void initializeComponents() {
		simulatorTable = new AlternatingLineTable();
		scrollPane = new JScrollPane(simulatorTable);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * @return the simulatorTable
	 */
	public AlternatingLineTable getSimulatorTable() {
		return simulatorTable;
	}

	/**
	 * @param simulatorTable the simulatorTable to set
	 */
	public void setSimulatorTable(AlternatingLineTable simulatorTable) {
		this.simulatorTable = simulatorTable;
	}


}
