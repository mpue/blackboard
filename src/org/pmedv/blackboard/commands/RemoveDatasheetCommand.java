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

import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.beans.DatasheetList;
import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.blackboard.provider.DataSheetProvider;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

/**
 * <p>
 * The <code>RemoveDatasheetCommand</code> removes one or more
 * selected {@link DatasheetBean} objects from the {@link DatasheetList}.
 * </p>
 * <p>
 * The according files are not being removed physically.
 * </p>
 * 
 * @author Matthias Pueski (24.09.2011)
 *
 */
public class RemoveDatasheetCommand extends AbstractCommand {

	private static final long serialVersionUID = -1410589465955713058L;
	
	public RemoveDatasheetCommand() {
		putValue(Action.NAME, resources.getResourceByKey("RemoveDatasheetCommand.name"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("RemoveDatasheetCommand.description"));
	}	
	
	@Override
	public void execute(ActionEvent e) {

		DataSheetProvider provider = AppContext.getContext().getBean(DataSheetProvider.class);		
		DatasheetDialog dlg = AppContext.getContext().getBean(DatasheetDialog.class);
		
		int rows[] = dlg.getSheetPanel().getDatasheetTable().getSelectedRows();
		
		ArrayList<DatasheetBean> removedSheets = new ArrayList<DatasheetBean>();
		
		// Collect sheets to be removed from the model
		for (int i=0; i < rows.length; i++) {		
			int modelIndex = dlg.getSheetPanel().getDatasheetTable().convertRowIndexToModel(rows[i]);
			removedSheets.add(dlg.getModel().getDatasheetBeans().get(modelIndex));
		}

		// finally remove sheets
		for (DatasheetBean sheet : removedSheets) {
			provider.removeSheet(sheet);
		}

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
