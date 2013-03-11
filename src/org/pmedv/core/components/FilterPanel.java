/*
 * Created by JFormDesigner on Wed Sep 15 12:07:57 CEST 2010
 */

package org.pmedv.core.components;

import java.util.ResourceBundle;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXSearchField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
@SuppressWarnings("unused")
public class FilterPanel extends JPanel {
	
	private static final long serialVersionUID = 5616053764681382891L;

	public FilterPanel() {
		initComponents();
	}

	private void initComponents() {
		ResourceBundle bundle = ResourceBundle.getBundle("org.pmedv.core.MessageResources");
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		filterTextField = new JXSearchField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"right:39dlu:grow, $lcgap, 131px, 2*($lcgap)",
			"$lgap, default"));

		//---- filterTextField ----
		filterTextField.setPrompt("Filter");
		add(filterTextField, cc.xy(3, 2));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JXSearchField filterTextField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	public JXSearchField getFilterTextField() {
		return filterTextField;
	}

}
