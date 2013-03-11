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

import java.awt.Color;
import java.awt.Component;
import java.awt.Stroke;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.components.LineEdgeType;
import org.pmedv.blackboard.renderer.LineEdgeTypeRenderer;
import org.pmedv.blackboard.renderer.LineEdgeTypeRenderer.Direction;
import org.pmedv.blackboard.renderer.LineStrokeRenderer;
import org.pmedv.blackboard.tools.StrokeFactory;
import org.pmedv.blackboard.tools.StrokeFactory.StrokeType;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ShapePropertiesPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private JComboBox startLineCombo;
	private JComboBox thicknessCombo;
	private JComboBox endLineCombo;
	private JComboBox styleCombo;
	private JLabel lblLineEndStyle;
	private JLabel lblObject;
	private JTextField objectField;
	private JLabel lblAngle;
	private JSpinner rotationSpinner;
	private JLabel lblStart;
	private JSpinner startAngleSpinner;
	
	private Stroke lastSelectedStroke = null;
	private LineEdgeType lastSelectedLineStartStyle = null;
	private LineEdgeType lastSelectedLineEndStyle = null;
	

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public ShapePropertiesPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:50dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		lblObject = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.selectedObject"));
		add(lblObject, "3, 2, center, default");
		
		objectField = new JTextField();
		objectField.setEditable(false);
		add(objectField, "5, 2, fill, default");
		objectField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.lineStartStyle"));
		add(lblNewLabel, "3, 5, center, default");
		
		startLineCombo = new JComboBox();
		add(startLineCombo, "5, 5, fill, default");
		
		lblLineEndStyle = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.lineEndStyle"));
		add(lblLineEndStyle, "3, 7, center, center");
		
		endLineCombo = new JComboBox();
		add(endLineCombo, "5, 7, fill, default");
		endLineCombo.setRenderer(new LineEdgeTypeRenderer(Direction.FROM_RIGHT));
		
		endLineCombo.addItem(LineEdgeType.ROUND_DOT);
		endLineCombo.addItem(LineEdgeType.STRAIGHT);
		endLineCombo.addItem(LineEdgeType.SIMPLE_ARROW);
		
		JLabel lblNewLabel_1 = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.lineWidth"));
		add(lblNewLabel_1, "3, 9, center, default");
		
		thicknessCombo = new JComboBox();
		add(thicknessCombo, "5, 9, fill, default");

		JLabel lblShapeStyle = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.shapeStyle"));
		add(lblShapeStyle, "3, 11, center, default");
				
		styleCombo = new JComboBox();
		add(styleCombo, "5, 11, fill, default");
		
		
		startLineCombo.setRenderer(new LineEdgeTypeRenderer(Direction.FROM_LEFT));
		
		startLineCombo.addItem(LineEdgeType.ROUND_DOT);
		startLineCombo.addItem(LineEdgeType.STRAIGHT);
		startLineCombo.addItem(LineEdgeType.SIMPLE_ARROW);
		
		for (Float f = 1.0f;f <= 10.0f;f+=1.0f) {
			thicknessCombo.addItem(StrokeFactory.createStroke(f, StrokeType.BASIC));
		}
		for (Float f = 1.0f;f <= 10.0f;f+=1.0f) {
			thicknessCombo.addItem(StrokeFactory.createStroke(f, StrokeType.HALF_DASHED));
		}
		for (Float f = 1.0f;f <= 10.0f;f+=1.0f) {
			thicknessCombo.addItem(StrokeFactory.createStroke(f, StrokeType.DASH_DOT));
		}		
		
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		// startLineCombo.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);				
		thicknessCombo.setRenderer(new LineStrokeRenderer());
		
				
		styleCombo.addItem(ShapeStyle.FILLED);
		styleCombo.addItem(ShapeStyle.OUTLINED);
		
		styleCombo.setRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

				// Get the renderer component from parent class
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				if (value == null) {
					return label;
				}
				
				ShapeStyle style = (ShapeStyle)value;
				
				// set according style description
				if (style.equals(ShapeStyle.FILLED)) {
					label.setText(resources.getResourceByKey("ShapePropertiesPanel.style.filled"));
				}
				else {
					label.setText(resources.getResourceByKey("ShapePropertiesPanel.style.outlined"));
				}
				
				return label;
			}
			
		});
		
		lblAngle = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.rotation"));
		add(lblAngle, "3, 14, center, default");
		
		rotationSpinner = new JSpinner();
		add(rotationSpinner, "5, 14, fill, default");
		
		lblStart = new JLabel(resources.getResourceByKey("ShapePropertiesPanel.startAngle"));
		add(lblStart, "3, 16, center, default");
		
		startAngleSpinner = new JSpinner();
		add(startAngleSpinner, "5, 16, fill, default");

	}

	public JComboBox getStartLineCombo() {
		return startLineCombo;
	}
	public JComboBox getThicknessCombo() {
		return thicknessCombo;
	}
	public JComboBox getEndLineCombo() {
		return endLineCombo;
	}
	public JComboBox getStyleCombo() {
		return styleCombo;
	}

	/**
	 * @return the objectField
	 */
	public JTextField getObjectField() {
		return objectField;
	}

	/**
	 * @return the rotationSpinner
	 */
	public JSpinner getRotationSpinner() {
		return rotationSpinner;
	}

	/**
	 * @return the startAngleSpinner
	 */
	public JSpinner getStartAngleSpinner() {
		return startAngleSpinner;
	}

	/**
	 * @return the lastSelectedStroke
	 */
	public Stroke getLastSelectedStroke() {
		return lastSelectedStroke;
	}

	/**
	 * @param lastSelectedStroke the lastSelectedStroke to set
	 */
	public void setLastSelectedStroke(Stroke lastSelectedStroke) {
		this.lastSelectedStroke = lastSelectedStroke;
	}

	/**
	 * @return the lastSelectedLineStartStyle
	 */
	public LineEdgeType getLastSelectedLineStartStyle() {
		return lastSelectedLineStartStyle;
	}

	/**
	 * @param lastSelectedLineStartStyle the lastSelectedLineStartStyle to set
	 */
	public void setLastSelectedLineStartStyle(LineEdgeType lastSelectedLineStartStyle) {
		this.lastSelectedLineStartStyle = lastSelectedLineStartStyle;
	}

	/**
	 * @return the lastSelectedLineEndStyle
	 */
	public LineEdgeType getLastSelectedLineEndStyle() {
		return lastSelectedLineEndStyle;
	}

	/**
	 * @param lastSelectedLineEndStyle the lastSelectedLineEndStyle to set
	 */
	public void setLastSelectedLineEndStyle(LineEdgeType lastSelectedLineEndStyle) {
		this.lastSelectedLineEndStyle = lastSelectedLineEndStyle;
	}
	
}
