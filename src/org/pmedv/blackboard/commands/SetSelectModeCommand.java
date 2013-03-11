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

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.EditorMode;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.components.CmdJButton;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.provider.ApplicationToolbarProvider;
import org.springframework.context.ApplicationContext;

public class SetSelectModeCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = 3308225266004745759L;

	public SetSelectModeCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SetSelectModeCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.select"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SetSelectModeCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		editor.setEditorMode(EditorMode.SELECT);
		editor.setCursor(Cursor.getDefaultCursor());

		ApplicationToolbarProvider provider = ctx.getBean(ApplicationToolbarProvider.class);

		JToolBar toolbar = provider.getToolbar();

		for (int i = 0; i < toolbar.getComponentCount(); i++) {

			if (toolbar.getComponent(i) instanceof CmdJButton) {
				CmdJButton button = (CmdJButton) toolbar.getComponent(i);
				if (button.getAction() instanceof SetDrawModeCommand)
					button.setBorderPainted(false);
				if (button.getAction() instanceof SetSelectModeCommand)
					button.setBorderPainted(true);
				if (button.getAction() instanceof SetDrawRectangleModeCommand)
					button.setBorderPainted(false);
				if (button.getAction() instanceof SetDrawEllipseModeCommand)
					button.setBorderPainted(false);
				if (button.getAction() instanceof SetConnectionCheckModeCommand)
					button.setBorderPainted(false);
				if (button.getAction() instanceof SetDrawMeasureModeCommand)
					button.setBorderPainted(false);
				if (button.getAction() instanceof SetMoveModeCommand)
					button.setBorderPainted(false);

			}

		}
		editor.refresh();
		editor.updateStatusBar();

	}

}
