package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.pmedv.blackboard.dialogs.SpiceSimulatorManageDialog;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

public class DeleteSimulatorCommand extends AbstractCommand {

	private SpiceSimulator simulator;
	
	public DeleteSimulatorCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DeleteSimulatorCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.delete"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("DeleteSimulatorCommand.description"));
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		if (simulator == null) {
			return;
		}
		
		SimulatorProvider provider = AppContext.getContext().getBean(SimulatorProvider.class);			
		
		try {
			provider.removeElement(simulator);
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
