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
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.blackboard.panels.LayersPanel;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.components.CmdJButton;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.provider.ApplicationToolbarProvider;
import org.springframework.context.ApplicationContext;


/**
 * Toggles the visibility of the grid
 * 
 * @author Matthias Pueski (16.06.2011)
 *
 */
public class ToggleMirrorCommand extends AbstractCommand implements EditorChangedListener {

	private static final long serialVersionUID = -1928790760091970040L;
	
	private boolean mirror = false;
	
	public ToggleMirrorCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ToggleMirrorCommand.name"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ToggleMirrorCommand.description"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.fliphorizontal"));		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, 0));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {

		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		JXLayer<?> zoomLayer = editor.getZoomLayer();
		
		TransformUI ui = (TransformUI)(Object) zoomLayer.getUI();
		DefaultTransformModel model = (DefaultTransformModel) ui.getModel();
		
		mirror = !mirror;		
		model.setMirror(mirror);
					
		ApplicationContext ctx = AppContext.getContext();
		
		// TODO : Works only if layers are not being renamed
		
		Boolean autoHide = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.autoHideLayers");
		
		if (autoHide) {
			LayersPanel layerPanel = ctx.getBean(ShowLayersCommand.class).getLayerPanel();
			for (Layer layer : layerPanel.getLayerModel().getLayer()) {
				
				if (layer.getName().equalsIgnoreCase("Top")) {
					layer.setVisible(!mirror);				
				}
				else if (layer.getName().equalsIgnoreCase("Bottom")) {
					layer.setVisible(mirror);
				}			
			}
			
			layerPanel.getLayerModel().fireTableDataChanged();			
		}
		
		ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
		
		if (mirror) {			
			win.getViewLabel().setText(resources.getResourceByKey("BoardDesignerPerspective.bottomView"));
		}
		else {
			win.getViewLabel().setText(resources.getResourceByKey("BoardDesignerPerspective.topView"));
		}
		
		editor.refresh();
		setButtonSelected(editor);
	}
	
	@Override
	public void editorChanged(EditorChangedEvent event) {
		
		if (event.getEditor() != null) {
			JXLayer<?> zoomLayer = event.getEditor().getZoomLayer();			
			TransformUI ui = (TransformUI)(Object) zoomLayer.getUI();
			DefaultTransformModel model = (DefaultTransformModel) ui.getModel();			
			mirror = model.isMirror();					
		}
		
		setButtonSelected(event.getEditor());
		setEnabled(event.getEditor() != null);
	}

	void setButtonSelected(BoardEditor editor) {
		// now set border of button depending on state
		ApplicationToolbarProvider provider = AppContext.getContext().getBean(ApplicationToolbarProvider.class);
		JToolBar toolbar = provider.getToolbar();
		for (int i = 0; i < toolbar.getComponentCount(); i++) {			
			if (toolbar.getComponent(i) instanceof CmdJButton) {
				CmdJButton button = (CmdJButton) toolbar.getComponent(i);
				if (button.getAction() instanceof ToggleMirrorCommand) {
					if (editor == null) {
						button.setBorderPainted(false);
					}
					else {
						button.setBorderPainted(mirror);						
					}					
				}
			}
		}						
	}	

}
