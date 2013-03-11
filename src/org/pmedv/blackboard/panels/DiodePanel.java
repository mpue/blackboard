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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.pmedv.blackboard.components.Diode.DiodeType;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class DiodePanel extends JPanel {
	
	private JTextField nameTextField;
	private JTextField valueTextField;
	private JComboBox typeComboBox;
	
	/**
	 * Create the panel.
	 */
	public DiodePanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name");
		add(lblName, "4, 4, right, default");
		
		nameTextField = new JTextField();
		add(nameTextField, "6, 4, fill, default");
		nameTextField.setColumns(10);
		
		JLabel lblValue = new JLabel("Value");
		add(lblValue, "4, 6, right, default");
		
		valueTextField = new JTextField();
		add(valueTextField, "6, 6, fill, default");
		valueTextField.setColumns(10);
		
		JLabel lblType = new JLabel("TextType");
		add(lblType, "4, 8, right, default");
		
		typeComboBox = new JComboBox(DiodeType.values());
		add(typeComboBox, "6, 8, fill, default");

	}

	/**
	 * @return the nameTextField
	 */
	public JTextField getNameTextField() {
		return nameTextField;
	}

	/**
	 * @return the valueTextField
	 */
	public JTextField getValueTextField() {
		return valueTextField;
	}

	/**
	 * @return the typeComboBox
	 */
	public JComboBox getTypeComboBox() {
		return typeComboBox;
	}
	
	

}
