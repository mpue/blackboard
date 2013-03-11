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

import net.infonode.docking.TabWindow;

import org.pmedv.core.perspectives.AbstractPerspective;

public interface ApplicationWindowAdvisor {

	public void windowActivatedHook(WindowEvent e);

	public void windowClosedHook(WindowEvent e);

	public void windowClosingHook(WindowEvent e);

	public void windowDeactivatedHook(WindowEvent e);

	public void windowDeiconifiedHook(WindowEvent e);

	public void windowIconifiedHook(WindowEvent e);

	public void windowOpenedHook(WindowEvent e);
	
	public void setWindow(ApplicationWindow win);
	
	public void preWindowCreate();
	
	public void postWindowCreate();
	
	public TabWindow getCurrentEditorArea();
	
	public void setCurrentEditorArea(TabWindow area);
	
	public void setCurrentPerspective(AbstractPerspective perspective);
	
	public AbstractPerspective getCurrentPerspective();
	
	
}
