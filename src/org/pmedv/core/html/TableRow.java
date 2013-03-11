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

import java.util.ArrayList;
import java.util.Iterator;

public class TableRow extends AbstractHTMLElement implements IHTMLElement {
	
	private ArrayList<TableColumn> columns;

	private String valign;
	private String bgcolor;
	private String width;
	private String height;
	
	public TableRow() {
		columns = new ArrayList<TableColumn>();
	}
	
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public ArrayList<TableColumn> getColumns() {
		return columns;
	}

	public String getValign() {
		return valign;
	}
	public void setValign(String valign) {
		this.valign = valign;
	}

	
	@SuppressWarnings("rawtypes")
	public String render() {

		StringBuffer s = new StringBuffer();
		
		s.append(Tags.TABLE_ROW_OPEN);
		s.append("\n");
		
		s.append(getUniversalAttributes());
		 
		if (valign != null && valign.length() > 0)
			s.append(Attribute.ATT_VALIGN+"="+Tags.QUOTE+valign+Tags.QUOTE);
		if (bgcolor != null && bgcolor.length() > 0)
			s.append(Attribute.ATT_BGCOLOR+"="+Tags.QUOTE+bgcolor+Tags.QUOTE);
		if (width != null && width.length() > 0)
			s.append(Attribute.ATT_WIDTH+"="+Tags.QUOTE+width+Tags.QUOTE);		
		if (height != null && height.length() > 0)
			s.append(Attribute.ATT_HEIGHT+"="+Tags.QUOTE+height+Tags.QUOTE);
		
		s.append(Tags.TAG_FINISH);		
		s.append("\n");
		
		for (Iterator it = columns.iterator();it.hasNext();) {
			IHTMLElement element = (IHTMLElement)it.next();			
			s.append(element.render());
		}
		
		s.append(Tags.TABLE_ROW_CLOSE);
		s.append("\n");
		
		return s.toString();
	}

	public void addColumn(TableColumn column) {
		columns.add(column);
	}

	public void removeColumn(TableColumn column) {
		columns.remove(column);
	}

	
}
