package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.pmedv.blackboard.panels.ModelListPanel;
import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

public class DeleteModelCommand extends AbstractCommand {

	public DeleteModelCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DeleteModelCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.deletemodel"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("DeleteModelCommand.description"));
	}

	private Model model;

	@Override
	public void execute(ActionEvent e) {

		ModelProvider provider = AppContext.getContext().getBean(ModelProvider.class);

		try {
			provider.removeElement(model);
			AppContext.getContext().getBean(ModelListPanel.class).getModel().removeModel(model);
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}

	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}

}
