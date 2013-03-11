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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationPerspective", propOrder = {
    "name",
    "id",
    "perspectiveClass",
    "perspectiveIcon",
    "toolTipText",
    "mnemonic",
    "modifier",
    "menubarContributions",
    "toolbarContributions"
})

public class ApplicationPerspective {
	
	private String name;
	private String id;
	private String perspectiveClass;
	private String perspectiveIcon;
	private String toolTipText;
	private String mnemonic;
	private String modifier;
	private ArrayList<ApplicationMenu> menubarContributions;
	private ArrayList<ApplicationToolbarItem> toolbarContributions;
	
	public ApplicationPerspective() {
		menubarContributions = new ArrayList<ApplicationMenu>();
	}
	
	/**
	 * @return the menubarContributions
	 */
	public ArrayList<ApplicationMenu> getMenubarContributions() {
		return menubarContributions;
	}
	/**
	 * @param menubarContributions the menubarContributions to set
	 */
	public void setMenubarContributions(
			ArrayList<ApplicationMenu> menubarContributions) {
		this.menubarContributions = menubarContributions;
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
	 * @return the perspectiveIcon
	 */
	public String getPerspectiveIcon() {
		return perspectiveIcon;
	}
	/**
	 * @param perspectiveIcon the perspectiveIcon to set
	 */
	public void setPerspectiveIcon(String perspectiveIcon) {
		this.perspectiveIcon = perspectiveIcon;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the perspectiveClass
	 */
	public String getPerspectiveClass() {
		return perspectiveClass;
	}
	/**
	 * @param perspectiveClass the perspectiveClass to set
	 */
	public void setPerspectiveClass(String perspectiveClass) {
		this.perspectiveClass = perspectiveClass;
	}

	/**
	 * @return the toolbarContributions
	 */
	public ArrayList<ApplicationToolbarItem> getToolbarContributions() {
		return toolbarContributions;
	}

	/**
	 * @param toolbarContributions the toolbarContributions to set
	 */
	public void setToolbarContributions(
			ArrayList<ApplicationToolbarItem> toolbarContributions) {
		this.toolbarContributions = toolbarContributions;
	}
	
	
}
