package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.pmedv.blackboard.dialogs.ModelEditDialog;
import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

public class EditModelCommand extends AbstractCommand {

	public EditModelCommand() {
		putValue(Action.NAME, resources.getResourceByKey("EditModelCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.editmodel"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("EditModelCommand.description"));
	}
	
	private Model model;
	
	@Override
	public void execute(ActionEvent e) {
		
		String title = resources.getResourceByKey("EditModelCommand.name");
		String subTitle = resources.getResourceByKey("EditModelCommand.description");
		ImageIcon icon = null;
		ModelEditDialog dlg = new ModelEditDialog(title, subTitle, icon, model);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
			return;
		}

		ModelProvider provider = AppContext.getContext().getBean(ModelProvider.class);
		
		try {
			provider.storeElement(model);
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	
	

}
