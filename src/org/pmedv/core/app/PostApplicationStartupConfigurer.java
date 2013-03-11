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
package org.pmedv.core.app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.commands.OpenBoardCommand;
import org.pmedv.core.beans.ApplicationPerspective;
import org.pmedv.core.beans.ApplicationWindowConfiguration;
import org.pmedv.core.commands.OpenPerspectiveCommand;
import org.pmedv.core.components.IMemento;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.perspectives.AbstractPerspective;
import org.pmedv.core.provider.ApplicationPerspectiveProvider;
import org.springframework.context.ApplicationContext;

/**
 * The <code>PostApplicationStartupConfigurer</code> restores the application 
 * state after application startup and does all neccesary work to be done
 * after the application has been started. 
 * 
 * @author Matthias Pueski
 *
 */
public class PostApplicationStartupConfigurer {

	private static final Log log = LogFactory.getLog(PostApplicationStartupConfigurer.class);
	
	private ApplicationWindowConfiguration config;
	
	private String fileLocation;
	
	public PostApplicationStartupConfigurer(String fileLocation) {
		this.fileLocation = fileLocation;
		log.info(AppContext.getName()+" initialized.");
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 16, 16);
		AppContext.getContext().getBean(ApplicationWindow.class).getStatusLabel().setIcon(new ImageIcon(image));		
	}
	
	/**
	 * Loads the perpective which was opened before last application shutdown
	 */
	
	public void restoreLastPerspective() {

		ApplicationContext ctx = AppContext.getContext();
		ApplicationPerspectiveProvider perspectiveProvider = ctx.getBean(ApplicationPerspectiveProvider.class);

		if (perspectiveProvider.getPerspectives().size() > 1) {
			String inputDir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";
			String inputFileName = "appWindowConfig.xml";			
			
			File output = new File(inputDir+inputFileName);
			
			if (output.exists()) {
				
				try {
					Unmarshaller u = JAXBContext.newInstance(ApplicationWindowConfiguration.class).createUnmarshaller();
					config = (ApplicationWindowConfiguration)u.unmarshal(output);
					
					log.info("restoring last perspective : "+config.getLastPerspectiveID());
					
					OpenPerspectiveCommand open = new OpenPerspectiveCommand(config.getLastPerspectiveID());
					open.execute(null);
					
				} 
				catch (JAXBException e) {
					log.info("could not restore ApplicationWindowConfiguration.");				
				}			
				
			}
			else {
				log.info("No ApplicationWindowConfiguration found.");
				return;
			}
			
			
			for (ApplicationPerspective ap : perspectiveProvider.getPerspectives()) {
				
				AbstractPerspective a = (AbstractPerspective)ctx.getBean(ap.getId());
				
				if (a instanceof IMemento && ap.getId().equals(config.getLastPerspectiveID())) {

					IMemento m = (IMemento)a;
					log.info("restoring perspective : "+a.ID);					
					m.loadState();
					
				}
			}
			
		}
		
		 // Only one perspective available, open it by default
		
		else if (perspectiveProvider.getPerspectives().size() == 1) {
			
			ApplicationPerspective ap = perspectiveProvider.getPerspectives().get(0);
			
			OpenPerspectiveCommand open = new OpenPerspectiveCommand(ap.getId());
			open.execute(null);
			
			final AbstractPerspective perspective = (AbstractPerspective) ctx.getBean(ap.getId());
			
			if (perspective instanceof IMemento) {

				IMemento m = (IMemento)perspective;
				log.info("restoring perspective : "+perspective.ID);					
				m.loadState();
				
			}
		}
		else {
			log.error("No perspectives found");
			throw new RuntimeException("No perspectives found");
		}
		
		if (fileLocation != null) {
			OpenBoardCommand cmd = new OpenBoardCommand("External", new File(fileLocation));
			cmd.execute(null);
		}
		
	}
	
}
