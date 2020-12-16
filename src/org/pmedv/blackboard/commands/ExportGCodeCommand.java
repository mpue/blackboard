package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.core.commands.AbstractOpenEditorCommand;


import net.infonode.docking.View;

public class ExportGCodeCommand extends AbstractOpenEditorCommand{

	public ExportGCodeCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ExportGCodeCommand.name"));		
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ExportGCodeCommand.description"));
	}
	
	
	@Override
	public void execute(ActionEvent e) {
 		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		if (editor != null) {

			StringBuffer data = new StringBuffer();
			
			RSyntaxTextArea textArea = new RSyntaxTextArea();
			textArea.getPopupMenu().addSeparator();
			
			ArrayList<Layer> layers = editor.getModel().getLayers();
			
			for (Layer layer : layers) {
				ArrayList<Item> items = layer.getItems();
				
				for (Item item : items)  {
					if (item instanceof Line) {
						Line line = (Line)item;						
						data.append("G01 X" + (float)(line.getStart().x / 16) + " Y" + (float)(line.getStart().y / 16) + " F100");
						data.append("\n");
						data.append("G01 X" + (float)(line.getEnd().x / 16)+ " Y" + (float)(line.getEnd().y / 16) +" F100");
						data.append("\n");
					}
				}
				
			}
			
			textArea.setText(data.toString());

	        RTextScrollPane textScrollPane = new RTextScrollPane(textArea);			
			View view = new View("GCode for "+editor.getCurrentFile().getName(),null,textScrollPane);
			openEditor(view, editor.getCurrentFile().getName().hashCode());
			
		}

		
	}

}
