package org.pmedv.blackboard.panels;

import java.awt.Font;
import java.awt.Label;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PinPropertiesPanel extends JPanel {

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField nameTextField;
	private JTextField numberTextField;
	private JRadioButton topLeftButton;
	private JRadioButton topRightButton;
	private JRadioButton bottomLeftButton;
	private JRadioButton bottomRightButton;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	
	/**
	 * Create the panel.
	 */
	public PinPropertiesPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name");
		add(lblName, "4, 4");
		
		nameTextField = new JTextField();
		add(nameTextField, "8, 4, fill, default");
		nameTextField.setColumns(10);
		
		JLabel lblNumber = new JLabel("Number");
		add(lblNumber, "4, 6");
		
		numberTextField = new JTextField();
		add(numberTextField, "8, 6, fill, default");
		numberTextField.setColumns(10);
		
		separator_1 = new JSeparator();
		add(separator_1, "2, 8, 7, 1");
		
		Label label = new Label("Caption orientation");
		label.setFont(new Font("Dialog", Font.BOLD, 12));
		add(label, "4, 10");
		
		separator = new JSeparator();
		add(separator, "2, 12, 7, 1");
		
		topLeftButton = new JRadioButton("Top-left");
		buttonGroup.add(topLeftButton);
		add(topLeftButton, "4, 14");
		
		topRightButton = new JRadioButton("Top-right");
		buttonGroup.add(topRightButton);
		add(topRightButton, "8, 14");
		
		bottomLeftButton = new JRadioButton("Bottom-left");
		buttonGroup.add(bottomLeftButton);
		add(bottomLeftButton, "4, 16");
		
		bottomRightButton = new JRadioButton("Bottom-right");
		buttonGroup.add(bottomRightButton);
		add(bottomRightButton, "8, 16");
		
		separator_2 = new JSeparator();
		add(separator_2, "2, 19, 7, 1");

	}
	
	public JTextField getNameTextField() {
		return nameTextField;
	}
	public JTextField getNumberTextField() {
		return numberTextField;
	}
	public JRadioButton getTopLeftButton() {
		return topLeftButton;
	}
	public JRadioButton getTopRightButton() {
		return topRightButton;
	}
	public JRadioButton getBottomLeftButton() {
		return bottomLeftButton;
	}
	public JRadioButton getBottomRightButton() {
		return bottomRightButton;
	}
}
