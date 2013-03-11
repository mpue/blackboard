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

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.divxdede.swing.busy.JBusyComponent;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.commands.AddDatasheetCommand;
import org.pmedv.blackboard.commands.ImportDatasheetFolderCommand;
import org.pmedv.blackboard.commands.RemoveDatasheetCommand;
import org.pmedv.blackboard.filter.DataSheetFilter;
import org.pmedv.blackboard.models.DataSheetTableModel;
import org.pmedv.blackboard.panels.DatasheetPanel;
import org.pmedv.blackboard.provider.DataSheetProvider;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.util.ErrorUtils;

/**
 * This is the main datasheet browser dialog, it displays a filterable list
 * of available {@link DatasheetBean} objects.
 * 
 * 
 * @author Matthias Pueski (06.09.2011)
 *
 */
public class DatasheetDialog extends AbstractNiceDialog {
	
	private static final long serialVersionUID = -2324807082194425186L;

	private static final ResourceService resources = ctx.getBean(ResourceService.class);	

	private DatasheetPanel sheetPanel;
	private DataSheetTableModel model;
	private static String title = resources.getResourceByKey("DatasheetDialog.title");
	private static String subTitle = resources.getResourceByKey("DatasheetDialog.subtitle");
	private static ImageIcon icon = resources.getIcon("icon.dialog.datasheet");
	private static final Log log = LogFactory.getLog(DatasheetDialog.class);
	private JBusyComponent<DatasheetPanel> busyPanel;
	
	private DataSheetProvider sheetProvider;
	
	public DatasheetDialog() {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class),null);
	}

	@Override
	protected void initializeComponents() {

		sheetProvider = AppContext.getContext().getBean(DataSheetProvider.class);
		
		sheetPanel = new DatasheetPanel();		
		busyPanel = new JBusyComponent<DatasheetPanel>(sheetPanel);
		
		setSize(new Dimension(900, 650));		
		getContentPanel().add(busyPanel);
		
		SwingWorker<ArrayList<DatasheetBean>, Void> w = new SwingWorker<ArrayList<DatasheetBean>, Void>() {

			@Override
			protected ArrayList<DatasheetBean> doInBackground() throws Exception {
				busyPanel.setBusy(true);
				sheetProvider.loadSheets();
				return sheetProvider.getDatasheetList().getDatasheets();
			}
			
			@Override
			protected void done() {
				log.info("Done loading sheets.");
				try {
					model = new DataSheetTableModel(get());
					sheetPanel.getDatasheetTable().setModel(model);															
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				busyPanel.setBusy(false);
				sheetPanel.transferFocus();				
			}
		};
		
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
		
		sheetPanel.getDatasheetTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					
					int modelIndex = sheetPanel.getDatasheetTable().convertRowIndexToModel(sheetPanel.getDatasheetTable().getSelectedRow());					
					DatasheetBean sheet = model.getDatasheetBeans().get(modelIndex);
					
					if (Desktop.isDesktopSupported()) {
			            Desktop desktop = Desktop.getDesktop();
			            try {
							desktop.open(new File(sheet.getLocation()));
						} 
			            catch (Exception e1) {
			            	ErrorUtils.showErrorDialog(e1);
						}
			        }        
				}
			}
		});
		
		
		sheetPanel.getAddSheetButton().setAction(AppContext.getContext().getBean(AddDatasheetCommand.class));
		sheetPanel.getRemoveSheetButton().setAction(AppContext.getContext().getBean(RemoveDatasheetCommand.class));
		sheetPanel.getImportFolderButton().setAction(AppContext.getContext().getBean(ImportDatasheetFolderCommand.class));
		
		// create filter for sheets
		DataSheetFilter filter = new DataSheetFilter(sheetPanel.getDatasheetTable());
		BindingGroup filterGroup = new BindingGroup();
		
		// bind filter JTextBox's text attribute to part tables filterString attribute
		filterGroup.addBinding(Bindings.createAutoBinding(READ, sheetPanel.getFilterPanel().getFilterTextField(),
				BeanProperty.create("text"), filter, BeanProperty.create("filterString")));
		filterGroup.bind();		
		w.execute();
					
	}

	public DatasheetPanel getSheetPanel() {
		return sheetPanel;
	}

	public DataSheetTableModel getModel() {
		return model;
	}

}
