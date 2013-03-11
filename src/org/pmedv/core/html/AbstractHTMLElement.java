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
package org.pmedv.core.html;

public class AbstractHTMLElement {

	protected String id;
	protected String styleClass;
	protected String style;
	protected String title;
	protected String align;
	
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	protected String getUniversalAttributes() {
		
		StringBuffer s = new StringBuffer();
		
		if (id != null && id.length() > 0)
			s.append(Attribute.ATT_ID+"="+Tags.QUOTE+id+Tags.QUOTE);
		if (styleClass != null && styleClass.length() > 0)
			s.append(Attribute.ATT_CLASS+"="+Tags.QUOTE+styleClass+Tags.QUOTE);		
		if (style != null && style.length() > 0)
			s.append(Attribute.ATT_STYLE+"="+Tags.QUOTE+style+Tags.QUOTE);
		if (title != null && title.length() > 0)
			s.append(Attribute.ATT_TITLE+"="+Tags.QUOTE+title+Tags.QUOTE);
		if (align != null && align.length() > 0)
			s.append(Attribute.ATT_ALIGN+"="+Tags.QUOTE+align+Tags.QUOTE);

		
		return s.toString();
		
	}

}
