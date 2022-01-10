/**

	BlackBoard BreadBoard Designer
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
package org.pmedv.core.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXStatusBar.Constraint;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.commands.SetColorCommand;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.provider.ApplicationMenuBarProvider;
import org.pmedv.core.provider.ApplicationToolbarProvider;
import org.springframework.context.ApplicationContext;

/**
 * The main application window
 * 
 * @author mpue
 * 
 *         10.12.2006
 * 
 */

@SuppressWarnings("unused")
public class ApplicationWindow extends AbstractApplicationWindow implements EditorChangedListener {
	private static final long serialVersionUID = 9047805487938155205L;
	private JPanel layoutPane;
	private JLabel statusLabel;
	private JLabel customLabel;
	private JProgressBar progressBar;
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private JXStatusBar statusBar;
	private JLabel viewLabel;
	
	private JComboBox zoomCombo;
	private JComboBox rasterCombo;
	private ZoomActionListener zoomActionListener;

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeComponents() {
		ApplicationContext ctx = AppContext.getContext();
		layoutPane = new JPanel(new BorderLayout());
		menuBar = createMenuBar();
		this.setJMenuBar(menuBar);
		this.add(layoutPane, java.awt.BorderLayout.CENTER);
		toolBar = createToolBar();
		JPanel toolbarPanel = new JPanel(new BorderLayout());
		this.add(toolBar, java.awt.BorderLayout.NORTH);
		statusBar = new JXStatusBar();
		ImageIcon offlineIcon = resources.getIcon("icon.status.offline");
		statusLabel = new JLabel();
		statusBar.add(statusLabel);
		statusLabel = new JLabel("Current color");
		statusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					AppContext.getContext().getBean(SetColorCommand.class).execute(null);
				}
			}
		});
		JXStatusBar.Constraint c1 = new Constraint();
		c1.setFixedWidth(250);
		statusBar.add(statusLabel, c1); // Fixed width of 100 with no inserts
		statusLabel.setIcon(offlineIcon);
		customLabel = new JLabel("x : 0 y : 0");
		JXStatusBar.Constraint c3 = new Constraint();
		c3.setFixedWidth(300);
		statusBar.add(customLabel, c3);
		
		JXStatusBar.Constraint c4 = new Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);
		viewLabel = new JLabel();
		statusBar.add(viewLabel,c4);
		
		JLabel zoomLabel = new JLabel("Zoom");
		zoomCombo = new JComboBox();
		zoomCombo.addItem(new Float(0.1));
		zoomCombo.addItem(new Float(0.25));
		zoomCombo.addItem(new Float(0.5));
		zoomCombo.addItem(new Float(0.75));
		zoomCombo.addItem(new Float(1));
		zoomCombo.addItem(new Float(1.25));
		zoomCombo.addItem(new Float(1.5));
		zoomCombo.addItem(new Float(2.0));
		zoomCombo.addItem(new Float(4.0));
		zoomCombo.addItem(new Float(6.0));
		zoomCombo.addItem(new Float(8.0));
		
		statusBar.add(zoomLabel);
		statusBar.add(zoomCombo);
		zoomCombo.setRenderer(new ZoomComboBoxRenderer());
		
		zoomActionListener = new ZoomActionListener();
		zoomCombo.addActionListener(zoomActionListener);
		
		JLabel rasterLabel = new JLabel("Raster");
		rasterCombo = new JComboBox();
		rasterCombo.addItem(new Integer(16));
		rasterCombo.addItem(new Integer(8));
		rasterCombo.addItem(new Integer(4));
		rasterCombo.addItem(new Integer(2));
		rasterCombo.addItem(new Integer(1));
		statusBar.add(rasterLabel);
		statusBar.add(rasterCombo);
		rasterCombo.setRenderer(new RasterComboBoxRenderer());
		rasterCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("comboBoxChanged")) {
					int raster = ((Integer) rasterCombo.getSelectedItem()).intValue();
					EditorUtils.getCurrentActiveEditor().setRaster(raster);
					EditorUtils.getCurrentActiveEditor().refresh();
				}
			}
		});
		// customLabel.setVisible(false);
		// progressBar.setVisible(false);
		this.add(statusBar, java.awt.BorderLayout.SOUTH);
		layoutPane.setBackground(Color.DARK_GRAY);
		
		Boolean showTooltips = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showTooltips");
		ToolTipManager.sharedInstance().setEnabled(showTooltips.booleanValue());

	}

	/**
	 * @return the statusLabel
	 */
	public JLabel getStatusLabel() {
		return statusLabel;
	}

	/**
	 * @param statusLabel the statusLabel to set
	 */
	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
	}

	/**
	 * @return the application's menu bar
	 */
	private JMenuBar createMenuBar() {
		ApplicationMenuBarProvider p = ctx.getBean(ApplicationMenuBarProvider.class);
		return p.getMenubar();
	}

	private JToolBar createToolBar() {
		ApplicationToolbarProvider p = ctx.getBean(ApplicationToolbarProvider.class);
		return p.getToolbar();
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @return the layoutPane
	 */
	public JPanel getLayoutPane() {
		return layoutPane;
	}

	/**
	 * @return the toolBar
	 */
	public JToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * @param toolBar the toolBar to set
	 */
	public void setToolBar(JToolBar toolbar) {
		this.toolBar = toolbar;
	}

	/**
	 * @return the menuBar
	 */
	public JMenuBar getAppMenuBar() {
		return menuBar;
	}

	/**
	 * @return the customLabel
	 */
	public JLabel getCustomLabel() {
		return customLabel;
	}

	public JLabel getViewLabel() {
		return viewLabel;
	}
	
	public JXStatusBar getStatusBar() {
		return statusBar;
	}

	@SuppressWarnings("serial")
	private final class RasterComboBoxRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			// Get the selected index. (The index param isn't
			// always valid, so just use the value.)
			int selectedIndex = ((Integer) value).intValue();
			switch (selectedIndex) {
				case 16:
					setText("1/1");
					break;
				case 8:
					setText("1/2");
					break;
				case 4:
					setText("1/4");
					break;
				case 2:
					setText("1/8");
					break;
				case 1:
					setText("1/16");
					break;
				default:
					break;
			}
			return this;
		}
	}

	@SuppressWarnings("serial")
	private final class ZoomComboBoxRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			float selectedValue = ((Float) value).floatValue();			
			setText(selectedValue*100+"%");
			return this;
		}
	}
	/**
	 * @return the rasterCombo
	 */
	public JComboBox getRasterCombo() {
		return rasterCombo;
	}

	public JComboBox getZoomCombo() {
		return zoomCombo;
	}

	@Override
	public void editorChanged(EditorChangedEvent event) {

		zoomCombo.removeActionListener(zoomActionListener);
		
		BoardEditor editor = event.getEditor();
		
		if (editor != null) {
			JXLayer<?> layer = editor.getZoomLayer();
			TransformUI ui = (TransformUI)(Object)layer.getUI();
			DefaultTransformModel model = (DefaultTransformModel)ui.getModel();
			zoomCombo.setSelectedItem(new Float(model.getScale()));		
			zoomCombo.addActionListener(zoomActionListener);						
		}
		
		
	}
	
	private final class ZoomActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals("comboBoxChanged")) {

				BoardEditor editor = EditorUtils.getCurrentActiveEditor();

				if (editor != null) {
					JXLayer<?> layer = editor.getZoomLayer();
					TransformUI ui = (TransformUI)(Object) layer.getUI();
					DefaultTransformModel model = (DefaultTransformModel) ui.getModel();
					model.setScale(((Float) zoomCombo.getSelectedItem()).floatValue());
				}

			}

		}

	}
	
	
}
