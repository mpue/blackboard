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

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;


public class SaveNetlistCommand extends AbstractCommand {

	public SaveNetlistCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SaveNetlistCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.save"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SaveNetlistCommand.description"));
	}

	private String netlist;
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);

		String path = System.getProperty("user.home");
		if (AppContext.getLastSelectedFolder() != null) {
			path = AppContext.getLastSelectedFolder();
		}

		final JFileChooser fc = new JFileChooser(path);

		fc.setDialogTitle(resources.getResourceByKey("SaveNetlistCommand.dialog.subtitle"));
		fc.setApproveButtonText(resources.getResourceByKey("msg.save"));

		int result = fc.showOpenDialog(win);

		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}

		if (fc.getSelectedFile() == null)
			return;

		File selectedFile = fc.getSelectedFile();

		AppContext.setLastSelectedFolder(selectedFile.getParentFile().getAbsolutePath());

		if (selectedFile.exists()) {
			result = JOptionPane.showConfirmDialog(win, resources.getResourceByKey("msg.warning.fileexists"), resources.getResourceByKey("msg.warning"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}

		if (selectedFile != null && netlist != null) {
			try {
				FileUtils.writeStringToFile(selectedFile, netlist);
			}
			catch (IOException e1) {
				ErrorUtils.showErrorDialog(e1);
			}
			
		}

	}

	/**
	 * @return the netlist
	 */
	public String getNetlist() {
		return netlist;
	}

	/**
	 * @param netlist the netlist to set
	 */
	public void setNetlist(String netlist) {
		this.netlist = netlist;
	}
	
	

}
