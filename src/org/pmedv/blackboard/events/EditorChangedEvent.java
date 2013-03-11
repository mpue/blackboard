package org.pmedv.blackboard.events;

import org.pmedv.blackboard.components.BoardEditor;

/**
 * @author Matthias Pueski (17.07.2011)
 *
 */
public class EditorChangedEvent {
	
	public static enum EventType {
		EDITOR_CHANGED,
		EDITOR_CLOSED
	}
	
	private BoardEditor editor;
	
	public EditorChangedEvent(final BoardEditor editor, EventType type) {
		this.editor = editor;
	}

	/**
	 * @return the editor
	 */
	public final BoardEditor getEditor() {
		return editor;
	}
	
}
