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
/*
GNU Lesser General Public License

ExtendedHTMLEditorKit
Copyright (C) 2001-2002  Frits Jalvingh & Howard Kistler
changes to ExtendedHTMLEditorKit
Copyright (C) 2003-2004  Karsten Pawlik

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.pmedv.core.components;

import java.net.URL;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
  * Description: This class extends HTMLEditorKit so that it can provide other renderer classes
  * instead of the defaults. Most important is the part which renders relative
  * image paths.
  *
  * @author <a href="mailto:jal@grimor.com">Frits Jalvingh</a>
  * @version 1.0
  */

public class ExtendedHTMLEditorKit extends HTMLEditorKit
{
	
	private static final Log log = LogFactory.getLog(ExtendedHTMLEditorKit.class);
	
	/** 
	 * Constructor
	 */
	public ExtendedHTMLEditorKit() {
	}

	/** 
	 * Method for returning a ViewFactory which handles the image rendering.
	 * @return returns a new ViewFactory Object.
	 */
	public ViewFactory getViewFactory() {
		return new HTMLFactoryExtended();
	}

	/**
	 * creates a default document
	 * @return returns a Document
	 */
	public Document createDefaultDocument() {
		return createDefaultDocument(null);
	}
	
	/**
	 * creates a default document and sets the base url to the given value.
	 * @param baseUrl creates the default document and sets the base url
	 * of the html-document to the given value. if base url is null,
	 * no base url will be set.
	 * @return returns a Document
	 */
	public Document createDefaultDocument(URL baseUrl) {
	  StyleSheet styles = getStyleSheet();
	  StyleSheet ss = new StyleSheet();
	  ss.addStyleSheet(styles);
	  ExtendedHTMLDocument doc = new ExtendedHTMLDocument(ss);
	  if (baseUrl != null) doc.setBase(baseUrl);
	  doc.setParser(getParser());
	  doc.setAsynchronousLoadPriority(4);
	  doc.setTokenThreshold(100);
	  return doc;
	}

	/** 
	 * Class that replaces the default ViewFactory and supports
	 * the proper rendering of both URL-based and local images.
	 */
	public static class HTMLFactoryExtended extends HTMLFactory implements ViewFactory {
		/** 
		 * Constructor
		 */
		public HTMLFactoryExtended() {
		}

		/** 
		 * Method to handle IMG tags and
		 * invoke the image loader.
		 * @param elem an element to create
		 * @return a View
		 */
		public View create(Element elem) {
			Object obj = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
			if(obj instanceof HTML.Tag) {
				HTML.Tag tagType = (HTML.Tag)obj;
				if(tagType == HTML.Tag.IMG) {
					try {
						return new RelativeImageView(elem);
					} catch (Exception e) {
						log.warn("Error while trying to create new RelativeImageView for element");
					}
				}
			}
			return super.create(elem);
		}
	}
}
