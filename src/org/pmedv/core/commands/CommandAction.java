package org.pmedv.core.commands;

import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

public abstract class CommandAction extends AbstractAction implements Command, Serializable {
	
	private static final long serialVersionUID = -8291805215011268388L;
	
	protected static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public CommandAction(){
		super();
	}
	
	public CommandAction(String name){
		super(name);
	}
	
	public CommandAction(String name, Icon icon){
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute(e);
//		BoardEditor editor = EditorUtils.getCurrentActiveEditor();
//		ApplicationContext ctx =AppContext.getContext();
//		UndoManager undoManager = editor.getUndoManager();
//		ctx.getBean(UndoCommand.class).putValue(Action.NAME, resources.getResourceByKey("UndoCommand.name")+" "+undoManager.getPresentationName());
//		ctx.getBean(RedoCommand.class).putValue(Action.NAME, resources.getResourceByKey("RedoCommand.name")+" "+undoManager.getPresentationName());		
	}

}
