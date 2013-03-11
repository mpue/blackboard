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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import net.infonode.docking.View;

import org.apache.commons.io.FileUtils;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.core.commands.AbstractOpenEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;

/**
 * The {@link CreatePartListCommand} creates a CSV file
 * from all parts of the current selected {@link BoardEditor}.
 * 
 * @author Matthias Pueski (13.06.2011)
 * 
 */
public class CreatePartListCommand extends AbstractOpenEditorCommand implements EditorChangedListener{
	
	private static final long serialVersionUID = -2617325756383421753L;

	public CreatePartListCommand() {
		putValue(Action.NAME, resources.getResourceByKey("CreatePartListCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.partlist"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("CreatePartListCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X,  InputEvent.ALT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {
		ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		StringBuffer partList = new StringBuffer();
		partList.append("Name,Value,Package\n");
		for (Layer layer : editor.getModel().getLayers()) {
			for (Item i : layer.getItems()) {
				if (i instanceof Part && !(i instanceof TextPart)) {
					Part p = (Part) i;
					partList.append(p.getName() + "," + p.getValue() + "," + p.getPackageType() + "\n");
				}
				if (i instanceof Resistor) {
					Resistor r = (Resistor) i;
					partList.append(r.getName() + "," + r.getValue() + " Ohm,RESISTOR\n");
				}
			}
		}
		final JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new CSVFilter());
		fc.setDialogTitle(resources.getResourceByKey("CreatePartListCommand.dialog.subtitle"));
		fc.setApproveButtonText(resources.getResourceByKey("msg.save"));
		int result = fc.showOpenDialog(win);
		if (result == JFileChooser.APPROVE_OPTION) {
			if (fc.getSelectedFile() == null)
				return;
			File selectedFile = fc.getSelectedFile();
			if (selectedFile != null) {
				// if csv extension is missing...
				if (!selectedFile.getName().endsWith(".csv")) {
					selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
				}
				try {
					FileUtils.writeStringToFile(selectedFile, partList.toString(), "UTF-8");
					int res = JOptionPane.showConfirmDialog(win, resources.getResourceByKey("msg.viewpartlist"), 
							resources.getResourceByKey("msg.question"),
							JOptionPane.YES_NO_OPTION);
					if (res == JOptionPane.YES_OPTION) {
						JTextArea textPane = new JTextArea();
						textPane.setText(partList.toString());
						JScrollPane s = new JScrollPane(textPane);
						s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						View v = new View("Partlist for " + editor.getCurrentFile().getName(), null, s);
						editor.setView(v);
						final ImageIcon icon = resources.getIcon("icon.document.html");
						v.getViewProperties().setIcon(icon);
						v.getViewProperties().setTitle("Partlist for " + editor.getCurrentFile().getName());
						openEditor(v, editor.getCurrentFile().hashCode());
						v.undock(new Point (20,10));
					}
				}
				catch (IOException e1) {
					ErrorUtils.showErrorDialog(e1);
				}
			}
		}
	}

	private static class CSVFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			if ((f.getName().endsWith(".csv")) || f.getName().endsWith(".CSV"))
				return true;
			else
				return false;
		}

		@Override
		public String getDescription() {
			return resources.getResourceByKey("msg.filefilter.csvfiles")+"(*.csv *.CSV)";
		}
	}
	
	@Override
	public void editorChanged(EditorChangedEvent event) {
		setEnabled(event.getEditor() != null);			
	}
}
