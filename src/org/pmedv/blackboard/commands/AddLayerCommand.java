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

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;

public class AddLayerCommand extends AbstractCommand {

	private static final long serialVersionUID = -4060436618149711784L;
	
	public AddLayerCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddLayerCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.addlayer"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddLayerCommand.description"));
	}	
	
	@Override
	public void execute(ActionEvent e) {

		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		if (editor != null) {

			final ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
			
			final String name = JOptionPane.showInputDialog(win, resources.getResourceByKey("AddLayerCommand.message"));

			if (name == null || name.length() < 1)
				return;
			
			int layerIndex = 0;

			for (Layer l : editor.getModel().getLayers()) {

				if (l.getIndex() > layerIndex)
					layerIndex = l.getIndex();

			}

			layerIndex++;

			editor.getModel().getLayers().add(new Layer(layerIndex, name));
			editor.updateStatusBar();
			editor.notifyListeners(EventType.EDITOR_CHANGED);
			editor.setFileState(FileState.DIRTY);
			
		}		
		
	}

}
