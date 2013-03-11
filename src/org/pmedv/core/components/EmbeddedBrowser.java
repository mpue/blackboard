/**

	BlackBoard BreadBoard Designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.core.components;

import java.awt.BorderLayout;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
@SuppressWarnings("unused")
public class EmbeddedBrowser extends JPanel {
	
	private static final long serialVersionUID = -8481488817434354488L;
	private JTextPane browserPane;
	
	
	/**
	 * @return the browserPane
	 */
	public JTextPane getBrowserPane() {
		return browserPane;
	}


	/**
	 * @param browserPane the browserPane to set
	 */
	public void setBrowserPane(JTextPane browserPane) {
		this.browserPane = browserPane;
	}


	public EmbeddedBrowser() {
		
		setLayout(new BorderLayout());		
		browserPane = new JTextPane();
		browserPane.setEditable(false);
		ExtendedHTMLEditorKit extendedHTMLEditorKit = new ExtendedHTMLEditorKit();
		browserPane.setEditorKit(extendedHTMLEditorKit);		
		ExtendedHTMLDocument doc;
		doc = (ExtendedHTMLDocument) extendedHTMLEditorKit.createDefaultDocument();
		
		JScrollPane scrollPane = new JScrollPane(browserPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(scrollPane,BorderLayout.CENTER);
	}
	
	
	public void setText(String text) throws IllegalArgumentException {

		try {
			Document doc = getBrowserPane().getDocument();
			doc.remove(0, doc.getLength());

			doc.putProperty("IgnoreCharsetDirective", new Boolean(true));

			Reader r = new StringReader(text);
			EditorKit kit = getBrowserPane().getEditorKit();
			kit.read(r, doc, 0);
		} 
		catch (Exception e) {
			throw new IllegalArgumentException("Invalid content was tried to set.");
		}

	}
	
}
