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
import javax.swing.KeyStroke;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.springframework.context.ApplicationContext;

/**
 * The {@link BrowsePartsCommand} opens a {@link PartDialog} in order
 * to browse for a bunch of parts to be added.
 * 
 * @author Matthias Pueski (13.06.2011)
 *
 */
public class BrowsePartsCommand extends AbstractEditorCommand {
	
	private static final long serialVersionUID = -7593914748336366674L;

	public BrowsePartsCommand() {
		putValue(Action.NAME, resources.getResourceByKey("BrowsePartsCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.browseparts"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("BrowsePartsCommand.description"));	
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
	}
	
// 	TODO : Display part view instead of dialog
// 	
//	public void execute(ActionEvent e) {		
//		PartView partView = AppContext.getContext().getBean(PartView.class);		
//		View view = new View(resources.getResourceByKey("BrowsePartsCommand.name"), resources.getIcon("icon.browseparts"), partView);
//		openEditor(view, partView.hashCode());		
//	}
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();

		PartDialog dlg = ctx.getBean(PartDialog.class);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL)
			return;

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (editor == null) {
			return;
		}
		
		int xLoc = 40;
		int yLoc = 40;

		for (Part part : dlg.getSelectedParts()) {

			// now get a new index		
			int max = 0;
			
			for (Layer layer : editor.getModel().getLayers()) {		
				for (Item item : layer.getItems()) {				
					if (item.getIndex() > max)
						max = item.getIndex();				
				}			
			}
			
			max++;				
			
			part.setXLoc(xLoc);
			part.setYLoc(yLoc);
			part.setOldXLoc(xLoc);
			part.setOldYLoc(yLoc);
			part.setOldWidth(part.getWidth());
			part.setOldHeight(part.getOldHeight());			
			
			part.setIndex(max);
			
			// Check if the default part layer exists
			
//			boolean onPartLayer = false;
//			
//			for (Layer layer : editor.getModel().getLayers()) {
//				if (layer.getIndex() == BoardEditorModel.PART_LAYER) {
//					part.setLayer(BoardEditorModel.PART_LAYER);
//					onPartLayer = true;
//					break;
//				}
//			}
//			
//			if (!onPartLayer) {
//				part.setLayer(editor.getModel().getCurrentLayer().getIndex());	
//			}
			
			xLoc += 64;
			yLoc += 64;
			
			/**
			 * Part has a defined designator. Thus we need to check if there are any
			 * parts sharing that designator and get the maximum index of that part
			 */
			
			if (part.getDesignator() != null && part.getDesignator().length() > 0) {
				part.setName(BoardUtil.getNextFreeDesignator(editor.getModel(), part.getDesignator()));
			}			
			else part.setName("");

			AddItemCommand cmd = new AddItemCommand();
			cmd.setItem(part);			
			cmd.execute(null);
			
			if (dlg.getSelectedParts().size() == 1) {
				editor.getSelectedItems().clear();
				editor.setSelectedItem(part);
			}
			else {
				editor.getSelectedItems().add(part);				
			}
			
		}
		
		// Switch to select mode after insert
		ctx.getBean(SetSelectModeCommand.class).execute(null);
		
		editor.setFileState(FileState.DIRTY);
		editor.refresh();
		editor.updateStatusBar();
	}

	@Override
	public void editorChanged(EditorChangedEvent event) {
	}
}
