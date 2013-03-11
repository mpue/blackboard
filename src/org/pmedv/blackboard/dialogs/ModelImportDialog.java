package org.pmedv.blackboard.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.pmedv.blackboard.models.ModelImportTableModel;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.dialogs.AbstractNiceDialog;

public class ModelImportDialog extends AbstractNiceDialog {
	
	private static final String title = resources.getResourceByKey("ImportLibraryCommand.name");
	private static final String subTitle = resources.getResourceByKey("ImportLibraryCommand.description");
	private static final ImageIcon icon = null;

	private AlternatingLineTable modelTable;
	private ModelImportTableModel model;
	
	private ArrayList<Model> models;
	
	private JButton selectAllButton;
	
	public ModelImportDialog(ArrayList<Model> models) {
		super(title,subTitle,icon,true,false,true,true,win,models);		
	}
	
	@Override
	protected void initializeComponents() {
		
		setSize(new Dimension(800,600));
		
		if (getUserObject() != null) {
			@SuppressWarnings("unchecked")
			ArrayList<Model> models = (ArrayList<Model>)getUserObject();
			model = new ModelImportTableModel(models);
		}
		else {
			model = new ModelImportTableModel();	
		}
		
		modelTable = new AlternatingLineTable(model);

		JScrollPane scrollPane = new JScrollPane(modelTable);		
		JPanel panel = new JPanel(new BorderLayout());
		
		selectAllButton = new JButton("Select all");
		selectAllButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Model m : model.getModels()) {
					m.setShouldImport(true);
					model.fireTableDataChanged();
				}				
			}
		});
		
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(selectAllButton, BorderLayout.SOUTH);
		
		getContentPanel().add(panel);
		
		getCancelButton().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
			}
		});
		
		getOkButton().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				models = new ArrayList<Model>();
				
				for (Model m : model.getModels()) {
					if (m.isShouldImport()) {
						models.add(m);
					}
				}
				
				result = OPTION_OK;
				setVisible(false);
				
			}
		});
		
	}

	public ArrayList<Model> getModels() {
		return models;
	}
	
	

}
