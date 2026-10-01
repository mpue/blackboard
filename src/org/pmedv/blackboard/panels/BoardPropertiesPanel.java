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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
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

	/** Pixels per breadboard hole (0.1 inch raster). */
	private static final int PIXELS_PER_HOLE = 16;
	/** Millimeters per breadboard hole (0.1 inch). */
	private static final float MM_PER_HOLE = 2.54f;

	private static final String UNIT_PIXEL = "pixel";
	private static final String UNIT_MM = "mm";
	private static final String UNIT_HOLES = "holes";

	private String previousUnit = UNIT_MM;
	
	@SuppressWarnings("unchecked")
	public BoardPropertiesPanel() {
		initComponents();
		unitComboBox.addItem(UNIT_MM);
		unitComboBox.addItem(UNIT_HOLES);
		unitComboBox.addItem(UNIT_PIXEL);
		unitComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				convertDimensionsToSelectedUnit();
			}
		});
		setBorder(BorderFactory.createEmptyBorder(8, 12, 16, 12));
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

	/**
	 * Displays width and height in the currently selected unit, converting from pixels.
	 */
	public void setDimensionsInPixels(int widthPx, int heightPx) {
		String unit = getSelectedUnit();
		boardWidthSpinner.setValue(fromPixels(widthPx, unit));
		boardHeightSpinner.setValue(fromPixels(heightPx, unit));
	}

	/**
	 * Board width in pixels, converted from the currently selected unit.
	 */
	public int getWidthInPixels() {
		return toPixels(getSpinnerValue(boardWidthSpinner), getSelectedUnit());
	}

	/**
	 * Board height in pixels, converted from the currently selected unit.
	 */
	public int getHeightInPixels() {
		return toPixels(getSpinnerValue(boardHeightSpinner), getSelectedUnit());
	}

	private void convertDimensionsToSelectedUnit() {
		String newUnit = getSelectedUnit();
		if (newUnit == null || newUnit.equals(previousUnit)) {
			return;
		}
		int widthPx = toPixels(getSpinnerValue(boardWidthSpinner), previousUnit);
		int heightPx = toPixels(getSpinnerValue(boardHeightSpinner), previousUnit);
		boardWidthSpinner.setValue(fromPixels(widthPx, newUnit));
		boardHeightSpinner.setValue(fromPixels(heightPx, newUnit));
		previousUnit = newUnit;
	}

	private String getSelectedUnit() {
		Object selected = unitComboBox.getSelectedItem();
		return selected != null ? selected.toString() : UNIT_MM;
	}

	private int getSpinnerValue(JSpinner spinner) {
		Object value = spinner.getValue();
		if (value instanceof Number) {
			return Math.round(((Number) value).floatValue());
		}
		return 0;
	}

	private int toPixels(int value, String unit) {
		if (UNIT_MM.equalsIgnoreCase(unit)) {
			return Math.round((value / MM_PER_HOLE) * PIXELS_PER_HOLE);
		}
		if (UNIT_HOLES.equalsIgnoreCase(unit)) {
			return value * PIXELS_PER_HOLE;
		}
		return value;
	}

	private int fromPixels(int pixels, String unit) {
		if (UNIT_MM.equalsIgnoreCase(unit)) {
			return Math.round((pixels / (float) PIXELS_PER_HOLE) * MM_PER_HOLE);
		}
		if (UNIT_HOLES.equalsIgnoreCase(unit)) {
			return Math.round(pixels / (float) PIXELS_PER_HOLE);
		}
		return pixels;
	}

}
