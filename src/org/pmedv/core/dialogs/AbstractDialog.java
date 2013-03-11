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
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

public abstract class AbstractDialog extends JDialog {
	
	private static final long serialVersionUID = -7552896221455776854L;
	protected ResourceService resources;
	protected ApplicationContext ctx;
	
	private JButton    newButton;
	private JButton    okButton;
	private JButton    cancelButton;
	private JPanel     contentPanel;
	private JPanel 	   buttonPanel;
	
	public int OPTION_CANCEL = 0;
	public int OPTION_OK = 1;
	public int actionResult = 0;
	
	private Icon newIcon;
	private Icon okIcon;
	private Icon cancelIcon;
	
	
	public AbstractDialog(String title, boolean modal) {
		
		this(title,true,false,true,true);
		
		ctx = AppContext.getContext();
		resources = ctx.getBean(ResourceService.class);

		newIcon    = resources.getIcon("icon.newdocument");
		okIcon     = resources.getIcon("icon.apply");
		cancelIcon = resources.getIcon("icon.cancel");

		
	}
	
	public AbstractDialog(String title, boolean modal,boolean showNewButton,boolean showOkButton, boolean showCancelButton) {

		setTitle(title);
		
		ctx = AppContext.getContext();
		resources = ctx.getBean(ResourceService.class);

		newIcon    = resources.getIcon("icon.newdocument");
		okIcon     = resources.getIcon("icon.apply");
		cancelIcon = resources.getIcon("icon.cancel");
		
		
		contentPanel = new JPanel(new BorderLayout());	
		contentPanel.add(createButtonBar(showNewButton,showOkButton,showCancelButton),BorderLayout.SOUTH);
		add(contentPanel);
		
		initializeComponents();
		// pack();
		centerAndAdjustSize();
		setModal(modal);
		setVisible(true);
		
	}
	
	protected abstract void initializeComponents();
	
	private JPanel createButtonBar(boolean showNewButton, boolean showOkButton, boolean showCancelButton) {
		
		buttonPanel = new JPanel(new FlowLayout());

		if (showNewButton) {
			newButton = new JButton("New");
			newButton.setIcon(newIcon);
			buttonPanel.add(newButton);			
		}
		if (showOkButton) {
			okButton = new JButton("Ok");
			okButton.setIcon(okIcon);
			buttonPanel.add(okButton);			
		}
		if (showCancelButton) {
			cancelButton = new JButton("Cancel");	
			cancelButton.setIcon(cancelIcon);
			buttonPanel.add(cancelButton);			
		}
		
		return buttonPanel;
	}
	
	
	
	
	private void centerAndAdjustSize() {
		setLocationRelativeTo(getRootPane());		
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getOkButton() {
		return okButton;
	}
	
	public JButton getNewButton() {
		return newButton;
	}

}
