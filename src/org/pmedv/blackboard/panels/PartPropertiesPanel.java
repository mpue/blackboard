/**

	BlackBoard breadboard designer
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
package org.pmedv.blackboard.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PartPropertiesPanel extends JPanel {
	
	private static final long serialVersionUID = 5379203557965630950L;

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public PartPropertiesPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		nameField = new JTextField();
		label2 = new JLabel();
		valueField = new JTextField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), default, $lcgap, default:grow, 2*($lcgap)",
			"$pgap, 2*($lgap, default), $lgap, $pgap"));

		//---- label1 ----
		label1.setText(resources.getResourceByKey("PartPropertiesPanel.name"));
		add(label1, cc.xy(3, 3));
		add(nameField, cc.xy(5, 3));

		//---- label2 ----
		label2.setText(resources.getResourceByKey("PartPropertiesPanel.value"));
		add(label2, cc.xy(3, 5));
		add(valueField, cc.xy(5, 5));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JTextField nameField;
	private JLabel label2;
	private JTextField valueField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	/**
	 * @return the nameField
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * @param nameField the nameField to set
	 */
	public void setNameField(JTextField nameField) {
		this.nameField = nameField;
	}

	/**
	 * @return the valueField
	 */
	public JTextField getValueField() {
		return valueField;
	}

	/**
	 * @param valueField the valueField to set
	 */
	public void setValueField(JTextField valueField) {
		this.valueField = valueField;
	}


}
