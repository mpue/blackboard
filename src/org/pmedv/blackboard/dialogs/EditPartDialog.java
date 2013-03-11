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

import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.filter.PNGFilter;
import org.pmedv.blackboard.panels.EditPartPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class EditPartDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = 1429247746599672970L;
	private EditPartPanel editPartPanel;

	private Part part;

	public EditPartDialog(String title, String subTitle, ImageIcon icon, Part part) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), part);
	}

	@Override
	protected void initializeComponents() {

		editPartPanel = new EditPartPanel();
		editPartPanel.getFileBrowserTextField().setParent(this);
		editPartPanel.getFileBrowserTextField().getFileChooser().setFileFilter(new PNGFilter());
		editPartPanel.getWidthSpinner().setValue(64);
		editPartPanel.getHeightSpinner().setValue(64);
		
		setSize(new Dimension(560, 600));
		getContentPanel().add(editPartPanel);

		if (getUserObject() != null && getUserObject() instanceof Part) {
			this.part = (Part) getUserObject();
			editPartPanel.getPartNameTextField().setText(part.getName());
			editPartPanel.getPackageTypeTextField().setText(part.getPackageType());
			editPartPanel.getAuthorTextField().setText(part.getAuthor());
			editPartPanel.getLicenseTextField().setText(part.getLicense());
			editPartPanel.getDescriptionTextArea().setText(part.getDescription());
			editPartPanel.getWidthSpinner().setValue(part.getWidth());
			editPartPanel.getHeightSpinner().setValue(part.getHeight());
			editPartPanel.getDesignatorTextField().setText(part.getDesignator());
		}

		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (part == null) {
					part = new Part();
				} 

				part.setName(editPartPanel.getPartNameTextField().getText());
				part.setPackageType(editPartPanel.getPackageTypeTextField().getText());
				part.setAuthor(editPartPanel.getAuthorTextField().getText());
				part.setLicense(editPartPanel.getLicenseTextField().getText());
				part.setDescription(editPartPanel.getDescriptionTextArea().getText());
				part.setWidth((Integer)editPartPanel.getWidthSpinner().getValue());
				part.setHeight((Integer)editPartPanel.getHeightSpinner().getValue());
				part.setDesignator(editPartPanel.getDesignatorTextField().getText());
				
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

	/**
	 * @return the editPartPanel
	 */
	public EditPartPanel getEditPartPanel() {
		return editPartPanel;
	}

	/**
	 * @return the part
	 */
	public Part getPart() {
		return part;
	}


}
