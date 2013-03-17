package org.pmedv.blackboard.panels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.pmedv.blackboard.spice.SpiceType;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SymbolPropertiesPanel extends JPanel {
	
	private final ResourceService resources;
	
	private JTextField nameTextfield;
	private RSyntaxTextArea categoryArea;
	private SymbolPreviewPanel symbolPreviewPanel;
	private JLabel lblModel;
	private JComboBox modelComboBox;
	private JLabel lblValue;
	private JTextField valueTextField;
	private JComboBox spiceTypeComboBox;
	private JCheckBox customModelCheckBox;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public SymbolPropertiesPanel() {
		
		resources = AppContext.getContext().getBean(ResourceService.class);
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
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
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		symbolPreviewPanel = new SymbolPreviewPanel();
		add(symbolPreviewPanel, "2, 3, 7, 1, fill, fill");
		
		JLabel nameLabel = new JLabel(resources.getResourceByKey("SymbolPropertiesPanel.Name"));
		add(nameLabel, "2, 5, right, default");
		
		nameTextfield = new JTextField();
		add(nameTextfield, "4, 5, 5, 1, fill, default");
		nameTextfield.setColumns(10);
		
		lblValue = new JLabel(resources.getResourceByKey("SymbolPropertiesPanel.Value"));
		add(lblValue, "2, 7, right, default");
		
		valueTextField = new JTextField();
		add(valueTextField, "4, 7, 5, 1, fill, default");
		valueTextField.setColumns(10);
		
		JLabel categoryLabel = new JLabel(resources.getResourceByKey("SymbolPropertiesPanel.Category"));
		add(categoryLabel, "2, 9, right, default");
		
		categoryArea = new RSyntaxTextArea();
		categoryArea.setRows(1);
		add(categoryArea, "4, 9, 5, 1, fill, fill");
		
		categoryArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JLabel lblCustomModel = new JLabel(resources.getResourceByKey("SymbolPropertiesPanel.CustomModel"));
		add(lblCustomModel, "2, 11");
		
		customModelCheckBox = new JCheckBox("");
		add(customModelCheckBox, "4, 11");
		
		JLabel lblSpiceType = new JLabel(resources.getResourceByKey("SymbolPropertiesPanel.SpiceType"));
		add(lblSpiceType, "6, 11, right, default");
		
		spiceTypeComboBox = new JComboBox(SpiceType.values());
		spiceTypeComboBox.setRenderer(new TypeComboBoxRenderer());
		add(spiceTypeComboBox, "8, 11, fill, default");
		
		lblModel = new JLabel(resources.getResourceByKey("SymbolPropertiesPanel.Model"));
		add(lblModel, "2, 13, right, default");
		
		modelComboBox = new JComboBox();
		add(modelComboBox, "4, 13, 5, 1, fill, default");

	}
	public RSyntaxTextArea getCategoryArea() {
		return categoryArea;
	}
	public JTextField getNameTextfield() {
		return nameTextfield;
	}
	public SymbolPreviewPanel getSymbolPreviewPanel() {
		return symbolPreviewPanel;
	}
	public JComboBox getModelComboBox() {
		return modelComboBox;
	}
	public JTextField getValueTextField() {
		return valueTextField;
	}
	public JComboBox getSpiceTypeComboBox() {
		return spiceTypeComboBox;
	}
	public JCheckBox getCustomModelCheckBox() {
		return customModelCheckBox;
	}
	
	private class TypeComboBoxRenderer extends DefaultListCellRenderer {
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			TypeComboBoxRenderer renderer = (TypeComboBoxRenderer) super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			if (value instanceof SpiceType) {
				SpiceType bt = (SpiceType) value;
				switch (bt) {
					case CAPACITOR:
						renderer.setText(resources.getResourceByKey("SpiceType.capacitor"));
						break;
					case CURRENT_CONTROLLED_SWITCH:
						renderer.setText(resources.getResourceByKey("SpiceType.currentControlledSwitch"));
						break;
					case DIODE:
						renderer.setText(resources.getResourceByKey("SpiceType.diode"));
						break;
					case INDUCTOR:
						renderer.setText(resources.getResourceByKey("SpiceType.inductor"));
						break;
					case MOSFET:
						renderer.setText(resources.getResourceByKey("SpiceType.mosfet"));
						break;
					case NPN : 
						renderer.setText(resources.getResourceByKey("SpiceType.NPN"));
						break;
					case PNP:
						renderer.setText(resources.getResourceByKey("SpiceType.PNP"));
						break;
					case RESISTOR:
						renderer.setText(resources.getResourceByKey("SpiceType.resistor"));
						break;
					case SUBCIRCUIT:
						renderer.setText(resources.getResourceByKey("SpiceType.subcircuit"));
						break;
					case VOLTAGE_CONTROLLED_SWITCH:
						renderer.setText(resources.getResourceByKey("SpiceType.voltageControlledSwitch"));
						break;
					case VOLTAGE_SOURCE:
						renderer.setText(resources.getResourceByKey("SpiceType.voltageSource"));
					default:
						break;
				}
			}
			return renderer;
		}
	}
	
}
