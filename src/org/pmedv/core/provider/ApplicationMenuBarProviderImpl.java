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
package org.pmedv.core.provider;

import java.awt.Font;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.commands.OpenBoardCommand;
import org.pmedv.core.beans.ApplicationMenu;
import org.pmedv.core.beans.ApplicationMenuItem;
import org.pmedv.core.beans.ApplicationMenubar;
import org.pmedv.core.beans.ApplicationPerspective;
import org.pmedv.core.beans.RecentFileList;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.commands.OpenPerspectiveCommand;
import org.pmedv.core.components.CmdJCheckBoxMenuItem;
import org.pmedv.core.components.JMenuWithId;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;


/**
 * The <code>ApplicationMenuBarProvider</code> is responsible for the <code>ApplicationMenuBar</code>.
 * It reads the menus and the according items from the file <b>resources/menus.xml</b> and parses them.
 * 
 * @author pueski
 *
 */
public class ApplicationMenuBarProviderImpl implements ApplicationMenuBarProvider {

	private static final Log log = LogFactory.getLog(ApplicationMenuBarProviderImpl.class);
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	private final ApplicationContext ctx = AppContext.getContext();
	
	private JMenuBar menubar;
	private HashMap<String, Integer> keyMap = new HashMap<String, Integer>();
	
	private ArrayList<JMenuWithId> helpMenus = new ArrayList<JMenuWithId>();

