package org.pmedv.core.commands;

import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

public abstract class AbstractCommand extends AbstractAction implements Command, Serializable {
	
	private static final long serialVersionUID = -8291805215011268388L;
	
	protected static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public AbstractCommand(){
		super();
	}
	
	public AbstractCommand(String name){
		super(name);
	}
	
	public AbstractCommand(String name, Icon icon){
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute(e);
	}

}
