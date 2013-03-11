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
package org.pmedv.blackboard.panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.pmedv.blackboard.Colors;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.components.FilterPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.util.UIUtils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The main panel for the part browser.
 * 
 * @author Matthias Pueski
 */
public class PartPanel extends JPanel {

	private static final long serialVersionUID = 1108325808379937929L;
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);

	public PartPanel() {
		initComponents();		
		UIUtils.flattenSplitPane(mainSplitpane);
		UIUtils.flattenSplitPane(imageDescriptionSplitPane);
		mainSplitpane.setDividerLocation(400);
		imageDescriptionSplitPane.setDividerLocation(300);
		bottomPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		imageLabel.setText(null);
		imagePanel.setBackground(Colors.LIGHTER_GRAY);
		bottomPanel.setBackground(Color.WHITE);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		mainSplitpane = new JSplitPane();
		panel1 = new JPanel();
		filterPanel = new FilterPanel();
		scrollPane1 = new JScrollPane();
		partTable = new AlternatingLineTable();
		imageDescriptionSplitPane = new JSplitPane();
		imagePanel = new JPanel();
		imageLabel = new JLabel();
		bottomPanel = new JPanel();
		label1 = new JLabel();
		partNameField = new JTextField();
		label2 = new JLabel();
		packageTypeField = new JTextField();
		label4 = new JLabel();
		authorField = new JTextField();
		label5 = new JLabel();
		licenseField = new JTextField();
		label3 = new JLabel();
		scrollPane2 = new JScrollPane();
		descriptionPane = new JEditorPane();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), default:grow, 2*($lcgap)",
			"2*($lgap), default:grow, 2*($lgap)"));

		//======== mainSplitpane ========
		{

			//======== panel1 ========
			{
				panel1.setLayout(new FormLayout(
					"default:grow",
					"default, $lgap, fill:default:grow"));
				panel1.add(filterPanel, cc.xy(1, 1));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(partTable);
				}
				panel1.add(scrollPane1, cc.xy(1, 3));
			}
			mainSplitpane.setLeftComponent(panel1);

			//======== imageDescriptionSplitPane ========
			{
				imageDescriptionSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

				//======== imagePanel ========
				{
					imagePanel.setBorder(LineBorder.createBlackLineBorder());
					imagePanel.setLayout(new FormLayout(
						"default, $lcgap, default:grow, $lcgap, default",
						"default, $lgap, default:grow, $lgap, default"));

					//---- imageLabel ----
					imageLabel.setText("text");
					imagePanel.add(imageLabel, cc.xy(3, 3, CellConstraints.CENTER, CellConstraints.FILL));
				}
				imageDescriptionSplitPane.setTopComponent(imagePanel);

				//======== bottomPanel ========
				{
					bottomPanel.setLayout(new FormLayout(
						"2*($lcgap), default, $lcgap, default:grow, 2*($lcgap)",
						"2*($lgap), 5*(default, $lgap), fill:default:grow, 2*($lgap)"));

					//---- label1 ----
					label1.setText(resources.getResourceByKey("PartPanel.partname"));
					bottomPanel.add(label1, cc.xy(3, 3));

					//---- partNameField ----
					partNameField.setEditable(false);
					bottomPanel.add(partNameField, cc.xy(5, 3));

					//---- label2 ----
					label2.setText(resources.getResourceByKey("PartPanel.packagetype"));
					bottomPanel.add(label2, cc.xy(3, 5));

					//---- packageTypeField ----
					packageTypeField.setEditable(false);
					bottomPanel.add(packageTypeField, cc.xy(5, 5));

					//---- label4 ----
					label4.setText(resources.getResourceByKey("PartPanel.author"));
					bottomPanel.add(label4, cc.xy(3, 7));

					//---- authorField ----
					authorField.setEditable(false);
					bottomPanel.add(authorField, cc.xy(5, 7));

					//---- label5 ----
					label5.setText(resources.getResourceByKey("PartPanel.license"));
					bottomPanel.add(label5, cc.xy(3, 9));

					//---- licenseField ----
					licenseField.setEditable(false);
					bottomPanel.add(licenseField, cc.xy(5, 9));

					//---- label3 ----
					label3.setText(resources.getResourceByKey("PartPanel.description"));
					bottomPanel.add(label3, cc.xy(3, 11));

					//======== scrollPane2 ========
					{

						//---- descriptionPane ----
						descriptionPane.setEditable(false);
						scrollPane2.setViewportView(descriptionPane);
					}
					bottomPanel.add(scrollPane2, cc.xywh(3, 13, 3, 1));
				}
				imageDescriptionSplitPane.setBottomComponent(bottomPanel);
			}
			mainSplitpane.setRightComponent(imageDescriptionSplitPane);
		}
		add(mainSplitpane, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JSplitPane mainSplitpane;
	private JPanel panel1;
	private FilterPanel filterPanel;
	private JScrollPane scrollPane1;
	private AlternatingLineTable partTable;
	private JSplitPane imageDescriptionSplitPane;
	private JPanel imagePanel;
	private JLabel imageLabel;
	private JPanel bottomPanel;
	private JLabel label1;
	private JTextField partNameField;
	private JLabel label2;
	private JTextField packageTypeField;
	private JLabel label4;
	private JTextField authorField;
	private JLabel label5;
	private JTextField licenseField;
	private JLabel label3;
	private JScrollPane scrollPane2;
	private JEditorPane descriptionPane;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	/**
	 * @return the mainSplitpane
	 */
	public JSplitPane getMainSplitpane() {
	
		return mainSplitpane;
	}

	
	/**
	 * @return the partTable
	 */
	public AlternatingLineTable getPartTable() {	
		return partTable;
	}

	
	/**
	 * @return the imageDescriptionSplitPane
	 */
	public JSplitPane getImageDescriptionSplitPane() {
	
		return imageDescriptionSplitPane;
	}

	
	/**
	 * @return the imageLabel
	 */
	public JLabel getImageLabel() {
	
		return imageLabel;
	}

	
	/**
	 * @return the partNameField
	 */
	public JTextField getPartNameField() {
	
		return partNameField;
	}

	
	/**
	 * @return the packageTypeField
	 */
	public JTextField getPackageTypeField() {
	
		return packageTypeField;
	}

	
	/**
	 * @return the descriptionPane
	 */
	public JEditorPane getDescriptionPane() {
	
		return descriptionPane;
	}

	
	/**
	 * @return the filterPanel
	 */
	public FilterPanel getFilterPanel() {
	
		return filterPanel;
	}

	/**
	 * @return the authorField
	 */
	public JTextField getAuthorField() {
		return authorField;
	}

	/**
	 * @return the licenseField
	 */
	public JTextField getLicenseField() {
		return licenseField;
	}



}
