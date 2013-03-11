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
package org.pmedv.core.components;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;

import org.pmedv.core.commands.Command;
import org.pmedv.core.commands.CommandHolder;


/**
 * This class represents a basic MenuItem with an associated command
 * 
 * @author mpue 19.12.2006
 *
 */


public class CmdJCheckBoxMenuItem extends JCheckBoxMenuItem implements CommandHolder {

	private static final long serialVersionUID = 1L;
	
	protected Command menuCommand;
	protected JFrame frame;
	
	public CmdJCheckBoxMenuItem(String name, Icon icon,JFrame frm) {
		super(name,icon);
		frame = frm;
		this.addActionListener(new CmdActionListener());
	}
	
	public CmdJCheckBoxMenuItem(String name, Icon icon,Command command, String mnemonic,String tooltipText,JFrame frm) {
		super(name,icon);
		this.frame = frm;
		this.menuCommand = command;		
		if (mnemonic != null && mnemonic.length() > 0)
			this.setMnemonic(mnemonic.charAt(0));
		this.setToolTipText(tooltipText);
		this.addActionListener(new CmdActionListener());
	}
	
	public Command getCommand() {
		return  menuCommand;
	}

	public void setCommand(Command cmd) {
		menuCommand = cmd;
	}

}
