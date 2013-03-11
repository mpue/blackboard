package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;

@SuppressWarnings("serial")
public class DuplicateCommand extends AbstractCommand {
	
	public DuplicateCommand() {
		putValue(Action.NAME, resources.getResourceByKey("DuplicateCommand.name"));
		putValue(Action.SMALL_ICON,resources.getIcon("icon.duplicate"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("DuplicateCommand.description"));	
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		setEnabled(false);
	}
	
	@Override
	public void execute(ActionEvent e) {
		AppContext.getContext().getBean(CopyCommand.class).execute(e);
		AppContext.getContext().getBean(PasteCommand.class).execute(e);
	}
	
}
