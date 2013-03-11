package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.pmedv.blackboard.dialogs.SpiceSimulatorManageDialog;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.dialogs.SpiceSimulatorDialog;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

public class AddSimulatorCommand extends AbstractCommand {

	public AddSimulatorCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddSimulatorCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.addsimulator"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddSimulatorCommand.description"));
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		String title = resources.getResourceByKey("AddSimulatorCommand.name");
		String subTitle = resources.getResourceByKey("AddSimulatorCommand.description");
		ImageIcon icon = null;
		SpiceSimulatorDialog dlg = new SpiceSimulatorDialog(title, subTitle, icon, null);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
			return;
		}
		
		if (dlg.getSimulator() != null) {
			
			SimulatorProvider provider = AppContext.getContext().getBean(SimulatorProvider.class);			
			if (dlg.getSimulator().getName() == null || dlg.getSimulator().getName().length() < 1) {
				throw new IllegalArgumentException("Name must be defined.");
			}
			
			try {
				if (provider.getElements().contains(dlg.getSimulator())) {
					throw new IllegalArgumentException("There's already a simulator with that name.");
				}
				provider.addElement(dlg.getSimulator());
				AppContext.getContext().getBean(SpiceSimulatorManageDialog.class).getModel().setSimulators(provider.getElements());
			}
			catch (Exception e1) {
				ErrorUtils.showErrorDialog(e1);
			}
		
			if (dlg.getSimulator().isDefaultSimulator()) {
				AppContext.getContext().getBean(SetSimulatorDefaultCommand.class).setSimulator(dlg.getSimulator());
				AppContext.getContext().getBean(SetSimulatorDefaultCommand.class).execute(null);
			}

			ErrorUtils.showErrorDialog(new IllegalStateException(resources.getResourceByKey("app.msg.restartneeded")));
			
		}
		
	}

}
