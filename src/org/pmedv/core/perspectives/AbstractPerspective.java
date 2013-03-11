/**
 * BlackBoard BreadBoard Designer Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2010-2011 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.core.perspectives;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.RootWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.util.ViewMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.app.AbstractApplication;
import org.pmedv.core.context.AppContext;

/**
 * The {@link AbstractPerspective} is the base for all perspectives of an {@link AbstractApplication}
 * it provides view state persitence for the Docking Windows, a predefinde {@link RootWindow} and
 * a predefined dafeult editor area, which is actually a {@link TabWindow}. 
 * 
 * @author Matthias Pueski
 *
 */
public abstract class AbstractPerspective extends JPanel {

	private static final long serialVersionUID = 5771846683962011959L;

	private static final Log log = LogFactory.getLog(AbstractPerspective.class);

	public String ID;

	protected TabWindow editorArea;
	protected RootWindow rootWindow;
	protected DockingWindowListener dockingListener;
	protected ViewMap viewMap;

	/**
	 * @return the viewMap
	 */
	public ViewMap getViewMap() {
		return viewMap;
	}

	/**
	 * @param viewMap the viewMap to set
	 */
	public void setViewMap(ViewMap viewMap) {
		this.viewMap = viewMap;
	}

	/**
	 * @return the rootWindow
	 */
	public RootWindow getRootWindow() {
		return rootWindow;
	}

	/**
	 * @param rootWindow the rootWindow to set
	 */
	public void setRootWindow(RootWindow rootWindow) {
		this.rootWindow = rootWindow;
	}

	/**
	 * @return the dockingListener
	 */
	public DockingWindowListener getDockingListener() {
		return dockingListener;
	}

	/**
	 * @param dockingListener the dockingListener to set
	 */
	public void setDockingListener(DockingWindowListener dockingListener) {
		this.dockingListener = dockingListener;
	}

	/**
	 * @return the editorArea
	 */
	public TabWindow getEditorArea() {
		return editorArea;
	}

	/**
	 * @param editorArea the editorArea to set
	 */
	public void setEditorArea(TabWindow editorArea) {
		this.editorArea = editorArea;
	}

	protected abstract void initializeComponents();

	protected AbstractPerspective() {
		initializeComponents();
	}

	/**
	 * Saves the current view state of the perspectives {@link RootWindow} to disk.
	 */
	protected void saveViewState() {

		String outputDir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";

		ObjectOutputStream oos = null;
		try {
			File viewStateCache = new File(outputDir + ID + ".ser");
			FileOutputStream fos = new FileOutputStream(viewStateCache);
			oos = new ObjectOutputStream(fos);			
			rootWindow.write(oos);
		}
		catch (FileNotFoundException e1) {
			log.debug("could not find serialized viewstate: " + e1.getMessage());
		}
		catch (IOException e2) {
			log.warn("could not save serialized viewstate: " + e2.getMessage());
			e2.printStackTrace();
		}
		finally {
			try {
				oos.close();
			}
			catch (IOException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * Loads the perspectives view state from disk and configures the {@link RootWindow}
	 * 
	 * @return true if load was successful, false if not
	 */
	protected boolean loadViewState() {

		String inputDir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";

		boolean loaded = false;

		ObjectInputStream ois = null;

		try {
			File viewStateCache = new File(inputDir + ID + ".ser");
			FileInputStream fis = new FileInputStream(viewStateCache);
			ois = new ObjectInputStream(fis);
			rootWindow.read(ois);
			loaded = true;
		}
		catch (FileNotFoundException e) {
			log.debug("could not find serialized viewstate: " + e.getMessage());
		}
		catch (Exception e) {
			log.warn("could not load serialized viewstate: " + e.getMessage());
			loaded = false;
		}
		finally {
			try {
				if (ois != null)
					ois.close();
			}
			catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return loaded;
	}

	/**
	 * Deletes the view state, this is just like a perspective reset
	 */
	public void deleteViewState() {

		String inputDir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";

		try {
			File viewStateCache = new File(inputDir + ID + ".ser");
			if (viewStateCache.exists())
				viewStateCache.delete();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

	}

}
