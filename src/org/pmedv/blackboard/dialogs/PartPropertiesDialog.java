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
package org.pmedv.blackboard.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.panels.PartPropertiesPanel;
import org.pmedv.core.dialogs.AbstractNiceDialog;

/**
 * Dialog for changing properties of a part.
 * 
 * @author Matthias Pueski (17.06.2011)
 *
 */
public class PartPropertiesDialog extends AbstractNiceDialog {
	
	private static final long serialVersionUID = -869262728305126685L;

	private PartPropertiesPanel partPanel;
	private Part part;

	public PartPropertiesDialog(String title, String subTitle, ImageIcon icon, Part part, JFrame parent) {
		super(title, subTitle, icon, true, false, true, true, parent, part);
	}

	@Override
	protected void initializeComponents() {
		partPanel = new PartPropertiesPanel();
		setSize(new Dimension(360, 240));
		getContentPanel().add(partPanel);
		if (getUserObject() != null && getUserObject() instanceof Part) {
			this.part = (Part) getUserObject();
			partPanel.getNameField().setText(part.getName());
			partPanel.getValueField().setText(part.getValue());
		}
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				part.setName(partPanel.getNameField().getText());
				part.setValue(partPanel.getValueField().getText());
				result = OPTION_OK;
				setVisible(false);
				dispose();
			}
		});
		getCancelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
				dispose();
			}
		});
	}
}
