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

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Diode;
import org.pmedv.blackboard.components.Diode.DiodeType;
import org.pmedv.blackboard.panels.DiodePanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class DiodeDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = 1429247746599672970L;
	private DiodePanel diodePanel;

	private Diode diode;

	public DiodeDialog(String title, String subTitle, ImageIcon icon, Diode diode) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), diode);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeComponents() {

		diodePanel = new DiodePanel();

		setSize(new Dimension(360, 300));
		getContentPanel().add(diodePanel);

		if (getUserObject() != null && getUserObject() instanceof Diode) {
			this.diode = (Diode) getUserObject();
			diodePanel.getNameTextField().setText(diode.getName());
			diodePanel.getValueTextField().setText(diode.getValue());
			diodePanel.getTypeComboBox().setSelectedItem(diode.getType());
		}

		if (getUserObject() == null) {
			BoardEditor editor = EditorUtils.getCurrentActiveEditor();
			String designator = BoardUtil.getNextFreeDesignator(editor.getModel(), "D");
			diodePanel.getNameTextField().setText(designator);
			diodePanel.getTypeComboBox().setSelectedItem(Diode.DiodeType.DO35);
		}

		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {


				if (diode == null) {
					diode = new Diode(32, 32, 96, 32, 0, diodePanel.getValueTextField().getText(), (DiodeType) diodePanel.getTypeComboBox().getSelectedItem());
					diode.setName(diodePanel.getNameTextField().getText());
				} 
				else {
					diode.setValue(diodePanel.getValueTextField().getText());
					diode.setName(diodePanel.getNameTextField().getText());
					diode.setType((DiodeType) diodePanel.getTypeComboBox().getSelectedItem());
					diode.setBody(Diode.getBodyimagemap().get(diode.getType()));	
				}

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
	 * @return the diodePanel
	 */
	public DiodePanel getDiodePanel() {
		return diodePanel;
	}

	/**
	 * @return the diode
	 */
	public Diode getDiode() {
		return diode;
	}

}