	private final String keys[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	/**
	 * @return the menubar
	 */
	public JMenuBar getMenubar() {
		return menubar;
	}

	@SuppressWarnings("unused")
	public ApplicationMenuBarProviderImpl() {
		
		populateKeyTable();
		
		menubar = new JMenuBar();	
		
		try {
			JAXBContext c = JAXBContext.newInstance(ApplicationMenubar.class);
			
			Unmarshaller u = c.createUnmarshaller();
			
			ApplicationMenubar appMenuBar  = (ApplicationMenubar)u.unmarshal(getClass().getClassLoader().getResourceAsStream("menus.xml"));
			
			for (ApplicationMenu currentMenu : appMenuBar.getMenus()) {
				
				JMenuWithId menu = new JMenuWithId(resources.getResourceByKey(currentMenu.getName()));
				
				menu.setMnemonic(resources.getResourceByKey(currentMenu.getName()).charAt(0));
				
				for (ApplicationMenuItem currentItem : currentMenu.getItems()) {
					
					try {
						
						if (currentItem.getActionClass() != null) {
							
							if (currentItem.getActionClass().equals("separator")) {
								menu.addSeparator();
								continue;
							}

							log.info("Mapping action class : "+currentItem.getActionClass());
							
							try {
								
								AbstractCommand command = null;
								Class<?> clazz = Class.forName(currentItem.getActionClass());
								
								if (currentItem.isBean()) {
									command = (AbstractCommand) ctx.getBean(clazz);
								}
								else {									
									command = (AbstractCommand)clazz.newInstance();									
								}
								
								ImageIcon icon = null;
								String mnemonic = null;
								String toolTipText = null;
								
								if (currentItem.getImageIcon() != null) {
									
									InputStream is = getClass().getClassLoader().getResourceAsStream(currentItem.getImageIcon());												 
									
									if (is == null) {
										is = getClass().getClassLoader().getResourceAsStream("icons/noresource_16x16.png");
									}
									
									icon = new ImageIcon(ImageIO.read(is));

								}
								if (currentItem.getMnemonic() != null) {
									mnemonic = currentItem.getMnemonic();
								}
								if (currentItem.getToolTipText() != null) {
									toolTipText = currentItem.getToolTipText();
								}
								if (currentItem.getType() != null && currentItem.getType().equals("ApplicationMenuItemType.CHECKBOX")) {
									
									CmdJCheckBoxMenuItem cmdItem = new CmdJCheckBoxMenuItem(currentItem.getName(),icon,command,mnemonic,toolTipText,null);
									
									if (mnemonic != null && currentItem.getModifier() != null) {
										
										if (currentItem.getModifier().equalsIgnoreCase("ctrl")) {
											cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.CTRL_MASK, false));										
										}
										else if (currentItem.getModifier().equalsIgnoreCase("alt")){
											cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.ALT_MASK, false));										
										}
										else if (currentItem.getModifier().equalsIgnoreCase("shift")){
											cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.SHIFT_MASK, false));										
										}

									}
									
									menu.add(cmdItem);		
									
									
								}
								else {
									
									JMenuItem cmdItem = new JMenuItem(command);
									
									if (mnemonic != null && currentItem.getModifier() != null) {
										
										if (currentItem.getModifier().equalsIgnoreCase("ctrl")) {
											cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.CTRL_MASK, false));										
										}
										else if (currentItem.getModifier().equalsIgnoreCase("alt")){
											cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.ALT_MASK, false));										
										}
										else if (currentItem.getModifier().equalsIgnoreCase("shift")){
											cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.SHIFT_MASK, false));										
										}

									}
									
									menu.add(cmdItem);		
									
								}
								
							} 
							catch (InstantiationException e) {
								log.info("could not instanciate menuitem, skipping.");
							} 
							catch (IllegalAccessException e) {
								log.info("could not access menuitem, skipping.");								
							}
							
						}
						
					} 
					catch (ClassNotFoundException e) {
						log.info("could not find action class "+currentItem.getActionClass());
					} 
					
				}
				
				if (currentMenu.getType() != null && currentMenu.getType().equalsIgnoreCase("file")) {
					
					JMenu openRecentMenu = new JMenu(resources.getResourceByKey("menu.recentfiles"));
				
					RecentFileList fileList = null;
					
					try {
						
						String inputDir = System.getProperty("user.home") + "/."+AppContext.getName()+"/";
						String inputFileName = "recentFiles.xml";			
						File inputFile = new File(inputDir+inputFileName);
						
						if (inputFile.exists()) {
							Unmarshaller u1 = JAXBContext.newInstance(RecentFileList.class).createUnmarshaller(); 							
							fileList = (RecentFileList)u1.unmarshal(inputFile);							
						}
						
						if (fileList == null)
							fileList = new RecentFileList();
						
						ArrayList<String> recent = fileList.getRecentFiles();
						Collections.reverse(recent);
						
						for (String recentFile : fileList.getRecentFiles()) {							
							File file = new File(recentFile);
							AbstractCommand openBoardAction = new OpenBoardCommand(file.getName(), file);							
							JMenuItem item = new JMenuItem(openBoardAction);
							openRecentMenu.add(item);
						}
						
					} 
					catch (JAXBException e) {
						e.printStackTrace();
					} 

					menu.addSeparator();
					menu.add(openRecentMenu);
					
					JMenu openSamplesMenu = new JMenu(resources.getResourceByKey("menu.samples"));
					
					File samplesDir = new File(AppContext.getWorkingDir(),"samples");
					
					File[] sampleFiles = samplesDir.listFiles();
					
					for (int i = 0; i < sampleFiles.length;i++) {
						if (sampleFiles[i].getName().endsWith(".bb")) {
							AbstractCommand openBoardAction = new OpenBoardCommand(sampleFiles[i].getName(), sampleFiles[i]);							
							JMenuItem item = new JMenuItem(openBoardAction);
							openSamplesMenu.add(item);													
						}
					}
					
					menu.add(openSamplesMenu);
					
				}
				
				menu.setId("common");
				
				if (currentMenu.getType() != null && !currentMenu.getType().equalsIgnoreCase("help"))
					menubar.add(menu);
				else
					helpMenus.add(menu);
				
				menubar.setFont(new Font("SansSerif", Font.PLAIN, 12));
				
			}
			
			ApplicationPerspectiveProvider perspectiveProvider = ctx.getBean(ApplicationPerspectiveProvider.class);
			ArrayList<ApplicationPerspective> perspectives = perspectiveProvider.getPerspectives();
			
			JMenuWithId perspectivesMenu = new JMenuWithId("Perspectives");
			perspectivesMenu.setId("common");
			perspectivesMenu.setMnemonic('P');
			
			for (ApplicationPerspective perspective : perspectives) {
 
				ImageIcon icon = null;
				String mnemonic = null;
				String toolTipText = null;
				
				if (perspective.getPerspectiveIcon() != null) {
					
					InputStream is = getClass().getClassLoader().getResourceAsStream(perspective.getPerspectiveIcon());												 
					
					if (is != null) {
						icon = new ImageIcon(ImageIO.read(is));	
					}
					else {
						
						is = getClass().getClassLoader().getResourceAsStream("icons/noresource_16x16.png");

						if (is != null)
							icon = new ImageIcon(ImageIO.read(is));
						
					}
					
				}
				if (perspective.getMnemonic() != null) {
					mnemonic = perspective.getMnemonic();
				}
				if (perspective.getToolTipText() != null) {
					toolTipText = perspective.getToolTipText();
				}
				
				log.info("mapping perspective class "+perspective.getPerspectiveClass());
				OpenPerspectiveCommand command = new OpenPerspectiveCommand(perspective.getId());	
				
				JMenuItem item = new JMenuItem(command);
				
				if (mnemonic != null && perspective.getModifier() != null) {
					
					if (perspective.getModifier().equalsIgnoreCase("ctrl")) {
						item.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.CTRL_MASK, false));										
					}
					else if (perspective.getModifier().equalsIgnoreCase("alt")){
						item.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.ALT_MASK, false));										
					}
					else if (perspective.getModifier().equalsIgnoreCase("shift")){
						item.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(mnemonic),InputEvent.SHIFT_MASK, false));										
					}

				}
				
				perspectivesMenu.add(item);
				
				for (ApplicationMenu pMenu : perspective.getMenubarContributions()) {
					
					JMenuWithId menu = new JMenuWithId(resources.getResourceByKey(pMenu.getName()));
					
					// if (pMenu.getMnemonic() != null && pMenu.getMnemonic().length() > 0)					
					menu.setMnemonic(resources.getResourceByKey(pMenu.getName()).charAt(0));
					
					for (ApplicationMenuItem currentItem : pMenu.getItems()) {
						
						try {

							if (currentItem.getActionClass() != null) {

								if (currentItem.getActionClass().equals("separator")) {
									menu.addSeparator();
									continue;
								}
								
								log.info("Mapping action class : "+currentItem.getActionClass());
								
								try {
									
									AbstractCommand pCommand = null;
									Class<?> clazz = Class.forName(currentItem.getActionClass());
									
									if (currentItem.isBean()) {
										pCommand = (AbstractCommand) ctx.getBean(clazz);
									}
									else {									
										pCommand = (AbstractCommand)clazz.newInstance();									
									}
									
									ImageIcon pIcon = null;
									String pMnemonic = null;
									String pToolTipText = null;
									
									if (currentItem.getImageIcon() != null) {
										
										InputStream is = getClass().getClassLoader().getResourceAsStream(currentItem.getImageIcon());												 
										pIcon = new ImageIcon(ImageIO.read(is));

									}
									if (currentItem.getMnemonic() != null) {
										pMnemonic = currentItem.getMnemonic();
									}
									if (currentItem.getToolTipText() != null) {
										pToolTipText = currentItem.getToolTipText();
									}

									if (currentItem.getType() != null && currentItem.getType().equals("ApplicationMenuItemType.CHECKBOX")) {

										log.info("Creating menu checkbox for class "+currentItem.getActionClass());
										
										JCheckBoxMenuItem cmdItem = new JCheckBoxMenuItem(pCommand);
										
										if (pMnemonic != null && currentItem.getModifier() != null) {
											
											if (currentItem.getModifier().equalsIgnoreCase("ctrl")) {
												cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(pMnemonic),InputEvent.CTRL_MASK, false));										
											}
											else if (currentItem.getModifier().equalsIgnoreCase("alt")){
												cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(pMnemonic),InputEvent.ALT_MASK, false));										
											}
											else if (currentItem.getModifier().equalsIgnoreCase("shift")){
												cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(pMnemonic),InputEvent.SHIFT_MASK, false));										
											}

										}
										
										menu.add(cmdItem);
										cmdItem.setSelected(true);
										
										
									}
									else {

										log.info("Creating menu entry for class "+currentItem.getActionClass());
										
										JMenuItem cmdItem = new JMenuItem(pCommand);
										
										if (pMnemonic != null && currentItem.getModifier() != null) {
											
											if (currentItem.getModifier().equalsIgnoreCase("ctrl")) {
												cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(pMnemonic),InputEvent.CTRL_MASK, false));										
											}
											else if (currentItem.getModifier().equalsIgnoreCase("alt")){
												cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(pMnemonic),InputEvent.ALT_MASK, false));										
											}
											else if (currentItem.getModifier().equalsIgnoreCase("shift")){
												cmdItem.setAccelerator(KeyStroke.getKeyStroke(keyMap.get(pMnemonic),InputEvent.SHIFT_MASK, false));										
											}

										}
										
										menu.add(cmdItem);		
										
									}
									
								} 
								catch (InstantiationException e) {
									log.info("could not instanciate menuitem, skipping.");
								} 
								catch (IllegalAccessException e) {
									log.info("could not access menuitem, skipping.");								
								}
								
							}
							
						} 
						catch (ClassNotFoundException e) {
							log.info("could not find action class "+currentItem.getActionClass());
						} 
						
					}
					
					menu.setId(perspective.getId());
					menu.setVisible(false);
					
					menubar.add(menu);

				}
				
				
			}
			
			if (perspectiveProvider.getPerspectives().size() > 1)			
				menubar.add(perspectivesMenu);
			
			// TODO : Nice to have, but do we need that really? Maybe a feature for Version 1.0 ...
			
