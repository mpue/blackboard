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
package org.pmedv.core.util;

import java.awt.FontMetrics;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class EditorUtils {
	
	/**
	 * Sets the tabWidth for a textpane
	 * 
	 * @param charactersPerTab
	 * @param textpane
	 */

	public static void setTabs( int charactersPerTab, JTextPane textpane) {
		
		charactersPerTab--;
		
		FontMetrics fm = textpane.getFontMetrics(textpane.getFont());
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * charactersPerTab;

		TabStop[] tabs = new TabStop[10];

		for (int j = 0; j < tabs.length; j++) {
			int tab = j + 1;
			tabs[j] = new TabStop(tab * tabWidth);
		}

		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = textpane.getDocument().getLength();
		textpane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
		
	}

}
