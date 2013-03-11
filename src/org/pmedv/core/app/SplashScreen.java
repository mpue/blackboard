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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicProgressBarUI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.Colors;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.core.util.WindowUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Displays a splash screen which shows a progressbar depending on the number of
 * beans to be initialized.
 * 
 * @author Matthias Pueski
 * 
 */

public class SplashScreen implements ApplicationContextAware, BeanPostProcessor, InitializingBean {

	// we have to substract the non singletons from the bean counter, in order
	// to get the progressbar right.

	private static final int NUM_OF_NON_SINGLETON_BEANS = 0; // including the splash screen bean itself

	private JWindow window;
	
	private Image image;

	private String imageResourcePath;

	private static final Log log = LogFactory.getLog(SplashScreen.class);
	private ApplicationContext context;
	private JProgressBar progressBar;
	private int progress = 0;
	private boolean showProgressLabel;

	
	
	public SplashScreen() {
		
		progressBar = new JProgressBar();
		progressBar.setForeground(Colors.COLOR_PROGRESSBAR);
		
		BasicProgressBarUI ui = new BasicProgressBarUI() {
		    protected Color getSelectionBackground() {
		        return Colors.COLOR_PROGRESSBAR; // string color over the background
		    }
		    protected Color getSelectionForeground() {
		        return Color.WHITE; // string color over the foreground
		    }
		};
		progressBar.setUI(ui);
	}

	/**
	 * Show the splash screen.
	 */
	public void splash() {
		
		window = new JWindow();
		// TODO : What was this for?
		// AWTUtilities.setWindowOpaque(window, false);
		
		if (image == null) {
			image = loadImage(imageResourcePath);
			if (image == null) {
				return;
			}
		}
		
		MediaTracker mediaTracker = new MediaTracker(window);
		mediaTracker.addImage(image, 0);
		
		try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException e) {
			log.error("Interrupted while waiting for splash image to load.");
		}

		int width = image.getWidth(null);
        int height = image.getHeight(null);
        
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = (Graphics2D)bimg.createGraphics();
        
        g2d.drawImage(image, 0, 0, null);        
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
		InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
		}
		catch (IOException e1) {
			properties.setProperty("version", "not set");
		}
		
		String version = properties.getProperty("version");
		
		File f = new File("build.number");
		
		properties = new Properties();
		
		try {
			properties.load(new FileReader(f));
		}
		catch (IOException e1) {
			properties.setProperty("build.number", "00");
		}
 
		String buildNumber = properties.getProperty("build.number");
		
        g2d.drawString("Version "+version+"."+buildNumber,400,305);
		
		JLabel panelImage = new JLabel(new ImageIcon(bimg));

		window.getContentPane().add(panelImage);
		window.getContentPane().add(progressBar, BorderLayout.SOUTH);
		window.pack();
		
		WindowUtils.center(window);

		window.setVisible(true);
	}

	public void setImageResourcePath(String path) {
		Assert.hasText(path, "The splash screen image resource path is required");
		this.imageResourcePath = path;
	}

	/**
	 * Dispose of the the splash screen. Once disposed, the same splash screen
	 * instance may not be shown again.
	 */
	public void dispose() {
		window.dispose();
		window = null;
	}

	private Image loadImage(String path) {

		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(path);			
			return ImageIO.read(is); 
			
		} 
		catch (IOException e) {
			return null;
		}
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	public Object postProcessBeforeInitialization(final Object bean, final String name) throws BeansException {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				progressBar.setValue(progress++);
				if (showProgressLabel) {
					if (bean instanceof PartDialog)
						progressBar.setString("Initializing part library.");
					else
						progressBar.setString("Loading module " + name);
				}
			}

		});

		return bean;
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
	 *      java.lang.String)
	 */
	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
		return bean;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		
		if (context.containsBean("lookAndFeelConfigurer")) {
			context.getBean("lookAndFeelConfigurer");
			progressBar = new JProgressBar();
		}

		Assert.state(StringUtils.hasText(imageResourcePath), "The splash screen image resource path is required");

		if (showProgressLabel) {
			progressBar.setStringPainted(true);
			progressBar.setString("Loading context");
		}

		progressBar.setMinimum(0);
		progressBar.setMaximum(context.getBeanDefinitionCount() - NUM_OF_NON_SINGLETON_BEANS);
		
		splash();
	}

	public boolean getShowProgressLabel() {
		return showProgressLabel;
	}

	public void setShowProgressLabel(boolean showProgressLabel) {
		this.showProgressLabel = showProgressLabel;
	}
}
