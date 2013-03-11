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
package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;

import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.blackboard.provider.DataSheetProvider;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;

/**
 * The <code>ImportDatasheetFolderCommand</code> imports all files inside a folder
 * as datasheet beans. 
 * 
 * TODO: We should provide an option to do this recursively.
 * 
 * @author Matthias Pueski (24.09.2011)
 *
 */
public class ImportDatasheetFolderCommand extends AbstractCommand {

	private static final long serialVersionUID = -5923122551781782644L;

	public ImportDatasheetFolderCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ImportDatasheetFolderCommand.name"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ImportDatasheetFolderCommand.description"));
	}	
	
	@Override
	public void execute(ActionEvent e) {

		DataSheetProvider provider = AppContext.getContext().getBean(DataSheetProvider.class);

		JFileChooser fc = new JFileChooser(AppContext.getLastSelectedFolder());
		fc.setDialogTitle(resources.getResourceByKey("ImportDatasheetFolderCommand.dialog.subtitle"));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = fc.showOpenDialog(AppContext.getContext().getBean(ApplicationWindow.class));

		if (result == JFileChooser.CANCEL_OPTION) {
			return;
		}

		if (fc.getSelectedFile() == null) {
			return;			
		}

		File selectedFolder = fc.getSelectedFile();

		for (int i=0; i < selectedFolder.listFiles().length;i++) {
			
			File currentFile = selectedFolder.listFiles()[i];
			
			DatasheetBean dsb = new DatasheetBean();
			
			if (currentFile.isFile()) {

				dsb.setDescription(currentFile.getName().substring(0,currentFile.getName().lastIndexOf(".")));
				dsb.setLocation(currentFile.getAbsolutePath());
				dsb.setName(dsb.getDescription());
				
				if (!provider.getDatasheetList().getDatasheets().contains(dsb)) {
					provider.addSheet(dsb);
				}
				
			}
			
		}
		
		try {
			provider.storeSheetList();
		} 
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
			return;
		}
		
		AppContext.getContext().getBean(DatasheetDialog.class).getModel().
			setDatasheetBeans(provider.getDatasheetList().getDatasheets());
		
	}

}
