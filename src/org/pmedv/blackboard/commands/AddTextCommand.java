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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.dialogs.TextPropertiesDialog;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.springframework.context.ApplicationContext;

/**
 * This command adds a new {@link Resistor} with a specified value to the board.
 * 
 * @author Matthias Pueski (29.05.2011)
 *
 */
public class AddTextCommand extends AbstractEditorCommand implements MouseMotionListener {

	private static final long serialVersionUID = 6153211504066921518L;
	private static final Log log = LogFactory.getLog(AddTextCommand.class);
	
	private int lastFontSize  = 12;
	private Font lastSelectedFont = new Font("Courier",Font.PLAIN,lastFontSize);
	
	int mouseX;
	int mouseY;
	
	public AddTextCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddTextCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.addtext"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddTextCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		final ApplicationContext ctx = AppContext.getContext();
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		final String title = resources.getResourceByKey("AddTextCommand.name");
		final String subTitle = resources.getResourceByKey("AddTextCommand.dialog.subtitle");
		final ImageIcon icon = resources.getIcon("icon.dialog.text");
		
		int x = editor.getModel().getWidth()/2;		
		int y = editor.getModel().getHeight()/2; 
		
		if (null != e && e.getSource().getClass().getName().startsWith("javax.swing.JPopupMenu")) {
			x = mouseX;
			y = mouseY;			
		}
		
		final TextPart p = new TextPart(resources.getResourceByKey("AddTextCommand.defaultText"),lastSelectedFont, Color.BLACK,x,y,0);
		
		final TextPropertiesDialog dlg = new TextPropertiesDialog(title,subTitle,icon,p);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL)
			return;

		// preserve font properties
		lastSelectedFont = p.getFont();
		lastFontSize = dlg.getFontSize();
		
		// now get a new index		
		int max = 0;
		
		for (Layer layer : editor.getModel().getLayers()) {		
			for (Item item : layer.getItems()) {				
				if (item.getIndex() > max)
					max = item.getIndex();				
			}			
		}
		
		max++;		
		p.setIndex(max);
		
		final Layer currentLayer = (Layer)ctx.getBean(ShowLayersCommand.class).getLayerPanel().getCurrentLayerCombo().getSelectedItem();		
		editor.getModel().getLayer(currentLayer.getIndex()).getItems().add(p);
		
		final UndoManager undoManager = editor.getUndoManager();
		
		if (!undoManager.addEdit(new AddTextEdit(p))) {
			log.error("could not add edit to undo manager");
		}
		
		editor.setFileState(FileState.DIRTY);
		editor.updateStatusBar();

		ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
		ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();		
	}
	

}
