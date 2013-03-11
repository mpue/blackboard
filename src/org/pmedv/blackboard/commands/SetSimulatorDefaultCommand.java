package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.pmedv.blackboard.dialogs.SpiceSimulatorManageDialog;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

public class SetSimulatorDefaultCommand extends AbstractCommand {

	private SpiceSimulator simulator;
	
	public SetSimulatorDefaultCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SetSimulatorDefaultCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.default"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SetSimulatorDefaultCommand.description"));

	}
	
	@Override
	public void execute(ActionEvent e) {
		
		if (simulator == null) {
			return;
		}
		
		SimulatorProvider provider = AppContext.getContext().getBean(SimulatorProvider.class);			
		
		try {
			for (SpiceSimulator sim : provider.getElements()) {
				sim.setDefaultSimulator(false);
				provider.storeElement(sim);
			}
			simulator.setDefaultSimulator(true);
			provider.storeElement(simulator);
			AppContext.getContext().getBean(SpiceSimulatorManageDialog.class).getModel().setSimulators(provider.getElements());
			
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		
	}

	/**
	 * @param simulator the simulator to set
	 */
	public void setSimulator(SpiceSimulator simulator) {
		this.simulator = simulator;
	}

	
	
}
