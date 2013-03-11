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
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.components.CmdJButton;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.provider.ApplicationToolbarProvider;

/**
 * Toggles the magneticity of symbols and wires
 * 
 * @author Matthias Pueski (16.06.2011)
 * 
 */
public class ToggleMagneticCommand extends AbstractCommand implements EditorChangedListener {

	private static final long serialVersionUID = -1928790760091970040L;

	public ToggleMagneticCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ToggleMagneticCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.magnet"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ToggleMagneticCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		editor.setMagnetic(!editor.isMagnetic());
		editor.refresh();
		editor.updateStatusBar();		
		setButtonSelected(editor);
	}
	
	@Override
	public void editorChanged(EditorChangedEvent event) {
		setButtonSelected(event.getEditor());
		setEnabled(event.getEditor() != null);
	}

	void setButtonSelected(BoardEditor editor) {
		// now set border of button depending on state
		ApplicationToolbarProvider provider = AppContext.getContext().getBean(ApplicationToolbarProvider.class);
		JToolBar toolbar = provider.getToolbar();
		for (int i = 0; i < toolbar.getComponentCount(); i++) {			
			if (toolbar.getComponent(i) instanceof CmdJButton) {
				CmdJButton button = (CmdJButton) toolbar.getComponent(i);
				if (button.getAction() instanceof ToggleMagneticCommand) {
					if (editor == null) {
						button.setBorderPainted(false);
					}
					else {
						button.setBorderPainted(editor.isMagnetic());						
					}					
				}
			}
		}						
	}			

}
