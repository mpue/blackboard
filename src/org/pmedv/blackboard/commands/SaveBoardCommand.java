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

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.IOUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.beans.BoardBean;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;

/**
 * This command saves the {@link BoardEditorModel} in a {@link BoardBean}
 * to disk. This command acts in two ways: If the current file being edited
 * is a fresh file, the user is asked for a name. Otherwise the board is
 * saved with its current name.
 * 
 * @author Matthias Pueski (09.05.2011)
 *
 */
public class SaveBoardCommand extends AbstractEditorCommand {

	private static final long serialVersionUID = 3337788749872851956L;

	private BoardEditor boardEditor = null;
	
	public SaveBoardCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SaveBoardCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.save"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SaveBoardCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}	
	
	public SaveBoardCommand(BoardEditor editor) {
		this.boardEditor = editor;
	}
	
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();
		ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		
		View view = (View) advisor.getCurrentEditorArea().getSelectedWindow();
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (boardEditor != null) {
			editor = boardEditor;
		}
		
		// new file, which has never been saved
		if (editor.getCurrentFile() == null) {

			String path = System.getProperty("user.home");					
			if (AppContext.getLastSelectedFolder() != null) {
				path = AppContext.getLastSelectedFolder();
			}					
			
			final JFileChooser fc = new JFileChooser(path);
			
			String extension = null;
			
			if (editor.getModel().getType().equals(BoardType.SCHEMATICS)) {
				fc.setFileFilter(new SchematicsFilter());
				extension = ".bbs";
			}
			else {
				fc.setFileFilter(new PerfboardFilter());
				extension = ".bb";
			}
			
			fc.setDialogTitle(resources.getResourceByKey("SaveBoardCommand.dialog.subtitle")+" : "+editor.getView().getViewProperties().getTitle());
			fc.setApproveButtonText(resources.getResourceByKey("msg.save"));
			
			int result = fc.showOpenDialog(win);

			if (result == JFileChooser.APPROVE_OPTION) {

				if (fc.getSelectedFile() == null)
					return;

				File selectedFile = fc.getSelectedFile();
				
				AppContext.setLastSelectedFolder(selectedFile.getParentFile().getAbsolutePath());
				
				if (selectedFile.exists()) {
					 result = JOptionPane.showConfirmDialog(win,
							 resources.getResourceByKey("msg.warning.fileexists"),
							 resources.getResourceByKey("msg.warning"), JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.NO_OPTION) {
						return;
					}
				}
				
				if (selectedFile != null) {				
					
					// if extension is missing...
					if (!selectedFile.getName().endsWith(extension)) {
						selectedFile = new File(selectedFile.getAbsolutePath() + extension);
					}					
					try {
						IOUtils.savePackedBoard(selectedFile, editor.getModel());
					}
					catch (IOException e1) {
						ErrorUtils.showErrorDialog(e1);
					}
					try {
						IOUtils.updateRecentFiles(selectedFile.getAbsolutePath());
					}
					catch (Exception e1) {
						ErrorUtils.showErrorDialog(e1);
					}
					editor.setCurrentFile(selectedFile);									
					editor.setFileState(FileState.SAVED);
					
					if (view != null) {
						ViewMap viewMap = advisor.getCurrentPerspective().getViewMap();					
						viewMap.removeView(editor.getView().hashCode());
						viewMap.addView(editor.getCurrentFile().hashCode(),view);						
					}
					
				}
				
			}
			else {
				// user cancelled Save As, we need to set the current file back to the editor
				editor.setCurrentFile(AppContext.getContext().getBean(SaveAsCommand.class).getCurrentFile());
			}
			
		}
		// existing file, check the dirty flag and save if necessary
		if  (editor.getFileState().equals(FileState.DIRTY)) { 
			try {
				IOUtils.savePackedBoard(editor.getCurrentFile(), editor.getModel());
			}
			catch (IOException e1) {
				ErrorUtils.showErrorDialog(e1);
			}
			editor.setFileState(FileState.SAVED);	
			editor.updateStatusBar();
		}
		
	}
	
	private static class PerfboardFilter extends FileFilter {

		@Override
		public boolean accept(File f) {

			if (f.isDirectory())
				return true;
			if ((f.getName().endsWith(".bb")) || f.getName().endsWith(".BB"))
				return true;
			else
				return false;
		}

		@Override
		public String getDescription() {
			return resources.getResourceByKey("msg.filefilter.bbfiles")+"(*.bb *.BB)";
		}

	}

	private static class SchematicsFilter extends FileFilter {

		@Override
		public boolean accept(File f) {

			if (f.isDirectory())
				return true;
			if ((f.getName().endsWith(".bbs")) || f.getName().endsWith(".BBS"))
				return true;
			else
				return false;
		}

		@Override
		public String getDescription() {
			return resources.getResourceByKey("msg.filefilter.bbsfiles")+"(*.bbs *.BBS)";
		}

	}
}
