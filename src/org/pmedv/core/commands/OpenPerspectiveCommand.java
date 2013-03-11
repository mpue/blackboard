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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.components.CmdJButton;
import org.pmedv.core.components.JMenuWithId;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.perspectives.AbstractPerspective;
import org.springframework.context.ApplicationContext;

public class OpenPerspectiveCommand extends AbstractCommand {
	
	private static final long serialVersionUID = 1991644622936975671L;
	private static final Log log = LogFactory.getLog(OpenPerspectiveCommand.class);
	
	private String id;
	
	public OpenPerspectiveCommand(String id) {
		this.id = id;
		putValue(Action.NAME, id);
		putValue(Action.SHORT_DESCRIPTION, "Opens the "+id+" perspective.");				
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		final ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);		
		final ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
		
		final AbstractPerspective perspective = (AbstractPerspective) ctx.getBean(id);
		
		JMenuBar appMenuBar = win.getAppMenuBar();
		
		for (int i=0; i< appMenuBar.getMenuCount();i++) {
			
			JMenuWithId menu = (JMenuWithId)appMenuBar.getMenu(i);
			
			if (menu.getId() != null && (menu.getId().equals("common") ||
										 menu.getId().equals(perspective.ID) ||
										 menu.getId().equals("languages")))			
				menu.setVisible(true);
			else
				menu.setVisible(false);
		}
		
		JToolBar appToolbar = win.getToolBar();
		
		for (int i=0; i < appToolbar.getComponentCount(); i++) {
			
			if (appToolbar.getComponent(i) instanceof CmdJButton) {

				CmdJButton button = (CmdJButton)appToolbar.getComponent(i);
				
				if (button.getId().equals("common") || button.getId().equals(perspective.ID))
					button.setVisible(true);
				else
					button.setVisible(false);
				
			}
			
		}
				
		log.info("Setting current editor area to "+perspective.getEditorArea());
		
		advisor.setCurrentEditorArea(perspective.getEditorArea());
		advisor.setCurrentPerspective(perspective);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
				Properties properties = new Properties();
				try {
					properties.load(is);
				}
				catch (IOException e1) {
					properties.setProperty("version", "not set");
				}
				
				// String title = resources.getResourceByKey(perspective.ID+".title");				
				// win.setTitle(configProvider.getConfig().getTitle()+" - "+title+" Version "+Constants.VERSION);		
				win.setTitle(AppContext.getName()+" "+properties.getProperty("version"));
				win.getLayoutPane().removeAll();
				win.getLayoutPane().add(perspective,BorderLayout.CENTER);		
				win.getLayoutPane().revalidate();
				win.getLayoutPane().repaint();
			}
			
		});
		
		
	}

}
