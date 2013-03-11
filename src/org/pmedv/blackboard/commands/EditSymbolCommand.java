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
package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;

import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.components.TextPart.TextType;
import org.pmedv.blackboard.dialogs.SymbolPropertiesDialog;
import org.pmedv.blackboard.panels.SymbolListPanel;
import org.pmedv.blackboard.provider.SymbolProvider;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

public class EditSymbolCommand extends AbstractCommand {

	private Symbol symbol;
	
	private boolean storeBean = true;
	
	public EditSymbolCommand() {
		putValue(Action.NAME, resources.getResourceByKey("EditSymbolCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.edit"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("EditSymbolCommand.description"));
		putValue(Action.ACCELERATOR_KEY, null);
	}

	@Override
	public void execute(ActionEvent e) {
		
		SymbolPropertiesDialog dlg = new SymbolPropertiesDialog(resources.getResourceByKey("EditSymbolCommand.name"), 
																resources.getResourceByKey("EditSymbolCommand.description"), null, symbol);
		dlg.getSymbolPropertiesPanel().getNameTextfield().setEnabled(false);
		
		// called from schematic editor, thus enable the ability to change the symbols name.
		if (!storeBean) {
			dlg.getSymbolPropertiesPanel().getNameTextfield().setEnabled(true);						
		}
		
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
			return;
		}
		
		symbol = dlg.getSymbol();
		
		/**
		 * Because we use this command also to persist an edited symbol
		 * back to the repository, we need to check if its being invoked from
		 * the schematic or the SymbolListPanel
		 */
		
		if (!storeBean) {
			
			storeBean = true;
			
			for (Item item : symbol.getItems()) {
				
				if (item instanceof TextPart) {
					
					TextPart text = (TextPart)item;
					
					if (text.getType() != null) {

						if (null != symbol.getName() && text.getType().equals(TextType.NAME)) {
							text.setText(symbol.getName());
						}
						if (null != symbol.getValue() && text.getType().equals(TextType.VALUE)) {
							text.setText(symbol.getValue());
						}
						
					}
					
				}
				
			}
			
			return;
		}
		
		SymbolProvider symbolProvider = AppContext.getContext().getBean(SymbolProvider.class);
		
		SymbolBean sb = new SymbolBean(symbol);

		try {
			symbolProvider.storeElement(sb);
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		// reload symbol list
		try {
			symbolProvider.loadElements();
			ArrayList<SymbolBean> symbolBeans = new ArrayList<SymbolBean>();			
			symbolBeans.addAll(symbolProvider.getElements());
			AppContext.getContext().getBean(SymbolListPanel.class).getModel().setSymbolBeans(symbolBeans);
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}

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
	 * Sets the store mode of the symbol, if set to true, the according bean is saved back
	 * to the repository.
	 * 
	 * @param storeBean determines if the according bean should be persisted to the symbol repository
	 */
	public void setStoreBean(boolean storeBean) {
		this.storeBean = storeBean;
	}
	
	
	
}
