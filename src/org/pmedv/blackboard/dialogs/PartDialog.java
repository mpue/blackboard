/**

	BlackBoard breadboard designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */
package org.pmedv.blackboard.dialogs;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.divxdede.swing.busy.JBusyComponent;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXTable;
import org.pmedv.blackboard.commands.CreatePartCommand;
import org.pmedv.blackboard.commands.DeletePartCommand;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.filter.PartFilter;
import org.pmedv.blackboard.models.PartTableModel;
import org.pmedv.blackboard.panels.PartPanel;
import org.pmedv.blackboard.tools.PartFactory;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.util.ErrorUtils;
import org.pmedv.core.util.FileUtils;

/**
 * This is the main part browser dialog, it displays a filterable list on the left side
 * as well as a part preview and the part details on the right side.
 * 
 * @author Matthias Pueski (13.01.2011)
 *
 */
public class PartDialog extends AbstractNiceDialog {
	
	private static final long serialVersionUID = 1429247746599672970L;
	
	private static final String title = resources.getResourceByKey("PartDialog.title");
	private static final String subTitle = resources.getResourceByKey("PartDialog.subtitle");
	private static final String editTitle = resources.getResourceByKey("PartDialog.edittitle");
	private static final String editSubTitle = resources.getResourceByKey("PartDialog.editsubtitle");
		
	private static final ResourceService resourceService = ctx.getBean(ResourceService.class);	
	
	private PartPanel partPanel;
	private ArrayList<Part> selectedParts;
	private PartTableModel model;

	private static ImageIcon icon = resourceService.getIcon("icon.dialog.part");
	private static final Log log = LogFactory.getLog(PartDialog.class);
	private JBusyComponent<PartPanel> partBusyPanel;
	
	private Part currentPart = null;
	private int selectedRow;
	
	private RSyntaxTextArea textArea;
	private JTabbedPane tabbedPane;
	
	private JPopupMenu tablePopupMenu;
	
	public PartDialog() {
		super(title, subTitle, icon, true, true, true, true, AppContext.getContext().getBean(ApplicationWindow.class),null);
	}

	@Override
	protected void initializeComponents() {

		partPanel = new PartPanel();		
		partBusyPanel = new JBusyComponent<PartPanel>(partPanel);
		
		setSize(new Dimension(900, 750));	
		
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        RTextScrollPane textScrollPane = new RTextScrollPane(textArea);
        
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(textArea);
        
        JPopupMenu popup = textArea.getPopupMenu();
        popup.addSeparator();
        popup.add(new JMenuItem(new SaveAction()));
        
        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);        
        tabbedPane.addTab("Parts", partBusyPanel);
        tabbedPane.addTab("Part Editor", textScrollPane);
        tabbedPane.setEnabledAt(1, false);        
		getContentPanel().add(tabbedPane);
		
		tablePopupMenu = new JPopupMenu();
		
		// We do the part loading in background and display a busy panel while loading
		
