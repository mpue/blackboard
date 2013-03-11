package org.pmedv.core.text;

import org.pmedv.core.text.editors.TextEditor;


/**
 * The {@link EditorFactory} allows us to create any kind of structured text 
 * editor. Each editor handling structured text (such as html and text editors)
 * must implement the {@link IStructuredTextEditor} interface, in order to be 
 * created by this factory. 
 * 
 * @author Matthias Pueski
 *
 */
public final class EditorFactory {

	protected static final EditorFactory INSTANCE = new EditorFactory();
	
	
	/**
	 * Creates the desired {@link IStructuredTextEditor} based on the {@link EditorType} given
	 * 
	 * @param type
	 * @return
	 */
	public final IStructuredTextEditor getEditor(final EditorType type) {
		return new TextEditor();
	}
	
	/**
	 * gets the only instance of this {@link EditorFactory}
	 * 
	 * @return the factory instance
	 */
	public static final EditorFactory getInstance() {
		return INSTANCE;
	}
	
}
