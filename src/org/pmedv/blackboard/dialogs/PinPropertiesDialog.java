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

import org.pmedv.blackboard.components.Pin;
import org.pmedv.blackboard.components.PinCaptionOrientation;
import org.pmedv.blackboard.panels.PinPropertiesPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

/**
 * Dialog for changing properties of a {@link Pin}.
 * 
 * @author Matthias Pueski (22.10.2012)
 * 
 */
@SuppressWarnings("serial")
public class PinPropertiesDialog extends AbstractNiceDialog {

	private PinPropertiesPanel pinPropertiesPanel;
	private Pin pin;
	private ButtonActionListener buttonListener;

	private static final String TOP_LEFT = "topLeft";
	private static final String TOP_RIGHT = "topRight";
	private static final String BOTTOM_LEFT = "bottomLeft";
	private static final String BOTTOM_RIGHT = "bottomRight";
	
	public PinPropertiesDialog(String title, String subTitle, ImageIcon icon, Pin pin) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), pin);
	}

	@Override
	protected void initializeComponents() {

		pinPropertiesPanel = new PinPropertiesPanel();
		setSize(new Dimension(320, 350));
		getContentPanel().add(pinPropertiesPanel);

		buttonListener = new ButtonActionListener();
		pinPropertiesPanel.getTopLeftButton().addActionListener(buttonListener);
		pinPropertiesPanel.getTopLeftButton().setActionCommand(TOP_LEFT);
		pinPropertiesPanel.getTopRightButton().addActionListener(buttonListener);
		pinPropertiesPanel.getTopRightButton().setActionCommand(TOP_RIGHT);
		pinPropertiesPanel.getBottomLeftButton().addActionListener(buttonListener);
		pinPropertiesPanel.getBottomLeftButton().setActionCommand(BOTTOM_LEFT);
		pinPropertiesPanel.getBottomRightButton().addActionListener(buttonListener);
		pinPropertiesPanel.getBottomRightButton().setActionCommand(BOTTOM_RIGHT);

		if (getUserObject() != null && getUserObject() instanceof Pin) {

			this.pin = (Pin) getUserObject();

			pinPropertiesPanel.getNameTextField().setText(pin.getName());
			pinPropertiesPanel.getNumberTextField().setText(String.valueOf(pin.getNum()));

			if (pin.getOrientation().equals(PinCaptionOrientation.TOP_LEFT)) {
				pinPropertiesPanel.getTopLeftButton().setSelected(true);
			}
			else if (pin.getOrientation().equals(PinCaptionOrientation.TOP_RIGHT)) {
				pinPropertiesPanel.getTopRightButton().setSelected(true);
			}
			else if (pin.getOrientation().equals(PinCaptionOrientation.BOTTOM_LEFT)) {
				pinPropertiesPanel.getBottomLeftButton().setSelected(true);
			}
			else if (pin.getOrientation().equals(PinCaptionOrientation.BOTTOM_RIGHT)) {
				pinPropertiesPanel.getBottomRightButton().setSelected(true);
			}

		}
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pin.setName(pinPropertiesPanel.getNameTextField().getText());
				pin.setNum(Integer.valueOf(pinPropertiesPanel.getNumberTextField().getText()));
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
	 * @return the pinPropertiesPanel
	 */
	public PinPropertiesPanel getPinPropertiesPanel() {
		return pinPropertiesPanel;
	}

	/**
	 * @return the pin
	 */
	public Pin getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(Pin pin) {
		this.pin = pin;
	}

	private class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getActionCommand().equals(TOP_LEFT)) {
				pin.setOrientation(PinCaptionOrientation.TOP_LEFT);
			}
			else if (e.getActionCommand().equals(TOP_RIGHT)) {
				pin.setOrientation(PinCaptionOrientation.TOP_RIGHT);
			}
			else if (e.getActionCommand().equals(BOTTOM_LEFT)) {
				pin.setOrientation(PinCaptionOrientation.BOTTOM_LEFT);
			}
			else if (e.getActionCommand().equals(BOTTOM_RIGHT)) {
				pin.setOrientation(PinCaptionOrientation.BOTTOM_RIGHT);
			}
			
		}

	}

}
