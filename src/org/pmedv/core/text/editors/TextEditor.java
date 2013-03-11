package org.pmedv.core.text.editors;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import org.pmedv.core.text.IStructuredTextEditor;

/**
 * The <code>TextEditor</code> is the basic text editor for the CTMS, it provides basic functions
 * such as cut copy and paste.
 * 
 * @author Matthias Pueski
 * 
 */
public class TextEditor extends JPanel implements IStructuredTextEditor {

	private static final long serialVersionUID = 5282485435624691244L;

	private JTextPane editor;
	private Document doc;
	@SuppressWarnings("unused")
	private EditorKit kit;
	private TextPopupMenu menu;

	public TextEditor() {

		setLayout(new BorderLayout());
		editor = new JTextPane();
		Font font;
		try {
			final UIDefaults defaults = UIManager.getLookAndFeelDefaults();
			font = (Font) defaults.get("TextArea.font");
		}
		catch (Exception ex) {
			font = new Font("Arial", Font.PLAIN, 14);
		}
		editor.setFont(font);

		kit = editor.getEditorKit();
		doc = editor.getDocument();

		menu = new TextPopupMenu(editor);

		JScrollPane scrollPane = new JScrollPane(editor);
		add(scrollPane, BorderLayout.CENTER);

	}

	@Override
	public String getText() {
		try {
			return doc.getText(0, doc.getLength());
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";

	}

	@Override
	public void setText(String text) {

		try {
			doc.remove(0, doc.getLength());
			doc.insertString(0, text, null);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setEditable(boolean editable) {
		editor.setEditable(editable);
	}

	@Override
	public JTextPane getEditor() {
		return editor;
	}

	@Override
	public void setCaretPosition(int position) {
		editor.setCaretPosition(position);
	}

	@Override
	public void requestFocus() {
		editor.requestFocus();
	}

	@Override
	public JPopupMenu getPopUpMenu() {
		return menu;
	}

}
