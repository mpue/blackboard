package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.pmedv.blackboard.dialogs.ModelEditDialog;
import org.pmedv.blackboard.panels.ModelListPanel;
import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

public class AddModelCommand extends AbstractCommand {

	public AddModelCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddModelCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.addmodel"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddModelCommand.description"));
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		String title = resources.getResourceByKey("AddModelCommand.name");
		String subTitle = resources.getResourceByKey("AddModelCommand.description");
		ImageIcon icon = null;
		ModelEditDialog dlg = new ModelEditDialog(title, subTitle, icon, null);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
			return;
		}
		
		if (dlg.getModel() != null) {
			
			ModelProvider provider = AppContext.getContext().getBean(ModelProvider.class);			
			if (dlg.getModel().getName() == null || dlg.getModel().getName().length() < 1) {
				throw new IllegalArgumentException("Name must be defined.");
			}
			
			try {
				if (provider.getElements().contains(dlg.getName())) {
					throw new IllegalArgumentException("There's already a model with that name.");
				}
				provider.addElement(dlg.getModel());
				AppContext.getContext().getBean(ModelListPanel.class).getModel().setModels(provider.getElements());
			}
			catch (Exception e1) {
				ErrorUtils.showErrorDialog(e1);
			}		
			
		}
		
	}

}
