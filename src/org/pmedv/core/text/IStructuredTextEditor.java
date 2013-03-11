package org.pmedv.core.text;

import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * The {@link IStructuredTextEditor} interface allows to access the data of a text editor created by
 * the {@link EditorFactory}
 * 
 * @author Matthias Pueski
 * 
 */
public interface IStructuredTextEditor {

	/**
	 * Gets the text content of the editor
	 * 
	 * @return a string containibg the text content of the editor.
	 */
	public String getText();

	/**
	 * Sets the text of the editor.
	 * 
	 * @param text the text to set
	 */
	public void setText(String text);

	/**
	 * Sets the text editable
	 * 
	 * @param editable
	 */
	public void setEditable(boolean editable);

	/**
	 * Sets the position of th caret of the current editor
	 * 
	 * @param position
	 */
	public void setCaretPosition(int position);

	/**
	 * Request the focus of this editor
	 */
	public void requestFocus();

	/**
	 * Returns the Editor component
	 * 
	 * @return
	 */
	public JTextComponent getEditor();

	/**
	 * Returns the editors popup menu
	 * 
	 * @return
	 */
	public JPopupMenu getPopUpMenu();

}
