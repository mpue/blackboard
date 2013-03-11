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

import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.text.JTextComponent;

public class TextSelector {

	private static FocusHandler	installedInstance;

	/**
	 * Install an PropertyChangeList listener to the default focus manager and
	 * selects text when a text component is focused.
	 */
	public static void install() {

		// already installed
		if (installedInstance != null) {
			return;
		}

		installedInstance = new FocusHandler();

		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();

		kfm.addPropertyChangeListener("focusOwner", installedInstance);
	}

	public static void uninstall() {

		if (installedInstance != null) {
			KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			kfm.removePropertyChangeListener("focusOwner", installedInstance);
		}
	}

	private static class FocusHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {

			if (evt.getNewValue() instanceof JTextComponent) {
				JTextComponent text = (JTextComponent) evt.getNewValue();
				// select text if the component is editable
				// and the caret is at the end of the text
				if (text.isEditable() && text.getDocument().getLength() == text.getCaretPosition()) {

					text.selectAll();
				}
			}
		}

	}
}
