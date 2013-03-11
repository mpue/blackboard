package org.pmedv.blackboard.panels;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXSearchField;
import org.pmedv.blackboard.commands.AddModelCommand;
import org.pmedv.blackboard.commands.DeleteModelCommand;
import org.pmedv.blackboard.commands.EditModelCommand;
import org.pmedv.blackboard.commands.ImportLibraryCommand;
import org.pmedv.blackboard.filter.ModelFilter;
import org.pmedv.blackboard.models.ModelTableModel;
import org.pmedv.blackboard.provider.AbstractElementProvider;
import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;

public class ModelListPanel extends JPanel {

	// the model table
	private AlternatingLineTable modelTable;
	// the model of the ModelTable
	private ModelTableModel model;
	// field for filtering
	private JXSearchField searchField;
	// button panel for table actions
	private JPanel buttonPanel;
	// the data provider for the model
	private AbstractElementProvider<Model> provider;
	// The buttons for the table actions
	private JButton addModelButton;
	private JButton importLibraryButton;
	// Popup for the table
	private JPopupMenu tablePopupMenu;
	
	public ModelListPanel() {
		super(new BorderLayout());
		initializeComponents();
	}

	private void initializeComponents() {

		searchField = new JXSearchField();
		searchField.setPrompt("Filter");
		add(searchField, BorderLayout.NORTH);
		
		// Prevent Actions being triggered having a single key as accelerator
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() >= 0x41 && e.getKeyCode() <= 0x5a && e.getModifiers() == 0) {
					e.consume();
				}
			}
		});

		provider = AppContext.getContext().getBean(ModelProvider.class);
		model = new ModelTableModel(provider.getElements());
		modelTable = new AlternatingLineTable(model);

		JScrollPane scrollPane = new JScrollPane(modelTable);
		add(scrollPane, BorderLayout.CENTER);

		tablePopupMenu = new JPopupMenu();

		tablePopupMenu.add(AppContext.getContext().getBean(AddModelCommand.class));
		tablePopupMenu.add(AppContext.getContext().getBean(EditModelCommand.class));
		tablePopupMenu.add(AppContext.getContext().getBean(DeleteModelCommand.class));
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		addModelButton = new JButton(AppContext.getContext().getBean(AddModelCommand.class));
		importLibraryButton = new JButton(AppContext.getContext().getBean(ImportLibraryCommand.class));
		
		buttonPanel.add(addModelButton);
		buttonPanel.add(importLibraryButton);

		add(buttonPanel, BorderLayout.SOUTH);

		// this is the filter configuration for the filter text box on the top
		ModelFilter filter = new ModelFilter(modelTable);
		BindingGroup filterGroup = new BindingGroup();
		// bind filter JTextBox's text attribute to part tables filterString
		// attribute
		filterGroup.addBinding(Bindings.createAutoBinding(READ, searchField, BeanProperty.create("text"), filter, BeanProperty.create("filterString")));
		filterGroup.bind();

		modelTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
			}

		});

	}

	private void handleMouseEvent(MouseEvent e) {
		Point p = e.getPoint();
		int row = modelTable.rowAtPoint(p);
		modelTable.setRowSelectionInterval(row, row);
		int selectedRow = modelTable.convertRowIndexToModel(modelTable.getSelectedRow());
		Model m = model.getModels().get(selectedRow);
		
		AppContext.getContext().getBean(EditModelCommand.class).setModel(m);
		AppContext.getContext().getBean(DeleteModelCommand.class).setModel(m);
		
		if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			AppContext.getContext().getBean(EditModelCommand.class).execute(null);
		}
		
		if (e.isPopupTrigger()) {
			tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());												
		}

	}

	/**
	 * @return the model
	 */
	public ModelTableModel getModel() {
		return model;
	}
	
	

}
