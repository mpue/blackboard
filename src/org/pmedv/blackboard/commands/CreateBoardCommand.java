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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.DockingUtil;
import net.infonode.gui.mouse.MouseButtonListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.jxlayer.JXLayer;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.IOUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.dialogs.BoardPropertiesDialog;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.CenterPanel;
import org.pmedv.core.commands.AbstractOpenEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.ui.transform.QualityHints;
import org.pmedv.core.ui.transform.TransformUtils;
import org.springframework.context.ApplicationContext;

/**
 * This command opens a new board editor inside a new tab window.
 * 
 * @author Matthias Pueski
 */
public class CreateBoardCommand extends AbstractOpenEditorCommand {

	private static final long serialVersionUID = 9098673573001624609L;

	private static final Log log = LogFactory.getLog(CreateBoardCommand.class);	
		
	@SuppressWarnings("unused")
	private String title;

	private View editorView;

	public CreateBoardCommand() {
		title = "untitled";
		putValue(Action.NAME, resources.getResourceByKey("CreateBoardCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.new"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("CreateBoardCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
	}

	public CreateBoardCommand(String title) {
		this.title = title;
	}

	@Override
	public void execute(ActionEvent e) {

		final ApplicationContext ctx = AppContext.getContext();			
		final ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		
		/*
		 * Get the resource service
		 */

		final ResourceService resources = ctx.getBean(ResourceService.class);

		/*
		 * The infonode docking framework must be invoked later since swing is
		 * not thread safe.
		 */

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				BoardEditorModel model = new BoardEditorModel();
				model.addDefaultLayers();
				model.setWidth(IOUtils.BOARD_DEFAULT_WIDTH);
				model.setHeight(IOUtils.BOARD_DEFAULT_HEIGHT);
				
				String title = resources.getResourceByKey("CreateBoardCommand.name");
				String subTitle = resources.getResourceByKey("CreateBoardCommand.dialog.subtitle");
				
				BoardPropertiesDialog bpd = new BoardPropertiesDialog(title,subTitle,resources.getIcon("icon.dialog.board"),model);
				bpd.setVisible(true);
				
				if (bpd.getResult() == AbstractNiceDialog.OPTION_CANCEL)
					return;
				
				final BoardEditor editor = new BoardEditor(model);
				
				// the center panel centers the editor inside the view no matter which dimensions it has
				CenterPanel panel = new CenterPanel();
				// go for the zoom
				JXLayer<?> zoomLayer = TransformUtils.createTransformJXLayer(editor, 1,new QualityHints());				
				panel.getCenterPanel().add(zoomLayer);				
				editor.setZoomLayer(zoomLayer);
				panel.setBoardEditor(editor);
				// put the panel into a scroll pane
				JScrollPane s = new JScrollPane(panel);
				s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				s.getVerticalScrollBar().setUnitIncrement(512);
				// create new view and connect the view with the editor
				editorView = new View(title, resources.getIcon("icon.editor"), s);
				editor.setView(editorView);
				editor.setFileState(FileState.NEW_AND_UNSAVED);
				editor.setCurrentFile(null);				
				// check all available open views
				ArrayList<View> views = EditorUtils.getCurrentPerspectiveViews(advisor.getCurrentPerspective());
				
				// we need this index for a new untitled view
				int lastUntitledIndex = 0;
				
				for (View v : views) {
					// found a view with an untitled editor
					if (v.getViewProperties().getTitle().startsWith("untitled")) {
						// select index if it's greater than the last
						int index = 0;
						
						if (v.getViewProperties().getTitle().contains("*")) {
							int end = v.getViewProperties().getTitle().lastIndexOf("*");
							index = Integer.valueOf(v.getViewProperties().getTitle().substring(8,end));
						}
						else {
							index = Integer.valueOf(v.getViewProperties().getTitle().substring(8));
						}
						
						
						if (index > lastUntitledIndex)
							lastUntitledIndex = index;
					}
				}
				// and finally add one in order to get the right name
				lastUntitledIndex++;
				
				editor.getView().getViewProperties().setTitle("untitled"+lastUntitledIndex);
				final int index = editor.getView().hashCode();

				EditorUtils.registerEditorListeners(editor);
				
				openEditor(editorView,index);

				log.info("Opening editor : " + title);

				// notifies the GUI about the editor change if a tab is switched by the mouse
				editorView.addTabMouseButtonListener(new MouseButtonListener() {

					@Override
					public void mouseButtonEvent(MouseEvent e) {
						handleMouseEvent(e, editor);
					}

				});
				// wee need to know if a mouse click inside an editor occurs
				editor.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						handleMouseEvent(e, editor);
					}
					
				});
				// This listener handles the close of an editor tab
				editorView.addListener(new DockingWindowAdapter() {

					@Override
					public void windowClosing(DockingWindow arg0) throws OperationAbortedException {

						if (editor != null)
							
							if (editor.getFileState().equals(FileState.DIRTY)) {
								int result = JOptionPane.showConfirmDialog(win,
										resources.getResourceByKey("msg.warning.notsaved"), 
										resources.getResourceByKey("msg.warning"), JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE);

								if (result == JOptionPane.NO_OPTION) {
									throw new OperationAbortedException("Aborted.");
								}
							}

						advisor.getCurrentPerspective().getViewMap().removeView(index);
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
		});
		
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
	
}
