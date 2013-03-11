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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;

/**
 * The {@link BrowseSheetsCommand} opens a {@link DatasheetDialog} in order
 * to browse for datasheets.
 * 
 * @author Matthias Pueski (06.08.2011)
 *
 */
public class BrowseSheetsCommand extends AbstractCommand {

	private static final long serialVersionUID = -3051907585852536276L;

	public BrowseSheetsCommand() {
		putValue(Action.NAME, resources.getResourceByKey("BrowseSheetsCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.datasheet"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("BrowseSheetsCommand.description"));	
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
	}
	
	@Override
	public void execute(ActionEvent e) {
		AppContext.getContext().getBean(DatasheetDialog.class).setVisible(true);
	}

}
