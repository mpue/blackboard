package org.pmedv.blackboard.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import org.pmedv.blackboard.panels.ModelPanel;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceType;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class ModelEditDialog extends AbstractNiceDialog {

	private Model model;

	private ModelPanel modelPanel;

	public ModelEditDialog(String title, String subTitle, ImageIcon icon, Model model) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), model);
	}

	@Override
	protected void initializeComponents() {

		modelPanel = new ModelPanel();

		setSize(new Dimension(640, 480));
		getContentPanel().add(modelPanel);

		if (getUserObject() != null && getUserObject() instanceof Model) {
			this.model = (Model) getUserObject();
			modelPanel.getNameTextField().setText(model.getName());
			modelPanel.getTypeComboBox().setSelectedItem(model.getType());
			modelPanel.getModelEditor().setText(model.getContent());
		}

		if (getUserObject() == null) {
			model = new Model();
		}

		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				result = OPTION_OK;

				model.setContent(modelPanel.getModelEditor().getText());
				model.setType((SpiceType) modelPanel.getTypeComboBox().getSelectedItem());
				model.setName(modelPanel.getNameTextField().getText());
				
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
		modelPanel.getModelEditor().setText(model.getContent());
		modelPanel.getNameTextField().setText(model.getName());
		modelPanel.getTypeComboBox().setSelectedItem(model.getType());
	}
	
	

}
