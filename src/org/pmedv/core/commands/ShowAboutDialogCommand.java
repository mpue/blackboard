/**
 * BlackBoard BreadBoard Designer Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2010-2011 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.core.commands;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AboutDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

public class ShowAboutDialogCommand extends AbstractCommand {

	private static final long serialVersionUID = 428365156605269679L;
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public ShowAboutDialogCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ShowAboutDialogCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.about"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ShowAboutDialogCommand.description"));				
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK));
	}	
	
	@Override
	public void execute(ActionEvent e) {

		final ApplicationContext ctx = AppContext.getContext();
		final ResourceService resources = ctx.getBean(ResourceService.class);
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);

		ImageIcon icon = resources.getIcon("icon.dialog.about");

		String title = "About";
		String subTitle = "Who created this Application.";

		AboutDialog aboutDialog = new AboutDialog(title, subTitle, icon, win);
		aboutDialog.setVisible(true);

	}

}
