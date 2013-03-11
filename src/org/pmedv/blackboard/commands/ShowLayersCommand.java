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
import java.awt.event.ActionListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.blackboard.panels.LayersPanel;
import org.pmedv.core.commands.AbstractOpenEditorCommand;
import org.pmedv.core.context.AppContext;

public class ShowLayersCommand extends AbstractOpenEditorCommand implements EditorChangedListener {

	private static final long serialVersionUID = -1204971682381964487L;
	private static final Log log = LogFactory.getLog(ShowLayersCommand.class);

	private BoardEditor editor = null;
	private LayersPanel layerPanel;

	public ShowLayersCommand() {
		layerPanel = new LayersPanel();
		initListeners();
	}

	private void initListeners() {
		layerPanel.getCurrentLayerCombo().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (editor != null) {
					Layer currentLayer = (Layer) layerPanel.getCurrentLayerCombo().getSelectedItem();

					log.info("setting current layer to : " + layerPanel.getCurrentLayerCombo().getSelectedItem());
					editor.getModel().setCurrentLayer(currentLayer);
					int opacity = (int) (editor.getModel().getCurrentLayer().getOpacity() * 100);
					layerPanel.getOpacitySlider().setValue(opacity);
				}
			}

		});

		layerPanel.getAddLayerButton().setAction(AppContext.getContext().getBean(AddLayerCommand.class));
		layerPanel.getRemoveLayerButton().setAction(AppContext.getContext().getBean(DeleteLayerCommand.class));
		
		layerPanel.getOpacitySlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				JSlider slider = (JSlider) e.getSource();
				float opacity = (float) slider.getValue() / 100.0f;
				editor.getModel().getCurrentLayer().setOpacity(opacity);
				editor.repaint();
				editor.setFileState(FileState.DIRTY);
			}

		});
	}

	@Override
	public void execute(ActionEvent e) {
	}

	@Override
	public void editorChanged(EditorChangedEvent event) {

		editor = event.getEditor();

		if (editor == null) {
			layerPanel.setLayers(null);
			return;
		}

		if (event.getEditor().getCurrentFile() != null)
			log.info(event.getEditor().getCurrentFile().getName());

		log.info(event.getEditor().getModel().getCurrentLayer());
		layerPanel.setLayers(event.getEditor().getModel().getLayers());
		layerPanel.getCurrentLayerCombo().setSelectedItem(event.getEditor().getModel().getCurrentLayer());

	}

	public LayersPanel getLayerPanel() {
		return layerPanel;
	}

}
