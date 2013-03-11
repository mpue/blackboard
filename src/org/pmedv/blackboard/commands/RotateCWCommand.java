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
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Rotateable;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * This command rotates an {@link Item} counter-clockwise. 
 * </p>
 * <p>
 * If the item is a {@link Symbol}and it is broken up, the applied transform will be lost.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
public class RotateCWCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -1204971682381964487L;
	private static final Log log = LogFactory.getLog(RotateCCWCommand.class);
	
	public RotateCWCommand() {
		putValue(Action.NAME, resources.getResourceByKey("RotateCWCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.rotatecw"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("RotateCWCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent evt) {
		
		ApplicationContext ctx = AppContext.getContext();		
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		UndoManager undoManager = editor.getUndoManager();
		
		if (editor.getSelectedItem() != null) {
			if (editor.getSelectedItem() instanceof Rotateable) {
				
				if (editor.getSelectedItem() instanceof Part) {
//					BoardUtil.findConnectionsToItem(editor.getSelectedItem(), editor.getModel());
//					Part part = (Part)editor.getSelectedItem();				
//					if (editor.isMagnetic()) {
//						
//						for (WireConnection wc : part.getWireConnections()) {
//							
//							if (wc.getType().equals(WireConnectionType.START)) {							
//								Point s = BoardUtil.rotatePoint(wc.getLine().getStart().getX(), wc.getLine().getStart().getY(), part.getXLoc(), part.getYLoc(), -90);
//								wc.getLine().getStart().setLocation(BoardUtil.snap(s.x, editor.getRaster()),BoardUtil.snap(s.y, editor.getRaster()));							
//							}
//							else if (wc.getType().equals(WireConnectionType.END)) {
//								Point e = BoardUtil.rotatePoint(wc.getLine().getEnd().getX(), wc.getLine().getEnd().getY(), part.getXLoc(), part.getYLoc(), -90);
//								wc.getLine().getEnd().setLocation(BoardUtil.snap(e.x, editor.getRaster()),BoardUtil.snap(e.y, editor.getRaster()));														
//							}
//							
//						}
//						
//					}
//					
				}
				
				Rotateable r = (Rotateable) editor.getSelectedItem();
				r.rotateCW();				
								
				if (!undoManager.addEdit(new RotateCWEdit(r))) {
					log.info("Could not add edit "+this.getClass());
				}				
				editor.refresh();
			}
		}
		
		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
		
	}
	
}
