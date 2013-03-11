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
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.Action;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.printing.ImagePrintable;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.util.ErrorUtils;
import org.pmedv.core.util.ImageUtils;

public class PrintBoardCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = -4060436618149711784L;
	
	public PrintBoardCommand() {
		putValue(Action.NAME, resources.getResourceByKey("PrintBoardCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.print"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("PrintBoardCommand.description"));
		setEnabled(false);
	}	
	
	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		BufferedImage image = ImageUtils.createImageFromComponent(editor);

		PrinterJob job = PrinterJob.getPrinterJob();
		ImagePrintable printable = new ImagePrintable(image);		
		job.setPrintable(printable);
		
		boolean doPrint = job.printDialog();

		if (doPrint) {

			try {
				job.print();
			}
			catch (PrinterException e1) {
				ErrorUtils.showErrorDialog(e1);
			}

		}

	}

}
