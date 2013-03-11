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
package org.pmedv.core.commands;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

public class HelpCommand extends AbstractOpenEditorCommand {

	private static final long serialVersionUID = 5495919812701807753L;

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public HelpCommand() {
		putValue(Action.NAME, "Help");
		putValue(Action.SMALL_ICON,resources.getIcon("icon.help"));
		putValue(Action.SHORT_DESCRIPTION, "Shows the help.");				
	}	
	
	@Override
	public void execute(ActionEvent e) {		

	}

}
