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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.dialogs.ExportImageDialog;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

/**
 * This command exports the current selected board as a PNG image.
 * 
 * @author Matthias Pueski
 *
 */
public class ExportImageCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = 7465440891087438543L;
	private static final Log log = LogFactory.getLog(ExportImageCommand.class);
	
	public ExportImageCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ExportImageCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.export"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ExportImageCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (editor == null) return;
		
		String title = resources.getResourceByKey("ExportImageCommand.name");
		String subtitle = resources.getResourceByKey("ExportImageCommand.dialog.subtitle");
		
		ExportImageDialog dlg = new ExportImageDialog(title, subtitle, resources.getIcon("icon.dialog.exportimage"));
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL)
			return;
		
		String name = dlg.getSelectedPath();
		
		AppContext.setLastSelectedFolder(dlg.getSelectedPath());
		
		if (!(name.endsWith(".png") || name.endsWith(".PNG")))
			name += ".png";	
				
		File f = new File(name);
		
		log.info("Exporting image to "+f.getAbsolutePath());
		
		try {
			ImageIO.write(dlg.getImage(), "PNG", f);
		}
		catch (IOException e1) {
			ErrorUtils.showErrorDialog(e1);
		}

	}

}
