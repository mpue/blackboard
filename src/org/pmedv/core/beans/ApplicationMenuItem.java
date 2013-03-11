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
@XmlType(name = "ApplicationMenuItem", propOrder = {
	"name",
	"actionClass",
	"imageIcon",
	"mnemonic",
	"toolTipText",
	"modifier",
	"type",
	"bean"
})

public class ApplicationMenuItem {
	
	private String name;	
	private String actionClass;
	private String imageIcon;
	private String mnemonic;
	private String toolTipText;
	private Boolean bean = false;

	/**
	 * This field is used to determine which modifier has to be pressed for the item (ALT,SHIFT,CTRL)
	 */
	
	private String modifier;
	private String type;
	
	

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
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
	 * @return the mnemonic
	 */
	public String getMnemonic() {
		return mnemonic;
	}

	/**
	 * @param mnemonic the mnemonic to set
	 */
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
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

	public ApplicationMenuItem() {
		
	}
	
	public ApplicationMenuItem(String name) {
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
