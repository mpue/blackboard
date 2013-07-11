package org.pmedv.blackboard.spice.panels;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;

import org.pmedv.blackboard.spice.FrequencyUnit;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class VoltageSourcePropertiesPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private JRadioButton dcRadioButton;
	private JRadioButton acRadioButton;
	private JSpinner dcVoltageSpinner;
	private JSpinner acPhaseSpinner;
	private JSpinner acAmplitudeSpinner;
	private JSpinner frequencySpinner;
	private JComboBox frequencyUnitComboBox;
	private JSpinner dcOffsetSpinner;
	private JRadioButton sineShapeRadioButton;
	private JRadioButton pulseShapeRadioButton;
	private final ButtonGroup voltageTypeGroup = new ButtonGroup();
	private final ButtonGroup shapeGroup = new ButtonGroup();
	private JSpinner dutyCycleSpinner;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public VoltageSourcePropertiesPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("3dlu:grow"),
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
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblVoltageSourceProperties = new JLabel(resources.getResourceByKey("SymbolPropertiesDialog.VoltageSourceProperties"));
		lblVoltageSourceProperties.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblVoltageSourceProperties, "4, 2, 9, 1");
		
		JSeparator separator = new JSeparator();
		add(separator, "2, 4, 11, 1");
		
		JLabel lblType = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.mode"));
		add(lblType, "4, 6");
		
		dcRadioButton = new JRadioButton("DC");
		voltageTypeGroup.add(dcRadioButton);
		add(dcRadioButton, "6, 6");
		
		acRadioButton = new JRadioButton("AC");
		voltageTypeGroup.add(acRadioButton);
		add(acRadioButton, "8, 6");
		
		JLabel lblNewLabel = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.dcVoltage"));
		add(lblNewLabel, "4, 8");
		
		dcVoltageSpinner = new JSpinner();
		add(dcVoltageSpinner, "6, 8, 3, 1");
		
		JLabel lblVolt = new JLabel("Volt");
		add(lblVolt, "10, 8");
		
		JLabel lblPhase = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.acPhase"));
		add(lblPhase, "4, 10");
		
		acPhaseSpinner = new JSpinner();
		add(acPhaseSpinner, "6, 10, 3, 1");
		
		JLabel lblDegrees = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.degrees"));
		add(lblDegrees, "10, 10");
		
		JLabel lblAcAmplitude = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.acAmplitude"));
		add(lblAcAmplitude, "4, 12");
		
		acAmplitudeSpinner = new JSpinner();
		add(acAmplitudeSpinner, "6, 12, 3, 1");
		
		JLabel lblVolt_1 = new JLabel("Volt");
		add(lblVolt_1, "10, 12");
		
		JLabel lblFrequency = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.frequency"));
		add(lblFrequency, "4, 14");
		
		frequencySpinner = new JSpinner();
		add(frequencySpinner, "6, 14, 3, 1");
		
		frequencyUnitComboBox = new JComboBox(FrequencyUnit.values());
		add(frequencyUnitComboBox, "10, 14, fill, default");
		
		JLabel lblDcOffset = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.dcOffset"));
		add(lblDcOffset, "4, 16");
		
		dcOffsetSpinner = new JSpinner();
		add(dcOffsetSpinner, "6, 16, 3, 1");
		
		JLabel lblVolt_2 = new JLabel("Volt");
		add(lblVolt_2, "10, 16");
		
		JLabel lblDutyCycle = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.duty"));
		add(lblDutyCycle, "4, 18");
		
		dutyCycleSpinner = new JSpinner();
		add(dutyCycleSpinner, "6, 18, 3, 1");
		
		JLabel label = new JLabel("%");
		add(label, "10, 18");
		
		JLabel lblShape = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.shape"));
		add(lblShape, "4, 20");
		
		sineShapeRadioButton = new JRadioButton(resources.getResourceByKey("VoltageSourcePropertiesPanel.sine"));
		shapeGroup.add(sineShapeRadioButton);
		add(sineShapeRadioButton, "6, 20");
		
		pulseShapeRadioButton = new JRadioButton(resources.getResourceByKey("VoltageSourcePropertiesPanel.pulse"));
		shapeGroup.add(pulseShapeRadioButton);
		add(pulseShapeRadioButton, "8, 20");

	}

	public JRadioButton getDcRadioButton() {
		return dcRadioButton;
	}
	public JRadioButton getAcRadioButton() {
		return acRadioButton;
	}
	public JSpinner getDcVoltageSpinner() {
		return dcVoltageSpinner;
	}
	public JSpinner getAcPhaseSpinner() {
		return acPhaseSpinner;
	}
	public JSpinner getAcAmplitudeSpinner() {
		return acAmplitudeSpinner;
	}
	public JSpinner getFrequencySpinner() {
		return frequencySpinner;
	}
	public JComboBox getFrequencyUnitComboBox() {
		return frequencyUnitComboBox;
	}
	public JSpinner getDcOffsetSpinner() {
		return dcOffsetSpinner;
	}
	public JRadioButton getSineShapeRadioButton() {
		return sineShapeRadioButton;
	}
	public JRadioButton getPulseShapeRadioButton() {
		return pulseShapeRadioButton;
	}	
	public JSpinner getDutyCycleSpinner() {
		return dutyCycleSpinner;
	}
}
