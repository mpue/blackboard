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
import org.pmedv.blackboard.spice.TimeUnit;
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
	private JSpinner riseTimeSpinner;
	private JSpinner fallTimeSpinner;
	private JComboBox riseTimeUnitCombo;
	private JComboBox fallTimeUnitCombo;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public VoltageSourcePropertiesPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(50dlu;min)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(50dlu;min)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(50dlu;min)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(50dlu;min)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(34dlu;min)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow"),
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
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblVoltageSourceProperties = new JLabel(resources.getResourceByKey("SymbolPropertiesDialog.VoltageSourceProperties"));
		lblVoltageSourceProperties.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblVoltageSourceProperties, "4, 2, 15, 1, fill, default");
		
		JSeparator separator = new JSeparator();
		add(separator, "2, 4, 17, 1");
		
		JLabel lblType = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.mode"));
		add(lblType, "4, 6, 3, 1");
		
		JLabel lblShape = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.shape"));
		add(lblShape, "12, 6, 3, 1");
		
		dcRadioButton = new JRadioButton("DC");
		voltageTypeGroup.add(dcRadioButton);
		add(dcRadioButton, "4, 8");
		
		acRadioButton = new JRadioButton("AC");
		voltageTypeGroup.add(acRadioButton);
		add(acRadioButton, "6, 8");
		
		sineShapeRadioButton = new JRadioButton(resources.getResourceByKey("VoltageSourcePropertiesPanel.sine"));
		shapeGroup.add(sineShapeRadioButton);
		add(sineShapeRadioButton, "12, 8");
		
		pulseShapeRadioButton = new JRadioButton(resources.getResourceByKey("VoltageSourcePropertiesPanel.pulse"));
		shapeGroup.add(pulseShapeRadioButton);
		add(pulseShapeRadioButton, "14, 8");
		
		JLabel lblNewLabel = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.dcVoltage"));
		add(lblNewLabel, "4, 10, 3, 1");
		
		JLabel lblDcOffset = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.dcOffset"));
		add(lblDcOffset, "12, 10, 3, 1");
		
		dcVoltageSpinner = new JSpinner();
		add(dcVoltageSpinner, "4, 12, 3, 1");
		
		JLabel lblVolt = new JLabel("Volt");
		add(lblVolt, "8, 12");
		
		dcOffsetSpinner = new JSpinner();
		add(dcOffsetSpinner, "12, 12, 3, 1");
		
		JLabel lblVolt_2 = new JLabel("Volt");
		add(lblVolt_2, "16, 12, 3, 1");
		
		JLabel lblPhase = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.acPhase"));
		add(lblPhase, "4, 14, 3, 1");
		
		JLabel lblAcAmplitude = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.acAmplitude"));
		add(lblAcAmplitude, "12, 14, 3, 1");
		
		acPhaseSpinner = new JSpinner();
		add(acPhaseSpinner, "4, 16, 3, 1");
		
		JLabel lblDegrees = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.degrees"));
		add(lblDegrees, "8, 16");
		
		acAmplitudeSpinner = new JSpinner();
		add(acAmplitudeSpinner, "12, 16, 3, 1");
		
		JLabel lblVolt_1 = new JLabel("Volt");
		add(lblVolt_1, "16, 16, 3, 1");
		
		JLabel lblFrequency = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.frequency"));
		add(lblFrequency, "4, 18, 3, 1");
		
		JLabel lblDutyCycle = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.duty"));
		add(lblDutyCycle, "12, 18, 3, 1");
		
		frequencySpinner = new JSpinner();
		add(frequencySpinner, "4, 20, 3, 1");
		
		frequencyUnitComboBox = new JComboBox(FrequencyUnit.values());
		add(frequencyUnitComboBox, "8, 20, fill, default");
		
		dutyCycleSpinner = new JSpinner();
		add(dutyCycleSpinner, "12, 20, 3, 1");
		
		JLabel label = new JLabel("%");
		add(label, "16, 20, 3, 1");
		
		JLabel lblRiseTime = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.riseTime"));
		add(lblRiseTime, "12, 22");
		
		riseTimeSpinner = new JSpinner();
		add(riseTimeSpinner, "12, 24, 3, 1");
		
		riseTimeUnitCombo = new JComboBox(TimeUnit.values());
		add(riseTimeUnitCombo, "16, 24, fill, default");
		
		JLabel lblFallTime = new JLabel(resources.getResourceByKey("VoltageSourcePropertiesPanel.fallTime"));
		add(lblFallTime, "12, 26");
		
		fallTimeSpinner = new JSpinner();
		add(fallTimeSpinner, "12, 28, 3, 1");
		
		fallTimeUnitCombo = new JComboBox(TimeUnit.values());
		add(fallTimeUnitCombo, "16, 28, fill, default");

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
	public JSpinner getRiseTimeSpinner() {
		return riseTimeSpinner;
	}
	public JSpinner getFallTimeSpinner() {
		return fallTimeSpinner;
	}
	public JComboBox getRiseTimeUnitCombo() {
		return riseTimeUnitCombo;
	}
	public JComboBox getFallTimeUnitCombo() {
		return fallTimeUnitCombo;
	}
}
