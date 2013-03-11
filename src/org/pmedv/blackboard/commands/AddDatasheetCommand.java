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

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.blackboard.dialogs.SheetPropertiesDialog;
import org.pmedv.blackboard.provider.DataSheetProvider;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

/**
 * The <code>AddDatasheetCommand</code> adds a new file to the datasheet catalog.
 * 
 * @author mpue
 *
 */
public class AddDatasheetCommand extends AbstractCommand {

	private static final long serialVersionUID = -1410589465955713058L;
	
	public AddDatasheetCommand() {
		putValue(Action.NAME, resources.getResourceByKey("AddDataSheetCommand.name"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("AddDataSheetCommand.description"));
	}	
	
	@Override
	public void execute(ActionEvent e) {

		final DatasheetBean sheet = new DatasheetBean();		
		final String title = resources.getResourceByKey("AddDataSheetCommand.name");
		final String subTitle = resources.getResourceByKey("AddDataSheetCommand.dialog.subtitle");
		final ImageIcon icon = resources.getIcon("icon.dialog.datasheet");		
		
		final SheetPropertiesDialog dlg = new SheetPropertiesDialog(title, subTitle, icon, sheet);
		dlg.setVisible(true);
		
		if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL)
			return;
		
		final DataSheetProvider provider = AppContext.getContext().getBean(DataSheetProvider.class);
		
		provider.addSheet(sheet);
		try {
			provider.storeSheetList();
		} 
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}
		
		AppContext.getContext().getBean(DatasheetDialog.class).getModel().
			setDatasheetBeans(provider.getDatasheetList().getDatasheets());
		
	}

}
