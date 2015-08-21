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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.panels.ResistorPanel;
import org.pmedv.blackboard.tools.ResistorFactory;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;


public class ResistorDialog extends AbstractNiceDialog {

	private static final long	serialVersionUID	= 1429247746599672970L;
	private ResistorPanel resistorPanel;

	private Resistor resistor;
	
	public ResistorDialog(String title, String subTitle, ImageIcon icon, Resistor resistor) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), resistor);
	}

	@Override
	protected void initializeComponents() {
	
		resistorPanel = new ResistorPanel();
		
		resistorPanel.getMultiplierCombo().addItem(new Integer(1));
		resistorPanel.getMultiplierCombo().addItem(new Integer(1000));
		resistorPanel.getMultiplierCombo().addItem(new Integer(1000000));
		
		resistorPanel.getMultiplierCombo().setRenderer(new MultiplyComboBoxRenderer());
		
		setSize(new Dimension(360,240));		
		getContentPanel().add(resistorPanel);
		
		if (getUserObject() != null && getUserObject() instanceof Resistor) {
			this.resistor = (Resistor)getUserObject();	
			resistorPanel.getNameField().setText(resistor.getName());
			resistorPanel.getValueSpinner().setValue(resistor.getValue());
			resistorPanel.getToleranceCombo().setSelectedItem(Float.valueOf(resistor.getTolerance()));
		}
		
		if (getUserObject() == null) {
			BoardEditor editor = EditorUtils.getCurrentActiveEditor();
			String designator = BoardUtil.getNextFreeDesignator(editor.getModel(),"R");
			resistorPanel.getNameField().setText(designator);						
		}
		
		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Float tolerance = (Float)resistorPanel.getToleranceCombo().getSelectedItem();
				
				Integer multiply = (Integer)resistorPanel.getMultiplierCombo().getSelectedItem();				
				Integer value = (Integer)resistorPanel.getValueSpinner().getValue();
				
				if (resistor == null) {
					resistor = ResistorFactory.getInstance().getResistor(multiply*value,tolerance);
				}
				else
					resistor.setValue(multiply*value);
				
				resistor.setBody(ResistorFactory.getInstance().getImageForValue(resistor.getValue(),tolerance));				
				resistor.setName(resistorPanel.getNameField().getText());
				
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
	 * @return the resistorPanel
	 */
	public ResistorPanel getResistorPanel() {
		return resistorPanel;
	}

	/**
	 * @return the resistor
	 */
	public Resistor getResistor() {
		return resistor;
	}
	
	private static class MultiplyComboBoxRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 6444031192500824251L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			MultiplyComboBoxRenderer renderer = (MultiplyComboBoxRenderer) super.getListCellRendererComponent(
					list, value, index, isSelected, cellHasFocus);

			if (value instanceof Integer) {

				Integer multiplier = (Integer)value;
				
				switch (multiplier) {
					case 1 : setText("Ohm");
						break;
					case 1000 : setText("kOhm");
						break;
					case 1000000 : setText("MOhm");
						break;
					default : break;
						
				}

			}

			return renderer;

		}

	}

}
