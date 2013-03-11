package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.pmedv.blackboard.app.Language;
import org.pmedv.core.commands.AbstractCommand;

public class SelectLanguageAction extends AbstractCommand {

	@SuppressWarnings("unused")
	private Language language;
	
	public SelectLanguageAction(Language language) {
		this.language = language;
		putValue(Action.NAME, language.getDisplayString());
		putValue(Action.SHORT_DESCRIPTION, "Sets the language to "+language.getDisplayString());
	}
	
	@Override
	public void execute(ActionEvent e) {
		
	}

	
}
