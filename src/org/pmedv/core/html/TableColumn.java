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

public class TableColumn extends AbstractHTMLElement implements IHTMLElement {
	
	private String align;
	private String valign;
	private String bgcolor;
	private String width;
	private String height;
	private String colspan;
	private String data;
	
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String getValign() {
		return valign;
	}
	public void setValign(String valign) {
		this.valign = valign;
	}

	
	public String render() {

		StringBuffer s = new StringBuffer();
		
		s.append(Tags.TABLE_COLUMN_OPEN);
		s.append("\n");
		
		s.append(getUniversalAttributes());
		 
		if (align != null && align.length() > 0)
			s.append(Attribute.ATT_ALIGN+"="+Tags.QUOTE+align+Tags.QUOTE);
		if (valign != null && valign.length() > 0)
			s.append(Attribute.ATT_VALIGN+"="+Tags.QUOTE+valign+Tags.QUOTE);
		if (bgcolor != null && bgcolor.length() > 0)
			s.append(Attribute.ATT_BGCOLOR+"="+Tags.QUOTE+bgcolor+Tags.QUOTE);
		if (width != null && width.length() > 0)
			s.append(Attribute.ATT_WIDTH+"="+Tags.QUOTE+width+Tags.QUOTE);		
		if (height != null && width.length() > 0)
			s.append(Attribute.ATT_HEIGHT+"="+Tags.QUOTE+height+Tags.QUOTE);
		if (colspan != null && colspan.length() > 0 )
			s.append(Attribute.ATT_COLSPAN+"="+Tags.QUOTE+colspan+Tags.QUOTE);
		
		s.append(Tags.TAG_FINISH);	
		s.append("\n");
		s.append(data);
		s.append(Tags.TABLE_COLUMN_CLOSE);
		s.append("\n");
		
		return s.toString();
	}
	public String getColspan() {
		return colspan;
	}
	public void setColspan(String colspan) {
		this.colspan = colspan;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}

}
