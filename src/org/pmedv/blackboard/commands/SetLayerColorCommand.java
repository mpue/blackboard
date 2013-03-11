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
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.LayerTableModel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.springframework.context.ApplicationContext;

/**
 * The {@link SetLayerColorCommand} allows the user to select a color for the selected layer
 * 
 * @author Matthias Pueski (13.06.2011)
 * 
 */
public class SetLayerColorCommand extends AbstractCommand {
	
	private static final long serialVersionUID = -2617325756383421753L;

	public SetLayerColorCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SetLayerColorCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.color"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SetLayerColorCommand.description"));
	}

	private JDialog dialog = null;
	private Layer layer = null;
	
	@Override
	public void execute(ActionEvent e) {
		
		ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		final String title = resources.getResourceByKey("SetLayerColorCommand.dialog.subtitle");
		
		final LayerTableModel model = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel().getLayerModel();
		AlternatingLineTable layerTable = AppContext.getContext().getBean(ShowLayersCommand.class).getLayerPanel().getLayerTable();
		
		int index = layerTable.convertRowIndexToModel(layerTable.getSelectedRow());
		
		if (index >= 0) {
			layer = model.getLayer().get(index);	
		}
		
		if (layer == null) {
			return;
		}
			
		final JColorChooser chooser = new JColorChooser();
		
		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				dialog.setVisible(false);			
				layer.setColor(chooser.getColor());
				model.fireTableDataChanged();
				EditorUtils.getCurrentActiveEditor().setFileState(FileState.DIRTY);
			}
		};
		
		ActionListener cancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		};
		
		dialog = JColorChooser.createDialog(win, title, true, chooser, okListener, cancelListener);
		dialog.setVisible(true);
	}
}
