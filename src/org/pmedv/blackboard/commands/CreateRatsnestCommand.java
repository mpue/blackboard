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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.blackboard.tools.StrokeFactory;
import org.pmedv.blackboard.tools.StrokeFactory.StrokeType;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ImageUtils;
import org.springframework.context.ApplicationContext;


/**
 * The <code>CreateRatsnestCommand</code> creates a solder ratsnest for a specific layer.
 * 
 * @author Matthias Pueski (11.11.2012)
 *
 */
public class CreateRatsnestCommand extends AbstractCommand implements EditorChangedListener {
	
	public CreateRatsnestCommand() {
		putValue(Action.NAME, resources.getResourceByKey("CreateRatsnestCommand.name"));
		// putValue(Action.SMALL_ICON,resources.getIcon("icon.ratsnest"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("CreateRatsnestCommand.description"));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {

		ApplicationContext ctx = AppContext.getContext();		
		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		final String selectMsg = resources.getResourceByKey("CreateRatsnestCommand.selectlayer");
		
		if (editor == null)
			return;
		
		// first select the layer to paint the ratsnest for
		
		Object[] layers = editor.getModel().getLayers().toArray();
		
		Layer layer = (Layer) JOptionPane.showInputDialog(ctx.getBean(ApplicationWindow.class), selectMsg,
				selectMsg, JOptionPane.PLAIN_MESSAGE, null, layers,layers[0]);

		// create a new image		
		BufferedImage image = new BufferedImage(editor.getWidth(), editor.getHeight(), BufferedImage.TYPE_INT_ARGB);		
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		// antialiasing
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		
		// set the background white
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0,editor.getWidth(), editor.getHeight());		
		
		g2d.setStroke(StrokeFactory.createStroke(8.0f, StrokeType.BASIC));
		g2d.setColor(Color.BLACK);
		
		// and now draw the lines 
		for (Item item : layer.getItems()) {			
			if (item instanceof Line) {
				item.draw(g2d);
			}
		}
		
		image = ImageUtils.invert(image);
		
		int layerIndex = 0;

		for (Layer l : editor.getModel().getLayers()) {

			if (l.getIndex() > layerIndex)
				layerIndex = l.getIndex();

		}

		layerIndex++;

		Layer ratsnest = new Layer(layerIndex, "Ratsnest");
		ratsnest.setImage(image);
		editor.getModel().getLayers().add(ratsnest);	
		editor.notifyListeners(EventType.EDITOR_CHANGED);
		editor.setFileState(FileState.DIRTY);
		editor.updateStatusBar();
		
	}

	@Override
	public void editorChanged(EditorChangedEvent event) {
		setEnabled(event.getEditor() != null);			
	}
	
}
