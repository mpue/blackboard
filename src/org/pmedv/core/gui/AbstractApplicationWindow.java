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
package org.pmedv.core.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

public abstract class AbstractApplicationWindow extends JFrame implements WindowListener {

	private static final long serialVersionUID = -6864011165092389358L;

	protected ApplicationContext ctx;
	protected ApplicationWindowAdvisor windowAdvisor;
	protected ResourceService resources;

	protected AbstractApplicationWindow() {

		ctx = AppContext.getContext();

		windowAdvisor = ctx.getBean(ApplicationWindowAdvisor.class);
		resources = ctx.getBean(ResourceService.class);
		windowAdvisor.setWindow((ApplicationWindow) this);
		windowAdvisor.preWindowCreate();

		initializeComponents();

		windowAdvisor.postWindowCreate();

	}

	protected abstract void initializeComponents();

	@Override
	public void windowActivated(WindowEvent e) {
		windowAdvisor.windowActivatedHook(e);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		windowAdvisor.windowClosedHook(e);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		windowAdvisor.windowClosingHook(e);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		windowAdvisor.windowDeactivatedHook(e);
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		windowAdvisor.windowDeiconifiedHook(e);

	}

	@Override
	public void windowIconified(WindowEvent e) {
		windowAdvisor.windowIconifiedHook(e);

	}

	@Override
	public void windowOpened(WindowEvent e) {
		windowAdvisor.windowOpenedHook(e);
	}

}
