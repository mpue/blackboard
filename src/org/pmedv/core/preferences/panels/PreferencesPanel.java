/*
 * Created by JFormDesigner on Wed Sep 01 11:40:26 CEST 2010
 */

package org.pmedv.core.preferences.panels;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
public class PreferencesPanel extends JPanel {
	public PreferencesPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		splitPane = new JSplitPane();
		scrollPane1 = new JScrollPane();
		moduleTree = new JTree();
		scrollPane2 = new JScrollPane();
		optionsPanel = new JPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), default:grow, 2*($lcgap)",
			"2*($lgap), default:grow, 2*($lgap)"));

		//======== splitPane ========
		{
			splitPane.setDividerLocation(100);

			//======== scrollPane1 ========
			{
				scrollPane1.setViewportView(moduleTree);
			}
			splitPane.setLeftComponent(scrollPane1);

			//======== scrollPane2 ========
			{

				//======== optionsPanel ========
				{
					optionsPanel.setLayout(new GridBagLayout());
					((GridBagLayout)optionsPanel.getLayout()).columnWidths = new int[] {0, 0};
					((GridBagLayout)optionsPanel.getLayout()).rowHeights = new int[] {0, 0};
					((GridBagLayout)optionsPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
					((GridBagLayout)optionsPanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
				}
				scrollPane2.setViewportView(optionsPanel);
			}
			splitPane.setRightComponent(scrollPane2);
		}
		add(splitPane, cc.xywh(2, 2, 3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JSplitPane splitPane;
	private JScrollPane scrollPane1;
	private JTree moduleTree;
	private JScrollPane scrollPane2;
	private JPanel optionsPanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	/**
	 * @return the moduleTree
	 */
	public JTree getModuleTree() {
		return moduleTree;
	}
	
	/**
	 * @return the optionsPanel
	 */
	public JPanel getOptionsPanel() {
		return optionsPanel;
	}

	
	/**
	 * @return the splitPane
	 */
	public JSplitPane getSplitPane() {
		return splitPane;
	}




}
