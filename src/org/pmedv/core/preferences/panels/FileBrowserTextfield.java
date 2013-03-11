/*
 * Created by JFormDesigner on Mon Sep 06 09:06:11 CEST 2010
 */

package org.pmedv.core.preferences.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
public class FileBrowserTextfield extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9209620854884015479L;


	public FileBrowserTextfield() {
		initComponents();
		
		browseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Open file");

				int result = fc.showOpenDialog(AppContext.getContext().getBean(ApplicationWindow.class));

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					final File selectedFile = fc.getSelectedFile();
				
					pathField.setText(selectedFile.getAbsolutePath());
					
				}				
				
			}
		});
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		pathField = new JTextField();
		browseButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow, $lcgap, default, 2*($lcgap)",
			"fill:default"));
		add(pathField, cc.xy(1, 1));

		//---- browseButton ----
		browseButton.setText("Browse...");
		add(browseButton, cc.xy(3, 1));
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	private JTextField pathField;
	private JButton browseButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	
	
	/**
	 * @return the pathField
	 */
	public JTextField getPathField() {
		return pathField;
	}


}
