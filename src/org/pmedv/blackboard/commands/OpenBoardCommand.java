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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.DockingUtil;
import net.infonode.gui.mouse.MouseButtonListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.divxdede.swing.busy.JBusyComponent;
import org.jdesktop.jxlayer.JXLayer;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.IOUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.CenterPanel;
import org.pmedv.core.commands.AbstractOpenEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.ui.transform.QualityHints;
import org.pmedv.core.ui.transform.TransformUtils;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;

/**
 * The <code>OpenBoardCommand</code> opens either a board by user selection, from a list 
 * of recent files or automatically at startup of the application.
 * 
 * @author Matthias Pueski
 *
 */
public class OpenBoardCommand extends AbstractOpenEditorCommand {
	
	private static final long serialVersionUID = -6482695154377933062L;
	private static final Log log = LogFactory.getLog(OpenBoardCommand.class);
	private String title;
	private View editorView;
	private File file;
	private boolean fromRecentFileList = false;

	private ArrayList<Runnable> postConfigurators = new ArrayList<Runnable>();

	public void addPostConfigurator(Runnable runner) {
		postConfigurators.add(runner);
	}
	
	public OpenBoardCommand() {
		this(resources.getResourceByKey("OpenBoardCommand.name"));
	}

	public OpenBoardCommand(String title) {
		this.title = title;
		putValue(Action.NAME, title);
		putValue(Action.SMALL_ICON, resources.getIcon("icon.open"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("OpenBoardCommand.description"));
	}

	public OpenBoardCommand(String title, File file) {
		this(title);
		this.file = file;
		fromRecentFileList = true;
	}

	@Override
	public void execute(ActionEvent e) {
		
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		
		// No file selected before, popup a dialog and query the user which file to open.
		if (file == null) {					
			String path = System.getProperty("user.home");					
			if (AppContext.getLastSelectedFolder() != null) {
				path = AppContext.getLastSelectedFolder();
			}					
			JFileChooser fc = new JFileChooser(path);
			fc.setDialogTitle(resources.getResourceByKey("OpenBoardCommand.name"));
			fc.setFileFilter(new FefaultFileFilter());
			int result = fc.showOpenDialog(win);
			if (result == JFileChooser.APPROVE_OPTION) {
				if (fc.getSelectedFile() == null)
					return;
				file = fc.getSelectedFile();
				AppContext.setLastSelectedFolder(file.getParentFile().getAbsolutePath());
			}
			else {
				return;
			}
		}

		final JWindow topWindow = new JWindow();
		topWindow.setSize(390,50);			
		topWindow.setLayout(new BorderLayout());
		topWindow.setBackground(Color.WHITE);
        
		final JPanel content = new JPanel(new BorderLayout());
		content.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(10,10,10,10)));
		content.setBackground(Color.WHITE);
		final JLabel infoLabel = new JLabel(resources.getResourceByKey("OpenBoardCommand.waitMsg")+ " "+file.getName());
		infoLabel.setVerticalAlignment(SwingConstants.CENTER);
		content.add(infoLabel, BorderLayout.SOUTH);
		
		final JBusyComponent<JPanel> busyPanel = new JBusyComponent<JPanel>(content);			
		busyPanel.setBusy(true);
	
		topWindow.getContentPane().add(busyPanel, BorderLayout.CENTER);			
		topWindow.setLocationRelativeTo(null);			
		topWindow.add(busyPanel, BorderLayout.CENTER);
		
		final SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

			@Override
			protected Boolean doInBackground() {
				topWindow.setVisible(true);
				doOpen();
				return Boolean.valueOf(true);
			}
			