		SwingWorker<ArrayList<Part>, Void> w = new SwingWorker<ArrayList<Part>, Void>() {

			@Override
			protected ArrayList<Part> doInBackground() {
				partBusyPanel.setBusy(true);
				try {
					return AppContext.getContext().getBean(PartFactory.class).getAvailableParts();
				}
				catch (Exception e) {
					ErrorUtils.showErrorDialog(e);
					return new ArrayList<Part>();
				}
			}
			
			@Override
			protected void done() {
				log.info("Done loading parts.");
				try {
					model = new PartTableModel(get());
					partPanel.getPartTable().setModel(model);															
				}
				catch (Exception e) {
					ErrorUtils.showErrorDialog(e);
				}
				partBusyPanel.setBusy(false);
				partPanel.transferFocus();				
			}
		};
		
		
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JXTable table = partPanel.getPartTable();
				result = OPTION_OK;
				selectedParts = new ArrayList<Part>();
				int rows[] = partPanel.getPartTable().getSelectedRows();
				try {
					for (int i = 0; i < rows.length; i++) {
						selectedParts.add(AppContext.getContext().getBean(PartFactory.class).createPart(
								model.getParts().get(table.convertRowIndexToModel(rows[i])).getFilename()));
					}
				}
				catch (Exception e1) {
					ErrorUtils.showErrorDialog(e1);
				}
				setVisible(false);
			}
		});
		
		getCancelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
			}
		});
		
		getNewButton().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppContext.getContext().getBean(CreatePartCommand.class).execute(e);				
			}
		});
		
		partPanel.getPartTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JXTable table = partPanel.getPartTable();
				int[] rows = table.getSelectedRows();
				if (rows.length == 1) {
					if (model == null)
						return;
					Part selectedPart = model.getParts().get(table.convertRowIndexToModel(rows[0]));
					currentPart = selectedPart;
					selectedRow = table.convertRowIndexToModel(rows[0]);
					partPanel.getImageLabel().setIcon(new ImageIcon(selectedPart.getImage()));
					partPanel.getImageLabel().setText(null);
					partPanel.getPartNameField().setText(selectedPart.getName());
					partPanel.getDescriptionPane().setText(selectedPart.getDescription());
					partPanel.getPackageTypeField().setText(selectedPart.getPackageType());
					partPanel.getAuthorField().setText(selectedPart.getAuthor());
					partPanel.getLicenseField().setText(selectedPart.getLicense());
					tabbedPane.setEnabledAt(1, true);
					textArea.setText(selectedPart.getXmlContent());
				}
				else {
					currentPart = null;
					partPanel.getImageLabel().setText(resources.getResourceByKey("PartDialog.selectmsg"));
					partPanel.getImageLabel().setIcon(null);
					tabbedPane.setEnabledAt(1, false);
					textArea.setText("");
				}
			}
		});
		
		tabbedPane.addChangeListener(new ChangeListener() {
		    // This method is called whenever the selected tab changes
		    public void stateChanged(ChangeEvent evt) {
		        JTabbedPane pane = (JTabbedPane)evt.getSource();
		        int index = pane.getSelectedIndex();
		        if (index == 0) {
		        	getTitleLabel().setText(title);
		        	getSubTitleLabel().setText(subTitle);		        	
		        }
		        else {
		        	getTitleLabel().setText(editTitle);
		        	getSubTitleLabel().setText(editSubTitle + " : "+currentPart.getName());		        			        	
		        }
		    }
		});
		
		partPanel.getPartTable().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handlePopupTrigger(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				handlePopupTrigger(e);
			}
		});
		
		tablePopupMenu.add(ctx.getBean(DeletePartCommand.class));
		
		// this is the filter configuration for the filter text box on the top
		PartFilter filter = new PartFilter(partPanel.getPartTable());
		BindingGroup filterGroup = new BindingGroup();
		// bind filter JTextBox's text attribute to part tables filterString
		// attribute
		filterGroup.addBinding(Bindings.createAutoBinding(READ, partPanel.getFilterPanel().getFilterTextField(),
				BeanProperty.create("text"), filter, BeanProperty.create("filterString")));
		filterGroup.bind();		
		w.execute();
					
	}

	private void handlePopupTrigger(MouseEvent e) {
		if (e.isPopupTrigger() && model.getParts().size() >= 1) {			
			Point p = e.getPoint();			 
			// get the row index that contains that coordinate
			int rowNumber = partPanel.getPartTable().rowAtPoint( p ); 
			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = partPanel.getPartTable().getSelectionModel();
			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( rowNumber, rowNumber );			
			tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());			
		}
	}
	
	/**
	 * @return the selectedParts
	 */
	public ArrayList<Part> getSelectedParts() {
		return selectedParts;
	}

	/**
	 * @return the model
	 */
	public PartTableModel getModel() {
		return model;
	}
	
	private class SaveAction extends AbstractAction {

    	public SaveAction() {
    		putValue(Action.NAME, resources.getResourceByKey("msg.save"));	
    	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JXTable table = partPanel.getPartTable();
			int[] rows = table.getSelectedRows();
			if (rows.length == 1) {
				if (model == null)
					return;
				int index = table.convertRowIndexToModel(rows[0]); 				
				Part selectedPart = model.getParts().get(index);				
				String content = textArea.getText();
				String name = selectedPart.getFilename();				
				String partFileLocation = AppContext.getWorkingDir() + "/parts/" + name;				
				FileUtils.writeFile(new File(partFileLocation),content);
				try {
					model.getParts().set(index, AppContext.getContext().getBean(PartFactory.class).createPart(name));
				} 
				catch (Exception e1) {
					ErrorUtils.showErrorDialog(e1);
				}
			}			
		}

	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		tabbedPane.setSelectedIndex(0);
	}

	private CompletionProvider createCompletionProvider() {
		
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		provider.addCompletion(new BasicCompletion(provider, "<part></part>"));
		provider.addCompletion(new BasicCompletion(provider, "<author></author>"));
		provider.addCompletion(new BasicCompletion(provider, "<license></license>"));
		provider.addCompletion(new BasicCompletion(provider, "<width></width>"));
		provider.addCompletion(new BasicCompletion(provider, "<height></height>"));
		provider.addCompletion(new BasicCompletion(provider, "<xLoc></xLoc>"));
		provider.addCompletion(new BasicCompletion(provider, "<yLoc></yLoc>"));
		provider.addCompletion(new BasicCompletion(provider, "<name></name>"));
		provider.addCompletion(new BasicCompletion(provider, "<imageName></imageName>"));
		provider.addCompletion(new BasicCompletion(provider, "<description></description>"));
		provider.addCompletion(new BasicCompletion(provider, "<packageType></packageType>"));
		provider.addCompletion(new BasicCompletion(provider, "<rotation></rotation>"));
		provider.addCompletion(new BasicCompletion(provider, "<designator></designator>"));
		provider.addCompletion(new BasicCompletion(provider, "<connections></connections>"));
		provider.addCompletion(new BasicCompletion(provider, "<pin></pin>"));
		provider.addCompletion(new BasicCompletion(provider, "<num></num>"));		
		
		return provider;

	}

	/**
	 * @return the currentPart
	 */
	public Part getCurrentPart() {
		return currentPart;
	}

	/**
	 * @return the selectedRow
	 */
	public int getSelectedRow() {
		return selectedRow;
	}
	
	

}
