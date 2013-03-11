package org.pmedv.core.preferences.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.core.beans.ApplicationPerspective;
import org.pmedv.core.beans.FileObject;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.preferences.models.ModuleTreeModel;
import org.pmedv.core.preferences.panels.FileBrowserTextfield;
import org.pmedv.core.preferences.panels.PreferencesPanel;
import org.pmedv.core.preferences.renderer.ModuleTreeRenderer;
import org.pmedv.core.provider.ApplicationPerspectiveProvider;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.util.UIUtils;

/**
 * <p>
 * The <code>PreferencesDialog</code> allows the user to change the client side preferences
 * </p>
 * <p>
 * This dialog provides an automatism in order to create a panel for any set of properties taken
 * from the {@link Preferences}.
 * </p>
 * <p>
 * A string property is written to the {@link Preferences} as follows:
 * </p>
 * <p>
 * <pre>
 *   &lt;void method="put"&gt;
 *     &lt;string&gt;com.iomedico.ctms.email.EmailView.defaultEmailFormat&lt;/string&gt;
 *     &lt;string&gt;text/html&lt;/string&gt;
 *  &lt;/void&gt;
 * </pre>
 * </p>
 * <p>
 * If one would like to provide a collection of possible values this is done for the
 * string property above as follows:
 * </p>
 * <p>
 * <pre>
 *   &lt;void method="put"&gt;
 *      &lt;string&gt;com.iomedico.ctms.email.EmailView.defaultEmailFormat.optionValues&lt;/string&gt;
 *      &lt;string&gt;text/html,text/plain&lt;/string&gt;
 *  &lt;/void&gt;
 * </pre>
 * </p>
 * <p>
 *    As one can see, the property is bound to the full qualified name of a class. Provided
 *    options, must be provided with the the full name of the class, the property name and a trailing
 *    &quot;.optionValues&quot;. The possible values are provided as comma separated strings.
 *    If the dialog detects the &quot;optionValues&quot; property for a property, it generates automatically a
 *    {@link JComboBox}.
 * </p>
 * <p>
 * 	  For each property, the according components are mapped automatically to the following JComponents:
 * <ul>
 * 	<li>String properties are mapped to {@link JTextField}</li>
 *  <li>String properties with optionValues are mapped to {@link JComboBox}</li>
 *  <li>Integer properties are mapped to {@link JSpinner}</li>
 *  <li>Boolean Properties are mapped to {@link JCheckBox}</li>
 *  <li>{@link FileObject} properties are mapped to {@link FileBrowserTextfield}
 * </ul>
 * 
 * @author Matthias Pueski
 * 
 */
@SuppressWarnings("rawtypes")
public class PreferencesDialog extends AbstractNiceDialog {

	private HashMap<String, JPanel> panels;

	private static final long serialVersionUID = -789645045146938191L;

	private PreferencesPanel preferencesPanel;
	private ModuleTreeModel model;

	private ResourceService resources;

	public PreferencesDialog(String title, String subTitle, ImageIcon icon, JFrame frame) {
		super(title, subTitle, icon, true, false, true, true, frame, null);
	}

