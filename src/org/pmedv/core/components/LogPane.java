package org.pmedv.core.components;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertRed;


/**
 * LogPane for the system. Simply extends JTextPane and makes it read only.
 * 
 * @author Matthias Pueski
 *
 */
@SuppressWarnings("unused")
public class LogPane extends JScrollPane {
	
	private static final long serialVersionUID = -110141873854643156L;

	private static final JTextPane textPane = new JTextPane();
	
	private StringBuffer textBuffer;
	
	// We need to initialize the look and feel again, because the log pane is initialized
	
	static {
		Plastic3DLookAndFeel.setPlasticTheme(new DesertRed());

		try {			
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
			com.jgoodies.looks.Options.setPopupDropShadowEnabled(true);
		} 
		catch (Exception e) {
		}
	}
	
	public LogPane() {
				
		super(textPane);
		textPane.setEditable(false);
		
		ExtendedHTMLEditorKit editorKit = new ExtendedHTMLEditorKit();
		textPane.setEditorKit(editorKit);
		
		ExtendedHTMLDocument aboutDoc;
		aboutDoc = (ExtendedHTMLDocument) editorKit.createDefaultDocument();
		
		textBuffer = new StringBuffer();
		
	}

	public void setText(String text) {
		textPane.setText(text);
	}
	
	public String getText() {
		return textPane.getText();
	}
	
	public void append(String text) {
			
		textBuffer.append("<br>");
		textBuffer.append(text);
		
		textPane.setText(textBuffer.toString());
		textPane.setCaretPosition(textPane.getDocument().getLength());
		
	}

}
