/**

	BlackBoard breadboard designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2012 Matthias Pueski
	
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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Diode;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.LineEdgeType;
import org.pmedv.blackboard.components.Palette;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.components.Shape;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.ShapePropertiesPanel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;

/**
 * The <code>AddItemCommand</code> adds a new {@link Item} to the {@link BoardEditor}
 * 
 * @author Matthias Pueski (08.06.2011)
 *
 */
public class AddItemCommand extends AbstractCommand {

	private static final long serialVersionUID = 2473298284426159552L;

	private static final Log log = LogFactory.getLog(AddItemCommand.class);
	
	private Item item = null;
		
	private final List<Item> removals = new LinkedList<Item>();
	private final List<Item> addables = new LinkedList<Item>();
	
	@Override
	public void execute(ActionEvent e) {
		
		if (item == null)
			return;

		boolean split = false;
		
		removals.clear();
		addables.clear();
		
		ApplicationContext ctx = AppContext.getContext();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		BoardEditorModel model = editor.getModel();
		UndoManager undoManager = editor.getUndoManager();				
		
		final ShapePropertiesPanel shapesPanel = ctx.getBean(ShapePropertiesPanel.class);
		item.setLayer(model.getCurrentLayer().getIndex());	
		
		Boolean useLayerColor = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor");
		
		if (useLayerColor) {
			item.setColor(model.getCurrentLayer().getColor());					
		}
		else {
			item.setColor(ctx.getBean(Palette.class).getCurrentColor());	
		}
		
		int maxIndex = EditorUtils.getFreeIndex(model);
		item.setIndex(maxIndex);	
		
		if (item instanceof Line) {
			
			Line line = (Line) item;
			
			// Line has to be larger than the current raster
			if (line.getLength() >= editor.getRaster()) {
				// we do not allow double connections
				if (model.getCurrentLayer().getItems().contains(line)) {
					ErrorUtils.showErrorDialog(new IllegalStateException(resources.getResourceByKey("msg.connection.alreadyexists")));
					return;
				}
			}
			
			line.setStartType((LineEdgeType) shapesPanel.getStartLineCombo().getSelectedItem());
			line.setEndType((LineEdgeType) shapesPanel.getEndLineCombo().getSelectedItem());
						
			split = checkSplits(editor, line);
			
			line.setStroke((BasicStroke)shapesPanel.getThicknessCombo().getSelectedItem());
			
		}
		else if (item instanceof Shape) {
			Shape s = (Shape)item;
			s.setStroke((BasicStroke)shapesPanel.getThicknessCombo().getSelectedItem());
			s.setStyle((ShapeStyle) shapesPanel.getStyleCombo().getSelectedItem());
		}

		log.debug("Adding item to layer "+model.getCurrentLayer());
		
		model.getCurrentLayer().getItems().add(item);	
		removePending(model);

		if (!split) {
			if (!undoManager.addEdit(new AddItemEdit(item))) {
				log.info("Could not add edit "+this.getClass());
			}						
		}
		
		editor.setFileState(FileState.DIRTY);

		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());

	}

	private void removePending(BoardEditorModel model) {
		for (Layer layer : model.getLayers()) {
			for (Item removal : removals) {
				layer.getItems().remove(removal);
			}				
		}
		
		for (Item item : addables) {
			if (item instanceof Line) {
				Line l = (Line)item;
				// keep layer of splitted lines
				model.getLayer(l.getLayer()).getItems().add(l);
			}
			else {
				model.getCurrentLayer().getItems().add(item);	
			}
			
		}
	}

	/**
	 * <p>
	 * Check if the newly created line hits another one and
	 * place junction points if lines meet each other.
	 * </p>
	 * 
	 * @param editor
	 * @param line
	 * 
	 * @return true if a split occured
	 */
	private boolean checkSplits(BoardEditor editor, Line line) {
		
		boolean split = false;
		
		for (Layer layer : editor.getModel().getLayers()) {
			
			for (Item item : layer.getItems()) {
				if (item instanceof Line && !(item instanceof Resistor) && !(item instanceof Diode)) {
					
					Line l = (Line)item;
					
					if (l.containsPoint(line.getStart(),1)) {
						// place no junction if start or end points of line meet
						if (!l.getStart().equals(line.getStart()) && !l.getEnd().equals(line.getStart())) {
							line.setStartType(LineEdgeType.ROUND_DOT);									
							createSplit(editor,line, l, true);
							split = true;
							
						}															
					}
					if (l.containsPoint(line.getEnd(),1)) {
						// place no junction if start or end points of line meet
						if (!l.getStart().equals(line.getEnd()) && !l.getEnd().equals(line.getEnd())) {
							line.setEndType(LineEdgeType.ROUND_DOT);
							createSplit(editor,line, l, false);
							split = true;
						}
					}
					
				}
			}
			
		}
		return split;
	}

	/**
	 * Creates a split of an existing line, means that the old line is splitted 
	 * into two new lines at the point where the new line hits an exising one
	 * @param editor 
	 * 
	 * @param model The model to perform the split on
	 * @param line The new {@link Line}
	 * @param l the {@link Line} to be hit
	 */
	private void createSplit(BoardEditor editor, Line line, Line l, boolean atStart) {
		
		Point start = new Point(l.getStart());
		Point end   = new Point(l.getEnd());
		
		Point middle = null;
		
		if (atStart) {
			middle = new Point(line.getStart());			
		}
		else {
			middle = new Point(line.getEnd());
		}
		
		Line l1 = new Line(start, middle, EditorUtils.getFreeIndex(editor.getModel()));
		l1.setStroke(l.getStroke());
		l1.setColor(l.getColor());
		l1.setLayer(l.getLayer());
		l1.setStartType(l.getStartType());
		l1.setEndType(l.getEndType());
		
		Line l2 = new Line(new Point(middle), end, EditorUtils.getFreeIndex(editor.getModel()));
		l2.setStroke(l.getStroke());
		l2.setColor(l.getColor());
		l2.setLayer(l.getLayer());
		l2.setStartType(l.getStartType());
		l2.setEndType(l.getEndType());

		addables.add(l1);
		addables.add(l2);
		removals.add(l);
		
		if (!editor.getUndoManager().addEdit(new CreateSplitEdit(line, l, l1, l2))) {
			log.info("Could not add edit "+this.getClass());
		}
		
		AppContext.getBean(RedoCommand.class).setEnabled(editor.getUndoManager().canRedo());
		AppContext.getBean(UndoCommand.class).setEnabled(editor.getUndoManager().canUndo());

	}

	/**
	 * @param item the {@link Item} to be added
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	
	
	
}