			@Override
			protected void done() {
				topWindow.setVisible(false);
				topWindow.dispose();				
				for (Runnable r : postConfigurators) {
					SwingUtilities.invokeLater(r);
				}
			}
		};
		
		worker.execute();		
		
	}

	public void doOpen() {

		final ApplicationContext ctx = AppContext.getContext();		
		final ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
		
		BoardEditorModel model = null;
		
		String filename = file.getName().toLowerCase();
		
		try {
			if (filename.endsWith(".bb") || filename.endsWith(".bbs")) {
				File input;
				try {
					input = IOUtils.unpackBoard(file);
				}
				catch (Exception e1) {
					ErrorUtils.showErrorDialog(e1);
					return;
				}
				model = IOUtils.openBoard(input);						
			}
			else // legacy xml support
				model = IOUtils.openBoard(file);
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
			file = null;
			return;
		}
		if (model == null) {
			ErrorUtils.showErrorDialog(new Exception(resources.getResourceByKey("OpenBoardCommand.error")+" : "+file.getName()));
			file = null;
			return;
		}
		
		final BoardEditor editor = new BoardEditor(model);
		CenterPanel panel = new CenterPanel();
		JXLayer<?> zoomLayer = TransformUtils.createTransformJXLayer(editor, 1,new QualityHints());				
		panel.getCenterPanel().add(zoomLayer);				
		editor.setZoomLayer(zoomLayer);
		panel.setBoardEditor(editor);
		JScrollPane s = new JScrollPane(panel);
		s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editorView = new View(title, resources.getIcon("icon.editor"), s);
		editor.setView(editorView);
		editor.setCurrentFile(file);
		editor.setFileState(FileState.OPENED);

		EditorUtils.registerEditorListeners(editor);
				
		editorView.getViewProperties().setTitle(file.getName());
		try {
			IOUtils.updateRecentFiles(file.getAbsolutePath());
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		openEditor(editorView, editor.getCurrentFile().hashCode());
		if (!fromRecentFileList)
			file = null;
		editorView.addTabMouseButtonListener(new MouseButtonListener() {
			@Override
			public void mouseButtonEvent(MouseEvent e) {
				handleMouseEvent(e, editor);
			}
		});
		editor.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e, editor);
			}
		});
		editorView.addListener(new DockingWindowAdapter() {
			@Override
			public void windowClosing(DockingWindow arg0) throws OperationAbortedException {
				if (editor.getFileState().equals(FileState.DIRTY)) {
					int result = JOptionPane.showConfirmDialog(ctx.getBean(ApplicationWindow.class),
							resources.getResourceByKey("msg.warning.notsaved"),
							resources.getResourceByKey("msg.warning"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.NO_OPTION) {
						throw new OperationAbortedException("Aborted.");
					}
				}
				log.info("Removing view with index " + editor.getCurrentFile().hashCode() + " from perspective "
						+ advisor.getCurrentPerspective().ID);
				advisor.getCurrentPerspective().getViewMap().removeView(editor.getCurrentFile().hashCode());
				editor.notifyListeners(EventType.EDITOR_CLOSED);
			}
			@Override
			public void windowClosed(DockingWindow window) {
				editor.notifyListeners(EventType.EDITOR_CLOSED);
			}
			
		});
		editor.updateStatusBar();
		editor.notifyListeners(EventType.EDITOR_CHANGED);
		ctx.getBean(SetSelectModeCommand.class).execute(null);
	}

	private void handleMouseEvent(MouseEvent e, BoardEditor editor) {
		if (e.getID() == MouseEvent.MOUSE_PRESSED) {
			log.info(e);		
			TabWindow tw = DockingUtil.getTabWindowFor(editor.getView());
			advisor.setCurrentEditorArea(tw);
			EditorUtils.setToolbarButtonState(editor);
			ctx.getBean(ApplicationWindow.class).getRasterCombo().setSelectedItem(new Integer(editor.getRaster()));
			editor.updateStatusBar();
			editor.getView().requestFocus();	
			editor.notifyListeners(EventType.EDITOR_CHANGED);
		}
	}
	
	private static class FefaultFileFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			if ((f.getName().endsWith(".bb") || 
				 f.getName().endsWith(".BB") ||
				 f.getName().endsWith(".bbs") ||
				 f.getName().endsWith(".BBS"))) 
				return true;
			else
				return false;
		}

		@Override
		public String getDescription() {
			return resources.getResourceByKey("msg.filefilter.bbfiles")+"(*.bb *.BB *.bbs *.BBS)";
		}
	}
}
