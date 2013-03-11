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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Palette;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.springframework.context.ApplicationContext;

/**
 * The {@link SetColorCommand} allows the user to select a color for selected lines
 * or for current drawing color.
 * 
 * @author Matthias Pueski (13.06.2011)
 * 
 */
public class SetColorCommand extends AbstractEditorCommand {
	
	private static final long serialVersionUID = -2617325756383421753L;
	private boolean selectDrawingColor;
	
	public SetColorCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SetColorCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.color"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SetColorCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C,  InputEvent.ALT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	private JDialog dialog = null;

	@Override
	public void execute(ActionEvent e) {
		
		ApplicationContext ctx = AppContext.getContext();
		final Palette palette = ctx.getBean(Palette.class);
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		final String title = resources.getResourceByKey("SetColorCommand.dialog.subtitle");
		
		selectDrawingColor = false;
		
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (editor == null || (editor.getSelectedItem() == null && editor.getSelectedItems().size() == 0)) {			
			selectDrawingColor = true;
		}
		
		final JColorChooser chooser = new JColorChooser();
		
		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (selectDrawingColor) {
					palette.setCurrentColor(chooser.getColor());
					BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
					Graphics g = image.getGraphics();
					g.setColor(palette.getCurrentColor());
					g.fillRect(0, 0, 16, 16);
					win.getStatusLabel().setIcon(new ImageIcon(image));					
					return;
				}
				
				if (editor.getSelectedItem() != null) {
					editor.getSelectedItem().setColor(chooser.getColor());
					editor.setFileState(FileState.DIRTY);
				}
				else {
					for (Item i : editor.getSelectedItems()) {
						i.setColor(chooser.getColor());
					}
					editor.setFileState(FileState.DIRTY);
				}
				dialog.setVisible(false);
				editor.updateStatusBar();
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
