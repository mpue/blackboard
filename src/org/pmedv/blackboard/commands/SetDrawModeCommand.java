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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.EditorMode;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.LineEdgeType;
import org.pmedv.blackboard.panels.ShapePropertiesPanel;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.components.CmdJButton;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.provider.ApplicationToolbarProvider;
import org.springframework.context.ApplicationContext;


/**
 * Sets the editor mode to draw lines
 * 
 * @author Matthias Pueski
 *
 */
public class SetDrawModeCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = 1408964747937526282L;
		
	public SetDrawModeCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SetDrawModeCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.draw"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SetDrawModeCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, 0));
		setEnabled(false);		
	}	
		
	@Override
	public void execute(ActionEvent e) {
		
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		editor.clearSelectionBorder();
		editor.setSelectedItem(null);
		editor.getSelectedItems().clear();
		editor.setEditorMode(EditorMode.DRAW_LINE);		
		editor.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		restoreSettings();		
		updateToolbar();
		
		editor.updateStatusBar();
		editor.refresh();
		
	}

	/**
	 * Updates the toolbar with the new editor state
	 */
	private void updateToolbar() {
		
		ApplicationContext ctx = AppContext.getContext();
		ApplicationToolbarProvider provider = ctx.getBean(ApplicationToolbarProvider.class);

		JToolBar toolbar = provider.getToolbar();

		for (int i = 0; i < toolbar.getComponentCount(); i++) {
			
			if (toolbar.getComponent(i) instanceof CmdJButton) {

				CmdJButton button = (CmdJButton) toolbar.getComponent(i);
				if (button.getAction() instanceof SetDrawModeCommand)
					button.setBorderPainted(true);
				if (button.getAction() instanceof SetSelectModeCommand)
					button.setBorderPainted(false);
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
	}

	/**
	 * Restores settings from the last selected style
	 */
	private void restoreSettings() {
		
		ShapePropertiesPanel shapesPanel = AppContext.getContext().getBean(ShapePropertiesPanel.class);
		
		if (shapesPanel.getStartLineCombo().getSelectedItem() == null) {
			
			if (shapesPanel.getLastSelectedLineStartStyle() != null) {
				shapesPanel.getStartLineCombo().setSelectedItem(shapesPanel.getLastSelectedLineStartStyle());
			}
			else {
				shapesPanel.getStartLineCombo().setSelectedItem(LineEdgeType.ROUND_DOT);	
			}			
		}
		if (shapesPanel.getEndLineCombo().getSelectedItem() == null) {
			if (shapesPanel.getLastSelectedLineEndStyle() != null) {
				shapesPanel.getEndLineCombo().setSelectedItem(shapesPanel.getLastSelectedLineEndStyle());
			}
			else {
				shapesPanel.getEndLineCombo().setSelectedItem(LineEdgeType.ROUND_DOT);	
			}			
		}
		if (shapesPanel.getThicknessCombo().getSelectedItem() == null) {
			
			if (shapesPanel.getLastSelectedStroke() != null) {
				shapesPanel.getThicknessCombo().setSelectedItem(shapesPanel.getLastSelectedStroke());
			}
			else {
				shapesPanel.getThicknessCombo().setSelectedIndex(1);	
			}
			
		}
	}

}
