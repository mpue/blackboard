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
package org.pmedv.core.context;

import java.io.File;

import org.pmedv.blackboard.components.Item;
import org.pmedv.core.components.ClipBoard;
import org.pmedv.core.services.ResourceService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * This class provides application-wide access to the Spring ApplicationContext.
 * The ApplicationContext is injected by the class "ApplicationContextProvider".
 *
 * @author Matthias Pueski
 */
public class AppContext {

    private static ApplicationContext ctx;    
    private static String lastSelectedFolder = System.getProperty("user.home");
    private static String name;
    private static File workingDir;
    
	private static final ClipBoard<Item> clipboard = new ClipBoard<Item>();
    
	/**
	 * @return the clipboard
	 */
	public static ClipBoard<Item> getClipboard() {
		return clipboard;
	}

	/**
	 * @return the lastSelectedFolder
	 */
	public static String getLastSelectedFolder() {
		return lastSelectedFolder;
	}

	/**
	 * @param lastSelectedFolder the lastSelectedFolder to set
	 */
	public static void setLastSelectedFolder(String lastSelectedFolder) {
		AppContext.lastSelectedFolder = lastSelectedFolder;
	}

	/**
     * Injected from the class "ApplicationContextProvider" which is automatically
     * loaded during Spring-Initialization.
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    /**
     * Get access to the Spring ApplicationContext from everywhere in your Application.
     *
     * @return
     */
    public static ApplicationContext getContext() {
        return ctx;
    }

	
	/**
	 * @return the name
	 */
	public static String getName() {	
		return name;
	}

	
	/**
	 * @param name the name to set
	 */
	public static void setName(String name) {	
		AppContext.name = name;
	}
    
	/**
	 * @return the workingDir
	 */
	public static File getWorkingDir() {
		return workingDir;
	}

	/**
	 * @param workingDir the workingDir to set
	 */
	public static void setWorkingDir(File workingDir) {
		AppContext.workingDir = workingDir;
	}

    /**
     * Just a static passthrough for getBean of application context.
     * 
     * @param <T>
     * @param requiredType
     * @return
     * @throws BeansException
     */

    public static <T> T getBean(Class<T> requiredType) throws BeansException
    {
    	// it might occur, that in some test cases this method is being called, 
    	// although the context has not been started. 
    	// In this case we just return a null reference.
    	
    	if (ctx == null) 
    	{    	
    		return null;
    	}
        return ctx.getBean(requiredType);
    }

}
