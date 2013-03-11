package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import net.infonode.docking.View;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.spice.SpiceUtil;
import org.pmedv.core.commands.AbstractOpenEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

@SuppressWarnings("serial")
public class ExportNetlistCommand extends AbstractOpenEditorCommand {

	public ExportNetlistCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ExportNetlistCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.netlist"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ExportNetlistCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
	}

	@Override
	public void execute(ActionEvent e) {

 		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

		if (editor != null) {

			StringBuffer data = null;
			
			try {
				data = SpiceUtil.createSpiceNetList(editor);
			}
			catch (Exception e1) {
				ErrorUtils.showErrorDialog(e1);
				return;
			}
			
			RSyntaxTextArea textArea = new RSyntaxTextArea();
			textArea.getPopupMenu().addSeparator();
			
			SaveNetlistCommand cmd = AppContext.getContext().getBean(SaveNetlistCommand.class);			
			textArea.getPopupMenu().add(cmd);
			textArea.setText(data.toString());			
			cmd.setNetlist(data.toString());
	        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
	        RTextScrollPane textScrollPane = new RTextScrollPane(textArea);			
			View view = new View("Netlist for "+editor.getCurrentFile().getName(),null,textScrollPane);
			openEditor(view, editor.getCurrentFile().getName().hashCode());
			
		}

	}





}
