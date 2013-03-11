package org.pmedv.blackboard.panels;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;

import org.pmedv.blackboard.components.TextPart;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TextPropertiesPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private JTextPane textPane;
	private JSpinner fontSizeSpinner;
	private JComboBox fontComboBox;
	private JComboBox typeComboBox;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public TextPropertiesPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblFont = new JLabel(resources.getResourceByKey("TextPropertiesPanel.fontFamily"));
		add(lblFont, "2, 2, right, default");
		
		fontComboBox = new JComboBox();
		add(fontComboBox, "4, 2, 5, 1, fill, default");
		
		JLabel lblNewLabel = new JLabel(resources.getResourceByKey("TextPropertiesPanel.type"));
		add(lblNewLabel, "2, 4, right, default");
		
		typeComboBox = new JComboBox(TextPart.TextType.values());
		add(typeComboBox, "4, 4, fill, default");
		
		JLabel lblFonzSize = new JLabel(resources.getResourceByKey("TextPropertiesPanel.fontSize"));
		add(lblFonzSize, "6, 4");
		
		fontSizeSpinner = new JSpinner();
		add(fontSizeSpinner, "8, 4");
		
		JLabel lblText = new JLabel(resources.getResourceByKey("TextPropertiesPanel.text"));
		add(lblText, "2, 6");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 8, 9, 1, fill, fill");
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

	}

	public JTextPane getTextPane() {
		return textPane;
	}
	public JSpinner getFontSizeSpinner() {
		return fontSizeSpinner;
	}
	public JComboBox getFontComboBox() {
		return fontComboBox;
	}
	public JComboBox getTypeComboBox() {
		return typeComboBox;
	}
}
