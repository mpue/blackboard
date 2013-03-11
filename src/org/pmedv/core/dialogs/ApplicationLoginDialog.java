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
package org.pmedv.core.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.pmedv.core.gui.panels.LoginPanel;

public class ApplicationLoginDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = 6057313413218721056L;

	private LoginPanel loginPanel;
	
	public ApplicationLoginDialog(String title, String subTitle,ImageIcon icon,  JFrame frame) {		
		super(title, subTitle, icon, true, false, true , true, frame, null);		
	}
	
	/**
	 * @return the loginPanel
	 */
	public LoginPanel getLoginPanel() {
		return loginPanel;
	}

	@Override
	protected void initializeComponents() {
		
		setBounds(new Rectangle(new Dimension(400, 250)));
		setSize(new Dimension(400, 250));
		
		Image iconImage = resources.getIcon("icon.application").getImage();
		setIconImage(iconImage);
		loginPanel = new LoginPanel();
		this.getContentPanel().add(loginPanel,BorderLayout.CENTER);	
		
		
	}

}
