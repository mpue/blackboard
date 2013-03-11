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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;

import org.pmedv.blackboard.dialogs.ScaleDesignerDialog;
import org.pmedv.core.commands.AbstractEditorCommand;

/**
 * This command opens the {@link ScaleDesignerDialog}
 * 
 * @author Matthias Pueski (21.03.2012)
 * 
 */
public class CreateScaleCommand extends AbstractEditorCommand implements MouseMotionListener {

	int mouseX;
	int mouseY;

	public CreateScaleCommand() {
		putValue(Action.NAME, resources.getResourceByKey("CreateScaleCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.scale"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("CreateScaleCommand.description"));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

		String title = resources.getResourceByKey("CreateScaleCommand.title");
		String subTitle = resources.getResourceByKey("CreateScaleCommand.subTitle");

		ScaleDesignerDialog dlg = new ScaleDesignerDialog(title, subTitle, null);
		dlg.setVisible(true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
