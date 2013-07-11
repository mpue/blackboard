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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Diode;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.dialogs.BoardPropertiesDialog;
import org.pmedv.blackboard.dialogs.DiodeDialog;
import org.pmedv.blackboard.dialogs.PartPropertiesDialog;
import org.pmedv.blackboard.dialogs.ResistorDialog;
import org.pmedv.blackboard.dialogs.TextPropertiesDialog;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class EditPropertiesCommand extends AbstractEditorCommand {
	
	private static final long serialVersionUID = -1204971682381964487L;

	public EditPropertiesCommand() {
		putValue(Action.NAME, resources.getResourceByKey("EditPropertiesCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.properties"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("EditPropertiesCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
		
		AbstractNiceDialog dlg = null;
		
		if (editor.getSelectedItems().size() > 1) {
			return;
		}
		else {
			if (editor.getSelectedItem() == null) {
				String title = resources.getResourceByKey("EditPropertiesCommand.board.title");
				String subTitle = resources.getResourceByKey("EditPropertiesCommand.board.subtitle");
				
				BoardPropertiesDialog bpd = new BoardPropertiesDialog(title,subTitle,resources.getIcon("icon.dialog.board"),editor.getModel());
				bpd.setVisible(true);
				
				if (bpd.getResult() == AbstractNiceDialog.OPTION_CANCEL)
					return;
				
				Dimension size = new Dimension(editor.getModel().getWidth(), editor.getModel().getHeight());
				editor.setSize(size);
				editor.setBounds(new Rectangle(size));
				editor.setPreferredSize(size);				
				editor.setBackgroundImage(editor.getModel().getBackgroundImage());
				editor.notifyListeners(EventType.EDITOR_CHANGED);
				editor.repaint();
			}
				
			if (editor.getSelectedItem() instanceof TextPart) {
				TextPart text = (TextPart) editor.getSelectedItem();
				String title = resources.getResourceByKey("EditPropertiesCommand.text.title");
				String subTitle = resources.getResourceByKey("EditPropertiesCommand.text.subtitle");
				ImageIcon icon = resources.getIcon("icon.dialog.text");
				dlg = new TextPropertiesDialog(title, subTitle, icon, text);
				dlg.setVisible(true);				
			}
			else if (editor.getSelectedItem() instanceof Resistor) {
				Resistor resistor = (Resistor)editor.getSelectedItem();
				String title = resources.getResourceByKey("EditPropertiesCommand.resistor.title");
				String subTitle = resources.getResourceByKey("EditPropertiesCommand.resistor.subtitle");
				ImageIcon icon = resources.getIcon("icon.dialog.resistor");				
				dlg = new ResistorDialog(title, subTitle, icon, resistor);
				dlg.setVisible(true);				
			}
			else if (editor.getSelectedItem() instanceof Diode) {
				Diode diode = (Diode)editor.getSelectedItem();
				String title = resources.getResourceByKey("EditPropertiesCommand.diode.title");
				String subTitle = resources.getResourceByKey("EditPropertiesCommand.diode.subtitle");
				ImageIcon icon = resources.getIcon("icon.dialog.resistor");				
				dlg = new DiodeDialog(title, subTitle, icon, diode);
				dlg.setVisible(true);				
			}
			else if(editor.getSelectedItem() instanceof Symbol) {
				Symbol symbol = (Symbol)editor.getSelectedItem();
				AppContext.getContext().getBean(EditSymbolCommand.class).setSymbol(symbol);
				AppContext.getContext().getBean(EditSymbolCommand.class).setStoreBean(false);
				AppContext.getContext().getBean(EditSymbolCommand.class).execute(null);
				
			}
			else if (editor.getSelectedItem() instanceof Part) {
				Part part = (Part)editor.getSelectedItem();
				String title = resources.getResourceByKey("EditPropertiesCommand.part.title");
				String subTitle = resources.getResourceByKey("EditPropertiesCommand.part.subtitle");
				ImageIcon icon = resources.getIcon("icon.dialog.part");				
				dlg = new PartPropertiesDialog(title, subTitle, icon, part, win);
				dlg.setVisible(true);				
			}
			
			editor.refresh();
			
		}
		
		if (dlg != null && dlg.getResult() == AbstractNiceDialog.OPTION_OK) {
			editor.setFileState(FileState.DIRTY);			
		}
		
	}
	
}
