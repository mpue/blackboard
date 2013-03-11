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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.beans.SymbolList;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.dialogs.SymbolPropertiesDialog;
import org.pmedv.blackboard.panels.SymbolListPanel;
import org.pmedv.blackboard.provider.SymbolProvider;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

/**
 * This command adds a new {@link Symbol} which usually has been 
 * created from a drawing to the {@link SymbolList}
 * 
 * @author Matthias Pueski (26.09.2011)
 *
 */
public class AddSymbolToLibraryCommand extends AbstractCommand {

	private static final long serialVersionUID = -1410589465955713058L;
	
	public AddSymbolToLibraryCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddSymbolToLibraryCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.symboltolibrary"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddSymbolToLibraryCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		setEnabled(false);
	}	
	
	@Override
	public void execute(ActionEvent e) {
		
		// which editor do we have?

		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		if (editor.getSelectedItem() == null)
			return;
		
		if (!(editor.getSelectedItem() instanceof Symbol)) {
			return;
		}
		
		Symbol symbol = null;
		
		try {
			symbol = (Symbol)editor.getSelectedItem().clone();
		}
		catch (CloneNotSupportedException e1) {
			// is supported, believe me, we do not care!
		}
		
		SymbolPropertiesDialog dlg = new SymbolPropertiesDialog(resources.getResourceByKey("AddSymbolToLibraryCommand.name"),
				"AddSymbolToLibraryCommand.description", null, symbol);
		dlg.setVisible(true);	
		
		String name = symbol.getName(); 

		// no name? skip
		
		if (name == null || name.length() < 1)
			return;
		
		int oldx = symbol.getXLoc();
		int oldy = symbol.getYLoc();
		
		symbol.setXLoc(0);
		symbol.setYLoc(0);
		
		for (Item subItem : symbol.getItems()) {

			if (subItem instanceof Line) {
				Line line = (Line) subItem;
				line.setStart(new Point((int) line.getStart().getX() - oldx, (int) line.getStart().getY() - oldy));
				line.setEnd(new Point((int) line.getEnd().getX() - oldx, (int) line.getEnd().getY() - oldy));
				line.setOldstart(new Point(line.getStart()));
				line.setOldEnd(new Point(line.getEnd()));
			}
			else {
				subItem.setXLoc(subItem.getXLoc() - oldx);
				subItem.setYLoc(subItem.getYLoc() - oldy);
				subItem.setOldXLoc(subItem.getXLoc());
				subItem.setOldYLoc(subItem.getYLoc());
				subItem.setOldWidth(subItem.getWidth());
				subItem.setOldHeight(subItem.getHeight());
			}

		}
		
		List<SymbolBean> symbols = AppContext.getContext().getBean(SymbolProvider.class).getElements();

		final SymbolBean sb = new SymbolBean(symbol);
		
		if (symbols.contains(sb)) {
			ErrorUtils.showErrorDialog(new IllegalArgumentException(resources.getResourceByKey("msg.itemnameexists")));
		}
		
		SymbolProvider provider = AppContext.getContext().getBean(SymbolProvider.class);
		
		// add symbol to provider which will persist the symbol 
		try {
			provider.addElement(sb);
			
			ArrayList<SymbolBean> symbolBeans = new ArrayList<SymbolBean>();			
			symbolBeans.addAll(provider.getElements());
			
			AppContext.getContext().getBean(SymbolListPanel.class).getModel().setSymbolBeans(symbolBeans);
		} 
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		
	}

}