	@Override
	protected void initializeComponents() {

		final Dimension size = new Dimension(800,500);
		setSize(size);
		setBounds(new Rectangle(size));

		panels = new HashMap<String, JPanel>();
		resources = AppContext.getContext().getBean(ResourceService.class);
	
		preferencesPanel = new PreferencesPanel();
		model = new ModuleTreeModel(new DefaultMutableTreeNode(resources.getResourceByKey("perspectives.text")));
		preferencesPanel.getModuleTree().setModel(model);
		preferencesPanel.getModuleTree().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		UIUtils.flattenSplitPane(preferencesPanel.getSplitPane());
		preferencesPanel.getModuleTree().setCellRenderer(new ModuleTreeRenderer());
		preferencesPanel.getModuleTree().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				Object o = node.getUserObject();

				if (o instanceof String) {

					String moduleClassName = (String) o;
					preferencesPanel.getSplitPane().setDividerLocation(0.3);
					preferencesPanel.getSplitPane().setRightComponent(new JScrollPane(panels.get(moduleClassName)));					

				}

			}
		});

		getContentPanel().add(preferencesPanel, BorderLayout.CENTER);
	
		
		ApplicationPerspectiveProvider perspectiveProvider = ctx.getBean(ApplicationPerspectiveProvider.class);
		ArrayList<ApplicationPerspective> perspectives = perspectiveProvider.getPerspectives();
		
		for (ApplicationPerspective a  : perspectives) {			
			model.addModule((DefaultMutableTreeNode) preferencesPanel.getModuleTree().getModel().getRoot(), a.getPerspectiveClass());
			panels.put(a.getPerspectiveClass(), createParamPanel(a.getPerspectiveClass(), Preferences.values));
		}
		
		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				syncPreferences();
				result = OPTION_OK;
				setVisible(false);
				Preferences.store();
				
				for (BoardEditor ed : EditorUtils.getCurrentPerspectiveOpenEditors(AppContext.getContext()
						.getBean(ApplicationWindowAdvisor.class).getCurrentPerspective()))
					ed.updateStatusBar();
				
			}

		});

		getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
			}

		});

		setLocationRelativeTo(getRootPane());

		// preferencesPanel.getSplitPane().setRightComponent(new JScrollPane(panels.get("org.pmedv.blackboard.BoardDesignerPerspective")));				
				

	}
	
	@Override
	public void setVisible(boolean b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				preferencesPanel.getModuleTree().setSelectionInterval(1, 1);				
			}
			
		});				
		super.setVisible(b);
	}

	/**
	 * Stores the preferences back from the panel to the {@link GWDefaults}
	 */
	private void syncPreferences() {

		for (String key : panels.keySet()) {

			JPanel pane = panels.get(key);

			int numComponents = pane.getComponentCount();

			for (int i = 0; i < numComponents; i++) {

				String propertyName = pane.getComponent(i).getName();

				if (pane.getComponent(i) instanceof JTextField) {
					JTextField textField = (JTextField) pane.getComponent(i);
					String value = textField.getText();
					Preferences.values.put(propertyName, value);
				}
				else if (pane.getComponent(i) instanceof JSpinner) {
					JSpinner spinner = (JSpinner) pane.getComponent(i);
					Integer value = (Integer) spinner.getValue();
					Preferences.values.put(propertyName, value);
				}
				else if (pane.getComponent(i) instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) pane.getComponent(i);
					Boolean value = Boolean.valueOf(checkBox.isSelected());
					Preferences.values.put(propertyName, value);
				}
				else if (pane.getComponent(i) instanceof JComboBox) {
					JComboBox comboBox = (JComboBox) pane.getComponent(i);
					String value = (String) comboBox.getSelectedItem();
					Preferences.values.put(propertyName, value);
				}
				else if (pane.getComponent(i) instanceof FileBrowserTextfield) {
					FileBrowserTextfield field = (FileBrowserTextfield) pane.getComponent(i);
					String value = field.getPathField().getText();
					FileObject f = new FileObject(value);
					Preferences.values.put(propertyName, f);
				}

			}

		}

	}

	/**
	 * Creates the panel for a view class according to a <code>HashMap</code> of values
	 * 
	 * @param className the full qualified class name
	 * @param defaults  the defaults to create the panel for
	 * 
	 * @return a jpanel containing ui elements for a specific set of properties
	 */
	@SuppressWarnings("unchecked")
	public JPanel createParamPanel(String className, HashMap defaults) {

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridwidth = 1;
		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		
		int row = 0;
		 
		Object[] keys = defaults.keySet().toArray();
		Arrays.sort(keys); 
		
		for (int k=0;k < keys.length;k++) {

			String key = (String) keys[k];

			if (key.startsWith(className) && !key.endsWith("optionValues")) {

				constraints.gridy = row;
				constraints.gridx = 0;

				JLabel label = new JLabel(resources.getResourceByKey(key));
				panel.add(label, constraints);

				if (defaults.get(key) instanceof String) {

					if (defaults.get(key + ".optionValues") != null) {

						String optionValues = (String) defaults.get(key + ".optionValues");

						JComboBox comboBox = new JComboBox();
						comboBox.setName(key);

						String value = (String) defaults.get(key);

						String[] options = optionValues.split(",");

						for (int i = 0; i < options.length; i++) {
							comboBox.addItem(options[i]);
						}

						comboBox.setSelectedItem(value);

						constraints.gridx = 1;
						panel.add(comboBox, constraints);

					}
					else {

						JTextField textfield = new JTextField(30);
						textfield.setName(key);
						String value = (String) defaults.get(key);
						textfield.setText(value);
						constraints.gridx = 1;
						panel.add(textfield, constraints);

					}

				}
				else if (defaults.get(key) instanceof Boolean) {
					JCheckBox checkBox = new JCheckBox();
					checkBox.setName(key);
					checkBox.setBackground(Color.WHITE);
					Boolean value = (Boolean) defaults.get(key);
					checkBox.setSelected(value);
					constraints.gridx = 1;
					panel.add(checkBox, constraints);
				}
				else if (defaults.get(key) instanceof Integer) {
					JSpinner spinner = new JSpinner();
					spinner.setName(key);
					Integer value = (Integer) defaults.get(key);
					spinner.setValue(value);
					constraints.gridx = 1;
					panel.add(spinner, constraints);
				}
				else if (defaults.get(key) instanceof FileObject) {
					FileBrowserTextfield textField = new FileBrowserTextfield();
					textField.setBackground(Color.WHITE);
					textField.setName(key);
					FileObject value = (FileObject) defaults.get(key);
					textField.getPathField().setText(value.getAbsolutePath());
					textField.getPathField().setBackground(Color.WHITE);
					constraints.gridx = 1;
					panel.add(textField, constraints);
				}

				row++;

			}

		}

		/**
		 * Add fill label for GridBagLayout
		 */
		
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		panel.add(new JLabel(), constraints);

		return panel;

	}



}
