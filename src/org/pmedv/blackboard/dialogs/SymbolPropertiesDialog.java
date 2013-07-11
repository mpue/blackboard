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
package org.pmedv.blackboard.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBException;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.panels.SymbolPropertiesPanel;
import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.blackboard.provider.SymbolProvider;
import org.pmedv.blackboard.spice.FrequencyUnit;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceType;
import org.pmedv.blackboard.spice.VoltageSourceProperties;
import org.pmedv.blackboard.spice.panels.VoltageSourcePropertiesPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;

/**
 * Dialog for changing properties of a {@link Symbol}.
 * 
 * @author Matthias Pueski (16.10.2012)
 *
 */
@SuppressWarnings("serial")
public class SymbolPropertiesDialog extends AbstractNiceDialog {

	private SymbolPropertiesPanel symbolPropertiesPanel;
	private Symbol symbol;
	private RSyntaxTextArea textArea;
	
	// panel for the properties of a a voltage source
	private VoltageSourcePropertiesPanel propertiesPanel;
	
	private JTabbedPane tabPane;
	
	public SymbolPropertiesDialog(String title, String subTitle, ImageIcon icon, Symbol symbol) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), symbol);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeComponents() {
		
		propertiesPanel = new VoltageSourcePropertiesPanel();
		
		propertiesPanel.getAcAmplitudeSpinner().setModel(new SpinnerNumberModel(0, 0, 1000, 0.1));
		propertiesPanel.getAcPhaseSpinner().setModel(new SpinnerNumberModel(0, 0, 1000, 0.1));
		propertiesPanel.getDcVoltageSpinner().setModel(new SpinnerNumberModel(0, 0, 1000, 0.1));
		propertiesPanel.getDcOffsetSpinner().setModel(new SpinnerNumberModel(0, 0, 1000, 0.1));
		
		symbolPropertiesPanel = new SymbolPropertiesPanel();
		setSize(new Dimension(640, 640));
		
		ModelProvider modelProvider = AppContext.getContext().getBean(ModelProvider.class);
				
		for (Model m : modelProvider.getElements()) {			
			symbolPropertiesPanel.getModelComboBox().addItem(m);			
		}
		
		symbolPropertiesPanel.getModelComboBox().setRenderer(new ModelComboBoxRenderer());
		symbolPropertiesPanel.getCustomModelCheckBox().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox)e.getSource();				
				symbolPropertiesPanel.getModelComboBox().setEnabled(checkBox.isSelected());
			}
		});
		
		symbolPropertiesPanel.getSpiceTypeComboBox().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox)e.getSource();
				SpiceType type = (SpiceType)comboBox.getSelectedItem();
				tabPane.setEnabledAt(2, null != type && type.equals(SpiceType.VOLTAGE_SOURCE));								
			}
		});
		
		tabPane = new JTabbedPane();
		tabPane.addTab("Symbol", symbolPropertiesPanel);
		tabPane.setTabPlacement(JTabbedPane.BOTTOM);
		tabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				if (tabPane.getSelectedIndex() == 0) {
					
					SymbolBean s = null;
					
					try {
						s = (SymbolBean)AppContext.getContext().getBean(SymbolProvider.class).getUnmarshaller().unmarshal(new StringReader(textArea.getText()));
					}
					catch (JAXBException e1) {
						ErrorUtils.showErrorDialog(e1);
						return;
					}					
					if (s != null) {
						symbol = new Symbol(s);
						symbolPropertiesPanel.getSymbolPreviewPanel().setSymbol(symbol);
						symbolPropertiesPanel.getSymbolPreviewPanel().invalidate();
						symbolPropertiesPanel.getSymbolPreviewPanel().repaint();
					}					
				}
				else if (tabPane.getSelectedIndex() == 1) {
					
					try {
						StringWriter sw = new StringWriter();
						AppContext.getContext().getBean(SymbolProvider.class).getMarshaller().marshal(new SymbolBean(symbol),sw);
						textArea.setText(sw.toString());
					}
					catch (JAXBException e1) {
						ErrorUtils.showErrorDialog(e1);
					}
					
				}
				
			}
		});
		
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        RTextScrollPane textScrollPane = new RTextScrollPane(textArea);
        
        CompletionProvider provider = createSymbolCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(textArea);		
		tabPane.addTab("XML", textScrollPane);		
		tabPane.addTab(resources.getResourceByKey("SymbolPropertiesDialog.VoltageSourceProperties"), propertiesPanel);
		
		getContentPanel().add(tabPane);
		
		if (getUserObject() != null && getUserObject() instanceof Symbol) {
			this.symbol = (Symbol) getUserObject();						
			symbolPropertiesPanel.getNameTextfield().setText(symbol.getName());
			symbolPropertiesPanel.getValueTextField().setText(symbol.getValue());
			symbolPropertiesPanel.getCategoryArea().setText(symbol.getCategory());
			symbolPropertiesPanel.getSymbolPreviewPanel().setSymbol(symbol);			
			symbolPropertiesPanel.getCustomModelCheckBox().setSelected(symbol.isCustomModel());						
			symbolPropertiesPanel.getModelComboBox().setEnabled(symbol.isCustomModel());
			symbolPropertiesPanel.getSpiceTypeComboBox().setSelectedItem(symbol.getType());
			
			if (symbol.isCustomModel()) {
				symbolPropertiesPanel.getModelComboBox().setSelectedItem(symbol.getModel());				
			}
			else {
			}
			
			textArea.setText(symbol.getXmlContent());
			
			if (null != symbol.getType() && symbol.getType().equals(SpiceType.VOLTAGE_SOURCE)) {				
				getVoltageProperties();
			}
		}
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				symbol.setName(symbolPropertiesPanel.getNameTextfield().getText());
				symbol.setValue(symbolPropertiesPanel.getValueTextField().getText());
				symbol.setCategory(symbolPropertiesPanel.getCategoryArea().getText());
				symbol.setCustomModel(symbolPropertiesPanel.getCustomModelCheckBox().isSelected());
				symbol.setType((SpiceType)symbolPropertiesPanel.getSpiceTypeComboBox().getSelectedItem());
				
				if (symbol.isCustomModel()) {
					symbol.setModel((Model)symbolPropertiesPanel.getModelComboBox().getSelectedItem());
				}
				else { 
					symbol.setModel(null);
				}
				
				if (null != symbol.getType() && symbol.getType().equals(SpiceType.VOLTAGE_SOURCE)) {					
					setVoltageProperties();					
				}
			
				result = OPTION_OK;
				setVisible(false);
				dispose();
			}

		});
		getCancelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
				dispose();
			}
		});
		
		setupAutoComplete();
	}

	private void setVoltageProperties() {
		
		if (symbol.getProperties() == null) {
			symbol.setProperties(new Properties());
		}
		
		if (propertiesPanel.getDcRadioButton().isSelected()) {
			symbol.getProperties().put(VoltageSourceProperties.MODE, "DC");						
		}
		else {
			symbol.getProperties().put(VoltageSourceProperties.MODE, "AC");
		}
		symbol.getProperties().put(VoltageSourceProperties.DC_VOLTAGE, propertiesPanel.getDcVoltageSpinner().getValue().toString());
		symbol.getProperties().put(VoltageSourceProperties.AC_PHASE, propertiesPanel.getAcPhaseSpinner().getValue().toString());
		symbol.getProperties().put(VoltageSourceProperties.AC_AMPLITUDE, propertiesPanel.getAcAmplitudeSpinner().getValue().toString());
		symbol.getProperties().put(VoltageSourceProperties.FREQUENCY, propertiesPanel.getFrequencySpinner().getValue().toString());
		symbol.getProperties().put(VoltageSourceProperties.FREQUENCY_UNIT, propertiesPanel.getFrequencyUnitComboBox().getSelectedItem().toString());
		symbol.getProperties().put(VoltageSourceProperties.DC_OFFSET, propertiesPanel.getDcOffsetSpinner().getValue().toString());
		symbol.getProperties().put(VoltageSourceProperties.DUTY_CYCLE, propertiesPanel.getDutyCycleSpinner().getValue().toString());
		
		if (propertiesPanel.getSineShapeRadioButton().isSelected()) {
			symbol.getProperties().put(VoltageSourceProperties.SHAPE,"SIN");						
		}
		else {
			symbol.getProperties().put(VoltageSourceProperties.SHAPE,"PULSE");
		}
	}
	
	private void getVoltageProperties() {
		
		try {
			if (symbol.getProperties().getProperty(VoltageSourceProperties.MODE).equalsIgnoreCase("DC")) {
				propertiesPanel.getDcRadioButton().setSelected(true);
			}
			else if (symbol.getProperties().getProperty(VoltageSourceProperties.MODE).equalsIgnoreCase("AC")) {
				propertiesPanel.getAcRadioButton().setSelected(true);
			}
		}
		catch (NullPointerException n) {
			propertiesPanel.getDcRadioButton().setSelected(true);
		} 	
		
		try {
			Float dcVoltage = Float.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.DC_VOLTAGE));
			propertiesPanel.getDcVoltageSpinner().setValue(dcVoltage);
		}
		catch (Exception e) {
			propertiesPanel.getDcVoltageSpinner().setValue(0);
		}
		
		try {
			Float acPhase = Float.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.AC_PHASE));
			propertiesPanel.getAcPhaseSpinner().setValue(acPhase);
		}
		catch (Exception e) {
			propertiesPanel.getAcPhaseSpinner().setValue(0);
		}
		
		try {
			Float acAmplitude = Float.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.AC_AMPLITUDE));
			propertiesPanel.getAcAmplitudeSpinner().setValue(acAmplitude);
		}
		catch (Exception e) {
			propertiesPanel.getAcAmplitudeSpinner().setValue(0);
		}

		try {
			Float frequency = Float.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.FREQUENCY));
			propertiesPanel.getFrequencySpinner().setValue(frequency);
		}
		catch (Exception e) {
			propertiesPanel.getFrequencySpinner().setValue(0);
		}
		
		try {
			FrequencyUnit unit = FrequencyUnit.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.FREQUENCY_UNIT)); 
			propertiesPanel.getFrequencyUnitComboBox().setSelectedItem(unit);
		}
		catch (Exception e) {
			propertiesPanel.getFrequencyUnitComboBox().setSelectedIndex(0);
		}
		
		try {
			Float dcOffset = Float.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.DC_OFFSET));
			propertiesPanel.getDcOffsetSpinner().setValue(dcOffset);
		}
		catch (Exception e) {
			propertiesPanel.getDcOffsetSpinner().setValue(0);
		}
		
		try {
			Integer dutyCycle = Integer.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.DUTY_CYCLE));
			propertiesPanel.getDutyCycleSpinner().setValue(dutyCycle);
		}
		catch (Exception e) {
			propertiesPanel.getDutyCycleSpinner().setValue(50);
		}
		
		try {
			if (symbol.getProperties().getProperty(VoltageSourceProperties.SHAPE).equalsIgnoreCase("SIN")) {
				propertiesPanel.getSineShapeRadioButton().setSelected(true);
			}
			else if (symbol.getProperties().getProperty(VoltageSourceProperties.SHAPE).equalsIgnoreCase("PULSE")) {
				propertiesPanel.getPulseShapeRadioButton().setSelected(true);
			}
		}
		catch (Exception e) {
			propertiesPanel.getSineShapeRadioButton().setSelected(true);
		}
	}
	
	private void setupAutoComplete() {
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(symbolPropertiesPanel.getCategoryArea());		
	}

	private CompletionProvider createCompletionProvider() {
		
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		SymbolProvider symbolProvider = AppContext.getContext().getBean(SymbolProvider.class);
		
		for (String category : symbolProvider.getCategories()) {
			if (category != null) {
				provider.addCompletion(new BasicCompletion(provider, category));							
			}
		}
		
		return provider;

	}

	private CompletionProvider createSymbolCompletionProvider() {
		
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		// provider.addCompletion(new BasicCompletion(provider, category));							
		
		return provider;

	}	
	
	/**
	 * @return the symbol
	 */
	public Symbol getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the symbolPropertiesPanel
	 */
	public SymbolPropertiesPanel getSymbolPropertiesPanel() {
		return symbolPropertiesPanel;
	}	
	
	private static class ModelComboBoxRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			
			ModelComboBoxRenderer renderer = (ModelComboBoxRenderer) super.getListCellRendererComponent(
					list, value, index, isSelected, cellHasFocus);

			if (value instanceof Model) {
				Model model = (Model)value;
				setText(model.getName());				
			}

			return renderer;

		}

	}
	
}
