package org.pmedv.blackboard.spice.panels;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SpiceSimulatorPanel extends JPanel {
	
	private JTextField nameTextField;
	private JTextField paramTextField;
	private JTextField pathTextField;
	private JButton browseButton;

	private Component parent;

	private JFileChooser fileChooser;
	private JCheckBox defaultCheckBox;
	
	
	/**
	 * Create the panel.
	 */
	public SpiceSimulatorPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name");
		add(lblName, "3, 3, right, default");
		
		nameTextField = new JTextField();
		add(nameTextField, "5, 3, 7, 1, fill, default");
		nameTextField.setColumns(10);
		
		JLabel lblPath = new JLabel("Path");
		add(lblPath, "3, 5, right, default");
		
		pathTextField = new JTextField();
		add(pathTextField, "5, 5, 5, 1, fill, default");
		pathTextField.setColumns(10);
		
		browseButton = new JButton("Browse");
		add(browseButton, "11, 5");
		
		JLabel lblDefault = new JLabel("Default");
		add(lblDefault, "3, 7, right, default");
		
		defaultCheckBox = new JCheckBox("");
		add(defaultCheckBox, "5, 7");
		
		JLabel lblParameters = new JLabel("Parameters");
		add(lblParameters, "7, 7, right, default");
		
		paramTextField = new JTextField();
		add(paramTextField, "9, 7, 3, 1, fill, default");
		paramTextField.setColumns(10);

		fileChooser = new JFileChooser(System.getProperty("user.home"));
		
		browseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				final int result = fileChooser.showOpenDialog(parent);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fileChooser.getSelectedFile() == null)
						return;

					final File selectedFile = fileChooser.getSelectedFile();
				
					pathTextField.setText(selectedFile.getAbsolutePath());
					
				}				
				
			}
		});
		
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}
	public JTextField getPathTextField() {
		return pathTextField;
	}
	public JButton getBrowseButton() {
		return browseButton;
	}
	public JTextField getParamTextField() {
		return paramTextField;
	}
	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	
	public JCheckBox getDefaultCheckBox() {
		return defaultCheckBox;
	}
}
