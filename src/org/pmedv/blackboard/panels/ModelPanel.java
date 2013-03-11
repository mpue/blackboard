package org.pmedv.blackboard.panels;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.pmedv.blackboard.spice.SpiceType;
import java.awt.Font;

public class ModelPanel extends JPanel {
	private JTextField nameTextField;
	private RSyntaxTextArea modelEditor;
	private JComboBox typeComboBox;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public ModelPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name");
		add(lblName, "3, 2, left, default");
		
		nameTextField = new JTextField();
		add(nameTextField, "5, 2, fill, default");
		nameTextField.setColumns(10);
		
		JLabel lblType = new JLabel("Type");
		add(lblType, "3, 4, left, default");
		
		typeComboBox = new JComboBox(SpiceType.values());
		add(typeComboBox, "5, 4, fill, default");
		
		JLabel lblDefinition = new JLabel("Definition");
		lblDefinition.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblDefinition, "3, 6, 3, 1");
		
		RTextScrollPane textScrollPane = new RTextScrollPane();
		add(textScrollPane, "3, 8, 3, 1, fill, fill");
		
		modelEditor = new RSyntaxTextArea();
		textScrollPane.setViewportView(modelEditor);

	}

	public RSyntaxTextArea getModelEditor() {
		return modelEditor;
	}
	public JComboBox getTypeComboBox() {
		return typeComboBox;
	}
	public JTextField getNameTextField() {
		return nameTextField;
	}
}
