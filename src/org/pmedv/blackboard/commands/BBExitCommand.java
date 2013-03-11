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
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.app.UnsavedResource;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.dialogs.UnsavedResourcesDialog;
import org.pmedv.core.commands.ExitCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.perspectives.AbstractPerspective;
import org.springframework.context.ApplicationContext;

/**
 * Exit command for the BlackBoard application. Performa a regular 
 * exit and checks if any resource is unsaved.
 * 
 * @author Matthias Pueski (19.11.2010)
 * 
 */
public class BBExitCommand extends ExitCommand {
	
	private static final Log log = LogFactory.getLog(BBExitCommand.class);
	
	private static final long serialVersionUID = -4648316039706055715L;
	
	public BBExitCommand() {
		super();
	}

	@Override
	public void execute(ActionEvent e) {
		
		final ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
		
		File tempDir = new File(System.getProperty("java.io.tmpdir"),"blackboard");
		
		try {
			if (tempDir != null) { 
				FileUtils.deleteDirectory(tempDir);
			}
		}
		catch (IOException e1) {
			log.info("Could not delete temp directory");
		}		
		
		ArrayList<UnsavedResource> unsaved = new ArrayList<UnsavedResource>();
		
		AbstractPerspective a = advisor.getCurrentPerspective();
		ArrayList<BoardEditor> editors = EditorUtils.getCurrentPerspectiveOpenEditors(a);
		
		for (BoardEditor editor : editors) {
			if (editor.getFileState().equals(FileState.DIRTY)) {
				unsaved.add(new UnsavedResource(editor));
			}
		}

		if (unsaved.size() > 0) {
			
			String title = resources.getResourceByKey("BBExitCommand.dialog.title");// Please select resources to save
			String subTitle = resources.getResourceByKey("BBExitCommand.dialog.subTitle");// "The following resources are unsaved";
			ImageIcon icon = resources.getIcon("icon.noresource64");
			
			UnsavedResourcesDialog dlg = new UnsavedResourcesDialog(title, subTitle, icon, unsaved);
			dlg.setVisible(true);
			
			if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
				return;
			}
			else {
				// save resources				
				for (UnsavedResource u : dlg.getResourcesToSave()) {
					new SaveBoardCommand(u.getEditor()).execute(null);
				}
				
			}
		}
		
		try {
			FileUtils.deleteDirectory(new File(AppContext.getWorkingDir(), "temp"));
		} 
		catch (IOException e1) {
			log.info("Could not delete temp directory");
		}
		
		super.execute(e);
	}
}
