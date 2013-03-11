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

public class Table extends AbstractHTMLElement implements IHTMLElement {
	
	private ArrayList<TableRow> rows;
	
	private boolean border;
	private String bgcolor;
	private String cellspacing;
	private String cellpadding;
	private String height;
	private String width;
	
	public Table() {
		rows = new ArrayList<TableRow>();
	}	
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public boolean isBorder() {
		return border;
	}
	public void setBorder(boolean border) {
		this.border = border;
	}
	public ArrayList<TableRow> getRows() {
		return rows;
	}

	public String render() {

		StringBuffer s = new StringBuffer();
		
		s.append(Tags.TABLE_OPEN);
		s.append("\n");
		s.append(getUniversalAttributes());
		 
		if (bgcolor != null && bgcolor.length() > 0)
			s.append(Attribute.ATT_BGCOLOR+"="+Tags.QUOTE+bgcolor+Tags.QUOTE);
		if (width != null && width.length() > 0)
			s.append(Attribute.ATT_WIDTH+"="+Tags.QUOTE+width+Tags.QUOTE);		
		if (height != null && height.length() > 0)
			s.append(Attribute.ATT_HEIGHT+"="+Tags.QUOTE+height+Tags.QUOTE);
		if (cellpadding != null && cellpadding.length() > 0)
			s.append(Attribute.ATT_CELLPADDING+"="+Tags.QUOTE+cellpadding+Tags.QUOTE);
		if (cellspacing != null && cellspacing.length() > 0)
			s.append(Attribute.ATT_CELLSPACING+"="+Tags.QUOTE+cellspacing+Tags.QUOTE);
		if (border)
			s.append(Attribute.ATT_BORDER+"="+Tags.QUOTE+"1"+Tags.QUOTE);
		else
			s.append(Attribute.ATT_BORDER+"="+Tags.QUOTE+"0"+Tags.QUOTE);
		
		s.append(Tags.TAG_FINISH);		
		s.append("\n");
		
		for (Iterator<TableRow> it = rows.iterator();it.hasNext();) {
			IHTMLElement element = (IHTMLElement)it.next();			
			s.append(element.render());
		} 
		
		s.append(Tags.TABLE_CLOSE);
		s.append("\n");
		
		return s.toString();
	}

	
	public void addRow(TableRow row) {
		rows.add(row);
	}

	public void removeRow(TableRow row) {
		rows.remove(row);
	}

	public String getCellpadding() {
		return cellpadding;
	}

	public void setCellpadding(String cellpadding) {
		this.cellpadding = cellpadding;
	}

	public String getCellspacing() {
		return cellspacing;
	}

	public void setCellspacing(String cellspacing) {
		this.cellspacing = cellspacing;
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

	public void setRows(ArrayList<TableRow> rows) {
		this.rows = rows;
	}
	



}
