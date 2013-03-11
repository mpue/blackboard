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
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.models.PartTableModel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;


/**
 * The delete command deletes a part from the part library
 *  
 * @author Matthias Pueski (02.03.2012)
 *
 */
public class DeletePartCommand extends AbstractCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	
	private static final Log log = LogFactory.getLog(DeletePartCommand.class);
	
	public DeletePartCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DeletePartCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.delete"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("DeletePartCommand.description"));
	}
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
		
		String message = resources.getResourceByKey("DeletePartCommand.warning");
		int result = JOptionPane.showConfirmDialog(win, message, resources.getResourceByKey("msg.warning"), JOptionPane.YES_NO_OPTION);
		
		if (result == JOptionPane.YES_OPTION) { 
			
			PartDialog dialog = AppContext.getContext().getBean(PartDialog.class);			
			Part part = dialog.getCurrentPart();
			
			File partFile = new File(AppContext.getWorkingDir() + "/parts/" + part.getFilename());
			log.debug("Deleting "+partFile.getAbsolutePath());
			partFile.delete();
			
			File imageFile = new File(AppContext.getWorkingDir() + "/parts/images/" + part.getImageName());
			log.debug("Deleting "+imageFile.getAbsolutePath());
			imageFile.delete();
			
			PartTableModel model = dialog.getModel();			
			model.getParts().remove(dialog.getSelectedRow());
			
			dialog.getModel().removePart(part);
			dialog.getModel().fireTableDataChanged();
		}
		
	}

	
}
