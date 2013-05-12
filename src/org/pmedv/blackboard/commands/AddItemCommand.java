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

import javax.swing.JOptionPane;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Box;
import org.pmedv.blackboard.components.Ellipse;
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
 * The <code>AddItemCommand</code> adds a new {@link Item} to the {@link BoardEditor}
 * 
 * @author Matthias Pueski (08.06.2011)
 *
 */
public class AddItemCommand extends AbstractCommand {

	private static final long serialVersionUID = 2473298284426159552L;

	private static final Log log = LogFactory.getLog(AddItemCommand.class);
	
	private Item item = null;
		
	private static final List<Item> removals = new LinkedList<Item>();
	private static final List<Item> addables = new LinkedList<Item>();
	
	@Override
	public void execute(ActionEvent e) {
		
		if (item == null)
			return;

		removals.clear();
		addables.clear();
		
		ApplicationContext ctx = AppContext.getContext();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		BoardEditorModel model = editor.getModel();
		
		final ShapePropertiesPanel shapesPanel = ctx.getBean(ShapePropertiesPanel.class);
		item.setLayer(model.getCurrentLayer().getIndex());	
		
		Boolean useLayerColor = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor");
		
		if (useLayerColor) {
			item.setColor(model.getCurrentLayer().getColor());					
		}
		else {
			item.setColor(ctx.getBean(Palette.class).getCurrentColor());	
		}
		
		int maxIndex = getFreeIndex(model);
		item.setIndex(maxIndex);	
		
		if (item instanceof Line) {
			
			Line line = (Line) item;
			
			// Line has to be larger than the current raster
			if (line.getLength() >= editor.getRaster()) {
				// we do not allow double connections
				if (model.getCurrentLayer().getItems().contains(line)) {
					JOptionPane.showMessageDialog(AppContext.getContext().getBean(ApplicationWindow.class), resources.getResourceByKey("msg.connection.alreadyexists"));
					return;
				}
			}
			
			line.setStartType((LineEdgeType) shapesPanel.getStartLineCombo().getSelectedItem());
			line.setEndType((LineEdgeType) shapesPanel.getEndLineCombo().getSelectedItem());
			
			// Place junction points if lines meet each other
			
			for (Layer layer : model.getLayers()) {
				
				for (Item item : layer.getItems()) {
					if (item instanceof Line) {
						
						Line l = (Line)item;
						
						if (l.containsPoint(line.getStart(),1)) {
							// place no junction if start or end points of line meet
							if (!l.getStart().equals(line.getStart()) && !l.getEnd().equals(line.getStart())) {
								line.setStartType(LineEdgeType.ROUND_DOT);									
								createSplit(model, line, l, true);							
							}															
						}
						if (l.containsPoint(line.getEnd(),1)) {
							// place no junction if start or end points of line meet
							if (!l.getStart().equals(line.getEnd()) && !l.getEnd().equals(line.getEnd())) {
								line.setEndType(LineEdgeType.ROUND_DOT);
								createSplit(model, line, l, false);
							}
						}
						
					}
				}
				
			}
			
			line.setStroke((BasicStroke)shapesPanel.getThicknessCombo().getSelectedItem());
			
		}
		else if (item instanceof Box) {
			Box box = (Box)item;
			box.setStroke((BasicStroke)shapesPanel.getThicknessCombo().getSelectedItem());
			box.setStyle((ShapeStyle) shapesPanel.getStyleCombo().getSelectedItem());
		}
		else if (item instanceof Ellipse) {
			Ellipse ellipse = (Ellipse)item;
			ellipse.setStroke((BasicStroke)shapesPanel.getThicknessCombo().getSelectedItem());
			ellipse.setStyle((ShapeStyle) shapesPanel.getStyleCombo().getSelectedItem());			
		}

		log.info("Adding item to layer "+model.getCurrentLayer());
		model.getCurrentLayer().getItems().add(item);
		
		// remove pending items
		
		for (Layer layer : model.getLayers()) {
			for (Item removal : removals) {
				layer.getItems().remove(removal);
			}				
		}
		
		for (Item item : addables) {
			model.getCurrentLayer().getItems().add(item);
		}
		
		UndoManager undoManager = editor.getUndoManager();				
		if (!undoManager.addEdit(new AddItemEdit(item))) {
			log.info("Could not add edit "+this.getClass());
		}
		editor.setFileState(FileState.DIRTY);

		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());

	}

	/**
	 * Creates a split of an existing line, means that the old line is splitted 
	 * into two new lines at the point where the new line hits an exising one
	 * 
	 * @param model The model to perform the split on
	 * @param line The new {@link Line}
	 * @param l the {@link Line} to be hit
	 */
	private void createSplit(BoardEditorModel model, Line line, Line l, boolean atStart) {
		Point start = new Point(l.getStart());
		Point end   = new Point(l.getEnd());
		
		Point middle = null;
		
		if (atStart) {
			middle = new Point(line.getStart());			
		}
		else {
			middle = new Point(line.getEnd());
		}
		
		Line l1 = new Line(start, middle, getFreeIndex(model));
		l1.setStroke(l.getStroke());
		l1.setColor(line.getColor());

		Line l2 = new Line(new Point(middle), end, getFreeIndex(model));
		l2.setStroke(l.getStroke());
		l2.setColor(line.getColor());

		// model.getCurrentLayer().getItems().add(l1);
		// model.getCurrentLayer().getItems().add(l2);
		addables.add(l1);
		addables.add(l2);
		removals.add(l);
	}


	private int getFreeIndex(BoardEditorModel model) {
		/**
		 * Determine next free index
		 */
		int maxIndex = 0;
		for (Layer layer : model.getLayers()) {
			for (Item item : layer.getItems()) {
				if (item.getIndex() > maxIndex) {
					maxIndex = item.getIndex();
				}
			}
		}
		maxIndex++;
		return maxIndex;
	}


	/**
	 * @param item the {@link Item} to be added
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	
	
	
}