//			JMenuWithId languagesMenu = new JMenuWithId("Languages");
//			
//			Locale[] availableLocales = Locale.getAvailableLocales();
//			
//			ArrayList<String> languages = new ArrayList<String>();
//			
//			for (Locale locale : availableLocales) {
//				
//				InputStream is = BlackBoard.class.getClassLoader().getResourceAsStream("MessageResources_"+locale.getLanguage().toLowerCase()+".properties");
//				if (is != null && !languages.contains(locale.getDisplayLanguage())) {
//					languages.add(locale.getDisplayLanguage());
//				}
//			}
//			
//			for (String language : languages) {
//				languagesMenu.add(new JMenuItem(language));				
//			}
//			
//			languagesMenu.setId("languages");
//			
//			menubar.add(languagesMenu);
//			
			for (JMenuWithId helpmenu : helpMenus) {
				menubar.add(helpmenu);
			}
			
			
			menubar.setFont(new Font("SansSerif", Font.PLAIN, 12));
			
		} 
		catch (JAXBException e) {
			log.info("could not deserialize menus.");
			throw new RuntimeException("could not deserialize menus.");							
		} 
		catch (IOException e) {
			log.info("could not load menus.");
			throw new RuntimeException("could not load menus.");							
		}
		
	}

	/**
	 * Populates the keyMap <code>HashMap</code> with key value pairs
	 * in form of <mnemonic,keyCode> in order to set the right accelerators for any mnemonic 
	 */
	
	private void populateKeyTable() {
		
		for (int i = 0x41;i < 0x5B;i++ ) {
			keyMap.put(keys[i-0x41], i);
		}
		
	}
	
	
}
