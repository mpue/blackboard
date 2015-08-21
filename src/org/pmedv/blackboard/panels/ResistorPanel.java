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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.pmedv.blackboard.tools.ESeries;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
public class ResistorPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public ResistorPanel() {
		initComponents();
		toleranceCombo.addItem(new Float(1));
		toleranceCombo.addItem(new Float(2));
		toleranceCombo.addItem(new Float(5));
		toleranceCombo.addItem(new Float(10));
		toleranceCombo.addItem(new Float(20));		
		toleranceCombo.addItem(new Float(21));
		toleranceCombo.setRenderer(new ToleranceComboBoxRenderer());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		nameField = new JTextField();
		label3 = new JLabel();
		toleranceCombo = new JComboBox();
		label2 = new JLabel();
		valueSpinner = new JSpinner();
		multiplierCombo = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), default, 3*($lcgap, default:grow), $lcgap, default, 2*($lcgap)",
			"2*($lgap), 2*(default, $lgap), $lgap"));

		//---- label1 ----
		label1.setText(resources.getResourceByKey("ResistorPanel.name"));
		add(label1, cc.xy(3, 3));
		add(nameField, cc.xywh(5, 3, 3, 1));

		//---- label3 ----
		label3.setText(resources.getResourceByKey("ResistorPanel.tolerance"));
		add(label3, cc.xy(9, 3, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		add(toleranceCombo, cc.xy(11, 3));

		//---- label2 ----
		label2.setText(resources.getResourceByKey("ResistorPanel.value"));
		add(label2, cc.xy(3, 5));
		add(valueSpinner, cc.xywh(5, 5, 5, 1));
		add(multiplierCombo, cc.xy(11, 5));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JTextField nameField;
	private JLabel label3;
	private JComboBox toleranceCombo;
	private JLabel label2;
	private JSpinner valueSpinner;
	private JComboBox multiplierCombo;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	/**
	 * @return the nameField
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * @return the valueSpinner
	 */
	public JSpinner getValueSpinner() {
		return valueSpinner;
	}

	/**
	 * @return the multiplierCombo
	 */
	public JComboBox getMultiplierCombo() {
		return multiplierCombo;
	}

	public JComboBox getToleranceCombo() {
		return toleranceCombo;
	}
	
	private static class ToleranceComboBoxRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			ToleranceComboBoxRenderer renderer = (ToleranceComboBoxRenderer) super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			if (value instanceof Float) {
				Float tolerance = (Float)value;
				if (tolerance == 21.0f)
					renderer.setText(">20% ("+ESeries.VALUES.get(tolerance)+")");
				else
					renderer.setText(tolerance+"% ("+ESeries.VALUES.get(tolerance)+")");
					
			}
			return renderer;
		}
	}
	
}
