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
package org.pmedv.core.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationToolbarItem", propOrder = {
	"name",
	"actionClass",
	"imageIcon",
	"toolTipText",
	"text",
	"bean"		
})

public class ApplicationToolbarItem {
	
	private String name;	
	private String actionClass;
	private String imageIcon;
	private String toolTipText;
	private String text;
	private Boolean bean = false;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the toolTipText
	 */
	public String getToolTipText() {
		return toolTipText;
	}

	/**
	 * @param toolTipText the toolTipText to set
	 */
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	/**
	 * @return the imageIcon
	 */
	public String getImageIcon() {
		return imageIcon;
	}

	/**
	 * @param imageIcon the imageIcon to set
	 */
	public void setImageIcon(String imageIcon) {
		this.imageIcon = imageIcon;
	}

	/**
	 * @return the actionClass
	 */
	public String getActionClass() {
		return actionClass;
	}

	/**
	 * @param actionClass the actionClass to set
	 */
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}

	public ApplicationToolbarItem() {
		
	}
	
	public ApplicationToolbarItem(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the bean
	 */
	public Boolean isBean() {
		return bean;
	}

	/**
	 * @param bean the bean to set
	 */
	public void setBean(Boolean bean) {
		this.bean = bean;
	}	

}
