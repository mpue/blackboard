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

import java.awt.BasicStroke;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.LineEdgeType;
import org.pmedv.blackboard.components.Palette;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.ShapePropertiesPanel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.preferences.Preferences;
import org.springframework.context.ApplicationContext;

/**
 * @author Matthias Pueski (08.06.2011)
 *
 */
public class AddLineCommand extends AbstractCommand {

	private static final long serialVersionUID = 2473298284426159552L;

	private static final Log log = LogFactory.getLog(AddLineCommand.class);
	
	private Line line = null;
	private BoardEditorModel model = null;
	
	public AddLineCommand(BoardEditorModel model) {
		this.model = model;
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		if (line == null)
			return;

		final ApplicationContext ctx = AppContext.getContext();
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		final UndoManager undoManager = editor.getUndoManager();
		
		line.setLayer(model.getCurrentLayer().getIndex());
		
		if (line.getLength() >= editor.getRaster()) {
			if (model.getCurrentLayer().getItems().contains(line)) {
				JOptionPane.showMessageDialog(AppContext.getContext().getBean(ApplicationWindow.class), resources.getResourceByKey("msg.connectionexists"));
			}
			else {
				
				final ShapePropertiesPanel shapesPanel = ctx.getBean(ShapePropertiesPanel.class);
				
				line.setStartType((LineEdgeType) shapesPanel.getStartLineCombo().getSelectedItem());
				line.setEndType((LineEdgeType) shapesPanel.getEndLineCombo().getSelectedItem());
				
				for (Layer layer : model.getLayers()) {
					
					for (Item item : layer.getItems()) {
						if (item instanceof Line) {
							
							Line l = (Line)item;
							
							if (l.containsPoint(line.getStart(),1)) {								
								line.setStartType(LineEdgeType.ROUND_DOT);								
							}
							if (l.containsPoint(line.getEnd(),1)) {
								line.setEndType(LineEdgeType.ROUND_DOT);
							}
							
						}
					}
					
				}
				
				line.setStroke((BasicStroke)shapesPanel.getThicknessCombo().getSelectedItem());
				
				final Boolean useLayerColor = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor");
				
				if (useLayerColor) {
					line.setColor(model.getCurrentLayer().getColor());					
				}
				else {
					line.setColor(ctx.getBean(Palette.class).getCurrentColor());	
				}
				
				log.info("Adding line to layer "+model.getCurrentLayer());
				model.getCurrentLayer().getItems().add(line);
				
				if (!undoManager.addEdit(new AddLineEdit(line))) {
					log.debug("Could not add edit "+this.getClass());
				}
				editor.setFileState(FileState.DIRTY);
			}
		}
		
		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
		
	}


	/**
	 * @param line the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
	}
	
	
	
}
