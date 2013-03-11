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
import javax.swing.JSpinner;

import org.pmedv.blackboard.models.BoardEditorModel.BoardType;
import org.pmedv.core.components.FileBrowserTextfield;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
@SuppressWarnings("serial")
public class BoardPropertiesPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	@SuppressWarnings("unchecked")
	public BoardPropertiesPanel() {
		initComponents();
		unitComboBox.addItem("pixel");
		unitComboBox.addItem("mm");
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label3 = new JLabel();
		typeCombo = new JComboBox(BoardType.values());
		label4 = new JLabel();
		fileField = new FileBrowserTextfield();
		label1 = new JLabel();
		boardWidthSpinner = new JSpinner();
		label5 = new JLabel();
		label2 = new JLabel();
		boardHeightSpinner = new JSpinner();
		unitComboBox = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), default, 3*($lcgap, default:grow), 2*($lcgap)",
			"2*($lgap), 4*(default, $lgap), $lgap"));

		//---- label3 ----
		label3.setText(resources.getResourceByKey("BoardPropertiesPanel.boardType"));
		add(label3, cc.xy(3, 3));
		add(typeCombo, cc.xywh(5, 3, 5, 1));

		//---- label4 ----
		label4.setText(resources.getResourceByKey("BoardPropertiesPanel.background"));
		add(label4, cc.xy(3, 5));
		add(fileField, cc.xywh(5, 5, 5, 1));

		//---- label1 ----
		label1.setText(resources.getResourceByKey("BoardPropertiesPanel.board.width"));
		add(label1, cc.xy(3, 7));
		add(boardWidthSpinner, cc.xy(5, 7));

		//---- label5 ----
		label5.setText(resources.getResourceByKey("BoardPropertiesPanel.unit"));
		add(label5, cc.xy(7, 7, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- label2 ----
		label2.setText(resources.getResourceByKey("BoardPropertiesPanel.board.height"));
		add(label2, cc.xy(3, 9));
		add(boardHeightSpinner, cc.xy(5, 9));
		add(unitComboBox, cc.xy(7, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label3;
	private JComboBox typeCombo;
	private JLabel label4;
	private FileBrowserTextfield fileField;
	private JLabel label1;
	private JSpinner boardWidthSpinner;
	private JLabel label5;
	private JLabel label2;
	private JSpinner boardHeightSpinner;
	private JComboBox unitComboBox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	/**
	 * @return the boardWidthSpinner
	 */
	public JSpinner getBoardWidthSpinner() {
		return boardWidthSpinner;
	}

	/**
	 * @return the boardHeightSpinner
	 */
	public JSpinner getBoardHeightSpinner() {
		return boardHeightSpinner;
	}

	/**
	 * @return the typeCombo
	 */
	public JComboBox getTypeCombo() {
		return typeCombo;
	}

	/**
	 * @return the fileField
	 */
	public FileBrowserTextfield getFileField() {
		return fileField;
	}

	/**
	 * @return the unitComboBox
	 */
	public JComboBox getUnitComboBox() {
		return unitComboBox;
	}



}
