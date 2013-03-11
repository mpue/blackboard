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
package org.pmedv.core.gui.controller;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

/**
 * The <code>AbstractController</code> is the base class for all controlling classes.
 * 
 * It provides some basic behaviour and has already access to the application context,
 * the resource service and the application window.
 * 
 * @author Matthias Pueski
 *
 */
public abstract class AbstractController {
	
	final protected ApplicationContext ctx;
	final protected ResourceService resources;
	protected ApplicationWindow window;
	
	protected AbstractController() {
		ctx = AppContext.getContext();
		resources = ctx.getBean(ResourceService.class);
		window = ctx.getBean(ApplicationWindow.class);
		initialize();
	}

	protected abstract void initialize();
	
}
