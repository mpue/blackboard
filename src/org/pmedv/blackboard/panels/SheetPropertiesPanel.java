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

import org.pmedv.core.components.FileBrowserTextfield;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SheetPropertiesPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private JTextField nameField;
	private JTextField descriptionField;
	private FileBrowserTextfield fileBrowserTextfield;

	/**
	 * Create the panel.
	 */
	public SheetPropertiesPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblFile = new JLabel(resources.getResourceByKey("SheetPropertiesPanel.file"));
		add(lblFile, "4, 3");
		
		fileBrowserTextfield = new FileBrowserTextfield();
		add(fileBrowserTextfield, "6, 3, fill, fill");
		
		JLabel lblName = new JLabel(resources.getResourceByKey("SheetPropertiesPanel.name"));
		add(lblName, "4, 5, left, default");
		
		nameField = new JTextField();
		add(nameField, "6, 5, fill, default");
		nameField.setColumns(10);
		
		JLabel lblDescription = new JLabel(resources.getResourceByKey("SheetPropertiesPanel.description"));
		add(lblDescription, "4, 7, right, default");
		
		descriptionField = new JTextField();
		add(descriptionField, "6, 7, fill, default");
		descriptionField.setColumns(10);

	}

	public FileBrowserTextfield getFileBrowserTextfield() {
		return fileBrowserTextfield;
	}
	public JTextField getDescriptionField() {
		return descriptionField;
	}
	public JTextField getNameField() {
		return nameField;
	}
}
