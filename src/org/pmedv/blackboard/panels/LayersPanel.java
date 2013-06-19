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
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.pmedv.blackboard.commands.DeleteLayerCommand;
import org.pmedv.blackboard.commands.DuplicateLayerCommand;
import org.pmedv.blackboard.commands.MoveLayerDownCommand;
import org.pmedv.blackboard.commands.MoveLayerUpCommand;
import org.pmedv.blackboard.commands.SetLayerColorCommand;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.LayerTableModel;
import org.pmedv.blackboard.models.LayerTableTransferHandler;
import org.pmedv.blackboard.tools.ColorIconFactory;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class LayersPanel extends JPanel {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private LayerTableModel model;
	private DefaultComboBoxModel layerComboModel;
	
	private JPopupMenu popupMenu;
	
	@SuppressWarnings("unchecked")
	public LayersPanel() {
		
		initComponents();
		
		model = new LayerTableModel();
		
		layerComboModel = new DefaultComboBoxModel();
		layerTable.setModel(model);
		layerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		layerTable.setSortable(false);
		layerTable.setColumnSelectionAllowed(false);
		layerTable.setRowSelectionAllowed(true);
		layerTable.getTableHeader().setReorderingAllowed(false);
		layerTable.setDragEnabled(true);
		layerTable.setDropMode(DropMode.INSERT_ROWS);
		layerTable.setTransferHandler(new LayerTableTransferHandler(layerTable)); 
		
		currentLayerCombo.setModel(layerComboModel);
		addLayerButton.setIcon(resources.getIcon("icon.addlayer"));
		removeLayerButton.setIcon(resources.getIcon("icon.removelayer"));
		addLayerButton.setEnabled(false);
		removeLayerButton.setEnabled(false);
		currentLayerCombo.setEnabled(false);
		opacitySlider.setEnabled(false);
		
		popupMenu = new JPopupMenu();		
		popupMenu.add(AppContext.getContext().getBean(MoveLayerUpCommand.class));
		popupMenu.add(AppContext.getContext().getBean(MoveLayerDownCommand.class));
		popupMenu.add(AppContext.getContext().getBean(SetLayerColorCommand.class));
		popupMenu.add(AppContext.getContext().getBean(DuplicateLayerCommand.class));
		popupMenu.add(AppContext.getContext().getBean(DeleteLayerCommand.class));
		
		layerTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handlePopupTrigger(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				handlePopupTrigger(e);
			}
		});
		
		layerTable.setDefaultRenderer(Color.class, new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel  label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				Color color = (Color)value;
				label.setIcon(ColorIconFactory.getIcon(color,16));
				label.setText(null);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
			
		});
		
		/*
		layerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = layerTable.convertRowIndexToModel(layerTable.getSelectedRow());
				Layer current = model.getLayer().get(index);
				currentLayerCombo.setSelectedItem(current);
			}
		});
		*/
	}

	private void handlePopupTrigger(MouseEvent e) {
		if (e.isPopupTrigger() && model.getLayer().size() >= 1) {
			
			Point p = e.getPoint();			 
			// get the row index that contains that coordinate
			int rowNumber = layerTable.rowAtPoint( p ); 
			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = layerTable.getSelectionModel();
			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( rowNumber, rowNumber );			
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
			
		}
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		currentLayerCombo = new JComboBox();
		label2 = new JLabel();
		opacitySlider = new JSlider();
		scrollPane1 = new JScrollPane();
		layerTable = new AlternatingLineTable();
		panel1 = new JPanel();
		addLayerButton = new JButton();
		removeLayerButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"2*($lcgap), default, 2*($lcgap, default:grow), 2*($lcgap)",
			"2*($lgap), 2*(default, $lgap), fill:default:grow, 2*($lgap), 2*(default, $lgap)"));

		//---- label1 ----
		label1.setText(resources.getResourceByKey("LayerPanel.currentLayerLabel"));
		add(label1, cc.xy(3, 3));
		add(currentLayerCombo, cc.xywh(5, 3, 3, 1));

		//---- label2 ----
		label2.setText(resources.getResourceByKey("LayerPanel.opacity"));
		add(label2, cc.xy(3, 5));

		//---- opacitySlider ----
		opacitySlider.setValue(100);
		add(opacitySlider, cc.xywh(5, 5, 3, 1));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(layerTable);
		}
		add(scrollPane1, cc.xywh(3, 7, 5, 1));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				"default:grow, $lcgap, default:grow",
				"default"));

			//---- addLayerButton ----
			panel1.add(addLayerButton, cc.xy(1, 1));

			//---- removeLayerButton ----
			panel1.add(removeLayerButton, cc.xy(3, 1));
		}
		add(panel1, cc.xywh(3, 10, 5, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JComboBox currentLayerCombo;
	private JLabel label2;
	private JSlider opacitySlider;
	private JScrollPane scrollPane1;
	private AlternatingLineTable layerTable;
	private JPanel panel1;
	private JButton addLayerButton;
	private JButton removeLayerButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

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
			addLayerButton.setEnabled(layers.size() > 0);
			removeLayerButton.setEnabled(layers.size() > 0);
			currentLayerCombo.setEnabled(layers.size() > 0);
			opacitySlider.setEnabled(layers.size() > 0);
		}
		else {
			layers = new ArrayList<Layer>();
			addLayerButton.setEnabled(false);
			removeLayerButton.setEnabled(false);
			currentLayerCombo.setEnabled(false);
			opacitySlider.setEnabled(false);
		}
		/**
		 * And finally add the listeners back to the box
		 */
		
		for (int i = 0; i < listeners.length; i++) {
			currentLayerCombo.addActionListener(listeners[i]);
		}
		
		model.setLayers(layers);		
	}
	
	public AlternatingLineTable getLayerTable() {
		return layerTable;
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
	 * @return the opacitySlider
	 */
	public JSlider getOpacitySlider() {
		return opacitySlider;
	}


}
