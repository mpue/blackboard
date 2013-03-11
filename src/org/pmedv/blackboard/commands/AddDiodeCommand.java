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
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Diode;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.dialogs.DiodeDialog;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

/**
 * This command adds a new {@link Diode} with a specified value to the board.
 * 
 * @author Matthias Pueski (29.05.2011)
 * 
 */
public class AddDiodeCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = 6153211504066921518L;
	private static final Log log = LogFactory.getLog(AddDiodeCommand.class);

	public AddDiodeCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddDiodeCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.diode"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddDiodeCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {
		ApplicationContext ctx = AppContext.getContext();
		ResourceService resourceService = ctx.getBean(ResourceService.class);
		String title = resources.getResourceByKey("AddDiodeCommand.name");
		String subTitle = resources.getResourceByKey("AddDiodeCommand.dialog.subtitle");
		ImageIcon icon = resourceService.getIcon("icon.dialog.diode");
		DiodeDialog dlg = new DiodeDialog(title, subTitle, icon, null);
		dlg.setVisible(true);
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL)
			return;
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		Diode diode = dlg.getDiode();

		// now get a new index
		int max = 0;
		for (Layer layer : editor.getModel().getLayers()) {
			for (Item item : layer.getItems()) {
				if (item.getIndex() > max)
					max = item.getIndex();
			}
		}
		max++;
		diode.setIndex(max);

		// Check if the default part layer exists

		boolean onPartLayer = false;

		for (Layer layer : editor.getModel().getLayers()) {
			if (layer.getIndex() == BoardEditorModel.PART_LAYER) {
				diode.setLayer(BoardEditorModel.PART_LAYER);
				onPartLayer = true;
				break;
			}
		}

		if (!onPartLayer) {
			diode.setLayer(editor.getModel().getCurrentLayer().getIndex());
		}

		editor.getModel().getLayer(diode.getLayer()).getItems().add(diode);
		UndoManager undoManager = editor.getUndoManager();
		if (!undoManager.addEdit(new AddDiodeEdit(diode))) {
			log.error("could not add edit to undo manager");
		}

		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());

		editor.setFileState(FileState.DIRTY);
		editor.updateStatusBar();
		editor.refresh();
	}
}
