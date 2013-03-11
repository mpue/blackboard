package org.pmedv.core.commands;

import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;


public abstract class AbstractEditorCommand extends AbstractCommand implements EditorChangedListener {
	
	@Override
	public void editorChanged(EditorChangedEvent event) {
		setEnabled(event.getEditor() != null);			
	}
	
}
