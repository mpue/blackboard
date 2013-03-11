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

import java.util.Enumeration;

import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.UndoableEdit;
/**
 * Description: Adds new Features to the standard Java HTMLDocument class.
 * 
 * @author Howard Kistler, Karsten Pawlik
 */
public class ExtendedHTMLDocument extends HTMLDocument {
	
	
	/**
	 * Constructs a new ExtendedHTMLDocument using the given values.
	 */
	public ExtendedHTMLDocument() {
	}
	
	/**
	 * Constructs a new ExtendedHTMLDocument using the given values.
	 * @param content document content
	 * @param styles document css-styles
	 */
	public ExtendedHTMLDocument(AbstractDocument.Content content, StyleSheet styles) {
		super(content, styles);
	}
	
	/**
	 * Constructs a new ExtendedHTMLDocument using the given values.
	 * @param styles document css-styles.
	 */
	public ExtendedHTMLDocument(StyleSheet styles) {
		super(styles);
	}
	
	/**
	 * Ueberschreibt die Attribute des Elements.
	 * @param element Element bei dem die Attribute geaendert werden sollen
	 * @param attributes AttributeSet mit den neuen Attributen
	 * @param tag Angabe was fuer ein Tag das Element ist
	 */
	@SuppressWarnings("rawtypes")
	public void replaceAttributes(Element element, AttributeSet attributes, HTML.Tag tag) {
		if( (element != null) && (attributes != null)) {
			try {
				writeLock();
				int start = element.getStartOffset();

				DefaultDocumentEvent changes = new DefaultDocumentEvent(start,
				element.getEndOffset() - start, DocumentEvent.EventType.CHANGE);

				AttributeSet sCopy = attributes.copyAttributes();
				changes.addEdit(new AttributeUndoableEdit(element, sCopy, false));

				MutableAttributeSet attr = (MutableAttributeSet) element.getAttributes();
				Enumeration aNames = attr.getAttributeNames();
				Object value;
				Object aName;
				while (aNames.hasMoreElements()) {
					aName = aNames.nextElement();
					value = attr.getAttribute(aName);
					if(value != null && !value.toString().equalsIgnoreCase(tag.toString())) {
						attr.removeAttribute(aName);
					}
				}
				attr.addAttributes(attributes);
				changes.end();

				fireChangedUpdate(changes);
				fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
			} finally {
				writeUnlock();
			}
		}
	}
	
	
	/**
	 * removes the given element between index and index+count.
	 * @param element element to delete
	 * @param index text index
	 * @param count character count
	 * @throws BadLocationException if thrown if index or index+count does not exist.
	 */
	public void removeElements(Element element, int index, int count) throws BadLocationException {
		writeLock();
		int start = element.getElement(index).getStartOffset();
		int end = element.getElement(index + count - 1).getEndOffset();
		try {
			Element[] removed = new Element[count];
			Element[] added = new Element[0];
	
			for (int counter = 0; counter < count; counter++) {
				removed[counter] = element.getElement(counter + index);
			}
	
			DefaultDocumentEvent dde = new DefaultDocumentEvent(start, end - start, DocumentEvent.EventType.REMOVE);
			((AbstractDocument.BranchElement)element).replace(index, removed.length,added);
			dde.addEdit(new ElementEdit(element, index, removed, added));
			UndoableEdit u = getContent().remove(start, end - start);
	
			if(u != null) {
				dde.addEdit(u);
			}
	
			postRemoveUpdate(dde);
			dde.end();
			fireRemoveUpdate(dde);
	
			if(u != null) {
				fireUndoableEditUpdate(new UndoableEditEvent(this, dde));
			}
		} finally {
			writeUnlock();
		}
	}
}
