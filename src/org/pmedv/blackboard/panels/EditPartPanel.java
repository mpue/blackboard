package org.pmedv.blackboard.panels;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.pmedv.core.components.FileBrowserTextfield;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EditPartPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private JTextField partNameTextField;
	private JTextField packageTypeTextField;
	private JTextField authorTextField;
	private JTextField licenseTextField;
	private JTextArea descriptionTextArea;
	private FileBrowserTextfield fileBrowserTextField;
	private JSpinner heightSpinner;
	private JSpinner widthSpinner;
	private JTextField designatorTextField;

	/**
	 * Create the panel.
	 */
	public EditPartPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
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
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblPartData = new JLabel(resources.getResourceByKey("EditPartPanel.partData"));
		lblPartData.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblPartData, "2, 2");
		
		JSeparator separator_3 = new JSeparator();
		add(separator_3, "2, 4, 9, 1");
		
		JLabel lblPartName = new JLabel(resources.getResourceByKey("EditPartPanel.partName"));
		add(lblPartName, "2, 6, left, default");
		
		partNameTextField = new JTextField();
		add(partNameTextField, "4, 6, 7, 1, fill, default");
		partNameTextField.setColumns(10);
		
		JLabel lblPackackeType = new JLabel(resources.getResourceByKey("EditPartPanel.packageType"));
		add(lblPackackeType, "2, 8, left, default");
		
		packageTypeTextField = new JTextField();
		add(packageTypeTextField, "4, 8, 7, 1, fill, default");
		packageTypeTextField.setColumns(10);
		
		JLabel lblAuthor = new JLabel(resources.getResourceByKey("EditPartPanel.author"));
		add(lblAuthor, "2, 10, left, default");
		
		authorTextField = new JTextField();
		add(authorTextField, "4, 10, 7, 1, fill, default");
		authorTextField.setColumns(10);
		
		JLabel lblLicense = new JLabel(resources.getResourceByKey("EditPartPanel.license"));
		add(lblLicense, "2, 12, left, default");
		
		licenseTextField = new JTextField();
		add(licenseTextField, "4, 12, 7, 1, fill, default");
		licenseTextField.setColumns(10);
		
		JLabel lblDesignator = new JLabel(resources.getResourceByKey("EditPartPanel.designator"));
		add(lblDesignator, "2, 14, left, default");
		
		designatorTextField = new JTextField();
		add(designatorTextField, "4, 14, fill, default");
		designatorTextField.setColumns(10);
		
		JSeparator separator_4 = new JSeparator();
		add(separator_4, "2, 16, 9, 1");
		
		JLabel lblPartDimensions = new JLabel(resources.getResourceByKey("EditPartPanel.dimensionsAndImage"));
		lblPartDimensions.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblPartDimensions, "2, 18, 3, 1");
		
		JSeparator separator = new JSeparator();
		add(separator, "2, 20, 9, 1");
		
		JLabel lblHeightpx = new JLabel(resources.getResourceByKey("EditPartPanel.imageHeight"));
		add(lblHeightpx, "2, 22");
		
		heightSpinner = new JSpinner();
		add(heightSpinner, "4, 22");
		
		JLabel lblWidthpx = new JLabel(resources.getResourceByKey("EditPartPanel.imageWidth"));
		add(lblWidthpx, "6, 22");
		
		widthSpinner = new JSpinner();
		add(widthSpinner, "8, 22");
		
		JLabel lblImage = new JLabel(resources.getResourceByKey("EditPartPanel.imageFile"));
		add(lblImage, "2, 24");
		
		fileBrowserTextField = new FileBrowserTextfield();
		add(fileBrowserTextField, "4, 24, 7, 1, fill, fill");
		
		JSeparator separator_2 = new JSeparator();
		add(separator_2, "2, 26, 9, 1");
		
		JLabel lblDescription = new JLabel(resources.getResourceByKey("EditPartPanel.description"));
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblDescription, "2, 28, left, default");
		
		JSeparator separator_1 = new JSeparator();
		add(separator_1, "2, 30, 9, 1");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 32, 9, 1, fill, fill");
		
		descriptionTextArea = new JTextArea();
		scrollPane.setViewportView(descriptionTextArea);

	}

	public JTextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}
	public FileBrowserTextfield getFileBrowserTextField() {
		return fileBrowserTextField;
	}
	public JTextField getLicenseTextField() {
		return licenseTextField;
	}
	public JTextField getAuthorTextField() {
		return authorTextField;
	}
	public JTextField getPackageTypeTextField() {
		return packageTypeTextField;
	}
	public JTextField getPartNameTextField() {
		return partNameTextField;
	}
	public JSpinner getHeightSpinner() {
		return heightSpinner;
	}
	public JSpinner getWidthSpinner() {
		return widthSpinner;
	}
	public JTextField getDesignatorTextField() {
		return designatorTextField;
	}
}
