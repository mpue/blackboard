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

import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.panels.SheetPropertiesPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

/**
 * Dialog for changing properties of a part.
 * 
 * @author Matthias Pueski (17.06.2011)
 *
 */
public class SheetPropertiesDialog extends AbstractNiceDialog {
	
	private static final long serialVersionUID = -869262728305126685L;

	private SheetPropertiesPanel sheetPanel;
	private DatasheetBean sheet;

	public SheetPropertiesDialog(String title, String subTitle, ImageIcon icon, DatasheetBean sheet) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), sheet);
	}

	@Override
	protected void initializeComponents() {
		sheetPanel = new SheetPropertiesPanel();
		setSize(new Dimension(400, 270));
		getContentPanel().add(sheetPanel);
		if (getUserObject() != null && getUserObject() instanceof DatasheetBean) {
			this.sheet = (DatasheetBean) getUserObject();
			sheetPanel.getNameField().setText(sheet.getName());
			sheetPanel.getDescriptionField().setText(sheet.getDescription());
			sheetPanel.getFileBrowserTextfield().getPathField().setText(sheet.getLocation());
		}
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sheet.setName(sheetPanel.getNameField().getText());
				sheet.setDescription(sheetPanel.getDescriptionField().getText());
				sheet.setLocation(sheetPanel.getFileBrowserTextfield().getPathField().getText());
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
