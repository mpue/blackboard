package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.pmedv.blackboard.dialogs.SpiceSimulatorManageDialog;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.blackboard.spice.dialogs.SpiceSimulatorDialog;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

public class EditSimulatorCommand extends AbstractCommand {

	private SpiceSimulator simulator;
	
	public EditSimulatorCommand() {
		putValue(Action.NAME, resources.getResourceByKey("EditSimulatorCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.edit"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("EditSimulatorCommand.description"));
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		String title = resources.getResourceByKey("EditSimulatorCommand.name");
		String subTitle = resources.getResourceByKey("EditSimulatorCommand.description");
		ImageIcon icon = null;
		
		if (simulator == null) {
			return;
		}
		
		SpiceSimulatorDialog dlg = new SpiceSimulatorDialog(title, subTitle, icon, simulator);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
			return;
		}
		
		if (dlg.getSimulator() != null) {
			
			SimulatorProvider provider = AppContext.getContext().getBean(SimulatorProvider.class);			
			
			try {
				provider.storeElement(simulator);
				AppContext.getContext().getBean(SpiceSimulatorManageDialog.class).getModel().setSimulators(provider.getElements());
			}
			catch (Exception e1) {
				ErrorUtils.showErrorDialog(e1);
			}
			
			if (simulator.isDefaultSimulator()) {
				AppContext.getContext().getBean(SetSimulatorDefaultCommand.class).setSimulator(simulator);
				AppContext.getContext().getBean(SetSimulatorDefaultCommand.class).execute(null);
			}
			
		}
		
	}

	/**
	 * @param simulator the simulator to set
	 */
	public void setSimulator(SpiceSimulator simulator) {
		this.simulator = simulator;
	}

	
	
}
