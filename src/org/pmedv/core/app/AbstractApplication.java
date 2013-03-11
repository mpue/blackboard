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
		 * Currently only unix and windows platforma are supported
		 */
		
		if (CheckEnv.getPlatform().equals("unsupported")) {
			System.err.println("Plattform is not supported");
			System.exit(-1);
		}
		
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

		/**
		 * Initialize spring application context.
		 */
		
		File workDir = new File(".");
		log.info("Working directory "+workDir.getAbsolutePath());
		AppContext.setWorkingDir(workDir);
		
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		/**
		 * Now check the default directory structure and create missing 
		 * directories if necessary
		 */
		
		String datadir = currentDir+"/."+AppContext.getName();
		
		File f = new File(datadir);
		
		if (!f.exists()) {
			log.info("Creating directory "+datadir);
			FileUtils.makeDirectory(datadir);
		}
		
		String tempDir = currentDir+"/."+AppContext.getName()+"/temp";
		
		f = new File(tempDir);
		if (!f.exists()) {
			log.info("Creating directory "+tempDir);
			FileUtils.makeDirectory(tempDir);
		}
		
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
