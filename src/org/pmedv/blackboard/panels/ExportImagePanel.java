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

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.pmedv.core.components.FileBrowserTextfield;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
public class ExportImagePanel extends JPanel {

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public ExportImagePanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		separator2 = compFactory.createSeparator(resources.getResourceByKey("ExportImagePanel.preview"));
		previewPanel = new JPanel();
		separator3 = compFactory.createSeparator(resources.getResourceByKey("ExportImagePanel.exportPath"));
		label1 = new JLabel();
		fileTextField = new FileBrowserTextfield();
		separator1 = compFactory.createSeparator(resources.getResourceByKey("ExportImagePanel.options"));
		grayscaleCheckbox = new JCheckBox();
		invertCheckbox = new JCheckBox();
		mirrorCheckbox = new JCheckBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), 2*(default, $lcgap), default:grow, 2*($lcgap)",
			"2*($lgap), default, $lgap, default:grow, 4*($lgap, default), 2*($lgap)"));
		add(separator2, cc.xywh(3, 3, 5, 1));

		//======== previewPanel ========
		{
			previewPanel.setLayout(new BorderLayout());
		}
		add(previewPanel, cc.xywh(3, 5, 5, 1, CellConstraints.FILL, CellConstraints.FILL));
		add(separator3, cc.xywh(3, 7, 5, 1));

		//---- label1 ----
		label1.setText(resources.getResourceByKey("ExportImagePanel.exportTo"));
		add(label1, cc.xy(3, 9));
		add(fileTextField, cc.xywh(5, 9, 3, 1));
		add(separator1, cc.xywh(3, 11, 5, 1));

		//---- grayscaleCheckbox ----
		grayscaleCheckbox.setText(resources.getResourceByKey("ExportImagePanel.grayscale"));
		add(grayscaleCheckbox, cc.xy(3, 13));

		//---- invertCheckbox ----
		invertCheckbox.setText(resources.getResourceByKey("ExportImagePanel.invert"));
		add(invertCheckbox, cc.xy(5, 13));

		//---- mirrorCheckbox ----
		mirrorCheckbox.setText(resources.getResourceByKey("ExportImagePanel.mirror"));
		add(mirrorCheckbox, cc.xy(7, 13));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JComponent separator2;
	private JPanel previewPanel;
	private JComponent separator3;
	private JLabel label1;
	private FileBrowserTextfield fileTextField;
	private JComponent separator1;
	private JCheckBox grayscaleCheckbox;
	private JCheckBox invertCheckbox;
	private JCheckBox mirrorCheckbox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	public JPanel getPreviewPanel() {
		return previewPanel;
	}

	
	public FileBrowserTextfield getFileTextField() {
		return fileTextField;
	}

	
	public JCheckBox getGrayscaleCheckbox() {
		return grayscaleCheckbox;
	}

	
	public JCheckBox getInvertCheckbox() {
		return invertCheckbox;
	}

	
	public JCheckBox getMirrorCheckbox() {
		return mirrorCheckbox;
	}
	
	
}
