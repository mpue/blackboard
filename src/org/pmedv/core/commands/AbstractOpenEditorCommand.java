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

import javax.swing.SwingUtilities;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.DockingUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.springframework.context.ApplicationContext;

/**
 * This abstract command provides the base functionality to
 * open any editor inside a tab window.
 * 
 * @author Matthias Pueski
 *
 */
public abstract class AbstractOpenEditorCommand extends AbstractCommand {
	
	private static final long serialVersionUID = 7152779678666584521L;

	private static final Log log = LogFactory.getLog(AbstractOpenEditorCommand.class);
	
	protected ApplicationContext ctx = AppContext.getContext();
	protected ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
	
	protected void openEditor(final View view, int index) {

		if (advisor.getCurrentEditorArea() == null || !advisor.getCurrentEditorArea().isRestorable()) {

			View v = EditorUtils.getFirstAvailableView(advisor.getCurrentPerspective());
			
			TabWindow w = null;
			
			if (v != null) {
				w = DockingUtil.getTabWindowFor(v);	
			}			
			if (w != null) {
				advisor.setCurrentEditorArea(w);
			}
			else {

				TabWindow editorArea = new TabWindow(new DockingWindow[] {});			
				editorArea.addListener(advisor.getCurrentPerspective().getDockingListener());
				
				advisor.getCurrentPerspective().setEditorArea(editorArea);
				
				if (advisor.getCurrentPerspective() != null) {
					if (advisor.getCurrentPerspective().getRootWindow() != null) {
						advisor.getCurrentPerspective().getRootWindow().setWindow(editorArea);
					}
					else {
						log.error("No root window available.");
					}
				}
				else {
					log.error("No current perspective set.");
				}
				
				advisor.setCurrentEditorArea(advisor.getCurrentPerspective().getEditorArea());	
			}
			
			
		}
			
		advisor.getCurrentEditorArea().addTab(view);
		log.info("Adding view with index "+index+" to perspective "+advisor.getCurrentPerspective().ID);
		
		if (index != 0)
			advisor.getCurrentPerspective().getViewMap().addView(index , view);
		
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				view.requestFocus();
			}
		});
		
	}
	

}
