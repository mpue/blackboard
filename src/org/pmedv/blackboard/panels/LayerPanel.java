
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.LayerTableModel;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

public class LayerPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private AlternatingLineTable table; 
	private JComboBox currentLayerCombo;
	private LayerTableModel model;
	private DefaultComboBoxModel layerComboModel;
	private final JButton addLayerButton = new JButton("Add layer");		
	private final JButton removeLayerButton = new JButton("Remove layer");
	
	
	public LayerPanel() {
		super(new BorderLayout());
		initializeComponents();		
	}

	@SuppressWarnings("unchecked")
	private void initializeComponents() {

		model = new LayerTableModel();
		table = new AlternatingLineTable(model);	
		table.setColumnControlVisible(true);
		final JScrollPane sp = new JScrollPane(table);		
		
		layerComboModel = new DefaultComboBoxModel();
		currentLayerCombo = new JComboBox(layerComboModel);
		
		JLabel currentLayerLabel = new JLabel("Current layer"); 		
		JPanel currentLayerPanel = new JPanel(new FlowLayout());
		currentLayerPanel.add(currentLayerLabel);
		currentLayerPanel.add(currentLayerCombo);
		
		JPanel opacityPanel = new JPanel(new FlowLayout());		
		JLabel opacityLabel = new JLabel("Opacity");
		JSlider opacitySlider = new JSlider(0,100);				
		opacityPanel.add(opacityLabel);
		opacityPanel.add(opacitySlider);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(currentLayerLabel, BorderLayout.NORTH);
		topPanel.add(opacityPanel, BorderLayout.SOUTH);
		
		add(topPanel, BorderLayout.NORTH);
		add(sp,BorderLayout.CENTER);		
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		addLayerButton.setIcon(resources.getIcon("icon.addlayer"));
		addLayerButton.setPreferredSize(new Dimension(100,25));
		removeLayerButton.setIcon(resources.getIcon("icon.removelayer"));
		removeLayerButton.setPreferredSize(new Dimension(100,25));
		buttonPanel.add(addLayerButton);
		buttonPanel.add(removeLayerButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}

	public AlternatingLineTable getLayerTable() {
		return table;
	}

	public LayerTableModel getLayerModel() {
		return model;
	}

	public JComboBox getCurrentLayerCombo() {
		return currentLayerCombo;
	}

	public DefaultComboBoxModel getLayerComboModel() {
		return layerComboModel;
	}

	public JButton getAddLayerButton() {
		return addLayerButton;
	}

	public JButton getRemoveLayerButton() {
		return removeLayerButton;
	}

	/**
	 * @param model the model to set
	 */
	@SuppressWarnings("unchecked")
	public void setLayers(ArrayList<Layer> layers) {
		
		/**
		 * Remove all listeners (in fact one) in order to prevent 
		 * multiple comboBoxChanged events 
		 */
		
		ActionListener[] listeners = currentLayerCombo.getListeners(ActionListener.class);		
		for (int i = 0; i < listeners.length; i++) {
			currentLayerCombo.removeActionListener(listeners[i]);
		}
		
		layerComboModel.removeAllElements();
		
		// Add new elements
		
		if (layers != null) {
			for (Layer l : layers) {
				layerComboModel.addElement(l);
			}						
		}
		else
			layers = new ArrayList<Layer>();

		/**
		 * And finally add the listeners back to the box
		 */
		
		for (int i = 0; i < listeners.length; i++) {
			currentLayerCombo.addActionListener(listeners[i]);
		}
		
		model.setLayers(layers);		
	}

	
	
}
