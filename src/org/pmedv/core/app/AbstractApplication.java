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

import java.io.File;
import java.util.Properties;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.CheckEnv;
import org.pmedv.core.util.FileUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the abstract base mother of the application which provides some basic 
 * functionality and invokes the application window.
 * 
 * @author Matthias Pueski
 *
 */
public abstract class AbstractApplication {
	
	private static final Log log = LogFactory.getLog(AbstractApplication.class);
	
	protected ApplicationContext ctx; 
	protected ApplicationWindow  win;
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the currentDir
	 */
	public String getCurrentDir() {
		return currentDir;
	}

	/**
	 * @param currentDir the currentDir to set
	 */
	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}

	private Properties properties;
	private String currentDir;
    @SuppressWarnings("unused")
	private SplashScreen splashScreen;

	public AbstractApplication(final String fileLocation) {
				
		/**
		 * Set the default UncaughtExceptionHandler which calls an error dialog
		 * with the full stacktrace inside.
		 */
		
//		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//
//			@Override
//			public void uncaughtException(Thread t, Throwable e) {
//				
//				StringBuffer message = new StringBuffer();
//				
//				message.append("<h2>An unhandled exception occured</h2>");
//				message.append("<p>The application cannot fulfil your request.</p>");
//				message.append("<p>The following stacktrace might help to locate the problem :</p>");
//				message.append(ResourceUtils.getStackTrace(e));
//				
//				ErrorDialog.showMessage(message.toString());
//				
//			}
//			
//		});
		
		/**
		 * Get default system properties
		 */

		try {
			properties = CheckEnv.getEnvVars();
		} 
		catch (Throwable e) {
			properties = new Properties();
		}
		
		currentDir = new File(System.getProperty("user.home")).getAbsolutePath();

		if (AppContext.getName() == null) {
			throw new IllegalStateException("Application name must be set before startup.");
		}

		File workDir = detectInstallDir();
		log.info("Working directory "+workDir.getAbsolutePath());
		AppContext.setWorkingDir(workDir);

		File dataDir = AppContext.getUserDataDir();
		if (!dataDir.exists()) {
			log.info("Creating directory "+dataDir.getAbsolutePath());
			FileUtils.makeDirectory(dataDir.getAbsolutePath());
		}
		File tempDir = new File(dataDir, "temp");
		if (!tempDir.exists()) {
			log.info("Creating directory "+tempDir.getAbsolutePath());
			FileUtils.makeDirectory(tempDir.getAbsolutePath());
		}
		if (AppContext.isRunningFromJar()) {
			String[] bundledDirs = getBundledDataDirectories();
			if (bundledDirs != null) {
                for (String bundledDir : bundledDirs) {
                    copyBundledDataIfMissing(bundledDir);
                }
			}
		}

		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		SwingUtilities.invokeLater(new Runnable() {
			
		      public void run() {

		    	displaySplashScreen(ctx);		    	  
		    	  
		  		/**
		  		 * Invoke the application window 
		  		 */
		    	  
		  		win = ctx.getBean(ApplicationWindow.class);
		  		win.setVisible(true);

		  		/**
		  		 * Configure window after startup 
		  		 */
		  		
		  		PostApplicationStartupConfigurer configurer = new PostApplicationStartupConfigurer(fileLocation);
		  		configurer.restoreLastPerspective();
		  		destroySplashScreen();
		  		
		      }
	    });		
		
	}

	/**
	 * Directory that contains shipped data next to the JAR
	 * (NSIS {@code $INSTDIR}); from the IDE it is the current directory.
	 */
	public static File detectInstallDir() {
		try {
			java.net.URL location = AbstractApplication.class.getProtectionDomain().getCodeSource().getLocation();
			File file = new File(location.toURI());
			if (file.isFile()) {
				AppContext.setRunningFromJar(true);
				return file.getParentFile();
			}
		}
		catch (Exception e) {
			log.warn("Could not determine install directory, using current directory.", e);
		}
		AppContext.setRunningFromJar(false);
		return new File(".");
	}

	/**
	 * Names of data folders shipped next to the JAR that should be copied
	 * into the user data directory on first run. Empty by default.
	 */
	protected String[] getBundledDataDirectories() {
		return new String[0];
	}

	/**
	 * Copies a bundled folder from the install dir into the user data dir
	 * when the destination does not exist yet.
	 */
	private void copyBundledDataIfMissing(String folderName) {
		File dest = new File(AppContext.getUserDataDir(), folderName);
		if (dest.exists()) {
			return;
		}
		File src = new File(AppContext.getWorkingDir(), folderName);
		if (!src.isDirectory()) {
			return;
		}
		try {
			org.apache.commons.io.FileUtils.copyDirectory(src, dest);
			log.info("Copied " + folderName + " to " + dest.getAbsolutePath());
		}
		catch (java.io.IOException e) {
			log.error("Could not copy bundled " + folderName + " to " + dest.getAbsolutePath(), e);
		}
	}

    /**
     * Displays the splashscreen during application startup. The splashscreen contains
     * a progressbar indicating the initializing progress of each bean. Thus the according
     * {@link BeanFactory} has to be passed. The wiring is done inside the {@link ApplicationContext}
     * 
     * @param beanFactory
     */
    private void displaySplashScreen(BeanFactory beanFactory) {
        try {

            this.splashScreen = ctx.getBean(SplashScreen.class);
            log.info("Displaying application splash screen...");

        }
        catch (Exception e) {
            log.warn("Unable to load and display startup splash screen.", e);
        }
    }
    
    private void destroySplashScreen() {
        if (ctx.getBean(SplashScreen.class) != null) {            
            new SplashScreenCloser(ctx.getBean(SplashScreen.class));
        }
    }

    /**
     * Closes the splash screen in the event dispatching (GUI) thread.
     * 
     * @author Keith Donald
     * @see SplashScreen
     */
    private static class SplashScreenCloser {

        /**
         * Closes the currently-displayed, non-null splash screen.
         * 
         * @param splashScreen
         */
        public SplashScreenCloser(final SplashScreen splashScreen) {

            /*
             * Removes the splash screen.
             * 
             * Invoke this <code> Runnable </code> using <code>
             * EventQueue.invokeLater </code> , in order to remove the splash
             * screen in a thread-safe manner.
             */
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    splashScreen.dispose();
                }
            });
        }
    }


}
