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

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXSearchField;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.commands.EditSymbolCommand;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.SymbolTableTransferHandler;
import org.pmedv.blackboard.filter.SymbolFilter;
import org.pmedv.blackboard.models.SymbolTableModel;
import org.pmedv.blackboard.provider.SymbolProvider;
import org.pmedv.core.components.AlternatingLineTable;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.util.ErrorUtils;

/**
 * The <code>SymbolListTable</code> is the main panel for symbol
 * management. 
 * 
 * @author Matthias Pueski (23.12.2011)
 *
 */
public class SymbolListPanel extends JPanel {

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	private static final long serialVersionUID = -4298865593845049380L;
	// the scroll pane containing the sheets table	
	private JScrollPane scrollPane;
	// table containing datasheets
	private AlternatingLineTable symbolTable;
	// model
	private SymbolTableModel model;
	// data provider
	private SymbolProvider provider;
	// Popup for table
	private JPopupMenu popUpMenu;
	// Filter panel
	private JXSearchField searchField;
	
	public SymbolListPanel() {		
		super(new BorderLayout());
		initializeComponents();		
	}

	@SuppressWarnings("serial")
	private void initializeComponents() {
		
		searchField = new JXSearchField();
		searchField.setPrompt("Filter");
		add(searchField,BorderLayout.NORTH);

		// Prevent Actions being triggered having a single key as accelerator
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() >= 0x41 && 
					e.getKeyCode() <= 0x5a && 
					e.getModifiers() == 0) {
					e.consume();
				}				
			}
		});
		
		symbolTable = new AlternatingLineTable();
		scrollPane = new JScrollPane(symbolTable);
		add(scrollPane, BorderLayout.CENTER);		
		provider = AppContext.getContext().getBean(SymbolProvider.class);
		
		try {
			provider.loadElements();
		} 
		catch (Exception e) {
			ErrorUtils.showErrorDialog(e);
		}
		
		ArrayList<SymbolBean> symbolBeans = new ArrayList<SymbolBean>();
		symbolBeans.addAll(provider.getElements());		
		model = new SymbolTableModel(symbolBeans);		
		symbolTable.setModel(model);		
		symbolTable.setRowHeight(128);
		symbolTable.setDragEnabled(true);
		symbolTable.setTransferHandler(new SymbolTableTransferHandler());
		// symbolTable.setPreferredSize(new Dimension(200,300));
		
		symbolTable.setDefaultRenderer(SymbolBean.class, new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel  label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);												
				SymbolBean symbol = (SymbolBean)value;	
				
				StringBuffer tooltip = new StringBuffer();
				tooltip.append("<html>");
				tooltip.append(symbol.getName());
				tooltip.append("<br>");
				tooltip.append(resources.getResourceByKey("Category"));
				tooltip.append(" : ");
				if (symbol.getCategory() != null)
					tooltip.append(symbol.getCategory());
				tooltip.append("</html>");
				setToolTipText(tooltip.toString());
				label.setIcon(provider.getSymbolMap().get(symbol.getName()));
				label.setText(null);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
			
		});
		
		popUpMenu = createPopupMenu();
		
		// this is the filter configuration for the filter text box on the top
		SymbolFilter filter = new SymbolFilter(symbolTable);
		BindingGroup filterGroup = new BindingGroup();
		// bind filter JTextBox's text attribute to part tables filterString
		// attribute
		filterGroup.addBinding(Bindings.createAutoBinding(READ, searchField,
				BeanProperty.create("text"), filter, BeanProperty.create("filterString")));
		filterGroup.bind();		

		
	}
	
	private JPopupMenu createPopupMenu() {
		
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem deleteItem = new JMenuItem(resources.getResourceByKey("DeleteCommand.name"));
		
		deleteItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
			
				int result = JOptionPane.showConfirmDialog(win,
						 resources.getResourceByKey("msg.warning.delete"),
						 resources.getResourceByKey("msg.warning"), JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.NO_OPTION) {
					return;
				}
				
				
				int selectedRow = symbolTable.convertRowIndexToModel(symbolTable.getSelectedRow());
				SymbolBean symbol = model.getSymbols().get(selectedRow);
				
				try {
					provider.removeElement(symbol);
					model.removeSymbol(symbol);
				}
				catch (Exception e1) {
					ErrorUtils.showErrorDialog(e1);
				}
			}
			
		});
		deleteItem.setIcon(resources.getIcon("icon.delete"));
		
		JMenuItem renameItem = new JMenuItem(resources.getResourceByKey("RenameCommand.name"));
		
		renameItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
				
				int selectedRow = symbolTable.convertRowIndexToModel(symbolTable.getSelectedRow());
				SymbolBean symbol = model.getSymbols().get(selectedRow);
				
				String name = JOptionPane.showInputDialog(win, resources.getResourceByKey("RenameCommand.message")+" "+symbol.getName());
				
				if (name == null || name.length() < 1) {
					return;
				}
				
				for (SymbolBean sb : AppContext.getContext().getBean(SymbolProvider.class).getElements()) {
					if(null != sb.getName() && sb.getName().equals(name)) {
						throw new IllegalArgumentException(resources.getResourceByKey("msg.itemnameexists"));
					}
				}
				
				try {
					// since icon depends on name, we need to reassign it.
					ImageIcon icon = provider.getSymbolMap().get(symbol.getName());
					provider.removeElement(symbol);
					provider.getSymbolMap().remove(symbol.getName());				
					symbol.setName(name);
					provider.addElement(symbol);
					provider.getSymbolMap().put(name, icon);
				}
				catch (Exception e1) {
					ErrorUtils.showErrorDialog(e1);
				}

				model.setSymbolBeans(provider.getElements());
				model.fireTableDataChanged();
			}
		});

		renameItem.setIcon(resources.getIcon("icon.rename"));
		
		menu.add(deleteItem);
		menu.add(AppContext.getContext().getBean(EditSymbolCommand.class));
		menu.add(renameItem);
		
		symbolTable.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mousePressed(MouseEvent e) {
				handleContextCLick(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				handleContextCLick(e);
			}
			
			
		});
		return menu;
		
	}

	void handleContextCLick(MouseEvent e) {
		
		Point p = e.getPoint();
		int row = symbolTable.rowAtPoint(p);
		symbolTable.setRowSelectionInterval(row, row);
		int selectedRow = symbolTable.convertRowIndexToModel(symbolTable.getSelectedRow());				
		SymbolBean symbolBean = model.getSymbols().get(selectedRow);
		Symbol symbol = new Symbol(symbolBean);
		AppContext.getContext().getBean(EditSymbolCommand.class).setSymbol(symbol);				
			
		if (e.isPopupTrigger()) {
			popUpMenu.show(symbolTable, e.getX(), e.getY());
		}
		else {
			if (e.getClickCount() == 2) {
				AppContext.getContext().getBean(EditSymbolCommand.class).execute(null);
			}
		}
	}
	
	/**
	 * @return the model
	 */
	public SymbolTableModel getModel() {
		return model;
	}

	public AlternatingLineTable getSymbolTable() {
		return symbolTable;
	}

}
